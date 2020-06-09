<template>
  <div class="app-container">
    <el-row type="flex" justify="right" style="padding-bottom:5px ">
      <el-col :span="8">
        <el-button-group>
          <el-button size="mini" type="primary" icon="el-icon-plus" round @click="handleAddClick">新增</el-button>
          <!-- <el-button v-show="this.delete" size="mini" type="danger" icon="el-icon-delete" round @click="handleDeleteClick">删除</el-button> -->
        </el-button-group>
      </el-col>
      <el-col :span="16">
        <el-row type="flex" justify="end" />
      </el-col>
    </el-row>
    <el-table ref="multipleTable" :data="objectList" tooltip-effect="dark" style="width: 100%" border fit highlight-current-row @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="42" />
      <el-table-column type="index" align="center" label="#" width="42" />
      <el-table-column prop="name" align="center" label="活动名称" width="114" />
      <!-- <el-table-column prop="title" align="center" label="活动标题" width="144" /> -->
      <el-table-column align="center" label="活动时间" width="264">
        <template slot-scope="scope">
          {{ dateTimeFormat(scope.row.startTime) }}至{{ dateTimeFormat(scope.row.endTime) }}
        </template>
      </el-table-column>
      <el-table-column prop="status" align="center" label="活动状态" width="104">
        <template slot-scope="scope">
          <span v-if="scope.row.status == 1">上线</span>
          <span v-if="scope.row.status == 2">下线</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="优惠模板" width="264">
        <template slot-scope="scope">
          <div v-for="item in scope.row.templateList" :key="item.id">
            {{ item.name }} <el-divider direction="vertical" />
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="url" align="center" label="活动链接" width="214" show-overflow-tooltip>
        <template slot-scope="scope">
          <el-link icon="el-icon-copy-document" @click="handleCopy(scope.row.url)">{{ scope.row.url }}</el-link>
        </template>
      </el-table-column>
      <el-table-column prop="rules" align="center" label="活动规则" />
      <el-table-column align="center" label="操作" width="84">
        <template slot-scope="scope">
          <el-button plain size="mini" @click="handleEditClick(scope.row)">修改</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-row type="flex" justify="end" style="padding:5px 0; ">
      <el-pagination background :current-page="currentPage" :page-sizes="[10, 50, 100, 200]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </el-row>
    <el-dialog title="优惠活动信息" :visible.sync="dialogFormVisible" :close-on-press-escape="false" :close-on-click-modal="false" top="15vh" width="42%">
      <el-form ref="dataForm" :model="object" :label-width="formLabelWidth" :disabled="ifView">
        <el-row>
          <el-col :span="24">
            <el-form-item label="活动名称">
              <el-input v-model="object.name" auto-complete="off" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="活动时间">
              <el-date-picker
                v-model="object.validDates"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="优惠券模板">
          <el-select v-model="templateIds" multiple placeholder="请选择优惠券模板">
            <el-option
              v-for="item in couponOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-row>
          <el-col :span="24">
            <el-form-item label="活动状态">
              <el-radio-group v-model="object.status">
                <el-radio :label="1">上线</el-radio>
                <el-radio :label="2">下线</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="活动规则">
              <el-input v-model="object.rules" type="textarea" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" :loading="loadingTags.add" @click="createData">确 定</el-button>
        <el-button v-else type="primary" :loading="loadingTags.edit" @click="modifyData">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { listActivities, addActivities, updateActivities } from '@/api/activities'
import { queryCouponTemplate } from '@/api/couponTemplate'
import { deepClone } from '@/utils/transferUtil'
import { formatNumber } from '@/utils/formatter'
import moment from 'moment'
export default {
  name: 'Activities',
  filters: {
    numberFilter(data) {
      return formatNumber(data)
    }
  },
  props: {
    add: {
      type: Boolean,
      default: false
    },
    edit: {
      type: Boolean,
      default: false
    },
    delete: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      objectList: [],
      multipleSelection: [],
      currentPage: 1,
      total: 0,
      pageSize: 10,
      dialogFormVisible: false,
      object: {
        termEndDate: '',
        useNotice: '',
        useAmount: ''
      },
      formLabelWidth: '120px',
      ifView: false,
      ids: [],
      dialogStatus: '',
      loadingTags: {
        add: false,
        edit: false
      },
      param: {
        pageNo: 1,
        pageSize: 10
      },
      couponOptions: [],
      templateIds: []
    }
  },
  watch: {
    multipleSelection: function() {
      const arr = []
      for (const i in this.multipleSelection) {
        arr.push(this.multipleSelection[i].id)
      }
      this.ids = arr.join()
    }
  },
  created() {
    this.getData()
    this.ininitCouponOptions()
  },
  methods: {
    handleCopy(data) {
      this.copy(data)
    },
    copy(data) {
      const url = data
      const oInput = document.createElement('input')
      oInput.value = url
      document.body.appendChild(oInput)
      oInput.select() // 选择对象;
      console.log(oInput.value)
      document.execCommand('Copy') // 执行浏览器复制命令
      this.$message({
        message: '复制成功',
        type: 'success'
      })
      oInput.remove()
    },
    ininitCouponOptions() {
      const param = { status: 1 }
      queryCouponTemplate(param).then(r => {
        this.couponOptions = r.result
      })
    },
    getData() {
      this.param.pageNo = this.currentPage
      listActivities(this.param).then(r => {
        this.objectList = r.result.list
        this.total = r.result.total
      })
    },
    handleAddClick() {
      this.loadingTags.add = false
      this.dialogFormVisible = true
      this.object = { status: 1 }
      this.ifView = false
      this.dialogStatus = 'create'
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.loadingTags.add = true
          if (this.object.validDates && this.object.validDates.length > 0) {
            this.object.startTime = this.object.validDates[0]
            this.object.endTime = this.object.validDates[1]
          }
          this.object.templateIds = this.templateIds.join()
          addActivities(this.object).then(() => {
            this.dialogFormVisible = false
            this.getData()
            this.$message({
              message: '创建成功',
              type: 'success'
            })
          })
        }
      })
    },
    handleEditClick(val) {
      this.loadingTags.edit = false
      this.dialogFormVisible = true
      this.object = deepClone(val)
      this.object.validDates = []
      this.object.validDates.push(this.object.startTime)
      this.object.validDates.push(this.object.endTime)
      this.templateIds = []
      if (this.object.templateList && this.object.templateList.length > 0) {
        for (var i = 0; i < this.object.templateList.length; i++) {
          this.templateIds.push(this.object.templateList[i].id)
        }
      }
      this.ifView = false
      this.dialogStatus = 'edit'
    },
    modifyData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.loadingTags.edit = true
          this.object.templateIds = this.templateIds.join()
          updateActivities(this.object).then(() => {
            this.dialogFormVisible = false
            this.getData()
            this.$message({
              message: '修改成功',
              type: 'success'
            })
          })
        }
      })
    },
    removeData() {
      // deleteConsultant(this.ids).then(() => {
      //   this.getData()
      //   this.$message({
      //     message: '删除成功',
      //     type: 'success'
      //   })
      // })
    },
    deleteRows() {
      if (this.ids.length > 0) {
        this.removeData()
      } else {
        this.$message({
          message: '请选择你需要删除的数据',
          type: 'warning'
        })
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.getData()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.getData()
    },
    handleDeleteClick() {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRows()
      })
    },
    dateTimeFormat: function(date) {
      return moment(date).format('YYYY-MM-DD HH:mm:ss')
    }
  }
}
</script>

<template>
  <div class="app-container">
    <el-row type="flex" justify="right" style="padding-bottom:5px ">
      <el-col :span="8">
        <el-button-group>
          <!-- <el-button  size="mini" type="primary" icon="el-icon-plus" round @click="handleAddClick">新增</el-button> -->
          <!-- <el-button v-show="this.delete" size="mini" type="danger" icon="el-icon-delete" round @click="handleDeleteClick">删除</el-button> -->
        </el-button-group>
      </el-col>
      <el-col :span="8">
        <el-row type="flex" justify="end">
          <el-radio v-model="param.type" label="" @change="userTypeChange('')">全部</el-radio>
          <el-radio v-model="param.type" label="0" @change="userTypeChange('0')">咨询师</el-radio>
          <el-radio v-model="param.type" label="1" @change="userTypeChange('1')">用户</el-radio>
          <el-button icon="el-icon-search" type="primary" @click="getData" />
        </el-row>
      </el-col>
    </el-row>
    <el-table ref="multipleTable" :data="objectList" tooltip-effect="dark" style="width: 100%" border fit highlight-current-row @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="42" />
      <el-table-column type="index" align="center" label="#" width="42" />
      <el-table-column prop="type" align="center" label="身份" width="72">
        <template slot-scope="scope">
          <span v-if="scope.row.type === 0">咨询师</span>
          <span v-if="scope.row.type === 1">用户</span>
        </template>
      </el-table-column>
      <el-table-column prop="realName" align="center" label="提意见人" width="144" />
      <el-table-column prop="content" align="center" label="内容" width="576" />
      <el-table-column align="center" label="操作" width="84">
        <template slot-scope="scope">
          <el-button plain size="mini" @click="handleRemoveClick(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-row type="flex" justify="end" style="padding:5px 0; ">
      <el-pagination background :current-page="currentPage" :page-sizes="[10, 50, 100, 200]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </el-row>
    <el-dialog title="按钮信息" :visible.sync="dialogFormVisible" :close-on-press-escape="false" :close-on-click-modal="false" top="15vh" width="48%">
      <el-form ref="dataForm" :model="object" :label-width="formLabelWidth" :disabled="ifView">
        <el-row>
          <el-col :span="8">
            <el-form-item label="名称" prop="buttonName">
              <el-input v-model="object.useNotice" auto-complete="off" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="使用金额" prop="sortNo">
              <el-input-number v-model="object.useAmount" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="过期时间" prop="buttonName">
              <div class="block">
                <el-date-picker
                  v-model="object.termEndDate"
                  type="date"
                  placeholder="选择日期" />
              </div>
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
import { proposalList, proposalDelete } from '@/api/proposal'
import { deepClone } from '@/utils/transferUtil'
import { formatNumber } from '@/utils/formatter'
import moment from 'moment'
export default {
  name: 'ButtonList',
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
      systemOptions: [],
      currentPage: 1,
      total: 0,
      pageSize: 10,
      dialogFormVisible: false,
      object: {
        termEndDate: '',
        useNotice: '',
        useAmount: ''
      },
      formLabelWidth: '80px',
      ifView: false,
      ids: [],
      dialogStatus: '',
      loadingTags: {
        add: false,
        edit: false
      },
      param: {
        pageNo: 1,
        pageSize: 10,
        type:''
      }
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
  },
  methods: {
    getData() {
      this.param.pageNo = this.currentPage
      proposalList(this.param).then(r => {
        this.objectList = r.result.list
        this.total = r.result.total
        this.currentPage = r.result.pageNum
      })
    },
    handleAddClick() {
      this.loadingTags.add = false
      this.dialogFormVisible = true
      this.object = {}
      this.ifView = false
      this.dialogStatus = 'create'
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.loadingTags.add = true
          this.useAmount = this.useAmount * 100
          // addCoupon(this.object).then(() => {
          //   this.dialogFormVisible = false
          //   this.getData()
          //   this.$message({
          //     message: '创建成功',
          //     type: 'success'
          //   })
          // })
        }
      })
    },
    handleEditClick(val) {
      this.loadingTags.edit = false
      this.dialogFormVisible = true
      this.object = deepClone(val)
      this.ifView = false
      this.dialogStatus = 'edit'
    },
    modifyData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.loadingTags.edit = true
          // updateConsultant(this.object).then(() => {
          //   this.dialogFormVisible = false
          //   this.getData()
          //   this.$message({
          //     message: '修改成功',
          //     type: 'success'
          //   })
          // })
        }
      })
    },
    removeData(id) {
      proposalDelete(id).then(() => {
        this.getData()
        this.$message({
          message: '删除成功',
          type: 'success'
        })
      })
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
    handleRemoveClick(val) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.object = val
        delete this.object.children
        delete this.object.parent
        this.removeData(this.object.id)
      })
    },
    dateTimeFormat: function(row, column) {
      var date = row[column.property]
      if (!date) {
        return ''
      }
      return moment(date).format('YYYY-MM-DD')
    },
    userTypeChange: function(type) {
      this.param.type = type
    }
  }
}
</script>

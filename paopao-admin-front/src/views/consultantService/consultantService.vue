<template>
  <div class="app-container">
    <el-row type="flex" justify="right" style="padding-bottom:5px ">
      <el-col :span="8">
        <el-button-group>
          <!-- <el-button  size="mini" type="primary" icon="el-icon-plus" round @click="handleAddClick">新增</el-button> -->
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
      <el-table-column prop="user.userName" align="center" label="申请账户" width="144" />
      <el-table-column prop="user.realName" align="center" label="申请人" width="144" />
      <el-table-column prop="user.insertTime" align="center" label="申请时间" width="144" :formatter="dateTimeFormat" />
      <el-table-column align="center" label="操作" width="114">
        <template slot-scope="scope">
          <el-button plain size="small" icon="el-icon-edit" @click="handleVerifyClick(scope.row.user.id)">
            审核
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-row type="flex" justify="end" style="padding:5px 0; ">
      <el-pagination background :current-page="currentPage" :page-sizes="[10, 50, 100, 200]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </el-row>
    <el-dialog title="服务信息" :visible.sync="dialogFormVisible" :close-on-press-escape="false" :close-on-click-modal="false" top="15vh" width="48%">
      <el-table ref="multipleTable" :data="itemList" tooltip-effect="dark" style="width: 100%" border fit highlight-current-row>
        <el-table-column type="index" align="center" label="#" width="42" />
        <el-table-column prop="goodsName" align="center" label="服务名称" />
        <el-table-column prop="sellPointText" align="center" label="服务方式" />
        <el-table-column prop="sellPrice" align="center" label="服务价格(单位元)">
          <template slot-scope="scope">
            {{ scope.row.sellPrice/100 }}
          </template>
        </el-table-column>
        <el-table-column prop="operateType" align="center" label="修改方式" width="80">
          <template slot-scope="scope">
            <span v-if="scope.row.operateType === 1">新增</span>
            <span v-if="scope.row.operateType===2">删除</span>
            <span v-if="scope.row.operateType===3">修改</span>
          </template>
        </el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button size="mini" type="primary" @click="removeData">拒绝</el-button>
        <el-button size="mini" type="primary" @click="createData">通过</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { consultantServiceList } from '@/api/consultantService'
// import { deepClone } from '@/utils/transferUtil'
import { formatNumber } from '@/utils/formatter'
import moment from 'moment'
import { consultantServiceInfo, addConsultantService, deleteConsultantService } from '@/api/consultantService'
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
      itemList: [],
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
        pageSize: 10
      },
      shopId: undefined
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
    handleVerifyClick(id) {
      this.dialogFormVisible = true
      this.shopId = id
      this.detailData(id)
    },
    detailData(id) {
      const param = { shopId: id }
      consultantServiceInfo(param).then(r => {
        this.itemList = r.result
      })
    },
    getData() {
      this.param.pageNo = this.currentPage
      consultantServiceList(this.param).then(r => {
        this.objectList = r.result.list
        this.total = r.result.total
        this.currentPage = r.result.pageNum
      })
    },
    createData() {
      const param = { shopId: this.shopId }
      addConsultantService(param).then(() => {
        this.dialogFormVisible = false
        this.getData()
        this.$message({
          message: '操作成功',
          type: 'success'
        })
      })
    },
    removeData() {
      const param = { shopId: this.shopId }
      deleteConsultantService(param).then(() => {
        this.dialogFormVisible = false
        this.getData()
        this.$message({
          message: '操作成功',
          type: 'success'
        })
      })
    },
    modifyData() {
      this.$refs['dataForm'].validate((valid) => {
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
    dateTimeFormat: function(row, column) {
      var date = row.user.insertTime
      if (!date) {
        return ''
      }
      return moment(date).format('YYYY-MM-DD HH:mm:ss')
    }
  }
}
</script>

<template>
  <div class="app-container">
    <el-row type="flex" justify="right" style="padding-bottom:5px ">
      <el-col :span="8" />
      <el-col :span="16">
        <el-row type="flex" justify="end" />
      </el-col>
    </el-row>
    <el-table ref="multipleTable" :data="objectList" tooltip-effect="dark" style="width: 100%" border fit highlight-current-row @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="42" />
      <el-table-column type="index" align="center" width="42" />
      <el-table-column align="center" label="转出账号" width="184">
        <template v-if="scope.row.fromUser" slot-scope="scope">
          <span>{{ scope.row.fromUser.userName }}({{ scope.row.fromUser.realName }})</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="转入账号" width="184">
        <template v-if="scope.row.toUser" slot-scope="scope">
          <span>{{ scope.row.toUser.userName }}({{ scope.row.toUser.realName }})</span>
        </template>
      </el-table-column>
      <el-table-column prop="pointType" align="center" label="操作类型" width="130">
        <template slot-scope="scope">
          <span v-if="scope.row.pointType === 1">充值</span>
          <span v-if="scope.row.pointType === 2">提现</span>
          <span v-if="scope.row.pointType === 3">咨询订单分佣结算</span>
          <span v-if="scope.row.pointType === 4">咨询订单退款</span>
          <span v-if="scope.row.pointType === 5">咨询订单余额支付</span>
          <span v-if="scope.row.pointType === 6">咨询订单支付宝支付</span>
          <span v-if="scope.row.pointType === 7">咨询订单微信</span>
          <span v-if="scope.row.pointType === 8">驳回</span>
          <span v-if="scope.row.pointType === 9">督导订单退款</span>
          <span v-if="scope.row.pointType === 10">督导订单余额支付</span>
          <span v-if="scope.row.pointType === 11">咨询订单分佣结算</span>
        </template>
      </el-table-column>
      <el-table-column prop="balanceBefore" align="center" label="操作前金额" width="120" :formatter="formatMoney" />
      <el-table-column prop="point" align="center" label="操作金额" width="120" :formatter="formatMoney" />
      <el-table-column prop="balance" align="center" label="操作后金额" width="120" :formatter="formatMoney" />
      <el-table-column prop="createDt" align="center" label="操作时间" :formatter="dateFormat" />
    </el-table>
    <el-row type="flex" justify="end" style="padding:5px 0; ">
      <el-pagination background :current-page="currentPage" :page-sizes="[10, 50, 100, 200]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </el-row>
  </div>
</template>
<script>
import { listPointHistory } from '@/api/account'
import moment from 'moment'
export default {
  name: 'ButtonList',
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
        buttonNo: '',
        buttonName: ''
      },
      ids: [],
      dialogStatus: '',
      loadingTags: {
        add: false,
        edit: false
      },
      param: {}
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
      this.param.pageSize = this.pageSize
      listPointHistory(this.param).then(r => {
        this.objectList = r.result.list
        this.total = r.result.total
      })
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
    formatMoney(row, column, cellValue) {
      return cellValue / 100 + '元'
    },
    dateFormat: function(row, column) {
      var date = row[column.property]
      if (!date) {
        return ''
      }
      return moment(date).format('YYYY-MM-DD HH:mm:ss')
    }
  }
}
</script>

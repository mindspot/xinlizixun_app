<template>
  <div class="app-container">
    <el-row type="flex" justify="right" style="padding-bottom:5px ">
      <el-col :span="8">
        <el-button-group>
          <el-button size="mini" type="danger" icon="el-icon-delete" round @click="handleCollectClick">领取</el-button>
        </el-button-group>
      </el-col>
      <el-col :span="16">
        <el-row type="flex" justify="end" />
      </el-col>
    </el-row>
    <el-table ref="multipleTable" :data="objectList" tooltip-effect="dark" style="width: 100%" border fit highlight-current-row @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="42" />
      <el-table-column prop="name" align="center" label="优惠劵名称" width="144" />
      <el-table-column prop="type" align="center" label="优惠劵类型" width="114">
        <template slot-scope="scope">
          <span v-if="scope.row.type == 1">满减券</span>
          <span v-if="scope.row.type == 2">立减券</span>
          <span v-if="scope.row.type == 3">折扣券</span>
          <span v-if="scope.row.type == 4">兑换码</span>
        </template>
      </el-table-column>
      <el-table-column prop="type" align="center" label="兑换码" width="144">
        <template v-if="scope.row.type == 4" slot-scope="scope">
          <el-link icon="el-icon-copy-document" @click="handleCopy(scope.row.redeemCode)">{{ scope.row.redeemCode }}</el-link>
        </template>
      </el-table-column>
      <el-table-column prop="amount" align="center" label="优惠金额" width="104">
        <template slot-scope="scope">
          {{ scope.row.amount }}
        </template>
      </el-table-column>
      <el-table-column prop="amountLimit" align="center" label="最低使用金额" width="104">
        <template slot-scope="scope">
          {{ scope.row.amountLimit }}
        </template>
      </el-table-column>
      <el-table-column prop="termEndDate" align="center" label="有效期" width="194">
        <template slot-scope="scope">
          {{ dateTimeFormat(scope.row.termStartDate) }}至{{ dateTimeFormat(scope.row.termEndDate) }}
        </template>
      </el-table-column>
      <el-table-column prop="name" align="center" label="领取人" width="104">
        <template v-if="scope.row.user" slot-scope="scope">
          {{ scope.row.user.realName }}
        </template>
      </el-table-column>
      <el-table-column prop="status" align="center" label="劵状态" width="104">
        <template slot-scope="scope">
          <span v-if="scope.row.status == 0">待领取</span>
          <span v-if="scope.row.status == 1">待使用</span>
          <span v-if="scope.row.status == 2">已使用</span>
          <span v-if="scope.row.status == 3">已过期</span>
          <span v-if="scope.row.status == 4">已取消</span>
        </template>
      </el-table-column>
      <el-table-column prop="useNotice" align="center" label="使用须知" />
      <el-table-column align="center" label="操作" width="84">
        <template slot-scope="scope">
          <el-button plain size="mini" @click="handleEditClick(scope.row)">修改</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-row type="flex" justify="end" style="padding:5px 0; ">
      <el-pagination background :current-page="currentPage" :page-sizes="[10, 50, 100, 200]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </el-row>
  </div>
</template>
<script>
import { couponList, redeemCoupon } from '@/api/coupon'
import { collectCoupon } from '@/api/couponTemplate'
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
        pageSize: 10
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
    handleCollectClick() {
      collectCoupon(1).then(r => {})
      redeemCoupon(2, '269351790961').then(r => {})
    },
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
    getData() {
      this.param.pageNo = this.currentPage
      couponList(this.param).then(r => {
        this.objectList = r.result.list
        this.total = r.result.total
        this.currentPage = r.result.pageNum
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
    dateTimeFormat: function(date) {
      return moment(date).format('YYYY-MM-DD')
    }
  }
}
</script>

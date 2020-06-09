<template>
  <div class="app-container">
    <el-row type="flex" justify="right" style="padding-bottom:5px ">
      <el-col :span="8">
        <el-button-group>
          <el-button v-show="add" size="mini" type="primary" icon="el-icon-plus" round @click="handleAddClick">新增</el-button>
          <el-button v-show="this.delete" size="mini" type="danger" icon="el-icon-delete" round @click="handleDeleteClick">删除</el-button>
        </el-button-group>
      </el-col>
      <el-col :span="16">
        <el-row type="flex" justify="end">
          <el-radio v-model="param.orderStatus" label="">全部</el-radio>
          <el-radio v-model="param.orderStatus" label="0">未付款</el-radio>
          <el-radio v-model="param.orderStatus" label="1">已付款</el-radio>
          <el-radio v-model="param.orderStatus" label="2">进行中</el-radio>
          <el-radio v-model="param.orderStatus" label="-1">买家取消</el-radio>
          <el-radio v-model="param.orderStatus" label="-2">卖家取消</el-radio>
          <el-radio v-model="param.orderStatus" label="10">完结</el-radio>
          <el-date-picker v-model="param.orderTime" type="date" placeholder="下单时间查询" />
          <el-input v-model="param.userName" style="width:160px;margin-left:10px" placeholder="根据购买人账号查询" clearable />
          <el-input v-model="param.consultantUserName" style="width:160px;margin-left:10px" placeholder="根据咨询师账号查询" clearable />
          <el-button icon="el-icon-search" type="primary" @click="getData" />
        </el-row>
      </el-col>
    </el-row>
    <el-table ref="multipleTable" :data="objectList" tooltip-effect="dark" style="width: 100%" border fit highlight-current-row @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="42" />
      <el-table-column type="index" align="center" label="#" width="42" />
      <el-table-column prop="orderNo" align="center" label="订单号" width="240" />

      <el-table-column prop="payStatus" align="center" label="状态" width="72">
        <template slot-scope="scope">
          <span v-if="scope.row.payStatus === 0">未支付</span>
          <span v-if="scope.row.payStatus === 1 &&  scope.row.orderStatus === 1">待接单</span>
          <span v-if="scope.row.payStatus === 1 &&  scope.row.orderStatus === 2">已确认</span>
          <span v-if="scope.row.payStatus === 1 &&  scope.row.orderStatus === 10">已完成</span>
          <span v-if="scope.row.payStatus === 1 &&  scope.row.orderStatus === -2">已取消</span>

        </template>
      </el-table-column>
      <el-table-column prop="payType" align="center" label="支付方式" width="114">
        <template slot-scope="scope">
          <span v-if="scope.row.payType === 1">微信</span>
          <span v-if="scope.row.payType === 2">支付宝</span>
          <span v-if="scope.row.payType === 3">余额</span>
        </template>
      </el-table-column>
      <el-table-column prop="orderTime" align="center" label="下单时间" width="164" :formatter="dateTimeFormat" />
      <el-table-column prop="ext" align="center" label="预约时间" width="164" >
        <template slot-scope="scope">
          {{ JSON.parse(scope.row.ext).consultantWorkDate }}   {{JSON.parse(scope.row.ext).consultantWorkTime}}
        </template>
      </el-table-column>
      <el-table-column prop="goodsAmount" align="center" label="商品金额(单位元)" width="72">
        <template slot-scope="scope">
          {{ scope.row.goodsAmount/100 }}
        </template>
      </el-table-column>
      <el-table-column prop="discountAmount" align="center" label="优惠金额(单位元)" width="72">
        <template slot-scope="scope">
          {{ scope.row.discountAmount/100 }}
        </template>
      </el-table-column>
      <el-table-column prop="orderAmount" align="center" label="支付金额(单位元)" width="72">
        <template slot-scope="scope">
          {{ scope.row.orderAmount/100 }}
        </template>
      </el-table-column>
      <el-table-column prop="consultantRealName" align="center" label="咨询师" width="72"/>
      <el-table-column prop="buyerRealName" align="center" label="购买人" width="72" />
      <el-table-column prop="goodsClassName" label="服务名称" width="72" />
      <el-table-column prop="goodsClass" label="类别" width="72">
        <template slot-scope="scope">
          <span v-if="scope.row.orderType === 6">套餐</span>
          <span v-if="scope.row.orderType === 1">单次服务</span>
          <span v-if="scope.row.orderType === 9">套餐卡支付</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" width="168">
        <template slot-scope="scope">
          <el-button v-show="scope.row.orderStatus === 10" plain size="small" @click="changeStatus(scope.row)">退款</el-button>
          <router-link :to="'/order/setMealOrderInfo/'+scope.row.id+'/'+scope.row.orderNo">
            <el-button plain size="small" icon="el-icon-edit">
              详情
            </el-button>
          </router-link>
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
            <el-form-item label="按钮编号" prop="buttonNo">
              <el-input v-model="object.buttonNo" auto-complete="off" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按钮名称" prop="buttonName">
              <el-input v-model="object.buttonName" auto-complete="off" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="按钮序号" prop="sortNo">
              <el-input-number v-model="object.sortNo" :min="1" auto-complete="off" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8">
            <el-form-item label="状态">
              <el-switch v-model="object.status" active-text="有效" inactive-text="无效" :active-value="1" :inactive-value="2" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-form-item label="备注">
              <el-input v-model="object.remark" type="textarea" />
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
import { orderList, memberRefund } from '@/api/order'
import { deepClone } from '@/utils/transferUtil'
import { formatNumber } from '@/utils/formatter'
import moment from 'moment'
export default {
  name: 'ButtonList', numberFilter(data) {
    return formatNumber(data)
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
        buttonNo: '',
        buttonName: ''
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
        userName: '',
        consultantUserName: '',
        goodsClass: 6,
        orderTime: '',
        orderStatus: ''
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
      if (this.param.orderTime !== '') {
        this.param.orderTime = moment(this.param.orderTime).format('YYYY-MM-DD HH:mm:ss')
      }
      this.param.pageNo = this.currentPage
      this.param.goodsClass = 6
      orderList(this.param).then(r => {
        this.objectList = r.result.list
        this.total = r.result.total
      })
    },
    changeStatus(item) {
      this.$confirm('该订单是否需要要退款?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        memberRefund(item.orderNo).then(r => {
          if (r.success) {
            this.$message({
              message: '退款成功',
              type: 'success'
            })
            this.getData()
          } else {
            this.$message({
              message: r.message,
              type: 'error'
            })
          }
        })
      })
    },
    handleAddClick() {
      this.loadingTags.add = false
      this.dialogFormVisible = true
      this.object = { status: 1, sortNo: 10 }
      this.ifView = false
      this.dialogStatus = 'create'
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.loadingTags.add = true
          // addConsultant(this.object).then(() => {
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
          updateConsultant(this.object).then(() => {
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
    dateTimeFormat: function(row, column) {
      var date = row[column.property]
      if (!date) {
        return ''
      }
      return moment(date).format('YYYY-MM-DD HH:mm:ss')
    }
  }
}
</script>

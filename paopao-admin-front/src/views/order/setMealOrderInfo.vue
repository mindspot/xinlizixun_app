<template>
  <div class="app-container">
    <el-collapse v-model="activeName" accordion>
      <el-collapse-item title="基本信息" name="1">
        <el-row :gutter="20">
          <el-col :span="10">
            <el-form ref="form" :model="consultant" label-width="100px" style="width:100%">
              <el-form-item label="订单号" required>
                <div class="value">{{ order.orderNo }}</div>
              </el-form-item>
              <el-form-item label="订单类型" required>
                <span v-if="order.orderType === 1">咨询订单</span>
                <span v-if="order.orderType === 6">套餐订单</span>
                <span v-if="order.orderType === 9">套餐支付订单</span>
              </el-form-item>
              <el-form-item label="下单人" required>
                <div class="value">{{ order.operName}}</div>
              </el-form-item>
              <el-form-item label="下单时间" required>
                <div class="value">{{ order.orderTime }}</div>
              </el-form-item>
              <el-form-item label="支付状态">
                <span v-if="order.payStatus === 0">未支付</span>
                <span v-if="order.payStatus === 1">已支付</span>
              </el-form-item>
              <el-form-item label="支付时间">
                <div class="value">{{ order.payTime }}</div>
              </el-form-item>
              <el-form-item label="订单状态">
                <span v-if="order.payStatus === 1 && order.orderStatus === 1">已支付</span>
                <span v-if="order.payStatus === 1 && order.orderStatus === 2">已确认</span>
                <span v-if="order.payStatus === 1 && order.orderStatus === 10">已完成</span>
                <span v-if="order.payStatus === 1 && order.orderStatus === -2">卖家已取消</span>
                <span v-if="order.payStatus === 1 && order.orderStatus === -1">买家已取消</span>
              </el-form-item>
              <el-form-item label="商品金额">
                <div class="value">{{ order.goodsAmount /100}}</div>
              </el-form-item>
              <el-form-item label="优惠金额">
                <div class="value">{{ order.discountAmount /100}}</div>
              </el-form-item>
              <el-form-item label="订单支付金额">
                <div class="value">{{ order.orderAmount/100 }}</div>
              </el-form-item>
              <el-form-item label="第三方交易号">
                <div class="value">{{order.outTradeNo }}</div>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
      </el-collapse-item>
    </el-collapse>
    <el-collapse v-model="activeName" accordion>
      <el-collapse-item title="套餐信息" name="1">
        <el-row :gutter="20">
          <el-col :span="10">
            <el-form ref="form" :model="setMelOrder" label-width="100px" style="width:100%">
              <el-form-item label="购买时间" required>
                <div class="value">{{ setMelOrder.buyDate }}</div>
              </el-form-item>
              <el-form-item label="过期时间" required>
                <div class="value">{{ setMelOrder.termEndDate }}</div>
              </el-form-item>
              <el-form-item label="次数" required>
                <div class="value">{{ setMelOrder.consultationNumber }}</div>
              </el-form-item>
            </el-form></el-col>
        </el-row>
      </el-collapse-item>
    </el-collapse>

    <el-collapse v-model="activeName" accordion>
      <el-collapse-item title="咨询问题信息" name="1">
        <el-row :gutter="20">
          <el-col :span="10">
            <el-form ref="form" :model="setMelOrder" label-width="100px" style="width:100%">
              <el-form-item label="咨询问题" required>
                <div class="value">{{ memberCase.detailedDescription }}</div>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="10">
            <el-form ref="form" :model="setMelOrder" label-width="100px" style="width:100%">
              <el-form-item label="咨询问题" required>
                <span v-if="memberCase.isConsultant === 0">否</span>
               <span v-if="memberCase.isConsultant === 1">是</span>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
         <el-row :gutter="20">
          <el-col :span="10">
            <el-form ref="form" :model="setMelOrder" label-width="100px" style="width:100%">
              <el-form-item label="咨询类型" required>
                <span v-for="item in commons" :key="item.id">{{ item.val }},</span>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>
<script>
import { setMelaOrderByOrderNo, setMelaOrderByOrderNoDetails, memberCaseByOrderNo, orderCaseClass } from '@/api/order'
import { getToken } from '@/utils/auth' // 验权
import { baseUrl } from '@/data/config'
import moment from 'moment'
export default {
  data() {
    return {
      baseUrl: baseUrl,
      cards: [],
      educationExperienceList: [],
      certificationList: [],
      trainingExperienceList: [],
      supervisedExperienceList: [],
      publishBookList: [],
      publishPaperList: [],
      dialogFormVisible: false,
      targetPeopleList: [],
      expertiseAreaList: [],
      platformWorkingTimeVOList: [],
      workTimeList: [],
      consultant: {},
      user: {},
      activeName: '1',
      firstConsultantRate: {},
      nextConsultantRate: {},
      consultantList: [],
      order: {},
      setMelOrder: {},
      memberCase: {},
      commons: {}
    }
  },
  computed: {
    myHeaders: function() {
      return {
        'X-Token': getToken()
      }
    },
    avatar: function() {
      return baseUrl + this.user.headImg
    }
  },
  mounted() {
    const orderNo = this.$route.params && this.$route.params.orderNo
    this.companyReviewResultGet(orderNo)
    this.companySetMealResultGet(orderNo)
    this.memberCaseGet(orderNo)
    this.companyOrderCaseClass(orderNo)
  },
  methods: {
    companyReviewResultGet(orderNo) {
      setMelaOrderByOrderNo(orderNo).then(r => {
        this.order = r.result
      })
    },
    companySetMealResultGet(orderNo) {
      setMelaOrderByOrderNoDetails(orderNo).then(r => {
        this.setMelOrder = r.result
      })
    },
    memberCaseGet(orderNo) {
      memberCaseByOrderNo(orderNo).then(r => {
        this.memberCase = r.result
      })
    },
    companyOrderCaseClass(orderNo) {
      orderCaseClass(orderNo).then(r => {
        this.commons = r.result
      })
    },
    dateFormat: function(date) {
      if (!date) {
        return ''
      }
      return moment(date).format('YYYY-MM-DD HH:ss')
    }
  }
}
</script>
<style scoped>
.value {
  border: 1px solid #999999;
  text-align: left;
  padding-left: 10px;
  min-height: 32px;
}
.c-rblue {
  color: #006dfe;
}
.value-time {
  border: 1px solid #999999;
  text-align: center;
  padding-left: 10px;
  min-height: 32px;
}
.avatar {
  width: 100px;
  height: 28px;
  margin-left: 10px;
  cursor: pointer;
}
.avatar-change {
  color: #999999;
  line-height: 20px;
  margin-top: -10px;
}
.file-change {
  color: #999999;
  line-height: 20px;
  float: right;
  margin-left: 5px;
  line-height: 32px;
}
</style>


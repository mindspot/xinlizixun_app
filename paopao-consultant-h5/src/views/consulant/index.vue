<template>
  <div id="consultant">
    <div class="header">
      <div class="step_1">
        <div class="box">
          <div v-if="step >0" class="step_icons">1</div>
          <img v-if="step >0" src="../../assets/images/t4.png" class="step_bar">
        </div>
        <h3>同意协议</h3>
      </div>
      <div class="step_1">
        <div class="box">
          <div v-if="step >1" class="step_icons">2</div>
          <div v-else class="step_icon">2</div>
          <img v-if="step >1" src="../../assets/images/t4.png" class="step_bar">
          <img v-else src="../../assets/images/t3.png" class="step_bar">
        </div>
        <h3>专业资质</h3>
      </div>
      <div class="step_1">
        <div class="box">
          <div v-if="step >2" class="step_icons">3</div>
          <div v-else class="step_icon">3</div>
          <img v-if="step >2" src="../../assets/images/t4.png" class="step_bar">
          <img v-else src="../../assets/images/t3.png" class="step_bar">
        </div>
        <h3>基础信息</h3>
      </div>
      <div class="step_1">
        <div class="box">
          <div v-if="step >3" class="step_icons">4</div>
          <div v-else class="step_icon">4</div>
          <img v-if="step >3" src="../../assets/images/t4.png" class="step_bar">
          <img v-else src="../../assets/images/t3.png" class="step_bar">
        </div>
        <h3>服务设置</h3>
      </div>
      <div class="step_1">
        <div v-if="step >4" class="step_icons">5</div>
        <div v-else class="step_icon">5</div>
        <h3>提交审核</h3>
      </div>
    </div>
    <div v-if="step === 1">
      <agreement @stepFirstDone="stepFirstDone" />
    </div>
    <div v-if="step === 2">
      <extra-info @stepSecondDone="stepSecondDone" />
    </div>
    <div v-if="step === 3">
      <base-info :default-data="consultant" @stepThirdDone="stepThirdDone" />
    </div>
    <div v-if="step === 4">
      <service-item @stepFourDone="stepFourDone" />
    </div>
    <div v-if="step === 5">
      <verify-result @stepFiveDone="stepFiveDone" />
    </div>
  </div>
</template>
<script>
import { getConsultantInfo } from '@/api/consultant'
import BaseInfo from '../../components/BaseInfo'
import ExtraInfo from '../../components/ExtraInfo'
import ServiceItem from '../../components/ServiceItem'
import VerifyResult from '../../components/VerifyResult'
import Agreement from '../../components/Agreement.vue'
export default {
  // 注册组件
  components: {
    'agreement': Agreement,
    'base-info': BaseInfo,
    'extra-info': ExtraInfo,
    'service-item': ServiceItem,
    'verify-result': VerifyResult
  },
  data() {
    return {
      step: Number(localStorage.getItem('STEP')),
      consultant: {}
    }
  },
  created() {
    getConsultantInfo().then(r => {
      this.consultant = r.result
    })
  },
  methods: {
    stepFirstDone() {
      this.step = 2
    },
    stepSecondDone() {
      this.step = 3
    },
    stepThirdDone() {
      this.step = 4
    },
    stepFourDone() {
      this.step = 5
    },
    stepFiveDone() {
      this.step = 1
    }
  }
}
</script>
<style scoped>
ul > li:first-child .step_icon {
  background-color: rgba(157, 220, 177);
  color: #ffffff;
}

.step_bar {
  width: 0.78rem;
  height: 0.56rem;
}
</style>

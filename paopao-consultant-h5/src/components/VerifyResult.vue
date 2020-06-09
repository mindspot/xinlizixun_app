<template>
  <div id="verify-reuslt" class="dialogFive">
    <div class="imgSection">
      <img src="../assets/images/wan.png" class="imgName">
    </div>
    <div class="center">
      <p>温馨提示：</p>
      <p>  审核通过后，系统人工客服届时将电话通知到您，并简单介绍工作平台的各项功能。</p>
      <p>  您可先行在泡泡心理官网，手机应用市场和app store自行下载“泡泡工作台”或点击下方链接进行下载安装。</p>
      <p>或点击下方链接进行下载安装</p>
      <br>
      <br>
      <br>
      <div>
        链接:
        <p>
          <a href="http://download.psyooo.com/6sfj" style="color: #FF0000;">http://download.psyooo.com/6sfj</a></p>
      </div>
      <div v-if="consultantStatus === 1" class="review" style="margin:0,15px,0,0">审核结果：审核中</div>
      <div v-if="consultantStatus === 2" class="review" style="margin:0,15px,0,0">审核结果：审核通过</div>
      <div v-if="consultantStatus === 3" class="review" style="margin:0,15px,0,0">审核结果：已驳回</div>
      <p v-if="consultantStatus === 3" style="color: red;">{{ consultantRemark }}</p>
    </div>
    <div class="footer-container">
      <van-button v-if="consultantStatus === 3" class="footer" @click="rewrite">重新提交</van-button>
      <van-button v-else class="footer" @click="close">关闭</van-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'DialogFive',
  props: {},
  data() {
    return {
      consultantStatus: Number(localStorage.getItem('CONSULTANT_STATUS')),
      consultantRemark: localStorage.getItem('CONSULTANT_REMARK')
    }
  },
  methods: {
    close() {
      this.$router.push({ name: 'login' })
    },
    rewrite() {
      this.$emit('stepFiveDone')
    }
  }
}
</script>

<style scoped>
@import "../assets/public.css";
.main {
	margin: .2rem auto 0;
	width: 6.86rem;
	border: 2px solid #9ddcaf;
	border-radius: .12rem;
	padding: 0 .2rem;
	box-sizing: border-box;
	padding-bottom: .66rem;
}
.imgSection {
	text-align: center;
	/* 	padding-top: .6rem;
	padding-bottom: .4rem; */
}

.imgName {
	width: 3rem;
	height: .6rem;
	margin-bottom: .4rem;
	margin-top: .6rem;
}
.review {
  margin-top: .6rem;
  font-size: .32rem;
  line-height: .32rem;
}
.center {
	border: .02rem solid rgba(157, 220, 177);
	border-radius: .2rem;
	width: 6.2rem;
	height: 6.8rem;
	padding: .38rem .38rem;
	margin: .4rem auto;
	font-size: .24rem;
	color: #000;
}
.footer-container {
  width: 6.2rem;
  margin: .4rem auto;
  font-size: .24rem;
}
.footer {
    text-align: center;
    color: #ffffff;
    width: 6.2rem;
    height: 0.8rem;
    line-height: 0.8rem;
    font-size: 0.26rem;
    background-color: #9ddcb0;
    border-radius: 0.14rem;
}
</style>

<template>
  <div id="extra-info" class="dialogThree">
    <div class="imgSection">
      <img src="../assets/images/bit.png" class="imgName">
    </div>
    <education-experience @getEducationExperienceData="getEducationExperienceData" />
    <certification @getCertificationData="getCertificationData" />
    <div class="imgSection">
      <img src="../assets/images/xuan.png" class="imgName">
    </div>
    <training-experience @getTrainingExperienceData="getTrainingExperienceData" />
    <supervised-experience @getSupervisedExperienceData="getSupervisedExperienceData" />
    <publish-material @getPublishBookData="getPublishBookData" @getPublishPaperData="getPublishPaperData" />
    <div id="footer-text">
      <p>平台具有严格的审核标准</p>
      <p>提交虚假资料将承担相应法律责任</p>
    </div>
    <van-button :loading="isLoading" loading-type="spinner" class="footer" @click="next">下一步</van-button>
  </div>
</template>
<script>
import SupervisedExperience from './components/SupervisedExperience'
import TrainingExperience from './components/TrainingExperience'
import EducationExperience from './components/EducationExperience'
import PublishMaterial from './components/PublishMaterial'
import Certification from './components/Certification'
import { insertConsultantExtraInfo } from '@/api/consultant'
import { isPureChar } from '@/utils/validate'
export default {
  name: 'DialogThree',
  components: {
    'training-experience': TrainingExperience,
    'supervised-experience': SupervisedExperience,
    'education-experience': EducationExperience,
    'publish-material': PublishMaterial,
    'certification': Certification
  },
  data() {
    return {
      isLoading: false,
      exraInfo: {}
    }
  },
  methods: {
    next() {
      if (this.exraInfo.educationExperienceList.length === 0) {
        this.$notify('请填写教育经历')
        return
      } else {
        const list = this.exraInfo.educationExperienceList
        for (var i = 0; i < list.length; i++) {
          if (!list[i].educationTypeDesc) {
            this.$notify('请填写学历')
            return false
          }
          if (!list[i].major) {
            this.$notify('请填写专业')
            return false
          }
          if (!list[i].certificateUrl) {
            this.$notify('请填写学历证书')
            return false
          }
        }
      }
      if (this.exraInfo.certificationList.length === 0) {
        this.$notify('请填写资质认证')
        return
      } else {
        const list = this.exraInfo.certificationList
        for (var j = 0; j < list.length; j++) {
          if (!list[j].certificateTypeName) {
            this.$notify('请填写资质名称')
            return false
          }
          if (!list[j].certificateNo) {
            this.$notify('请填写资质编号')
            return false
          } else {
            if (!(isPureChar(list[j].certificateNo))) {
              this.$notify('资质编号格式不正确')
              return false
            }
          }
          if (!list[j].certificateAge) {
            this.$notify('请填写持证年限')
            return false
          }
          if (!list[j].certificateUrl) {
            this.$notify('请填写资质证书')
            return false
          }
        }
      }
      if (this.exraInfo.publishPaperList.length > 0) {
        const list = this.exraInfo.publishPaperList
        for (let j = 0; j < list.length; j++) {
          if (list[j].publishSite) {
            if (!isPureChar(list[j].publishSite)) {
              this.$notify('网址格式不正确')
              return false
            }
          }
        }
      }
      this.isLoading = true
      insertConsultantExtraInfo(this.exraInfo).then(r => {
        if (r.success) {
          this.$toast.success('保存成功')
          this.$emit('stepSecondDone')
          this.isLoading = false
        } else {
          this.$notify(r.message)
        }
      })
    },
    getEducationExperienceData(list) {
      this.exraInfo.educationExperienceList = list
    },
    getCertificationData(list) {
      this.exraInfo.certificationList = list
    },

    getPublishBookData(list) {
      this.exraInfo.publishBookList = list
    },
    getPublishPaperData(list) {
      this.exraInfo.publishPaperList = list
    },
    getTrainingExperienceData(list) {
      this.exraInfo.trainingExperienceList = list
    },
    getSupervisedExperienceData(list) {
      this.exraInfo.supervisedExperienceList = list
    }
  }
}
</script>
<style scoped>
@import "../assets/public.css";
.el-input__inner {
  width: 4.82rem;
  height: 0.72rem;
  line-height: 0.72rem;
  font-size: 0.24rem;
  border: 0.02rem solid #f0f0f0;
  border-radius: 0.1rem;
}
.el-select-dropdown__item {
  font-size: 0.18rem;
}
.el-input__icon {
  line-height: 0.72rem;
}
.imgSection {
  text-align: center;
  /* 	padding-top: .6rem;
	padding-bottom: .4rem; */
}

.imgName {
  width: 3rem;
  height: 0.6rem;
  margin-bottom: 0.4rem;
  margin-top: 0.6rem;
}

.center {
  border: 0.02rem solid rgba(157, 220, 177);
  border-radius: 0.2rem;
  width: 6.2rem;
  padding: 0.38rem 0.38rem;
  margin: 0.4rem auto;
  font-size: 0.24rem;
  color: #000;
}

.centers {
  display: flex;
  justify-content: center;
  border: 0.02rem solid rgba(157, 220, 177);
  border-radius: 0.2rem;
  width: 6.9rem;
  padding: 0.38rem 0;
  margin: 0.4rem auto;
  font-size: 0.24rem;
  color: #000;
}

.center-top {
  font-size: 0.26rem;
  display: flex;
  justify-content: center;
  padding-bottom: 0.2rem;
  color: #9edcb0;
}

.center-top img {
  width: 0.34rem;
  height: 0.26rem;
  margin: 0 0.2rem;
}

.center-tops {
  font-size: 0.2rem;
  text-align: center;
  padding-bottom: 0.2rem;
  color: #666;
}

#main {
  width: 4.82rem;
  height: 0.72rem;
  border: 0.02rem solid #f0f0f0;
  border-radius: 0.1rem;
}

#content {
  width: 3.82rem;
  height: 0.72rem;
  font-size: 0.24rem;
  background: #ffffff;
  line-height: 0.72rem;
  text-align: center;
  border-radius: 0.1rem;
}

#selectImg {
  top: 13px;
  right: 10px;
  cursor: pointer;
  width: 0.36rem;
  height: 0.12rem;
}

#selectItem {
  display: none;
  border: 0.01rem solid #eee;
  width: 4.82rem;
}

#selectItem ul {
  list-style: none;
  z-index: 10;
}

#selectItem ul li {
  height: 0.3rem;
  line-height: 0.3rem;
  padding-left: 0.1rem;
  background-color: #fff;
  font-size: 0.2rem;
  z-index: 10;
}

#selectItem ul li:hover {
  background-color: #ebebeb;
}

.cj_btn {
  width: 4.82rem;
  font-size: 0.24rpx;
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

input[type="radio"] + label::before {
  content: " ";
  /*不换行空格*/
  display: inline-block;
  vertical-align: middle;
  font-size: 18px;
  width: 0.3rem;
  height: 0.3rem;
  margin-right: 0.1rem;
  border-radius: 50%;
  background: url(../assets/images/yes.png);
  background-size: 0.3rem 0.3rem;
  /*原始按钮图片地址*/
}

input[type="radio"]:checked + label::before {
  content: " ";
  /*不换行空格*/
  display: inline-block;
  vertical-align: middle;
  font-size: 18px;
  width: 0.3rem;
  height: 0.3rem;
  margin-right: 0.1rem;
  border-radius: 50%;
  background: url(../assets/images/no.png);
  background-size: 0.3rem 0.3rem;
  /*选中的按钮图片地址*/
}

input[type="radio"] {
  position: absolute;
  clip: rect(0, 0, 0, 0);
}

.onlable {
  margin-right: 0.4rem;
}
.cj_btn select {
  width: 4.82rem;
  height: 0.72rem;
  appearance: none;
  -moz-appearance: none;
  -webkit-appearance: none;
  background: url(../assets/images/t6.png) no-repeat right;
  background-size: 0.36rem 0.12rem;
  font-size: 0.2rem;
  font-family: Microsoft YaHei;
  color: #666;
  outline: none;
  border: 0.02rem solid #f0f0f0;
  border-radius: 0.1rem;
  padding-left: 0.2rem;
}

.cj_btns {
  width: 4.82rem;
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

.cj_btns select {
  width: 1.58rem;
  height: 0.72rem;
  appearance: none;
  -moz-appearance: none;
  -webkit-appearance: none;
  background: url(../assets/images/t6.png) no-repeat right;
  background-size: 0.36rem 0.12rem;
  font-size: 0.2rem;
  font-family: Microsoft YaHei;
  color: #666;
  outline: none;
  border: 0.02rem solid #f0f0f0;
  border-radius: 0.1rem;
  padding-left: 0.2rem;
}

.nameInput {
  width: 4.64rem;
  height: 0.72rem;
  border: 0.02rem solid #f0f0f0;
  border-radius: 0.1rem;
  font-size: 0.24rem;
  background: #ffffff;
  padding-left: 0.18rem;
}

.nameInputs {
  width: 2.1rem;
  height: 0.72rem;
  border: 0.02rem solid #f0f0f0;
  border-radius: 0.1rem;
  font-size: 0.24rem;
  background: #ffffff;
  padding-left: 0.18rem;
}

/*定义图像以及大小*/
.justImageFileInput {
  width: 3rem;
  height: 1.5rem;
  border-radius: 0.1rem 0.1rem 0 0;
  background-image: url(../assets/images/jia.png);
  background-size: 100% 1.5rem;
  /*这里可以换成图片路径（background-image：../img/....）注意图片路径*/
}
.upload-image {
  width: 3rem;
  height: 1.5rem;
  border-radius: 0.1rem 0.1rem 0 0;
  background-size: 100% 1.5rem;
  /*这里可以换成图片路径（background-image：../img/....）注意图片路径*/
}

.justInput {
  width: 3rem;
  height: 1.5rem;
  opacity: 0;
}

.Preservation {
  width: 3.78rem;
  height: 0.76rem;
  line-height: 0.76rem;
  color: #ffffff;
  background: #9ddcb0;
  text-align: center;
  border-radius: 0.2rem;
  font-size: 0.28rem;
  border: 0.02rem solid #9ddcb0;
  flex: 1;
}

.Preservations {
  width: 6.2rem;
  height: 0.76rem;
  line-height: 0.76rem;
  color: #ffffff;
  background: #9ddcb0;
  text-align: center;
  border-radius: 0.2rem;
  font-size: 0.28rem;
  margin: 0 auto;
}

.delete {
  width: 2.14rem;
  height: 0.76rem;
  line-height: 0.76rem;
  color: #9ddcb0;
  border: 0.02rem solid #9ddcb0;
  border-radius: 0.2rem;
  font-size: 0.28rem;
  text-align: center;
}

.increase {
  width: 6.9rem;
  height: 0.76rem;
  margin: 0.4rem auto;
  line-height: 0.76rem;
  color: #9ddcb0;
  border: 0.02rem solid #9ddcb0;
  border-radius: 0.38rem;
  font-size: 0.28rem;
  text-align: center;
  display: block;
  background: #fff;
}

button {
  width: 6.9rem;
  height: 0.76rem;
  margin: 0.4rem auto;
  line-height: 0.76rem;
  color: #9ddcb0;
  border: 0.02rem solid #9ddcb0;
  border-radius: 0.38rem;
  font-size: 0.28rem;
  text-align: center;
  display: block;
  background: #fff;
}

#footer-text {
  font-size: 0.24rem;
  color: #9edcb0;
  text-align: center;
  padding-bottom: 0.2rem;
}
.footer {
  text-align: center;
  color: #ffffff;
  width: 6.4rem;
  height: 0.8rem;
  line-height: 0.8rem;
  font-size: 0.26rem;
  background-color: #9ddcb0;
  border-radius: 0.14rem;
  margin: 0.4rem;
}
</style>

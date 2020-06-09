<template>
  <div id="base-info" class="dialogTwo">
    <!-- <form name="myForm" onsubmit="return validateForm()"> -->
    <div ref="addForm" label-width="100px" class="demo-addForm">
      <center>
        <div class="app-flex" style="border-bottom: .01rem solid #f0f0f0;">
          <div class="box" style="line-height: 1rem;">
            <div style="line-height: 1rem;">
              <span style="color:red;font-size: .16rem;">＊</span>
              头像:</div>
            <img
              src="../assets/images/tan.png"
              style="width: .3rem;height: .3rem;margin-left: .5rem;"
              @click="tips = true"
            >
          </div>
          <div class="avatar">
            <div v-if="!object.headImg" class="avatar-default">
              <div class="fileInput" />
            </div>
            <div v-else>
              <img class="avatar" :src="baseUrl + object.headImg">
            </div>
            <H5Cropper :option="option" @imgorigoinf="imgorigoinf" @getbase64Data="getbase64Data" @getblobData="getblobData" />
          </div>
          <!-- <img :src="headImg" /> -->
        </div>
        <van-dialog v-model="tips">
          <div class="tips">
            <p>1.照片清晰，文件小于5M（Jpg，Png格式图片）</p>
            <p>2.人像居中至少露出肩部，顶部及两侧需留白</p>
            <p>3.色调温暖明亮，内容积极向上，状态和蔼可亲</p>
          </div>
        </van-dialog>
        <div style="border-bottom: .01rem solid #f0f0f0;padding-bottom: .34rem;">
          <div class="box" style="line-height: 1rem;">
            <span style="color:red;font-size: .16rem;">＊</span>
            <div>身份证</div>
          </div>
          <div class="app-flex">
            <div class="shen" @click="uploadFrontClick">
              <div v-if="!cardFrontPath" class="justImageFileInput" />
              <div v-else>
                <img class="card-image" :src="baseUrl + cardFrontPath">
              </div>
              <span>上传身份证人头面</span>
            </div>
            <input id="front" :multiple="false" type="file" accept="image/png,image/jpeg,image/jpg" style="display: none" @change="frontResult">
            <div class="shen" @click="uploadBackClick">
              <div v-if="!cardReversePath" class="backImageFileInput" />
              <div v-else>
                <img class="card-image" :src="baseUrl + cardReversePath">
              </div>
              <span>上传身份证国徽面</span>
            </div>
            <input id="back" :multiple="false" type="file" accept="image/png,image/jpeg,image/jpg" style="display: none" @change="backResult">
          </div>
          <div class="app-flex">
            <div class="box" style="line-height: 1rem;">
              <span style="color:red;font-size: .16rem;">＊</span>
              <p>姓名:</p>
            </div>
            <van-field
              v-model="object.realName"
              class="nameInput"
              maxlength="6"
              center
            />
          </div>
          <div class="app-flex">
            <div class="box" style="line-height: 1rem;">
              <span style="color:red;font-size: .16rem;">＊</span>
              <p>性别:</p>
            </div>
            <div class="cj_btn">
              <input id="cjon" v-model="object.sex" type="radio" name="Gender" value="0">
              <label for="cjon" class="onlable box">
                <p>男</p>
              </label>
              <input id="cjoff" v-model="object.sex" type="radio" name="Gender" value="1">
              <label for="cjoff" class="onlable box">
                <p>女</p>
              </label>
            </div>
          </div>
          <div class="app-flex">
            <div class="box" style="line-height: 1rem;">
              <span style="color:red;font-size: .16rem;">＊</span>
              <p>出生年月:</p>
            </div>
            <div class="cj_btn">
              <van-field
                v-model="object.birthDate"
                class="nameInputs"
                center
                readonly
                placeholder="出生年月"
                @click="startTimePick"
              />
            </div>
          </div>
          <van-popup v-model="startTimePicker" position="bottom">
            <van-datetime-picker
              v-model="currentDate"
              type="year-month"
              :min-date="minDate"
              :max-date="maxDate"
              @cancel="startTimePicker = false"
              @confirm="onStartTimeConfirm"
            />
          </van-popup>
          <div class="app-flex">
            <div class="box" style="line-height: 1rem;">
              <span style="color:red;font-size: .16rem;">＊</span>
              <p>所在地区:</p>
            </div>
            <div class="cj_btn">
              <van-field
                v-model="fullArea"
                right-icon="arrow-down"
                readonly
                placeholder="选择所在地区"
                clickable
                @click="showPicker = true"
              />
            </div>
          </div>
          <div class="app-flex">
            <div class="box" style="line-height: 1rem;">
              <span style="color:red;font-size: .16rem;">＊</span>
              <p>详细地址:</p>
            </div>
            <van-field
              v-model="object.addrDetail"
              class="nameInput"
              maxlength="30"
              center
            />
          </div>
          <div class="app-flex input">
            <div class="box" style="line-height: 1rem;">
              <span style="color:red;font-size: .16rem;">＊</span>
              <p>手机号码:</p>
            </div>
            <div>
              <van-field
                v-model="object.phone"
                class="nameInput"
                type="digit"
                maxlength="11"
                center
              />
            </div>
          </div>
          <div class="app-flex">
            <div class="box" style="line-height: 1rem;">
              <span style="color:red;font-size: .16rem;">＊</span>
              <p>微信号码:</p>
            </div>
            <van-field
              v-model="object.weixing"
              class="nameInput"
              maxlength="20"
              center
            />
          </div>
          <div class="app-flex">
            <div style="line-height: 1rem;">邀请码:</div>
            <van-field
              v-model="object.invitationCode"
              class="nameInput"
              maxlength="20"
              center
            />
          </div>
          <div class="app-flex">
            <div style="line-height: 1rem;">邮箱:</div>
            <van-field
              v-model="object.mailbox"
              class="nameInput"
              maxlength="50"
              center
            />
          </div>
          <div class="app-flex">
            <div class="box" style="line-height: 1rem;">
              <span style="color:red;font-size: .16rem;">＊</span>
              <p>从业年限:</p>
            </div>
            <van-field
              v-model="object.workingSeniority"
              class="nameInput"
              type="digit"
              maxlength="2"
              placeholder="年"
              center
            />
          </div>
          <div class="app-flex">
            <div class="box" style="line-height: 1rem;">
              <span style="color:red;font-size: .16rem;">＊</span>
              <p>累计个案:</p>
            </div>
            <van-field
              v-model="object.accumulativeCase"
              class="nameInput"
              type="digit"
              maxlength="7"
              placeholder="小时"
              center
            />
          </div>
          <div class="app-flex input">
            <div class="box" style="line-height: 1rem;">
              <!-- <span style="color:red;font-size: .16rem;">＊</span> -->
              <p>签名:</p>
            </div>
            <van-field
              v-model="object.sendWord"
              rows="1"
              autosize
              type="textarea"
              class="nameInput"
              maxlength="50"
              center
            />
          </div>
          <div class="app-flex input">
            <div class="box" style="line-height: 1rem;">
              <p>个人简介:</p>
            </div>
            <van-field
              v-model="object.briefIntroduction"
              rows="5"
              autosize
              maxlength="5000"
              type="textarea"
              class="text-area"
              center
            />
          </div>
          <div class="footer" style="margin-top: .28rem;">
            <van-button :loading="isLoading" loading-type="spinner" class="footer" @click="next">下一步</van-button>
          </div>
        </div>
      </center>
    </div>
    <van-popup v-model="showPicker" position="bottom">
      <van-area
        show-toolbar
        :area-list="areaList"
        @cancel="showPicker = false"
        @confirm="onConfirm"
      />
    </van-popup>
  </div>
</template>
<script>
import { fileUploadUrl, baseUrl, loadUrl } from '@/data/config'
import { settledIninserConsultant } from '@/api/consultant'
import Axios from 'axios'
import AreaList from '@/data/area'
import H5Cropper from 'vue-cropper-h5'
import { isValidMobile, isValidEmail, isPureChar, isValidNumber } from '@/utils/validate'
export default {
  components: { H5Cropper },
  props: {
    defaultData: {
      type: Object,
      default() {
        return {
        }
      },
      required: false
    }
  },
  data() {
    return {
      startTimePicker: false,
      minDate: new Date(1960, 0, 1),
      maxDate: new Date(),
      currentDate: new Date(),
      baseUrl: baseUrl,
      isLoading: false,
      showPicker: false,
      areaList: AreaList,
      tips: false,
      cardFrontPath: '',
      cardReversePath: '',
      object: {
      },
      option: {
        ceilbutton: true
      }, // 配置
      fileName: '',
      fileBlob: undefined

    }
  },
  computed: {
    fullArea: function() {
      const areaNameList = []
      if (this.object.provName) {
        areaNameList.push(this.object.provName)
      }
      if (this.object.cityName) {
        areaNameList.push(this.object.cityName)
      }
      if (this.object.areaName) {
        areaNameList.push(this.object.areaName)
      }
      if (areaNameList.length > 0) {
        return areaNameList.join('-')
      } else {
        return ''
      }
    }
  },
  watch: {
    'defaultData': {
      handler(newVal) {
        this.object = newVal
        if (this.object.cards.length > 0) {
          this.cardFrontPath = this.object.cards[0].imgUrl
          this.cardReversePath = this.object.cards[1].imgUrl
        }
      },
      deep: true,
      immediate: true
    }
  },
  methods: {
    startTimePick() {
      this.startTimePicker = true
    },
    onStartTimeConfirm(value) {
      this.object.birthDate = this.timeFormat(value)
      this.startTimePicker = false
    },
    timeFormat(time) { // 时间格式化 2019-09-08
      const year = time.getFullYear()
      const month = time.getMonth() + 1
      return year + '-' + (month < 10 ? '0' + month : month)
    },
    checkParam() {
      if (!this.object.headImg) {
        this.$notify('请上传图像')
        return false
      }
      if (!this.cardFrontPath) {
        this.$notify('请上传身份正面')
        return false
      }
      if (!this.cardReversePath) {
        this.$notify('请上传身份反面')
        return false
      }
      if (!this.object.realName) {
        this.$notify('请输入姓名')
        return false
      }
      if (!this.object.birthDate) {
        this.$notify('请选择出生年月')
        return false
      }
      if (!this.fullArea) {
        this.$notify('请选择省-市-区')
        return false
      }
      if (!this.object.addrDetail) {
        this.$notify('请输入详细地址')
        return false
      }
      if (!this.object.phone) {
        this.$notify('请输入手机号码')
        return false
      }

      if (this.object.phone) {
        if (!isValidMobile(this.object.phone)) {
          this.$notify('手机号码格式不正确')
          return false
        }
      }
      if (!this.object.weixing) {
        this.$notify('请输入微信号码')
        return false
      } else {
        if (!isPureChar(this.object.weixing)) {
          this.$notify({ type: 'danger', message: '微信号码格式不正确' })
          return false
        }
      }
      if (this.object.invitationCode) {
        if (!isPureChar(this.object.invitationCode)) {
          this.$notify({ type: 'danger', message: '邀请码不能有汉字' })
          return false
        }
      }
      if (this.object.mailbox) {
        if (!isValidEmail(this.object.mailbox)) {
          this.$notify({ type: 'danger', message: '邮箱格式不正确' })
          return false
        }
      }
      if (!this.object.workingSeniority) {
        this.$notify('请输入从业年限')
        return false
      } else {
        if (!isValidNumber(this.object.workingSeniority)) {
          this.$notify('从业年限格式不正确')
          return false
        }
      }
      if (!this.object.accumulativeCase) {
        this.$notify('请输入累计个案')
        return false
      } else {
        if (!isValidNumber(this.object.accumulativeCase)) {
          this.$notify('累计个案格式不正确')
          return false
        }
      }
      return true
    },
    next() {
      if (this.checkParam()) {
        this.isLoading = true
        const idCards = []
        idCards.push({ imgUrl: this.cardFrontPath })
        idCards.push({ imgUrl: this.cardReversePath })
        this.object.cards = idCards
        settledIninserConsultant(this.object).then(r => {
          this.isLoading = false
          if (r.success) {
            this.$toast.success('保存成功')
            this.$emit('stepThirdDone')
          } else {
            this.$notify(r.message)
          }
        })
      }
    },
    onConfirm(value) {
      if (value[0] !== undefined) {
        this.object.provName = value[0].name
        this.object.provCode = value[0].code
      }
      if (value[1] !== undefined) {
        this.object.cityName = value[1].name
        this.object.cityCode = value[1].code
      }
      if (value[2] !== undefined) {
        this.object.areaName = value[2].name
        this.object.areaCode = value[2].code
      } else {
        this.object.areaName = ''
        this.object.areaCode = ''
      }
      this.showPicker = false
    },
    uploadFrontClick() {
      document.getElementById('front').click()
    },
    frontResult: function(e) {
      const file = e.target.files[0]
      if (file) {
        if (file.size > 0 && file.size <= 5242880) { // 判断文件大小
          var reader = new FileReader()
          reader.onload = function(event) {
            var txt = event.target.result
            var img = document.getElementById('front')
            img.src = txt
            img.style.backgroundRepeat = 'no-repeat'
          }
        } else {
          this.$notify('图片不能小于0kb且不能大于5mb')
          return
        }
      } else {
        return
      }
      reader.readAsDataURL(file)
      const param = new FormData() // 创建form对象
      param.append('file', file) // 通过append向form对象添加数据
      param.append('output', 'json') // 添加form表单中其他数据
      param.append('path', '/image') // 添加form表单中其他数据
      param.append('scene', 'image') // 添加form表单中其他数据
      const config = {
        headers: { 'Content-Type': 'multipart/form-data' }
      } 
      this.cardFrontPath = loadUrl
      // 添加请求头
      Axios.post(fileUploadUrl, param, config).then(
        response => {
          if (response.data.path === undefined) {
            this.$notify('身份证反面上传失败')
            return
          }
          this.cardFrontPath = response.data.path
        }
      )
    },
    uploadBackClick() {
      document.getElementById('back').click()
    },
    backResult: function(e) {
      const file = e.target.files[0]
      if (file) {
        if (file.size > 0 && file.size <= 5242880) { // 判断文件大小
          var reader = new FileReader()
          reader.onload = function(event) {
            var txt = event.target.result
            var img = document.getElementById('back')
            img.src = txt
            img.style.backgroundRepeat = 'no-repeat'
          }
        } else {
          this.$notify('图片不能小于0kb且不能大于5mb')
          return
        }
      } else {
        return
      }
      reader.readAsDataURL(file)
      const param = new FormData() // 创建form对象
      param.append('file', file) // 通过append向form对象添加数据
      param.append('output', 'json') // 添加form表单中其他数据
      param.append('path', '/image') // 添加form表单中其他数据
      param.append('scene', 'image') // 添加form表单中其他数据
      const config = {
        headers: { 'Content-Type': 'multipart/form-data' }
      } 
      this.cardReversePath = loadUrl
      // 添加请求头
      Axios.post(fileUploadUrl, param, config).then(
        response => {
          this.cardReversePath = response.data.path
        }
      )
    },
    getbase64Data(data) {
      this.img = data
    },
    getblobData(data) {
      this.fileBlob = data
      const files = new window.File([this.fileBlob], this.fileName, { type: this.fileType })
      // console.log(files)
      this.avatarImageChange(files)
    },
    imgorigoinf(file) {
      this.fileName = file.name
      this.fileType = file.type
    },
    avatarImageChange(files) {
      const file = files
      if (file) {
        if (file.size > 0 && file.size <= 5242880) { // 判断文件大小
          var reader = new FileReader()
          reader.onload = function(event) {
            var txt = event.target.result
            var img = document.getElementById('front')
            img.src = txt
            img.style.backgroundRepeat = 'no-repeat'
          }
        } else {
          this.$notify('图片不能小于0kb且不能大于5mb')
          return
        }
      } else {
        return
      }
      reader.readAsDataURL(file)
      const param = new FormData() // 创建form对象
      param.append('file', file) // 通过append向form对象添加数据
      param.append('output', 'json') // 添加form表单中其他数据
      param.append('path', '/image') // 添加form表单中其他数据
      param.append('scene', 'image') // 添加form表单中其他数据
      const config = {
        headers: { 'Content-Type': 'multipart/form-data' }
      } 
      this.$set(this.object, 'headImg', loadUrl)
      // 添加请求头
      Axios.post(fileUploadUrl, param, config).then(
        res => {
          console.log(res)
          this.$set(this.object, 'headImg', res.data.path)
        }
      )
    }
  }
}
</script>
<style>
@import "../assets/public.css";
.el-input__inner {
  width: 1.4rem;
  height: 0.72rem;
  line-height: 0.72rem;
  font-size: 0.24rem;
  border: 0.02rem solid #f0f0f0;
  border-radius: 0.1rem;
}
.tips {
  margin: 18px;
  line-height: 0.62rem;
  font-size: 0.26rem;
  text-align: left;
}
.van-button {
    position: relative;
    display: inline-block;
    box-sizing: border-box;
    height: 40px;
    margin: 0;
    padding: 0;
    font-size: 16px;
    line-height: 42px;
    text-align: center;
    border-radius: 2px;
    cursor: pointer;
    -webkit-transition: opacity .2s;
    transition: opacity .2s;
    -webkit-appearance: none;
    -webkit-text-size-adjust: 100%;
}
.el-select-dropdown__item {
  font-size: 0.18rem;
}
.el-input__icon {
  line-height: 0.72rem;
}
.el-select .el-input .el-select__caret {
  color: #9ddcaf !important;
  font-size: 0.26rem;
}
center {
  border: 0.02rem solid rgba(157, 220, 177);
  border-radius: 0.05rem;
  width: 6.2rem;
  padding: 0 0.38rem;
  margin: 0.4rem auto;
  font-size: 0.24rem;
  color: #333;
}
.footer {
  text-align: center;
  color: #ffffff;
  width: 6.4rem;
  height: 0.8rem;
  line-height: 0.8rem;
  font-size: 0.26rem;
  margin: 0 auto;
  background-color: #9ddcb0;
  border-radius: 0.14rem;
  margin-bottom: 0.4rem;
}
/*定义图像以及大小*/
.avatar-default {
  width: 0.8rem;
  height: 0.8rem;
  border-radius: 50%;
  /* position: absolute; */
  background-image: url(../assets/images/t1.png);
  background-size: 0.8rem 0.8rem;
  /*这里可以换成图片路径（background-image：../img/....）注意图片路径*/
}
/*定义图像以及大小*/
.avatar {
  width: 0.8rem;
  height: 0.8rem;
  border-radius: 50%;
  /* position: absolute; */
  background-size: 0.8rem 0.8rem;
  position: relative;
  /*这里可以换成图片路径（background-image：../img/....）注意图片路径*/
}
/*定义上传*/

.fileInput {
  height: 100%;
  /* 	position: absolute;
	right: 0;
	top: 0; */
  opacity: 0;
}

.shen {
  width: 2.7rem;
  height: 2.34rem;
  background: #9ddcaf;
  border-radius: 0.2rem;
  line-height: 0.54rem;
  color: #ffffff;
  font-size: 24rpx;
}

/*定义图像以及大小*/
.justImageFileInput {
  width: 100%;
  height: 1.78rem;
  border-radius: 0.1rem 0.1rem 0 0;
  background-image: url(../assets/images/zheng.png);
  background-size: 100% 1.78rem;
  /*这里可以换成图片路径（background-image：../img/....）注意图片路径*/
}
.card-image {
  width: 100%;
  height: 1.78rem;
  border-radius: 0.1rem 0.1rem 0 0;
}
/*定义上传*/

.justInput {
  height: 100%;
  opacity: 0;
}

/*定义身份证反面图像以及大小*/
.backImageFileInput {
  width: 100%;
  height: 1.78rem;
  border-radius: 0.1rem 0.1rem 0 0;
  background-image: url(../assets/images/fan.png);
  background-size: 100% 1.78rem;
  /*这里可以换成图片路径（background-image：../img/....）注意图片路径*/
}
.backImageFileInput .label {
  padding-top: 1.7rem;
}
.backInput {
  height: 100%;
  opacity: 0;
}

.nameInput {
  width: 4.82rem;
  height: 0.72rem;
  border: 0.02rem solid #f0f0f0;
  border-radius: 0.1rem;
  font-size: 0.24rem;
  background: #ffffff;
}
.text-area {
  width: 4.82rem;
  border: 0.02rem solid #f0f0f0;
  border-radius: 0.1rem;
  font-size: 0.24rem;
  background: #ffffff;
}
.cj_btn {
  width: 4.82rem;
  font-size: 0.24rem;
  display: flex;
  align-items: center;
  justify-content: flex-start;
}

.cj_btn input[type="radio"] + label::before {
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

select {
  width: 1.4rem;
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
}

.input {
  position: relative;
}

.span {
  text-align: center;
  color: #ffffff;
  width: 6.9rem;
  height: 0.8rem;
  line-height: 0.8rem;
  font-size: 0.26rem;
  margin: 0 auto;
  background-color: #9ddcb0;
  border-radius: 0.14rem;
  margin-bottom: 0.4rem;
}
 .wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;
  }

  .block {
    width: 100%;
    height: 120px;
    background-color: #fff;
  }
</style>

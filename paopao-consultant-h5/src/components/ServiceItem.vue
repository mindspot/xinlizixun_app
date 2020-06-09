<template>
  <div id="service-item">
    <div class="main">
      <div class="main__tip">
        <p>本页所有选项，可在入驻申请通过后</p>
        <p>在咨询师工作平台进行再次设置</p>
      </div>
      <div class="main__major">
        <div class="main__major__title">
          <span class="main__major__title__field"><span style="color:red;font-size: .16rem;">＊</span>专业领域：</span>
          <span class="main__major__title__tip">
            请选择
            <i>1-3</i>项最擅长的领域
          </span>
        </div>
        <div class="main__major__content">
          <div class="main__major__content__line">
            <van-checkbox-group v-model="expertiseAreaResult" :max="3" direction="horizontal">
              <van-checkbox v-for="item in expertiseAreaEnums" :key="item.id" :name="item.id" icon-size=".35rem" style="margin:0.1rem;" checked-color="#9ddcaf" shape="square">
                {{ item.val }}
              </van-checkbox>
            </van-checkbox-group>
          </div>
        </div>
        <div class="main__major__title">
          <span class="main__major__title__field">工作时间：</span>
          <span class="main__major__title__tip">
            白色区域为接单时间
          </span>
        </div>
        <div class="main__label">
          <div class="main__label__type">
            <van-row style="margin:.1rem .01rem;width:100%;">
              <van-col v-for="(item,index) in platformWorkingTimeList" :key="index" span="8">
                <van-button style="font-size: .26rem; width:100%;" :class="{'work': item.status !== 0 }" type="default" @click="setWorkStatus(item)">{{ item.consultantWorkStart }}-{{ item.consultantWorkEnd }}</van-button>
              </van-col>
            </van-row>
          </div>
        </div>
        <div class="main__point_at">
          <div class="main__point_at__title"> <span style="color:red;font-size: .16rem;">＊</span>针对人群:</div>
          <div class="main__major__content">
            <div class="main__major__content__line">
              <van-checkbox-group v-model="targetPeopleResult" direction="horizontal">
                <van-checkbox v-for="item in targetPeopleEnums" :key="item.id" :name="item.id" icon-size=".35rem" style="margin:0.1rem;" checked-color="#9ddcaf" shape="square">
                  {{ item.val }}
                </van-checkbox>
              </van-checkbox-group>
            </div>
          </div>
          <div class="main__major__content__line line1 main__point_at__title__content" />
        </div>

        <div class="main__work_type">
          <div class="main__work_type__title"><span style="color:red;font-size: .16rem;">＊</span>工作类型：</div>
          <div class="main__work_type__content">
            <div v-for="(item,index) in consultPriceList" :key="index" class="main__work_type__content__line">
              <van-row gutter="1" style="height: .7rem;">
                <van-col span="8">
                  <van-field
                    v-model="item.val"
                    right-icon="arrow-down"
                    class="input-select"
                    center
                    readonly
                    placeholder="方式"
                    @click="consultPick(index)"
                  />
                </van-col>
                <van-col span="8">
                  <van-field
                    v-model="item.serviceName"
                    right-icon="arrow-down"
                    class="input-select"
                    center
                    readonly
                    placeholder="次数"
                    @click="serviceTimePick(index)"
                  />
                </van-col>
                <van-col span="5">
                  <van-field
                    v-model="item.sellPrice"
                    center
                    type="digit"
                    maxlength="6"
                    class="input-select"
                    placeholder="费用"
                  />
                </van-col>
                <van-col span="3">
                  <div v-if="index>0" class="input-button" @click="minusRow(index)">-</div>
                  <div v-else class="input-button" @click="plusRow">+</div>
                </van-col>
              </van-row>
              <van-popup v-model="consultPicker" position="bottom">
                <van-picker
                  show-toolbar
                  :columns="consultEnums"
                  value-key="value"
                  @cancel="consultPicker = false"
                  @confirm="onConsultConfirm"
                />
              </van-popup>
              <van-popup v-model="serveTimePicker" position="bottom">
                <van-picker
                  show-toolbar
                  :columns="serveTimeEnums"
                  value-key="desc"
                  @cancel="serveTimePicker = false"
                  @confirm="onServeTimeConfirm"
                />
              </van-popup>
            </div>
          </div>
        </div>
        <div class="main__tip">
          <p>注意：请输入整数数字价格</p>
          <p>选择“语音、视频、面对面”，默认咨询单次</p>
          <p>选择“语音/视频/面对面”，可选择多次，为套餐类服务</p>
        </div>
      </div>
    </div>
    <div class="footer-container">
      <van-button :loading="isLoading" class="footer" loading-type="spinner" @click="next">提交审核</van-button>
    </div>
  </div>
</template>>
<script>
import { listTargetPeople, listExpertiseArea, listPlatformWorkingTime } from '@/api/option'
import { settledIninsertWord, listWorkingTime } from '@/api/consultant'
import { isValidNumber } from '@/utils/validate'
export default {
  name: 'DialogFour',
  props: {},
  data() {
    return {
      startTimePicker: false,
      endTimePicker: false,
      startTimeIndex: 0,
      endTimeIndex: 0,
      serveTimePicker: false,
      consultPicker: false,
      isLoading: false,
      targetPeopleResult: [],
      expertiseAreaResult: [],
      checked: false,
      targetPeopleEnums: [],
      expertiseAreaEnums: [],
      consultPriceList: [],
      consultPriceIndex: 0,
      platformWorkingTimeList: [],
      consultEnums: [{ code: 0, value: '语音' }, { code: 1, value: '视频' }, { code: 2, value: '面对面' }, { code: 6, value: '语/视/面' }],
      serveTimeEnums: [{ times: 3, desc: '3次*50分钟' }, { times: 6, desc: '6次*50分钟' },
        { times: 9, desc: '9次*50分钟' },
        { times: 12, desc: '12次*50分钟' },
        { times: 15, desc: '15次*50分钟' },
        { times: 18, desc: '18次*50分钟' }
      ]
    }
  },
  created() {
    listTargetPeople().then(r => {
      this.targetPeopleEnums = r.result.commonList
      if (r.result.relatedIds) {
        this.targetPeopleResult = r.result.relatedIds
      }
    })
    listExpertiseArea().then(r => {
      this.expertiseAreaEnums = r.result.commonList
      if (r.result.relatedIds) {
        this.expertiseAreaResult = r.result.relatedIds
      }
    })
    listPlatformWorkingTime().then(r => {
      if (r.success) {
        this.platformWorkingTimeList = r.result
      } else {
        this.$notify(r.message)
      }
    })
    listWorkingTime().then(r => {
      if (r.result.length > 0) {
        this.consultPriceList = r.result
      } else {
        const consultPrice = {}
        this.consultPriceList.push(consultPrice)
      }
    })
  },
  methods: {
    next() {
      const param = {}
      param.classification = this.expertiseAreaResult
      param.contention = this.targetPeopleResult
      param.works = this.consultPriceList
      param.platformWorkingTimeVOList = this.platformWorkingTimeList
      if (param.classification.length === 0) {
        this.$notify('请选择擅长的领域')
        return
      }
      if (param.contention.length === 0) {
        this.$notify('请选择针对人群')
        return
      }
      var containSingle = false
      if (param.works.length > 0) {
        for (var i = 0; i < param.works.length; i++) {
          if (!param.works[i].val) {
            this.$notify('请选择服务方式')
            return
          }
          if (!param.works[i].serviceTimes) {
            this.$notify('请选择服务次数')
            return
          } else if (param.works[i].serviceTimes === 1) {
            containSingle = true
          }
          if (!param.works[i].sellPrice) {
            this.$notify('请选择服务费用')
            return
          } else {
            if (!isValidNumber(param.works[i].sellPrice)) {
              this.$notify('服务费用格式不正确')
              return
            }
          }
        }
      }
      if (!containSingle) {
        this.$notify('需要添加单次类产品')
        return
      }
      this.isLoading = true
      settledIninsertWord(param).then(r => {
        this.isLoading = false
        if (!r.success) {
          this.$notify(r.message)
        } else {
          this.$toast.success('保存成功')
          localStorage.setItem('CONSULTANT_STATUS', 1)
          this.$emit('stepFourDone')
        }
      })
    },
    setWorkStatus(item) {
      if (item.status === 0) {
        item.status = 2
      } else {
        item.status = 0
      }
    },
    consultPick(index) {
      this.consultPriceIndex = index
      this.consultPicker = true
    },
    onConsultConfirm(value) {
      this.consultPriceList[this.consultPriceIndex].val = value.value
      this.consultPriceList[this.consultPriceIndex].code = value.code
      if (value.code !== 6) {
        this.consultPriceList[this.consultPriceIndex].serviceTimes = 1
        this.consultPriceList[this.consultPriceIndex].serviceName = '50分钟'
      } else {
        this.consultPriceList[this.consultPriceIndex].serviceTimes = 3
        this.consultPriceList[this.consultPriceIndex].serviceName = '3次*50分钟'
      }
      this.consultPicker = false
    },
    serviceTimePick(index) {
      if (this.consultPriceList[index].code === 6) {
        this.consultPriceIndex = index
        this.serveTimePicker = true
      }
    },
    onServeTimeConfirm(value) {
      this.consultPriceList[this.consultPriceIndex].serviceTimes = value.times
      this.consultPriceList[this.consultPriceIndex].serviceName = value.desc
      this.serveTimePicker = false
    },
    minusRow(index) {
      this.consultPriceList.splice(index, 1)
    },
    plusRow() {
      if (this.consultPriceList.length < 8) {
        const consultPrice = {}
        this.consultPriceList.push(consultPrice)
      } else {
        this.$notify('最多只能添加8条记录')
      }
    }
  }
}
</script>
<style scoped>
@import "../assets/public.css";
.work {
  background-color: #999999;
}
.disabled {
	display: none;
}
.main {
	margin: .2rem auto 0;
	width: 6.86rem;
	border: 2px solid #9ddcaf;
	border-radius: .12rem;
	padding: 0 .2rem;
	box-sizing: border-box;
	padding-bottom: .66rem;
}

.main__tip {
	height: .9rem;
	text-align: center;
	font-size: .22rem;
	color: #9ddcaf;
	line-height: .3rem;
	padding-top: .12rem;
	padding-bottom: .17rem;
	box-sizing: border-box;
}

.main__major {
	/* height: 2.02rem; */
	padding-top: .19rem;
}

.main__major__title {
	height: .63rem;
	line-height: .63rem;
}

.main__major__title__field {
	font-size: .24rem;
	color: #333;
	font-weight: 600;
}

.main__major__title__tip {
	font-size: .2rem;
	color: #999;
}
.main__major__title__tip i {
	font-size: .24rem;
}

.main__major__content {
	padding-left: .5rem;
}

.main__major__content__line {
	line-height: .32rem;
}

.line1 {
	margin-bottom: .2rem;
}

.main__major__content__label {
	display: inline-block;
	margin-right: .3rem;
}
.main__major__content__label__frame-selected {
	background: #9ddcaf url(../assets/images/selected.png) no-repeat center;
}

.main__major__content__label__value {
	font-size: .22rem;
	margin-left: .2rem;
	display: inline-block;
	vertical-align: middle;
}

.main__label {
	border: 1px solid #9ddcaf;
	border-radius: .13rem;
	height: auto;
	padding: 0 .1rem;
	box-sizing: border-box;
}

.main__label__title {
	color: #9ddcaf;
	font-size: .2rem;
	line-height: .48rem;
	margin-bottom: .04rem;
}

.main__label__type {
	/* width: 5.8rem; */
  height: auto;
}

.main__label__type__section__item-selected {
	background-color: #9ddcaf;
	color: #fff;
}

.main__point_at {
	padding-top: .43rem;
	padding-bottom: .1rem;
	border-bottom: .02rem solid #999;
}

.main__point_at__title__content {
	padding-left: .48rem;
	box-sizing: border-box;
}

.main__point_at__title {
	color: #333;
	font-size: .22rem;
}

.main__point_at__title__section {
	margin: .1rem 0;
	width: 1.83rem;
}

.main__work_type {
	padding-top: .19rem;
}

.main__work_type__title {
	line-height: .43rem;
	font-size: .22rem;
	color: #333;
}

.main__work_type__content__line {
	margin: .1rem .2rem .1rem 0;
	font-size: .22rem;
	color: #333;
}
.input-select {
	border: 1px solid #9ddcaf;
	border-radius: .08rem;
	height: .7rem;
	font-size: .22rem;
	line-height: .7rem;
	padding-left: .2rem;
}
.input-check {
	height: .7rem;
	font-size: .22rem;
	line-height: .7rem;
	padding-left: .2rem;
}
.input-button {
    border-radius: 50%;
    height: .7rem;
    width: 100%;
    color: white;
    font-size: .7rem;
    line-height: .7rem;
    text-align: center;
    background-color: #9ddcaf;
}
.main__work_type__content__common {
	height: .7rem;
	border: 1px solid #9ddcaf;
	border-radius: .08rem;
	display: inline-block;
	line-height: .7rem;
	padding-left: .2rem;
	box-sizing: border-box;
	background: url(../assets/images/down-arrow.png) no-repeat right .14rem top .28rem;
}
.main__work_type__content__line__type {
	width: 1.74rem;
}

.main__work_type__content__line__time {
	width: 1.92rem;
}

.main__work_type__content__line__money {
	width: 1.18rem;
	font-size: .22rem;
	background: none;
}

.main__work_type__content__line__icon {
	width: .6rem;
	height: .6rem;
	vertical-align: middle;
	border: 0;
}
.main__tip {
	text-align: left;
}

.main__tip p {
	font-size: .2rem;
	color: #000;
}
.footer-container {
    margin: 0.4rem;
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
}
</style>

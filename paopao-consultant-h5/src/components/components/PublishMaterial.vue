<template>
  <div id="publish-material">
    <!-- 出版书籍开始 -->
    <div v-for="(item,index) in publishBookList" :key="'PublishBook'+index" class="center">
      <div class="center-top">
        <img src="../../assets/images/zou.png">
        出版书籍
        <img src="../../assets/images/you.png">
      </div>
      <p class="center-tops">(推荐书写；若无相关信息，可不填)</p>
      <div class="app-flex">
        <div style="line-height: 1rem;">书名:</div>
        <van-field
          v-model="item.materialName"
          class="nameInput"
          center
          maxlength="20"
          placeholder="请输入书名"
        />
      </div>
      <div class="app-flex">
        <div style="line-height: 1rem;">出版社:</div>
        <van-field
          v-model="item.publishSite"
          class="nameInput"
          center
          maxlength="30"
          placeholder="请输入出版社"
        />
      </div>
      <div class="app-flex">
        <div style="line-height: 1rem;">出版时间:</div>
        <div class="cj_btn">
          <van-field
            v-model="item.publishTime"
            right-icon="arrow-down"
            center
            readonly
            placeholder="请选择出版时间"
            @click="publishTimePick(index)"
          />
        </div>
        <van-popup v-model="publishTimePicker" position="bottom">
          <van-datetime-picker
            v-model="currentDate"
            type="year-month"
            :min-date="minDate"
            :max-date="maxDate"
            @cancel="publishTimePicker = false"
            @confirm="onPublishTimeConfirm"
          />
        </van-popup>
      </div>
      <div class="app-flex">
        <van-button class="Preservation" :loading="bookLoading" loading-text="保存中..." @click="addPublishBook(index)">保存</van-button>
        <van-button v-if="index != 0" :loading="bookLoading" loading-text="保存中..." class="delete" @click="minusPublishBookRow(index)">删除此项</van-button>
      </div>
    </div>
    <van-button v-if="publishBookList.length < 3" class="increase" @click="plusPublishBookRow">增加一项</van-button>
    <!-- 出版书籍结束 -->
    <!-- 论文开始- -->
    <div v-for="(item,index) in publishPaperList" :key="'PublishPaper'+index" class="center">
      <div class="center-top">
        <img src="../../assets/images/zou.png">
        发表论文
        <img src="../../assets/images/you.png">
      </div>
      <p class="center-tops">(推荐书写；若无相关信息，可不填)</p>
      <div class="app-flex">
        <div style="line-height: 1rem;">标题:</div>
        <van-field
          v-model="item.materialName"
          class="nameInput"
          maxlength="50"
          center
          placeholder="请输入论文标题"
        />
      </div>
      <div class="app-flex">
        <div style="line-height: 1rem;">参考网址:</div>
        <van-field
          v-model="item.publishSite"
          class="nameInput"
          center
          maxlength="200"
          placeholder="请输入参考网络"
        />
      </div>
      <div class="app-flex">
        <van-button class="Preservation" :loading="paperLoading" loading-text="保存中..." @click="addPublishPaper(index)">保存</van-button>
        <van-button v-if="index != 0" :loading="paperLoading" loading-text="删除中..." class="delete" @click="minusPublishPaperRow(index)">删除此项</van-button>
      </div>
    </div>
    <van-button v-if="publishPaperList.length < 3" class="increase" @click="plusPublishPaperRow">增加一项</van-button>
    <!-- 论文结束 -->
  </div>
</template>
<script>
import { listPublishBook, listPublishPaper, addPublishMaterial, deletePublishMaterial } from '@/api/material'
import { isPureChar } from '@/utils/validate'
export default {
  data() {
    return {
      consultantId: localStorage.getItem('CONSULTANT_ID'),
      loading: false,
      minDate: new Date(2000, 0, 1),
      maxDate: new Date(),
      currentDate: new Date(),
      publishBookList: [],
      publishBookIndex: 0,
      publishTimePicker: false,
      publishPaperList: [],
      publishPaperIndex: 0,
      paperLoading: false,
      bookLoading: false
    }
  },
  watch: {
    'publishBookList': {
      handler(newVal) {
        this.$emit('getPublishBookData', newVal)
      },
      deep: true,
      immediate: true
    },
    'publishPaperList': {
      handler(newVal) {
        this.$emit('getPublishPaperData', newVal)
      },
      deep: true,
      immediate: true
    }
  },
  created() {
    this.initPublishBookData()
    this.initPublishPaperData()
  },
  methods: {
    initPublishBookData() {
      listPublishBook(this.consultantId).then(r => {
        if (r.result.length > 0) {
          this.publishBookList = r.result
        } else {
          var publishBook = { id: 0, type: 1, consultantId: this.consultantId }
          this.publishBookList.push(publishBook)
        }
      })
    },
    initPublishPaperData() {
      listPublishPaper(this.consultantId).then(r => {
        if (r.result.length > 0) {
          this.publishPaperList = r.result
        } else {
          var publishPaper = { id: 0, type: 2, consultantId: this.consultantId }
          this.publishPaperList.push(publishPaper)
        }
      })
    },
    plusPublishBookRow() {
      var publishBook = { id: 0, type: 1, consultantId: this.consultantId }
      this.publishBookList.push(publishBook)
    },
    minusPublishBookRow(index) {
      if (this.publishBookList[index].id > 0) {
        deletePublishMaterial(this.publishBookList[index].id).then(r => {
        })
      }
      this.publishBookList.splice(index, 1)
    },
    publishTimePick(index) {
      this.publishBookIndex = index
      this.publishTimePicker = true
    },
    onPublishTimeConfirm(value) {
      this.publishBookList[this.publishBookIndex].publishTime = this.timeFormat(value)
      this.publishTimePicker = false
    },
    addPublishBook(index) {
      this.publishBookList[index].consultantId = this.consultantId
      this.bookLoading = true
      addPublishMaterial(this.publishBookList[index]).then(r => {
        if (r.success) {
          this.$toast.success('保存成功')
        } else {
          this.$notify(r.message)
        }
        this.bookLoading = false
      })
    },
    timeFormat(time) { // 时间格式化 2019-09-08
      const year = time.getFullYear()
      const month = time.getMonth() + 1
      return year + '-' + (month < 10 ? '0' + month : month)
    },
    plusPublishPaperRow() {
      var publishPaper = { id: 0, type: 2, consultantId: this.consultantId }
      this.publishPaperList.push(publishPaper)
    },
    minusPublishPaperRow(index) {
      if (this.publishPaperList[index].id > 0) {
        this.loading = true
        deletePublishMaterial(this.publishPaperList[index].id).then(r => {
          this.loading = false
        })
      }
      this.publishPaperList.splice(index, 1)
    },
    addPublishPaper(index) {
      if (this.publishPaperList[index].publishSite) {
        if (!isPureChar(this.publishPaperList[index].publishSite)) {
          this.$notify('网址格式不正确')
          return
        }
      }
      this.publishPaperList[index].consultantId = this.consultantId
      this.paperLoading = true
      addPublishMaterial(this.publishPaperList[index]).then(r => {
        if (r.success) {
          this.$toast.success('保存成功')
        } else {
          this.$notify(r.message)
        }
        this.paperLoading = false
      })
    }
  }
}
</script>
<style scoped>
@import "../../assets/public.css";
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
  background: url(../../assets/images/yes.png);
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
  background: url(../../assets/images/no.png);
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
  background: url(../../assets/images/t6.png) no-repeat right;
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
  background: url(../../assets/images/t6.png) no-repeat right;
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
  background-image: url(../../assets/images/jia.png);
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
</style>

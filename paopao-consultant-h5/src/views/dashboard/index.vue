<template>
  <div class="dome">
    <div class="cropper">
      <img :src="img" class="img">
      <!-- option是配置，格式是对象，getbase64Data是组件的一个方法获取裁剪完的头像 2.14新增一个获取getblobData的方法,有需要的自取 -->
      <H5Cropper :option="option" @imgorigoinf="imgorigoinf" @getbase64Data="getbase64Data" @getblobData="getblobData" />
    </div>
    <div class="info">
      <div></div>
      <div></div>
    </div>
  </div>
</template>

<script>
import { fileUploadUrl, baseUrl } from '@/data/config'
import H5Cropper from 'vue-cropper-h5'
import Axios from 'axios'
import { fileUploadUrl } from '@/data/config'
export default {
  components: { H5Cropper },
  data() {
    return {
      option: {}, // 配置
      fileName: '',
      fileBlob: undefined,
      img:
        baseUrl+'/group1/images/28f0cb520a0fa83efaff7aed1e97894.png'
    }
  },
  methods: {
    getbase64Data(data) {
      this.img = data
    },
    getblobData(data) {
      this.fileBlob = data
      const files = new window.File([this.fileBlob], this.fileName, { type: this.fileType })
      console.log(files)
      this.educationImageChange(files)
    },
    imgorigoinf(file) {
      this.fileName = file.name
      this.fileType = file.type
    },
    educationImageChange(files) {
      const file = files
      console.log(file)
      if (file) {
        if (file.size > 0 && file.size <= 5485760) { // 判断文件大小
          var reader = new FileReader()
          reader.onload = function(event) {
            console.log(event)
            // var txt = event.target.result
            // var img = document.getElementById('education')
            // img.src = txt
            // img.style.backgroundRepeat = 'no-repeat'
          }
        } else {
          this.$notify('图片不能小于0kb且不能大于5mb')
          return
        }
      }
      reader.readAsDataURL(file)
      const param = new FormData() // 创建form对象
      param.append('file', file) // 通过append向form对象添加数据
      param.append('output', 'json') // 添加form表单中其他数据
      param.append('path', '/image') // 添加form表单中其他数据
      param.append('scene', 'image') // 添加form表单中其他数据
      const config = {
        headers: { 'Content-Type': 'multipart/form-data' }
      } // 添加请求头
      Axios.post(fileUploadUrl, param, config).then(
        res => {
          console.log(res)
          this.img = res.data.url
          // this.$set(this.educationExperienceList[this.educationIndex], 'certificateUrl', res.data.path)
        }
      )
    }
  }
}
</script>

<style scoped>
.dome {
  display: flex;
  justify-content: space-between;
  padding-left: 22px;
}
.cropper {
  width: 80px;
  height: 80px;
  line-height: 80px;
  /* 切记position: relative一定要有 */
  position: relative;
  border-radius: 80px;
  overflow: hidden;
  text-align: center;
}
.img {
  position: absolute;
  width: 100%;
  height: 100%;
  left: 0;
  top: 0;
}
.info {
  font-size: 18px;
  height: 40px;
  line-height: 40px;
  margin-left: 30px;
  flex: 1;
  text-align: left;
}
</style>

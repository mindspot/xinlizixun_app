<template>
  <div id="download">
    <div class="download-bg">
      <div :style="{backgroundImage:'url('+require('../../assets/images/download-bg.png')+')'}" class="header" />
      <div :style="{backgroundImage:'url('+require('../../assets/images/logo.png')+')'}" class="logo" />
      <div class="download">
        <img src="../../assets/images/qr_download.png">
        <div class="btn-group">
          <a v-if="isApple" class="btn apple" href="#" @click="downloadIos">IOS App下载</a>
          <a v-if="!isApple" class="btn android" href="#" @click="downloadAndroid">Android App下载</a>
        </div>
      </div>
      <div class="copyright">©2011 - {{ year }}泡泡版权所有</div>
      <div v-if="isWeixin" class="weixin-tips">
        <p><img src="../../assets/images/live_weixin.png" alt="微信打开"></p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Download',
  data() {
    return {
      isWeixin: false,
      isWeixinText: navigator.userAgent,
      isApple: false,
      isAndroid: false,
      browser: {
        versions: (function() {
          var u = navigator.userAgent
          // var app = navigator.appVersion
          return {// 移动终端浏览器版本信息
            trident: u.indexOf('Trident') > -1, // IE内核
            presto: u.indexOf('Presto') > -1, // opera内核
            webKit: u.indexOf('AppleWebKit') > -1, // 苹果、谷歌内核
            gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') === -1, // 火狐内核
            mobile: !!u.match(/AppleWebKit.*Mobile.*/) || !!u.match(/AppleWebKit/), // 是否为移动终端
            ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), // ios终端
            android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, // android终端或者uc浏览器
            iPhone: u.indexOf('iPhone') > -1 || u.indexOf('Mac') > -1, // 是否为iPhone或者QQHD浏览器
            iPad: u.indexOf('iPad') > -1, // 是否iPad
            webApp: u.indexOf('Safari') === -1, // 是否web应该程序，没有头部与底部,
            weixin: u.indexOf('MicroMessenger') > -1,
            weibo: u.indexOf('WeiBo') > -1,
            qq: u.indexOf('QQ') > -1
          }
        }()),
        language: (navigator.browserLanguage || navigator.language).toLowerCase()
      },
      winHeight: typeof window.innerHeight !== 'undefined' ? window.innerHeight : document.documentElement.clientHeight,
      year: (new Date()).getFullYear()
    }
  },
  activated() {
    this.getBrowser()
  },
  methods: {
    downloadIos() {
      localStorage.setItem('app-downloaded', 'null')
      // window.location = 'https://itunes.apple.com/cn/app/huobs/id1510066804'
      window.location = 'itms-services://?action=download-manifest&url=https://api.huobs.com/image/app/apple/manifest.plist'
    },
    downloadAndroid() {
      localStorage.setItem('app-downloaded', 'null')
      window.location = 'https://api.huobs.com/image/app/android/huobs.apk'
    },
    getBrowser() {
      this.isWeixin = false
      this.isApple = false
      this.isAndroid = false
      if (this.browser.versions.weixin || this.browser.versions.qq || this.browser.versions.weibo) {
        this.isWeixin = true
      }
      if (this.browser.versions.ios || this.browser.versions.iPhone || this.browser.versions.iPad) {
        this.isApple = true
      }
      if (this.browser.versions.android || this.browser.versions.mobile) {
        this.isAndroid = true
      }
    }
  }
}
</script>

<style scoped>
  body {
    background-color: #ffffff !important;
  }
  /*.header {
    background-image: url(../../assets/images/download-bg.png);
  }*/
  .header {
    height: 135px;
    width: 100%;
    background-repeat: no-repeat;
    background-position: center;
    background-size: 100% 135px;
  }
  /*.logo {
    background-image: url(../../assets/images/logo.png);
  }*/
  .logo {
    width: 150px;
    height: 60px;
    margin: 0 auto;
    background-repeat: no-repeat;
    background-position: center;
    background-size: 150px 60px;
    overflow: hidden;
    text-indent: -999em;
    margin-top: 0px;
  }
  .download {
    margin: 0 auto;
    margin-top: 40px;
  }
  .download img {
    display: block;
    width: 141px;
    height: 195px;
    margin: 0 auto;
  }
  .download .btn-group {
    margin-top: 30px;
  }
  .download .btn {
    text-align: center;
    font-size: 14px;
    line-height: 3.2;
    color: #fff;
    background-repeat: no-repeat;
    background-position: 34px center;
    background-color: #333;
    display: block;
    margin: 0 auto;
    padding-left: 10%;
    border-radius: 6px;
    width: 60%;
  }
  .download .btn.apple {
    background-image: url(../../assets/images/apple.png);
  }
  .download .btn.android {
    background-image: url(../../assets/images/android.png);
  }
  .copyright {
    position: absolute;
    width: 100%;
    bottom: 20px;
    text-align: center;
    font-size: 14px;
    color: #666666;
  }
  .download-bg {
    position: fixed; left:0; top:0;
    width: 100%; height:100%;
    background-color: white;
    background-repeat: no-repeat;
    background-position-y:bottom;
  }
  .weixin-tips {
    position: fixed; left:0; top:0;
    background: rgba(0,0,0,0.8);
    filter:alpha(opacity=80);
    width: 100%; height:100%;
    z-index: 100;
  }
  .weixin-tips p {
    text-align: center;
    margin-top: 10%;
    padding:0 5%;
  }
  .weixin-tips img{
    width: 100%;
  }
</style>

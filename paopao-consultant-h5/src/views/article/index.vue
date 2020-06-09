<template>
  <div id="article" class="article-container">
    <div class="tittle">{{ article.articleVal }}</div>
    <span class="author">{{ article.author }}</span>
    <div class="article-detail" v-html="article.content" />
    <!-- <video
      id="myVideo"
      class="video-js"
    >
      <source
        src="http://vali.cp31.ott.cibntv.net.302.myalicdn.com/youku/677412564ed3a717fb3a95c24/03000801005E903EDE47A83003E880A6F4E933-9E1D-41B4-ABE8-083B79A95EBE.mp4?sid=158818622900010007989_00_Bd7215010c909f276bda93b078266e580&sign=0eb1952c000d8dbded4fc996b6987692&ctype=50&ali_redirect_domain=vali.cp31.ott.cibntv.net&ali_redirect_ex_ftag=c8275fd272bda22ac87efae4bda1e25881e930682836c79d&ali_redirect_ex_tmining_ts=1588186286&ali_redirect_ex_tmining_expire=3600&ali_redirect_ex_hot=0"
        type="video/mp4"
      >
    </video> -->
  </div>
</template>
<script>
import { getArticle } from '@/api/option'
export default {
  data() {
    return {
      article: {}
    }
  },
  mounted() {
    this.getArticle()
    // this.initVideo()
  },
  methods: {
    initVideo() {
      // 初始化视频方法
      this.$video('myVideo', {
        // 确定播放器是否具有用户可以与之交互的控件。没有控件，启动视频播放的唯一方法是使用autoplay属性或通过Player API。
        controls: true,
        // 自动播放属性,muted:静音播放
        autoplay: 'muted',
        // 建议浏览器是否应在<video>加载元素后立即开始下载视频数据。
        preload: 'auto',
        // 设置视频播放器的显示宽度（以像素为单位）
        width: '800px',
        // 设置视频播放器的显示高度（以像素为单位）
        height: '400px'
      })
    },
    getArticle() {
      var articleId = this.$route.params.id
      if (!articleId) {
        return
      }
      getArticle(articleId)
        .then(r => {
          this.article = r.result
        })
        .catch(err => {
          console.log(err)
        })
    }
  }
}
</script>
<style scoped>
.article-container {
  margin: .3rem .4rem;
  width: 6.9rem;
  padding: 0.08rem 0;
}
.tittle {
  text-align: center;
  font-size: 20px;
  padding:6px 6px
}
.author {
  font-size: 13px;
  color: #999999;
  position: absolute;
  right: 10%;
}
.article-detail {
  margin: 0.3rem 0;
  font-size: 14px;
  color: #333333;
  line-height: 28px;
  padding:8px 8px
}
</style>

<template>
  <div class="app-container">
    <el-tabs v-model="activeName">
      <el-tab-pane label="发布信息" name="first">
        <el-form ref="postForm" :model="postForm" :rules="rules" class="form-container">
          <div class="createPost-main-container" style="width:500px">
            <el-form-item label-width="100px" label="作者:" class="postInfo-container-item">
              <el-input v-model="postForm.author" />
            </el-form-item>
            <el-form-item label-width="100px" label="文章分类:" class="postInfo-container-item">
              <el-select v-model="postForm.commonId" placeholder="请选择文章分类">
                <el-option
                  v-for="item in categoryList"
                  :key="item.id"
                  :label="item.val"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>
            <el-form-item label-width="100px" label="排序:" class="postInfo-container-item">
              <el-input-number v-model="postForm.sort" />
            </el-form-item>
          </div>
          <el-form-item prop="image_uri" label-width="100px" label="标题图片:" style="margin-bottom: 30px;">
            <Upload v-model="postForm.articleImg" />
          </el-form-item>
          <el-form-item>
            <el-row type="flex" justify="center" class="mt-10">
              <el-button :loading="loading" type="primary" @click="submitForm">
                直接发布
              </el-button>
            </el-row>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="文章正文" name="second">
        <el-form ref="postForm" :model="postForm" :rules="rules">
          <el-form-item label-width="100px" label="大标题:">
            <el-input v-model="postForm.articleVal" type="textarea" autosize placeholder="请输入标题" />
          </el-form-item>
          <el-form-item style="margin-bottom: 10px;" label-width="100px" label="小标题:">
            <el-input v-model="postForm.articleTitle" placeholder="请输入摘要" />
          </el-form-item>
          <el-form-item prop="content" style="margin-bottom: 30px;">
            <Tinymce ref="editor" v-model="postForm.content" :height="280" />
          </el-form-item>
          <el-form-item>
            <el-row type="flex" justify="center" class="mt-10">
              <el-button :loading="loading" type="primary" @click="submitForm">
                直接发布
              </el-button>
            </el-row>
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script>
import Tinymce from '@/components/Tinymce'
import Upload from '@/components/Upload/SingleImage3'
import { addArticle, fetchArticle, listAllCategory } from '@/api/article'
// import { searchUser } from '@/api/remote-search'

const defaultForm = {
  title: '', // 文章题目
  content: '', // 文章内容
  brief: '', // 文章摘要
  source_uri: '', // 文章外链
  image_uri: '', // 文章图片
  publishDt: undefined, // 前台展示时间
  id: undefined,
  platforms: ['a-platform'],
  comment_disabled: false,
  sort: 10,
  commonId: undefined
}

export default {
  name: 'ArticleDetail',
  components: { Tinymce, Upload },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    const validateRequire = (rule, value, callback) => {
      if (value === '') {
        console.log(rule)
        callback(new Error(rule.field + '为必传项'))
      } else {
        callback()
      }
    }
    return {
      activeName: 'first',
      postForm: Object.assign({}, defaultForm),
      loading: false,
      categoryList: [],
      rules: {
        brief: [{ validator: validateRequire }],
        title: [{ validator: validateRequire }],
        content: [{ validator: validateRequire }]
      },
      tempRoute: {}
    }
  },
  computed: {
    displayTime: {
      get() {
        return (+new Date(this.postForm.publishDt))
      },
      set(val) {
        this.postForm.publishDt = new Date(val)
      }
    }
  },
  created() {
    if (this.isEdit) {
      const id = this.$route.params && this.$route.params.id
      this.fetchData(id)
    } else {
      this.postForm = Object.assign({}, defaultForm)
    }
    this.tempRoute = Object.assign({}, this.$route)
    listAllCategory().then(r => {
      this.categoryList = r.result
    })
  },
  methods: {
    fetchData(id) {
      fetchArticle(id).then(r => {
        this.postForm = r.result
      }).catch(err => {
        console.log(err)
      })
    },
    submitForm() {
      if (this.postForm.commonId === undefined || this.postForm.commonId === 0) {
        this.$message({
          title: '错误',
          message: '请选择文章分类',
          type: 'success',
          duration: 2000
        })
        return
      }

      this.$refs.postForm.validate(valid => {
        if (valid) {
          this.loading = true
          console.log(this.postForm)
          addArticle(this.postForm).then(r => {
            if (this.isEdit) {
              this.$message({
                title: '成功',
                message: '修改文章成功',
                type: 'success',
                duration: 2000
              })
            } else {
              this.$message({
                title: '成功',
                message: '发布文章成功',
                type: 'success',
                duration: 2000
              })
              this.postForm = Object.assign({}, defaultForm)
              this.$refs.editor.setContent('')
              this.postForm.content = ''
              this.$refs.postForm.resetFields()
            }
            this.loading = false
          })
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import "~@/styles/mixin.scss";

.createPost-container {
  position: relative;

  .createPost-main-container {
    padding: 40px 45px 20px 50px;

    .postInfo-container {
      position: relative;
      @include clearfix;
      margin-bottom: 10px;

      .postInfo-container-item {
        float: left;
      }
    }
  }

  .word-counter {
    width: 40px;
    position: absolute;
    right: 10px;
    top: 0px;
  }
}

.article-textarea /deep/ {
  textarea {
    padding-right: 40px;
    resize: none;
    border: none;
    border-radius: 0px;
    border-bottom: 1px solid #bfcbd9;
  }
}
</style>

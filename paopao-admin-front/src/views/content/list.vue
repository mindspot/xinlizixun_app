<template>
  <div class="app-container">
    <el-row type="flex" justify="right" style="padding-bottom:5px; ">
      <el-col :span="16">
        <el-button-group>
          <el-button
            size="mini"
            type="primary"
            icon="el-icon-plus"
            @click="handleAddClick"
          >新增文章
          </el-button>
          <el-upload
            :data="excelPostData"
            :action="uploadUrl"
            :headers="myHeaders"
            :show-file-list="false"
            :on-success="handleFileSuccess"
            :before-upload="beforeUpload"
            class="fl"
          >
            <el-button icon="el-icon-upload2" size="mini" type="primary">批量导入</el-button>
          </el-upload>
          <el-button icon="el-icon-link" size="mini" round type="primary">
            <a style="color: #ffffff;" :href="downloadUrl" download="" title="模板下载">模板下载</a>
          </el-button>
        </el-button-group>
      </el-col>
      <el-col :span="8">
        <el-row type="flex" justify="end">
          <!-- <CategorySelect @getSelectedCategory="getSelectedCategory" /> -->
          <el-input v-model="listQuery.title" style="width:160px;margin-left:10px" placeholder="根据标题查询" clearable />
          <el-button icon="el-icon-search" type="primary" @click="getList" />
        </el-row>
      </el-col>
    </el-row>
    <el-table :data="list" border fit highlight-current-row style="width: 100%">
      <el-table-column type="selection" align="center" width="42" />
      <el-table-column type="index" label="#" align="center" width="42" />
      <el-table-column width="120" align="center" label="文章分类">
        <template slot-scope="scope">
          <span>{{ scope.row.common.val }}</span>
        </template>
      </el-table-column>
      <el-table-column width="120" align="center" label="文章标题">
        <template slot-scope="scope">
          <span>{{ scope.row.articleVal }}</span>
        </template>
      </el-table-column>
      <el-table-column width="120" align="center" label="文章小标题">
        <template slot-scope="scope">
          <span>{{ scope.row.articleTitle }}</span>
        </template>
      </el-table-column>
      <el-table-column width="120" align="center" label="作者">
        <template slot-scope="scope">
          <span>{{ scope.row.author }}</span>
        </template>
      </el-table-column>
      <el-table-column width="180" prop="insertTime" align="center" label="发布时间" :formatter="dateFormat" />
      <el-table-column width="100" label="排序信息">
        <template slot-scope="scope">
          <span>{{ scope.row.sort }}</span>
        </template>
      </el-table-column>
      <el-table-column width="100" prop="browseVolume" label="浏览次数" />
      <el-table-column prop="articleLink" label="文章链接" show-overflow-tooltip />
      <el-table-column align="center" fixed="right" label="操作" width="180">
        <template slot-scope="scope">
          <router-link :to="'/article-manage/edit-article/'+scope.row.id">
            <el-button plain size="small" icon="el-icon-edit">
              编辑
            </el-button>
          </router-link>
          <el-button plain size="small" @click="handleRemoveClick(scope.row)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-row type="flex" justify="end">
      <el-pagination background :current-page="currentPage" :page-sizes="[10, 50, 100, 200]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </el-row>
  </div>
</template>

<script>
import { listArticle, deleteArticle } from '@/api/article'
import { excelUploadUrl, excelDownloadUrl } from '@/data/config'
import { getToken } from '@/utils/auth' // 验权
import moment from 'moment'
export default {
  name: 'ArticleList',
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      uploadUrl: excelUploadUrl + '/article',
      downloadUrl: excelDownloadUrl + '/1',
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        oneCategoryId: '',
        twoCategoryId: '',
        pageNo: 1,
        pageSize: 10
      },
      currentPage: 1,
      pageSize: 10
    }
  },
  computed: {
    excelPostData: function() {
      return {
        userId: 1
      }
    },
    myHeaders: function() {
      return {
        'X-Token': getToken()
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    handleAddClick() {
      this.$router.push('/article-manage/create-article')
    },
    getSelectedCategory(oneLevelCategoryId, twoLevelCategoryId) {
      this.listQuery.oneCategoryId = oneLevelCategoryId
      this.listQuery.twoCategoryId = twoLevelCategoryId
    },
    getData() {
      this.listLoading = true
      this.listQuery.pageNo = this.currentPage
      listArticle(this.listQuery).then(r => {
        this.list = r.result.list
        this.total = r.result.total
        this.listLoading = false
      })
    },
    getList() {
      this.listLoading = true
      listArticle(this.listQuery).then(r => {
        this.list = r.result.list
        this.total = r.result.total
        this.listLoading = false
      })
    },
    removeData(id) {
      deleteArticle(id).then(r => {
        this.getData()
        this.$message({
          message: '删除成功',
          type: 'success'
        })
      })
    },
    handleSizeChange(val) {
      this.currentPage = val
      this.getData()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.getData()
    },
    handleFileSuccess(res, file) {
      if (res.success) {
        this.open('导入成功')
        this.getData()
      } else {
        this.open(res.result[0].errorMessage)
      }
    },
    open(message) {
      this.$alert(message, '导入结果', {
        confirmButtonText: '确定'
      })
    },
    beforeUpload(file) {
      const isExcelFile = this.isExcel(file)
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isExcelFile) {
        this.$message.error('文件只能是 excel 格式!')
      }
      if (!isLt2M) {
        this.$message.error('上传文件大小不能超过 2MB!')
      }
      return isExcelFile && isLt2M
    },
    isExcel(file) {
      return /\.(xlsx|xls|csv)$/.test(file.name)
    },
    dateFormat: function(row, column) {
      var date = row[column.property]
      if (!date) {
        return ''
      }
      return moment(date).format('YYYY-MM-DD HH:ss')
    },
    handleRemoveClick(val) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.object = val
        delete this.object.children
        delete this.object.parent
        this.removeData(this.object.id)
      })
    }
  }
}
</script>

<style scoped>
.edit-input {
  padding-right: 100px;
}
.cancel-btn {
  position: absolute;
  right: 15px;
  top: 10px;
}
.fl {
  float: left;
}
</style>

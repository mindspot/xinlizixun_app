<template>
  <div class="app-container">
    <el-row type="flex" justify="right" style="padding-bottom:5px; ">
      <el-col :span="8">
        <el-button
          size="mini"
          type="primary"
          icon="el-icon-plus"
          @click="handleAddClick"
        >新增
        </el-button>
      </el-col>
      <el-col :span="16">
        <el-row type="flex" justify="end">
          <!-- <el-button icon="fa fa-search" type="primary" @click="getData" /> -->
        </el-row>
      </el-col>
    </el-row>
    <el-table
      :data="objectList"
      style="width: 100%;"
      border
    >
      <el-table-column type="selection" align="center" width="42" />
      <el-table-column
        type="index"
        label="#"
        width="42"
      />
      <el-table-column
        prop="val"
        label="分类名称"
        width="144"
      />
      <el-table-column prop="icon" align="center" label="图片" width="480">
        <!-- 图片的显示 -->
        <template slot-scope="scope">
          <el-image
            style="width: 100px; height: 100px"
            :src="scope.row.icon"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="sort"
        label="排序编号"
        sortable
        align="center"
        width="104"
      />
      <el-table-column prop="insertTime" align="center" label="创建时间" width="164" :formatter="dateTimeFormat" />
      <el-table-column
        prop="remarks"
        label="备注"
      />
      <el-table-column label="操作" align="center" fixed="right" width="148">
        <template slot-scope="scope">
          <el-button-group>
            <el-button size="mini" plain @click="handleEditClick(scope.row)">编辑</el-button>
            <el-button size="mini" plain @click="handleRemoveClick(scope.row)">删除</el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
    <el-row type="flex" justify="end">
      <el-pagination background :current-page="listQuery.pageNo" :page-sizes="[10, 50, 100, 200]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </el-row>
    <el-dialog title="分类" :visible.sync="dialogVisible" :close-on-click-modal="false" width="500px">
      <el-form ref="menu" :model="object" label-width="80px" class="form-item-normal">
        <el-form-item label="分类名称">
          <el-input v-model="object.val" />
        </el-form-item>
        <el-row>
          <el-col :span="8">
            <el-upload
              class="upload"
              :action="uploadUrl"
              :on-success="uploadSuccess"
              :limit="1"
              :data="uploadDate"
            >
              <el-button size="mini" type="primary">点击上传</el-button>
            </el-upload>
          </el-col>
        </el-row>
        <el-form-item label="排序号">
          <el-input-number v-model="object.sort" :min="0" label="数字越大优先级越高" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button v-if="actionType ==='ADD'" :lading="loadingTags.add" type="primary" @click="addData">确 定</el-button>
        <el-button v-else type="primary" :lading="loadingTags.edit" @click="modifyData">修 改</el-button>
      </span>
    </el-dialog>
  </div>
</template>
<script>
import { listCategory, addArticleCategory, updateArticleCategory, deleteArticleCategory } from '@/api/article'
import { mapGetters } from 'vuex'
import { deepClone } from '@/utils/transferUtil'
import moment from 'moment'
import { fileUploadUrl } from '@/data/config'
export default {
  name: 'Category',
  filters: {
    statusTagFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  props: {
    edit: {
      type: Boolean,
      default: false
    },
    delete: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      total: 0,
      listQuery: {
        pageNo: 1,
        pageSize: 10
      },
      loading: false,
      ifShow: true,
      roleTree: [],
      menuButtonOptionTree: [],
      defaultProps: {
        children: 'children',
        label: 'title'
      },
      dialogVisible: false,
      loadingTags: {
        add: false,
        edit: false
      },
      uploadUrl: fileUploadUrl,
      uploadDate: {
        output: 'json',
        path: 'images',
        scene: 'image'
      },
      formLabelWidth: '80px',
      // 选项卡对应变量
      activeName: 'first',
      object: {
        val: '',
        type: 2,
        icon: ''
      },
      objectList: [],
      actionType: ''
    }
  },
  computed: {
    ...mapGetters([
      'userName',
      'userId',
      'refId'
    ])
  },
  created() {
    this.getData()
  },
  methods: {
    getData() {
      this.listQuery.ifPage = true
      listCategory(this.listQuery).then(r => {
        this.objectList = r.result.list
        this.total = r.result.total
      })
    },
    handleSizeChange(val) {
      this.listQuery.pageSize = val
      this.getData()
    },
    handleCurrentChange(val) {
      this.listQuery.pageNo = val
      this.getData()
    },
    removeData(id) {
      deleteArticleCategory(id).then(r => {
        this.getData()
        this.$message({
          message: '删除成功',
          type: 'success'
        })
      })
    },
    handleEditClick(val) {
      this.loadingTags.edit = false
      this.dialogVisible = true
      this.object = deepClone(val)
      this.actionType = 'EDIT'
    },
    modifyData() {
      this.loadingTags.edit = true
      updateArticleCategory(this.object).then(r => {
        this.dialogVisible = false
        this.$message({
          message: '修改成功',
          type: 'success'
        })
        this.getData()
      }).catch(() => {
        this.loadingTags.edit = false
        this.$message({
          message: '修改失败',
          type: 'error'
        })
      })
    },
    handleAddClick() {
      this.loadingTags.add = false
      this.dialogVisible = true
      this.object = { status: 1, sort: 0, level: 1 }
      this.actionType = 'ADD'
    },
    addData() {
      this.loadingTags.add = true
      addArticleCategory(this.object).then(r => {
        this.dialogVisible = false
        this.$message({
          message: '创建成功',
          type: 'success'
        })
        this.getData()
      }).catch(() => {
        this.loadingTags.add = false
        this.$message({
          message: '创建失败',
          type: 'error'
        })
      })
    },
    handleRemoveClickhandleRemoveClick(val) {
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
    },
    dateTimeFormat: function(row, column) {
      var date = row[column.property]
      if (!date) {
        return ''
      }
      return moment(date).format('YYYY-MM-DD HH:mm:ss')
    },
    uploadSuccess(response, file, fileList) {
      if (response.path !== undefined) {
        this.object.icon = response.path
      }
    }
  }
}
</script>

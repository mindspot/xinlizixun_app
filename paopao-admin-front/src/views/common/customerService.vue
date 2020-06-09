<template>
  <div class="app-container">
    <el-row type="flex" justify="right" style="padding-bottom:5px ">
      <el-col :span="8">
        <el-button-group>
          <el-button  size="mini" type="primary" icon="el-icon-plus" round @click="handleAddClick">新增</el-button>
          <!-- <el-button v-show="this.delete" size="mini" type="danger" icon="el-icon-delete" round @click="handleDeleteClick">删除</el-button> -->
        </el-button-group>
      </el-col>
      <el-col :span="16">
        <el-row type="flex" justify="end" />
      </el-col>
    </el-row>
    <el-table ref="multipleTable" :data="objectList" tooltip-effect="dark" style="width: 100%" border fit highlight-current-row @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="42" />
      <el-table-column type="index" align="center" label="#" width="42" />

      <el-table-column prop="valCode" label="身份" width="72">
        <template slot-scope="scope">
          <span v-if="scope.row.valCode === 0">咨询师客服</span>
          <span v-if="scope.row.valCode === 1">用户客服</span>
          <span v-if="scope.row.valCode === 2">咨询师在线客服</span>
          <span v-if="scope.row.valCode === 3">用户在线客服</span>
        </template>
      </el-table-column>
      <el-table-column prop="val" align="center" label="账号" width="148" />
      <el-table-column prop="sort" align="center" label="排序" width="148" />
      <el-table-column align="center" label="操作" width="148">
        <template slot-scope="scope">
          <el-button-group>
            <el-button size="mini" plain @click="handleEditClick(scope.row)">编辑</el-button>
            <el-button size="mini" plain @click="handleRemoveClick(scope.row)">删除</el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
    <el-row type="flex" justify="end" style="padding:5px 0; ">
      <el-pagination background :current-page="currentPage" :page-sizes="[10, 50, 100, 200]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </el-row>
    <el-dialog title="信息" :visible.sync="dialogFormVisible" :close-on-press-escape="false" :close-on-click-modal="false" top="15vh" width="48%">
      <el-form ref="dataForm" :model="object" :label-width="formLabelWidth" :disabled="ifView">
        <el-row>
          <el-col :span="12"  style="padding:15px;">
            <el-form-item label="账号" prop="buttonNo">
              <el-input v-model="object.val" auto-complete="off" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="8"  style="padding:15px;">
            <el-form-item label="序号" prop="buttonNo">
              <el-input-number v-model="object.sort" :min="1" auto-complete="off" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="16">
            <el-form-item>
              <template>
                <el-radio v-model="radio" label="0">咨询师客服</el-radio>
                <el-radio v-model="radio" label="1">用户客服</el-radio>
                <el-radio v-model="radio" label="2">咨询师在线客服</el-radio>
                <el-radio v-model="radio" label="3">用户在线客服</el-radio>
              </template>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" :loading="loadingTags.add" @click="createData">确 定</el-button>
        <el-button v-else type="primary" :loading="loadingTags.edit" @click="modifyData">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { commonList, addCommon, deleteCommon, updateCommon } from '@/api/common'
import { deepClone } from '@/utils/transferUtil'
import { fileUploadUrl } from '@/data/config'
export default {
  name: 'ButtonList',
  props: {
    add: {
      type: Boolean,
      default: false
    },
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
      objectList: [],
      multipleSelection: [],
      systemOptions: [],
      currentPage: 1,
      total: 0,
      pageSize: 10,
      dialogFormVisible: false,
      object: {
        val: '',
        sort: 0
      },
      formLabelWidth: '80px',
      ifView: false,
      ids: [],
      dialogStatus: '',
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
      param: {
        pageNo: 1,
        pageSize: 10,
        type: 4001
      },
      radio: '0'
    }
  },
  watch: {
    multipleSelection: function() {
      const arr = []
      for (const i in this.multipleSelection) {
        arr.push(this.multipleSelection[i].id)
      }
      this.ids = arr.join()
    }
  },
  created() {
    this.getData()
  },
  methods: {
    getData() {
      this.param.pageNo = this.currentPage
      commonList(this.param).then(r => {
        this.objectList = r.result.list
        this.total = r.result.total
        this.currentPage = r.result.pageNum
      })
    },
    handleAddClick() {
      this.loadingTags.add = false
      this.dialogFormVisible = true
      this.object = { type: this.param.type, sort: 10, valCode: this.radio }
      this.ifView = false
      this.dialogStatus = 'create'
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.loadingTags.add = true
          this.object.valCode = this.radio
          addCommon(this.object).then(() => {
            this.dialogFormVisible = false
            this.getData()
            this.$message({
              message: '创建成功',
              type: 'success'
            })
          })
        }
      })
    },
    handleEditClick(val) {
      this.loadingTags.edit = false
      this.dialogFormVisible = true
      this.object = deepClone(val)
      this.ifView = false
      this.dialogStatus = 'edit'
    },
    modifyData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.loadingTags.edit = true
          this.object.valCode = this.radio
          updateCommon(this.object).then(() => {
            this.dialogFormVisible = false
            this.getData()
            this.$message({
              message: '修改成功',
              type: 'success'
            })
          })
        }
      })
    },
    removeData(id) {
      deleteCommon(id).then(r => {
        this.getData()
        this.$message({
          message: '删除成功',
          type: 'success'
        })
      })
    },
    deleteRows() {
      if (this.ids.length > 0) {
        this.removeData()
      } else {
        this.$message({
          message: '请选择你需要删除的数据',
          type: 'warning'
        })
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.getData()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.getData()
    },
    handleDeleteClick() {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRows()
      })
    },
    uploadSuccess(response, file, fileList) {
      if (response.path !== undefined) {
        this.object.val = response.path
      }
    },
    bannerChange() {

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

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
        prop="customerName"
        label="客户名称"
        width="144"
      />
      <el-table-column
        prop="channel"
        label="渠道"
        width="144"
      />
      <el-table-column
        prop="sex"
        label="性别"
        width="144"
      />
      <el-table-column
        prop="age"
        label="年龄"
        width="144"
      />
      <el-table-column
        prop="phone"
        label="手机号"
        width="144"
      />
      <el-table-column
        prop="wx"
        label="微信号"
        width="144"
      />
      <el-table-column
        prop="qq"
        label="QQ号"
        width="144"
      />
      <el-table-column
        prop="userId"
        label="客户ID"
        width="144"
      />
      
      <el-table-column
        prop="numberOfContacts"
        label="联系次数"
        width="144"
      />
      <el-table-column
        prop="numberOfContacts"
        label="当前负责人"
        width="144"
      />
      <el-table-column label="操作" align="center" fixed="right" width="148">
        <template slot-scope="scope">
          <el-button-group>
            <el-button size="mini" plain @click="handleEditClick(scope.row)">编辑</el-button>
            <!-- <el-button size="mini" plain @click="handleRemoveClick(scope.row)">删除</el-button> -->
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
    <el-row type="flex" justify="end">
      <el-pagination background :current-page="listQuery.pageNo" :page-sizes="[10, 50, 100, 200]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </el-row>
    <el-dialog title="派单" :visible.sync="dialogVisible" :close-on-click-modal="false" width="500px">
      <el-form ref="dataForm" :model="object" label-width="80px" class="form-item-normal">
        <el-form-item label="渠道">
          <el-input v-model="object.channel" />
        </el-form-item>
        <el-form-item label="客户名称">
          <el-input v-model="object.customerName" />
        </el-form-item>
        <el-form-item label="性别">
          <el-input v-model="object.sex" />
        </el-form-item>
        <el-form-item label="年龄">
          <el-input v-model="object.age" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="object.phone" />
        </el-form-item>
        <el-form-item label="微信号">
          <el-input v-model="object.wx" />
        </el-form-item>
        <el-form-item label="QQ号">
          <el-input v-model="object.qq" />
        </el-form-item>
        <el-form-item label="客户ID">
          <el-input v-model="object.userId" />
        </el-form-item>
        <el-form-item label="当前负责人">
          <el-input v-model="object.val" />
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
import { dispatchList, addDispatch } from '@/api/dispatch'
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
      object: {},
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
      dispatchList(this.listQuery).then(r => {
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

    },
    handleEditClick(val) {
      this.loadingTags.edit = false
      this.dialogVisible = true
      this.object = deepClone(val)
      this.actionType = 'EDIT'
    },
    modifyData() {
      this.loadingTags.edit = true
    },
    handleAddClick() {
      this.loadingTags.add = false
      this.dialogVisible = true
      this.object = {}
      this.actionType = 'ADD'
    },
    addData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.loadingTags.add = true
          addDispatch(this.object).then(() => {
            this.dialogVisible = false
            this.getData()
            this.$message({
              message: '创建成功',
              type: 'success'
            })
          })
        }
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

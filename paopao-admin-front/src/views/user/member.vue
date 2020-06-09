<template>
  <div class="app-container">
    <el-row type="flex" justify="right" style="padding-bottom:5px ">
      <el-col :span="8">
        <el-button-group>
          <el-button size="mini" type="primary" icon="el-icon-plus" round @click="handleAddClick">新增</el-button>
          <!-- <el-button v-show="this.delete" size="mini" type="danger" icon="el-icon-delete" round @click="handleDeleteClick">删除</el-button> -->
        </el-button-group>
      </el-col>
      <el-col :span="16">
        <el-row type="flex" justify="end">
          <el-input v-model="param.userName" style="width:160px;margin-left:10px" placeholder="根据会员账号查询" clearable />
          <el-input v-model="param.realName" style="width:160px;margin-left:10px" placeholder="根据会员昵称查询" clearable />
          <el-button icon="el-icon-search" type="primary" @click="getData" />
        </el-row>
      </el-col>
    </el-row>
    <el-table ref="multipleTable" :data="objectList" tooltip-effect="dark" style="width: 100%" border fit highlight-current-row @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="42" />
      <el-table-column type="index" label="#" align="center" width="42" />
      <el-table-column prop="userName" align="center" label="登录账号" width="120" />
      <el-table-column prop="realName" align="center" label="姓名" width="164" />
      <el-table-column prop="sex" align="center" label="性别" width="72">
        <template slot-scope="scope">
          <span v-if="scope.row.sex === 0">男</span>
          <span v-if="scope.row.sex === 1">女</span>
        </template>
      </el-table-column>
      <el-table-column prop="maritalStatus" align="center" label="婚姻状态" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.maritalStatus === 0">未婚</span>
          <span v-if="scope.row.maritalStatus === 1">已婚</span>
          <span v-if="scope.row.maritalStatus === 2">恋爱中</span>
        </template>
      </el-table-column>
      <el-table-column prop="status" align="center" label="状态" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.status === 0">审核</span>
          <span v-if="scope.row.status === 1">禁止</span>
          <span v-if="scope.row.status === 2">正常</span>
        </template>
      </el-table-column>
      <el-table-column prop="age" align="center" label="年龄" width="72" />
      <el-table-column prop="occupation" align="center" label="职业" width="144" />
      <el-table-column prop="insertTime" align="center" label="创建时间" width="164" :formatter="dateTimeFormat" />
      <el-table-column align="center" label="备注" />
      <el-table-column align="center" label="操作" width="84">
        <template slot-scope="scope">
          <el-button plain size="mini" @click="handleEditClick(scope.row)">审核</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-row type="flex" justify="end" style="padding:5px 0; ">
      <el-pagination background :current-page="currentPage" :page-sizes="[10, 50, 100, 200]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </el-row>
    <el-dialog title="信息" :visible.sync="dialogFormVisible" :close-on-press-escape="false" :close-on-click-modal="false" top="15vh" width="48%">
      <el-form ref="dataForm" :model="object" :label-width="formLabelWidth" :disabled="ifView">
        <el-row>
          <el-col :span="8">
            <el-form-item label="手机号码">
              <el-input v-model="object.userName" auto-complete="off" />
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
import { listMember, addMember, deleteMember, updateMember } from '@/api/member'
import { deepClone } from '@/utils/transferUtil'
import moment from 'moment'
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
        userName: ''
      },
      formLabelWidth: '80px',
      ifView: false,
      ids: [],
      dialogStatus: '',
      loadingTags: {
        add: false,
        edit: false
      },
      param: {
        pageNo: 1,
        pageSize: 10,
        userName: '',
        realName: ''
      }
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
      listMember(this.param).then(r => {
        this.objectList = r.result.list
        this.total = r.result.total
        this.currentPage = r.result.pageNum
      })
    },
    handleAddClick() {
      this.loadingTags.add = false
      this.dialogFormVisible = true
      this.object = { status: 1, sortNo: 10 }
      this.ifView = false
      this.dialogStatus = 'create'
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.loadingTags.add = true
          addMember(this.object).then(() => {
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
          updateMember(this.object).then(() => {
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
    removeData() {
      deleteMember(this.ids).then(() => {
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
    dateTimeFormat: function(row, column) {
      var date = row[column.property]
      if (!date) {
        return ''
      }
      return moment(date).format('YYYY-MM-DD HH:mm:ss')
    }
  }
}
</script>

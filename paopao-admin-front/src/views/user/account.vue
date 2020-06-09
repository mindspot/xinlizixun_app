<template>
  <div class="app-container">
    <el-row type="flex" justify="right" style="padding-bottom:5px ">
      <el-col :span="8">
        <!-- <el-button-group>
          <el-button v-show="add" size="mini" type="primary" icon="el-icon-plus" round @click="handleAddClick">新增</el-button>
          <el-button v-show="this.delete" size="mini" type="danger" icon="el-icon-delete" round @click="handleDeleteClick">删除</el-button>
        </el-button-group> -->
      </el-col>
      <el-col :span="16">
        <el-row type="flex" justify="end">
          <el-input v-model="param.userName" style="width:160px;margin-left:10px" placeholder="根据咨询师账号查询" clearable />
          <el-input v-model="param.realName" style="width:160px;margin-left:10px" placeholder="根据咨询师昵称查询" clearable />
          <el-button icon="el-icon-search" type="primary" @click="getData" />
        </el-row>
      </el-col>
    </el-row>
    <el-table ref="multipleTable" :data="objectList" tooltip-effect="dark" style="width: 100%" border fit highlight-current-row @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="42" />
      <el-table-column type="index" align="center" label="#" width="42" />
      <el-table-column prop="status" align="center" label="账号状态" width="74">
        <template slot-scope="scope">
          <span v-if="scope.row.status === 0">等待审核</span>
          <span v-if="scope.row.status === 1">限制登录</span>
          <span v-if="scope.row.status === 2">正常</span>
        </template>
      </el-table-column>
      <el-table-column prop="type" align="center" label="账号类型" width="74">
        <template slot-scope="scope">
          <span v-if="scope.row.type === 0">咨询师</span>
          <span v-if="scope.row.type === 1">客户</span>
          <span v-if="scope.row.type === 2">管理员</span>
          <span v-if="scope.row.type === 4">文章管理</span>
        </template>
      </el-table-column>
      <el-table-column prop="userName" align="center" label="登录账号" />
      <el-table-column prop="realName" align="center" label="姓名" />
      <el-table-column prop="sex" align="center" label="性别" width="72">
        <template slot-scope="scope">
          <span v-if="scope.row.sex === 0">男</span>
          <span v-if="scope.row.sex === 1">女</span>
        </template>
      </el-table-column>
      <el-table-column prop="age" align="center" label="年龄" width="72" />
      <el-table-column prop="account" align="center" label="账户余额" width="120">
        <template slot-scope="scope">
          {{ scope.row.account/100 }}
        </template>
      </el-table-column>
      <el-table-column prop="cashWithdrawalAmount" align="center" label="提现金额" width="120">
        <template slot-scope="scope">
          {{ scope.row.account/100 }}
        </template>
      </el-table-column>
      <el-table-column prop="insertTime" align="center" label="注册时间" width="164" :formatter="dateTimeFormat" />
      <el-table-column fixed="right" align="center" label="操作" width="120">
        <template slot-scope="scope">
          <el-button-group>
            <el-button v-if="scope.row.status == 2" plain @click="handleUpdateClick(scope.row.id,1)">限制登入</el-button>
            <el-button v-if="scope.row.status != 2" plain @click="handleUpdateClick(scope.row.id,2)">恢复登入</el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
    <el-row type="flex" justify="end" style="padding:5px 0; ">
      <el-pagination background :current-page="currentPage" :page-sizes="[10, 50, 100, 200]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </el-row>
  </div>
</template>
<script>
import { listUser, updateUserStatus } from '@/api/user'
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
      replaceDialogVisible: false,
      objectList: [],
      multipleSelection: [],
      systemOptions: [],
      currentPage: 1,
      total: 0,
      pageSize: 10,
      dialogFormVisible: false,
      object: {
        buttonNo: '',
        buttonName: '',
        userType: ''
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
        realName: '',
        userType: 1,
        invitationCode: ''
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
      listUser(this.param).then(r => {
        this.objectList = r.result.list
        this.total = r.result.total
      })
    },
    handleAddClick() {
      this.loadingTags.add = false
      this.dialogFormVisible = true
      this.object = { status: 2, sort: 10 }
      this.ifView = false
      this.dialogStatus = 'create'
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
    handleUpdateClick(id, status) {
      this.$confirm('是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.updateAccountStatus(id, status)
      })
    },
    userTypeChange(val) {
      this.param.userType = val
    },
    updateAccountStatus(id, status) {
      updateUserStatus(id, status).then(() => {
        this.dialogFormVisible = false
        this.getData()
        this.$message({
          message: '操作成功',
          type: 'success'
        })
      })
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

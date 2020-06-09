<template>
  <div class="app-container">
    <el-row type="flex" justify="right" style="padding-bottom:5px ">
      <el-col :span="8">
        <el-button-group>
          <el-button v-show="add" size="mini" type="primary" icon="el-icon-plus" round @click="handleAddClick">新增</el-button>
          <el-button v-show="this.delete" size="mini" type="danger" icon="el-icon-delete" round @click="handleDeleteClick">删除</el-button>
        </el-button-group>
      </el-col>
      <el-col :span="16">
        <el-row type="flex" justify="end">
          <el-input v-model="param.invitationCode" style="width:160px;margin-left:10px" placeholder="督导码" clearable />
          <el-input v-model="param.userName" style="width:160px;margin-left:10px" placeholder="根据咨询师账号查询" clearable />
          <el-input v-model="param.realName" style="width:160px;margin-left:10px" placeholder="根据咨询师昵称查询" clearable />
          <el-button icon="el-icon-search" type="primary" @click="getData" />
        </el-row>
      </el-col>
    </el-row>
    <el-table ref="multipleTable" :data="objectList" tooltip-effect="dark" style="width: 100%" border fit highlight-current-row @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="44" />
      <el-table-column type="index" align="center" label="#" width="44" />
      <el-table-column prop="sort" align="center" sortable label="排序" width="114">
        <template slot-scope="scope">
          <el-link icon="el-icon-edit" @click="setSort(scope.row)">{{ scope.row.sort }}</el-link>
        </template>
      </el-table-column>
      <el-table-column prop="spreadUrl" align="center" label="推广链接" width="144" show-overflow-tooltip>
        <template slot-scope="scope">
          <el-link icon="el-icon-copy-document" @click="handleCopy(scope.row.spreadUrl)">{{ scope.row.spreadUrl }}</el-link>
        </template>
      </el-table-column>
      <el-table-column prop="status" align="center" label="状态" width="72">
        <template slot-scope="scope">
          <span v-if="scope.row.status === 1">审核中</span>
          <span v-if="scope.row.status === 2">审核通过</span>
          <span v-if="scope.row.status === 3">已驳回</span>
        </template>
      </el-table-column>
      <el-table-column prop="invitationCode" align="center" label="邀请码" width="120" />
      <el-table-column prop="user.userName" align="center" label="登录账号" width="120" />
      <el-table-column prop="user.realName" align="center" label="姓名" width="114" />
      <el-table-column align="center" label="性别" width="72">
        <template slot-scope="scope">
          <span v-if="scope.row.user.sex === 0">男</span>
          <span v-if="scope.row.user.sex === 1">女</span>
        </template>
      </el-table-column>
      <el-table-column prop="user.age" align="center" label="年龄" width="72" />
      <el-table-column prop="cityName" align="center" label="城市" width="74" />
      <el-table-column prop="consultationFee" align="center" label="咨询费用" width="120">
        <template slot-scope="scope">
          {{ scope.row.consultationFee/100 }}
        </template>
      </el-table-column>
      <el-table-column prop="insertTime" label="入驻时间" align="center" width="164" :formatter="dateFormat" />
      <el-table-column align="center" fixed="right" width="260" label="操作">
        <template slot-scope="scope">
          <el-button-group>
            <el-button v-if="scope.row.status === 2" plain size="small" @click="handleSupervisorClick(scope.row)">授权</el-button>
            <el-button plain size="small" @click="handleGroupClick(scope.row)">指派</el-button>
            <el-button v-if="scope.row.status === 1" plain size="small" @click="handleReviewClick(scope.row)">审核</el-button>
            <router-link :to="'/user/consultantInfo/'+scope.row.id">
              <el-button plain size="small" icon="el-icon-view">
                详情
              </el-button>
            </router-link>
            <el-button plain size="small" @click="handleTitelClick(scope.row)">头衔</el-button>
          </el-button-group>
        </template>
      </el-table-column>
    </el-table>
    <el-row type="flex" justify="end" style="padding:5px 0; ">
      <el-pagination background :current-page="currentPage" :page-sizes="[10, 50, 100, 200]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </el-row>
    <el-dialog title="督导师设定" :visible.sync="dialogFormVisible" :close-on-press-escape="false" :close-on-click-modal="false" top="15vh" width="48%">
      <el-form ref="dataForm" :model="object" :label-width="formLabelWidth" :disabled="ifView">
        <el-form-item
          label="督导码"
          prop="invitationCode"
          :rules="[
            { required: true, message: '督导码不能为空'}
          ]"
        >
          <el-input v-model="object.invitationCode" auto-complete="off" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" :loading="loadingTags.edit" @click="setSupervisor">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="指派分组" :visible.sync="dialogGroupVisible" :close-on-press-escape="false" :close-on-click-modal="false" top="15vh" width="48%">
      <el-form ref="dataGroupForm" :model="object" :label-width="formLabelWidth" :disabled="ifView">
        <el-form-item
          label="督导码"
          prop="invitationCode"
          :rules="[
            { required: true, message: '督导码不能为空'}
          ]"
        >
          <el-select v-model="object.invitationCode" placeholder="请选择督导码">
            <el-option
              v-for="(item,index) in invitationCodeList"
              :key="index"
              :label="item"
              :value="item"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogGroupVisible = false">取 消</el-button>
        <el-button type="primary" :loading="loadingTags.group" @click="setSupervisorGroup">确 定</el-button>
      </div>
    </el-dialog>
    <review-index :dialog-show="reviewDialogStatus" :ref-id="toReviewId" :ref-type="0" @dialogHide="dialogHide" />
    <el-dialog title="头衔设定" :visible.sync="dialogTitelVisible" :close-on-press-escape="false" :close-on-click-modal="false" top="15vh" width="48%">
      <el-form ref="dataForm" :model="object" :label-width="formLabelWidth" :disabled="ifView">
        <el-form-item
          label="头衔"
        >
          <el-input v-model="object.title" auto-complete="off" />
        </el-form-item>
        <el-form-item
          label="面对面地址"
        >
          <el-input v-model="object.faceAddress" auto-complete="off" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogTitelVisible = false">取 消</el-button>
        <el-button type="primary" :loading="loadingTags.edit" @click="setTitel">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>
<script>
import { listConsultant, addConsultant, deleteConsultant, updateSupervisor, supervisorCodeList, updateConsultant, updateTitel } from '@/api/consultant'
import { deepClone } from '@/utils/transferUtil'
import reviewIndex from '@/components/Review/index'
import moment from 'moment'
export default {
  name: 'ConsultantManange',
  components: {
    'review-index': reviewIndex
  },
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
      invitationCodeList: ['paopaoxinli'],
      multipleSelection: [],
      systemOptions: [],
      currentPage: 1,
      total: 0,
      pageSize: 10,
      dialogFormVisible: false,
      dialogGroupVisible: false,
      replaceDialogVisible: false,
      dialogTitelVisible: false,
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
        edit: false,
        review: false,
        group: false
      },
      param: {
        pageNo: 1,
        pageSize: 10,
        userName: '',
        realName: '',
        userType: 0,
        invitationCode: ''
      },
      toReplaceList: [],
      reviewData: {},
      toReviewId: 0,
      reviewDialogStatus: false
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
    filterToReplaceList() {
      this.param.pageNo = this.currentPage
      this.param.pageSize = this.pageSize
      listConsultant(this.param).then(r => {
        this.toReplaceList = r.result.list
      })
    },
    getData() {
      this.param.pageNo = this.currentPage
      this.param.pageSize = this.pageSize
      listConsultant(this.param).then(r => {
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
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.loadingTags.add = true
          addConsultant(this.object).then(() => {
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
    handleSupervisorClick(val) {
      this.loadingTags.edit = false
      this.dialogFormVisible = true
      this.object = deepClone(val)
      this.ifView = false
      this.dialogStatus = 'edit'
    },
    handleTitelClick(val) {
      this.loadingTags.edit = false
      this.dialogTitelVisible = true
      this.object = deepClone(val)
      this.ifView = false
      this.dialogStatus = 'edit'
    },
    setSupervisor() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.loadingTags.edit = true
          updateSupervisor(this.object.id, this.object.invitationCode).then(r => {
            if (r.success) {
              this.dialogFormVisible = false
              this.getData()
              this.$message({
                message: '修改成功',
                type: 'success'
              })
            } else {
              this.$message({
                message: r.message,
                type: 'error'
              })
            }
            this.loadingTags.edit = false
          })
        }
      })
    },
    setTitel() {
      this.loadingTags.edit = true
      console.log(this.object)
      updateTitel(this.object).then(r => {
        if (r.success) {
          this.dialogTitelVisible = false
          this.getData()
          this.$message({
            message: '修改成功',
            type: 'success'
          })
        } else {
          this.$message({
            message: r.message,
            type: 'error'
          })
        }
        this.loadingTags.edit = false
      })
    },
    setSort(val) {
      this.object = val
      this.$prompt('请输入排序序号', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPattern: /[1-9]/,
        inputErrorMessage: '排序序号不正确'
      }).then(({ value }) => {
        this.object.sort = value
        updateConsultant(this.object).then(() => {
          this.$message({
            message: '修改成功',
            type: 'success'
          })
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '取消输入'
        })
      })
    },
    removeData() {
      deleteConsultant(this.ids).then(() => {
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
    userTypeChange(val) {
      this.param.userType = val
    },
    handleGroupClick(val) {
      this.object = { id: val.id }
      supervisorCodeList().then(r => {
        if (r.result.length > 0) {
          this.invitationCodeList = r.result
        }
      })
      this.dialogGroupVisible = true
    },
    setSupervisorGroup() {
      this.$refs['dataGroupForm'].validate((valid) => {
        if (valid) {
          this.loadingTags.group = true
          updateConsultant(this.object).then(() => {
            this.loadingTags.group = false
            this.getData()
            this.$message({
              message: '修改成功',
              type: 'success'
            })
            this.dialogGroupVisible = false
          })
        }
      })
    },
    handleReviewClick(val) {
      console.log()
      if (!val.invitationCode) {
        this.$message({
          message: '请先指派分组',
          type: 'warning'
        })
        return
      }
      this.toReviewId = val.id
      this.reviewDialogStatus = true
    },
    dialogHide() {
      this.reviewDialogStatus = false
      this.getData()
    },
    handleCopy(data) {
      this.copy(data)
    },
    copy(data) {
      const url = data
      const oInput = document.createElement('input')
      oInput.value = url
      document.body.appendChild(oInput)
      oInput.select() // 选择对象;
      console.log(oInput.value)
      document.execCommand('Copy') // 执行浏览器复制命令
      this.$message({
        message: '复制成功',
        type: 'success'
      })
      oInput.remove()
    },
    dateFormat: function(row, column) {
      var date = row[column.property]
      if (!date) {
        return ''
      }
      return moment(date).format('YYYY-MM-DD HH:ss')
    }
  }
}
</script>

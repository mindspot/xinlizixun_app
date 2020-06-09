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
          <el-input v-model="param.invitationCode" style="width:160px;margin-left:10px" placeholder="督导码" clearable />
          <el-input v-model="param.userName" style="width:160px;margin-left:10px" placeholder="根据咨询师账号查询" clearable />
          <el-input v-model="param.realName" style="width:160px;margin-left:10px" placeholder="根据咨询师昵称查询" clearable />
          <el-button icon="el-icon-search" type="primary" @click="getData" />
        </el-row>
      </el-col>
    </el-row>
    <el-table ref="multipleTable" :data="objectList" tooltip-effect="dark" style="width: 100%" border highlight-current-row @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="64" />
      <el-table-column type="index" align="center" label="#" width="64" />
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
      <el-table-column prop="invitationCode" align="center" label="督导码" width="144" />
      <el-table-column prop="user.userName" align="center" label="登录账号" width="120" />
      <el-table-column prop="user.realName" align="center" label="姓名" width="72" />
      <el-table-column align="center" label="性别" width="72">
        <template slot-scope="scope">
          <span v-if="scope.row.user.sex === 0">男</span>
          <span v-if="scope.row.user.sex === 1">女</span>
        </template>
      </el-table-column>
      <el-table-column prop="cityName" align="center" label="城市" width="74" />
      <el-table-column prop="insertTime" align="center" label="入驻时间" width="164" :formatter="dateFormat" />
      <el-table-column fixed="right" align="center" label="操作" width="360">
        <template slot-scope="scope">
          <el-button-group>
            <el-button plain size="small" @click="handleCancelSupervisor(scope.row)">取消督导</el-button>
            <el-button plain size="small" @click="handleEditClick(scope.row)">分成比例</el-button>
            <el-button plain size="small" @click="handleMoveClick(scope.row)">组员维护</el-button>
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
    <el-dialog title="分成比例设定" :visible.sync="dialogFormVisible" :close-on-press-escape="false" :close-on-click-modal="false" top="15vh" width="40%">
      <el-form ref="dataForm" :model="object" label-width="100px" :disabled="ifView">
        <h3>首单比例设定</h3>
        <el-row>
          <el-col :span="12">
            <el-form-item
              label="平台比例"
            >
              <el-input-number v-model="firstConsultantRate.platformRate" :max="100" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="渠道比例"
            >
              <el-input-number v-model="firstConsultantRate.channelRate" :max="100" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="咨询师比例"
            >
              <el-input-number v-model="firstConsultantRate.consultantRate" :max="100" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="督导师比例"
            >
              <el-input-number v-model="firstConsultantRate.partnerRate" :max="100" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
        <h3>复单比例设定</h3>
        <el-row>
          <el-col :span="12">
            <el-form-item
              label="平台比例"
            >
              <el-input-number v-model="nextConsultantRate.platformRate" :max="100" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="渠道比例"
            >
              <el-input-number v-model="nextConsultantRate.channelRate" :max="100" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="咨询师比例"
            >
              <el-input-number v-model="nextConsultantRate.consultantRate" :max="100" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              label="督导师比例"
            >
              <el-input-number v-model="nextConsultantRate.partnerRate" :max="100" :min="0" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" :loading="loadingTags.edit" @click="modifyData">确 定</el-button>
      </div>
    </el-dialog>
    <consultant-move :visible="dialogMoveVisible" :invitation-code="object.invitationCode" @consultantMove="consultantMove" />
    <el-dialog title="头衔设定" :visible.sync="dialogTitelVisible" :close-on-press-escape="false" :close-on-click-modal="false" top="15vh" width="48%">
      <el-form ref="dataForm" :model="object" :label-width="formLabelWidth" :disabled="ifView">
        <el-form-item
          label="头衔">
          <el-input v-model="object.title" auto-complete="off" />
        </el-form-item>
        <el-form-item
          label="面对面地址">
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
import { listConsultant, updateConsultantResult, updateConsultant, updateCancel, updateTitel } from '@/api/consultant'
import ConsultantMove from './component/consultantMove'
import { deepClone } from '@/utils/transferUtil'
import moment from 'moment'
export default {
  name: 'SupervisorList',
  components: {
    'consultant-move': ConsultantMove
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
      dialogMoveVisible: false,
      objectList: [],
      multipleSelection: [],
      systemOptions: [],
      currentPage: 1,
      total: 0,
      pageSize: 10,
      dialogFormVisible: false,
      dialogTitelVisible: false,
      object: {
        invitationCode: '',
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
      },
      firstConsultantRate: {},
      nextConsultantRate: {}
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
      listConsultant(this.param).then(r => {
        this.objectList = r.result.list
        this.total = r.result.total
      })
    },
    handleEditClick(val) {
      this.firstConsultantRate = val.firstConsultantRate
      this.nextConsultantRate = val.nextConsultantRate
      if (!this.nextConsultantRate) {
        this.nextConsultantRate = { id: 0, consultantId: val.id, type: 2 }
      }
      if (!this.firstConsultantRate) {
        this.firstConsultantRate = { id: 0, consultantId: val.id, type: 1 }
      }
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
    modifyData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.firstConsultantRate.platformRate +
       this.firstConsultantRate.channelRate + this.firstConsultantRate.consultantRate +
        this.firstConsultantRate.partnerRate !== 100) {
            this.$message({
              message: '首单分成比例设置错误',
              type: 'error'
            })
            return
          }
          if (this.nextConsultantRate.platformRate +
       this.nextConsultantRate.channelRate + this.nextConsultantRate.consultantRate +
        this.nextConsultantRate.partnerRate !== 100) {
            this.$message({
              message: '复单分成比例设置错误',
              type: 'error'
            })
            return
          }
          this.loadingTags.edit = true
          const param = {}
          param.firstConsultantRate = this.firstConsultantRate
          param.nextConsultantRate = this.nextConsultantRate
          updateConsultantResult(param).then(() => {
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
    userTypeChange(val) {
      this.param.userType = val
    },
    handleMoveClick(val) {
      this.object = deepClone(val)
      this.dialogMoveVisible = true
    },
    consultantMove(val) {
      this.dialogMoveVisible = false
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
    },
    handleCancelSupervisor(val) {
      this.$confirm('是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.updateCancelSupervisor(val)
      })
    },
    updateCancelSupervisor(val) {
      updateCancel(val.id).then(() => {
        this.dialogFormVisible = false
        this.getData()
        this.$message({
          message: '操作成功',
          type: 'success'
        })
      })
    }
  }
}
</script>
<style>
.el-dialog__body {
  padding: 20px;
  padding-top: 10px;
}
.el-dialog__header {
  padding: 20px;
  padding-bottom: 10px;
}
.el-table th.gutter{
   display: table-cell!important;
}
</style>

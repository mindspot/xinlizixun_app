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
        <el-row type="flex" justify="end" />
      </el-col>
    </el-row>
    <el-table ref="multipleTable" :data="objectList" tooltip-effect="dark" style="width: 100%" border fit highlight-current-row @selection-change="handleSelectionChange">
      <el-table-column type="selection" align="center" width="42" />
      <el-table-column prop="name" align="center" label="优惠劵名称" width="144" />
      <el-table-column prop="no" align="center" label="优惠劵编号" width="124" />
      <el-table-column prop="provideQty" align="center" label="发放/领取量" width="104">
        <template slot-scope="scope">
          <span>{{ scope.row.provideQty }}/{{ scope.row.takeQty }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="limitPerPerson" align="center" label="每人限领" width="84" />
      <el-table-column prop="type" align="center" label="优惠劵类型" width="104">
        <template slot-scope="scope">
          <span v-if="scope.row.type == 1">满减券</span>
          <span v-if="scope.row.type == 2">立减券</span>
          <span v-if="scope.row.type == 3">折扣券</span>
          <span v-if="scope.row.type == 4">兑换码</span>
        </template>
      </el-table-column>
      <el-table-column prop="useNotice" align="center" label="优惠方式" width="104">
        <template slot-scope="scope">
          <span v-if="scope.row.type == 1">
            满 {{ scope.row.maxAmount }}元,减{{ scope.row.actualAmount }}元
          </span>
          <span v-if="scope.row.type == 2">立减{{ scope.row.actualAmount }}元</span>
          <span v-if="scope.row.type == 3">折扣{{ scope.row.actualAmount }}%</span>
          <span v-if="scope.row.type == 4">优惠{{ scope.row.actualAmount }}元</span>
        </template>
      </el-table-column>
      <el-table-column prop="validType" align="center" label="有效期限制" width="104">
        <template slot-scope="scope">
          <span v-if="scope.row.validType == 1">固定日期</span>
          <span v-if="scope.row.validType == 2">固定天数</span>
        </template>
      </el-table-column>
      <el-table-column prop="termEndDate" align="center" width="184" label="有效期">
        <template slot-scope="scope">
          <span v-if="scope.row.validType == 2">
            领取后，当天起,{{ scope.row.validDays }}天有效
          </span>
          <span v-if="scope.row.validType == 1">
            {{ dateTimeFormat(scope.row.validStartTime) }}至{{ dateTimeFormat(scope.row.validEndTime) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="status" align="center" label="状态" width="104">
        <template slot-scope="scope">
          <span v-if="scope.row.status == 1">有效</span>
          <span v-if="scope.row.status == 2">失效</span>
        </template>
      </el-table-column>
      <el-table-column prop="url" align="center" label="领取链接" width="214" show-overflow-tooltip>
        <template slot-scope="scope">
          <el-link icon="el-icon-copy-document" @click="handleCopy(scope.row.url)">{{ scope.row.url }}</el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" fixed="right" label="操作" width="174">
        <template slot-scope="scope">
          <el-button v-if="scope.row.type === 4" plain size="mini" @click="handleProvideClick(scope.row)">立即生效</el-button>
          <el-button plain size="mini" @click="handleEditClick(scope.row)">修改</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-row type="flex" justify="end" style="padding:5px 0; ">
      <el-pagination background :current-page="currentPage" :page-sizes="[10, 50, 100, 200]" :page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="handleSizeChange" @current-change="handleCurrentChange" />
    </el-row>
    <el-dialog title="优惠券模板信息" :visible.sync="dialogFormVisible" :close-on-press-escape="false" :close-on-click-modal="false" top="15vh" width="42%">
      <el-form ref="dataForm" :model="object" :label-width="formLabelWidth" :disabled="ifView">
        <el-row>
          <el-col :span="12">
            <el-form-item label="优惠券名称">
              <el-input v-model="object.name" auto-complete="off" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="优惠券编号">
              <el-input v-model="object.no" auto-complete="off" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="优惠券类型">
              <el-radio-group v-model="object.type" size="small">
                <el-radio-button :label="1">满减券</el-radio-button>
                <el-radio-button :label="2">立减券</el-radio-button>
                <el-radio-button :label="3">折扣券</el-radio-button>
                <el-radio-button :label="4">兑换码</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="优惠方式">
          <el-row v-if="object.type === 1">
            <el-col :span="12">
              <el-input v-model="object.maxAmount">
                <template slot="prepend">满</template>
                <template slot="append">元</template>
              </el-input>
            </el-col>
            <el-col :span="12">
              <el-input v-model="object.actualAmount">
                <template slot="prepend">减</template>
                <template slot="append">元</template>
              </el-input>
            </el-col>
          </el-row>
          <el-row v-if="object.type === 2">
            <el-col :span="12">
              <el-input v-model="object.actualAmount">
                <template slot="prepend">立减</template>
                <template slot="append">元</template>
              </el-input>
            </el-col>
          </el-row>
          <el-row v-if="object.type === 3">
            <el-col :span="12">
              <el-input v-model="object.actualAmount">
                <template slot="prepend">折扣</template>
                <template slot="append">%</template>
              </el-input>
            </el-col>
          </el-row>
          <el-row v-if="object.type === 4">
            <el-col :span="12">
              <el-input v-model="object.actualAmount">
                <template slot="prepend">优惠</template>
                <template slot="append">元</template>
              </el-input>
            </el-col>
          </el-row>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="发放数量">
              <el-input v-model="object.provideQty" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="每人限领">
              <el-input v-model="object.limitPerPerson" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="有效期限制">
          <el-radio-group v-model="object.validType" size="small">
            <el-radio-button :label="1">固定日期</el-radio-button>
            <el-radio-button :label="2">固定天数</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="object.validType === 1" label="固定日期">
          <el-date-picker
            v-model="object.validDates"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
          />
        </el-form-item>
        <el-form-item v-if="object.validType === 2" label="固定天数">
          <el-row>
            <el-col :span="12">
              <el-input v-model="object.validDays" />
            </el-col>
            <el-col :span="12">
              <el-link type="warning">领取后，当天生效</el-link>
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="使用须知">
          <el-input v-model="object.remark" type="textarea" />
        </el-form-item>
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
import { addCouponTemplate, updateCouponTemplate, listCouponTemplate, generateCoupon } from '@/api/couponTemplate'
import { deepClone } from '@/utils/transferUtil'
import { formatNumber } from '@/utils/formatter'
import moment from 'moment'
export default {
  name: 'TemplateList',
  filters: {
    numberFilter(data) {
      return formatNumber(data)
    }
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
      multipleSelection: [],
      systemOptions: [],
      currentPage: 1,
      total: 0,
      pageSize: 10,
      dialogFormVisible: false,
      object: {
        termEndDate: '',
        useNotice: '',
        useAmount: ''
      },
      formLabelWidth: '100px',
      ifView: false,
      ids: [],
      dialogStatus: '',
      loadingTags: {
        add: false,
        edit: false
      },
      param: {
        pageNo: 1,
        pageSize: 10
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
    getData() {
      this.param.pageNo = this.currentPage
      listCouponTemplate(this.param).then(r => {
        this.objectList = r.result.list
        this.total = r.result.total
      })
    },
    handleAddClick() {
      this.loadingTags.add = false
      this.dialogFormVisible = true
      this.object = { type: 1, validType: 1, validDates: [] }
      this.ifView = false
      this.dialogStatus = 'create'
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.loadingTags.add = true
          if (this.object.validType === 1) {
            if (this.object.validDates && this.object.validDates.length > 0) {
              this.object.validStartTime = this.object.validDates[0]
              this.object.validEndTime = this.object.validDates[1]
            } else {
              this.$message.error('请选择有效期')
              return false
            }
          } else {
            this.object.validStartTime = ''
            this.object.validEndTime = ''
          }
          addCouponTemplate(this.object).then(() => {
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
      if (this.object.validType === 1) {
        this.object.validDates = []
        this.object.validDates.push(this.object.validStartTime)
        this.object.validDates.push(this.object.validEndTime)
      }
      this.ifView = false
      this.dialogStatus = 'edit'
    },
    modifyData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.loadingTags.edit = true
          if (this.object.validType === 1) {
            if (this.object.validDates && this.object.validDates.length > 0) {
              this.object.validStartTime = this.object.validDates[0]
              this.object.validEndTime = this.object.validDates[1]
            } else {
              this.$message.error('请选择有效期')
              return false
            }
          } else {
            this.object.validStartTime = ''
            this.object.validEndTime = ''
          }
          updateCouponTemplate(this.object).then(() => {
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
      // deleteConsultant(this.ids).then(() => {
      //   this.getData()
      //   this.$message({
      //     message: '删除成功',
      //     type: 'success'
      //   })
      // })
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
    handleProvideClick(data) {
      this.$confirm('此操作将生成兑换码优惠券, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        generateCoupon(data.id).then(r => {
          this.$message({
            message: '生成成功',
            type: 'success'
          })
        })
      })
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
    dateTimeFormat: function(date) {
      return moment(date).format('YYYY-MM-DD')
    }
  }
}
</script>

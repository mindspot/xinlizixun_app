<template>
  <!--配载货物 -->
  <el-dialog title="组内成员变更" :fullscreen="true" :visible.sync="dialogMoveVisible" :close-on-press-escape="false" :close-on-click-modal="false">
    <template slot="title">
      <span class="span-title">过滤</span>
    </template>
    <el-row :gutter="24">
      <el-col :span="24" />
    </el-row>
    <el-row type="flex" justify="center" style="padding-top: 10px;">
      <el-col :span="11">
        <el-divider>组内咨询师</el-divider>
        <el-table
          :data="innerList"
          :header-cell-style="{'background-color': '#fafafa'}"
          height="400"
          border
          style="width: 100%;"
          @selection-change="handleLeftSelectionChange"
        >
          <el-table-column type="selection" width="42" align="center" />
          <el-table-column prop="id" label="ID" sortable width="72" />
          <el-table-column prop="user.realName" label="姓名" width="94" />
          <el-table-column prop="user.userName" label="账号" width="140" />
          <el-table-column prop="cityName" label="城市" width="74" />
          <el-table-column prop="insertTime" label="入驻时间" width="90" :formatter="dateFormat" />
          <el-table-column prop="remark" label="备注" show-overflow-tooltip />
        </el-table>
      </el-col>
      <el-col :span="2">
        <el-row style="padding-top: 160px;">
          <el-row type="flex" justify="center">
            <el-button type="primary" :disabled="moveToRight" icon="el-icon-arrow-right" circle @click="moveToRightClick" />
          </el-row>
          <el-row type="flex" justify="center" style="padding-top: 10px">
            <el-button type="primary" :disabled="moveToLeft" icon="el-icon-arrow-left" circle @click="moveToLeftClick" />
          </el-row>
        </el-row>
      </el-col>
      <el-col :span="11">
        <el-divider>组外咨询师</el-divider>
        <el-table
          :data="outList"
          border
          height="400"
          :header-cell-style="{'background-color': '#fafafa'}"
          style="width: 100%"
          @selection-change="handleRightSelectionChange"
        >
          <el-table-column type="selection" width="42" align="center" />
          <el-table-column prop="id" label="ID" sortable width="72" />
          <el-table-column prop="user.realName" label="姓名" width="94" />
          <el-table-column prop="user.userName" label="账号" width="140" />
          <el-table-column prop="cityName" label="城市" width="74" />
          <el-table-column prop="insertTime" label="入驻时间" width="90" :formatter="dateFormat" />
          <el-table-column prop="remark" label="备注" show-overflow-tooltip />
        </el-table>
      </el-col>
    </el-row>
    <div slot="footer" class="dialog-footer">
      <el-button @click="callBack">取 消</el-button>
      <el-button type="primary" :loading="loadingTags.load" @click="saveData">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>
import { listConsultantResult, supervisorChange } from '@/api/consultant'
import moment from 'moment'
export default {
  name: 'ConsultantMove',
  props: {
    visible: {
      type: Boolean,
      default: false,
      required: true
    },
    invitationCode: {
      type: String,
      default: '',
      required: false
    }
  },
  data() {
    return {
      multipleLeftSelection: [],
      leftIds: [],
      multipleRightSelection: [],
      rightIds: [],
      dialogMoveVisible: this.visible,
      transportBillParam: {
        billNo: '',
        consignorOrderNo: '',
        consignorName: '',
        consigneeName: '',
        receiveStationName: '',
        destinationStationName: '',
        billDate: '',
        consignorCompanyName: '',
        cargoName: '',
        qty: ''
      },
      loadingTags: {
        load: false
      },
      moveToLeft: true,
      moveToRight: true,
      cargoFormLabelWidth: '80px',
      formLabelWidth: '80px',
      outList: [],
      innerList: []
    }
  },
  watch: {
    visible: function() {
      this.dialogMoveVisible = this.visible
      if (this.dialogMoveVisible) {
        this.listInnerData(this.invitationCode)
        this.listOuterData()
      }
    },
    multipleLeftSelection: function() {
      const arr = []
      for (const i in this.multipleLeftSelection) {
        arr.push(this.multipleLeftSelection[i].id)
      }
      this.leftIds = arr.join()
      if (this.leftIds.length > 0) {
        this.moveToRight = false
      } else {
        this.moveToRight = true
      }
    },
    multipleRightSelection: function() {
      const arr = []
      for (const i in this.multipleRightSelection) {
        arr.push(this.multipleRightSelection[i].id)
      }
      this.rightIds = arr.join()
      if (this.rightIds.length > 0) {
        this.moveToLeft = false
      } else {
        this.moveToLeft = true
      }
    }
  },
  activated() {

  },
  methods: {
    queryData() {
    },
    listInnerData(invitationCode) {
      this.innerList = []
      const param = {}
      param.invitationCode = invitationCode
      listConsultantResult(param).then(r => {
        this.innerList = r.result
      })
    },
    listOuterData() {
      this.outList = []
      const param = {}
      param.invitationCode = ''
      listConsultantResult(param).then(r => {
        this.outList = r.result
      })
    },
    moveToLeftClick() {
      for (const i in this.multipleRightSelection) {
        if (!this.checkElementDuplicate(this.innerList, this.multipleRightSelection[i])) {
          this.innerList.push(this.multipleRightSelection[i])
        } else {
          this.mergeLeftElement(this.innerList, this.multipleRightSelection[i])
        }
        this.removeElement(this.outList, this.multipleRightSelection[i])
      }
    },
    mergeLeftElement(fromList, object) {
      for (const i in fromList) {
        if (fromList[i].id === object.id) {
          fromList[i].inventoryQty = object.inventoryQty
        }
      }
    },
    moveToRightClick() {
      for (const i in this.multipleLeftSelection) {
        if (!this.checkElementDuplicate(this.outList, this.multipleLeftSelection[i])) {
          this.outList.push(this.multipleLeftSelection[i])
        } else {
          this.mergeRightElement(this.outList, this.multipleLeftSelection[i])
        }
        this.removeElement(this.innerList, this.multipleLeftSelection[i])
      }
    },
    mergeRightElement(fromList, object) {
      for (const i in fromList) {
        if (fromList[i].id === object.id) {
          fromList[i].operateQty = object.inventoryQty + fromList[i].operateQty
        }
      }
    },
    handleLeftSelectionChange(val) {
      this.multipleLeftSelection = val
    },
    handleRightSelectionChange(val) {
      this.multipleRightSelection = val
    },
    dateFormat: function(row, column) {
      var date = row[column.property]
      if (!date) {
        return ''
      }
      return moment(date).format('YYYY-MM-DD')
    },
    removeElement(fromList, object) {
      for (const i in fromList) {
        if (fromList[i].id === object.id) {
          fromList.splice(i, 1)
        }
      }
    },
    checkElementDuplicate(fromList, object) {
      for (const i in fromList) {
        if (fromList[i].id === object.id) {
          return true
        }
      }
      return false
    },
    saveData() {
      const arr = []
      for (const i in this.innerList) {
        arr.push(this.innerList[i].id)
      }
      const idStr = arr.join(',')
      this.loadingTags.load = true
      supervisorChange(idStr, this.invitationCode).then(r => {
        this.loadingTags.load = false
        this.dialogMoveVisible = false
        this.$emit('consultantMove', this.dialogCargoVisible)
      })
    },
    callBack() {
      this.transportBillParam = {
        billNo: '',
        consignorOrderNo: '',
        consignorName: '',
        consigneeName: ''
      }
      this.dialogMoveVisible = false
      this.$emit('consultantMove', this.dialogCargoVisible)
    }
  }
}
</script>

<style scoped>

</style>

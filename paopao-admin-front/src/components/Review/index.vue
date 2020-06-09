<template>
  <el-dialog
    title="数据审核"
    :visible.sync="reviewDialogStatus"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
  >
    <el-form ref="reviewDataForm" :model="reviewData">
      <el-form-item label="审核结果" :label-width="formLabelWidth">
        <el-select v-model="reviewData.status" filterable placeholder="选择审核结果">
          <el-option
            v-for="item in reviewStatusEnums"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="审核备注" :label-width="formLabelWidth">
        <el-input v-model="reviewData.reviewRemark" type="textarea" auto-complete="off" />
      </el-form-item>
    </el-form>
    <div slot="footer" class="dialog-footer">
      <el-button @click="reviewDialogStatus = false">取 消</el-button>
      <el-button type="primary" :loading="loadingTags.review" @click="submitReview">确 定</el-button>
    </div>
  </el-dialog>
</template>
<script>
import { addReviewRecord } from '@/api/review'
import { ReviewStatusEnum, getReviewRefTypeEnum } from '@/data/enums'
import { mapGetters } from 'vuex'
export default {
  name: 'Review',
  props: {
    dialogShow: {
      type: Boolean,
      default: false
    },
    refId: {
      type: Number,
      default: 0,
      required: true
    },
    refType: {
      type: Number,
      default: 0,
      required: true
    }
  },
  data() {
    return {
      loadingTags: {
        review: false
      },
      formLabelWidth: '80px',
      reviewData: {},
      reviewDialogStatus: false,
      reviewStatusEnums: []
    }
  },
  computed: {
    ...mapGetters([
      'userId',
      'realName'
    ])
  },
  watch: {
    dialogShow: function() {
      this.reviewDialogStatus = this.dialogShow
      this.reviewData = { refId: this.refId, refType: this.refType, refTypeName: getReviewRefTypeEnum(this.refType), status: 2, reviewRemark: '' }
    },
    reviewDialogStatus(val) {
      if (!val) {
        this.$emit('dialogHide')
      }
    }
  },
  mounted() {
    this.reviewStatusEnums = ReviewStatusEnum
  },
  methods: {
    handleReviewClick(val) {
      this.loadingTags.review = false
      this.reviewDialogStatus = true
    },
    submitReview() {
      if (this.reviewData.status === 3 && !this.reviewData.reviewRemark) {
        this.$message({
          message: '请填写备注',
          type: 'error'
        })
      } else {
        this.loadingTags.review = true
        this.reviewData.reviewUserId = this.userId
        this.reviewData.reviewUserName = this.userName
        this.reviewData.reviewStationId = this.stationId
        this.reviewData.reviewStationName = this.stationName
        addReviewRecord(this.reviewData).then(() => {
          this.reviewDialogStatus = false
          this.loadingTags.review = false
          this.$message({
            message: '审核成功',
            type: 'success'
          })
        }).catch(r => {
          this.loadingTags.review = false
        })
      }
    }
  }
}
</script>

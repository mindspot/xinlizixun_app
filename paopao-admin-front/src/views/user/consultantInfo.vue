<template>
  <div class="app-container">
    <el-collapse v-model="activeName" accordion>
      <el-collapse-item title="基本信息" name="1">
        <el-row :gutter="20">
          <el-col :span="10">
            <el-form ref="form" :model="consultant" label-width="100px" style="width:100%">
              <el-form-item label="图像" required>
                <el-row v-if="user.headImg" type="flex" justify="center">
                  <el-avatar :size="64" :src="avatar" />
                </el-row>
              </el-form-item>
              <el-form-item label="手机" required>
                <div class="value">{{ user.userName }}</div>
              </el-form-item>
              <el-form-item label="姓名" required>
                <div class="value">{{ user.realName }}</div>
              </el-form-item>
              <el-form-item label="性别" required>
                <div class="value">{{ user.sex?"女":'男' }}</div>
              </el-form-item>
              <el-form-item label="出生年月" required>
                <div class="value">{{ consultant.birthDate }}</div>
              </el-form-item>
              <el-form-item label="手机" required>
                <div class="value">{{ consultant.phone }}</div>
              </el-form-item>
              <el-form-item label="微信">
                <div class="value">{{ consultant.phone }}</div>
              </el-form-item>
              <el-form-item label="邮箱">
                <div class="value">{{ consultant.mailbox }}</div>
              </el-form-item>
              <el-form-item label="所在地区">
                <div class="value">{{ consultant.provName }}-{{ consultant.cityName }}-{{ consultant.areaName }}</div>
              </el-form-item>
              <el-form-item label="详细地址">
                <div class="value">{{ consultant.addrDetail }}</div>
              </el-form-item>
              <el-form-item label="寄语">
                <div class="value">{{ consultant.sendWord }}</div>
              </el-form-item>
              <el-form-item label="身份证">
                <el-row type="flex">
                  <el-col v-for="card in cards" :key="card.id" :span="12">
                    <el-image
                      style="width: 100px; height: 100px"
                      :src="baseUrl + card.imgUrl"
                      :preview-src-list="[baseUrl + card.imgUrl]"
                    />
                  </el-col>
                </el-row>
              </el-form-item>
              <el-form-item label="简介">
                <div class="value">{{ consultant.briefIntroduction }}</div>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
      </el-collapse-item>
      <el-collapse-item title="专业资质" name="2">
        <h3>教育经历</h3>
        <el-row v-for="item in educationExperienceList" :key="'Education'+item.id" :gutter="20">
          <el-col :span="10">
            <el-form label-width="100px" style="width:100%">
              <el-form-item label="学历" required>
                <div class="value">{{ item.educationTypeDesc }}</div>
              </el-form-item>
              <el-form-item label="专业" required>
                <div class="value">{{ item.major }}</div>
              </el-form-item>
              <el-form-item label="图像" required>
                <el-row type="flex">
                  <el-image
                    style="width: 100px; height: 100px"
                    :src="baseUrl + item.certificateUrl"
                    :preview-src-list="[baseUrl + item.certificateUrl]"
                  />
                </el-row>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
        <h3>资质证明</h3>
        <el-row v-for="certification in certificationList" :key="'Certification'+ certification.id" :gutter="20">
          <el-col :span="10">
            <el-form label-width="100px" style="width:100%">
              <el-form-item label="资质名称" required>
                <div class="value">{{ certification.certificateTypeName }}</div>
              </el-form-item>
              <el-form-item label="资质编号" required>
                <div class="value">{{ certification.certificateNo }}</div>
              </el-form-item>
              <el-form-item label="持证年限" required>
                <div class="value">{{ certification.certificateAge }}</div>
              </el-form-item>
              <el-form-item label="资质证书" required>
                <el-row type="flex">
                  <el-image
                    style="width: 100px; height: 100px"
                    :src="baseUrl + certification.certificateUrl"
                    :preview-src-list="[baseUrl + certification.certificateUrl]"
                  />
                </el-row>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
        <h3>培训经历</h3>
        <el-row v-for="training in trainingExperienceList" :key="'Training'+ training.id" :gutter="20">
          <el-col :span="10">
            <el-form label-width="100px" style="width:100%">
              <el-form-item label="主办机构">
                <div class="value">{{ training.organization }}</div>
              </el-form-item>
              <el-form-item label="起止时间">
                <div class="value">{{ training.startDate }} 到 {{ training.endDate }}</div>
              </el-form-item>
              <el-form-item label="课程名称">
                <div class="value">{{ training.courseName }}</div>
              </el-form-item>
              <el-form-item label="证明人">
                <div class="value">{{ training.certifier }}</div>
              </el-form-item>
              <el-form-item label="培训证书">
                <el-row type="flex">
                  <el-image
                    style="width: 100px; height: 100px"
                    :src="baseUrl +training.certificateUrl"
                    :preview-src-list="[baseUrl +training.certificateUrl]"
                  />
                </el-row>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
        <h3>督导经历</h3>
        <el-row v-for="supervised in supervisedExperienceList" :key="'Supervised'+ supervised.id" :gutter="20">
          <el-col :span="10">
            <el-form label-width="100px" style="width:100%">
              <el-form-item label="督导师/机构">
                <div class="value">{{ supervised.supervisor }}</div>
              </el-form-item>
              <el-form-item label="起止时间">
                <div class="value">{{ supervised.startDate }} 到 {{ supervised.endDate }}</div>
              </el-form-item>
              <el-form-item label="督导取向">
                <div class="value">{{ supervised.supervisionOrientation }}</div>
              </el-form-item>
              <el-form-item label="证明人">
                <div class="value">{{ supervised.certifier }}</div>
              </el-form-item>
              <el-form-item label="证明信件">
                <el-row type="flex">
                  <el-image
                    style="width: 100px; height: 100px"
                    :src="baseUrl + supervised.certificateUrl"
                    :preview-src-list="[baseUrl + supervised.certificateUrl]"
                  />
                </el-row>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
        <h3>出版书籍</h3>
        <el-row v-for="book in publishBookList" :key="'Book'+ book.id" :gutter="20">
          <el-col :span="10">
            <el-form label-width="100px" style="width:100%">
              <el-form-item label="书名">
                <div class="value">{{ book.materialName }}</div>
              </el-form-item>
              <el-form-item label="出版社">
                <div class="value">{{ book.publishSite }}</div>
              </el-form-item>
              <el-form-item label="出版时间">
                <div class="value">{{ book.publishTime }}</div>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
        <h3>发表论文</h3>
        <el-row v-for="paper in publishPaperList" :key="'Paper'+ paper.id" :gutter="20">
          <el-col :span="10">
            <el-form label-width="100px" style="width:100%">
              <el-form-item label="标题">
                <div class="value">{{ paper.materialName }}</div>
              </el-form-item>
              <el-form-item label="参考网址">
                <div class="value">{{ paper.publishSite }}</div>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
      </el-collapse-item>
      <el-collapse-item v-if="consultant.userType == 1&& firstConsultantRate && nextConsultantRate" title="分成比列" name="4">
        <el-row :gutter="20">
          <el-col :span="10">
            <h3>首单分成比例</h3>
            <el-form ref="form" :model="firstConsultantRate" label-width="120px" style="width:100%">
              <el-form-item label="平台分成比例">
                <div class="value">{{ firstConsultantRate.platformRate }}</div>
              </el-form-item>
              <el-form-item label="渠道分成比例">
                <div class="value">{{ firstConsultantRate.channelRate }}</div>
              </el-form-item>
              <el-form-item label="咨询师分成比例">
                <div class="value">{{ firstConsultantRate.consultantRate }}</div>
              </el-form-item>
              <el-form-item label="合伙人分成比例">
                <div class="value">{{ firstConsultantRate.partnerRate }}</div>
              </el-form-item>
            </el-form>
            <h3>单分成比例</h3>
            <el-form ref="form" :model="nextConsultantRate" label-width="120px" style="width:100%">
              <el-form-item label="平台分成比例">
                <div class="value">{{ nextConsultantRate.platformRate }}</div>
              </el-form-item>
              <el-form-item label="渠道分成比例">
                <div class="value">{{ nextConsultantRate.channelRate }}</div>
              </el-form-item>
              <el-form-item label="咨询师分成比例">
                <div class="value">{{ nextConsultantRate.consultantRate }}</div>
              </el-form-item>
              <el-form-item label="合伙人分成比例">
                <div class="value">{{ nextConsultantRate.partnerRate }}</div>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
      </el-collapse-item>
      <el-collapse-item title="服务设置" name="5">
        <h3>擅长领域</h3>
        <el-tag
          v-for="item in expertiseAreaList"
          :key="item.id"
          type="success"
          effect="dark"
        >
          {{ item.common.val }}
        </el-tag>
        <h3>针对人群</h3>
        <el-tag
          v-for="item in targetPeopleList"
          :key="item.id"
          type="success"
          effect="dark"
        >
          {{ item.common.val }}
        </el-tag>
        <h3>工作时间</h3>
        <el-row :gutter="20">
          <el-col :span="10">
            <el-row>
              <el-col
                v-for="item in platformWorkingTimeVOList"
                :key="item.platformWorkingTimeId"
                :span="8"
              >
                <div v-if="item.status === 2" style="background-color: #888888;" class="value-time">
                  {{ item.consultantWorkStart }}-{{ item.consultantWorkEnd }}
                </div>
                <div v-else class="value-time">
                  {{ item.consultantWorkStart }}-{{ item.consultantWorkEnd }}
                </div>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
        <h3>服务方式</h3>
        <el-row :gutter="20">
          <el-col :span="10">
            <el-table ref="multipleTable" :data="workTimeList" tooltip-effect="dark" style="width: 100%" border fit highlight-current-row>
              <el-table-column type="index" align="center" label="#" width="42" />
              <el-table-column prop="val" align="center" label="服务方式" />
              <el-table-column prop="serviceName" align="center" label="服务次数" />
              <el-table-column prop="sellPrice" align="center" label="服务价格" />
            </el-table>
          </el-col>
        </el-row>
      </el-collapse-item>
      <el-collapse-item v-show="consultant.userType == 1" title="组内成员" name="6">
        <el-table ref="multipleTable" :data="consultantList" tooltip-effect="dark" style="width: 100%" border fit highlight-current-row>
          <el-table-column type="index" align="center" label="#" width="64" />
          <el-table-column prop="sort" align="center" sortable label="排序" width="114">
            <template slot-scope="scope">
              {{ scope.row.sort }}
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
          <el-table-column prop="birthDate" align="center" label="出生年月" width="112" />
          <el-table-column prop="cityName" align="center" label="城市" width="74" />
          <el-table-column prop="consultationFee" align="center" label="咨询费用" width="120">
            <template slot-scope="scope">
              {{ scope.row.consultationFee/100 }}
            </template>
          </el-table-column>
          <el-table-column prop="insertTime" label="入驻时间" align="center" :formatter="dateFormat" />
        </el-table>
      </el-collapse-item>
    </el-collapse>
  </div>
</template>

<script>
import { getConsultantInfo } from '@/api/consultant'
import { getToken } from '@/utils/auth' // 验权
import { baseUrl } from '@/data/config'
import moment from 'moment'
export default {
  data() {
    return {
      baseUrl: baseUrl,
      cards: [],
      educationExperienceList: [],
      certificationList: [],
      trainingExperienceList: [],
      supervisedExperienceList: [],
      publishBookList: [],
      publishPaperList: [],
      dialogFormVisible: false,
      targetPeopleList: [],
      expertiseAreaList: [],
      platformWorkingTimeVOList: [],
      workTimeList: [],
      consultant: {},
      user: {},
      activeName: '1',
      firstConsultantRate: {},
      nextConsultantRate: {},
      consultantList: []
    }
  },
  computed: {
    myHeaders: function() {
      return {
        'X-Token': getToken()
      }
    },
    avatar: function() {
      return baseUrl + this.user.headImg
    }
  },
  mounted() {
    const id = this.$route.params && this.$route.params.id
    this.companyReviewResultGet(id)
  },
  methods: {
    companyReviewResultGet(id) {
      getConsultantInfo(id).then(r => {
        this.consultant = r.result
        this.user = r.result.user
        this.cards = r.result.cards
        this.educationExperienceList = r.result.educationExperienceList
        this.trainingExperienceList = r.result.trainingExperienceList
        this.certificationList = r.result.certificationList
        this.supervisedExperienceList = r.result.supervisedExperienceList
        this.publishBookList = r.result.publishBookList
        this.publishPaperList = r.result.publishPaperList
        this.targetPeopleList = r.result.targetPeopleList
        this.expertiseAreaList = r.result.expertiseAreaList
        this.platformWorkingTimeVOList = r.result.platformWorkingTimeVOList
        this.workTimeList = r.result.workTimeList
        this.firstConsultantRate = r.result.firstConsultantRate
        this.nextConsultantRate = r.result.nextConsultantRate
        this.consultantList = r.result.consultantList
      })
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
<style scoped>
.value {
  border: 1px solid #999999;
  text-align: left;
  padding-left: 10px;
  min-height: 32px;
}
.c-rblue {
  color: #006dfe;
}
.value-time {
  border: 1px solid #999999;
  text-align: center;
  padding-left: 10px;
  min-height: 32px;
}
.avatar {
  width: 100px;
  height: 28px;
  margin-left: 10px;
  cursor: pointer;
}
.avatar-change {
  color: #999999;
  line-height: 20px;
  margin-top: -10px;
}
.file-change {
  color: #999999;
  line-height: 20px;
  float: right;
  margin-left: 5px;
  line-height: 32px;
}
</style>


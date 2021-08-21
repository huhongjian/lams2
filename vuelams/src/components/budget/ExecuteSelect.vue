<template>
  <div>
    <el-dialog
        :title="title"
        :visible.sync="executeSelectVisible"
        :before-close="handleClose"
        width="80%">
      <div>
        <el-form :model="searchValue" :rules="rules" ref="searchValueForm">
          <el-row>
            <el-col :span="8">
              <el-form-item label="预算科目:" prop="itemType">
                <el-select v-model="searchValue.itemType" clearable placeholder="预算科目" size="mini"
                           style="width: 300px;">
                  <el-option
                      v-for="item in planItemTypes"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="4">
              <el-button size="mini" icon="el-icon-search" type="primary" @click="initItems">搜索</el-button>
            </el-col>
          </el-row>
        </el-form>
        <div style="margin-top: 10px">
          <el-table
              v-show="equipmentFeeList.length!=0"
              :data="equipmentFeeList"
              stripe
              border
              v-loading="loading"
              element-loading-text="正在加载..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)"
              style="width: 100%">
            <el-table-column
                prop="id"
                label="序号"
                width="50">
            </el-table-column>
            <el-table-column
                prop="projectName"
                :show-overflow-tooltip="true"
                label="科研项目">
            </el-table-column>
            <el-table-column
                prop="planName"
                :show-overflow-tooltip="true"
                label="预算计划">
            </el-table-column>
            <el-table-column
                prop="equipmentName"
                :show-overflow-tooltip="true"
                label="设备名称">
            </el-table-column>
            <el-table-column
                prop="usage"
                label="用途">
            </el-table-column>
            <el-table-column
                prop="unitPrice"
                label="单价（万元）"
                width="100">
            </el-table-column>
            <el-table-column
                prop="amount"
                label="数量"
                width="50">
            </el-table-column>
            <el-table-column
                prop="subTotal"
                label="合计（万元）"
                width="100">
            </el-table-column>
            <el-table-column
                prop="unfulfilledQuota"
                label="未执行额度（万元）"
                width="130">
            </el-table-column>
            <el-table-column
                prop="executeRate"
                label="执行率（%）"
                width="100">
            </el-table-column>
            <el-table-column
                width="80"
                label="操作">
              <template slot-scope="scope">
                <el-button type="primary" plain @click="showExecuteView(scope.row)">执行</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-table
              v-show="materialFeeList.length!=0"
              :data="materialFeeList"
              stripe
              border
              v-loading="loading"
              element-loading-text="正在加载..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)"
              style="width: 100%">
            <el-table-column
                prop="id"
                label="序号"
                width="50">
            </el-table-column>
            <el-table-column
                prop="projectName"
                :show-overflow-tooltip="true"
                label="科研项目">
            </el-table-column>
            <el-table-column
                prop="planName"
                :show-overflow-tooltip="true"
                label="预算计划">
            </el-table-column>
            <el-table-column
                prop="materialName"
                :show-overflow-tooltip="true"
                label="材料用途">
            </el-table-column>
            <el-table-column
                prop="specification"
                label="规格"
                :show-overflow-tooltip="true"
                width="100">
            </el-table-column>
            <el-table-column
                prop="amount"
                label="数量"
                width="50">
            </el-table-column>
            <el-table-column
                prop="unitPrice"
                label="单价（万元）"
                width="100">
            </el-table-column>
            <el-table-column
                prop="unit"
                label="单位"
                width="50">
            </el-table-column>
            <el-table-column
                prop="subTotal"
                label="合计（万元）"
                width="100">
            </el-table-column>
            <el-table-column
                prop="unfulfilledQuota"
                label="未执行额度（万元）"
                width="130">
            </el-table-column>
            <el-table-column
                prop="executeRate"
                label="执行率（%）"
                width="100">
            </el-table-column>
            <el-table-column
                width="80"
                label="操作">
              <template slot-scope="scope">
                <el-button type="primary" plain @click="showExecuteView(scope.row)">执行</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-table
              v-show="testingFeeList.length!=0"
              :data="testingFeeList"
              stripe
              border
              v-loading="loading"
              element-loading-text="正在加载..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)"
              style="width: 100%">
            <el-table-column
                prop="id"
                label="序号"
                width="50">
            </el-table-column>
            <el-table-column
                prop="projectName"
                :show-overflow-tooltip="true"
                label="科研项目">
            </el-table-column>
            <el-table-column
                prop="planName"
                :show-overflow-tooltip="true"
                label="预算计划">
            </el-table-column>
            <el-table-column
                prop="content"
                :show-overflow-tooltip="true"
                label="加工内容">
            </el-table-column>
            <el-table-column
                prop="times"
                label="次数"
                width="50">
            </el-table-column>
            <el-table-column
                prop="unitPrice"
                label="单价（万元）"
                width="100">
            </el-table-column>
            <el-table-column
                prop="subTotal"
                label="合计（万元）"
                width="100">
            </el-table-column>
            <el-table-column
                prop="unfulfilledQuota"
                label="未执行额度（万元）"
                width="150">
            </el-table-column>
            <el-table-column
                prop="executeRate"
                label="执行率(%)"
                width="100">
            </el-table-column>
            <el-table-column
                width="80"
                label="操作">
              <template slot-scope="scope">
                <el-button type="primary" plain @click="showExecuteView(scope.row)">执行</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-table
              v-show="meetingFeeList.length!=0"
              :data="meetingFeeList"
              stripe
              border
              v-loading="loading"
              element-loading-text="正在加载..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)"
              style="width: 100%">
            <el-table-column
                prop="id"
                label="序号"
                width="50">
            </el-table-column>
            <el-table-column
                prop="projectName"
                :show-overflow-tooltip="true"
                label="科研项目">
            </el-table-column>
            <el-table-column
                prop="planName"
                :show-overflow-tooltip="true"
                label="预算计划">
            </el-table-column>
            <el-table-column
                prop="content"
                :show-overflow-tooltip="true"
                label="会议内容">
            </el-table-column>
            <el-table-column
                prop="relation"
                :show-overflow-tooltip="true"
                label="与课题相关性">
            </el-table-column>
            <el-table-column
                prop="meal"
                label="伙食费（人/天）"
                width="80">
            </el-table-column>
            <el-table-column
                prop="otherCost"
                label="其他费用（人/天）"
                width="90">
            </el-table-column>
            <el-table-column
                prop="days"
                label="天数"
                width="50">
            </el-table-column>
            <el-table-column
                prop="amount"
                label="参会人数"
                width="80">
            </el-table-column>
            <el-table-column
                prop="meetingNum"
                label="会议次数"
                width="70">
            </el-table-column>
            <el-table-column
                prop="subTotal"
                label="合计（万元）"
                width="70">
            </el-table-column>
            <el-table-column
                prop="unfulfilledQuota"
                label="未执行额度（万元）"
                width="90">
            </el-table-column>
            <el-table-column
                prop="executeRate"
                label="执行率（%）"
                width="100">
            </el-table-column>
            <el-table-column
                width="80"
                label="操作">
              <template slot-scope="scope">
                <el-button type="primary" plain @click="showExecuteView(scope.row)">执行</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-table
              v-show="researchFeeList.length!=0"
              :data="researchFeeList"
              stripe
              border
              v-loading="loading"
              element-loading-text="正在加载..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)"
              style="width: 100%">
            <el-table-column
                prop="id"
                label="序号"
                width="50">
            </el-table-column>
            <el-table-column
                prop="projectName"
                :show-overflow-tooltip="true"
                label="科研项目">
            </el-table-column>
            <el-table-column
                prop="planName"
                :show-overflow-tooltip="true"
                label="预算计划">
            </el-table-column>
            <el-table-column
                prop="place"
                :show-overflow-tooltip="true"
                label="调研地点">
            </el-table-column>
            <el-table-column
                prop="purpose"
                :show-overflow-tooltip="true"
                label="调研目的">
            </el-table-column>
            <el-table-column
                prop="personnelType"
                label="人员类别"
                width="80">
            </el-table-column>
            <el-table-column
                prop="times"
                width="50"
                label="人次">
            </el-table-column>
            <el-table-column
                prop="transFee"
                width="100"
                label="交通费（元）">
            </el-table-column>
            <el-table-column
                prop="accommodationFee"
                width="90"
                label="住宿（元）">
            </el-table-column>
            <el-table-column
                prop="otherFee"
                width="100"
                label="公杂费（元）">
            </el-table-column>
            <el-table-column
                prop="days"
                width="50"
                label="天数">
            </el-table-column>
            <el-table-column
                prop="subTotal"
                width="70"
                label="合计（万元）">
            </el-table-column>
            <el-table-column
                prop="unfulfilledQuota"
                label="未执行额度（万元）"
                width="90">
            </el-table-column>
            <el-table-column
                prop="executeRate"
                label="执行率(%)"
                width="80">
            </el-table-column>
            <el-table-column
                width="80"
                label="操作">
              <template slot-scope="scope">
                <el-button type="primary" plain @click="showExecuteView(scope.row)">执行</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-table
              v-show="academicFeeList.length!=0"
              :data="academicFeeList"
              stripe
              border
              v-loading="loading"
              element-loading-text="正在加载..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)"
              style="width: 100%">
            <el-table-column
                prop="id"
                label="序号"
                width="50">
            </el-table-column>
            <el-table-column
                prop="projectName"
                :show-overflow-tooltip="true"
                label="科研项目">
            </el-table-column>
            <el-table-column
                prop="planName"
                :show-overflow-tooltip="true"
                label="预算计划">
            </el-table-column>
            <el-table-column
                prop="meetingName"
                :show-overflow-tooltip="true"
                label="会议名称">
            </el-table-column>
            <el-table-column
                prop="personnelType"
                :show-overflow-tooltip="true"
                label="人员类别"
                width="100">
            </el-table-column>
            <el-table-column
                prop="times"
                width="50"
                label="人次">
            </el-table-column>
            <el-table-column
                prop="year"
                width="50"
                label="年度">
            </el-table-column>
            <el-table-column
                prop="days"
                width="50"
                label="会期(天)">
            </el-table-column>
            <el-table-column
                prop="transFee"
                label="往返交通费（元）"
                width="90">
            </el-table-column>
            <el-table-column
                prop="accommodationFee"
                label="住宿及补助费（元）"
                width="100">
            </el-table-column>
            <el-table-column
                prop="subTotal"
                width="60"
                label="合计(万元)">
            </el-table-column>
            <el-table-column
                prop="unfulfilledQuota"
                label="未执行额度（万元）"
                width="90">
            </el-table-column>
            <el-table-column
                prop="executeRate"
                label="执行率(%)"
                width="80">
            </el-table-column>
            <el-table-column
                width="80"
                label="操作">
              <template slot-scope="scope">
                <el-button type="primary" plain @click="showExecuteView(scope.row)">执行</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-table
              v-show="transportationFeeList.length!=0"
              :data="transportationFeeList"
              stripe
              border
              v-loading="loading"
              element-loading-text="正在加载..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)"
              style="width: 100%">
            <el-table-column
                prop="id"
                label="序号"
                width="50">
            </el-table-column>
            <el-table-column
                prop="projectName"
                :show-overflow-tooltip="true"
                label="科研项目">
            </el-table-column>
            <el-table-column
                prop="planName"
                :show-overflow-tooltip="true"
                label="预算计划">
            </el-table-column>
            <el-table-column
                prop="remark"
                label="说明">
            </el-table-column>
            <el-table-column
                prop="subTotal"
                align="left"
                label="合计（万元）"
                width="100">
            </el-table-column>
            <el-table-column
                prop="unfulfilledQuota"
                label="未执行额度（万元）"
                width="130">
            </el-table-column>
            <el-table-column
                prop="executeRate"
                label="执行率(%)"
                width="100">
            </el-table-column>
            <el-table-column
                width="80"
                label="操作">
              <template slot-scope="scope">
                <el-button type="primary" plain @click="showExecuteView(scope.row)">执行</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-table
              v-show="publishFeeList.length!=0"
              :data="publishFeeList"
              stripe
              border
              v-loading="loading"
              element-loading-text="正在加载..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)"
              style="width: 100%">
            <el-table-column
                prop="id"
                label="序号"
                width="50">
            </el-table-column>
            <el-table-column
                prop="projectName"
                :show-overflow-tooltip="true"
                label="科研项目">
            </el-table-column>
            <el-table-column
                prop="planName"
                :show-overflow-tooltip="true"
                label="预算计划">
            </el-table-column>
            <el-table-column
                prop="type"
                :show-overflow-tooltip="true"
                label="类别">
            </el-table-column>
            <el-table-column
                prop="remark"
                label="说明">
            </el-table-column>
            <el-table-column
                prop="subTotal"
                align="left"
                label="合计（万元）"
                width="100">
            </el-table-column>
            <el-table-column
                prop="unfulfilledQuota"
                label="未执行额度（万元）"
                width="130">
            </el-table-column>
            <el-table-column
                prop="executeRate"
                label="执行率(%)"
                width="100">
            </el-table-column>
            <el-table-column
                width="80"
                label="操作">
              <template slot-scope="scope">
                <el-button type="primary" plain @click="showExecuteView(scope.row)">执行</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-table
              v-show="paymentList.length!=0"
              :data="paymentList"
              stripe
              border
              v-loading="loading"
              element-loading-text="正在加载..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)"
              style="width: 100%">
            <el-table-column
                prop="id"
                label="序号"
                width="50">
            </el-table-column>
            <el-table-column
                prop="projectName"
                :show-overflow-tooltip="true"
                label="科研项目">
            </el-table-column>
            <el-table-column
                prop="planName"
                :show-overflow-tooltip="true"
                label="预算计划">
            </el-table-column>
            <el-table-column
                prop="personnelType"
                :show-overflow-tooltip="true"
                width="80"
                label="人员种类">
            </el-table-column>
            <el-table-column
                prop="content"
                :show-overflow-tooltip="true"
                label="工作内容">
            </el-table-column>
            <el-table-column
                prop="amount"
                label="人数"
                width="50">
            </el-table-column>
            <el-table-column
                prop="standard"
                label="标准（元）"
                width="100">
            </el-table-column>
            <el-table-column
                prop="hours"
                label="工作时间"
                width="80">
            </el-table-column>
            <el-table-column
                prop="subTotal"
                label="合计（万元）"
                width="100">
            </el-table-column>
            <el-table-column
                prop="unfulfilledQuota"
                label="未执行额度（万元）"
                width="130">
            </el-table-column>
            <el-table-column
                prop="executeRate"
                label="执行率(%)"
                width="100">
            </el-table-column>
            <el-table-column
                width="80"
                label="操作">
              <template slot-scope="scope">
                <el-button type="primary" plain @click="showExecuteView(scope.row)">执行</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-table
              v-show="advisoryList.length!=0"
              :data="advisoryList"
              stripe
              border
              v-loading="loading"
              element-loading-text="正在加载..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)"
              style="width: 100%">
            <el-table-column
                prop="id"
                label="序号"
                width="50">
            </el-table-column>
            <el-table-column
                prop="projectName"
                :show-overflow-tooltip="true"
                label="科研项目">
            </el-table-column>
            <el-table-column
                prop="planName"
                :show-overflow-tooltip="true"
                label="预算计划">
            </el-table-column>
            <el-table-column
                prop="content"
                :show-overflow-tooltip="true"
                label="咨询内容">
            </el-table-column>
            <el-table-column
                prop="personnelType"
                align="left"
                label="人员类型"
                width="80">
            </el-table-column>
            <el-table-column
                prop="standard"
                label="标准（元/天）"
                width="100">
            </el-table-column>
            <el-table-column
                prop="amount"
                label="人数"
                width="50">
            </el-table-column>
            <el-table-column
                prop="days"
                label="天数"
                width="50">
            </el-table-column>
            <el-table-column
                prop="subTotal"
                label="总计（万元）"
                width="100">
            </el-table-column>
            <el-table-column
                prop="unfulfilledQuota"
                label="未执行额度（万元）"
                width="130">
            </el-table-column>
            <el-table-column
                prop="executeRate"
                label="执行率(%)"
                width="100">
            </el-table-column>
            <el-table-column
                width="80"
                label="操作">
              <template slot-scope="scope">
                <el-button type="primary" plain @click="showExecuteView(scope.row)">执行</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-table
              v-show="otherFeeList.length!=0"
              :data="otherFeeList"
              stripe
              border
              v-loading="loading"
              element-loading-text="正在加载..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)"
              style="width: 100%">
            <el-table-column
                prop="id"
                label="序号"
                width="50">
            </el-table-column>
            <el-table-column
                prop="projectName"
                :show-overflow-tooltip="true"
                label="科研项目">
            </el-table-column>
            <el-table-column
                prop="planName"
                :show-overflow-tooltip="true"
                label="预算计划">
            </el-table-column>
            <el-table-column
                prop="type"
                :show-overflow-tooltip="true"
                label="类别">
            </el-table-column>
            <el-table-column
                prop="remark"
                label="说明">
            </el-table-column>
            <el-table-column
                prop="subTotal"
                label="合计（万元）"
                width="100">
            </el-table-column>
            <el-table-column
                prop="unfulfilledQuota"
                label="未执行额度（万元）"
                width="130">
            </el-table-column>
            <el-table-column
                prop="executeRate"
                label="执行率(%)"
                width="100">
            </el-table-column>
            <el-table-column
                width="80"
                label="操作">
              <template slot-scope="scope">
                <el-button type="primary" plain @click="showExecuteView(scope.row)">执行</el-button>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="$emit('close')">关 闭</el-button>
  </span>
    </el-dialog>

    <ExecutePlan v-on:close="executePlanVisible = false"
                 v-on:freshPlan="freshPlan"
                 :planItem="planItem"
                 :executePlanVisible="executePlanVisible"
                 :title="title"></ExecutePlan>
  </div>

</template>

<script>

import ExecutePlan from "@/components/budget/ExecutePlan";
import ExecuteDetail from "@/components/budget/ExecuteDetail";

export default {
  name: "ExecuteSelect",
  props: ['title', 'executeSelectVisible'],
  components: {
    ExecutePlan,
    ExecuteDetail
  },
  data() {
    return {
      executePlanVisible: false,
      planItemTypes: [
        {
          id: 1,
          name: '设备费'
        },
        {
          id: 2,
          name: '材料费'
        },
        {
          id: 3,
          name: '测试化验加工费'
        },
        {
          id: 4,
          name: '会议费'
        },
        {
          id: 5,
          name: '课题调研差旅费'
        },
        {
          id: 6,
          name: '学术会议差旅费'
        },
        {
          id: 7,
          name: '北京市内交通费'
        },
        {
          id: 8,
          name: '出版/文献/信息传播/知识产权事务费'
        },
        {
          id: 9,
          name: '劳务费'
        },
        {
          id: 10,
          name: '专家咨询费'
        },
        {
          id: 11,
          name: '其他支出'
        },
      ],
      searchValue: {
        itemType: null
      },
      loading: false,
      equipmentFeeList: [],
      materialFeeList: [],
      testingFeeList: [],
      meetingFeeList: [],
      researchFeeList: [],
      academicFeeList: [],
      transportationFeeList: [],
      publishFeeList: [],
      paymentList: [],
      advisoryList: [],
      otherFeeList: [],
      planItem: {},
      budgetRecordList: [],
      itemUrl: "",
      planList: [],
      rules: {
        itemType: [{required: true, message: '请选择预算科目', trigger: 'blur'}]
      },
    }
  },
  mounted() {
    this.initPlans();
  },
  methods: {
    getItemUrl() {
      if (this.searchValue.itemType == 1) {
        this.itemUrl = 'equipmentFee'
      } else if (this.searchValue.itemType == 2) {
        this.itemUrl = 'materialFee'
      } else if (this.searchValue.itemType == 3) {
        this.itemUrl = 'testingFee'
      } else if (this.searchValue.itemType == 4) {
        this.itemUrl = 'meetingFee'
      } else if (this.searchValue.itemType == 5) {
        this.itemUrl = 'researchFee'
      } else if (this.searchValue.itemType == 6) {
        this.itemUrl = 'academicFee'
      } else if (this.searchValue.itemType == 7) {
        this.itemUrl = 'transportationFee'
      } else if (this.searchValue.itemType == 7) {
        this.itemUrl = 'publishFee'
      } else if (this.searchValue.itemType == 9) {
        this.itemUrl = 'payment'
      } else if (this.searchValue.itemType == 10) {
        this.itemUrl = 'advisory'
      } else if (this.searchValue.itemType == 11) {
        this.itemUrl = 'otherFee'
      }
    },
    emptyList() {
      this.equipmentFeeList = [];
      this.materialFeeList = [];
      this.testingFeeList = [];
      this.meetingFeeList = [];
      this.researchFeeList = [];
      this.academicFeeList = [];
      this.transportationFeeList = [];
      this.publishFeeList = [];
      this.paymentList = [];
      this.advisoryList = [];
      this.otherFeeList = [];
    },
    initItems() {
      this.$refs['searchValueForm'].validate(valid => {
        if (valid) {
          this.loading = true;
          this.getItemUrl();
          let url = "/planItem/" + this.itemUrl + "/getRecommend";
          this.getRequest(url).then(resp => {
            this.loading = false;
            if (resp) {
              this.emptyList();
              if (this.searchValue.itemType == 1) {
                this.equipmentFeeList = resp.obj;
              } else if (this.searchValue.itemType == 2) {
                this.materialFeeList = resp.obj;
              } else if (this.searchValue.itemType == 3) {
                this.testingFeeList = resp.obj;
              } else if (this.searchValue.itemType == 4) {
                this.meetingFeeList = resp.obj;
              } else if (this.searchValue.itemType == 5) {
                this.researchFeeList = resp.obj;
              } else if (this.searchValue.itemType == 6) {
                this.academicFeeList = resp.obj;
              } else if (this.searchValue.itemType == 7) {
                this.transportationFeeList = resp.obj;
              } else if (this.searchValue.itemType == 8) {
                this.publishFeeList = resp.obj;
              } else if (this.searchValue.itemType == 9) {
                this.paymentList = resp.obj;
              } else if (this.searchValue.itemType == 10) {
                this.advisoryList = resp.obj;
              } else if (this.searchValue.itemType == 11) {
                this.otherFeeList = resp.obj;
              }
            }
          })
        }
      });
    },
    initPlans() {
      let url = '/plan/get/all';
      this.getRequest(url).then(resp => {
        if (resp) {
          this.planList = resp.obj;
        }
      });
    },
    freshPlan(id) {
    },
    initBudgetRecordList() {
      this.loading = true;
      let url = '/execution/item/get';
      this.postRequest(url, this.planItem).then(resp => {
        this.loading = false;
        if (resp) {
          this.budgetRecordList = resp.obj;
        }
      });
    },
    showExecuteView(data) {
      this.title = '预算执行信息';
      this.planItem = data;
      this.executePlanVisible = true;
    },
    handleClose() {
      this.$emit('close');
    }
  }
}
</script>

<style scoped>

</style>
<template>
  <div>
    <el-dialog
        :title="title"
        :visible.sync="planDetailVisible"
        :before-close="handleClose"
        width="80%">
      <div>
        <el-form :model="plan" :rules="rules" ref="planForm">
          <el-row>
            <el-col :span="20">
              <el-form-item label="预算计划名称:" prop="planName">
                {{ plan.planName }}
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="15">
              <el-form-item label="科研项目:" prop="projectId">
                {{ plan.projectName }}
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="5">
              <el-form-item label="本计划预算（万元）:" prop="funding">
                {{ plan.funding }}
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="5">
              <el-form-item label="未执行额度（万元）:" prop="unfulfilledQuota">
                {{ plan.unfulfilledQuota }}
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="1.5">
              <el-form-item label="执行率:" prop="executeRate"></el-form-item>
            </el-col>
            <el-col :span="22">
              <el-progress :text-inside="true" :stroke-width="10" :percentage=plan.executeRate></el-progress>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="20">
              <el-form-item label="备注:" prop="remark">
                {{ plan.remark }}
              </el-form-item>
            </el-col>
          </el-row>
          <el-collapse>
            <el-collapse-item title="设备费" name="1">
              <el-tag>金额: {{ plan.planItemDataMap.equipmentFeeData.funding }}（万元）</el-tag>
              <el-tag type="success">未执行额度: {{ plan.planItemDataMap.equipmentFeeData.unfulfilledQuota }}（万元）</el-tag>
              <el-tag type="info">执行率: {{ plan.planItemDataMap.equipmentFeeData.executeRate }}%</el-tag>
              <el-progress :text-inside="true" :stroke-width="10"
                           :percentage=plan.planItemDataMap.equipmentFeeData.executeRate></el-progress>
              <el-table
                  :data="plan.planItemDataMap.equipmentFeeData.planItemList"
                  @selection-change="handleSelectionChange"
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
                    width="150">
                </el-table-column>
                <el-table-column
                    prop="executeRate"
                    label="执行率"
                    width="100">
                </el-table-column>
                <el-table-column
                    width="150"
                    label="操作">
                  <template slot-scope="scope">
                    <el-button @click="showExecuteDetailView(scope.row)">详情</el-button>
                    <el-button type="primary" plain @click="showExecuteView(scope.row)">执行</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-collapse-item>
            <el-collapse-item title="材料费" name="2">
              <el-tag>金额: {{ plan.planItemDataMap.materialFeeData.funding }}（万元）</el-tag>
              <el-tag type="success">未执行额度: {{ plan.planItemDataMap.materialFeeData.unfulfilledQuota }}（万元）</el-tag>
              <el-tag type="info">执行率: {{ plan.planItemDataMap.materialFeeData.executeRate }}%</el-tag>
              <el-progress :text-inside="true" :stroke-width="10"
                           :percentage=plan.planItemDataMap.materialFeeData.executeRate></el-progress>
              <el-table
                  :data="plan.planItemDataMap.materialFeeData.planItemList"
                  @selection-change="handleSelectionChange"
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
                    width="150">
                </el-table-column>
                <el-table-column
                    prop="executeRate"
                    label="执行率"
                    width="100">
                </el-table-column>
                <el-table-column
                    width="150"
                    label="操作">
                  <template slot-scope="scope">
                    <el-button @click="showExecuteDetailView(scope.row)">详情</el-button>
                    <el-button type="primary" plain @click="showExecuteView(scope.row)">执行</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-collapse-item>
            <el-collapse-item title="测试化验加工费" name="3">
              <el-tag>金额: {{ plan.planItemDataMap.testingFeeData.funding }}（万元）</el-tag>
              <el-tag type="success">未执行额度: {{ plan.planItemDataMap.testingFeeData.unfulfilledQuota }}（万元）</el-tag>
              <el-tag type="info">执行率: {{ plan.planItemDataMap.testingFeeData.executeRate }}%</el-tag>
              <el-progress :text-inside="true" :stroke-width="10"
                           :percentage=plan.planItemDataMap.testingFeeData.executeRate></el-progress>
              <el-table
                  :data="plan.planItemDataMap.testingFeeData.planItemList"
                  @selection-change="handleSelectionChange"
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
                    label="执行率"
                    width="100">
                </el-table-column>
                <el-table-column
                    width="150"
                    label="操作">
                  <template slot-scope="scope">
                    <el-button @click="showExecuteDetailView(scope.row)">详情</el-button>
                    <el-button type="primary" plain @click="showExecuteView(scope.row)">执行</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-collapse-item>
            <el-collapse-item title="差旅/会议/国际合作与交流费" name="4">
              <el-tag type="warning">会议费</el-tag>
              <el-tag>金额: {{ plan.planItemDataMap.meetingFeeData.funding }}（万元）</el-tag>
              <el-tag type="success">未执行额度: {{ plan.planItemDataMap.meetingFeeData.unfulfilledQuota }}（万元）</el-tag>
              <el-tag type="info">执行率: {{ plan.planItemDataMap.meetingFeeData.executeRate }}%</el-tag>
              <el-progress :text-inside="true" :stroke-width="10"
                           :percentage=plan.planItemDataMap.meetingFeeData.executeRate></el-progress>
              <el-table
                  :data="plan.planItemDataMap.meetingFeeData.planItemList"
                  @selection-change="handleSelectionChange"
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
                    prop="content"
                    label="会议内容">
                </el-table-column>
                <el-table-column
                    prop="relation"
                    label="与课题相关性">
                </el-table-column>
                <el-table-column
                    prop="meal"
                    label="伙食费（人/天）"
                    width="120">
                </el-table-column>
                <el-table-column
                    prop="otherCost"
                    label="其他费用（人/天）"
                    width="130">
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
                    width="150"
                    label="操作">
                  <template slot-scope="scope">
                    <el-button @click="showExecuteDetailView(scope.row)">详情</el-button>
                    <el-button type="primary" plain @click="showExecuteView(scope.row)">执行</el-button>
                  </template>
                </el-table-column>
              </el-table>
              <el-tag type="warning">课题调研差旅费</el-tag>
              <el-tag>金额: {{ plan.planItemDataMap.researchFeeData.funding }}（万元）</el-tag>
              <el-tag type="success">未执行额度: {{ plan.planItemDataMap.researchFeeData.unfulfilledQuota }}（万元）</el-tag>
              <el-tag type="info">执行率: {{ plan.planItemDataMap.researchFeeData.executeRate }}%</el-tag>
              <el-progress :text-inside="true" :stroke-width="10"
                           :percentage=plan.planItemDataMap.researchFeeData.executeRate></el-progress>
              <el-table
                  :data="plan.planItemDataMap.researchFeeData.planItemList"
                  @selection-change="handleSelectionChange"
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
                    width="100"
                    label="合计（万元）">
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
                    width="150"
                    label="操作">
                  <template slot-scope="scope">
                    <el-button @click="showExecuteDetailView(scope.row)">详情</el-button>
                    <el-button type="primary" plain @click="showExecuteView(scope.row)">执行</el-button>
                  </template>
                </el-table-column>
              </el-table>
              <el-tag type="warning">国内学术会议差旅费</el-tag>
              <el-tag>金额: {{ plan.planItemDataMap.academicFeeData.funding }}（万元）</el-tag>
              <el-tag type="success">未执行额度: {{ plan.planItemDataMap.academicFeeData.unfulfilledQuota }}（万元）</el-tag>
              <el-tag type="info">执行率: {{ plan.planItemDataMap.academicFeeData.executeRate }}%</el-tag>
              <el-progress :text-inside="true" :stroke-width="10"
                           :percentage=plan.planItemDataMap.academicFeeData.executeRate></el-progress>
              <el-table
                  :data="plan.planItemDataMap.academicFeeData.planItemList"
                  @selection-change="handleSelectionChange"
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
                    prop="meetingName"
                    label="会议名称">
                </el-table-column>
                <el-table-column
                    prop="personnelType"
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
                    width="80"
                    label="会期（天）">
                </el-table-column>
                <el-table-column
                    prop="transFee"
                    label="往返交通费（元）"
                    width="150">
                </el-table-column>
                <el-table-column
                    prop="accommodationFee"
                    label="住宿及补助费（元）"
                    width="150">
                </el-table-column>
                <el-table-column
                    prop="subTotal"
                    width="100"
                    label="合计（万元）">
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
                    width="150"
                    label="操作">
                  <template slot-scope="scope">
                    <el-button @click="showExecuteDetailView(scope.row)">详情</el-button>
                    <el-button type="primary" plain @click="showExecuteView(scope.row)">执行</el-button>
                  </template>
                </el-table-column>
              </el-table>
              <el-tag type="warning">北京市内交通费</el-tag>
              <el-tag>金额: {{ plan.planItemDataMap.transFeeData.funding }}（万元）</el-tag>
              <el-tag type="success">未执行额度: {{ plan.planItemDataMap.transFeeData.unfulfilledQuota }}（万元）</el-tag>
              <el-tag type="info">执行率: {{ plan.planItemDataMap.transFeeData.executeRate }}%</el-tag>
              <el-progress :text-inside="true" :stroke-width="10"
                           :percentage=plan.planItemDataMap.transFeeData.executeRate></el-progress>
              <el-table
                  :data="plan.planItemDataMap.transFeeData.planItemList"
                  @selection-change="handleSelectionChange"
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
                    width="150"
                    label="操作">
                  <template slot-scope="scope">
                    <el-button @click="showExecuteDetailView(scope.row)">详情</el-button>
                    <el-button type="primary" plain @click="showExecuteView(scope.row)">执行</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-collapse-item>
            <el-collapse-item title="出版/文献/信息传播/知识产权事务费" name="5">
              <el-tag>金额: {{ plan.planItemDataMap.publishFeeData.funding }}（万元）</el-tag>
              <el-tag type="success">未执行额度: {{ plan.planItemDataMap.publishFeeData.unfulfilledQuota }}（万元）</el-tag>
              <el-tag type="info">执行率: {{ plan.planItemDataMap.publishFeeData.executeRate }}%</el-tag>
              <el-progress :text-inside="true" :stroke-width="10"
                           :percentage=plan.planItemDataMap.publishFeeData.executeRate></el-progress>
              <el-table
                  :data="plan.planItemDataMap.publishFeeData.planItemList"
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
                    width="150"
                    label="操作">
                  <template slot-scope="scope">
                    <el-button @click="showExecuteDetailView(scope.row)">详情</el-button>
                    <el-button type="primary" plain @click="showExecuteView(scope.row)">执行</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-collapse-item>
            <el-collapse-item title="劳务费" name="6">
              <el-tag>金额: {{ plan.planItemDataMap.paymentData.funding }}（万元）</el-tag>
              <el-tag type="success">未执行额度: {{ plan.planItemDataMap.paymentData.unfulfilledQuota }}（万元）</el-tag>
              <el-tag type="info">执行率: {{ plan.planItemDataMap.paymentData.executeRate }}%</el-tag>
              <el-progress :text-inside="true" :stroke-width="10"
                           :percentage=plan.planItemDataMap.paymentData.executeRate></el-progress>
              <el-table
                  :data="plan.planItemDataMap.paymentData.planItemList"
                  @selection-change="handleSelectionChange"
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
                    width="150"
                    label="操作">
                  <template slot-scope="scope">
                    <el-button @click="showExecuteDetailView(scope.row)">详情</el-button>
                    <el-button type="primary" plain @click="showExecuteView(scope.row)">执行</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-collapse-item>
            <el-collapse-item title="专家咨询费" name="7">
              <el-tag>金额: {{ plan.planItemDataMap.advisoryData.funding }}（万元）</el-tag>
              <el-tag type="success">未执行额度: {{ plan.planItemDataMap.advisoryData.unfulfilledQuota }}（万元）</el-tag>
              <el-tag type="info">执行率: {{ plan.planItemDataMap.advisoryData.executeRate }}%</el-tag>
              <el-progress :text-inside="true" :stroke-width="10"
                           :percentage=plan.planItemDataMap.advisoryData.executeRate></el-progress>
              <el-table
                  :data="plan.planItemDataMap.advisoryData.planItemList"
                  @selection-change="handleSelectionChange"
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
                    width="150"
                    label="操作">
                  <template slot-scope="scope">
                    <el-button @click="showExecuteDetailView(scope.row)">详情</el-button>
                    <el-button type="primary" plain @click="showExecuteView(scope.row)">执行</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-collapse-item>
            <el-collapse-item title="其他支出" name="8">
              <el-tag>金额: {{ plan.planItemDataMap.otherFeeData.funding }}（万元）</el-tag>
              <el-tag type="success">未执行额度: {{ plan.planItemDataMap.otherFeeData.unfulfilledQuota }}（万元）</el-tag>
              <el-tag type="info">执行率: {{ plan.planItemDataMap.otherFeeData.executeRate }}%</el-tag>
              <el-progress :text-inside="true" :stroke-width="10"
                           :percentage=plan.planItemDataMap.otherFeeData.executeRate></el-progress>
              <el-table
                  :data="plan.planItemDataMap.otherFeeData.planItemList"
                  @selection-change="handleSelectionChange"
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
                    width="150"
                    label="操作">
                  <template slot-scope="scope">
                    <el-button @click="showExecuteDetailView(scope.row)">详情</el-button>
                    <el-button type="primary" plain @click="showExecuteView(scope.row)">执行</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </el-collapse-item>
          </el-collapse>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="handleClose">取 消</el-button>
      </span>
    </el-dialog>
    <ExecutePlan v-on:close="executePlanVisible = false"
                 v-on:freshPlan="freshPlan"
                 :plan="plan"
                 :planItem="planItem"
                 :executePlanVisible="executePlanVisible"
                 :title="title"></ExecutePlan>
    <ExecuteDetail v-on:close="executeDetailVisible = false"
                   v-on:freshPlan="freshPlan"
                   :planId="plan.id"
                   :executeDetailVisible="executeDetailVisible"
                   :budgetRecordList="budgetRecordList"
                   :title="title"></ExecuteDetail>
  </div>
</template>
<script>
import ExecutePlan from "@/components/budget/ExecutePlan";
import ExecuteDetail from "@/components/budget/ExecuteDetail";

export default {
  name: "ProjectDetail",
  props: ['plan', 'title', 'planDetailVisible'],
  components: {
    ExecutePlan,
    ExecuteDetail
  },
  data() {
    return {
      budgetRecordList: [],
      loading: false,
      title: '',
      planItem: {
        id: null,
        itemType: "",
        itemTypeName: "",
        remark: '',
        subTotal: 0
      },
      executePlanVisible: false,
      executeDetailVisible: false
    }
  },
  methods: {
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
    showExecuteDetailView(data) {
      this.planItem = data;
      this.initBudgetRecordList();
      this.title = '预算执行记录';
      this.executeDetailVisible = true;
    },
    handleClose() {
      this.$emit('close');
      this.$parent.initPlans();
    },
    freshPlan(id) {
      this.loading = true;
      this.$emit('freshPlan', id);
      this.loading = false;
    }
  }
}
</script>

<style scoped>
</style>
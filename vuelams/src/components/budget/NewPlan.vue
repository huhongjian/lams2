<template>
  <div>
    <el-dialog
        :title="title"
        :visible.sync="newPlanVisible"
        :before-close="handleClose"
        width="80%">
      <div>
        <el-form :model="plan" :rules="rules" ref="planForm">
          <el-row>
            <el-form-item label="预算计划名称:" prop="planName">
              <el-input size="mini" style="width: 800px" prefix-icon="el-icon-edit"
                        v-model="plan.planName"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-col :span="15">
              <el-form-item label="科研项目:" prop="projectId">
                <el-select style="width: 500px" v-model="plan.projectId" clearable placeholder="请选择归属的科研项目">
                  <el-option
                      v-for="item in projectLists"
                      :key="item.id"
                      :label="item.projectName"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="本计划预算（万元）:" prop="funding">
                {{ plan.funding }}
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="20">
              <el-form-item label="备注:" prop="remark">
                <el-input size="mini"
                          type="textarea"
                          :rows="2"
                          placeholder="请输入预算计划备注说明"
                          v-model="plan.remark"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-button type="primary" icon="el-icon-plus" @click="showAddView">
              新增预算明细
            </el-button>
            <el-button type="success" style="display: inline-flex;margin-left: 8px" @click="exportData"
                       icon="el-icon-download">
              导入数据
            </el-button>
            <el-button type="success" style="display: inline-flex;margin-left: 8px" @click="exportData"
                       icon="el-icon-download">
              下载模板
            </el-button>
          </el-row>
        </el-form>
        <el-collapse>
          <el-collapse-item title="设备费" name="1">
            <el-tag>金额: {{ plan.planItemDataMap.equipmentFeeData.funding }}（万元）</el-tag>
            <el-table
                :data="plan.planItemDataMap.equipmentFeeData.planItemList"
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
                  align="left"
                  width="80">
              </el-table-column>
              <el-table-column
                  prop="equipmentName"
                  :show-overflow-tooltip="true"
                  label="设备名称">
              </el-table-column>
              <el-table-column
                  prop="usage"
                  :show-overflow-tooltip="true"
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
                  width="80">
              </el-table-column>
              <el-table-column
                  prop="subTotal"
                  label="合计（万元）"
                  width="100">
              </el-table-column>
              <el-table-column
                  prop="remark"
                  :show-overflow-tooltip="true"
                  label="备注">
              </el-table-column>
              <el-table-column
                  width="80"
                  label="操作">
                <template slot-scope="scope">
                  <el-button type="danger" plain @click="deleteEquipmentFee(scope.row.id)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-collapse-item>
          <el-collapse-item title="材料费" name="2">
            <el-tag>金额: {{ plan.planItemDataMap.materialFeeData.funding }}（万元）</el-tag>
            <el-table
                :data="plan.planItemDataMap.materialFeeData.planItemList"
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
                  align="left"
                  width="80">
              </el-table-column>
              <el-table-column
                  prop="materialName"
                  :show-overflow-tooltip="true"
                  label="材料用途">
              </el-table-column>
              <el-table-column
                  prop="specification"
                  label="规格"
                  width="200">
              </el-table-column>
              <el-table-column
                  prop="amount"
                  label="数量"
                  width="80">
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
                  label="小计（万元）"
                  width="100">
              </el-table-column>
              <el-table-column
                  prop="remark"
                  :show-overflow-tooltip="true"
                  label="备注">
              </el-table-column>
              <el-table-column
                  width="80"
                  label="操作">
                <template slot-scope="scope">
                  <el-button type="danger" plain @click="deleteMaterialFee(scope.row.id)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-collapse-item>
          <el-collapse-item title="测试化验加工费" name="3">
            <el-tag>金额: {{ plan.planItemDataMap.testingFeeData.funding }}（万元）</el-tag>
            <el-table
                :data="plan.planItemDataMap.testingFeeData.planItemList"
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
                  width="100">
              </el-table-column>
              <el-table-column
                  prop="content"
                  :show-overflow-tooltip="true"
                  label="加工内容">
              </el-table-column>
              <el-table-column
                  prop="times"
                  align="left"
                  label="次数"
                  width="100">
              </el-table-column>
              <el-table-column
                  prop="unitPrice"
                  align="left"
                  label="单价（万元）"
                  width="100">
              </el-table-column>
              <el-table-column
                  prop="subTotal"
                  align="left"
                  label="合计（万元）"
                  width="100">
              </el-table-column>
              <el-table-column
                  prop="remark"
                  :show-overflow-tooltip="true"
                  label="备注">
              </el-table-column>
              <el-table-column
                  width="80"
                  label="操作">
                <template slot-scope="scope">
                  <el-button type="danger" plain @click="deleteTestingFee(scope.row.id)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-collapse-item>
          <el-collapse-item title="差旅/会议/国际合作与交流费" name="4">
            <el-tag type="warning">会议费</el-tag>
            <el-tag>金额: {{ plan.planItemDataMap.meetingFeeData.funding }}（万元）</el-tag>
            <el-table
                :data="plan.planItemDataMap.meetingFeeData.planItemList"
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
                  align="left"
                  width="50">
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
                  width="80">
              </el-table-column>
              <el-table-column
                  prop="subTotal"
                  label="合计（万元）"
                  width="100">
              </el-table-column>
              <el-table-column
                  prop="remark"
                  :show-overflow-tooltip="true"
                  label="备注">
              </el-table-column>
              <el-table-column
                  width="80"
                  label="操作">
                <template slot-scope="scope">
                  <el-button type="danger" plain @click="deleteMeetingFee(scope.row.id)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-tag type="warning">课题调研差旅费</el-tag>
            <el-tag>金额: {{ plan.planItemDataMap.researchFeeData.funding }}（万元）</el-tag>
            <el-table
                :data="plan.planItemDataMap.researchFeeData.planItemList"
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
                  label="人次"
                  width="50">
              </el-table-column>
              <el-table-column
                  prop="transFee"
                  label="交通费（元）"
                  width="100">
              </el-table-column>
              <el-table-column
                  prop="accommodationFee"
                  label="住宿（元）"
                  width="90">
              </el-table-column>
              <el-table-column
                  prop="otherFee"
                  label="公杂费（元）"
                  width="100">
              </el-table-column>
              <el-table-column
                  prop="days"
                  label="天数"
                  width="50">
              </el-table-column>
              <el-table-column
                  prop="subTotal"
                  label="合计（万元）"
                  width="100">
              </el-table-column>
              <el-table-column
                  prop="remark"
                  :show-overflow-tooltip="true"
                  label="备注">
              </el-table-column>
              <el-table-column
                  width="80"
                  label="操作">
                <template slot-scope="scope">
                  <el-button type="danger" plain @click="deleteResearchFee(scope.row.id)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-tag type="warning">国内学术会议差旅费</el-tag>
            <el-tag>金额: {{ plan.planItemDataMap.academicFeeData.funding }}（万元）</el-tag>
            <el-table
                :data="plan.planItemDataMap.academicFeeData.planItemList"
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
                  :show-overflow-tooltip="true"
                  label="会议名称">
              </el-table-column>
              <el-table-column
                  prop="personnelType"
                  label="人员类别"
                  width="80">
              </el-table-column>
              <el-table-column
                  prop="times"
                  label="人次"
                  width="50">
              </el-table-column>
              <el-table-column
                  prop="year"
                  label="年度"
                  width="50">
              </el-table-column>
              <el-table-column
                  prop="days"
                  label="会期（天）"
                  width="90">
              </el-table-column>
              <el-table-column
                  prop="registrationFee"
                  label="注册费（元）"
                  width="100">
              </el-table-column>
              <el-table-column
                  prop="transFee"
                  label="往返交通费（元）"
                  width="120">
              </el-table-column>
              <el-table-column
                  prop="accommodationFee"
                  label="住宿及补助费（元）"
                  width="150">
              </el-table-column>
              <el-table-column
                  prop="subTotal"
                  label="合计（万元）"
                  width="100">
              </el-table-column>
              <el-table-column
                  prop="remark"
                  :show-overflow-tooltip="true"
                  label="备注">
              </el-table-column>
              <el-table-column
                  width="80"
                  label="操作">
                <template slot-scope="scope">
                  <el-button type="danger" plain @click="deleteAcademicFee(scope.row.id)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-tag type="warning">北京市内交通费</el-tag>
            <el-tag>金额: {{ plan.planItemDataMap.transFeeData.funding }}（万元）</el-tag>
            <el-table
                :data="plan.planItemDataMap.transFeeData.planItemList"
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
                  :show-overflow-tooltip="true"
                  label="说明">
              </el-table-column>
              <el-table-column
                  prop="subTotal"
                  label="合计（万元）"
                  width="100">
              </el-table-column>
              <el-table-column
                  prop="remark"
                  :show-overflow-tooltip="true"
                  label="备注">
              </el-table-column>
              <el-table-column
                  width="80"
                  label="操作">
                <template slot-scope="scope">
                  <el-button type="danger" plain @click="deleteTransportationFee(scope.row.id)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-collapse-item>
          <el-collapse-item title="出版/文献/信息传播/知识产权事务费" name="5">
            <el-tag>金额: {{ plan.planItemDataMap.publishFeeData.funding }}（万元）</el-tag>
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
                  :show-overflow-tooltip="true"
                  label="说明">
              </el-table-column>
              <el-table-column
                  prop="subTotal"
                  label="合计（万元）"
                  width="100">
              </el-table-column>
              <el-table-column
                  width="80"
                  label="操作">
                <template slot-scope="scope">
                  <el-button type="danger" plain @click="deletePublishFee(scope.row.id)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-collapse-item>
          <el-collapse-item title="劳务费" name="6">
            <el-tag>金额: {{ plan.planItemDataMap.paymentData.funding }}（万元）</el-tag>
            <el-table
                :data="plan.planItemDataMap.paymentData.planItemList"
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
                  label="人员种类"
                  width="100">
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
                  prop="remark"
                  :show-overflow-tooltip="true"
                  label="备注">
              </el-table-column>
              <el-table-column
                  width="80"
                  label="操作">
                <template slot-scope="scope">
                  <el-button type="danger" plain @click="deletePublishFee(scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-collapse-item>
          <el-collapse-item title="专家咨询费" name="7">
            <el-tag>金额: {{ plan.planItemDataMap.advisoryData.funding }}（万元）</el-tag>
            <el-table
                :data="plan.planItemDataMap.advisoryData.planItemList"
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
                  label="人员类型"
                  width="100">
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
                  prop="remark"
                  :show-overflow-tooltip="true"
                  label="备注">
              </el-table-column>
              <el-table-column
                  width="80"
                  label="操作">
                <template slot-scope="scope">
                  <el-button type="danger" plain @click="deleteAdvisory(scope.row.id)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-collapse-item>
          <el-collapse-item title="其他支出" name="8">
            <el-tag>金额: {{ plan.planItemDataMap.otherFeeData.funding }}（万元）</el-tag>
            <el-table
                :data="plan.planItemDataMap.otherFeeData.planItemList"
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
                  :show-overflow-tooltip="true"
                  label="说明">
              </el-table-column>
              <el-table-column
                  prop="subTotal"
                  align="left"
                  label="合计（万元）"
                  width="100">
              </el-table-column>
              <el-table-column
                  width="80"
                  label="操作">
                <template slot-scope="scope">
                  <el-button type="danger" plain @click="deleteOtherFee(scope.row.id)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-collapse-item>
        </el-collapse>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="handleClose">取 消</el-button>
    <el-button type="primary" @click="doAddPlan">确 定</el-button>
  </span>
    </el-dialog>
    <NewPlanItem v-on:close="newPlanItemVisible = false"
                 v-on:handlePlanItemIds="handlePlanItemIds"
                 :planItem="planItem"
                 :newPlanItemVisible="newPlanItemVisible" :title="planItemTitle"></NewPlanItem>
  </div>
</template>

<script>
import NewPlanItem from "@/components/budget/NewPlanItem";

export default {
  name: "NewPlan",
  props: ['plan', 'title', 'newPlanVisible', 'projectLists'],
  data() {
    return {
      // 预算明细类型id数组
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
      // 初始化资产信息
      initData: {
        // 添加的全部资产id
        aids: [],
        page: 1,
        size: 10
      },
      // 选中的资产id
      assetIds: [],
      // 资产图片列表，用于编辑页面
      fileList: [],
      // 资产图片url列表，用于详情页面
      urlList: [],
      newPlanItemVisible: false,
      dialogVisible2: false,
      planItemTitle: '',
      loading: false,
      planItem: {
        id: null,
        itemType: null,
        remark: '',
        subTotal: 0
      },
      total: 0,
      page: 1,
      size: 10,
      planItemTypes: [],
      rules: {
        planName: [{required: true, message: '请输入计划名称', trigger: 'blur'}],
        projectId: [{required: true, message: '请选择项目', trigger: 'blur'}]
      },
    }
  },
  components: {
    NewPlanItem
  },
  methods: {
    emptyIdList() {
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
    initPlanItemList() {
      this.loading = true;
      this.plan.funding = 0;
      this.postRequest('/planItem/equipmentFee/getByIds', this.equipmentFeeList).then(resp => {
        if (resp && resp.obj) {
          this.plan.planItemDataMap.equipmentFeeData = resp.obj;
          this.plan.funding += resp.obj.funding;
        }
      });
      this.postRequest('/planItem/materialFee/getByIds', this.materialFeeList).then(resp => {
        if (resp && resp.obj) {
          this.plan.planItemDataMap.materialFeeData = resp.obj;
          this.plan.funding += resp.obj.funding;
        }
      });
      this.postRequest('/planItem/testingFee/getByIds', this.testingFeeList).then(resp => {
        if (resp && resp.obj) {
          this.plan.planItemDataMap.testingFeeData = resp.obj;
          this.plan.funding += resp.obj.funding;
        }
      });
      this.postRequest('/planItem/meetingFee/getByIds', this.meetingFeeList).then(resp => {
        if (resp && resp.obj) {
          this.plan.planItemDataMap.meetingFeeData = resp.obj;
          this.plan.funding += resp.obj.funding;
        }
      });
      this.postRequest('/planItem/researchFee/getByIds', this.researchFeeList).then(resp => {
        if (resp && resp.obj) {
          this.plan.planItemDataMap.researchFeeData = resp.obj;
          this.plan.funding += resp.obj.funding;
        }
      });
      this.postRequest('/planItem/academicFee/getByIds', this.academicFeeList).then(resp => {
        if (resp && resp.obj) {
          this.plan.planItemDataMap.academicFeeData = resp.obj;
          this.plan.funding += resp.obj.funding;
        }
      });
      this.postRequest('/planItem/transportationFee/getByIds', this.transportationFeeList).then(resp => {
        if (resp && resp.obj) {
          this.plan.planItemDataMap.transFeeData = resp.obj;
          this.plan.funding += resp.obj.funding;
        }
      });
      this.postRequest('/planItem/publishFee/getByIds', this.publishFeeList).then(resp => {
        if (resp && resp.obj) {
          this.plan.planItemDataMap.publishFeeData = resp.obj;
          this.plan.funding += resp.obj.funding;
        }
      });
      this.postRequest('/planItem/payment/getByIds', this.paymentList).then(resp => {
        if (resp && resp.obj) {
          this.plan.planItemDataMap.paymentData = resp.obj;
          this.plan.funding += resp.obj.funding;
        }
      });
      this.postRequest('/planItem/advisory/getByIds', this.advisoryList).then(resp => {
        if (resp && resp.obj) {
          this.plan.planItemDataMap.advisoryData = resp.obj;
          this.plan.funding += resp.obj.funding;
        }
      });
      this.postRequest('/planItem/otherFee/getByIds', this.otherFeeList).then(resp => {
        if (resp && resp.obj) {
          this.plan.planItemDataMap.otherFeeData = resp.obj;
          this.plan.funding += resp.obj.funding;
        }
      });
      this.loading = false;
    },
    exportData() {

    },
    doAddPlan() {
      if (this.plan.id) {
        this.$refs['planForm'].validate(valid => {
          if (valid) {
            this.putRequest("/plan/edit", this.plan).then(resp => {
              if (resp) {
                this.emptyIdList();
                this.$emit('close');
                this.$parent.initPlans();
              }
            })
          }
        });
      } else {
        this.$refs['planForm'].validate(valid => {
          if (valid) {
            this.postRequest("/plan/add", this.plan).then(resp => {
              if (resp) {
                this.emptyIdList();
                this.$emit('close');
                this.$parent.initPlans();
              }
            })
          }
        });
      }
    },
    emptyItem() {
      this.planItem = {}
    },
    showAddView() {
      this.emptyItem();
      this.planItemTitle = '新增预算明细';
      this.newPlanItemVisible = true;
    },
    handleClose() {
      this.emptyIdList();
      this.$emit('close');
      this.deleteRequest("/planItem/academicFee/clear");
      this.deleteRequest("/planItem/advisory/clear");
      this.deleteRequest("/planItem/equipmentFee/clear");
      this.deleteRequest("/planItem/materialFee/clear");
      this.deleteRequest("/planItem/meetingFee/clear");
      this.deleteRequest("/planItem/otherFee/clear");
      this.deleteRequest("/planItem/payment/clear");
      this.deleteRequest("/planItem/publishFee/clear");
      this.deleteRequest("/planItem/researchFee/clear");
      this.deleteRequest("/planItem/testingFee/clear");
      this.deleteRequest("/planItem/transportationFee/clear");
    },
    handlePlanItemIds(itemType, subType, id) {
      this.freshPids();
      if (itemType == 1) {
        this.equipmentFeeList.push(id)
      } else if (itemType == 2) {
        this.materialFeeList.push(id)
      } else if (itemType == 3) {
        this.testingFeeList.push(id)
      } else if (itemType == 4 && subType == 1) {
        this.meetingFeeList.push(id)
      } else if (itemType == 4 && subType == 2) {
        this.researchFeeList.push(id)
      } else if (itemType == 4 && subType == 3) {
        this.academicFeeList.push(id)
      } else if (itemType == 4 && subType == 4) {
        this.transportationFeeList.push(id)
      } else if (itemType == 5) {
        this.publishFeeList.push(id)
      } else if (itemType == 6) {
        this.paymentList.push(id)
      } else if (itemType == 7) {
        this.advisoryList.push(id)
      } else if (itemType == 8) {
        this.otherFeeList.push(id)
      }
      this.initPlanItemList()
      this.newPlanItemVisible = false;
    },
    freshPids() {
      if (this.plan == null) {
        return
      }
      if (this.plan.equipmentFeeList != null) {
        for (var i = 0; i < this.plan.equipmentFeeList.length; i++) {
          this.equipmentFeeList.push(this.plan.equipmentFeeList[i].id);
        }
      }
      if (this.plan.materialFeeList != null) {
        for (var i = 0; i < this.plan.materialFeeList.length; i++) {
          this.materialFeeList.push(this.plan.materialFeeList[i].id);
        }
      }
      if (this.plan.testingFeeList != null) {
        for (var i = 0; i < this.plan.testingFeeList.length; i++) {
          this.testingFeeList.push(this.plan.testingFeeList[i].id);
        }
      }
      if (this.plan.meetingFeeList != null) {
        for (var i = 0; i < this.plan.meetingFeeList.length; i++) {
          this.meetingFeeList.push(this.plan.meetingFeeList[i].id);
        }
      }
      if (this.plan.researchFeeList != null) {
        for (var i = 0; i < this.plan.researchFeeList.length; i++) {
          this.researchFeeList.push(this.plan.researchFeeList[i].id);
        }
      }
      if (this.plan.academicFeeList != null) {
        for (var i = 0; i < this.plan.academicFeeList.length; i++) {
          this.academicFeeList.push(this.plan.academicFeeList[i].id);
        }
      }
      if (this.plan.transportationFeeList != null) {
        for (var i = 0; i < this.plan.transportationFeeList.length; i++) {
          this.transportationFeeList.push(this.plan.transportationFeeList[i].id);
        }
      }
      if (this.plan.publishFeeList != null) {
        for (var i = 0; i < this.plan.publishFeeList.length; i++) {
          this.publishFeeList.push(this.plan.publishFeeList[i].id);
        }
      }
      if (this.plan.paymentList != null) {
        for (var i = 0; i < this.plan.paymentList.length; i++) {
          this.paymentList.push(this.plan.paymentList[i].id);
        }
      }
      if (this.plan.advisoryList != null) {
        for (var i = 0; i < this.plan.advisoryList.length; i++) {
          this.advisoryList.push(this.plan.advisoryList[i].id);
        }
      }
      if (this.plan.otherFeeList != null) {
        for (var i = 0; i < this.plan.otherFeeList.length; i++) {
          this.otherFeeList.push(this.plan.otherFeeList[i].id);
        }
      }
    },
    deleteEquipmentFee(id) {
      let newArr = [];
      for (var i = 0; i < this.equipmentFeeList.length; i++) {
        if (this.equipmentFeeList[i] != id) {
          newArr.push(this.equipmentFeeList[i]);
        }
      }
      this.equipmentFeeList = newArr;
      this.initPlanItemList();
    },
    deleteMaterialFee(id) {
      let newArr = [];
      for (var i = 0; i < this.materialFeeList.length; i++) {
        if (this.materialFeeList[i] != id) {
          newArr.push(this.materialFeeList[i]);
        }
      }
      this.materialFeeList = newArr;
      this.initPlanItemList();
    },
    deleteTestingFee(id) {
      let newArr = [];
      for (var i = 0; i < this.testingFeeList.length; i++) {
        if (this.testingFeeList[i] != id) {
          newArr.push(this.testingFeeList[i]);
        }
      }
      this.testingFeeList = newArr;
      this.initPlanItemList();
    },
    deleteMeetingFee(id) {
      let newArr = [];
      for (var i = 0; i < this.meetingFeeList.length; i++) {
        if (this.meetingFeeList[i] != id) {
          newArr.push(this.meetingFeeList[i]);
        }
      }
      this.meetingFeeList = newArr;
      this.initPlanItemList();
    },
    deleteResearchFee(id) {
      let newArr = [];
      for (var i = 0; i < this.researchFeeList.length; i++) {
        if (this.equipmentFeeList[i] != id) {
          newArr.push(this.researchFeeList[i]);
        }
      }
      this.researchFeeList = newArr;
      this.initPlanItemList();
    },
    deleteAcademicFee(id) {
      let newArr = [];
      for (var i = 0; i < this.academicFeeList.length; i++) {
        if (this.academicFeeList[i] != id) {
          newArr.push(this.academicFeeList[i]);
        }
      }
      this.academicFeeList = newArr;
      this.initPlanItemList();
    },
    deleteTransportationFee(id) {
      let newArr = [];
      for (var i = 0; i < this.transportationFeeList.length; i++) {
        if (this.transportationFeeList[i] != id) {
          newArr.push(this.transportationFeeList[i]);
        }
      }
      this.transportationFeeList = newArr;
      this.initPlanItemList();
    },
    deletePublishFee(id) {
      let newArr = [];
      for (var i = 0; i < this.publishFeeList.length; i++) {
        if (this.publishFeeList[i] != id) {
          newArr.push(this.publishFeeList[i]);
        }
      }
      this.publishFeeList = newArr;
      this.initPlanItemList();
    },
    deletePayment(id) {
      let newArr = [];
      for (var i = 0; i < this.paymentList.length; i++) {
        if (this.paymentList[i] != id) {
          newArr.push(this.paymentList[i]);
        }
      }
      this.paymentList = newArr;
      this.initPlanItemList();
    },
    deleteAdvisory(id) {
      let newArr = [];
      for (var i = 0; i < this.advisoryList.length; i++) {
        if (this.advisoryList[i] != id) {
          newArr.push(this.advisoryList[i]);
        }
      }
      this.advisoryList = newArr;
      this.initPlanItemList();
    },
    deleteOtherFee(id) {
      let newArr = [];
      for (var i = 0; i < this.otherFeeList.length; i++) {
        if (this.otherFeeList[i] != id) {
          newArr.push(this.otherFeeList[i]);
        }
      }
      this.otherFeeList = newArr;
      this.initPlanItemList();
    }
  }
}
</script>

<style scoped>
</style>
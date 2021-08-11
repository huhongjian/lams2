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
                      v-for="item in projectTypes"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="本计划预算（万元）:" prop="funding">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="plan.funding"></el-input>
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
            <el-button @click="deleteAsset" style="display: inline-flex;margin-left: 8px" type="danger">
              删除
            </el-button>
          </el-row>
          <el-collapse>
            <el-collapse-item title="设备费" name="1">
              <el-table
                  :data="plan.equipmentFeeList"
                  @selection-change="handleSelectionChange"
                  stripe
                  border
                  v-loading="loading"
                  element-loading-text="正在加载..."
                  element-loading-spinner="el-icon-loading"
                  element-loading-background="rgba(0, 0, 0, 0.8)"
                  style="width: 100%">
                <el-table-column
                    type="selection"
                    width="55">
                </el-table-column>
                <el-table-column
                    fixed
                    label="序号"
                    align="left"
                    width="100">
                  <template slot-scope="scope">
                    <el-button size="mini" @click="showDetailView(scope.row)">{{ scope.row.id }}</el-button>
                  </template>
                </el-table-column>
                <el-table-column
                    fixed
                    prop="equipmentName"
                    :show-overflow-tooltip="true"
                    align="left"
                    label="设备名称">
                </el-table-column>
                <el-table-column
                    prop="usage"
                    align="left"
                    label="用途"
                    width="200">
                </el-table-column>
                <el-table-column
                    prop="unitPrice"
                    align="left"
                    label="单价（万元）"
                    width="200">
                </el-table-column>
                <el-table-column
                    prop="amount"
                    align="left"
                    label="数量"
                    width="200">
                </el-table-column>
                <el-table-column
                    prop="subTotal"
                    align="left"
                    label="合计（万元）"
                    width="200">
                </el-table-column>
              </el-table>
            </el-collapse-item>
            <el-collapse-item title="材料费" name="2">
              <el-table
                  :data="plan.materialFeeList"
                  @selection-change="handleSelectionChange"
                  stripe
                  border
                  v-loading="loading"
                  element-loading-text="正在加载..."
                  element-loading-spinner="el-icon-loading"
                  element-loading-background="rgba(0, 0, 0, 0.8)"
                  style="width: 100%">
                <el-table-column
                    type="selection"
                    width="55">
                </el-table-column>
                <el-table-column
                    fixed
                    label="序号"
                    align="left"
                    width="100">
                  <template slot-scope="scope">
                    <el-button size="mini" @click="showDetailView(scope.row)">{{ scope.row.id }}</el-button>
                  </template>
                </el-table-column>
                <el-table-column
                    fixed
                    prop="materialName"
                    :show-overflow-tooltip="true"
                    align="left"
                    label="材料用途">
                </el-table-column>
                <el-table-column
                    prop="specification"
                    align="left"
                    label="规格"
                    width="200">
                </el-table-column>
                <el-table-column
                    prop="amount"
                    align="left"
                    label="数量"
                    width="200">
                </el-table-column>
                <el-table-column
                    prop="unitPrice"
                    align="left"
                    label="单价（万元）"
                    width="200">
                </el-table-column>
                <el-table-column
                    prop="unit"
                    align="left"
                    label="单位"
                    width="200">
                </el-table-column>
                <el-table-column
                    prop="subTotal"
                    align="left"
                    label="小计（万元）"
                    width="200">
                </el-table-column>
              </el-table>
            </el-collapse-item>
            <el-collapse-item title="测试化验加工费" name="3">
              <el-table
                  :data="plan.testingFeeList"
                  @selection-change="handleSelectionChange"
                  stripe
                  border
                  v-loading="loading"
                  element-loading-text="正在加载..."
                  element-loading-spinner="el-icon-loading"
                  element-loading-background="rgba(0, 0, 0, 0.8)"
                  style="width: 100%">
                <el-table-column
                    type="selection"
                    width="55">
                </el-table-column>
                <el-table-column
                    fixed
                    label="序号"
                    align="left"
                    width="100">
                  <template slot-scope="scope">
                    <el-button size="mini" @click="showDetailView(scope.row)">{{ scope.row.id }}</el-button>
                  </template>
                </el-table-column>
                <el-table-column
                    prop="content"
                    :show-overflow-tooltip="true"
                    align="left"
                    label="加工内容">
                </el-table-column>
                <el-table-column
                    prop="times"
                    align="left"
                    label="次数"
                    width="200">
                </el-table-column>
                <el-table-column
                    prop="unitPrice"
                    align="left"
                    label="单价（万元）"
                    width="200">
                </el-table-column>
                <el-table-column
                    prop="subTotal"
                    align="left"
                    label="合计（万元）"
                    width="200">
                </el-table-column>
              </el-table>
            </el-collapse-item>
            <el-collapse-item title="差旅/会议/国际合作与交流费" name="4">
              <el-tag>会议费</el-tag>
              <el-table
                  :data="plan.meetingFeeList"
                  @selection-change="handleSelectionChange"
                  stripe
                  border
                  v-loading="loading"
                  element-loading-text="正在加载..."
                  element-loading-spinner="el-icon-loading"
                  element-loading-background="rgba(0, 0, 0, 0.8)"
                  style="width: 100%">
                <el-table-column
                    type="selection"
                    width="55">
                </el-table-column>
                <el-table-column
                    fixed
                    label="序号"
                    align="left"
                    width="100">
                  <template slot-scope="scope">
                    <el-button size="mini" @click="showDetailView(scope.row)">{{ scope.row.id }}</el-button>
                  </template>
                </el-table-column>
                <el-table-column
                    prop="content"
                    align="left"
                    label="会议内容">
                </el-table-column>
                <el-table-column
                    prop="relation"
                    align="left"
                    label="与课题相关性">
                </el-table-column>
                <el-table-column
                    prop="meal"
                    align="left"
                    label="伙食费（人/天）">
                </el-table-column>
                <el-table-column
                    prop="otherCost"
                    align="left"
                    label="其他费用（人/天）"
                    width="150">
                </el-table-column>
                <el-table-column
                    prop="days"
                    align="left"
                    label="天数">
                </el-table-column>
                <el-table-column
                    prop="amount"
                    align="left"
                    label="参会人数">
                </el-table-column>
                <el-table-column
                    prop="meetingNum"
                    align="left"
                    label="会议次数">
                </el-table-column>
                <el-table-column
                    prop="subTotal"
                    align="left"
                    label="合计（万元）">
                </el-table-column>
              </el-table>
              <el-tag>课题调研差旅费</el-tag>
              <el-table
                  :data="plan.researchFeeList"
                  @selection-change="handleSelectionChange"
                  stripe
                  border
                  v-loading="loading"
                  element-loading-text="正在加载..."
                  element-loading-spinner="el-icon-loading"
                  element-loading-background="rgba(0, 0, 0, 0.8)"
                  style="width: 100%">
                <el-table-column
                    type="selection"
                    width="55">
                </el-table-column>
                <el-table-column
                    fixed
                    label="序号"
                    align="left">
                  <template slot-scope="scope">
                    <el-button size="mini" @click="showDetailView(scope.row)">{{ scope.row.id }}</el-button>
                  </template>
                </el-table-column>
                <el-table-column
                    prop="place"
                    align="left"
                    label="调研地点">
                </el-table-column>
                <el-table-column
                    prop="purpose"
                    align="left"
                    label="调研目的">
                </el-table-column>
                <el-table-column
                    prop="personnelType"
                    align="left"
                    label="人员类别">
                </el-table-column>
                <el-table-column
                    prop="times"
                    align="left"
                    label="人次">
                </el-table-column>
                <el-table-column
                    prop="transFee"
                    align="left"
                    label="交通费（元）">
                </el-table-column>
                <el-table-column
                    prop="accommodationFee"
                    align="left"
                    label="住宿（元）">
                </el-table-column>
                <el-table-column
                    prop="otherFee"
                    align="left"
                    label="公杂费（元）">
                </el-table-column>
                <el-table-column
                    prop="days"
                    align="left"
                    label="天数">
                </el-table-column>
                <el-table-column
                    prop="subTotal"
                    align="left"
                    label="合计（万元）">
                </el-table-column>
              </el-table>
              <el-tag>国内学术会议差旅费</el-tag>
              <el-table
                  :data="plan.academicFeeList"
                  @selection-change="handleSelectionChange"
                  stripe
                  border
                  v-loading="loading"
                  element-loading-text="正在加载..."
                  element-loading-spinner="el-icon-loading"
                  element-loading-background="rgba(0, 0, 0, 0.8)"
                  style="width: 100%">
                <el-table-column
                    type="selection"
                    width="55">
                </el-table-column>
                <el-table-column
                    fixed
                    label="序号"
                    align="left"
                    width="100">
                  <template slot-scope="scope">
                    <el-button size="mini" @click="showDetailView(scope.row)">{{ scope.row.id }}</el-button>
                  </template>
                </el-table-column>
                <el-table-column
                    prop="meetingName"
                    align="left"
                    label="会议名称">
                </el-table-column>
                <el-table-column
                    prop="personnelType"
                    align="left"
                    label="人员类别">
                </el-table-column>
                <el-table-column
                    prop="times"
                    align="left"
                    label="人次">
                </el-table-column>
                <el-table-column
                    prop="year"
                    align="left"
                    label="年度">
                </el-table-column>
                <el-table-column
                    prop="days"
                    align="left"
                    label="会期（天）">
                </el-table-column>
                <el-table-column
                    prop="transFee"
                    align="left"
                    label="往返交通费（元）"
                    width="150">
                </el-table-column>
                <el-table-column
                    prop="accommodationFee"
                    align="left"
                    label="住宿及补助费（元）"
                    width="150">
                </el-table-column>
                <el-table-column
                    prop="subTotal"
                    align="left"
                    label="合计（万元）">
                </el-table-column>
              </el-table>
              <el-tag>北京市内交通费</el-tag>
              <el-table
                  :data="plan.transportationFeeList"
                  @selection-change="handleSelectionChange"
                  stripe
                  border
                  v-loading="loading"
                  element-loading-text="正在加载..."
                  element-loading-spinner="el-icon-loading"
                  element-loading-background="rgba(0, 0, 0, 0.8)"
                  style="width: 100%">
                <el-table-column
                    type="selection"
                    width="55">
                </el-table-column>
                <el-table-column
                    fixed
                    label="序号"
                    align="left"
                    width="100">
                  <template slot-scope="scope">
                    <el-button size="mini" @click="showDetailView(scope.row)">{{ scope.row.id }}</el-button>
                  </template>
                </el-table-column>
                <el-table-column
                    prop="remark"
                    align="left"
                    label="说明">
                </el-table-column>
                <el-table-column
                    prop="subTotal"
                    align="left"
                    label="合计（万元）"
                    width="200">
                </el-table-column>
              </el-table>
            </el-collapse-item>
            <el-collapse-item title="出版/文献/信息传播/知识产权事务费" name="5">
              <el-table
                  :data="plan.publishFeeList"
                  @selection-change="handleSelectionChange"
                  stripe
                  border
                  v-loading="loading"
                  element-loading-text="正在加载..."
                  element-loading-spinner="el-icon-loading"
                  element-loading-background="rgba(0, 0, 0, 0.8)"
                  style="width: 100%">
                <el-table-column
                    type="selection"
                    width="55">
                </el-table-column>
                <el-table-column
                    fixed
                    label="序号"
                    align="left"
                    width="100">
                  <template slot-scope="scope">
                    <el-button size="mini" @click="showDetailView(scope.row)">{{ scope.row.id }}</el-button>
                  </template>
                </el-table-column>
                <el-table-column
                    fixed
                    prop="type"
                    :show-overflow-tooltip="true"
                    align="left"
                    width="200"
                    label="类别">
                </el-table-column>
                <el-table-column
                    prop="remark"
                    align="left"
                    label="说明">
                </el-table-column>
                <el-table-column
                    prop="subTotal"
                    align="left"
                    label="合计（万元）"
                    width="200">
                </el-table-column>
              </el-table>
            </el-collapse-item>
            <el-collapse-item title="劳务费" name="6">
              <el-table
                  :data="plan.paymentList"
                  @selection-change="handleSelectionChange"
                  stripe
                  border
                  v-loading="loading"
                  element-loading-text="正在加载..."
                  element-loading-spinner="el-icon-loading"
                  element-loading-background="rgba(0, 0, 0, 0.8)"
                  style="width: 100%">
                <el-table-column
                    type="selection"
                    width="55">
                </el-table-column>
                <el-table-column
                    fixed
                    label="序号"
                    align="left"
                    width="100">
                  <template slot-scope="scope">
                    <el-button size="mini" @click="showDetailView(scope.row)">{{ scope.row.id }}</el-button>
                  </template>
                </el-table-column>
                <el-table-column
                    fixed
                    prop="personnelType"
                    :show-overflow-tooltip="true"
                    align="left"
                    label="人员种类">
                </el-table-column>
                <el-table-column
                    prop="content"
                    align="left"
                    label="工作内容"
                    width="200">
                </el-table-column>
                <el-table-column
                    prop="amount"
                    align="left"
                    label="人数"
                    width="200">
                </el-table-column>
                <el-table-column
                    prop="standard"
                    align="left"
                    label="标准（元）"
                    width="200">
                </el-table-column>
                <el-table-column
                    prop="hours"
                    align="left"
                    label="工作时间"
                    width="200">
                </el-table-column>
                <el-table-column
                    prop="subTotal"
                    align="left"
                    label="合计（万元）"
                    width="200">
                </el-table-column>
              </el-table>
            </el-collapse-item>
            <el-collapse-item title="专家咨询费" name="7">
              <el-table
                  :data="plan.advisoryList"
                  @selection-change="handleSelectionChange"
                  stripe
                  border
                  v-loading="loading"
                  element-loading-text="正在加载..."
                  element-loading-spinner="el-icon-loading"
                  element-loading-background="rgba(0, 0, 0, 0.8)"
                  style="width: 100%">
                <el-table-column
                    type="selection"
                    width="55">
                </el-table-column>
                <el-table-column
                    fixed
                    label="序号"
                    align="left"
                    width="100">
                  <template slot-scope="scope">
                    <el-button size="mini" @click="showDetailView(scope.row)">{{ scope.row.id }}</el-button>
                  </template>
                </el-table-column>
                <el-table-column
                    fixed
                    prop="content"
                    :show-overflow-tooltip="true"
                    align="left"
                    label="咨询内容">
                </el-table-column>
                <el-table-column
                    prop="personnelType"
                    align="left"
                    label="人员类型"
                    width="200">
                </el-table-column>
                <el-table-column
                    prop="standard"
                    align="left"
                    label="标准（元/天）"
                    width="200">
                </el-table-column>
                <el-table-column
                    prop="amount"
                    align="left"
                    label="人数"
                    width="200">
                </el-table-column>
                <el-table-column
                    prop="days"
                    align="left"
                    label="天数"
                    width="200">
                </el-table-column>
                <el-table-column
                    prop="subTotal"
                    align="left"
                    label="总计（万元）"
                    width="200">
                </el-table-column>
              </el-table>
            </el-collapse-item>
            <el-collapse-item title="其他支出" name="8">
              <el-table
                  :data="plan.otherFeeList"
                  @selection-change="handleSelectionChange"
                  stripe
                  border
                  v-loading="loading"
                  element-loading-text="正在加载..."
                  element-loading-spinner="el-icon-loading"
                  element-loading-background="rgba(0, 0, 0, 0.8)"
                  style="width: 100%">
                <el-table-column
                    type="selection"
                    width="55">
                </el-table-column>
                <el-table-column
                    fixed
                    label="序号"
                    align="left"
                    width="100">
                  <template slot-scope="scope">
                    <el-button size="mini" @click="showDetailView(scope.row)">{{ scope.row.id }}</el-button>
                  </template>
                </el-table-column>
                <el-table-column
                    fixed
                    prop="type"
                    :show-overflow-tooltip="true"
                    align="left"
                    label="类别"
                    width="200">
                </el-table-column>
                <el-table-column
                    prop="remark"
                    align="left"
                    label="说明">
                </el-table-column>
                <el-table-column
                    prop="subTotal"
                    align="left"
                    label="合计（万元）"
                    width="200">
                </el-table-column>
              </el-table>
            </el-collapse-item>
          </el-collapse>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="$emit('close')">取 消</el-button>
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
  props: ['plan', 'title', 'newPlanVisible', 'projectTypes'],
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
      },
      total: 0,
      page: 1,
      size: 10,
      planItemTypes: [],
      rules: {
        reason: [{required: true, message: '请输入申请理由', trigger: 'blur'}]
      },
    }
  },
  components: {
    NewPlanItem
  },
  methods: {
    initPlanItemList() {
      this.loading = true;
      this.postRequest('/planItem/equipmentFee/getByIds', this.equipmentFeeList).then(resp => {
        if (resp) {
          this.plan.equipmentFeeList = resp.obj;
        }
      });
      this.postRequest('/planItem/materialFee/getByIds', this.materialFeeList).then(resp => {
        if (resp) {
          this.plan.materialFeeList = resp.obj;
        }
      });
      this.postRequest('/planItem/testingFee/getByIds', this.testingFeeList).then(resp => {
        if (resp) {
          this.plan.testingFeeList = resp.obj;
        }
      });
      this.postRequest('/planItem/meetingFee/getByIds', this.meetingFeeList).then(resp => {
        if (resp) {
          this.plan.meetingFeeList = resp.obj;
        }
      });
      this.postRequest('/planItem/researchFee/getByIds', this.researchFeeList).then(resp => {
        if (resp) {
          this.plan.researchFeeList = resp.obj;
        }
      });
      this.postRequest('/planItem/academicFee/getByIds', this.academicFeeList).then(resp => {
        if (resp) {
          this.plan.academicFeeList = resp.obj;
        }
      });
      this.postRequest('/planItem/transportationFee/getByIds', this.transportationFeeList).then(resp => {
        if (resp) {
          this.plan.transportationFeeList = resp.obj;
        }
      });
      this.postRequest('/planItem/publishFee/getByIds', this.publishFeeList).then(resp => {
        if (resp) {
          this.plan.publishFeeList = resp.obj;
        }
      });
      this.postRequest('/planItem/payment/getByIds', this.paymentList).then(resp => {
        if (resp) {
          this.plan.paymentList = resp.obj;
        }
      });
      this.postRequest('/planItem/advisory/getByIds', this.advisoryList).then(resp => {
        if (resp) {
          this.plan.advisoryList = resp.obj;
        }
      });
      this.postRequest('/planItem/otherFee/getByIds', this.otherFeeList).then(resp => {
        if (resp) {
          this.plan.otherFeeList = resp.obj;
        }
      });
      this.loading = false;
    },
    exportData() {

    },
    deleteAsset() {

    },
    handleSelectionChange() {

    },
    showDetailView() {

    },
    doAddPlan() {
      if (this.plan.id) {
        this.$refs['planForm'].validate(valid => {
          if (valid) {
            this.putRequest("/plan/edit", this.plan).then(resp => {
              if (resp) {
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
      this.$emit('close');
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
    }
  }
}
</script>

<style scoped>

</style>
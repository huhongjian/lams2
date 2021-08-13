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
              <el-tag>金额: {{ plan.equipmentFeeData.funding }}（万元）</el-tag>
              <el-tag type="success">未执行额度: {{ plan.equipmentFeeData.unfulfilledQuota }}（万元）</el-tag>
              <el-tag type="info">执行率: {{ plan.equipmentFeeData.executeRate }}%</el-tag>
              <el-progress :text-inside="true" :stroke-width="10"
                           :percentage=plan.equipmentFeeData.executeRate></el-progress>
              <el-table
                  :data="plan.equipmentFeeData.equipmentFeeList"
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
                    width="50">
                </el-table-column>
                <el-table-column
                    fixed
                    prop="id"
                    label="序号"
                    align="left"
                    width="50">
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
                    label="用途">
                </el-table-column>
                <el-table-column
                    prop="unitPrice"
                    align="left"
                    label="单价（万元）"
                    width="100">
                </el-table-column>
                <el-table-column
                    prop="amount"
                    align="left"
                    label="数量"
                    width="50">
                </el-table-column>
                <el-table-column
                    prop="subTotal"
                    align="left"
                    label="合计（万元）"
                    width="100">
                </el-table-column>
                <el-table-column
                    prop="unfulfilledQuota"
                    align="left"
                    label="未执行额度（万元）"
                    width="150">
                </el-table-column>
                <el-table-column
                    prop="executeRate"
                    align="left"
                    label="执行率"
                    width="100">
                </el-table-column>
                <el-table-column
                    fixed="right"
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
    <el-button @click="handleClose">取 消</el-button>
      </span>
    </el-dialog>
    <ExecutePlan v-on:close="executePlanVisible = false"
                 v-on:freshPlan="freshPlan"
                 :plan="plan" :planItem="planItem"
                 :executePlanVisible="executePlanVisible"
                 :title="title"></ExecutePlan>
    <ExecuteDetail v-on:close="executeDetailVisible = false" :executeDetailVisible="executeDetailVisible"
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
      loading: false,
      title: '',
      planItem: {
        id: null,
        itemType: "",
        itemTypeName: "",
        remark: '',
      },
      executePlanVisible: false,
      executeDetailVisible: false
    }
  },
  methods: {
    showExecuteView(data) {
      this.title = '预算执行信息';
      this.planItem = data;
      this.executePlanVisible = true;
    },
    showExecuteDetailView() {
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
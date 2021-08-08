<template>
  <div>
    <el-dialog
        :title="title"
        :visible.sync="newPlanVisible"
        :before-close="handleClose"
        width="80%">
      <div>
        <el-form :model="project" :rules="rules" ref="projectForm">
          <el-row>
            <el-form-item label="预算计划名称:" prop="projectName">
              <el-input size="mini" style="width: 800px" prefix-icon="el-icon-edit"
                        v-model="project.projectName"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-col :span="15">
              <el-form-item label="科研项目:" prop="projectType">
                <el-select style="width: 500px" v-model="project.projectType" clearable placeholder="请选择">
                  <el-option
                      v-for="item in projectTypes"
                      :key="item.id"
                      :label="item.name"
                      :value="item.name">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="本计划预算（万元）:" prop="projectName">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="project.projectName"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="20">
              <el-form-item label="备注:" prop="reason">
                <el-input size="mini"
                          type="textarea"
                          :rows="2"
                          placeholder="请输入申请理由"
                          v-model="project.projectName"></el-input>
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
          <el-collapse @change="initPlanItemList">
            <el-collapse-item title="设备费" name="1">
              <el-table
                  :data="equipmentFeeList"
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
                  :data="materialFeeList"
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
                  :data="testingFeeList"
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
              <el-tag>北京市内交通费</el-tag>
              <el-table
                  :data="transportationFeeList"
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
                  :data="publishFeeList"
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
                  :data="paymentList"
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
                  :data="advisoryList"
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
                  :data="otherFeeList"
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
    <el-button type="primary" @click="doAddPlanItem">确 定</el-button>
  </span>
    </el-dialog>
    <NewPlanItem v-on:close="newPlanItemVisible = false" :planItem="planItem"
                 :newPlanItemVisible="newPlanItemVisible" :title="planItemTitle"
                 :planItemTypes="planItemTypes"></NewPlanItem>
  </div>
</template>

<script>
import NewPlanItem from "@/components/budget/NewPlanItem";

export default {
  name: "NewProject",
  props: ['project', 'title', 'newPlanVisible', 'projectTypes'],
  data() {
    return {
      planSubjects: [],
      // 预算明细类型
      equipmentFeeList: [],
      materialFeeList: [],
      testingFeeList: [],
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
  mounted() {
    this.initPlanItemTypes();
  },
  methods: {
    initPlanItemList(val) {
      this.getRequest('/planItem/equipmentFee/get').then(resp => {
        if (resp) {
          this.equipmentFeeList = resp.obj;
        }
      });
      this.getRequest('/planItem/materialFee/get').then(resp => {
        if (resp) {
          this.materialFeeList = resp.obj;
        }
      });
      this.getRequest('/planItem/testingFee/get').then(resp => {
        if (resp) {
          this.testingFeeList = resp.obj;
        }
      });
      this.getRequest('/planItem/transportationFee/get').then(resp => {
        if (resp) {
          this.transportationFeeList = resp.obj;
        }
      });
      this.getRequest('/planItem/publishFee/get').then(resp => {
        if (resp) {
          this.publishFeeList = resp.obj;
        }
      });
      this.getRequest('/planItem/materialFee/get').then(resp => {
        if (resp) {
          this.materialFeeList = resp.obj;
        }
      });
      this.getRequest('/planItem/payment/get').then(resp => {
        if (resp) {
          this.paymentList = resp.obj;
        }
      });
      this.getRequest('/planItem/advisory/get').then(resp => {
        if (resp) {
          this.advisoryList = resp.obj;
        }
      });
      this.getRequest('/planItem/otherFee/get').then(resp => {
        if (resp) {
          this.otherFeeList = resp.obj;
        }
      });
    },
    exportData() {

    },
    deleteAsset() {

    },
    handleSelectionChange() {

    },
    showDetailView() {

    },
    initPlanItemTypes() {
      let url = '/plan/item/types/get';
      this.getRequest(url).then(resp => {
        if (resp) {
          this.planItemTypes = resp.obj;
        }
      });
    },
    doAddPlanItem() {
      if (this.project.id) {
        this.$refs['projectForm'].validate(valid => {
          if (valid) {
            this.putRequest("/project/edit", this.project).then(resp => {
              if (resp) {
                this.$emit('close');
                this.$parent.initProjects();
              }
            })
          }
        });
      } else {
        this.$refs['projectForm'].validate(valid => {
          if (valid) {
            this.postRequest("/project/add", this.project).then(resp => {
              if (resp) {
                this.$emit('close');
                this.$parent.initProjects();
              }
            })
          }
        });
      }
    },
    emptyItem() {
      this.planItem = {
        planItemId: null,
        planItemType: '',
        planItemRemark: '',
        equipmentFee: {}
      }
    },
    showAddView() {
      this.emptyItem();
      this.planItemTitle = '新增预算明细';
      this.newPlanItemVisible = true;
    },
    handleClose() {
      this.$emit('close');
    }
  }
}
</script>

<style scoped>

</style>
<template>
  <div>
    <div>
      <div style="display: flex;justify-content: space-between; width: 100%">
        <div>
          <el-button type="primary" icon="el-icon-plus" @click="showAddView">
            添加计划
          </el-button>
          <el-button type="success" style="display: inline-flex;margin-left: 8px" @click="exportData"
                     icon="el-icon-download">
            导出数据
          </el-button>
          <el-button @click="deletePlan" style="display: inline-flex;margin-left: 8px" type="danger">
            删除
          </el-button>
        </div>
        <div>
          <el-input placeholder="请输入项目序号进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
                    clearable
                    @clear="initPlans"
                    style="width: 350px;margin-right: 10px" v-model="keyword"
                    @keydown.enter.native="initPlans" :disabled="showAdvanceSearchView"></el-input>
          <el-button icon="el-icon-search" type="primary" @click="initPlans"
                     :disabled="showAdvanceSearchView">
            搜索
          </el-button>
          <el-button type="primary" @click="showAdvanceSearchView = !showAdvanceSearchView">
            <i :class="showAdvanceSearchView?'fa fa-angle-double-up':'fa fa-angle-double-down'"
               aria-hidden="true"></i>
            高级搜索
          </el-button>
        </div>
      </div>
      <transition name="slide-fade">
        <div v-show="showAdvanceSearchView"
             style="border: 1px solid #759ad1;border-radius: 5px;box-sizing: border-box;padding: 5px;margin: 10px 0px;">
          <el-row>
            <el-col :span="10">
              计划名称:
              <el-input size="mini" style="width: 220px" clearable prefix-icon="el-icon-edit"
                        v-model="searchValue.planName"></el-input>
            </el-col>
            <el-col :span="10">
              科研项目:
              <el-select v-model="searchValue.projectId" clearable placeholder="科研项目" size="mini" style="width: 300px;">
                <el-option
                    v-for="item in projectLists"
                    :key="item.id"
                    :label="item.projectName"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="10">
              创建人邮箱:
              <el-input size="mini" style="width: 200px" clearable prefix-icon="el-icon-edit"
                        v-model="searchValue.userEmail"></el-input>
            </el-col>
            <el-col :span="10">
              创建时间:
              <el-date-picker
                  v-model="searchValue.dateScope"
                  type="daterange"
                  size="mini"
                  unlink-panels
                  value-format="yyyy-MM-dd"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
            <el-col :span="4">
              <el-button size="mini" @click="clearSearchValue">重置</el-button>
              <el-button size="mini" @click="showAdvanceSearchView = false">取消</el-button>
              <el-button size="mini" icon="el-icon-search" type="primary" @click="initPlansAdv">搜索</el-button>
            </el-col>
          </el-row>
        </div>
      </transition>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="plans"
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
            label="计划编号"
            align="left"
            width="80">
          <template slot-scope="scope">
            <el-button size="mini" @click="showDetailView(scope.row)">{{ scope.row.id }}</el-button>
          </template>
        </el-table-column>
        <el-table-column
            prop="planName"
            :show-overflow-tooltip="true"
            label="预算计划名称">
        </el-table-column>
        <el-table-column
            prop="projectName"
            :show-overflow-tooltip="true"
            label="科研项目">
        </el-table-column>
        <el-table-column
            prop="funding"
            width="120"
            label="预算额度（万元）">
        </el-table-column>
        <el-table-column
            prop="unfulfilledQuota"
            width="150"
            label="未执行额度（万元）">
        </el-table-column>
        <el-table-column
            prop="executeRate"
            width="95"
            label="执行率（%）">
        </el-table-column>
        <el-table-column
            prop="createTime"
            width="100"
            label="创建时间">
        </el-table-column>
        <el-table-column
            fixed="right"
            width="80"
            label="操作">
          <template slot-scope="scope">
            <el-button @click="showEditView(scope.row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="display: flex;justify-content: flex-end">
        <el-pagination
            background
            @current-change="currentChange"
            @size-change="sizeChange"
            layout="sizes, prev, pager, next, jumper, ->, total, slot"
            :total="total">
        </el-pagination>
      </div>
    </div>
    <NewPlan v-on:close="newPlanVisible = false" :newPlanVisible="newPlanVisible" :plan="plan"
             :title="title" :projectLists="projectLists"></NewPlan>
    <PlanDetail v-on:close="planDetailVisible = false"
                v-on:freshPlan="freshPlan"
                :planDetailVisible="planDetailVisible" :plan="plan" :title="title"></PlanDetail>
  </div>
</template>

<script>
import NewPlan from "@/components/budget/NewPlan";
import PlanDetail from "@/components/budget/PlanDetail";

export default {
  name: "BudgetProject",
  data() {
    return {
      plans: [],
      searchValue: {
        planName: null,
        projectId: null,
        userEmail: null,
        dateScope: null
      },
      title: '',
      showAdvanceSearchView: false,
      loading: false,
      newPlanVisible: false,
      // 详情页可见性
      planDetailVisible: false,
      total: 0,
      page: 1,
      keyword: '',
      size: 10,
      plan: {
        id: "",
        planName: "",
        projectId: "",
        funding: 0,
        unfulfilledQuota: "",
        executeRate: "",
        userEmail: "",
        user: {
          id: "",
          name: "",
          phone: "",
          username: "",
          enabled: "",
          remark: ""
        },
        planItemDataMap: {
          equipmentFeeData: {},
          materialFeeData: {},
          testingFeeData: {},
          meetingFeeData: {},
          researchFeeData: {},
          academicFeeData: {},
          transFeeData: {},
          publishFeeData: {},
          paymentData: {},
          advisoryData: {},
          otherFeeData: {},
        },
        createTime: "",
        updateTime: ""
      },
      projectLists: [],
      type: "",
      planIds: []
    }
  },
  components: {
    NewPlan,
    PlanDetail
  },
  mounted() {
    this.initProjects();
    this.initPlans();
  },
  methods: {
    exportData() {

    },
    handleSelectionChange(val) {
      this.planIds = [];
      for (let i = 0; i < val.length; i++) {
        this.planIds.push(val[i].id);
      }
    },
    showEditView(data) {
      this.title = '编辑预算计划';
      this.plan = data;
      this.newPlanVisible = true;
    },
    deleteProject() {

    },
    emptyPlan() {
      this.plan = {
        id: "",
        planName: "",
        projectId: "",
        funding: 0,
        unfulfilledQuota: 0,
        executeRate: 0,
        userEmail: "",
        user: {
          id: "",
          name: "",
          phone: "",
          username: "",
          enabled: "",
          remark: ""
        },
        planItemDataMap: {
          equipmentFeeData: {},
          materialFeeData: {},
          testingFeeData: {},
          meetingFeeData: {},
          researchFeeData: {},
          academicFeeData: {},
          transFeeData: {},
          publishFeeData: {},
          paymentData: {},
          advisoryData: {},
          otherFeeData: {},
        },
        createTime: "",
        updateTime: ""
      };
    },
    showAddView() {
      this.emptyPlan();
      this.title = '新增预算计划';
      this.newPlanVisible = true;
    },
    freshPlan(id) {
      let url = '/plan/get/?page=1&size=1&planId=' + id;
      this.getRequest(url).then(resp => {
        if (resp) {
          this.plan = resp.data[0];
        }
      });
    },
    showDetailView(data) {
      this.title = '科研项目详情';
      this.plan = data;
      this.planDetailVisible = true;
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      if (this.type && this.type == 'advanced') {
        this.initPlansAdv();
      } else {
        this.initPlans();
      }
    },
    currentChange(currentPage) {
      this.page = currentPage;
      if (this.type && this.type == 'advanced') {
        this.initPlansAdv();
      } else {
        this.initPlans();
      }
    },
    initPlans() {
      this.type = '';
      this.loading = true;
      let url = '/plan/get/?page=' + this.page + '&size=' + this.size + "&planId=" + this.keyword;
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.plans = resp.data;
          this.total = resp.total;
        }
      });
    },
    initPlansAdv() {
      this.type = 'advanced'
      this.loading = true;
      let url = '/plan/get/?page=' + this.page + '&size=' + this.size;
      if (this.searchValue.planName) {
        url += '&planName=' + this.searchValue.planName;
      }
      if (this.searchValue.projectId) {
        url += '&projectId=' + this.searchValue.projectId;
      }
      if (this.searchValue.userEmail) {
        url += '&userEmail=' + this.searchValue.userEmail;
      }
      if (this.searchValue.dateScope) {
        url += '&dateScope=' + this.searchValue.dateScope;
      }
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.plans = resp.data;
          this.total = resp.total;
        }
      });
    },
    clearSearchValue() {
      this.searchValue = {
        planName: null,
        projectId: null,
        userEmail: null,
        dateScope: null
      }
    },
    initProjects() {
      let url = '/project/get/all';
      this.getRequest(url).then(resp => {
        if (resp) {
          this.projectLists = resp.obj;
        }
      });
    },
  }
}
</script>

<style scoped>
</style>
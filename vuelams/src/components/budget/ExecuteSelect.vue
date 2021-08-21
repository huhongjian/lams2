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
              :data="planItemList"
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
                :show-overflow-tooltip="true"
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
          name: '国内学术会议差旅费'
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
      planItemList: [],
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
    initItems() {
      this.$refs['searchValueForm'].validate(valid => {
        if (valid) {
          this.loading = true;
          this.getItemUrl();
          let url = "/planItem/" + this.itemUrl + "/getRecommend";
          this.getRequest(url).then(resp => {
            this.loading = false;
            if (resp) {
              this.planItemList = resp.obj;
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
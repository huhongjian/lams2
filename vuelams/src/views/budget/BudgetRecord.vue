<template>
  <div>
    <div>
      <div style="display: flex;justify-content: space-between; width: 100%">
        <div>
          <el-button type="primary" @click="showSelectView">
            点我执行
          </el-button>
          <el-button @click="revertExecution" style="display: inline-flex;margin-left: 8px" type="danger">
            撤销执行
          </el-button>
          <el-button type="primary" @click="reimburse" plain>
            确认报销
          </el-button>
          <el-button @click="cancelReimburse" style="display: inline-flex;margin-left: 8px" type="danger" plain>
            取消报销
          </el-button>
          <el-upload
              :show-file-list="false"
              :before-upload="beforeUpload"
              :on-success="onSuccess"
              :on-error="onError"
              :disabled="importDataDisabled"
              style="display: inline-flex;margin-left: 8px"
              action="/plan/import">
            <el-button :disabled="importDataDisabled" type="success" :icon="importDataBtnIcon">
              {{ importDataBtnText }}
            </el-button>
          </el-upload>
          <el-button type="success" style="display: inline-flex;margin-left: 8px" @click="getTemplate"
                     icon="el-icon-download">
            下载模板
          </el-button>
          <el-button type="success" style="display: inline-flex;margin-left: 8px" @click="exportData"
                     icon="el-icon-download">
            导出数据
          </el-button>
        </div>
        <div>
          <el-input placeholder="请输入项目序号进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
                    clearable
                    @clear="initExecutions"
                    style="width: 350px;margin-right: 10px" v-model="keyword"
                    @keydown.enter.native="initExecutions" :disabled="showAdvanceSearchView"></el-input>
          <el-button icon="el-icon-search" type="primary" @click="initExecutions"
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
            <el-col :span="8">
              计划名称:
              <el-input size="mini" style="width: 220px" clearable prefix-icon="el-icon-edit"
                        v-model="searchValue.planName"></el-input>
            </el-col>
            <el-col :span="8">
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
            <el-col :span="8">
              是否报销:
              <el-select v-model="searchValue.isReimburse" clearable placeholder="是否报销" size="mini"
                         style="width: 300px;">
                <el-option
                    v-for="item in reimburseTypes"
                    :key="item.id"
                    :label="item.name"
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
              执行时间:
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
              <el-button size="mini" icon="el-icon-search" type="primary" @click="initExecutionsAdv">搜索</el-button>
            </el-col>
          </el-row>
        </div>
      </transition>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="executions"
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
            prop="id"
            label="流水编号"
            width="80">
        </el-table-column>
        <el-table-column
            label="是否报销"
            width="70">
          <template slot-scope="scope">
            <span style="color: #00e079; font-weight: bold"
                  v-if="scope.row.isReimburse=='1'">{{ scope.row.reimburseName }}</span>
            <span v-else>{{ scope.row.reimburseName }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="funding"
            align="left"
            label="金额（万元）"
            width="100">
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
            prop="itemTypeName"
            :show-overflow-tooltip="true"
            width="120"
            label="预算科目">
        </el-table-column>
        <el-table-column
            prop="planItemId"
            width="100"
            label="预算明细序号">
        </el-table-column>
        <el-table-column
            prop="remark"
            align="left"
            label="备注">
        </el-table-column>
        <el-table-column
            prop="user.name"
            width="150"
            align="left"
            label="执行用户">
        </el-table-column>
        <el-table-column
            prop="executeDate"
            width="100"
            align="left"
            label="执行时间">
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
    <ExecuteSelect v-on:close="executeSelectVisible = false"
                   :executeSelectVisible="executeSelectVisible"
                   :title="title"></ExecuteSelect>
  </div>
</template>

<script>

import ExecuteSelect from "@/components/budget/ExecuteSelect";

export default {
  name: "BudgetProject",
  components: {
    ExecuteSelect,
  },
  data() {
    return {
      importDataBtnText: '导入数据',
      importDataBtnIcon: 'el-icon-upload2',
      importDataDisabled: false,
      searchValue: {
        planName: null,
        projectId: null,
        isReimburse: null,
        userEmail: null,
        dateScope: null
      },
      showAdvanceSearchView: false,
      executeSelectVisible: false,
      title: "",
      executions: [],
      recordIds: [],
      loading: false,
      total: 0,
      page: 1,
      keyword: '',
      size: 10,
      projectLists: [],
      reimburseTypes: [
        {
          id: 0,
          name: '未报销'
        },
        {
          id: 1,
          name: '已报销'
        }
      ],
      type: ""
    }
  },
  mounted() {
    this.initExecutions();
    this.initProjects();
  },
  methods: {
    onError(err, file, fileList) {
      this.importDataBtnText = '导入数据';
      this.importDataBtnIcon = 'el-icon-upload2';
      this.importDataDisabled = false;
    },
    onSuccess(response, file, fileList) {
      this.importDataBtnText = '导入数据';
      this.importDataBtnIcon = 'el-icon-upload2';
      this.importDataDisabled = false;
      this.initPlans();
    },
    beforeUpload() {
      this.importDataBtnText = '正在导入';
      this.importDataBtnIcon = 'el-icon-loading';
      this.importDataDisabled = true;
    },
    showSelectView() {
      this.title = '预算明细推荐Top10';
      this.executeSelectVisible = true;
    },
    handleSelectionChange(val) {
      this.recordIds = [];
      for (let i = 0; i < val.length; i++) {
        this.recordIds.push(val[i].id);
      }
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      if (this.type && this.type == 'advanced') {
        this.initExecutionsAdv();
      } else {
        this.initExecutions();
      }
    },
    currentChange(currentPage) {
      this.page = currentPage;
      if (this.type && this.type == 'advanced') {
        this.initExecutionsAdv();
      } else {
        this.initExecutions();
      }
    },
    initExecutionsAdv() {
      this.type = 'advanced'
      this.loading = true;
      let url = '/execution/get/?page=' + this.page + '&size=' + this.size;
      if (this.searchValue.isReimburse != null) {
        url += '&isReimburse=' + this.searchValue.isReimburse;
      }
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
          this.executions = resp.data;
          this.total = resp.total;
        }
      });
    },
    initExecutions() {
      this.type = '';
      this.loading = true;
      let url = '/execution/get/?page=' + this.page + '&size=' + this.size + "&id=" + this.keyword;
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.executions = resp.data;
          this.total = resp.total;
        }
      });
    },
    clearSearchValue() {
      this.searchValue = {
        isReimburse: null,
        planName: null,
        projectId: null,
        userEmail: null,
        dateScope: null
      }
    },
    revertExecution() {
      this.$confirm('此操作将永久删除选中的记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequestWithData("/execution/delete", this.recordIds).then(resp => {
          if (resp) {
            this.recordIds = [];
            this.initExecutions();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    initProjects() {
      let url = '/project/get/all';
      this.getRequest(url).then(resp => {
        if (resp) {
          this.projectLists = resp.obj;
        }
      });
    },
    cancelReimburse() {
      this.putRequest("/execution/cancel", this.recordIds).then(resp => {
        if (resp) {
          this.recordIds = [];
          this.initExecutions();
        }
      })
    },
    reimburse() {
      this.putRequest("/execution/reimburse", this.recordIds).then(resp => {
        if (resp) {
          this.recordIds = [];
          this.initExecutions();
        }
      })
    },
  }
}
</script>

<style scoped>
</style>
<template>
  <div>
    <div width="100%">
      <el-card class="box-card" style="background-color: #ffffff">
        <div slot="header" class="clearfix">
          <span class="box-title">项目总额度（万元）</span>
        </div>
        <div class="box-value">
          {{ headTableData.total }}
        </div>
      </el-card>
      <el-card class="box-card" style="background-color: #ffffff">
        <div slot="header" class="clearfix">
          <span class="box-title">执行额度（万元）</span>
        </div>
        <div class="box-value">
          {{ headTableData.inUse }}
        </div>
      </el-card>
      <el-card class="box-card" style="background-color: #ffffff">
        <div slot="header" class="clearfix">
          <span class="box-title">剩余额度（万元）</span>
        </div>
        <div class="box-value">
          {{ headTableData.free }}
        </div>
      </el-card>
      <el-card class="box-card" style="background-color: #ffffff">
        <div slot="header" class="clearfix">
          <span class="box-title">计划中额度（万元）</span>
        </div>
        <div class="box-value">
          {{ headTableData.inRepair }}
        </div>
      </el-card>
      <el-card class="box-card" style="background-color: #ffffff">
        <div slot="header" class="clearfix">
          <span class="box-title">预算执行率</span>
        </div>
        <div class="box-value">
          {{ headTableData.money }}
        </div>
      </el-card>
    </div>
    <div>
      <div style="display: flex;justify-content: space-between; width: 100%">
        <div>
          <el-button type="primary" icon="el-icon-plus" @click="showAddView">
            新增项目
          </el-button>
          <el-button type="success" style="display: inline-flex;margin-left: 8px" @click="exportData"
                     icon="el-icon-download">
            导出数据
          </el-button>
          <el-button @click="deleteProject" style="display: inline-flex;margin-left: 8px" type="danger">
            删除
          </el-button>
        </div>
        <div>
          <el-input placeholder="请输入项目序号进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
                    clearable
                    @clear="initProjects"
                    style="width: 350px;margin-right: 10px" v-model="keyword"
                    @keydown.enter.native="initProjects" :disabled="showAdvanceSearchView"></el-input>
          <el-button icon="el-icon-search" type="primary" @click="initProjects"
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
              项目名称:
              <el-input size="mini" style="width: 220px" clearable prefix-icon="el-icon-edit"
                        v-model="searchValue.userEmail"></el-input>
            </el-col>
            <el-col :span="10">
              项目类别:
              <el-select v-model="searchValue.status" clearable placeholder="状态" size="mini" style="width: 300px;">
                <el-option
                    v-for="item in projectTypes"
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
              <el-button size="mini" icon="el-icon-search" type="primary" @click="initOrdersAdv">搜索</el-button>
            </el-col>
          </el-row>
        </div>
      </transition>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="projects"
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
            label="项目编号"
            align="left"
            width="80">
          <template slot-scope="scope">
            <el-button size="mini" @click="getProject(scope.row)">{{ scope.row.id }}</el-button>
          </template>
        </el-table-column>
        <el-table-column
            prop="projectName"
            align="left"
            :show-overflow-tooltip="true"
            label="项目名称">
        </el-table-column>
        <el-table-column
            prop="projectType"
            :show-overflow-tooltip="true"
            label="项目类别">
        </el-table-column>
        <el-table-column
            prop="years"
            width="120"
            label="执行年限（年）">
        </el-table-column>
        <el-table-column
            prop="funding"
            width="120"
            label="资助额度（万元）">
        </el-table-column>
        <el-table-column
            prop="executeRate"
            width="95"
            align="left"
            label="执行率%">
        </el-table-column>
        <el-table-column
            prop="user.name"
            width="95"
            align="left"
            label="申请人">
        </el-table-column>
        <el-table-column
            prop="createTime"
            width="100"
            align="left"
            label="申请时间">
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
    <NewProject v-on:close="newProjectVisible = false" :newProjectVisible="newProjectVisible" :project="project"
                :title="title" :projectTypes="projectTypes"></NewProject>
    <ProjectDetail v-on:close="projectDetailVisible = false" :projectDetailVisible="projectDetailVisible"
                   :project="project" :title="title"></ProjectDetail>
  </div>
</template>

<script>
import ProjectDetail from "@/components/budget/ProjectDetail";
import NewProject from "@/components/budget/NewProject";

export default {
  name: "BudgetProject",
  data() {
    return {
      headTableData: {
        total: '',
        inUse: '',
        free: '',
        inRepair: '',
        money: '',
        cleaned: ''
      },
      searchValue: {
        status: null,
        userEmail: null,
        dateScope: null
      },
      title: '',
      showAdvanceSearchView: false,
      projects: [],
      loading: false,
      newProjectVisible: false,
      // 详情页可见性
      projectDetailVisible: false,
      total: 0,
      page: 1,
      keyword: '',
      size: 10,
      project: {
        id: "",
        name: "",
        type: "",
        years: "",
        funding: "",
        userEmail: "admin",
        user: {
          id: "",
          name: "",
          phone: "",
          username: "",
          enabled: "",
          remark: ""
        },
        createTime: "",
        updateTime: ""
      },
      projectTypes: [],
      type: ""
    }
  },
  components: {
    NewProject,
    ProjectDetail
  },
  mounted() {
    this.initProjectTypes();
    this.initProjects();
    this.initHeadTableData();
  },
  methods: {
    emptyProject() {
      this.project = {
        id: "",
        name: "",
        type: "",
        years: "",
        funding: "",
        userEmail: "",
        user: {
          id: "",
          name: "",
          phone: "",
          username: "",
        },
        createTime: "",
        updateTime: ""
      };
    },
    initHeadTableData() {
      let url = '/project/headTable'
      this.getRequest(url).then(resp => {
        if (resp) {
          this.headTableData = resp.obj;
        }
      });
    },
    showAddView() {
      this.emptyProject();
      this.title = '新增科研项目';
      this.newProjectVisible = true;
    },
    showDetailView(data) {
      this.title = '科研项目详情';
      this.project = data;
      this.projectDetailVisible = true;
    },
    sizeChange: function (currentSize) {
      this.size = currentSize;
      this.initOrders();
    },
    currentChange: function (currentPage) {
      this.page = currentPage;
      this.initOrdersAdv();
    },
    initProjects() {
      this.type = '';
      this.loading = true;
      let url = '/project/get/?page=' + this.page + '&size=' + this.size + "&projectId=" + this.keyword;
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.projects = resp.data;
          this.total = resp.total;
        }
      });
    },
    initOrdersAdv() {
      this.type = 'advanced'
      this.loading = true;
      let url = '/order/project/get/?page=' + this.page + '&size=' + this.size;
      if (this.searchValue.status) {
        url += '&status=' + this.searchValue.status;
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
          this.orders = resp.data;
          this.total = resp.total;
        }
      });
    },
    clearSearchValue() {
      this.searchValue = {
        status: null,
        userEmail: null,
        beginDateScope: null
      }
    },
    initProjectTypes() {
      let url = '/project/types/get';
      this.getRequest(url).then(resp => {
        if (resp) {
          this.projectTypes = resp.obj;
        }
      });
    },
  }
}
</script>

<style scoped>

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}

.box-card {
  width: 19.8%;
  float: left;
}

.box-title {
  font-size: 14px;
  font-family: 微软雅黑;
  font-weight: bold;
}

.box-value {
  font-size: 14px;
  font-family: 微软雅黑;
  font-weight: bold;
}
</style>
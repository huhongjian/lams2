<template>
  <div>
    <div>
      <div style="display: flex;justify-content: space-between; width: 100%">
        <div>
          <el-button type="primary" icon="el-icon-plus" @click="showAddWarningView">
            新增
          </el-button>
          <el-button type="info" @click="pauseConfig">
            暂停
          </el-button>
          <el-button @click="deleteConfig" style="display: inline-flex;margin-left: 8px" type="danger">
            删除
          </el-button>
        </div>
        <div>
          <el-input placeholder="请输入项目序号进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
                    clearable
                    @clear="initConfigs"
                    style="width: 350px;margin-right: 10px" v-model="keyword"
                    @keydown.enter.native="initConfigs" :disabled="showAdvanceSearchView"></el-input>
          <el-button icon="el-icon-search" type="primary" @click="initConfigs"
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
              预算计划:
              <el-select v-model="searchValue.planId" clearable placeholder="预算计划" size="mini"
                         style="width: 300px;">
                <el-option
                    v-for="item in planList"
                    :key="item.id"
                    :label="item.planName"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              科研项目:
              <el-select v-model="searchValue.projectId" clearable placeholder="科研项目" size="mini"
                         style="width: 300px;">
                <el-option
                    v-for="item in projectList"
                    :key="item.id"
                    :label="item.projectName"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              预算科目:
              <el-select v-model="searchValue.itemType" clearable placeholder="预算科目" size="mini"
                         style="width: 300px;">
                <el-option
                    v-for="item in planItemTypes"
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
              <el-button size="mini" icon="el-icon-search" type="primary" @click="initConfigsAdv">搜索</el-button>
            </el-col>
          </el-row>
        </div>
      </transition>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="configs"
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
            label="配置编号"
            align="left"
            width="80">
          <template slot-scope="scope">
            <el-button size="mini" @click="showConfigDetail(scope.row)">{{ scope.row.id }}</el-button>
          </template>
        </el-table-column>
        <el-table-column
            prop="configStatusName"
            label="状态"
            width="70">
          <template slot-scope="scope">
            <span style="color: #00e079; font-weight: bold"
                  v-if="scope.row.configStatus=='1'">{{ scope.row.configStatusName }}</span>
            <span style="color: #c0c0c0;"
                  v-else-if="scope.row.configStatus=='2'">{{ scope.row.configStatusName }}</span>
            <span v-else>{{ scope.row.configStatusName }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="projectName"
            align="left"
            :show-overflow-tooltip="true"
            label="项目名称">
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
            prop="executeRate"
            width="120"
            label="预期执行率（%）">
        </el-table-column>
        <el-table-column
            prop="userEmail"
            width="100"
            :show-overflow-tooltip="true"
            label="创建人">
        </el-table-column>
        <el-table-column
            prop="createTime"
            width="100"
            align="left"
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
    <NewWarning v-on:close="newWarningVisible = false" :newWarningVisible="newWarningVisible"
                :warningConfig="warningConfig"
                :title="title"></NewWarning>
    <WarningDetail v-on:close="warningDetailVisible = false" :warningDetailVisible="warningDetailVisible"
                   :warningConfig="warningConfig"
                   :title="title"></WarningDetail>
  </div>
</template>

<script>
import NewWarning from "@/components/budget/NewWarning";
import WarningDetail from "@/components/budget/WarningDetail";

export default {
  name: "BudgetConf",
  components: {
    NewWarning,
    WarningDetail
  },
  mounted() {
    this.initConfigs();
    this.initPlans();
    this.initProjects();
  },
  data() {
    return {
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
        planId: null,
        projectId: null,
        itemType: null,
        userEmail: null,
        dateScope: null
      },
      title: '',
      showAdvanceSearchView: false,
      configs: [],
      loading: false,
      newWarningVisible: false,
      // 详情页可见性
      warningDetailVisible: false,
      total: 0,
      page: 1,
      keyword: '',
      size: 10,
      warningConfig: {
        id: "",
        projectId: "",
        planId: "",
        itemType: "",
        executeRate: "",
        userEmail: "",
        toUser: [],
        dateScope: [],
        createTime: "",
        updateTime: ""
      },
      projectTypes: [],
      planList: [],
      projectList: [],
      configIds: [],
      type: ""
    }
  },
  methods: {
    initPlans() {
      let url = '/plan/get/all';
      this.getRequest(url).then(resp => {
        if (resp) {
          this.planList = resp.obj;
        }
      });
    },
    initProjects() {
      let url = '/project/get/all';
      this.getRequest(url).then(resp => {
        if (resp) {
          this.projectList = resp.obj;
        }
      });
    },
    showConfigDetail(data) {
      this.title = '预算报警配置详情';
      this.warningConfig = data;
      this.warningDetailVisible = true;
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      if (this.type && this.type == 'advanced') {
        this.initConfigsAdv();
      } else {
        this.initConfigs();
      }
    },
    currentChange(currentPage) {
      this.page = currentPage;
      if (this.type && this.type == 'advanced') {
        this.initConfigsAdv();
      } else {
        this.initConfigs();
      }
    },
    handleSelectionChange(val) {
      this.configIds = [];
      for (let i = 0; i < val.length; i++) {
        this.configIds.push(val[i].id);
      }
    },
    pauseConfig() {
      this.putRequest("/warning/config/pause", this.configIds).then(resp => {
        if (resp) {
          this.configIds = [];
          this.initConfigs();
        }
      })
    },
    deleteConfig() {
      this.$confirm('此操作将永久删除选中的记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequestWithData("/warning/config/delete", this.configIds).then(resp => {
          if (resp) {
            this.configIds = [];
            this.initConfigs();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    initConfigs() {
      this.type = '';
      this.loading = true;
      let url = '/warning/config/get/?page=' + this.page + '&size=' + this.size + "&projectId=" + this.keyword;
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.configs = resp.data;
          this.total = resp.total;
        }
      });
    },
    emptyConfig() {
      this.warningConfig = {
        id: "",
        projectId: "",
        planId: "",
        itemType: "",
        executeRate: "",
        userEmail: "",
        toUser: [],
        dateScope: "",
        createTime: "",
        updateTime: ""
      };
    },
    showAddWarningView() {
      this.emptyConfig();
      this.title = '新增预算报警';
      this.newWarningVisible = true;
    },
    showEditView(data) {
      this.title = '编辑预算报警';
      this.warningConfig = data;
      this.warningConfig.dateScope = [];
      this.warningConfig.dateScope.push(data.startTime);
      this.warningConfig.dateScope.push(data.endTime);
      this.newWarningVisible = true;
    },
    handleClose() {
      this.$emit('close');
    },
    initConfigsAdv() {
      this.type = 'advanced'
      this.loading = true;
      let url = '/warning/config/get?page=' + this.page + '&size=' + this.size;
      if (this.searchValue.planId) {
        url += '&planId=' + this.searchValue.planId;
      }
      if (this.searchValue.projectId) {
        url += '&projectId=' + this.searchValue.projectId;
      }
      if (this.searchValue.itemType) {
        url += '&itemType=' + this.searchValue.itemType;
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
          this.configs = resp.data;
          this.total = resp.total;
        }
      });
    },
    clearSearchValue() {
      this.searchValue = {
        planId: null,
        projectId: null,
        itemType: null,
        userEmail: null,
        dateScope: null
      }
    },
  }
}
</script>

<style scoped>

</style>
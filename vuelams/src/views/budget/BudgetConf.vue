<template>
  <div>
    <div>
      <div style="display: flex;justify-content: space-between; width: 100%">
        <div>
          <el-button type="primary" icon="el-icon-plus" @click="showAddWarningView">
            新增
          </el-button>
          <el-button type="info" @click="showAddView">
            暂停
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
                        v-model="searchValue.projectName"></el-input>
            </el-col>
            <el-col :span="10">
              项目类别:
              <el-select v-model="searchValue.projectType" clearable placeholder="项目类别" size="mini"
                         style="width: 300px;">
                <el-option
                    v-for="item in projectTypes"
                    :key="item.id"
                    :label="item.name"
                    :value="item.name">
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
              <el-button size="mini" icon="el-icon-search" type="primary" @click="initProjectsAdv">搜索</el-button>
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
            width="55">
        </el-table-column>
        <el-table-column
            fixed
            label="配置编号"
            align="left"
            width="80">
          <template slot-scope="scope">
            <el-button size="mini" @click="getOperateList(scope.row)">{{ scope.row.id }}</el-button>
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
            width="120"
            label="预算科目">
        </el-table-column>
        <el-table-column
            prop="executeRate"
            width="90"
            label="报警执行率">
        </el-table-column>
        <el-table-column
            prop="freqName"
            width="80"
            align="left"
            label="报警频率">
        </el-table-column>
        <el-table-column
            prop="toUserString"
            width="200"
            align="left"
            label="接收人">
        </el-table-column>
        <el-table-column
            prop="userEmail"
            width="150"
            align="left"
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
    <ProjectDetail v-on:close="projectDetailVisible = false" :projectDetailVisible="projectDetailVisible"
                   :project="project" :title="title"></ProjectDetail>
  </div>
</template>

<script>
import PlanDetail from "@/components/budget/PlanDetail";
import NewWarning from "@/components/budget/NewWarning";

export default {
  name: "BudgetConf",
  components: {
    NewWarning,
    PlanDetail
  },
  mounted() {
    this.initConfigs();
  },
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
        projectName: null,
        projectType: null,
        userEmail: null,
        dateScope: null
      },
      title: '',
      showAdvanceSearchView: false,
      configs: [],
      loading: false,
      newWarningVisible: false,
      // 详情页可见性
      projectDetailVisible: false,
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
        dateScope: "",
        createTime: "",
        updateTime: ""
      },
      projectTypes: [],
      type: ""
    }
  },
  methods: {
    initConfigs(){
      this.type = '';
      this.loading = true;
      let url = '/warning/get/?page=' + this.page + '&size=' + this.size + "&projectId=" + this.keyword;
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
    handleClose() {
      this.countType = null;
      this.$emit('close');
    },
  }
}
</script>

<style scoped>

</style>
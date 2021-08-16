<template>
  <div>
    <div>
      <div style="display: flex;justify-content: space-between">
        <div style="display: inline-flex;margin-left: 8px"></div>
        <div>
          <el-input placeholder="请输入操作记录编号进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
                    clearable
                    @clear="initRecords"
                    style="width: 350px;margin-right: 10px" v-model="keyword"
                    @keydown.enter.native="initRecords" :disabled="showAdvanceSearchView"></el-input>
          <el-button icon="el-icon-search" type="primary" @click="initRecords" :disabled="showAdvanceSearchView">
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
            prop="id"
            label="报警编号"
            align="left"
            width="80">
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
            label="预算计划">
        </el-table-column>
        <el-table-column
            prop="years"
            width="120"
            label="预算科目">
        </el-table-column>
        <el-table-column
            prop="funding"
            width="120"
            label="报警执行率">
        </el-table-column>
        <el-table-column
            prop="executeRate"
            width="95"
            align="left"
            label="报警频率">
        </el-table-column>
        <el-table-column
            prop="executeRate"
            width="200"
            align="left"
            :show-overflow-tooltip="true"
            label="接收人">
        </el-table-column>
        <el-table-column
            prop="user.name"
            width="95"
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
export default {
  name: "BudgetWarn",
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
      projects: [],
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
        configStatus: "",
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
    emptyConfig() {
      this.warningConfig = {
        id: "",
        projectId: "",
        planId: "",
        itemType: "",
        configStatus: "",
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
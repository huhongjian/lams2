<template>
  <div>
    <div>
      <div style="display: flex;justify-content: space-between">
        <div style="display: inline-flex;margin-left: 8px"></div>
        <div>
          <el-input placeholder="请输入记录编号进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
                    clearable
                    @clear="initContents"
                    style="width: 350px;margin-right: 10px" v-model="keyword"
                    @keydown.enter.native="initContents" :disabled="showAdvanceSearchView"></el-input>
          <el-button icon="el-icon-search" type="primary" @click="initContents" :disabled="showAdvanceSearchView">
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
              <el-select v-model="searchValue.projectName" clearable placeholder="科研项目" size="mini"
                         style="width: 300px;">
                <el-option
                    v-for="item in projectLists"
                    :key="item.id"
                    :label="item.projectName"
                    :value="item.projectName">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              预算科目:
              <el-select v-model="searchValue.itemTypeName" clearable placeholder="科研项目" size="mini"
                         style="width: 300px;">
                <el-option
                    v-for="item in planItemTypes"
                    :key="item.id"
                    :label="item.name"
                    :value="item.name">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="10">
              接收人邮箱:
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
              <el-button size="mini" icon="el-icon-search" type="primary" @click="initContentsAdv">搜索</el-button>
            </el-col>
          </el-row>
        </div>
      </transition>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="contents"
          stripe
          border
          v-loading="loading"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.8)"
          style="width: 100%">
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
            prop="planName"
            :show-overflow-tooltip="true"
            label="预算计划">
        </el-table-column>
        <el-table-column
            prop="itemTypeName"
            width="120"
            :show-overflow-tooltip="true"
            label="预算科目">
        </el-table-column>
        <el-table-column
            prop="executeRate"
            width="95"
            label="当前执行率%">
        </el-table-column>
        <el-table-column
            prop="needExecuteRate"
            width="95"
            align="left"
            label="预期执行率%">
        </el-table-column>
        <el-table-column
            prop="toEmail"
            width="150"
            align="left"
            :show-overflow-tooltip="true"
            label="接收人">
        </el-table-column>
        <el-table-column
            prop="createTime"
            width="100"
            align="left"
            label="创建时间">
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
  </div>
</template>

<script>
export default {
  name: "BudgetWarn",
  mounted() {
    this.initContents();
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
        planName: null,
        projectName: null,
        itemTypeName: null,
        userEmail: null,
        dateScope: null
      },
      showAdvanceSearchView: false,
      contents: [],
      loading: false,
      total: 0,
      page: 1,
      keyword: '',
      size: 10,
      projectLists: [],
      type: ""
    }
  },
  methods: {
    sizeChange(currentSize) {
      this.size = currentSize;
      if (this.type && this.type == 'advanced') {
        this.initContentsAdv();
      } else {
        this.initContents();
      }
    },
    currentChange(currentPage) {
      this.page = currentPage;
      if (this.type && this.type == 'advanced') {
        this.initContentsAdv();
      } else {
        this.initContents();
      }
    },
    initContents() {
      this.type = '';
      this.loading = true;
      let url = '/warning/record/get/?page=' + this.page + '&size=' + this.size + "&id=" + this.keyword;
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.contents = resp.data;
          this.total = resp.total;
        }
      });
    },
    initContentsAdv() {
      this.type = 'advanced'
      this.loading = true;
      let url = '/warning/record/get/?page=' + this.page + '&size=' + this.size;
      if (this.searchValue.planName) {
        url += '&planName=' + this.searchValue.planName;
      }
      if (this.searchValue.projectName) {
        url += '&projectName=' + this.searchValue.projectName;
      }
      if (this.searchValue.itemTypeName) {
        url += '&itemTypeName=' + this.searchValue.itemTypeName;
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
          this.contents = resp.data;
          this.total = resp.total;
        }
      });
    },
    clearSearchValue() {
      this.searchValue = {
        planName: null,
        projectName: null,
        itemTypeName: null,
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
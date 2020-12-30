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
            <el-col :span="8">
              类型:
              <el-select v-model="searchValue.operate"
                         clearable
                         placeholder="类型"
                         size="mini"
                         style="width: 130px;">
                <el-option
                    v-for="item in types"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="8">
              操作人邮箱:
              <el-input size="mini" style="width: 150px" clearable prefix-icon="el-icon-edit"
                        v-model="searchValue.operatorMail"></el-input>
            </el-col>
          </el-row>
          <el-row style="margin-top: 10px">
            <el-col :span="8">
              操作说明:
              <el-input size="mini" style="width: 200px" prefix-icon="el-icon-edit"
                        clearable
                        v-model="searchValue.text"></el-input>
            </el-col>
            <el-col :span="8">
              申请时间:
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
            <el-col :span="4" :offset="4">
              <el-button size="mini" @click="clearSearchValue">重置</el-button>
              <el-button size="mini" @click="showAdvanceSearchView = false">取消</el-button>
              <el-button size="mini" icon="el-icon-search" type="primary" @click="initRecordsAdv">搜索</el-button>
            </el-col>
          </el-row>
        </div>
      </transition>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="records"
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
            label="操作记录编号"
            width="100">
        </el-table-column>
        <el-table-column
            fixed
            prop="operateName"
            label="操作类型"
            width="100">
        </el-table-column>
        <el-table-column
            prop="operatorName"
            label="操作人"
            width="100">
        </el-table-column>
        <el-table-column
            prop="operatorMail"
            label="操作人邮箱"
            width="150">
        </el-table-column>
        <el-table-column
            prop="text"
            :show-overflow-tooltip="true"
            label="操作说明">
        </el-table-column>
        <el-table-column
            prop="operateTime"
            width="150"
            align="left"
            label="操作时间">
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
  name: "SysLog",
  data() {
    return {
      searchValue: {
        operate: null,
        operatorMail: null,
        text: null,
        dateScope: null
      },
      title: '',
      showAdvanceSearchView: false,
      records: [],
      loading: false,
      total: 0,
      page: 1,
      keyword: '',
      size: 10,
      types: [
        {
          id: 1,
          name: '修改资产信息'
        },
        {
          id: 2,
          name: '修改资产状态'
        },
        {
          id: 3,
          name: '资产采购申请'
        },
        {
          id: 4,
          name: '资产借用申请'
        },
        {
          id: 5,
          name: '编辑工单信息'
        },
        {
          id: 6,
          name: '删除工单'
        },
        {
          id: 7,
          name: '取消工单'
        },
        {
          id: 8,
          name: '处理工单'
        },
        {
          id: 9,
          name: '归还资产'
        }],
      record: {
        id: "",
        operate: "",
        operateName: "",
        operator: "",
        operatorMail: "",
        text: "",
        operateTime: ""
      },
      type: ""
    }
  },
  mounted() {
    this.initRecords();
  },
  methods: {
    sizeChange(currentSize) {
      this.size = currentSize;
      if (this.type && this.type == 'advanced') {
        this.initRecordsAdv();
      } else {
        this.initRecords();
      }
    },
    currentChange(currentPage) {
      this.page = currentPage;
      if (this.type && this.type == 'advanced') {
        this.initRecordsAdv();
      } else {
        this.initRecords();
      }
    },
    initRecords() {
      this.type = '';
      this.loading = true;
      let url = '/record/get/?page=' + this.page + '&size=' + this.size + "&id=" + this.keyword;
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.records = resp.data;
          this.total = resp.total;
        }
      });
    },
    initRecordsAdv() {
      this.type = 'advanced'
      this.loading = true;
      let url = '/record/get/?page=' + this.page + '&size=' + this.size;
      if (this.searchValue.operate) {
        url += '&operate=' + this.searchValue.operate;
      }
      if (this.searchValue.operatorMail) {
        url += '&operatorMail=' + this.searchValue.operatorMail;
      }
      if (this.searchValue.text) {
        url += '&text=' + this.searchValue.text;
      }
      if (this.searchValue.dateScope) {
        url += '&dateScope=' + this.searchValue.dateScope;
      }
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.records = resp.data;
          this.total = resp.total;
        }
      });
    },
    clearSearchValue() {
      this.searchValue = {
        operate: null,
        operatorMail: null,
        text: null,
        dateScope: null
      }
    }
  }
}
</script>

<style scoped>

</style>
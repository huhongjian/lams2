<template>
  <div>
    <div>
      <div style="display: flex;justify-content: space-between">
        <div>
          <el-button type="primary" icon="el-icon-plus" @click="showAddView">
            归还资产
          </el-button>
          <el-button type="success" @click="exportData"
                     icon="el-icon-download">
            导出数据
          </el-button>
          <el-button @click="deleteOrder" style="display: inline-flex;margin-left: 8px" type="danger">
            删除
          </el-button>
        </div>
        <div>
          <el-input placeholder="请输入归还单号进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
                    clearable
                    @clear="initOrders"
                    style="width: 350px;margin-right: 10px" v-model="keyword"
                    @keydown.enter.native="initOrders" :disabled="showAdvanceSearchView"></el-input>
          <el-button icon="el-icon-search" type="primary" @click="initOrders" :disabled="showAdvanceSearchView">
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
            <el-col :span="6">
              类型:
              <el-select v-model="searchValue.type"
                         clearable
                         placeholder="类型"
                         size="mini"
                         style="width: 130px;">
                <el-option
                    v-for="item in types"
                    :key="item.id"
                    :label="item.name"
                    :value="item.name">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="5">
              品牌:
              <el-input size="mini" style="width: 100px" prefix-icon="el-icon-edit"
                        clearable
                        v-model="searchValue.brand"></el-input>
            </el-col>
            <el-col :span="5">
              状态:
              <el-select v-model="searchValue.status" clearable placeholder="状态" size="mini" style="width: 130px;">
                <el-option
                    v-for="item in statuses"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
          <el-row style="margin-top: 10px">
            <el-col :span="6">
              负责人邮箱:
              <el-input size="mini" style="width: 150px" clearable prefix-icon="el-icon-edit"
                        v-model="searchValue.userEmail"></el-input>
            </el-col>
            <el-col :span="9">
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
            <el-col :span="5" :offset="4">
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
          :data="orders"
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
            fixed
            label="归还单号"
            align="left"
            width="90">
          <template slot-scope="scope">
            <el-button size="mini" @click="getOperateList(scope.row)">{{ scope.row.id }}</el-button>
          </template>
        </el-table-column>
        <el-table-column
            prop="statusName"
            width="90"
            label="状态">
          <template slot-scope="scope">
            <span style="color: #00e079; font-weight: bold"
                  v-if="scope.row.status=='11'">{{ scope.row.statusName }}</span>
            <span style="color: #ff4777; font-weight: bold"
                  v-else-if="scope.row.status=='12'">{{ scope.row.statusName }}</span>
            <span style="color: #c0c0c0;"
                  v-else-if="scope.row.status=='7'">{{ scope.row.statusName }}</span>
            <span v-else>{{ scope.row.statusName }}</span>
          </template>
        </el-table-column>
        <el-table-column
            :show-overflow-tooltip="true"
            label="关联资产编号">
          <template slot-scope="scope">
            <el-tag type="success" style="margin-right: 4px" v-for="(asset,indexj) in scope.row.assetList"
                    :key="indexj">{{ asset.id }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="user.name"
            width="95"
            align="left"
            label="申请人">
        </el-table-column>
        <el-table-column
            prop="user.username"
            width="150"
            label="申请人邮箱">
        </el-table-column>
        <el-table-column
            prop="user.phone"
            width="100"
            label="申请人电话">
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
            <el-button v-show="scope.row.status=='10'&&scope.row.user.username==user.username"
                       @click="showEditView(scope.row)">编辑
            </el-button>
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
    <NewOrderReturn v-on:close="dialogVisible4 = false" :dialogVisible4="dialogVisible4" :order="order" :title="title"
                    :types="types"></NewOrderReturn>
    <OrderDetail v-on:close="dialogVisible7 = false" :dialogVisible7="dialogVisible7" :order="order" :title="title"
                 :urlList="urlList" :operateList='operateList'></OrderDetail>
  </div>
</template>

<script>
import OrderDetail from "@/components/order/OrderDetail";
import NewOrderReturn from "@/components/order/NewOrderReturn";

export default {
  name: "AssetReturn",
  data() {
    return {
      user: JSON.parse(window.sessionStorage.getItem("user")),
      searchValue: {
        type: null,
        brand: null,
        status: null,
        userEmail: null,
        dateScope: null
      },
      title: '',
      showAdvanceSearchView: false,
      orders: [],
      loading: false,
      dialogVisible5: false,
      dialogVisible7: false,
      dialogVisible4: false,
      total: 0,
      page: 1,
      keyword: '',
      size: 10,
      types: [],
      statuses: [
        {
          id: 3,
          name: "已入库"
        },
        {
          id: 4,
          name: "申请借用"
        },
        {
          id: 5,
          name: "已借出"
        },
        {
          id: 6,
          name: "审批未通过"
        },
        {
          id: 7,
          name: "已关闭"
        }
      ],
      operateList: [],
      order: {
        id: "",
        category: "",
        categoryName: "",
        status: "",
        statusName: "",
        expireTime: "",
        reason: "",
        userEmail: "",
        user: {
          id: "",
          name: "",
          phone: "",
          username: "",
          enabled: "",
          remark: ""
        },
        createTime: "",
        updateTime: "",
        assetList: []
      },
      // 搜索类型，空是普通搜索，‘advanced’是高级搜索
      type: "",
      // 资产图片列表，用于编辑页面
      fileList: [],
      // 资产图片url列表，用于详情页面
      urlList: []
    }
  },
  components: {
    NewOrderReturn,
    OrderDetail
  },
  mounted() {
    this.initOrders();
    this.initTypes();
  },
  methods: {
    exportData() {
      let url = '/order/basic/export/out/?category=4';
      if (this.type && this.type == 'advanced') {
        if (this.searchValue.type) {
          url += '&type=' + this.searchValue.type;
        }
        if (this.searchValue.brand) {
          url += '&brand=' + this.searchValue.brand;
        }
        if (this.searchValue.status) {
          url += '&status=' + this.searchValue.status;
        }
        if (this.searchValue.userEmail) {
          url += '&userEmail=' + this.searchValue.userEmail;
        }
        if (this.searchValue.dateScope) {
          url += '&dateScope=' + this.searchValue.dateScope;
        }
      } else {
        url += "&id=" + this.keyword;
      }
      window.open(url, '_parent');
    },
    emptyOrder() {
      this.order = {
        id: "",
        category: "",
        categoryName: "",
        status: "",
        statusName: "",
        expireTime: "",
        reason: "",
        userEmail: "",
        user: {
          id: "",
          name: "",
          phone: "",
          username: "",
        },
        createTime: "",
        updateTime: "",
        assetList: []
      };
    },
    showAddView() {
      this.emptyOrder();
      this.title = '归还资产';
      this.dialogVisible4 = true;
    },
    showEditView(data) {
      this.title = '编辑申请信息';
      this.order = data;
      this.dialogVisible4 = true;
    },
    showDetailView(data) {
      this.title = '申请单详情';
      this.order = data;
      if (this.order.asset && this.order.asset.fileList) {
        this.urlList = [];
        for (let i = 0; i < this.order.asset.fileList.length; i++) {
          this.urlList.push(this.order.asset.fileList[i].url);
        }
      } else {
        this.urlList = null;
      }
      this.dialogVisible7 = true;
    },
    getOperateList(data) {
      this.getRequest('/order/task/getOperateList?id=' + data.id).then(resp => {
        if (resp) {
          this.operateList = resp.obj;
          this.showDetailView(data);
        }
      });
    },
    deleteOrder() {
      this.$confirm('此操作将永久删除选中的记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequestWithData("/order/basic/delete", this.orderIds).then(resp => {
          if (resp) {
            this.initOrders();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      if (this.type && this.type == 'advanced') {
        this.initOrdersAdv();
      } else {
        this.initOrders();
      }
    },
    currentChange(currentPage) {
      this.page = currentPage;
      if (this.type && this.type == 'advanced') {
        this.initOrdersAdv();
      } else {
        this.initOrders();
      }
    },
    initOrders() {
      this.type = '';
      this.loading = true;
      let url = '/order/basic/get/?category=4&page=' + this.page + '&size=' + this.size + "&oid=" + this.keyword;
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.orders = resp.data;
          this.total = resp.total;
        }
      });
    },
    initOrdersAdv() {
      this.type = 'advanced'
      this.loading = true;
      let url = '/order/basic/get/?category=4&page=' + this.page + '&size=' + this.size;
      if (this.searchValue.type) {
        url += '&type=' + this.searchValue.type;
      }
      if (this.searchValue.brand) {
        url += '&brand=' + this.searchValue.brand;
      }
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
    handleSelectionChange(val) {
      for (let i = 0; i < val.length; i++) {
        this.orderIds.push(val[i].id);
      }
    },
    clearSearchValue() {
      this.searchValue = {
        type: null,
        brand: null,
        status: null,
        userEmail: null,
        dateScope: null
      }
    },
    initTypes() {
      let url = '/asset/types/get';
      this.getRequest(url).then(resp => {
        if (resp) {
          this.types = resp.obj;
        }
      });
    }
  }
}
</script>

<style>
</style>
<template>
  <div>
    <div>
      <div style="display: flex;justify-content: space-between">
        <div>
          <el-button type="primary" icon="el-icon-plus" @click="showAddEmpView">
            新增资产
          </el-button>
          <el-button type="success" style="display: inline-flex;margin-left: 8px" @click="exportData"
                     icon="el-icon-download">
            导出数据
          </el-button>
          <el-button @click="deleteOrder" style="display: inline-flex;margin-left: 8px" type="danger">
            删除
          </el-button>
        </div>
        <div>
          <el-input placeholder="请输入采购单号进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
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
            <el-col :span="5">
              申请人邮箱:
              <el-input size="mini" style="width: 150px" clearable prefix-icon="el-icon-edit"
                        v-model="searchValue.userEmail"></el-input>
            </el-col>
          </el-row>
          <el-row style="margin-top: 10px">
            <el-col :span="6">
              价格:
              <el-input size="mini" style="width: 100px" prefix-icon="el-icon-edit"
                        clearable
                        v-model="searchValue.priceLow"></el-input>
              至:
              <el-input size="mini" style="width: 100px" prefix-icon="el-icon-edit"
                        clearable
                        v-model="searchValue.priceHigh"></el-input>
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
            fixed
            label="采购单号"
            align="left"
            width="80">
          <template slot-scope="scope">
            <el-button size="mini" @click="getOperateList(scope.row)">{{ scope.row.id }}</el-button>
          </template>
        </el-table-column>
        <el-table-column
            fixed
            prop="asset.id"
            align="left"
            label="资产编号"
            width="80">
        </el-table-column>
        <el-table-column
            prop="asset.type"
            align="left"
            label="类型"
            width="60">
        </el-table-column>
        <el-table-column
            prop="asset.brand"
            label="品牌"
            align="left"
            width="60">
        </el-table-column>
        <el-table-column
            prop="statusName"
            width="90"
            label="状态">
          <template slot-scope="scope">
            <span style="color: #00e079; font-weight: bold"
                  v-if="scope.row.status=='2'||scope.row.status=='3'">{{ scope.row.statusName }}</span>
            <span style="color: #ff4777; font-weight: bold"
                  v-else-if="scope.row.status=='6'||scope.row.status=='8'||scope.row.status=='7'">{{
                scope.row.statusName
              }}</span>
            <span style="color: #c0c0c0;"
                  v-else-if="scope.row.status=='5'">{{ scope.row.statusName }}</span>
            <span v-else>{{ scope.row.statusName }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="asset.price"
            width="90"
            label="价格（元）">
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
            align="left"
            label="申请人邮箱">
        </el-table-column>
        <el-table-column
            prop="user.phone"
            width="100"
            label="申请人电话">
        </el-table-column>
        <el-table-column
            prop="reason"
            :show-overflow-tooltip="true"
            label="申请理由">
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
    <OrderEdit v-on:close="dialogVisible = false" :dialogVisible="dialogVisible" :order="order" :fileList="fileList"
               :title="title"></OrderEdit>
    <OrderDetail v-on:close="dialogVisible2 = false" :dialogVisible2="dialogVisible2" :order="order" :title="title"
                 :urlList="urlList" :operateList='operateList'></OrderDetail>
  </div>
</template>

<script>
import OrderDetail from "@/components/order/OrderDetail";
import OrderEdit from "@/components/order/OrderEdit";

export default {
  name: "EmpBasic",
  data() {
    return {
      searchValue: {
        type: null,
        brand: null,
        status: null,
        userEmail: null,
        priceLow: null,
        priceHigh: null,
        dateScope: null
      },
      title: '',
      showAdvanceSearchView: false,
      orders: [],
      loading: false,
      // 编辑页面/新增页面可见性
      dialogVisible: false,
      // 详情页可见性
      dialogVisible2: false,
      total: 0,
      page: 1,
      keyword: '',
      size: 10,
      types: [
        {
          id: 1,
          name: '手机'
        }, {
          id: 2,
          name: '主机'
        }, {
          id: 3,
          name: '交换机'
        }, {
          id: 4,
          name: '测距仪'
        }],
      statuses: [
        {
          id: 1,
          name: "申请采购"
        },
        {
          id: 2,
          name: "审批通过"
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
      // 选中的工单id，删除时使用
      orderIds: [],
      order: {
        id: "",
        category: "",
        categoryName: "",
        status: "",
        statusName: "",
        expireTime: "",
        reason: "测试",
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
        updateTime: "",
        asset: {
          id: "",
          status: "",
          statusName: "",
          brand: "华为",
          type: "手机",
          price: "4000",
          fileList: [],
          adv: {}
        }
      },
      // 搜索类型，空是普通搜索，‘advanced’是高级搜索
      type: "",
      fileList: [],
      urlList: []
    }
  },
  components: {
    OrderDetail,
    OrderEdit
  },
  mounted() {
    this.initOrders();
  },
  methods: {
    exportData() {
      let url = '/order/basic/export/?category=1';
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
        if (this.searchValue.priceLow) {
          url += '&priceLow=' + this.searchValue.priceLow;
        }
        if (this.searchValue.priceHigh) {
          url += '&priceHigh=' + this.searchValue.priceHigh;
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
        reason: "测试",
        userEmail: "admin",
        user: {
          id: "",
          name: "",
          phone: "",
          username: "",
        },
        createTime: "",
        updateTime: "",
        asset: {
          id: "",
          brand: "华为",
          type: "手机",
          price: "4000",
          adv: {},
        }
      };
    },
    showAddEmpView() {
      this.emptyOrder();
      this.fileList = null;
      this.title = '资产采购申请';
      this.dialogVisible = true;
    },
    showEditView(data) {
      this.title = '编辑申请信息';
      this.order = data;
      if (this.order.asset.fileList && this.order.asset.fileList.length > 0) {
        this.fileList = this.order.asset.fileList;
      } else {
        this.fileList = [];
      }
      this.dialogVisible = true;
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
      this.dialogVisible2 = true;
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
      let url = '/order/basic/get/?category=1&page=' + this.page + '&size=' + this.size + "&oid=" + this.keyword;
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
      let url = '/order/basic/get/?category=1&page=' + this.page + '&size=' + this.size;
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
      if (this.searchValue.priceLow) {
        url += '&priceLow=' + this.searchValue.priceLow;
      }
      if (this.searchValue.priceHigh) {
        url += '&priceHigh=' + this.searchValue.priceHigh;
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
        priceLow: null,
        priceHigh: null,
        dateScope: null
      }
    }
  }
}
</script>

<style>
</style>
<template>
  <div>
    <div>
      <div style="display: flex;justify-content: space-between">
        <div>
          <p style="display: inline-flex;margin-left: 8px"></p>
        </div>
        <div>
          <el-input placeholder="请输入申请单编号进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
                    clearable
                    @clear="initOrders"
                    style="width: 350px;margin-right: 10px" v-model="keyword"
                    @keydown.enter.native="initOrders" :disabled="showAdvanceSearchView"></el-input>
          <el-button icon="el-icon-search" type="primary" @click="initOrders"
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
              申请人邮箱:
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
    <Mine v-on:currentSize="sizeChange" v-on:currentPage="currentChange" :orders="orders" :total="total" :page="page"
          :size="size" :loading="loading"></Mine>
    <OrderEdit v-on:close="dialogVisible = false" :dialogVisible="dialogVisible" :order="order" :fileList="fileList"
               :title="title"></OrderEdit>
    <OrderDetail v-on:close="dialogVisible2 = false" :dialogVisible2="dialogVisible2" :order="order" :title="title"
                 :operateList='operateList'></OrderDetail>
  </div>
</template>

<script>
import OrderDetail from "@/components/order/OrderDetail";
import OrderEdit from "@/components/order/OrderEdit";
import Mine from "@/components/order/Mine";

export default {
  name: "MyApply",
  data() {
    return {
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
      dialogVisible: false,
      // 详情页可见性
      dialogVisible2: false,
      total: 0,
      page: 1,
      keyword: '',
      size: 10,
      operateList: [],
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
          adv: {}
        }
      },
      types: [],
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
        },
        {
          id: 8,
          name: "审批未通过"
        }
      ],
      type: "",
      fileList: []
    }
  },
  components: {
    Mine,
    OrderDetail,
    OrderEdit
  },
  mounted() {
    this.initOrders();
    this.initTypes();
  },
  methods: {
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
    showEditView(data) {
      this.title = '编辑申请信息';
      this.order = data;
      if (this.order.asset.fileList && this.order.asset.fileList.length > 0) {
        this.fileList = this.order.asset.fileList;
      } else {
        this.fileList = null;
      }
      this.dialogVisible = true;
    },
    showDetailView(data) {
      this.title = '申请单详情';
      this.order = data;
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
    sizeChange: function (currentSize) {
      this.size = currentSize;
      this.initOrders();
    },
    currentChange: function (currentPage) {
      this.page = currentPage;
      this.initOrdersAdv();
    },
    initOrders() {
      this.type = '';
      this.loading = true;
      let url = '/order/task/getMyApply/?page=' + this.page + '&size=' + this.size + "&oid=" + this.keyword;
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
      let url = '/order/task/getMyApply/?page=' + this.page + '&size=' + this.size;
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
    clearSearchValue() {
      this.searchValue = {
        type: null,
        brand: null,
        status: null,
        userEmail: null,
        beginDateScope: null
      }
    },
    initTypes() {
      let url = '/asset/types/get';
      this.getRequest(url).then(resp => {
        if (resp) {
          this.types = resp.obj;
        }
      });
    },
  }
}
</script>

<style>
</style>
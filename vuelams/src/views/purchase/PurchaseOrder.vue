<template>
  <div>
    <div>
      <div style="display: flex;justify-content: space-between">
        <div>
          <el-button type="success" style="display: inline-flex;margin-left: 8px" @click="exportData"
                     icon="el-icon-download">
            导出数据
          </el-button>
          <el-button @click="deleteOrder" style="display: inline-flex;margin-left: 8px" type="danger">
            删除
          </el-button>
        </div>
        <div>
          <el-input placeholder="请输入订单号进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
                    clearable
                    @clear="initPurchaseOrders"
                    style="width: 350px;margin-right: 10px" v-model="keyword"
                    @keydown.enter.native="initPurchaseOrders" :disabled="showAdvanceSearchView"></el-input>
          <el-button icon="el-icon-search" type="primary" @click="initPurchaseOrders" :disabled="showAdvanceSearchView">
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
              <el-button size="mini" icon="el-icon-search" type="primary" @click="initPurchaseOrdersAdv">搜索</el-button>
            </el-col>
          </el-row>
        </div>
      </transition>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="purchases"
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
            label="订单号"
            align="left"
            width="80">
          <template slot-scope="scope">
            <el-button size="mini" @click="showDetailView(scope.row)">{{ scope.row.id }}</el-button>
          </template>
        </el-table-column>
        <el-table-column
            fixed
            prop="name"
            :show-overflow-tooltip="true"
            align="left"
            label="订单名称"
            width="80">
        </el-table-column>
        <el-table-column
            fixed
            prop="total"
            align="left"
            label="订单总价（元）"
            width="110">
        </el-table-column>
        <el-table-column
            prop="discount"
            align="left"
            label="订单优惠（元）"
            width="110">
        </el-table-column>
        <el-table-column
            prop="pay"
            label="实际支付（元）"
            align="left"
            width="110">
        </el-table-column>
        <el-table-column
            prop="hasInvoice"
            width="95"
            align="left"
            label="是否有发票">
          <template slot-scope="scope">
            <span v-if="scope.row.hasInvoice==true">是</span>
            <span v-else>否</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="invoiceTime"
            width="100"
            align="left"
            label="发票日期">
        </el-table-column>
        <el-table-column
            :show-overflow-tooltip="true"
            label="关联资产编号">
          <template slot-scope="scope">
            <el-tag type="success" style="margin-right: 4px" v-for="(asset,indexj) in scope.row.assetList"
                    :key="indexj">{{ asset.id }}
            </el-tag>
            <el-popover
                placement="right"
                title="资产编号列表"
                @show="showPop(scope.row)"
                @hide="hidePop(scope.row)"
                width="200"
                trigger="click">
              <el-select v-model="selectedAids" multiple filterable placeholder="请选择">
                <el-option
                    v-for="(id,indexj) in aids"
                    :key="indexj"
                    :label="id"
                    :value="id">
                </el-option>
              </el-select>
              <el-button slot="reference" icon="el-icon-more" type="text"></el-button>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column
            prop="remark"
            :show-overflow-tooltip="true"
            label="订单备注">
        </el-table-column>
        <el-table-column
            prop="createTime"
            width="100"
            align="left"
            label="创建日期">
        </el-table-column>
        <el-table-column
            prop="updateTime"
            width="100"
            align="left"
            label="更新日期">
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
    <PurchaseOrderEdit v-on:close="dialogVisible3 = false" :dialogVisible3="dialogVisible3" :purchase="purchase"
                       :title="title"></PurchaseOrderEdit>
    <PurchaseOrderDetail v-on:close="dialogVisible2 = false" :dialogVisible2="dialogVisible2" :purchase="purchase"
                         :title="title" :urlList="urlList"></PurchaseOrderDetail>
  </div>
</template>

<script>
import PurchaseOrderEdit from "@/components/purchaseOrder/PurchaseOrderEdit";
import PurchaseOrderDetail from "@/components/purchaseOrder/PurchaseOrderDetail";

export default {
  name: "PurchaseOrder",
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
      purchases: [],
      loading: false,
      // 编辑页面/新增页面可见性
      dialogVisible: false,
      // 详情页可见性
      dialogVisible2: false,
      // 添加订单信息
      dialogVisible3: false,
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
          assetName: "",
          brand: "华为",
          type: "手机",
          price: "4000",
          fileList: [],
          adv: {}
        }
      },
      // 搜索类型，空是普通搜索，‘advanced’是高级搜索
      type: "",
      // 资产图片列表，用于编辑页面
      fileList: [],
      // 资产图片url列表，用于详情页面
      urlList: [],
      purchase: {
        id: "",
        name: "",
        total: "",
        discount: "",
        pay: "",
        purchaseDate: "",
        hasInvoice: false,
        invoiceDate: "",
        remark: "",
        creatorEmail: "",
        updaterEmail: "",
        creator: {},
        updater: {},
        createTime: "",
        updateTime: "",
        assetList: []
      },
      aids: [],
      selectedAids: []
    }
  },
  components: {
    PurchaseOrderEdit,
    PurchaseOrderDetail
  },
  mounted() {
    this.initPurchaseOrders();
  },
  methods: {
    exportData() {
      let url = '/order/basic/export/in/?category=1';
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
          fileList: [],
          adv: {},
        }
      };
    },
    showAddView() {
      this.emptyOrder();
      this.fileList = [];
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
      this.title = '订单详情';
      this.purchase = data;
      // if (this.order.asset && this.order.asset.fileList) {
      //   this.urlList = [];
      //   for (let i = 0; i < this.order.asset.fileList.length; i++) {
      //     this.urlList.push(this.order.asset.fileList[i].url);
      //   }
      // } else {
      //   this.urlList = null;
      // }
      this.dialogVisible2 = true;
    },
    deleteOrder() {
      this.$confirm('此操作将永久删除选中的记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequestWithData("/order/basic/delete", this.orderIds).then(resp => {
          if (resp) {
            this.initPurchaseOrders();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    showPurchaseOrderView() {
      // this.postRequest("/order/basic/delete", this.orderIds).then(resp => {
      //   if (resp) {
      //     this.initPurchaseOrders();
      //   }
      // });
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      if (this.type && this.type == 'advanced') {
        this.initPurchaseOrdersAdv();
      } else {
        this.initPurchaseOrders();
      }
    },
    currentChange(currentPage) {
      this.page = currentPage;
      if (this.type && this.type == 'advanced') {
        this.initPurchaseOrdersAdv();
      } else {
        this.initPurchaseOrders();
      }
    },
    initPurchaseOrders() {
      this.type = '';
      this.loading = true;
      let url = '/purchase/get/?page=' + this.page + '&size=' + this.size + "&poid=" + this.keyword;
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.purchases = resp.data;
          this.total = resp.total;
        }
      });
    },
    initPurchaseOrdersAdv() {
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
    },
    hidePop(data) {
      let url = '/purchase/asset?poid=' + data.id;
      this.putRequest(url, this.selectedAids).then(resp => {
        if (resp) {
          // this.initPurchaseOrdersAdv();
          this.initPurchaseOrders();
        }
      });
    },
    showPop(data) {
      this.initAllAssetIds();
      let assets = data.assetList;
      this.selectedAids = [];
      assets.forEach(a => {
        this.selectedAids.push(a.id);
      })
    },
    initAllAssetIds() {
      this.getRequest("/asset/all").then(resp => {
        if (resp) {
          this.aids = resp.obj;
        }
      })
    }
  }
}
</script>

<style>
</style>
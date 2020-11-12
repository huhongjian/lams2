<template>
  <div>
    <div>
      <div style="display: flex;justify-content: space-between">
        <div>
          <el-button style="display: inline-flex;margin-left: 8px" type="primary" icon="el-icon-plus"
                     @click="showAddView">
            发起离退流程
          </el-button>
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
            <el-col :span="7">
              状态:
              <el-select v-model="searchValue.status"
                         clearable
                         placeholder="状态"
                         size="mini"
                         style="width: 130px;">
                <el-option
                    v-for="item in statuses"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="9">
              理由:
              <el-input size="mini" style="width: 350px" prefix-icon="el-icon-edit"
                        clearable
                        v-model="searchValue.reason"></el-input>
            </el-col>
          </el-row>
          <el-row style="margin-top: 10px">
            <el-col :span="7">
              申请人邮箱:
              <el-input size="mini" style="width: 200px" clearable prefix-icon="el-icon-edit"
                        v-model="searchValue.userEmail"></el-input>
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
          :size="size" :loading="loading" :isOut="isOut"></Mine>
    <el-dialog
        :title="title"
        :visible.sync="dialogVisible2"
        width="80%">
      <div>
        <el-form :model="order">
          <el-row>
            <el-col :span="6">
              <el-form-item label="申请单号:" prop="id">
                {{ order.id }}
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="流程:" prop="categoryName">
                {{ order.categoryName }}
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="状态:" prop="statusName">
                {{ order.statusName }}
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="电话:" prop="user.phone">
                {{ order.user.phone }}
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="邮件:" prop="user.username">
                {{ order.user.username }}
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="10">
              <el-form-item v-show="order.reason&&order.reason!=''" label="理由:" prop="reason">
                {{ order.reason }}
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="10">
              <el-form-item v-show="order.createTime" label="创建时间:" prop="createTime">
                {{ order.createTime }}
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
    <template v-for="op in operateList">
      <el-button type="primary" @click="checkAndHandle(op.operateType)">{{ op.operate }}</el-button>
    </template>
        <el-button @click="dialogVisible2=false">取 消</el-button>
  </span>
    </el-dialog>
    <el-dialog
        :title="title"
        :visible.sync="dialogVisible"
        width="80%">
      <div>
        <el-form :model="order" ref="orderForm">
          <el-row>
            <el-col :span="20">
              <el-form-item label="理由:" prop="reason">
                <el-input size="mini"
                          type="textarea"
                          :rows="2"
                          placeholder="请输入申请理由"
                          v-model="order.reason"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible=false">取 消</el-button>
    <el-button type="primary" @click="doStudentOut">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
import Mine from "@/components/order/Mine";

export default {
  name: "MyOut",
  data() {
    return {
      isOut: true,
      loading: false,
      searchValue: {
        status: null,
        reason: null,
        userEmail: null,
        dateScope: null
      },
      title: '',
      orders: [],
      showAdvanceSearchView: false,
      dialogVisible: false,
      // 详情页可见性
      dialogVisible2: false,
      total: 0,
      page: 1,
      keyword: '',
      size: 10,
      statuses: [
        {
          id: 9,
          name: "申请离退"
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
      candidateBranches: {},
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
      taskHandleDto: {
        id: null,
        operateType: null,
        candidateUser: null
      }
    }
  },
  components: {
    Mine
  },
  mounted() {
    this.initOrders();
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
          enabled: "",
          remark: ""
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
    showDetailView(data) {
      this.title = '资产详情';
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
    sizeChange(currentSize) {
      this.size = currentSize;
      this.initOrders('advanced');
    },
    currentChange(currentPage) {
      this.page = currentPage;
      this.initOrders('advanced');
    },
    showAddView() {
      this.emptyOrder();
      this.title = '离退流程申请';
      this.dialogVisible = true;
    },
    checkAndHandle(data) {
      this.taskHandleDto.operateType = data;
      if (data == '5') {
        this.visible = true;
      } else if (data == '7') {
        this.cancel();
      } else {
        this.handle();
      }
    },
    handle() {
      this.taskHandleDto.id = this.order.id;
      this.postRequest("/order/task/handleTask", this.taskHandleDto).then(resp => {
        if (resp) {
          this.dialogVisible2 = false;
          this.initOrders();
        }
      });
    },
    cancel() {
      this.taskHandleDto.id = this.order.id;
      this.postRequest("/order/task/cancel", this.taskHandleDto).then(resp => {
        if (resp) {
          this.visible = false;
          this.dialogVisible2 = false;
          this.initOrders();
        }
      })
    },
    doStudentOut() {
      this.postRequest("/stuOut/add", this.order).then(resp => {
        if (resp) {
          this.dialogVisible = false;
          this.initOrders();
        }
      });
    },
    initOrders() {
      this.type = '';
      this.loading = true;
      let url = '/stuOut/get/?page=' + this.page + '&size=' + this.size + "&oid=" + this.keyword;
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
      let url = '/stuOut/get/?page=' + this.page + '&size=' + this.size;
      if (this.searchValue.status) {
        url += '&status=' + this.searchValue.status;
      }
      if (this.searchValue.reason) {
        url += '&reason=' + this.searchValue.reason;
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
        reason: null,
        userEmail: null,
        dateScope: null
      }
    }
  }
}
</script>
<style>
</style>
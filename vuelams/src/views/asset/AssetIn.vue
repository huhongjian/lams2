<template>
  <div>
    <div>
      <div style="display: flex;justify-content: space-between">
        <div>
          <el-button type="primary" icon="el-icon-plus" @click="showAddEmpView">
            新增资产
          </el-button>
          <el-upload
              :show-file-list="false"
              :before-upload="beforeUpload"
              :on-success="onSuccess"
              :on-error="onError"
              :disabled="importDataDisabled"
              style="display: inline-flex;margin-left: 8px"
              action="/employee/basic/import">
            <el-button :disabled="importDataDisabled" type="success" :icon="importDataBtnIcon">
              {{ importDataBtnText }}
            </el-button>
          </el-upload>
          <el-button type="success" style="display: inline-flex;margin-left: 8px" @click="exportData"
                     icon="el-icon-download">
            导出数据
          </el-button>
        </div>
        <div>
          <el-input placeholder="请输入资产名称进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
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
             style="border: 1px solid #409eff;border-radius: 5px;box-sizing: border-box;padding: 5px;margin: 10px 0px;">
          <el-row>
            <el-col :span="5">
              类型:
              <el-select v-model="searchValue.type" placeholder="类型" size="mini"
                         style="width: 130px;">
                <el-option
                    v-for="item in types"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              品牌:
              <el-input size="mini" style="width: 100px" prefix-icon="el-icon-edit"
                        v-model="searchValue.brand"></el-input>
            </el-col>
            <el-col :span="4">
              状态:
              <el-select v-model="searchValue.status" placeholder="状态" size="mini" style="width: 130px;">
                <el-option
                    v-for="item in statuses"
                    :key="item.id"
                    :label="item.name"
                    :value="item.name">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              申请人:
              <el-input size="mini" style="width: 100px" prefix-icon="el-icon-edit"
                        v-model="searchValue.applicant"></el-input>
            </el-col>
            <el-col :span="4">
              申请人邮箱:
              <el-input size="mini" style="width: 100px" prefix-icon="el-icon-edit"
                        v-model="searchValue.applicantEmail"></el-input>
            </el-col>
          </el-row>
          <el-row style="margin-top: 10px">
            <el-col :span="6">
              价格:
              <el-input size="mini" style="width: 100px" prefix-icon="el-icon-edit"
                        v-model="searchValue.priceLow"></el-input>
              至:
              <el-input size="mini" style="width: 100px" prefix-icon="el-icon-edit"
                        v-model="searchValue.priceHigh"></el-input>
            </el-col>
            <el-col :span="9">
              入职日期:
              <el-date-picker
                  v-model="searchValue.beginDateScope"
                  type="daterange"
                  size="mini"
                  unlink-panels
                  value-format="yyyy-MM-dd"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
            <el-col :span="5" :offset="3">
              <el-button size="mini">取消</el-button>
              <el-button size="mini" icon="el-icon-search" type="primary" @click="initOrders('advanced')">搜索</el-button>
            </el-col>
          </el-row>
        </div>
      </transition>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="orders"
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
            label="ID"
            align="left"
            width="90">
          <template slot-scope="scope">
            <el-button size="mini" @click="getCandidateBranchInfo(scope.row)">{{ scope.row.id }}</el-button>
          </template>
        </el-table-column>
        <el-table-column
            prop="asset.id"
            align="left"
            label="资产编号"
            width="90">
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
            width="80">
        </el-table-column>
        <el-table-column
            prop="status"
            width="90"
            label="状态">
          <template slot-scope="scope">
            <span style="color: #00e079; font-weight: bold"
                  v-if="scope.row.status=='审批通过'||scope.row.status=='已入库'">{{ scope.row.status }}</span>
            <span style="color: #ff4777; font-weight: bold"
                  v-else-if="scope.row.status=='审批未通过'">{{ scope.row.status }}</span>
            <span style="color: #c0c0c0;"
                  v-else-if="scope.row.status=='已借出'||scope.row.status=='已关闭'">{{ scope.row.status }}</span>
            <span v-else>{{ scope.row.status }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="asset.price"
            width="80"
            label="价格">
        </el-table-column>
        <el-table-column
            prop="applicant"
            width="95"
            align="left"
            label="申请人">
        </el-table-column>
        <el-table-column
            prop="applicantEmail"
            width="150"
            align="left"
            label="申请人邮箱">
        </el-table-column>
        <el-table-column
            prop="applicantPhone"
            width="100"
            label="申请人电话">
        </el-table-column>
        <el-table-column
            prop="reason"
            label="申请理由">
        </el-table-column>
        <el-table-column
            prop="createTime"
            width="180"
            align="left"
            label="申请时间">
        </el-table-column>
        <el-table-column
            fixed="right"
            width="100"
            label="操作">
          <template slot-scope="scope">
            <el-button @click="showEditEmpView(scope.row)" style="padding: 3px" size="mini">编辑</el-button>
            <el-button @click="deleteEmp(scope.row)" style="padding: 3px" size="mini" type="danger">删除
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
    <AssetEdit v-on:close="dialogVisible = false" :dialogVisible="dialogVisible" :order="order" :title="title"
               :rules='rules'></AssetEdit>
    <AssetDetail v-on:close="dialogVisible2 = false" :dialogVisible2="dialogVisible2" :order="order" :title="title"
                 :candidateBranches='candidateBranches' :rules='rules'></AssetDetail>
  </div>
</template>

<script>
import AssetDetail from "@/components/asset/AssetDetail";
import AssetEdit from "@/components/asset/AssetEdit";

export default {
  name: "EmpBasic",
  data() {
    return {
      searchValue: {
        type: null,
        nationId: null,
        jobLevelId: null,
        posId: null,
        engageForm: null,
        departmentId: null,
        beginDateScope: null
      },
      title: '',
      importDataBtnText: '导入数据',
      importDataBtnIcon: 'el-icon-upload2',
      importDataDisabled: false,
      showAdvanceSearchView: false,
      allDeps: [],
      orders: [],
      loading: false,
      popVisible: false,
      popVisible2: false,
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
          id: 3,
          name: "审批未通过"
        }
      ],
      candidateBranches: {},
      inputDepName: '所属部门',
      order: {
        id: "",
        status: "",
        reason: "测试",
        applicant: "胡宏建",
        applicantPhone: "18840833079",
        applicantEmail: "admin",
        createTime: "",
        asset: {
          id: "",
          brand: "华为",
          type: "手机",
          price: "4000",
          adv: {},
        },
      },
      rules: {
        name: [{required: true, message: '请输入用户名', trigger: 'blur'}],
        email: [{required: true, message: '请输入邮箱地址', trigger: 'blur'}, {
          type: 'email',
          message: '邮箱格式不正确',
          trigger: 'blur'
        }],
        phone: [{required: true, message: '请输入电话号码', trigger: 'blur'}]
      }
    }
  },
  components: {
    AssetDetail,
    AssetEdit
  },
  mounted() {
    this.initOrders();
  },
  methods: {
    searvhViewHandleNodeClick(data) {
      this.inputDepName = data.name;
      this.searchValue.departmentId = data.id;
      this.popVisible2 = !this.popVisible2
    },
    onError(err, file, fileList) {
      this.importDataBtnText = '导入数据';
      this.importDataBtnIcon = 'el-icon-upload2';
      this.importDataDisabled = false;
    },
    onSuccess(response, file, fileList) {
      this.importDataBtnText = '导入数据';
      this.importDataBtnIcon = 'el-icon-upload2';
      this.importDataDisabled = false;
      this.initOrders();
    },
    beforeUpload() {
      this.importDataBtnText = '正在导入';
      this.importDataBtnIcon = 'el-icon-loading';
      this.importDataDisabled = true;
    },
    exportData() {
      window.open('/employee/basic/export', '_parent');
    },
    emptyAsset() {
      this.asset = {
        id: "",
        brand: "华为",
        type: "手机",
        price: "4000",
        applicant: "胡宏建",
        applicantPhone: "18840833079",
        applicantEmail: "admin",
        reason: "测试",
        adv: {}
      }
      this.inputDepName = '';
    },
    showEditEmpView(data) {
      this.title = '编辑资产信息';
      this.order = data;
      this.dialogVisible = true;
    },
    showDetailView(data) {
      this.title = '资产详情';
      this.order = data;
      this.dialogVisible2 = true;
    },
    getCandidateBranchInfo(data) {
      this.getRequest('/order/task/getCandidateTaskBranchInfo?id=' + data.id).then(resp => {
        if (resp) {
          this.candidateBranches = resp.obj;
          this.showDetailView(data);
        }
      });
    },
    deleteEmp(data) {
      this.$confirm('此操作将永久删除【' + data.name + '】, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest("/employee/basic/" + data.id).then(resp => {
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
    handleNodeClick(data) {
      this.inputDepName = data.name;
      this.emp.departmentId = data.id;
      this.popVisible = !this.popVisible
    },
    showDepView() {
      this.popVisible = !this.popVisible
    },
    showDepView2() {
      this.popVisible2 = !this.popVisible2
    },
    initPositions() {
      this.getRequest('/employee/basic/positions').then(resp => {
        if (resp) {
          this.positions = resp;
        }
      })
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      this.initOrders();
    },
    currentChange(currentPage) {
      this.page = currentPage;
      this.initOrders('advanced');
    },
    showAddEmpView() {
      this.emptyAsset();
      this.title = '资产采购申请';
      this.dialogVisible = true;
    },
    initOrders(type) {
      this.loading = true;
      let url = '/order/basic/get/?category=1&page=' + this.page + '&size=' + this.size;
      if (type && type == 'advanced') {
        if (this.searchValue.politicId) {
          url += '&politicId=' + this.searchValue.politicId;
        }
        if (this.searchValue.nationId) {
          url += '&nationId=' + this.searchValue.nationId;
        }
        if (this.searchValue.jobLevelId) {
          url += '&jobLevelId=' + this.searchValue.jobLevelId;
        }
        if (this.searchValue.posId) {
          url += '&posId=' + this.searchValue.posId;
        }
        if (this.searchValue.engageForm) {
          url += '&engageForm=' + this.searchValue.engageForm;
        }
        if (this.searchValue.departmentId) {
          url += '&departmentId=' + this.searchValue.departmentId;
        }
        if (this.searchValue.beginDateScope) {
          url += '&beginDateScope=' + this.searchValue.beginDateScope;
        }
      } else {
        url += "&name=" + this.keyword;
      }
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.orders = resp.data;
          this.total = resp.total;
        }
      });
    }
  }
}
</script>

<style>
/* 可以设置不同的进入和离开动画 */
/* 设置持续时间和动画函数 */
.slide-fade-enter-active {
  transition: all .8s ease;
}

.slide-fade-leave-active {
  transition: all .8s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}

.slide-fade-enter, .slide-fade-leave-to
  /* .slide-fade-leave-active for below version 2.1.8 */
{
  transform: translateX(10px);
  opacity: 0;
}
</style>
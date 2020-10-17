<template>
  <div>
    <Mine v-on:currentSize="sizeChange" v-on:currentPage="currentChange" :orders="orders"
          :showAdvanceSearchView="showAdvanceSearchView" :total="total" :page="page" :size="size"
          :loading="loading"></Mine>
    <AssetEdit v-on:close="dialogVisible = false" :dialogVisible="dialogVisible" :order="order" :title="title"
               :rules='rules'></AssetEdit>
    <AssetDetail v-on:close="dialogVisible2 = false" :dialogVisible2="dialogVisible2" :order="order" :title="title"
                 :candidateBranches='candidateBranches' :rules='rules'></AssetDetail>
  </div>
</template>

<script>
import AssetDetail from "@/components/order/OrderDetail";
import AssetEdit from "@/components/order/OrderEdit";
import Mine from "@/components/order/Mine";

export default {
  name: "MyOut",
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
      nations: [],
      joblevels: [],
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
          id: 0,
          name: "申请采购"
        },
        {
          id: 0,
          name: "审批通过"
        },
        {
          id: 0,
          name: "审批未通过"
        }
      ],
      candidateBranches: {},
      tiptopDegrees: ['本科', '大专', '硕士', '博士', '高中', '初中', '小学', '其他'],
      inputDepName: '所属部门',
      order: {
        id: "",
        category: "",
        categoryName: "",
        status: "",
        duration: "",
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
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      rules: {
        name: [{required: true, message: '请输入用户名', trigger: 'blur'}],
        gender: [{required: true, message: '请输入性别', trigger: 'blur'}],
        birthday: [{required: true, message: '请输入出生日期', trigger: 'blur'}],
        idCard: [{required: true, message: '请输入身份证号码', trigger: 'blur'}, {
          pattern: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/,
          message: '身份证号码格式不正确',
          trigger: 'blur'
        }],
        wedlock: [{required: true, message: '请输入婚姻状况', trigger: 'blur'}],
        nationId: [{required: true, message: '请输入您组', trigger: 'blur'}],
        nativePlace: [{required: true, message: '请输入籍贯', trigger: 'blur'}],
        politicId: [{required: true, message: '请输入政治面貌', trigger: 'blur'}],
        email: [{required: true, message: '请输入邮箱地址', trigger: 'blur'}, {
          type: 'email',
          message: '邮箱格式不正确',
          trigger: 'blur'
        }],
        phone: [{required: true, message: '请输入电话号码', trigger: 'blur'}],
        address: [{required: true, message: '请输入员工地址', trigger: 'blur'}],
        departmentId: [{required: true, message: '请输入部门名称', trigger: 'blur'}],
        jobLevelId: [{required: true, message: '请输入职称', trigger: 'blur'}],
        posId: [{required: true, message: '请输入职位', trigger: 'blur'}],
        engageForm: [{required: true, message: '请输入聘用形式', trigger: 'blur'}],
        tiptopDegree: [{required: true, message: '请输入学历', trigger: 'blur'}],
        specialty: [{required: true, message: '请输入专业', trigger: 'blur'}],
        school: [{required: true, message: '请输入毕业院校', trigger: 'blur'}],
        beginDate: [{required: true, message: '请输入入职日期', trigger: 'blur'}],
        workState: [{required: true, message: '请输入工作状态', trigger: 'blur'}],
        workID: [{required: true, message: '请输入工号', trigger: 'blur'}],
        contractTerm: [{required: true, message: '请输入合同期限', trigger: 'blur'}],
        conversionTime: [{required: true, message: '请输入转正日期', trigger: 'blur'}],
        notworkDate: [{required: true, message: '请输入离职日期', trigger: 'blur'}],
        beginContract: [{required: true, message: '请输入合同起始日期', trigger: 'blur'}],
        endContract: [{required: true, message: '请输入合同结束日期', trigger: 'blur'}],
        workAge: [{required: true, message: '请输入工龄', trigger: 'blur'}],
      }
    }
  },
  components: {
    AssetDetail,
    AssetEdit,
    Mine
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
      this.asset = data;
      this.dialogVisible = true;
    },
    showDetailView(data) {
      this.title = '资产详情';
      this.asset = data;
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
      let url = '/order/basic/get/?category=3&page=' + this.page + '&size=' + this.size;
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
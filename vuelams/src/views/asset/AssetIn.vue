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
                    @clear="initEmps"
                    style="width: 350px;margin-right: 10px" v-model="keyword"
                    @keydown.enter.native="initEmps" :disabled="showAdvanceSearchView"></el-input>
          <el-button icon="el-icon-search" type="primary" @click="initEmps" :disabled="showAdvanceSearchView">
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
              政治面貌:
              <el-select v-model="searchValue.politicId" placeholder="政治面貌" size="mini"
                         style="width: 130px;">
                <el-option
                    v-for="item in politicsstatus"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              民族:
              <el-select v-model="searchValue.nationId" placeholder="民族" size="mini"
                         style="width: 130px;">
                <el-option
                    v-for="item in nations"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              职位:
              <el-select v-model="searchValue.posId" placeholder="职位" size="mini" style="width: 130px;">
                <el-option
                    v-for="item in positions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              职称:
              <el-select v-model="searchValue.jobLevelId" placeholder="职称" size="mini"
                         style="width: 130px;">
                <el-option
                    v-for="item in joblevels"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="7">
              聘用形式:
              <el-radio-group v-model="searchValue.engageForm">
                <el-radio label="劳动合同">劳动合同</el-radio>
                <el-radio label="劳务合同">劳务合同</el-radio>
              </el-radio-group>
            </el-col>
          </el-row>
          <el-row style="margin-top: 10px">
            <el-col :span="5">
              所属部门:
              <el-popover
                  placement="right"
                  title="请选择部门"
                  width="200"
                  trigger="manual"
                  v-model="popVisible2">
                <el-tree default-expand-all :data="allDeps" :props="defaultProps"
                         @node-click="searvhViewHandleNodeClick"></el-tree>
                <div slot="reference"
                     style="width: 130px;display: inline-flex;font-size: 13px;border: 1px solid #dedede;height: 26px;border-radius: 5px;cursor: pointer;align-items: center;padding-left: 8px;box-sizing: border-box;margin-left: 3px"
                     @click="showDepView2">{{ inputDepName }}
                </div>
              </el-popover>
            </el-col>
            <el-col :span="10">
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
            <el-col :span="5" :offset="4">
              <el-button size="mini">取消</el-button>
              <el-button size="mini" icon="el-icon-search" type="primary" @click="initEmps('advanced')">搜索</el-button>
            </el-col>
          </el-row>
        </div>
      </transition>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="assets"
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
            prop="type"
            align="left"
            label="类型"
            width="90">
        </el-table-column>
        <el-table-column
            prop="brand"
            label="品牌"
            align="left"
            width="80">
        </el-table-column>
        <el-table-column
            prop="status"
            width="70"
            label="状态">
        </el-table-column>
        <el-table-column
            prop="price"
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
            width="95"
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
            prop="applyDate"
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
    <el-dialog
        :title="title"
        :visible.sync="dialogVisible"
        width="80%">
      <div>
        <el-form :model="asset" :rules="rules" ref="assetForm">
          <el-row>
            <el-col :span="6">
              <el-form-item label="品牌:" prop="brand">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit" v-model="asset.brand"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="类型:" prop="type">
                <el-select v-model="asset.type" placeholder="请选择">
                  <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="价格:" prop="price">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit" v-model="asset.price"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="申请人:" prop="applicant">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="asset.applicant"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="电话:" prop="applicantPhone">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="asset.applicantPhone"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="邮件:" prop="applicantEmail">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="asset.applicantEmail"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="20">
              <el-form-item label="理由:" prop="reason">
                <el-input size="mini" style="width: 300px" prefix-icon="el-icon-edit" v-model="asset.reason"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <template v-if="asset.adv">
          <el-form v-show="asset.type=='手机'" :model="asset.adv" :rules="rules" ref="assetForm">
            <el-row>
              <el-col :span="6">
                <el-form-item label="内存:" prop="brand">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="asset.adv.memory"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="屏幕尺寸:" prop="brand">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="asset.adv.screenSize"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <el-form v-show="asset.type=='交换机'" :model="asset.adv" :rules="rules" ref="assetForm">
            <el-row>
              <el-col :span="6">
                <el-form-item label="接口数:" prop="brand">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="asset.adv.nums"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="类型:" prop="type">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="asset.adv.type"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <el-form v-show="asset.type=='主机'" :model="asset.adv" :rules="rules" ref="assetForm">
            <el-row>
              <el-col :span="6">
                <el-form-item label="cpu:" prop="cpu">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="asset.adv.cpu"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="内存:" prop="memory">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="asset.adv.memory"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <el-form v-show="asset.type=='测距仪'" :model="asset.adv" :rules="rules" ref="assetForm">
            <el-row>
              <el-col :span="6">
                <el-form-item label="精度:" prop="brand">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="asset.adv.precision"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="距离:" prop="brand">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="asset.adv.distance"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="方式:" prop="brand">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="asset.adv.methods"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </template>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="doAddAsset">确 定</el-button>
  </span>
    </el-dialog>
    <AssetDetail v-on:close="dialogVisible2 = false" :dialogVisible2="dialogVisible2" :asset="asset" :title="title"
                 :candidateBranches='candidateBranches' :rules='rules'></AssetDetail>
  </div>
</template>

<script>
import AssetDetail from '../../components/sys/basic/AssetDetail';

export default {
  name: "EmpBasic",
  data() {
    return {
      searchValue: {
        politicId: null,
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
      assets: [],
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
      politicsstatus: [],
      positions: [],
      candidateBranches: {},
      taskHandleDto: {
        id: null,
        operateType: null,
        candidateUser: null
      },
      tiptopDegrees: ['本科', '大专', '硕士', '博士', '高中', '初中', '小学', '其他'],
      options: [{
        value: '手机',
        label: '手机'
      }, {
        value: '主机',
        label: '主机'
      }, {
        value: '交换机',
        label: '交换机'
      }, {
        value: '测距仪',
        label: '测距仪'
      }],
      inputDepName: '所属部门',
      asset: {
        id: "",
        brand: "华为",
        type: "手机",
        price: "4000",
        applicant: "系统管理员",
        applicantPhone: "188",
        applicantEmail: "29411",
        reason: "测试",
        adv: {}
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
    AssetDetail
  },
  mounted() {
    this.initEmps();
    this.initData();
    this.initPositions();
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
      this.initEmps();
    },
    beforeUpload() {
      this.importDataBtnText = '正在导入';
      this.importDataBtnIcon = 'el-icon-loading';
      this.importDataDisabled = true;
    },
    exportData() {
      window.open('/employee/basic/export', '_parent');
    },
    emptyEmp() {
      this.asset = {
        id: "",
        brand: "华为",
        type: "手机",
        price: "4000",
        applicant: "系统管理员",
        applicantPhone: "188",
        applicantEmail: "29411",
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
      this.getRequest('/asset/task/getCandidateTaskBranchInfo?id=' + data.id).then(resp => {
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
            this.initEmps();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    doAddAsset() {
      if (this.asset.id) {
        this.$refs['assetForm'].validate(valid => {
          if (valid) {
            this.putRequest("/asset/basic/edit", this.asset).then(resp => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmps();
              }
            })
          }
        });
      } else {
        this.$refs['assetForm'].validate(valid => {
          if (valid) {
            this.postRequest("/asset/basic/add", this.asset).then(resp => {
              if (resp) {
                this.dialogVisible = false;
                this.initEmps();
              }
            })
          }
        });
      }
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
    getMaxWordID() {
      this.getRequest("/employee/basic/maxWorkID").then(resp => {
        if (resp) {
          this.emp.workID = resp.obj;
        }
      })
    },
    initData() {
      if (!window.sessionStorage.getItem("nations")) {
        this.getRequest('/employee/basic/nations').then(resp => {
          if (resp) {
            this.nations = resp;
            window.sessionStorage.setItem("nations", JSON.stringify(resp));
          }
        })
      } else {
        this.nations = JSON.parse(window.sessionStorage.getItem("nations"));
      }
      if (!window.sessionStorage.getItem("joblevels")) {
        this.getRequest('/employee/basic/joblevels').then(resp => {
          if (resp) {
            this.joblevels = resp;
            window.sessionStorage.setItem("joblevels", JSON.stringify(resp));
          }
        })
      } else {
        this.joblevels = JSON.parse(window.sessionStorage.getItem("joblevels"));
      }
      if (!window.sessionStorage.getItem("politicsstatus")) {
        this.getRequest('/employee/basic/politicsstatus').then(resp => {
          if (resp) {
            this.politicsstatus = resp;
            window.sessionStorage.setItem("politicsstatus", JSON.stringify(resp));
          }
        })
      } else {
        this.politicsstatus = JSON.parse(window.sessionStorage.getItem("politicsstatus"));
      }
      if (!window.sessionStorage.getItem("deps")) {
        this.getRequest('/employee/basic/deps').then(resp => {
          if (resp) {
            this.allDeps = resp;
            window.sessionStorage.setItem("deps", JSON.stringify(resp));
          }
        })
      } else {
        this.allDeps = JSON.parse(window.sessionStorage.getItem("deps"));
      }
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      this.initEmps();
    },
    currentChange(currentPage) {
      this.page = currentPage;
      this.initEmps('advanced');
    },
    showAddEmpView() {
      this.emptyEmp();
      this.title = '资产采购申请';
      this.getMaxWordID();
      this.dialogVisible = true;
    },
    initEmps(type) {
      this.loading = true;
      let url = '/asset/basic/get/?category=1&page=' + this.page + '&size=' + this.size;
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
          this.assets = resp.data;
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
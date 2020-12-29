<template>
  <div>
    <el-dialog
        :title="title"
        :visible.sync="dialogVisible7"
        :before-close="handleClose"
        width="80%">
      <div>
        <el-form :model="order">
          <template v-if="order.user">
            <el-row>
              <el-col :span="6">
                <el-form-item label="申请人:" prop="applicant">
                  {{ order.user.name }}
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="申请人电话:" prop="applicantPhone">
                  {{ order.user.phone }}
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="申请人邮箱:" prop="applicantEmail">
                  {{ order.user.username }}
                </el-form-item>
              </el-col>
            </el-row>
          </template>
          <el-row>
            <el-col :span="10">
              <el-form-item v-show="order.expireTime" label="预计归还时间:" prop="expireTime">
                {{ order.expireTime }}
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
          <el-table
              :data="order.assetList"
              stripe
              border
              element-loading-text="正在加载..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)"
              style="width: 100%">
            <el-table-column
                fixed
                label="资产编号"
                align="left"
                width="100">
              <template slot-scope="scope">
                <el-button size="mini" @click="showDetailView(scope.row)">{{ scope.row.id }}</el-button>
              </template>
            </el-table-column>
            <el-table-column
                fixed
                prop="assetName"
                :show-overflow-tooltip="true"
                align="left"
                label="资产名称">
            </el-table-column>
            <el-table-column
                prop="statusName"
                label="状态"
                width="200">
              <template slot-scope="scope">
            <span style="color: #00e079; font-weight: bold"
                  v-if="scope.row.status=='2'">{{ scope.row.statusName }}</span>
                <span style="color: #ff4777; font-weight: bold" v-else-if="scope.row.status=='3'">{{
                    scope.row.statusName
                  }}</span>
                <span style="color: #c0c0c0;"
                      v-else-if="scope.row.status=='4'">{{ scope.row.statusName }}</span>
                <span v-else>{{ scope.row.statusName }}</span>
              </template>
            </el-table-column>
            <el-table-column
                prop="type"
                align="left"
                label="类型"
                width="200">
            </el-table-column>
            <el-table-column
                prop="brand"
                label="品牌"
                align="left"
                width="200">
            </el-table-column>
            <el-table-column
                prop="price"
                label="价格（元）"
                align="left"
                width="200">
            </el-table-column>
          </el-table>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
    <template v-for="op in operateList">
      <el-button type="primary" @click="checkAndHandle(op.operateType)">{{ op.operate }}</el-button>
    </template>
        <el-button @click="$emit('close')">取 消</el-button>
  </span>
    </el-dialog>
    <AssetDetail v-on:close="dialogVisible2 = false" :dialogVisible2="dialogVisible2" :asset="asset"
                 :urlList="urlList" :title="title"></AssetDetail>
    <el-dialog
        :visible.sync="visible"
        width="30%">
      <el-form :model="taskHandleDto" :rules="rules" ref="transForm">
        <el-row style="margin:0 auto;width: 300px">
          <el-form-item label="转交至:">
          </el-form-item>
        </el-row>
        <el-row style="margin:0 auto;width: 300px">
          <el-form-item label="姓名:" prop="name">
            <el-input size="mini" style="width: 200px" prefix-icon="el-icon-edit"
                      v-model="name"></el-input>
          </el-form-item>
        </el-row>
        <el-row style="margin:0 auto;width: 300px">
          <el-form-item label="邮箱:" prop="candidateUser">
            <el-input size="mini" style="width: 200px" prefix-icon="el-icon-edit"
                      v-model="taskHandleDto.candidateUser"></el-input>
          </el-form-item>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="transfer">转交</el-button>
        <el-button @click="visible=false">取 消</el-button>
  </span>
    </el-dialog>
    <el-dialog
        :visible.sync="visible2"
        width="30%">
      <el-form :model="order" :rules="borrowFormRules" ref="borrowForm">
        <el-row style="margin:0 auto;width: 300px">
          <el-form-item label="预计归还时间:" prop="expireTime">
            <el-date-picker
                v-model="order.expireTime"
                type="date"
                :picker-options="pickerOptions"
                placeholder="选择日期"
                format="yyyy 年 MM 月 dd 日"
                value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
        </el-row>
        <el-row style="margin:0 auto;width: 300px">
          <el-form-item label="申请理由:" prop="reason">
            <el-input size="mini"
                      type="textarea"
                      :rows="2"
                      v-model="order.reason"></el-input>
          </el-form-item>
        </el-row>
      </el-form>
      <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="borrow">确 认</el-button>
        <el-button @click="clearBorrowForm">取 消</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
import AssetDetail from "@/components/asset/AssetDetail";

export default {
  name: "OrderDetail",
  props: ['order', 'title', 'dialogVisible7', 'urlList', 'operateList', 'out'],
  data() {
    return {
      dialogVisible2: false,
      asset: {
        id: "",
        brand: "",
        type: "",
        price: "",
        fileList: [],
        adv: {},
        remark: ""
      },
      visible: false,
      visible2: false,
      name: '',
      taskHandleDto: {
        id: null,
        operateType: null,
        candidateUser: null
      },
      rules: {
        candidateUser: [{required: true, message: '请输入转交人邮箱', trigger: 'blur'}]
      },
      borrowFormRules: {
        expireTime: [{required: true, message: '请输入预计转交时间', trigger: 'blur'}],
        reason: [{required: true, message: '请输入申请理由', trigger: 'blur'}]
      },
      // 禁用一些时间
      pickerOptions: {
        disabledDate: (time) => {
          return this.dealDisabledDate(time);
        }
      }
    }
  },
  components: {
    AssetDetail
  },
  methods: {
    transfer() {
      this.$refs['transForm'].validate(valid => {
        if (valid) {
          this.handle();
        }
      });
    },
    handle() {
      this.taskHandleDto.id = this.order.id;
      this.postRequest("/order/task/handleTask", this.taskHandleDto).then(resp => {
        if (resp) {
          this.visible = false;
          this.$emit('close');
          this.$parent.initOrders();
        }
      });
    },
    cancel() {
      this.taskHandleDto.id = this.order.id;
      this.postRequest("/order/task/cancel", this.taskHandleDto).then(resp => {
        if (resp) {
          this.visible = false;
          this.$emit('close');
          this.$parent.initOrders();
        }
      })
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
    borrow() {
      this.$refs['borrowForm'].validate(valid => {
        if (valid) {
          this.postRequest("/order/basic/borrow", this.order).then(resp => {
            if (resp) {
              this.visible2 = false;
              this.$emit('close');
              this.$parent.initOrders();
            }
          });
        }
      });
    },
    handleClose() {
      this.$emit('close');
    },
    clearBorrowForm() {
      this.order.expireTime = '';
      this.order.reason = '';
      this.visible2 = false;
    },
    showDetailView(data) {
      this.title = '申请单详情';
      this.asset = data;
      if (this.asset && this.asset.fileList) {
        this.urlList = [];
        for (let i = 0; i < this.asset.fileList.length; i++) {
          this.urlList.push(this.asset.fileList[i].url);
        }
      } else {
        this.urlList = null;
      }
      this.dialogVisible2 = true;
    }
  }
}
</script>

<style scoped>

</style>
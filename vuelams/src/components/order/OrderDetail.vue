<template>
  <div>
    <el-dialog
        :title="title"
        :visible.sync="dialogVisible2"
        :before-close="handleClose"
        width="80%">
      <div>
        <el-form :model="order.asset">
          <el-row>
            <el-col :span="20">
              <el-form-item label="资产编号:" prop="id">
                {{ order.asset.id }}
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="20">
              <el-form-item label="名称:" prop="assetName">
                {{ order.asset.assetName }}
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="品牌:" prop="brand">
                {{ order.asset.brand }}
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="类型:" prop="type">
                {{ order.asset.type }}
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="价格（元）:" prop="price">
                {{ order.asset.price }}
              </el-form-item>
            </el-col>
          </el-row>
          <template v-if="order.user">
            <el-row>
              <el-col :span="6">
                <el-form-item label="用户:" prop="applicant">
                  {{ order.user.name }}
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="电话:" prop="applicantPhone">
                  {{ order.user.phone }}
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="邮件:" prop="applicantEmail">
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
          <el-row>
            <el-col :span="10">
              <el-form-item v-show="order.asset&&order.asset.remark!=''" label="资产备注:" prop="remark">
                {{ order.asset.remark }}
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <template v-if="order.asset.adv">
          <el-form v-show="order.asset.type=='手机'" :model="order.asset.adv">
            <el-row>
              <el-col :span="6">
                <el-form-item label="内存（G）:" prop="memory">
                  {{ order.asset.adv.memory }}
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="屏幕尺寸（寸）:" prop="screenSize">
                  {{ order.asset.adv.screenSize }}
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <el-form v-show="order.asset.type=='交换机'" :model="order.asset.adv">
            <el-row>
              <el-col :span="6">
                <el-form-item label="接口数:" prop="nums">
                  {{ order.asset.adv.nums }}
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="类型:" prop="type">
                  {{ order.asset.adv.type }}
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <el-form v-show="order.asset.type=='主机'" :model="order.asset.adv">
            <el-row>
              <el-col :span="6">
                <el-form-item label="cpu:" prop="cpu">
                  {{ order.asset.adv.cpu }}
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="内存（G）:" prop="memory">
                  {{ order.asset.adv.memory }}
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <el-form v-show="order.asset.type=='测距仪'" :model="order.asset.adv">
            <el-row>
              <el-col :span="6">
                <el-form-item label="精度:" prop="precision">
                  {{ order.asset.adv.precision }}
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="距离:" prop="distance">
                  {{ order.asset.adv.distance }}
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="方式:" prop="methods">
                  {{ order.asset.adv.methods }}
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <el-form>
            <el-row v-show="urlList&&urlList.length>0">
              <el-form-item label="资产相关图片:"></el-form-item>
            </el-row>
            <el-row>
              <template v-for="url in urlList">
                <el-col :span="3">
                  <el-form-item>
                    <el-image
                        style="width: 130px; height: 130px"
                        :src="url"
                        :preview-src-list="urlList">
                    </el-image>
                  </el-form-item>
                </el-col>
              </template>
            </el-row>
          </el-form>
        </template>
      </div>
      <span slot="footer" class="dialog-footer">
      <el-button v-if="out&&out==true&&(order.status=='3'||order.status=='8'||order.status=='7')"
                 type="primary"
                 @click="visible2=true">借 用</el-button>
    <template v-for="op in operateList">
      <el-button type="primary" @click="checkAndHandle(op.operateType)">{{ op.operate }}</el-button>
    </template>
        <el-button @click="$emit('close')">取 消</el-button>
  </span>
    </el-dialog>
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
export default {
  name: "OrderDetail",
  props: ['order', 'title', 'dialogVisible2', 'urlList', 'operateList', 'out'],
  data() {
    return {
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
    dealDisabledDate(time) {
      var times = Date.now() - 8.64e7;
      return time.getTime() < times;
    },
    clearBorrowForm() {
      this.order.expireTime = '';
      this.order.reason = '';
      this.visible2 = false;
    }
  }
}
</script>

<style scoped>

</style>
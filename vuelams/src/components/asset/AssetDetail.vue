<template>
  <div>
    <el-dialog
        :title="title"
        :visible.sync="dialogVisible2"
        :before-close="handleClose"
        width="80%">
      <div>
        <el-form :model="order.asset" :rules="rules" ref="assetForm">
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
          <el-row>
            <el-col :span="6">
              <el-form-item label="申请人:" prop="applicant">
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
            <el-col :span="6">
              <el-form-item label="理由:" prop="reason">{{ order.reason }}</el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <template v-if="order.asset.adv">
          <el-form v-show="order.asset.type=='手机'" :model="order.asset.adv" :rules="rules" ref="orderForm">
            <el-row>
              <el-col :span="6">
                <el-form-item label="内存（G）:" prop="adv">
                  {{ order.asset.adv.memory }}
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="屏幕尺寸（寸）:" prop="adv">
                  {{ order.asset.adv.screenSize }}
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <el-form v-show="order.asset.type=='交换机'" :model="order.asset.adv" :rules="rules" ref="orderForm">
            <el-row>
              <el-col :span="6">
                <el-form-item label="接口数:" prop="adv">
                  {{ order.asset.adv.nums }}
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="类型:" prop="adv">
                  {{ order.asset.adv.type }}
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <el-form v-show="order.asset.type=='主机'" :model="order.asset.adv" :rules="rules" ref="orderForm">
            <el-row>
              <el-col :span="6">
                <el-form-item label="cpu:" prop="adv">
                  {{ order.asset.adv.cpu }}
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="内存（G）:" prop="adv">
                  {{ order.asset.adv.memory }}
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <el-form v-show="order.asset.type=='测距仪'" :model="order.asset.adv" :rules="rules" ref="orderForm">
            <el-row>
              <el-col :span="6">
                <el-form-item label="精度:" prop="adv">
                  {{ order.asset.adv.precision }}
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="距离:" prop="adv">
                  {{ order.asset.adv.distance }}
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="方式:" prop="adv">
                  {{ order.asset.adv.methods }}
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </template>
      </div>
      <span slot="footer" class="dialog-footer">
      <el-button v-if="order.status=='已入库'" type="primary" @click="borrow()">借用</el-button>
    <template v-for="op in candidateBranches.operateList">
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
          <el-form-item label="姓名:">
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
      <el-button type="primary" @click="handle()">转交</el-button>
        <el-button @click="visible=false">取 消</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "AssetDetail",
  props: ['order', 'title', 'dialogVisible2', 'candidateBranches', 'rules'],
  data() {
    return {
      visible: false,
      name: '',
      taskHandleDto: {
        id: null,
        operateType: null,
        candidateUser: null
      }
    }
  },
  methods: {
    handle() {
      this.taskHandleDto.id = this.order.id;
      this.postRequest("/order/task/handleTask", this.taskHandleDto).then(resp => {
        if (resp) {
          this.visible = false;
          this.$emit('close');
          this.$parent.initOrders();
        }
      })
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
      this.postRequest("/order/basic/borrow", this.order).then(resp => {
        if (resp) {
          this.$parent.initOrders();
          this.$emit('close');
        }
      });
    },
    handleClose() {
      this.$emit('close');
    }
  }
}
</script>

<style scoped>

</style>
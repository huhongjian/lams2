<template>
  <div>
    <el-dialog
        :title="title"
        :visible.sync="dialogVisible2"
        width="80%">
      <div>
        <el-form :model="asset" :rules="rules" ref="assetForm">
          <el-row>
            <el-col :span="6">
              <el-form-item label="品牌:" prop="brand">
                {{ asset.brand }}
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="类型:" prop="type">
                {{ asset.type }}
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="价格:" prop="price">
                {{ asset.price }}
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="申请人:" prop="applicant">
                {{ asset.applicant }}
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="电话:" prop="applicantPhone">
                {{ asset.applicantPhone }}
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="邮件:" prop="applicantEmail">
                {{ asset.applicantEmail }}
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="理由:" prop="reason">{{ asset.reason }}</el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <template v-if="asset.adv">
          <el-form v-show="asset.type=='手机'" :model="asset.adv" :rules="rules" ref="assetForm">
            <el-row>
              <el-col :span="6">
                <el-form-item label="内存:" prop="memory">
                  {{ asset.adv.memory }}
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="屏幕尺寸:" prop="screenSize">
                  {{ asset.adv.screenSize }}
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </template>
      </div>
      <span slot="footer" class="dialog-footer">
      <el-button v-if="asset.status=='已入库'&&out" type="primary" @click="borrow()">借用</el-button>
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
            <el-input size="mini" style="width: 200px" prefix-icon="el-icon-edit"></el-input>
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
  props: ['out', 'asset', 'title', 'dialogVisible2', 'candidateBranches', 'rules'],
  data() {
    return {
      visible: false,
      taskHandleDto: {
        id: null,
        operateType: null,
        candidateUser: null
      }
    }
  },
  methods: {
    handle() {
      this.taskHandleDto.id = this.asset.id;
      this.postRequest("/asset/task/handleTask", this.taskHandleDto).then(resp => {
        if (resp) {
          this.visible = false;
          this.$emit('close');
          this.$parent.initEmps();
        }
      })
    },
    checkAndHandle(data) {
      this.taskHandleDto.operateType = data;
      if (data == '5') {
        this.visible = true;
      } else {
        this.handle();
      }
    },
    borrow() {
      this.postRequest("/asset/basic/borrow", this.asset).then(resp => {
        if (resp) {
          this.$parent.initEmps();
          this.$emit('close');
        }
      });
    },
  }
}
</script>

<style scoped>

</style>
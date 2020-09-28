<template>
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
      <el-button type="primary" @click="handle(op)">{{ op.operate }}</el-button>
    </template>
        <el-button @click="$emit('close')">取 消</el-button>
  </span>
  </el-dialog>
</template>

<script>
export default {
  name: "AssetDetail",
  props: ['out', 'asset', 'title', 'dialogVisible2', 'candidateBranches', 'rules'],
  data() {
    return {
      taskHandleDto: {
        id: null,
        operateType: null,
        candidateUser: null
      }
    }
  },
  methods: {
    handle(data) {
      this.taskHandleDto.id = this.asset.id;
      this.taskHandleDto.operateType = data.operateType;
      this.taskHandleDto.candidateUser = this.candidateBranches.candidateUser;
      this.postRequest("/asset/task/handleTask", this.taskHandleDto).then(resp => {
        if (resp) {
          this.$emit('close');
          this.initEmps();
        }
      })
    },
    borrow() {
      this.postRequest("/asset/basic/borrow", this.asset).then(resp => {
        if (resp) {
          this.initEmps();
          this.$emit('close');
        }
      });
    },
  }
}
</script>

<style scoped>

</style>
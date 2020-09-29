<template>
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
              <el-input size="mini" style="width: 200px" prefix-icon="el-icon-edit"
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
    <el-button @click="$emit('close')">取 消</el-button>
    <el-button type="primary" @click="doAddAsset">确 定</el-button>
  </span>
  </el-dialog>
</template>

<script>
export default {
  name: "AssetEdit",
  props: ['asset', 'title', 'dialogVisible', 'rules'],
  data() {
    return {
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
      }]
    }
  },
  methods: {
    doAddAsset() {
      if (this.asset.id) {
        this.$refs['assetForm'].validate(valid => {
          if (valid) {
            this.putRequest("/asset/basic/edit", this.asset).then(resp => {
              if (resp) {
                this.$emit('close');
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
                this.$emit('close');
                this.$parent.initEmps();
              }
            })
          }
        });
      }
    },
  }
}
</script>

<style scoped>

</style>
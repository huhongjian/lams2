<template>
  <el-dialog
      :title="title"
      :visible.sync="dialogVisible"
      :before-close="handleClose"
      width="80%">
    <div>
      <el-form :model="order" :rules="rules" ref="orderForm">
        <el-row>
          <el-col :span="6">
            <el-form-item label="品牌:" prop="asset.brand">
              <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                        v-model="order.asset.brand"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="类型:" prop="type">
              <el-select v-model="order.asset.type" clearable placeholder="请选择">
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
            <el-form-item label="价格（元）:" prop="asset.price">
              <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                        v-model="order.asset.price"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
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
      <template v-if="order.asset.adv">
        <el-form v-show="order.asset.type=='手机'" :model="order.asset.adv">
          <el-row>
            <el-col :span="6">
              <el-form-item label="内存（G）:" prop="memory">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="order.asset.adv.memory"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="屏幕尺寸（寸）:" prop="screenSize">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="order.asset.adv.screenSize"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form v-show="order.asset.type=='交换机'" :model="order.asset.adv">
          <el-row>
            <el-col :span="6">
              <el-form-item label="接口数:" prop="nums">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="order.asset.adv.nums"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="类型:" prop="type">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="order.asset.adv.type"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form v-show="order.asset.type=='主机'" :model="order.asset.adv">
          <el-row>
            <el-col :span="6">
              <el-form-item label="cpu:" prop="cpu">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="order.asset.adv.cpu"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="内存（G）:" prop="memory">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="order.asset.adv.memory"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form v-show="order.asset.type=='测距仪'" :model="order.asset.adv">
          <el-row>
            <el-col :span="6">
              <el-form-item label="精度:" prop="precision">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="order.asset.adv.precision"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="距离:" prop="distance">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="order.asset.adv.distance"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="方式:" prop="methods">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="order.asset.adv.methods"></el-input>
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
  name: "OrderEdit",
  props: ['order', 'title', 'dialogVisible'],
  data() {
    return {
      options: [
        {
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
      rules: {
        [`asset.brand`]: [{required: true, message: '请输入品牌', trigger: 'blur'}],
        [`asset.price`]: [{required: true, message: '请输入价格', trigger: 'blur'}],
        reason: [{required: true, message: '请输入申请理由', trigger: 'blur'}]
      }
    }
  },
  methods: {
    doAddAsset() {
      if (this.order.asset.id) {
        this.$refs['orderForm'].validate(valid => {
          if (valid) {
            this.putRequest("/order/basic/edit", this.order).then(resp => {
              if (resp) {
                this.$emit('close');
                this.$parent.initOrders();
              }
            })
          }
        });
      } else {
        this.$refs['orderForm'].validate(valid => {
          if (valid) {
            this.postRequest("/order/basic/add", this.order).then(resp => {
              if (resp) {
                this.$emit('close');
                this.$parent.initOrders();
              }
            })
          }
        });
      }
    },
    handleClose() {
      this.$emit('close');
    }
  }
}
</script>

<style scoped>

</style>
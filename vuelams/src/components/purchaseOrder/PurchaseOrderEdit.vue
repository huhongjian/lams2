<template>
  <div>
    <el-dialog
        :title="title"
        :visible.sync="dialogVisible3"
        :before-close="handleClose"
        width="80%">
      <div>
        <el-form :model="purchase" :rules="rules" ref="purchaseForm">
          <el-row>
            <el-col :span="15">
              <el-form-item label="订单名称:" prop="name">
                <el-input size="mini" style="width: 800px" prefix-icon="el-icon-edit"
                          v-model="purchase.name"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="订单总价（元）:" prop="total">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="purchase.total"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="订单优惠（元）:" prop="discount">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="purchase.discount"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="实际支付（元）:" prop="pay">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="purchase.pay"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="9">
              <el-form-item label="购买日期:" prop="purchaseDate">
                <el-date-picker
                    v-model="purchase.purchaseDate"
                    type="date"
                    placeholder="选择日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="是否有发票:" prop="hasInvoice">
                <el-select v-model="purchase.hasInvoice" clearable placeholder="请选择">
                  <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="9">
              <el-form-item label="发票日期:" prop="invoiceDate">
                <el-date-picker
                    v-model="purchase.invoiceDate"
                    type="date"
                    placeholder="选择日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="20">
              <el-form-item label="订单备注:" prop="remark">
                <el-input size="mini"
                          type="textarea"
                          :rows="2"
                          v-model="purchase.remark"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form>
          <el-row>
            <el-form-item label="订单相关图片:"></el-form-item>
          </el-row>
          <el-row>
            <el-form-item prop="pics">
              <el-upload
                  ref="upload"
                  :data="uploadData"
                  :file-list="fileList"
                  action="/purchase/pic/add"
                  list-type="picture-card"
                  :on-preview="handlePictureCardPreview"
                  :on-remove="handleRemove"
                  :auto-upload="false">
                <i class="el-icon-plus"></i>
              </el-upload>
            </el-form-item>
          </el-row>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="$emit('close')">取 消</el-button>
    <el-button type="primary" @click="doAddPurchase">确 定</el-button>
  </span>
    </el-dialog>
    <el-dialog :visible.sync="visible">
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "PurchaseOrderEdit",
  props: ['purchase', 'fileList', 'title', 'dialogVisible3', 'assetIds'],
  data() {
    return {
      addData: {
        purchaseOrder: {},
        aids: []
      },
      uploadData: {
        poid: ""
      },
      options: [
        {
          value: true,
          label: '是'
        }, {
          value: false,
          label: '否'
        }],
      rules: {
        total: [{required: true, message: '请输入订单总价', trigger: 'blur'}],
        discount: [{required: true, message: '请输入订单优惠', trigger: 'blur'}],
        pay: [{required: true, message: '请输入实际支付', trigger: 'blur'}],
        purchaseDate: [{required: true, message: '请输入购买日期', trigger: 'blur'}],
        hasInvoice: [{required: true, message: '请选择发票情况', trigger: 'blur'}]
      },
      dialogImageUrl: '',
      visible: false
    }
  },
  methods: {
    doAddPurchase() {
      if (this.purchase.id) {
        this.$refs['purchaseForm'].validate(valid => {
          if (valid) {
            this.putRequest("/purchase/edit", this.purchase).then(resp => {
              if (resp) {
                this.uploadData.poid = this.purchase.id;
                this.$refs.upload.submit();
                this.$emit('empty');
                this.$emit('close');
                this.$parent.initPurchaseOrders();
              }
            })
          }
        });
      } else {
        this.$refs['purchaseForm'].validate(valid => {
          if (valid) {
            this.addData.purchaseOrder = this.purchase;
            this.addData.aids = this.assetIds;
            this.postRequest("/purchase/add", this.addData).then(resp => {
              if (resp) {
                this.uploadData.poid = resp.obj;
                this.$refs.upload.submit();
                this.$emit('empty');
                this.$emit('close');
                this.$parent.initPurchaseOrders();
              }
            })
          }
        });
      }
    },
    handleClose() {
      this.$emit('close');
    },
    handleRemove(file, fileList) {
      if (file.id) {
        this.deleteRequest("/purchase/pic/remove/?pid=" + file.id).then(resp => {
              if (resp) {
                this.$parent.initPurchaseOrders();
              }
            }
        );
      }
      console.log(file, fileList);
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.visible = true;
    }
  }
}
</script>

<style scoped>

</style>
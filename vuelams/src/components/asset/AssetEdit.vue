<template>
  <div>
    <el-dialog
        :title="title"
        :visible.sync="dialogVisible"
        :before-close="handleClose"
        width="80%">
      <div>
        <el-form :model="asset" :rules="rules" ref="assetForm">
          <el-row>
            <el-col :span="15">
              <el-form-item label="资产名称:" prop="assetName">
                <el-input size="mini" style="width: 800px" prefix-icon="el-icon-edit"
                          v-model="asset.assetName"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="品牌:" prop="brand">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="asset.brand"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="类型:" prop="type">
                <el-select v-model="asset.type" clearable placeholder="请选择">
                  <el-option
                      v-for="item in types"
                      :key="item.id"
                      :label="item.name"
                      :value="item.name">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="价格（元）:" prop="price">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="asset.price"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="20">
              <el-form-item label="资产备注:" prop="remark">
                <el-input size="mini"
                          type="textarea"
                          :rows="2"
                          v-model="asset.remark"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row v-show="asset.id==null">
            <el-form-item label="数量（个）:" prop="amount">
              <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                        v-model="addData.amount"></el-input>
            </el-form-item>
          </el-row>
        </el-form>
        <template v-if="asset.adv">
          <el-form v-show="asset.type=='手机'" :model="asset.adv">
            <el-row>
              <el-col :span="6">
                <el-form-item label="内存（G）:" prop="memory">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="asset.adv.memory"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="屏幕尺寸（寸）:" prop="screenSize">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="asset.adv.screenSize"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <el-form v-show="asset.type=='交换机'" :model="asset.adv">
            <el-row>
              <el-col :span="6">
                <el-form-item label="接口数:" prop="nums">
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
          <el-form v-show="asset.type=='主机'" :model="asset.adv">
            <el-row>
              <el-col :span="6">
                <el-form-item label="cpu:" prop="cpu">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="asset.adv.cpu"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="内存（G）:" prop="memory">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="asset.adv.memory"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <el-form v-show="asset.type=='测距仪'" :model="asset.adv">
            <el-row>
              <el-col :span="6">
                <el-form-item label="精度:" prop="precision">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="asset.adv.precision"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="距离:" prop="distance">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="asset.adv.distance"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="方式:" prop="methods">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="asset.adv.methods"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </template>
        <el-form>
          <el-row>
            <el-form-item label="资产相关图片:"></el-form-item>
          </el-row>
          <el-row>
            <el-form-item prop="pics">
              <el-upload
                  ref="upload"
                  :data="uploadData"
                  :file-list="fileList"
                  action="/order/basic/pic/add"
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
    <el-button @click="handleClose">取 消</el-button>
    <el-button type="primary" @click="doAddAsset">确 定</el-button>
  </span>
    </el-dialog>
    <el-dialog :visible.sync="visible">
      <img width="100%" :src="dialogImageUrl" alt="">
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "AssetEdit",
  props: ['asset', 'fileList', 'title', 'dialogVisible', 'types'],
  data() {
    return {
      addData: {
        asset: {},
        amount: 1
      },
      uploadData: {
        aids: []
      },
      rules: {
        assetName: [{required: true, message: '请输入资产名称', trigger: 'blur'}],
        brand: [{required: true, message: '请输入品牌', trigger: 'blur'}],
        price: [{required: true, message: '请输入价格', trigger: 'blur'}],
        reason: [{required: true, message: '请输入申请理由', trigger: 'blur'}]
      },
      dialogImageUrl: '',
      visible: false
    }
  },
  methods: {
    doAddAsset() {
      if (this.asset.id) {
        this.$refs['assetForm'].validate(valid => {
          if (valid) {
            this.putRequest("/asset/edit", this.asset).then(resp => {
              if (resp) {
                this.uploadData.aids = [];
                this.uploadData.aids.push(this.asset.id);
                this.$refs.upload.submit();
                this.$emit('close');
                this.$parent.initAssets();
              }
            })
          }
        });
      } else {
        this.$refs['assetForm'].validate(valid => {
          if (valid) {
            this.addData.asset = this.asset;
            this.postRequest("/asset/add", this.addData).then(resp => {
              if (resp) {
                this.addData.amount = 1;
                this.uploadData.aids = resp.obj;
                this.$refs.upload.submit();
                this.$emit('handleAssetIds', resp.obj);
              }
            })
          }
        });
      }
    },
    handleClose() {
      this.addData.amount = 1;
      this.$emit('close');
    },
    handleRemove(file, fileList) {
      if (file.id) {
        this.deleteRequest("/order/basic/pic/remove/?pid=" + file.id).then(resp => {
              if (resp) {
                this.$parent.initOrders();
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
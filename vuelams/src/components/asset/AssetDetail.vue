<template>
  <el-dialog
      :title="title"
      :visible.sync="dialogVisible2"
      :before-close="handleClose"
      width="80%">
    <div>
      <el-form :model="asset">
        <el-row>
          <el-col :span="20">
            <el-form-item label="资产编号:" prop="id">
              {{ asset.id }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="20">
            <el-form-item label="资产名称:" prop="assetName">
              {{ asset.assetName }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="状态:" prop="statusName">
              {{ asset.statusName }}
            </el-form-item>
          </el-col>
        </el-row>
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
            <el-form-item label="价格（元）:" prop="price">
              {{ asset.price }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="10">
            <el-form-item v-show="asset.remark&&asset.remark!=''" label="资产备注:" prop="remark">
              {{ asset.remark }}
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template v-if="asset.adv">
        <el-form v-show="asset.type=='手机'" :model="asset.adv">
          <el-row>
            <el-col :span="6">
              <el-form-item label="内存（G）:" prop="memory">
                {{ asset.adv.memory }}
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="屏幕尺寸（寸）:" prop="screenSize">
                {{ asset.adv.screenSize }}
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form v-show="asset.type=='交换机'" :model="asset.adv">
          <el-row>
            <el-col :span="6">
              <el-form-item label="接口数:" prop="nums">
                {{ asset.adv.nums }}
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="类型:" prop="type">
                {{ asset.adv.type }}
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form v-show="asset.type=='主机'" :model="asset.adv">
          <el-row>
            <el-col :span="6">
              <el-form-item label="cpu:" prop="cpu">
                {{ asset.adv.cpu }}
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="内存（G）:" prop="memory">
                {{ asset.adv.memory }}
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form v-show="asset.type=='测距仪'" :model="asset.adv">
          <el-row>
            <el-col :span="6">
              <el-form-item label="精度:" prop="precision">
                {{ asset.adv.precision }}
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="距离:" prop="distance">
                {{ asset.adv.distance }}
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="方式:" prop="methods">
                {{ asset.adv.methods }}
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
        <el-button @click="$emit('close')">关 闭</el-button>
  </span>
  </el-dialog>
</template>

<script>
export default {
  name: "AssetDetail",
  props: ['asset', 'title', 'urlList', 'dialogVisible2'],
  methods: {
    handleClose() {
      this.$emit('close');
    }
  }
}
</script>

<style scoped>

</style>
<template>
  <div style="margin-top: 10px">
    <el-table
        :data="assets"
        stripe
        border
        v-loading="loading"
        element-loading-text="正在加载..."
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.8)"
        style="width: 100%">
      <el-table-column
          type="selection"
          width="55">
      </el-table-column>
      <el-table-column
          fixed
          label="资产编号"
          align="left"
          width="100">
        <template slot-scope="scope">
          <el-button size="mini" @click="dialogVisible2=true">{{ scope.row.id }}</el-button>
        </template>
      </el-table-column>
      <el-table-column
          prop="statusName"
          label="状态">
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
          width="150">
      </el-table-column>
      <el-table-column
          prop="brand"
          label="品牌"
          align="left"
          width="150">
      </el-table-column>
      <el-table-column
          prop="price"
          label="价格（元）"
          align="left"
          width="150">
      </el-table-column>
      <el-table-column
          prop="readyDate"
          width="200"
          align="left"
          label="入库时间">
      </el-table-column>
      <el-table-column
          prop="updateTime"
          width="200"
          align="left"
          label="更新时间">
      </el-table-column>
      <el-table-column
          fixed="right"
          width="150"
          label="操作">
        <template slot-scope="scope">
          <el-button @click="showEditView(scope.row)" style="padding: 3px" size="mini">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="display: flex;justify-content: flex-end">
      <el-pagination
          background
          @current-change="currentChange"
          @size-change="sizeChange"
          layout="sizes, prev, pager, next, jumper, ->, total, slot"
          :total="total">
      </el-pagination>
    </div>
    <AssetDetail v-on:close="dialogVisible2 = false" :dialogVisible2="dialogVisible2" :asset="asset"
                 :title="title"></AssetDetail>
    <el-dialog
        :title="title"
        :visible.sync="dialogVisible"
        width="80%">
      <div>
        <el-form :model="asset" :rules="rules" ref="assetForm">
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
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
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
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible=false">取 消</el-button>
    <el-button type="primary" @click="">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
import AssetDetail from "@/components/asset/AssetDetail";

export default {
  name: "AssetClean",
  mounted() {
    this.initAssets();
  },
  data() {
    return {
      loading: false,
      total: 0,
      page: 1,
      keyword: '',
      size: 10,
      dialogVisible: false,
      dialogVisible2: false,
      title: "",
      assets: [],
      asset: {
        id: "",
        brand: "华为",
        type: "手机",
        price: "4000",
        adv: {}
      },
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
        brand: [{required: true, message: '请输入品牌', trigger: 'blur'}],
        price: [{required: true, message: '请输入价格', trigger: 'blur'}],
        reason: [{required: true, message: '请输入申请理由', trigger: 'blur'}]
      },
      type: ""
    }
  },
  components: {
    AssetDetail
  },
  methods: {
    sizeChange(currentSize) {
      this.size = currentSize;
      if (this.type && this.type == 'advanced') {
        this.initAssetsAdv();
      } else {
        this.initAssets();
      }
    },
    currentChange(currentPage) {
      this.page = currentPage;
      if (this.type && this.type == 'advanced') {
        this.initAssetsAdv();
      } else {
        this.initAssets();
      }
    },
    showEditView(data) {
      this.title = '编辑资产信息';
      this.asset = data;
      this.dialogVisible = true;
    },
    showDetailView(data) {
      this.title = '资产信息详情';
      this.asset = data;
      this.dialogVisible2 = true;
    },
    initAssets() {
      this.type = '';
      this.loading = true;
      let url = '/asset/get/?page=' + this.page + '&size=' + this.size + "&aid=" + this.keyword;
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.assets = resp.data;
          this.total = resp.total;
        }
      });
    },
    initAssetsAdv() {
      this.type = 'advanced'
      this.loading = true;
      let url = '/asset/get/?category=1&page=' + this.page + '&size=' + this.size;
      if (this.searchValue.type) {
        url += '&type=' + this.searchValue.type;
      }
      if (this.searchValue.brand) {
        url += '&brand=' + this.searchValue.brand;
      }
      if (this.searchValue.status) {
        url += '&status=' + this.searchValue.status;
      }
      if (this.searchValue.userEmail) {
        url += '&userEmail=' + this.searchValue.userEmail;
      }
      if (this.searchValue.priceLow) {
        url += '&priceLow=' + this.searchValue.priceLow;
      }
      if (this.searchValue.priceHigh) {
        url += '&priceHigh=' + this.searchValue.priceHigh;
      }
      if (this.searchValue.beginDateScope) {
        url += '&beginDateScope=' + this.searchValue.beginDateScope;
      }
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.assets = resp.data;
          this.total = resp.total;
        }
      });
    },
  }
}
</script>

<style scoped>

</style>
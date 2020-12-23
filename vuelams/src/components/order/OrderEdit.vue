<template>
  <div>
    <el-dialog
        :title="title"
        :visible.sync="dialogVisible5"
        :before-close="handleClose"
        width="80%">
      <div>
        <el-form :model="order" :rules="rules" ref="orderForm">
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
          <el-row>
            <el-button type="primary" icon="el-icon-plus" @click="showAddView">
              新增资产
            </el-button>
            <el-button @click="deleteAsset" style="display: inline-flex;margin-left: 8px" type="danger">
              删除
            </el-button>
          </el-row>
          <div style="margin-top: 20px">
            <el-table
                :data="order.assetList"
                @selection-change="handleSelectionChange"
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
                  <el-button size="mini" @click="showDetailView(scope.row)">{{ scope.row.id }}</el-button>
                </template>
              </el-table-column>
              <el-table-column
                  fixed
                  prop="assetName"
                  :show-overflow-tooltip="true"
                  align="left"
                  label="资产名称">
              </el-table-column>
              <el-table-column
                  prop="statusName"
                  label="状态"
                  width="200">
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
                  width="200">
              </el-table-column>
              <el-table-column
                  prop="brand"
                  label="品牌"
                  align="left"
                  width="200">
              </el-table-column>
              <el-table-column
                  prop="price"
                  label="价格（元）"
                  align="left"
                  width="200">
              </el-table-column>
              <el-table-column
                  fixed="right"
                  width="80"
                  label="操作">
                <template slot-scope="scope">
                  <el-button @click="showEditView(scope.row)">编辑</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="handleCancle">取 消</el-button>
    <el-button type="primary" @click="doAddOrderIn">确 定</el-button>
  </span>
    </el-dialog>
    <AssetDetail v-on:close="dialogVisible2 = false" :dialogVisible2="dialogVisible2" :asset="asset"
                 :urlList="urlList" :title="title"></AssetDetail>
    <AssetEdit v-on:close="dialogVisible = false" :dialogVisible="dialogVisible" :asset="asset" :fileList="fileList"
               :title="title2" :types="types" :oid="this.order.id"></AssetEdit>
  </div>
</template>

<script>
import AssetEdit from "@/components/asset/AssetEdit";
import AssetDetail from "@/components/asset/AssetDetail";

export default {
  name: "NewOrder",
  props: ['order', 'title', 'dialogVisible5', 'types'],
  data() {
    return {
      deleteData: {
        assetIds: [],
        oid: null
      },
      // 选中的资产id
      assetIds: [],
      // 资产图片列表，用于编辑页面
      fileList: [],
      // 资产图片url列表，用于详情页面
      urlList: [],
      dialogVisible: false,
      dialogVisible2: false,
      title2: '',
      loading: false,
      asset: {
        id: "",
        status: "",
        statusName: "",
        assetName: "",
        brand: "",
        type: "",
        price: "",
        fileList: [],
        adv: {},
        remark: ""
      },
      total: 0,
      page: 1,
      size: 10,
      rules: {
        [`asset.assetName`]: [{required: true, message: '请输入资产名称', trigger: 'blur'}],
        [`asset.brand`]: [{required: true, message: '请输入品牌', trigger: 'blur'}],
        [`asset.price`]: [{required: true, message: '请输入价格', trigger: 'blur'}],
        reason: [{required: true, message: '请输入申请理由', trigger: 'blur'}]
      },
    }
  },
  components: {
    AssetEdit,
    AssetDetail
  },
  methods: {
    handleCancle() {
      this.$parent.initOrders();
      this.$emit('close');
    },
    emptyAsset() {
      this.asset = {
        id: "",
        assetName: "",
        brand: "",
        type: "",
        price: "",
        fileList: [],
        adv: {},
        remark: ""
      }
    },
    doAddOrderIn() {
      if (this.order.id) {
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
    showAddView() {
      this.emptyAsset();
      this.fileList = [];
      this.title2 = '新增资产';
      this.dialogVisible = true;
    },
    showDetailView(data) {
      this.title = '申请单详情';
      this.asset = data;
      if (this.asset && this.asset.fileList) {
        this.urlList = [];
        for (let i = 0; i < this.asset.fileList.length; i++) {
          this.urlList.push(this.asset.fileList[i].url);
        }
      } else {
        this.urlList = null;
      }
      this.dialogVisible2 = true;
    },
    showEditView(data) {
      this.title2 = '编辑资产';
      this.asset = data;
      if (this.asset.fileList && this.asset.fileList.length > 0) {
        this.fileList = this.asset.fileList;
      } else {
        this.fileList = [];
      }
      this.dialogVisible = true;
    },
    deleteAsset() {
      this.$confirm('此操作将永久删除选中的记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteData.assetIds = this.assetIds;
        this.deleteData.oid = this.order.id;
        this.deleteRequestWithData("/asset/delete", this.deleteData).then(resp => {
          if (resp) {
            this.assetIds = [];
            this.initAssets();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    handleClose() {
      this.$emit('close');
    },
    initAssets() {
      this.loading = true;
      let url = '/asset/getOrderAssetList/?oid=' + this.order.id + '&page=' + this.page + '&size=' + this.size;
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.order.assetList = resp.data;
          this.total = resp.total;
        }
      });
    },
    handleSelectionChange(val) {
      this.assetIds = [];
      for (let i = 0; i < val.length; i++) {
        this.assetIds.push(val[i].id);
      }
    }
  }
}
</script>

<style scoped>

</style>
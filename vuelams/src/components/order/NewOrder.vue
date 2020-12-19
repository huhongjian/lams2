<template>
  <div>
    <el-dialog
        :title="title"
        :visible.sync="dialogVisible4"
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
            <el-button @click="deleteOrder" style="display: inline-flex;margin-left: 8px" type="danger">
              删除
            </el-button>
          </el-row>
          <div style="margin-top: 20px">
            <el-table
                :data="order.assetList"
                stripe
                border
                v-loading="loading"
                element-loading-text="正在加载..."
                element-loading-spinner="el-icon-loading"
                element-loading-background="rgba(0, 0, 0, 0.8)"
                style="width: 100%">
              <el-table-column
                  fixed
                  prop="id"
                  label="资产编号"
                  align="left"
                  width="100">
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
    <el-button @click="$emit('close')">取 消</el-button>
    <el-button type="primary" @click="">确 定</el-button>
  </span>
    </el-dialog>
    <AssetDetail v-on:close="dialogVisible2 = false" :dialogVisible2="dialogVisible2" :asset="asset"
                 :urlList="urlList" :title="title"></AssetDetail>
    <OrderEdit v-on:close="dialogVisible = false" :dialogVisible="dialogVisible" :order="order" :fileList="fileList"
               :title="title2" :types="types"></OrderEdit>
  </div>
</template>

<script>
import OrderEdit from "@/components/order/OrderEdit";
import AssetDetail from "@/components/asset/AssetDetail";

export default {
  name: "NewOrder",
  props: ['order', 'fileList', 'title', 'dialogVisible4', 'types'],
  data() {
    return {
      dialogVisible: false,
      title2: '',
      loading: false,
      assets:[],
      rules: {
        [`asset.assetName`]: [{required: true, message: '请输入资产名称', trigger: 'blur'}],
        [`asset.brand`]: [{required: true, message: '请输入品牌', trigger: 'blur'}],
        [`asset.price`]: [{required: true, message: '请输入价格', trigger: 'blur'}],
        reason: [{required: true, message: '请输入申请理由', trigger: 'blur'}]
      },
    }
  },
  components: {
    OrderEdit,
    AssetDetail
  },
  methods: {
    showAddView() {
      // this.emptyOrder();
      // this.fileList = [];
      this.title2 = '新增资产';
      this.dialogVisible = true;
    },
    handleClose() {
      this.$emit('close');
    },
  }
}
</script>

<style scoped>

</style>
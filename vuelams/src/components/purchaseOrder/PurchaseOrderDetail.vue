<template>
  <el-dialog
      :title="title"
      :visible.sync="dialogVisible2"
      :before-close="handleClose"
      width="80%">
    <div>
      <el-form :model="purchase">
        <el-row>
          <el-col :span="6">
            <el-form-item label="订单编号:" prop="id">
              {{ purchase.id }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="订单名称:" prop="name">
              {{ purchase.name }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item label="订单总价（元）:" prop="total">
              {{ purchase.total }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="订单优惠（元）:" prop="discount">
              {{ purchase.discount }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="实际支付（元）:" prop="pay">
              {{ purchase.pay }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item label="购买日期:" prop="purchaseDate">
              {{ purchase.purchaseDate }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item label="是否有发票:" prop="hasInvoice">
              {{ purchase.hasInvoice }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="发票日期:" prop="invoiceDate">
              {{ purchase.invoiceDate }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item label="创建者姓名:" prop="creator.name">
              {{ purchase.creator.name }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="创建者邮箱:" prop="creatorEmail">
              {{ purchase.creatorEmail }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item label="更新者姓名:" prop="updater.name">
              {{ purchase.updater.name }}
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="更新者邮箱:" prop="updaterEmail">
              {{ purchase.updaterEmail }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <el-form-item label="关联资产信息:">
            </el-form-item>
          </el-col>
        </el-row>
        <el-table
            :data="purchase.assetList"
            stripe
            border
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
        </el-table>
        <el-row style="margin-top: 10px">
          <el-col :span="10">
            <el-form-item v-show="purchase.remark&&purchase.remark!=''" label="订单备注:" prop="remark">
              {{ purchase.remark }}
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
    </div>
    <span slot="footer" class="dialog-footer">
        <el-button @click="$emit('close')">关 闭</el-button>
  </span>
  </el-dialog>
</template>

<script>
export default {
  name: "PurchaseOrderDetail",
  props: ['purchase', 'title', 'urlList', 'dialogVisible2'],
  methods: {
    handleClose() {
      this.$emit('close');
    }
  }
}
</script>

<style scoped>

</style>
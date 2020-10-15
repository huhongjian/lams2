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
          prop="id"
          fixed
          label="资产编号"
          align="left"
          width="90">
      </el-table-column>
      <el-table-column
          prop="type"
          align="left"
          label="类型"
          width="60">
      </el-table-column>
      <el-table-column
          prop="brand"
          label="品牌"
          align="left"
          width="80">
      </el-table-column>
      <el-table-column
          prop="price"
          label="价格"
          align="left"
          width="80">
      </el-table-column>
      <el-table-column
          prop="charger"
          width="95"
          align="left"
          label="负责人">
      </el-table-column>
      <el-table-column
          prop="chargerEmail"
          width="150"
          label="负责人邮箱">
      </el-table-column>
      <el-table-column
          prop="chargerPhone"
          width="100"
          label="负责人电话">
      </el-table-column>
      <el-table-column
          prop="asset.readyDate"
          width="100"
          align="left"
          label="入库时间">
      </el-table-column>
      <el-table-column
          prop="updateTime"
          width="100"
          align="left"
          label="更新时间">
      </el-table-column>
      <el-table-column
          fixed="right"
          width="150"
          label="操作">
        <template slot-scope="scope">
          <el-button @click="showEditEmpView(scope.row)" style="padding: 3px" size="mini">编辑</el-button>
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
  </div>
</template>

<script>
export default {
  name: "AssetInfo",
  data() {
    return {
      assets: []
    }
  }
}
</script>

<style scoped>

</style>
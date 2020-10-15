<template>
  <div style="margin-top: 10px">
    <el-table
        :data="cleans"
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
          label="清理单号"
          align="left"
          width="90">
      </el-table-column>
      <el-table-column
          prop="aid"
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
          prop="applicant"
          width="95"
          align="left"
          label="清理人">
      </el-table-column>
      <el-table-column
          prop="applicantEmail"
          width="150"
          label="清理人邮箱">
      </el-table-column>
      <el-table-column
          prop="applicantPhone"
          width="150"
          label="清理人电话">
      </el-table-column>
      <el-table-column
          prop="description"
          width="100"
          label="清理原因">
      </el-table-column>
      <el-table-column
          prop="createTime"
          width="100"
          align="left"
          label="创建时间">
      </el-table-column>
      <el-table-column
          prop="updateTime"
          width="100"
          align="left"
          label="完成时间">
      </el-table-column>
      <el-table-column
          fixed="right"
          width="150"
          label="操作">
        <template slot-scope="scope">
          <el-button @click="showEditEmpView(scope.row)" style="padding: 3px" size="mini">编辑</el-button>
          <el-button @click="showEditEmpView(scope.row)" style="padding: 3px" size="mini">完成</el-button>
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
  name: "AssetClean"
}
</script>

<style scoped>

</style>
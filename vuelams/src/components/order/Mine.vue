<template>
  <div>
    <div style="margin-top: 10px">
      <el-table
          :data="orders"
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
            label="申请单编号"
            align="left"
            width="90">
          <template slot-scope="scope">
            <el-button size="mini" @click="getOperateList(scope.row)">{{
                scope.row.id
              }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column
            prop="categoryName"
            align="left"
            label="流程"
            width="90">
        </el-table-column>
        <el-table-column
            prop="statusName"
            width="90"
            label="状态">
          <template slot-scope="scope">
            <span style="color: #00e079; font-weight: bold"
                  v-if="scope.row.status=='2'||scope.row.status=='3'">{{ scope.row.statusName }}</span>
            <span style="color: #ff4777; font-weight: bold"
                  v-else-if="scope.row.status=='6'||scope.row.status=='8'||scope.row.status=='7'">{{
                scope.row.statusName
              }}</span>
            <span style="color: #c0c0c0;"
                  v-else-if="scope.row.status=='5'">{{ scope.row.statusName }}</span>
            <span v-else>{{ scope.row.statusName }}</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="expireTime"
            width="100"
            align="left"
            label="预计归还时间">
        </el-table-column>
        <el-table-column
            prop="reason"
            :show-overflow-tooltip="true"
            label="理由">
        </el-table-column>
        <el-table-column
            prop="user.name"
            width="95"
            align="left"
            label="申请人">
        </el-table-column>
        <el-table-column
            prop="user.username"
            width="150"
            align="left"
            label="申请人邮箱">
        </el-table-column>
        <el-table-column
            prop="user.phone"
            width="100"
            label="申请人电话">
        </el-table-column>
        <el-table-column
            prop="createTime"
            align="left"
            label="申请时间"
            width="180">
        </el-table-column>
        <el-table-column
            prop="updateTime"
            align="left"
            label="更新时间"
            width="180">
        </el-table-column>
        <el-table-column
            v-if="isOut&&isOut==false"
            fixed="right"
            width="80"
            label="操作">
          <template slot-scope="scope">
            <el-button @click="showEditView(scope.row)">编辑</el-button>
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
  </div>
</template>

<script>

export default {
  name: "Mine",
  props: ['orders', 'total', 'page', 'size', 'loading', 'isOut'],
  methods: {
    sizeChange(currentSize) {
      this.$emit('currentSize', currentSize)
    },
    currentChange(currentPage) {
      this.$emit('currentPage', currentPage)
    },
    getOperateList(data) {
      this.$parent.getOperateList(data);
    },
    showEditView(data) {
      this.$parent.showEditView(data);
    },
  }
}
</script>

<style scoped>

</style>
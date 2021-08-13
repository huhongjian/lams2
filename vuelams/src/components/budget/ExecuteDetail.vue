<template>
  <div>
    <el-dialog
        :title="title"
        :visible.sync="executeDetailVisible"
        :before-close="handleClose"
        width="80%">
      <el-button @click="deleteProject" style="display: inline-flex;margin-left: 8px" type="danger">
        撤销执行
      </el-button>
      <div style="margin-top: 10px">
        <el-table
            :data="budgetRecordList"
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
              prop="id"
              label="流水编号"
              align="left"
              width="80">
          </el-table-column>
          <el-table-column
              prop="projectName"
              align="left"
              width="100"
              label="金额（万元）">
          </el-table-column>
          <el-table-column
              prop="projectType"
              :show-overflow-tooltip="true"
              label="科研项目">
          </el-table-column>
          <el-table-column
              prop="years"
              label="预算计划">
          </el-table-column>
          <el-table-column
              prop="funding"
              label="预算科目">
          </el-table-column>
          <el-table-column
              prop="executeRate"
              width="95"
              align="left"
              label="备注">
          </el-table-column>
          <el-table-column
              prop="createTime"
              width="150"
              align="left" v
              label="申请时间">
          </el-table-column>
        </el-table>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="$emit('close')">取 消</el-button>
    <el-button type="primary" @click="$emit('close')">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>

export default {
  name: "ExecuteDetail",
  props: ['title', 'executeDetailVisible', 'budgetRecordList'],
  data() {
    return {
      recordIds: [],
      loading: false,
      execution: {}
    }
  },
  methods: {
    handleSelectionChange(val) {
      this.recordIds = [];
      for (let i = 0; i < val.length; i++) {
        this.recordIds.push(val[i].id);
      }
    },
    handleClose() {
      this.$emit('close');
    }
  }
}
</script>

<style scoped>

</style>
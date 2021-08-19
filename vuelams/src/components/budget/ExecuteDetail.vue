<template>
  <div>
    <el-dialog
        :title="title"
        :visible.sync="executeDetailVisible"
        :before-close="handleClose"
        width="80%">
      <el-button @click="revertExecution" style="display: inline-flex;margin-left: 8px" type="danger">
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
              prop="id"
              label="流水编号"
              width="80">
          </el-table-column>
          <el-table-column
              prop="funding"
              width="100"
              label="金额（万元）">
          </el-table-column>
          <el-table-column
              prop="projectName"
              :show-overflow-tooltip="true"
              label="科研项目">
          </el-table-column>
          <el-table-column
              prop="planName"
              :show-overflow-tooltip="true"
              label="预算计划">
          </el-table-column>
          <el-table-column
              prop="itemTypeName"
              :show-overflow-tooltip="true"
              width="150"
              label="预算科目">
          </el-table-column>
          <el-table-column
              prop="planItemId"
              width="100"
              label="预算明细序号">
          </el-table-column>
          <el-table-column
              prop="remark"
              :show-overflow-tooltip="true"
              label="备注">
          </el-table-column>
          <el-table-column
              prop="user.name"
              width="100"
              label="执行人">
          </el-table-column>
          <el-table-column
              prop="createTime"
              width="100"
              label="执行时间">
          </el-table-column>
        </el-table>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="$emit('close')">关 闭</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>

export default {
  name: "ExecuteDetail",
  props: ['title', 'executeDetailVisible', 'budgetRecordList', 'planId'],
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
    },
    revertExecution() {
      this.$confirm('此操作将永久删除选中的记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequestWithData("/execution/delete", this.recordIds).then(resp => {
          if (resp) {
            this.recordIds = [];
            this.$parent.initBudgetRecordList();
            this.$emit('freshPlan', this.planId);
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    }
  }
}
</script>

<style scoped>

</style>
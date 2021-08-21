<template>
  <div>
    <el-dialog
        :title="title"
        :visible.sync="executePlanVisible"
        :before-close="handleClose"
        width="80%">
      <div>
        <el-form :model="budgetRecord" ref="executionForm">
          <el-row>
            <el-col :span="20">
              <el-form-item label="预算计划名称:" prop="planName">
                {{ planItem.planName }}
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="15">
              <el-form-item label="科研项目:" prop="projectId">
                {{ planItem.projectName }}
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="金额（万元）:" prop="planName">
                <el-input size="mini" style="width: 200px" prefix-icon="el-icon-edit"
                          v-model="budgetRecord.funding"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="执行时间:" prop="planName">
                <el-date-picker
                    v-model="budgetRecord.executeDate"
                    type="date"
                    placeholder="选择日期时间">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-form-item label="备注:" prop="remark">
              <el-input size="mini"
                        type="textarea"
                        :rows="2"
                        placeholder="请输入预算执行信息备注说明"
                        v-model="budgetRecord.remark"></el-input>
            </el-form-item>
          </el-row>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="$emit('close')">取 消</el-button>
    <el-button type="primary" @click="executePlan">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>

export default {
  name: "ExecutePlan",
  props: ['title', 'planItem', 'executePlanVisible', 'projectLists'],
  data() {
    return {
      budgetRecord: {
        id: "",
        funding: "",
        projectId: "",
        projectName: "",
        planId: "",
        planName: "",
        planItemId: "",
        itemType: "",
        itemTypeName: "",
        remark: "",
        userEmail: "",
        executeDate: "",
        createTime: "",
        updateTime: ""
      }
    }
  },
  mounted() {
    this.emptyBudgetRecord();
  },
  methods: {
    emptyBudgetRecord() {
      this.budgetRecord = {
        id: "",
        funding: "",
        projectId: "",
        projectName: "",
        planId: "",
        planName: "",
        planItemId: "",
        itemType: "",
        itemTypeName: "",
        remark: "",
        userEmail: "",
        executeDate: "",
        createTime: "",
        updateTime: ""
      }
    },
    handleClose() {
      this.$emit('close');
    },
    executePlan() {
      let url = '/execution/add';
      this.budgetRecord.projectId = this.planItem.projectId;
      this.budgetRecord.projectName = this.planItem.projectName;
      this.budgetRecord.planId = this.planItem.planId;
      this.budgetRecord.planName = this.planItem.planName;
      this.budgetRecord.planItemId = this.planItem.id;
      this.budgetRecord.itemType = this.planItem.itemType;
      this.budgetRecord.itemTypeName = this.planItem.itemTypeName;
      this.$refs['executionForm'].validate(valid => {
        if (valid) {
          this.postRequest(url, this.budgetRecord).then(resp => {
            if (resp) {
              this.$emit('close');
              this.emptyBudgetRecord();
              this.$emit('freshPlan', this.planItem.planId);
            }
          });
        }
      });
    }
  }
}
</script>

<style scoped>

</style>
<template>
  <el-dialog
      :title="title"
      :visible.sync="warningDetailVisible"
      :before-close="handleClose"
      width="80%">
    <div>
      <el-form :model="warningConfig">
        <el-row>
          <el-form-item label="报警配置编号:" prop="id">
            {{ warningConfig.id }}
          </el-form-item>
        </el-row>
        <el-row>
          <el-col span="10">
            <el-form-item label="项目编号:" prop="projectId">
              {{ warningConfig.projectId }}
            </el-form-item>
          </el-col>
          <el-col span="10">
            <el-form-item label="项目名称:" prop="projectName">
              {{ warningConfig.projectName }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col span="10">
            <el-form-item label="计划编号:" prop="planId">
              {{ warningConfig.planId }}
            </el-form-item>
          </el-col>
          <el-col span="10">
            <el-form-item label="计划名称:" prop="planName">
              {{ warningConfig.planName }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col span="10">
            <el-form-item label="预算科目:" prop="itemTypeName">
              {{ warningConfig.itemTypeName }}
            </el-form-item>
          </el-col>
          <el-col span="10">
            <el-form-item label="报警执行率（%）:" prop="executeRate">
              {{ warningConfig.executeRate }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="10">
            <el-form-item label="报警时间:" prop="dateScope">
              {{ warningConfig.startTime }} 至 {{ warningConfig.endTime }}
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="报警频率:" prop="freqName">
              {{ warningConfig.freqName }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-form-item label="报警接收人列表:" prop="toUserString">
            {{ warningConfig.toUserString }}
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="处理建议:" prop="advice">
            {{ warningConfig.advice }}
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="创建人邮箱:" prop="userEmail">
            {{ warningConfig.userEmail }}
          </el-form-item>
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
  name: "WarningDetail",
  props: ['warningConfig', 'title', 'warningDetailVisible'],
  methods: {
    handleClose() {
      this.$emit('close');
    }
  }
}
</script>

<style scoped>

</style>
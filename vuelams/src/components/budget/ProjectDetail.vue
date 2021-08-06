<template>
  <el-dialog
      :title="title"
      :visible.sync="projectDetailVisible"
      :before-close="handleClose"
      width="50%">
    <div>
      <el-form :model="project">
        <el-row>
          <el-form-item label="项目编号:" prop="id">
            {{ project.id }}
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="项目名称:" prop="projectName">
            {{ project.projectName }}
          </el-form-item>
        </el-row>
        <el-row>
          <el-form-item label="类型:" prop="projectType">
            {{ project.projectType }}
          </el-form-item>
        </el-row>
        <el-row>
          <el-col :span="10">
            <el-form-item label="项目总体执行年限（年）:" prop="projectYears">
              {{ project.years }}
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="资助额度（万元）:" prop="projectFunding">
              {{ project.funding }}
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
            <el-form-item label="执行率%:" prop="executeRate">
              {{ project.executeRate }}
            </el-form-item>
        </el-row>
        <el-row>
          <el-col :span="10">
            <el-form-item label="创建人:" prop="name">
              {{ project.user.name }}
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="创建人邮箱:" prop="userEmail">
              {{ project.user.username }}
            </el-form-item>
          </el-col>
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
  name: "ProjectDetail",
  props: ['project', 'title', 'projectDetailVisible'],
  methods: {
    handleClose() {
      this.$emit('close');
    }
  }
}
</script>

<style scoped>

</style>
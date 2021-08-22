<template>
  <div>
    <el-dialog
        :title="title"
        :visible.sync="newProjectVisible"
        :before-close="handleClose"
        width="80%">
      <div>
        <el-form :model="project" :rules="rules" ref="projectForm">
          <el-row>
            <el-form-item label="项目名称:" prop="projectName">
              <el-input size="mini" style="width: 800px" prefix-icon="el-icon-edit"
                        v-model="project.projectName"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="类型:" prop="projectType">
              <el-select style="width: 800px" v-model="project.projectType" clearable placeholder="请选择">
                <el-option
                    v-for="item in projectTypes"
                    :key="item.id"
                    :label="item.name"
                    :value="item.name">
                </el-option>
              </el-select>
            </el-form-item>
          </el-row>
          <el-row>
            <el-col :span="10">
              <el-form-item label="项目总体执行年限（年）:" prop="years">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="project.years"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item label="资助额度（万元）:" prop="funding">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="project.funding"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="$emit('close')">取 消</el-button>
    <el-button type="primary" @click="doAddProject">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>

export default {
  name: "NewProject",
  props: ['project', 'title', 'newProjectVisible', 'projectTypes'],
  data() {
    return {
      rules: {
        projectName: [{required: true, message: '请输入项目名称', trigger: 'blur'}],
        projectType: [{required: true, message: '请选择项目类型', trigger: 'blur'}],
        years: [{required: true, message: '请输入年限', trigger: 'blur'}],
        funding: [{required: true, message: '请输入资助额度', trigger: 'blur'}]
      },
    }
  },
  methods: {
    doAddProject() {
      if (this.project.id) {
        this.$refs['projectForm'].validate(valid => {
          if (valid) {
            this.putRequest("/project/edit", this.project).then(resp => {
              if (resp) {
                this.$emit('close');
                this.$parent.initProjects();
                this.$parent.initHeadTableData();
              }
            })
          }
        });
      } else {
        this.$refs['projectForm'].validate(valid => {
          if (valid) {
            this.postRequest("/project/add", this.project).then(resp => {
              if (resp) {
                this.$emit('close');
                this.$parent.initProjects();
                this.$parent.initHeadTableData();
              }
            })
          }
        });
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
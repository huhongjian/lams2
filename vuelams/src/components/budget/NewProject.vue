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
            <el-col :span="15">
              <el-form-item label="项目名称:" prop="projectName">
                <el-input size="mini" style="width: 800px" prefix-icon="el-icon-edit"
                          v-model="project.projectName"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="15">
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
            </el-col>
            <el-col :span="15">
              <el-form-item label="项目总体执行年限（年）:" prop="projectYears">
                <el-input size="mini" style="width: 800px" prefix-icon="el-icon-edit"
                          v-model="project.years"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="15">
              <el-form-item label="资助额度（万元）:" prop="projectFunding">
                <el-input size="mini" style="width: 800px" prefix-icon="el-icon-edit"
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
      // 初始化资产信息
      initData: {
        // 添加的全部资产id
        aids: [],
        page: 1,
        size: 10
      },
      // 选中的资产id
      assetIds: [],
      // 资产图片列表，用于编辑页面
      fileList: [],
      // 资产图片url列表，用于详情页面
      urlList: [],
      dialogVisible: false,
      dialogVisible2: false,
      title2: '',
      loading: false,
      asset: {
        id: "",
        status: "",
        statusName: "",
        assetName: "",
        brand: "",
        type: "",
        price: "",
        fileList: [],
        adv: {},
        remark: ""
      },
      total: 0,
      page: 1,
      size: 10,
      rules: {
        reason: [{required: true, message: '请输入申请理由', trigger: 'blur'}]
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
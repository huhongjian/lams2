<template>
  <div>
    <el-dialog
        :title="title"
        :visible.sync="newWarningVisible"
        :before-close="handleClose"
        width="80%">
      <div>
        <el-form :model="warningConfig" :rules="rules" ref="configForm">
          <el-row>
            <el-col :span="8">
              <el-form-item label="科研项目:" prop="projectId">
                <el-select v-model="warningConfig.projectId" clearable placeholder="请选择">
                  <el-option
                      v-for="item in projectList"
                      :key="item.id"
                      :label="item.projectName"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="预算计划:" prop="planId">
                <el-select v-model="warningConfig.planId" clearable placeholder="请选择">
                  <el-option
                      v-for="item in planList"
                      :key="item.id"
                      :label="item.planName"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="预算科目:" prop="itemType">
                <el-select v-model="warningConfig.itemType" clearable placeholder="请选择">
                  <el-option
                      v-for="item in planItemTypes"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-form-item label="报警执行率（%）:" prop="executeRate">
              <el-input size="mini" style="width: 200px" prefix-icon="el-icon-edit"
                        v-model="warningConfig.executeRate"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-col :span="10">
              <el-form-item label="报警时间:" prop="dateScope">
                <el-date-picker
                    v-model="warningConfig.dateScope"
                    type="daterange"
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item label="报警频率:" prop="freq">
                <el-select v-model="warningConfig.freq" clearable placeholder="请选择">
                  <el-option
                      v-for="item in warningFreq"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-form-item label="报警接收人列表:" prop="toUser">
              <el-select v-model="warningConfig.toUser" multiple filterable placeholder="请选择">
                <el-option
                    v-for="item in userList"
                    :key="item.username"
                    :label="item.name"
                    :value="item.username">
                </el-option>
              </el-select>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="处理建议:" prop="advice">
              <el-input size="mini"
                        type="textarea"
                        :rows="2"
                        v-model="warningConfig.advice"></el-input>
            </el-form-item>
          </el-row>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="handleClose">取 消</el-button>
        <el-button type="primary" @click="doAddWarning">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "NewWarning",
  props: ['warningConfig', 'title', 'newWarningVisible'],

  mounted() {
    this.initProjects();
    this.initPlans();
    this.initUsers();
  },
  data() {
    return {
      projectList: [],
      planList: [],
      userList: [],
      planItemTypes: [
        {
          id: 1,
          name: '设备费'
        },
        {
          id: 2,
          name: '材料费'
        },
        {
          id: 3,
          name: '测试化验加工费'
        },
        {
          id: 4,
          name: '会议费'
        },
        {
          id: 5,
          name: '课题调研差旅费'
        },
        {
          id: 6,
          name: '国内学术会议差旅费'
        },
        {
          id: 7,
          name: '北京市内交通费'
        },
        {
          id: 8,
          name: '出版/文献/信息传播/知识产权事务费'
        },
        {
          id: 9,
          name: '劳务费'
        },
        {
          id: 10,
          name: '专家咨询费'
        },
        {
          id: 11,
          name: '其他支出'
        },
      ],
      warningFreq: [
        {
          id: 1,
          name: "每天"
        },
        {
          id: 2,
          name: "每周"
        }
      ],
    }
  },
  methods: {
    handleClose() {
      this.countType = null;
      this.$emit('close');
    },
    initProjects() {
      let url = '/project/get/all';
      this.getRequest(url).then(resp => {
        if (resp) {
          this.projectList = resp.obj;
        }
      });
    },
    initPlans() {
      let url = '/plan/get/all';
      this.getRequest(url).then(resp => {
        if (resp) {
          this.planList = resp.obj;
        }
      });
    },
    initUsers() {
      this.getRequest("/system/user/all").then(resp => {
        if (resp) {
          this.userList = resp.obj;
        }
      });
    },
    doAddWarning() {
      if (this.warningConfig.id) {
        this.$refs['configForm'].validate(valid => {
          if (valid) {
            this.putRequest("/plan/edit", this.warningConfig).then(resp => {
              if (resp) {
                this.$emit('close');
                this.$parent.initPlans();
              }
            })
          }
        });
      } else {
        this.$refs['configForm'].validate(valid => {
          if (valid) {
            this.postRequest("/warning/config/add", this.warningConfig).then(resp => {
              if (resp) {
                this.$emit('close');
                this.$parent.initConfigs();
              }
            })
          }
        });
      }
    },
  }
}
</script>

<style scoped>

</style>
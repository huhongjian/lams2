<template>
  <div>
    <el-dialog
        :title="title"
        :visible.sync="newPlanItemVisible"
        :before-close="handleClose"
        width="80%">
      <div>
        <el-form :model="planItem" :rules="rules" ref="planItemForm">
          <el-row>
            <el-col :span="15">
              <el-form-item label="预算科目:" prop="itemType">
                <el-select style="width: 200px" v-model="planItem.itemType" clearable placeholder="请选择">
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
            <el-form-item label="备注:" prop="remark">
              <el-input size="mini"
                        type="textarea"
                        :rows="2"
                        placeholder="预算明细其他备注信息"
                        v-model="planItem.remark"></el-input>
            </el-form-item>
          </el-row>
        </el-form>
        <el-form v-show="planItem.itemType==1" :model="planItem">
          <el-row>
            <el-form-item label="设备名称:" prop="itemName">
              <el-input size="mini" prefix-icon="el-icon-edit"
                        v-model="planItem.equipmentName"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="用途:" prop="usage">
              <el-input size="mini" prefix-icon="el-icon-edit"
                        v-model="planItem.usage"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="单价（万元）:" prop="unitPrice">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.unitPrice"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="数量:" prop="amount">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.amount"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form v-show="planItem.itemType==2" :model="planItem">
          <el-row>
            <el-form-item label="材料用途:" prop="usage">
              <el-input size="mini" prefix-icon="el-icon-edit"
                        v-model="planItem.usage"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="材料名称:" prop="materialName">
              <el-input size="mini" prefix-icon="el-icon-edit"
                        v-model="planItem.materialName"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="规格:" prop="specification">
              <el-input size="mini" prefix-icon="el-icon-edit"
                        v-model="planItem.specification"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-col :span="15">
              <el-form-item label="计价方式:" prop="count">
                <el-select style="width: 200px" v-model="countType" clearable placeholder="请选择">
                  <el-option
                      v-for="item in countTypeList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row v-show="countType==1">
            <el-col :span="6">
              <el-form-item label="单价（万元）:" prop="unitPrice">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.unitPrice"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="数量:" prop="amount">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.amount"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="单位:" prop="unit">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.unit"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row v-show="countType==2">
            <el-col :span="6">
              <el-form-item label="小计（万元）:" prop="subTotal">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.subTotal"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form v-show="planItem.itemType==3" :model="planItem">
          <el-row>
            <el-form-item label="加工内容:" prop="content">
              <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                        v-model="planItem.content"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="单价（万元）:" prop="unitPrice">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.unitPrice"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="次数:" prop="amount">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.amount"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form v-show="planItem.itemType==4" :model="planItem">
          <el-row>
            <el-form-item label="类别:" prop="subType">
              <el-select style="width: 200px" v-model="planItem.subType" clearable placeholder="请选择">
                <el-option
                    v-for="item in planItemTypes"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="说明:" prop="description">
              <el-input size="mini"
                        type="textarea"
                        :rows="2"
                        placeholder="预算明细说明"
                        v-model="planItem.description"></el-input>
            </el-form-item>
          </el-row>
        </el-form>
        <el-form v-show="planItem.itemType==5" :model="planItem">
          <el-row>
            <el-form-item label="类别:" prop="usage">
              <el-input size="mini" prefix-icon="el-icon-edit"
                        v-model="planItem.usage"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="说明:" prop="description">
              <el-input size="mini"
                        type="textarea"
                        :rows="2"
                        placeholder="预算明细说明"
                        v-model="planItem.description"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="合计（万元）:" prop="usage">
              <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                        v-model="planItem.usage"></el-input>
            </el-form-item>
          </el-row>
        </el-form>
        <el-form v-show="planItem.itemType==6" :model="planItem">
          <el-row>
            <el-form-item label="人员种类:" prop="personnelType">
              <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                        v-model="planItem.personnelType"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="工作内容:" prop="content">
              <el-input size="mini" prefix-icon="el-icon-edit"
                        v-model="planItem.content"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="人数:" prop="amount">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.amount"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="标准（元/人月）:" prop="unitPrice">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.unitPrice"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="工作时间（月）:" prop="workingHours">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.workingHours"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form v-show="planItem.itemType==7" :model="planItem">
          <el-row>
            <el-form-item label="咨询内容:" prop="content">
              <el-input size="mini" prefix-icon="el-icon-edit"
                        v-model="planItem.content"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="人员类型:" prop="personnelType">
              <el-input size="mini" prefix-icon="el-icon-edit"
                        v-model="planItem.personnelType"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="标准（元/天）:" prop="unitPrice">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.unitPrice"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="人数:" prop="amount">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.amount"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="天数:" prop="workingHours">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.workingHours"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
          </el-row>
        </el-form>
        <el-form v-show="planItem.itemType==8" :model="planItem">
          <el-row>
            <el-form-item label="类别:" prop="usage">
              <el-input size="mini" prefix-icon="el-icon-edit"
                        v-model="planItem.usage"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="说明:" prop="description">
              <el-input size="mini"
                        type="textarea"
                        :rows="2"
                        placeholder="预算明细说明"
                        v-model="planItem.description"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="合计（万元）:" prop="usage">
              <el-input size="mini" prefix-icon="el-icon-edit"
                        v-model="planItem.usage"></el-input>
            </el-form-item>
          </el-row>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="handleClose">取 消</el-button>
    <el-button type="primary" @click="doAddPlanItem">确 定</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>

export default {
  name: "NewProjectItem",
  props: ['planItem', 'title', 'newPlanItemVisible', "planItemTypes"],
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
      countType: null,
      countTypeList: [
        {
          id: 1,
          name: '按单价计算'
        },
        {
          id: 2,
          name: '按合计计算'
        },
      ],
      total: 0,
      page: 1,
      size: 10,
      itemUrl: "",
      rules: {
        remark: [{required: true, message: '请输入申请理由', trigger: 'blur'}]
      },
    }
  },
  methods: {
    getItemUrl() {
      if (this.planItem.itemType == 1) {
        this.itemUrl = 'equipmentFee'
      }else if (this.planItem.itemType == 2){
        this.itemUrl = 'materialFee'
      }
    },
    doAddPlanItem() {
      if (this.planItem.id) {
        this.$refs['planItemForm'].validate(valid => {
          if (valid) {
            this.getItemUrl();
            let url = "/planItem/edit/" + this.itemUrl;
            this.putRequest(url, this.planItem).then(resp => {
              if (resp) {
                this.$emit('close');
                this.$parent.initProjects();
              }
            })
          }
        });
      } else {
        this.$refs['planItemForm'].validate(valid => {
          if (valid) {
            this.getItemUrl();
            let url = "/planItem/add/" + this.itemUrl;
            this.postRequest(url, this.planItem).then(resp => {
              if (resp) {
                this.$emit('close');
                // this.$parent.initProjects();
              }
            })
          }
        });
      }
    },
    handleClose() {
      this.countType = null;
      this.$emit('close');
    }
  }
}
</script>

<style scoped>

</style>
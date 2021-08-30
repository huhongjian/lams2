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
                <el-select style="width: 300px" v-model="planItem.itemType" clearable placeholder="请选择">
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
        <el-form v-if="planItem.itemType==1" :model="planItem" :rules="rules" ref="planItemForm">
          <el-row>
            <el-form-item label="设备名称:" prop="equipmentName" key="equipment">
              <el-input size="mini" prefix-icon="el-icon-edit"
                        v-model="planItem.equipmentName"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="用途:" prop="usage" key="equipment">
              <el-input size="mini" prefix-icon="el-icon-edit"
                        v-model="planItem.usage"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="单价（万元）:" prop="unitPrice" key="equipment">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.unitPrice"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="数量:" prop="amount" key="equipment">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.amount"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form v-if="planItem.itemType==2" :model="planItem" :rules="rules" ref="planItemForm">
          <el-row>
            <el-form-item label="材料用途:" prop="usage" key="material">
              <el-input size="mini" prefix-icon="el-icon-edit"
                        v-model="planItem.usage"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="材料名称:" prop="materialName" key="material">
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
              <el-form-item label="计价方式:" prop="count" key="material">
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
          <el-row v-if="countType==1">
            <el-col :span="6">
              <el-form-item label="单价（万元）:" prop="unitPrice" key="material">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.unitPrice"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="数量:" prop="amount" key="material">
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
          <el-row v-if="countType==2">
            <el-col :span="6">
              <el-form-item label="小计（万元）:" prop="subTotal" key="material">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.subTotal"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form v-if="planItem.itemType==3" :model="planItem" :rules="rules" ref="planItemForm">
          <el-row>
            <el-form-item label="加工内容:" prop="content" key="testing">
              <el-input size="mini" prefix-icon="el-icon-edit"
                        v-model="planItem.content"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="单价（万元）:" prop="unitPrice" key="testing">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.unitPrice"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="次数:" prop="times" key="testing">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.times"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form v-if="planItem.itemType==4" :model="planItem" :rules="rules" ref="planItemForm">
          <el-row>
            <el-form-item label="类别:" prop="subType">
              <el-select style="width: 200px" v-model="planItem.subType" clearable placeholder="请选择">
                <el-option
                    v-for="item in meetingFeeTypeList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-row>
          <el-form v-if="planItem.subType==1" :model="planItem" :rules="rules" ref="planItemForm">
            <el-row>
              <el-form-item label="会议内容:" prop="content" key="meeting">
                <el-input size="mini" prefix-icon="el-icon-edit"
                          v-model="planItem.content"></el-input>
              </el-form-item>
            </el-row>
            <el-row>
              <el-form-item label="与课题相关性:" prop="relation" key="meeting">
                <el-input size="mini" prefix-icon="el-icon-edit"
                          v-model="planItem.relation"></el-input>
              </el-form-item>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item label="伙食费（元/人/天）:" prop="meal" key="meeting">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="planItem.meal"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="其他费用（元/人/天）:" prop="otherCost" key="meeting">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="planItem.otherCost"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <el-form-item label="天数:" prop="days" key="meeting">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="planItem.days"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="参会人数:" prop="amount" key="meeting">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="planItem.amount"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="会议次数:" prop="meetingNum" key="meeting">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="planItem.meetingNum"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <el-form v-if="planItem.subType==2" :model="planItem" :rules="rules" ref="planItemForm">
            <el-row>
              <el-form-item label="调研地点:" prop="place" key="research">
                <el-input size="mini" prefix-icon="el-icon-edit"
                          v-model="planItem.place"></el-input>
              </el-form-item>
            </el-row>
            <el-row>
              <el-form-item label="调研目的:" prop="purpose" key="research">
                <el-input size="mini" prefix-icon="el-icon-edit"
                          v-model="planItem.purpose"></el-input>
              </el-form-item>
            </el-row>
            <el-row>
              <el-col :span="6">
                <el-form-item label="人员类别:" prop="personnelType" key="research">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="planItem.personnelType"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="人次:" prop="times" key="research">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="planItem.times"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="6">
                <el-form-item label="往返交通费（元）:" prop="transFee" key="research">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="planItem.transFee"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="住宿（元）:" prop="accommodationFee" key="research">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="planItem.accommodationFee"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="公杂费（元）:" prop="otherFee" key="research">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="planItem.otherFee"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="天数:" prop="days" key="research">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="planItem.days"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <el-form v-if="planItem.subType==3" :model="planItem" :rules="rules" ref="planItemForm">
            <el-row>
              <el-form-item label="会议名称:" prop="meetingName" key="academic">
                <el-input size="mini" prefix-icon="el-icon-edit"
                          v-model="planItem.meetingName"></el-input>
              </el-form-item>
            </el-row>
            <el-row>
              <el-col :span="6">
                <el-form-item label="人员类别:" prop="personnelType" key="academic">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="planItem.personnelType"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="人次:" prop="times" key="academic">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="planItem.times"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="年度:" prop="year" key="academic">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="planItem.year"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="会期（天）:" prop="days" key="academic">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="planItem.days"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-form-item label="计价方式:" prop="count" key="academic">
              <el-select style="width: 200px" v-model="countType" clearable placeholder="请选择">
                <el-option
                    v-for="item in countTypeList"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-row v-if="countType==1">
              <el-col :span="8">
                <el-form-item label="注册费（元/人）:" prop="registrationFee" key="academic">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="planItem.registrationFee"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="往返交通费（元/人/天）:" prop="transFee" key="academic">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="planItem.transFee"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="住宿及补助费（元/人/天）:" prop="accommodationFee" key="academic">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="planItem.accommodationFee"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row v-if="countType==2">
              <el-col :span="8">
                <el-form-item label="费用（万元/次）:" prop="subTotal" key="academic">
                  <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                            v-model="planItem.subTotal"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
          <el-form v-if="planItem.subType==4" :model="planItem" :rules="rules" ref="planItemForm">
            <el-row>
              <el-form-item label="合计（万元）:" prop="subTotal" key="transportation">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.subTotal"></el-input>
              </el-form-item>
            </el-row>
          </el-form>
        </el-form>
        <el-form v-if="planItem.itemType==5" :model="planItem" :rules="rules" ref="planItemForm">
          <el-row>
            <el-form-item label="类别:" prop="type" key="publish">
              <el-input size="mini" prefix-icon="el-icon-edit"
                        v-model="planItem.type"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="合计（万元）:" prop="subTotal" key="publish">
              <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                        v-model="planItem.subTotal"></el-input>
            </el-form-item>
          </el-row>
        </el-form>
        <el-form v-if="planItem.itemType==6" :model="planItem" :rules="rules" ref="planItemForm">
          <el-row>
            <el-form-item label="人员种类:" prop="personnelType" key="payment">
              <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                        v-model="planItem.personnelType"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="工作内容:" prop="content" key="payment">
              <el-input size="mini" prefix-icon="el-icon-edit"
                        v-model="planItem.content"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="人数:" prop="amount" key="payment">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.amount"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="标准（元/人/月）:" prop="standard" key="payment">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.standard"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="工作时间（月）:" prop="hours" key="payment">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.hours"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <el-form v-if="planItem.itemType==7" :model="planItem" :rules="rules" ref="planItemForm">
          <el-row>
            <el-form-item label="咨询内容:" prop="content" key="advisory">
              <el-input size="mini" prefix-icon="el-icon-edit"
                        v-model="planItem.content"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="人员类型:" prop="personnelType" key="advisory">
              <el-input size="mini" prefix-icon="el-icon-edit"
                        v-model="planItem.personnelType"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-col :span="6">
              <el-form-item label="标准（元/天）:" prop="standard" key="advisory">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.standard"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="人数:" prop="amount" key="advisory">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.amount"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="天数:" prop="days" key="advisory">
                <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                          v-model="planItem.days"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
          </el-row>
        </el-form>
        <el-form v-if="planItem.itemType==8" :model="planItem" :rules="rules" ref="planItemForm">
          <el-row>
            <el-form-item label="类别:" prop="type" key="other">
              <el-input size="mini" prefix-icon="el-icon-edit"
                        v-model="planItem.type"></el-input>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item label="合计（万元）:" prop="subTotal" key="other">
              <el-input size="mini" style="width: 150px" prefix-icon="el-icon-edit"
                        v-model="planItem.subTotal"></el-input>
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
  props: ['planItem', 'title', 'newPlanItemVisible'],
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
          name: '差旅/会议/国际合作与交流费'
        },
        {
          id: 5,
          name: '出版/文献/信息传播/知识产权事务费'
        },
        {
          id: 6,
          name: '劳务费'
        },
        {
          id: 7,
          name: '专家咨询费'
        },
        {
          id: 8,
          name: '其他支出'
        },
      ],
      countTypeList: [
        {
          id: '1',
          name: '按单价计算'
        },
        {
          id: '2',
          name: '按合计计算'
        },
      ],
      meetingFeeTypeList: [
        {
          id: 1,
          name: '会议费'
        },
        {
          id: 2,
          name: '课题调研差旅费'
        },
        {
          id: 3,
          name: '学术会议差旅费'
        },
        {
          id: 4,
          name: '北京市内交通费'
        },
      ],
      total: 0,
      page: 1,
      size: 10,
      itemUrl: "",
      rules: {
        itemType: [{required: true, message: '请选择预算科目', trigger: 'blur'}],
        equipmentName: [{required: true, message: '请输入名称', trigger: 'blur'}],
        materialName: [{required: true, message: '请输入名称', trigger: 'blur'}],
        usage: [{required: true, message: '请输入用途', trigger: 'blur'}],
        place: [{required: true, message: '请输入地点', trigger: 'blur'}],
        purpose: [{required: true, message: '请输入目的', trigger: 'blur'}],
        unitPrice: [{
          pattern: /^[+-]?(0|([1-9]\d*))(\.\d+)?$/,
          required: true,
          message: '请输入单价，参数必须为数字',
          trigger: 'blur'
        }],
        amount: [{pattern: /^[0-9]*$/, required: true, message: '请输入数量，参数必须为整数', trigger: 'blur'}],
        hours: [{pattern: /^[0-9]*$/, required: true, message: '请输入数量，参数必须为整数', trigger: 'blur'}],
        days: [{pattern: /^[0-9]*$/, required: true, message: '请输入数量，参数必须为整数', trigger: 'blur'}],
        meetingNum: [{pattern: /^[0-9]*$/, required: true, message: '请输入会议数量，参数必须为整数', trigger: 'blur'}],
        year: [{pattern: /^[0-9]*$/, required: true, message: '请输入年度，参数必须为整数', trigger: 'blur'}],
        specification: [{required: true, message: '请输入规格', trigger: 'blur'}],
        unit: [{required: true, message: '请输入单位', trigger: 'blur'}],
        subTotal: [{
          pattern: /^[+-]?(0|([1-9]\d*))(\.\d+)?$/,
          required: true,
          message: '请输入合计，参数必须为数字',
          trigger: 'blur'
        }],
        meal: [{pattern: /^[+-]?(0|([1-9]\d*))(\.\d+)?$/, required: true, message: '请输入伙食费，参数必须为数字', trigger: 'blur'}],
        otherCost: [{
          pattern: /^[+-]?(0|([1-9]\d*))(\.\d+)?$/,
          required: true,
          message: '请输入其他费用，参数必须为数字',
          trigger: 'blur'
        }],
        standard: [{
          pattern: /^[+-]?(0|([1-9]\d*))(\.\d+)?$/,
          required: true,
          message: '请输入标准，参数必须为数字',
          trigger: 'blur'
        }],
        otherFee: [{
          pattern: /^[+-]?(0|([1-9]\d*))(\.\d+)?$/,
          required: true,
          message: '请输入公杂费，参数必须为数字',
          trigger: 'blur'
        }],
        accommodationFee: [{
          pattern: /^[+-]?(0|([1-9]\d*))(\.\d+)?$/,
          required: true,
          message: '请输入住宿费，参数必须为数字',
          trigger: 'blur'
        }],
        registrationFee: [{
          pattern: /^[+-]?(0|([1-9]\d*))(\.\d+)?$/,
          required: true,
          message: '请输入注册费，参数必须为数字',
          trigger: 'blur'
        }],
        transFee: [{
          pattern: /^[+-]?(0|([1-9]\d*))(\.\d+)?$/,
          required: true,
          message: '请输入交通费，参数必须为数字',
          trigger: 'blur'
        }],
        content: [{required: true, message: '请输入内容', trigger: 'blur'}],
        relation: [{required: true, message: '请输入与课题相关性', trigger: 'blur'}],
        type: [{required: true, message: '请输入类型', trigger: 'blur'}],
        times: [{pattern: /^[0-9]*$/, required: true, message: '请输入次数，参数必须为整数', trigger: 'blur'}],
        personnelType: [{required: true, message: '请输入人员类型', trigger: 'blur'}],
        meetingName: [{required: true, message: '请输入会议名称', trigger: 'blur'}],
      },
    }
  },
  methods: {
    getItemUrl() {
      if (this.planItem.itemType == 1) {
        this.itemUrl = 'equipmentFee'
      } else if (this.planItem.itemType == 2) {
        this.itemUrl = 'materialFee'
      } else if (this.planItem.itemType == 3) {
        this.itemUrl = 'testingFee'
      } else if (this.planItem.itemType == 4 && this.planItem.subType == 1) {
        this.itemUrl = 'meetingFee'
      } else if (this.planItem.itemType == 4 && this.planItem.subType == 2) {
        this.itemUrl = 'researchFee'
      } else if (this.planItem.itemType == 4 && this.planItem.subType == 3) {
        this.itemUrl = 'academicFee'
      } else if (this.planItem.itemType == 4 && this.planItem.subType == 4) {
        this.itemUrl = 'transportationFee'
      } else if (this.planItem.itemType == 5) {
        this.itemUrl = 'publishFee'
      } else if (this.planItem.itemType == 6) {
        this.itemUrl = 'payment'
      } else if (this.planItem.itemType == 7) {
        this.itemUrl = 'advisory'
      } else if (this.planItem.itemType == 8) {
        this.itemUrl = 'otherFee'
      }
    },
    doAddPlanItem() {
      this.$refs['planItemForm'].validate(valid => {
        if (valid) {
          this.getItemUrl();
          let url = "/planItem/" + this.itemUrl + "/add/";
          this.postRequest(url, this.planItem).then(resp => {
            if (resp) {
              this.$emit('handlePlanItemIds', this.planItem.itemType, this.planItem.subType, resp.obj);
            }
          })
        }
      });
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
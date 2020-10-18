<template>
  <div>
    <div width="100%">
      <el-card class="box-card" style="background-color: #fff143">
        <div slot="header" class="clearfix">
          <span class="box-title">总资产数</span>
        </div>
        <div class="box-value">
          {{ headTableData.total }}
        </div>
      </el-card>
      <el-card class="box-card" style="background-color: #44cef6">
        <div slot="header" class="clearfix">
          <span class="box-title">使用中数量</span>
        </div>
        <div class="box-value">
          {{ headTableData.inUse }}
        </div>
      </el-card>
      <el-card class="box-card" style="background-color: #9ed048">
        <div slot="header" class="clearfix">
          <span class="box-title">闲置数量</span>
        </div>
        <div class="box-value">
          {{ headTableData.free }}
        </div>
      </el-card>
      <el-card class="box-card" style="background-color: #cca4e3">
        <div slot="header" class="clearfix">
          <span class="box-title">维修中数量</span>
        </div>
        <div class="box-value">
          {{ headTableData.inRepair }}
        </div>
      </el-card>
      <el-card class="box-card" style="background-color: #f9906f">
        <div slot="header" class="clearfix">
          <span class="box-title">资产总金额（元）</span>
        </div>
        <div class="box-value">
          {{ headTableData.money }}
        </div>
      </el-card>
      <el-card class="box-card" style="background-color: #c2ccd0">
        <div slot="header" class="clearfix">
          <span class="box-title">报废数量</span>
        </div>
        <div class="box-value">
          {{ headTableData.cleaned }}
        </div>
      </el-card>
    </div>
    <el-card class="chart-card" style="width: 100%">
      <div slot="header" class="clearfix">
        <span>资产概况</span>
        <el-select v-model="value1" style="float: right; padding: 3px 0" multiple placeholder="请选择">
          <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
          </el-option>
        </el-select>
      </div>
      <el-table
          :data="tableData"
          style="width: 50%; float:left">
        <el-table-column
            prop="date"
            label="日期"
            width="180">
        </el-table-column>
        <el-table-column
            prop="name"
            label="姓名"
            width="180">
        </el-table-column>
        <el-table-column
            prop="address"
            label="地址">
        </el-table-column>
      </el-table>
      <ve-ring :data="chartData" style="width: 50%; float:right"></ve-ring>
    </el-card>
    <div style="width: 100%">
      <el-card class="chart-card" style="width: 49%; float: left">
        <div slot="header" class="clearfix">
          <span>资产增长趋势</span>
          <el-date-picker
              style="float: right; padding: 3px 0"
              v-model="value2"
              type="monthrange"
              align="right"
              unlink-panels
              range-separator="至"
              start-placeholder="开始月份"
              end-placeholder="结束月份"
              :picker-options="pickerOptions">
          </el-date-picker>
        </div>
        <ve-line :data="chartData"></ve-line>
      </el-card>
      <el-card class="chart-card" style="width: 49%; float: right">
        <div slot="header" class="clearfix">
          <span>资产分类统计</span>
        </div>
        <ve-bar :data="chartData"></ve-bar>
      </el-card>
    </div>
  </div>
</template>

<script>
import VeLine from 'v-charts/lib/line';
import VeRing from 'v-charts/lib/ring';
import VeBar from 'v-charts/lib/bar';

export default {
  name: "AssetDashBoard",
  mounted() {
    this.initHeadTableData();
  },
  data() {
    return {
      pickerOptions: {
        shortcuts: [{
          text: '本月',
          onClick(picker) {
            picker.$emit('pick', [new Date(), new Date()]);
          }
        }, {
          text: '今年至今',
          onClick(picker) {
            const end = new Date();
            const start = new Date(new Date().getFullYear(), 0);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近六个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setMonth(start.getMonth() - 6);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
      value1: [],
      value2: '',
      options: [
        {
          value: '选项1',
          label: '黄金糕'
        },
        {
          value: '选项2',
          label: '双皮奶'
        },
        {
          value: '选项3',
          label: '蚵仔煎'
        },
        {
          value: '选项4',
          label: '龙须面'
        },
        {
          value: '选项5',
          label: '北京烤鸭'
        }
      ],
      value: '',
      headTableData: {
        total: '',
        inUse: '',
        free: '',
        inRepair: '',
        money: '',
        cleaned: ''
      },
      tableData: [
        {
          date: '2016-05-02',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1518 弄'
        }, {
          date: '2016-05-04',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1517 弄'
        }, {
          date: '2016-05-01',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1519 弄'
        }, {
          date: '2016-05-03',
          name: '王小虎',
          address: '上海市普陀区金沙江路 1516 弄'
        }],
      chartData: {
        columns: ['日期', '销售额'],
        rows: [
          {'日期': '1月1日', '销售额': 123},
          {'日期': '1月2日', '销售额': 1223},
          {'日期': '1月3日', '销售额': 2123},
          {'日期': '1月4日', '销售额': 4123},
          {'日期': '1月5日', '销售额': 3123},
          {'日期': '1月6日', '销售额': 7123}
        ]
      }
    }
  },
  methods: {
    initHeadTableData() {
      let url = '/asset/dashboard/get'
      this.getRequest(url).then(resp => {
        if (resp) {
          this.headTableData = resp.obj;
        }
      });
    }
  },
  components: {VeLine, VeRing, VeBar}
}
</script>

<style scoped>
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}

.box-card {
  width: 16.5%;
  float: left;
}

.box-title {
  font-size: 14px;
  font-family: 微软雅黑;
  font-weight: bold;
}

.box-value {
  font-size: 14px;
  font-family: 微软雅黑;
  font-weight: bold;
}
</style>
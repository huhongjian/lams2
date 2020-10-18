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
        <el-select v-model="typeList"
                   @change="initData()"
                   style="float: right; padding: 3px 0" multiple placeholder="请选择资产类型">
          <el-option
              v-for="item in types"
              :key="item.id"
              :label="item.name"
              :value="item.name">
          </el-option>
        </el-select>
      </div>
      <el-table
          :data="tableData"
          :row-style="{height:'60px'}"
          style="font-size: 15px; width: 50%; float:left">
        <el-table-column
            prop="statusName"
            label="类型"
            width="180">
        </el-table-column>
        <el-table-column
            prop="count"
            label="数量"
            width="180">
        </el-table-column>
        <el-table-column
            prop="money"
            label="金额（元）">
        </el-table-column>
      </el-table>
      <ve-ring :data="ringData" style="width: 50%; float:right"></ve-ring>
    </el-card>
    <div style="width: 100%">
      <el-card class="chart-card" style="width: 49%; float: left">
        <div slot="header" class="clearfix">
          <span>资产增长趋势</span>
          <el-date-picker
              @change="initLineData"
              style="float: right; padding: 3px 0"
              v-model="monthScope"
              type="daterange"
              align="right"
              unlink-panels
              value-format="yyyy-MM-dd"
              range-separator="至"
              start-placeholder="开始月份"
              end-placeholder="结束月份">
          </el-date-picker>
        </div>
        <ve-line :data="lineData"></ve-line>
      </el-card>
      <el-card class="chart-card" style="width: 49%; float: right">
        <div slot="header" class="clearfix">
          <span>资产分类统计</span>
        </div>
        <ve-bar :data="typeChartData"></ve-bar>
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
    this.initData();
    this.initTypeChartData();
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
      typeList: [],
      monthScope: '',
      types: [
        {
          id: 1,
          name: '手机'
        }, {
          id: 2,
          name: '主机'
        }, {
          id: 3,
          name: '交换机'
        }, {
          id: 4,
          name: '测距仪'
        }],
      headTableData: {
        total: '',
        inUse: '',
        free: '',
        inRepair: '',
        money: '',
        cleaned: ''
      },
      tableData: [],
      ringData: {
        columns: ['statusName', 'count'],
        rows: []
      },
      typeChartData: {
        columns: ['type', 'count'],
        rows: []
      },
      lineData: {
        columns: ['date', 'total', 'inUse', 'free', 'inRepair', 'cleaned'],
        rows: []
      }
    }
  },
  methods: {
    initData() {
      this.initRingData();
      this.initTableData();
      this.initLineData();
    },
    initHeadTableData() {
      let url = '/asset/dashboard/get/headTable'
      this.getRequest(url).then(resp => {
        if (resp) {
          this.headTableData = resp.obj;
        }
      });
    },
    initRingData() {
      let url = '/asset/dashboard/get/ringData'
      this.postRequest(url, this.typeList).then(resp => {
        if (resp) {
          this.ringData.rows = resp.obj;
        }
      });
    },
    initTableData() {
      let url = '/asset/dashboard/get/tableData'
      this.postRequest(url, this.typeList).then(resp => {
        if (resp) {
          this.tableData = resp.obj;
        }
      });
    },
    initTypeChartData() {
      let url = '/asset/dashboard/get/typeChartData'
      this.getRequest(url).then(resp => {
        if (resp) {
          this.typeChartData.rows = resp.obj;
        }
      });
    },
    initLineData() {
      let url = '/asset/dashboard/get/lineData';
      if (this.monthScope) {
        url += '/?monthScope=' + this.monthScope;
      }
      this.getRequest(url).then(resp => {
        if (resp) {
          this.lineData.rows = resp.obj;
        }
      });
    }
  },
  components: {VeLine, VeRing, VeBar}
}
</script>

<style scoped>

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
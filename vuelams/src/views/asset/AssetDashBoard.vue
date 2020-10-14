<template>
  <div>
    <div width="100%">
      <el-card class="box-card" style="background-color: #fff143">
        <div slot="header" class="clearfix">
          <span>总资产数</span>
        </div>
        <div>
          1
        </div>
      </el-card>
      <el-card class="box-card" style="background-color: #44cef6">
        <div slot="header" class="clearfix">
          <span>使用中数量</span>
        </div>
        <div>
          2
        </div>
      </el-card>
      <el-card class="box-card" style="background-color: #9ed048">
        <div slot="header" class="clearfix">
          <span>闲置数量</span>
        </div>
        <div>
          3
        </div>
      </el-card>
      <el-card class="box-card" style="background-color: #cca4e3">
        <div slot="header" class="clearfix">
          <span>维修中数量</span>
        </div>
        <div>
          4
        </div>
      </el-card>
      <el-card class="box-card" style="background-color: #f9906f">
        <div slot="header" class="clearfix">
          <span>资产总金额</span>
        </div>
        <div>
          5
        </div>
      </el-card>
      <el-card class="box-card" style="background-color: #c2ccd0">
        <div slot="header" class="clearfix">
          <span>报废数量</span>
        </div>
        <div>
          6
        </div>
      </el-card>
    </div>
    <el-card class="chart-card" style="width: 100%">
      <div slot="header" class="clearfix">
        <span>卡片名称</span>
        <el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>
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
          <span>卡片名称</span>
          <el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>
          <div>
            <ve-line :data="chartData"></ve-line>
          </div>
        </div>
      </el-card>
      <el-card class="chart-card" style="width: 49%; float: right">
        <div slot="header" class="clearfix">
          <span>卡片名称</span>
          <el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>
          <div>
            <ve-bar :data="chartData"></ve-bar>
          </div>
        </div>
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
  data() {
    return {
      tableData: [{
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
  width: 15%;
  float: left;
}
</style>
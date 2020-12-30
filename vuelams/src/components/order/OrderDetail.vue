<template>
  <div>
    <el-dialog
        :title="title"
        :visible.sync="dialogVisible7"
        :before-close="handleClose"
        width="80%">
      <div>
        <el-form :model="order">
          <template v-if="order.user">
            <el-row>
              <el-col :span="6">
                <el-form-item label="申请人:" prop="applicant">
                  {{ order.user.name }}
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="申请人电话:" prop="applicantPhone">
                  {{ order.user.phone }}
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="申请人邮箱:" prop="applicantEmail">
                  {{ order.user.username }}
                </el-form-item>
              </el-col>
            </el-row>
          </template>
          <el-row>
            <el-col :span="10">
              <el-form-item v-show="order.expireTime" label="预计归还时间:" prop="expireTime">
                {{ order.expireTime }}
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="10">
              <el-form-item v-show="order.reason&&order.reason!=''" label="理由:" prop="reason">
                {{ order.reason }}
              </el-form-item>
            </el-col>
          </el-row>
          <el-table
              :data="order.assetList"
              stripe
              border
              element-loading-text="正在加载..."
              element-loading-spinner="el-icon-loading"
              element-loading-background="rgba(0, 0, 0, 0.8)"
              style="width: 100%">
            <el-table-column
                fixed
                label="资产编号"
                align="left"
                width="100">
              <template slot-scope="scope">
                <el-button size="mini" @click="showDetailView(scope.row)">{{ scope.row.id }}</el-button>
              </template>
            </el-table-column>
            <el-table-column
                fixed
                prop="assetName"
                :show-overflow-tooltip="true"
                align="left"
                label="资产名称">
            </el-table-column>
            <el-table-column
                prop="statusName"
                label="状态"
                width="200">
              <template slot-scope="scope">
            <span style="color: #00e079; font-weight: bold"
                  v-if="scope.row.status=='2'">{{ scope.row.statusName }}</span>
                <span style="color: #ff4777; font-weight: bold" v-else-if="scope.row.status=='3'">{{
                    scope.row.statusName
                  }}</span>
                <span style="color: #c0c0c0;"
                      v-else-if="scope.row.status=='4'">{{ scope.row.statusName }}</span>
                <span v-else>{{ scope.row.statusName }}</span>
              </template>
            </el-table-column>
            <el-table-column
                prop="type"
                align="left"
                label="类型"
                width="200">
            </el-table-column>
            <el-table-column
                prop="brand"
                label="品牌"
                align="left"
                width="200">
            </el-table-column>
            <el-table-column
                prop="price"
                label="价格（元）"
                align="left"
                width="200">
            </el-table-column>
          </el-table>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
    <template v-for="op in operateList">
      <el-button type="primary" @click="checkAndHandle(op.operateType)">{{ op.operate }}</el-button>
    </template>
        <el-button @click="$emit('close')">取 消</el-button>
  </span>
    </el-dialog>
    <AssetDetail v-on:close="dialogVisible2 = false" :dialogVisible2="dialogVisible2" :asset="asset"
                 :urlList="urlList" :title="title"></AssetDetail>
  </div>
</template>
<script>
import AssetDetail from "@/components/asset/AssetDetail";

export default {
  name: "OrderDetail",
  props: ['order', 'title', 'dialogVisible7', 'operateList'],
  data() {
    return {
      dialogVisible2: false,
      asset: {
        id: "",
        brand: "",
        type: "",
        price: "",
        fileList: [],
        adv: {},
        remark: ""
      },
      name: '',
      taskHandleDto: {
        id: null,
        operateType: null,
        candidateUser: null
      },
      urlList: []
    }
  },
  components: {
    AssetDetail
  },
  methods: {
    handle() {
      this.taskHandleDto.id = this.order.id;
      this.postRequest("/order/task/handleTask", this.taskHandleDto).then(resp => {
        if (resp) {
          this.visible = false;
          this.$emit('close');
          this.$parent.initOrders();
        }
      });
    },
    cancel() {
      this.taskHandleDto.id = this.order.id;
      this.postRequest("/order/task/cancel", this.taskHandleDto).then(resp => {
        if (resp) {
          this.visible = false;
          this.$emit('close');
          this.$parent.initOrders();
        }
      })
    },
    checkAndHandle(data) {
      this.taskHandleDto.operateType = data;
      if (data == '7') {
        this.cancel();
      } else {
        this.handle();
      }
    },
    handleClose() {
      this.$emit('close');
    },
    showDetailView(data) {
      this.title = '资产详情';
      this.asset = data;
      if (this.asset && this.asset.fileList) {
        this.urlList = [];
        for (let i = 0; i < this.asset.fileList.length; i++) {
          this.urlList.push(this.asset.fileList[i].url);
        }
      } else {
        this.urlList = null;
      }
      this.dialogVisible2 = true;
    }
  }
}
</script>

<style scoped>

</style>
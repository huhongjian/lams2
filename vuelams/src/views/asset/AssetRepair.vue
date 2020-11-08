<template>
  <div>
    <div>
      <div style="display: flex;justify-content: space-between">
        <div style="display: inline-flex;margin-left: 8px"></div>
        <div>
          <el-input placeholder="请输入资产编号进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
                    clearable
                    @clear="initAssets"
                    style="width: 350px;margin-right: 10px" v-model="keyword"
                    @keydown.enter.native="initAssets" :disabled="showAdvanceSearchView"></el-input>
          <el-button icon="el-icon-search" type="primary" @click="initAssets" :disabled="showAdvanceSearchView">
            搜索
          </el-button>
          <el-button type="primary" @click="showAdvanceSearchView = !showAdvanceSearchView">
            <i :class="showAdvanceSearchView?'fa fa-angle-double-up':'fa fa-angle-double-down'"
               aria-hidden="true"></i>
            高级搜索
          </el-button>
        </div>
      </div>
      <transition name="slide-fade">
        <div v-show="showAdvanceSearchView"
             style="border: 1px solid #759ad1;border-radius: 5px;box-sizing: border-box;padding: 5px;margin: 10px 0px;">
          <el-row>
            <el-col :span="6">
              类型:
              <el-select v-model="searchValue.type"
                         clearable
                         placeholder="类型"
                         size="mini"
                         style="width: 130px;">
                <el-option
                    v-for="item in types"
                    :key="item.id"
                    :label="item.name"
                    :value="item.name">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="5">
              品牌:
              <el-input size="mini" style="width: 100px" prefix-icon="el-icon-edit"
                        clearable
                        v-model="searchValue.brand"></el-input>
            </el-col>
            <el-col :span="5">
              状态:
              <el-select v-model="searchValue.status" clearable placeholder="状态" size="mini" style="width: 130px;">
                <el-option
                    v-for="item in statuses"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>
          </el-row>
          <el-row style="margin-top: 10px">
            <el-col :span="6">
              价格:
              <el-input size="mini" style="width: 100px" prefix-icon="el-icon-edit"
                        clearable
                        v-model="searchValue.priceLow"></el-input>
              至:
              <el-input size="mini" style="width: 100px" prefix-icon="el-icon-edit"
                        clearable
                        v-model="searchValue.priceHigh"></el-input>
            </el-col>
            <el-col :span="9">
              入库时间:
              <el-date-picker
                  v-model="searchValue.dateScope"
                  type="daterange"
                  size="mini"
                  unlink-panels
                  value-format="yyyy-MM-dd"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
            <el-col :span="5" :offset="4">
              <el-button size="mini" @click="clearSearchValue">重置</el-button>
              <el-button size="mini" @click="showAdvanceSearchView = false">取消</el-button>
              <el-button size="mini" icon="el-icon-search" type="primary" @click="initAssetsAdv">搜索</el-button>
            </el-col>
          </el-row>
        </div>
      </transition>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="assets"
          stripe
          border
          v-loading="loading"
          element-loading-text="正在加载..."
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.8)"
          style="width: 100%">
        <el-table-column
            type="selection"
            width="55">
        </el-table-column>
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
            prop="statusName"
            label="状态">
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
            width="150">
        </el-table-column>
        <el-table-column
            fixed
            prop="assetName"
            :show-overflow-tooltip="true"
            align="left"
            label="资产名称"
            width="80">
        </el-table-column>
        <el-table-column
            prop="brand"
            label="品牌"
            align="left"
            width="150">
        </el-table-column>
        <el-table-column
            prop="price"
            label="价格（元）"
            align="left"
            width="150">
        </el-table-column>
        <el-table-column
            prop="boughtTime"
            width="100"
            align="left"
            label="购入时间">
        </el-table-column>
        <el-table-column
            prop="readyDate"
            width="200"
            align="left"
            label="入库时间">
        </el-table-column>
        <el-table-column
            prop="updateTime"
            width="200"
            align="left"
            label="更新时间">
        </el-table-column>
        <el-table-column
            fixed="right"
            width="150"
            label="操作">
          <template slot-scope="scope">
            <el-button v-show="scope.row.status=='2'" @click="changeStatus(scope.row,'3')">
              报修
            </el-button>
            <el-button v-show="scope.row.status=='3'" type="success" @click="changeStatus(scope.row,'2')">
              恢复
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="display: flex;justify-content: flex-end">
        <el-pagination
            background
            @current-change="currentChange"
            @size-change="sizeChange"
            layout="sizes, prev, pager, next, jumper, ->, total, slot"
            :total="total">
        </el-pagination>
      </div>
      <AssetDetail v-on:close="dialogVisible2 = false" :dialogVisible2="dialogVisible2" :asset="asset"
                   :urlList="urlList" :title="title"></AssetDetail>
    </div>
  </div>
</template>

<script>
import AssetDetail from "@/components/asset/AssetDetail";

export default {
  name: "AssetRepair",
  mounted() {
    this.initAssets();
    this.initTypes();
  },
  data() {
    return {
      showAdvanceSearchView: false,
      searchValue: {
        type: null,
        brand: null,
        status: null,
        priceLow: null,
        priceHigh: null,
        dateScope: null
      },
      loading: false,
      total: 0,
      page: 1,
      keyword: '',
      size: 10,
      dialogVisible2: false,
      title: "",
      assets: [],
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
        boughtTime: ""
      },
      rules: {
        brand: [{required: true, message: '请输入品牌', trigger: 'blur'}],
        price: [{required: true, message: '请输入价格', trigger: 'blur'}]
      },
      type: "",
      types: [],
      statuses: [
        {
          id: 2,
          name: "正常"
        },
        {
          id: 3,
          name: "故障"
        },
        {
          id: 4,
          name: "报废"
        }
      ],
      // 资产图片url列表，用于详情页面
      urlList: []
    }
  },
  components: {
    AssetDetail
  },
  methods: {
    sizeChange(currentSize) {
      this.size = currentSize;
      if (this.type && this.type == 'advanced') {
        this.initAssetsAdv();
      } else {
        this.initAssets();
      }
    },
    currentChange(currentPage) {
      this.page = currentPage;
      if (this.type && this.type == 'advanced') {
        this.initAssetsAdv();
      } else {
        this.initAssets();
      }
    },
    changeStatus(data, val) {
      data.status = val;
      this.putRequest("/asset/changeStatus", data).then(resp => {
        this.initAssets();
      });
    },
    showDetailView(data) {
      this.title = '资产信息详情';
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
    },
    initAssets() {
      this.type = '';
      this.loading = true;
      let url = '/asset/get/?page=' + this.page + '&size=' + this.size + "&aid=" + this.keyword;
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.assets = resp.data;
          this.total = resp.total;
        }
      });
    },
    initAssetsAdv() {
      this.type = 'advanced'
      this.loading = true;
      let url = '/asset/get/?category=1&page=' + this.page + '&size=' + this.size;
      if (this.searchValue.type) {
        url += '&type=' + this.searchValue.type;
      }
      if (this.searchValue.brand) {
        url += '&brand=' + this.searchValue.brand;
      }
      if (this.searchValue.status) {
        url += '&status=' + this.searchValue.status;
      }
      if (this.searchValue.userEmail) {
        url += '&userEmail=' + this.searchValue.userEmail;
      }
      if (this.searchValue.priceLow) {
        url += '&priceLow=' + this.searchValue.priceLow;
      }
      if (this.searchValue.priceHigh) {
        url += '&priceHigh=' + this.searchValue.priceHigh;
      }
      if (this.searchValue.dateScope) {
        url += '&dateScope=' + this.searchValue.dateScope;
      }
      this.getRequest(url).then(resp => {
        this.loading = false;
        if (resp) {
          this.assets = resp.data;
          this.total = resp.total;
        }
      });
    },
    clearSearchValue() {
      this.searchValue = {
        type: null,
        brand: null,
        status: null,
        priceLow: null,
        priceHigh: null,
        dateScope: null
      }
    },
    initTypes() {
      let url = '/asset/types/get';
      this.getRequest(url).then(resp => {
        if (resp) {
          this.types = resp.obj;
        }
      });
    },
  }
}
</script>

<style scoped>

</style>
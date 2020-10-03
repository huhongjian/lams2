<template>
  <div>
    <div>
      <div style="display: flex;justify-content: space-between">
        <div>
          <p style="display: inline-flex;margin-left: 8px"></p>
        </div>
        <div>
          <el-input placeholder="请输入资产名称进行搜索，可以直接回车搜索..." prefix-icon="el-icon-search"
                    clearable
                    @clear="initOrders"
                    style="width: 350px;margin-right: 10px" v-model="keyword"
                    @keydown.enter.native="initOrders" :disabled="showAdvanceSearchView"></el-input>
          <el-button icon="el-icon-search" type="primary" @click="initOrders"
                     :disabled="showAdvanceSearchView">
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
             style="border: 1px solid #409eff;border-radius: 5px;box-sizing: border-box;padding: 5px;margin: 10px 0px;">
          <el-row>
            <el-col :span="5">
              类型:
              <el-select v-model="searchValue.type" placeholder="类型" size="mini"
                         style="width: 130px;">
                <el-option
                    v-for="item in types"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              品牌:
              <el-input size="mini" style="width: 100px" prefix-icon="el-icon-edit"
                        v-model="searchValue.brand"></el-input>
            </el-col>
            <el-col :span="4">
              状态:
              <el-select v-model="searchValue.status" placeholder="状态" size="mini" style="width: 130px;">
                <el-option
                    v-for="item in statuses"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="4">
              申请人:
              <el-input size="mini" style="width: 100px" prefix-icon="el-icon-edit"
                        v-model="searchValue.applicant"></el-input>
            </el-col>
            <el-col :span="4">
              申请人邮箱:
              <el-input size="mini" style="width: 100px" prefix-icon="el-icon-edit"
                        v-model="searchValue.applicantEmail"></el-input>
            </el-col>
          </el-row>
          <el-row style="margin-top: 10px">
            <el-col :span="6">
              价格:
              <el-input size="mini" style="width: 100px" prefix-icon="el-icon-edit"
                        v-model="searchValue.priceLow"></el-input>
              至:
              <el-input size="mini" style="width: 100px" prefix-icon="el-icon-edit"
                        v-model="searchValue.priceHigh"></el-input>
            </el-col>
            <el-col :span="9">
              入职日期:
              <el-date-picker
                  v-model="searchValue.beginDateScope"
                  type="daterange"
                  size="mini"
                  unlink-panels
                  value-format="yyyy-MM-dd"
                  range-separator="至"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期">
              </el-date-picker>
            </el-col>
            <el-col :span="5" :offset="3">
              <el-button size="mini">取消</el-button>
              <el-button size="mini" icon="el-icon-search" type="primary" @click="initEmps('advanced')">搜索</el-button>
            </el-col>
          </el-row>
        </div>
      </transition>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="orders"
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
            label="ID"
            align="left"
            width="90">
          <template slot-scope="scope">
            <el-button size="mini" @click="getCandidateBranchInfo(scope.row)">{{
                scope.row.id
              }}
            </el-button>
          </template>
        </el-table-column>
        <el-table-column
            prop="categoryName"
            align="left"
            label="流程"
            width="90">
        </el-table-column>
        <el-table-column
            prop="status"
            label="状态"
            width="90">
        </el-table-column>
        <el-table-column
            prop="duration"
            label="用时"
            width="90">
        </el-table-column>
        <el-table-column
            prop="reason"
            label="理由">
        </el-table-column>
        <el-table-column
            prop="applicant"
            width="95"
            align="left"
            label="申请人">
        </el-table-column>
        <el-table-column
            prop="applicantEmail"
            width="150"
            align="left"
            label="申请人邮箱">
        </el-table-column>
        <el-table-column
            prop="applicantPhone"
            width="100"
            label="申请人电话">
        </el-table-column>
        <el-table-column
            prop="createTime"
            align="left"
            label="申请时间"
            width="180">
        </el-table-column>
        <el-table-column
            prop="updateTime"
            align="left"
            label="更新时间"
            width="180">
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
    </div>
  </div>
</template>

<script>

export default {
  name: "Mine",
  props: ['orders', 'showAdvanceSearchView', 'rules', 'total', 'page', 'size', 'loading'],
  data() {
    return {
      keyword: '',
      searchValue: {
        type: null,
        nationId: null,
        jobLevelId: null,
        posId: null,
        engageForm: null,
        departmentId: null,
        beginDateScope: null
      },
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
      statuses: [
        {
          id: 0,
          name: "申请采购"
        },
        {
          id: 0,
          name: "审批通过"
        },
        {
          id: 0,
          name: "审批未通过"
        }
      ]
    }
  },
  methods: {
    sizeChange(currentSize) {
      this.$emit('currentSize', currentSize)
    },
    currentChange(currentPage) {
      this.$emit('currentPage', currentPage)
    },
    initOrders(type) {
      this.$parent.initOrders(type);
    },
    getCandidateBranchInfo(data) {
      this.$parent.getCandidateBranchInfo(data);
    }
  }
}
</script>

<style scoped>

</style>
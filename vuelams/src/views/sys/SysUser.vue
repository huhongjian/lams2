<template>
  <div>
    <div style="margin-top: 10px;display: flex;justify-content: center">
      <el-input v-model="keywords" placeholder="通过用户名搜索用户..." prefix-icon="el-icon-search"
                style="width: 400px;margin-right: 10px" @keydown.enter.native="doSearch"></el-input>
      <el-button icon="el-icon-search" type="primary" @click="doSearch">搜索</el-button>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="users"
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
            prop="id"
            label="ID"
            align="left"
            width="50">
        </el-table-column>
        <el-table-column
            prop="name"
            align="left"
            label="姓名"
            width="100">
        </el-table-column>
        <el-table-column
            prop="phone"
            label="电话"
            width="150">
        </el-table-column>
        <el-table-column
            prop="username"
            label="邮箱"
            width="150">
        </el-table-column>
        <el-table-column
            label="角色">
          <template slot-scope="scope">
            <el-tag type="success" style="margin-right: 4px" v-for="(role,indexj) in scope.row.roles"
                    :key="indexj">{{ role.nameZh }}
            </el-tag>
            <el-popover
                placement="right"
                title="角色列表"
                @show="showPop(scope.row)"
                @hide="hidePop(scope.row)"
                width="200"
                trigger="click">
              <el-select v-model="selectedRoles" multiple placeholder="请选择">
                <el-option
                    v-for="(r,indexk) in allroles"
                    :key="indexk"
                    :label="r.nameZh"
                    :value="r.id">
                </el-option>
              </el-select>
              <el-button slot="reference" icon="el-icon-more" type="text"></el-button>
            </el-popover>
          </template>
        </el-table-column>
        <el-table-column
            prop="enabled"
            width="160"
            align="left"
            label="状态">
          <template slot-scope="scope">
            <el-switch
                v-model="scope.row.enabled"
                active-text="启用"
                @change="enabledChange(scope.row)"
                active-color="#13ce66"
                inactive-color="#ff4949"
                inactive-text="禁用">
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column
            prop="remark"
            width="200"
            align="left"
            label="备注">
        </el-table-column>
        <el-table-column
            fixed="right"
            width="60"
            label="操作">
          <template slot-scope="scope">
            <el-button @click="deleteUser(scope.row)" style="padding: 3px" size="mini" type="danger">删除</el-button>
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
    </div>
  </div>
</template>

<script>
export default {
  name: "SysUser",
  data() {
    return {
      loading: false,
      total: 0,
      page: 1,
      keywords: '',
      size: 10,
      users: [],
      selectedRoles: [],
      allroles: []
    }
  },
  mounted() {
    this.initUsers();
  },
  methods: {
    deleteUser(user) {
      this.$confirm('此操作将永久删除【' + user.name + '】, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest("/system/user/" + user.id).then(resp => {
          if (resp) {
            this.initUsers();
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    doSearch() {
      this.initUsers();
    },
    hidePop(user) {
      let roles = [];
      Object.assign(roles, user.roles);
      let flag = false;
      if (roles.length != this.selectedRoles.length) {
        flag = true;
      } else {
        for (let i = 0; i < roles.length; i++) {
          let role = roles[i];
          for (let j = 0; j < this.selectedRoles.length; j++) {
            let sr = this.selectedRoles[j];
            if (role.id == sr) {
              roles.splice(i, 1);
              i--;
              break;
            }
          }
        }
        if (roles.length != 0) {
          flag = true;
        }
      }
      if (flag) {
        let url = '/system/user/role?uid=' + user.id;
        this.selectedRoles.forEach(sr => {
          url += '&rids=' + sr;
        });
        this.putRequest(url).then(resp => {
          if (resp) {
            this.initUsers();
          }
        });
      }
    },
    showPop(user) {
      this.initAllRoles();
      let roles = user.roles;
      this.selectedRoles = [];
      roles.forEach(r => {
        this.selectedRoles.push(r.id);
      })
    },
    enabledChange(user) {
      delete user.roles;
      this.putRequest("/system/user/info", user).then(resp => {
        if (resp) {
          this.initUsers();
        }
      })
    },
    initAllRoles() {
      this.getRequest("/system/user/roles").then(resp => {
        if (resp) {
          this.allroles = resp;
        }
      })
    },
    sizeChange(currentSize) {
      this.size = currentSize;
      this.initUsers();
    },
    currentChange(currentPage) {
      this.page = currentPage;
      this.initUsers();
    },
    initUsers() {
      this.loading = true;
      this.getRequest("/system/user/?keywords=" + this.keywords + '&page=' + this.page + '&size=' + this.size).then(resp => {
        if (resp) {
          this.loading = false;
          this.users = resp.data;
          this.total = resp.total;
        }
      })
    }
  }
}
</script>

<style>
</style>
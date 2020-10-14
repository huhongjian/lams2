<template>
  <div>
    <el-container>
      <el-header class="homeHeader">
        <div class="title">实验室资产管理系统</div>
        <div>
          <el-dropdown class="userInfo" @command="commandHandler">
            <span class="el-dropdown-link">
              {{ user.name }}
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="userinfo">个人中心</el-dropdown-item>
              <el-dropdown-item command="setting">设置</el-dropdown-item>
              <el-dropdown-item command="logout" divided>注销登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <el-menu router unique-opened>
            <el-submenu :index="index+''" v-for="(item,index) in routes" v-if="!item.hidden" :key="index">
              <template slot="title">
                <i style="color: #759ad1;margin-right: 5px" :class="item.iconCls"></i>
                <span>{{ item.name }}</span>
              </template>
              <el-menu-item :index="child.path" v-for="(child,indexj) in item.children" :key="indexj">
                {{ child.name }}
              </el-menu-item>
            </el-submenu>
          </el-menu>
        </el-aside>
        <el-main>
          <el-breadcrumb separator-class="el-icon-arrow-right" v-if="this.$router.currentRoute.path!='/home'">
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ this.$router.currentRoute.name }}</el-breadcrumb-item>
          </el-breadcrumb>
          <div v-if="this.$router.currentRoute.path=='/home'">
            <AssetDashBoard></AssetDashBoard>
          </div>
          <router-view class="homeRouterView"/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import AssetDashBoard from "@/views/asset/AssetDashBoard";

export default {
  name: "Home",
  data() {
    return {
      // user: JSON.parse(window.sessionStorage.getItem("user"))
    }
  },
  components: {
    AssetDashBoard
  },
  computed: {
    routes() {
      return this.$store.state.routes;
    },
    user() {
      return this.$store.state.currentHr;
    }
  },
  methods: {
    goChat() {
      this.$router.push("/chat");
    },
    commandHandler(cmd) {
      if (cmd == 'logout') {
        this.$confirm('此操作将注销登录, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.getRequest("/logout");
          window.sessionStorage.removeItem("user")
          this.$store.commit('initRoutes', []);
          this.$router.replace("/");
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消操作'
          });
        });
      } else if (cmd == 'userinfo') {
        this.$router.push('/hrinfo');
      }
    }
  }
}
</script>

<style>
.homeRouterView {
  margin-top: 10px;
}

.homeHeader {
  background-color: #759ad1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0px 15px;
  box-sizing: border-box;
}

.homeHeader .title {
  font-size: 20px;
  font-family: 微软雅黑;
  color: #ffffff
}

.homeHeader .userInfo {
  cursor: pointer;
}

.el-dropdown-link {
  display: flex;
  align-items: center;
  color: #ffffff;
}
</style>
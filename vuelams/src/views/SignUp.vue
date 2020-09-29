<template>
  <div>
    <el-form
        :rules="rules"
        ref="signInForm"
        v-loading="loading"
        element-loading-spinner="el-icon-loading"
        element-loading-background="rgba(0, 0, 0, 0.8)"
        :model="signInForm"
        class="loginContainer">
      <h3 class="loginTitle">用户注册</h3>
      <el-form-item label="姓名" prop="name">
        <el-input type="text" v-model="signInForm.name" auto-complete="new-password"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" prop="username">
        <el-input type="text" v-model="signInForm.username" auto-complete="new-password"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input type="password" v-model="signInForm.password" autocomplete="new-password"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="checkPass">
        <el-input type="password" v-model="signInForm.checkPass" @keydown.enter.native="submitForm('signInForm')"
                  autocomplete="new-password"></el-input>
      </el-form-item>
      <el-row>
        <el-button type="primary" @click="submitForm('signInForm')">提交</el-button>
        <el-button @click="resetForm('signInForm')">重置</el-button>
      </el-row>
    </el-form>
  </div>

</template>

<script>
export default {
  name: "SignUp",
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.signInForm.checkPass !== '') {
          this.$refs.signInForm.validateField('checkPass');
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.signInForm.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      loading: false,
      rules: {
        name: [{required: true, message: '请输入用户名', trigger: 'blur'}],
        username: [{required: true, message: '请输入邮箱', trigger: 'blur'},
          {
            type: 'email',
            message: '邮箱格式不正确',
            trigger: 'blur'
          }],
        password: [{validator: validatePass, trigger: 'blur'}],
        checkPass: [{validator: validatePass2, trigger: 'blur'}]
      },
      signInForm: {
        name: 'hhj',
        username: '296@qq.com',
        password: '123',
        checkPass: '123'
      },
    }
  },
  methods: {
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.putRequest("/user/add", this.signInForm).then(resp => {
            if (resp) {
              this.$router.replace("/");
            }
          })
        } else {
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
}
</script>

<style scoped>

</style>
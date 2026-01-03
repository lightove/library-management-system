<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>图书管理系统登录</h2>
        </div>
      </template>
      <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" style="width: 100%;">登录</el-button>
          <el-button type="link" @click="handleRegister" style="width: 100%; margin-top: 10px;">注册账号</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import axios from '../utils/axios'

const router = useRouter()
const loginFormRef = ref(null)
const loginForm = ref({ username: '', password: '' })
const loginRules = ref({
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
})

// 登录
const handleLogin = async () => {
  try {
    await loginFormRef.value.validate()
    const res = await axios.post('/api/user/login', loginForm.value);
    localStorage.setItem('user', JSON.stringify(res.data));
    ElMessage.success('登录成功');
    router.push('/home/book-manage');
  } catch (error) {
    ElMessage.error('登录失败：' + (error.response?.data?.msg || '用户名或密码错误'));
  }
}

// 注册
const handleRegister = async () => {
  const username = prompt('请输入注册用户名：');
  if (!username) return ElMessage.warning('用户名不能为空');
  const password = prompt('请输入注册密码：');
  if (!password) return ElMessage.warning('密码不能为空');

  try {
    await axios.post('/api/user/register', { username: username.trim(), password: password.trim() });
    ElMessage.success('注册成功，请等待管理员审核');
  } catch (error) {
    ElMessage.error('注册失败：' + (error.response?.data?.msg || '用户名已存在'));
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
}
.login-card {
  width: 400px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
.card-header {
  text-align: center;
}
</style>
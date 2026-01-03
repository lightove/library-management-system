home
<template>
  <div class="home-container">
    <el-container style="height: 100vh;">
      <!-- 头部区域 -->
      <el-header style="text-align: right; font-size: 12px">
        <el-dropdown>
          <i class="el-icon-setting" style="margin-right: 15px; font-size: 18px;"></i>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <span style="font-size: 14px;">
          {{ user.realName || user.username }}（{{ user.role === 0 ? '管理员' : '普通用户' }}）
        </span>
      </el-header>

      <!-- 主体区域：侧边栏 + 内容区 -->
      <el-container>
        <!-- 侧边菜单栏 -->
        <el-aside width="200px" style="background-color: #eef1f6">
          <el-menu 
            :default-active="activeMenu" 
            class="el-menu-vertical-demo" 
            @select="handleMenuSelect"
            background-color="#eef1f6"
            text-color="#333"
            active-text-color="#409eff"
          >
            <!-- 图书查询（所有用户可见） -->
            <el-menu-item index="book-manage">
              <el-icon><BookIcon /></el-icon>
              <template #title>图书查询</template>
            </el-menu-item>
            
            <!-- 用户审核（仅管理员可见） -->
            <el-menu-item index="user-audit" v-if="user.role === 0">
              <el-icon><UserIcon /></el-icon>
              <template #title>用户审核</template>
            </el-menu-item>
            
            <!-- 我的借阅（仅普通用户可见） -->
            <el-menu-item index="borrow-manage" v-if="user.role === 1">
              <el-icon><DocumentCopyIcon /></el-icon>
              <template #title>我的借阅</template>
            </el-menu-item>
            
            <!-- 借阅统计（仅管理员可见，新增） -->
            <el-menu-item index="statistics" v-if="user.role === 0">
              <el-icon><BarChartIcon /></el-icon>
              <template #title>借阅统计</template>
            </el-menu-item>
          </el-menu>
        </el-aside>

        <!-- 内容展示区（子路由出口） -->
        <el-main style="padding: 0;">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
// 适配Element Plus 2.3.8的图标导入方式
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

// 提取所需图标（无Icon后缀，适配2.3.8版本）
const BookIcon = ElementPlusIconsVue.Book
const UserIcon = ElementPlusIconsVue.User
const DocumentCopyIcon = ElementPlusIconsVue.DocumentCopy
const BarChartIcon = ElementPlusIconsVue.BarChart // 新增统计图标

// 路由实例
const router = useRouter()
const route = useRoute()

// 获取当前登录用户信息
const user = ref(JSON.parse(localStorage.getItem('user') || '{}'))

// 当前激活的菜单（默认匹配当前路由）
const activeMenu = ref(route.path.replace('/home/', ''))

// 菜单选择事件：跳转到对应子路由
const handleMenuSelect = (index) => {
  activeMenu.value = index
  router.push(`/home/${index}`)
}

// 退出登录
const logout = () => {
  ElMessageBox.confirm(
    '确定退出登录吗？',
    '提示',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    // 清除本地用户信息
    localStorage.removeItem('user')
    // 跳转到登录页
    router.push('/')
    ElMessage.success('退出登录成功！')
  })
}

// 页面挂载时校验登录状态
onMounted(() => {
  // 未登录用户强制跳转到登录页
  if (!user.value.id) {
    router.push('/')
    ElMessage.warning('请先登录！')
  }
})
</script>

<style scoped>
.home-container {
  width: 100%;
  height: 100%;
}

.el-header {
  background-color: #fff;
  color: #333;
  line-height: 60px;
  border-bottom: 1px solid #e6e6e6;
  padding: 0 20px;
}

.el-aside {
  color: #333;
  box-shadow: 2px 0 6px rgba(0, 0, 0, 0.1);
}

.el-menu-vertical-demo {
  height: 100%;
  border-right: none;
}

.el-main {
  background-color: #f9fafb;
  height: calc(100vh - 60px);
  overflow-y: auto;
}
</style>
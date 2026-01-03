<template>
  <div class="user-audit" style="padding: 20px;">
    <h2>用户审核</h2>
    <el-table 
      :data="waitAuditUsers" 
      border 
      style="width: 100%; margin-top: 10px;"
      v-loading="loading"
    >
      <el-table-column prop="id" label="用户ID" width="80" />
      <el-table-column prop="username" label="用户名" width="150" />
      <!-- 直接显示后端拼接的字符串 -->
      <el-table-column prop="createTimeStr" label="注册时间" width="200" />
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button type="primary" size="small" @click="auditUser(row.id, 1)">审核通过</el-button>
          <el-button type="danger" size="small" @click="auditUser(row.id, 2)" style="margin-left: 10px;">审核拒绝</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-if="!loading && waitAuditUsers.length === 0" style="text-align: center; margin-top: 20px; color: #999;">
      暂无待审核用户
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import axios from '../utils/axios'

const waitAuditUsers = ref([])
const loading = ref(false)

const loadWaitAuditUsers = async () => {
  loading.value = true;
  try {
    const res = await axios.get('/api/user/wait-audit');
    console.log('用户数据：', res.data); // 确认有createTimeStr字段
    waitAuditUsers.value = res.data || [];
  } catch (error) {
    ElMessage.error('加载失败：' + error.message);
  } finally {
    loading.value = false;
  }
}

const auditUser = async (userId, status) => {
  try {
    await axios.post(`/api/user/audit/${userId}/${status}`);
    ElMessage.success(status === 1 ? '审核通过' : '审核拒绝');
    loadWaitAuditUsers();
  } catch (error) {
    ElMessage.error('审核失败：' + error.message);
  }
}

onMounted(loadWaitAuditUsers)
</script>
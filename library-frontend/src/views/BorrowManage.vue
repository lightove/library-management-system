<template>
  <div class="borrow-manage" style="padding: 20px;">
    <h2>我的借阅</h2>
    <el-table 
      :data="borrowList" 
      border 
      style="width: 100%; margin-top: 10px;"
      v-loading="loading"
    >
      <el-table-column prop="id" label="借阅ID" width="80" />
      <el-table-column prop="bookId" label="图书ID" width="80" />
      <!-- 直接显示后端拼接的字符串 -->
      <el-table-column prop="borrowDateStr" label="借阅日期" width="200" />
      <el-table-column prop="dueDateStr" label="应还日期" width="200" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag v-if="row.status === 0" type="warning">未归还</el-tag>
          <el-tag v-else type="success">已归还</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button 
            type="primary" 
            size="small" 
            @click="returnBook(row.id)" 
            v-if="row.status === 0"
          >
            归还
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-if="!loading && borrowList.length === 0" style="text-align: center; margin-top: 20px; color: #999;">
      暂无借阅记录
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import axios from '../utils/axios'

const borrowList = ref([])
const loading = ref(false)
const user = JSON.parse(localStorage.getItem('user') || '{}')

const loadMyBorrows = async () => {
  if (!user.id) {
    ElMessage.warning('请先登录');
    return;
  }
  loading.value = true;
  try {
    const res = await axios.get(`/api/borrow/user/${user.id}`);
    console.log('借阅数据：', res.data); // 确认有borrowDateStr/dueDateStr字段
    borrowList.value = res.data || [];
  } catch (error) {
    ElMessage.error('加载失败：' + error.message);
  } finally {
    loading.value = false;
  }
}

const returnBook = async (borrowId) => {
  try {
    await axios.post(`/api/borrow/return/${borrowId}`);
    ElMessage.success('归还成功');
    loadMyBorrows();
  } catch (error) {
    ElMessage.error('归还失败：' + error.message);
  }
}

onMounted(loadMyBorrows)
</script>
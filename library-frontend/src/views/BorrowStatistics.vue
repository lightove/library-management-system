<template>
  <div class="statistics" style="padding: 20px;">
    <h2>借阅统计</h2>
    <el-row :gutter="20" style="margin-top: 20px;" v-if="stats">
      <el-col :span="8">
        <el-card>
          <div class="stat-item">
            <span class="label">未归还图书数：</span>
            <span class="value">{{ stats.unReturned }}</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div class="stat-item">
            <span class="label">已归还图书数：</span>
            <span class="value">{{ stats.returned }}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <div v-if="!loading && !stats" style="text-align: center; margin-top: 20px; color: #999;">
      暂无统计数据
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import axios from '../utils/axios'

const stats = ref(null)
const loading = ref(false)

// 加载统计数据（修复接口路径）
const loadStats = async () => {
  loading.value = true;
  try {
    // 确保接口路径带/api前缀，匹配Vite代理
    const res = await axios.get('/api/borrow/stats');
    stats.value = res.data || { unReturned: 0, returned: 0 };
  } catch (error) {
    ElMessage.error('加载统计数据失败：' + (error.message || '接口异常'));
    console.error('统计接口错误：', error);
  } finally {
    loading.value = false;
  }
}

onMounted(loadStats)
</script>

<style scoped>
.stat-item {
  font-size: 16px;
  text-align: center;
  padding: 10px 0;
}
.label {
  margin-right: 10px;
}
.value {
  font-weight: bold;
  color: #409eff;
}
</style>
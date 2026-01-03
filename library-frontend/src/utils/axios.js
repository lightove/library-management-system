import axios from 'axios';
import { ElMessage } from 'element-plus';

// 创建axios实例，请求前缀为/api（匹配Vite代理）
const service = axios.create({
  baseURL: '/api', // 关键：用代理前缀，不再直接写后端地址
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  }
});

// 请求拦截器
service.interceptors.request.use(
  (config) => {
    return config;
  },
  (error) => {
    ElMessage.error('请求异常：' + error.message);
    return Promise.reject(error);
  }
);

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    const res = response.data;
    // 统一处理业务错误
    if (res.code !== 200 && res.msg) {
      ElMessage.error(res.msg);
    }
    return res;
  },
  (error) => {
    // 打印错误详情，方便排查
    console.error('请求失败详情：', error);
    ElMessage.error('服务器异常：' + (error.message || '网络错误'));
    return Promise.reject(error);
  }
);

export default service;
<template>
  <div class="book-manage" style="padding: 20px;">
    <h2>图书管理</h2>
    <el-button type="primary" @click="dialogVisible = true" v-if="isAdmin" style="margin-bottom: 10px;">
      图书入库/编辑
    </el-button>
    <el-input v-model="searchTitle" placeholder="输入书名查询" style="width: 300px; margin-bottom: 10px;" />
    <el-button type="primary" @click="loadBooks">查询</el-button>

    <el-table :data="bookList" border style="width: 100%;">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="isbn" label="ISBN" width="150" />
      <el-table-column prop="title" label="书名" width="200" />
      <el-table-column prop="author" label="作者" width="150" />
      <el-table-column prop="publisher" label="出版社" width="150" />
      <el-table-column prop="stock" label="库存" width="80" />
      <el-table-column label="操作" width="200">
        <template #default="{ row }">
          <!-- 普通用户显示借阅按钮 -->
          <el-button type="success" @click="borrowBook(row.id)" v-if="!isAdmin">借阅</el-button>
          <!-- 管理员显示编辑、删除按钮 -->
          <el-button type="primary" size="small" @click="editBook(row)" v-if="isAdmin">编辑</el-button>
          <el-button type="danger" size="small" @click="deleteBook(row.id)" v-if="isAdmin">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 入库/编辑弹窗 -->
    <el-dialog v-model="dialogVisible" title="图书入库/编辑">
      <el-form :model="newBook" label-width="80px">
        <el-form-item label="ISBN" prop="isbn">
          <el-input v-model="newBook.isbn" />
        </el-form-item>
        <el-form-item label="书名" prop="title">
          <el-input v-model="newBook.title" />
        </el-form-item>
        <el-form-item label="作者" prop="author">
          <el-input v-model="newBook.author" />
        </el-form-item>
        <el-form-item label="出版社" prop="publisher">
          <el-input v-model="newBook.publisher" />
        </el-form-item>
        <el-form-item label="库存" prop="stock">
          <el-input v-model.number="newBook.stock" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="saveBook">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import axios from '../utils/axios'

const user = JSON.parse(localStorage.getItem('user') || '{}')
const isAdmin = user.role === 0
const searchTitle = ref('')
const bookList = ref([])
const dialogVisible = ref(false)
const newBook = ref({ id: '', isbn: '', title: '', author: '', publisher: '', stock: 0 })

// 加载图书
const loadBooks = async () => {
  try {
    const res = await axios.get('/api/book', { params: { title: searchTitle.value } })
    bookList.value = res.data
  } catch (error) {
    ElMessage.error('加载图书失败：' + error.msg)
  }
}

// 图书入库/编辑
const saveBook = async () => {
  try {
    if (newBook.value.id) {
      // 编辑
      await axios.put('/api/book', newBook.value)
      ElMessage.success('编辑成功')
    } else {
      // 入库
      await axios.post('/api/book', newBook.value)
      ElMessage.success('入库成功')
    }
    dialogVisible.value = false
    loadBooks()
    // 重置表单
    newBook.value = { id: '', isbn: '', title: '', author: '', publisher: '', stock: 0 }
  } catch (error) {
    ElMessage.error('操作失败：' + error.msg)
  }
}

// 编辑图书
const editBook = (book) => {
  dialogVisible.value = true
  newBook.value = { ...book } // 回显数据
}

// 删除图书
const deleteBook = async (bookId) => {
  if (confirm('确定删除该图书吗？')) {
    try {
      await axios.delete(`/api/book/${bookId}`)
      ElMessage.success('删除成功')
      loadBooks()
    } catch (error) {
      ElMessage.error('删除失败：' + error.msg)
    }
  }
}

// 借阅图书
const borrowBook = async (bookId) => {
  try {
    await axios.post(`/api/book/borrow/${user.id}/${bookId}`)
    ElMessage.success('借阅成功')
    loadBooks()
  } catch (error) {
    ElMessage.error('借阅失败：' + error.msg)
  }
}

onMounted(loadBooks)
</script>
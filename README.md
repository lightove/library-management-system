# library-management-system
基于AI辅助开发的图书馆管理系统

一、	图书馆管理系统
基于Vue 3 + Spring Boot + MySQL开发的高校图书馆管理系统，支持图书管理、用户管理、借阅管理、统计报表等核心功能，适配高校图书馆日常运营场景，区分管理员与普通用户权限体系。

二、	项目结构
library-management-system/
├── library-frontend/          # 前端Vue项目（Vue3 + Element Plus + Vite）
├── library-backend/           # 后端Spring Boot项目（SpringBoot 2.7.x + MyBatis-Plus）

三、	技术栈
1.	前端
核心框架：Vue 3（Composition API）
UI组件库：Element Plus
构建工具：Vite
网络请求：Axios
路由管理：Vue Router 4

2.	后端
核心框架：Spring Boot 2.7.x
ORM框架：MyBatis-Plus
数据库：MySQL 8.0
接口规范：RESTful API
返回格式：统一Result封装
四、	数据库初始化
1. 手动创建MySQL数据库：
CREATE DATABASE IF NOT EXISTS library_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
2. 执行sql脚本，完成表结构与测试数据初始化
-- user表
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `role` int DEFAULT 1,
  `status` int DEFAULT 0,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- book表
CREATE TABLE `book` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `isbn` varchar(50) DEFAULT NULL,
  `title` varchar(100) NOT NULL,
  `author` varchar(50) DEFAULT NULL,
  `publisher` varchar(50) DEFAULT NULL,
  `stock` int DEFAULT 0,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- borrow表
CREATE TABLE `borrow` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `book_id` bigint NOT NULL,
  `borrow_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `due_date` date DEFAULT NULL,
  `return_date` date DEFAULT NULL,
  `status` int DEFAULT 0,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

五、	后端部署
1.	配置数据库连接（修改src/main/resources/application.yml）：
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/library_db?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root 
    password: 123456 
server:
  port: 8080 # 后端服务端口
2.	部署pom.xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.18</version> 
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    
    <!-- 项目基本信息-->
    <groupId>com.library</groupId>
    <artifactId>library-backend</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>library-backend</name>
    <description>Library Management System</description>
    
    <!-- 核心配置 -->
    <properties>
        <java.version>8</java.version>
        <mybatis-plus.version>3.5.3.1</mybatis-plus.version>
        <mysql.version>8.0.33</mysql.version> <!-- 兼容MySQL5.5/8.0 -->
    </properties>
    
    <!-- 依赖列表 -->
    <dependencies>
        <!-- Spring Boot Web核心（REST接口、Tomcat） -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <!-- MyBatis Plus（简化MyBatis操作） -->
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-boot-starter</artifactId>
            <version>${mybatis-plus.version}</version>
        </dependency>
        
        <!-- MySQL驱动-->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>${mysql.version}</version>
            <scope>runtime</scope>
        </dependency>
        
        <!-- Lombok -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        
        <!-- Spring Boot DevTools-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        
        <!-- Spring Boot测试-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <!-- 构建配置 -->
    <build>
        <plugins>
            <!-- Spring Boot打包插件 -->
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
3.	启动后端服务
右键运行LibraryBackendApplication.java，点击“Run Java”
后端服务默认访问地址：http://localhost:8080
3.	前端部署
1)	安装依赖
npm install
2)	配置代理（修改vite.config.js，已默认适配后端端口）：
export default defineConfig({
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
})

3)	启动前端服务：
npm run dev
前端服务默认访问地址：http://localhost:5173

六、	核心功能
管理员权限：
模块	功能点	说明
图书管理	图书入库/查询/库存维护	支持ISBN/书名/作者模糊检索，实时更新库存
用户管理	待审核用户列表/用户审核	审核通过/拒绝普通用户注册申请
统计报表	借阅数据统计	展示未归还/已归还图书数量统计

普通用户权限：
模块	功能点	说明
账号管理	注册/登录	注册后需管理员审核通过方可登录
借阅管理	图书借阅/归还	校验图书库存，生成/更新借阅记录
记录查询	个人借阅记录查询	展示借阅日期、应还日期、归还状态等信息

测试账号：
角色	用户名	密码	备注
管理员	admin	123456	拥有全部功能权限
普通用户	user	123456	需先在后台审核通过后登录

核心接口说明：
接口	请求方法	接口路径	功能说明
用户注册	POST	/api/user/register	提交用户注册信息
用户登录	POST	/api/user/login	用户身份验证
待审核用户	GET	/api/user/wait-audit	获取待审核用户列表
图书借阅	POST	/api/borrow	发起图书借阅请求
图书归还	POST	/api/borrow/return	归还已借图书
我的借阅	GET	/api/borrow/user/{id}	查询个人借阅记录
借阅统计	GET	/api/borrow/stats	获取借阅数据统计

注意事项：
1. 首次启动需确保MySQL服务正常运行，且已创建library_db数据库
2. 前端代理已配置，无需额外处理跨域问题；若修改后端端口，需同步更新vite.config.js
3. 生产环境部署需：
修改数据库密码为强密码
配置Spring Security权限控制
开启HTTPS协议
部署前端静态资源至Nginx
4. 测试数据仅用于功能验证，生产环境需清空后重新录入真实数据


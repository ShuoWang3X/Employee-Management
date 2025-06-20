### 职工档案管理系统说明文档

# 职工档案管理系统说明文档

## 项目概述

职工档案管理系统是一个基于 Java Swing 和 MySQL 数据库的桌面应用程序，用于管理企业或组织的职工信息。系统提供了职工信息的增删改查、数据导入导出、条件搜索等功能，帮助用户高效地管理职工档案。

## 技术架构

### 后端技术

- Java SE 17
- MySQL 5.7 数据库
- JDBC 数据库连接
- Maven 项目管理工具

### 前端技术

- Java Swing GUI 组件
- AWT 图形处理

### 项目结构

```plaintext
coursedesign/
├── DBUtil.java              # 数据库连接工具类
├── Employee.java            # 职工实体类
├── EmployeeDetailDialog.java # 职工详情对话框
├── EmployeeDialog.java      # 职工信息对话框
├── EmployeeManagementSystem.java # 主系统类
├── EmployeeManager.java     # 职工管理类
├── EmployeeTableModel.java  # 职工表格模型
└── SearchDialog.java        # 搜索对话框
```

## 功能模块说明

### 1. 数据库连接模块 (DBUtil.java)

- **功能描述**：提供数据库连接和操作的静态方法，支持配置文件加载、连接重试、资源关闭等功能
- **核心功能**：
  - 从配置文件加载数据库连接参数
  - 提供带重试机制的数据库连接获取方法
  - 封装 SQL 查询和更新操作
  - 批量更新和事务管理

### 2. 职工实体模块 (Employee.java)

- **功能描述**：职工信息的实体类，实现序列化和比较接口
- **核心属性**：
  - 基本信息：工号、姓名、性别、出生日期等
  - 工作信息：部门、职位、入职日期、薪资等
  - 联系信息：电话、邮箱、地址等
- **核心功能**：
  - 日期和薪资的格式化显示
  - 对象比较和哈希处理

### 3. 界面交互模块

#### 3.1 主系统界面 (EmployeeManagementSystem.java)

- **功能描述**：系统主界面，提供操作工具栏和职工表格显示
- **核心功能**：
  - 职工信息表格展示
  - 工具栏操作按钮（增删改查、导入导出等）
  - 状态条信息显示
  - 数据刷新和保存

#### 3.2 职工信息对话框 (EmployeeDialog.java)

- **功能描述**：用于新增和修改职工信息的对话框
- **核心功能**：
  - 职工信息表单输入
  - 输入数据验证
  - 日期和数值类型的格式处理

#### 3.3 职工详情对话框 (EmployeeDetailDialog.java)

- **功能描述**：用于查看职工详细信息的对话框
- **核心功能**：
  - 职工信息的详细展示
  - 格式化日期和数值显示

#### 3.4 搜索对话框 (SearchDialog.java)

- **功能描述**：用于设置职工搜索条件的对话框
- **核心功能**：
  - 多条件组合搜索
  - 搜索条件验证
  - 日期和数值范围搜索

### 4. 数据管理模块 (EmployeeManager.java)

- **功能描述**：负责职工数据的增删改查、数据库操作和文件操作
- **核心功能**：
  - 数据库表结构管理
  - 职工数据的 CRUD 操作
  - 数据分组和筛选
  - CSV 文件导入导出
  - 工号自动生成

### 5. 表格模型模块 (EmployeeTableModel.java)

- **功能描述**：职工数据在表格中的展示模型
- **核心功能**：
  - 表格列定义和数据映射
  - 单元格值格式化
  - 表格数据更新通知

## 数据库设计

### 职工表 (employees)

```sql
CREATE TABLE IF NOT EXISTS employees (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(10),
    birth_date DATE,
    hire_date DATE,
    department VARCHAR(100),
    position VARCHAR(100),
    education VARCHAR(20),
    salary DECIMAL(10, 2),
    phone VARCHAR(50),
    email VARCHAR(100),
    address VARCHAR(200),
    remarks TEXT
);
```

### 字段说明

| 字段名     | 类型          | 说明             |
| ---------- | ------------- | ---------------- |
| id         | VARCHAR(20)   | 职工工号（主键） |
| name       | VARCHAR(100)  | 姓名（非空）     |
| gender     | VARCHAR(10)   | 性别             |
| birth_date | DATE          | 出生日期         |
| hire_date  | DATE          | 入职日期         |
| department | VARCHAR(100)  | 部门             |
| position   | VARCHAR(100)  | 职位             |
| education  | VARCHAR(20)   | 学历             |
| salary     | DECIMAL(10,2) | 薪资             |
| phone      | VARCHAR(50)   | 联系电话         |
| email      | VARCHAR(100)  | 电子邮箱         |
| address    | VARCHAR(200)  | 家庭住址         |
| remarks    | TEXT          | 备注信息         |

## 运行环境

### 软件要求

- JDK 11 或更高版本
- MySQL 5.7 或更高版本
- Maven 3.6 或更高版本

### 依赖项

```xml
<dependencies>
    <!-- MySQL数据库驱动 -->
    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <version>8.0.33</version>
    </dependency>
    
    <!-- 日志框架 -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>1.7.36</version>
    </dependency>
    <dependency>
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>1.2.11</version>
    </dependency>
    
    <!-- 数据库连接池 -->
    <dependency>
        <groupId>com.zaxxer</groupId>
        <artifactId>HikariCP</artifactId>
        <version>4.0.3</version>
    </dependency>
    
    <!-- Joda-Time日期处理 -->
    <dependency>
        <groupId>joda-time</groupId>
        <artifactId>joda-time</artifactId>
        <version>2.12.5</version>
    </dependency>
</dependencies>
```

## 部署与运行

### 1. 配置数据库

1. 创建 MySQL 数据库：`CREATE DATABASE employee_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;`
2. 确保数据库用户有权限创建表和插入数据

### 2. 配置连接参数

- 修改`db.properties`文件或在运行时提供外部配置文件

- 配置示例：

  ```properties
  db.url=jdbc:mysql://localhost:3306/employee_db?serverTimezone=Asia/Shanghai&characterEncoding=UTF-8
  db.username=root
  db.password=123456
  ```

### 3. 编译与打包

```bash
# 编译项目
mvn clean compile

# 打包可执行JAR
mvn package
```

### 4. 运行程序

```bash
java -jar target/employee-management-1.0-SNAPSHOT.jar
```

## 使用说明

### 1. 主界面操作

- **工具栏按钮**：
  - 新增：添加新职工信息
  - 修改：修改选中职工信息
  - 删除：删除选中职工信息
  - 查询：按条件搜索职工
  - 刷新：从数据库重新加载数据
  - 导出：将数据导出为 CSV 文件
  - 导入：从 CSV 文件导入数据

### 2. 职工信息管理

- **新增职工**：点击 "新增" 按钮，填写职工信息表单
- **修改职工**：选中表格中的职工，点击 "修改" 按钮
- **删除职工**：选中表格中的职工，点击 "删除" 按钮，确认后删除

### 3. 数据搜索

- 点击 "查询" 按钮，在搜索对话框中设置搜索条件
- 支持按工号、姓名、部门、职位等多条件组合搜索
- 支持按薪资范围和入职日期范围搜索

### 4. 数据导入导出

- **导出数据**：点击 "导出" 按钮，选择保存位置和文件名
- **导入数据**：点击 "导入" 按钮，选择 CSV 文件并确认导入

## 系统特点

1. **用户友好的界面**：采用 Swing 构建的图形界面，操作简单直观
2. **完整的功能集**：提供职工信息的全生命周期管理
3. **灵活的搜索功能**：支持多条件组合搜索和范围搜索
4. **数据导入导出**：支持 CSV 格式的数据导入导出
5. **完善的错误处理**：对数据库操作和用户输入进行全面的错误处理
6. **配置灵活**：支持从外部配置文件加载数据库连接参数

## 未来改进方向

1. 添加数据备份与恢复功能
2. 实现更复杂的统计和报表功能
3. 添加用户权限管理功能
4. 优化大数据量下的性能
5. 增加数据可视化展示
6. 支持多语言界面切换
7. 开发 Web 版本，支持远程访问"# Employee-Management" 
"# Employee-Management" 

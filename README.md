# CookFlow - 智能食谱管理系统

> 一个面向烹饪爱好者的全栈食谱管理平台，集成 AI 助手、在线烹饪书籍、食材百科与社区论坛，提供从食谱发现到采购清单的一站式体验。

---

## 项目简介

CookFlow 是一个基于 **Spring Boot + Vue 3** 的全栈 Web 应用，旨在为用户提供丰富、互动、智能的食谱管理体验。用户可浏览分类菜谱、查看食材百科、阅读烹饪书籍、参与社区讨论，并借助 AI 助手获取个性化烹饪建议。

项目分为 **用户端** 和 **管理端**：
- **用户端** — 访客与注册用户，浏览食谱、食材、书籍、论坛等
- **管理端** — 管理员，用于用户管理、菜谱审核、内容监控、敏感词管理等

## 技术栈

| 层次 | 技术 |
|------|------|
| **前端** | Vue 3 + TypeScript + Vite + Pinia + Vue Router + Element Plus + Tailwind CSS + ECharts |
| **后端** | Spring Boot 2.5 + Spring Security + MyBatis + Flowable 6.8 |
| **数据库** | MySQL |
| **工作流引擎** | Flowable（菜谱审核流程、任务管理） |
| **构建** | Maven (后端) / Vite (前端) |
| **其他** | Swagger 3.0 (API 文档)、JWT 认证、Druid 连接池、Quartz 定时任务 |

## 功能特性

### 用户端

| 模块 | 功能 |
|------|------|
| **首页** | 轮播图、分类导航、推荐菜谱、个性化推荐 |
| **菜谱区** | 菜谱列表浏览、分类筛选、关键词搜索、菜谱详情（图文步骤、食材清单、收藏） |
| **食材区** | 食材分类浏览、字母索引、食材详情（选购技巧、处理步骤、营养数据、知识科普） |
| **书籍区** | 烹饪书籍在线阅读、图文混排、目录导航 |
| **拓展区** | 热度菜品排行、美食地图、季节食历、食材历史（菜系文化时间线） |
| **论坛** | 分享区（用户发帖分享菜谱）、问答区（提问/回答）、主题活动（发布与参与） |
| **AI 助手** | 智能对话，提供烹饪建议、食谱推荐、食材问答 |
| **购物车** | 食材加入购物车、数量管理、一键结算生成采购清单 |
| **个人中心** | 用户信息管理、收藏管理 |

### 管理端

| 功能 | 说明 |
|------|------|
| **用户管理** | 用户列表、信息编辑、账号状态管理 |
| **用户监测** | 用户行为监控与数据分析 |
| **菜谱审核** | 基于 Flowable 工作流的菜谱发布审核流程 |
| **菜谱列表** | 全量菜谱管理与编辑 |
| **类别管理** | 菜谱/食材分类的增删改查 |
| **敏感词管理** | 敏感词库维护与内容过滤 |
| **系统监控** | 服务器状态、在线用户、操作日志 |

## 项目结构

```
CookFlow/
├── CookFlowJava/          # 后端（Spring Boot 多模块）
│   └── CookFlowJava/
│       ├── admin/         # 管理端 Controller
│       ├── system/        # 系统模块（用户、角色、权限）
│       ├── framework/     # 核心框架配置（安全、拦截器）
│       ├── generator/     # 代码生成器
│       ├── quartz/        # 定时任务
│       ├── flowable/      # Flowable 工作流引擎
│       ├── common/        # 公共工具类
│       └── Hnit/          # 业务模块（菜谱、食材、书籍、论坛）
├── CookFlowVue/           # 前端（Vue 3 + TypeScript）
│   └── CookFlow2/
│       ├── src/
│       │   ├── views/     # 页面组件
│       │   ├── store/     # Pinia 状态管理
│       │   ├── router/    # 路由配置
│       │   ├── api/       # API 请求封装
│       │   └── components/# 公共组件
│       └── ...
└── screenshots/           # 项目截图
```

## 快速开始

### 后端

1. **环境要求**：JDK 1.8+、Maven 3.6+、MySQL
2. **导入数据库**：执行 `CookFlowVue/CookFlow2/db_schema.sql`
3. **修改配置**：`CookFlowJava/CookFlowJava/Hnit/main/resources/application.yml` 中配置数据库连接
4. **启动**：
   ```bash
   cd CookFlowJava/CookFlowJava
   mvn clean install
   java -jar admin/target/admin.jar
   ```

### 前端

1. **环境要求**：Node.js 18+
2. **安装依赖**：
   ```bash
   cd CookFlowVue/CookFlow2
   npm install
   ```
3. **启动开发服务器**：
   ```bash
   npm run dev
   ```
4. **构建**：
   ```bash
   npm run build
   ```

## API 文档

完整的 API 文档请参考 `CookFlowVue/CookFlow2/API_DOCUMENTATION.md` 或 OpenAPI 描述文件 `CookFlowVue/CookFlow2/openapi.yaml`。

## 项目亮点

- **AI 助手集成**：基于对话的智能烹饪助手，提供个性化食谱推荐与烹饪建议
- **Flowable 工作流**：菜谱审核发布流程自动化，支持审批流转与状态管理
- **丰富的多媒体内容**：图文步骤菜谱、食材百科、烹饪书籍、美食地图、菜系历史时间线
- **社区生态**：用户分享、问答互动、主题活动，构建烹饪爱好者社区
- **管理后台**：完整的运营管理工具，支持内容审核、用户监控与系统运维

# 项目截图预览

### 1.1用户端登录界面
![1.1用户端登录界面](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/1.1用户端登录界面.png)

### 1.2用户端注册界面
![1.2用户端注册界面](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/1.2用户端注册界面.png)

### 1.3管理端登录界面
![1.3管理端登录界面](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/1.3管理端登录界面.png)

### 2首页
![2首页](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/2首页.png?v=20260727)

### 3菜谱区
![3菜谱区](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/3菜谱区.png)

### 4菜谱详情页
![4菜谱详情页](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/4菜谱详情页.png)

### 4.1菜谱-步骤页面
![4.1菜谱-步骤页面](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/4.1菜谱-步骤页面.png)

### 5食材区
![5食材区](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/5食材区.png)

### 6书籍区
![6书籍区](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/6书籍区.png)

### 6.1书籍详情页
![6.1书籍详情页](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/6.1书籍详情页.png)

### 6.2书籍阅读界面
![6.2书籍阅读界面](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/6.2书籍阅读界面.png)

### 7.1拓展区-热度菜品
![7.1拓展区-热度菜品](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/7.1拓展区-热度菜品.png)

### 7.2拓展区-美食地图
![7.2拓展区-美食地图](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/7.2拓展区-美食地图.png)

### 7.3拓展区-季节食历
![7.3拓展区-季节食历](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/7.3拓展区-季节食历.png)

### 7.4拓展区-食材历史
![7.4拓展区-食材历史](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/7.4拓展区-食材历史.png)

### 8.1论坛分享区
![8.1论坛分享区](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/8.1论坛分享区.png)

### 8.2论坛-问答区
![8.2论坛-问答区](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/8.2论坛-问答区.png)

### 8.2.1问答详情页
![8.2.1问答详情页](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/8.2.1问答详情页.png)

### 9ai助手界面
![9ai助手界面](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/9ai助手界面.png)

### 10购物车界面
![10购物车界面](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/10购物车界面.png)

### 11个人界面
![11个人界面](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/11个人界面.png)

### 12.1管理端首页
![12.1管理端首页](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/12.1管理端首页.png)

### 12.2管理端用户管理界面
![12.2管理端用户管理界面](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/12.2管理端用户管理界面.png)

### 12.3管理端用户监测界面
![12.3管理端用户监测界面](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/12.3管理端用户监测界面.png)

### 12.4管理端菜谱审核界面
![12.4管理端菜谱审核界面](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/12.4管理端菜谱审核界面.png)

### 12.5管理端菜谱列表界面
![12.5管理端菜谱列表界面](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/12.5管理端菜谱列表界面.png)

### 12.6管理端类别管理界面
![12.6管理端类别管理界面](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/12.6管理端类别管理界面.png)

### 12.7管理端敏感词管理界面
![12.7管理端敏感词管理界面](https://cdn.jsdelivr.net/gh/fengrenzi/CookFlow@main/screenshots/12.7管理端敏感词管理界面.png)

# CookFlow2 API Documentation (简洁版)

Base URL: `/api`

认证: `Authorization: Bearer <token>` (JWT)

## Endpoints

### Auth
- `POST /api/auth/login` — 登录
  - Request: `{ username, password }`
  - Response: `{ token, username }`
- `POST /api/auth/logout` — 登出（Auth）
- `GET /api/auth/me` — 当前用户信息（Auth）

### Recipes
- `GET /api/recipes` — 列表，支持 `page,size,category,q`
- `GET /api/recipes/{id}` — 获取详情
- `POST /api/recipes` — 创建
- `PUT /api/recipes/{id}` — 更新
- `DELETE /api/recipes/{id}` — 删除
- `POST /api/recipes/{id}/favorite` — 收藏/取消收藏（Auth）

Recipe 模型：`id, name, description, image, ingredients[], steps[], category, prepTime, cookTime`

### Ingredients
- `GET /api/ingredients/categories` — 分类结构
- `GET /api/ingredients` — 列表，支持 `category,letter,q,page,size`
- `GET /api/ingredients/{id}` — 详情
- `POST /api/ingredients/recommendations` — 从选中食材推荐菜谱（body: `{ selectedIngredientIds: [] }`）

### Ingredient Detail
- `GET /api/ingredient-detail/{id}` — 返回 `{ carouselItems, selectionTips, processingSteps, nutritionData, knowledgePoints }`

### Home
- `GET /api/home/images`
- `GET /api/home/categories`
- `GET /api/home/recipes`
- `GET /api/home/recommends` (Auth optional)

### Cart (Auth)
- `GET /api/cart`
- `POST /api/cart/items` — 添加项
- `PUT /api/cart/items/{id}` — 更新数量
- `DELETE /api/cart/items/{id}` — 删除项
- `POST /api/cart/checkout` — 结算，返回购物清单

### Books
- `GET /api/books`
- `GET /api/books/{id}`
- `GET /api/books/{bookId}/recipes/{pageNumber}`

### Users
- `GET /api/users/{username}`
- `PUT /api/users/{username}`

## 错误返回
统一格式： `{ code: number, message: string, detail?: any }`

## 表结构映射建议
- `recipes` 表 — 存储菜谱主数据
- `ingredients` 与 `recipe_ingredients` — 食材与配料映射
- `users`, `favorites`, `cart_items` — 用户相关
-- Database schema for CookFlow2
-- MySQL syntax (adjust types as needed)

-- Users table
CREATE TABLE users (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID，自增主键',
  username VARCHAR(100) NOT NULL UNIQUE COMMENT '登录用户名，唯一',
  password_hash VARCHAR(255) NOT NULL COMMENT '密码哈希（不可逆）',
  avatar VARCHAR(255) COMMENT '头像URL',
  bio TEXT COMMENT '用户个人简介',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- Recipes table
CREATE TABLE recipes (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '菜谱ID，自增主键',
  name VARCHAR(255) NOT NULL COMMENT '菜谱名称',
  description TEXT COMMENT '菜谱描述/正文',
  image VARCHAR(255) COMMENT '主图URL',
  category VARCHAR(100) COMMENT '菜谱分类（字符串或分类ID）',
  prep_time INT COMMENT '准备时间（分钟）',
  cook_time INT COMMENT '烹饪时间（分钟）',
  created_by BIGINT COMMENT '创建者用户ID',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (created_by) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜谱表';

-- Ingredients table
CREATE TABLE ingredients (
  id VARCHAR(64) PRIMARY KEY COMMENT '食材ID，自定义或组合ID（如 a-1）',
  name VARCHAR(255) NOT NULL COMMENT '食材名称',
  img_url VARCHAR(255) COMMENT '食材图片URL',
  category VARCHAR(50) COMMENT '食材分类ID或名称',
  letter CHAR(1) COMMENT '字母分组（首字母）',
  unit VARCHAR(50) COMMENT '默认计量单位（如 g、个）',
  extra JSON COMMENT '扩展字段，存放额外元信息'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='食材表';

-- Recipe <-> Ingredient mapping (many-to-many with amount)
CREATE TABLE recipe_ingredients (
  recipe_id BIGINT NOT NULL COMMENT '关联菜谱ID',
  ingredient_id VARCHAR(64) NOT NULL COMMENT '关联食材ID',
  amount DECIMAL(10,3) COMMENT '数量（按照 unit 单位）',
  unit VARCHAR(50) COMMENT '计量单位（覆盖 ingredients.unit）',
  PRIMARY KEY (recipe_id, ingredient_id),
  FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE,
  FOREIGN KEY (ingredient_id) REFERENCES ingredients(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜谱与食材关联表（含数量）';

-- Recipe steps (ordered)
CREATE TABLE recipe_steps (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '步骤ID，自增',
  recipe_id BIGINT NOT NULL COMMENT '所属菜谱ID',
  step_index INT NOT NULL COMMENT '步骤顺序编号，从0或1开始',
  content TEXT COMMENT '步骤内容（文本或富文本）',
  FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜谱步骤表（有序）';

-- Favorites (user favorite recipes)
CREATE TABLE favorites (
  user_id BIGINT NOT NULL COMMENT '用户ID',
  recipe_id BIGINT NOT NULL COMMENT '被收藏的菜谱ID',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (user_id, recipe_id),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (recipe_id) REFERENCES recipes(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏表';

-- Cart items (per user)
CREATE TABLE cart_items (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '购物项ID',
  user_id BIGINT NOT NULL COMMENT '所属用户ID',
  type ENUM('recipe','book') NOT NULL COMMENT '项类型：菜谱或书中菜谱',
  related_id VARCHAR(64) COMMENT '关联的 recipe id 或 book id',
  quantity INT DEFAULT 1 COMMENT '数量',
  ingredients JSON COMMENT '可选：具体食材列表（含数量）',
  added_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车项表';

-- Books and book recipes
CREATE TABLE books (
  id VARCHAR(64) PRIMARY KEY COMMENT '书籍ID',
  title VARCHAR(255) COMMENT '书名',
  author VARCHAR(255) COMMENT '作者',
  metadata JSON COMMENT '书籍元数据'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书籍表';

CREATE TABLE book_recipes (
  book_id VARCHAR(64) NOT NULL COMMENT '书籍ID',
  page_number INT NOT NULL COMMENT '页码',
  recipe_id BIGINT COMMENT '对应的菜谱ID（若存在）',
  PRIMARY KEY (book_id, page_number),
  FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE,
  FOREIGN KEY (recipe_id) REFERENCES recipes(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='书籍中页码到菜谱的映射表';

-- Ingredient detail static content (carousel, tips, steps, nutrition, knowledge)
CREATE TABLE ingredient_details (
  ingredient_id VARCHAR(64) PRIMARY KEY COMMENT '食材ID',
  carousel JSON COMMENT '轮播图数据（JSON数组）',
  selection_tips JSON COMMENT '挑选提示（JSON数组）',
  processing_steps JSON COMMENT '处理步骤（JSON数组）',
  nutrition JSON COMMENT '营养信息（JSON数组）',
  knowledge_points JSON COMMENT '科普知识点（JSON数组）'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='食材详情静态内容表';

-- Core supplemental tables: comments, tags, recipe_stats, events, notifications, media

CREATE TABLE comments (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评论ID',
  resource_type VARCHAR(50) NOT NULL COMMENT '资源类型：recipe/post/book等',
  resource_id VARCHAR(64) NOT NULL COMMENT '资源ID',
  parent_id BIGINT NULL COMMENT '父评论ID（用于回复）',
  user_id BIGINT NOT NULL COMMENT '评论用户ID',
  content TEXT NOT NULL COMMENT '评论内容',
  at_users JSON NULL COMMENT '被@用户ID数组',
  status VARCHAR(20) DEFAULT 'visible' COMMENT '状态: visible/hidden/flagged',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表（支持@/回复/状态）';
CREATE INDEX idx_comments_resource ON comments(resource_type, resource_id);
CREATE INDEX idx_comments_user ON comments(user_id);

CREATE TABLE tags (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '标签ID',
  name VARCHAR(100) NOT NULL UNIQUE COMMENT '标签名称',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签表';

CREATE TABLE tag_map (
  tag_id BIGINT NOT NULL COMMENT '标签ID',
  resource_type VARCHAR(50) NOT NULL COMMENT '资源类型',
  resource_id VARCHAR(64) NOT NULL COMMENT '资源ID',
  PRIMARY KEY (tag_id, resource_type, resource_id),
  FOREIGN KEY (tag_id) REFERENCES tags(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='标签映射表';

CREATE TABLE recipe_stats (
  recipe_id BIGINT PRIMARY KEY COMMENT '菜谱ID',
  views BIGINT DEFAULT 0 COMMENT '浏览量',
  likes INT DEFAULT 0 COMMENT '点赞数',
  favorites INT DEFAULT 0 COMMENT '收藏数',
  comments INT DEFAULT 0 COMMENT '评论数',
  score DOUBLE DEFAULT 0 COMMENT '综合热度分',
  last_updated DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '统计更新时间',
  FOREIGN KEY (recipe_id) REFERENCES recipes(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜谱聚合统计，用于排行榜';
CREATE INDEX idx_recipe_stats_score ON recipe_stats(score DESC, last_updated);

CREATE TABLE events (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '事件ID',
  user_id BIGINT NULL COMMENT '触发事件的用户ID',
  event_type VARCHAR(50) COMMENT '事件类型：view/click/favorite/add_to_cart/purchase等',
  resource_type VARCHAR(50) COMMENT '资源类型',
  resource_id VARCHAR(64) COMMENT '资源ID',
  properties JSON NULL COMMENT '扩展属性，如 device/referrer',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '事件时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户行为事件表（事件流）';
CREATE INDEX idx_events_user ON events(user_id, event_type, created_at);

CREATE TABLE notifications (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '通知ID',
  user_id BIGINT NOT NULL COMMENT '接收用户ID',
  type VARCHAR(50) COMMENT '通知类型',
  payload JSON COMMENT '通知负载，包含跳转信息',
  is_read BOOLEAN DEFAULT FALSE COMMENT '是否已读',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户通知表';
CREATE INDEX idx_notifications_user_unread ON notifications(user_id, is_read);

CREATE TABLE media (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '媒体ID',
  owner_id BIGINT NULL COMMENT '上传者用户ID',
  type VARCHAR(50) COMMENT '媒体类型：image/video/audio',
  url VARCHAR(1024) COMMENT '存储/访问URL',
  storage_key VARCHAR(512) COMMENT '云存储Key',
  mime VARCHAR(100) COMMENT 'MIME类型',
  size BIGINT COMMENT '字节大小',
  thumb_url VARCHAR(1024) COMMENT '缩略图URL（如果有）',
  metadata JSON COMMENT '额外元数据（宽高、时长等）',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (owner_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='媒体资源表（图片/视频/音频）';
CREATE INDEX idx_media_owner ON media(owner_id);

-- Management / moderation / permissions tables

CREATE TABLE roles (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
  name VARCHAR(100) NOT NULL UNIQUE COMMENT '角色名称，如 admin, moderator, user',
  description VARCHAR(255) COMMENT '描述',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

CREATE TABLE permissions (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '权限ID',
  name VARCHAR(100) NOT NULL UNIQUE COMMENT '权限标识，如 content:edit, user:ban',
  description VARCHAR(255) COMMENT '权限描述',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

CREATE TABLE role_permissions (
  role_id BIGINT NOT NULL,
  permission_id BIGINT NOT NULL,
  PRIMARY KEY (role_id, permission_id),
  FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE,
  FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色-权限映射';

CREATE TABLE user_roles (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  assigned_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (user_id, role_id),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户-角色映射';

CREATE TABLE moderation_logs (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '审核日志ID',
  operator_id BIGINT NULL COMMENT '执行审核的管理员ID',
  action VARCHAR(50) COMMENT '操作类型：approve/reject/hide/ban',
  target_type VARCHAR(50) COMMENT '目标类型：recipe/post/comment/user',
  target_id VARCHAR(64) COMMENT '目标ID',
  reason TEXT COMMENT '操作理由',
  metadata JSON COMMENT '扩展信息',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (operator_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审核与操作记录';

CREATE TABLE sensitive_words (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '敏感词ID',
  word VARCHAR(255) NOT NULL COMMENT '敏感词',
  category VARCHAR(50) COMMENT '分类，如 profanity/politics',
  replace_with VARCHAR(255) COMMENT '替换词（可为空）',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='敏感词库，用于内容审核/替换';

CREATE TABLE audit_logs (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '审计日志ID',
  actor_id BIGINT NULL COMMENT '执行者用户ID（可为空）',
  action VARCHAR(100) COMMENT '动作描述',
  target_type VARCHAR(50) COMMENT '目标类型',
  target_id VARCHAR(64) COMMENT '目标ID',
  ip VARCHAR(45) COMMENT '操作IP',
  user_agent VARCHAR(255) COMMENT 'User-Agent',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='敏感操作审计日志';

-- AI / Conversation tables (for 烹饪助手的会话上下文、语音与文本记录)
CREATE TABLE ai_conversations (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '会话ID',
  user_id BIGINT NULL COMMENT '发起会话的用户ID（若匿名可为空）',
  session_key VARCHAR(128) COMMENT '会话键，用于前端恢复会话',
  model VARCHAR(100) COMMENT '使用的模型名',
  model_version VARCHAR(100) COMMENT '模型版本/配置',
  context JSON NULL COMMENT '会话额外上下文（结构化）',
  summary TEXT COMMENT '会话摘要/要点（可供快速检索）',
  last_active_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最近活跃时间',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI 会话元信息表';

-- 可选：将对话消息拆成单独表以便审计/索引
CREATE TABLE ai_messages (
  id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '消息ID',
  conversation_id BIGINT NOT NULL COMMENT '所属会话ID',
  sender VARCHAR(50) COMMENT 'sender: user/system/assistant',
  content TEXT COMMENT '文本内容（若为语音已转写）',
  content_type VARCHAR(50) DEFAULT 'text' COMMENT 'text/audio/image等',
  metadata JSON NULL COMMENT '语音识别替换结果、语速、置信度等',
  token_count INT DEFAULT 0 COMMENT '供计费/分析使用的token数',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '消息时间',
  FOREIGN KEY (conversation_id) REFERENCES ai_conversations(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI 会话消息表（按消息存储）';
CREATE INDEX idx_ai_messages_conv ON ai_messages(conversation_id, created_at);

-- 建议索引与全文搜索 / Elasticsearch 映射说明（非 SQL，仅提示）
-- 1) Elasticsearch 建议：
--    - 对 recipes(name, description), posts(title, body), comments(content) 建立 ES 索引，使用中文分词器（ik_max_word 或 jieba）并开启 ngram/edge_ngram 做联想。
--    - 对 ingredient.name 建立 keyword + text 双字段（keyword 用于过滤，text 用于全文搜索）。
--    - 对 ai_conversations.summary 建立低延迟文本索引以便快速检索历史会话。

-- 2) MySQL FULLTEXT 建议（若不使用 ES）：
--    ALTER TABLE recipes ADD FULLTEXT INDEX ft_recipes_name_desc (name, description);
--    ALTER TABLE posts ADD FULLTEXT INDEX ft_posts_title_body (title, body);

-- 3) 索引要点：
--    - 对高频过滤字段建立普通索引（recipes.category、recipes.created_at、recipe_ingredients.ingredient_id、events.user_id,event_type,recipes.id）。
--    - 对排序/排行榜字段使用聚合表（recipe_stats）以避免扫描大表。

-- 4) ES mapping 示例（简要）：
-- {
--  "mappings": {
--    "properties": {
--      "name": { "type": "text", "analyzer": "ik_max_word", "fields": {"keyword": {"type":"keyword"}} },
--      "description": { "type": "text", "analyzer": "ik_max_word" },
--      "category": { "type": "keyword" },
--      "created_at": { "type": "date" }
--    }
--  }
-- }

-- 说明：建议将 events 写入 Kafka/Message Queue，然后并行写入 ClickHouse（分析）与 ES（可搜索实体）；主 DB 保持事务性写入。


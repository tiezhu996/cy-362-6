CREATE TABLE IF NOT EXISTS operation_records (
  id SERIAL PRIMARY KEY,
  module_name VARCHAR(120) NOT NULL,
  owner_name VARCHAR(80) NOT NULL,
  status VARCHAR(40) NOT NULL,
  metric VARCHAR(40) NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO operation_records (module_name, owner_name, status, metric)
VALUES ('剧本库与DM管理', '运营组', 'ready', '100%');

CREATE TABLE IF NOT EXISTS scripts (
  id SERIAL PRIMARY KEY,
  name VARCHAR(120) NOT NULL,
  type VARCHAR(40) NOT NULL,
  difficulty VARCHAR(20) NOT NULL,
  duration INTEGER NOT NULL,
  player_count INTEGER NOT NULL,
  description TEXT,
  cover_url VARCHAR(255),
  dm_id INTEGER,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS dms (
  id SERIAL PRIMARY KEY,
  name VARCHAR(80) NOT NULL,
  phone VARCHAR(20),
  avatar_url VARCHAR(255),
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS group_sessions (
  id SERIAL PRIMARY KEY,
  script_id INTEGER NOT NULL REFERENCES scripts(id),
  dm_id INTEGER NOT NULL REFERENCES dms(id),
  session_time TIMESTAMP NOT NULL,
  player_names TEXT[] NOT NULL,
  status VARCHAR(20) NOT NULL DEFAULT 'scheduled',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS reviews (
  id SERIAL PRIMARY KEY,
  group_session_id INTEGER NOT NULL REFERENCES group_sessions(id),
  script_id INTEGER NOT NULL REFERENCES scripts(id),
  dm_id INTEGER NOT NULL REFERENCES dms(id),
  player_name VARCHAR(80) NOT NULL,
  script_rating INTEGER NOT NULL CHECK (script_rating BETWEEN 1 AND 5),
  script_comment TEXT,
  dm_rating INTEGER NOT NULL CHECK (dm_rating BETWEEN 1 AND 5),
  dm_comment TEXT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE(group_session_id, player_name)
);

INSERT INTO dms (name, phone) VALUES
  ('张DM', '13800138001'),
  ('李DM', '13800138002'),
  ('王DM', '13800138003');

INSERT INTO scripts (name, type, difficulty, duration, player_count, description, dm_id) VALUES
  ('雾起云浮', '推理', '困难', 300, 6, '民国背景硬核推理本，多重反转挑战脑力。', 1),
  ('月下沙利叶', '恐怖', '中等', 240, 6, '沉浸式恐怖体验，DM演绎至关重要。', 2),
  ('须臾', '推理', '困难', 360, 6, '变格推理神作，逻辑严密环环相扣。', 3),
  ('曦和失焰', '推理', '中等', 300, 7, '机制推理本，互动性强。', 1),
  ('年轮', '情感', '简单', 240, 5, '经典情感本，故事感人至深。', 2);

INSERT INTO group_sessions (script_id, dm_id, session_time, player_names, status) VALUES
  (1, 1, '2025-01-15 14:00:00', ARRAY['小明', '小红', '小刚', '小丽', '小强', '小美'], 'completed'),
  (2, 2, '2025-01-16 19:00:00', ARRAY['阿杰', '阿明', '阿华', '阿丽', '阿芬', '阿龙'], 'completed'),
  (3, 3, '2025-01-17 13:00:00', ARRAY['大伟', '大强', '大美', '大丽', '大华', '大刚'], 'completed'),
  (4, 1, '2025-01-18 14:00:00', ARRAY['小天', '小雨', '小风', '小云', '小雪', '小雷', '小霞'], 'completed'),
  (1, 2, '2025-01-20 19:00:00', ARRAY['张三', '李四', '王五', '赵六', '钱七', '孙八'], 'completed'),
  (2, 3, '2025-01-22 14:00:00', ARRAY['周吴', '郑王', '冯陈', '褚卫', '蒋沈', '韩杨'], 'in_progress'),
  (5, 1, '2025-01-25 14:00:00', ARRAY['朱秦', '尤许', '何吕', '施张', '孔曹'], 'scheduled');

INSERT INTO reviews (group_session_id, script_id, dm_id, player_name, script_rating, script_comment, dm_rating, dm_comment) VALUES
  (1, 1, 1, '小明', 5, '逻辑严密，反转惊艳，是玩过最棒的推理本！', 5, '张DM控场一流，节奏把握恰到好处，沉浸感满分。'),
  (1, 1, 1, '小红', 4, '剧情很棒，但部分线索有点牵强。', 4, '整体不错，希望下次能更耐心解答问题。'),
  (1, 1, 1, '小刚', 5, '硬核推理爱好者必玩，全程烧脑过瘾。', 5, 'DM专业水准高，体验非常好。'),
  (2, 2, 2, '阿杰', 5, '恐怖氛围拉满，吓得我不敢睁眼！', 5, '李DM的演绎太绝了，代入感超强。'),
  (2, 2, 2, '阿明', 4, '恐怖效果不错，推理部分稍弱。', 5, 'DM很会调动气氛，体验极佳。'),
  (3, 3, 3, '大伟', 5, '变格巅峰之作，作者脑洞太大了！', 4, '王DM很认真，但对变格设定解释可以更清晰。'),
  (3, 3, 3, '大强', 5, '逻辑闭环完美，每一个细节都有呼应。', 5, 'DM带本经验丰富，全程丝滑。'),
  (4, 4, 1, '小天', 4, '机制新颖，推理和互动结合得很好。', 5, '张DM对机制理解透彻，讲解清楚。'),
  (5, 1, 2, '张三', 5, '二刷还是觉得精彩，每次都有新发现。', 4, '李DM带本也不错，但比张DM稍逊一筹。'),
  (5, 1, 2, '李四', 4, '经典好本，值得推荐。', 5, 'DM服务态度好，体验很棒。');

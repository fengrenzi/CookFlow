-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('AI 会话消息（按消息存储）', '3', '1', 'AiMessages', 'system/AiMessages/index', 1, 0, 'C', '0', '0', 'system:AiMessages:list', '#', 'admin', sysdate(), '', null, 'AI 会话消息（按消息存储）菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('AI 会话消息（按消息存储）查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:AiMessages:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('AI 会话消息（按消息存储）新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:AiMessages:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('AI 会话消息（按消息存储）修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:AiMessages:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('AI 会话消息（按消息存储）删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:AiMessages:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('AI 会话消息（按消息存储）导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:AiMessages:export',       '#', 'admin', sysdate(), '', null, '');
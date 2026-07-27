-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('AI 会话元信息', '3', '1', 'AiConversations', 'system/AiConversations/index', 1, 0, 'C', '0', '0', 'system:AiConversations:list', '#', 'admin', sysdate(), '', null, 'AI 会话元信息菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('AI 会话元信息查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:AiConversations:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('AI 会话元信息新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:AiConversations:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('AI 会话元信息修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:AiConversations:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('AI 会话元信息删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:AiConversations:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('AI 会话元信息导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:AiConversations:export',       '#', 'admin', sysdate(), '', null, '');
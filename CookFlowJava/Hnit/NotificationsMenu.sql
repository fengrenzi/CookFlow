-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户通知', '3', '1', 'Notifications', 'system/Notifications/index', 1, 0, 'C', '0', '0', 'system:Notifications:list', '#', 'admin', sysdate(), '', null, '用户通知菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户通知查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:Notifications:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户通知新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:Notifications:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户通知修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:Notifications:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户通知删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:Notifications:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('用户通知导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:Notifications:export',       '#', 'admin', sysdate(), '', null, '');
-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('标签映射', '3', '1', 'TagMap', 'system/TagMap/index', 1, 0, 'C', '0', '0', 'system:TagMap:list', '#', 'admin', sysdate(), '', null, '标签映射菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('标签映射查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:TagMap:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('标签映射新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:TagMap:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('标签映射修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:TagMap:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('标签映射删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:TagMap:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('标签映射导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:TagMap:export',       '#', 'admin', sysdate(), '', null, '');
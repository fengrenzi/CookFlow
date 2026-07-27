-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('食材', '3', '1', 'Ingredients', 'system/Ingredients/index', 1, 0, 'C', '0', '0', 'system:Ingredients:list', '#', 'admin', sysdate(), '', null, '食材菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('食材查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:Ingredients:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('食材新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:Ingredients:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('食材修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:Ingredients:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('食材删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:Ingredients:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('食材导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:Ingredients:export',       '#', 'admin', sysdate(), '', null, '');
-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('书籍', '3', '1', 'Books', 'system/Books/index', 1, 0, 'C', '0', '0', 'system:Books:list', '#', 'admin', sysdate(), '', null, '书籍菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('书籍查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:Books:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('书籍新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:Books:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('书籍修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:Books:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('书籍删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:Books:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('书籍导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:Books:export',       '#', 'admin', sysdate(), '', null, '');
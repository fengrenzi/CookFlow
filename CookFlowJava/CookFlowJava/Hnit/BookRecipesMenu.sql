-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('书籍中页码到菜谱的映射', '3', '1', 'BookRecipes', 'system/BookRecipes/index', 1, 0, 'C', '0', '0', 'system:BookRecipes:list', '#', 'admin', sysdate(), '', null, '书籍中页码到菜谱的映射菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('书籍中页码到菜谱的映射查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:BookRecipes:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('书籍中页码到菜谱的映射新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:BookRecipes:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('书籍中页码到菜谱的映射修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:BookRecipes:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('书籍中页码到菜谱的映射删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:BookRecipes:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('书籍中页码到菜谱的映射导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:BookRecipes:export',       '#', 'admin', sysdate(), '', null, '');
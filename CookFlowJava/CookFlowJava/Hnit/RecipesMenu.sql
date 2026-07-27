-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜谱', '3', '1', 'Recipes', 'system/Recipes/index', 1, 0, 'C', '0', '0', 'system:Recipes:list', '#', 'admin', sysdate(), '', null, '菜谱菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜谱查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:Recipes:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜谱新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:Recipes:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜谱修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:Recipes:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜谱删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:Recipes:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜谱导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:Recipes:export',       '#', 'admin', sysdate(), '', null, '');
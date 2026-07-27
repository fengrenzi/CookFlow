-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('食材详情静态内容', '3', '1', 'IngredientDetails', 'system/IngredientDetails/index', 1, 0, 'C', '0', '0', 'system:IngredientDetails:list', '#', 'admin', sysdate(), '', null, '食材详情静态内容菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('食材详情静态内容查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:IngredientDetails:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('食材详情静态内容新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:IngredientDetails:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('食材详情静态内容修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:IngredientDetails:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('食材详情静态内容删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:IngredientDetails:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('食材详情静态内容导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:IngredientDetails:export',       '#', 'admin', sysdate(), '', null, '');
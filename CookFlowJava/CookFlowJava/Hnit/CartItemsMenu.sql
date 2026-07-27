-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('购物车项', '3', '1', 'CartItems', 'system/CartItems/index', 1, 0, 'C', '0', '0', 'system:CartItems:list', '#', 'admin', sysdate(), '', null, '购物车项菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('购物车项查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:CartItems:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('购物车项新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:CartItems:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('购物车项修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:CartItems:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('购物车项删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:CartItems:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('购物车项导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:CartItems:export',       '#', 'admin', sysdate(), '', null, '');
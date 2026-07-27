-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('评论（支持@/回复/状态）', '3', '1', 'Comments', 'system/Comments/index', 1, 0, 'C', '0', '0', 'system:Comments:list', '#', 'admin', sysdate(), '', null, '评论（支持@/回复/状态）菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('评论（支持@/回复/状态）查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:Comments:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('评论（支持@/回复/状态）新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:Comments:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('评论（支持@/回复/状态）修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:Comments:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('评论（支持@/回复/状态）删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:Comments:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('评论（支持@/回复/状态）导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:Comments:export',       '#', 'admin', sysdate(), '', null, '');
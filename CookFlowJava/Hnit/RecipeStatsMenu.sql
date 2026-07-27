-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜谱聚合统计，用于排行榜', '3', '1', 'RecipeStats', 'system/RecipeStats/index', 1, 0, 'C', '0', '0', 'system:RecipeStats:list', '#', 'admin', sysdate(), '', null, '菜谱聚合统计，用于排行榜菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜谱聚合统计，用于排行榜查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:RecipeStats:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜谱聚合统计，用于排行榜新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:RecipeStats:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜谱聚合统计，用于排行榜修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:RecipeStats:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜谱聚合统计，用于排行榜删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:RecipeStats:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜谱聚合统计，用于排行榜导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:RecipeStats:export',       '#', 'admin', sysdate(), '', null, '');
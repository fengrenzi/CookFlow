-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜谱与食材关联（含数量）', '3', '1', 'RecipeIngredients', 'system/RecipeIngredients/index', 1, 0, 'C', '0', '0', 'system:RecipeIngredients:list', '#', 'admin', sysdate(), '', null, '菜谱与食材关联（含数量）菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜谱与食材关联（含数量）查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:RecipeIngredients:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜谱与食材关联（含数量）新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:RecipeIngredients:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜谱与食材关联（含数量）修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:RecipeIngredients:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜谱与食材关联（含数量）删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:RecipeIngredients:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜谱与食材关联（含数量）导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:RecipeIngredients:export',       '#', 'admin', sysdate(), '', null, '');
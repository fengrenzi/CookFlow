-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜谱步骤（有序）', '3', '1', 'RecipeSteps', 'system/RecipeSteps/index', 1, 0, 'C', '0', '0', 'system:RecipeSteps:list', '#', 'admin', sysdate(), '', null, '菜谱步骤（有序）菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜谱步骤（有序）查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:RecipeSteps:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜谱步骤（有序）新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:RecipeSteps:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜谱步骤（有序）修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:RecipeSteps:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜谱步骤（有序）删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:RecipeSteps:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('菜谱步骤（有序）导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:RecipeSteps:export',       '#', 'admin', sysdate(), '', null, '');
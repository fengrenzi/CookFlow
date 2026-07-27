-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('敏感词库，用于内容审核/替换', '3', '1', 'SensitiveWords', 'system/SensitiveWords/index', 1, 0, 'C', '0', '0', 'system:SensitiveWords:list', '#', 'admin', sysdate(), '', null, '敏感词库，用于内容审核/替换菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('敏感词库，用于内容审核/替换查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:SensitiveWords:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('敏感词库，用于内容审核/替换新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:SensitiveWords:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('敏感词库，用于内容审核/替换修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:SensitiveWords:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('敏感词库，用于内容审核/替换删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:SensitiveWords:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('敏感词库，用于内容审核/替换导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:SensitiveWords:export',       '#', 'admin', sysdate(), '', null, '');
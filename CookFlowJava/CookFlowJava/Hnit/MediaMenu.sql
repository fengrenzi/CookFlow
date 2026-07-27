-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('媒体资源（图片/视频/音频）', '3', '1', 'Media', 'system/Media/index', 1, 0, 'C', '0', '0', 'system:Media:list', '#', 'admin', sysdate(), '', null, '媒体资源（图片/视频/音频）菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('媒体资源（图片/视频/音频）查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'system:Media:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('媒体资源（图片/视频/音频）新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'system:Media:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('媒体资源（图片/视频/音频）修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'system:Media:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('媒体资源（图片/视频/音频）删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'system:Media:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('媒体资源（图片/视频/音频）导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'system:Media:export',       '#', 'admin', sysdate(), '', null, '');
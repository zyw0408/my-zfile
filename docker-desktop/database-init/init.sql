-- ZFile 数据库初始化脚本 (MySQL)
SET FOREIGN_KEY_CHECKS = 0;
SET NAMES utf8mb4;

-- 创建业务数据库
CREATE DATABASE IF NOT EXISTS `zfile` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `zfile`;

-- ----------------------------
-- Table structure for storage_source
-- ----------------------------
DROP TABLE IF EXISTS `storage_source`;
CREATE TABLE `storage_source` (
                                  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                                  `enable` bit(1) DEFAULT NULL COMMENT '使用启用',
                                  `enable_cache` bit(1) DEFAULT NULL COMMENT '是否开启缓存',
                                  `name` varchar(255) DEFAULT NULL COMMENT '存储源名称',
                                  `auto_refresh_cache` bit(1) DEFAULT NULL COMMENT '是否开启缓存自动刷新',
                                  `type` varchar(64) DEFAULT NULL COMMENT '存储源类型',
                                  `search_enable` bit(1) DEFAULT NULL COMMENT '是否开启搜索',
                                  `search_ignore_case` bit(1) DEFAULT NULL COMMENT '搜索是否忽略大小写',
                                  `order_num` int DEFAULT NULL COMMENT '排序',
                                  `default_switch_to_img_mode` bit(1) DEFAULT NULL COMMENT '是否默认开启图片模式',
                                  `remark` text COMMENT '备注',
                                  `key` varchar(64) DEFAULT NULL COMMENT '存储源别名',
                                  `enable_file_operator` bit(1) DEFAULT NULL COMMENT '是否启用文件操作',
                                  `search_mode` varchar(32) DEFAULT NULL COMMENT '搜索模式, 仅从缓存中搜索还是直接搜索',
                                  `enable_file_anno_operator` bit(1) DEFAULT NULL COMMENT '是否允许匿名进行文件操作',
                                  `compatibility_readme` bit(1) DEFAULT NULL,
                                  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储源设置';

-- ----------------------------
-- Table structure for filter_config
-- ----------------------------
DROP TABLE IF EXISTS `filter_config`;
CREATE TABLE `filter_config` (
                                 `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                                 `storage_id` int DEFAULT NULL COMMENT '存储源 ID',
                                 `expression` varchar(255) DEFAULT NULL COMMENT '路径表达式',
                                 `description` varchar(255) DEFAULT NULL COMMENT '表达式描述',
                                 `mode` varchar(255) DEFAULT NULL,
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='过滤设置';

-- ----------------------------
-- Table structure for short_link
-- ----------------------------
DROP TABLE IF EXISTS `short_link`;
CREATE TABLE `short_link` (
                              `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                              `short_key` varchar(255) DEFAULT NULL COMMENT '短链 key',
                              `url` text COMMENT '链接 url',
                              `create_date` datetime DEFAULT NULL COMMENT '创建时间',
                              `storage_id` int DEFAULT NULL COMMENT '存储源 ID',
                              `expire_date` datetime DEFAULT NULL,
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='短链设置';

-- ----------------------------
-- Table structure for storage_source_config
-- ----------------------------
DROP TABLE IF EXISTS `storage_source_config`;
CREATE TABLE `storage_source_config` (
                                         `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                                         `name` varchar(255) DEFAULT NULL COMMENT '存储源属性 name',
                                         `type` text COMMENT '存储源类型',
                                         `title` varchar(255) DEFAULT NULL COMMENT '存储源属性名称',
                                         `storage_id` int DEFAULT NULL COMMENT '存储源 ID',
                                         `value` text COMMENT '存储源属性 value',
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='存储源属性设置';

-- ----------------------------
-- Table structure for system_config
-- ----------------------------
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config` (
                                 `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                                 `name` varchar(255) DEFAULT NULL COMMENT '系统设置属性 name',
                                 `value` text COMMENT '系统设置属性 value',
                                 `title` varchar(255) DEFAULT NULL COMMENT '系统设置属性标题',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统设置';

-- ----------------------------
-- Table structure for password_config
-- ----------------------------
DROP TABLE IF EXISTS `password_config`;
CREATE TABLE `password_config` (
                                   `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                                   `storage_id` int DEFAULT NULL COMMENT '存储源 ID',
                                   `expression` varchar(255) DEFAULT NULL COMMENT '路径表达式',
                                   `password` varchar(255) DEFAULT NULL COMMENT '密码',
                                   `description` varchar(255) DEFAULT NULL COMMENT '表达式描述',
                                   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='密码文件夹设置';

-- ----------------------------
-- Table structure for readme_config
-- ----------------------------
DROP TABLE IF EXISTS `readme_config`;
CREATE TABLE `readme_config` (
                                 `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                                 `storage_id` int DEFAULT NULL COMMENT '存储源 ID',
                                 `expression` varchar(255) DEFAULT NULL COMMENT '路径表达式',
                                 `description` varchar(255) DEFAULT NULL COMMENT '表达式描述',
                                 `readme_text` text COMMENT 'readme 文本内容, 支持 md 语法.',
                                 `display_mode` varchar(32) DEFAULT NULL COMMENT '显示模式，支持顶部显示: top, 底部显示:bottom, 弹窗显示: dialog',
                                 `path_mode` varchar(32) DEFAULT NULL,
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='readme 文档配置';

-- ----------------------------
-- Table structure for download_log
-- ----------------------------
DROP TABLE IF EXISTS `download_log`;
CREATE TABLE `download_log` (
                                `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                                `path` varchar(2048) DEFAULT NULL COMMENT '文件路径',
                                `storage_key` varchar(64) DEFAULT NULL COMMENT '存储源 key',
                                `create_time` datetime DEFAULT NULL COMMENT '访问时间',
                                `ip` varchar(64) DEFAULT NULL COMMENT '访问 ip',
                                `user_agent` varchar(2048) DEFAULT NULL COMMENT '访问 user_agent',
                                `referer` varchar(2048) DEFAULT NULL COMMENT '访问 referer',
                                `short_key` varchar(255) DEFAULT NULL COMMENT '短链 key',
                                `download_type` varchar(32) DEFAULT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件下载日志';

-- ----------------------------
-- Table structure for permission_config
-- ----------------------------
DROP TABLE IF EXISTS `permission_config`;
CREATE TABLE `permission_config` (
                                     `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                                     `operator` varchar(32) DEFAULT NULL COMMENT '操作',
                                     `allow_admin` bit(1) DEFAULT NULL COMMENT '允许管理员操作',
                                     `allow_anonymous` bit(1) DEFAULT NULL COMMENT '允许匿名用户操作',
                                     `storage_id` int DEFAULT NULL COMMENT '存储源 ID',
                                     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限设置表';

-- ----------------------------
-- Table structure for login_log
-- ----------------------------
DROP TABLE IF EXISTS `login_log`;
CREATE TABLE `login_log` (
                             `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
                             `username` varchar(255) DEFAULT NULL COMMENT '用户名',
                             `nickname` varchar(255) DEFAULT NULL COMMENT '用户昵称',
                             `password` varchar(255) DEFAULT NULL COMMENT '密码',
                             `create_time` datetime DEFAULT NULL COMMENT '登录时间',
                             `ip` varchar(64) DEFAULT NULL COMMENT '登录 ip',
                             `user_agent` varchar(2048) DEFAULT NULL COMMENT '登录 user_agent',
                             `referer` varchar(2048) DEFAULT NULL COMMENT '登录 referer',
                             `result` varchar(255) DEFAULT NULL COMMENT '登录结果',
                             PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志';

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                        `id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
                        `username` varchar(255) DEFAULT NULL COMMENT '用户名',
                        `nickname` varchar(255) DEFAULT NULL COMMENT '昵称',
                        `password` varchar(32) DEFAULT NULL COMMENT '用户密码',
                        `enable` bit(1) DEFAULT NULL COMMENT '是否启用',
                        `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                        `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                        `default_permissions` text COMMENT '默认权限',
                        `salt` varchar(32) DEFAULT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- Table structure for user_storage_source
-- ----------------------------
DROP TABLE IF EXISTS `user_storage_source`;
CREATE TABLE `user_storage_source` (
                                       `id` int NOT NULL AUTO_INCREMENT COMMENT '用户存储源ID',
                                       `user_id` int DEFAULT NULL COMMENT '用户ID',
                                       `storage_source_id` int DEFAULT NULL COMMENT '存储源ID',
                                       `root_path` text COMMENT '根路径',
                                       `enable` bit(1) DEFAULT NULL COMMENT '是否启用',
                                       `permissions` text COMMENT '权限列表',
                                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户存储源表';

-- ----------------------------
-- Table structure for sso_config
-- ----------------------------
DROP TABLE IF EXISTS `sso_config`;
CREATE TABLE `sso_config` (
                              `id` int NOT NULL AUTO_INCREMENT COMMENT '主键 ID',
                              `provider` varchar(255) NOT NULL COMMENT '服务商简称',
                              `name` varchar(255) NOT NULL COMMENT '名称',
                              `icon` text NOT NULL COMMENT '图标, 支持url和base64',
                              `client_id` varchar(255) NOT NULL COMMENT 'Client ID',
                              `client_secret` varchar(255) NOT NULL COMMENT 'Client Secret',
                              `auth_url` varchar(255) NOT NULL COMMENT '授权端点',
                              `token_url` varchar(255) NOT NULL COMMENT 'Token 端点',
                              `user_info_url` varchar(255) NOT NULL COMMENT '用户信息端点',
                              `scope` varchar(255) NOT NULL COMMENT '授权范围, 可填多个, 用空格隔开',
                              `binding_field` varchar(255) NOT NULL COMMENT '单点登录系统中用户与业务系统中用户的绑定字段',
                              `enabled` bit(1) NOT NULL COMMENT '服务商是否启用',
                              `order_num` int NOT NULL DEFAULT '0' COMMENT '排序字段, 越小越靠前',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='单点登录 (OIDC/OAuth2.0) 配置信息表';

-- ----------------------------
-- Records of system_config
-- ----------------------------
INSERT INTO `system_config` (`name`, `title`) VALUES ('siteName', '站点名称');
INSERT INTO `system_config` (`name`, `title`) VALUES ('username', '管理员账号');
INSERT INTO `system_config` (`name`, `title`) VALUES ('password', '管理员密码');
INSERT INTO `system_config` (`name`, `title`) VALUES ('domain', '站点域名');
INSERT INTO `system_config` (`name`, `title`) VALUES ('customCss', '自定义 CSS');
INSERT INTO `system_config` (`name`, `title`) VALUES ('customJs', '自定义 JS (可用于统计代码)');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('tableSize', '表格大小', 'small');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('showDocument', '是否显示文档', 'true');
INSERT INTO `system_config` (`name`, `title`) VALUES ('announcement', '网站公告');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('showAnnouncement', '是否显示网站公告', 'true');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('layout', '页面布局', 'full');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('showLinkBtn', '是否显示生成直链按钮', 'true');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('showShortLink', '是否显示短链', 'true');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('showPathLink', '是否显示路径直链', 'true');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('installed', '是否已初始化安装', 'false');
INSERT INTO `system_config` (`name`, `title`) VALUES ('avatar', '头像地址');
INSERT INTO `system_config` (`name`, `title`) VALUES ('icp', 'ICP 备案号');
INSERT INTO `system_config` (`name`, `title`) VALUES ('customVideoSuffix', '自定义视频文件后缀格式');
INSERT INTO `system_config` (`name`, `title`) VALUES ('customImageSuffix', '自定义图像文件后缀格式');
INSERT INTO `system_config` (`name`, `title`) VALUES ('customAudioSuffix', '自定义音频文件后缀格式');
INSERT INTO `system_config` (`name`, `title`) VALUES ('customTextSuffix', '自定义文本文件后缀格式');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('directLinkPrefix', '直链前缀地址', 'directlink');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('refererType', '直链 Referer 防盗链类型', 'off');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('refererAllowEmpty', '直链 Referer 是否允许为空', 'true');
INSERT INTO `system_config` (`name`, `title`) VALUES ('refererValue', '直链 Referer 值');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('loginVerifyMode', '登陆验证方式，支持验证码和 2FA 认证', 'off');
INSERT INTO `system_config` (`name`, `title`) VALUES ('loginVerifySecret', '登陆验证 Secret');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('rootShowStorage', '根目录是否显示所有存储源', 'true');
INSERT INTO `system_config` (`name`, `title`) VALUES ('frontDomain', '前端域名，前后端分离情况下需要配置');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('recordDownloadLog', '是否记录下载日志', 'true');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('showLogin', '是否在前台显示登陆按钮', 'true');
INSERT INTO `system_config` (`name`, `title`) VALUES ('rsaHexKey', 'RSA 算法 HEX 格式密钥');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('webdavEnable', '启用 WebDAV 服务', 'false');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('webdavProxy', '是否启用 WebDAV 服务器中转下载', 'true');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('webdavUsername', 'WebDAV 账号', '');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('webdavPassword', 'WebDAV 密码', '');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('allowPathLinkAnonAccess', '是否允许路径直链可直接访问', 'false');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('maxShowSize', '默认最大显示文件数', '1000');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('loadMoreSize', '每次加载更多文件数', '50');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('siteHomeName', '站点 Home 名称', '首页');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('siteHomeLogo', '站点 Home Logo', NULL);
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('siteHomeLogoLink', '站点 Logo 打开链接', '/');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('siteHomeLogoTargetMode', '站点 Logo 链接打开方式', '_blank');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('defaultSortField', '默认排序字段', 'name');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('defaultSortOrder', '默认排序字段', 'asc');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('linkLimitSecond', '限制直链下载秒数', '3600');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('linkDownloadLimit', '限制直链下载次数', '10000');
INSERT INTO `system_config` (`name`, `title`) VALUES ('faviconUrl', '网站 favicon 图标地址');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('linkExpireTimes', '短链过期时间设置', '[{ "value": 1, "unit": "d", "seconds": 86400 }]');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('defaultSavePwd', '是否默认记住密码', 'false');
INSERT INTO `system_config` (`name`, `title`) VALUES ('onlyOfficeSecret', 'OnlyOffice Secret，用于生成 JWT Token');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('enableHoverMenu', '是否启用悬浮菜单', 'true');
INSERT INTO `system_config` (`name`, `title`) VALUES ('accessIpBlocklist', 'ip 黑名单');
INSERT INTO `system_config` (`name`, `title`) VALUES ('accessUaBlocklist', 'ua 黑名单');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('loginImgVerify', '是否启用登录图片验证码', 'false');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('adminTwoFactorVerify', '是否为管理员启用双因素认证', 'false');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('siteAdminLogoTargetMode', '管理员页面点击 Logo 回到首页打开方式', '_blank');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('siteAdminVersionOpenChangeLog', '管理员页面点击版本号打开更新日志', 'true');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('webdavAllowAnonymous', '是否允许匿名访问', 'false');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('loginLogMode', '登录日志模式', 'ignoreAllPwd');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('mobileLayout', '移动端布局', 'full');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('customOfficeSuffix', 'Office 预览类型', 'doc,docx,csv,xls,xlsx,ppt,pptx,xlsm');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('guestIndexHtml', '匿名用户首页显示内容', '');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('mobileFileClickMode', '移动端默认文件点击模式', 'dbclick');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('forceBackendAddress', '强制后端地址', '');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('kkFileViewUrl', 'kkFileView 地址', '');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('customKkFileViewSuffix', 'kkFileView 预览类型', '3dm,3ds,3mf,7z,bim,bpmn,brep,cf2,dcm,dng,doc,docx,dot,dotm,dotx,dps,drawio,dwf,dwfx,dwg,dwt,dxf,emf,eml,eps,et,ett,fbx,fcstd,flv,fodt,fods,glb,gltf,gzip,ifc,iges,jar,jfif,jpg,js,md,mkv,mov,mp3,mp4,mpeg,mpg,obj,odp,ods,odt,ofd,off,ogg,otp,ots,ott,pages,pdf,php,plt,ply,png,ppt,pptx,psd,py,rar,rm,rmvb,rtf,six,stl,step,svg,swf,tar,tga,tif,tiff,ts,tsv,txt,vsd,vsdx,wav,webm,webp,wmf,wmv,wps,wpt,wrl,xla,xlam,xls,xlsm,xlsx,xlt,xltm,xmind,xml,zip');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('kkFileViewOpenMode', 'kkFileView 预览方式', 'iframe');
INSERT INTO `system_config` (`name`, `title`, `value`) VALUES ('mobileShowSize', '移动端显示文件大小', 'true');

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (0, 'template', '虚拟新用户', NULL, b'1', now(), now(), NULL, NULL);
INSERT INTO `user` VALUES (1, (SELECT `value` FROM `system_config` WHERE `name` = 'username'), '管理员', (SELECT `value` FROM `system_config` WHERE `name` = 'password'), b'1', current_timestamp, NULL, NULL, NULL);
INSERT INTO `user` VALUES (2, 'guest', '匿名用户', NULL, b'1', current_timestamp, NULL, NULL, NULL);

-- ----------------------------
-- Updates
-- ----------------------------
UPDATE `readme_config` SET `path_mode` = 'relative' WHERE `path_mode` IS NULL;
UPDATE `system_config` SET `value` = 'https://office.zfile.vip' WHERE `value` = 'http://office.zfile.vip';
UPDATE `system_config` SET `value` = '' WHERE `name` = 'loginVerifySecret';
UPDATE `system_config` SET `value` = 'false' WHERE `name` = 'loginImgVerify';
UPDATE `system_config` SET `value` = 'false' WHERE `name` = 'adminTwoFactorVerify';
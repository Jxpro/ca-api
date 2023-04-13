CREATE TABLE `request` (
	`id` INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	`user_id` INT UNSIGNED NOT NULL COMMENT '申请证书的用户ID',
	`subject_id` INT UNSIGNED DEFAULT NULL COMMENT '主体ID',
	`license_id` INT UNSIGNED DEFAULT NULL COMMENT '许可证ID',
	`key_id` INT UNSIGNED DEFAULT NULL COMMENT '公钥ID',
	`serial_number` BIGINT UNSIGNED DEFAULT NULL COMMENT '证书序列号',
	`not_before` DATETIME DEFAULT NULL COMMENT '证书颁发时间',
	`not_after` DATETIME DEFAULT NULL COMMENT '证书过期时间',
	`revoke_time` DATETIME DEFAULT NULL COMMENT '证书撤销时间',
	`state` ENUM ( '待完善', '待审核', '未通过', '已通过', '已撤销' ) NOT NULL COMMENT '证书状态',
	UNIQUE KEY `subject_id`(`subject_id`),
	UNIQUE KEY `license_id`(`license_id`),
	UNIQUE KEY `key_id`(`key_id`),
	CONSTRAINT use_fk FOREIGN KEY ( user_id ) REFERENCES `user` ( id ),
	CONSTRAINT sub_fk FOREIGN KEY ( subject_id ) REFERENCES `subject` ( id ) ON DELETE SET NULL,
	CONSTRAINT lic_fk FOREIGN KEY ( license_id ) REFERENCES `license` ( id ) ON DELETE SET NULL,
	CONSTRAINT key_fk FOREIGN KEY ( key_id ) REFERENCES `user_key` ( id ) ON DELETE SET NULL
) ENGINE = INNODB AUTO_INCREMENT = 32;

CREATE TABLE `user` (
	`id` INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT COMMENT '用户id，自增',
	`nickname` VARCHAR ( 16 ) NOT NULL COMMENT '用户昵称，最长16个字符',
	`username` VARCHAR ( 32 ) NOT NULL COMMENT '用户名，最长32个字符',
	`email` VARCHAR ( 64 ) NOT NULL COMMENT '邮箱，最长64个字符',
	`password` CHAR ( 64 ) NOT NULL COMMENT '密码，使用SHA256，结果固定64字符',
	`role` ENUM ( 'admin', 'user' ) NOT NULL COMMENT '权限，admin可以审核证书',
	UNIQUE KEY `username` ( `username` )
) ENGINE = INNODB AUTO_INCREMENT = 32;

CREATE TABLE `subject` (
	`id` INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	`common_name` VARCHAR ( 255 ) NOT NULL COMMENT '姓名',
	`organization` VARCHAR ( 255 ) DEFAULT NULL COMMENT '组织',
	`organizational_unit` VARCHAR ( 255 ) DEFAULT NULL COMMENT '部门',
	`country` VARCHAR ( 255 ) DEFAULT NULL COMMENT '国家',
	`state_or_province_name` VARCHAR ( 255 ) DEFAULT NULL COMMENT '省份',
	`email` VARCHAR ( 255 ) DEFAULT NULL COMMENT '邮箱'
) ENGINE = INNODB AUTO_INCREMENT = 32;

CREATE TABLE `user_key` (
	`id` INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	`algorithm` ENUM ( 'RSA', 'EC' ) NOT NULL COMMENT '签名算法：RSA或EC',
	`curve_name` ENUM ( 'secp256k1', 'sm2p256v1', 'prime256v1' ) DEFAULT NULL COMMENT '曲线名称，暂限三选一：secp256k1，sm2p256v1，prime256v1',
	`key_size` ENUM ( '2048', '4096' ) DEFAULT NULL COMMENT 'RSA公钥长度，2048或4096',
	`param1` VARCHAR ( 1024 ) DEFAULT NULL COMMENT 'RSA公钥n或ECC Point x',
	`param2` VARCHAR ( 1024 ) DEFAULT NULL COMMENT 'RSA公钥e或ECC Point y'
) ENGINE = INNODB AUTO_INCREMENT = 32;

CREATE TABLE `license` (
	`id` INT UNSIGNED NOT NULL PRIMARY KEY AUTO_INCREMENT,
	`origin_name` VARCHAR ( 255 ) NOT NULL COMMENT '文件原名',
	`content_hash` CHAR ( 64 ) NOT NULL COMMENT '内容哈希，作为保存文件的名称'
) ENGINE = INNODB AUTO_INCREMENT = 32;
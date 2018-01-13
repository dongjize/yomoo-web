DROP SCHEMA IF EXISTS `yomoo`;
CREATE SCHEMA IF NOT EXISTS `yomoo`
  DEFAULT CHARACTER SET utf8;
USE `yomoo`;

DROP TABLE IF EXISTS `login_ticket`;
CREATE TABLE `login_ticket` (
  `id`         INT UNSIGNED       AUTO_INCREMENT PRIMARY KEY,
  `expired`    TIMESTAMP NOT NULL,
  `status`     TINYINT,
  `ticket`     VARCHAR(200),
  `created_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id`         INT UNSIGNED                                               AUTO_INCREMENT PRIMARY KEY,
  `phone`      CHAR(11) UNIQUE                                   NOT NULL,
  `password`   VARCHAR(100)                                      NOT NULL,
  `name`       VARCHAR(100),
  `salt`       VARCHAR(200),
  `type`       ENUM ('farmer', 'vendor', 'butcher', 'supporter') NOT NULL,
  `intro`      VARCHAR(500),
  `created_at` TIMESTAMP                                         NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP                                         NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CHECK (length(`phone`) = 11),
  CHECK (length(`password`) >= 6 AND length(`password`) <= 20)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

DROP TABLE IF EXISTS `farmer`;
CREATE TABLE `farmer` (
  `id`            INT UNSIGNED PRIMARY KEY,
  `village`       VARCHAR(100),
  `group_`        VARCHAR(100),
  `street_num`    VARCHAR(100),
  `livestock`     VARCHAR(100),
  `exp_livestock` VARCHAR(100),
  `created_at`    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT `fk_farmer_id` FOREIGN KEY (`id`) REFERENCES `user` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

# 饲料表
DROP TABLE IF EXISTS `fodder`;
CREATE TABLE `fodder` (
  `id`          INT UNSIGNED          AUTO_INCREMENT PRIMARY KEY,
  `name`        VARCHAR(100) NOT NULL,
  `description` VARCHAR(500),
  `fodder_spec` VARCHAR(50)  NOT NULL,
  `created_at`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

# 饲料进货表
DROP TABLE IF EXISTS `purchase`;
CREATE TABLE `purchase` (
  `id`         INT UNSIGNED          AUTO_INCREMENT PRIMARY KEY,
  `vendor_id`  INT UNSIGNED NOT NULL,
  `tips`       VARCHAR(200),
  `created_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

# 饲料进货条目（fodder和purchase的中间表）
DROP TABLE IF EXISTS `purchase_entry`;
CREATE TABLE `purchase_entry` (
  `id`             INT UNSIGNED          AUTO_INCREMENT PRIMARY KEY,
  `fodder_id`      INT UNSIGNED NOT NULL,
  `purchase_id`    INT UNSIGNED NOT NULL,
  `purchase_price` INT          NOT NULL,
  `quantity`       INT          NOT NULL,
  `sell_price`     FLOAT,
  `created_at`     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT `fk_purchase_entry_fodder_id` FOREIGN KEY (`fodder_id`) REFERENCES `fodder` (`id`),
  CONSTRAINT `fk_purchase_entry_purchase_id` FOREIGN KEY (`purchase_id`) REFERENCES `purchase` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

# 订单信息
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id`         INT UNSIGNED                    AUTO_INCREMENT PRIMARY KEY,
  `farmer_id`  INT UNSIGNED           NOT NULL,
  `vendor_id`  INT UNSIGNED           NOT NULL,
  `order_type` ENUM ('payed', 'owed') NOT NULL,
  `tips`       VARCHAR(200),
  `created_at` TIMESTAMP              NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT `fk_fodder_order_farmer_id` FOREIGN KEY (`farmer_id`) REFERENCES `farmer` (`id`),
  CONSTRAINT `fk_fodder_order_vendor_id` FOREIGN KEY (`vendor_id`) REFERENCES `user` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

# 饲料和销售商中间表（进货信息）
DROP TABLE IF EXISTS `fodder_vendor`;
CREATE TABLE `fodder_vendor` (
  `id`         INT UNSIGNED          AUTO_INCREMENT PRIMARY KEY,
  `fodder_id`  INT UNSIGNED NOT NULL,
  `vendor_id`  INT UNSIGNED NOT NULL,
  `sell_price` FLOAT,
  `stock`      INT          NOT NULL,
  `created_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT `fk_fodder_vendor_fodder_id` FOREIGN KEY (`fodder_id`) REFERENCES `user` (`id`),
  CONSTRAINT `fk_fodder_vendor_vendor_id` FOREIGN KEY (`vendor_id`) REFERENCES `user` (`id`),
  CHECK (stock >= 0)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

# 订单的一个条目（order和fodder_vendor的中间表）
DROP TABLE IF EXISTS `order_entry`;
CREATE TABLE `order_entry` (
  `id`         INT UNSIGNED          AUTO_INCREMENT PRIMARY KEY,
  `fv_id`      INT UNSIGNED NOT NULL,
  `order_id`   INT UNSIGNED NOT NULL,
  `quantity`   INT          NOT NULL,
  `sell_price` FLOAT        NOT NULL,
  `created_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT `fk_order_entry_fodder_vendor_id` FOREIGN KEY (`fv_id`) REFERENCES `fodder_vendor` (`id`),
  CONSTRAINT `fk_order_entry_order_id` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

# 养殖技术表
DROP TABLE IF EXISTS `breeding_info`;
CREATE TABLE `breeding_info` (
  `id`           INT UNSIGNED          AUTO_INCREMENT PRIMARY KEY,
  `publisher_id` INT UNSIGNED NOT NULL,
  `title`        VARCHAR(100) NOT NULL,
  `content`      TEXT         NOT NULL,
  `created_at`   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT `fk_breeding_info_publisher_id` FOREIGN KEY (`publisher_id`) REFERENCES `user` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

# 养殖技术需求表
DROP TABLE IF EXISTS `breeding_info_demand`;
CREATE TABLE `breeding_info_demand` (
  `id`           INT UNSIGNED          AUTO_INCREMENT PRIMARY KEY,
  `publisher_id` INT UNSIGNED NOT NULL,
  `title`        VARCHAR(100) NOT NULL,
  `content`      TEXT         NOT NULL,
  `created_at`   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT `fk_breeding_info_demand_publisher_id` FOREIGN KEY (`publisher_id`) REFERENCES `user` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

# 牲畜需求信息表
DROP TABLE IF EXISTS `livestock_demand`;
CREATE TABLE `livestock_demand` (
  `id`           INT UNSIGNED          AUTO_INCREMENT PRIMARY KEY,
  `publisher_id` INT UNSIGNED NOT NULL,
  `title`        VARCHAR(100) NOT NULL,
  `content`      TEXT,
  `created_at`   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT `fk_livestock_demand_publisher_id` FOREIGN KEY (`publisher_id`) REFERENCES `user` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

#
# 删除外键
# 语法： ALTER TABLE table-name DROP FOREIGN KEY key-id;
# 例：   ALTER TABLE `tb_active` DROP FOREIGN KEY `FK_ID`
#

INSERT INTO `user` (`id`, `phone`, `password`, `name`, `type`) VALUES
  (1, '13838383838', '123456', '狗蛋', 'farmer'),
  (2, '13000000001', '123456', '狗蛋', 'farmer'),
  (3, '13000000002', '123456', '狗蛋', 'farmer'),
  (4, '13000000003', '123456', '狗蛋', 'farmer'),
  (5, '13000000004', '123456', '狗蛋', 'farmer'),
  (6, '13000000005', '123456', '狗蛋', 'farmer'),
  (7, '13000000006', '123456', '狗蛋', 'farmer'),
  (8, '13000000007', '123456', '狗蛋', 'farmer'),
  (9, '13000000008', '123456', '狗蛋', 'farmer'),
  (10, '13000000009', '123456', '狗蛋', 'farmer'),
  (11, '13000000010', '123456', '狗蛋', 'farmer'),
  (12, '13000000011', '123456', '狗蛋', 'farmer'),
  (13, '13000000012', '123456', '狗蛋', 'farmer'),
  (14, '13000000013', '123456', '狗蛋', 'farmer'),
  (15, '13000000014', '123456', '狗蛋', 'farmer'),
  (16, '13000000015', '123456', '狗蛋', 'farmer'),
  (17, '13000000016', '123456', '狗蛋', 'farmer'),
  (18, '13000000017', '123456', '狗蛋', 'farmer'),
  (19, '13000000018', '123456', '狗蛋', 'farmer'),
  (20, '13000000019', '123456', '狗蛋', 'farmer'),
  (21, '13000000020', '123456', '狗蛋', 'farmer'),
  (22, '13000000021', '123456', '狗蛋', 'farmer'),
  (23, '13000000022', '123456', '狗蛋', 'farmer'),
  (24, '13000000023', '123456', '二狗', 'vendor'),
  (25, '13000000024', '123456', '二傻', 'vendor'),
  (26, '13000000025', '123456', '三胖', 'vendor'),
  (27, '13000000026', '123456', '王二麻子', 'butcher'),
  (28, '13000000027', '123456', '傻根', 'butcher'),
  (29, '13000000028', '123456', '雷锋', 'supporter'),
  (30, '13000000029', '123456', '王小二', 'supporter');

INSERT INTO `farmer` (`id`, `village`, `group_`, `street_num`, `livestock`, `exp_livestock`) VALUES
  (1, '哈哈哈', '呵呵呵', '嘿嘿嘿嘿', '10头牛', '20头牛'),
  (2, '哈哈哈', '呵呵呵', '嘿嘿嘿嘿', '10头牛', '20头牛'),
  (3, '哈哈哈', '呵呵呵', '嘿嘿嘿嘿', '10头牛', '20头牛'),
  (4, '哈哈哈', '呵呵呵', '嘿嘿嘿嘿', '10头牛', '20头牛'),
  (5, '哈哈哈', '呵呵呵', '嘿嘿嘿嘿', '10头牛', '20头牛'),
  (6, '哈哈哈', '呵呵呵', '嘿嘿嘿嘿', '10头牛', '20头牛'),
  (7, '哈哈哈', '呵呵呵', '嘿嘿嘿嘿', '10头牛', '20头牛'),
  (8, '哈哈哈', '呵呵呵', '嘿嘿嘿嘿', '10头牛', '20头牛'),
  (9, '哈哈哈', '呵呵呵', '嘿嘿嘿嘿', '10头牛', '20头牛'),
  (10, '哈哈哈', '呵呵呵', '嘿嘿嘿嘿', '10头牛', '20头牛'),
  (11, '哈哈哈', '呵呵呵', '嘿嘿嘿嘿', '10头牛', '20头牛'),
  (12, '哈哈哈', '呵呵呵', '嘿嘿嘿嘿', '10头牛', '20头牛'),
  (13, '哈哈哈', '呵呵呵', '嘿嘿嘿嘿', '10头牛', '20头牛'),
  (14, '哈哈哈', '呵呵呵', '嘿嘿嘿嘿', '10头牛', '20头牛'),
  (15, '哈哈哈', '呵呵呵', '嘿嘿嘿嘿', '10头牛', '20头牛'),
  (16, '哈哈哈', '呵呵呵', '嘿嘿嘿嘿', '10头牛', '20头牛'),
  (17, '哈哈哈', '呵呵呵', '嘿嘿嘿嘿', '10头牛', '20头牛'),
  (18, '哈哈哈', '呵呵呵', '嘿嘿嘿嘿', '10头牛', '20头牛'),
  (19, '哈哈哈', '呵呵呵', '嘿嘿嘿嘿', '10头牛', '20头牛'),
  (20, '哈哈哈', '呵呵呵', '嘿嘿嘿嘿', '10头牛', '20头牛'),
  (21, '哈哈哈', '呵呵呵', '嘿嘿嘿嘿', '10头牛', '20头牛'),
  (22, '哈哈哈', '呵呵呵', '嘿嘿嘿嘿', '10头牛', '20头牛'),
  (23, '哈哈哈', '呵呵呵', '嘿嘿嘿嘿', '10头牛', '20头牛');

INSERT INTO `fodder` (`name`, `description`, `fodder_spec`) VALUES
  ('金坷垃', '小麦亩产一千八', '5kg'),
  ('金坷垃', '小麦亩产一千八', '20kg'),
  ('金坷垃', '小麦亩产一千八', '40kg'),
  ('银坷垃', '小麦亩产一千八', '5kg'),
  ('银坷垃', '小麦亩产一千八', '20kg'),
  ('银坷垃', '小麦亩产一千八', '40kg'),
  ('铜坷垃', '小麦亩产一千八', '5kg'),
  ('铜坷垃', '小麦亩产一千八', '20kg'),
  ('铜坷垃', '小麦亩产一千八', '40kg'),
  ('铁坷垃', '小麦亩产一千八', '5kg'),
  ('铁坷垃', '小麦亩产一千八', '20kg'),
  ('铁坷垃', '小麦亩产一千八', '40kg'),
  ('土坷垃', '小麦亩产一千八', '5kg'),
  ('土坷垃', '小麦亩产一千八', '20kg'),
  ('土坷垃', '小麦亩产一千八', '40kg'),
  ('shi坷垃', '小麦亩产一千八', '5kg'),
  ('shi坷垃', '小麦亩产一千八', '20kg'),
  ('shi坷垃', '小麦亩产一千八', '40kg');

INSERT INTO `breeding_info_demand` (`publisher_id`, `title`, `content`) VALUES
  (1, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (2, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (2, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (2, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (3, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (3, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (3, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o');


INSERT INTO `breeding_info` (`publisher_id`, `title`, `content`) VALUES
  (24, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (25, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (26, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (24, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (25, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (26, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (24, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (25, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (26, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (24, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (25, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (26, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (24, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (25, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (26, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (24, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (25, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (26, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (24, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (25, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (26, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (24, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (25, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (26, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (24, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (25, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (26, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (24, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (25, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (26, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (24, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (25, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (26, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (24, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (25, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (26, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o'),
  (24, 'aaa', 'pwoighnkaejrgbahe;pfigbn;o');
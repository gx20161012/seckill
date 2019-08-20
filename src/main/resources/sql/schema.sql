CREATE DATABASE seckill DEFAULT CHARACTER SET utf8;

USE seckill;

DROP TABLE IF EXISTS `product`;
DROP TABLE IF EXISTS `order`;

-- 创建秒杀商品表
CREATE TABLE `product`(
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品ID',
  `title` varchar (1000) DEFAULT NULL COMMENT '商品标题',
  `image` varchar (1000) DEFAULT NULL COMMENT '商品图片',
  `price` decimal (10,2) DEFAULT NULL COMMENT '商品原价格',
  `cost_price` decimal (10,2) DEFAULT NULL COMMENT '商品秒杀价格',
  `stock_count` bigint DEFAULT NULL COMMENT '剩余库存数量',
  `start_time` timestamp NOT NULL DEFAULT '1970-02-01 00:00:01' COMMENT '秒杀开始时间',
  `end_time` timestamp NOT NULL DEFAULT '1970-02-01 00:00:01' COMMENT '秒杀结束时间',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_start_time` (`start_time`),
  KEY `idx_end_time` (`end_time`),
  KEY `idx_create_time` (`end_time`)
) CHARSET=utf8 ENGINE=InnoDB COMMENT '秒杀商品表';

-- 创建秒杀订单表
CREATE TABLE `order`(
  `id` bigint NOT NULL COMMENT '秒杀商品ID',
  `money` decimal (10, 2) DEFAULT NULL COMMENT '支付金额',
  `user_phone` bigint NOT NULL COMMENT '用户手机号',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `status` tinyint NOT NULL DEFAULT -1 COMMENT '状态：-1无效 0成功 1已付款',
  PRIMARY KEY (`id`, `user_phone`) /*联合主键，保证一个用户只能秒杀一件商品*/
) CHARSET=utf8 ENGINE=InnoDB COMMENT '秒杀订单表';


INSERT INTO `product` VALUES ('1', 'Apple/苹果 iPhone 6s Plus 国行原装苹果6sp 5.5寸全网通4G手机', 'https://g-search3.alicdn.com/img/bao/uploaded/i4/i3/2249262840/O1CN011WqlHkrSuPEiHxd_!!2249262840.jpg_230x230.jpg', '2600.00', '1100.00', '10', '2019-08-20 10:10:00', '2019-08-22 17:30:00', '2019-08-13 16:22:38');
INSERT INTO `product` VALUES ('2', 'ins新款连帽毛领棉袄宽松棉衣女冬外套学生棉服', 'https://gw.alicdn.com/bao/uploaded/i3/2007932029/TB1vdlyaVzqK1RjSZFzXXXjrpXa_!!0-item_pic.jpg_180x180xz.jpg', '200.00', '150.00', '10', '2019-08-20 17:10:00', '2019-08-22 10:30:00', '2019-08-13 16:22:38');
INSERT INTO `product` VALUES ('3', '可爱超萌兔子毛绒玩具垂耳兔公仔布娃娃睡觉抱女孩玩偶大号女生 ', 'https://g-search3.alicdn.com/img/bao/uploaded/i4/i2/3828650009/TB22CvKkeOSBuNjy0FdXXbDnVXa_!!3828650009.jpg_230x230.jpg', '160.00', '130.00', '20', '2019-08-20 10:10:00', '2019-08-22 16:30:00', '2019-08-13 16:22:38');
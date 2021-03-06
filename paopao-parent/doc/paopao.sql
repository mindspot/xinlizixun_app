CREATE TABLE `coupon` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `template_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '优惠券模板',
  `name` varchar(16) NOT NULL DEFAULT '' COMMENT '优惠券名称',
  `type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '类型',
  `term_start_date` datetime DEFAULT NULL COMMENT '有效期：开始时间',
  `term_end_date` datetime DEFAULT NULL COMMENT '有效期：截止时间',
  `amount_limit` int(11) DEFAULT '0',
  `amount` int(11) DEFAULT '0' COMMENT '优惠券金额',
  `redeem_code` varchar(16) NOT NULL DEFAULT '' COMMENT '兑换码',
  `use_notice` varchar(256) DEFAULT NULL COMMENT '使用须知',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '优惠券状态',
  `user_id` int(11) unsigned NOT NULL DEFAULT '0' COMMENT 'userId',
  `create_by` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建人',
  `create_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_by` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '修改人',
  `modified_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='会员优惠券';

CREATE TABLE `coupon_template` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `no` varchar(16) DEFAULT '' COMMENT '编号',
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '优惠券名称',
  `type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1 满减、2 立减、3 折扣券 4 优惠码',
  `max_amount` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '满多少金额',
  `actual_amount` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '用券金额',
  `provide_qty` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '发放数量',
  `take_qty` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '已领取的优惠券数量',
  `used_qty` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '已使用的优惠券数量',
  `valid_type` tinyint(1) NOT NULL DEFAULT '0' COMMENT '时效:1绝对时效（领取后XXX-XXX时间段有效）  2相对时效（领取后N天有效）',
  `valid_start_time` datetime DEFAULT NULL COMMENT '使用开始时间',
  `valid_end_time` datetime DEFAULT NULL COMMENT '使用结束时间',
  `valid_days` tinyint(3) unsigned NOT NULL DEFAULT '0' COMMENT '自领取之日起有效天数',
  `limit_per_person` int(11) NOT NULL DEFAULT '0' COMMENT '每人限领',
  `url` varchar(256) NOT NULL DEFAULT '' COMMENT '领取url',
  `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '1失效 2 有效',
  `remark` varchar(256) NOT NULL DEFAULT '' COMMENT '备注',
  `create_by` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '创建人',
  `create_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_by` bigint(20) unsigned NOT NULL DEFAULT '0' COMMENT '修改人',
  `modified_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `promotional_activities` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `template_ids` varchar(32) NOT NULL DEFAULT '' COMMENT '关联模板',
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '活动名称',
  `title` varchar(64) DEFAULT NULL COMMENT '活动标题',
  `image_url` varchar(128) NOT NULL DEFAULT '' COMMENT '活动图片',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `rules` varchar(256) NOT NULL DEFAULT '' COMMENT '活动规则',
  `url` varchar(128) DEFAULT '' COMMENT '活动链接',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '活动状态',
  `create_by` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '创建人',
  `create_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modified_by` int(11) unsigned NOT NULL DEFAULT '0' COMMENT '修改人',
  `modified_dt` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
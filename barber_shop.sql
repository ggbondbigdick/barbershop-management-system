
DROP TABLE IF EXISTS `attendance`;
CREATE TABLE `attendance` (
  `id` varchar(50) NOT NULL COMMENT 'ID',
  `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(20) DEFAULT NULL COMMENT '更新时间',
  `type` varchar(2) DEFAULT NULL COMMENT '类型 1-上班 2-请假',
  `staff` varchar(50) DEFAULT NULL COMMENT '员工',
  `overtime` varchar(2) DEFAULT NULL COMMENT '加班 1-是 2-否',
  `remark` varchar(300) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='考勤';


DROP TABLE IF EXISTS `evaluate`;
CREATE TABLE `evaluate` (
  `id` varchar(50) NOT NULL COMMENT 'ID',
  `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建用户',
  `update_time` varchar(20) DEFAULT NULL COMMENT '更新时间',
  `type` varchar(2) DEFAULT NULL COMMENT '类型 1-项目 2-员工',
  `reserve_object` varchar(50) DEFAULT NULL COMMENT '对象',
  `content` varchar(300) DEFAULT NULL COMMENT '内容',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='评价';


DROP TABLE IF EXISTS `haircutting_project`;
CREATE TABLE `haircutting_project` (
  `id` varchar(50) NOT NULL COMMENT 'ID',
  `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(20) DEFAULT NULL COMMENT '更新时间',
  `type` varchar(2) DEFAULT NULL COMMENT '类型 1-洗发 2-理发 3-烫发 4-染发 5-头发保养',
  `name` varchar(50) DEFAULT NULL COMMENT '名称',
  `thumbnail` varchar(50) DEFAULT NULL COMMENT '缩略图',
  `introduce` varchar(100) DEFAULT NULL COMMENT '介绍',
  `price` double(10,2) DEFAULT NULL COMMENT '价格',
  `remark` varchar(300) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='理发项目';


DROP TABLE IF EXISTS `reserve`;
CREATE TABLE `reserve` (
  `id` varchar(50) NOT NULL COMMENT 'ID',
  `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建用户',
  `update_time` varchar(20) DEFAULT NULL COMMENT '更新时间',
  `type` varchar(2) DEFAULT NULL COMMENT '类型 1-项目 2-员工',
  `reserve_object` varchar(50) DEFAULT NULL COMMENT '对象',
  `reserve_staff` varchar(50) DEFAULT NULL COMMENT '指定理发师',
  `reserve_time` varchar(20) DEFAULT NULL COMMENT '时间',
  `state` varchar(2) DEFAULT NULL COMMENT '状态 1.预约中 2.预约成功 3.待支付 4.正在服务 5.服务完成 6.预约失败',
  `pay_time` varchar(20) DEFAULT NULL COMMENT '支付时间',
  `evaluate` varchar(2) DEFAULT NULL COMMENT '是否评价 1-是 2-否',
  `remark` varchar(300) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='预约';


DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file` (
  `id` varchar(50) NOT NULL COMMENT 'ID',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
  `update_user` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` varchar(20) DEFAULT NULL COMMENT '更新时间',
  `file_name_upload` varchar(120) DEFAULT NULL COMMENT '上传文件名',
  `file_name_save` varchar(60) DEFAULT NULL COMMENT '保存文件名',
  `file_suffix` varchar(20) DEFAULT NULL COMMENT '文件后缀',
  `save_path` varchar(600) DEFAULT NULL COMMENT '保存路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='文件信息';


DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(50) NOT NULL COMMENT 'ID',
  `create_time` varchar(20) DEFAULT NULL COMMENT '创建时间',
  `update_time` varchar(20) DEFAULT NULL COMMENT '更新时间',
  `avatar` varchar(50) DEFAULT NULL COMMENT '头像',
  `account` varchar(30) DEFAULT NULL COMMENT '账号',
  `name` varchar(15) DEFAULT NULL COMMENT '名称',
  `age` int DEFAULT NULL COMMENT '年龄',
  `sex` varchar(2) DEFAULT NULL COMMENT '性别 1-男 2-女',
  `contact_tel` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `password` varchar(600) DEFAULT NULL COMMENT '密码',
  `good_type` varchar(2) DEFAULT NULL COMMENT '擅长类型 1-洗发 2-理发 3-烫发 4-染发 5-头发保养',
  `balance` double(10,2) unsigned zerofill DEFAULT NULL COMMENT '余额',
  `role` varchar(2) DEFAULT NULL COMMENT '角色 1-管理员 2-用户 3-员工',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='用户';


INSERT INTO `sys_user` (`id`, `create_time`, `update_time`, `avatar`, `account`, `name`, `age`, `sex`, `contact_tel`, `password`, `good_type`, `balance`, `role`) VALUES ('3b5cf01ea4fe47f7a89d8c3aab103b35', '2025-05-13 01:22:38', '2025-05-13 01:22:38', '876492f3747843698bd179b5c3ad00d1', 'TT', 'tt', 19, '1', '1000', 'Nb5eHbGilV8UeqCOaUED9Olr58zoFwswLSw0sbdf0aKf4SCgKtWxSgeJ7DtlWf3fhNzhPiF3m+F6h+TMBxkkuKmZnsVW481IDaNGKd/B7bLgS1/CWO+OWW3OHkXaepQhEKNSd8db1d9yIkimuGpRBYQTzcNgrtCtPZVYXs/oehVp7sPXCwLjPkJe+VpbW70tyd08aSQG9/gDKtnFyxVwP9GeHGAuq+FHZ7ogCQKxRs7Dozn7j1vOh5UnPdfAc3nHg6OXrK/sGJerSNlWelMTw77U24zcILHruH5P1hI6oSLRGNzoph96Y1mzcvzdosITd62WFONrNfWA7igEtXY5hw==', NULL, 0000000.00, '3');
INSERT INTO `sys_user` (`id`, `create_time`, `update_time`, `avatar`, `account`, `name`, `age`, `sex`, `contact_tel`, `password`, `good_type`, `balance`, `role`) VALUES ('493b21352e2f4d55bb4b9aaa937ea846', '2025-05-04 17:32:00', '2025-05-04 17:32:00', NULL, 'USER', '用户', 26, '2', '10000', 'w2GCpuH222Ytc8Wubf3BtN3pxCWEe1WHF7E88oob30BaNURGq/n9foe97SspCQ6mZOz3XE8bhF0AD0yA4EmwTMk42UC8s3Nhe15DZr4ZBPoDktP9z/UIhBRhZIDbBsJcaRhMYIgwgwGkHVpdtTRgTPiFKWrc6eyamfiaYZFAHceepC5P0RuuLW01o293xv2Rc0pFrBQFP8ky1afr+i4O3akBsy+99zbErhMj6rNNeUD0KTdYda7Q4GGiCeW9rasXPMpihYEZjTAY+TtJQauG7Ai53ER8W9lGqbxqEbq6NmWzjrl8tLEuJSMB/reWpP5rKRcxCDtakIpc6SwHBjb81A==', NULL, 0000085.10, '2');
INSERT INTO `sys_user` (`id`, `create_time`, `update_time`, `avatar`, `account`, `name`, `age`, `sex`, `contact_tel`, `password`, `good_type`, `balance`, `role`) VALUES ('872dab029a28401eac50a019126bd9d3', '2025-04-01 00:00:00', '2025-04-01 00:00:00', NULL, 'admin', '管理员', 20, '1', '10000', 'BwCNWiWEa3D6jHQ3cuXXNbaSjHJHCDORmab5jgr0vJSnAiL1MobJWQcxaQ6RJF/GxPx3ZUwTzKq0CdlTBVFjqkdLea3IYxb73Sr8dzdwUvcafl4odzpHRbqxUiYzd4kbmTNCjRNHy44fIGtQzW9TgvSVOZiO6Mkd8oIv8V+cBg56GXGYyxh1htnFG0UwTbbga7fPQZrKdymKSKMqDCofwXkLtxKA9eU5LEZPY88DU4pbIabss6/0+MmujSUl9TmrGpZ8Mj52buFdG/Oj+Iguy+9nW8igUhH1emt/f2RZkA59mp69JjpAMaIwac50dL8ZGQrOjJVJIeg6FrSwn0d+TA==', NULL, 0000000.00, '1');
INSERT INTO `sys_user` (`id`, `create_time`, `update_time`, `avatar`, `account`, `name`, `age`, `sex`, `contact_tel`, `password`, `good_type`, `balance`, `role`) VALUES ('ca1f119090ba4693a2641751882ecedb', '2025-04-11 16:06:46', '2025-04-28 23:57:14', '139282f87ffb4df180fd93bf83024cff', 'YG', '员工', 22, '1', '13040504229', 'LDOFMM9lv1XFUjlKl/9GwhSOKeTZ3vUv1aBQR/ZlBC+UIB3WjSE4tj0HAu9mv9e/93U7a3JhDv0CDPAL0dGgRH1t+PWriFzM1szLivxZSj3chhto95y73jYqGZlq+p81ihqqLCPgqWHpUY0Xm8/4zw9OUJ7UQKTyFg3Bi9l67vftGNZgBlWga9eP3zq2+CxG9xzxJSnBVac7yfR2FZJi+t0/9lwPl4no1o0OtUT6YdZ93pVGyywhozA/it5UPRkVu+2GA1yWqa3LJdgGxhEMAOI0mVx94EvdRnKwID2RALdK9RH6IXDAQ8+ahgfNBxuG6FHP1WG/i4nstS0FLHiT1w==', NULL, 0000000.00, '3');

CREATE DATABASE IF NOT EXISTS gallery_tour DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE gallery_tour;

SET NAMES utf8mb4;
SET CHARACTER_SET_CLIENT = utf8mb4;
SET CHARACTER_SET_RESULTS = utf8mb4;
SET CHARACTER_SET_CONNECTION = utf8mb4;

DROP TABLE IF EXISTS favorite;
DROP TABLE IF EXISTS comment;
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS time_slot;
DROP TABLE IF EXISTS virtual_tour;
DROP TABLE IF EXISTS exhibit_item;
DROP TABLE IF EXISTS announcement;
DROP TABLE IF EXISTS exhibition;
DROP TABLE IF EXISTS exhibition_category;
DROP TABLE IF EXISTS sys_user;

-- ========== 用户表 ==========
CREATE TABLE sys_user (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50)  NOT NULL COMMENT '用户名',
    password    VARCHAR(200) NOT NULL COMMENT '密码(BCrypt)',
    nickname    VARCHAR(50)  NOT NULL COMMENT '昵称',
    phone       VARCHAR(20)           COMMENT '手机号',
    email       VARCHAR(100)          COMMENT '邮箱',
    avatar      VARCHAR(500)          COMMENT '头像URL',
    gender      TINYINT      NOT NULL DEFAULT 0 COMMENT '性别: 0未知 1男 2女',
    role        TINYINT      NOT NULL DEFAULT 1 COMMENT '角色: 0管理员 1普通用户',
    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 0禁用 1正常',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted     TINYINT      NOT NULL DEFAULT 0,
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ========== 展览分类表 ==========
CREATE TABLE exhibition_category (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL COMMENT '分类名称',
    description VARCHAR(500)          COMMENT '分类描述',
    sort_order  INT          NOT NULL DEFAULT 0 COMMENT '排序',
    status      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 0禁用 1正常',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted     TINYINT      NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='展览分类表';

-- ========== 展览表 ==========
CREATE TABLE exhibition (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    title          VARCHAR(200)   NOT NULL COMMENT '展览标题',
    description    TEXT                    COMMENT '展览描述',
    cover_image    VARCHAR(500)            COMMENT '封面图',
    images         TEXT                    COMMENT '图片集(JSON数组)',
    category_id    BIGINT                  COMMENT '分类ID',
    location       VARCHAR(200)            COMMENT '展览地点',
    hall_name      VARCHAR(100)            COMMENT '展厅名称',
    start_date     DATE           NOT NULL COMMENT '开始日期',
    end_date       DATE           NOT NULL COMMENT '结束日期',
    daily_capacity INT            NOT NULL DEFAULT 200 COMMENT '每日接待容量',
    ticket_price   DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '票价',
    status         TINYINT        NOT NULL DEFAULT 0 COMMENT '状态: 0草稿 1即将开展 2展出中 3已结束',
    view_count     INT            NOT NULL DEFAULT 0 COMMENT '浏览量',
    create_time    DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time    DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted        TINYINT        NOT NULL DEFAULT 0,
    INDEX idx_category (category_id),
    INDEX idx_status (status),
    INDEX idx_date (start_date, end_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='展览表';

-- ========== 展品表 ==========
CREATE TABLE exhibit_item (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    exhibition_id BIGINT       NOT NULL COMMENT '所属展览ID',
    name          VARCHAR(200) NOT NULL COMMENT '展品名称',
    artist        VARCHAR(100)          COMMENT '艺术家/作者',
    era           VARCHAR(50)           COMMENT '年代',
    description   TEXT                  COMMENT '展品描述',
    cover_image   VARCHAR(500)          COMMENT '展品图片',
    audio_url     VARCHAR(500)          COMMENT '语音讲解URL',
    sort_order    INT          NOT NULL DEFAULT 0 COMMENT '排序',
    create_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted       TINYINT      NOT NULL DEFAULT 0,
    INDEX idx_exhibition (exhibition_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='展品表';

-- ========== 虚拟导览表 ==========
CREATE TABLE virtual_tour (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    exhibition_id BIGINT       NOT NULL COMMENT '所属展览ID',
    title         VARCHAR(200) NOT NULL COMMENT '导览标题',
    description   TEXT                  COMMENT '导览描述',
    panorama_url  VARCHAR(500) NOT NULL COMMENT '全景图/3D资源URL',
    thumbnail     VARCHAR(500)          COMMENT '缩略图',
    tour_type     TINYINT      NOT NULL DEFAULT 0 COMMENT '类型: 0全景 1-3D模型',
    sort_order    INT          NOT NULL DEFAULT 0 COMMENT '排序',
    create_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted       TINYINT      NOT NULL DEFAULT 0,
    INDEX idx_exhibition (exhibition_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='虚拟导览表';

-- ========== 预约时间段表 ==========
CREATE TABLE time_slot (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    exhibition_id BIGINT      NOT NULL COMMENT '所属展览ID',
    slot_name     VARCHAR(50) NOT NULL COMMENT '时段名称(如09:00-12:00)',
    start_time    TIME        NOT NULL COMMENT '开始时间',
    end_time      TIME        NOT NULL COMMENT '结束时间',
    max_capacity  INT         NOT NULL DEFAULT 50 COMMENT '最大预约人数',
    create_time   DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted       TINYINT     NOT NULL DEFAULT 0,
    INDEX idx_exhibition (exhibition_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约时间段表';

-- ========== 预约表 ==========
CREATE TABLE reservation (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id          BIGINT      NOT NULL COMMENT '用户ID',
    exhibition_id    BIGINT      NOT NULL COMMENT '展览ID',
    time_slot_id     BIGINT      NOT NULL COMMENT '时间段ID',
    reservation_date DATE        NOT NULL COMMENT '预约日期',
    num_visitors     INT         NOT NULL DEFAULT 1 COMMENT '参观人数',
    contact_name     VARCHAR(50) NOT NULL COMMENT '联系人姓名',
    contact_phone    VARCHAR(20) NOT NULL COMMENT '联系人电话',
    status           TINYINT     NOT NULL DEFAULT 0 COMMENT '状态: 0待确认 1已确认 2已取消 3已完成 4已过期',
    cancel_reason    VARCHAR(500)         COMMENT '取消原因',
    create_time      DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time      DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted          TINYINT     NOT NULL DEFAULT 0,
    INDEX idx_user (user_id),
    INDEX idx_exhibition (exhibition_id),
    INDEX idx_date (reservation_date),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预约表';

-- ========== 公告表 ==========
CREATE TABLE announcement (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(200) NOT NULL COMMENT '公告标题',
    content     TEXT         NOT NULL COMMENT '公告内容',
    cover_image VARCHAR(500)          COMMENT '封面图',
    status      TINYINT      NOT NULL DEFAULT 0 COMMENT '状态: 0草稿 1已发布',
    top_flag    TINYINT      NOT NULL DEFAULT 0 COMMENT '是否置顶: 0否 1是',
    create_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    deleted     TINYINT      NOT NULL DEFAULT 0,
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告表';

-- ========== 评论表 ==========
CREATE TABLE comment (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT   NOT NULL COMMENT '用户ID',
    exhibition_id BIGINT   NOT NULL COMMENT '展览ID',
    content       TEXT     NOT NULL COMMENT '评论内容',
    rating        TINYINT  NOT NULL DEFAULT 5 COMMENT '评分(1-5)',
    status        TINYINT  NOT NULL DEFAULT 0 COMMENT '状态: 0待审核 1已通过 2已拒绝',
    create_time   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted       TINYINT  NOT NULL DEFAULT 0,
    INDEX idx_user (user_id),
    INDEX idx_exhibition (exhibition_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- ========== 收藏表 ==========
CREATE TABLE favorite (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT   NOT NULL COMMENT '用户ID',
    exhibition_id BIGINT   NOT NULL COMMENT '展览ID',
    create_time   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_exhibition (user_id, exhibition_id),
    INDEX idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

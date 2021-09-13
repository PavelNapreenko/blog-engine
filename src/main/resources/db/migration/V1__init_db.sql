SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";

DROP TABLE IF EXISTS captcha_codes;
DROP TABLE IF EXISTS global_settings;
DROP TABLE IF EXISTS post_comments;
DROP TABLE IF EXISTS posts;
DROP TABLE IF EXISTS post_votes;
DROP TABLE IF EXISTS tag2post;
DROP TABLE IF EXISTS tags;
DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS `users`
(
    `id`           int                                     NOT NULL AUTO_INCREMENT,
    `code`         varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
    `email`        varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `is_moderator` tinyint(1)                              NOT NULL,
    `name`         varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `password`     varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `photo`        text COLLATE utf8mb4_unicode_ci         DEFAULT NULL,
    `reg_time`     datetime(6)                             NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_users_email` (`email`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS `posts`
(
    `id`                int                                     NOT NULL AUTO_INCREMENT,
    `is_active`         tinyint(1)                              NOT NULL,
    `moderation_status` varchar(10) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `text`              text COLLATE utf8mb4_unicode_ci         NOT NULL,
    `time`              datetime(6)                             NOT NULL,
    `title`             varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `view_count`        int                                     NOT NULL,
    `user_id`           int                                     NOT NULL,
    `moderator_id`      int DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_posts_title` (`title`),
    KEY `idx_posts_active_status_date` (`is_active`, `moderation_status`, `time`, `id`),
    KEY `fk_posts_author_id` (`user_id`),
    KEY `fk_posts_moderator_id` (`moderator_id`),
    CONSTRAINT `fk_posts_author_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
    CONSTRAINT `fk_posts_moderator_id` FOREIGN KEY (`moderator_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS `post_comments`
(
    `id`        int                             NOT NULL AUTO_INCREMENT,
    `text`      text COLLATE utf8mb4_unicode_ci NOT NULL,
    `time`      datetime(6)                     NOT NULL,
    `parent_id` int DEFAULT NULL,
    `post_id`   int                             NOT NULL,
    `user_id`   int                             NOT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_comments_post_id` (`post_id`),
    KEY `idx_comments_user_id` (`user_id`),
    KEY `idx_comments_parent` (`parent_id`),
    KEY `idx_comments_time` (`time`),
    CONSTRAINT `fk_comments_parent_id` FOREIGN KEY (`parent_id`) REFERENCES `post_comments` (`id`),
    CONSTRAINT `fk_comments_post_id` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
    CONSTRAINT `fk_comments_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS `tags`
(
    `id`   int                                     NOT NULL AUTO_INCREMENT,
    `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_tags_name` (`name`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS `tag2post`
(
    `id`   int                                     NOT NULL AUTO_INCREMENT,
    `post_id` int NOT NULL,
    `tag_id`  int NOT NULL,
    PRIMARY KEY (`id`),
    KEY `fk_posts_tags_tag_id` (`tag_id`),
    CONSTRAINT `fk_posts_tags_post_id` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
    CONSTRAINT `fk_posts_tags_tag_id` FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS `post_votes`
(
    `id`      int         NOT NULL AUTO_INCREMENT,
    `time`    datetime(6) NOT NULL,
    `value`   tinyint     NOT NULL,
    `post_id` int         NOT NULL,
    `user_id` int         NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_votes_user_post` (`user_id`, `post_id`),
    KEY `fk_votes_post_id` (`post_id`),
    CONSTRAINT `fk_votes_post_id` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`),
    CONSTRAINT `fk_votes_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS `captcha_codes`
(
    `id`          int                                     NOT NULL AUTO_INCREMENT,
    `code`        varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `secret_code` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `time`        datetime(6)                             NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS `global_settings`
(
    `id`    int                                     NOT NULL AUTO_INCREMENT,
    `code`  varchar(30) COLLATE utf8mb4_unicode_ci  NOT NULL,
    `name`  varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
    `value` varchar(5) COLLATE utf8mb4_unicode_ci   NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `idx_global_settings_code` (`code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
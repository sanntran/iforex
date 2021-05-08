CREATE SCHEMA `forex` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin ;

CREATE TABLE `orders` (
                        `id` bigint(20) NOT NULL AUTO_INCREMENT,
                        `ticket` bigint(20) NOT NULL,
                        `lots` double NOT NULL,
                        `type` int(11) NOT NULL,
                        `profit` double NULL,
                        `open_time` timestamp NULL,
                        `close_time` timestamp NULL,
                        `open_price` double NOT NULL,
                        `close_price` double NULL,
                        `stop_loss` double NULL,
                        `take_profit` double NULL,
                        `swap` double NULL,
                        `commission` double NULL,
                        `comment` varchar(255) COLLATE utf8_unicode_ci NULL,
                        `expiration` timestamp NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin_ci;

CREATE TABLE `ticks` (
                       `id` bigint(20) NOT NULL AUTO_INCREMENT,
                       `time` timestamp NULL,
                       `bid` double NULL,
                       `ask` double NULL,
                       PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin_ci;



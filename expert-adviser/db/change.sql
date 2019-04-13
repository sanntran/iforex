CREATE SCHEMA `forex` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;

CREATE TABLE `forex`.`eur_usd_m1_candles` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL,
  `time` DATETIME NULL,
  `period` VARCHAR(31) NULL,
  `high` DOUBLE NULL,
  `low` DOUBLE NULL,
  `open` DOUBLE NULL,
  `close` DOUBLE NULL,
  `volume` BIGINT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC));



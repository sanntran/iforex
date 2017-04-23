CREATE SCHEMA `iforex` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;

CREATE TABLE `iforex`.`eur_usd_m5_candles` (
  `id` BIGINT NOT NULL,
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
  
  ALTER TABLE `iforex`.`eur_usd_m5_candles` 
CHANGE COLUMN `id` `id` BIGINT(20) NOT NULL AUTO_INCREMENT ;

CREATE TABLE `iforex`.`eur_usd_d1_wave` (
  `id` INT NOT NULL,
  `name` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC));

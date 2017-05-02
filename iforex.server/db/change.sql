CREATE SCHEMA `iforex` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;


CREATE TABLE `iforex`.`eur_usd_m15_candles` (
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

CREATE TABLE `iforex`.`eur_usd_m15_waves` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC));

  CREATE TABLE `iforex`.`eur_usd_m15_waves_candles` (
  `wave_id` BIGINT NOT NULL,
  `candle_id` BIGINT NOT NULL,
  PRIMARY KEY (`wave_id`, `candle_id`),
  INDEX `eur_usd_m15_waves_candles_fk_candle_id_idx` (`candle_id` ASC),
  CONSTRAINT `eur_usd_m15_waves_candles_fk_wave_id`
    FOREIGN KEY (`wave_id`)
    REFERENCES `iforex`.`eur_usd_m15_wave` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `eur_usd_m15_waves_candles_fk_candle_id`
    FOREIGN KEY (`candle_id`)
    REFERENCES `iforex`.`eur_usd_m15_candles` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);


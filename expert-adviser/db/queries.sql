truncate table forex.candles;
truncate table forex.averages;
truncate table forex.supports;
truncate table forex.resistances;
truncate table forex.orders;
truncate table forex.predictions;
truncate table forex.results;
truncate table forex.ticks;

select * from forex.averages;
select avg(high-low) from forex.candles;

select * from forex.predictions limit 100;

select * from forex.orders;

CREATE TABLE `predictions` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `candle` bigint(20) NOT NULL,
  `time` timestamp NULL DEFAULT NULL,
  `open` double DEFAULT NULL,
  `close` double DEFAULT NULL,
  `average` double DEFAULT NULL,
  `distance` double DEFAULT NULL,
  `slope` double DEFAULT NULL,
  `slope_distance` double DEFAULT NULL,
  `slope_slope` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `predictions_time_idx` (`time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

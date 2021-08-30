truncate table forex.candles;
truncate table forex.averages;
truncate table forex.supports;
truncate table forex.resistances;
truncate table forex.orders;
truncate table forex.predictions;
truncate table forex.results;
truncate table forex.ticks;

select * from forex.candles;
select avg(high-low) from forex.candles;

select * from forex.predictions limit 100;

ALTER TABLE `forex`.`predictions` 
ADD COLUMN `distance_avg_short_avg_long` double DEFAULT NULL,
 ADD COLUMN  `distance_avg_medium_avg_long` double DEFAULT NULL,
 ADD COLUMN  `slope_avg_short` double DEFAULT NULL,
 ADD COLUMN  `slope_avg_medium` double DEFAULT NULL,
ADD COLUMN  `slope_avg_long` double DEFAULT NULL,
ADD COLUMN   `slope_distance_avg_short_avg_long` double DEFAULT NULL,
ADD COLUMN  `slope_distance_avg_medium_avg_long` double DEFAULT NULL
;
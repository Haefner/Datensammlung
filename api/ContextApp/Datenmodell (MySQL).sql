CREATE TABLE `Sensor_Accel` 
( 
	`Android_ID` VARCHAR(32) NOT NULL,
	`Messung_Start` DATETIME NOT NULL,
	`zeit` DOUBLE NOT NULL,
	`X` DOUBLE,
	`Y` DOUBLE,
	`Z` DOUBLE
); 

CREATE TABLE `Sensor_Gyro` 
( 
	`Android_ID` VARCHAR(32) NOT NULL,
	`Messung_Start` DATETIME NOT NULL,
	`zeit` DOUBLE NOT NULL,
	`X` DOUBLE,
	`Y` DOUBLE,
	`Z` DOUBLE
); 

CREATE TABLE `Datensatz` 
( 
	`Android_ID` VARCHAR(32) NOT NULL,
	`Messung_Start` DATETIME NOT NULL,
	`Messung_Ende` DATETIME
); 

CREATE TABLE `Sensor_Rota` 
( 
	`Android_ID` VARCHAR(32) NOT NULL,
	`Messung_Start` DATETIME NOT NULL,
	`zeit` DOUBLE NOT NULL,
	`X` DOUBLE,
	`Y` DOUBLE,
	`Z` DOUBLE
); 

CREATE TABLE `Sensor_Mag` 
( 
	`Android_ID` VARCHAR(32) NOT NULL,
	`Messung_Start` DATETIME NOT NULL,
	`zeit` DOUBLE NOT NULL,
	`X` DOUBLE,
	`Y` DOUBLE,
	`Z` DOUBLE
); 

ALTER TABLE `Sensor_Accel` ADD PRIMARY KEY (`Android_ID`, `Messung_Start`, `zeit`);
ALTER TABLE `Sensor_Gyro` ADD PRIMARY KEY (`Android_ID`, `Messung_Start`, `zeit`);
ALTER TABLE `Datensatz` ADD PRIMARY KEY (`Android_ID`, `Messung_Start`);
ALTER TABLE `Sensor_Rota` ADD PRIMARY KEY (`Android_ID`, `Messung_Start`, `zeit`);
ALTER TABLE `Sensor_Mag` ADD PRIMARY KEY (`Android_ID`, `Messung_Start`, `zeit`);

ALTER TABLE `Sensor_Accel` ADD CONSTRAINT `Sensor_Accel_Android_ID_Messung_Start_fkey` FOREIGN KEY (`Android_ID`,`Messung_Start`) REFERENCES `Datensatz`(`Android_ID`,`Messung_Start`);
ALTER TABLE `Sensor_Gyro` ADD CONSTRAINT `Sensor_Gyro_Android_ID_Messung_Start_fkey` FOREIGN KEY (`Android_ID`,`Messung_Start`) REFERENCES `Datensatz`(`Android_ID`,`Messung_Start`);
ALTER TABLE `Sensor_Rota` ADD CONSTRAINT `Sensor_Rota_Android_ID_Messung_Start_fkey` FOREIGN KEY (`Android_ID`,`Messung_Start`) REFERENCES `Datensatz`(`Android_ID`,`Messung_Start`);
ALTER TABLE `Sensor_Mag` ADD CONSTRAINT `Sensor_Mag_Android_ID_Messung_Start_fkey` FOREIGN KEY (`Android_ID`,`Messung_Start`) REFERENCES `Datensatz`(`Android_ID`,`Messung_Start`);

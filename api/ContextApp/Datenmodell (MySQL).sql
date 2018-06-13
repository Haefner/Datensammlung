CREATE TABLE `Sensor_Accel` 
( 
	`ID` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`Messung_Start` DATETIME NOT NULL,
	`Android_ID` VARCHAR(32) NOT NULL,
	`zeit` DOUBLE NOT NULL,
	`X` DOUBLE NOT NULL,
	`Y` DOUBLE NOT NULL,
	`Z` DOUBLE NOT NULL
); 

CREATE TABLE `Sensor_Gyro` 
( 
	`ID` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`Messung_Start` DATETIME NOT NULL,
	`Android_ID` VARCHAR(32) NOT NULL,
	`zeit` DOUBLE NOT NULL,
	`X` DOUBLE NOT NULL,
	`Y` DOUBLE NOT NULL,
	`Z` DOUBLE NOT NULL
); 

CREATE TABLE `Datensatz` 
( 
	`Android_ID` VARCHAR(32) NOT NULL,
	`Messung_Start` DATETIME NOT NULL,
	`Messung_Ende` DATETIME
); 

CREATE TABLE `Sensor_Rota` 
( 
	`ID` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`Messung_Start` DATETIME NOT NULL,
	`Android_ID` VARCHAR(32) NOT NULL,
	`zeit` DOUBLE NOT NULL,
	`X` DOUBLE NOT NULL,
	`Y` DOUBLE NOT NULL,
	`Z` DOUBLE NOT NULL
); 

CREATE TABLE `Sensor_Mag` 
( 
	`ID` INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`Messung_Start` DATETIME NOT NULL,
	`Android_ID` VARCHAR(32) NOT NULL,
	`zeit` DOUBLE NOT NULL,
	`X` DOUBLE NOT NULL,
	`Y` DOUBLE NOT NULL,
	`Z` DOUBLE NOT NULL
); 


ALTER TABLE `Datensatz` ADD PRIMARY KEY (`Android_ID`, `Messung_Start`);

ALTER TABLE `Sensor_Accel` ADD CONSTRAINT `Sensor_Accel_Android_ID_Messung_Start_fkey` FOREIGN KEY (`Android_ID`,`Messung_Start`) REFERENCES `Datensatz`(`Android_ID`,`Messung_Start`);
ALTER TABLE `Sensor_Gyro` ADD CONSTRAINT `Sensor_Gyro_Android_ID_Messung_Start_fkey` FOREIGN KEY (`Android_ID`,`Messung_Start`) REFERENCES `Datensatz`(`Android_ID`,`Messung_Start`);
ALTER TABLE `Sensor_Rota` ADD CONSTRAINT `Sensor_Rota_Android_ID_Messung_Start_fkey` FOREIGN KEY (`Android_ID`,`Messung_Start`) REFERENCES `Datensatz`(`Android_ID`,`Messung_Start`);
ALTER TABLE `Sensor_Mag` ADD CONSTRAINT `Sensor_Mag_Android_ID_Messung_Start_fkey` FOREIGN KEY (`Android_ID`,`Messung_Start`) REFERENCES `Datensatz`(`Android_ID`,`Messung_Start`);

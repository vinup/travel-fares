DROP TABLE IF EXISTS taps;
CREATE TABLE taps (
                      tapId BIGINT PRIMARY KEY,
                      stopId INT NOT NULL,
                      tapTime TIMESTAMP NOT NULL,
                      tapType VARCHAR(32) NOT NULL,
                      tapDataFileId VARCHAR(255),
                      pan VARCHAR(255),
                      busId VARCHAR(255),
                      companyId VARCHAR(255),
                      isMappedToTrip BOOLEAN DEFAULT FALSE
);

DROP TABLE IF EXISTS trips;
CREATE TABLE trips (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       started TIMESTAMP,
                       finished TIMESTAMP,
                       duration INT,
                       fromStopId INT,
                       toStopId INT,
                       chargeAmount DECIMAL(10, 2) NOT NULL,
                       companyId VARCHAR(255),
                       busId VARCHAR(255),
                       pan VARCHAR(255),
                       tapDataFileId VARCHAR(255),
                       status VARCHAR(32) NOT NULL
);
DROP TABLE IF EXISTS fares;
CREATE TABLE fares (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       fareId INT NOT NULL,
                       fare DECIMAL(10, 2) NOT NULL,
                       sourceStopId INT,
                       destinationStopId INT
);

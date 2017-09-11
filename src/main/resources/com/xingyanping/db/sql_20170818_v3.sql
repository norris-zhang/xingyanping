ALTER TABLE `client_port_relationship` 
ADD COLUMN `cprs_effective_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP AFTER `cprs_updated`,
ADD COLUMN `cprs_expiring_date` DATETIME NULL AFTER `cprs_effective_date`;

update `client_port_relationship` set `cprs_effective_date`='2017-08-01 0:00:00';

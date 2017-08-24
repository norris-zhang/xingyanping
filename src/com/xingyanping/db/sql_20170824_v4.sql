ALTER TABLE `original_report` 
ADD COLUMN `orre_dist_content` TEXT NULL AFTER `orre_updated`,
ADD COLUMN `orre_complaint_type` VARCHAR(50) NULL AFTER `orre_dist_content`;

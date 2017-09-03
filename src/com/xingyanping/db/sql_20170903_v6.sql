ALTER TABLE `client_port_relationship` 
ADD COLUMN `cprs_order` INT NOT NULL DEFAULT 0 AFTER `cprs_expiring_date`;

update `client_port_relationship` set `cprs_order`=1 where `cprs_company_short_name`='鸿联九五';
update `client_port_relationship` set `cprs_order`=2 where `cprs_company_short_name`='天下皆阳';
update `client_port_relationship` set `cprs_order`=3 where `cprs_company_short_name`='正乾传媒';
update `client_port_relationship` set `cprs_order`=4 where `cprs_company_short_name`='西市发展';
update `client_port_relationship` set `cprs_order`=5 where `cprs_company_short_name`='原树管理';
update `client_port_relationship` set `cprs_order`=6 where `cprs_company_short_name`='山东网易';
update `client_port_relationship` set `cprs_order`=7 where `cprs_company_short_name`='济南北秀';
update `client_port_relationship` set `cprs_order`=8 where `cprs_company_short_name`='陕西创远' and `cprs_port` like '10690219%';
update `client_port_relationship` set `cprs_order`=9 where `cprs_company_short_name`='杭州三石';
update `client_port_relationship` set `cprs_order`=10 where `cprs_company_short_name`='山东航空';
update `client_port_relationship` set `cprs_order`=11 where `cprs_company_short_name`='企业银行';
update `client_port_relationship` set `cprs_order`=12 where `cprs_company_short_name`='济南百秀';
update `client_port_relationship` set `cprs_order`=13 where `cprs_company_short_name`='陕西上格';
update `client_port_relationship` set `cprs_order`=14 where `cprs_company_short_name`='陕西创远' and `cprs_port` like '106575681025%';
update `client_port_relationship` set `cprs_order`=15 where `cprs_company_short_name`='创力天行';
update `client_port_relationship` set `cprs_order`=16 where `cprs_company_short_name`='西安锦诚';
update `client_port_relationship` set `cprs_order`=17 where `cprs_company_short_name`='陕西世荣';
update `client_port_relationship` set `cprs_order`=18 where `cprs_company_short_name`='中传视友';

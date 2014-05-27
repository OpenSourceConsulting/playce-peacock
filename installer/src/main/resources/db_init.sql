grant all privileges on *.* to '${username}'@'%' identified by '${password}';
grant all privileges on *.* to '${username}'@'localhost' identified by '${password}';
flush privileges;

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS ${db_schema};
CREATE SCHEMA IF NOT EXISTS `${db_schema}` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `${db_schema}` ;

-- -----------------------------------------------------
-- Table `machine_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `machine_tbl` (
  `MACHINE_ID` VARCHAR(36) NOT NULL,
  `MACHINE_MAC_ADDR` VARCHAR(12) NULL,
  `IS_VM` CHAR(1) NULL,
  `OS_NAME` VARCHAR(50) NULL,
  `OS_VER` VARCHAR(50) NULL,
  `OS_ARCH` VARCHAR(20) NULL,
  `CPU_CLOCK` VARCHAR(20) NULL,
  `CPU_NUM` VARCHAR(20) NULL,
  `MEM_SIZE` VARCHAR(20) NULL,
  `IP_ADDR` VARCHAR(15) NULL,
  `HOST_NAME` VARCHAR(255) NULL,
  `SSH_PORT` VARCHAR(5) NULL,
  `SSH_USERNAME` VARCHAR(255) NULL,
  `SSH_PASSWORD` VARCHAR(255) NULL,
  `SSH_KEY_FILE` VARCHAR(255) NULL,
  `DELETE_YN` CHAR(1) NOT NULL DEFAULT 'N',
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  PRIMARY KEY (`MACHINE_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `software_repo_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `software_repo_tbl` (
  `SOFTWARE_ID` INT NOT NULL,
  `SOFTWARE_NAME` VARCHAR(45) NULL,
  `SOFTWARE_VERSION` VARCHAR(45) NULL,
  `SOFTWARE_VENDOR` VARCHAR(40) NULL,
  `FILE_LOCATION` VARCHAR(45) NULL,
  `FILE_NAME` VARCHAR(200) NULL,
  `DESCRIPTION` TEXT NULL,
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  PRIMARY KEY (`SOFTWARE_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `software_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `software_tbl` (
  `MACHINE_ID` VARCHAR(36) NOT NULL,
  `SOFTWARE_ID` INT NOT NULL,
  `INSTALL_LOCATION` VARCHAR(255) NULL,
  `INSTALL_STAT` VARCHAR(45) NULL COMMENT 'RUNNING, ERROR, COMPLETED',
  `INSTALL_LOG` LONGTEXT NULL,
  `DESCRIPTION` VARCHAR(255) NULL,
  `DELETE_YN` CHAR(1) NOT NULL DEFAULT 'N',
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  PRIMARY KEY (`MACHINE_ID`, `SOFTWARE_ID`),
  INDEX `fk_SOFTWARE_TBL_MACHINE_TBL1_idx` (`MACHINE_ID` ASC),
  INDEX `fk_software_tbl_software_repo_tbl1_idx` (`SOFTWARE_ID` ASC),
  CONSTRAINT `fk_SOFTWARE_TBL_MACHINE_TBL1`
    FOREIGN KEY (`MACHINE_ID`)
    REFERENCES `machine_tbl` (`MACHINE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_software_tbl_software_repo_tbl1`
    FOREIGN KEY (`SOFTWARE_ID`)
    REFERENCES `software_repo_tbl` (`SOFTWARE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mon_factor_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mon_factor_tbl` (
  `MON_FACTOR_ID` VARCHAR(10) NOT NULL,
  `MON_FACTOR_NAME` VARCHAR(45) NOT NULL,
  `MON_FACTOR_UNIT` VARCHAR(10) NOT NULL,
  `MON_FACTOR_DESC` VARCHAR(255) NULL,
  `AUTO_SCALING_YN` CHAR(1) NULL,
  `DISPLAY_NAME` VARCHAR(45) NULL,
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  PRIMARY KEY (`MON_FACTOR_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mon_data_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mon_data_tbl` (
  `MACHINE_ID` VARCHAR(36) NOT NULL,
  `MON_FACTOR_ID` VARCHAR(10) NOT NULL,
  `MON_DATA_ID` INT(11) NOT NULL AUTO_INCREMENT,
  `MON_DATA_VALUE` VARCHAR(50) NOT NULL,
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  PRIMARY KEY (`MON_DATA_ID`, `MACHINE_ID`, `MON_FACTOR_ID`),
  INDEX `fk_MON_DATA_TBL_MON_FACTOR_TBL1_idx` (`MON_FACTOR_ID` ASC),
  INDEX `fk_MON_DATA_TBL_MACHINE_TBL1_idx` (`MACHINE_ID` ASC),
  CONSTRAINT `fk_MON_DATA_TBL_MON_FACTOR_TBL1`
    FOREIGN KEY (`MON_FACTOR_ID`)
    REFERENCES `mon_factor_tbl` (`MON_FACTOR_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_MON_DATA_TBL_MACHINE_TBL1`
    FOREIGN KEY (`MACHINE_ID`)
    REFERENCES `machine_tbl` (`MACHINE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `os_package_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `os_package_tbl` (
  `PKG_ID` INT(11) NOT NULL,
  `MACHINE_ID` VARCHAR(36) NOT NULL,
  `NAME` VARCHAR(100) NULL,
  `ARCH` VARCHAR(20) NULL,
  `SIZE` VARCHAR(20) NULL,
  `VERSION` VARCHAR(50) NULL,
  `RELEASE_INFO` VARCHAR(50) NULL,
  `INSTALL_DATE` DATETIME NULL,
  `SUMMARY` VARCHAR(255) NULL,
  `DESCRIPTION` TEXT NULL,
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  PRIMARY KEY (`PKG_ID`, `MACHINE_ID`),
  INDEX `fk_OS_PACKAGE_TBL_MACHINE_TBL1_idx` (`MACHINE_ID` ASC),
  CONSTRAINT `fk_OS_PACKAGE_TBL_MACHINE_TBL1`
    FOREIGN KEY (`MACHINE_ID`)
    REFERENCES `machine_tbl` (`MACHINE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `roles_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `roles_tbl` (
  `ROLE_ID` INT(11) NOT NULL,
  `ROLE_NAME` VARCHAR(30) NULL,
  `PERMISSION` VARCHAR(45) NULL,
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  PRIMARY KEY (`ROLE_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `users_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `users_tbl` (
  `USER_ID` INT(11) NOT NULL,
  `ROLE_ID` INT(11) NOT NULL,
  `LOGIN_ID` VARCHAR(30) NULL,
  `HASHED_PASSWD` VARCHAR(50) NULL,
  `USER_NAME` VARCHAR(20) NULL,
  `DEPT_NAME` VARCHAR(45) NULL,
  `EMAIL` VARCHAR(60) NULL,
  `IS_ADMIN` TINYINT(1) NULL,
  `STATUS` INT NULL DEFAULT 1,
  `LAST_LOGON` DATETIME NULL,
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  PRIMARY KEY (`USER_ID`),
  INDEX `fk_USERS_TBL_ROLES_TBL1_idx` (`ROLE_ID` ASC),
  CONSTRAINT `fk_USERS_TBL_ROLES_TBL1`
    FOREIGN KEY (`ROLE_ID`)
    REFERENCES `roles_tbl` (`ROLE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `user_machine_map_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_machine_map_tbl` (
  `USER_ID` INT(11) NOT NULL,
  `MACHINE_ID` VARCHAR(36) NOT NULL,
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  INDEX `fk_USER_MACHINE_MAP_TBL_USERS_TBL1_idx` (`USER_ID` ASC),
  INDEX `fk_USER_MACHINE_MAP_TBL_MACHINE_TBL1_idx` (`MACHINE_ID` ASC),
  PRIMARY KEY (`USER_ID`, `MACHINE_ID`),
  CONSTRAINT `fk_USER_MACHINE_MAP_TBL_USERS_TBL1`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `users_tbl` (`USER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_USER_MACHINE_MAP_TBL_MACHINE_TBL1`
    FOREIGN KEY (`MACHINE_ID`)
    REFERENCES `machine_tbl` (`MACHINE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `user_group_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_group_tbl` (
  `GROUP_ID` INT(11) NOT NULL,
  `GROUP_NAME` VARCHAR(20) NOT NULL,
  `DESCRIPTION` VARCHAR(100) NULL,
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  PRIMARY KEY (`GROUP_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `user_group_map_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `user_group_map_tbl` (
  `GROUP_ID` INT(11) NOT NULL,
  `USER_ID` INT(11) NOT NULL,
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  PRIMARY KEY (`GROUP_ID`, `USER_ID`),
  INDEX `fk_USER_GROUP_MAP_TBL_USERS_TBL1_idx` (`USER_ID` ASC),
  CONSTRAINT `fk_USER_GROUP_MAP_TBL_USER_GROUP_TBL1`
    FOREIGN KEY (`GROUP_ID`)
    REFERENCES `user_group_tbl` (`GROUP_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_USER_GROUP_MAP_TBL_USERS_TBL1`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `users_tbl` (`USER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `auto_scaling_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `auto_scaling_tbl` (
  `AUTO_SCALING_ID` INT(11) NOT NULL AUTO_INCREMENT,
  `AUTO_SCALING_NAME` VARCHAR(45) NULL,
  `VM_TEMPLATE_ID` VARCHAR(36) NULL COMMENT 'RHEV에 생성되어 있는 VM Image Template ID',
  `MIN_MACHINE_SIZE` INT(11) NULL COMMENT 'Auto Scaling 시 동작되어지는 최소 머신(인스턴스) 수',
  `MAX_MACHINE_SIZE` INT(11) NULL COMMENT 'Auto Scaling 시 동작되어지는 최대 머신(인스턴스) 수',
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  PRIMARY KEY (`AUTO_SCALING_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `as_policy_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `as_policy_tbl` (
  `AUTO_SCALING_ID` INT(11) NOT NULL,
  `POLICY_NAME` VARCHAR(45) NULL,
  `MON_FACTOR_ID` VARCHAR(10) NULL COMMENT '검사 대상 모니터링 항목',
  `THRESHOLD_UP_LIMIT` INT(11) NULL COMMENT '사용자 정의 임계치 최대 값',
  `THRESHOLD_DOWN_LIMIT` INT(11) NULL COMMENT '사용자 정의 임계치 최소 값',
  `THRESHOLD_UP_PERIOD` INT(11) NULL COMMENT '정의된 모니터링 항목의 측정 값이 최대 임계치를 초과하여 유지될 수 있는 분단위 값으로 해당 시간동안 최대 임계치 이상을 유지할 경우 Scale Up 대상이 된다.',
  `THRESHOLD_DOWN_PERIOD` INT(11) NULL COMMENT '정의된 모니터링 항목의 측정 값이 최소 임계치 이하에서 유지될 수 있는 분단위 값으로 해당 시간동안 최소 임계치 이하를 유지할 경우 Scale Down 대상이 된다.',
  `INCREASE_UNIT` INT(3) NULL COMMENT 'Scale Up 시 생성되어야 할 머신(인스턴스) 갯수',
  `DECREASE_UNIT` INT(3) NULL COMMENT 'Scale Down 시 생성되어야 할 머신(인스턴스) 갯수',
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  PRIMARY KEY (`AUTO_SCALING_ID`),
  INDEX `fk_as_policy_tbl_auto_scaling_tbl1_idx` (`AUTO_SCALING_ID` ASC),
  CONSTRAINT `fk_as_policy_tbl_auto_scaling_tbl1`
    FOREIGN KEY (`AUTO_SCALING_ID`)
    REFERENCES `auto_scaling_tbl` (`AUTO_SCALING_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `load_balancer_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `load_balancer_tbl` (
  `LOAD_BALANCER_ID` INT(11) NOT NULL AUTO_INCREMENT,
  `MACHINE_ID` VARCHAR(36) NOT NULL,
  `LB_NAME` VARCHAR(45) NULL,
  `LB_DNS_NAME` VARCHAR(255) NULL,
  `AUTO_SCALING_ID` INT(11) NULL,
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  PRIMARY KEY (`LOAD_BALANCER_ID`),
  INDEX `fk_LOAD_BALANCER_TBL_AS_GROUP_TBL1_idx` (`AUTO_SCALING_ID` ASC),
  INDEX `fk_load_balancer_tbl_machine_tbl1_idx` (`MACHINE_ID` ASC),
  CONSTRAINT `fk_LOAD_BALANCER_TBL_AS_GROUP_TBL1`
    FOREIGN KEY (`AUTO_SCALING_ID`)
    REFERENCES `auto_scaling_tbl` (`AUTO_SCALING_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_load_balancer_tbl_machine_tbl1`
    FOREIGN KEY (`MACHINE_ID`)
    REFERENCES `machine_tbl` (`MACHINE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `config_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `config_tbl` (
  `MACHINE_ID` VARCHAR(36) NOT NULL,
  `SOFTWARE_ID` INT NOT NULL,
  `CONFIG_FILE_ID` INT NOT NULL,
  `CONFIG_FILE_LOCATION` VARCHAR(200) NULL,
  `CONFIG_FILE_NAME` VARCHAR(45) NULL,
  `CONFIG_FILE_CONTENTS` TEXT NULL,
  `DELETE_YN` CHAR(1) NOT NULL DEFAULT 'N',
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  PRIMARY KEY (`MACHINE_ID`, `SOFTWARE_ID`, `CONFIG_FILE_ID`),
  CONSTRAINT `fk_config_file_info_tbl_software_tbl1`
    FOREIGN KEY (`MACHINE_ID` , `SOFTWARE_ID`)
    REFERENCES `software_tbl` (`MACHINE_ID` , `SOFTWARE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `config_repo_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `config_repo_tbl` (
  `CONFIG_FILE_ID` INT NOT NULL,
  `SOFTWARE_ID` INT NOT NULL,
  `CONFIG_FILE_SOURCE_LOCATION` VARCHAR(200) NULL COMMENT 'Software 설치 시 Agent로 복사 될 초기 설정 파일의 위치로\n복사 대상 설정파일이 아닌 경우 null이 될 수 있다.',
  `CONFIG_FILE_TARGET_LOCATION` VARCHAR(200) NULL COMMENT 'Software가 설치된 Agent의 파일시스템 상의 경로\n(eg.)\nApache :  ${INSTALL_LOCATION}/conf ,\n                ${INSTALL_LOCATION}/conf/extra\nMySQL : /\nTomcat : ${INSTALL_LOCATION}\nJBoss : ${INSTALL_LOCATION}',
  `CONFIG_FILE_NAME` VARCHAR(45) NULL,
  `PROPERTIES` TEXT NULL COMMENT ',(comma)로 구분된 치환 대상 프로퍼티 목록',
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  PRIMARY KEY (`CONFIG_FILE_ID`, `SOFTWARE_ID`),
  INDEX `fk_config_file_tbl_software_repo_tbl1_idx` (`SOFTWARE_ID` ASC),
  CONSTRAINT `fk_config_file_tbl_software_repo_tbl1`
    FOREIGN KEY (`SOFTWARE_ID`)
    REFERENCES `software_repo_tbl` (`SOFTWARE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `provisioning_item_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `provisioning_item_tbl` (
  `PROVISIONING_ID` INT NOT NULL,
  `SOFTWARE_ID` INT NOT NULL,
  `ACTION_NAME` VARCHAR(45) NULL,
  `SHELL_COMMAND` VARCHAR(45) NULL,
  `WORKING_DIR` VARCHAR(255) NULL,
  `SHELL_OPTIONS` VARCHAR(1000) NULL,
  `VARIABLES` VARCHAR(1000) NULL COMMENT 'shell option 또는 config 파일 내에서 치환되어야 할 변수 목록으로 ,(comma)로 구분된다.\n(eg.)\nServerRoot,Port,ServerName',
  `FILE_NAME` VARCHAR(255) NULL,
  `FILE_CONTENTS` TEXT NULL,
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  PRIMARY KEY (`PROVISIONING_ID`, `SOFTWARE_ID`),
  INDEX `fk_provisioning_item_tbl_software_repo_tbl1_idx` (`SOFTWARE_ID` ASC),
  CONSTRAINT `fk_provisioning_item_tbl_software_repo_tbl1`
    FOREIGN KEY (`SOFTWARE_ID`)
    REFERENCES `software_repo_tbl` (`SOFTWARE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `lb_listener_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lb_listener_tbl` (
  `LOAD_BALANCER_ID` INT(11) NOT NULL,
  `LISTEN_PORT` INT(11) NOT NULL,
  `PROTOCOL` VARCHAR(5) NULL,
  `STICKINESS_YN` CHAR(1) NOT NULL DEFAULT 'N',
  `BACKEND_PORT` INT(11) NULL,
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  PRIMARY KEY (`LOAD_BALANCER_ID`, `LISTEN_PORT`),
  INDEX `fk_lb_rules_tbl_load_balancer_tbl1_idx` (`LOAD_BALANCER_ID` ASC),
  CONSTRAINT `fk_lb_rules_tbl_load_balancer_tbl1`
    FOREIGN KEY (`LOAD_BALANCER_ID`)
    REFERENCES `load_balancer_tbl` (`LOAD_BALANCER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `lb_machine_map_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lb_machine_map_tbl` (
  `LOAD_BALANCER_ID` INT(11) NOT NULL,
  `MACHINE_ID` VARCHAR(36) NOT NULL,
  `BACKUP_YN` CHAR(1) NOT NULL DEFAULT 'N' COMMENT 'The server is only used in load balancing when all other non-backup servers are unavailable.',
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  PRIMARY KEY (`LOAD_BALANCER_ID`, `MACHINE_ID`),
  INDEX `fk_lb_machine_map_tbl_machine_tbl1_idx` (`MACHINE_ID` ASC),
  CONSTRAINT `fk_lb_machine_map_tbl_load_balancer_tbl1`
    FOREIGN KEY (`LOAD_BALANCER_ID`)
    REFERENCES `load_balancer_tbl` (`LOAD_BALANCER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_lb_machine_map_tbl_machine_tbl1`
    FOREIGN KEY (`MACHINE_ID`)
    REFERENCES `machine_tbl` (`MACHINE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

INSERT INTO mon_factor_tbl(MON_FACTOR_ID,MON_FACTOR_NAME,MON_FACTOR_UNIT,MON_FACTOR_DESC,AUTO_SCALING_YN,DISPLAY_NAME,REG_USER_ID,REG_DT,UPD_USER_ID,UPD_DT)
VALUES ('FACTOR_001','IDLE_CPU','%','Percentage of time that the CPU or CPUs were not processing any commands and the system did not have an outstanding disk I/O request.',null,null,1,NOW(),1,NOW());
INSERT INTO mon_factor_tbl(MON_FACTOR_ID,MON_FACTOR_NAME,MON_FACTOR_UNIT,MON_FACTOR_DESC,AUTO_SCALING_YN,DISPLAY_NAME,REG_USER_ID,REG_DT,UPD_USER_ID,UPD_DT)
VALUES ('FACTOR_002','COMBINED_CPU','%','Percentage of CPU utilization while executing at all(system, kernel, user, application).','Y','CPU',1,NOW(),1,NOW());
INSERT INTO mon_factor_tbl(MON_FACTOR_ID,MON_FACTOR_NAME,MON_FACTOR_UNIT,MON_FACTOR_DESC,AUTO_SCALING_YN,DISPLAY_NAME,REG_USER_ID,REG_DT,UPD_USER_ID,UPD_DT)
VALUES ('FACTOR_003','TOTAL_MEMORY','KB','Total system memory',null,null,1,NOW(),1,NOW());
INSERT INTO mon_factor_tbl(MON_FACTOR_ID,MON_FACTOR_NAME,MON_FACTOR_UNIT,MON_FACTOR_DESC,AUTO_SCALING_YN,DISPLAY_NAME,REG_USER_ID,REG_DT,UPD_USER_ID,UPD_DT)
VALUES ('FACTOR_004','FREE_MEMORY','KB','Total free system memory',null,null,1,NOW(),1,NOW());
INSERT INTO mon_factor_tbl(MON_FACTOR_ID,MON_FACTOR_NAME,MON_FACTOR_UNIT,MON_FACTOR_DESC,AUTO_SCALING_YN,DISPLAY_NAME,REG_USER_ID,REG_DT,UPD_USER_ID,UPD_DT)
VALUES ('FACTOR_005','USED_MEMORY','KB','Total used system memory','Y','MEMORY',1,NOW(),1,NOW());

INSERT INTO software_repo_tbl (SOFTWARE_ID,SOFTWARE_NAME,SOFTWARE_VERSION,SOFTWARE_VENDOR,FILE_LOCATION,FILE_NAME,DESCRIPTION,REG_USER_ID,REG_DT,UPD_USER_ID,UPD_DT) 
VALUES (1,'Apache','2.2.25','Apache Software Foundation','/repo/apache/2.2.25/','httpd-2.2.25.tar.gz','Apache HTTP Daemon',1,NOW(),1,NOW());
INSERT INTO software_repo_tbl (SOFTWARE_ID,SOFTWARE_NAME,SOFTWARE_VERSION,SOFTWARE_VENDOR,FILE_LOCATION,FILE_NAME,DESCRIPTION,REG_USER_ID,REG_DT,UPD_USER_ID,UPD_DT) 
VALUES (2,'MySQL','5.5.34','Oracle Corporation','/repo/mysql/5.5.34/','MySQL-server.rpm,MySQL-client.rpm','MySQL 5.5.34',1,NOW(),1,NOW());
INSERT INTO software_repo_tbl (SOFTWARE_ID,SOFTWARE_NAME,SOFTWARE_VERSION,SOFTWARE_VENDOR,FILE_LOCATION,FILE_NAME,DESCRIPTION,REG_USER_ID,REG_DT,UPD_USER_ID,UPD_DT) 
VALUES (3,'JBoss','5.2.0','Red Hat,Inc.','/repo/jboss/','jboss-eap-5.2.0.zip,jboss-cluster-template-5.2.0.zip','JBoss EAP 5.2.0',1,NOW(),1,NOW());
INSERT INTO software_repo_tbl (SOFTWARE_ID,SOFTWARE_NAME,SOFTWARE_VERSION,SOFTWARE_VENDOR,FILE_LOCATION,FILE_NAME,DESCRIPTION,REG_USER_ID,REG_DT,UPD_USER_ID,UPD_DT) 
VALUES (4,'Tomcat','6.0.37','Apache Software Foundation','/repo/tomcat/','apache-tomcat-6.0.37.zip,tomcat-template-6.0.37.zip','Apache Tomcat 6.0.37',1,NOW(),1,NOW());

INSERT INTO config_repo_tbl (CONFIG_FILE_ID,SOFTWARE_ID,CONFIG_FILE_SOURCE_LOCATION,CONFIG_FILE_TARGET_LOCATION,CONFIG_FILE_NAME,PROPERTIES,REG_USER_ID,REG_DT,UPD_USER_ID,UPD_DT) 
VALUES (1,1,'/repo/apache/2.2.25/conf/','${INSTALL_LOCATION}/conf/','httpd.conf','ServerRoot,Port,ServerName',1,NOW(),1,NOW());
INSERT INTO config_repo_tbl (CONFIG_FILE_ID,SOFTWARE_ID,CONFIG_FILE_SOURCE_LOCATION,CONFIG_FILE_TARGET_LOCATION,CONFIG_FILE_NAME,PROPERTIES,REG_USER_ID,REG_DT,UPD_USER_ID,UPD_DT) 
VALUES (2,1,'/repo/apache/2.2.25/conf/','${INSTALL_LOCATION}/conf/extra/','httpd-mpm.conf',NULL,1,NOW(),1,NOW());
INSERT INTO config_repo_tbl (CONFIG_FILE_ID,SOFTWARE_ID,CONFIG_FILE_SOURCE_LOCATION,CONFIG_FILE_TARGET_LOCATION,CONFIG_FILE_NAME,PROPERTIES,REG_USER_ID,REG_DT,UPD_USER_ID,UPD_DT) 
VALUES (3,1,'/repo/apache/2.2.25/conf/','${INSTALL_LOCATION}/conf/','mod-jk.conf',NULL,1,NOW(),1,NOW());
INSERT INTO config_repo_tbl (CONFIG_FILE_ID,SOFTWARE_ID,CONFIG_FILE_SOURCE_LOCATION,CONFIG_FILE_TARGET_LOCATION,CONFIG_FILE_NAME,PROPERTIES,REG_USER_ID,REG_DT,UPD_USER_ID,UPD_DT) 
VALUES (4,1,'/repo/apache/2.2.25/conf/','${INSTALL_LOCATION}/conf/','uriworkermap.properties',NULL,1,NOW(),1,NOW());
INSERT INTO config_repo_tbl (CONFIG_FILE_ID,SOFTWARE_ID,CONFIG_FILE_SOURCE_LOCATION,CONFIG_FILE_TARGET_LOCATION,CONFIG_FILE_NAME,PROPERTIES,REG_USER_ID,REG_DT,UPD_USER_ID,UPD_DT) 
VALUES (5,1,'/repo/apache/2.2.25/conf/','${INSTALL_LOCATION}/conf/','workers.properties',NULL,1,NOW(),1,NOW());
INSERT INTO config_repo_tbl (CONFIG_FILE_ID,SOFTWARE_ID,CONFIG_FILE_SOURCE_LOCATION,CONFIG_FILE_TARGET_LOCATION,CONFIG_FILE_NAME,PROPERTIES,REG_USER_ID,REG_DT,UPD_USER_ID,UPD_DT) 
VALUES (6,2,'/repo/mysql/5.5.34/','/etc/','my.cnf','mysql.datadir,mysql.port',1,NOW(),1,NOW());
INSERT INTO config_repo_tbl (CONFIG_FILE_ID,SOFTWARE_ID,CONFIG_FILE_SOURCE_LOCATION,CONFIG_FILE_TARGET_LOCATION,CONFIG_FILE_NAME,PROPERTIES,REG_USER_ID,REG_DT,UPD_USER_ID,UPD_DT) 
VALUES (7,3,'/repo/jboss/datasource/','${INSTALL_LOCATION}/','${db_type}-ds.xml','databaseType,jndiName,connectionUrl,userName,password,minPoolSize,maxPoolSize',1,NOW(),1,NOW());
INSERT INTO config_repo_tbl (CONFIG_FILE_ID,SOFTWARE_ID,CONFIG_FILE_SOURCE_LOCATION,CONFIG_FILE_TARGET_LOCATION,CONFIG_FILE_NAME,PROPERTIES,REG_USER_ID,REG_DT,UPD_USER_ID,UPD_DT) 
VALUES (8,3,NULL,'${INSTALL_LOCATION}/bin/','env.sh','jbossHome,serverHome,serverName,partitionName,bindAddress,bindPort',1,NOW(),1,NOW());
INSERT INTO config_repo_tbl (CONFIG_FILE_ID,SOFTWARE_ID,CONFIG_FILE_SOURCE_LOCATION,CONFIG_FILE_TARGET_LOCATION,CONFIG_FILE_NAME,PROPERTIES,REG_USER_ID,REG_DT,UPD_USER_ID,UPD_DT) 
VALUES (9,4,NULL,'${INSTALL_LOCATION}/bin/','env.sh','javaHome,serverName,catalinaHome,catalinaBase,portOffset,compUser',1,NOW(),1,NOW());

INSERT INTO `roles_tbl` (`ROLE_ID`, `ROLE_NAME`, `PERMISSION`, `REG_DT`) VALUES ('1', 'admin', 'all', NOW());
INSERT INTO `roles_tbl` (`ROLE_ID`, `ROLE_NAME`, `PERMISSION`, `REG_DT`) VALUES ('2', 'role2', 'write', NOW());

INSERT INTO `users_tbl` (`USER_ID`, `ROLE_ID`, `LOGIN_ID`, `HASHED_PASSWD`, `USER_NAME`, `DEPT_NAME`, `EMAIL`, `IS_ADMIN`, `STATUS`, `REG_DT`) 
VALUES ('1', '1', 'admin', password('admin'), 'Administrator', '개발팀', 'admin@osci.kr', '0', '1', NOW());

INSERT INTO `user_group_tbl` (`GROUP_ID`, `GROUP_NAME`, `DESCRIPTION`, `REG_DT`) VALUES ('1', 'Administra', 'Open Source Consulting', NOW());
INSERT INTO `user_group_tbl` (`GROUP_ID`, `GROUP_NAME`, `DESCRIPTION`, `REG_DT`) VALUES ('2', 'ncia', '...', NOW());

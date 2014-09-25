SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `peacock` ;
CREATE SCHEMA IF NOT EXISTS `peacock` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `peacock` ;

-- -----------------------------------------------------
-- Table `peacock`.`hypervisor_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peacock`.`hypervisor_tbl` (
  `HYPERVISOR_ID` INT(11) NOT NULL AUTO_INCREMENT COMMENT '하이퍼바이저 아이디',
  `HYPERVISOR_TYPE` VARCHAR(45) NULL COMMENT '하이퍼바이저 타입(RHEV, Xen, VMWare, AWS 등)',
  `RHEVM_NAME` VARCHAR(45) NULL,
  `RHEVM_PROTOCOL` VARCHAR(45) NULL,
  `RHEVM_PORT` VARCHAR(5) NULL,
  `RHEVM_DOMAIN` VARCHAR(45) NULL,
  `RHEVM_HOST` VARCHAR(45) NULL,
  `RHEVM_USERNAME` VARCHAR(45) NULL,
  `RHEVM_PASSWORD` VARCHAR(45) NULL,
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  PRIMARY KEY (`HYPERVISOR_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `peacock`.`machine_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peacock`.`machine_tbl` (
  `MACHINE_ID` VARCHAR(36) NOT NULL,
  `HYPERVISOR_ID` INT(11) NULL,
  `DISPLAY_ID` VARCHAR(10) NOT NULL COMMENT '사용자에게 보여지기 위한 Instance ID 값으로 테이블 내에서 유니크 하도록 생성',
  `DISPLAY_NAME` VARCHAR(45) NULL COMMENT '사용자가 지정한 인스턴스 이름',
  `CLUSTER` VARCHAR(45) NULL,
  `IS_PRD` CHAR(1) NOT NULL DEFAULT 'N' COMMENT '운영용 인스턴스 여부',
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
  `DELETE_YN` CHAR(1) NOT NULL DEFAULT 'N',
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  PRIMARY KEY (`MACHINE_ID`),
  INDEX `fk_machine_tbl_hypervisor_tbl1_idx` (`HYPERVISOR_ID` ASC),
  UNIQUE INDEX `DISPLAY_ID_UNIQUE` (`DISPLAY_ID` ASC),
  CONSTRAINT `fk_machine_tbl_hypervisor_tbl1`
    FOREIGN KEY (`HYPERVISOR_ID`)
    REFERENCES `peacock`.`hypervisor_tbl` (`HYPERVISOR_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `peacock`.`software_repo_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peacock`.`software_repo_tbl` (
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
-- Table `peacock`.`software_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peacock`.`software_tbl` (
  `MACHINE_ID` VARCHAR(36) NOT NULL,
  `SOFTWARE_ID` INT NOT NULL,
  `INSTALL_LOCATION` VARCHAR(255) NULL,
  `INSTALL_STAT` VARCHAR(45) NULL COMMENT 'RUNNING, ERROR, COMPLETED',
  `INSTALL_LOG` LONGTEXT NULL,
  `SERVICE_STOP_CMD` VARCHAR(255) NULL,
  `SERVICE_START_CMD` VARCHAR(255) NULL,
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
    REFERENCES `peacock`.`machine_tbl` (`MACHINE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_software_tbl_software_repo_tbl1`
    FOREIGN KEY (`SOFTWARE_ID`)
    REFERENCES `peacock`.`software_repo_tbl` (`SOFTWARE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `peacock`.`mon_factor_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peacock`.`mon_factor_tbl` (
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
-- Table `peacock`.`mon_data_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peacock`.`mon_data_tbl` (
  `MACHINE_ID` VARCHAR(36) NOT NULL,
  `MON_FACTOR_ID` VARCHAR(10) NOT NULL,
  `MON_DATA_ID` INT(11) NOT NULL AUTO_INCREMENT,
  `MON_DATA_VALUE` VARCHAR(255) NOT NULL,
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  PRIMARY KEY (`MON_DATA_ID`, `MACHINE_ID`, `MON_FACTOR_ID`),
  INDEX `fk_MON_DATA_TBL_MON_FACTOR_TBL1_idx` (`MON_FACTOR_ID` ASC),
  INDEX `fk_MON_DATA_TBL_MACHINE_TBL1_idx` (`MACHINE_ID` ASC),
  INDEX `RED_DT_idx` (`MACHINE_ID` ASC, `REG_DT` ASC),
  CONSTRAINT `fk_MON_DATA_TBL_MON_FACTOR_TBL1`
    FOREIGN KEY (`MON_FACTOR_ID`)
    REFERENCES `peacock`.`mon_factor_tbl` (`MON_FACTOR_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_MON_DATA_TBL_MACHINE_TBL1`
    FOREIGN KEY (`MACHINE_ID`)
    REFERENCES `peacock`.`machine_tbl` (`MACHINE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `peacock`.`os_package_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peacock`.`os_package_tbl` (
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
    REFERENCES `peacock`.`machine_tbl` (`MACHINE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `peacock`.`users_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peacock`.`users_tbl` (
  `USER_ID` INT(11) NOT NULL,
  `LOGIN_ID` VARCHAR(30) NULL,
  `USER_NAME` VARCHAR(20) NULL,
  `PASSWD` VARCHAR(45) NULL,
  `HASHED_PASSWD` VARCHAR(50) NULL,
  `DEPT_NAME` VARCHAR(30) NULL,
  `EMAIL` VARCHAR(60) NULL,
  `LAST_LOGON` DATETIME NULL,
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  PRIMARY KEY (`USER_ID`),
  UNIQUE INDEX `LOGIN_ID_UNIQUE` (`LOGIN_ID` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `peacock`.`user_machine_map_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peacock`.`user_machine_map_tbl` (
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
    REFERENCES `peacock`.`users_tbl` (`USER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_USER_MACHINE_MAP_TBL_MACHINE_TBL1`
    FOREIGN KEY (`MACHINE_ID`)
    REFERENCES `peacock`.`machine_tbl` (`MACHINE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `peacock`.`auto_scaling_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peacock`.`auto_scaling_tbl` (
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
-- Table `peacock`.`as_policy_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peacock`.`as_policy_tbl` (
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
    REFERENCES `peacock`.`auto_scaling_tbl` (`AUTO_SCALING_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `peacock`.`load_balancer_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peacock`.`load_balancer_tbl` (
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
    REFERENCES `peacock`.`auto_scaling_tbl` (`AUTO_SCALING_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_load_balancer_tbl_machine_tbl1`
    FOREIGN KEY (`MACHINE_ID`)
    REFERENCES `peacock`.`machine_tbl` (`MACHINE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `peacock`.`config_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peacock`.`config_tbl` (
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
    REFERENCES `peacock`.`software_tbl` (`MACHINE_ID` , `SOFTWARE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `peacock`.`config_repo_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peacock`.`config_repo_tbl` (
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
    REFERENCES `peacock`.`software_repo_tbl` (`SOFTWARE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `peacock`.`lb_listener_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peacock`.`lb_listener_tbl` (
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
    REFERENCES `peacock`.`load_balancer_tbl` (`LOAD_BALANCER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `peacock`.`lb_machine_map_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peacock`.`lb_machine_map_tbl` (
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
    REFERENCES `peacock`.`load_balancer_tbl` (`LOAD_BALANCER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_lb_machine_map_tbl_machine_tbl1`
    FOREIGN KEY (`MACHINE_ID`)
    REFERENCES `peacock`.`machine_tbl` (`MACHINE_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `peacock`.`machine_additional_info_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peacock`.`machine_additional_info_tbl` (
  `MACHINE_ID` VARCHAR(36) NOT NULL,
  `HOST_NAME` VARCHAR(255) NULL,
  `IP_ADDRESS` VARCHAR(15) NULL,
  `NETMASK` VARCHAR(15) NULL,
  `GATEWAY` VARCHAR(15) NULL,
  `NAME_SERVER` VARCHAR(100) NULL COMMENT '\',\' 구분자로 구분된 네임서버 목록',
  `APPLY_YN` CHAR(1) NOT NULL DEFAULT 'N',
  `SSH_PORT` VARCHAR(5) NULL COMMENT 'Instance(Machine)의 부가 정보(고정 IP 관련 정보, SSH 관련 접속 정보 등)을 저장하기 위한 테이블로 machine_tbl과 1:1로 매핑된다. machine_tbl은 실제 Agent가 구동 된 이후에 값이 저장되는 테이블로써 초기 사용자에 의해 설정된 값을 사전에 저장하기 위해 별도의 테이블로 구성한다.',
  `SSH_USERNAME` VARCHAR(255) NULL,
  `SSH_PASSWORD` VARCHAR(255) NULL,
  `SSH_KEY_FILE` VARCHAR(255) NULL,
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  PRIMARY KEY (`MACHINE_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `peacock`.`alm_project_repository_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peacock`.`alm_project_repository_tbl` (
  `REPOSITORY_CODE` VARCHAR(20) NOT NULL,
  `REPOSIROTY_TYPE` INT NOT NULL,
  `REPOSIROTY_DESCRIPTION` VARCHAR(255) NULL,
  `REPOSIROTY_URL` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`REPOSITORY_CODE`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `peacock`.`alm_project_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peacock`.`alm_project_tbl` (
  `PROJECT_CODE` VARCHAR(20) NOT NULL,
  `PROJECT_NAME` VARCHAR(100) NOT NULL,
  `PROJECT_DESCRIPTION` VARCHAR(255) NULL,
  `REPOSITORY_CODE` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`PROJECT_CODE`),
  INDEX `fk_alm_project_tbl_alm_project_repository_tbl1_idx` (`REPOSITORY_CODE` ASC),
  CONSTRAINT `fk_alm_project_tbl_alm_project_repository_tbl1`
    FOREIGN KEY (`REPOSITORY_CODE`)
    REFERENCES `peacock`.`alm_project_repository_tbl` (`REPOSITORY_CODE`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `peacock`.`alm_project_mapping_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peacock`.`alm_project_mapping_tbl` (
  `PROJECT_CODE` VARCHAR(20) NOT NULL,
  `MAPPING_TYPE` INT NOT NULL,
  `MAPPING_CODE` VARCHAR(45) NOT NULL,
  INDEX `fk_table1_alm_project_tbl1_idx` (`PROJECT_CODE` ASC),
  PRIMARY KEY (`PROJECT_CODE`, `MAPPING_TYPE`, `MAPPING_CODE`),
  CONSTRAINT `fk_table1_alm_project_tbl1`
    FOREIGN KEY (`PROJECT_CODE`)
    REFERENCES `peacock`.`alm_project_tbl` (`PROJECT_CODE`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `peacock`.`time_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peacock`.`time_tbl` (
  `REG_DT` DATETIME NOT NULL,
  PRIMARY KEY (`REG_DT`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `peacock`.`permission_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peacock`.`permission_tbl` (
  `PERM_ID` INT NOT NULL,
  `PERM_NM` VARCHAR(100) NOT NULL,
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  PRIMARY KEY (`PERM_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `peacock`.`menu_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peacock`.`menu_tbl` (
  `MENU_ID` INT NOT NULL,
  `MENU_NM` VARCHAR(100) NOT NULL,
  `THREAD` VARCHAR(10) NOT NULL,
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  PRIMARY KEY (`MENU_ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `peacock`.`permission_menu_map_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peacock`.`permission_menu_map_tbl` (
  `PERM_ID` INT NOT NULL,
  `MENU_ID` INT NOT NULL,
  `READ_YN` INT NOT NULL DEFAULT 0,
  `WRITE_YN` INT NOT NULL DEFAULT 0,
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  INDEX `fk_permission_menu_map_tbl_menu_tbl1_idx` (`MENU_ID` ASC),
  INDEX `fk_permission_menu_map_tbl_permission_tbl1_idx` (`PERM_ID` ASC),
  PRIMARY KEY (`PERM_ID`, `MENU_ID`),
  CONSTRAINT `fk_permission_menu_map_tbl_menu_tbl1`
    FOREIGN KEY (`MENU_ID`)
    REFERENCES `peacock`.`menu_tbl` (`MENU_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_permission_menu_map_tbl_permission_tbl1`
    FOREIGN KEY (`PERM_ID`)
    REFERENCES `peacock`.`permission_tbl` (`PERM_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `peacock`.`permission_user_map_tbl`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `peacock`.`permission_user_map_tbl` (
  `PERM_ID` INT NOT NULL,
  `USER_ID` INT(11) NOT NULL,
  `REG_USER_ID` INT(11) NULL,
  `REG_DT` DATETIME NULL,
  `UPD_USER_ID` INT(11) NULL,
  `UPD_DT` DATETIME NULL,
  INDEX `fk_permission_user_map_tbl_permission_tbl1_idx` (`PERM_ID` ASC),
  INDEX `fk_permission_user_map_tbl_users_tbl1_idx` (`USER_ID` ASC),
  PRIMARY KEY (`PERM_ID`, `USER_ID`),
  CONSTRAINT `fk_permission_user_map_tbl_permission_tbl1`
    FOREIGN KEY (`PERM_ID`)
    REFERENCES `peacock`.`permission_tbl` (`PERM_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_permission_user_map_tbl_users_tbl1`
    FOREIGN KEY (`USER_ID`)
    REFERENCES `peacock`.`users_tbl` (`USER_ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

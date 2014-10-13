package com.athena.peacock.controller.web.alm.project.migration;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.athena.peacock.controller.web.common.dao.AbstractBaseDao;

@Repository
public class AlmProjectMigrationDao extends AbstractBaseDao {

	public List<ProjectMigrationUserDto> getUserList() {
		return sqlSession.selectList("ProjectMigrationMapper.getAllUser");
	}

	public void checkUser(ProjectMigrationUserDto migrationUser) {
		sqlSession.update("ProjectMigrationMapper.updateProjectUserCheck",
				migrationUser);
	}

}

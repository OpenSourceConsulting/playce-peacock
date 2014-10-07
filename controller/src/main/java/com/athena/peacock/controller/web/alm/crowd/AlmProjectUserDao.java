package com.athena.peacock.controller.web.alm.crowd;

import org.springframework.stereotype.Repository;

import com.athena.peacock.controller.web.alm.crowd.dto.ProjectUserDto;
import com.athena.peacock.controller.web.common.dao.AbstractBaseDao;

@Repository
public class AlmProjectUserDao extends AbstractBaseDao {

	public void insertProjectUser(ProjectUserDto user) {
		sqlSession.insert("ProjectUserMapper.insertProjectUser", user);
	}

	public void updateProjectUser(ProjectUserDto user) {
		sqlSession.update("ProjectUserMapper.updateProjectUser", user);
	}

}

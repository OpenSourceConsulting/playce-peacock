package com.athena.peacock.controller.web.alm.project;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.athena.peacock.controller.web.alm.project.dto.ProjectDto;
import com.athena.peacock.controller.web.common.dao.AbstractBaseDao;

@Repository("almProjectDao")
public class AlmProjectDao extends AbstractBaseDao {

	public ProjectDto getProject(String projectCode){
		return sqlSession.selectOne("ProjectMapper.getProject", projectCode);
	}
	
	public List<ProjectDto> getProjectList(){
		return sqlSession.selectList("ProjectMapper.getProjectList");
	}
}

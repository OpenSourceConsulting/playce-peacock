package com.athena.peacock.controller.web.alm.project;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.athena.peacock.controller.web.alm.project.dto.ProjectDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectMappingDto;
import com.athena.peacock.controller.web.common.dao.AbstractBaseDao;

@Repository("almProjectDao")
public class AlmProjectDao extends AbstractBaseDao {

	public ProjectDto getProject(String projectCode){
		return sqlSession.selectOne("ProjectMapper.getProject", projectCode);
	}
	
	public List<ProjectDto> getProjectList(){
		return sqlSession.selectList("ProjectMapper.getProjectList");
	}
	
	public void insertProject(ProjectDto project){
		sqlSession.insert("ProjectMapper.insertProject", project);
	}
	
	public void insertProjectMapping(ProjectMappingDto mappingDto){
		sqlSession.insert("ProjectMapper.insertProjectMapping",mappingDto);
	}
	
	public int getProjectExist(String projectCode){
		return sqlSession.selectOne("ProjectMapper.getProjectExist", projectCode);
	}
}

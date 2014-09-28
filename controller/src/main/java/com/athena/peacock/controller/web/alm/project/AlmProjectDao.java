package com.athena.peacock.controller.web.alm.project;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.athena.peacock.controller.web.alm.project.dto.ProjectDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectHistoryDto;
import com.athena.peacock.controller.web.alm.project.dto.ProjectMappingDto;
import com.athena.peacock.controller.web.common.dao.AbstractBaseDao;
import com.athena.peacock.controller.web.user.UserDto;

@Repository("almProjectDao")
public class AlmProjectDao extends AbstractBaseDao {

	public ProjectDto getProject(String projectCode) {
		return sqlSession.selectOne("ProjectMapper.getProject", projectCode);
	}

	public List<ProjectDto> getProjectList() {
		return sqlSession.selectList("ProjectMapper.getProjectList");
	}

	public void insertProject(ProjectDto project) {
		sqlSession.insert("ProjectMapper.insertProject", project);
	}

	public List<ProjectMappingDto> getProjectMapping(
			ProjectMappingDto mappingDto) {

		return sqlSession.selectList("ProjectMapper.getProjectMapping",
				mappingDto);
	}

	public List<ProjectMappingDto> getProjectMappingStandBy() {

		return sqlSession.selectList("ProjectMapper.getProjectMappingStandBy");
	}

	public void insertProjectMapping(ProjectMappingDto mappingDto) {
		sqlSession.insert("ProjectMapper.insertProjectMapping", mappingDto);
	}

	public void deleteProjectMapping(ProjectMappingDto mappingDto) {
		sqlSession.delete("ProjectMapper.deleteProjectMapping", mappingDto);
	}
	
	public void startProjectMappingJob(ProjectMappingDto mappingDto){
		sqlSession.update("ProjectMapper.startProjectMapping", mappingDto);
	}
	
	public void endProjectMappingJob(ProjectMappingDto mappingDto){
		sqlSession.update("ProjectMapper.endProjectMapping", mappingDto);
	}
	

	public int getProjectExist(String projectCode) {
		return sqlSession.selectOne("ProjectMapper.getProjectExist",
				projectCode);
	}

	public void insertProjectHistory(ProjectHistoryDto projectHistory) {
		sqlSession.insert("ProjectMapper.insertProjectHistory", projectHistory);
	}
}

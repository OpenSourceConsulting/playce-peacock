package com.athena.peacock.controller.web.alm.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.peacock.controller.web.alm.repository.dto.RepositoryDto;
import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;

@Service
public class AlmReposirotyService {

	@Autowired
	private AlmReposirotyDao repositoryDao;

	public AlmReposirotyService() {
		// TODO Auto-generated constructor stub

	}

	public GridJsonResponse getRepositoryList(ExtjsGridParam gridParam) {

		GridJsonResponse response = new GridJsonResponse();
		List<RepositoryDto> projects = repositoryDao.getRepositoryList();
		response.setList(projects);

		return response;
	}

	public DtoJsonResponse getRepository(String repositoryCode) {

		DtoJsonResponse response = new DtoJsonResponse();

		RepositoryDto dto = repositoryDao.getRepository(repositoryCode);
		response.setData(dto);
		return response;
	}
	
	public DtoJsonResponse insertAlmProjectRepository(RepositoryDto param){
		
		DtoJsonResponse response = new DtoJsonResponse();
		response.setMsg("Repository가 등록되었습니다.");
		repositoryDao.insertAlmProjectRepository(param);
	
		return response;
	}
}

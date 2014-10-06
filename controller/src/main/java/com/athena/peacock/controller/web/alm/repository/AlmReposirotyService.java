package com.athena.peacock.controller.web.alm.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.peacock.controller.web.alm.repository.dto.RepositoryDto;
import com.athena.peacock.controller.web.alm.svn.AlmSvnService;
import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;

@Service
public class AlmReposirotyService {

	@Autowired
	private AlmReposirotyDao repositoryDao;

	@Autowired
	private AlmSvnService svnService;

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

	public DtoJsonResponse insertAlmProjectRepository(RepositoryDto param) {

		DtoJsonResponse response = new DtoJsonResponse();
		response.setMsg("리포지토리가 등록되었습니다.");
		param.setRepositoryStatus("FAIL");
		svnService.checkSvnProject(param.getRepositoryCode());
		repositoryDao.insertAlmProjectRepository(param);

		return response;
	}

	public DtoJsonResponse deleteAlmProjectRepository(String repositoryCode) {

		DtoJsonResponse response = new DtoJsonResponse();
		response.setMsg("리포지토리가 삭제되었습니다.");
		repositoryDao.deleteAlmProjectRepository(repositoryCode);
		return response;
	}
}

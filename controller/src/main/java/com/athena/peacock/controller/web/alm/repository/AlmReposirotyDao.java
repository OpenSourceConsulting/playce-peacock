package com.athena.peacock.controller.web.alm.repository;

import org.springframework.stereotype.Repository;

import com.athena.peacock.controller.web.alm.repository.dto.RepositoryDto;
import com.athena.peacock.controller.web.common.dao.AbstractBaseDao;

@Repository("almRepositoryDao")
public class AlmReposirotyDao extends AbstractBaseDao {

	public RepositoryDto getRepository(String repositoryCode) {
		return sqlSession.selectOne("ProjectMapper.getProject", repositoryCode);
	}

}

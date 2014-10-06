package com.athena.peacock.controller.web.alm.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.athena.peacock.controller.web.alm.repository.dto.RepositoryDto;
import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;

/**
 * <pre>
 * 사용자 관리 컨트롤러.
 * </pre>
 * 
 * @author Jungsu Han
 * @version 1.0
 */
@Controller
@RequestMapping("/alm")
public class AlmReposirotyController {

	@Autowired
	private AlmReposirotyService almRepositoryService;

	public AlmReposirotyController() {
		// TODO Auto-generated constructor stub
	}

	@RequestMapping(value = "/repository", method = RequestMethod.GET)
	public @ResponseBody
	GridJsonResponse getRepositoryList(
			@RequestParam(value = "offset", defaultValue = "0") int offset,
			@RequestParam(value = "search", required = false) String search) {

		ExtjsGridParam gridParam = new ExtjsGridParam();
		gridParam.setPage(offset);

		if (search != null) {
			gridParam.setSearch(search);
		}

		return almRepositoryService.getRepositoryList(gridParam);
	}

	@RequestMapping(value = "/repository", method = RequestMethod.POST)
	public @ResponseBody
	DtoJsonResponse createRepositoryList(@RequestBody RepositoryDto param) {
		return almRepositoryService.insertAlmProjectRepository(param);
	}

	@RequestMapping(value = "/repository/{repositoryCode}", method = RequestMethod.DELETE)
	public @ResponseBody
	DtoJsonResponse deleteRepositoryList(@PathVariable String repositoryCode) {
		return almRepositoryService.deleteAlmProjectRepository(repositoryCode);
	}

	@RequestMapping(value = "/repository/{repositoryCode}", method = RequestMethod.GET)
	public @ResponseBody
	DtoJsonResponse list(@PathVariable String repositoryCode) {
		return almRepositoryService.getRepository(repositoryCode);
	}

}

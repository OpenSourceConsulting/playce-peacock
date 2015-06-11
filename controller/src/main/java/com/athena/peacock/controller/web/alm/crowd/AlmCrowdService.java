/* 
 * Athena Peacock Project - Server Provisioning Engine for IDC or Cloud
 * 
 * Copyright (C) 2013 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Bong-Jin Kwon	2013. 9. 25.		First Draft.
 */
package com.athena.peacock.controller.web.alm.crowd;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.athena.peacock.controller.web.alm.crowd.dto.AlmGroupDto;
import com.athena.peacock.controller.web.alm.crowd.dto.AlmUserAddDto;
import com.athena.peacock.controller.web.alm.crowd.dto.AlmUserDto;
import com.athena.peacock.controller.web.alm.crowd.dto.ProjectUserDto;
import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;
import com.atlassian.crowd.embedded.api.PasswordCredential;
import com.atlassian.crowd.embedded.api.SearchRestriction;
import com.atlassian.crowd.exception.ApplicationPermissionException;
import com.atlassian.crowd.exception.GroupNotFoundException;
import com.atlassian.crowd.exception.InvalidAuthenticationException;
import com.atlassian.crowd.exception.InvalidCredentialException;
import com.atlassian.crowd.exception.InvalidGroupException;
import com.atlassian.crowd.exception.InvalidUserException;
import com.atlassian.crowd.exception.MembershipAlreadyExistsException;
import com.atlassian.crowd.exception.MembershipNotFoundException;
import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.exception.UserNotFoundException;
import com.atlassian.crowd.integration.rest.entity.GroupEntity;
import com.atlassian.crowd.integration.rest.entity.PasswordEntity;
import com.atlassian.crowd.integration.rest.entity.UserEntity;
import com.atlassian.crowd.integration.rest.service.factory.RestCrowdClientFactory;
import com.atlassian.crowd.model.group.Group;
import com.atlassian.crowd.model.group.GroupType;
import com.atlassian.crowd.model.user.User;
import com.atlassian.crowd.search.builder.Restriction;
import com.atlassian.crowd.search.query.entity.restriction.BooleanRestriction;
import com.atlassian.crowd.search.query.entity.restriction.BooleanRestrictionImpl;
import com.atlassian.crowd.search.query.entity.restriction.PropertyRestriction;
import com.atlassian.crowd.search.query.entity.restriction.constants.GroupTermKeys;
import com.atlassian.crowd.search.query.entity.restriction.constants.UserTermKeys;
import com.atlassian.crowd.service.client.CrowdClient;

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Bong-Jin Kwon
 * @version 1.0
 */
@Service
public class AlmCrowdService {

	protected final Logger logger = LoggerFactory
			.getLogger(AlmCrowdService.class);

	@Value("#{contextProperties['alm.crowd.url']}")
	private String crowdUrl;

	@Value("#{contextProperties['alm.crowd.id']}")
	private String crowdId;

	@Value("#{contextProperties['alm.crowd.pw']}")
	private String crowdPw;

	@Value("#{contextProperties['alm.crowd.group']}")
	private String projectgroup;

	@Autowired
	private AlmProjectUserDao userDao;

	private CrowdClient crowdClient;

	public AlmCrowdService() {
		// TODO Auto-generated constructor stub
	}

	@PostConstruct
	private void init() {
		crowdClient = new RestCrowdClientFactory().newInstance(crowdUrl,
				crowdId, crowdPw);
	}

	public Object processCrowdService() {

		return null;
	}

	public GridJsonResponse getList(String type, ExtjsGridParam gridParam) {

		GridJsonResponse response = new GridJsonResponse();

		try {
			if (type.equals("USER")) {
				response.setList(getUserList(gridParam));
			} else {
				response.setList(getGroupList(gridParam));
			}
		} catch (OperationFailedException e) {
			response.setSuccess(false);
			response.setMsg("리스트를 불러오는데 실패했습니다.");
		} catch (InvalidAuthenticationException e) {
			response.setSuccess(false);
			response.setMsg("Crowd 인증에 실패했습니다.");
		} catch (ApplicationPermissionException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : ApplicationPermissionException \n 애플리케이션에서 권한이 없습니다.");
		}

		return response;
	}

	private List<AlmUserDto> getUserList(ExtjsGridParam gridParam)
			throws OperationFailedException, InvalidAuthenticationException,
			ApplicationPermissionException {

		List<AlmUserDto> userLists = new ArrayList<AlmUserDto>();

		int page = getCrowdPage(gridParam);

		Iterable<User> usernames;

		// Search Text가 있을 경우
		if (gridParam.getSearch() != null) {
			//PropertyRestriction<String> restriction = Restriction.on(UserTermKeys.USERNAME).startingWith(gridParam.getSearch());
			//usernames = crowdClient.searchUsers(restriction, page, 50);

			SearchRestriction restriction = new BooleanRestrictionImpl(BooleanRestriction.BooleanLogic.OR, 
					Restriction.on(UserTermKeys.USERNAME).startingWith(gridParam.getSearch()),
					Restriction.on(UserTermKeys.DISPLAY_NAME).startingWith(gridParam.getSearch()));

			usernames = crowdClient.searchUsers(restriction, page, 50);
		} else { // Search Text가 없을경우
			PropertyRestriction<Boolean> restriction = Restriction.on(UserTermKeys.ACTIVE).containing(true);
			usernames = crowdClient.searchUsers(restriction, page, 50);
		}

		for (User profile : usernames) {
			AlmUserDto tmp = new AlmUserDto();
			tmp.setUserId(profile.getName()); // id
			tmp.setDisplayName(profile.getDisplayName());// displayName;
			tmp.setEmailAddress(profile.getEmailAddress()); // email;
			tmp.setFirstName(profile.getFirstName()); // first name
			tmp.setLastName(profile.getLastName()); // last name
			userLists.add(tmp);
		}

		return userLists;

	}

	private List<AlmGroupDto> getGroupList(ExtjsGridParam gridParam)
			throws OperationFailedException, InvalidAuthenticationException,
			ApplicationPermissionException {

		List<AlmGroupDto> groupLists = new ArrayList<AlmGroupDto>();

		int page = getCrowdPage(gridParam);

		Iterable<Group> groupnames;

		// Search Text가 있을 경우
		if (gridParam.getSearch() != null) {

			PropertyRestriction<String> groupRestriction = Restriction.on(
					GroupTermKeys.NAME).startingWith(gridParam.getSearch());
			groupnames = crowdClient.searchGroups(groupRestriction, page, 50);

		} else { // Search Text가 없을경우
			PropertyRestriction<Boolean> groupRestriction = Restriction.on(
					GroupTermKeys.ACTIVE).containing(true);
			groupnames = crowdClient.searchGroups(groupRestriction, page, 50);
		}

		for (Group profile : groupnames) {
			AlmGroupDto tmp = new AlmGroupDto();
			tmp.setName(profile.getName());
			tmp.setDescription(profile.getDescription());
			tmp.setActive(profile.isActive());
			groupLists.add(tmp);
		}

		return groupLists;

	}

	public DtoJsonResponse getUser(String userId) {

		DtoJsonResponse response = new DtoJsonResponse();

		try {
			User user = crowdClient.getUser(userId);
			response.setData(user);
		} catch (OperationFailedException e) {
			response.setSuccess(false);
			response.setMsg("유저 정보 조회에 실패했습니다.");
		} catch (ApplicationPermissionException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : ApplicationPermissionException \n 애플리케이션에서 권한이 없습니다.");
		} catch (InvalidAuthenticationException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : InvalidAuthenticationException \n 애플리케이션에서 인증 에러가 발생했습니다.");
		} catch (UserNotFoundException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : UserNotFoundException \n 사용자를 찾을 수 없습니다.");
		}
		return response;
	}

	// Group Infomation
	public DtoJsonResponse getGroup(String groupId) {

		DtoJsonResponse response = new DtoJsonResponse();

		try {
			Group group = crowdClient.getGroup(groupId);
			response.setData(group);
		} catch (OperationFailedException e) {
			response.setSuccess(false);
			response.setMsg("OperationFailedException");
		} catch (ApplicationPermissionException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : ApplicationPermissionException \n 애플리케이션에서 권한이 없습니다.");
		} catch (InvalidAuthenticationException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : InvalidAuthenticationException \n 애플리케이션에서 인증 에러가 발생했습니다.");
		} catch (GroupNotFoundException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : GroupNotFoundException \n 그룹을 찾을 수 없습니다.");
		}

		return response;
	}

	// 유저 추가
	public DtoJsonResponse addUser(AlmUserAddDto userData) {

		DtoJsonResponse response = new DtoJsonResponse();

		UserEntity myuser = new UserEntity(userData.getName(),
				userData.getFirstName(), userData.getLastName(),
				userData.getDisplayName(), userData.getEmail(),
				new PasswordEntity(userData.getPassword()), true);
		try {
			// SVN 사용자를 위한 유저 정보 등록
			ProjectUserDto userDto = new ProjectUserDto();
			userDto.setPASSWORD(userData.getPassword());
			userDto.setUSERNAME(userData.getName());
			userDao.insertProjectUser(userDto);

			// Confluence에 저장
			crowdClient.addUser(myuser,
					new PasswordCredential(userData.getPassword()));
			response.setMsg("사용자가 추가되었습니다.");
		} catch (InvalidUserException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : UserNotFoundException \n 사용자를 찾을 수 없습니다.");
		} catch (InvalidCredentialException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : InvalidAuthenticationException \n 애플리케이션에서 인증 에러가 발생했습니다.");
		} catch (OperationFailedException e) {
			response.setSuccess(false);
			response.setMsg("OperationFailedException");
		} catch (InvalidAuthenticationException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : InvalidAuthenticationException \n 애플리케이션에서 인증 에러가 발생했습니다.");
		} catch (ApplicationPermissionException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : ApplicationPermissionException \n 애플리케이션에서 권한이 없습니다.");
		}

		return response;
	}

	// 유저 수정
	public DtoJsonResponse modifyUser(AlmUserAddDto userData) {

		DtoJsonResponse response = new DtoJsonResponse();

		UserEntity myuser = new UserEntity(userData.getName(),
				userData.getFirstName(), userData.getLastName(),
				userData.getDisplayName(), userData.getEmail(),
				new PasswordEntity(userData.getPassword()), true);
		try {

			crowdClient.updateUser(myuser);
			response.setMsg("사용자 정보가 수정되었습니다.");
		} catch (InvalidUserException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : UserNotFoundException \n 사용자를 찾을 수 없습니다.");
		} catch (OperationFailedException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : OperationFailedException \n 사용자 정보 수정이 실패했습니다.");
		} catch (InvalidAuthenticationException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : InvalidAuthenticationException \n 애플리케이션에서 인증 에러가 발생했습니다.");
		} catch (ApplicationPermissionException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : ApplicationPermissionException \n 애플리케이션에서 권한이 없습니다.");
		} catch (UserNotFoundException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : OperationFailedException \n 사용자 정보 수정이 실패했습니다.");
		}

		return response;
	}

	// 유저 삭제
	public DtoJsonResponse removeUser(String username) {

		DtoJsonResponse response = new DtoJsonResponse();

		try {
			crowdClient.removeUser(username);
			response.setMsg("사용자가 삭제되었습니다.");
		} catch (InvalidAuthenticationException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : InvalidAuthenticationException \n 애플리케이션에서 인증 에러가 발생했습니다.");
		} catch (UserNotFoundException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : UserNotFoundException \n 사용자를 찾을 수 없습니다.");
		} catch (OperationFailedException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : OperationFailedException \n 유저 삭제가 실패했습니다..");
		} catch (ApplicationPermissionException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : ApplicationPermissionException \n 애플리케이션에서 권한이 없습니다.");
		}

		return response;
	}

	// 유저 비밀번호 변경
	public DtoJsonResponse changePasswordUser(String username, String password) {

		DtoJsonResponse response = new DtoJsonResponse();

		try {
			
			//Crowd에 변경처리
			crowdClient.updateUserCredential(username, password);
			
			// ALM User 에도 변경처리하기
			ProjectUserDto userDto = new ProjectUserDto();
			userDto.setUSERNAME(username);
			userDto.setPASSWORD(password);
			userDao.updateProjectUser(userDto);
			

			response.setMsg("사용자 비밀번호가 변경되었습니다.");
		} catch (OperationFailedException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : OperationFailedException \n 비밀번호 변경에 실패했습니다.");
		} catch (InvalidAuthenticationException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : InvalidAuthenticationException \n 애플리케이션에서 인증 에러가 발생했습니다.");
		} catch (ApplicationPermissionException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : ApplicationPermissionException \n 애플리케이션에서 권한이 없습니다.");
		} catch (UserNotFoundException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : UserNotFoundException \n 사용자를 찾을 수 없습니다.");
		} catch (InvalidCredentialException e) {
			logger.info("Crowd Excpetion" + e.getMessage());

		}

		return response;

	}

	// 그룹 생성
	public DtoJsonResponse addGroup(AlmGroupDto groupData) {

		DtoJsonResponse response = new DtoJsonResponse();

		GroupEntity mygroup = new GroupEntity(groupData.getName(),
				groupData.getDescription(), GroupType.GROUP,
				groupData.isActive());

		try {
			crowdClient.addGroup(mygroup);
			response.setMsg("그룹이 생성되었습니다");
		} catch (OperationFailedException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : OperationFailedException \n 그룹이 생성이 실패했습니다.");
		} catch (InvalidAuthenticationException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : InvalidAuthenticationException \n 애플리케이션에서 인증 에러가 발생했습니다.");
		} catch (ApplicationPermissionException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : ApplicationPermissionException \n 애플리케이션에서 권한이 없습니다.");
		} catch (InvalidGroupException e) {
			response.setSuccess(false);
			response.setMsg("InvalidGroupException");
		}

		try {

			crowdClient.addGroupToGroup(groupData.getName(), projectgroup);

		} catch (GroupNotFoundException | UserNotFoundException
				| MembershipAlreadyExistsException | OperationFailedException
				| InvalidAuthenticationException
				| ApplicationPermissionException e) {
			// TODO Auto-generated catch block
			response.setSuccess(false);
			response.setMsg("그룹에 추가하는데 문제가 발생했습니다.");
		}

		return response;
	}

	// 그룹삭제
	public DtoJsonResponse removeGroup(String groupname) {

		DtoJsonResponse response = new DtoJsonResponse();

		try {
			crowdClient.removeGroup(groupname);
			response.setMsg("그룹을 삭제하였습니다.");
		} catch (GroupNotFoundException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : GroupNotFoundException \n 그룹을 찾을 수 없습니다.");
		} catch (OperationFailedException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : OperationFailedException \n 그룹 삭제중 에러가 발생했습니다.");
		} catch (ApplicationPermissionException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : ApplicationPermissionException \n 애플리케이션에서 권한이 없습니다.");
		} catch (InvalidAuthenticationException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : InvalidAuthenticationException \n 애플리케이션에서 인증 에러가 발생했습니다.");
		}

		return response;
	}

	// 그룹에 유저정보
	public GridJsonResponse getGroupUser(String groupname) {

		GridJsonResponse response = new GridJsonResponse();

		try {
			response.setList(getGroupUsers(groupname));
			response.setMsg("사용자 그룹이 조회되었습니다.");
		} catch (GroupNotFoundException | ApplicationPermissionException
				| InvalidAuthenticationException | OperationFailedException e) {
			// TODO Auto-generated catch block
			response.setSuccess(false);
			response.setMsg("사용자 그룹이 조회에 실패했습니다.");
		}

		return response;
	}

	// User의 Gorup정보
	public GridJsonResponse getUserGroup(String username) {

		GridJsonResponse response = new GridJsonResponse();

		try {
			response.setList(getGroupsForUser(username));
			response.setMsg("사용자 그룹이 조회되었습니다.");
		} catch (UserNotFoundException | OperationFailedException
				| InvalidAuthenticationException
				| ApplicationPermissionException e1) {
			response.setSuccess(false);
			response.setMsg("사용자 그룹이 조회에 실패했습니다.");
		}

		return response;
	}

	public List<User> getGroupUsers(String groupname)
			throws GroupNotFoundException, ApplicationPermissionException,
			InvalidAuthenticationException, OperationFailedException {

		List<User> users = crowdClient.getUsersOfGroup(groupname, 0, 10000);
		return users;
	}

	public List<Group> getGroupsForUser(String username)
			throws UserNotFoundException, OperationFailedException,
			InvalidAuthenticationException, ApplicationPermissionException {
		List<Group> groups = crowdClient.getGroupsForUser(username, 0, 10000);
		return groups;
	}

	// 그룹에 유저 추가
	public DtoJsonResponse addUserToGroup(String username, String groupname) {

		DtoJsonResponse response = new DtoJsonResponse();

		try {

			String[] users = username.split(",");
			for (int i = 0; i < users.length; i++) {
				crowdClient.addUserToGroup(users[i], groupname);
			}
			response.setMsg("사용자가 그룹에 등록되었습니다.");
		} catch (GroupNotFoundException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : GroupNotFoundException \n 그룹을 찾을 수 없습니다.");
		} catch (UserNotFoundException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : UserNotFoundException \n 사용자를 찾을 수 없습니다.");
		} catch (MembershipAlreadyExistsException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : MembershipAlreadyExistsException \n 이미 그룹에 추가된 유저입니다.");
		} catch (OperationFailedException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : OperationFailedException \n 사용자를 그룹에 등록하는데 실패했습니다.");
		} catch (InvalidAuthenticationException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : InvalidAuthenticationException \n 애플리케이션에서 인증 에러가 발생했습니다.");
		} catch (ApplicationPermissionException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : ApplicationPermissionException \n 애플리케이션에서 권한이 없습니다.");
		}

		return response;
	}

	// 그룹에 유저삭제
	public DtoJsonResponse removeUserFromGroup(String username, String groupname) {

		DtoJsonResponse response = new DtoJsonResponse();

		try {
			crowdClient.removeUserFromGroup(username, groupname);
			response.setMsg("사용자가 그룹에서 삭제되었습니다");
		} catch (GroupNotFoundException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : GroupNotFoundException \n 그룹을 찾을 수 없습니다.");
		} catch (MembershipNotFoundException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : MembershipNotFoundException \n 그룹에서 사용자를 찾을 수 없습니다.");
		} catch (UserNotFoundException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : UserNotFoundException \n 사용자를 찾을 수 없습니다.");
		} catch (ApplicationPermissionException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : ApplicationPermissionException \n 애플리케이션에서 권한이 없습니다.");
		} catch (InvalidAuthenticationException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : InvalidAuthenticationException \n 애플리케이션에서 인증 에러가 발생했습니다.");
		} catch (OperationFailedException e) {
			response.setSuccess(false);
			response.setMsg("ErrorCode : OperationFailedException \n 작업이 실패했습니다.");
		}

		return response;
	}

	public List<User> getGroupUserList(String groupname) {

		List<User> users = null;
		try {
			users = crowdClient.getUsersOfGroup(groupname, 0, 10000);
		} catch (GroupNotFoundException | ApplicationPermissionException
				| InvalidAuthenticationException | OperationFailedException e) {
			logger.info("Crowd Excpetion" + e.getMessage());
		}
		return users;

	}

	public List<Group> getNestedChildGroupsOfGroup() {

		try {
			List<Group> groups = crowdClient.getNestedChildGroupsOfGroup(
					projectgroup, 0, 100000);

			return groups;
		} catch (GroupNotFoundException | OperationFailedException
				| InvalidAuthenticationException
				| ApplicationPermissionException e) {
			logger.info("Crowd Excpetion" + e.getMessage());
		}
		return null;

	}

	public List<ProjectUserDto> getProjectAllUser() {

		return userDao.getUserList();

	}

	private int getCrowdPage(ExtjsGridParam gridParam) {

		// paging 처리
		int page = gridParam.getPage();
		if (page >= 1) {
			page = page - 1;
		}
		page = page * 50;
		return page;
	}
}
// end of UserService.java
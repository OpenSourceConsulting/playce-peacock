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
package com.athena.peacock.controller.web.alm.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.athena.peacock.controller.web.alm.user.dto.AlmGroupDto;
import com.athena.peacock.controller.web.alm.user.dto.AlmUserAddDto;
import com.athena.peacock.controller.web.alm.user.dto.AlmUserDto;
import com.athena.peacock.controller.web.common.model.DtoJsonResponse;
import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.athena.peacock.controller.web.common.model.GridJsonResponse;
import com.atlassian.crowd.embedded.api.PasswordCredential;
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
public class AlmUserService {

	private CrowdClient crowdClient;

	public AlmUserService() {
		// TODO Auto-generated constructor stub

		// 추후 변수처리
		crowdClient = new RestCrowdClientFactory().newInstance(
				"http://119.81.162.218:8095/crowd", "demo", "demo");
	}

	public void insertUser(AlmUserDto user) {

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
			response.setMsg("OperationFailedException");
		} catch (InvalidAuthenticationException e) {
			response.setSuccess(false);
			response.setMsg("InvalidAuthenticationException");
		} catch (ApplicationPermissionException e) {
			response.setSuccess(false);
			response.setMsg("InvalidAuthenticationException");
		}

		return response;
	}

	private List<AlmUserDto> getUserList(ExtjsGridParam gridParam)
			throws OperationFailedException, InvalidAuthenticationException,
			ApplicationPermissionException {

		List<AlmUserDto> userLists = new ArrayList<AlmUserDto>();

		// UserList
		PropertyRestriction<Boolean> restriction = Restriction.on(
				UserTermKeys.ACTIVE).containing(true);

		Iterable<User> usernames = crowdClient.searchUsers(restriction, 0, 10);

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

		PropertyRestriction<Boolean> groupRestriction = Restriction.on(
				GroupTermKeys.ACTIVE).containing(true);

		Iterable<Group> groupnames = crowdClient.searchGroups(groupRestriction,
				0, 10);

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
			response.setMsg("OperationFailedException");
		} catch (ApplicationPermissionException e) {
			response.setSuccess(false);
			response.setMsg("ApplicationPermissionException");
		} catch (InvalidAuthenticationException e) {
			response.setSuccess(false);
			response.setMsg("InvalidAuthenticationException");
		} catch (UserNotFoundException e) {
			response.setSuccess(false);
			response.setMsg("UserNotFoundException");
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
			response.setMsg("ApplicationPermissionException");
		} catch (InvalidAuthenticationException e) {
			response.setSuccess(false);
			response.setMsg("InvalidAuthenticationException");
		} catch (GroupNotFoundException e) {
			response.setSuccess(false);
			response.setMsg("GroupNotFoundException");
		} 
		
		return response;
	}

	public DtoJsonResponse addUser(AlmUserAddDto userData) {

		DtoJsonResponse response = new DtoJsonResponse();

		UserEntity myuser = new UserEntity(userData.getName(), userData.getFirstName(), userData.getLastName(),
				userData.getDisplayName(), userData.getEmail(), new PasswordEntity(userData.getPassword()),
				true);
		try {
			crowdClient.addUser(myuser, new PasswordCredential(userData.getPassword()));
			response.setMsg("사용자 생성 성공");
		} catch (InvalidUserException e) {
			response.setSuccess(false);
			response.setMsg("InvalidUserException");
		} catch (InvalidCredentialException e) {
			response.setSuccess(false);
			response.setMsg("InvalidCredentialException");
		} catch (OperationFailedException e) {
			response.setSuccess(false);
			response.setMsg("OperationFailedException");
		} catch (InvalidAuthenticationException e) {
			response.setSuccess(false);
			response.setMsg("InvalidAuthenticationException");
		} catch (ApplicationPermissionException e) {
			response.setSuccess(false);
			response.setMsg("ApplicationPermissionException");
		}

		return response;
	}
	
	public DtoJsonResponse addGroup(AlmUserAddDto userData) {

		DtoJsonResponse response = new DtoJsonResponse();

		GroupEntity mygroup = new GroupEntity("name", "description", GroupType.GROUP, true);
		
		try {
			crowdClient.addGroup(mygroup);
			response.setMsg("그룹 생성 성공");
		} catch (OperationFailedException e) {
			response.setSuccess(false);
			response.setMsg("OperationFailedException");
		} catch (InvalidAuthenticationException e) {
			response.setSuccess(false);
			response.setMsg("InvalidAuthenticationException");
		} catch (ApplicationPermissionException e) {
			response.setSuccess(false);
			response.setMsg("ApplicationPermissionException");
		} catch (InvalidGroupException e) {
			response.setSuccess(false);
			response.setMsg("InvalidGroupException");
		}

		return response;
	}
	
	public DtoJsonResponse addUserToGroup(AlmUserAddDto userData) {

		DtoJsonResponse response = new DtoJsonResponse();

		try {
			crowdClient.addUserToGroup("username", "groupname");
			response.setMsg("유저 추가 성공");
		} catch (GroupNotFoundException e) {
			response.setSuccess(false);
			response.setMsg("GroupNotFoundException");
		} catch (UserNotFoundException e) {
			response.setSuccess(false);
			response.setMsg("UserNotFoundException");
		} catch (MembershipAlreadyExistsException e) {
			response.setSuccess(false);
			response.setMsg("MembershipAlreadyExistsException");
		} catch (OperationFailedException e) {
			response.setSuccess(false);
			response.setMsg("OperationFailedException");
		} catch (InvalidAuthenticationException e) {
			response.setSuccess(false);
			response.setMsg("InvalidAuthenticationException");
		} catch (ApplicationPermissionException e) {
			response.setSuccess(false);
			response.setMsg("ApplicationPermissionException");
		}

		return response;
	}
	
	public DtoJsonResponse removeUserFromGroup(AlmUserAddDto userData) {

		DtoJsonResponse response = new DtoJsonResponse();

		try {
			crowdClient.removeUserFromGroup("username", "groupname");
			response.setMsg("유저 삭제 성공");
		} catch (GroupNotFoundException e) {
			response.setSuccess(false);
			response.setMsg("GroupNotFoundException");
		} catch (MembershipNotFoundException e) {
			response.setSuccess(false);
			response.setMsg("MembershipNotFoundException");
		} catch (UserNotFoundException e) {
			response.setSuccess(false);
			response.setMsg("UserNotFoundException");
		} catch (ApplicationPermissionException e) {
			response.setSuccess(false);
			response.setMsg("ApplicationPermissionException");
		} catch (InvalidAuthenticationException e) {
			response.setSuccess(false);
			response.setMsg("InvalidAuthenticationException");
		} catch (OperationFailedException e) {
			response.setSuccess(false);
			response.setMsg("OperationFailedException");
		}

		return response;
	}

}
// end of UserService.java
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

import com.athena.peacock.controller.web.common.model.ExtjsGridParam;
import com.atlassian.crowd.exception.ApplicationPermissionException;
import com.atlassian.crowd.exception.InvalidAuthenticationException;
import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.integration.rest.service.factory.RestCrowdClientFactory;
import com.atlassian.crowd.model.user.User;
import com.atlassian.crowd.search.builder.Restriction;
import com.atlassian.crowd.search.query.entity.restriction.PropertyRestriction;
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

	public List<AlmUserDto> getUserList(ExtjsGridParam gridParam) {

		List<AlmUserDto> userLists = new ArrayList<AlmUserDto>();

		try {

			// UserList
			PropertyRestriction<Boolean> restriction = Restriction.on(
					UserTermKeys.ACTIVE).containing(true);

			Iterable<User> usernames = crowdClient.searchUsers(restriction, 0,
					10);

			for (User profile : usernames) {
				AlmUserDto tmp = new AlmUserDto();
				tmp.setUserId(profile.getName()); // id
				tmp.setDisplayName(profile.getDisplayName());// displayName;
				tmp.setEmailAddress(profile.getEmailAddress()); //email;
				tmp.setFirstName(profile.getFirstName()); //first name
				tmp.setLastName(profile.getLastName()); //last name
				userLists.add(tmp);
			}

		} catch (OperationFailedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationPermissionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAuthenticationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return userLists;

	}

}
// end of UserService.java
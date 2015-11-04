package com.athena.peacock.svn.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.athena.peacock.svn.dto.SvnDto;
import com.athena.peacock.svn.dto.SvnGroupUserDto;
import com.athena.peacock.svn.dto.SvnProjectDto;
import com.athena.peacock.svn.dto.SvnUserDto;

@Service
public class SvnService {

	public void sync() {
		SvnDto syncDto = getArchetype();
		writePasswdFile(syncDto);
		writeAuthzFile(syncDto);
	}

	public SvnDto getArchetype() {

		try {
			String url = "http://prov.hiway.hhi.co.kr/alm/project/svn/sync";

			HttpHeaders requestHeaders = new HttpHeaders();

			List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
			requestHeaders.setAccept(acceptableMediaTypes);

			// Populate the headers in an HttpEntity object to use for the
			// request
			HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

			// Create a new RestTemplate instance
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(
					new MappingJackson2HttpMessageConverter());

			// Perform the HTTP GET request
			ResponseEntity<SvnDto> responseEntity = restTemplate.exchange(url,
					HttpMethod.GET, requestEntity, SvnDto.class);

			return responseEntity.getBody();
		} catch (Exception e) {

		}

		return null;
	}

	private void writePasswdFile(SvnDto syncDto) {

		List<SvnUserDto> users = syncDto.getUsers();

		try {
			// //////////////////////////////////////////////////////////////
			BufferedWriter out = new BufferedWriter(new FileWriter("/home/svn/conf/passwd.peacock"));
			String header = "[users]";
			out.write(header);
			out.newLine();
			
			out.write("peacock = peacock");
			out.newLine();
			out.write("admin = manage");
			out.newLine();
			out.write("jenkins = hhi9200");
			out.newLine();

			for (SvnUserDto user : users) {
				String tmp = String.format("%s = %s", user.getUsername(),
						user.getPassword());
				out.write(tmp);
				out.newLine();
			}

			out.write("###SUCESS###");			
			out.close();
		} catch (IOException e) {
		}

	}

	private void writeAuthzFile(SvnDto syncDto) {

		List<SvnGroupUserDto> groupList = syncDto.getGroups();

		List<SvnProjectDto> svnlists = syncDto.getProjects();

		try {
			// //////////////////////////////////////////////////////////////
			BufferedWriter out = new BufferedWriter(new FileWriter("/home/svn/conf/authz.peacock"));

			String noti = "#DO not modify manually";
			out.write(noti);
			out.newLine();
			
			
			// aliases
			String header = "[aliases]";
			out.write(header);
			out.newLine();
			out.newLine();

			// group
			String groups = "[groups]";
			out.write(groups);
			out.newLine();

			for (SvnGroupUserDto groupDto : groupList) {
				
				String userList = groupDto.getUserList();
				userList = userList.toUpperCase();
				String tmp = String.format("%s = %s", groupDto.getGroupName(),
						userList);
				out.write(tmp);
				out.newLine();
			}

			out.newLine();

			// project
			String project = "[/]";
			out.write(project);
			out.newLine();

			out.write("peacock =  rw");
			out.newLine();
			
			out.write("admin =  rw");
			out.newLine();

			out.write("jenkins =  rw");
			out.newLine();

			boolean hiwaySampleExists = false;
			for (SvnProjectDto svnProject : svnlists) {

				String tmp = String
						.format("[%s:/%s]", svnProject.getRepository(),
								svnProject.getProjectCode());
				out.write(tmp);
				out.newLine();
				
				if (svnProject.getRepository().equals("hiway") && svnProject.getProjectCode().equals("sample")) {
					hiwaySampleExists = true;
				}

				String auth = String.format("@%s = rw",
						svnProject.getProjectCode());
				out.write(auth);
				out.newLine();
				
				if (hiwaySampleExists) {
					out.write("* = r");
					out.newLine();
				}
			}
			
			if (!hiwaySampleExists) {
				out.write("[hiway:/sample]");
				out.newLine();
				
				out.write("* = r");
				out.newLine();
			}

			out.write("###SUCESS###");
			out.close();
		} catch (IOException e) {

		}

	}
}

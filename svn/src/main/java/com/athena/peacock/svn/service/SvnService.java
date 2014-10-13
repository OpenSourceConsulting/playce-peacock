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
			// logger.debug("exception {}", e.getMessage());
		}

		return null;
	}

	private void writePasswdFile(SvnDto syncDto) {

		List<SvnUserDto> users = syncDto.getUsers();

		try {
			// //////////////////////////////////////////////////////////////
			BufferedWriter out = new BufferedWriter(new FileWriter("out.txt"));
			String header = "[users]";
			out.write(header);
			out.newLine();
			
			String peacock  = "peacock = peacock";
			out.write(peacock);
			out.newLine();
			
			String admin  = "admin = hhi9200";
			out.write(admin);
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
			BufferedWriter out = new BufferedWriter(new FileWriter("authz.txt"));

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
				String tmp = String.format("%s = %s", groupDto.getGroupName(),
						groupDto.getUserList());
				out.write(tmp);
				out.newLine();
			}

			out.newLine();

			// project
			String project = "[/]";
			out.write(project);
			out.newLine();

			String projectMain = "peacock =  rw";
			out.write(projectMain);
			out.newLine();
			out.newLine();

			for (SvnProjectDto svnProject : svnlists) {

				String tmp = String
						.format("[%s:/%s]", svnProject.getRepository(),
								svnProject.getProjectCode());
				out.write(tmp);
				out.newLine();

				String auth = String.format("@%s = rw",
						svnProject.getProjectCode());
				out.write(auth);
				out.newLine();
				out.newLine();
			}

			out.write("###SUCESS###");
			out.close();
		} catch (IOException e) {

		}

	}
}

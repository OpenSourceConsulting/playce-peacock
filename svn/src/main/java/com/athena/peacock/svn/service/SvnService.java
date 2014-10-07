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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.athena.peacock.svn.dto.SvnGroupUserDto;
import com.athena.peacock.svn.dto.SvnProject;
import com.athena.peacock.svn.dto.SvnUserDto;

@Service
public class SvnService {

	public void test() {
		System.out.println("##111##########");
		getArchetype();
		writePasswdFile();
		writeAuthzFile();
	}

	public void getArchetype() {

		try {
			String url = "http://localhost:8080/controller/alm/jenkins/job";

			HttpHeaders requestHeaders = new HttpHeaders();

			List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
			acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
			requestHeaders.setAccept(acceptableMediaTypes);
			requestHeaders.setContentType(MediaType.APPLICATION_JSON);

			// Populate the headers in an HttpEntity object to use for the
			// request
			HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

			// Create a new RestTemplate instance
			RestTemplate restTemplate = new RestTemplate();
			// restTemplate.getMessageConverters().add(new
			// Jaxb2RootElementHttpMessageConverter());

			// Perform the HTTP GET request
			ResponseEntity<String> responseEntity = restTemplate.exchange(url,
					HttpMethod.GET, requestEntity, String.class);

			System.out.println(responseEntity);
			// return ArchetypeCatalog.class.cast(responseEntity.getBody());
		} catch (Exception e) {
			// logger.debug("exception {}", e.getMessage());
			e.printStackTrace();
		}

		// return null;
	}

	private void writePasswdFile() {

		List<SvnUserDto> users = new ArrayList<SvnUserDto>();

		SvnUserDto u1 = new SvnUserDto();
		u1.setPassword("osc09");
		u1.setUsername("osc09");

		SvnUserDto u2 = new SvnUserDto();
		u2.setPassword("osc08");
		u2.setUsername("osc08");

		users.add(u1);
		users.add(u2);

		try {
			// //////////////////////////////////////////////////////////////
			BufferedWriter out = new BufferedWriter(new FileWriter("out.txt"));
			String header = "[users]";
			out.write(header);
			out.newLine();

			for (SvnUserDto user : users) {
				String tmp = String.format("%s = %s", user.getUsername(),
						user.getPassword());
				out.write(tmp);
				out.newLine();
			}

			out.close();
		} catch (IOException e) {
			System.err.println(e); // 에러가 있다면 메시지 출력
		}

	}

	private void writeAuthzFile() {

		List<SvnGroupUserDto> groupList = new ArrayList<SvnGroupUserDto>();
		SvnGroupUserDto group = new SvnGroupUserDto();
		group.setGroupName("xt001");
		group.setUserList("osc09,osc08");

		groupList.add(group);

		List<SvnProject> svnlists = new ArrayList<SvnProject>();
		SvnProject svn = new SvnProject();
		svn.setProject("hiway");
		svn.setRepository("hiway");
		svn.setProjectCode("xt001");
		svnlists.add(svn);

		try {
			// //////////////////////////////////////////////////////////////
			BufferedWriter out = new BufferedWriter(new FileWriter("authz.txt"));

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

			String projectMain = "osc09 =  rw";
			out.write(projectMain);
			out.newLine();
			out.newLine();
			
			for (SvnProject svnProject : svnlists) {

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

			out.close();
		} catch (IOException e) {
			System.err.println(e); // 에러가 있다면 메시지 출력
		}

	}
}

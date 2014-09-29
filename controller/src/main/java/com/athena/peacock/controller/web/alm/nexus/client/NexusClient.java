package com.athena.peacock.controller.web.alm.nexus.client;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.athena.peacock.controller.web.alm.nexus.client.model.ArchetypeCatalog;

@Component
public class NexusClient {

	@Value("#{contextProperties['alm.jenkins.url']}")
	private String JENKINS_URL;

	static RestTemplate restTemplate = new RestTemplate();
	

	
	/*public static void main(String[] args) throws Exception {
		
		String url = "http://119.81.150.69:8081/nexus/content/groups/public/archetype-catalog.xml";

		HttpHeaders requestHeaders = new HttpHeaders();

		List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
		acceptableMediaTypes.add(MediaType.APPLICATION_XML);
		requestHeaders.setAccept(acceptableMediaTypes);
		requestHeaders.setContentType(MediaType.TEXT_XML);

		// Populate the headers in an HttpEntity object to use for the
		// request
		HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

		// Create a new RestTemplate instance
		RestTemplate restTemplate = new RestTemplate();
		//restTemplate.getMessageConverters().add(new Jaxb2RootElementHttpMessageConverter());

		// Perform the HTTP GET request
		ResponseEntity<ArchetypeCatalog> responseEntity = restTemplate
				.exchange(url, HttpMethod.GET, requestEntity,
						ArchetypeCatalog.class);
		
		
		
		ArchetypeCatalog.class.cast(responseEntity.getBody());
		
		
		
		
		JAXBContext context = JAXBContext.newInstance(ArchetypeCatalog.class);
		Marshaller marshaller = context.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

		StringWriter writer = new StringWriter();
		
		QName qName = new QName("com.redhat.rhevm.api.model", "archetype-catalog");
	    JAXBElement<ArchetypeCatalog> root = new JAXBElement<ArchetypeCatalog>(qName, ArchetypeCatalog.class, ArchetypeCatalog.class.cast(responseEntity.getBody()));
		
	    //*
	    marshaller.marshal(root, writer);
	    /
		marshaller.marshal(nics, writer);
		//
	    
		System.out.println(writer.toString());

	}*/

	public ArchetypeCatalog getJobs() {

		try {
			String url = "http://119.81.150.69:8081/nexus/content/groups/public/archetype-catalog.xml";

			HttpHeaders requestHeaders = new HttpHeaders();

			List<MediaType> acceptableMediaTypes = new ArrayList<MediaType>();
			acceptableMediaTypes.add(MediaType.APPLICATION_XML);
			requestHeaders.setAccept(acceptableMediaTypes);
			requestHeaders.setContentType(MediaType.TEXT_XML);

			// Populate the headers in an HttpEntity object to use for the
			// request
			HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

			// Create a new RestTemplate instance
			RestTemplate restTemplate = new RestTemplate();
			//restTemplate.getMessageConverters().add(new Jaxb2RootElementHttpMessageConverter());

			// Perform the HTTP GET request
			ResponseEntity<ArchetypeCatalog> responseEntity = restTemplate
					.exchange(url, HttpMethod.GET, requestEntity,
							ArchetypeCatalog.class);

			return ArchetypeCatalog.class.cast(responseEntity.getBody());
		} catch (Exception e) {
			// logger.debug("exception {}", e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

}

/* 
 * Copyright 2013 The Athena-Peacock Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Sang-cheon Park	2014. 1. 2.		First Draft.
 */
package com.athena.peacock.installer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.ibatis.jdbc.ScriptRunner;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class Installer {

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		/** 
		 * ../server/webapps/ROOT/WEB-INF/classes/context.properties 파일이 존재하는지 확인하고
		 * 파일이 없을 경우 context.properties 파일의 경로를 입력받는다.
		 */
		String path =  Installer.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll("/installer-0.0.1-SNAPSHOT.jar", "");
		File file = null;
		
		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}
		
		path = path.substring(0, path.lastIndexOf("/")) + "/server/webapps/ROOT/WEB-INF/classes/context.properties";
		
		file = new File(path);
		while (!file.exists()) {
			//System.out.println(path + " 파일이 존재하지 않습니다.");
			
			path = getValue("context.properties 파일의 전체 경로를 입력하세요.", null);
			file = new File(path);
		}
		
		/**
		 * Datasource, RHEV-M, Netty 관련 설정을 사용자로부터 입력받는다.
		 */
		Map<String, String[]> properties = getPorperties(file);
		
		List<String> keys = new ArrayList<String>(properties.keySet());
		String value = null;
		for (String key : keys) {
			value = properties.get(key)[0];
			
			if (value.startsWith("${")) {
				value = null;
			}
			
			if (properties.get(key).length == 2 && properties.get(key)[1] != null) {
				//System.out.println("\n" + properties.get(key)[1]);
			}
			properties.put(key, new String[]{getValue(key + "을(를) 입력하세요.", value)});
		}
		
		/**
		 * 입력받은 내용을 바탕으로 context.properties 파일을 작성한다.
		 */
		saveProperties(properties, file);
		
		value = getValue("DB 초기화 작업을 수행하시겠습니까? [Y/N]", "Y");
    	
    	if (value.toUpperCase().equals("Y")) {
    		String driver = getValue("JDBC Driver를 입력하세요.", "com.mysql.jdbc.Driver");
    		String host = getValue("DB Server Host IP를 입력하세요.", "localhost");
    		String port = getValue("DB Server Listen Port를 입력하세요.", "3306");
    		String schema = getValue("DB Schema 명을 입력하세요.", "peacock");
    		String username = getValue("Grant 권한이 있는 사용자를 입력하세요.", null);
    		String password = getValue("비밀번호를 입력하세요.", "");
    		
    		String url = "jdbc:mysql://" + host + ":" + port + "?useUnicode=true&characterEncoding=UTF-8";
    		
    		initData(driver, url, schema, username, password, properties.get("username")[0], properties.get("password")[0]);
    	}
    	
    	//System.out.println("\n\n+:+:+:+: Athena-Peacock Server configuration is successfully done. +:+:+:+:");
	}//end of main()
	
	/**
	 * <pre>
	 * 표준입력으로부터 값을 입력받는다.
	 * </pre>
	 * @param msg
	 * @param defaults
	 * @return
	 * @throws Exception
	 */
	public static String getValue(String msg, String defaults) throws Exception {
		String value = null;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        
        while (true) {
        	//System.out.print(msg);
        	
        	if (defaults != null && !defaults.equals("")) {
        		//System.out.print("(기본값 : " + defaults + ")");
        	}
        	
        	//System.out.print(" => ");
        	
        	value = br.readLine();
        	
        	if (value.equals("")) {
        		if (defaults != null && !defaults.equals("")) {
        			value = defaults;
        			break;
        		} else {
        			continue;
        		}
            } else {
            	break;
            }
        }
        
        return value;
	}//end of getValue()
	
	/**
	 * <pre>
	 * context.properties 파일로부터 key, value 값을 메모리에 저장한다.
	 * </pre>
	 * @param file
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String[]> getPorperties(File file) throws Exception {
		Map<String, String[]> properties = new LinkedHashMap<String, String[]>();

		BufferedReader br = new BufferedReader(new FileReader(file));

		String comment = null;
		String data = null;
		while ((data = br.readLine()) != null) {
			data = data.trim();
			
			if ("".equals(data)) {
				comment = null;
				continue;
			}
			
			if (data.startsWith("#")) {
				comment = data;
				continue;
			}
			
			if (data.indexOf("=") > 0) {
				properties.put(data.substring(0, data.indexOf("=")), new String[]{data.substring(data.indexOf("=") + 1), comment});
				comment = null;
			}
		}
		
		if (br != null) {
			br.close();
		}

		return properties;
	}//end of getPorperties()
	
	/**
	 * <pre>
	 * 수정된 key, value 값을 context.properties 파일에 저장한다.
	 * </pre>
	 * @param properties
	 * @param file
	 * @throws Exception
	 */
	public static void saveProperties(Map<String, String[]> properties, File file) throws Exception {
		String[] contents = readFile(file).split("\n");
		BufferedWriter bw = new BufferedWriter(new FileWriter(file));

		for (String data : contents) {
			data = data.trim();
			
			if (data.indexOf("=") > 0 && !data.startsWith("#")) {
				data = data.substring(0, data.indexOf("="));
				data = data + "=" + properties.get(data)[0];
			}
			
			bw.write(data);
			bw.newLine();
		}
		
		if (bw != null) {
			bw.close();
		}
	}//end of saveProperties()
	
	/**
	 * <pre>
	 * 파일 내용을 String 형태로 읽는다.
	 * </pre>
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static String readFile(File path) throws Exception {
		Charset encoding = StandardCharsets.UTF_8;
		byte[] encoded = Files.readAllBytes(Paths.get(path.toURI()));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}//end of readFile()
	
	/**
	 * <pre>
	 * DataBase를 초기화 한다.
	 * </pre>
	 * @param driver
	 * @param url
	 * @param schema
	 * @param masterUser
	 * @param masterPass
	 * @param peacockUser
	 * @param peacockPass
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public static void initData(String driver, String url, String schema, String masterUser, String masterPass, String peacockUser, String peacockPass) throws Exception {
		InputStream is = Installer.class.getResourceAsStream("/db_init.sql");
		
		Scanner s = new Scanner(is, "UTF-8").useDelimiter("\\A");
	    String contents = s.hasNext() ? s.next() : "";
	    
	    contents = contents.replaceAll("\\$\\{db_schema\\}", schema);
	    contents = contents.replaceAll("\\$\\{username\\}", peacockUser);
	    contents = contents.replaceAll("\\$\\{password\\}", peacockPass);
		
	    Class.forName(driver);
	    new ScriptRunner(DriverManager.getConnection(url, masterUser, masterPass)).runScript(new StringReader(contents));
	}//end of initData()
}
//end of Installer.java
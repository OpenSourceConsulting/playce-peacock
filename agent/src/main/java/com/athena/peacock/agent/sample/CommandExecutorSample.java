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
 * Sang-cheon Park	2013. 8. 14.		First Draft.
 */
package com.athena.peacock.agent.sample;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.CommandLineUtils.StringStreamConsumer;
import org.codehaus.plexus.util.cli.Commandline;

import com.athena.peacock.agent.util.OSUtil;
import com.athena.peacock.agent.util.OSUtil.OSType;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class CommandExecutorSample {

	/**
	 * <pre>
	 * 
	 * </pre>
	 * @param args
	 * @throws CommandLineException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws CommandLineException, IOException {
		
		// Windows wmic usage
		// http://blog.naver.com/PostView.nhn?blogId=diadld2&logNo=30157625015
		// http://www.petenetlive.com/KB/Article/0000619.htm
		
		OSType osType = OSUtil.getOSName();
		
		File executable = null;
		Commandline commandLine = null;
		
		if (osType.equals(OSType.WINDOWS)) {
			executable = new File("C:\\Windows\\System32\\wbem\\WMIC.exe");
			commandLine = new Commandline();
			commandLine.setExecutable(executable.getAbsolutePath());
			
			//commandLine.setExecutable("wmic");  // available only that command is in path

			/** change working directory if necessary */
			commandLine.setWorkingDirectory("/");
			
			/** invoke createArg() and setValue() one by one for each arguments */
			commandLine.createArg().setValue("product");
			commandLine.createArg().setValue("get");
			commandLine.createArg().setValue("name,vendor,version");
			
			/** invoke createArg() and setLine() for entire arguments */
			//commandLine.createArg().setLine("product get name,vendor,version");
			
			/** verify command string */
			System.out.println("C:\\> " + commandLine.toString() + "\n");
		} else {
			executable = new File("/bin/cat");
			commandLine = new Commandline();
			commandLine.setExecutable(executable.getAbsolutePath());
			
			/** change working directory if necessary */
			commandLine.setWorkingDirectory("/");
			
			/** invoke createArg() and setValue() one by one for each arguments */
			commandLine.createArg().setValue("-n");
			commandLine.createArg().setValue("/etc/hosts");
			
			/** invoke createArg() and setLine() for entire arguments */
			//commandLine.createArg().setLine("-n /etc/hosts");
			
			/** verify command string */
			System.out.println("~]$ " + commandLine.toString() + "\n");
		}
		
		/** also enable StringWriter, PrintWriter, WriterStreamConsumer and etc. */
		StringStreamConsumer consumer = new CommandLineUtils.StringStreamConsumer();

		int returnCode = CommandLineUtils.executeCommandLine(commandLine, consumer, consumer, Integer.MAX_VALUE);
		
		if (returnCode == 0) {
			// success
			System.out.println("==============[SUCCEED]==============");
            System.out.println(consumer.getOutput());
            
            if (osType.equals(OSType.WINDOWS)) {
	            List<Product> productList = parse(consumer.getOutput());
	            for (Product product : productList) {
	                System.out.println(product);
	            }
	            
	            int UTF_8 = 0x01;
	            int EUC_KR = 0x02;
	            int KSC5601 = 0x04;
	            int MS949 = 0x08;
	            int ISO8859_1 = 0x10;
	            
	            int mode = 0x00;
	            //mode ^= UTF_8;
	            //mode ^= EUC_KR;
	            //mode ^= KSC5601;
	            //mode ^= MS949;
	            //mode ^= ISO8859_1;
	            
	            if ((mode & UTF_8) == UTF_8) {
	                System.out.println("+:+:+:+: UTF-8 +:+:+:+:");
	                System.out.println(new String(consumer.getOutput().getBytes(), "UTF-8"));
	                System.out.println("+:+:+:+: EUC-KR => UTF-8 +:+:+:+:");
	                System.out.println(new String(consumer.getOutput().getBytes("EUC-KR"), "UTF-8"));
	                System.out.println("+:+:+:+: KSC5601 => UTF-8 +:+:+:+:");
	                System.out.println(new String(consumer.getOutput().getBytes("KSC5601"), "UTF-8"));
	                System.out.println("+:+:+:+: MS949 => UTF-8 +:+:+:+:");
	                System.out.println(new String(consumer.getOutput().getBytes("MS949"), "UTF-8"));
	                System.out.println("+:+:+:+: ISO8859_1 => UTF-8 +:+:+:+:");
	                System.out.println(new String(consumer.getOutput().getBytes("ISO8859_1"), "UTF-8"));                
	            }
	            if ((mode & EUC_KR) == EUC_KR) {
	                System.out.println("+:+:+:+: EUC-KR +:+:+:+:");
	                System.out.println(new String(consumer.getOutput().getBytes(), "EUC-KR"));
	                System.out.println("+:+:+:+: UTF-8 => EUC-KR +:+:+:+:");
	                System.out.println(new String(consumer.getOutput().getBytes("UTF-8"), "EUC-KR"));
	                System.out.println("+:+:+:+: KSC5601 => EUC-KR +:+:+:+:");
	                System.out.println(new String(consumer.getOutput().getBytes("KSC5601"), "EUC-KR"));
	                System.out.println("+:+:+:+: MS949 => EUC-KR +:+:+:+:");
	                System.out.println(new String(consumer.getOutput().getBytes("MS949"), "EUC-KR"));
	                System.out.println("+:+:+:+: ISO8859_1 => EUC-KR +:+:+:+:");
	                System.out.println(new String(consumer.getOutput().getBytes("ISO8859_1"), "EUC-KR"));
	            }
	            if ((mode & KSC5601) == KSC5601) {
	                System.out.println("+:+:+:+: KSC5601 +:+:+:+:");
	                System.out.println(new String(consumer.getOutput().getBytes(), "KSC5601"));
	                System.out.println("+:+:+:+: EUC-KR => KSC5601 +:+:+:+:");
	                System.out.println(new String(consumer.getOutput().getBytes("EUC-KR"), "KSC5601"));
	                System.out.println("+:+:+:+: UTF-8 => KSC5601 +:+:+:+:");
	                System.out.println(new String(consumer.getOutput().getBytes("UTF-8"), "KSC5601"));
	                System.out.println("+:+:+:+: MS949 => KSC5601 +:+:+:+:");
	                System.out.println(new String(consumer.getOutput().getBytes("MS949"), "KSC5601"));
	                System.out.println("+:+:+:+: ISO8859_1 => KSC5601 +:+:+:+:");
	                System.out.println(new String(consumer.getOutput().getBytes("ISO8859_1"), "KSC5601"));
	            } 
	            if ((mode & MS949) == MS949) {
	                System.out.println("+:+:+:+: MS949 +:+:+:+:");
	                System.out.println(new String(consumer.getOutput().getBytes(), "MS949"));
	                System.out.println("+:+:+:+: EUC-KR => MS949 +:+:+:+:");
	                System.out.println(new String(consumer.getOutput().getBytes("EUC-KR"), "MS949"));
	                System.out.println("+:+:+:+: UTF-8 => MS949 +:+:+:+:");
	                System.out.println(new String(consumer.getOutput().getBytes("UTF-8"), "MS949"));
	                System.out.println("+:+:+:+: KSC5601 => MS949 +:+:+:+:");
	                System.out.println(new String(consumer.getOutput().getBytes("KSC5601"), "MS949"));
	                System.out.println("+:+:+:+: ISO8859_1 => MS949 +:+:+:+:");
	                System.out.println(new String(consumer.getOutput().getBytes("ISO8859_1"), "MS949"));
	            }
	            if ((mode & ISO8859_1) == ISO8859_1) {
	                System.out.println("+:+:+:+: ISO8859_1 +:+:+:+:");
	                System.out.println(new String(consumer.getOutput().getBytes(), "ISO8859_1"));
	                System.out.println("+:+:+:+: EUC-KR => ISO8859_1 +:+:+:+:");
	                System.out.println(new String(consumer.getOutput().getBytes("EUC-KR"), "ISO8859_1"));
	                System.out.println("+:+:+:+: UTF-8 => ISO8859_1 +:+:+:+:");
	                System.out.println(new String(consumer.getOutput().getBytes("UTF-8"), "ISO8859_1"));
	                System.out.println("+:+:+:+: KSC5601 => ISO8859_1 +:+:+:+:");
	                System.out.println(new String(consumer.getOutput().getBytes("KSC5601"), "ISO8859_1"));
	                System.out.println("+:+:+:+: MS949 => ISO8859_1 +:+:+:+:");
	                System.out.println(new String(consumer.getOutput().getBytes("MS949"), "ISO8859_1"));
	            }
            }
		} else {
			// fail
			System.err.println("==============[FAILED]==============");
			System.err.println(consumer.getOutput());
		}
	}
	
	public static List<Product> parse(String output) {
	    String[] lines = output.split(System.lineSeparator());

        List<Product> productList = new ArrayList<Product>();
        Product product = null;
	    String line = null;
	    int nameIdx = 0, vendorIdx = 0, versionIdx = 0;
	    for (int i = 0; i < lines.length; i++) {
	        line = lines[i];
	        
	        if (i == 0) {
	            nameIdx = line.indexOf("Name");
	            vendorIdx = line.indexOf("Vendor");
	            versionIdx = line.indexOf("Version");
	            continue;
	        }
	        
	        if (StringUtils.isEmpty(line)) {
	            continue;
	        }
            
            product = new Product();
            product.setName(line.substring(nameIdx, vendorIdx).trim());
            
            if (!line.substring(vendorIdx, versionIdx).startsWith(" ")) {
                product.setVendor(line.substring(vendorIdx, versionIdx).trim());
                product.setVersion(line.substring(versionIdx).trim());
            } else {
                // in case of vendor info does not exist.
                product.setVendor("");
                product.setVersion(line.substring(vendorIdx).trim());
            }
            
            productList.add(product);
	    }
	    
	    return productList;
	}

}
//end of CommandExecutorSample.java

/**
 * <pre>
 * 
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
class Product {
    private String name;
    private String vendor;
    private String version;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * @return the vendor
     */
    public String getVendor() {
        return vendor;
    }
    
    /**
     * @param vendor the vendor to set
     */
    public void setVendor(String vendor) {
        this.vendor = vendor;
    }
    
    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }
    
    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder()
                            .append("Name : ")
                            .append(name)
                            .append(", Vendor : ")
                            .append(vendor)
                            .append(", Version : ")
                            .append(version);
        
        return sb.toString();
    }
}
//end of Product.java
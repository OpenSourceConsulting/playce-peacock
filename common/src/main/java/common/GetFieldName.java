/* 
 * Athena Peacock - Auto Provisioning
 * 
 * Copyright (C) 2012-2014 Open Source Consulting, Inc. All rights reserved by Open Source Consulting, Inc.
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
 * hjlee			2014. 7. 9.		First Draft.
 */
package common;

/**
 * <pre>
 * 
 * </pre>
 * @author hjlee
 * @version 1.0
 */
public class GetFieldName {

	public static void main(String args[]){
		String field = "CONFIG_FILE_ID, SOFTWARE_ID, CONFIG_FILE_SOURCE_LOCATION, CONFIG_FILE_TARGET_LOCATION, CONFIG_FILE_NAME, PROPERTIES, REG_USER_ID, REG_DT, UPD_USER_ID, UPD_DT";
		String[] fields = field.split(",");
		
		String tmpField = "";
		
		String lastField = "";
		
		for(int i=0;i<fields.length;i++) {
			tmpField = fields[i].trim();
			
			tmpField = tmpField.toLowerCase();
			
			String tmpField2 = "";
			for(int j=0;j<tmpField.length();j++) {
				char a = tmpField.charAt(j);
				
				if(a == '_') {
					j++;
					tmpField2 += String.valueOf(tmpField.charAt(j)).toUpperCase();
				} else {
					tmpField2 += a;
				}
			}
			
			if(i > 0) lastField += ",";
			lastField += tmpField2;
		}
		
		System.out.println(lastField);
		 
	}
}
//end of GetFieldName.java
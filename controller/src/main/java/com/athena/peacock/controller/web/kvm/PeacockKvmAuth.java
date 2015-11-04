/* 
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
 * Sang-cheon Park	2015. 7. 15.		First Draft.
 */
package com.athena.peacock.controller.web.kvm;

import org.libvirt.ConnectAuth;

/**
 * <pre>
 * /etc/sasl2/libvirt.conf
 * 
 * If using digest-md5 for username/passwds, then this is the file
 * containing the passwds. Use 'saslpasswd2 -a libvirt [username]'
 * to add entries, and 'sasldblistusers2 -f [sasldb_path]' to browse it
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class PeacockKvmAuth extends ConnectAuth {
	
	private String username;
	private String password;

	public PeacockKvmAuth(String username, String password) {
		this.username = username;
		this.password = password;
		
		credType = new CredentialType[] { CredentialType.VIR_CRED_AUTHNAME, CredentialType.VIR_CRED_ECHOPROMPT,
                CredentialType.VIR_CRED_REALM, CredentialType.VIR_CRED_PASSPHRASE, CredentialType.VIR_CRED_NOECHOPROMPT };
	}
	
	/* (non-Javadoc)
	 * @see org.libvirt.ConnectAuth#callback(org.libvirt.ConnectAuth.Credential[])
	 */
	@SuppressWarnings("incomplete-switch")
	@Override
	public int callback(Credential[] cred) {
        try {
            for (Credential c : cred) {
                String response = null;
                switch (c.type) {
                    case VIR_CRED_USERNAME:
                    case VIR_CRED_AUTHNAME:
                    case VIR_CRED_ECHOPROMPT:
                    case VIR_CRED_REALM:
                        response = username;
                        break;
                    case VIR_CRED_PASSPHRASE:
                    case VIR_CRED_NOECHOPROMPT:
                        response = password;
                        break;
                    default:
                }
                if (response.equals("") && !c.defresult.equals("")) {
                    c.result = c.defresult;
                } else {
                    c.result = response;
                }
                if (c.result.equals("")) {
                    return -1;
                }
            }
        } catch (Exception e) {
            return -1;
        }
        return 0;
	}//end of callback()
}
//end of PeacockKvmAuth.java
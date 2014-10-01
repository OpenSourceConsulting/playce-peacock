package com.athena.peacock.controller.web.alm.user;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.athena.peacock.controller.web.alm.crowd.AlmCrowdService;
import com.athena.peacock.controller.web.alm.project.dto.ProjectUserPasswordResetDto;
import com.athena.peacock.controller.web.common.model.DtoJsonResponse;

@Service
public class AlmUserService {

	@Autowired
	private AlmCrowdService crowdService;
	
	public void sendResetPassword() {

		System.out.println(new Date(System.currentTimeMillis()));

		try {
			String encode = AES256Cipher.AES_Encode("aaa", "58bfdf2717a98f99");
			String decode = AES256Cipher.AES_Decode("nxHbNubKHRdrYZokyf5Cww==",
					"58bfdf2717a98f99");
		} catch (InvalidKeyException | UnsupportedEncodingException
				| NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException
				| IllegalBlockSizeException | BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public DtoJsonResponse resetPassword(ProjectUserPasswordResetDto resetDto) {

		DtoJsonResponse response = new DtoJsonResponse();
		response.setMsg("패스워드가 변경되었습니다.");
		return response;
	}
}

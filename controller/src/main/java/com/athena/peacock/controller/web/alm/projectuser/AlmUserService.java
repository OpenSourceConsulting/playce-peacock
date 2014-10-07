package com.athena.peacock.controller.web.alm.projectuser;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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

	public void sendResetPassword(String username) {

		StringBuffer sb = new StringBuffer();
		sb.append(username);
		sb.append(":");
		sb.append(System.currentTimeMillis());

		try {
			String encode = AES256Cipher.AES_Encode(sb.toString(),
					"58bfdf2717a98f99");
			System.out.println(encode);
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

		String decode = "";
		try {
			decode = AES256Cipher.AES_Decode(resetDto.getKey(),
					"58bfdf2717a98f99");
		} catch (InvalidKeyException | UnsupportedEncodingException
				| NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException
				| IllegalBlockSizeException | BadPaddingException e) {
			response.setMsg("패스워드 변경 실패했습니다.");
			response.setSuccess(false);
		}

		String[] userInfomation = decode.split(":");
		crowdService.changePasswordUser(userInfomation[0],
				resetDto.getPassword());
		response.setMsg("패스워드가 변경되었습니다.");
		return response;
	}
}

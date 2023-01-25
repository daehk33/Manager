package com.enicom.nims.login;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * [RSAUtil] 로그인시 Id, Password RSA로 암/복호화
 * 
 * @author khj
 */
@Service
public class RSAUtil {
	private static Logger logger = LoggerFactory.getLogger(RSAUtil.class);
	private final static String KEY_NAME = "_RSA_NIMS_Key_";

	/**
	 * [RSAUtil] RSA 공개/암호키 발행
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void generate(HttpServletRequest request) throws Exception {
		logger.info("rsa generated!!!");
		KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		generator.initialize(2048);

		KeyPair keyPair = generator.genKeyPair();
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");

		PublicKey publicKey = keyPair.getPublic();
		PrivateKey privateKey = keyPair.getPrivate();

		// 세션에 공개키의 문자열을 키로 하여 개인키 저장
		request.getSession().setAttribute(KEY_NAME, privateKey);

		// 공개키를 문자열로 변환하여 Request Attribute로 넘겨줌.
		RSAPublicKeySpec publicSpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);

		String publicKeyModulus = publicSpec.getModulus().toString(16);
		String publicKeyExponent = publicSpec.getPublicExponent().toString(16);

		request.setAttribute("publicKeyModulus", publicKeyModulus);
		request.setAttribute("publicKeyExponent", publicKeyExponent);
	}

	/**
	 * [RSAUtil] 암호화된 ID, PW 복호화
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModifiableHttpServletRequest decrypt(HttpServletRequest request)
			throws Exception {
		logger.info("rsa decrypted!!!");
		ModifiableHttpServletRequest requestNew = new ModifiableHttpServletRequest(request);

		// 로그인 시도한 ID 및 Password
		String securedId = request.getParameter("manager_id");
		String securedPassword = request.getParameter("password");
		
		HttpSession session = request.getSession();
		PrivateKey privateKey = (PrivateKey) session.getAttribute(KEY_NAME);

		logger.debug("privateKey: {}", privateKey);
		session.removeAttribute(KEY_NAME); // 키 재사용 방지

		String id = decryptRsa(privateKey, securedId);
		String password = decryptRsa(privateKey, securedPassword);
		requestNew.setParameter("manager_id", id);
		requestNew.setParameter("password", password);

		return requestNew;
	}

	/**
	 * [RSAUtil] securedValue를 privateKey로 복호화
	 * 
	 * @param privateKey
	 * @param securedValue
	 * @return
	 * @throws Exception
	 */
	private String decryptRsa(PrivateKey privateKey, String securedValue) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		byte[] encryptedBytes = hexToByteArray(securedValue);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);

		byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
		String decryptedValue = new String(decryptedBytes, "utf-8"); // 문자 인코딩 주의.
		return decryptedValue;
	}

	/**
	 * [RSAUtil] 16진 문자열 => byte 배열 변환
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hexToByteArray(String hex) {
		if (hex == null || hex.length() % 2 != 0) {
			return new byte[] {};
		}

		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length(); i += 2) {
			byte value = (byte) Integer.parseInt(hex.substring(i, i + 2), 16);
			bytes[(int) Math.floor(i / 2)] = value;
		}
		return bytes;
	}

}

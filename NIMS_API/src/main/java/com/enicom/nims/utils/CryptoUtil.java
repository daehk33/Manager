package com.enicom.nims.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.annotation.Resource;

import org.apache.commons.net.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.enicom.nims.vo.AriaVO;

import egovframework.rte.fdl.cryptography.EgovCryptoService;
import egovframework.rte.fdl.cryptography.EgovPasswordEncoder;

/**
 * @author nicom
 * @category 암호화
 */
@Service("cryptoUtil")
public class CryptoUtil {
	private static Logger logger = LoggerFactory.getLogger(CryptoUtil.class);
	
	@Resource(name = "ARIACryptoService")
	private EgovCryptoService egovCryptoService;
	 
	@Resource(name = "passwordEncoder")
	private EgovPasswordEncoder passwordEncoder;
	
	/**
	 * [암호화] SHA-256 방식으로 암호화
	 * @param msg
	 * @return
	 */
	public static String encrypt(String msg) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(msg.getBytes());
			return bytesToHex(md.digest());
			
		} catch (NoSuchAlgorithmException e) {
			logger.error("존재하는 알고리즘인지 확인해 주세요.");
		}
		return null;
	}
	
	/**
	 * [암호화] 알고리즘 지정 가능 - MD5, SHA-1, SHA-256
	 * @param msg , algorithm
	 * @return String
	 */
	public static String encrypt(String msg, String algorithm) {
		try {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(msg.getBytes());
			return bytesToHex(md.digest());
			
		} catch (NoSuchAlgorithmException e) {
			logger.error(algorithm+"이 존재하는 알고리즘인지 확인해 주세요.");
		}
		return null;
	}
	

	private static String bytesToHex(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for(byte b: bytes) {
			builder.append(String.format("%02x", b));
		}
		return builder.toString();
	}
	
	/**
	 * [암호화] Aria 암호화
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String encryptByAria(String str) throws Exception {
		
		// 암호화 시키기 전 Byte로 변환
		byte[] byteContent = str.getBytes("UTF-8");
		
		// 암호화
		byte[] encrypt = egovCryptoService.encrypt(byteContent, AriaVO.ariaKey);
		String encrypted = Base64.encodeBase64String(encrypt);
		
		logger.warn(String.format("* 암호화 전 : %s", str));
		logger.warn(String.format("* 암호화 후 : %s", encrypted));
		return encrypted;
	}
	
	/**
	 * [암호화] Aria 암호 복호화
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	public String decryptByAria(String str) throws Exception {
		byte [] decrypt = Base64.decodeBase64(str); // String -> Byte로 Decode
		byte [] decrypted = egovCryptoService.decrypt(decrypt, AriaVO.ariaKey);
		
		String decResult = new String(decrypted, "UTF-8"); // 최종결과
		
		logger.warn(String.format("* 복호화 결과 : %s", decResult));
		
		return decResult;
	}
}

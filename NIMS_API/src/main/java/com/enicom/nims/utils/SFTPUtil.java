package com.enicom.nims.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

@Service("sftUtil")
public class SFTPUtil {
	private static Logger logger = LoggerFactory.getLogger(SFTPUtil.class);
	
	@Value("#{config['sftp.localImagePath']}")
	private String localImagePath;
	
	@Value("#{config['sftp.keyPath']}")
	private String keyPath;
	
	private Session session = null;
	private Channel channel = null;
	private ChannelSftp channelSftp = null;
	
	// SFTP 서버 연결
	public void connect(String host, String id, Integer port) {
		logger.info("SFTP Connecting >>> " + LocalDateTime.now());
		logger.info("Connecting to " + host);
		
		// Jsch 객체 생성
		JSch jsch = new JSch();
		
		try {
			// 접속 정보 설정 
			jsch.addIdentity(keyPath);
			
			session = jsch.getSession(id, host, port);
			
			// 세션관련 설정
			Properties properties = new Properties();
			
			// 호스트 정보 검사 안하도록 설정
			properties.put("StrictHostKeyChecking", "no");
			session.setConfig(properties);
			
			// 접속
			session.connect(1000);
			
			// SFTP 채널 접속
			channel = session.openChannel("sftp");
			channel.connect();
		}
		catch (JSchException e) {
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		channelSftp = (ChannelSftp) channel;
	}
	
	public void disconnection() {
		logger.info("SFTP disconnecting >>> " + LocalDateTime.now());
		
		if(channelSftp != null) channelSftp.quit();
		if(channel != null) channel.disconnect();
		if(session != null) session.disconnect();
	}
	
	public boolean fileUpload(String remotePath, String deviceId, String type, boolean isServer) {
		FileInputStream inputStream = null;
		
		// 장비별 폴더 경로 설정
		String path = "";
		if(type.equalsIgnoreCase("image")) {
			path = localImagePath + deviceId + "/";
		}
		else {
			return false;
		}
		
		// 장비가 서버인 경우 해당 작업 진행
		if(isServer) {
			try {
				File dir = new File(path);
				
				// 해당 경로 폴더 없는경우 생성
				if(!dir.exists() ) {
					try {
						dir.mkdir();
						logger.info(path + "에 폴더가 생성되었습니다.");
					}
					catch(Exception e) {
						e.printStackTrace();
						return false;
					}
				}
				
				File originalFiles = new File(path);
				String[] fileList = originalFiles.list();
				
				for(String file: fileList) {
					String originalPath = path + file;
					String newPath = remotePath + file;
					
					File originalFile = new File(originalPath);
					
					if(originalFile.isFile()) {
						FileUtil.copyFile(originalPath, newPath);
					}
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				return false;
			}
			
			return true;
		}
		
		// 연결 상태 확인
		if(session.isConnected() && channel.isConnected()) {
			try {
				// 서버측 폴더 File 객체 생성
				File dir= new File(path);
				
				String[] fileList = dir.list();
				for(String fileName : fileList) {
					// 업로드할 파일 객체 생성
					inputStream = new FileInputStream(path + fileName);
					
					// 파일 업로드
					channelSftp.cd(remotePath);
					channelSftp.put(inputStream, fileName);
				}
				
				inputStream.close();
			}
			catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		else {
			return false;
		}
		
		return true;
	}
	
	public boolean fileDownload(String remotePath, String deviceId, String type, boolean isServer) {
		InputStream inputStream = null;
		FileOutputStream outputStream = null;
		
		// 장비별 폴더 경로 설정
		String path = "";
		if(type.equalsIgnoreCase("image")) {
			path = localImagePath + deviceId + "/";
		}
		else {
			return false;
		}
		
		// 장비가 서버인 경우 해당 작업 진행
		if(isServer) {
			try {
				File dir = new File(path);
				
				// 해당 경로 폴더 없는경우 생성
				if(!dir.exists() ) {
					try {
						dir.mkdir();
						logger.info(path + "에 폴더가 생성되었습니다.");
					}
					catch(Exception e) {
						e.printStackTrace();
						return false;
					}
				}
				
				File originalFiles = new File(remotePath);
				String[] fileList = originalFiles.list();
				
				for(String file: fileList) {
					String originalPath = remotePath + file;
					String newPath = path + file;
					
					File originalFile = new File(originalPath);
					
					if(originalFile.isFile()) {
						FileUtil.copyFile(originalPath, newPath);
					}
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				return false;
			}
			
			return true;
		}
		
		if(session.isConnected() && channel.isConnected()) {
			try {
				logger.info("fileDownload is Running >>> " + LocalDateTime.now());
				
				// 서버측에 해당 장비ID 폴더 유무 확인
				File dir = new File(path);
				
				if(!dir.exists()) {
					try {
						dir.mkdir();
						logger.info(path + "에 폴더가 생성되었습니다.");
					}
					catch(Exception e) {
						e.printStackTrace();
						return false;
					}
				}
				
				// 파일 위치로 이동
				channelSftp.cd(remotePath);
				
				// 경로에 위치한 파일 목록 불러오기
				Vector<ChannelSftp.LsEntry> fileList = channelSftp.ls(remotePath);
				
				for(LsEntry entry : fileList) {
					// 경로에서 받은 파일명 변수 처리
					String fileName = entry.getFilename();
					
					if(!entry.getAttrs().isDir()) {
						// 파일 GET
						inputStream = channelSftp.get(fileName);
						
						// 파일 저장 ( 장비별로 폴더 분할 )
						File localFile = new File(path + fileName);
						outputStream = new FileOutputStream(localFile);
						FileCopyUtils.copy(inputStream, outputStream);
						
						inputStream.close();
						outputStream.close();
					}
				}
				
				return true;
			}
			catch (SftpException e) {
				e.printStackTrace();
				return false;
			}
			catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}
		else {
			return false;
		}
	}
	
	public boolean fileRemove(String remotePath, String deviceId, String fileName, boolean isServer) {
		// 장비별 폴더 경로 설정
		String path = localImagePath + deviceId + "/";
		
			try {
				logger.info("fileRemove is Running >>> " + LocalDateTime.now());
				
				// 로컬 파일
				File file = new File(path + fileName);
				
				if(file.exists()) {
					if(file.delete()) {
						logger.info("Local file remove success");
					}
					else {
						logger.error("Local file remove failed");
					}
				}
				else {
					logger.info("There's no local file in " + path);
				}
				
				// 장비가 서버인 경우 동작
				if(isServer) {
					file = new File(remotePath + fileName);
					
					if(file.exists()) {
						if(file.delete()) {
							logger.info("Remote file remove success");
							return true;
						}
						else {
							logger.error("Remote file remove failed");
							return false;
						}
					}
					else {
						logger.info("There's no remote file in " + remotePath);
						return false;
					}
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				return false;
			}
			
			if(session.isConnected() && channel.isConnected() && !isServer) {
				try {
					// 경로 이동
					channelSftp.cd(remotePath);
					
					// 파일 삭제
					channelSftp.rm(fileName);
					
					logger.info("Remote file remove success");
				}
				catch(Exception e) {
					e.printStackTrace();
					logger.error("Remote file remove failed");
				}
			}
		return true;
	}
}

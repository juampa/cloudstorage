package com.logalty.cloudstorage.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.logalty.cloudstorage.uploader.Uploader;

public class MainFileService {

	private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

	@Autowired
	private String fileIn ;

	@Autowired
	private Uploader uploader ; 

	public MainFileService() {
		// TODO Auto-generated constructor stub
	}

	public void execute() {

		log.info("Starting job...");

		try {

			InputStream    fis;
			BufferedReader br;
			String         line;

			fis = new FileInputStream(fileIn);
			br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
			while ((line = br.readLine()) != null) {

				File fileEntry = new File(line) ;

				if (! (fileEntry.isDirectory() || isFileLocked(fileEntry))) 
				{
					log.debug("Processing : " + fileEntry.getName());

					/*
					 * Aqui ira la logica de la subida
					 */
					uploader.upload(fileEntry) ;


				}
			}

			// Done with the file
			br.close();

		} catch (FileNotFoundException fio) 
		{
			log.error(fio.getMessage());
			throw new RuntimeException(fio) ;
		} catch (IOException ioe)
		{
			log.error(ioe.getMessage());
			throw new RuntimeException(ioe) ;
		} catch (Exception e)
		{
			log.error(e.getMessage());
			throw new RuntimeException(e) ;
		}
		log.info("End job...");		
	}

	private boolean isFileLocked(File file)
	{
		boolean isFileUnlocked = false;
		try {
			org.apache.commons.io.FileUtils.touch(file);
			isFileUnlocked = true;
		} catch (IOException e) {
			isFileUnlocked = false;
		}

		return ! isFileUnlocked ;
	}

	public Uploader getUploader() {
		return uploader;
	}

	public void setUploader(Uploader uploader) {
		this.uploader = uploader;
	}

	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		MainFileService exampleService = (MainFileService) ctx.getBean("example") ;
		exampleService.execute();
		
	}

	public String getFileIn() {
		return fileIn;
	}

	public void setFileIn(String fileIn) {
		this.fileIn = fileIn;
	}
}

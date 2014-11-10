package com.logalty.cloudstorage.job;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.logalty.cloudstorage.uploader.Uploader;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class AmazonUploadJob implements Job {

	private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
	
	public static final String DIRECTORY_IN = "D:\\pruebas\\nube\\in" ;
	public static final String DIRECTORY_OUT = "D:\\pruebas\\nube\\out" ;
	
	public static final String  FILE_ENDS_PATTERN = ".par"; 
	
	
	@Autowired
	private Uploader uploader ; 
	
	
	public void execute(JobExecutionContext jobContext) throws JobExecutionException {
		
		log.info("Starting job...");
		
		/**
		 * Leeremos de momento de un directorio y eso forzara la subida a "Amazon".
		 * De momento se copiara a un fichero de salida
		 *  
		 */
		
		File diretoryIn = new File(DIRECTORY_IN) ; 
		FilenameFilter fileNameFilter = new FilenameFilter() {
			
			public boolean accept(File dir, String name) {
				return name.endsWith(FILE_ENDS_PATTERN) ;
			}
		};
		
		for (final File fileEntry : diretoryIn.listFiles(fileNameFilter)) {
			// No permitimos directorios anidados de momento.
			if (! (fileEntry.isDirectory() || isFileLocked(fileEntry))) 
	        {
				log.debug("Processing : " + fileEntry.getName());
				
				boolean sucess = true ;
				try 
				{
					File fileOut = new File(DIRECTORY_OUT + File.separator + fileEntry.getName()) ;				
					copyFile(fileEntry, fileOut) ;
				} catch (IOException e) {
					// En caso de error este fichero no se movera, y se reintentara posteriormente.
					sucess = false ;
				}
				
				if (sucess)
				{
					fileEntry.delete() ;
				}
	        } 
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
	
	private void copyFile(File sourceFile, File destFile) throws IOException {
	    if(!destFile.exists()) {
	        destFile.createNewFile();
	    }

	    FileChannel source = null;
	    FileChannel destination = null;
	    try {
	    	
	        source = new FileInputStream(sourceFile).getChannel();
	        destination = new FileOutputStream(destFile).getChannel();
	     
	        long count = 0;
	        long size = source.size();              
	        while((count += destination.transferFrom(source, count, size-count))<size);
	    }
	    finally {
	        if(source != null) {
	            source.close();
	        }
	        if(destination != null) {
	            destination.close();
	        }
	    }
	}

	public Uploader getUploader() {
		return uploader;
	}

	public void setUploader(Uploader uploader) {
		this.uploader = uploader;
	}
}

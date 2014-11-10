package com.logalty.cloudstorage.uploader;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.StorageClass;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

public class AmazonUploader implements Uploader 
{

	private final String existingBucketName = "juampatestbucket";
	private TransferManager tm; 
	
	
	public AmazonUploader()
	{
		tm = new TransferManager(new ProfileCredentialsProvider());
	}
	
	public boolean upload(File file)
	{		
		

		try {
			//Obtenemos el md5 del fichero

			ObjectMetadata meta = prepareMetadata(file) ;

			PutObjectRequest putObjectRequest = new PutObjectRequest(existingBucketName, "/2014/07/" + file.getName(), file) ;
			putObjectRequest.setStorageClass(StorageClass.Standard);
			putObjectRequest.setMetadata(meta);

			// TransferManager processes all transfers asynchronously, 
			// so this call will return immediately.
			Upload upload = tm.upload(putObjectRequest);

			// Or you can block and wait for the upload to finish
			upload.waitForCompletion();
			System.out.println("Upload complete. " + file.getName());
		} catch (AmazonClientException amazonClientException) {
			System.out.println("Unable to upload file, upload was aborted.");
			amazonClientException.printStackTrace();
			return false ;
		} catch (InterruptedException e) {
			System.out.println("Unable to upload file, upload was aborted.");
			e.printStackTrace();
			return false ;
		} catch (Exception e) {
			System.out.println("Unable to upload file, upload was aborted.");
			e.printStackTrace();
			return false ;			
		}

		return true ;
	}

	private ObjectMetadata prepareMetadata(File file) throws Exception
	{

		InputStream fis = new FileInputStream(file) ;
		byte[] byteArray = IOUtils.toByteArray(fis);
		fis.close();
		
		// get MD5 base64 hash
		byte[] resultByte = DigestUtils.md5(byteArray) ;
	
		String hashtext = new String(Base64.encodeBase64(resultByte));

		ObjectMetadata meta =  new ObjectMetadata();
		meta.setContentLength(byteArray.length);
		meta.setContentMD5(hashtext);

		return meta ;


	}
	
	public void shutDown()  
	{
		tm.shutdownNow(); 
	}
	
	public static void main(String[] args) {
		AmazonUploader amazonUploader = new AmazonUploader() ;
		amazonUploader.upload(new File("D:\\pruebas\\pocos.txt")) ;
		amazonUploader.upload(new File("D:\\pruebas\\sin_certificado.txt")) ;
		amazonUploader.shutDown();
	}
}

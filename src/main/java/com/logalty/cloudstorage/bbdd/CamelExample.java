package com.logalty.cloudstorage.bbdd;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws.s3.S3Constants;
import org.springframework.beans.factory.annotation.Autowired;

public class CamelExample extends RouteBuilder {

	@Autowired
	private String bucketName = "juampatestbucket";

	@Autowired
	private String region = "eu-west-1";

	

	@Override
	public void configure() throws Exception {
		final String awsEndpoint =

			String.format("aws-s3://%s?storageClass=REDUCED_REDUNDANCY&region=%s&amazonS3Client=#amazonS3",
						this.bucketName, this.region);

		from("file://inputdir/?recursive=true")
		.setHeader(S3Constants.KEY, simple("name"))
		.setHeader(S3Constants.CONTENT_TYPE, simple("application/pdf"))
		.to(awsEndpoint)
		.end();
		// @formatter:on 

	}

}

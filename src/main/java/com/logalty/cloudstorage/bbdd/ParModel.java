package com.logalty.cloudstorage.bbdd;

import java.io.Serializable;
import java.util.Date;

public class ParModel implements Serializable {

	/**
	 * Auto Generated
	 */
	private static final long serialVersionUID = -2789029488810077358L;

	private String guid ;
	
	private String filePath ;
	
	private Boolean isInAmazon ;
	private Boolean isInGoogle ;
	
	private final Date createdAt ;
	private Date lastUpdate ;
	
	public ParModel(String guid, String filePath)
	{
		this.guid = guid ;
		
		this.filePath = filePath ;
		
		this.isInAmazon = false ;
		this.isInGoogle = false ;
		
		this.createdAt = new Date() ;
		this.lastUpdate = new Date() ;		
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public Boolean getIsInAmazon() {
		return isInAmazon;
	}

	public void setIsInAmazon(Boolean isInAmazon) {
		this.isInAmazon = isInAmazon;
	}

	public Boolean getIsInGoogle() {
		return isInGoogle;
	}

	public void setIsInGoogle(Boolean isInGoogle) {
		this.isInGoogle = isInGoogle;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Date getCreatedAt() {
		return createdAt;
	}
	
	@Override
	public boolean equals(Object o) {
		
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		ParModel other = (ParModel) o ;
		
		if (guid != null ? !guid.equals(other.guid) : other.guid != null) return false;
		if (filePath != null ? !filePath.equals(other.filePath) : other.filePath != null) return false;
		
		return true;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}

package com.freightliner.pcsparsing;

import java.io.Serializable;



public interface Components extends Serializable {
	
	public int getAccountNumber();


	public void setAccountNumber(int accountNumber) ;


	public String getCompItemNo();


	public void setCompItemNo(String compItemNo);


	public String getCompQty();

	public void setCompQty(String compQty) ;


	public String getDepartmentCode();


	public void setDepartmentCode(String departmentCode);


	public String getItemDescription() ;


	public void setItemDescription(String description) ;


	public String getEngRevisionLevel() ;


	public void setEngRevisionLevel(String engRevisionLevel) ;


	public String getItemNumber() ;


	public void setItemNumber(String itemNumber) ;


	public String getMfgRevisionLevel() ;


	public void setMfgRevisionLevel(String mfgRevisionLevel) ;


	public String getMipNumber() ;


	public void setMipNumber(String mipNumber) ;


	public String getModuleNumber() ;


	public void setModuleNumber(String moduleNumber) ;

	public String getPurchasingUnitOfMeasure() ;


	public void setPurchasingUnitOfMeasure(String purchasingUnitOfMeasure) ;
	
	public void setItarRestrIndc(String itarRestrIndc) ;


	public String getSequenceNumber() ;


	public void setSequenceNumber(String sequenceNumber) ;

	public String getItemStatus() ;


	public void setItemStatus(String status) ;


	public String getTsoSequenceNumber() ;


	public void setTsoSequenceNumber(String tsoSequenceNumber) ;


	public String getTsoSplitNumber() ;


	public void setTsoSplitNumber(String tsoSplitNumber) ;


	public String getItemType() ;


	public void setItemType(String typeCode) ;	
	


}

package com.freightliner.pcsparsing.util;

import java.sql.Timestamp;
import  java.util.Date;

public class ApplicationUtilities {
	
	public static Timestamp getCurrentJavaSqlTimestamp(Date inputDate){
		
		Timestamp returnTS = null;
		
		if(null!=inputDate){
			
			returnTS =  new Timestamp(inputDate.getTime());
		
		}else{
			
			Date date = new java.util.Date();
			returnTS = new Timestamp(date.getTime());
		}
		
		return returnTS;
		
	}//end of method getCurrentJavaSqlTimestamp(0
	
	


}

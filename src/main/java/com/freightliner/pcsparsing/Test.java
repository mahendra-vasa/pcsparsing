package com.freightliner.pcsparsing;

public class Test {
	
	
	
	public static void main(String []bhl)
	{
		StringBuffer sb = new StringBuffer();
	    sb.append("This is the test");
	    
	    System.out.println("the length of the String is :="+sb.length());
	    sb.delete(0, sb.length());
	    System.out.println("the new length is :="+sb.toString().length());
	    		System.out.println("contet is :="+sb.toString());
	}

}

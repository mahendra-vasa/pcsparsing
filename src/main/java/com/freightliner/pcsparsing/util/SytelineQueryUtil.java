package com.freightliner.pcsparsing.util;

public class SytelineQueryUtil {
	
	private static String queryAppender="DECLARE @Infobar InfobarType, @CurrSite SiteType = (SELECT site FROM parms_mst) EXEC [dbo].[SetSiteSp] @CurrSite, @Infobar OUTPUT ";

	public static String getQueryAppender() {
		return queryAppender;
	}
}

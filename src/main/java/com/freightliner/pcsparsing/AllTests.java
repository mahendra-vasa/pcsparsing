package com.freightliner.pcsparsing;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.freightliner.pcsparsing.impl.BOMTestCase;
import com.freightliner.pcsparsing.impl.DataSourceTestCase;
import com.freightliner.pcsparsing.impl.ItemTestCase;
import com.freightliner.pcsparsing.impl.LocationTestCase;
import com.freightliner.pcsparsing.impl.ModuleOrderTestCase;
import com.freightliner.pcsparsing.impl.ModuleTestCase;
import com.freightliner.pcsparsing.impl.SerialNumberTestCase;
import com.freightliner.pcsparsing.impl.TruckTestCase;
import com.freightliner.pcsparsing.impl.UserActionTestCase;
import com.freightliner.pcsparsing.service.BOMFinderTestCase;
import com.freightliner.pcsparsing.service.ItemFinderTestCase;
import com.freightliner.pcsparsing.service.LocationFinderTestCase;
import com.freightliner.pcsparsing.service.ModuleFinderTestCase;
import com.freightliner.pcsparsing.service.ModuleServiceTestCase;
import com.freightliner.pcsparsing.service.ParseServiceTestCase;
import com.freightliner.pcsparsing.service.SerialNumberFinderTestCase;
import com.freightliner.pcsparsing.service.SerialNumberServiceTestCase;
import com.freightliner.pcsparsing.service.TruckFinderTestCase;
import com.freightliner.pcsparsing.service.UserActionServiceTestCase;

public class AllTests extends TestSuite {
	
	public AllTests(String name) {
		super(name);
	}
	
    public static Test suite () {
        TestSuite suite = new TestSuite();
        suite.addTest(new TestSuite(BOMTestCase.class));
        suite.addTest(new TestSuite(DataSourceTestCase.class));
        suite.addTest(new TestSuite(ItemTestCase.class));
        suite.addTest(new TestSuite(LocationTestCase.class));
        suite.addTest(new TestSuite(ModuleOrderTestCase.class));
        suite.addTest(new TestSuite(ModuleTestCase.class));
        suite.addTest(new TestSuite(SerialNumberTestCase.class));
        suite.addTest(new TestSuite(TruckTestCase.class));
        suite.addTest(new TestSuite(UserActionTestCase.class));
        suite.addTest(new TestSuite(BOMFinderTestCase.class));
        suite.addTest(new TestSuite(ItemFinderTestCase.class));
        suite.addTest(new TestSuite(LocationFinderTestCase.class));
        suite.addTest(new TestSuite(ModuleFinderTestCase.class));
        suite.addTest(new TestSuite(ModuleServiceTestCase.class));
        suite.addTest(new TestSuite(ParseServiceTestCase.class));
        suite.addTest(new TestSuite(SerialNumberFinderTestCase.class));
        suite.addTest(new TestSuite(SerialNumberServiceTestCase.class));
        suite.addTest(new TestSuite(TruckFinderTestCase.class));
        suite.addTest(new TestSuite(UserActionServiceTestCase.class));
        return suite;
    }

}

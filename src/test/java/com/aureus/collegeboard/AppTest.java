package com.aureus.collegeboard;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;


/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    final static Logger logger = Logger.getLogger(AppTest.class);
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {

        StudentUtil su = StudentUtil.instantiateStudentUtil();
        logger.info("Unit tests in JUnit\n" + "----------------------------------\n");
        logger.info("Test for kids not yet in school");

        /*
         test if a month and day is in the schoolyear or not
         */
        //;assertTrue(new inTheSchoolYear());

        assertEquals(-1, su.getGradeOnGivenDate("2016-06-02", "2000-06-02"));
        //day before Kindergarten starts, August 31st 2003 for someone graduating in 2016
        assertEquals(-1, su.getGradeOnGivenDate("2016-06-02", "2003-08-30"));
        //what about first day of kindergarten? September 1st 2003
        assertEquals(0, su.getGradeOnGivenDate("2016-06-02", "2003-09-01"));
    }
}

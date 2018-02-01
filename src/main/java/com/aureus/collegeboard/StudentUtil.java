package com.aureus.collegeboard;

import org.apache.log4j.Logger;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Locale;


public class StudentUtil {

    final static Logger logger = Logger.getLogger(StudentUtil.class);

    private static StudentUtil studentUtilInstance;
    public StudentUtil(){

    }

    /**
     *
     * Since we aren't using static methods to call, and we only need one object
     * instantiated for this class, I'm enforcing a singleton pattern from which
     * the methods of the class can be called as many times as we like.
     *
     * @return the single instance of StudentUtil
     */
    public static StudentUtil instantiateStudentUtil(){

        if(null == studentUtilInstance){

            studentUtilInstance = new StudentUtil();
            logger.info("Student Util Instance initialized");
        }

        return studentUtilInstance;
    }

    /**
     *
     * This method does the work for the class.  It receives both String date
     * fields.  If givenDate is null, it sets today's date for givenDate.
     *
     * It leverages the java.time API from jdk 8+ (previous java time API's were
     * not useful).  The method finds the number of days, months, and years
     * between the student's graduation date and given date to determine
     * what grade they are in (with Academic years running from Sep-01 to
     * Jun-30 inclusive).
     *
     * The edge case of children in the summer months before Kindergarten starts
     * is handled in a poor way, using string coparisons rather than date comparisons,
     * and I know I can handle that better.
     *
     * @param highSchoolGradDate String
     * @param givenDate String
     * @return student's grade at the time of givenDate int
     */

    public int getGradeOnGivenDate(String highSchoolGradDate, String givenDate) {

        int grade=0; //just initialize this and we'll store the grade of the person in it
        int lastYearOfHighSchool=12;

        try{

            DateTimeFormatter formatterYears = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            formatterYears = formatterYears.withLocale(Locale.US);

            //edge case
            String startOfSchoolYear = "09-01";
            String[] givenDateArray = givenDate.split("-");
            String compareGivenDate = givenDateArray[1] + "-" + givenDateArray[2];

            LocalDate gD; //this is the localdate object I'll use for simple arithmetic
            LocalDate hSGD;  //and the other one

            String[] hsgdArray = highSchoolGradDate.split("-");
            String[] gdArray = givenDate.split("-");

            //for leap year below - with some funky logic and black magic

            int countOfLeapYears =0;

            if(null==givenDate){ //a bit of an issue with null values as parameters in java 9

                gD = LocalDate.now();
            }else
                gD = LocalDate.parse(givenDate, formatterYears);

            String[] givenDateDayMonth = givenDate.split("-");
            String monthDayCheck = givenDateDayMonth[1] + "-" + givenDateDayMonth[2];
            hSGD = LocalDate.parse(highSchoolGradDate, formatterYears);


            if(hSGD.isLeapYear() && gD.getYear()==hSGD.getYear()){
                countOfLeapYears++;
            }else if (gD.isBefore(hSGD)){

                for (LocalDate date = gD; date.isBefore(hSGD); date = date.plusYears(1)) {
                    if (date.isLeapYear()) {
                        countOfLeapYears++;
                    }
                }
            }
            logger.info("Number of leap years: " + countOfLeapYears);
            long daysUntilGraduation = ChronoUnit.DAYS.between(gD, hSGD);
            logger.info("Days until graduation: " + daysUntilGraduation);

            //need to subtract leap years if I'm going to divide by 365 to get the actual number of years
            double yearsUntilGraduation = ((double)daysUntilGraduation-countOfLeapYears)/365;

            logger.info("Years until graduation: " + yearsUntilGraduation);

            grade = lastYearOfHighSchool - (int)yearsUntilGraduation;

            //since we consider anyone that graduated the previous grade to be in the "next" grade, but that doesn't
            //work for when kids aren't in school yet (think pre-k), we have an edge case where a kid would look
            //like he's in grade 0 (kindergarten) despite it only being after June 30th of the year he starts
            //kindergarten.
            if(grade<0 || (yearsUntilGraduation > 12 && compareGivenDate.compareTo(startOfSchoolYear) <0)){
                grade =-1;
                logger.info("Return value: -1, pre-k");
            }else if((grade>12) || (grade==12 && daysUntilGraduation<0)){
                grade =99;
                logger.info("Return value: 99 post-grad");
            }

        } catch (NullPointerException npe) {
            logger.error("Null pointer exception: ", npe);
            System.out.println("Caught a null pointer");
        } catch (Exception e) {
            logger.error("Stack trace: ", e);
        }

        logger.info("Return value: " + grade);
        return grade;

    }



    public static void main(String[] args){


        StudentUtil su = StudentUtil.instantiateStudentUtil();
        logger.info("Unit tests outside JUnitj\n" + "----------------------------------\n");
        int trial1 = su.getGradeOnGivenDate("2016-06-02", "2016-06-02");
        logger.info(trial1);
        int trial2 = su.getGradeOnGivenDate("2016-06-02", "2000-06-02");
        logger.info(trial2);
        int trial3 = su.getGradeOnGivenDate("2016-06-02", "2016-06-03");
        logger.info(trial3);
        int trial4 = su.getGradeOnGivenDate("2016-06-02", "2003-08-30");
        logger.info(trial4);
        int trial5 = su.getGradeOnGivenDate("2016-06-02", "2016-01-30");
        logger.info(trial5);
        int trial6 = su.getGradeOnGivenDate("2016-06-02", "2015-07-30");
        logger.info(trial6);
        int trial7 = su.getGradeOnGivenDate("2016-06-02", "2003-09-01");
        logger.info(trial7);
        int trial8 = su.getGradeOnGivenDate("2016-06-02", "2003-12-31");
        logger.info(trial8);
        int trial9 = su.getGradeOnGivenDate("2016-06-02", "2017-01-30");
        logger.info(trial9);

    }
}

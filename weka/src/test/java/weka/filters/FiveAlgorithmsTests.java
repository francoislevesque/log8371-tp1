package weka.filters;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.test.WekaTestSuite;

/**
 * Test class for filters among the 5 selected algorithms for TP1.
 */
public class FiveAlgorithmsTests extends WekaTestSuite {

    public static Test suite() {
        TestSuite suite = (TestSuite) suite("weka.filters.Filter");

        suite.addTest(AllFilterTest.suite());
        suite.addTest(MultiFilterTest.suite());

        return suite;
    }

    public static void main(String []args) {
        junit.textui.TestRunner.run(suite());
    }
}

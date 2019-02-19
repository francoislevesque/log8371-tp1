package weka;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test class for five algorithms selected.
 */
public class FiveAlgorithmsTests extends TestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite();

        // filters
        suite.addTest(weka.filters.AllTests.suite());

        return suite;
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}

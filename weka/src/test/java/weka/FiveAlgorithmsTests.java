package weka;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Test class for five algorithms selected.
 */
public class FiveAlgorithmsTests extends TestSuite {

    public static Test suite() {
        TestSuite suite = new TestSuite();

        // Core components
        suite.addTest(weka.core.AllTests.suite());

        // associators
        suite.addTest(weka.associations.AllTests.suite());

        // attribute selection
        suite.addTest(weka.attributeSelection.AllTests.suite());

        // classifiers
        suite.addTest(weka.classifiers.AllTests.suite());

        // clusterers
        suite.addTest(weka.clusterers.AllTests.suite());

        // data generators
        suite.addTest(weka.datagenerators.AllTests.suite());

        // estimators
        // suite.addTest(weka.estimators.AllTests.suite());

        // filters
        suite.addTest(weka.filters.AllTests.suite());

        // High level applications
        // suite.addTest(weka.experiment.AllTests.suite());
        // suite.addTest(weka.gui.AllTests.suite());

        return suite;
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}

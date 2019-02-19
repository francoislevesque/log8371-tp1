package weka.filters;

import junit.framework.Test;
import junit.framework.TestSuite;
import weka.test.WekaTestSuite;

/**
 * Test class for filters among the 5 selected algorithms for TP1.
 */
public class FiveAlgorithmsTests extends WekaTestSuite {

    public static Test suite() {

        TestSuite suite = new TestSuite();

        // Add tests for 5 selected algorithms.
        suite.addTest(RenameRelationTest.suite());
        suite.addTest(weka.filters.unsupervised.instance.RemoveFrequentValuesTest.suite());
        suite.addTest(weka.filters.unsupervised.attribute.ObfuscateTest.suite());
        suite.addTest(weka.filters.unsupervised.attribute.RemoveUselessTest.suite());
        suite.addTest(weka.filters.unsupervised.attribute.AddNoiseTest.suite());

        return suite;
    }

    public static void main(String []args) {
        junit.textui.TestRunner.run(suite());
    }
}

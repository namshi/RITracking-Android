package de.rocketinternet.android.tracking;

import android.test.InstrumentationTestCase;

/**
 *  @author alessandro.balocco
 */
public class Test extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void test() throws Exception {
        final int expected = 5;
        final int reality = 5;
        assertEquals(expected, reality);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}

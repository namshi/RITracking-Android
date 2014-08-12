package de.rocketinternet.android.tracking.handlers;

import android.net.Uri;
import android.test.InstrumentationTestCase;

import java.net.URL;

import de.rocketinternet.android.tracking.listeners.RIOnHandledOpenUrlMockImpl;

/**
 * @author alessandro.balocco
 */
public class RIOpenUrlHandlerTest extends InstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testWrongHostFoundAndMissedListenerNotification() {
        Uri wrongUri = Uri.parse("schema://testHostWrong/testPath/testPath2?key1=value1&key2=value2");

        RIOnHandledOpenUrlMockImpl listener = new RIOnHandledOpenUrlMockImpl();
        RIOpenUrlHandler handler = new RIOpenUrlHandler("WrongIdentifier", "testHost", "testPath", listener);
        handler.handleOpenUrl(wrongUri);

        assertFalse(listener.isHandlerCalled());
    }

    public void testWrongPathFoundAndMissedListenerNotification() {
        Uri wrongUri = Uri.parse("schema://testHostWrong/testPathWrong/testPath2?key1=value1&key2=value2");

        RIOnHandledOpenUrlMockImpl listener = new RIOnHandledOpenUrlMockImpl();
        RIOpenUrlHandler handler = new RIOpenUrlHandler("WrongIdentifier", "testHost", "testPath", listener);
        handler.handleOpenUrl(wrongUri);

        assertFalse(listener.isHandlerCalled());
    }

    public void testRightPatternFoundAndSuccessfulListenerNotification() {
        Uri rightUri = Uri.parse("schema://testHost/testPath?key1=value1&key2=value2");

        RIOnHandledOpenUrlMockImpl listener = new RIOnHandledOpenUrlMockImpl();
        RIOpenUrlHandler handler = new RIOpenUrlHandler("RightIdentifier", "testHost", "testPath", listener);
        handler.handleOpenUrl(rightUri);

        assertTrue(listener.isHandlerCalled());
        assertEquals("RightIdentifier", listener.getHandlerIdentifier());
        assertTrue(listener.getQueryParams().containsKey("key1"));
    }
}

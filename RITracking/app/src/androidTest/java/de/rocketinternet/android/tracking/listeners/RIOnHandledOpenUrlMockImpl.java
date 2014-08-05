package de.rocketinternet.android.tracking.listeners;

import java.util.HashMap;
import java.util.Map;

/**
 *  @author alessandro.balocco
 */
public class RIOnHandledOpenUrlMockImpl implements RIOnHandledOpenUrl {

    private boolean mIsHandlerCalled;
    private String mHandlerIdentifier;
    private Map<String, String> mParams;

    @Override
    public void onHandledOpenUrl(String identifier, Map<String, String> params) {
        mIsHandlerCalled = true;
        mHandlerIdentifier = identifier;
        mParams = params;
    }

    public boolean isHandlerCalled() {
        return mIsHandlerCalled;
    }

    public String getHandlerIdentifier() {
        return mHandlerIdentifier;
    }

    public Map<String, String> getQueryParams() {
        return mParams;
    }
}

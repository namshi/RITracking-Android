package de.rocketinternet.android.tracking.handlers;

import android.net.Uri;
import android.text.TextUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.rocketinternet.android.tracking.listeners.RIOnHandledOpenUrl;
import de.rocketinternet.android.tracking.utils.RILogUtils;

/**
 *  @author alessandro.balocco
 *
 *  Convenience controller to wrap logic for particular deepling URL structures based on regular
 *  expression match pattern
 */
public class RIOpenUrlHandler {

    private String mIdentifier;
    private String mHost;
    private String mPath;
    private RIOnHandledOpenUrl mListener;

    public RIOpenUrlHandler(String identifier, String host, String path, RIOnHandledOpenUrl listener) {
        mIdentifier = identifier;
        mHost = host;
        mPath = path;
        mListener = listener;
    }

    /**
     *  Method that check if this handler need to handle the provided uri
     *
     *  @param uri  the provided uri to analyze
     *  @return     true if the uri was handled successfully
     */
    public boolean handleOpenUrl(Uri uri) {
        if (mHost == null || !mHost.equalsIgnoreCase(uri.getHost())) {
            return false;
        }

        String path = uri.getPath();
        if (!TextUtils.isEmpty(path) && path.length() > 0 && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        if (mPath == null || !mPath.equalsIgnoreCase(path)) {
            return false;
        }

        Map<String, String> params = new HashMap<String, String>();

        String urlQueryParams = uri.getQuery();
        String[] separatedQueryParams = urlQueryParams.split("&");
        for (int i = 0; i < separatedQueryParams.length; i++) {
            String[] queryParam = separatedQueryParams[i].split("=");
            params.put(queryParam[0], queryParam[1]);
            RILogUtils.logError("Query param " + (i + 1) + " with key = " + queryParam[0] + " and value = " + queryParam[1]);
        }

        if (mListener != null) {
            mListener.onHandledOpenUrl(mIdentifier, params);
            return true;
        }

        return false;
    }
}

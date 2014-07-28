package de.rocketinternet.android.tracking.handlers;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.rocketinternet.android.tracking.listeners.OnHandledOpenUrl;
import de.rocketinternet.android.tracking.utils.RILogUtils;

/**
 *  @author alessandro.balocco
 *
 *  Convenience controller to wrap logic for particular deepling URL structures based on regular
 *  expression match pattern
 */
public class RIOpenUrlHandler {

    private OnHandledOpenUrl mListener;
    private String mRegex;
    private String[] mMacros;

    public RIOpenUrlHandler(OnHandledOpenUrl listener, String regex, String[] macros) {
        mListener = listener;
        mRegex = regex;
        mMacros = macros;
    }

    public void handleOpenUrl(URL url) {
        List<String> matches = new ArrayList<String>();
        Pattern pattern = Pattern.compile(mRegex);
        Matcher matcher = pattern.matcher(url.toString());
        while (matcher.find()) {
            matches.add(matcher.group());
        }

        if (matches.size() == 0) {
            return;
        }

        Map<String, String> params = new HashMap<String, String>();

        String urlQueryParams = url.getQuery();
        String[] separatedQueryParams = urlQueryParams.split("&");
        for (int i = 0; i < separatedQueryParams.length; i++) {
            String[] queryParam = separatedQueryParams[i].split("=");
            params.put(queryParam[0], queryParam[1]);
            RILogUtils.logError("Query param " + (i + 1) + " with key = " + queryParam[0] + " and value = " + queryParam[1]);
        }

        if (mListener != null) {
            mListener.onHandledOpenUrl(params);
        }
    }
}

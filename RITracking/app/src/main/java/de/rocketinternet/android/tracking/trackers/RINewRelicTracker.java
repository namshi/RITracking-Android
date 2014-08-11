package de.rocketinternet.android.tracking.trackers;

import android.content.Context;
import android.text.TextUtils;

import com.newrelic.agent.android.NewRelic;

import java.util.concurrent.Executors;

import de.rocketinternet.android.tracking.core.RITrackingConfiguration;
import de.rocketinternet.android.tracking.interfaces.RIInteractionTracking;
import de.rocketinternet.android.tracking.interfaces.RIScreenTracking;
import de.rocketinternet.android.tracking.trackers.utils.RITrackersConstants;
import de.rocketinternet.android.tracking.utils.RILogUtils;

/**
 * @author alessandro.balocco
 *         Convenience controller to proxy-pass tracking information to NewRelic
 */
public class RINewRelicTracker extends RITracker implements
        RIScreenTracking,
        RIInteractionTracking {

    private static final String TRACKER_ID = "RINewRelicTrackerID";

    @Override
    public void execute(Runnable runnable) {
        mQueue.execute(runnable);
    }

    @Override
    public String getIdentifier() {
        return mIdentifier;
    }

    @Override
    public boolean initializeTracker(Context context) {
        RILogUtils.logDebug("Initializing New Relic tracker");
        String appToken = RITrackingConfiguration.getInstance().getValueFromKeyMap(RITrackersConstants.NEWRELIC_APP_TOKEN);
        if (!TextUtils.isEmpty(appToken)) {
            createTracker(context, appToken);
            return true;
        } else {
            RILogUtils.logError("New Relic tracker is not needed and will not be initialized. Add the " +
                    "App Token in properties file if you want New Relic to be integrated");
        }
        return false;
    }

    private void createTracker(Context context, String appToken) {
        NewRelic.withApplicationToken(appToken).start(context);
        mQueue = Executors.newFixedThreadPool(NUMBER_OF_CONCURRENT_TASKS);
        mIdentifier = TRACKER_ID;
    }

    @Override
    public void trackScreenWithName(String name) {
        RILogUtils.logDebug("New Relic tracker - Give interactions name: " + name);

        NewRelic.setInteractionName(name);
    }

    @Override
    public String trackStartInteraction(String name) {
        RILogUtils.logDebug("New Relic tracker - Starting interaction with name: " + name);

        return NewRelic.startInteraction(name);
    }

    @Override
    public void trackEndInteraction(String id) {
        RILogUtils.logDebug("New Relic tracker - Stopping interaction with id: " + id);

        NewRelic.endInteraction(id);
    }
}

# RITracking-Android

A Tracking SDK for Android

The SDK can be integrated into Android application projects and provide convenient 
tracking via customizing convenience wrappers in subclasses (i.e. activities). 
The integrated trackers (Google Tag Manager, AD4Push, etc.) are pervasively initialized 
using a central properties list file bundled with the application to contain all required 
keys, token and further configuration.

## Introduction
The library is developed with Android Studio IDE. This means that the structure of the 
code is based on that editor.  

The tracking library supports API level 9 as minimum SDK. This decision was made because 
newer version of Google Play Services provide support starting from that API level to 
enable more powerful functionalities. Users of the library must be sure to select api 
level 9 as minimum SDK version for the application in the gradle.build file to avoid 
compatibility issues.  

Refer to [this link](https://developer.android.com/google/play-services/index.html) for further information about Google Play Services.  

The library is supporting the following trackers at the moment:

*   Google Tag Manager (GTM)
*   Ad4Push
*   AdJust
*   BugSense
*   NewRelic

When the app is launched some information about the device and the launch time are 
collected. These information can be used later for tracking purposes.

## Events

The library will receive specific requests from developers and it will dispatch them to 
registered trackers. 

The ones listed below are the events that the library allows to track:

* ###### Track screens name   
    The screen name is tracked by calling the corresponding method provided by the 
    library.
        
        String screenName = "The name of the screen";
        
        RITracking.getInstance().trackScreenWithName(screenName);

    Developers can decide to send the screen name automatically or request the library to 
handle that by explicitly calling the above method. Both scenarios are described below.  
As written in the __Integration__ paragraph, developers should extend one of the provided 
containers (RITrackingActivity, RITrackingFragment, etc...) for their Activities or 
Fragments.  
    
    By doing that and specifying the screen annotation on top of their classes developers 
will request the library to automatically track their views. It is also working with 
Fragments when a library Fragment class is extended (ex. RITrackingFragment).
    
    The example below show how it's possible to request automatic screen tracking by 
extenting an activity from the library:
    
        @RITrackingScreenAnnotation(screenName = "The name of the screen")
        public class RIMainExampleActivity extends RITrackingActivity {
            /* Activity code */
        }
    
    For the ones that want to track their screen manually, the first method is always available.

* ###### Track generic events
    Generic events are tracked by calling the corresponding method provided by the library.
        
        String eventName = "eventName";
        int eventValue = 0; // <-- optional
        String userAction = "userAction"; // <-- optional
        String appCategory = "appCategory"; // <-- optional
        Map<String, Object> dataMap = new HashMap<String, Object>(); // <-- optional
        
        RITracking.getInstance().trackEvent(eventName, eventValue, userAction, appCategory, dataMap);
    
    In this case it is up to the developers to call it when needed.

* ###### Track users event
    User related events are of 3 types and they are tracked by calling one of the 
corresponding methods provided by the library.  
    
    __User Info__: The first method tracks user information, below an example:
        
        String userEvent = "userEvent";
        Map<String, Object> dataMap = new HashMap<String, Object>();
        
        RITracking.getInstance().trackUserInfo(eventName, eventValue, userAction, appCategory, dataMap);
    
    __Device Info__: The second method is used to update user device information:
        
        Map<String, Object> deviceInfo = new HashMap<String, Object>();
        
        RITracking.getInstance().trackUpdateDeviceInfo(deviceInfo);
        
    __Geolocation__: The third method is used to update user geolocation:
        
        Location location = new Location("Test location");
        location.setLatitude(0.0);
        location.setLongitude(0.0);
        
        RITracking.getInstance().trackUpdateGeoLocation(location);
        
    In this cases it is up to developers to call the methods when needed.

* ###### Track exception
    Handled exceptions can be tracked by calling the corresponding method provided by 
the library.
        
        HashMap<String, String> params = new HashMap<String, String>(); // <-- optional
        Exception exception = new Exception("This is an exception");

        RITracking.getInstance().trackEvent(eventName, eventValue, userAction, appCategory, dataMap);
    
    In this case it is up to developers to call the method when needed.

* ###### Track e-commerce events 
    E-commerce related events and actions are of 3 types and they are tracked by calling 
the corresponding methods provided by the library. Tracking e-commerce events requests 
also developer to use library models. Check the __Models__ section to have a better
understanding of the models used by the library
    
    __Checkout__: The first method tracks a checkout action using a RITrackingTransaction 
to contains all the useful information.  

        RITrackingTransaction transaction = new RITrackingTransaction();

        RITracking.getInstance().trackCheckoutTransaction(transaction);

    __Add product to cart__: The second method tracks when a product is added to the 
customer cart.

        RITrackingProduct product = new RITrackingProduct();
        String cartId = "The id of the cart";
        String location = "Product detail"; // The location from where the product was added
        
        RITracking.getInstance().trackAddProductToCart(product, cartId, location);

    __Remove product from cart__: The third method tracks when a product is removed
from the customer cart.

        RITrackingProduct product = new RITrackingProduct();
        int quantity = 1;           // The removed quantity
        double cartValue = 32.50;    // The value of the cart before removal
        
        RITracking.getInstance().trackRemoveProductFromCart(product, quantity, cartValue);
        
* ###### Track interactions  
    Interactions can be tracked for example before a method, defining it as a starting 
point for an interaction. They are used by New Relic tracker. Check the dedicated section 
for more information. Interactions are tracked by calling the corresponding methods 
provided by the library.
    
    __Start interaction__: The first method tracks when an interaction is started. This 
method returns a String representing the id of the interaction and that can be used to 
stop it prematurely.

        String name = "The name of the interaction";

        RITracking.getInstance().trackStartInteraction(name);
        
    __End interaction__: The second method tracks when an interaction is ended. This 
method needs the id of the interaction that should be ended.

        String id = "The id of the interaction";

        RITracking.getInstance().trackEndInteraction(String id);

* ###### Track network interactions
    Network interactions are used to track network request and possible network failures. 
They are used by New Relic tracker. Check the dedicated section for more information. 
Network interactions are tracked by calling the corresponding methods provided by the 
library.
    
    __Network transaction__: The first method tracks HTTP transactions with several available levels of detail.

        String url = "http://www.testUrl.com";
        int statusCode = 200;
        long startTime = System.currentTimeMillis(); // When request is fired
        long endTime = System.currentTimeMillis(); // When response is received
        long bytesSent = 55555 // The amount of sent bytes
        long bytesReceived = 22222 // The amount of received bytes
        String responseBody = "The response body" // This is optional
        Map<String, String> params = new Hashmap<String, String>(); // This is optional

        RITracking.getInstance().trackHttpTransaction(url, statusCode, startTime, endTime,
                        bytesSent, bytesReceived, responseBody, params);
    
    __Network failures__: The second method tracks network failures.

        String url = "http://www.testUrl.com";
        long startTime = System.currentTimeMillis(); // When request is fired
        long endTime = System.currentTimeMillis(); // When response is received
        Exception exception = new Exception("The caught exception");
        NetworkFailure failure = NetworkFailure.Unknown;

        RITracking.getInstance().trackNetworkFailure(url, startTime, endTime, exception, failure);

Continue reading this guide to have a better understanding about which events every 
tracker is supposed to track and which are the parameters that each one is expecting for 
them.

## Models

The library makes use of custom models in order to provide different kind of information
required by different trackers.

#### RITrackingTransaction

This model describes a transaction that happens when the user of the app requests a 
checkout action. It is composed by the following fields:

* String __transactionId__ | _(The id of the transaction)_
* String __affiliation__ | _(The transaction affiliation)_
* RITrackingPaymentMethod __paymentMethod__ | _(The payment method used for this transaction)_
* float __voucherAmount__ | _(The voucher amount)_
* int __numberOfPreviousPurchases__ | _(The number of previous purchases by the user or 0 if this is a new user)_
* RITrackingTotal __total__ | _(The total information of the transaction)_
* List<RITrackingProduct> __productsList__ | _(The list of the products that are ready to be purchased)_

#### RITrackingTotal

This model describes the total amount for a certain order containing further 
information as follow:

* double __net__ | _(The net of the order)_
* float __tax__ | _(The tax of the order)_
* int __shipping__ | _(The shipping price of the order)_
* String __currency__ | _(The currency used for the order)_

#### RITrackingProduct

This model describes a product involved in an operation. Product is described using these
parameters:

* String __identifier__ | _(The id of the product)_
* String __name__ | _(The name of the product)_
* int __quantity__ | _(The quantity of the product)_
* double __price__ | _(The price of the product)_
* String __currency__ | _(The currency of the product price)_
* String __category__ | _(The category of the product)_
* String __subCategory__ | _(The sub-category of the product)_
* String __brand__ | _(The brand of the product)_
* int __mDiscount__ | _(The percentage of discount of this product)_
* float __averageRating__ | _(The average rating total value)_

## Basic integration

#### Setup

The library automatically initializes all the trackers which are correctly setup in 
manifest and properties file. Integrating the library requires the users to follow these 
steps:

1.  Application class should extend library Application class:
        
        public class MyApplication extends RITrackingApplication {
            /* ... */
        }

2.  Copy the properties file, that is located the library assets folder, into the 
application assets folder

        ri_tracking_config.properties

3.  For activities it is necessary to extends one of the ones provided by the library if 
Ad4Push needs to be implemented. If it is not needed it's worth anyway to use these 
classes because they provide automatic screen tracking functionalities as describe in 
__Events__ paragraph.  
    
    The tracking library also provides Fragment classes that developers can extend to 
have auto screen tracking using annotations (as describe in __Events__ paragraph). 
    
    Supported activities and fragments:
    *   __RITrackingActivity__: it extends Activity.
    *   __RITrackingSplashActivity__: it extends Activity (check Ad4Push paragraph for other insights).
    *   __RITrackingActionBarActivity__: it extends ActionBarActivity.
    *   __RITrackingListActivity__: it extends ListActivity.
    *   ___...___
    *   ___in development, trying to offers support for all use cases___
    *   ___...___
    *   __RITrackingFragment__: it extends Fragment.
    *   __RITrackingListFragment__: it extends ListFragment.
    *   __RITrackingFragmentSupport__: it extends Fragment class from android support library.
    *   __RITrackingListFragmentSupport__: it extends ListFragment class from android support library.

#### Deep-Links
The library simplifies deep-linking filtering and definition. If the application is 
interested in receiving and handling deep-links these are the steps to follow:  

1.  Create an Activity that extends RITrackingDeepLinkingActivity:
        
        public class RISampleDeepLinkingActivity extends RITrackingDeepLinkingActivity {
            /*...*/
        }

    By doing that the IDE will require this class to implement the method for registering
    handlers for deep-links:
        
        @Override
        protected void registerHandlers() {
            // Register Handlers for intercepting deep-links
            RITracking.getInstance().registerHandler("IDENTIFIER", "HOST", "PATH", this);
        }  
    
    Finally let the class implements RIOnHandledOpenUrl in order to receive callbacks from
the handlers:
    
        public class RISampleDeepLinkingActivity extends RITrackingDeepLinkingActivity implements RIOnHandledOpenUrl {
            
            /*...*/
            
            @Override
            public void onHandledOpenUrl(String identifier, Map<String, String> params) {
                // Handle the eventual callback
                if (identifier.equals("IDENTIFIER")) {
                    // Handle callback from handler and use params to accomplish expected action
                }
            }
        }
        
2.  Declare activity in the AndroidManifest of the application with expected filters:
        
        <activity
            android:name="your.package.name.RISampleDeepLinkingActivity"
            android:label="@string/app_name" >
            <intent-filter >
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
                <!-- Accepts URIs that begin with "scheme://”. Change with expected scheme -->
                <data android:scheme="scheme" />
            </intent-filter>
        </activity>

Continue reading the __Tracker setup__ paragraph to find instructions on how to integrate 
single trackers.

## Trackers setup

Integration differs for every single tracker. Below are provided specific informations for
everyone of them, together with an overview of each tracker and the events that each one 
is expected to track.

### Google Tag Manager  
#### Overview
Google Tag Manager is meant to track _events_, _user events_, _screens_ and _e-commerce events_. 
This tracker after successfully retrieving the application container, will push 
all the informations and the events to a so called __Data Layer__ that will automatically 
match and sync with the web platform.  

For more information and Google Tag Manager official documentation click on [this link](https://developers.google.com/tag-manager/android/v4/). 

#### Integration
Integrating Google Tag Manager tracker requires the following steps: 

1.  Update the _RIGoogleTagManagerContainerID_ in the properties file with the right 
container ID
        
        RIGoogleTagManagerContainerID=GTM-******

2. Copy the gtm_container binary file corresponding to the expected container in the raw 
folder of the project.

3. Check AndroidManifest.xml of the library project and copy the parts related to GTM 
being sure to add the correct package name where needed.

### Ad4Push 
#### Overview
Ad4Push tracker is meant to track _events_, _user events_, _screens_ and _e-commerce events_. 
This tracker will be initialized using a private key, a partner key and a sender 
id from Google.

For more information and Ad4Push official documentation click on [this link](http://www.ad4screen.com/DocSDK/doku.php). 

#### Integration
Integrating Ad4Push tracker requires the following steps: 

1.  Update the _RIAd4PushIntegration_ flag in the properties file with a value of true or 
false depending on the app needs.
        
        RIAd4PushIntegration=true    // in this case the library will try to initialize the tracker

2. Check AndroidManifest.xml of the library project and copy the parts related to Ad4Push 
being sure to add the correct package name where needed and the "partner key", 
"private key" and "sender id" to the Ad4Push Service.

3. If you want to listen and intercept callbacks from push notification there is a class 
called RISamplePushNotificationReceiver that can be used as an example for that purpose. 
Be sure to add the receiver to the manifest. Down below the code of that sample class:
        
        public class RISamplePushNotificationReceiver extends BroadcastReceiver {
        
            public static final String INTENT_ACTION_DISPLAYED = "com.ad4screen.sdk.intent.action.DISPLAYED";
            public static final String INTENT_ACTION_CLICKED = "com.ad4screen.sdk.intent.action.CLICKED";
        
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                Bundle bundle = intent.getExtras();
                handleEvent(context, action, bundle);
            }
            
            private void handleEvent(Context context, String action, Bundle extras) {
                if (extras != null && !TextUtils.isEmpty(action)) {
                    if (action.equals(INTENT_ACTION_DISPLAYED)) {} 
                    else if (action.equals(INTENT_ACTION_CLICKED)) {}
                }
            }
        }

4. If the application requires to show a splash screen it is recommended to extends 
__RITrackingSplashActivity__ that will lock push notifications until the splash is 
dismissed accordingly to [Ad4Push documentation](http://www.ad4screen.com/DocSDK/doku.php?id=troubleshooting#splashscreen)

### AdJust 
#### Overview
AdJust tracker is meant to track _events_ and _e-commerce events_. 
This tracker will be initialized using an AppToken in the AndroidManifest. The tracker 
will start tracking session automatically by extending one of the container provided with 
the library and described in the __Basic Integration - Setup__ paragraph.

For more information and AdJust official documentation click on [this link](https://github.com/adjust/android_sdk). 

#### Integration
Integrating AdJust tracker requires the following steps: 

1.  Update the _RIAdJustIntegration_ flag in the properties file with a value of true or 
false depending on the app needs.
        
        RIAdJustIntegration=true    // in this case the library will try to initialize the tracker

2. Check AndroidManifest.xml of the library project and copy the parts related to AdJust 
being sure to add the correct AppToken for your application.

### BugSense 
#### Overview
BugSense tracker is meant to track _exceptions_ and leaving breadcrumbs that in this case
will correspond to the _screen name_. This tracker will be initialized using an ApiKey 
from the properties file. The tracker will start tracking session automatically when app 
starts and by extending one of the container provided with the library, and described in 
the __Basic Integration - Setup__ paragraph, developers can automatically track activity session.

For more information and BugSense official documentation click on [this link](https://www.bugsense.com/docs/android). 

#### Integration
Integrating BugSense tracker requires the following steps: 

1.  Update the _RIGoogleTagManagerContainerID_ in the properties file with the right 
Api Key
        
        RIBugSenseApiKey=YourApiKey

### NewRelic 
#### Overview
New Relic tracker is meant to track _interactions_, _network requests_ and 
_network failures_. When activities with name are set up, a group of interactions with the
same name will be created by the tracker. It will be initialized using an App Token 
from the properties file. 

For more information and New Relic official documentation click on [this link](https://docs.newrelic.com/docs/mobile-monitoring/mobile-sdk-api/new-relic-mobile-sdk-api/working-android-sdk-api). 

#### Integration
Integrating New Relic tracker requires the following steps: 

1.  Update the _RINewRelicAppToken_ in the properties file with the right 
App Token
        
        RINewRelicAppToken=YourAppToken

2.  If you want to trace a certain method with the usual @Trace annotation, you can do 
that directly as described in the documentation that you can find by clicking on the link 
provided above.

## License

The MIT License (MIT)

Copyright (c) 2014 Martin Biermann

Permission is hereby granted, free of charge, to any person obtaining a copy of this 
software and associated documentation files (the "Software"), to deal in the Software 
without restriction, including without limitation the rights to use, copy, modify, merge, 
publish, distribute, sub-license, and/or sell copies of the Software, and to permit 
persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or 
substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE 
FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR 
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
DEALINGS IN THE SOFTWARE.
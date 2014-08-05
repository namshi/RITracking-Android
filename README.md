# RITracking-Android

A Tracking SDK for Android

The SDK can be integrated into Android application projects and provide 
convenient tracking via customizing convenience wrappers in subclasses 
(i.e. activities). The integrated trackers (Google Tag Manager, AD4Push, etc.) 
are pervasively initialized using a central property list file bundled with the 
application to contain all required keys, token and further configuration. 
The tracking interface is initialized using a file path reference to the 
property list file.

## Introduction  

The library is developed with Android Studio as IDE. This means that the structure of the
code is based of that editor.  

The tracking library supports API level 9 as minimum SDK. This decision was made 
because newer version of Google Play Services provide support starting from that 
API level to enable more powerful functionalities. Users should be sure to select 
api level 9 as minimum SDK version for the application in the manifest to avoid 
problems.  

Refer to [this link](https://developer.android.com/google/play-services/index.html) for 
further information about Google Play Services.  

The library is supporting the following trackers at the moment:
*   Google Tag Manager (GTM)
*   Ad4Push ___<-- in development___
*   AdJust  ___<-- in development___
*   BugSense ___<-- in development___
*   NewRelic ___<-- in development___

When the app is launched some information about the device and the launch time are collected.
These information can be used later for tracking issues.

## Events

The library is in charge of receive specific requests from developers and dispatching them to
registered trackers. 

The ones listed below are the events that the libray allows to track:
* ###### Track screens name 
    The screen name is tracked by calling the corresponding method provided by the library.
        
        String screenName = "The name of the screen";
        
        RITracking.getInstance().trackScreenWithName(screenName);
    Developers can decide to both decide to send the screen name automatically or handle 
    that by explicitly calling the above method and both scenarios are descibed below.  
    As it is written in the __Integration__ paragraph developers should extend one of the 
    provided containers (RITrackingActivity, RITrackingFragment, ecc...) for their Activities
    or Fragments.  
    
    By doing that and specifying the screen annotation on top of their class they will 
    request the library to automatically track their view. It is also working with Fragments
    when a library Fragment class is extended (ex. RITrackingFragment).
    
    The example below show how it's possible to request automatic screen tracking using activity:
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
    In this case it is up to the user to call the method when needed.

* ###### Track users event
    User related events are tracked by calling the corresponding method provided by the library.
        String userEvent = "userEvent";
        Map<String, Object> dataMap = new HashMap<String, Object>();
        
        RITracking.getInstance().trackUser(eventName, eventValue, userAction, appCategory, dataMap);
    In this case it is up to the user to call the method when needed.

* ###### Track exception
    ___in development___

* ###### Track e-commerce events 
    ___in development___

Continue reading this guide to have a better understanding which events every tracker is supposed to
track and which are the parameters that each one is expecting for them

## Basic integration

#### Setup

The library automatically initializes all the trackers which are correctly setup
in manifest and properties file.  
Integrating the library requires the users to follow these steps:
1.  Application class should extend library Application class:
        
        public class YourApplication extends RITrackingApplication {
            /* ... */
        }

2.  Copy the properties file in the library assets folder into the application assets folder
        ri_tracking_config.properties

3.  For activities it is necessary to extends one of the ones provided by the library if Ad4Push 
    needs to be implemented. If it is not needed it's worth anyway to use these classes because they 
    provide automatic screen tracking functionalities as describe in __Events__ paragraph.  
    
    The tracking library also provides Fragment classes that developers can extend to 
    have auto screen tracking using annotations (as describe in __Events__ paragraph). 
    
    Supported activities and fragments:
    *   __RITrackingActivity__: it extends Activity  
    *   ___in development to add more classes___  
    *   
    *   
    *   __RITrackingFragment__: it extends Fragment
    *   __RITrackingFragmentSupport__: it extends Fragment class from support library.

#### Deep-Links
The library simplifies deep-linking filtering and definition. If the application is interested in
receving and handling deep-links these are the steps to follow:
1.  Create an Activity that extends RIDeepLinkingActivity:
        
        public class RIDeepLinkingExampleActivity extends RIDeepLinkingActivity {
            /*...*/
        }
    
    By doing that the IDE will require this class to implement the method for registering
    handlers for deep-links:
        
        @Override
        protected void registerHandlers() {
            // Register Handlers for intercepting deep-links
            RITracking.getInstance().registerHandler("IDENTIFIER", "HOST", "PATH", this);
        }
    
    Finally make the class implements RIOnHandledOpenUrl to get callback from handlers:
        
        public class RIDeepLinkingExampleActivity extends RIDeepLinkingActivity implements RIOnHandledOpenUrl {
            
            /*...*/
            
            @Override
            public void onHandledOpenUrl(String identifier, Map<String, String> params) {
                // Handle the eventual callback
                if (identifier.equals("IDENTIFIER")) {
                    // Handle callback from handler and use params to accomplish expected action
                }
            }
        }

2.  Declare activity in the AndroidManifest of the application with expected filter:
        
        <activity
            android:name="your.package.name.RIDeepLinkingExampleActivity"
            android:label="@string/app_name" >
            <intent-filter >
                <action android:name="android.intent.action.VIEW" />
                <category android:name="android.intent.category.DEFAULT" />
                <category android:name="android.intent.category.BROWSABLE" />
                <!-- Accepts URIs that begin with "scheme://”. Change with expected scheme -->
                <data android:scheme="scheme" />
            </intent-filter>
        </activity>

Continue reading the __Tracker setup__ paragraph to find instruction on how to integrate 
single trackers.

## Trackers setup

Intregation differs for every single tracker. Below are provided specific information for everyone
of them, together with an overview of each tracker and the events that each one is expected to track.

### Google Tag Manager  
#### Overview
Google Tag Manager is meant to track _events_, _userEvents_, _screens_ and _e-commerce events_. This
tracker after successfully retriving the application container, will push all the information 
and the event to a so called __Data Layer__ that will automatically match and sync with the web
platform.  

For more information and official documentation 
[this link](https://developers.google.com/tag-manager/android/v4/) is the way to go. 

#### Integration
Integrating Google Tag Manager tracker will require the following steps: 

1.  Update the _RIGoogleTagManagerContainerID_ in the properties file with the right container ID
        
        RIGoogleTagManagerContainerID=GTM-******

2. Copy the gtm_container binary file corresponding to the expected container in the raw folder 
of the project.

3. Check AndroidManifest.xml of the library project and copy the parts related to GTM being sure to
add the correct package name where needed.

### Ad4Push 
___in development___

### AdJust 
___in development___

### BugSense 
___in development___

### NewRelic 
___in development___

## License

The MIT License (MIT)

Copyright (c) 2014 Martin Biermann

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
asi-estimator
=============

Dependencies
------------

 - Java 1.6
 - Google Web Toolkit SDK 2.4.0
 - Google App Engine SDK 1.7.0
 - GWT Visualization API 1.1.2
 - GWT Maps API 1.1.1
 
Build Instructions
------------------

This project can be built with Eclipse or Ant.

If you are using ant to build the project, make sure that there is a folder called gwt-2.4.0 and a folder called appengine-java-sdk in the same folder as the asi-estimator folder.  The directory structure should look something like this:

		./
			asi-estimator/
				lib/
				src/
				build.xml
				...
			appengine-sdk-java-1.7.0/
				bin/
				config/
				lib/
				...
			gwt-2.4.0/
				doc/
				gwt-dev.jar
				gwt-user.jar
				...

Links to Dependencies
---------------------
[appengine-sdk-java-1.7.0](https://developers.google.com/appengine/downloads#Google_App_Engine_SDK_for_Java)
[gwt-2.4.0](http://code.google.com/p/google-web-toolkit/downloads/detail?name=gwt-2.4.0.zip).
[gwt-visualization-1.1.2](http://code.google.com/p/gwt-google-apis/downloads/detail?name=gwt-visualization-1.1.2.zip&can=2&q=)
[gwt-maps-1.1.1](http://gwt-google-apis.googlecode.com/files/gwt-maps-1.1.1-rc1.zip)

Inside the asi-estimator folder, run `ant build` to build the project, and `ant test` to run the unit tests.


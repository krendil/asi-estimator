asi-estimator
=============

Dependencies
------------

 - Java 1.6
 - Google Web Toolkit SDK 2.4.0
 - Google App Engine SDK 1.7.0
 
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

You can find a zip file of appengine-sdk-java-1.7.0 [here](https://developers.google.com/appengine/downloads#Google_App_Engine_SDK_for_Java) and gwt-2.4.0 [here](http://code.google.com/p/google-web-toolkit/downloads/detail?name=gwt-2.4.0.zip).

Inside the asi-estimator folder, run `ant build` to build the project, and `ant test` to run the unit tests.


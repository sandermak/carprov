### Example code for 'Provisioning the IoT'

This repo contains a sample application that is used for the presentation 'Provisioning the IoT' by Paul Bakker and Sander Mak. Slides for this talk can be found [here](tbd).

The application itself is a small dashboard with apps that could run as an in-car entertainment system:

![The dashboard with some apps](http://i.imgur.com/yji2wZih.png)

## Technology
The sample app is created with a JavaFX user-interface. Under the hood, OSGi is used to create separate modules for the dashboard and each of the apps. In our demo, we deploy these bundles in a modular fashion to targets using [Apache ACE](https://ace.apache.org).

## Building and running the code

You need a Java 8 JDK installed to build the code. There are two ways two build:

- offline, using Gradle: ```./gradlew build```
- interactive, by importing the projects into an Eclipse installation with the [BndTools plugin](http://bndtools.org/installation.html) installed

In both cases, the bundles end up in the ```generated``` folders of the subprojects. 

You can run the sample by right-clicking ```carprov.dashboard/carprov.bndrun``` in Eclipse and selecting 'Run as -> Bnd OSGi Run Launcher'. Alternatively you can create a runnable jar containing all modules. Do this with ```./gradlew export``` and start the exported jar directly: ```java -jar carprov.dashboard/generated/distributions/executable/carprov.jar```

## Demo scenario
...


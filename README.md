iJava
=====

This project implements the iJava translation system.

Build Instructions
------------------

This project uses the Gradle build system, which does not
need to be installed in order to build the system. 

First, ensure that you have the Java 8 SDK installed
and that JAVA_HOME is set to the location of the SDK.

Then, execute the following to build:

Windows: **gradlew installDist**

Linux: **bash gradlew installDist**

The gradlew wrapper script will download the Gradle
build system and build.

Run Instructions
----------------

Navigate to the build/install/ijava/bin folder and
execute:

ijava *language* path/to/java/source/file.java

where *language* is **cpp** or **python**

Development Instructions
------------------------

To generate an Eclipse project file, execute:

**gradlew eclipse**

**Basic Project Setup**

Required:
  1. [Maven installed](http://maven.apache.org/)
  1. Eclipse [Helios](http://www.eclipse.org/downloads/packages/eclipse-ide-java-developers/heliossr2) or [Indigo](http://www.eclipse.org/downloads/packages/eclipse-ide-java-developers/indigosr1)
  1. [The Sonatype maven plugin for Eclipse](http://eclipse.org/m2e/)
  1. [The Subversion svn plugin for Eclipse](http://www.eclipse.org/subversive/downloads.php)

First check out the multiplicity project from the branch.

Go to http://tel.dur.ac.uk/artifactory/webapp/mavensettings.html and generate the settings.xml file which you'll need to put in ~/.m2/

Check out multiplicity3-parent into eclipse as a general (non-Java) project.  In eclipse go to file > import and select maven > Existing Maven Projects.  Ensure all the projects contained in multiplicity3-parent are selected and click finish.


**To Create a Jar Deployment**

Ensure that any src/main/resources or src/test/resources folders on a project’s classpath do not have entries in their excluded fields. Right click the project and select Export > Java > Runnable jar file.
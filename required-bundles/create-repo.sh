#/bin/bash

WORKSPACE=`pwd`

ECLIPSE_HOME=/Users/mlippert/Entwicklung/vmware/ide/sts260-64/sts-2.6.0.RELEASE
LAUNCHER_JAR=`ls $ECLIPSE_HOME/plugins/org.eclipse.equinox.launcher_*`

rm $WORKSPACE/repo/content.jar $WORKSPACE/repo/artifacts.jar

$JAVA_HOME/bin/java -jar $LAUNCHER_JAR -consolelog -nosplash -verbose \
	-application org.eclipse.equinox.p2.publisher.FeaturesAndBundlesPublisher \
	-metadataRepository file:/$WORKSPACE/repo -artifactRepository file:/$WORKSPACE/repo \
	-source $WORKSPACE/repo -publishArtifacts -compress -configs gtk.linux.x86
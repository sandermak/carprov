rm -rf ./felix-cache
java \
-Dorg.apache.felix.deploymentadmin.stopunaffectedbundle=false \
-Dagent.identification.agentid=car2 \
-Dagent.discovery.serverurls=http://10.0.1.200:8080 \
-Dagent.controller.syncinterval=2 \
-Dagent.controller.syncdelay=3 \
-jar ace-launcher.jar -v -c car.conf
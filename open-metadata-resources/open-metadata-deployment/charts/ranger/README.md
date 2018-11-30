<!-- SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
**Simple chart for ranger & mariaDB**

How to install ranger using this chart

* Install Kubernetes/start
* 'helm install ranger'
* Wait for ranger to start (bootstrap takes up to a minute)
* Go to http://localhost:6080

Note - this assumes the default use of the 'LoadBalancer' service with the ability to expose port 6080. Public cloud 
services tend to restrict ports to the ephemeral range, refer to cloud docs for detail or modify the service 
configuration

**todos**
 - could make this chart depend on a mariadb chart rather than include it
 - need to figure out where to build, and then host the required docker image(s)
 - other charts may need to include this chart
 - Multiple instances won't work correctly as port numbers are hardcoded
 - The ranger docker image hardcodes a hostname 'mariadb' which it connects to on startup - need to inject this 
 configuration for proper multi instance support
 - more ports are exposed than may be needed
 - Passwords should be pulled out to configuration (this affects ranger image itself too)
 - Look at contributing this to the Ranger project
 - Move non ranger specific charts elsewhere (ie see http://github.com/odpi/egeria)

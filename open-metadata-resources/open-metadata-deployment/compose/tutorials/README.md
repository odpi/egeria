<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Docker Compose script to support Egeria labs and tutorials

This image is intended to support on-site labs, tutorials. 

Rather than having to install Egeria, prerequisites and tools separately, these scripts make it easy
to get a stack running quickly.

This has much reduced function than Egeria's [kubernetes support](../../charts) via Helm which is a better
option for real-world coding and deployment. 
It is therefore recommended you consider switching to kubernetes as it offers us more flexibility.
See [https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/charts/odpi-egeria-lab](https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/charts/odpi-egeria-lab).

## Contents

Components included in the docker compose scripts are:
 * Multiple egeria images -- this uses the latest docker egeria image, as published to [Docker Hub](https://hub.docker.com/search?q=egeria&type=image)
   - Core [OMAG server platform](../../../../open-metadata-implementation/admin-services/docs/concepts/omag-server-platform.md) - core:9443 internally, localhost:19443 externally
   - Data Lake OMAG server platform   - datalake:9443 internally, localhost:19444 externally 
   - Development OMAG server platform - dev:9443 internally, localhost:19445 externally
   - Factory OMAG server platform    - factory:9443 internally, localhost:19446 externally
   - [UI Platform](../../../../open-metadata-implementation/user-interfaces)   - ui:8443 internally (https), localhost:18443 externally (https)
   - UI Static content     - staticui:80 internally (https), localhost:10080 externally (https)
   - React UI's [Presentation server](../../../../open-metadata-implementation/admin-services/docs/concepts/presentation-server.md)       - presentation:8091 internally (https), localhost:18091 externally (https) 
 * [nginx](https://nginx.org/en/)  - nginx:443 internally (https), localhost:10443 externally (https)
 * [Apache Kafka](../../../../developer-resources/tools/Apache-Kafka.md) - kafka:9092 internally, localhost:19092 externally - standard Bitnami image
 * [Apache Zookeeper](../../../../developer-resources/tools/Apache-Kafka.md) - zookeeper:2181 internally, localhost:12181 externally- standard Bitnami image
 * Jupyter notebooks - notebook:8888 internally, localhost:18888 externally (lab version, base image) - see https://jupyter-docker-stacks.readthedocs.io/en/latest/

## Usage

 - Docker & Docker compose must be installed - see https://docs.docker.com/install/
 - Configure docker with at least 4GB memory
 - Ensure you have access to both 'egeria-tutorial.yaml' and the 'notebook-start.d' subdirectory 
 which contains an initialization script 'getnotebooks.sh'. You can either run directly from a git 
 clone, or download the files individually
 - To start the environment `docker-compose -f ./egeria-tutorial.yaml up`
 - you will notice all the components starting. As the notebook server starts it will also load
   the latest notebooks we have available directly from git.
 - go to http://localhost:18888 to interact with the Jupyter Notebook
 - use https://localhost:18091 to interact with the React UI (Tex, Rex, Dino)
 - use https://localhost:10443 to interact with the Polymer UI (this is a proxy that uses uistatic & ui for content)
 - To stop the environment : `docker-compose -f ./egeria-tutorial.yaml down`
 - To refresh the images (recommended to pick up latest code) : `docker-compose -f ./egeria-tutorial.yaml pull`
 
 ## Checking what is running
 
 Use the docker command to check containers are running. You should have
 * 1 Jupyter container to run the notebooks
 * 4 Egeria containers
 * Kafka and Zookeeper containers for messaging
 
 for example:
 ```
$ docker ps                                                                                                                                                                                                                                                                                                                            [10:43:05]
CONTAINER ID        IMAGE                       COMMAND                  CREATED             STATUS              PORTS                                                             NAMES
668a0cb4c603        odpi/jupyter:2.10-SNAPSHOT   "tini -g -- start.sh…"   4 minutes ago       Up 4 minutes        0.0.0.0:18888->8888/tcp                                           tutorials_notebook_1
dd0061d76740        odpi/egeria:2.10-SNAPSHOT    "/usr/local/s2i/run"     4 minutes ago       Up 4 minutes        5005/tcp, 8080/tcp, 8443/tcp, 8778/tcp, 0.0.0.0:19446->9443/tcp   tutorials_factory_1
b4529b10242e        odpi/egeria:2.10-SNAPSHOT    "/usr/local/s2i/run"     4 minutes ago       Up 4 minutes        5005/tcp, 8080/tcp, 8443/tcp, 8778/tcp, 0.0.0.0:19443->9443/tcp   tutorials_core_1
9ca6fac8eeb4        odpi/egeria:2.10-SNAPSHOT    "/usr/local/s2i/run"     4 minutes ago       Up 4 minutes        5005/tcp, 8080/tcp, 8443/tcp, 8778/tcp, 0.0.0.0:19445->9443/tcp   tutorials_dev_1
edd729d03841        odpi/egeria:2.10-SNAPSHOT    "/usr/local/s2i/run"     4 minutes ago       Up 4 minutes        5005/tcp, 8080/tcp, 8443/tcp, 8778/tcp, 0.0.0.0:19444->9443/tcp   tutorials_datalake_1
ea3884c6ceb8        bitnami/kafka:latest        "/opt/bitnami/script…"   4 minutes ago       Up 4 minutes        0.0.0.0:19092->9092/tcp                                           tutorials_kafka_1
b076f6ddc67c        bitnami/zookeeper:latest    "/opt/bitnami/script…"   4 minutes ago       Up 4 minutes        2888/tcp, 3888/tcp, 8080/tcp, 0.0.0.0:12181->2181/tcp             tutorials_zookeeper_1

```

If you don't see all the containers running, try the instructions under 'usage' above to re-pull the images, and stop/start
the containers. 

If this still fails, check the logs (below) and also consider if there is enough memory for docker. Finally follow any 
debugging guides for the container environment itself (outside the scope of this readme) in case of some system
issue or incompatibility.

 ## Logging
 
 Docker compose will normally log the output from all containers in the terminal where you launched the command
 to start. This can make seeing egeria audit login messages difficult, especially as the kafka and zookeeper
 images we use perform lots of debugging
 
 To look at the logs of individual containers, use the `docker log` command and either the container id, or name from the list 
 of running containers. See the docker documentation for more detail on this command.
 
 For example, to see the state of the core container use:
 ```
$ docker logs tutorials_core_1                                                                                                                                                                                                                                                                                                         [10:43:39]
/usr/local/s2i/run: line 15: /opt/jboss/container/maven/default//scl-enable-maven: No such file or directory
Starting the Java application using /opt/jboss/container/java/run/run-java.sh ...
INFO exec  java -XX:+UseParallelOldGC -XX:MinHeapFreeRatio=10 -XX:MaxHeapFreeRatio=20 -XX:GCTimeRatio=4 -XX:AdaptiveSizePolicyWeight=90 -XX:MaxMetaspaceSize=100m -XX:+ExitOnOutOfMemoryError -cp "." -jar /deployments/server/server-chassis-spring-2.10-SNAPSHOT.jar  
 ODPi Egeria
    ____   __  ___ ___    ______   _____                                 ____   _         _     ___
   / __ \ /  |/  //   |  / ____/  / ___/ ___   ____ _   __ ___   ____   / _  \ / / __    / /  / _ /__   ____ _  _
  / / / // /|_/ // /| | / / __    \__ \ / _ \ / __/| | / // _ \ / __/  / /_/ // //   |  / _\ / /_ /  | /  _// || |
 / /_/ // /  / // ___ |/ /_/ /   ___/ //  __// /   | |/ //  __// /    /  __ // // /  \ / /_ /  _// / // /  / / / /
 \____//_/  /_//_/  |_|\____/   /____/ \___//_/    |___/ \___//_/    /_/    /_/ \__/\//___//_/   \__//_/  /_/ /_/

 :: Powered by Spring Boot (v2.3.3.RELEASE) ::

09:39:23.811 [main] INFO  o.s.b.w.e.tomcat.TomcatWebServer - Tomcat initialized with port(s): 9443 (https)
09:39:51.619 [main] INFO  o.s.b.w.e.tomcat.TomcatWebServer - Tomcat started on port(s): 9443 (https) with context path ''

Mon Sep 21 09:39:29 GMT 2020 No OMAG servers listed in startup configuration
Mon Sep 21 09:39:51 GMT 2020 OMAG server platform ready for more configuration
```
It is much easier to see any audit log messages produced - for example if you run this command you will see messages that
confirm which other cohort members are communicating with that server, the subsystems that are running
and the events that are being exchanged.  For example:

```
Mon Sep 21 09:49:00 GMT 2020 cocoMDS5 Startup OMRS-AUDIT-0032 The local repository outbound event manager is sending out the 548 type definition events that were generated and buffered during server initialization
Mon Sep 21 09:49:00 GMT 2020 cocoMDS2 Information OMRS-AUDIT-0132 A new remote connector from server cocoMDS3 with metadata collection id 9d524bcf-e62a-4e42-8c41-ed6426387b05 has been deployed to enterprise connector for Project Management OMAS
Mon Sep 21 09:49:00 GMT 2020 cocoMDS2 Information OMRS-AUDIT-0130 The enterprise repository services are managing federated queries to the following list of servers: [cocoMDS3]
Mon Sep 21 09:49:00 GMT 2020 cocoMDS2 Cohort OMRS-AUDIT-0110 A new registration request has been received for cohort cocoCohort from server cocoMDS3 that hosts metadata collection 9d524bcf-e62a-4e42-8c41-ed6426387b05
```
This can be quite verbose when the servers start up, since a lot of type information is exchanged between the servers. 

To follow 'in real time' you can use `docker logs -f <container>` (-f for 'follow')

## Persistence
By default, persistence is disabled. However in the egeria-tutorial.yaml file you can uncomment the required 
definitions for volumes, and their use. 

Note that if you do this, and allow docker to automatically create volumes, they are bound to the lifecycle imposed by docker-compose. This means that when you issue the 'docker-compose -f ./egeria-tutorial.yaml down' they are deleted. To avoid this you will need to manually create the volumes and change their definition to external.

To do this edit egeria-tutorial.yaml and change `external: false` to `external: true`.

Then manually create each volume ie `docker volume create datalake-data` before running `docker-compose -f ./egeria-tutorial.yaml up`. Now when you subsequently do the down, the volumes will be left behind. Use `docker volume ls` to list & `docker volume rm <id>` to delete.

The Egeria notebook environment uses the in-memory repository by default, so you will also need to switch this to write data to these volumes:

Set the value of 'repositoryType' to 'local-graph-repository' in the .env file to use the graph repository.

 ### Using the environment to extend notebooks or develop new ones
 
  - If you are using a notebook written to assume 'localhost:9443' or similar, replace with the following fragment. This will use the correct defaults for the environment (k8s or compose), or localhost if these are not yet. :
  corePlatformURL     = os.environ.get('corePlatformURL','https://localhost:9443')
  dataLakePlatformURL = os.environ.get('dataLakePlatformURL','https://localhost:9444')
  devPlatformURL      = os.environ.get('devPlatformURL','https://localhost:9445')
  factoryPlatformURL  = os.environ.get('factoryPlatformURL','https://localhost:9446')
 - The notebooks downloaded from git are refreshed on each start. Ensure any modifications to notebooks are saved elsewhere




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

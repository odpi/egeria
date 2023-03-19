<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Egeria Logo](https://github.com/odpi/egeria/raw/main/assets/img/egeria3.png)

[![GitHub](https://img.shields.io/github/license/odpi/egeria)](LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/org.odpi.egeria/egeria)](https://mvnrepository.com/artifact/org.odpi.egeria)

# Egeria - Open Metadata and Governance
  
Egeria provides the Apache-2.0 licensed open metadata and governance 
type system, frameworks, APIs, event payloads and interchange protocols to enable tools,
engines and platforms to exchange metadata in order to get the best
value from data whilst ensuring it is properly governed.

* [Egeria Home Page](https://egeria.odpi.org)
* [Github](https://github.com/odpi/egeria)
* [Slack discussions](https://slack.lfai.foundation)

## Image: egeria

This *nix based image contains all the required runtime artifacts for egeria - for example the main server chassis, the user interface, required dependencies etc.

Specifically it contains the full [egeria assembly](https://github.com/odpi/egeria/blob/main/open-metadata-distribution/open-metadata-assemblies/src/main/assemblies/egeria-omag.xml)

## Usage

It's recommended you use the Egeria tutorials to get started. Below are some examples of using the image standalone. Refer to the main Egeria docs for further information

### Launch a container running the latest version of egeria in the background (version 2.0 and above)

```
$ docker run  -p 9443:9443  odpi/egeria:latest
Picked up JAVA_TOOL_OPTIONS:
 ODPi Egeria
    ____   __  ___ ___    ______   _____                                 ____   _         _     ___
   / __ \ /  |/  //   |  / ____/  / ___/ ___   ____ _   __ ___   ____   / _  \ / / __    / /  / _ /__   ____ _  _
  / / / // /|_/ // /| | / / __    \__ \ / _ \ / __/| | / // _ \ / __/  / /_/ // //   |  / _\ / /_ /  | /  _// || |
 / /_/ // /  / // ___ |/ /_/ /   ___/ //  __// /   | |/ //  __// /    /  __ // // /  \ / /_ /  _// / // /  / / / /
 \____//_/  /_//_/  |_|\____/   /____/ \___//_/    |___/ \___//_/    /_/    /_/ \__/\//___//_/   \__//_/  /_/ /_/

 :: Powered by Spring Boot (v2.2.5.RELEASE) ::
```

Wed Mar 25 15:11:54 GMT 2020 No OMAG servers listed in startup configuration
Wed Mar 25 15:11:58 GMT 2020 OMAG server platform ready for more configuration

All requests to egeria should be via https on port 9443

### Launch a container running the latest version of egeria in the background (version 1.x)

Use
```
$ docker run  -p 8080:8080  odpi/egeria:latest
```

In this older version only http was supported, so the default port is 8080 (http).

### Run the egeria user interface

```
$ docker run  -p 8443:8443  odpi/egeria:latest /bin/bash -c "java -jar /opt/egeria/user-interface/ui-chassis-spring-*.jar"
Picked up JAVA_TOOL_OPTIONS:
 ODPi Egeria

       ______                        _                  __  __   ____
      / ____/ ____ _  ___    _____  (_) ____ _         / / / /  /  _/
     / __/   / __ `/ / _ \  / ___/ / / / __ `/        / / / /   / /
    / /___  / /_/ / /  __/ / /    / / / /_/ /        / /_/ /  _/ /
   /_____/  \__, /  \___/ /_/    /_/  \__,_/         \____/  /___/
           /____/


 :: Powered by Spring Boot (v2.2.5.RELEASE) ::
```

### Run an interactive shell
```
$ docker run  -it -p 8443:8443  odpi/egeria:latest /bin/bash -
bash-4.4$ pwd
/opt/egeria
bash-4.4$ ls
LICENSE            NOTICE             clients            conformance-suite  server             user-interface     utilities
bash-4.4$
```
### Persisting data

By default any data you create whilst the docker image is running will get written to the image, and may only get saved if you specifically commit. Best practice is to use a docker volume or mount point. More information on this can be found at [https://docs.docker.com/storage/#:~:text=Volumes%20are%20the%20best%20way,modify%20them%20at%20any%20time.](Docker docs).

As of version 2.6 (or main as of 18 Dec 2020), Egeria saves all data when running to the 'data' directory. In the docker image this is at '/deployments/data'

Similarly when using this image within kubernetes you should ensure persistent storage is mounted over this directory. Our k8s examples will do this as of 2.6

An example using docker:

Create the volume locally on your docker host:
```
$ docker volume create egeria-data 
egeria-data
```

List your volumes:
```
$ docker volume ls
DRIVER    VOLUME NAME
local     egeria-data
```

Find out more about where that volume is stored locally:
```
$ docker volume inspect egeria-data                                                                    
[
    {
        "CreatedAt": "2020-12-17T12:37:11Z",
        "Driver": "local",
        "Labels": {},
        "Mountpoint": "/data/docker/volumes/egeria-data/_data",
        "Name": "egeria-data",
        "Options": {},
        "Scope": "local"
    }
]
```

With that in place we can now run our docker image, this time making use of the volume above to persist data ie:
```
$ docker run -p 9443:9443 -v source=egeria-data,target=/deployments/data odpi/egeria:latest 
/usr/local/s2i/run: line 15: /opt/jboss/container/maven/default//scl-enable-maven: No such file or directory
Starting the Java application using /opt/jboss/container/java/run/run-java.sh ...
INFO exec  java -XX:+UseParallelOldGC -XX:MinHeapFreeRatio=10 -XX:MaxHeapFreeRatio=20 -XX:GCTimeRatio=4 -XX:AdaptiveSizePolicyWeight=90 -XX:MaxMetaspaceSize=100m -XX:+ExitOnOutOfMemoryError -XX:MaxMetaspaceSize=1g -cp "." -jar /deployments/server/server-chassis-spring-4.0-SNAPSHOT.jar
 ODPi Egeria
    ____   __  ___ ___    ______   _____                                 ____   _         _     ___
   / __ \ /  |/  //   |  / ____/  / ___/ ___   ____ _   __ ___   ____   / _  \ / / __    / /  / _ /__   ____ _  _
  / / / // /|_/ // /| | / / __    \__ \ / _ \ / __/| | / // _ \ / __/  / /_/ // //   |  / _\ / /_ /  | /  _// || |
 / /_/ // /  / // ___ |/ /_/ /   ___/ //  __// /   | |/ //  __// /    /  __ // // /  \ / /_ /  _// / // /  / / / /
 \____//_/  /_//_/  |_|\____/   /____/ \___//_/    |___/ \___//_/    /_/    /_/ \__/\//___//_/   \__//_/  /_/ /_/

 :: Powered by Spring Boot (v2.3.3.RELEASE) ::
```

### Reviewing additional label metadata in the image:

Note that you'll be able to see the image version, build date, last git update etc (this is an old example from 1.6, when we used http & port 8080)
```
$ docker inspect --format='{{range $k, $v := .ContainerConfig.Labels}} {{- printf "%s = \"%s\"\n" $k $v -}} {{end}}' odpi/egeria:latest
org.opencontainers.image.vendor = "= ODPi       org.opencontainers.image.title = Egeria       org.opencontainers.image.description = Common image for core ODPi Egeria runtime.       org.opencontainers.image.url = https://egeria.odpi.org/       org.opencontainers.image.source = https://github.com/odpi/egeria       org.opencontainers.image.authors = ODPi Egeria       org.opencontainers.image.revision = 2e8b97d       org.opencontainers.image.licenses = Apache-2.0       org.opencontainers.image.created = 2020-03-25T12:03:52+0000       org.opencontainers.image.version = 1.6-SNAPSHOT       org.opencontainers.image.documentation = https://egeria.odpi.org/open-metadata-resources/open-metadata-deployment/docker/egeria/       org.opencontainers.image.ext.vcs-date = 2020-03-25T11:46:50+0000       org.opencontainers.image.ext.docker.cmd = docker run -d -p 8080:8080 odpi/egeria       org.opencontainers.image.ext.docker.cmd.devel = docker run -d -p 8080:8080 -p 5005:5005 -e JAVA_DEBUG=true odpi/egeria       org.opencontainers.image.ext.docker.debug = docker exec -it  /bin/sh       org.opencontainers.image.ext.docker.params = JAVA_DEBUG=set to true to enable JVM debugging"

```

### Extending the image to include additional libraries, such as connectors

By default the docker image is setup to install the egeria server chassis jar into `/deployments/server`/
The `/deployments/server/lib` directly is also set up in the classpath via Spring's loader.path property - set via LOADER_PATH in Docker

Egeria's capability can be extended through connectors. These will take the form of a java jar
which needs to be dynamically loaded at runtime. As such follow one of the following methods
to deploy any additional connectors

#### Adding to the image using a volume

In docker, or kubernetes mount an additional volume in the image - for example at `/deployments/server/extlib`
Then ensure the variable 'LOADER.PATH' is set to include this directory in addition to '/deployments/server/lib' which is
required for the egeria server chassis to work

This example will:
 * Create a new docker volume to store the library
 * Run a lightweight docker container so we can copy files
 * Copy a local library into the volume
 * Ensure the permissions are correct for the egeria image 
 * Shut down the utility container
 * Launch the egeria container with the classpath used by the server chassis spring app set to include the new volume - where our custom connector is located
```shell
extralibs
$ docker run -d --rm --name copyutil -v extralibs:/mnt alpine tail -f /dev/null                                                                                   [14:49:29]
13368e4f645bbc7470785d83b831f05a0ae907ee149f9f62ab9943869b8baa63
$ docker cp ~/src/egeria-database-connectors/postgres-connector/build/libs/postgres-connector-2.5-SNAPSHOT-javadoc.jar copyutil:/mnt                              [14:49:52]
$ docker exec copyutil chown -R 185 /mnt                                                                                                                          [14:50:25]
$ docker stop copyutil                                                                                                                                            [14:50:44]
copyutil
$ docker run --env LOADER_PATH=/deployments/extralibs:/deployments/server/lib -v extralibs:/deployments/extralibs -p 9443:9443 egeria:latest
```

There are many ways of achieving the same result. If working locally you may wish to use a docker 'bind-mount', whilst in kubernetes
you may need to create another job to retrieve the required libraries or content -- in this case the image approach (below) may be easier.

#### Adding to the image through a docker build

Use the egeria image as a base, for example begin your custom Dockerfile with
`FROM odpi/egeria:4.0-SNAPSHOT`

Then add in the files you need, as well as customize the LOADER_PATH variable ie
`COPY myextralib.jar /deployments/server/lib`

If you use the server/lib directory you do not need to change the value of the LOADER_PATH variable as this directory
is already included.

---


License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# IGC connector 

## Overview

Dockerfile to run the IGC proxy

## Usage

 - switch into this directory
 - docker build -t igcproxy-egeriavdc:0.1.0 .

## Publishing the build

As the build process is refined, this will be automated & use a more
egeria-centric id.

 - docker login
 - {enter credentials}
 - docker tag igcproxy-egeriavdc:0.1.0 planetf1/igcproxy-egeriavdc:0.1.0
 - docker push planetf1/igcproxy-egeriavdc:0.1.0

## Additional notes

* Ensure that you have a recent version of Docker installed from
   [docker.io](http://www.docker.io).
* Issueing API calls to Egeria - the endpoint is localhost:8080
* If you need to login to the image to see what is happening, use `docker ps` to find the name of the running container, and the `docker exec -it <container name> /bin/bash` to get an interactive shell

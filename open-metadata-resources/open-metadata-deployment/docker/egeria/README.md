<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Egeria-docker-selfbuild

## Overview

The Dockerfile in this folder can be used to build a Docker image running
the latest Egeria master branch.

It will build egeria directly from the master build repository, and then configure it to start with a default configuration, on port 8080

## Usage

 - switch into this directory
 - docker build -t egeria-egeriavdc:0.1.0 .

## Publishing the build

As the build process is refined, this will be automated & use a more
egeria-centric id.

 - docker login
 - {enter credentials}
 - docker tag egeria-egeriavdc:0.1.0 planetf1/egeria-egeriavdc:0.1.0
 - docker push planetf1/egeria-egeriavdc:0.1.0

## Additional notes

* Ensure that you have a recent version of Docker installed from
   [docker.io](http://www.docker.io).
* Issueing API calls to Egeria - the endpoint is localhost:8080
* If you need to login to the image to see what is happening, use `docker ps` to find the name of the running container, and the `docker exec -it <container name> /bin/bash` to get an interactive shell

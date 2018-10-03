<!-- SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Egeria-docker-selfbuild

## Overview

The Dockerfile in this folder can be used to build a Docker image running
the latest Egeria master branch.

It will build egeria directly from the master build repository, and then configure it to start with a default configuration, on port 8080

## Usage


1. Ensure that you have a recent version of Docker installed from
   [docker.io](http://www.docker.io).
2. Set this folder as your working directory.
3. Type `docker build -t egeria .` to build a Docker image called **egeria**.
4. Type `docker run egeria` to launch the Egeria Server chassis
5. Issue API calls to Egeria - the endpoint is localhost:8080
6. If you need to login to the image to see what is happening, use `docker ps` to find the name of the running container, and the `docker exec -it <container name> /bin/bash` to get an interactive shell

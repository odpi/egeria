<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# atlas_docker

## Overview

The Dockerfile in this folder can be used to build a Docker image running
the latest Atlas master branch in standalone mode. It does this by setting
up necessary dependencies, checking out the master branch of Atlas from
GitHub, and then building Atlas. By default, this image will start the Atlas
on port 21000.

The ATLAS-1773 Oct 2018 patch for Open Metadata is applied.

## Usage

 - switch into this directory
 - docker build -t atlas-1773-egeria:0.1.0 .

## Publishing the build

As the build process is refined, this will be automated & use a more
egeria-centric id.

 - docker login
 - {enter credentials}
 - docker tag atlas-1773-egeria:0.1.0 planetf1/atlas-1773-egeria:0.1.0
 - docker push planetf1/atlas-1773-egeria:0.1.0


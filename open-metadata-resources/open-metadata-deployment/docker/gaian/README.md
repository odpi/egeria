<!-- SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# gaian

Docker build script for Apache Ranger
 - pulls latest gaian zip directly from github (binary)
 - unpacks & creates a launch script to start the server
 - no storage defined

## Usage

 - switch into this directory
 - docker build .

## Publishing the build

The helm charts in Egeria which make use of this docker image
assume it has been pushed to dockerhub as 'planetf1/gaian'

As the build process is refined, this will be automated & use a more 
egeria-centric id.

 - 



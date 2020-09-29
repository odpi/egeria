<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# gaian

Docker build script for Gaian
 - pulls latest gaian zip directly from github (binary)
 - unpacks & creates a launch script to start the server
 - no storage defined

## Usage

 - switch into this directory
 - docker build -t gaian-egeriavdc:0.1.0 .

## Publishing the build

As the build process is refined, this will be automated & use a more
egeria-centric id.

 - docker login
 - {enter credentials}
 - docker tag gaian-egeriavdc:0.1.0 planetf1/gaian-egeriavdc:0.1.0
 - docker push planetf1/gaian-egeriavdc:0.1.0



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

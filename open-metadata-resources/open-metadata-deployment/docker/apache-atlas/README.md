<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# apache-atlas

Docker build script for Apache atlas

 - pulls latest apache atlas tar.gz directly from ftp.apache.org
 - version 2.0.0

## Usage

 - switch into this directory
 - docker build -f Docker.build -t odpi/apache-atlas:build .
 - docker build -t apache-atlas .

## Version change in build image

If atlas verison other 2.0.0
- docker build --build-arg atlas_version=<version> -f Docker.build -t odpi/apache-atlas:build .

## Publishing the build

As the build process is refined, this will be automated & use a more
egeria-centric id.

 - docker login
 - {enter credentials}
 - docker tag ranger-admin-egeriavdc:0.2.0 planetf1/ranger-admin-egeriavdc:0.2.0
 - docker push planetf1/ranger-admin-egeriavdc:0.2.0
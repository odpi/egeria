<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# ranger-admin

Docker build script for Apache Ranger
 - version 1.2 (last stable as of Nov 2018)
 - Builds from source by downloading source from git, since prebuilt binaries are hard to find ....
 - A standard ranger build takes 10 mins. From scratch the container build here takes around 30 mins
 - deploys solr within the same container
 - depends on an external mariadb service (hostname: mariadb)
 - passwords are hardcoded

## Usage

 - switch into this directory
 - docker build -t ranger-admin-egeriavdc:0.2.0 .

## Publishing the build

As the build process is refined, this will be automated & use a more
egeria-centric id.

 - docker login
 - {enter credentials}
 - docker tag ranger-admin-egeriavdc:0.2.0 planetf1/ranger-admin-egeriavdc:0.2.0
 - docker push planetf1/ranger-admin-egeriavdc:0.2.0


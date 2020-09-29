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



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

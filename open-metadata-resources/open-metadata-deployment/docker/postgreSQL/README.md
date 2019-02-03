<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# postgresql_docker

## Overview

Runs the official postgresql image, with one addition - an SSH server is added which is
needed to support ansible deployments
## Usage

 - switch into this directory
 - docker build -t postgresql-egeriavdc:0.0.1 .

## Publishing the build


 - docker login
 - {enter credentials}
 - docker tag postgresql-egeriavdc:0.0.1 planetf1/postgresql-egeriavdc:0.0.1
 - docker push planetf1/postgresql-egeriavdc:0.0.1


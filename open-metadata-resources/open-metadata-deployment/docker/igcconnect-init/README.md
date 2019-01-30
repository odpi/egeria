<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# egeria_init

## Overview

Configuration script for IGC  in Egeria

## Usage

 - switch into this directory
 - docker build -t igcproxy-init-egeriavdc:0.1.0 .

## Publishing the build

As the build process is refined, this will be automated & use a more
egeria-centric id.

 - docker login
 - {enter credentials}
 - docker tag igcproxy-init-egeriavdc:0.1.0 planetf1/igcproxy-init-egeriavdc:0.1.0
 - docker push planetf1/igcproxy-init-egeriavdc:0.1.0


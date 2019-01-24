<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# kafka_monitor

## Overview


## Usage

 - switch into this directory
 - docker build -t kafka-monitor-egeriavdc:0.1.0 .

## Publishing the build

As the build process is refined, this will be automated & use a more
egeria-centric id.

 - docker login
 - {enter credentials}
 - docker tag kafka-monitor-egeriavdc:0.1.0 planetf1/kafka-monitor-egeriavdc:0.1.0
 - docker push planetf1/kafka-monitor-egeriavdc:0.1.0


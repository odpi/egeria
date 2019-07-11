<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Docker Compose script to support Egeria labs and tutorials

This image is intended to support on-site labs, tutorials. 

Rather than having to install Egeria, prereqs & tools seperately, these scripts make it easy
to get a stack running quickly

This had much reduced function than Egeria's kubernetes support via Helm which is a better
option for real-world coding & deployment

Components included are:
 * Egeria -- this uses the latest egeria image, as published to dockerhub
 * Kafka & Zookeeper - standard Bitnami image
 * Jupyter notebook (lab version) - see https://jupyter-docker-stacks.readthedocs.io/en/latest/

## Usage

 - Ensure docker is installed & configured. See https://docs.docker.com/install/ 
 - docker-compose -f ./egeria-tutorial.yaml up
 - go to http://localhost:18888 to interact with the Jupyter Notebook
 - Note that in order to avoid typical port clashes, when referring to any of the components outside docker:
   - Egeria - use http://localhost:18888
   - Kafka  - use localhost:19092



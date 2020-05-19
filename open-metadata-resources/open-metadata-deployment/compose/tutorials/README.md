<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Docker Compose script to support Egeria labs and tutorials

This image is intended to support on-site labs, tutorials. 

Rather than having to install Egeria, prereqs & tools separately, these scripts make it easy
to get a stack running quickly

This has much reduced function than Egeria's kubernetes support via Helm which is a better
option for real-world coding & deployment. The same configuration we have here for the lab is 
also available for k8s. 
See https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/charts/odpi-egeria-lab

Components included are:
 * Multiple egeria images -- this uses the latest docker egeria image, as published to dockerhub
   and as used by our helm charts
   - Core egeria server - core:9443 internally, localhost:19443 externally
   - Datalake server    - datalake:9443 internally, localhost:19444 externally 
   - Development server - dev:9443 internally, localhost:19445 externally
   - Factory server     - factory:9443 internally, localhost:19446 externally
   - Egeria ui          - ui:8443 internally (https), localhost:18443 externally (https)
 * kafka - kafka:9092 internally, localhost:19092 externally - standard Bitnami image
 * zookeeper - zookeeper:2181 internally, localhost:12181 externally- standard Bitnami image
 * notebook - notebook:8888 internally, localhost:18888 externally (lab version, base image) - see https://jupyter-docker-stacks.readthedocs.io/en/latest/

## Usage

 - Docker & Docker compose must be installed - see https://docs.docker.com/install/
 - Configure docker with at least 4GB memory
 - Ensure you have access to both 'egeria-tutorial.yaml' and the 'notebook-start.d' subdirectory 
 which contains an initialization script 'getnotebooks.sh'. You can either run directly from a git 
 clone, or download the files individually
 - To start the environment `docker-compose -f ./egeria-tutorial.yaml up`
 - you will notice all the components starting. As the notebook server starts it will also load
   the latest notebooks we have available directly from git.
 - go to http://localhost:18888 to interact with the Jupyter Notebook 
 - To stop the environment : `docker-compose -f ./egeria-tutorial.yaml down`
 - To refresh the images (recommended to pick up latest code) : `docker-compose -f ./egeria-tutorial.yaml pull`

 ### Using the environment to extend notebooks or develop new ones
 
  - If you are using a notebook written to assume 'localhost:8080' or similar, replace with the following fragment. This will use the correct defaults for the environment (k8s or compose), or localhost if these are not yet. :
  corePlatformURL     = os.environ.get('corePlatformURL','http://localhost:8080')
  dataLakePlatformURL = os.environ.get('dataLakePlatformURL','http://localhost:8081')
  devPlatformURL      = os.environ.get('devPlatformURL','http://localhost:8082')
  factoryPlatformURL  = os.environ.get('factoryPlatformURL','http://localhost:8083')
 - The notebooks downloaded from git are refreshed on each start. Ensure any modifications to notebooks are saved elsewhere



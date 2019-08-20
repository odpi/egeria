<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Docker Compose script to support Egeria labs and tutorials

This image is intended to support on-site labs, tutorials. 

Rather than having to install Egeria, prereqs & tools seperately, these scripts make it easy
to get a stack running quickly

This has much reduced function than Egeria's kubernetes support via Helm which is a better
option for real-world coding & deployment. The same configuration we have here for the lab is 
also available for k8s. 
See https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/charts/lab

Components included (with hostnames) are:
 * Multiple egeria images -- this uses the latest docker egeria image, as published to dockerhub
   and as used by our helm charts
   - egeriacore (Core egeria server - port 8080)
   - egeriadev  (dev egeria server - port 8080)
   - egeriadl   (datalake egeria server - port 8080)
 * kafka - Kafka (port 9092) - standard Bitnami image
 * zookeeper - Zookeeper (port 1082)- standard Bitnami image
 * notebook - Jupyter notebook (lab version, base image) - see https://jupyter-docker-stacks.readthedocs.io/en/latest/

## Usage

 - Ensure you have access to both 'egeria-tutorial.yaml' and the 'notebook-start.d' subdirectory 
 which contains an initialization script 'getnotebooks.sh'. You can either run directly from a git 
 clone, or download the files individually
 - Ensure docker is installed & configured. See https://docs.docker.com/install/ 
 - docker-compose -f ./egeria-tutorial.yaml up
 - you will notice all the components starting. As the notebook server starts it will also load
   the latest notebooks we have available directly from git.
 - go to http://localhost:18888 to interact with the Jupyter Notebook 
 - In the notebook use the regular port numbers above
 - docker-compose -f ./egeria-tutorial.yaml down

 ### Debugging, workarounds, or using the services outside the notebook
  - Note that in order to avoid typical port clashes, when referring to any of the components outside docker/notebook add 10000 to
    the internal port number, and use localhost for hostname. The exception is egeria where the servers are at localhost:18080 , localhost:18081, localhost:18082
  - If you are using a notebook written to assume 'localhost:8080' or similar, replace with the following fragment. This will use the correct defaults for the environment (k8s or compose), or localhost if these are not yet. :
  corePlatformURL     = os.environ.get('corePlatformURL','http://localhost:8080')
  dataLakePlatformURL = os.environ.get('dataLakePlatformURL','http://localhost:8081')
  devPlatformURL      = os.environ.get('devPlatformURL','http://localhost:8082')



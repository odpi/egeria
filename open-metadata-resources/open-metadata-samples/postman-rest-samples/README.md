<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Sample Postman Scripts

Postman is a useful tool for testing REST APIs. See [https://www.getpostman.com/](https://www.getpostman.com/) for more
information about Postman.

It captures, stores and executes specific REST API calls so you do not have to keep typing the URL and parameters
each time you want to issue a request.

## Sample Collections

Collections help to group related REST API calls together.

In order to run these, you should have a set of environment variables configured as detailed in the next section
("Environment variables").

Egeria by default uses https:// requests with a self-signed certificate. Any PostMan users therefore will need to
go into settings->general and turn off 'SSL certificate verification' or requests will fail.

If interested, you can also [learn more about Postman Collections](https://learning.getpostman.com/docs/postman/collections/intro-to-collections/)
in general.

### Egeria Core

- [In-memory repository configuration and startup](https://raw.githubusercontent.com/odpi/egeria/main/open-metadata-resources/open-metadata-samples/postman-rest-samples/collection/Egeria-InMemoryRepository.postman_collection.json)

### [Egeria IBM Information Server Connectors](https://github.com/odpi/egeria-connector-ibm-information-server)

#### Egeria IBM Information Governance Catalog Connector

1. [Connector configuration and startup](https://raw.githubusercontent.com/odpi/egeria-connector-ibm-information-server/main/samples/Egeria-IBM-IGC-config.postman_collection.json)
1. [Sample read operations](https://raw.githubusercontent.com/odpi/egeria-connector-ibm-information-server/main/samples/Egeria-IBM-IGC-read.postman_collection.json)
1. Sample write operations

To use the sample read operations, you will first need to load the
[Coco Pharmaceuticals](../../open-metadata-deployment/sample-data/coco-pharmaceuticals)
samples to your IBM Information Governance Catalog environment.

#### Egeria IBM DataStage Connector

- [Connector configuration and startup](https://raw.githubusercontent.com/odpi/egeria-connector-ibm-information-server/main/samples/Egeria-IBM-DataStage-config.postman_collection.json)

The connector should automatically pickup and process any jobs already in your environment; however,
you can also load the
[Coco Pharmaceuticals](../../open-metadata-deployment/sample-data/coco-pharmaceuticals)
samples if you have an empty environment.

### [Egeria Apache Atlas Connector](https://github.com/odpi/egeria-connector-apache-atlas)

1. [Connector configuration and startup](https://raw.githubusercontent.com/odpi/egeria-connector-apache-atlas/main/samples/Egeria-Apache-Atlas-config.postman_collection.json)
1. [Sample read operations](https://raw.githubusercontent.com/odpi/egeria-connector-apache-atlas/main/samples/Egeria-Apache-Atlas-read.postman_collection.json)
1. Sample write operations

To use the sample read operations, you will first need to load the
[Hortonworks Sandbox](https://www.cloudera.com/downloads/hortonworks-sandbox.html)
samples to your Apache Atlas environment.

-----

## Environment variables

Postman supports the definition of variables like hostname, kafka queue, userId, etc.  These can then be used in
specific Postman commands.  This makes it easier for them to be used by multiple users, or with different
configurations.

A superset of all of the potential variables that we use in our samples (along with default values) are provided in:
[Egeria.postman_environment.json](Egeria.postman_environment.json).

You can either download the linked file and import it into Postman, or always import the latest version by importing
from the following link from within Postman:
[https://raw.githubusercontent.com/odpi/egeria/main/open-metadata-resources/open-metadata-samples/postman-rest-samples/Egeria.postman_environment.json](https://raw.githubusercontent.com/odpi/egeria/main/open-metadata-resources/open-metadata-samples/postman-rest-samples/Egeria.postman_environment.json)

You will most likely want to override some of these values (such as `baseURL` or `kafkaep`) depending on your
own environment's configuration; or you may even want to make several copies of this with different settings in each
one to be able to quickly change between different environments you have running at the same time.

Note that many of the variables are optional, depending on your particular configuration. The mandatory variables are
the following:

- `baseURL`: the base URL of your Egeria OMAG Server Platform, including the 'https://' prefix
- `user`: the user name of the user carrying out operations within the Egeria OMAG Server Platform
- `server`: the name of the server within the Platform in which to carry out operations
- `cohort`: the name of the cohort with which the server should interact
- `kafkaep`: the Apache Kafka endpoint (hostname:port) to use for Egeria's event bus

[Learn more more about Postman Environments](https://learning.getpostman.com/docs/postman/environments-and-globals/intro-to-environments-and-globals/).


----
* Return to [open-metadata-samples](..).
* Return to [Site Map](../../../Content-Organization.md)
* Return to [Home Page](../../../index.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Sample Postman Scripts

Postman is a useful tool for testing REST APIs. See [https://www.getpostman.com/](https://www.getpostman.com/) for more
information about Postman.

It captures, stores and executes specific REST API calls so you do not have to keep typing the URL and parameters
each time you want to issue a request.

## Sample Collections

Collections help to group related REST API calls together.

In order to run these, you should have a set of environment variables configured as detailed in the [next section](#environment-variables).

Egeria by default uses secured HTTP (`https://`) requests with a self-signed certificate. Any Postman users therefore will need to
go into settings->general and turn off 'SSL certificate verification' or requests will fail.

If interested, you can also [learn more about Postman Collections](https://learning.getpostman.com/docs/postman/collections/intro-to-collections/)
in general.

## Environment variables

Postman supports the definition of variables like hostname, kafka queue, userId, etc.  These can then be used in
specific Postman commands.  This makes it easier for them to be used by multiple users, or with different
configurations.

A superset of all the potential variables that we use in our samples (along with default values) are provided in:
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

[Learn more about Postman Environments](https://learning.getpostman.com/docs/postman/environments-and-globals/intro-to-environments-and-globals/).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
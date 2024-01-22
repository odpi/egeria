<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Sample Jetbrains HTTP Scripts

The Jetbrains  HTTP Client  is a useful utility for testing REST APIs that is both integrated into the Jetbrains tooling 
and freely available to be run standalone. Further information can be found at 
[Jetbrains HTTP Client](https://www.jetbrains.com/help/idea/http-client-in-product-code-editor.html). 
Information about the freely downloadable client can be found at 
[Download HTTP Client CLI: CI-friendly way of executing .http files](https://www.jetbrains.com/ijhttp/download/#section=zip-archive).


It captures, stores and executes specific REST API calls, so you do not have to keep typing the URL and parameters
each time you want to issue a request. You can also sequence and script REST calls into scenarios for testing or 
demonstration.


## Sample Collections

Collections help to group related REST API calls together.

In order to run these, you should have a set of environment variables configured as detailed in the [next section](#environment-variables).



## Environment variables

The HTTP Client supports the definition of variables like hostname, kafka queue, userId, etc.  These can then be used in
specific HTTP Client commands.  This makes it easier for them to be used by multiple users, or with different
configurations. Environment variables are stored in a file named either `http-client.env.json` or 
`http-client.private.env.json`. The first is meant to be shared across all users and the second meant for private 
customizations. 

A superset of potential variables that we use in our samples (along with default values) are provided in:
open-metadata-resources/open-metadata-deployment/http-client-rest-samples/http-client.env.json. This file contains 
several JSON structures that 
group environment variables for different purposes - the Egeria grouping has generally used variables while, for 
instance the Atlas grouping has variables specialized for Atlas. This file can be copied, into your own to adapt to your
own environment and requirements.

You will most likely want to override some of these values (such as `baseURL` or `kafkaep`) depending on your
own environment's configuration - updating the `http-client-private.env.json` file with your own customizations make it 
easy to preserve the defaults but select the configuration you need. A given environment file can have many configuration
sets within it, each created to support a different use case. Selection of the environment to use can be done
at execution time or when editing the requests file. You can, for example, set up different environments for development, 
test, and production.

Note that many of the variables are optional, depending on your particular configuration. The most commonly used
variables are the following:

- `baseURL`: the base URL of your Egeria OMAG Server Platform, including the 'https://' prefix
- `user`: the user name of the user carrying out operations within the Egeria OMAG Server Platform
- `server`: the name of the server within the Platform in which to carry out operations
- `cohort`: the name of the cohort with which the server should interact
- `kafkaep`: the Apache Kafka endpoint (hostname:port) to use for Egeria's event bus

[Learn more about the HTTP Client](https://www.jetbrains.com/help/idea/http-client-in-product-code-editor.html#composing-http-requests).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
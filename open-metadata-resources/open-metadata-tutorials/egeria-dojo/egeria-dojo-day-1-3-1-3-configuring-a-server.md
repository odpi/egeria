<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

![Blue - Intermediate sessions](egeria-dojo-session-coding-blue-intermediate-session.png)

# Configuring a metadata server on the OMAG Server Platform

The OMAG Server Platform is able to host one-to-many OMAG servers.
An OMAG Server is responsible for supporting the integration of different types of
technology.  There are different types of OMAG Servers in Egeria.
In this session you are going to learn how to set up particular type of OMAG server called a metadata server.

![Configuring a metadata server Content](egeria-dojo-day-1-3-1-3-configuring-a-server.png)

Begin by understanding about the different types of OMAG Servers and what they are used for by
following the link below:
* [Egeria's OMAG Servers](../../../open-metadata-implementation/admin-services/docs/concepts/omag-server.md)

In this first exercise you are going to use Postman to configure a simple metadata server called
`myMetadataServer`.

* In the Postman Egeria Environment, update the variable called `server` from `myServer` to `myMetadataServer`.

Using the `Egeria-admin-services-server-configuration` Postman collection and the instructions
from the [Admin services user guide on metadata servers](../../../open-metadata-implementation/admin-services/docs/concepts/metadata-server.md)
create the configuration for `myMetadataServer` as follows.  For each value, find the right REST API request in the
Postman collection.  Then look at where the values come from.  Sometimes you will need to change the variable
value in the Egeria Environment, sometimes you can type it directly into the request URL and other times,
the request in Postman is just what you need.

Each time you add a configuration value, 
[retrieve the server's configuration](../../../open-metadata-implementation/admin-services/docs/concepts/configuration-document.md)
to see how the effect of your requests are changing the server's configuration.

* **local server URL root** to `http://localhost:18080`

* **localServerType** to `Egeria Dojo Metadata Server` (update the value in the request)
 
* **organizationName** to your organization name (update the variable `organization_name`).
 
* **localServerUserId** to `myMetadataServerUserId`.

* **localServerPassword** to `myMetadataServerPassword`

* **maxPageSize** - the maximum page size that can be set on requests to the server. The default value is 1000.

* Add a graph local repository

* Configure the **Asset Owner* Open Metadata Access Service (OMAS)

* Set up the Coco Pharmaceutical **Server** Security connector to provide authorization checks for inbound REST API calls.

Once the configuration is complete you are ready to move on to the next section.

## Test yourself

* What is the name of the place where a server's configuration is assembled?
* What determines where the server configuration is stored?
* What is the quickest way to discover what has recently changed in a server's configuration?

----

* Return to [Platform set up and configuration](egeria-dojo-day-1-3-1-platform-set-up-and-configuration.md)
* Return to [Dojo Overview](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
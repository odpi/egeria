<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Configuring Registration to an Open Metadata Repository Cohort

An [OMAG Server](../concepts/omag-server.md) that is capable of being a
[Cohort Member](../concepts/cohort-member.md) can register with one of more
open metadata repository cohorts.

Before calling these commands, make sure that the [default settings for the event bus](configuring-event-bus.md)
are configured.

## Enable access to a cohort

The following command registers the server with a cohort.
Each cohort has a memorable name - eg `cocoCohort`.

```
POST {serverURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/cohorts/{cohortName}
```

## Override the default value for the cohort topic

At the heart of a cohort is the cohort topic.  This is a topic on an event bus that
the members use to exchange metadata on.

The default topic name is `egeria.omag.openmetadata.repositoryservices.cohort.{cohortName}.OMRSTopic`.

It is possible to change the topic name used by a cohort with the following command.
The `{newTopicName}` flows in the request body.
```
POST {serverURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/cohorts/{cohortName}topic-name-override
{newTopicName}
```
It must be issued against each member of the cohort so that they are
all connecting to the same cohort topic.

The new value takes affect the next time the server is started.

## Disconnect from a cohort

This command unregisters a server from a cohort.

```
DELETE {serverURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/cohorts/{cohortName}
```

----
* Return to [Configuring an OMAG Server](configuring-an-omag-server.md)
* Return to [Configuring a Metadata Access Point](../concepts/metadata-access-point.md#Configuring-a-Metadata-Access-Point)
* Return to [Configuring a Metadata Server](../concepts/metadata-server.md#Configuring-a-Metadata-Server)
* Return to [Configuring a Repository Proxy](../concepts/repository-proxy.md#Configuring-a-Repository-Proxy)
* Return to [Configuring a Conformance Test Server](../concepts/conformance-test-server.md#Configuring-a-Conformance-Test-Server)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
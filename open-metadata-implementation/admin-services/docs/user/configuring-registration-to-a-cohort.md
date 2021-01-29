<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Configuring Registration to an Open Metadata Repository Cohort

An [OMAG Server](../concepts/omag-server.md) that is capable of being a
[Cohort Member](../concepts/cohort-member.md) can register with one of more
open metadata repository cohorts.

Before calling these commands, make sure that the [default settings for the event bus](configuring-event-bus.md)
are configured.

## Add access to a cohort

The following command registers the server with a cohort.
Each cohort has a memorable name - eg `cocoCohort`.

```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/cohorts/{cohortName}
```

## Query the name of the cohort topic

At the heart of a cohort is the cohort topic.  This is a topic on an event bus that
the members use to exchange metadata on.

The default topic name is `egeria.omag.openmetadata.repositoryservices.cohort.{cohortName}.OMRSTopic`.

It is possible to query the topic name in use for the cohort so it can be configured in
the event bus for environments where topics are not auto-created on use.

It is possible to change the topic name used by a cohort with the following command.
```
GET {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/cohorts/{cohortName}topic-name-override
```
This is an example of the result for a configured cohort:
```json
{
    "class": "StringResponse",
    "relatedHTTPCode": 200,
    "resultString": "egeria.openmetadata.repositoryservices.cohort.cocoCohort.OMRSTopic"
}
```
If the cohort name is not known, the result looks like this:
```json
{
    "class": "StringResponse",
    "relatedHTTPCode": 400,
    "exceptionClassName": "org.odpi.openmetadata.adminservices.ffdc.exception.OMAGInvalidParameterException",
    "exceptionErrorMessage": "OMAG-ADMIN-400-033 The OMAG server cocoMDS1 is unable to override the cohort topic until the cocoCohortXXX cohort is set up",
    "exceptionSystemAction": "No change has occurred in this server's configuration document.",
    "exceptionUserAction": "Add the cohort configuration using the administration services and retry the request."
}
```

## Override the value for the cohort topic

It is also possible to change the topic name used by a cohort with the following command.
The `{newTopicName}` flows in the request body.
```
POST {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/cohorts/{cohortName}topic-name-override
{newTopicName}
```
It must be issued against each member of the cohort so that they are
all connecting to the same cohort topic.

The new value takes affect the next time the server is started.

## Disconnect from a cohort

This command unregisters a server from a cohort.

```
DELETE {platformURLRoot}/open-metadata/admin-services/users/{adminUserId}/servers/{serverName}/cohorts/{cohortName}
```

----
* Return to [Configuring an OMAG Server](configuring-an-omag-server.md)
* Return to [Configuring a Metadata Access Point](../concepts/metadata-access-point.md#Configuring-a-Metadata-Access-Point)
* Return to [Configuring a Metadata Server](../concepts/metadata-server.md#Configuring-a-Metadata-Server)
* Return to [Configuring a Repository Proxy](../concepts/repository-proxy.md#Configuring-a-Repository-Proxy)
* Return to [Configuring a Conformance Test Server](../concepts/conformance-test-server.md#Configuring-a-Conformance-Test-Server)
* Return to [configuration document structure](../concepts/configuration-document.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
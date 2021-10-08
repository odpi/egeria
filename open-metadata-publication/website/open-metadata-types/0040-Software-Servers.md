<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0040 Software Servers

Software servers describe the middleware software servers
(such as application servers, data movement engines and database servers)
that run on the Hosts.
An [OMAG Server](../../../open-metadata-implementation/admin-services/docs/concepts/omag-server.md)
is an example of a Software Server.

There are many types of software servers and there are specialized
types for the following 




![UML](0040-Software-Servers.png#pagewidth)

Within the software server model we capture its userId that it operates under.
Most metadata repositories are run in a secure mode requiring
incoming requests to include the requester’s security credentials.
Therefore we have an identifier for each unique logged on security identity
(aka userId).
This identity is recorded within specific entities and relationships
when they are created or updated.
By storing the user identifier for the server, it is possible to
correlate the server with the changes to the metadata
(and related data assets) that it makes. 



## Related information

* See model [0110 Actors](0110-Actors.md) and [0117 IT Profiles](0117-IT-Profiles.md) in Area 1 for details of how user identifiers
  are correlated with **ActorProfiles** for people and teams.  The **ITProfile** makes it possible to define
  a profile for a server's userId so that additional information about the userId can be captured.

*

----

* Return to [Area 0](Area-0-models.md).
* Return to [Overview](.).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
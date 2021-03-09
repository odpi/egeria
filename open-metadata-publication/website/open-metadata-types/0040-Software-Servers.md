<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0040 Software Servers

Software servers describe the middleware software servers
(such as application servers, data movement engines and database servers)
that run on the Hosts.
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

See model 0110 Actors in Area 1 for details of how user identifiers
are correlated with people and teams).

![UML](0040-Software-Servers.png#pagewidth)

Open metadata may also capture the network endpoint(s) that the server
is connected to and the host it is deployed to.

The endpoint defines the parameters needed to connect to the server.
It also features in the Connection model (0201 in Area 2) used by
applications and
tools to call the server.
Thus through the endpoint entity it is possible to link the
connection to the underlying server.

An [OMAG Server](../../../open-metadata-implementation/admin-services/docs/concepts/omag-server.md)
is an example of a Software Server.

Return to [Area 0](Area-0-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
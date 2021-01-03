<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Governance Engine Open Metadata Access Service (OMAS) Connectors

The governance engine connectors are the interfaces of the
connectors supported by the Governance Engine OMAS.

Currently there are two connectors, one for the client
and one for the server, that are used to exchange events
over the Governance Engine OMAS's out topic.

There are two connectors because events should only flow one way
and so the client needs a different interface to the server.

Specifically the client interface is a listener interface
to allow the client to receive events from the server.
The server interface is an event sending interface.

If the Governance Engine OMAS supported an in topic,
there would be two additional connectors: a listening type
interface for the server and a sending type
interface for the client.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

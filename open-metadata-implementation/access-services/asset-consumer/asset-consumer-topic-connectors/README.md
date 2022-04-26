<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Asset Consumer Open Metadata Access Service (OMAS) Connectors

The asset consumer connectors are the interfaces of the
connectors supported by the Asset Consumer OMAS.

Currently, there are two connectors, one for the client
and one for the server, that are used to exchange events
over the Asset Consumer OMAS's out topic.

There are two connectors because events should only flow one way
and so the client needs a different interface to the server.

Specifically the client interface is a listener interface
to allow the client to receive events from the server.
The server interface is an event sending interface.

If the Asset Consumer OMAS supported an in topic,
there would be two additional connectors: a listening type
interface for the server and a sending type
interface for the client.

----
Return to the [asset-consumer](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Discovery Engine Open Metadata Access Service (OMAS) API

The Discovery Engine OMAS API provides the shared Java classes between the
server and client needed in addition to the
metadata interfaces defined in the
[Open Discovery Framework (ODF)](../../../frameworks/open-discovery-framework).

Specifically, the Discovery Engine OMAS 
provides
an [out topic](https://egeria-project.org/concepts/out-topic)
used to keep the Discovery Engine OMAS's client (typically a Discovery Server)
up-to-date with configuration changes.

This out topic is supported by a client interface extension and
connectors to interact with the topic.

Currently there are two connectors defined in the
Discovery Engine OMAS's API: one for the client
and one for the server.  They are used to exchange events
over the Discovery Engine OMAS's out topic.

There are two connectors because events should only flow one way
and so the client needs a different interface to the server.

Specifically the client interface is a listener interface
to allow the client to receive events from the server.
The server interface is an event sending interface.

If the Discovery Engine OMAS supported an in topic,
there would be two additional connectors: a listening type
interface for the server and a sending type
interface for the client.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# OMF Topic Connectors

The OMF Topic connectors are the interfaces of the
connectors supported by the Open Metadata Metadata Access Services.

There are two connectors, one for the client
and one for the server, that are used to exchange events
over the OMF out topic.

There are two connectors because events should only flow one way
and so the client needs a different interface to the server.

Specifically the client interface is a listener interface
to allow the client to receive events from the server.
The server interface is an event sending interface.


----
Return to the [omf-metadata-management](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Governance Action Framework (GAF)
  
The Governance Action Framework (GAF) provides the interfaces
and base implementations for components (called governance actions) that
take action to correct a situation that is harmful to the data
or the organization in some way.

Most governance actions execute in external engines that are working with data
and related assets.
The GAF offers embeddable functions and APIs to simplify the implementation of
the governance actions, whilst being resilient and with good performance.

Governance actions themselves are open connectors
(see [Open Connector Framework (OCF)](../open-connector-framework))
that support the interfaces defined by the GAF.
They may produce audit log records and exceptions
and they may make changes to metadata through the open metadata access services.

The GAF also supports the development of stewardship services 
to analyze the audit log records and process the exceptions.

These stewardship services are built using the Open Metadata and
Governance Stewardship Toolkit and they run in the Open Metadata and
Governance Stewardship Server.


----
Return to [frameworks](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.


<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Open Metadata Security Server Side support

Inside an OMAG Server Platform, and each OMAG Server that is running on it,
are security verifiers. They have the same interface as the
two [Open Metadata Security Connectors](../metadata-security-connectors).
However, they are always present and are the components that the other services call.

Inside, the security verifiers optionally hold the Open Metadata Security Connector instance
if it is defined for the server/platform.  When a security verifier is called,
it delegates the call to the connector if present, or responds
to say that the all is well.  With this design, no other component needs to
manage the optional nature of the Open Metadata Security Connectors.

----
* Return to [Module Overview](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Open Metadata Security Connectors

The open metadata security connectors support plugins to
implement security authorization decisions in an OMAG Server Platform
or an OMAG Server.

They are optional and are specified in the platform or server's configuration.
The way they operate is described in the [Open Metadata Security Module Overview](..)
and their interfaces are described on the [Connector API](../metadata-security-apis) page.

Each connector has a base class.  These are:
* **OpenMetadataPlatformSecurityConnector** for the OMAG Server Platform
* **OpenMetadataServerSecurityConnector** for the OMAG Servers

When there is no open metadata security connector
plugged into a server and/or platform, everything is permitted.
If just these base classes are added then nothing is permitted.
They provide reusable methods to report security exceptions and call them
in every connector method.

When you write your organization's implementation of these connectors,
you extend these classes and add the special checks for authorized use
in each method.
When an action is not authorized, your method can either call the appropriate reusable methods or
call its equivalent superclass method to report the error.

There are complete working examples of these connectors used in
the [Egeria Hands On Labs](../../../../open-metadata-resources/open-metadata-labs).
The code for these connectors is in the
[open-metadata-security-samples](../../../../open-metadata-resources/open-metadata-samples/open-metadata-security-samples) module.

----
  
* Return to [Extending Egeria using Connectors](../../../../open-metadata-publication/website/developer-guide/extending-egeria-using-connectors.md) 
* Return to [Open Metadata Security Module Overview](..)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
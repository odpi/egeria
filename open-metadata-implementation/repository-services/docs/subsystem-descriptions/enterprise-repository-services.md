<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Enterprise Repository Services

The **enterprise repository services** support the federating metadata collection that is able to retrieve
metadata from all of the repositories that are members of
the cohort(s) that the local metadata server belongs to.  They include the following components.

* **[Enterprise Connector Manager](../component-descriptions/enterprise-connector-manager.md)** - Manages the list of open metadata repositories
that the Enterprise OMRS Repository Connector should call to retrieve an enterprise view of the metadata collections
supported by these repositories.
* **[Enterprise Repository Connector](../component-descriptions/enterprise-repository-connector.md)** - Supports federated queries.
  * Enterprise OMRS Connector Provider - The OCF Connector Provider factory for the Enterprise OMRS Repository Connector.
  * Enterprise OMRS Repository Connector - Implements the OMRS Repository Connector interface that supports enterprise
  access to the list of open metadata repositories registered with the OMRS Enterprise Connector Manager.
  * Enterprise OMRS Metadata Collection - Manages calls to the list of open metadata repositories
  registered with the OMRS Enterprise Connector Manager on behalf of the Enterprise OMRS Repository Connector.
  * Enterprise OMRS Connector Properties - Provides the connected asset properties for the Enterprise OMRS Repository Connector.
  
The enterprise repository services are enabled automatically in a metadata server when one or more
[Open Metadata Access Services (OMASs)](../../../access-services) are configured.



----
* Return to [repository services subsystem descriptions](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.


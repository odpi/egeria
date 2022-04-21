<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Released](../../../images/egeria-content-status-released.png#pagewidth)

# Repository Handler

The repository handler provides methods for managing linked groups
of Open Metadata Repository Services (OMRS) instances.

It supports the following abstractions:
* Conversion of repository services exceptions into common exceptions.
* Creation of the correct type of instances dependent on the setting of the external source GUID.  This means that
  the correct provenance information is added to the instance.
* Validation of an instance's provenance information when an update is made.  This means that external instances
  can only be updated by processes that represent the external source of the instance.

The aim is to reduce the coding needed in the specific handlers used by the Open Metadata Access Services (OMASs) and ensure
external entities are managed correctly.

* [Documentation](https://egeria-project.org/services/repository-handler)

----
* Return to [common-services](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
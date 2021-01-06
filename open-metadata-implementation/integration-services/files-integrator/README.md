<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

![TechPreview](../../../open-metadata-publication/website/images/egeria-content-status-tech-preview.png#pagewidth)

# Files Integrator Open Metadata Integration Service (OMIS)

The Files Integrator integration service supports the collection of information about files
stored in a filesystem and then storing it in the open metadata ecosystem.

The modules are as follows:

* [files-integrator-api](files-integrator-api) - defines the interface for an integration
connector that is supported by the Files Integrator integration service.  This includes the implementation
of the context that wraps the Data Manager OMAS's clients.

* [files-integrator-server](files-integrator-server) - implements the context manager for
the Files Integrator integration service.

This integration service is paired with the [Data Manager](../../access-services/data-manager)
Open Metadata Access Service (OMAS).

----
Return to the [Integration Services](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
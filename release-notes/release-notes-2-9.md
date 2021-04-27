<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 2.9 (May 2021)

Release 2.9 adds:
* Changes to metadata types
* Changes to building Egeria on Windows

Details of these and other changes are in the sections that follow.

## Description of Changes

### Metadata Types

* The **UserProfileManager**, **UserAccessDirectory** and **MasterDataManager** classification for **Referenceables** has been added
to model [0056 Asset Managers](../open-metadata-publication/website/open-metadata-types/0056-Asset-Managers.md).
* A new relationship called **ActionTarget** to link a **To Do** to the elements that need work has been added
to model [0137 Actions for People](../open-metadata-publication/website/open-metadata-types/0137-Actions.md).
* A new attribute called **filterExpression** has been added to the **Port** entity in
model [0217 Ports](../open-metadata-publication/website/open-metadata-types/0217-Ports.md).
* A new classification called **PrimaryCategory** has been added to
model [0335 Primary Category](../open-metadata-publication/website/open-metadata-types/0335-Primary-Category.md).
* A new classification called **GovernanceMeasurements** has been added to
model [0450 Governance Rollout](../open-metadata-publication/website/open-metadata-types/0450-Governance-Rollout.md).
* The **RelationalColumnType** entity in
model [0534 Relational Schema](../open-metadata-publication/website/open-metadata-types/0534-Relational-Schemas.md).
only allows for a column to be primitive. It could be a literal, enum or external and so this type has been deprecated
and the appropriate schema types should be used directly.

### Building Egeria on Windows

To build Egeria on Windows you should use [Windows Subsystem for Linux](https://docs.microsoft.com/en-us/windows/wsl/) Version 2 or above & install an 
appropriate Linux distribution.

Egeria should then be built & run within this environment. 

IDEs such as IntelliJ and VSCode support editing and code management within the Windows GUI alongside build and execution in Linux.

See [PR #5084](https://github.com/odpi/egeria/pull/5084) for more information.

### Bug fixes and other updates
* Additional Bug Fixes
* Dependency Updates

For details on both see the commit history in GitHub.

## Known Issues

* It is recommended to use a chromium-based browser such as Google Chrome or Microsoft Edge, or Apple Safari for the Egeria React UI. Some parts of the UI experience such as Dino currently experience problems with Firefox. See [odpi/egeria-react-ui#96](https://github.com/odpi/egeria-react-ui/issues/96) .
* When running the 'Understanding Platform Services' lab, ensure you run the 'egeria-service-config' notebook first and do not restart the python kernel before running this lab. See [#4842](https://github.com/odpi/egeria/issues/4842) .

# Egeria Implementation Status at Release 2.9

![Egeria Implementation Status](../open-metadata-publication/website/roadmap/functional-organization-showing-implementation-status-for-2.9.png#pagewidth)

Link to Egeria's [Roadmap](../open-metadata-publication/website/roadmap) for more details about the
Open Metadata and Governance vision, strategy and content.


# Further Help and Support

See the [Community Guide](../Community-Guide.md).

----
* Return to [Release Notes](.)
   
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

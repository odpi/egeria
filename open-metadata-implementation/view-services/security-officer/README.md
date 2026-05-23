<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

![InDev](../../../images/egeria-content-status-in-development.png#pagewidth)

# Security Officer OMVS 

The Security Officer OMVS is a REST API designed to support user interfaces (UIs) for supporting a Security Officer as they lead the security governance program.  This builds on the capabilities of the Governance Officer OMVS.

## Key Features

The Security officer API features include:

* **User Account Management**: Setting up and maintaining user accounts within the platform's security connectors.
* **Security Access Control**: Configuring and retrieving security access control rules that protect open metadata.
* **Governance Zone Management**: Defining and managing the hierarchy of governance zones, which are used to control access to assets and other metadata elements.

## Further information

* [Security Officer API Overview](https://egeria-project.org/services/omvs/security-officer/overview/)
* [Governance Zone Concept](https://egeria-project.org/concepts/governance-zone/)
* [Metadata Security Concept](https://egeria-project.org/features/metadata-security/overview/)

Sample REST API requests can be found in the [Egeria-api-security-officer.http](Egeria-api-security-officer.http) file.
Additional sample files for specific scenarios include:
* [Egeria-coco-audit-users.http](Egeria-coco-audit-users.http)
* [Egeria-coco-manage-users.http](Egeria-coco-manage-users.http)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
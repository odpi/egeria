<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 1.11 (Planned August 2020)

Release 1.11 focuses on providing the secured self-service data access point
for a data lake.  There are two main features:

  * **Metadata Controlled Security** where security tags based on the metadata attachments to the assets
    drive the enforcement engine managing access control
  * **Data Virtualization Configuration** where the data virtualization engine that is providing
    the endpoint for the self-service access point.

Below are the highlights:

* There are new access services:
   * The [Governance Engine OMAS](../open-metadata-implementation/access-services/governance-engine) supports the security sync server (below).
   * The [Security Officer OMAS](../open-metadata-implementation/access-services/security-officer) supports the assigning of security tags to assets and their schemas.
   * The [Information View OMAS](../open-metadata-implementation/access-services/information-view) supports the virtualizer server (below).
   
* There are new governance servers:
   * The [Security Sync Server](../open-metadata-implementation/governance-servers/security-sync-services) exchanges security configuration with enforcement engines such as Apache Ranger.
     It calls the Governance Engine OMAS.
   * The [Security Officer Server](../open-metadata-implementation/governance-servers/security-officer-services) determines the security tags for assets.
     It calls the Security Officer OMAS.
   * The [Virtualizer Server](../open-metadata-implementation/governance-servers/virtualization-services) supports the automatic configuration of a data virtualization platform based on the
     Assets that are being cataloged in open metadata.  It consumes notifications
     from the Information View OMAS and stores details of the views it is creating
     through the Data Platform OMAS.   

* New [tutorials](../open-metadata-resources/open-metadata-tutorials),
  [hands-on labs](../open-metadata-resources/open-metadata-labs),
  [samples](../open-metadata-resources/open-metadata-samples) and 
  [open metadata archives](../open-metadata-resources/open-metadata-archives) will demonstrate the new
  capability.
   
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
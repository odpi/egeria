<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 1.3

Release 1.3 focuses on the support for the detection of duplicate
assets and linking them together.

Below are the highlights of the release:

* The first governance server is released:
 
   * The [Stewardship Server](../open-metadata-implementation/governance-servers/stewardship-services) supports the scanning of assets and the notification when duplicate suspects are detected.  It can also orchestrate the linking of duplicate assets.
  
  This server is supported by: 
   * The [Stewardship Action OMAS](../open-metadata-implementation/access-services/stewardship-action) supports the asset scan API, the recording of exceptions and duplicate suspects and the linking of duplicate assets.

* There are new [tutorials](../open-metadata-resources/open-metadata-tutorials),
  [hands-on labs](../open-metadata-resources/open-metadata-labs) and
  [samples](../open-metadata-resources/open-metadata-samples) demonstrating
  the new archive and de-duplication features.

* The Conformance Suite Repository Workbench is now at Version 1.1, with the following enhancements:

   * Tests for relationship searches have moved into a separate, optional RELATIONSHIP_SEARCH profile. A repository connector can be fully conformant with the (mandatory) METADATA_SHARING profile despite not supporting the `findRelationshipsByProperty` or `findRelationshipsByPropertyValue` methods.

   * The ADVANCED_SEARCH profile is now divided into two profiles: ENTITY_ADVANCED _SEARCH and RELATIONSHIP_ADVANCED_SEARCH. They are both optional profiles. A repository connector can be fully conformant with the ENTITY_ADVANCED _SEARCH profile, despite not supporting either of the RELATIONSHIP_SEARCH or RELATIONSHIP_ADVANCED_SEARCH profiles.

   * New test verify the correct handling of mappingProperties in the InstanceAuditHeader.

   * The tests for 'rehome' of a reference copy now use an instance mastered by a third (virtual) repository rather than the CTS Server's repository.

   * Type verification of relationship end types now cater for connectors that do not support all the supertypes of an entity type.

   * Search result checking is improved.

   * The CTS notebook (under open-metadata-resources/open-metadata-labs) has been enhanced:

      * The Conformance Profile Results cell there is additional reporting for the new profiles

      * A new cell "Polling for Status" shows how to determine whether the repository-workbench has completed its synchronous tests. The new API it demonstrates could be used to support automated testing

      * A new cell "Monitoring Progress" show how progress can be monitored based on workbench results retrieved during a test run.

      * Improvements to HTTP response checking and reporting of errors

   
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
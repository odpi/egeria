<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 2.10 (June 2021)

Release 2.10 adds:
* New and improved open metadata types for governance
* New API for Governance Program OMAS

Details of these and other changes are in the sections that follow.

## Description of Changes

### Metadata Types

* Correction to the **Campaign** classification.

  The Campaign classification is supposed to connect to a **Project** entity to indicate the project
  is a grouping of a series of projects designed to achieve a large goal.  Unfortunately it was coded to connect to
  the **Collection** entity.  In 2.10, the Campaign classification has been changed to attach to **Referenceable**
  to allow it to be connected to **Project** without affecting backward compatibility.
  
  See new type description in model [0130](../open-metadata-publication/website/open-metadata-types/0130-Projects.md).
  
* Extension of the **UsedInContext** relationship.

  The UsedInContext relationship linked two **GlossaryTerm** entities together to show that one glossary term described
  the context in which the other was valid.  This relationship has been updated to allow the end that describes
  the context to be a **Referenceable**.  This means that the context where glossary terms are valid can be expressed
  as elements such as projects, business capabilities, parts of an organization, subject areas, zones and many more.

  See new type description in model [0360](../open-metadata-publication/website/open-metadata-types/0360-Contexts.md).
  
* Allowing ownership to be expressed as a **PersonRole**.

  Issue [#5104](https://github.com/odpi/egeria/issues/5104) described the problem that the
  governance open metadata types allowed owners to be expressed as **PersonalProfile** or **UserIdentity**
  entities.
  The properties did not identify if the owner was identified as a GUID or as a qualifiedName.
  In 2.10, a new classification for expressing ownership called, not surprisingly, **Ownership**,
  was introduced which allows and type and property name for the owner to be specified with the
  owner's identifier.  The types for entities such as **GovernanceActionType** and **IncidentReport** that include
  ownership properties in them have been updated to deprecate these properties in favor of using
  the Ownership classification.  The **AssetOwnership** classification is also deprecated.

  See new type description in model [0445](../open-metadata-publication/website/open-metadata-types/0445-Governance-Roles.md).

* Update to **Certification** relationship.

  The Certification relationship supports the identification of the people involved in the certification of an element.  2.10 adds
  new attributes to specify the type and property names of the identifier used to identify these people.
  This is consistent with the new **Ownership** classification.
  
  See new type descriptions in model [0440](../open-metadata-publication/website/open-metadata-types/0440-Organizational-Controls.md).

* Update to **License** relationship.

  The License relationship supports the identification of the people involved in the licencing of an element.  2.10 adds
  new attributes to specify the type and property names of the identifier used to identify these people.
  This is consistent with the new **Ownership** classification.
  
  See new type descriptions in model [0440](../open-metadata-publication/website/open-metadata-types/0440-Organizational-Controls.md).

* Governance role updates.

  The **GovernanceOfficer** entity now inherits from **GovernanceRole** rather than **PersonRole**.
  This means it is just another governance role that can be linked with **GovernanceResponsibility**
  definitions.  In addition there are new types for different types of owners.  These are **ComponentOwner**
  and **DataItemOwner**.

  See new type descriptions in model [0445](../open-metadata-publication/website/open-metadata-types/0445-Governance-Roles.md).
  
* Extend the **GovernanceResponsibilityAssignment**

  The GovernanceResponsibilityAssignment relationship identifies the responsibilities for a particular role.
  It used to link to a **GovernanceRole** and in 2.10, it has been updated to link to a **PersonRole**.  This means
  that governance responsibilities can be added to any role, such as **ProjectManager** and **CommunityMember**,
  rather than just specialized governance roles.
  
  See new type descriptions in model [0445](../open-metadata-publication/website/open-metadata-types/0445-Governance-Roles.md).

* Deprecating the **domain** attribute in various governance types.

  The domain attribute is typed by the **GovernanceDomain** enum.  This provides a fixed list of governance domains.
  An extensible mechanism for expressing the governance domain was added in release 2.4 using the
  **GovernanceDomainDescription** entity and the **domainIdentifier** attribute.
  In this release, the use of **domain** is deprecated in **GovernanceDefinition**,
  **GovernanceZone**, **SubjectAreaDefinition**, **GovernanceMetric**, **GovernanceRole** and **GovernanceOfficer**.
  
  See new type descriptions in model [0401](../open-metadata-publication/website/open-metadata-types/0401-Governance-Definitions.md).

* Extensions to governance drivers.

  There are now new subtypes of the **GovernanceDriver** entity called **RegulationArticle** and
  **BusinessImperative**.  It is also possible to link governance drivers using the new
  **GovernanceDriverLink** relationship.
  
  See new type descriptions in model [0405](../open-metadata-publication/website/open-metadata-types/0405-Governance-Drivers.md).

* Update to **AssetOrigin** classification.

  The AssetOrigin classification supports the identification of the origin of an asset.  This can be in terms of the
  organization, business capability and other values.  2.10 adds new attributes to identify if the
  organization or business capability is identified by its GUID or qualifiedName.
  
  See new type descriptions in model [0440](../open-metadata-publication/website/open-metadata-types/0440-Organizational-Controls.md).

* Improve IncidentClassifiers.

  If is possible to define multiple sets of **IncidentClassifier** values that can be
  used on **IncidentReport** entities to help to group and prioritize them.  The IncidentClassifier entity
  now inherits from **Referencable** and there is a new classification called **IncidentClassifierSet**
  to mark a **Collection** entity as containing IncidentClassifier definitions.

  See new type descriptions in model [0470](../open-metadata-publication/website/open-metadata-types/0470-Incident-Reporting.md).
  
* Deprecation of **ResponsibilityStaffContact**.

  The ResponsibilityStaffContact relationship has been deprecated in favor of the
  **GovernanceResponsibilityAssignment** relationship.

### New Services for Governance Program OMAS

The APIs defined for Governance Program OMAS have been updated to reflect the changes
to the open metadata types described above.  The APIs and client implementations
are in place.  The server-side is coming in a future release.

### Bug fixes and other updates

* Additional Bug Fixes

  * Cascaded deletes for entities grouped using the **Anchors** classification
    are now deleting the correct entities.  Prior to this release, some entities were
    missed and others were deleted incorrectly.
    
* Dependency Updates

For details on both see the commit history in GitHub.

## Known Issues

* It is recommended to use a chromium-based browser such as Google Chrome or Microsoft Edge, or Apple Safari for the Egeria React UI. Some parts of the UI experience such as Dino currently experience problems with Firefox. See [odpi/egeria-react-ui#96](https://github.com/odpi/egeria-react-ui/issues/96) .
* When running the 'Understanding Platform Services' lab, ensure you run the 'egeria-service-config' notebook first and do not restart the python kernel before running this lab. See [#4842](https://github.com/odpi/egeria/issues/4842) .

# Egeria Implementation Status at Release 2.10

![Egeria Implementation Status](../open-metadata-publication/website/roadmap/functional-organization-showing-implementation-status-for-2.10.png#pagewidth)

Link to Egeria's [Roadmap](../open-metadata-publication/website/roadmap) for more details about the
Open Metadata and Governance vision, strategy and content.


# Further Help and Support

See the [Community Guide](../Community-Guide.md).

----
* Return to [Release Notes](.)
   
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

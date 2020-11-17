<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->


# 0425 Subject Areas

Subject areas are topic areas that are important to the organization.  Typically they cover data that
is widely shared across the organization and there is business value in maintaining consistency in
the data values in each copy.  The role of the subject area definition is to act as an anchor
for all of the subject area materials.  This helps to coordinate the
efforts to build the common definitions and standards for each subject area.

Each subject area has an owner who is responsible for the
common definitions relating to the subject area.  Often the subject area owner is a senior person in the
organization with expertise in the subject area.  He/she
coordinates other subject matter experts to author and maintain the common definitions and standards.

This includes:

* A [glossary of terms](0310-Glossary.md) that describe the key concepts in the subject area.
* Lists and hierarchies of [reference data](0545-Reference-Data.md) that relate to particular data values
  in the subject area.
* Quality rules for specific data values in the subject area.
* Preferred data structures and schemas.

The effort required to author and maintain these definitions, plus the governance process required to
ensure they are used wherever appropriate is offset by the savings in managing and using the shared
data associated with the subject area.

## Open Metadata Types

The **SubjectAreaDefinition** provides the description of the subject area.  By creating this definition,
is a declaration that data about this subject area is of significance to the organization and
will be receiving special attention.

A subject area may be sub-divided into more specific subject areas.  The subject areas can be linked together
into a hierarchy using **SubjectAreaHierarchy** relationships.

The subject area definition can be linked to the [governance definitions](0401-Governance-Definitions.md)
via the **SubjectAreaGovernance** relationship.  An organization can create
governance definitions that are applicable to  all subject areas,
or are specific to the subject area they are linked to.  Typically they will have a mixture of these.

Finally the content for the subject area (glossaries, reference data, schemas etc) are identified
using the **SubjectArea** classification that carries the name of the subject area it belongs to.
This classification makes it easy to locate all of the subject area's content.


![UML](0425-Subject-Areas.png#pagewidth)

## Support for Subject Areas in Egeria

The following Open Metadata Access Services (OMASs) support
subject areas:

* [Governance Program OMAS](../../../open-metadata-implementation/access-services/governance-program)
  supports the maintenance of the hierarchy of subject area definitions
  and the associated governance definitions.

* [Subject Area OMAS](../../../open-metadata-implementation/access-services/subject-area) supports the subject matter experts in building
  the glossary and valid values for a subject area.
  
* [Digital Architecture OMAS](../../../open-metadata-implementation/access-services/digital-architecture)
  is for use by technical architects to define reference data, common data models and schema
  and rule implementations for the data and associated processing of the subject area.

## Further Information

The [Coco Pharmaceuticals case study](https://opengovernance.odpi.org/coco-pharmaceuticals/) includes
* [Planning for Common Data Definitions](https://opengovernance.odpi.org/coco-pharmaceuticals/scenarios/planning-for-common-data-definitions/)
* [Defining Subject Area](https://opengovernance.odpi.org/coco-pharmaceuticals/scenarios/defining-subject-areas/)


----
Return to [Area 4](Area-4-models.md).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
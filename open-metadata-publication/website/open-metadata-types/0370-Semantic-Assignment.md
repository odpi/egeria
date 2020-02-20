<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0370 Semantic Assignment

**SemanticAssignment** is a relationship used to assign a term
to a [referenceable](0010-Base-Model.md) object.
This means that the term describes the meaning of the 
referenceable object.  The semantic assignment needs to be a controlled relationship when glossary definitions are used to provide classifications for the data assets and hence define how the data is to be governed. 

![UML](0370-Semantic-Assignment.png#pagewidth)

Thus **TermAssignmentStatus** defines how much the semantic assignment should be trusted.  The relationship is created by the user (person or engine) identified by the createdBy attribute.  The confidence attribute in the relationship stores the level of confidence (0-100%) in the correctness of the relationship - it is typically used by discovery engines.   The steward is the person responsible for assessing the relationship and deciding if it should be approved or not.

* DISCOVERED - this semantic assignment was added by a discovery engine.
* PROPOSED - this semantic assignment was proposed by person - they may be a subject mater expert, or consumer of the Referenceable asset.
* IMPORTED - the relationship has been imported from outside of the open metadata cluster.
* VALIDATED - this relationship has been reviewed and is highly trusted.
* DEPRECATED - this relationship is being phased out.  There may be another semantic relationship to the Referenceable that will ultimately replace this relationship.
* OBSOLETE - this relationship is no longer in use.
* OTHER - the status of the relationship does not match any of the other term status values.  The description field can be used to add details about the relationship.


Return to [Area 3](Area-3-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
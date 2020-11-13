<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0380 Spine Objects

The spine object model adds the relationships that enable a
glossary to contain the definition of spine objects that can be
used to control access to data, and the guild the design of new
data stores and APIs.

Model 0380 shows the relationships and classifications used to
describe spine object.

![UML](0380-Spine-Objects.png#pagewidth)

* **SpineObject** - is a classification to say the term represents a type of object
* **SpineAttribute** - is a classification to say the term represents a type of attribute that is common for a spine object.
* **ObjectIdentifier** - is a classification saying that a term is typically an identifier attributed for a spine object.

Note that a term may be a spine object and/or a spine attribute and/or an object identifier at the same time.

* **TermHASARelationship** - is a term relationship between a term representing a SpineObject and a term representing a SpineAttribute.
* **TermISATYPEOFRelationship** - is a term relationship between two SpineObjects saying that one is the subtype (specialisation) of the other.
* **TermTYPEDBYRelationship** - is a term relationship between a SpineAttribute and a SpineObject to say that the SpineAttribute is implemented using a type represented by the **SpineObject**.


Return to [Area 3](Area-3-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0019 More Information

The **MoreInformation** relationship enables Referenceables of different subtypes
to be associated in a way that indicates that one provides more detail about another.
It can be used to show linkage between a glossary and the primary top level category, between
a glossary term and its implementation.  It is a looser association that a relationship
such as [Semantic Assignment](0370-Semantic-Assignment.md).

![UML](0019-More-Information.png#pagewidth)

The [Asset Manager Open Metadata Access Service (OMAS)](../../../open-metadata-implementation/access-services/asset-manager)
makes use of this relationship to link an asset to a glossary term that is providing supplementary properties
to the asset.

----

* Return to [Area 0](Area-0-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# 0012 - Search Keywords

Some **Referenceables** such as [Glossary Terms](0330-Terms.md) have a lot of text in their definitions
making them easy to find using search strings.  [Assets](0010-Base-Model.md) definitions, on the other hand,
typically have less text.  Even the schema associated with the asset
might only contain technical terms and they are often abbreviated.  The result is that
assets can be hard for consumers to find.

It is possible to link assets and schemas to glossary terms to make
them findable by association.  However, this linkage is a formal semantic relationship.
Consumers of the assets can add [Informal Tags](0150-Feedback.md) to the assets and schemas
but only once they have found them.  The search keywords provide a mechanism to allow the asset owner to tag
the asset - and linked elements such as the schema - with a variety of keywords that can be matched
during a search.  This helps to boost relevant assets to he top of the search results.

![UML](0012-Search-Keywords.png#pagewidth)


The **SearchKeyword** entity stores the definition of the search keyword.  It is linked to the
assets and their associated elements using the **SearchKeywordLink** relationship.  Related keywords can be
linked together using the **RelatedKeyword** relationship to allow simple synonym type expansions
of the search.

Search keywords can be added manually through the Asset Owner OMAS.
Some files such as documents and photos may
have keywords embedded in them.  These can be automatically discovered through
metadata discovery and stored in their corresponding asset properties
using these same search keyword elements.


## More information

* [Asset Owner Open Metadata Access Service (OMAS)](../../../open-metadata-implementation/access-services/asset-owner)
* [Automated Metadata Discovery](../metadata-discovery)

----
Return to [Area 0](Area-0-models.md).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
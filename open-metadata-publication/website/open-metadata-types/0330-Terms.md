<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0330 Terms

The vocabulary for the glossary is documented using terms.
Each term represents a concept of short phrase in the vocabulary.
Just like a category, a term is owned by a glossary but can be
linked into a category from any glossary.
Model 0330 shows the glossary term.

![UML](0330-Terms.png#pagewidth)

* **GlossaryTerm** represents a term in a glossary. 
* **TermAnchor** links each term to exactly one Glossary object.
This means that this is its home glossary.
If the Glossary object is deleted then so are all of the terms linked to it.
* **TermCategorization** is a relationship used to organize terms into categories.
A term may be linked with many categories and a category may have many terms linked to it.
This relationship may connect terms and categories both in the same glossary and in different glossaries.
* **LibraryTermReference** provides reference information for how this term corresponds to a term in an external glossary.

Return to [Area 3](Area-3-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
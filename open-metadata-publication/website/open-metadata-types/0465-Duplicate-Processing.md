<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0465 Duplicate Processing

The **KnownDuplicateLink** is use to link two elements that
represent the same "thing".  For example, if the two elements
that this relationship linked were assets, it would mean that they
both has the same real-world counterpart.

The elements linked by KnownDuplicateLink also have the
**KnownDuplicate** classification to alert the processing that is retrieving the
metadata that it should look for duplicates.

![UML](0465-Duplicate-Processing.png#pagewidth)

The **DuplicateType** Indicates the type of duplicate processing that
created the KnowDuplicateLink.

* **PEER** - means the two elements have simply been linked together.
* **CONSOLIDATED** - means that the element at end2 is an\element that has been created
by consolidating the properties of a number of duplicate entities.


Return to [Area 4](Area-4-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
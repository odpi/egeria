<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0531 Document Schemas

Model 0531 shows the definitions for structured documents such as JSON or XML.

Note that the type information for each attribute within these structures can be directly embedded
on the **DocumentSchemaAttribute** through the [TypeEmbeddedAttribute](0505-Schema-Attributes.md) classification. Also
recall that the [NestedSchemaAttribute](0505-Schema-Attributes.md) relationship can be used to capture nested
(hierarchical) structures within such documents.

![UML](0531-Document-Schemas.png#pagewidth)



## Deprecated Types

The **SimpleDocumentType**, **StructDocumentType** and **MapDocumentType** types have been deprecated because they
  offer little value since the type is typically stored in the **TypeEmbeddedAttribute** classification.
  This change makes the document schemas consistent with other types of schema.

---

* Return to [Area 5](Area-5-models.md).
* Return to [Overview](.).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
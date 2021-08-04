<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0512 Derived Schema Elements

Derived schema elements access other data, possibly in the same or a different
asset to generate/derive the values for the attribute.  Examples of this are database views and derived database columns.

The **CalculatedValue** classification contains the formula.
There are optional links to **DerivedSchemaTypeQueryTarget** to identify which schema elements are accessed by the formula.  
The formula may contain placeholders that refer to the **queryId**
in **DerivedSchemaTypeQueryTarget**.

![UML](0512-Derived-Schema-Elements.png#pagewidth)


## Deprecated Types

The following types were replaced by **DerivedSchemaTypeQueryTarget**.

* **DerivedSchemaAttribute**
* **SchemaLinkElement**
* **LinkedType**
* **SchemaLinkToType**

---

* Return to [Area 5](Area-5-models.md).
* Return to [Overview](.).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
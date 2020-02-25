<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0545 Reference Data

Reference data provides authoritative definitions of valid values for
data.

The list of valid values can be modelled directly in open metadata using
the **ValidValuesSet** entity with one or more ValidValue entities linked off of it.
There is one **ValidValue** entity instance for each of the valid values.

Typically the valid values set is linked off of a [GlossaryTerm](0330-Terms.md)
or a [SchemaElement](0501-Schema-Elements.md).

A valid values set can also be implemented in an asset that can be used as a look up
table while data is being processed.

![UML](0545-Reference-Data.png#pagewidth)


Return to [Area 5](Area-5-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
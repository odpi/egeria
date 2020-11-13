<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0503 Asset Schema

Model 0503 shows the relationship between an [Asset](0010-Base-Model.md)
and a [SchemaType](0501-Schema-Elements.md).

When an asset is linked to a schema type it means that the schema type describes
the structure of the Asset. 

The schema type can not be shared between different assets.
If you are cataloging multiple assets with the same data structure, you can use
a template which will copy the schema structure from the template asset to the new asset.
(See [Templated Cataloging](../cataloging-assets/templated-cataloging.md) for more details.

![UML](0503-Asset-Schema.png#pagewidth)


Return to [Area 5](Area-5-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
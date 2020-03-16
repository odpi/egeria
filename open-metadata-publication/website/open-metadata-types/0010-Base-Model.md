<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0010 Base Model

The base model defines key concepts such as Referenceable, Asset,
Infrastructure, Process and DataSet.


![UML](0010-Base-Model.png#pagewidth)

Referenceable is the super type for many of the open metadata entity
types. A referenceable is something that is important enough to
be assigned a unique (qualified) name within its type.
This unique name is often used outside of the open metadata
ecosystem as its unique identifier.
Referenceable also has provision for storing additional properties.

Referenceables can have chains of related feedback and additional knowledge attached to
them.  The LastAttachment entity is a convenience mechanism to
indicate where the last change occurred.

Asset represents the most significant type of referenceable.
An asset is something (either physical or digital) that is of
value and so needs to be managed and governed.

Infrastructure, Process and DataSet are examples of Assets.
More information on assets can be found
[here](../../../open-metadata-implementation/access-services/docs/concepts/assets).

Return to [Area 0](Area-0-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
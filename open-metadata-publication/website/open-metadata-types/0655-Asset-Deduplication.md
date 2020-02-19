<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2019. -->

# 0655 Asset Deduplication

In the open metadata ecosystem, it is possible that the same 
[asset](../../../open-metadata-implementation/access-services/docs/concepts/assets) has been defined in
more than one tool/engine/platform.
When these technologies are connected together and exchange metadata, there are then multiple asset definitions for
the same physical asset.  It is often necessary to keep each original copy since they are needed by the originating technology.
However, the values of the metadata and attachments can diverge over time - or may already be different at the time
that the assets are exchanged).  If these values are being used for governance, or there is a desire to
keep descriptions and other types of attachments synchronized across the copies, then the open metadata
and governance (OMAG) services need to detect these duplicates and monitor divergent values.

The annotations shown below are used by discovery services to record first that two or more
[Asset entities](0010-Base-Model.md) seem to
describe the same (real/physical) asset.  This is the `SuspectDuplicateAnnotation`.  When this annotation
is recorded, it is processed by the stewardship **Governance Actions** to decide if they are really duplicates or
not.

Where duplicates have been established, discovery services can use the remaining annotation to
record divergent values.  Again the stewardship governance actions will determine what action, if any, to take.

Specifically, `DivergentValueAnnotation`, `DivergentClassificationAnnotation` and `DivergentRelationshipAnnotation`
are used to record divergent values in the Asset entities' properties, classifications and direct relationships
respectively.  `DivergentAttachmentValueAnnotation`, `DivergentAttachmentClassificationAnnotation` and
`DivergentAttachmentRelationshipAnnotation` record divergent values in attachments such as schemas, feedback and
connections.

![UML](0655-Asset-Deduplication.png#pagewidth)


Return to [Area 6](Area-6-models.md).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
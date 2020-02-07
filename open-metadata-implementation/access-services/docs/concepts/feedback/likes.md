<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Like

A "like" is an attachment that can be made to a [referenceable](../referenceable.md)
such as an [asset](../assets), a personal message, personal note,
forums and forum contributions, comments, teams and communities.

It indicates that the person attaching the like approves of the
referenceable.   It is effectively the simplest type of [review](reviews-and-star-ratings.md).

## Further reading

* The [Asset Consumer OMAS](../../../asset-consumer) enables the consumer of an asset to
add a like to an asset.
These likes are then visible to other asset consumers through the
[OCF Metadata Management APIs](../../../../common-services/ocf-metadata-management).
The [asset owner](../user-roles/asset-owner.md) can also see the
likes using the [Asset Owner OMAS](../../../asset-owner).

* A like is stored as a
[**Like** open metadata type](../../../../../open-metadata-publication/website/open-metadata-types/0150-Feedback.md).

* A like can be attached to any [referenceable](../referenceable.md). The relationship
that attaches the like to the referenceable treats the referenceable as the like's
[anchor](../anchor.md).  This means the like is deleted when the referenceable is deleted.




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
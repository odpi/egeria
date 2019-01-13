<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Reviews and star ratings

A review provides more detailed feedback than a simple [like](likes.md).
It includes the ability to add a star rating (one to five stars or "not recommended")
and a review comment.

## Further Reading

* The [Asset Consumer OMAS](../../../asset-consumer) enables the consumer of an asset to
add a review to an asset.
These reviews are then visible to other asset consumers through the
[Connected Asset OMAS](../../../connected-asset).
The [asset owner](../user-roles/asset-owner.md) can also see the
reviews using the [Asset Owner OMAS](../../../asset-owner).

* The [Community Profile OMAS](../../../community-profile) enables
the owner of a personal profile or the administrator of a community
are able to remove inappropriate or out-of-date comments
attached to their personal profile or community respectively.

* A comment is stored as a
[**Comment** open metadata type](../../../../../open-metadata-publication/website/open-metadata-types/0150-Feedback.md).

* A comment can be attached to any [referenceable](../referenceable.md). The relationship
that attaches the comment to the referenceable treats the referenceable as the comment's
[anchor](../anchor.md).  This means the comment is deleted when the referenceable is deleted.




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
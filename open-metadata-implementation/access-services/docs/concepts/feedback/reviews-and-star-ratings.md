<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Reviews and star ratings

A review provides more detailed feedback than a simple [like](likes.md).
It includes the ability to add a star rating (one to five stars or "not recommended")
and a review comment.

## Further reading

* The [Asset Consumer OMAS](../../../asset-consumer) enables the consumer of an asset to
add a review to an asset.
These reviews are then visible to other asset consumers through the
[OCF Metadata Management APIs](../../../../common-services/ocf-metadata-management).
The [asset owner](../user-roles/asset-owner.md) can also see the
reviews using the [Asset Owner OMAS](../../../asset-owner).

* A review is stored as a
[**Rating** open metadata type](../../../../../open-metadata-publication/website/open-metadata-types/0150-Feedback.md).

* A review can be attached to any [referenceable](../referenceable.md). The relationship
that attaches the review to the referenceable treats the referenceable as the review's
[anchor](../anchor.md).  This means the review is deleted when the referenceable is deleted.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Comments

Comments are informal messages or feedback.  They can be attached to many places, for example
an [asset](../assets),
a [personal note](../../../community-profile/docs/concepts/personal-notes.md),
a [community](../../../community-profile/docs/concepts/community.md), 
a [community forum or community forum contribution](../../../community-profile/docs/concepts/community-forum.md),
a [review](reviews-and-star-ratings.md)
or another comment.

Sometimes specific names are used for comments depending on what
they are attached to.  For example:
 * a **personal note comment** is a comment attached to a personal note.
 * a **forum comment** is a comment attached to a community forum either directly or via a community forum contribution.
 * an **asset comment** is a comment attached to an asset.
 * a **comment reply** is a comment attached to another comment.  

The ability to reply to a comment (or a comment reply) means that comments can be chained
together to show a detailed conversation on a topic.

## Further Reading

* The [Asset Consumer OMAS](../../../asset-consumer) enables the consumer of an asset to
add comments to an asset.
These comments are then visible to other asset consumers through the
[OCF Metadata Management APIs](../../../../common-services/ocf-metadata-management).
The [asset owner](../user-roles/asset-owner.md) can also see the
comments using the [Asset Owner OMAS](../../../asset-owner).

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
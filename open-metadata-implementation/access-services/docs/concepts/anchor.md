<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Anchor

Anchors are metadata entities that group other entities together.
They act like containers.  This means, for example, if the anchor is deleted then
the entities linked to the anchor are also deleted.

For example, if a [personal message](../../community-profile/docs/concepts/personal-message.md) is attached to
a [personal profile](../../community-profile/docs/concepts/personal-profile.md) then that personal profile is its anchor.
If the personal profile is deleted then the personal message is deleted too (but not the resources themselves).

The anchor relationship is specific to particular types of metadata entities.
So not all metadata entities linked to an anchor are deleted when the anchor is deleted.

For example, a personal profile 



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
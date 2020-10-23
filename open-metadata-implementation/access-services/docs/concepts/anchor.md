<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Anchor

**Anchors** are [Referenceable](referenceable.md) metadata entities that group other entities together.
They act like containers.  This means, for example, if the anchor entity is deleted then
the entities anchored to this entity are also deleted.  

The value of establishing this grouping is to ensure that entities that have little meaning without their
anchor entity are cleaned up properly and are not left to uselessly clutter the repository.
For example, if a [personal message](../../community-profile/docs/concepts/personal-message.md) is attached to
a [personal profile](../../community-profile/docs/concepts/personal-profile.md) then that personal profile is its anchor.
If the personal profile is deleted then the personal message is deleted too.

Anchored entities are also bound by the visibility and security restrictions of their anchor. 
For example, [Asset](assets) visibility is controlled by [Governance Zones](governance-zones).
An Asset is only visible through a service if it is a member of that service's **supportedZones**.  Similarly
authorization to perform specific operations on an Asset is granted by the
[Open Metadata Security Services](../../../common-services/metadata-security).
When a SchemaType is attached to an Asset, it is anchored to that Asset.
Subsequent requests to read or update the SchemaType will result in visibility
and authorization checks for the requesting user being made with respect to its Asset anchor.

The anchor grouping is limited to particular types of metadata entities that are only
meaningful in the context of their anchor entity.
So not all metadata entities linked to an anchor are anchored to it.  They have an independent
existence and may be linked to many anchors, without obligation.

For example, a personal profile may contain links to specific "favorite" Assets.
When the personal profile is deleted, the assets are not effected - except that they loose
their relationship to the personal profile.

## LatestChange and Anchors classification

The **LatestChange** classification is attached to each anchor Referenceable.
It is used to record the latest change to this anchor or any of the entities anchored to it.

So for example, if a hierarchy of SchemaElements,
or a hierarchy of Comments (all Referenceables) were anchored to an Asset
(also a Referenceable), then the LatestChange classification goes on the
Asset and records changes to any of these entities.
This includes changing property values, attaching or detaching entities through relationships
as well ans any changes to their classifications.

Maintaining "LatestChanges" on Asset means that it is
easier to monitor for changes affecting the Asset and any of its anchored entities.
However it also means that it must be easy to located the Asset
from any of the anchored entities when they change, even though they may not be directly connected.

The **Anchors** classification makes it easier to find the anchor entity.
It is attached to any entity anchored to a Referenceable.
In the example above, Anchors would be on all of the
SchemaElements and Comments.
Anchors would contain the unique identifier (guid) of the anchor Referenceable.
There is also the unique identifier (guid) of the SchemaType at the root of a schema structure and the
unique identifier (guid) of the Comment at root of a comment tree to make it easier to process these elements
as a group.

Figure 1 shows an example Asset with a number of entities anchored to it.
The entities that have the Anchors classification are those that are
anchored to the Asset.
This includes entities such as Ratings, Likes and Attachments
(from the [Open Discovery Framework (ODF)](../../../frameworks/open-discovery-framework).
It is worthwhile maintaining the Anchors classification because reads of and updates to the anchored
entities will happen many times, and it is rare that an anchored entity will change its anchor during its life time.

![Figure 1](anchors-classifications-on-dependant-objects.png#pagewidth)
> **Figure 1:** Examples of dependent entities that are anchored to an Asset

If a GlossaryTerm, or InformalTag is attached to the Asset, they are not anchored to it.
GlossaryTerms and InformalTags are independent entities.
They are not anchored to the Asset and hence do not have an Anchors classification.
Any change to these entities does not reflect in the LatestChange classification of the Asset.
However, the act of attaching them to, or detaching them from the Asset is recorded
in the Asset's LatestChange classification.  Since the GlossaryTerm is also an anchor,
when the GlossaryTerm and the Asset are linked
together, this change is reflected in both of their LatestChange classifications because
they are both Referenceable anchors.

NoteLogs are Referenceables that can be attached to many other Referenceables.
They can be set up either to be anchored with a single Referenceable or to
be their own anchor to allow then to be attached to and detached from
many Referenceables over its lifetime.

For example, these are cases where the NoteLog is anchored to another Referenceable

* NoteLogs are used to support the personal blog linked off of the Personal Profile in Community Profile OMAS.
* Assets may have a NoteLog to record "news" for consumers such as planned maintenance and unexpected situations.

Egeria uses the Anchors classification on a NoteLog to indicate that the NoteLog is tied to the Referenceable it is attached to. 
The presence of this classification would prevent it from being linked to another Referenceable.

Figure 2 illustrates additional objects connecting to an asset that do not have the Anchors classification
because they are not anchored to the Asset.
Also notice there are two NoteLogs attached to the asset,
one with the Anchors classification and one without.
The one with the Anchors classification is anchored to the the Asset. The one without the Anchors classification is
independent of the Asset.

![Figure 2](anchors-classifications-on-attached-objects.png#pagewidth)
> **Figure 2:** Examples of other types of entities that are linked to an Asset but not necessarily anchored there

## Further information

* [Generic Handlers](../../../common-services/generic-handlers) provide support for the
  Anchors and LatestChange classifications.
* [Open Metadata Type definitions for LatestChange and Anchors classification](../../../../open-metadata-publication/website/open-metadata-types/0011-Managing-Referenceables.md)
* [Open Metadata security checks for Assets and Connections](../../../common-services/metadata-security/metadata-security-apis)
* [Governance Zones and Assets](governance-zones)

----
* Return to [Access Service Concepts](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
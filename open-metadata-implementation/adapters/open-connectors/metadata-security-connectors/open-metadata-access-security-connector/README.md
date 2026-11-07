<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Security Access Connectors

The connector in this module provides a combined implementation of the [open metadata security connectors](https://egeria-project.org/features/metadata-security/overview/)
that can be added to the OMAG Server Platform and to any OMAG server
running on the platform.  The connector makes use of externalized security information
supplied by an embedded [Secrets Store Connector](https://egeria-project.org/concepts/secrets-store-connector/).

There is a sample YAML-based secrets store called `coco-user-directory.omsecrets` included in Egeria's 
[deployment](../../../../../open-metadata-resources/open-metadata-deployment/secrets).
With this sample secrets store, it is possible to experiment with the use of 
[governance zones](https://egeria-project.org/concepts/governance-zone)
to control the visibility of elements
through the open metadata services as well as more standard authorization services base around the platform/server/service called.

The open metadata platform security connector provides authorization
services for the platform services and the admin service to create a new server.
The open metadata server security connector is specific to an OMAG server instance and
is defined in the configuration document for a server.

This connector illustrates how a security connector implementation extends the appropriate base class from 
[metadata-security](../../../../common-services/metadata-security) and uses their methods to provide
a security service.

Features of this implementation are described below.

## User security

The user security methods pass through all user account management and verification to the nested secrets store connector.
The other security methods validate that that calling user has a valid user account before performing any specific checks.

## Governance Zones

Governance zones are supported through the element security methods.
Each governance zone is modelled as a security access control.
The zone itself represents a collection of metadata.
The security access control defines which users can perform which operation.
There is a DEFAULT operation that means, all operations not explicitly defined.

An element with no zones is accessible to every user with an active account.
An element in one or more zones is accessible if any one of its zones permits the operation.
A zone with no security access control in the user directory, or whose control has neither a list for the
operation nor a DEFAULT list, permits no-one - so every governance zone in use must be defined in the user directory.
The exception is a zone named after a user, which is that user's own.

This connector is the only component that applies the personal-zone rule.  The rest of the server treats zone
names as opaque, so the rule is implemented in both of the places where the connector reports on zones:

* `validateZoneAccess` permits any operation through a zone whose name matches the calling user's userId.
* `getReadableZonesForUser` always adds the calling user's userId to the list of zones the user can read (see below).

A replacement security connector that wants personal zones must implement the rule in both places.

### Filtering searches by zone

By default, the connector returns, from `getReadableZonesForUser`, the list of zones whose elements the calling
user could read.  The server adds a condition to each search so that the repository leaves out elements governed
by zones that are not on this list.  Every element returned is still checked by `validateZoneAccess`, so the list
may contain zones the user can only read for some elements (such as those they own), but it must never leave out
a zone the user can read.  An element in no zone is returned to every user.

The list is built from:

* the user's personal zone;
* each zone whose READ list (or DEFAULT list, if there is no READ list) names the user's account through
  `allUsers`, the account type group, a security group or a security role;
* each zone whose list names an element-dependent group: the user's userId, or the instance owners, existing
  maintainers or new maintainers groups.

Zones without a security access control permit no-one, so they are never on the list.  The connector asks
each secrets store for its full set of security access controls.  If any store can not list them, no list is
returned and searches are not filtered.

The filter relies on the effective zones that the server keeps in each element's Anchors classification.
Anchors classifications written before the server maintained these zones, or loaded from an open metadata archive,
can be out of date.  **Until they are refreshed, some users may see less metadata than before**: an element whose
Anchors classification lists the wrong zones is left out of the searches of users who are entitled to see it.
Reading the element by its unique identifier is not affected.

To refresh existing Anchors classifications, run the `resynchronize-anchors-zones` request type of the Egeria
Governance Engine (the *Anchors Zone Resynchronizer* governance action service in the core content pack) with no
action targets.  It checks every Anchors classification in the repository and corrects those that differ from the
zones that govern their element.  Run it after upgrading, and again after loading an open metadata archive.
It completes with:

* `anchors-zones-corrected` if it corrected any classifications - run it again to confirm;
* `anchors-zones-up-to-date` if every classification was already correct;
* `anchors-zones-resync-failed` if any element could not be checked or corrected.  The corrections it could make
  are saved, and the server's audit log has an `OMAG-GENERIC-HANDLERS-0001` message for each element it could not
  correct.

The same check is available directly as `resynchronizeAnchorsZoneMembership` on the Open Metadata Store.  Each
correction is sent to the repository named in the classification header, so reference copies from other cohort
members are corrected through the member that owns them.

Set `pushDownZoneFilter` to `false` to search without the filter.  This restores the earlier behaviour while the
Anchors classifications are refreshed.  It is also a way to check whether they need refreshing: if a user finds
elements with the property set to `false` that they do not find with it set to `true`, run the governance action
service.


## Connection security

Finally, the connector will only permit a connector with 
to access Connection object that have security information in it such as userIds and passwords.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
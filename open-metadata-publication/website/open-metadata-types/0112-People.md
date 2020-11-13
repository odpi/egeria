<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0112 People, their personal network and their roles

The [ActorProfile](0110-Actors.md) is extended to capture more information about a person.
This is recorded in the **Person** entity.

Open metadata supports
[Karma Points](../../../open-metadata-implementation/access-services/community-profile/docs/concepts/karma-point.md).
These are awarded for participation in the collaboration
around open metadata.
The number of karma points awarded to the individual is
recorded in their **Person** entity.

Person entities can be linked together to a list of a person's close/important colleagues.
The perspective on who is a
close/important colleague is a personal perspective.
Therefore the **Peer** relationship separates the concept of
who has linked to a person (**myFollowers**) from
who they have specifically linked to (**myPeers**).

Open metadata also separates the person from the roles they perform.
This is because people often perform many roles and these change over time.
Also roles may be put in place before the person is appointed to it and
the person appointed can change from time to time.

The **PersonRole** entity is linked to a **Person** entity with the
**PersonRoleAppointment**
relationship to show that the person has been appointed.

The PersonRole entity is extended in multiple places to show different types of roles.
For example:
* **Team Leader** and **Team Member** in [0015 Teams](0115-Teams.md)
* **Governance Role** in [0445 Governance Roles](0445-Governance-Roles.md)
* **Project Manager** in [0130 Projects](0130-Projects.md)

Figure 1 captures the profile for a person along with their roles and peer network.

![UML](0112-People.png#pagewidth)
> Figure 1: Describing the profile for a person


The Community Profile OMAS provides support for a person's profile.
See [personal profile](../../../open-metadata-implementation/access-services/community-profile/docs/concepts/personal-profile.md).
It also supports the ability to query a person's roles
(see [personal roles](../../../open-metadata-implementation/access-services/community-profile/docs/concepts/personal-roles.md))
and their peer network (see [peer network](../../../open-metadata-implementation/access-services/community-profile/docs/concepts/personal-roles.md))

Return to [Area 1](Area-1-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
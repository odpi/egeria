<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Community Profile Open Metadata Access Service (OMAS)

The Community Profile OMAS provides APIs and events for tools and applications managing
the personal profiles, teams and communities for people collaborating around data.


## Personal profiles

A personal profile provides a place for an individual to share information about themselves
with the other people they are collaborating with.  It is associated with one or more
of the person's userIds.

There are two sets of APIs related to profiles:

* the "my-profile" API supports the management of the personal profile associated
with the requesting user.

* the "profiles" API supports an administrator creating and deleting profiles for other
people (individuals are expected to update their own profile using the my-profile API).
It also allows the administrator to maintain the list of userIds associated with a profile.

Many organizations already have a system that maintains information about their employees
and or customers and or business partners.
The Community Profile OMAS therefore supports an event exchange with such a system
to keep the profiles synchronized.  Incoming events support the adding and deleting of
profiles.  Outgoing events report on the Karma points awarded to individuals.


## Teams

A team is a group of people working together.  They may have one or more leaders.
Teams can be arranged in a hierarchy to march an organization's hierarchy.

Just like the profiles interface, the teams interface supports events to synchronize
team structures from another system as well as a REST API.


## Communities

Communities collect together resources, best practices and ideas for a group of people
who are collaborating on a specific topic or skill.

The communities interface support a both a REST API and event synchronization to
add and retrieve information from the community.



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.


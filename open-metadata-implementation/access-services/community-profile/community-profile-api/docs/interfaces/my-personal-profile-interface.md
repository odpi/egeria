<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# My personal profile interface

This is a management interface to enable an individual to manage their
[personal profile](../../../docs/concepts/personal-profile.md).

It includes the following operations:

* **get my profile** - returns the profile for this user.
This operation requires the person to have a personal profile.
  
  Implementation: 
  [Java](../../../community-profile-client/docs/user/retrieving-my-personal-profile-with-java.md), 
  [REST](../../../community-profile-server/docs/user/retrieving-my-personal-profile-with-rest.md)

* **get my karma points** - returns the total karma points for this user.
This operation requires the person to have a personal profile.
  
  Implementation: 
  [Java](../../../community-profile-client/docs/user/retrieving-my-karma-points-with-java.md), 
  [REST](../../../community-profile-server/docs/user/retrieving-my-karma-points-with-rest.md)

* **set my profile** - create or update the profile for the requesting user. 
This operation is not permitted if the profile is locked to (managed by) an
external system.
  
  Implementation: 
  [Java](../../../community-profile-client/docs/user/retrieving-my-personal-roles-with-java.md), 
  [REST](../../../community-profile-server/docs/user/retrieving-my-personal-roles-with-rest.md)

* **delete my profile** - removes an individual's personal profile along with their personal
[notes](../../../docs/concepts/personal-notes.md) and
[messages](../../../docs/concepts/personal-message.md).
This operation is not permitted if the profile is locked to (managed by) an
external system, or if the person is appointed to one or more
[roles](../../../docs/concepts/personal-roles.md). 
  
  Implementation: 
  [Java](../../../community-profile-client/docs/user/retrieving-my-personal-roles-with-java.md), 
  [REST](../../../community-profile-server/docs/user/retrieving-my-personal-roles-with-rest.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
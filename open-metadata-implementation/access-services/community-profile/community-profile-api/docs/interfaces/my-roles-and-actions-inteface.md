<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# My roles and actions interface

This is a query interface for retrieving the
[personal roles](../../../docs/concepts/personal-roles.md)
and [actions (to dos)](../../../docs/concepts/to-do.md)
assigned to the calling user.

It includes the following operations:

* **get my personal roles** - returns a list of the personal roles
that the calling user is appointed to. 
  
  Implementation: 
  [Java](../../../community-profile-client/docs/user/retrieving-my-personal-roles-with-java.md), 
  [REST](../../../community-profile-server/docs/user/retrieving-my-personal-roles-with-rest.md)
     
* **get my to dos** - returns a list of to dos assigned
to all of the personal roles of the calling user.

  Implementation: 
  [Java](../../../community-profile-client/docs/user/retrieving-my-to-dos-with-java.md), 
  [REST](../../../community-profile-server/docs/user/retrieving-my-to-dos-with-rest.md)
  
* **get my to dos by role** - returns a list of to dos assigned
to the specific personal role of the calling user.

  Implementation: 
  [Java](../../../community-profile-client/docs/user/retrieving-my-to-dos-by-role-with-java.md), 
  [REST](../../../community-profile-server/docs/user/retrieving-my-to-dos-by-role-with-rest.md)



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
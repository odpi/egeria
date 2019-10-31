<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Community Profile OMAS REST Interfaces

The community profile OMAS REST services use a URL that begins

```
<serverRootURL>/servers/{serverName}/open-metadata/access-services/community-profile/users/{userId}
```

where:
* **serverName** is the name of the server
* **userId** is the userId of the person (or server/engine/process) making the call.

The specific REST APIs for the Community Profile OMAS extend this basic URL.

## my-profile

The my-profile interface supports an individual working with their own
[personal profile](../../../docs/concepts/personal-profile.md).

* [Retrieving my personal profile](retrieving-my-personal-profile-with-rest.md)
* [Retrieving my karma points](retrieving-my-karma-points-with-rest.md)
* [Setting up my personal profile](setting-up-my-personal-profile-with-rest.md)


## my-roles-and-actions

The my-roles-and-actions interface enables a calling user to retrieve a
list of the [roles](../../../docs/concepts/personal-roles.md) they are currently appointed to and any
attached [actions (to dos)](../../../docs/concepts/to-do.md).

* [Retrieving my personal roles](retrieving-my-personal-roles-with-rest.md)
* [Retrieving my to dos](retrieving-my-to-dos-with-rest.md)
* [Retrieving my to dos by role](retrieving-my-to-dos-by-role-with-rest.md)

## personal-profiles

The personal-profiles API supports an administrator creating and deleting profiles for other
people (individuals are expected to update their own profile using the my-profile API).
It also allows the administrator to maintain the list of userIds associated with a profile.


----

> Note: The equivalent Java interfaces are documented in the [community-profile-client](../../../community-profile-client/docs/user)
module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
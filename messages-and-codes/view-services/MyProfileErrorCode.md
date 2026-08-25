<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# MyProfileErrorCode

Used to define first failure data capture (FFDC) for errors that occur within the OMAG Server It is used in conjunction with OMAG Exceptions, both Checked and Runtime (unchecked).

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 2 |
| **Message identifiers begin** | `OMVS-MY-PROFILE-400-` |
| **Java class** | `org.odpi.openmetadata.viewservices.myprofile.ffdc.MyProfileErrorCode` |
| **Module** | [open-metadata-implementation/view-services/my-profile/my-profile-server](../../open-metadata-implementation/view-services/my-profile/my-profile-server) |
| **Source** | [MyProfileErrorCode.java](../../open-metadata-implementation/view-services/my-profile/my-profile-server/src/main/java/org/odpi/openmetadata/viewservices/myprofile/ffdc/MyProfileErrorCode.java) |
| **Further reading** | <https://egeria-project.org/services/omvs/my-profile/overview/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [OMVS-MY-PROFILE-400-001](#omvs-my-profile-400-001) | 400 | The personal profile for user {0} already exists |
| [OMVS-MY-PROFILE-400-002](#omvs-my-profile-400-002) | 400 | The personal profile for user {0} does not exists |

----

### OMVS-MY-PROFILE-400-001

> The personal profile for user {0} already exists

|  |  |
|---|---|
| **Java constant** | `MyProfileErrorCode.PROFILE_ALREADY_EXISTS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

No action is taken.

**User action**

Retrieve the personal profile for this user with the 'getMyProfile' operation.


----

### OMVS-MY-PROFILE-400-002

> The personal profile for user {0} does not exists

|  |  |
|---|---|
| **Java constant** | `MyProfileErrorCode.PROFILE_DOESNT_EXISTS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

No action is taken except this exception is thrown.

**User action**

Create a new profile for this user with the 'addMyProfile' operation.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.

<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Security FVT (security-fvt)

This suite exercises the **metadata-security** module: the authorization decisions that the OMAG Server
Platform, the servers on it and the generic handlers delegate to the open metadata security connectors.
It is [auth-fvt](../auth-fvt)'s companion. That suite asks *who are you?* - logging on, tokens, passwords.
This one asks *what may you do?* - and the two questions are answered by different code.

## Why it exists

The module is a chain, and the suite tests the chain rather than any one link in it:

* `OpenMetadataPlatformSecurityVerifier` and `OpenMetadataServerSecurityVerifier` in
  `metadata-security-server` are what the admin services, platform services, multi-tenant services and
  generic handlers actually call. They are always present, and permit everything when no connector is
  configured.
* When a connector is configured they delegate to it. The base classes in `metadata-security-connectors`
  refuse everything, and an implementation overrides what it wants to permit.
* The implementation under test is the one shipped with the platform, the
  [Open Metadata Access Security Connector](../../../open-metadata-implementation/adapters/open-connectors/metadata-security-connectors/open-metadata-access-security-connector),
  which reads its user directory and access controls from a YAML secrets store.

None of this had a functional test. The interfaces have unit tests for their message sets, and the other
suites either exclude the connector or install it with a directory containing only their own
administrator - so "the connector recognises the user" was the only decision ever exercised. Which lists a
user has to be on to configure a server, whether a governance zone really hides an element from a search,
whether an operation-specific list overrides the `DEFAULT` one, and whether the dynamic groups built from
account type, ownership and maintenance history admit the users they should, were all untested.

## Running it

```
./gradlew :open-metadata-test:open-metadata-fvt:security-fvt:test -PrunSecurityFvt
```

It needs no database, no broker and no content packs: an in-memory repository and a generated user
directory. It runs in about a minute. Like the other FVT suites it is opt-in and does not run as part of a
normal build.

## How it is set up

[OMAGPlatformExtension](src/test/java/org/odpi/openmetadata/securityfvt/OMAGPlatformExtension.java) starts
one platform in-process on a free port, with authentication switched on and the access security connector
installed as the **platform** security connector. It then configures one in-memory metadata access store,
running only the Open Metadata Store service, with the same connector as its **server** security connector,
and starts it. Both connectors read the same secrets collection, so one directory answers "may this user
configure a server?" and "may this user read this element?" alike.

### The user directory is generated, and it is the fixture

Every decision the connector makes comes from one YAML file, written fresh by the extension on each run:
the accounts and their types, the security roles and groups each carries, and the access controls. The
tests are written against what `writeUserDirectory` defines, so that method is the first thing to read when
a test is unclear. In outline:

| User | Type | Roles / groups | Purpose |
|---|---|---|---|
| `secfvtadmin` | employee | administrator, operator, investigator | configures servers; also this suite's own administrator |
| `secfvtoperator` | employee | operator, investigator | may start servers but not configure them |
| `secfvtinvestigator` | employee | investigator | may look but not change |
| `secfvtsteward` | employee | stewards group | the zones grant their `DEFAULT` operations to this group |
| `secfvtcurator` | employee | stewards group, plus a `defaultZones` setting | shows default zones being applied |
| `secfvtemployee` | employee | none | an ordinary user; also has a personal zone named after it |
| `secfvtnpa` | digital | none | admitted like the employee, but a different account type |
| `secfvtcontractor` | contractor | none | admitted to the server, refused the store service |
| `secfvtexternal` | external | none | refused at the server |
| `secfvtdisabled` | employee, disabled | none | refused as unknown |
| `secfvtservernpa` | digital | none | the metadata access store's own userId |

The platform-level controls are at the connector's default names - `admin-services`, `platform-services`
and `server-operations` - because the connector's configuration is left at its defaults. The server control
is named after the server and the service control after the service, because that is how the connector
looks them up. The zones each have a control except one, `secfvt-unlisted`, which deliberately does not.

Each account that logs on has its own token collection in the same file, and every client in the suite
names the collection of the user it acts as. The connector never sees the token - it sees a userId - but
building the clients this way means the userId a test passes is one the platform actually authenticated.
The two exceptions are the disabled and unknown identities, which cannot obtain a token and reach the
server the way any userId does: as the path parameter of the store's REST API.

## What it covers

[PlatformSecurityFVT](src/test/java/org/odpi/openmetadata/securityfvt/PlatformSecurityFVT.java) -
`OpenMetadataPlatformSecurity`, and the administrator/operator checks of `OpenMetadataServerSecurity`.

* the administrator can create a server configuration; the operator and the investigator cannot
* the investigator can list servers and read the configuration of a server with no connector of its own
* the investigator cannot change a configuration or start a server; the operator can look at a running server
* once a server has its own connector, reading its configuration is put to that connector too, as an
  operator check, and changing it as an administrator check - so the operator, admitted by the platform, is
  refused by the store's connector
* a valid account with no platform role is refused even the investigator's view

[ServerAndServiceSecurityFVT](src/test/java/org/odpi/openmetadata/securityfvt/ServerAndServiceSecurityFVT.java) -
`validateUserForServer` and `validateUserForService`, the two doors every request passes.

* employees and digital accounts are admitted to both
* an external user is refused at the server (`403-002`)
* a contractor is admitted to the server and refused the service (`403-003`)
* a disabled account, and an identity not in the directory, are refused as unknown (`403-017`) before
  either control is consulted

[ElementSecurityFVT](src/test/java/org/odpi/openmetadata/securityfvt/ElementSecurityFVT.java) -
`OpenMetadataElementSecurity`, as the connector implements it with governance zones. Every call goes
through the generic handlers, so what is tested is the decision as a user meets it.

* an element with no zones, or in a zone with no access control, is visible to every admitted user
* a restricted zone refuses an explicit read (`403-020`) and silently leaves the element out of a search;
  the same element is found by a search from a user who may see it
* creating in a zone the user is not permitted `CREATE` in is refused (`403-008`)
* an operation-specific list overrides `DEFAULT` for that operation only: `READ` open to all, update and
  delete refused, and the refusal names the operation
* the `employeeUsers` dynamic group admits by account type
* any one of an element's zones is enough to permit
* a zone named after a user is that user's own
* the `newMaintainer` group follows the element's `maintainedBy` history: the creator is refused an update,
  a new user is permitted once and refused the second time
* an account's `defaultZones` are applied to what it creates, and are added to the zones it asks for

[AccessControlManagementFVT](src/test/java/org/odpi/openmetadata/securityfvt/AccessControlManagementFVT.java) -
the management half of `OpenMetadataUserSecurity`, through the platform.

* an operator can create, read and delete an access control, and it is persisted to the directory file
* an existing control is returned with each of its operation lists; the investigator is refused
* the user list is filtered by status and type; an account is returned without its secrets

Not covered: `OpenMetadataRepositorySecurity` (the shipped connector permits every repository operation,
so there is no decision to test), `OpenMetadataEventsSecurity` (needs a cohort), `selectConnection` (needs
an asset with several connections), and the anchor-based methods (every element here is its own anchor).

## What it has found

### Fixed: the instance owner group admitted everyone

`theInstanceOwnerGroupAdmitsOnlyTheElementsOwners` asserts that a zone granting its operations to the
`instanceOwner` group admits the users named as owners and refuses the rest. When first run it failed: the
digital account, which is neither an owner nor a steward, could read the element.

`OpenMetadataAccessSecurityConnector.isUserAnOwner` read the Ownership classification's owners through
`OpenMetadataProperty.USER_ID` - the property named `userId`. The classification, as defined in
`OpenMetadataTypesArchive2_10`, carries `owner`, `ownerTypeName`, `ownerPropertyName` and
`OpenMetadataProperty.USER_IDS` - `userIds`. The lookup could never find anything, the owner list came
back null, and the method treats a null list as "everyone is an owner" - so every zone that relied on the
group was open to every admitted user. The connector now reads `USER_IDS`, and the test passes.

### Fixed: the administration client dropped the userId from a refusal

Every refusal in the suite is checked for the user it names. The ones that went through the
administration API failed that check on the first run: `AdminClientRESTExceptionHandler` rebuilt the
`UserNotAuthorizedException` with `null` for the userId, where the common client handler in `ffdc-services`
reads it from the response's exception properties. The server sent it; the client discarded it. The
administration client now reads it the same way, and the tests assert the userId directly on every
refusal, whichever API it came through.

### Correct, but worth knowing

* **A server's own connector is consulted on reads of its configuration document, as an operator check.**
  The platform asks for an investigator; the server, once it has a security connection, asks for an
  operator. So an investigator who can read a plain server's configuration is refused the store's - and
  an activation, which starts by loading the document, is refused by the store's connector before the
  platform's operator check is reached. The suite records this rather than judging it.
* **A service-level refusal does not name the service.** The `403-003` message names only the operation,
  which for a whole-service refusal is `any`. The service is in the audit log record the connector writes,
  not in the exception. So a contractor refused the store service can be told apart from an external user
  refused the server only by the message identifier, which is what the tests assert.
* **A search never reports a refusal.** An element the user may not see is left out of the results, and
  the audit log records it as a filtered element. The tests pair the empty result with a search by a user
  who can see the element, so that "not found" is shown to be the security connector's doing.

----
Return to [open-metadata-fvt](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

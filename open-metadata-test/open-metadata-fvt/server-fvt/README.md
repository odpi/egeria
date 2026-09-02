<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Server Administration and Operation FVT (server-fvt)

This suite exercises the services that configure a server, start it, look at what it is doing, and shut it
down again — driven through their **Java clients** rather than through the REST API underneath them.

| Service | What is covered |
| --- | --- |
| `admin-services` | Building, storing, reading back and deploying a configuration document |
| `platform-services` | What the platform knows about itself, and the server lifecycle |
| `server-operations` | What a *running* server is doing |
| `repository-services` | The audit log and the metadata highway of a running server |
| `engine-services` | The four engine services' connector validation |
| `governance-server-services` | The engine host and integration daemon control surfaces |
| `user-security` | Which endpoints are open, which are not, and the clients' own token exchange |

## Why it exists

These are the services behind the Runtime Manager API. The REST endpoints underneath them are exercised
constantly, but always with the same handful of options, and the Java clients on top of them had almost no
automated coverage at all. That combination hides two kinds of defect:

* **A client whose URL or request body no longer matches the endpoint it calls.** Nothing catches this at
  compile time — the client and the controller live in different modules and share only a string literal — so
  an endpoint that is renamed, moved to another service or removed leaves behind a client method that
  compiles perfectly and 404s on its first call. The suite found five of them.
* **An input that is not validated, or is validated with a message that does not say what is wrong.** An
  administration API is driven by hand, so "what did I get wrong?" is most of what it is for.

The suite found defects of both kinds, plus several the design did not anticipate — including one that a
REST-level test could not have caught at all. The client-side defects are fixed; the server-side behaviour
they exposed is not. Both lists are under [What it has found](#what-it-has-found).

## Running it

```
./gradlew :open-metadata-test:open-metadata-fvt:server-fvt:test -PrunServerFvt
```

It needs **no PostgreSQL server, no Apache Kafka broker and no content packs** — an in-memory repository and
an in-memory event bus are enough, because the subject is how servers are configured and operated rather than
what is stored in them. It takes well under a minute. Like the other FVT suites it is opt-in and does not run
as part of a normal build.

## How it is set up

[OMAGPlatformExtension](src/test/java/org/odpi/openmetadata/serverfvt/OMAGPlatformExtension.java) starts one
platform in-process for the whole run, on a port allocated at runtime, and configures and starts three
servers on it: a metadata access store (`serverFvtMetadataStore`), an engine host (`serverFvtEngineHost`) and
an integration daemon (`serverFvtIntegrationDaemon`). Tests that need a server of their own create it, and
delete it in a `finally` block.

### It runs with authentication switched on

This is the one setup decision worth arguing with. Every sibling suite except auth-fvt excludes `user-authn`
and installs a permit-all filter chain, which is simpler and cannot fail for reasons unrelated to what is
being tested. This suite does not, for two reasons:

* `user-security` is one of the services under test, and most of it — `/api/about`, `/api/public/app/info`,
  the token endpoints — does not exist at all on a platform that excludes the module. A suite that excluded it
  would report those clients as broken when they are merely absent.
* Every client under test can obtain a bearer token for itself from a secrets store, and that path —
  `SpringRESTClientConnector.refreshAuthorizationToken` calling the YAML secrets store, which POSTs to
  `/api/token` — is used in every real deployment and by no other test. Against a permit-all platform it would
  be exercised but never actually *tested*, because a request carrying no token at all would succeed too.

So the port is allocated before the platform starts, a user directory naming that port is generated into
`build/server-fvt-data/`, and every client is built against it.
[UserSecurityFVT](src/test/java/org/odpi/openmetadata/serverfvt/UserSecurityFVT.java) is what makes this
meaningful: it pairs a successful authenticated call with the same call refused when no credentials are
presented. Without that pair, the rest of the suite would pass just as happily against a platform that had
stopped checking.

### The governance servers deliberately have nothing to run

No content pack is loaded, so the engine host's governance engine and the integration daemon's integration
group never find their definitions. That is intentional. What this suite checks of those two servers is their
**control** surface — can their status be retrieved, is an unknown engine reported as unknown, is a refresh
accepted — and all of it is answerable by a started server with nothing to do.

It is also the more valuable state to test. A governance server that has *failed* to find its configuration
is what an operator is looking at when they reach for these APIs; subscription-fvt always asks these
questions of a server that is working. Two of the findings below only appear in this state.

## What it has found

**All 81 tests pass.** Every defect the suite found has been fixed — the client-side ones first, then the
server-side behaviour behind them, each confirmed against the owner's account of how the component is meant to
work before anything was changed. What each test is really asking, and what it caught, is in the javadoc
against it.

### Fixed: clients addressing an endpoint that was not there

Each of these compiled, was public API, and 404d on its first call. Nothing in the build noticed, because the
client and the controller share only a string literal.

| Client method | Was calling | Fix |
| --- | --- | --- |
| `ServerOperationsClient.getServerStatus` | `/open-metadata/server-operations/servers/{name}/status` | Points at platform-services, which owns a server's start/stop history |
| `OMAGServerConfigurationClient.getOMAGServerInstanceConfig` | `/admin-services/servers/{name}/instance/configuration` | Points at server-operations, which owns a *running* instance's configuration |
| `OMAGServerConfigurationClient.addJDBCAuditLogDestination` | `.../audit-log-destinations/jdbc` | Deprecated; delegates to the new `addPostgreSQLAuditLogDestination(Map)`, which takes the storage properties that destination needs |
| `OMAGServerPlatformConfigurationClient.{set,clear,get}PlatformSecurityConnection` | `/admin-services/platform/security/connection` | Point at platform-services (they remain deprecated, but now work) |
| `OMAGServerConfigurationClient.setServerType` | `.../servers/{name}/server-type` | **Server-side**: the client was right and the administration service existed — the Spring mapping was missing from `ConfigPropertiesResource`, whose own class comment says it configures the server type. Added. |

### Fixed: clients that failed before a request was sent

* **`ServerOperationsClient.addOpenMetadataArchive`** built a URL template with two variables and supplied
  one, so URI expansion threw `IllegalArgumentException: Not enough variable values available to expand '1'`.
  Its two sibling methods both passed `delegatingUserId` and were correct. Entirely client-side — no amount of
  using the REST API directly would have surfaced it.
* **Placeholder numbering.** `ConfigurationManagementClient.getAllServerConfigurations` failed with
  `Illegal character in query`, because its template numbered its only placeholder `{1}` rather than `{0}`.
  This was a *class* of latent defect rather than one method: `SpringRESTClientConnector` binds placeholders
  by order of first appearance, so `{1}` alone works, while `JDKRESTClientConnector` binds them by index, so
  `{1}` alone is never substituted — meaning whether such a client worked depended on which connector was
  active. `PlatformServicesClient.setUserAccount` and `setSecurityAccessControl` carried the same numbering.
  All are renumbered, and every URL template across the seven services has been checked for contiguity.
* **`AuditLogServicesClient.getSeverityList`** could not read its own response, and this is the one defect
  here that a REST-level test would **not** have found — the call returned 200 with a well-formed body.
  `AuditLogSeveritiesResponse.severities` was typed as the `AuditLogRecordSeverity` *interface*, which Jackson
  cannot construct. Behind that sat a second problem: the severities are enum constants, which Jackson
  serializes by name, so the body was `["UNKNOWN","INFO",...]` — the ordinal and description, the two things
  an endpoint called `severity-definitions` exists to provide, were never sent to any client in any language.
  The response now carries the concrete `OMRSAuditLogReportSeverity`, which was already in the repository for
  exactly this purpose and unused.

### Fixed: smaller things

* **A blank server name was accepted** where a null one was refused, so it went into the URL path and left an
  empty segment — sending the request to a different endpoint, or to none, and reporting whatever it hit
  rather than the name that was missing. `InvalidParameterHandler.validateOMAGServerPlatformURL` now treats
  blank as not specified, for the platform URL as well as the server name.
* **The connector-oriented client constructors never assigned `serverName`.** `OMAGServerConfigurationClient`
  validated its URL against the still-null field and never set it, so every URL that client built carried a
  null server name. The constructor now takes a `serverName`, as do the ten server-scoped subclasses that
  delegate to it. Nothing called them, which is why it had gone unnoticed — and is also why widening the
  signature breaks no working code. (`ConfigurationManagementClient` and `OMAGServerPlatformConfigurationClient`
  address the platform rather than a named server and are unchanged.)
* **`AuditLogServicesClient.getSeverityList` and `addJDBCAuditLogDestination`** each declared a `methodName`
  copied from a neighbouring method, so every error they reported named the wrong operation — which is how a
  404 on `/jdbc` came to be reported as a problem with the file destination.
* **`AuditLogServicesClient` and `MetadataHighwayServicesClient`** documented `restURLRoot` as
  `serverURLroot + "/servers/" + serverName` while prepending `/servers/{0}/...` themselves, so following the
  javadoc gave a doubled path segment and a 404 on every call. Their javadoc now says what the code does, and
  notes the contrast with `MetadataCollectionServicesClient`, which genuinely does want the server name.
* **`OMAGServerConfigurationClient.setServerDescription`** passed the description twice, once as the request
  body and once as a surplus URL variable.

### Fixed: removing an audit log destination by display name

`clearAuditLogDestination` accepts either a qualified name or, failing that, a display name. The display-name
fallback was a **second pass over the same list**, each pass adding the non-matching entries to one survivor
list. So removing by display name added every entry that had not matched the qualified name a second time,
and left the entry that *did* match the display name in the list as well, because the first pass had already
put it there.

Asking a server with `[Console- default, SLF4J- no output]` to remove `SLF4J` by display name returned
`[Console- default, SLF4J- no output, Console- default]` — the destination was not removed, and the other one
was duplicated. Which criterion is being matched on is now settled before the survivors are collected, in a
single pass.

This sits directly in the path an administrator takes to swap the default audit log for one of their own,
since the display name is the one they see.

### Fixed: a server with no cohorts reported an error

Every metadata highway call went through a helper that raised `OMRS-REST-API-503-003 There is no metadata
highway...` whenever the server had no cohort configured — which is the ordinary state of most servers, not a
fault. A caller asking "which cohorts is this server in?" could not be told "none", and being unfederated was
indistinguishable from being broken.

A missing metadata highway is now treated as a server that belongs to no cohort, which is exactly how the
metadata highway manager already answered about a cohort it did not hold: null, an empty list, or `false` for
"the cohort name was not recognized". An unknown or unstarted server still fails, because that is a failure the
caller needs.

Worth recording precisely, because the first draft of this suite described it wrongly: the error was **not** an
HTTP 503. It arrived as **HTTP 200 with `relatedHTTPCode: 503` in the response body**, which is Egeria's
convention — a call that reaches Egeria's own code answers 200 and carries any error in the body, so that
failures from the network, Spring or Jackson stay distinguishable from failures Egeria itself reported. No
monitoring tool was ever seeing a 503.

### Fixed: an integration daemon with no connectors raised a NullPointerException

`IntegrationConnectorCacheMap.getConnectorIds()` returns null rather than an empty list when a daemon has no
connectors — the same empty-means-null idiom the response beans use. Four places iterate that list.
`shutdown()` checked it; `getConnectorReports()`, `refreshConnector(null)` and `restartConnector(null)` did
not, so a daemon whose integration group had not loaded raised
`Cannot invoke "java.util.List.iterator()" because "connectorIds" is null` on all three.

That is the state an operator is in when they call any of them: nobody asks a healthy daemon for its status,
or tells a working connector to refresh. Reporting an idle daemon, and treating "refresh all of nothing" as a
no-op, is the answer each of the three was reaching for.

The suite only found the first of the three. The other two came out of understanding the null rather than
reporting it, and `anIntegrationDaemonWithNoConnectorsAcceptsARefreshOfThemAll` and its restart sibling now
hold them — both confirmed to fail against the unfixed code.

### Fixed: the engine services were missing from `getAllServices()`

`OMAGServerPlatformInstanceMap.getAllRegisteredServices` aggregated the common, access, view and governance
categories and omitted the engine one, so the four OMESs were reported by `getEngineServices()` and absent
from the list of all registered services — even though the platform was running them.

There are six of these endpoints, one per category plus one for everything, and this is how they drift: a
category is added or grows and the aggregation is not updated with it. "All" is what a tool builds its picture
of the platform from — the Runtime Manager API among them — so a service missing from it does not exist as far
as that tool is concerned. `thePlatformListsItsRegisteredServices` compares by name across every category
rather than by count, so a category the aggregation forgets in future fails it the same way.

### Fixed: server operations was missing from a running server's service list

`getActiveServicesForServer` (platform-services) included `Server Operations` and `getActiveServices`
(server-operations) did not, so the same question asked through two URLs gave two answers and a caller had no
way to know which list they were holding.

The two build their answers from different places. Platform services reads the platform's instance map, which
every service registers with. Server operations reads the status map its own activation code fills in as it
starts each subsystem — and server operations is the component doing the starting, so it was the one running
service that never recorded itself. It now registers as `RUNNING` when the instance is created, which is when
it begins answering for that server, and is marked `INACTIVE` on the shutdown path alongside everything else.

### Fixed: refreshing an engine that had not loaded was refused as an unknown name

An engine host holds a placeholder for every engine configured into it, and reports one whose definition it
has not managed to retrieve as `ASSIGNED`. `refreshConfig(engineName)` looked its handler up without retrying,
found none, and reported `ENGINE-HOST-SERVICES-400-005 ... is not running in the engine host ...` — word for
word the message it gives for an engine name the host has never heard of.

Two things were wrong with that. Refreshing a named engine is exactly the request that can bring an `ASSIGNED`
engine into service, because it re-runs the retrieval of the definition, which may have been created since the
last attempt — so the operator's remedy was being refused. And the refusal told them they had mistyped the
name, sending them to look for a problem that was not there.

`GovernanceEngineMap` already had the mechanism: the `EngineConfig` overload of `getGovernanceEngineHandler`
retrieves the definition and builds the handler, and it is what the out-topic listeners and the configuration
refresh thread use to bring newly-defined engines to life. Only the `String` overload — a plain lookup — was
reachable from `refreshConfig`. A `refreshGovernanceEngineHandler(String)` now bridges the two, and
`refreshConfig` uses it on both paths, so refreshing every engine also gives the stalled ones another chance.

The name check now decides the error: a name this host has no engine configured for is refused and named,
while a configured engine is refreshed. If its definition still cannot be retrieved the request stays accepted
and the engine stays `ASSIGNED` — the caller asked the host to try again and it did; why it failed is in the
audit log.

### Correct, but worth knowing

* **A server acquires a default audit log destination it was not asked for**, so the first destination added
  to a fresh server leaves it with two. This is the rule that a server is never left without an audit log: a
  server is not valid without repository services configuration, and the default repository services
  configuration includes the default audit log — a particular configuration of the console destination. The
  default is a floor rather than a fixture: it can be removed by name and put back with `setDefaultAuditLog()`,
  which is what `theDefaultAuditLogDestinationCanBeRemovedAndAddedBack` checks. An earlier version of this
  suite reported the second destination as a defect.

* **A server security connection takes effect on the very next call, including for the person who set it.**
  Once stored, `getServerConfig` builds a verifier from it and calls `validateUserAsServerAdmin` on every
  subsequent read or update of that server's configuration — so an administrator who installs a connector
  whose user directory does not contain them can no longer configure that server, and because
  `clearServerSecurityConnection` is itself an update, they cannot undo it through the API either. Recovery
  means correcting the directory the connector points at, or removing the configuration document.

  This is the connector doing its job rather than a defect, and an earlier version of this suite reported it
  as one — the test had installed a security connector with no user directory behind it, so it recognised
  nobody, and the correct refusal was read as a failure.
  `AdminServicesConfigurationFVT.aServerSecurityConnectionIsEnforcedFromTheNextCall` now asserts the
  enforcement, and its sibling checks the round trip with a directory the administrator is actually in. The
  one thing an operator should know before using this call is that it is immediate: there is no window in
  which to check the result and change your mind.

## A note on how the tests are written

Almost every call in the administration API returns a `VoidResponse`, so a test that only checked "the call
succeeded" would pass against a server that accepted the request and stored nothing. Every configuration test
here therefore sets a value and then goes and looks for it, and where a property is reachable two ways — its
own getter and the whole document — it is checked both ways, because those are separate pieces of server-side
code.

The negative tests assert on the **message**, not just on the failure. Two things are asked of every
rejection: that it happens, and that it names the thing that was wrong. For an API with around forty setters,
most taking a single string, a message that does not say which one was rejected is not much better than
silence.

A test can also fail because the suite has misunderstood the design, and in a suite whose output is a list of
defects that is the expensive mistake — it produces a finding that costs somebody a day to disprove. This
suite made it three times while being written, and all three are worth remembering:

* it installed a server security connector with no user directory behind it, watched it refuse every
  subsequent call, and reported the platform as locking administrators out irreversibly. The connector was
  answering correctly about a directory containing nobody.
* it reported the default audit log destination as a duplicate of the caller's, not knowing the rule that a
  server is never left without one.
* it described a metadata highway error as an HTTP 503, without checking the wire. Egeria answers 200 and puts
  the error in the body; the 503 was a `relatedHTTPCode` field. The underlying complaint was sound, the
  evidence for it was not.

The first two were not platform defects at all, and in each case a real defect was sitting just underneath the
wrong one — the display-name removal bug was found only after the default destination was understood rather
than complained about. The habit that catches these: before reporting that a component behaves wrongly,
establish what it is supposed to do, check it was given what it needs to do it, and measure the evidence you
are about to quote.

Where a test asserts an exact status code or an exact exception, that is deliberate. A negative assertion in
this area can pass because the thing under test is broken in a different way — "not 200" is also satisfied by
a 404 against an endpoint that does not exist, and `assertThrows(Exception.class, ...)` is satisfied by a
client that never sent the request at all. `ServerOperationsFVT.anArchiveConnectionCanBeAddedToARunningServer`
has to inspect the message text for exactly this reason: the REST client connector catches everything,
including its own URI-expansion failure, and re-reports it as the same Java type as a genuine server refusal.

----
Return to [open-metadata-fvt](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

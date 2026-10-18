<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# View Services Functional Verification Tests (view-services-fvt)

The view-services-fvt module tests the **Open Metadata View Services (OMVSs)**: the REST APIs that user
interfaces, Dr.Egeria and pyegeria call. Its aim is that every request of every view service is called with
valid input, and that what comes back is checked: an element that was created can be read back with its
properties, an update is visible, a search finds it, a link shows from both ends, and a delete removes it.

## How it runs

`OMAGPlatformExtension` starts one OMAG Server Platform in-process, on a free port, with two servers:

* **viewServicesFvtMetadataStore** - a metadata access store loaded with the open metadata types and the Core
  Content Pack, using the in-memory repository or PostgreSQL.
* **viewServicesFvtViewServer** - a view server running **every** registered view service.

The platform runs a permit-all security filter chain whose anonymous user is the suite's own
(`viewservicesfvtuser`). View services take the caller from the security context, so that is the user every
request runs as, and it is whose profile My Profile works with.

Requests go over plain HTTP with the services' own request and response beans (`ViewServiceClient`), so a change
to a bean breaks the compile rather than quietly changing what a test checks. Egeria reports failures inside a
200 response, so every response's `relatedHTTPCode` is checked before its contents are used.

Test data carries a run-unique marker in its qualified names, so a run against a PostgreSQL repository that
still holds a previous run's elements neither collides with them nor finds them.

## Coverage report

`EndpointCoverage` records each request as Spring routes it: its request-mapping pattern, not the URL the test
built. At the end of a run it compares those records with every request mapping that the view service
controllers declare. Any request that was never called is written to `build/view-services-fvt-coverage.md`,
grouped by controller. Add `-PviewServicesFvtStrictCoverage` to fail the run when any request is uncovered.

## Running it

```
./gradlew :open-metadata-test:open-metadata-fvt:view-services-fvt:test -PrunViewServicesFvtInMemory
./gradlew :open-metadata-test:open-metadata-fvt:view-services-fvt:test -PrunViewServicesFvt
```

The first command needs nothing else running. The second uses the PostgreSQL server named in
`src/test/resources/application.properties` (by default `egeria-shared-postgres` on port 5442).

## Requests that need other servers

This suite runs no engine host or integration daemon. Some requests reach those servers directly. For those,
the test checks for the specific refusal and explains why in a comment.

----
Return to [open-metadata-fvt](..)

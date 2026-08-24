<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata API Checks

The open-metadata-api-checks module holds static checks over the view service REST APIs and the open
metadata type system. They catch classes of mistake that **compile cleanly** — where the code is valid Java
and the fault only shows up when a platform boots, when a caller sends a particular request, or not at all.

The checks read the project's own source files rather than running anything, so they need no platform, no
database and no network, and they finish in seconds. They run as part of the normal build — there is no
opt-in property to set, unlike the [FVT suites](../open-metadata-fvt).

```bash
./gradlew :open-metadata-test:open-metadata-api-checks:test
```

## The checks

**[`RESTAPIPathUniquenessTest`](src/test/java/org/odpi/openmetadata/apichecks/RESTAPIPathUniquenessTest.java)** —
no view service resource class maps the same HTTP verb and path twice.

Spring refuses to build its request mapping when two methods claim the same verb and path, and the whole
application context then fails to start. Nothing catches that at compile time: the duplicates are just two
annotations with the same value. Without this check, the first sign of trouble is a platform that will not
boot — a long way from the line that caused it. The same path under *different* verbs is legal and is used
deliberately, so the check keys on the verb as well as the path.

**[`RESTAPINullRequestBodyTest`](src/test/java/org/odpi/openmetadata/apichecks/RESTAPINullRequestBodyTest.java)** —
every view service REST method that uses its `requestBody` tests it for null first.

These request bodies are optional (`@RequestBody(required = false)`), so a caller can send nothing. A method
that goes straight to `requestBody.getProperties()` answers that caller with a `NullPointerException` and a
stack trace instead of a message telling them what was wrong. Letting Spring reject a missing body by marking
it required is not used, because Spring's message is not good enough to hand to an end user — the services
produce their own, and this check makes sure they actually do.

What counts as handling it is deliberately loose: any test of `requestBody` against null. Some methods return
an error, others treat a missing body as "no properties supplied" and carry on. Both are fine; only ignoring
the possibility is not.

**[`OpenMetadataTypeAPICoverageTest`](src/test/java/org/odpi/openmetadata/apichecks/OpenMetadataTypeAPICoverageTest.java)** —
every relationship and classification type can be maintained through the open metadata handlers.

A type with a properties bean but no handler method is invisible to callers: the type exists, the bean
exists, and there is no supported way to create or remove an instance of it short of the generic
metadata-expert calls. Gaps like this accumulate quietly, because adding a type does not fail anything. This
check makes them fail the build instead.

A type counts as covered when a handler method that writes to the store also names the type. Types that are
deliberately maintained another way are listed in `EXPECTED_ABSENTEES`, **each with a stated reason** —
supertypes that exist only to be inherited from, relationships maintained through a handler that takes the
type name as a parameter, and so on. A type that is simply missing an API belongs in the code, not in that
list.

## Reading source rather than classes

Both helpers exist because these checks look at source files:

* **[`SourceTree`](src/test/java/org/odpi/openmetadata/apichecks/SourceTree.java)** locates the project's own
  java files — view service resources, REST services, handlers, connector context clients. A test's working
  directory is its own module, so it walks up to find the repository root rather than assuming a fixed depth.
* **[`JavaMethods`](src/test/java/org/odpi/openmetadata/apichecks/JavaMethods.java)** splits a source file
  into its top-level public methods so a check can look at one method at a time. It is a deliberately simple
  split on the declaration line rather than a parse — the checks only need to know which lines belong to
  which method.

Source is used rather than compiled classes because what these checks look for — an annotation's value, a
null test around a parameter — is not reliably visible at runtime.

## Adding a check

The bar is that the mistake compiles. If javac or an existing test would catch it, it belongs there instead.
These checks earn their place by covering the gap between "the code is valid" and "the code works", where the
feedback would otherwise arrive at boot time, at call time, or never.

Each check asserts up front that it actually found the files it expects to scan (for example
`resources.size() > 10`), so that a source layout change makes the check fail loudly rather than quietly
passing over nothing.

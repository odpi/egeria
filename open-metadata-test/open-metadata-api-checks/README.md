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

**[`BeanEqualityContractTest`](src/test/java/org/odpi/openmetadata/apichecks/BeanEqualityContractTest.java)** —
no class calls `super.equals` or `super.hashCode` when it has no superclass to call.

The house style for a bean part way up a hierarchy is to compare its own fields and defer the rest to its
parent. In a bean at the root of its own hierarchy, or one that only implements an interface, `super` is
`Object` — and the two calls then mean something quite different from what they look like. `Object.equals`
is identity, which the `this == objectToCompare` test at the top of the method has already dealt with, so
the guard can only return false and the bean never equals a separate bean holding the same content.
`Object.hashCode` is the identity hash, so mixing it into `Objects.hash(...)` gives every instance a
different hash code and breaks the equals/hashCode contract.

Neither fails at compile time — both resolve perfectly well against `Object` — and neither raises an error
at runtime. What appears instead is duplicates in a `HashSet`, a de-duplication step that finds nothing, or
a comparison that says two identical beans differ. The fix is to remove the call, not to add a superclass.

This check reads the whole implementation rather than a chosen set of bean packages, because beans are
written in the frameworks, in the REST request and response bodies, in the view services and in the server
status beans, and the pattern turned up in all of them.

**[`JacksonSubtypeRegistrationTest`](src/test/java/org/odpi/openmetadata/apichecks/JacksonSubtypeRegistrationTest.java)** —
every open metadata framework bean can be deserialized as the base type it is sent as.

These beans travel polymorphically: a field declared as `OpenMetadataRootProperties` holds any of the
properties beans beneath it. Jackson resolves the type id in the JSON using the `@JsonSubTypes` list on the
base, so a bean nobody added to that list serializes perfectly and then fails to come back with
`InvalidTypeIdException: Could not resolve type id ...`. The write half of the round trip works, so a test
that only serializes passes, and the omission can sit unnoticed until some later release declares a field
as the base type and every payload carrying the bean starts failing.

Registration anywhere in the subtype graph below the base counts, not only on the immediate parent: Jackson
collects subtypes transitively, so a bean registered on a sibling still resolves. A check that insisted on
the immediate parent would report beans that work.

The scope is the framework's `properties`, `metadataelements` and `search` packages. The REST request and
response hierarchies also declare `@JsonTypeInfo`, but each endpoint names its concrete response type, so
around a hundred of those beans are unregistered and correct as they are — including them would mean an
exclusion list longer than the findings, which is not a check. If a response hierarchy ever does start
travelling as its base type, its package belongs here.

**[`BeanEqualityChainTest`](src/test/java/org/odpi/openmetadata/apichecks/BeanEqualityChainTest.java)** —
a bean deferring part of its equality to its parent has a parent that implements it.

This is the other half of the check above, and the half that looks correct. The bean *does* have a
superclass and calls `super.equals` on it quite legitimately — but no class anywhere up the chain implements
`equals`, so the call reaches `Object` all the same. The effect is identical and better hidden: equality
becomes identity, `hashCode` becomes the identity hash, and every field declared by the classes in between
takes part in no comparison at all.

It found `CatalogTemplate` and `ResourceDescription` in the automated curation view service, both deferring
to a `RefDataElementBase` that declared three fields and implemented neither method. The fix is to implement
the two methods on the class missing them — usually the base, which fixes every subclass at once — not to
delete the `super` call from the subclasses.

A chain that leaves the implementation is not judged: where an ancestor is a class from the JDK or a
dependency, whether it implements `equals` cannot be read from this repository's source, and a guess either
way would be worse than silence.

**[`BeanReferenceComparisonTest`](src/test/java/org/odpi/openmetadata/apichecks/BeanReferenceComparisonTest.java)** —
no `equals` method compares a reference field with `==`.

Inside an `equals` method, `field == that.field` reads exactly like a comparison of two values, and for a
primitive or an enum constant it is one. For anything else it asks whether the two beans hold the *same
object*, which two separately built beans never do. The bean can then never equal a separate bean with the
same content — not a copy of itself, not one that came back over JSON — while its `hashCode`, written the
usual way with `Objects.hash(...)`, goes on hashing by value. Equal hash codes and unequal beans is the
equals/hashCode contract broken in the direction that is hardest to notice: nothing throws, nothing logs,
and a de-duplication step simply stops finding anything.

It is also inherited. The first of these found here was `DataAssetProperties.authors`, and more than fifty
beans extend it, so every `CSVFileProperties`, `DatabaseProperties` and `TopicProperties` in the model had
the defect from one line. Seven more turned up on the same scan, including a `String` compared with `==`.

Primitives and enums are left alone, which is why this check reads the whole tree rather than only the
beans: knowing that `contentStatus` is a `ContentStatus` is no use without knowing that `ContentStatus` is
an enum. A boxed primitive is **not** left alone — comparing an `Integer` with `==` works only inside the
cache the JVM happens to keep, which is the worst kind of working.

**[`ToStringNamesItsOwnClassTest`](src/test/java/org/odpi/openmetadata/apichecks/ToStringNamesItsOwnClassTest.java)** —
a `toString` names the class it belongs to, and each of its labels names the field it prints.

A `toString` is written by copying a sibling's and editing it, which is why both halves of it go wrong the
same way. Neither is caught by anything: the method compiles, returns a string, and says something false.

**The wrong class.** A `ClassificationProperties` announced itself as a `ClassificationBeanProperties`, an
`EntityProperties` as a `RelationshipProperties`, a `DataManagerProperties` as an `EngineProperties`. That
does not merely fail to help — it sends whoever is reading the log to the wrong class. This was true of
**230 types** across the implementation.

**The wrong label.** `", governanceEngineGUID='" + executorEngineGUID` prints one field under another's
name, usually because the field was renamed and the label was not. **140 of those.**

Only a label whose printed expression is a field of the same class is checked, so a label over a getter
call or an expression is left alone. Where a mismatch is *deliberate* — `StarRating` swaps two of them and
says so in a comment — a trailing comment on the line is taken as the explanation and the line is skipped.
That is a narrow escape hatch on purpose: a deliberate mismatch is rare enough to be worth a sentence.

**[`DefinitionGUIDCollisionTest`](src/test/java/org/odpi/openmetadata/apichecks/DefinitionGUIDCollisionTest.java)** —
no GUID belonging to an open metadata type is also used as the identity of an element a content pack creates.

Content pack writers hard code the GUID of each element they create so that regenerating a pack keeps every
element's identity. The archive builder keeps **one** GUID namespace for the whole archive — type
definitions and instances together — and a content pack loads the open metadata types as a dependent
archive. A definition whose GUID is already a type's GUID is therefore an element the builder will refuse.

`SLF4J_AUDIT_LOG_DESTINATION_CONNECTOR` carried `e8303911-ba1c-4640-974e-c4d57ee1b310`, the type GUID of the
`DigitalProductDependency` relationship. The builder refused the entity, the archive helper swallowed the
refusal, and `CoreContentPack` shipped for its whole history with a collection membership pointing at a
connector type that was not in the pack. Nothing failed and nothing said so.

Two GUIDs colliding is a coincidence no amount of care prevents and no reviewer would spot. The fix is a
fresh GUID for the *definition* — a type's GUID is fixed by the standard and by every repository holding an
instance of it — and whether that is safe depends on whether the element has ever shipped.

## Reading source rather than classes

The helpers exist because these checks look at source files:

* **[`SourceTree`](src/test/java/org/odpi/openmetadata/apichecks/SourceTree.java)** locates the project's own
  java files — view service resources, REST services, handlers, connector context clients. A test's working
  directory is its own module, so it walks up to find the repository root rather than assuming a fixed depth.
* **[`JavaMethods`](src/test/java/org/odpi/openmetadata/apichecks/JavaMethods.java)** splits a source file
  into its top-level public methods so a check can look at one method at a time. It is a deliberately simple
  split on the declaration line rather than a parse — the checks only need to know which lines belong to
  which method.
* **[`JavaClasses`](src/test/java/org/odpi/openmetadata/apichecks/JavaClasses.java)** picks the top-level
  class out of a file — its name, whether it is abstract, and the class it extends. It matches the
  declaration named after the file, which is what keeps nested and inner classes out of the way.

Source is used rather than compiled classes because what these checks look for — an annotation's value, a
null test around a parameter — is not reliably visible at runtime.

## Adding a check

The bar is that the mistake compiles. If javac or an existing test would catch it, it belongs there instead.
These checks earn their place by covering the gap between "the code is valid" and "the code works", where the
feedback would otherwise arrive at boot time, at call time, or never.

Each check asserts up front that it actually found the files it expects to scan (for example
`resources.size() > 10`), so that a source layout change makes the check fail loudly rather than quietly
passing over nothing.

<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Unit Test Utilities

The open-metadata-ut module provides the base classes that Egeria's unit tests extend, so that the same
checks do not have to be written again in each module.  The tests themselves live alongside the code they
are testing; this module only supplies the shared scaffolding.

The utilities are in
[`org.odpi.openmetadata.test.unittest.utilities`](src/main/java/org/odpi/openmetadata/test/unittest/utilities):

* **`MessageSetTest`** - the common base for the two message set tests below.  It validates that a message
  set enum has unique message ids and non-null names and descriptions, and that its values serialize to
  JSON and back again.

* **`AuditLogMessageSetTest`** - extend this in the unit test for a component's `AuditCode` enum.

* **`ExceptionMessageSetTest`** - extend this in the unit test for a component's `ErrorCode` enum.

* **`BeanTestBase`** - utilities for testing that a bean (or a REST response object) supports `toString`,
  `hashCode`, `equals` and JSON serialization/deserialization correctly.

* **`OMFCheckedExceptionBasedTest`** - validates that an exception class is properly populated and supports
  `toString`, `hashCode` and `equals`.

Because these classes are the parents of tests in other modules, they are built into the module's *main*
source tree rather than its test source tree, and the module is declared as a `testImplementation`
dependency by the modules that use it.

----
* Return to [Open Metadata Test](..)
* Return to [Module Organization](../../Content-Organization.md)
* Return to [Home](../../index.md)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

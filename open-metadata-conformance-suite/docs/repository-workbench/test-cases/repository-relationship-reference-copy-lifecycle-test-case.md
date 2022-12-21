<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Repository relationship reference copy lifecycle test case

This test validates that reference copies of entities can be managed through all stages of their life cycle.

## Operation

The relationship reference copy lifecycle tests extract each of the type definitions for relationship and create an instance of that relationship ON THE CTS
server. This generates an event across the cohort, that is received by the TUT server, which then creates a reference copy of the relationship.
By manipulating the master instance of the relationship on the CTS server, and by issuing test operations directly against the reference copy
on the TUT server, the test case verifies that the TUT supports all permissible operations on the reference copy, and correctly prevents any
non-permissible operations from being performed.

Finally, the testcase deletes the master instance (from the CTS) and reference copy (in the TUT).


## Assertions

* **repository-relationship-reference-copy-lifecycle-01** - `typeName` reference relationship is known.
* **repository-relationship-reference-copy-lifecycle-02** - `typeName` reference relationship can be retrieved as Relationship.
* **repository-relationship-reference-copy-lifecycle-03** - `typeName` reference relationship matches the relationship that was saved.
* **repository-relationship-reference-copy-lifecycle-04** - `typeName` reference relationship status cannot be updated.
* **repository-relationship-reference-copy-lifecycle-05** - `typeName` reference relationship properties cannot be updated.
* **repository-relationship-reference-copy-lifecycle-06** - `typeName` reference relationship type cannot be changed.
* **repository-relationship-reference-copy-lifecycle-07** - `typeName` reference relationship identity cannot be changed.
* **repository-relationship-reference-copy-lifecycle-08** - `typeName` reference relationship purged.
* **repository-relationship-reference-copy-lifecycle-09** - `typeName` reference relationship re-homed.


## Discovered Properties

The discovered properties for this test show which of the optional capabilities are supported for this type.

* **`typeName` reference copy support** : Enabled/Disabled - indicates whether the optional support for creating and managing reference copies is enabled or not.

## Sample Output

This is the sample output for the ConnectionConnectorType type.

```json
    {
        "class": "OpenMetadataTestCaseResult",
        "discoveredProperties": {
            "ConnectionConnectorType reference copy support": "Enabled"
        },
        "notSupportAssertions": [],
        "successMessage": "Reference copies of entities can be managed through their lifecycle",
        "successfulAssertions": [
            "repository-test-case-base-01: Repository connector supplied to conformance suite.",
            "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
            "repository-test-case-base-01: Repository connector supplied to conformance suite.",
            "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
            "repository-test-case-base-01: Repository connector supplied to conformance suite.",
            "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
            "repository-test-case-base-01: Repository connector supplied to conformance suite.",
            "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
            "repository-test-case-base-01: Repository connector supplied to conformance suite.",
            "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
            "repository-test-case-base-01: Repository connector supplied to conformance suite.",
            "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
            "repository-relationship-reference-copy-lifecycle-01: ConnectionConnectorType reference relationship is known.",
            "repository-relationship-reference-copy-lifecycle-02: ConnectionConnectorType reference relationship can be retrieved as Relationship.",
            "repository-relationship-reference-copy-lifecycle-03: ConnectionConnectorType reference relationship matches the relationship that was saved.",
            "repository-relationship-reference-copy-lifecycle-04: ConnectionConnectorType reference relationship status cannot be updated.",
            "repository-relationship-reference-copy-lifecycle-04: ConnectionConnectorType reference relationship status cannot be updated.",
            "repository-relationship-reference-copy-lifecycle-06: ConnectionConnectorType reference relationship type cannot be changed.",
            "repository-relationship-reference-copy-lifecycle-07: ConnectionConnectorType reference relationship identity cannot be changed.",
            "repository-relationship-reference-copy-lifecycle-08: ConnectionConnectorType reference relationship purged.",
            "repository-test-case-base-01: Repository connector supplied to conformance suite.",
            "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
            "repository-relationship-reference-copy-lifecycle-09: ConnectionConnectorType reference relationship re-homed."
        ],
        "testCaseDescriptionURL": "https://egeria-project.org/guides/cts/repository-workbench/test-cases/repository-relationship-reference-copy-lifecycle-test-case.md",
        "testCaseId": "repository-relationship-reference-copy-lifecycle-ConnectionConnectorType",
        "testCaseName": "Repository relationship reference copy lifecycle test case",
        "unsuccessfulAssertions": []
    }
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
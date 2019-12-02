<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Repository entity reference copy lifecycle test case

This test validates that reference copies of entities can be managed through all stages of their life cycle.

## Operation

The entity reference copy lifecycle tests extract each of the type definitions for entity and create an instance of that entity ON THE CTS
server. This generates an event across the cohort, that is received by the TUT server, which then creates a reference copy of the entity.
By manipulating the master instance of the entity on the CTS server, and by issuing test operations directly against the reference copy
on the TUT server, the test case verifies that the TUT supports all permissible operations on the reference copy, and correctly prevents any
non-permissible operations from being performed.

Finally, the testcase deletes the master instance (from the CTS) and reference copy (in the TUT).


## Assertions

* **repository-entity-reference-copy-lifecycle-01** - `typeName` reference entity is known.
* **repository-entity-reference-copy-lifecycle-02** - `typeName` reference entity can be retrieved as EntitySummary.
* **repository-entity-reference-copy-lifecycle-03** - `typeName` reference entity can be retrieved as EntityDetail.
* **repository-entity-reference-copy-lifecycle-04** - `typeName` reference entity matches the entity that was saved.
* **repository-entity-reference-copy-lifecycle-05** - `typeName` reference entity has no relationships.
* **repository-entity-reference-copy-lifecycle-06** - `typeName` reference entity status cannot be updated.
* **repository-entity-reference-copy-lifecycle-07** - `typeName` reference entity properties cannot be updated.
* **repository-entity-reference-copy-lifecycle-08** - `typeName` reference entity type cannot be changed.
* **repository-entity-reference-copy-lifecycle-09** - `typeName` reference entity identity cannot be changed.
* **repository-entity-reference-copy-lifecycle-10** - `typeName` reference entity purged.
* **repository-entity-reference-copy-lifecycle-11** - `typeName` reference entity saved.
* **repository-entity-reference-copy-lifecycle-12** - `typeName` reference entity re-homed.


## Discovered Properties

The discovered properties for this test show which of the optional capabilities are supported for this type.

* **`typeName` reference copy support** : Enabled/Disabled - indicates whether the optional support for creating and managing reference copies is enabled or not.

## Sample Output

This is the sample output for the ExternalId type.

```json
    {
        "class": "OpenMetadataTestCaseResult",
        "discoveredProperties": {
            "ExternalId reference copy support": "Enabled"
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
            "repository-entity-reference-copy-lifecycle-01: ExternalId reference entity is known.",
            "repository-entity-reference-copy-lifecycle-02: ExternalId reference entity can be retrieved as EntitySummary.",
            "repository-entity-reference-copy-lifecycle-03: ExternalId reference entity can be retrieved as EntityDetail.",
            "repository-entity-reference-copy-lifecycle-04: ExternalId reference entity matches the entity that was saved.",
            "repository-entity-reference-copy-lifecycle-05: ExternalId reference entity has no relationships.",
            "repository-entity-reference-copy-lifecycle-06: ExternalId reference entity status cannot be updated.",
            "repository-entity-reference-copy-lifecycle-06: ExternalId reference entity status cannot be updated.",
            "repository-test-case-base-01: Repository connector supplied to conformance suite.",
            "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
            "repository-test-case-base-01: Repository connector supplied to conformance suite.",
            "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
            "repository-entity-reference-copy-lifecycle-07: ExternalId reference entity properties cannot be updated.",
            "repository-entity-reference-copy-lifecycle-08: ExternalId reference entity type cannot be changed.",
            "repository-entity-reference-copy-lifecycle-09: ExternalId reference entity identity cannot be changed.",
            "repository-entity-reference-copy-lifecycle-10: ExternalId reference entity purged.",
            "repository-test-case-base-01: Repository connector supplied to conformance suite.",
            "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
            "repository-test-case-base-01: Repository connector supplied to conformance suite.",
            "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
            "repository-entity-reference-copy-lifecycle-11: ExternalId reference entity saved.",
            "repository-entity-reference-copy-lifecycle-12: ExternalId reference entity re-homed."
        ],
        "testCaseDescriptionURL": "https://egeria.odpi.org/open-metadata-conformance-suite/docs/repository-workbench/test-cases/repository-entity-reference-copy-lifecycle-test-case.md",
        "testCaseId": "repository-entity-reference-copy-lifecycle-ExternalId",
        "testCaseName": "Repository entity reference copy lifecycle test case",
        "unsuccessfulAssertions": []
    }
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
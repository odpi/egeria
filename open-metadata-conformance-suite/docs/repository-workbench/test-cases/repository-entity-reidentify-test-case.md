<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Repository entity reidentify test case

This test validates that entities can be reidentified.

## Operation

The entity reidentify tests extract each of the type definitions for entity and creates an instance of that entity type.
It ensures that the entity can be retrieved (from the repository) using its original GUID. It then reidentifies the
entity with a new GUID. It verifies that the reidentified entity has an updated version and can no longer be retrieved under its
original GUID, but that it can be retrieved using its new GUID.

Finally the testcase deletes the entity instance.

## Assertions

* **repository-entity-reidentify-01** - `typeName` new entity created.
* **repository-entity-reidentify-02** - `typeName` new entity retrieved.
* **repository-entity-reidentify-03** - `typeName` entity is reidentified.
* **repository-entity-reidentify-04** - `typeName` entity with new identity version number is `version`.
* **repository-entity-reidentify-05** - `typeName` entity no longer retrievable by previous GUID
* **repository-entity-reidentify-06** - `typeName` entity retrievable by new GUID..


## Discovered Properties

The discovered properties for this test show which of the optional capabilities are supported for this type.

* **`typeName` reidentify support** : Enabled/Disabled - indicates whether the optional support for the reidentify command is enabled or not.

## Sample Output

This is the sample output for the DesignPattern type.

```json
    {
        "class": "OpenMetadataTestCaseResult",
        "discoveredProperties": {
            "DesignPattern reidentify support": "Enabled"
        },
        "notSupportAssertions": [],
        "successMessage": "Entities can be reidentified",
        "successfulAssertions": [
            "repository-test-case-base-01: Repository connector supplied to conformance suite.",
            "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
            "repository-test-case-base-01: Repository connector supplied to conformance suite.",
            "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
            "repository-test-case-base-01: Repository connector supplied to conformance suite.",
            "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
            "repository-entity-reidentify-01: DesignPattern new entity created.",
            "repository-entity-reidentify-02: DesignPattern new entity retrieved.",
            "repository-entity-reidentify-03: DesignPattern entity is reidentified.",
            "repository-entity-reidentify-04: DesignPattern entity with new identity version number is 2",
            "repository-entity-reidentify-05: DesignPattern entity no longer retrievable by previous GUID.",
            "repository-entity-reidentify-06: DesignPattern entity retrievable by new GUID."
        ],
        "testCaseDescriptionURL": "https://egeria-project.org/guides/cts/repository-workbench/test-cases/repository-entity-reidentify-test-case.md",
        "testCaseId": "repository-entity-reidentify-DesignPattern",
        "testCaseName": "Repository entity reidentify test case",
        "unsuccessfulAssertions": []
    }
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
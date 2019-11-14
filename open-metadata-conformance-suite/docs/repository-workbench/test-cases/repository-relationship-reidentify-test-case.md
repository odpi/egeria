<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Repository relationship reidentify test case

This test validates that entities can be reidentified.

## Operation

The relationship reidentify tests extract each of the type definitions for relationship and creates an instance of that relationship type.
It ensures that the relationship can be retrieved (from the repository) using its original GUID. It then reidentifies the
relationship with a new GUID. It verifies that the reidentified relationship has an updated version and can no longer be retrieved under its
original GUID, but that it can be retrieved using its new GUID.

Finally the testcase deletes the relationship instance.

## Assertions

* **repository-relationship-reidentify-01** - `typeName` new relationship created.
* **repository-relationship-reidentify-02** - `typeName` new relationship retrieved.
* **repository-relationship-reidentify-03** - `typeName` relationship is reidentified.
* **repository-relationship-reidentify-04** - `typeName` relationship with new idrelationship version number is `version`.
* **repository-relationship-reidentify-05** - `typeName` relationship no longer retrievable by previous GUID
* **repository-relationship-reidentify-06** - `typeName` relationship retrievable by new GUID..


## Discovered Properties

The discovered properties for this test show which of the optional capabilities are supported for this type.

* **`typeName` reidentify support** : Enabled/Disabled - indicates whether the optional support for the reidentify command is enabled or not.

## Sample Output

This is the sample output for the SchemaQueryImplementation type.

```json
    {
        "class": "OpenMetadataTestCaseResult",
        "discoveredProperties": {
            "SchemaQueryImplementation reidentify support": "Enabled"
        },
        "notSupportAssertions": [],
        "successMessage": "Relationships can be reidentified",
        "successfulAssertions": [
            "repository-test-case-base-01: Repository connector supplied to conformance suite.",
            "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
            "repository-test-case-base-01: Repository connector supplied to conformance suite.",
            "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
            "repository-test-case-base-01: Repository connector supplied to conformance suite.",
            "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
            "repository-relationship-reidentify-01: SchemaQueryImplementation new relationship created.",
            "repository-relationship-reidentify-02: SchemaQueryImplementation new relationship retrieved.",
            "repository-relationship-reidentify-03: SchemaQueryImplementation relationship is reidentified.",
            "repository-relationship-reidentify-04: SchemaQueryImplementation relationship with new idrelationship version number is 2",
            "repository-relationship-reidentify-05: SchemaQueryImplementation relationship no longer retrievable by previous GUID.",
            "repository-relationship-reidentify-06: SchemaQueryImplementation relationship retrievable by new GUID."
        ],
        "testCaseDescriptionURL": "https://egeria.odpi.org/open-metadata-conformance-suite/docs/repository-workbench/test-cases/repository-relationship-reidentify-test-case.md",
        "testCaseId": "repository-relationship-reidentify-DesignPattern",
        "testCaseName": "Repository relationship reidentify test case",
        "unsuccessfulAssertions": []
    }
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
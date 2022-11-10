<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Repository entity reference copy lifecycle test case

This test validates that reference copies of entities can be managed through all stages of their life cycle.

## Operation

The reference copy classification lifecycle tests extract the set of classification types supported by the repository under test.
It then iterates over he entity tyeps that to which each classification type can be applied, and for each combination of classification
type and entity type, it creates an entity reference copy, classifies it and declassifies it.

Finally, the testcase deletes the entity reference copy.


## Assertions

* **repository-reference-copy-classification-lifecycle-01** - No classifications attached to new entity reference copy of type `typeName`.
* **repository-reference-copy-classification-lifecycle-02** - `typeName` entity reference copy returned when classification added.
* **repository-reference-copy-classification-lifecycle-03** - `typeName` classification added to entity reference copy of type.
* **repository-reference-copy-classification-lifecycle-04** - `typeName` classification properties added to entity reference copy of type.
* **repository-reference-copy-classification-lifecycle-05** - `typeName` classification removed from entity reference copy of type

## Discovered Properties

The discovered properties for this test show which of the optional capabilities are supported for this type.

* **`typeName` reference copy support** : Enabled/Disabled - indicates whether the optional support for creating and managing reference copies is enabled or not.

## Sample Output

This is the sample output for the MetamodelInstance-ConceptBeadLink classification type.

```json
    {
        "class": "OpenMetadataTestCaseResult",
        "discoveredProperties": {
            "MetamodelInstance-ConceptBeadLink reference copy support": "Enabled"
        },
        "notSupportAssertions": [],
        "successMessage": "Classifications on entity reference copies can be managed through their lifecycle",
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
            "repository-reference-copy-classification-lifecycle-01: No classifications attached to new entity reference copy of type ConceptBeadLink",
            "repository-reference-copy-classification-lifecycle-02: ConceptBeadLink entity reference copy returned when classification added.",
            "repository-reference-copy-classification-lifecycle-03: MetamodelInstance-ConceptBeadLink classification added to entity reference copy of type ConceptBeadLink",
            "repository-reference-copy-classification-lifecycle-04: MetamodelInstance-ConceptBeadLink classification properties added to entity reference copy of type ConceptBeadLink",
            "repository-reference-copy-classification-lifecycle-05: MetamodelInstance-ConceptBeadLink classification removed from entity reference copy of type ConceptBeadLink"
        ],
        "testCaseDescriptionURL": "https://egeria-project.org/guides/cts/repository-workbench/test-cases/repository-reference-copy-classification-lifecycle-test-case.md",
        "testCaseId": "repository-reference-copy-classification-lifecycle-MetamodelInstance-ConceptBeadLink",
        "testCaseName": "Repository reference copy classification lifecycle test case",
        "unsuccessfulAssertions": []
    },
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
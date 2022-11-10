<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Repository entity retype test case

This test validates that entities can be retyped.

## Operation

The entity retype tests extract each of the type definitions for entity and creates an instance of that entity type.
It retrieves the list of possible subtypes of the entity type and iterates over the list. For each possible subtype the test case
retypes the entity instance to the subtype, checks that the returned entity has the expected type, has the expected properties
and has a higher version number than previously. It then performs similar checks against the entity as retrieved from the
repository.

The test case then retypes the entity back to its original type and repeats the above checks.

Finally the testcase deletes the entity instance.

## Assertions

* **repository-entity-reidentify-01** - `typeName` new entity created.
* **repository-entity-reidentify-02** - `typeName` new entity retrieved.
* **repository-entity-reidentify-03** - `typeName` entity is retyped.
* **repository-entity-reidentify-04** - `typeName` retyped entity has expected type.
* **repository-entity-reidentify-05** - `typeName` retyped entity has expected properties.
* **repository-entity-reidentify-06** - `typeName` retyped entity version number is `version`.
* **repository-entity-reidentify-07** - `typeName` retyped entity can be retrieved.
* **repository-entity-reidentify-08** - `typeName` retyped entity has expected type.
* **repository-entity-reidentify-09** - `typeName` retyped entity has expected properties..
* **repository-entity-reidentify-10** - `typeName` retyped entity is returned.
* **repository-entity-reidentify-11** - `typeName` retyped entity has expected type.
* **repository-entity-reidentify-12** - `typeName` retyped entity has expected properties
* **repository-entity-reidentify-13** - `typeName` retyped entity version number is `version`
* **repository-entity-reidentify-14** - `typeName` retyped entity can be retrieved
* **repository-entity-reidentify-15** - `typeName` retyped entity has expected type.
* **repository-entity-reidentify-16** - `typeName` retyped entity has expected properties.


## Discovered Properties

The discovered properties for this test show which of the optional capabilities are supported for this type.

* **`typeName` retype support** : Enabled/Disabled - indicates whether the optional support for the reidentify command is enabled or not.

## Sample Output

This is the sample output for the DesignModelElement type.

```json
    {
        "class": "OpenMetadataTestCaseResult",
        "discoveredProperties": {
            "DesignModelElement retype support": "Enabled"
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
            "repository-entity-retype-01: DesignModelElement new entity created.",
            "repository-entity-retype-02: DesignModelElement new entity retrieved.",
            "repository-entity-retype-03: DesignModelElement entity is retyped.",
            "repository-entity-retype-04: DesignModelElement retyped entity has exected type.",
            "repository-entity-retype-05: DesignModelElement retyped entity has expected properties.",
            "repository-entity-retype-06: DesignModelElement retyped entity version number is 2",
            "repository-entity-retype-07: DesignModelElement retyped entity can be retrieved.",
            "repository-entity-retype-08: DesignModelElement retyped entity has expected type.",
            "repository-entity-retype-09: DesignModelElement retyped entity has expected properties.",
            "repository-entity-retype-10: DesignModelElement retyped entity can be retrieved.",
            "repository-entity-retype-11: DesignModelElement retyped entity has expected type.",
            "repository-entity-retype-12: DesignModelElement retyped entity has expected properties.",
            "repository-entity-retype-13: DesignModelElement retyped entity version number is 2",
            "repository-entity-retype-14: DesignModelElement retyped entity can be retrieved.",
            "repository-entity-retype-15: DesignModelElement retyped entity has expected type.",
            "repository-entity-retype-16: DesignModelElement retyped entity has expected properties.",
            "repository-entity-retype-03: DesignModelElement entity is retyped.",
            "repository-entity-retype-04: DesignModelElement retyped entity has expected type.",
            "repository-entity-retype-05: DesignModelElement retyped entity has expected properties.",
            "repository-entity-retype-06: DesignModelElement retyped entity version number is 2",
            "repository-entity-retype-07: DesignModelElement retyped entity can be retrieved."
            "repository-entity-retype-08: DesignModelElement retyped entity has expected type.",
            "repository-entity-retype-09: DesignModelElement retyped entity has expected properties.",
            "repository-entity-retype-10: DesignModelElement retyped entity can be retrieved."
            "repository-entity-retype-11: DesignModelElement retyped entity has expected type.",
            "repository-entity-retype-12: DesignModelElement retyped entity has expected properties.",
            "repository-entity-retype-13: DesignModelElement retyped entity version number is 2",
            "repository-entity-retype-14: DesignModelElement retyped entity can be retrieved.",
            "repository-entity-retype-15: DesignModelElement retyped entity has expected type.",
            "repository-entity-retype-16: DesignModelElement retyped entity has expected properties.",
            "repository-entity-retype-03: DesignModelElement entity is retyped.",
            "repository-entity-retype-04: DesignModelElement retyped entity has expected type.",
            "repository-entity-retype-05: DesignModelElement retyped entity has expected properties.",
            "repository-entity-retype-06: DesignModelElement retyped entity version number is 2",
            "repository-entity-retype-07: DesignModelElement retyped entity can be retrieved.",
            "repository-entity-retype-08: DesignModelElement retyped entity has expected type.",
            "repository-entity-retype-09: DesignModelElement retyped entity has expected properties.",
            "repository-entity-retype-10: DesignModelElement retyped entity can be retrieved.",
            "repository-entity-retype-11: DesignModelElement retyped entity has expected type.",
            "repository-entity-retype-12: DesignModelElement retyped entity has expected properties.",
            "repository-entity-retype-13: DesignModelElement retyped entity version number is 2",
            "repository-entity-retype-14: DesignModelElement retyped entity can be retrieved.",
            "repository-entity-retype-15: DesignModelElement retyped entity has expected type.",
            "repository-entity-retype-16: DesignModelElement retyped entity has expected properties.",
            "repository-entity-retype-03: DesignModelElement entity is retyped.",
            "repository-entity-retype-04: DesignModelElement retyped entity has expected type.",
            "repository-entity-retype-05: DesignModelElement retyped entity has expected properties.",
            "repository-entity-retype-06: DesignModelElement retyped entity version number is 2",
            "repository-entity-retype-07: DesignModelElement retyped entity can be retrieved.",
            "repository-entity-retype-08: DesignModelElement retyped entity has expected type.",
            "repository-entity-retype-09: DesignModelElement retyped entity has expected properties.",
            "repository-entity-retype-10: DesignModelElement retyped entity can be retrieved.",
            "repository-entity-retype-11: DesignModelElement retyped entity has expected type.",
            "repository-entity-retype-12: DesignModelElement retyped entity has expected properties.",
            "repository-entity-retype-13: DesignModelElement retyped entity version number is 2",
            "repository-entity-retype-14: DesignModelElement retyped entity can be retrieved.",
            "repository-entity-retype-15: DesignModelElement retyped entity has expected type.",
            "repository-entity-retype-16: DesignModelElement retyped entity has expected properties.",
            "repository-entity-retype-03: DesignModelElement entity is retyped.",
            "repository-entity-retype-04: DesignModelElement retyped entity has expected type.",
            "repository-entity-retype-05: DesignModelElement retyped entity has expected properties.",
            "repository-entity-retype-06: DesignModelElement retyped entity version number is 2",
            "repository-entity-retype-07: DesignModelElement retyped entity can be retrieved.",
            "repository-entity-retype-08: DesignModelElement retyped entity has expected type.",
            "repository-entity-retype-09: DesignModelElement retyped entity has expected properties.",
            "repository-entity-retype-10: DesignModelElement retyped entity can be retrieved.",
            "repository-entity-retype-11: DesignModelElement retyped entity has expected type.",
            "repository-entity-retype-12: DesignModelElement retyped entity has expected properties.",
            "repository-entity-retype-13: DesignModelElement retyped entity version number is 2",
            "repository-entity-retype-14: DesignModelElement retyped entity can be retrieved.",
            "repository-entity-retype-15: DesignModelElement retyped entity has expected type.",
            "repository-entity-retype-16: DesignModelElement retyped entity has expected properties."
        ],
        "testCaseDescriptionURL": "https://egeria-project.org/guides/cts/repository-workbench/test-cases/repository-entity-retype-test-case.md",
        "testCaseId": "repository-entity-retype-DesignModelElement",
        "testCaseName": "Repository entity retype test case",
        "unsuccessfulAssertions": []
    }
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
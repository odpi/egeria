<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Repository find type definitions by external standard identifiers test case

Test that type definitions with external type identifiers can be
retrieved using these identifiers as search parameters.

## Operation

The test scans all defined type definitions to determine the
expected search results and then issues findTypesByExternalID
operations to validate that all possible searches work correctly.

## Assertions

* **repository-find-types-by-external-standard-identifiers-test-case-01** All type definitions returned for external standard name `standard`
* **repository-find-types-by-external-standard-identifiers-test-case-02** All type definitions returned for external standard organization name `organization`
* **repository-find-types-by-external-standard-identifiers-test-case-03** All type definitions returned for external standard type name `typename`

## Discovered Properties

* **Number of type definitions mapped to external standard identifiers** count of the type definitions with embedded external identifiers.

## Example Output

```json
{
      "class" : "OpenMetadataTestCaseResult",
      "testCaseId" : "repository-find-types-by-external-standard-identifiers",
      "testCaseName" : "Repository find type definitions by external standard identifiers test case",
      "testCaseDescriptionURL" : "https://egeria-project.org/guides/cts/repository-workbench/repository-find-types-by-external-standard-identifiers-test-case.md",
      "assertionMessage" : "Type definitions can be extracted by external standard identifiers",
      "successfulAssertions" : [ "All type definitions returned for external standard mappings." ],
      "unsuccessfulAssertions" : [ ],
      "discoveredProperties" : {
        "Number of type definitions mapped to external standard identifiers" : 0
      }
}
```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
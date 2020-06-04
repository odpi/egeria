<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Verify consistency of type definition from event and REST API

This test case verifies that the TypeDef passed
in a type definition event is consistent with the TypeDef
returned from the same repository's API.

## Operation

The test case queries the repository API and compares the results
with the type definition located in the incoming event.

## Assertions

* **repository-consistency-of-typedef-01** - `TypeDefName` type definition from event is consistent with API

  This assertion confirms the consistency of the two TypeDefs.
  
## Discovered Properties

None.

## Example Output

Below is an example of a failed test case.  The failure occurred early in the
test cycle and so the test halted before there where any specific assertions
recorded.

```json
        {
            "class": "OpenMetadataTestCaseResult",
            "testCaseId": "repository-consistency-of-typedef-WorkflowEngine-221",
            "testCaseName": "Verify consistency of type definition from event and REST API",
            "testCaseDescriptionURL": "https://egeria.odpi.org/open-metadata-conformance-suite/docs/repository-workbench/test-cases/repository-consistency-of-typedef-test-case.md",
            "successfulAssertions": [
                "repository-test-case-base-01: Repository connector supplied to conformance suite.",
                "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite."
            ],
            "unsuccessfulAssertions": [
                "test-case-base-01: Unexpected Exception RepositoryErrorException"
            ],
            "conformanceException": {
                "exceptionClassName": "org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException",
                "errorMessage": "OMRS-REST-API-503-004 A client-side exception was received from API call getTypeDefByGUID to repository REST-connected Repository https://localhost:9443/servers/cocoMDS1.  The error message was 500 null"
            },
            "notSupportAssertions": []
        
        }
```

This next example shows the test case running successfully:

```json
        {
            "class": "OpenMetadataTestCaseResult",
            "testCaseId": "repository-consistency-of-typedef-WorkflowEngine-221",
            "testCaseName": "Verify consistency of type definition from event and REST API",
            "testCaseDescriptionURL": "https://egeria.odpi.org/open-metadata-conformance-suite/docs/repository-workbench/test-cases/repository-consistency-of-typedef-test-case.md",
            "successfulAssertions": [
                "repository-test-case-base-01: Repository connector supplied to conformance suite.",
                "repository-test-case-base-02: Metadata collection for repository connector supplied to conformance suite.",
                "repository-consistency-of-attribute-typedef-01: string  attribute type definition from event is consistent with API."
            ],
            "unsuccessfulAssertions": [],
            "notSupportAssertions": []
        
        }
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Repository classification lifecycle test case

This test validates that classifications can be managed through all stages of their life cycle.

## Operation

The classification lifecycle tests extract each of the type definitions for classifications and create an instance of
that classification and a corresponding entity so it can
drive the classification through each phase of its lifecycle validating that the classification values are correctly maintained.

## Assertions

private static final String assertion1     = testCaseId + "-01";
    private static final String assertionMsg1  = " classification can attach to a supported entity.";
    private static final String assertion2     = testCaseId + "-02";
    private static final String assertionMsg2  = " classification added to entity of type ";
    private static final String assertion3     = testCaseId + "-03";
    private static final String assertionMsg3  = " classification properties added to entity of type ";
    private static final String assertion4     = testCaseId + "-04";
    private static final String assertionMsg4  = " classification removed from entity of type ";
    private static final String assertion5     = testCaseId + "-05";
    private static final String assertionMsg5  = " classification added to all identified entities";
    

## Discovered Properties

None.


## Example Output


```json

{
      "class" : "OpenMetadataTestCaseResult",
      "testCaseId" : "repository-classification-lifecycle",
      "testCaseName" : "Repository classification lifecycle test case",
      "testCaseDescriptionURL" : "https://odpi.github.io/egeria/open-metadata-conformance-suite/docs/repository-workbench/repository-classification-lifecycle-test-case.md",
      "successMessage" : "Classifications can be managed through their lifecycle",
      "successfulAssertions" : [ "DataValue classification can attach to a supported entity.", "DataValue classification added to entity of type GlossaryTerm", "DataValue classification removed from entity of type GlossaryTerm", "DataValue classification added to all identified entities" ],
      "unsuccessfulAssertions" : [ ],
      "discoveredProperties" : { }
}

```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
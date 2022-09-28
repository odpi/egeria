<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Platform origin test case

Validate the retrieval of the origin descriptor from the server platform that hosts one or more open metadata
repositories and or servers.

## Operation

This test uses the getServerPlatformOrigin operation (`../open-metadata/platform-services/users/{userId}/server-platform-origin`)
operation to test that the platform knows its origin descriptor.
Typically this describes the vendor, product name and version of the server.

## Assertions

* **platform-origin-01** Origin descriptor retrieved from platform.

   The origin descriptor has successfully been retrieved from the server platform.
If this assertion fails, check that the server platform is started and the
open metadata services are activated.

## Discovered properties

* **Platform origin id** descriptive name for the server platform implementation

## Example output

```json
{
      "class" : "OpenMetadataTestCaseResult",
      "testCaseId" : "platform-origin",
      "testCaseName" : "Platform origin test case",
      "testCaseDescriptionURL" : "https://egeria.odpi.org/open-metadata-conformance-suite/docs/platform-workbench/platfrom-origin-test-case.md",
      "assertionMessage" : "Platform origin descriptor successfully retrieved",
      "successfulAssertions" : [ "Origin descriptor retrieved from platform." ],
      "unsuccessfulAssertions" : [ ],
      "discoveredProperties" : {
        "Repository origin id" : "Egeria OMAG Server Platform (version 3.13-SNAPSHOT)"
      }
}
```



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

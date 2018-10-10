<!-- SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

  
# Open Metadata Compliance Suite Documentation

The open metadata compliance suite contains a series of tests to exercise
the open metadata APIs of a metadata repository.  The tests are
run from a client application called **OpenMetadataTestLab** that
runs each test in turn.  It is invoked as follows:

```
$ OpenMetadataTestLab <URL root of repository under test>

```
and it prints a high level summary of the results to stdout.  Below is an
example of a failing repository.

```
$ OpenMetadataTestLab http://localhost:8080
===============================
Open Metadata Compliance Test  
===============================
Compliance Report for server: http://localhost:8080

Number of tests: 671
Number of tests passed: 357
Number of tests failed: 254
Number of tests skipped: 60

Server at http://localhost:8080 is not yet an open metadata repository

Process finished with exit code 1
```

The detailed results of the tests are accumulated
in a JSON file called `openmetadata.functional.testlab.results`.

The tests are dynamically driven from different workbenches, each focused
on a specific type of behavior.

* **[Open Metadata Repository Origin Test Workbench](origin-workbench)** retrieves
information about the specific implementation of the repository under test.

* **[Open Metadata Repository Test Workbench](repository-workbench)** tests
the behavior of the Open Metadata Repository Services (OMRS) REST API.

A workbench manages the specific environment necessary to run its tests.

Given the variety of support required by a repository to be classed as
an open metadata repository, the number of tests required to test the
breadth of its function also varies.
Some of the workbenches use information
from the repository to generate their full suite of tests, so each repository
may require a different number of tests to cover its function.

The output from the compliance suite contains:
* Information about when the test was run.
* Number of tests passing and failing.
* Details of the assertions within each test that either passed or failed.
* Additional properties about the repository including support for
optional features.

The high level structure of the results JSON document is as follows:

```json
{
  "class" : "OpenMetadataTestLabResults",
  "testRunDate" : 1534243775373,
  "serverRootURL" : "http://localhost:8080",
  "testResultsFromWorkbenches" : [ {
    "class" : "OpenMetadataTestWorkbenchResults",
    "workbenchName" : "Open Metadata Origin Test Workbench",
    "versionNumber" : "V0.1 SNAPSHOT",
    "workbenchDocumentationURL" : "https://odpi.github.io/egeria/open-metadata-compliance-suite/docs/origin-workbench",
    "passedTestCases" : [ ],
    "testCaseCount" : 1,
    "testPassCount" : 1,
    "testFailedCount" : 0,
    "testSkippedCount" : 0
  }, {
    "class" : "OpenMetadataTestWorkbenchResults",
    "workbenchName" : "Open Metadata Repository Test Workbench",
    "versionNumber" : "V0.1 SNAPSHOT",
    "workbenchDocumentationURL" : "https://odpi.github.io/egeria/open-metadata-compliance-suite/docs/repository-workbench",
    "passedTestCases" : [  ],
    "testCaseCount" : 670,
    "testPassCount" : 356,
    "testFailedCount" : 254,
    "testSkippedCount" : 60
  } ],
  "testCaseCount" : 671,
  "testPassCount" : 357,
  "testFailedCount" : 254,
  "testSkippedCount" : 60
}
```
More detail of the output from individual test cases that run
(which have been removed for clarify from the JSON snippet
above) are described for each workbench.
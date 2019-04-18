<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

  
# Open Metadata Conformance Suite Documentation

The open metadata conformance suite provides a testing framework to help the developers
integrate a specific technology into the open metadata ecosystem.

Figure 1 shows the structure of the open metadata conformance suite.

![Figure 1](conformance-suite-oveview.png)
> **Figure 1:** Overview of the open metadata conformance suite

The actual tests are run by an **open metadata conformance workbench** within the open metadata conformance suite server.
Each workbench focuses on testing a specific type of technology.
Today there are 2 workbenches:
* **[Platform Workbench](platform-workbench)** - which tests the REST API of an
[Open Metadata and Governance (OMAG) Server Platform](../../open-metadata-implementation/governance-servers/admin-services/docs/concepts/omag-server-platform.md)

* **[Repository Workbench](repository-workbench)** - which tests both the repository services 
[REST API](../../open-metadata-implementation/repository-services/docs/component-descriptions/omrs-rest-services.md)
and [event exchange](../../open-metadata-implementation/repository-services/docs/event-descriptions)
of an [open metadata repository](../../open-metadata-implementation/repository-services/docs/open-metadata-repository.md).

Future workbenches will cover other APIs and event types as well
demonstrate the ability to handle various workloads and also
a performance benchmark test.

The workbenches are configured using the OMAG Server Platform Administration Services.
This defines which workbenches to run and how to connect to the technology to test.
This configuration defines an OMAG Server that will run the requested conformance suite tests.
The requested workbenches will begin to execute their tests as soon as the OMAG server is started.

The OMAG Server also supports a REST API for querying the results of running
the conformance suite tests.  These commands include:

* Retrieve the results from a single named workbench.
* Retrieve the results from all workbenches and test cases.
* Retrieve the results from all failed test cases.
* Retrieve the results from a specific test cases.

The Open Metadata Conformance Suite also has a 
client called `OpenMetadataConformanceTestReport` that will retrieve
the conformance report and push it to a file called `openmetadata.conformance.testlab.results`.
The client also outputs a summary of the test run.
The example below is for an unsuccessful run:

```
$ OpenMetadataConformanceTestReport cSuiteServer http://localhost:8081
=======================================
 Open Metadata Conformance Test Report 
=======================================
 ... contacting conformance suite server: cSuiteServer (http://localhost:8081)
Conformance report from server:  cSuiteServer

Number of tests: 847
Number of tests passed: 846
Number of tests failed: 1
Number of tests skipped: 0

Technology under test is not yet conformant

Process finished with exit code 1
$
```

This output is an example of a successful run:

```
$ OpenMetadataConformanceTestReport cSuiteServer http://localhost:8081
=======================================
 Open Metadata Conformance Test Report 
=======================================
 ... contacting conformance suite server: cSuiteServer (http://localhost:8081)
Conformance report from server:  cSuiteServer

Number of tests: 848
Number of tests passed: 848
Number of tests failed: 0
Number of tests skipped: 0

Congratulations, technology under test is conformant

Process finished with exit code 0
$
```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
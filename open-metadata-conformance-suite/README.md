<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
  
# Open Metadata Conformance Suite

The open metadata conformance suite provides a test suite to help the developers
integrate a specific technology into the open metadata ecosystem.

The first suite of conformance tests focuses on functional
behavior of a metadata repository.  Future test suites will
demonstrate the ability to handle various workloads and also
a performance benchmark test.

Each conformance test suite is implemented by an 
**Open Metadata Test Lab**.  The test lab is a client program
that is given the URL root of the open metadata APIs for the
server to test. The example below tests a server running at http://localhost:8080

```bash
$ OpenMetadataTestLab http://localhost:8080
```

The test lab then creates one or more specialized
**Open Metadata Test Workbenches** that are able to set up
and run batches of **Open Metadata Test Cases**.

Each workbench implementation provides a specific environment
to the test cases to run in.

The test cases will issue calls to the server and assess the
response.  This is an "imitation game".
The aim of the tests is to get the same result from any
compliant open metadata repository.  If the test case
cannot tell the difference from the reference implementation
then the server being tested **is** an open metadata repository.

As the workbenches run the test cases, results are accumulated
into a test results document called
<code>openmetadata.<i>type</i>.testlab.results</code>.
For example, the `functional` test results document is
`openmetadata.functional.testlab.results`.

Each workbench and test has its own documentation page located
in the [docs](docs/README.md) directory.  Links to the
individual test's documentation is included in the test results document.



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.


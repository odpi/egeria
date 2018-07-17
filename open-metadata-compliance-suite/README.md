<!-- SPDX-License-Identifier: Apache-2.0 -->
  
# Open Metadata Compliance Suite

The open metadata compliance suite provides a test suite to help the developers
integrate a specific technology into the open metadata ecosystem.

The first suite of compliance tests focuses on functional
behavior of a metadata repository.  Future test suites will
demonstrate the ability to handle various workloads and also
a performance benchmark test.

Each compliance test suite is implemented by an 
**Open Metadata Test Lab**.  The test lab is a client program
that is given the URL root of the open metadata APIs for the
server to test. The example below tests a server running at http://localhost:8080

```
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
can not tell the difference from the reference implementation
then the server being tested **is** an open metadata repository.

As the workbenches run the test cases, results are accumulated
into a test results document called
<code>openmetadata.<i>type</i>.testlab.results</code>.
For example, the functional test results document is
<code>openmetadata.functional.testlab.results</code>.

Each workbench and test has its own documentation page located
in the [docs](docs/README.md) directory.  Links to the
individual test's documentation is included in the test results document.


  
  
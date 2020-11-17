<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Data Manager OMAS FVT Suite

The Data Manager OMAS FVT Suite drives the 
[Data Manager OMAS](../../../../open-metadata-implementation/access-services/data-manager/data-manager-client)
Java clients through the
full range of its API calls both with the server activated and with it not present.
It uses an OMAG Metadata Server.

To run the test, start the server and call the test suite as follows:

```bash
$ FVTSuite serverURLRoot serverName userId
```

Where: 
* `serverURLRoot` is the hostname and port name for the OMAG Server Platform where the
metadata server is running.  If no `serverURLRoot` is supplied, it defaults to `https://localhost:9443`.

* `serverName` is the name for the Metadata Server that is called during the tests.
If no `serverName` is supplied, it defaults to `fvtMDS`.

* `userId` is the name for the user that is issuing the calls to the Metadata Server that is called during the tests.
If no `userId` is supplied, it defaults to `testUser`.


The tests aim to be self-contained.  This means they create any metadata
they use and delete it at the end.  Metadata will only be left in the repository
if one of the test fails.


## Running in the build

These tests are also configured to run in the build using the failsafe plugin.



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
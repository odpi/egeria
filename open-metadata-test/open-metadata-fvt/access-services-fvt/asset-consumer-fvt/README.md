<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Asset Consumer OMAS FVT Suite

The Asset Consumer OMAS FVT Suite drives the 
[AssetConsumer](../../../../open-metadata-implementation/access-services/asset-consumer/asset-consumer-client/docs/user/java-client)
Java client through the
full range of its API calls both with the server activated and with it not present.
It uses an OMAG Metadata Server, typically configured with the in-memory repository
to be sure the server is empty when the test starts.

To run the test, start the server and call the test suite as follows:

```bash
$ AssetConsumerOMASFVTSuite serverURLRoot
```

Where `serverURLRoot` is the hostname and port name for the OMAG Server Platform where the
metadata server is running.

If no `serverURLRoot` is supplied, it defaults to `https://localhost:9443`.

The tests aim to be self-contained.  This means they create any metadata
they use and delete it at the end.  Metadata will only be left in the repository
if one of the test fails.

The tests are divided into three groups:
* error handling - this covers parameter validation
* crud - this covers create, update, delete and simple gets
* search - this covers locating specific instances from a collection




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
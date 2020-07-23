<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Asset Consumer OMAS FVT Suite

The Asset Consumer OMAS FVT Suite focuses initially on driving the 
[AssetConsumer](../../../../open-metadata-implementation/access-services/asset-consumer/asset-consumer-client/docs/user/java-client)
Java client through the
full range of its API calls both with the server activated and with it not present.

It uses an OMAG Server configured with the in-memory repository.
To run the test, start the server and call the test suite as follows:

```bash
$ AssetConsumerOMASFVTSuite serverURLRoot
```

Where:

* `serverURLRoot` is the hostname and port name for the OMAG Server.  For example: `https://localhost:9443`





----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
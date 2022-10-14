<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
# Asset Lineage OMAS Java Client

The Asset Lineage OMAS client interface provides the functionality that allows the external tools to scan 
 the cohort based on the entity's type and produces on the output topic events that contains the contexts for the found entities.

There is a single client called `AssetLineage`.  It has two constructors:

* No authentication embedded in the HTTP request - for test systems.
* Basic authentication using a userId and password embedded in the HTTP request.

Both constructors take the [URL root for the server platform](https://egeria-project.org/concepts/platform-url-root/)
where the Asset Lineage OMAS is running and its [server name](https://egeria-project.org/concepts/server-name/).

Here is a code example with the user id and password specified:

```
AssetLineage   client = new AssetLineage("cocoMDS1",
                                           "https://localhost:9444",
                                           "cocoUI",
                                           "cocoUIPassword");
```

This client is set up to call the `cocoMDS1` server running on the `https://localhost:9444`
OMAG Server Platform.  The userId and password is for the application
where the client is running.  The userId of the real end user is passed
on each request.

## Client operations

Once you have an instance of the client, you can use connectors to:
- get the entities by type and to publish the events that describe the lineage context on the Asset Lineage Out Topic as it is described in [**publish-entities**](publish-entities.md);
- get the asset context of the entity as it is described in [**provide-asset-context**](provide-asset-context.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
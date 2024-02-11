<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Lineage Warehouse Services Client

The client provides language-specific client packages to make it easier for applications to call the interface.

The `LineageWarehouseClient` provides an interface to query for the lineage information.
  
It has two constructors:

* No authentication embedded in the HTTP request - for test systems.
* Basic authentication using a userId and password embedded in the HTTP request.

Both constructors take the [URL root for the OMAG server platform](https://egeria-project.org/concepts/platform-url-root/)
where OLS is running and its [server name](https://egeria-project.org/concepts/server-name/).

Here is a code example with the user id and password specified:

```

LineageWarehouseInterface client = new LineageWarehouseClient(
                                        "cocoMDS1",
                                        "https://localhost:9444",
                                         "cocoUI",
                                         "cocoUIPassword");

```

This client is set up to call the `cocoMDS1` server running on the `https://localhost:9444`
OMAG Server Platform.  The userId and password is for the application
where the client is running. The userId of the real end user is passed
on each request.

## Client operations

Once you have an instance of the client, you can use it to query for [lineage](lineage.md):


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
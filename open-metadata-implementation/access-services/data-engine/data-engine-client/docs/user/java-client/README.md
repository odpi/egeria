<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project.  -->

# Data Engine Open Metadata Access Service (OMAS) Client


The Data Engine OMAS client interface supports the creation of process, ports, schema types and corresponding relationships.

There is a single client called `DataEngineImpl`.  It has two constructors:

* No authentication embedded in the HTTP request - for test systems.
* Basic authentication using a userId and password embedded in the HTTP request.

Both constructors take the [URL root for the server platform](../../../../../docs/concepts/client-server/omas-server-url-root.md)
where the Data Engine OMAS is running and its [server name](../../../../../docs/concepts/client-server/omas-server-name.md).

Here is a code example with the user id and password specified:

```java
DatEngine   client = new DataEngineImpl   ("cocoMDS1",
                                           "https://localhost:9444",
                                           "cocoUI",
                                           "cocoUIPassword");

```

This client is set up to call the `cocoMDS1` server running on the `https://localhost:9444`
OMAG Server Platform.  The userId and password is for the application
where the client is running.  The userId of the real end user is passed
on each request.

## Client operations

Once you have an instance of the client, you can use it to create the open metadata entities for the ETL jobs.

<!-- TODO  update with user doc about client operations -->

* Create a schema type
* Create a port implementation, with a schema type and the corresponding PortSchema relationship
* Create a port alias, with a PortDelegation relationship
* Create a process, with a list of the port implementations and port aliases involved in a transformation
 

> Note: The equivalent REST interfaces are documented in the
[data-engine-server](../../../../data-engine-server/README.md)
module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

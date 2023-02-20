<!-- Copyright Contributors to the ODPi Egeria project. -->

![Released](../../../../../../images/egeria-content-status-released.png#pagewidth)

# OMRS REST API Repository Connector Implementation

The [OMRS REST Repository Connector](../../../../../repository-services/docs/component-descriptions/rest-repository-connector.md) calls the 
[OMRS REST API](../../../../../repository-services/docs/component-descriptions/omrs-rest-services.md) in a remote server
in order to access and maintain metadata in a remote [open metadata repository](../../../../../repository-services/docs/open-metadata-repository.md).

It maintains an array of method names for methods that are returning the FunctionNotSupportedException.
This exception indicates that the remote repository has not implemented an optional method.

If a called method is in this array, the *OMRS REST Repository Connector* throws FunctionNotSupportedException
locally rather than issuing the remote request.

## What if the remote connector implementation is updated?

The array of method names is maintained in memory.  It is cleared when the local server is restarted.
If the remote member is significantly updated so new repository methods are supported, a restart of the local server
will clear the array and the *OMRS REST Repository Connector* will retry all the methods as they are needed.

----
Return to the [open-metadata-collection-store-connectors](..)
Link to [Egeria Docs](https://egeria-project.org/connectors/#cohort-member-client-connectors)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
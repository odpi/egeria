<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Repository server identifiers test case

Validate the retrieval of various identifiers and properties from the open metadata repository's
connector.  This connector is created from the Connection object sent to the
conformance suite server during cohort registration.

## Operation

This test uses the getter method on the repository's connector to retrieve
the identifiers related to the technology under test.

## Assertions

* **repository-server-ids-01** Repository connector retrieved from cohort registration.

    The repository connector has been successfully retrieved from the registration message sent from the technology
    under test.  The local server will initialize the connector with information from the registration request and
    other local configuration.

* **repository-server-ids-02** Retrieved helper object for building TypeDefs and metadata instances from repository connector.

    The repository helper provides many methods to the repository connector for building valid type definitions
    (TypeDefs) and metadata instances (entities, relationships and classifications).  The repository validator
    is passed to the newly created repository connector.
    This assertion validates that this object is set up in the repository connector.

* **repository-server-ids-03** Retrieved validator object to check the validity of TypeDefs and metadata instances from repository connector.

    The repository validator provides many validation methods for type definitions (TypeDefs)
    and metadata instances (entities, relationships and classifications).  It is supplied to the newly created repository connector.
    This assertion validates that this object is set up in the repository connector.
    
* **repository-server-ids-04** Retrieved correct local user Id from repository connector.

    The local user Id should be used on all outbound events sent from the repository connector.
    This userId is passed to the repository connector during its initialization.  This assertion
    checks that the repository connector is able to return the same value as was provided
    during initialization.
    
* **repository-server-ids-05** Retrieved correct max page size from repository connector.

    The maximum page size is a value configured in the connector during its initialization.  It is used to
    restrict the number of result objects that can be returned from a single request.  The repository connector
    should return the same page size limit on the `getMaxPageSize()` method as was passed to it during initialization.

## Discovered properties

* **repository name** - Name of the repository.  The default is "Open Metadata Repository".  This value can be
over-ridden by setting the displayName property in the Connection object that flows
in the registration request.  For repositories managed by OMAG Servers running in the
OMAG Server platform, this Connection object is the `LocalRepositoryRemoteConnection`.

* **server name** - name of the server where the repository is accessed from.

* **server type** - type name of the server where the repository is managed from.

* **organization name** - name of the organization that owns the repository.

## Example output

```json
{
 
 
}
```



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
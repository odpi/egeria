<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Repository type definition test case

Test that the type definition is properly filled out
and can be retrieved in different ways.

## Operation

This test uses calls to retrieve type definitions and validates that the content
is valid and consistent with other calls.

## Assertions

* **repository-typedef-`type name`-01** - type definition has a name.
  
  Each type definition should have a unique name.
  
* **repository-typedef-`type name`-02** - type definition has a guid.

  Each type definition should have a unique identifier.  The combination
  of the name and guid is often used to ensure the correct type is being used
  since it is possible that two repositories may define different types
  with the same name.  These two types are unlikely to have the same guid so it
  is possible to identify the conflict.
  
* **repository-typedef-`type name`-03** - type definition has a version number.

  Each type definition has a version number that starts with 1 and increments
  each time the type definition is updated.  This version number is used to
  sequence updates that are received from remote repository.
  
* **repository-typedef-`type name`-04** - type definition has a version name.

  Each type definition has a descriptive version name.  This is used to identify
  major and minor changes between versions.  The version name is descriptive
  and not parsed or interpreted by OMRS.
  
* **repository-typedef-`type name`-05** - type definition has a valid category.

  Each type definition has a category that indicates whether the it is
  an entity definition (EntityDef), a relationship definition (RelationshipDef)
  or a classification definition (ClassificationDef).
  
* **repository-typedef-`type name`-06** - type definition has a valid origin.

  Each type definition has the unique identifier of the archive or repository
  where it was defined.
  
* **repository-typedef-`type name`-07** - type definition has a creator.

  Each type definition has the name of the person or process that created it.
  There is also a field for the last person to update the type definition
  which is set of the version is greater than one.
  
* **repository-typedef-`type name`-08** - type definition has a creation date.

  Each type definition has the date/time it was first created.  There is also
  a field for the date/time that the type definition was last updated
  which is set of the version is greater than one.
  
* **repository-typedef-`type name`-09** - type definition has a valid initial status.

  Each type definition has a value for the default initial status that an
  instance of this type is set to when it is created.  This initial status
  can be overridden on the create request for the instance.
  
* **repository-typedef-`type name`-10** - type definition has a list of valid statuses.

  Each type definition describes the list of valid status that an instance can
  be set to during its lifetime.  OMRS defines a list of possible statuses to
  choose from in the 
  **[InstanceStatus](https://github.com/odpi/egeria/blob/master/open-metadata-implementation/repository-services/repository-services-apis/src/main/java/org/odpi/openmetadata/repositoryservices/connectors/stores/metadatacollectionstore/properties/instances/InstanceStatus.java)** enum.
  
* **repository-typedef-`type name`-11** - classification can be added to at least one entity.

  A type definition for a classification (ClassificationDef) includes a list of
  one of more entity type definition names that define which types of entities
  can have this classification attached.
  
* **repository-typedef-`type name`-12** - relationship type definition has two ends.

  A type definition for a relationship (RelationshipDef) must define the types
  of the entities that it links together.  These are defined in an end definition
  (RelationshipEndDef).
  
* **repository-typedef-`type name`-13** - type verified by repository.

  The repository has verified that it supports the type definition through
  the verifyTypeDef (`../open-metadata/repository-services/users/{userId}/types/typedef/compatibility`)
  operation.
  
* **repository-typedef-`type name`-14** - type retrieved from repository by name.

  The repository has returned a type definition with the same name through
  the getTypeDefByName (`../open-metadata/repository-services/users/{userId}/types/typedef/name/{name}`)
  operation.

* **repository-typedef-`type name`-15** - same type retrieved from repository by name.

  The type definition returned by getTypeDefByName is identical to the one
  returned by getAllTypes.
  
* **repository-typedef-`type name`-16** - type retrieved from repository by guid.

  The repository has returned a type definition with the same guid through
  the getTypeDefByGUID (`../open-metadata/repository-services/users/{userId}/types/typedef/{guid}`)
  operation.
  
* **repository-typedef-`type name`-17** - same type retrieved from repository by guid.

  The type definition returned by getTypeDefByGUID is identical to the one
  returned by getAllTypes.
  
* **repository-typedef-`type name`-18** - type found by repository by name.

  The repository has returned a type definition with the same name through
  the findTypesByName (`../open-metadata/repository-services/users/{userId}/types/by-name?name={name}`)
  operation.
  
* **repository-typedef-`type name`-19** - same type found by repository by name.

  The type definition returned by findTypesByName is identical to the one
  returned by getAllTypes.
  
* **repository-typedef-`type name`-20** - type name is unique.

  Only one type with this type definition's name was returned by findTypesByName.

## Example output

```json
{
      "class" : "OpenMetadataTestCaseResult",
      "testCaseId" : "repository-typedef-LicenseType",
      "testCaseName" : "Repository type definition test case",
      "testCaseDescriptionURL" : "https://egeria.odpi.org/open-metadata-conformance-suite/docs/origin-workbench/repository-typedef-test-case.md",
      "assertionMessage" : "LicenseType type definition is compliant",
      "successfulAssertions" : [ "LicenseType type definition has a name.", "LicenseType type definition has a guid.", "LicenseType type definition has a version number.", "LicenseType type definition has a version name.", "LicenseType type definition has a valid category.", "LicenseType type definition has a valid origin.", "LicenseType type definition has a creator.", "LicenseType type definition has a creation date.", "LicenseType type definition has an initial status.", "LicenseType type definition has a list of valid statuses.", "LicenseType type verified by repository.", "LicenseType type retrieved from repository by name.", "LicenseType same type retrieved from repository by name.", "LicenseType type retrieved from repository by guid.", "LicenseType same type retrieved from repository by guid.", "LicenseType type found by repository by name.", "LicenseType same type found by repository by name.", "LicenseType type name is unique." ],
      "unsuccessfulAssertions" : [ ],
      "discoveredProperties" : {
        "LicenseType version" : "1.0.1",
        "LicenseType category" : "EntityDef",
        "LicenseType unique identifier (GUID)" : "046a049d-5f80-4e5b-b0ae-f3cf6009b513",
        "LicenseType description" : "A type of license that sets out specific terms and conditions for the use of an asset."
      }
}
```



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
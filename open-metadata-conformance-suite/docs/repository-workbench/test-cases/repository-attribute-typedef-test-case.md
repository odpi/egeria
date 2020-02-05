<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Repository type definition test case

Test that the attribute type definition is properly filled out
and can be retrieved in different ways.

## Operation

This test uses calls to retrieve attribute
type definitions and validates that the content
is valid and consistent with other calls.

## Assertions

* **repository-attribute-typedef-`type name`-01** - type definition has a name.
  
  Each type definition should have a unique name.
  
* **repository-attribute-typedef-`type name`-02** - type definition has a guid.

  Each type definition should have a unique identifier.  The combination
  of the name and guid is often used to ensure the correct type is being used
  since it is possible that two repositories may define different types
  with the same name.  These two types are unlikely to have the same guid so it
  is possible to identify the conflict.
  
* **repository-attribute-typedef-`type name`-03** - type definition has a version number.

  Each type definition has a version number that starts with 1 and increments
  each time the type definition is updated.  This version number is used to
  sequence updates that are received from remote repository.
  
* **repository-attribute-typedef-`type name`-04** - type definition has a version name.

  Each type definition has a descriptive version name.  This is used to identify
  major and minor changes between versions.  The version name is descriptive
  and not parsed or interpreted by OMRS.
  
* **repository-attribute-typedef-`type name`-05** - type definition has a valid category.

  Each type definition has a category that indicates whether the it is
  primitive attribute (PrimitiveDef), a Collection attribute (CollectionDef)
  or an enumeration definition (EnumDef).
  
* **repository-attribute-typedef-`type name`-06** - primitive type definition has a valid category.

  Each primitive attribute has a primitive category that defines the specific type of
  of the attribute such as string, integer, etc.
  
* **repository-attribute-typedef-`type name`-07** - collection type definition has a valid category.

  Each collection type definition has a category that states the type of collection
  such as array, map etc.
  
* **repository-attribute-typedef-`type name`-08** - collection type definition has a valid argument count and element types.

  Each collection type definition has argument types to control its contents.
  
* **repository-attribute-typedef-`type name`-09** - enum type definition has elements.

  Each enumeration type definition has a list of valid value elements.
  
* **repository-attribute-typedef-`type name`-10** - type verified by repository.

  The repository has verified that it supports the type definition through
  the verifyAttributeTypeDef (`../open-metadata/repository-services/users/{userId}/types/attribute-typedef/compatibility`)
  operation.
  
* **repository-attribute-typedef-`type name`-11** - type retrieved from repository by name.

  The repository has returned a type definition with the same name through
  the getAttributeTypeDefByName (`../open-metadata/repository-services/users/{userId}/types/attribute-typedef/name/{name}`)
  operation.

* **repository-attribute-typedef-`type name`-12** - same type retrieved from repository by name.

  The type definition returned by getAttributeTypeDefByName is identical to the one
  returned by getAllTypes.
  
* **repository-attribute-typedef-`type name`-13** - type retrieved from repository by guid.

  The repository has returned a type definition with the same guid through
  the getTypeDefByGUID (`../open-metadata/repository-services/users/{userId}/types/typedef/{guid}`)
  operation.
  
* **repository-attribute-typedef-`type name`-14** - same type retrieved from repository by guid.

  The type definition returned by getTypeDefByGUID is identical to the one
  returned by getAllTypes.


## Discovered Properties


* **`attributeTypeDefName` unique identifier (GUID)** 
* **`attributeTypeDefName` description**
* **`attributeTypeDefName` category**
* **`attributeTypeDefName` version**
* **`attributeTypeDefName` description GUID** (if not null)


## Example Output

```json
{
      "class" : "OpenMetadataTestCaseResult",
      "testCaseId" : "repository-attribute-typedef-GovernanceDomain",
      "testCaseName" : "Repository attribute type definition test case",
      "testCaseDescriptionURL" : "https://egeria.odpi.org/open-metadata-conformance-suite/docs/origin-workbench/repository-attribute-typedef-test-case.md",
      "assertionMessage" : "GovernanceDomain attribute type definition is compliant",
      "successfulAssertions" : [ "GovernanceDomain type definition has a name.",
                                 "GovernanceDomain type definition has a guid.",
                                 "GovernanceDomain type definition has a version number.",
                                 "GovernanceDomain type definition has a version name.",
                                 "GovernanceDomain type definition has a valid category.",
                                 "GovernanceDomain enum type definition has elements.",
                                 "GovernanceDomain type verified by repository.",
                                 "GovernanceDomain type retrieved from repository by name.",
                                 "GovernanceDomain same type retrieved from repository by name.",
                                 "GovernanceDomain type retrieved from repository by guid.",
                                 "GovernanceDomain same type retrieved from repository by guid." ],
      "unsuccessfulAssertions" : [ ],
      "discoveredProperties" : {
        "GovernanceDomain unique identifier (GUID)" : "baa31998-f3cb-47b0-9123-674a701e87bc",
        "GovernanceDomain description" : "Defines the governance domains that open metadata seeks to unite.",
        "GovernanceDomain category" : "EnumDef",
        "GovernanceDomain version" : "1.0.1" }
}
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
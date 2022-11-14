<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Repository find attribute type definitions by category test case

This test validates that all of the supported AttributeTypeDefs can be retrieved by category.

## Operation

Extract all known AttributeTypeDefs.  Separate them into three lists: 
Primitive AttributeTypeDefs (PrimitiveDefs), 
Collection AttributeTypeDefs (CollectionDefs)
and Enumeration AttributeTypeDefs (EnumDefs).
Issue a query for each of these categories of AttributeTypeDef and validate that what is returned matches
the original complied lists.

## Assertions

* **repository-find-attribute-typedefs-by-category-01** - All attribute type definitions returned by category.

   The correct list of AttributeTypeDefs were returned.


## Discovered Properties

* **Number of supported PrimitiveDefs** - count of returned primitive attribute types
* **Supported PrimitiveDefs** - list of returned primitive attribute types
* **Number of supported CollectionDefs** - count of returned collection attribute types
* **Supported CollectionDefs** - list of returned collection attribute types
* **Number of supported EnumDefs** - count of returned enumeration attribute types
* **Supported EnumDefs** - list of returned enumeration attribute types

## Example Output


```json
{
      "class" : "OpenMetadataTestCaseResult",
      "testCaseId" : "repository-find-attribute-typedefs-by-category",
      "testCaseName" : "Repository find attribute type definitions by category test case",
      "testCaseDescriptionURL" : "https://egeria-project.org/guides/cts/repository-workbench/repository-find-attribute-typedefs-by-category-test-case.md",
      "assertionMessage" : "Attribute type definitions can be extracted by category",
      "successfulAssertions" : [ "All attribute type definitions returned by category." ],
      "unsuccessfulAssertions" : [ ],
      "discoveredProperties" : {
        "Number of supported CollectionDefs" : 4,
        "Number of supported EnumDefs" : 24,
        "Number of supported PrimitiveDefs" : 13,
        "Supported PrimitiveDefs" : [ "string", "char", "boolean", "biginteger", "byte", "long", "double", "date", "object", "bigdecimal", "short", "float", "int" ],
        "Supported CollectionDefs" : [ "array<int>", "map<string,string>", "array<string>", "map<string,object>" ],
        "Supported EnumDefs" : [ "CrowdSourcingRole", "CriticalityLevel", "TermRelationshipStatus", "DataClassAssignmentStatus", "Endianness", "ContactMethodType", "GovernanceClassificationStatus", "BusinessCapabilityType", "StarRating", "OperationalStatus", "OrderBy", "TermAssignmentStatus", "RetentionBasis", "ServerAssetUse", "ConfidentialityLevel", "MediaUsage", "ActivityType", "CommunityMembershipType", "ConfidenceLevel", "KeyPattern", "CommentType", "MediaType", "GovernanceDomain", "ToDoStatus" ]
      }
}
```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
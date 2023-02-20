<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Repository classification lifecycle test case

This test validates that classifications can be managed through all stages of their life cycle.

Classifications are attached to entities.
They provide additional properties and a label that characterizes
the entity that it is connected to.

Each type of classification is defined by a ClassificationDef.
This test is run for each ClassificationDef supported by the repository.
The ClassificationDef includes a list of entity types that support this type
of classification.

## Operation

The classification lifecycle test extracts each of the type definitions the entities supported
by the ClassificationDef.
It creates an entity of each type and 
for classifications and create an instance of
that classification and a corresponding entity so it can
drive the classification through each phase of its lifecycle validating that the classification values are correctly maintained.

## Assertions
  
* **repository-classification-lifecycle-`classificationName`-`entityType`-01** - No classifications attached to new entity of type `typeName`.
* **repository-classification-lifecycle-`classificationName`-`entityType`-02** - `typeName` entity return when classification added.
* **repository-classification-lifecycle-`classificationName`-`entityType`-03** - classification added to entity of type `typeName`.
* **repository-classification-lifecycle-`classificationName`-`entityType`-04** - classification properties added to entity of type `typeName`.
* **repository-classification-lifecycle-`classificationName`-`entityType`-05** - classification removed from entity of type `typeName`.
    

## Discovered Properties


## Example Output


```json
{
      "class" : "OpenMetadataTestCaseResult",
      "testCaseId" : "repository-classification-lifecycle",
      "testCaseName" : "Repository classification lifecycle test case",
      "testCaseDescriptionURL" : "https://egeria-project.org/guides/cts/repository-workbench/test-cases/repository-classification-lifecycle-test-case.md",
      "assertionMessage" : "Classifications can be managed through their lifecycle",
      "successfulAssertions" : [ "DataValue classification can attach to a supported entity.", "DataValue classification added to entity of type GlossaryTerm", "DataValue classification removed from entity of type GlossaryTerm", "DataValue classification added to all identified entities" ],
      "unsuccessfulAssertions" : [ ],
      "discoveredProperties" : { }
}
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
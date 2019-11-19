<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Asset Consumer's Glossary Interface (AssetConsumerGlossaryInterface)

AssetConsumerGlossaryInterface provides the ability to look up the
meaning of terms associated with an asset.

* **getMeaning** - Returns the full definition (meaning) of a term using the unique identifier of the glossary term
that contains the definition.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/get-meaning-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/get-meaning-with-rest.md)

* **getMeaningsByName** - Returns a list of the full definition (meaning) of the terms matching the supplied name.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/get-meaning-by-name-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/get-meaning-by-name-with-rest.md)
           
* **findMeanings** - Returns a list of the full definition (meaning) of the terms matching the supplied search string - this search string may include wild card characters.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/find-meanings-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/find-meanings-with-rest.md)
           
* **getAssetsByMeaning** - Returns a list of unique identifiers for Assets that are linked to the supplied term.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/java-client/get-assets-by-meaning-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/get-assets-by-meaning-with-rest.md)
           

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
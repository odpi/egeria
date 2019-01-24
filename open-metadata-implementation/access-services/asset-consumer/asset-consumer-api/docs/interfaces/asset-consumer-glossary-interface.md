<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Asset Consumer's Glossary Interface (AssetConsumerGlossaryInterface)

AssetConsumerGlossaryInterface provides the ability to look up the
meaning of terms associated with an asset.

* **getMeaning** - Returns the full definition (meaning) of a term using the unique identifier of the glossary term
that contains the definition.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/get-meaning-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/get-meaning-with-rest.md)

* **getMeaningByName** - Returns the full definition (meaning) of the terms matching the supplied name.

  Implementation: 
  [Java](../../../asset-consumer-client/docs/user/get-meaning-by-name-with-java.md),
  [REST](../../../asset-consumer-server/docs/user/get-meaning-by-name-with-rest.md)
           

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Asset Consumer OMAS Java Client

The Asset Consumer OMAS client interface supports the creation of
[connectors](../../../../../../frameworks/open-connector-framework/docs/concepts/connector.md) to access
[assets](../../../../../docs/concepts/assets).  It also supports feedback and tagging of assets.

There is a single client called `AssetConsumer`.  It has two constructors:

* No authentication embedded in the HTTP request - for test systems.
* Basic authentication using a userId and password embedded in the HTTP request.

Both constructors take the [URL root for the server platform](../../../../../docs/concepts/client-server/omas-server-url-root.md)
where the Asset Consumer OMAS is running and its [server name](../../../../../docs/concepts/client-server/omas-server-name.md).

Here is a code example with the user id and password specified:

```
AssetConsumer   client = new AssetConsumer("cocoMDS1",
                                           "https://localhost:9444",
                                           "cocoUI",
                                           "cocoUIPassword");
```

This client is set up to call the `cocoMDS1` server running on the `https://localhost:9444`
OMAG Server Platform.  The userId and password is for the application
where the client is running.  The userId of the real end user is passed
on each request.

## Client operations

Once you have an instance of the client, you can use connectors to get access to assets and the
metadata properties that describe them: 

* [**get asset using a supplied connection name**](get-asset-for-connection-name-with-java.md)
* [**get asset using a supplied connection guid**](get-asset-for-connection-guid-with-java.md)
* [**get asset properties using a supplied asset guid**](get-asset-properties-with-java.md)
* [**get a list of asset guids for assets with names that match the supplied search string**](get-asset-list-by-name-with-java.md)
* [**get a list of asset guids for assets with the supplied token (either guid or name)**](get-asset-list-by-token-with-java.md)
* [**get connector using a supplied connection object**](get-connector-by-connection-with-java.md)
* [**get connector using a supplied connection guid**](get-connector-by-guid-with-java.md)
* [**get connector using a supplied connection name**](get-connector-by-name-with-java.md)
* [**get connector using a supplied asset guid**](get-connector-by-asset-guid-with-java.md)
* [**find assets**](find-assets-with-java.md)

Provide feedback, on either the content of the asset, or the metadata properties:

* [**add comment to an asset's properties**](add-comment-to-asset-with-java.md)
* [**add a reply to an existing comment**](add-comment-reply-with-java.md)
* [**update the text in a comment**](update-comment-with-java.md)
* [**remove comment from an asset's properties**](remove-comment-from-asset-with-java.md)
* [**add like to an asset's properties**](add-like-to-asset-with-java.md)
* [**remove like from an asset's properties**](remove-like-from-asset-with-java.md)
* [**add review and/or star rating to an asset's properties**](add-review-to-asset-with-java.md)
* [**update review and/or star rating assigned to an asset's properties**](find-tags-with-java.md)
* [**remove review/star rating from an asset's properties**](remove-review-from-asset-with-java.md)

Maintain tags that add more information about the asset to the metadata properties:

* [**create a private tag**](create-private-tag-with-java.md)
* [**create a public tag**](create-public-tag-with-java.md)
* [**update a tag's description**](update-tag-description-with-java.md)
* [**delete a tag and all of the links to assets**](delete-tag-with-java.md)
* [**get the tag definition of a tag using a supplied guid**](get-tag-with-java.md)
* [**get a list of tags using a supplied name**](get-tags-by-name-with-java.md)
* [**get a list of private tags created by the caller using a supplied name**](get-tags-by-name-with-java.md)
* [**get a list of tags using a supplied search string**](find-tags-with-java.md)
* [**link a tag to an asset's properties**](add-tag-to-asset-with-java.md)
* [**remove the link to tag from an asset's properties**](remove-tag-from-asset-with-java.md)
* [**get assets attached to a specific tag**](get-assets-by-tag-with-java.md)

Retrieve additional information about the meaning of data associated with the asset.

* [**get full description of meaning using a supplied search string**](get-meaning-by-name-with-java.md)
* [**get full description of meaning using a glossary term guid**](get-meaning-with-java.md)
* [**get a list of possible meanings for a term**](find-meanings-with-java.md)
* [**get assets attached to a specific glossary term](get-assets-by-meaning-with-java.md)

Add an audit log message about the asset:

* [**add log message about the asset to the server's audit log**](add-log-message-to-asset-with-java.md)

The [**Asset Management Samples**](../../../../../../../open-metadata-resources/open-metadata-samples/access-services-samples/asset-management-samples)
provide examples of the Asset Consumer Java client in action.

> Note: The equivalent REST interfaces are documented in the
[asset-consumer-server](../../../../asset-consumer-server/docs/user)
module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

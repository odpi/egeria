<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Asset Consumer OMAS Java Client

The Asset Consumer OMAS client interface supports the creation of
[connectors](../../../../../../frameworks/open-connector-framework/docs/concepts/connector.md) to access
[assets](../../../../../docs/concepts/assets).  It also supports feedback and tagging of assets.

There is a single client called `AssetConsumer`.  It has two constructors:

* No authentication embedded in the HTTP request - for test systems.
* Basic authentication using a userId and password embedded in the HTTP request.

Both constructors take the [URL root for the server](../../../../../docs/concepts/client-server/omas-server-url-root.md)
where the Asset Consumer OMAS is running and its [server name](../../../../../docs/concepts/client-server/omas-server-name.md).


## Client operations

* [**add comment reply**](add-comment-reply-with-java.md)
* [**add comment to asset**](add-comment-to-asset-with-java.md)
* [**add like to asset**](add-like-to-asset-with-java.md)
* [**add log message to asset**](add-log-message-to-asset-with-java.md)
* [**add review to asset**](add-review-to-asset-with-java.md)
* [**add tag to asset**](add-tag-to-asset-with-java.md)
* [**create private tag**](create-private-tag-with-java.md)
* [**create public tag**](create-public-tag-with-java.md)
* [**delete tag**](delete-tag-with-java.md)
* [**get asset for connection name**](get-asset-for-connection-name-with-java.md)
* [**get asset for connection guid**](get-asset-for-connection-guid-with-java.md)
* [**get asset properties**](get-asset-properties-with-java.md)
* [**get connector by connection object**](get-connector-by-connection-with-java.md)
* [**get connector by guid**](get-connector-by-guid-with-java.md)
* [**get connector by name**](get-connector-by-name-with-java.md)
* [**get meaning by name**](get-meaning-by-name-with-java.md)
* [**get meaning**](get-meaning-with-java.md)
* [**get tag**](get-tag-with-java.md)
* [**get tags by name**](get-tags-by-name-with-java.md)
* [**remove comment from asset**](remove-comment-from-asset-with-java.md)
* [**remove like from asset**](remove-like-from-asset-with-java.md)
* [**remove review from asset**](remove-review-from-asset-with-java.md)
* [**remove tag from asset**](remove-tag-from-asset-with-java.md)
* [**update comment**](update-comment-with-java.md)
* [**update review on asset**](update-review-on-asset-with-java.md)
* [**update tag description**](update-tag-description-with-java.md)


> Note: The equivalent REST interfaces are documented in the
[asset-consumer-server](../../../../asset-consumer-server/docs/user)
module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
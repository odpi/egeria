<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![Released](../../../open-metadata-publication/website/images/egeria-content-status-released.png#pagewidth)

# Asset Consumer Open Metadata Access Service (OMAS)

The Asset Consumer OMAS provides services to an individual who wants to work
with [assets](../docs/concepts/assets) such as:

* data stores, data sets and data feeds
* reports
* APIs
* functions such as analytical services

It supports:

* the retrieval of [connection](../../frameworks/open-connector-framework/docs/concepts/connection.md)
  objects from the open metadata repositories.  A connection object is used to create a connector to an asset.
* the creation of a [connector](../../frameworks/open-connector-framework/docs/concepts/connector.md)
based on the properties in a connection object.
* the retrieval of properties about an asset.  These properties are called the
  [connected asset properties](../../frameworks/open-connector-framework/docs/concepts/connected-asset-properties.md).
* the adding of feedback (comments, ratings and likes) to an asset.
* the attachment of [informal tags](../docs/concepts/attachments/informal-tags.md) to an asset.
* the adding of an audit log record for an asset.
* the publishing of notifications about assets on Asset Consumer OMAS's [out topic](../docs/concepts/client-server/out-topic.md).

Adding feedback through the Asset Consumer OMAS results in [Karma Points](../community-profile/docs/concepts/karma-point.md) being awarded
to the individual.  These are maintained in the individual's profile.
A karma point is awarded for each contribution of feedback
through the API. (The awarding of Karma points is managed by the
[Community Profile OMAS](../community-profile).)

The connectors returned by the Asset Consumer OMAS are [Open Connector
Framework (OCF)](../../frameworks/open-connector-framework) connectors.
The caller can use the connector to access
the contents of the asset itself and the properties about the
asset it is accessing.   This service is provided by the
[OCF Metadata Management Common Service](../../common-services/ocf-metadata-management).

## Digging Deeper

* [User Documentation](docs/user)
* [Design Documentation](docs/design)


----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

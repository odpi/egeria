<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# The Smart Collections Integration Connector

The **SmartCollectionsIntegrationConnector** maintains the membership of Results Set collections that are attached
to it as catalog targets.

For each results set catalog target, the connector navigates the **SmartQuery** relationship to find the
**SavedQuery** asset that defines how to work out the collection's current membership.  It issues the saved
query's REST call and adjusts the results set's membership so that it matches the elements returned, updating the
results set's `createdTime`/`startTime`/`completionTime`/`completionMessage` properties as it does so.

## Deployment and configuration

The connector requires a secret to be configured so that it can authenticate the REST calls it issues to run each
results set's saved query.

See [Egeria-smart-collections.http](Egeria-smart-collections.http) for a worked example that creates a results set
collection and a saved query, links them together, attaches the results set to this connector as a catalog target,
and triggers a refresh.


----
* Return to [Integration Connectors module](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

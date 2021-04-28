<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 2.8 (April 2021)

Release 2.8 adds:
* New support for event and property filtering for the open metadata server security connector
* Changes to metadata types
* New performance workbench for the CTS (technical preview)
* New interface for retrieving the complete history of a single metadata instance
* Splitting of CTS results into multiple smaller files

Details of these and other changes are in the sections that follow.

## Description of Changes

### Updates to the Open Metadata Security Connector

Before this release, the repository services support 3 filtering points for managing events for the OMRS Cohort Topic.

* Should an event be sent to the cohort
* Should an event be retrieved from the cohort
* Should a received event be stored in the local repository

These filtering points are set up in the configuration document of the server.
It is possible to specify rules to determine which types of events and which types of metadata elements are filtered out.
However this configuration provides no control to allow filtering of events for specific instances.

This release extends the metadata server security connector so it can be called at these same filter points.
This will be through optional interfaces that the security connector can choose to implement.

If the current rules are set up, they will still be executed. This change complements the existing filtering.

The server security connector also implements the repository security interface called when metadata is being
added/updated/deleted/retrieved through the APIs. Extending the security connector for event filtering means that it can make consistent decisions on the sharing of metadata through the cohorts and through the APIs.

##### Configuring the server security connector

Configuring the server security connector will not change with this feature.
If the connector needs custom attributes to select rule sets etc, these can be specified in the configuration properties.

See [https://egeria.odpi.org/open-metadata-implementation/admin-services/docs/user/configuring-the-server-security-connector.html](../open-metadata-implementation/admin-services/docs/user/configuring-the-server-security-connector.md).

##### Implementing the server security connector

The security server connector will have two new interfaces that it can implement: one for the cohort events and one for saving events to the local repository.

* The event interface will have two methods, one for sending and one for receiving. The parameters will include the cohort name and the event contents. It can return the event unchanged, return a modified event (eg with sensitive content removed) or return null to say that the event is filtered out.

* The event saving interface will receive the instance header and can return a boolean to indicate if the local repository should store it. If true is returned, the refresh event sequence is initiated. The repository connector then has the ultimate choice when the refreshed instance is returned from the home repository as to whether to store it or not.

There is a single instance of the connector in the server so it is able to maintain counts and cache rules etc. It can also be implemented as a facade to a proprietary service.

More information on the security connector can be found 
on this page:

[https://egeria.odpi.org/open-metadata-implementation/common-services/metadata-security/metadata-security-apis](../open-metadata-implementation/common-services/metadata-security/metadata-security-apis)

### Metadata Types

* Updates to the location types in [model 0025](../open-metadata-publication/website/open-metadata-types/0025-Locations.md):
   * Add the **mapProjection** property to the **FixedLocation** classification
   * Change the **address** property to **networkAddress** in the **CyberLocation** classification
   * Deprecated **HostLocation** in favor of the **AssetLocation** relationship 
   
* Deprecate the **RuntimeForProcess** relationship since it is superfluous - use **ServerAssetUse** since
  **Application** is a **SoftwareServerCapability**.
  See [model 0045](../open-metadata-publication/website/open-metadata-types/0045-Servers-and-Assets.md).

* Replace the **deployedImplementationType** property with the **businessCapabilityType** in the **BusinessCapability**
  since it is a more descriptive name.
  See [model 0440](../open-metadata-publication/website/open-metadata-types/0440-Organizational-Controls.md).

### Performance workbench

The performance workbench intends to test the response time of all repository (metadata collection) methods for the
technology under test. The volume of the test can be easily configured to also test scalability.

More information is available [in the workbench's documentation](../open-metadata-conformance-suite/docs/performance-workbench/README.md).

### Instance history interface

Two new (optional) methods have been introduced to the metadata collection interface:

* `getEntityDetailHistory`
* `getRelationshipHistory`

Both methods take the GUID of the instance for which to retrieve history, an optional range of times between which to
retrieve the historical versions (or if both are `null` to retrieve _all_ historical versions), and a set of paging
parameters.

If not implemented by a repository, these will simply throw `FunctionNotSupported` exceptions by default
to indicate that they are not implemented.

### CTS results output

Up to this release, the detailed results of a CTS run could only be be retrieved by pulling a huge
(100's of MB) file across the REST interface for the CTS. Aside from not typically working with most REST clients
(like Postman), this had the additional impact of a sudden huge hit on the JVM heap to serialize such a large JSON
structure (immediately grabbing ~1GB of the heap).

While this old interface still exists for backwards compatibility, the new default interface provided in this release
allows users to pull down just an overall summary of the results separately from the full detailed results, and the
detailed results are now broken down into separate files by profile and test case: each of which can therefore be
retrieved individually.

(So, for example, if you see from the summary that only 1-2 profiles are not conformant, you can retrieve just the
details for those profiles rather than all details.)

### Changes to deployment of the Polymer based UI

In previous releases, a zuul router component was used within the UI server chassis to route requests
for static content to a separate server.

In this release any routing needs to be setup externally, for example by placing a nginx proxy in front of both the ui
chassis and static content server. This is now done by our docker-compose environment & helm charts so to access
the UI you need to go to the nginx proxy. Further summary information can be found in the documentation for those assets.

### Bug fixes and other updates

* Additional Bug Fixes
* Dependency Updates

For details on both see the commit history in GitHub.

## Known Issues

* It is recommended to use a chromium-based browser such as Google Chrome or Microsoft Edge, or Apple Safari for the Egeria React UI. Some parts of the UI experience such as Dino currently experience problems with Firefox. See [odpi/egeria-react-ui#96](https://github.com/odpi/egeria-react-ui/issues/96) .
* Egeria source code currently fails to build on Windows natively. Please use Linux, MacOS, or compile under WSL/WSL2 on Windows. See [#4917](https://github.com/odpi/egeria/issues/4917)
* Several Java samples fail (governance, admin) - [#4656](https://github.com/odpi/egeria/issues/4656),  [#4662](https://github.com/odpi/egeria/issues/4656),  [#4056](https://github.com/odpi/egeria/issues/4056)
* The React UI used by the helm charts and compose is based on react UI release 2.7.0 due to layout issues found with 2.8.0. See  [#5022](https://github.com/odpi/egeria/issues/5022)
* The platform services notebook may fail to query servers correctly. See [#5023](https://github.com/odpi/egeria/issues/5023)
* The building a data catalog notebook may fail if run quickly. See [#2688](https://github.com/odpi/egeria/issues/2688)
* The data curation notebook is incomplete and still being developed. The final steps may fail to work in a container environment. See [#5021](https://github.com/odpi/egeria/issues/5021)

# Egeria Implementation Status at Release 2.8

![Egeria Implementation Status](../open-metadata-publication/website/roadmap/functional-organization-showing-implementation-status-for-2.8.png#pagewidth)

Link to Egeria's [Roadmap](../open-metadata-publication/website/roadmap) for more details about the
Open Metadata and Governance vision, strategy and content.


# Further Help and Support

As part of the Linux AI & Data Foundation, our slack channels have moved to the [LF AI & Data Slack workspace](slack.lfaidata.foundation), and our mailing lists can now be found at https://lists.lfaidata.foundation/groups

Continue to use these resources, along with GitHub to report bugs or ask questions.

----
* Return to [Release Notes](.)
   
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

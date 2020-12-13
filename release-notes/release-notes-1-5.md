<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 1.5 (March 2020)

Release 1.5 delivers the automatic metadata discovery of duplicate assets.
Additional, in Tech Preview, Data Engine OMAS, and a data engine proxy server.

## Released Components

* The first governance server is released:
  * The [Discovery Server](../open-metadata-implementation/frameworks/open-discovery-framework/docs/discovery-server.md) supports the scanning of assets and the notification when duplicate suspects are detected.
  
  This server is supported by: 
   * The [Discovery Engine OMAS](../open-metadata-implementation/access-services/discovery-engine) supports the detection, recording and notification of exceptions and duplicate suspects.

   * The [Open Discovery Framework (ODF)](../open-metadata-implementation/frameworks/open-discovery-framework) is now defined and
     implemented to support the interfaces for automated discovery services.
     It complements the [Open Connector Framework (OCF)](../open-metadata-implementation/frameworks/open-connector-framework) delivered in release 1.0.

* There are new [tutorials](../open-metadata-resources/open-metadata-tutorials),
  [hands-on labs](../open-metadata-resources/open-metadata-labs) and
  [samples](../open-metadata-resources/open-metadata-samples) demonstrating
  the new de-duplication detection features.

## Technical Previews

   * The [Data Engine OMAS](../open-metadata-implementation/access-services/data-engine) supports the processing of notifications from data engines such as ETL platforms in order to catalog information about the data movement, transformation and copying they are engaged in.
   
   * The [Data Engine Proxy Server](../open-metadata-implementation/governance-servers/data-engine-proxy-services) is also included in the technical preview.
     It supports the polling of data engines such as ETL platforms
     in order to catalog information about the data movement, transformation and copying they are engaged in.
     It calls the Data Engine OMAS.

## Egeria Implementation Status at Release 1.5
 
![Egeria Implementation Status](../open-metadata-publication/website/roadmap/functional-organization-showing-implementation-status-for-1.5.png#pagewidth)
 
 Link to Egeria's [Roadmap](../open-metadata-publication/website/roadmap) for more details about the
 Open Metadata and Governance vision, strategy and content.
 
----
 * Return to [Release Notes](.)
         
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

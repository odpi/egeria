<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 1.7 (May 2020)

Release 1.7 contains many bug fixes & preparatory development work for future new features.

Below are the highlights:

* There is support for loading standard glossaries and design models coded in OWL/JSON-LD into
  the open metadata ecosystem.  The input file is converted to an
  [Open Metadata Archive](../open-metadata-resources/open-metadata-archives) which can be loaded directly
  into a metadata server.

* Many dependencies have been updated including:
    * Kafka client upgraded to 2.5
    * Spring security updated to 5.3.1, spring boot,data to 2.2.6, spring to 5.2.5
  
  For a full list refer to the git commit logs.

## Known Issues

* (https://github.com/odpi/egeria/issues/2935)[2935] - Governance Engine OMAS reports exception when entities added
* (https://github.com/odpi/egeria/issues/3005)[3005] - Occasional failure in 'Building a Data Catalog' notebook

## Egeria Implementation Status at Release 1.7
 
![Egeria Implementation Status](../open-metadata-publication/website/roadmap/functional-organization-showing-implementation-status-for-1.7.png#pagewidth)
 
 Link to Egeria's [Roadmap](../open-metadata-publication/website/roadmap) for more details about the
 Open Metadata and Governance vision, strategy and content.
 
----
 * Return to [Release Notes](.)
    
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),

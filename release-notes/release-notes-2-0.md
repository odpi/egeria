<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 2.0 (July 2020)

Release 2.0 adds support for:

- Encryption by default (HTTPS/SSL, encrypted configuration file)
- bug fixes
- dependency updates

Below are the highlights:

- The Egeria server chassis default URL is now https://localhost:9443 - the server now listens on port 9443 and supports https only. All clients have been updated accordingly. At this point SSL certificate validation is disabled.  This will be enabled in a future release.
- Docker containers, docker-compose scripts, kubernetes deployments have all been updated to use https accordingly.
- The [Encrypted Configuration File Store Connector](../open-metadata-implementation/adapters/open-connectors/configuration-store-connectors/configuration-encrypted-file-store-connector/README.md) is now used by default to ensure security of sensitive configuration details like credentials.

## Egeria Implementation Status at Release 2.0
 
 ![Egeria Implementation Status](../open-metadata-publication/website/roadmap/functional-organization-showing-implementation-status-for-2.0.png#pagewidth)
 
 Link to Egeria's [Roadmap](../open-metadata-publication/website/roadmap) for more details about the
 Open Metadata and Governance vision, strategy and content.
 
----
 * Return to [Release Notes](.)
    
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

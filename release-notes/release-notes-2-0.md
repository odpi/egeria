<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 2.0 (Planned June 2020)

Release 2.0 adds support for:

- Data Science and AI model governance
- Ethics governance
- Encryption by default (HTTPS/SSL, encrypted configuration file)

Below are the highlights:

- There is a new access service called the [Data Science OMAS](../open-metadata-implementation/access-services/data-science) provides support for capturing lineage around data science models.
- There are new [tutorials](../open-metadata-resources/open-metadata-tutorials),
  [hands-on labs](../open-metadata-resources/open-metadata-labs),
  [samples](../open-metadata-resources/open-metadata-samples) and
  [open metadata archives](../open-metadata-resources/open-metadata-archives) demonstrating
  these new capabilities of Egeria.
- The Egeria server chassis default URL is now https://localhost:9443 - the server now listens on port 9443 and supports https only. All clients have been updated accordingly. At this point SSL certificate validation is disabled. This will be enabled in a future release.
- Docker containers, docker-compose scripts, kubernetes deployments have all been updated to use https accordingly.
- The [Encrypted Configuration File Store Connector](../open-metadata-implementation/adapters/open-connectors/configuration-store-connectors/configuration-encrypted-file-store-connector/README.md) is now used by default to ensure security of sensitive configuration details like credentials.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

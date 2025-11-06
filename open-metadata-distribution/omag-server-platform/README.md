<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# OMAG Server Platform Distribution TAR file

The **OMAG Server Platform** module creates a distribution **tar** file that can be copied
and used to install Egeria's OMAG Server Platform.   For further information see
[Installing Egeria Tutorial](https://egeria-project.org/education/tutorials/installing-egeria-tutorial).

## Default configuration

When the platform is started, five servers are automatically initialized:

* *active-metadata-store* is a [metadata access store](https://egeria-project.org/concepts/metadata-access-store/) using the [in-memory repository](https://egeria-project.org/connectors/repository/in-memory/overview/).  It is activated with support for the [out-topic](https://egeria-project.org/concepts/out-topic/) which means it can send notification to other servers.  It also loads the [Core Content Pack](https://egeria-project.org/content-packs/core-content-pack/overview/).
* *simple-metadata-store* is also a [metadata access store](https://egeria-project.org/concepts/metadata-access-store/) using the [in-memory repository](https://egeria-project.org/connectors/repository/in-memory/overview/).  However, it has no configured event notification support.  It does load the [Simple Content Pack](https://egeria-project.org/content-packs/simple-content-pack/overview/).
* *integration-daemon* is an [integration daemon](https://egeria-project.org/concepts/integration-daemon/) that runs the [integration connectors](https://egeria-project.org/concepts/integration-connector/) configured in any of the following [integration groups](https://egeria-project.org/concepts/integration-group/):

    * *Egeria:IntegrationGroup:Default*
    * *Egeria:IntegrationGroup:ApacheAtlas*
    * *Egeria:IntegrationGroup:ApacheKafka*
    * *Egeria:IntegrationGroup:OpenAPIs*
    * *Egeria:IntegrationGroup:OpenMetadataObservability*
    * *Egeria:IntegrationGroup:PostgreSQL*
    * *Egeria:IntegrationGroup:UnityCatalog*
    * *Egeria:IntegrationGroup:OpenMetadataDigitalProducts*
  
* *engine-host* is an [engine host](https://egeria-project.org/concepts/engine-host/) that runs requests to [governance services](https://egeria-project.org/concepts/governance-service/) that are configured in any of the following [governance engines](https://egeria-project.org/concepts/governance-engine/):

    * *AssetOnboarding*
    * *Stewardship*
    * *FileSurvey*
    * *FileGovernance*
    * *UnityCatalogSurvey*
    * *UnityCatalogGovernance*
    * *PostgreSQLSurvey*
    * *PostgreSQLGovernance*
    * *ApacheAtlasSurvey*
    * *ApacheAtlasGovernance*
    * *EgeriaSurvey*
    * *EgeriaGovernance*
    * *ApacheKafkaSurvey*
    * *ApacheKafkaGovernance*
    * *EgeriaWatchdog*
    * *MetadataObservability*

* *view-server* is a [view server](https://egeria-project.org/concepts/view-server/) that supports the Open Metadata and Governance REST APIs that access/maintain the open metadata in the repositories and control the governance operations running in the engine host and integration daemon.

The capability of these servers is enabled/enhanced when the [content packs](https://egeria-project.org/content-packs/) for the [Egeria Solutions](https://egeria-project.org/egeria-solutions/) are loaded.

![Default Configuration](docs/default-egeria-config.svg)

In the default configuration, the *simple-metadata-store* is running but is not accessible via the Open Metadata and Governance REST APIs.  It can be recreated using the `BuildSampleConfigs.http` script.

The `ConnectCohort.http` script contains the administration commands to connect *active-metadata-store*
with *simple-metadata-store* via an [Open Metadata Repository Cohort](https://egeria-project.org/features/cohort-operation/overview/).
Once connected, metadata queries issued to *view-server* will return metadata from both the
*active-metadata-store* repository and the *simple-metadata-store* repository.

![Add Cohort to Default Configuration](docs/default-egeria-config-with-cohort.svg)

In addition, the [Getting started with Egeria](https://getting-started-with-egeria.pdr-associates.com/introduction.html) blogs show how to make use of this assembly.

## Tailoring the default distribution

The diagram below shows the file layout for the OMAG Server Platform runtime environment. The directories shown in white are built by this assembly.  The ones shown in blue are created during the platform operation.

![File layout for the omag-server-platform assembly](docs/fileLayout-omag-server-platform.png)

Each of the directories created by the assembly includes a `README.md` file that describes its content and how to use it.

You can add/remove files under the `assembly` directory and then run the `Dockerfile` script to create your own custom Docker contain for Egeria.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.



 
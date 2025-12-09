<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Default server configurations

This directory contains the server configurations for the five [OMAG Servers](https://egeria-project.org/concepts/omag-server/) that make up the default configuration:

* **simple-metadata-store** is a [Metadata Access Store](https://egeria-project.org/concepts/metadata-access-store/)
  that provides REST APIs for retrieving and maintaining open metadata.
  This server is set up to use a repository that keeps its metadata in memory.
  It loads the [Simple Catalog Content Pack](https://egeria-project.org/content-packs/simple-content-pack/overview/).
  This means that each time the server is restarted, it starts with just the content of the Simple Catalog Content Pack
  in its repository.  

The `simple-metadata-store` server is not configured to use Apache Kafka and so it does not produce events
when metadata is changed. The next set of servers make use of Apache Kafka to both send and receive events.
The Apache Kafka broker should be listening at `localhost:9092`.

* **active-metadata-store** is a [Metadata Access Store](https://egeria-project.org/concepts/metadata-access-store/)
  that supports both REST APIs for retrieving and maintaining open metadata along with
  event notifications each time there is change in the metadata.  It is also storing its
  metadata in memory.
  
  This server automatically loads the
  [Core Content Pack](https://egeria-project.org/content-packs/core-content-pack/overview/),
  [Egeria Content Pack](https://egeria-project.org/content-packs/egeria-content-pack/overview/),
  [Files Content Pack](https://egeria-project.org/content-packs/files-content-pack/overview/),
  [Open Lineage Content Pack](https://egeria-project.org/content-packs/open-lineage-content-pack/overview/),
  [PostgreSQL Content Pack](https://egeria-project.org/content-packs/postgres-content-pack/overview/),
  [Jacquard Harvester Content Pack](https://egeria-project.org/content-packs/jacquard-harvester-content-pack/overview/) and
  [Jacquard Harvester Content Pack](https://egeria-project.org/content-packs/organization-insight-content-pack/overview/).

* **integration-daemon** is an [Integration Daemon](https://egeria-project.org/concepts/integration-daemon/) that
  runs [Integration Connectors](https://egeria-project.org/concepts/integration-connectors/).
  These integration connectors are responsible for cataloguing metadata from external (third party) systems.
  The configuration of these integration connectors is found in the qs-metadata-store.
  For example, it has an integration connector that catalogs files stored on the filesystem.
  It is set up to catalog any file located in `sample-data/data-files`
  under the `platform` directory. It is also looking for additional configuration added to active-metadata-store
  under the **Egeria:IntegrationGroup:DefaultIntegrationGroup** 
  [integration group](https://egeria-project.org/concepts/integration-group/).

* **engine-host** is an [Engine Host](https://egeria-project.org/concepts/engine-host/) that is running the [governance engines](https://egeria-project.org/concepts/governance-engine/)
  used to create and manage metadata.  The configuration of these governance engines is found in the active-metadata-store.

The final server provides the services for Egeria's python capabilities built around pyegeria.

* **view-server** is a [View Server](https://egeria-project.org/concepts/view-server/) that calls the 
  active-metadata-store to send and retrieve metadata from its repository.  Its services are designed to
  support calls from non-Java environments such as python and javascript.
 Egeria's user interfaces make calls to the view server.

These server configurations can be (re)created using the `BuildSampleconfigs.http` script.

## Starting the servers

Ensure the OMAG Server Platform is running at `https://localhost:9443`.  The servers will automatically start when the platform starts.

You can edit the `application.properties` file in the `platform` directory and change the `startup.server.list` property to list the servers that should be automatically started when the platform is started:
```properties
# Comma separated names of servers to be started.  The server names should be unquoted.
startup.server.list=active-metadata-store,engine-host,integration-daemon,view-server
```
When the platform is restarted the servers start in the order listed.  
More information on the `application.properties` file can be found in the
[Configuring an OMAG Server Platform](https://egeria-project.org/guides/admin/configuring-the-omag-server-platform/) documentation.

## Loading some sample metadata

The `integration-daemon` server is set up to monitor files that are copied under
either the `landing-area` or `sample-data` directories under the platform directory.

For example, if you run the following command from this directory (ie `...platform/data/servers`)
```bash
cp -r ../../../opt/sample-data ../..
```
A set of data files is copied into the `sample-data` directory and will be automatically catalogued by Egeria.

## Loading the Coco Pharmaceuticals Metadata

Running the `AddCocoMetadata.http` script will add metadata from the Coco Pharmaceuticals scenarios.  This includes
activating the `ClinicalTrials@CocoPharmaceuticals` engine to the `engine-host` server.

## Connecting the metadata stores via a cohort

Running the `ConnectCohort.http` script connects `simple-metadata-store`, `active-metadata-store` and a new [Metadata Access Point](https://egeria-project.org/concepts/metadata-access-point/) server called `metadata-access-point` together in a 
cohort.  It rewires `view-server` to call `metadata-access-point` to show that it is possible to have a cohort member without a repository, since `view-server` can query the metadata in both `active-metadata-store` and  `simple-metadata-store` when you use the services of `view-server`.

(`view-server` is connected to `metadata-access-point`.  The cohort turns requests to `active-metadata-store` into
a federated query across both `active-metadata-store` and `simple-metadata-store`.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
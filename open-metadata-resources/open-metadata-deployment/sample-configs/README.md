<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Sample server configurations

This directory contains the server configurations for three [OMAG Servers](https://egeria-project.org/concepts/omag-server/):

* **simple-metadata-store** is a [Metadata Access Store](https://egeria-project.org/concepts/metadata-access-store/)
  that provides REST APIs for retrieving and maintaining open metadata.
  This server is set up to use a repository that keeps its metadata in memory.
  This means that each time the server is restarted, it starts with an empty repository.

The next two servers use Apache Kafka to send and receive events.
The Apache Kafka broker should be listening at `localhost:9092`.

* **active-metadata-store** is a [Metadata Access Store](https://egeria-project.org/concepts/metadata-access-store/)
  that supports both REST APIs for retrieving and maintaining open metadata along with
  event notifications each time there is change in the metadata.

* **integration-daemon** is an [Integration Daemon](https://egeria-project.org/concepts/integration-daemon/)
  that catalogs files stored on the filesystem.  It is set up to catalog any file located in `sample-data/data-files`
  under the `platform` directory.

To use these configurations, copy their directories under the `platform/data` directory. Then copy `opt/sample-data` to `platform/sample-data`.

```bash
mkdir ../../platform/data/servers
cp -r * ../../platform/data/servers
cp -r ../sample-data ../../platform
```
When the OMAG Server Platform is running at `https://localhost:9443`, issue the following curl command,
replacing `{{server}}` with the name of the server to start.  Message appear from the platform to indicate the
status of the server.

```bash
curl --location --request POST 'https://localhost:9443/open-metadata/platform-services/users/garygeeke/server-platform/servers/{{server}}/instance' \
--data ''
```




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
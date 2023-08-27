<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# The data directory

This directory is the default location where the platform writes its runtime files.

Files stored under the `platform` directory relate to the platform operation.
For example, the platform stores its encryption key for the
[Configuration Document Store](https://egeria-project.org/concepts/configuration-document-store-connector/)
under the `platform/keys` directory.

Files stored under the `servers` directory relate to the [OMAG Servers](https://egeria-project.org/concepts/omag-server/)
running on the platform.  There is a subdirectory for each server, which is where its
[configuration document](https://egeria-project.org/concepts/configuration-document/),
and optionally its repository (if its repository is file-based) and 
[cohort registry store](https://egeria-project.org/concepts/cohort-registry-store-connector/).



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
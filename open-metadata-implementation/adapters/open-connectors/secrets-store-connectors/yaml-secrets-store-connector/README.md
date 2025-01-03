<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# The YAML File Secrets Store Connector

The *YAML File Secrets Store Connector* retrieves secrets from a YAML file.
The structure of this store is described in the `SecretsStore` java class found in the `secretsstore` package.
The secrets store is organized into named collections.
Each collection represents the related secrets needed by a particular type of caller.

The location of the YAML File is configured in the endpoint of the connector's connection object.
The collection to use is supplied in the `secretsCollectionName` property found in the connection's `configurationProperties`.  
The connector will fail if either of these two values are missing.


## Full description

See [https://egeria-project.org/connectors/secrets/yaml-file-secrets-store-connector](https://egeria-project.org/connectors/secrets/yaml-file-secrets-store-connector)

----
* Return to [Secrets Store Connectors module](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
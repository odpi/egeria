<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# The Environment Variable Secrets Store Connector

The *YAML File Secrets Store Connector* retrieves secrets from environment variables.  
Each secret is found in an environment variable named after the secret name,
prefixed with its secret's collection  and "_".  For example, the userId secret for the secret's collection
"MY_CONNECTOR" is in an environment variable called MY_CONNECTOR_userId.

It returns null if the environment variable is not defined.

## Deployment and configuration

See [https://egeria-project.org/connectors/secrets/environment-variable-secrets-store-connector](https://egeria-project.org/connectors/secrets/environment-variable-secrets-store-connector)

----
* Return to [Secrets Store Connectors module](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
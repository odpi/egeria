<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Lineage Services Server REST Interface

The OLS REST services use a URL that begins

```

<serverRootURL>/servers/{serverName}/open-metadata/lineage-warehouse/users/{userId}
```

where:
* **serverName** is the name of the server
* **userId** is the userId of the person (or server) making the call

The specific REST API for OLS extends this basic URL

## get lineage for entity

* [get lineage](get-lineage-with-rest.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
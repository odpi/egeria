<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Configuring the basic properties of an UI server

The basic properties of the logical UI server are used in logging and events originating
from the server. They help to document the purpose of the server (which helps with problem determination)
and enable performance improvements by allowing the server to ignore activity or
metadata that is not relevant to its operation.

The basic properties are:

* **localServerId** - Unique identifier for this server. By default, this is
  initialized to a randomly generated Universal Unique identifier (UUID).

* **localServerName** - meaningful name for the server for use in messages and UIs. Ideally this value is
  unique to aid administrators in understanding the source of messages and events from the server.
  This value is set to the server name assigned when the configuration is created.

* **localServerType** - descriptive type name for the server.  Again this is useful information for the
  administrator to understand the role of the server. The default value is "Open Metadata and Governance Server".
 
* **organizationName** - descriptive name for the organization that owns the local server/repository.
  This is useful when the open metadata repository cluster consists of metadata servers from different
  organizations, or different departments of an enterprise.  The default value is null.

* **localServerURL** - network address of the UI server platform where this server runs
  (typically host and port number but must also include the initial part of the URL before "open-metadata" if defined).
  The default value is "https://localhost:8443".
 
* **localServerUserId** - UserId to use for server initiated REST calls. The default is "UIServer".

* **maxPageSize** - the maximum page size that can be set on requests to the server. The default value is 1000.

The sections that follow cover how to set up these values.

## Set server type name

The server type name should be set to something that describes the logical UI
Server's role.
It may be a department name, when serving UIs for a particular department. 

```
POST https://localhost:8443/open-metadata/ui-admin-services/users/garygeeke/servers/cocoUIS1/server-type?typeName="CDO Office UI"
```

## Set organization name

The organization name may be the owning organization or department or
team supported by the server.
Here the organization name is set to "Clinical Trials".

```
POST https://localhost:8443/open-metadata/ui-admin-services/users/garygeeke/servers/cocoUIS1/organization-name?name="Clinical Trials UI"
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
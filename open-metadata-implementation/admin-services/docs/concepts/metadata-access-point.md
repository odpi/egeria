<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Metadata Access Point

A **Metadata Access Point** is an OMAG Server that can be a
[member of an open metadata repository cohort](cohort-member.md)
and supports the [access services](../../../access-services).

![Figure 1](metadata-access-point.png#pagewidth)
> Figure 1: Metadata Access Point in OMAG server ecosystem

This means it provides specialist metadata
APIs to user interfaces and governance servers that embrace metadata from
all connected open metadata repository cohorts.

The basic metadata access point has no metadata repository and metadata
is retrieved and stored from remote repositories via the [cohort](cohort-member.md).
It can be upgraded to a [Metadata Server](metadata-server.md)
by adding a metadata repository which will enable it to
store metadata locally.


## Configuring a Metadata Access Point

Each [type of OMAG Server](omag-server.md) is configured by creating
a [configuration document](configuration-document.md).  The contents
of the configuration document identify the type of server and
the options on the services it runs.

Figure 2 shows the structure of the configuration document for the metadata
access point.

![Figure 2](metadata-access-point-config.png#pagewidth)
> Figure 2: Configuration Document for a Metadata Access Point

The tasks for configuring an metadata access point are as follows:

* [Setting up the default event bus](../user/configuring-event-bus.md)
* [Configuring the default local server URL root](../user/configuring-local-server-url.md)
* [Setting basic properties for an OMAG server](../user/configuring-omag-server-basic-properties.md)
* [Configuring the audit log destinations](../user/configuring-the-audit-log.md)
* [Configuring the server security connector](../user/configuring-the-server-security-connector.md)
* [Configuring registration to a cohort](../user/configuring-registration-to-a-cohort.md)
* [Configuring the Open Metadata Access Services (OMASs)](../user/configuring-the-access-services.md)


----
Return to [Cohort Members](cohort-member.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
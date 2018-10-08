<!-- SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
  
# Open Metadata Adapters

The adapters provide components that either:

* Plug into the open metadata implementation to support one of its functions, or 
* Plug open metadata capability into another technology.

**[open-connectors](open-connectors)** are connectors that support the 
Open Connector Framework (OCF) - see the [open-connector-framework](../frameworks/open-connector-framework/README.md) module.
This is the principle (preferred) mechanism for supporting pluggable
components in the open metadata implementation.  The OCF has additional uses
in providing reusable/pluggable components for external technology - particularly
related to data access.

**[authentication-plugins](authentication-plugins)** support extensions to technology such as LDAP that
are used to verify the identity of an individual or service requesting
access to data/metadata.

**[governance-engines-plugins](governance-engines-plugins)** support plugins to governance engines
to enable them to use open metadata settings in their validation
and enforcement decisions and the resulting actions they take.
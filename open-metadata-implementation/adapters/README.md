<!-- SPDX-License-Identifier: Apache-2.0 -->
  
# Open Metadata Adapters

The adapters provide components that either:

* Plug into the open metadata implementation to support one of its functions or 
* Plug open metadata capability into another technology.

**Open Connectors** are connectors that support the 
Open Connector Framework (OCF) - see the open-connector-framework module.
This is the principle (preferred) mechanism for supporting pluggable
components in the open metadata implementation.  The OCF has additional uses
in providing reusable/pluggable components for external technology - particularly
related to data access.

**Authentication Plugins** support extensions to technology such as LDAP that
are used to verify the identity of an individual or service requesting
access to data/metadata.

**Governance Engines Plugins** support plugins to governance engines
to enable them to use open metadata settings in their validation
and enforcement decisions they make and the resulting actions they take.
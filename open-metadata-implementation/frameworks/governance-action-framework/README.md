<!-- SPDX-License-Identifier: Apache-2.0 -->
  
# Governance Action Framework (GAF)
  
The Governance Action Framework (GAF) provides the interfaces
and base implementations for components (called governance actions) that
take action to correct a situation that is harmful to the data
or the organization in some way.

Most governance actions execute in external engines that are working with data
and related assets.
The GAF offers embeddable functions and APIs to simplify the implementation of
the governance actions, whilst being resilient and performant.

Governance actions themselves produce audit log records and exceptions.
The Governance Action Framework supports the development of stewardship services 
to analyze the audit log records and process the exceptions.

These stewardship services are built using the Open Metadata and
Governance Stewardship Toolkit and they run in the Open Metadata and
Governance Stewardship Server.

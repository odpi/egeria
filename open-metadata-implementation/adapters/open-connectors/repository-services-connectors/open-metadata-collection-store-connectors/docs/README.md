<!-- SPDX-License-Identifier: Apache-2.0 -->

# Open Metadata Collection Store Connectors Documentation

The open metadata collection store connectors are used to
integrate an existing metadata repository into the the open
metadata ecosystem.  There are two types of connectors.

* The **repository connector** provides
a standard repository interface that wraps the proprietary
interface of the metadata repository.
* The **event mapper connector** captures
events that provide notifications that metadata has changed
in the metadata repository and passes them to the
[Open Metadata Repository Services (OMRS)](../../../../../repository-services/docs/README.md).

The event mapper connector often calls repository connector
to expand out the information needed by the OMRS.
The links below provide more information.

* **[Overview of the Repository Connector Interface](overview-of-the-repository-connector-interface.md)** -
a walk-through of the methods on the repository connector interface.  This connector enables
the Open Metadata Repository Services (OMRS) the ability to search, query, create, update and delete
metadata in an existing metadata repository.

* **[Overview of the Event Mapper Connector Interface](overview-of-the-event-mapper-connector-interface.md)** -
a walk-through of the methods on the event mapper connector interface.  This connector enables
events from an existing metadata repository that indicate that metadata is changing, to be passed
to the OMRS so it can be distributed to the other metadata repositories who are members of the same cohort.
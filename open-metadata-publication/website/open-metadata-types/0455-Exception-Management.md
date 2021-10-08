<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0455 Exception Management

Governance is supported by a number of operational logs.  Each operational log is a collection of log records.
In many cases each log record is stored as an individual file for ease of management.
It is not uncommon that the volume of log records is high and the value of the information in
each log record is variable.
As a result individual log records are not catalogued unless they are significant in some way.
(For example, it may be that the log record needs to be kept for regulatory purposes,
or there is stewardship action required.)
As such, the log records are written to storage as the processing that they describe proceeds.
Asynchronous processes then scan and review the content of the log records to ensure the processing is as expected.
They may also purge the log records that are no longer needed.

This log record processing may maintain various counts and other measurements that can be captured in the metadata
or in an external data store.  The **LogAnalysis** classification enables the capture of these measurements
in metadata.  For example,
log records that represent significant events may be catalogued as assets and
the **LogAnalysis** classification attached to the asset.
The results of the log analysis across many record may be rolled up into a
[GovernanceMeasurement](0450-Governance-Rollout.md) attached to the appropriate
[GovernanceDefinition](0401-Governance-Definitions.md).

The types of operational logs and associated processing are:

* Metering captures the usage of resources.  This may be to enforce usage quotas, or for billing.
  A collection of related metering record is represented as a **MeteringLog**.  If it is implemented using files
  then each file can be classified with **MeteringLogFile**.
  
* Operational lineage logging captures the execution path and results of processes.  It is used
  to prove that processes ran, and ran at the right time, processing the
  right quantity of data at appropriate quality.
  A collection of related lineage log records is represented as a **LineageLog**.
  If the lineage log is implemented using files, each file can be classified with **LineageLogFile**.

* Audit logging records significant events.  This may be successful processing events or detected errors that need action.
  A collection of related audit log records is represented as a **AuditLog**.
  If the audit log is implemented using files, each file can be classified with **AuditLogFile**.
  
* Exception management handles errors detected by [verification points](0460-Governance-Execution-Points.md).
  Exception records are managed in one or more **ExceptionBacklog** collections.
  The exception backlog may be implemented as a series of files classified as **ExceptionLogFile**.


![UML](0455-Exception-Management.png#pagewidth)


The [software servers](0040-Software-Servers.md) that are managing operational governance can be
classified with the following:

* **StewardshipServer** describes a server that is handling the stewardship processes.
* **GovernanceDaemon** describes a background server that is hosing automated governance processing.

---

* Return to [Area 4](Area-4-models.md).
* Return to [Overview](.).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
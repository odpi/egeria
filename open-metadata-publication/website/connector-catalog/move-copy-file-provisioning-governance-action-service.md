<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Move/Copy File Provisioning Governance Action Service

* Connector Category: [Provisioning Governance Action Service](../../../open-metadata-implementation/frameworks/governance-action-framework/docs/provisioning-governance-service.md)
* Hosting Service: [Governance Action OMES](../../../open-metadata-implementation/engine-services/governance-action)
* Hosting Server: [Engine Host](../../../open-metadata-implementation/admin-services/docs/concepts/engine-host.md)
* Source Module: [governance-action-connectors](../../../open-metadata-implementation/adapters/open-connectors/governance-action-connectors)
* Jar File Name: governance-action-connectors.jar
* ConnectorProviderClassName: org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning.MoveCopyFileGovernanceActionProvider


**Move/Copy File** Provisioning Governance Action Service moves or copies files from one location to another and
optionally creates lineage between them.

This governance action service is highly configurable.
It can be set up to perform a move file or a copy file action,
the destination file name may be adapted from the source file name using a file pattern and
the lineage may be turned on or off.


![Figure 1](move-copy-file-provisioning-governance-action-service.png#pagewidth)
> **Figure 1:** Operation of the move/copy file provisioning governance action service

If lineage is requested, it includes adding a new [DataFile](../open-metadata-types/0220-Files-and-Folders.md)
asset for the new file in the destination folder and creating [LineageMapping](../open-metadata-types/0770-Lineage-Mapping.md)
relationships between the process that represents this governance action service and both the
asset for the original source file and as well as the destination.
There are four choices for lineage:

Figure 2 shows the default lineage from this service.
It allows exact traceability between source file and destination file.

![Figure 2](move-copy-file-provisioning-governance-action-service-lineage-1.png#pagewidth)
> **Figure 2:** Exact traceability between source and destination files

Figure 3 shows lineage where there is no need to track the
the lineage between individual files, because for example, the source files
always come from the same source and all go to the same destination. 

![Figure 3](move-copy-file-provisioning-governance-action-service-lineage-2.png#pagewidth)
> **Figure 3:** Traceability between the collection of source files and the collection of destination files

Figure 4 links the source files to the instance of this governance action process
but not to the individual destination files.

![Figure 4](move-copy-file-provisioning-governance-action-service-lineage-3.png#pagewidth)
> **Figure 4:** Traceability between the collection of source files and the provisioning instance and the destination folder

Figure 5 shows the traceability between the source files and the governance action process
and the destination folder.  It is not possible to tell which instance of the
governance action service processes each source file.
        
![Figure 5](move-copy-file-provisioning-governance-action-service-lineage-4.png#pagewidth)
> **Figure 5:** Traceability between the collection of source files and the destination folder

Figure 6 shows the most abbreviated lineage that shows the linkage from the source folder
to the governance action process to the destination folder.  There is no traceability at the file level.
This pattern is appropriate when the files themselves are very numerous, they all follow the same
processing path and the folders represent the data sets.

![Figure 6](move-copy-file-provisioning-governance-action-service-lineage-5.png#pagewidth)
> **Figure 6:** Traceability between the source files' folder and the destination folder

Irrespective of the pattern used, the resulting changes to the metadata can lead to the triggering of additional activity
in the open metadata ecosystem.  For example:

* The [Asset Lineage OMAS](../../../open-metadata-implementation/access-services/asset-lineage)
may publish the lineage to the [Open Lineage Server](../../../open-metadata-implementation/admin-services/docs/concepts/open-lineage-server.md).

* Additional [Governance Action Services](../../../open-metadata-implementation/frameworks/governance-action-framework/docs/governance-action-service.md)
may be triggered.

## Configuration

This connector uses the [Governance Action OMES](../../../open-metadata-implementation/engine-services/governance-action)
running in the [Engine Host](../../../open-metadata-implementation/admin-services/docs/concepts/engine-host.md).

The following configuration properties can be set to control
the behavior of the service on every instance.

* **provisionUncataloguedFiles** - if this property is set to any value, the service may work with files that are not catalogued
  in open metadata.  This is used when processing files that are entering the open metadata ecosystem.
 
* **targetFileNamePattern** - pattern to construct the name of the destination file. If this is not set, the source file name is used.
  
* **noLineage** - if this property is set to any value, do not produce lineage as part of the provisioning process.

* **processName** - if this property is set, it overrides the default process name (ie the name of this
  governance action service) with the value that this property is set to.
  
* **lineageWithTopLevelProcessOnly** - if this property is set, lineage mappings are connected to the top level process representing
  this 

* **lineageToDestinationFolderOnly** - if this property is set, the lineage relationship from the
  governance action service to the destination is linked to the destination folder rather than the new file in the destination folder.
  Without this value, the default behavior is to show lineage from governance action process to file.
  
* **lineageFromSourceFolderOnly** - if this property is set, the lineage relationship from the source to the
  governance action service is linked from the source folder rather than the source file.
  Without this value, the default behavior is to show lineage from source file to governance action process.  

This is its connection definition to use when
creating the definition of the governance action service
using the [Governance Engine OMAS](../../../open-metadata-implementation/access-services/governance-engine).
Remove the configuration properties that are not required.
Replace `{processName}` and `{pattern}` as required. 


```json
{
   "connection" : { 
                      "class" : "Connection",
                      "connectorType" : 
                      {
                           "class" : "ConnectorType",
                           "connectorProviderClassName" : "org.odpi.openmetadata.adapters.connectors.governanceactions.provisioning.MoveCopyFileGovernanceActionProvider"           
                      },
                      "configurationProperties": 
                      {
                              "provisionUncataloguedFiles": "",
                              "targetFileNamePattern": "{pattern}",
                              "noLineage": "",
                              "processName": "{processName}",
                              "lineageWithTopLevelProcessOnly": "",
                              "lineageToDestinationFolderOnly": "",
                              "lineageFromSourceFolderOnly": ""
                      }
                  }
}

```

## Governance Action Settings

When this governance action service is called through a [GovernanceAction](../open-metadata-types/0463-Governance-Actions.md)
it supports the following options.

### Request Types and Parameters

There are two request types:

* **copy-file** to request a file copy
* **move-file** to request a file move

The source and destination can be configured as request parameters:

* **source-file**
* **destination-folder**

Which can be overridden by the following named action targets

* **source-file**
* **destination-folder**

### Completion Status and Guards

On completion, this governance action service uses:

* `CompletionStatus.ACTIONED` with guard `provisioning-complete` or
* `CompletionStatus.FAILED` with guard `provisioning-failed`


## Examples of use

* [Open Metadata Labs](../../../open-metadata-resources/open-metadata-labs): this connector is configured
in the `governDL01` engine host server as part of the **automated curation** asset management lab.

## Related Information

* [Designing a Provisioning Governance Action Service](../../../open-metadata-implementation/frameworks/governance-action-framework/docs/provisioning-governance-service.md)


----
* Return to the [Connector Catalog](.)
* Return to the [Governance Action Connectors Overview](../../../open-metadata-implementation/adapters/open-connectors/governance-action-connectors)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Connectors to digital resources and their open metadata

These connectors are for use by external applications and tools to connect with resources 
and services in the digital landscape.  These connectors also supply the Asset metadata from Egeria
that describes these resources.

Instances of these connectors are created through the 
[Asset Consumer OMAS](../../../open-metadata-implementation/access-services/asset-consumer) or
[Asset Owner OMAS](../../../open-metadata-implementation/access-services/asset-owner) interfaces.
They use the Connection linked to the corresponding Asset in the open metadata ecosystem.
Connection objects are associated with assets in the
metadata catalog using the Asset Owner OMAS,
[Data Manager OMAS](../../../open-metadata-implementation/access-services/data-manager) and
[Asset Manager OMAS](../../../open-metadata-implementation/access-services/asset-manager).


## Files

* The [Avro file connector](../../../open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/avro-file-connector) 
  provides access to an Avro file that has been catalogued using open metadata.
  
* The [basic file connector](../../../open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/basic-file-connector) 
  provides support to read and write to a file using the Java File object.  

* The [CSV file connector](../../../open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/csv-file-connector) 
  is able to retrieve data from a Comma Separated Values (CSV) file where the contents are stored in logical columns
  with a special character delimiter between the columns.
  
* The [data folder connector](../../../open-metadata-implementation/adapters/open-connectors/data-store-connectors/file-connectors/data-folder-connector)
  is for accessing data that is
  stored as a number of files within a folder (directory).
  
## Databases

*More coming ...*


## Further information

* [Link to the Asset Consumer OMAS User Guide](../../../open-metadata-implementation/access-services/asset-consumer/docs/user) for information on using .
* [Link to the Open Metadata Repository Services](../../../open-metadata-implementation/repository-services/docs) for information on how the different store connectors are used.
* [Link to the Administration Guide](../../../open-metadata-implementation/admin-services/docs/user) for information on how to configure connectors into Egeria's runtime.


----

* [Return to the Connector Catalog](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
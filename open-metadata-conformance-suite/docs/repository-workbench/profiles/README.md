<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Repository conformance profiles

The functions expected of an open metadata are numerous.
These functions are broken down into the profiles listed below.
An open metadata repository needs to support at least one profile to be conformant.  However,
in practice, metadata sharing is required in order to support any of the other profiles so it is
effectively mandatory.

* **[Metadata sharing](metadata-sharing)** -                                                                                                            
The technology under test is able to share metadata with other members of the cohort.
* **[Reference copies](reference-copies)** -                                                                                                             
The technology under test is able to store reference copies of metadata from other members of the cohort.                 
* **[Metadata maintenance](metadata-maintenance)** -                                                                                                         
The technology under test supports requests to create, update and purge metadata instances.
* **[Dynamic types](dynamic-types)** -                                                                                                                
The technology under test supports changes to the list of its supported types while it is running.
* **[Historical search](historical-search)** -                                                                                                            
The technology under test supports search for the state of the metadata instances at a specific time in the past.
* **[Entity proxies](entity-proxies)** -                                                                                                               
The technology under test is able to store stubs for entities to use on relationships when the full entity is not available.
* **[Soft-delete and restore](soft-delete-restore)** -                                                           
The technology under test allows an instance to be soft-deleted and restored.
* **[Undo an update](undo-update)** -                                                                                                      
The technology under test is able to restore an instance to its previous version (although the version number is updated).
* **[Reidentify instance](reidentify-instance)** -                                                                                                    
The technology under test supports the command to change the unique identifier (guid) of a metadata instance.
* **[Retype instance](retype-instance)** -                                                                                        
The technology under test supports the command to change the type of a metadata instance to either its super type or a subtype.
* **[Rehome instance](rehome-instance)** -                                                                                                             
The technology under test supports the command to update the metadata collection id for a metadata instance.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
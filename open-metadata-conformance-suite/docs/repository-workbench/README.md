<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

  
# Open Metadata Conformance Repository Workbench

The open metadata conformance repository workbench is responsible for testing the ability of 
an open metadata repository to connect and interact with other open metadata repositories
in a conformant way.

It tests both the repository's repository services API and its ability to exchange events
with the [OMRS Cohort Event Topic](../../../open-metadata-implementation/repository-services/docs/omrs-event-topic.md).

The workbench uses the registration information that is passed when the technology
under test registers with the same
[open metadata repository cohort](../../../open-metadata-implementation/repository-services/docs/open-metadata-repository-cohort.md)
as the conformance suite.
It will confirm that the information received in the events matches the information returned
by the technology under test's repository services.

This workbench works as a pipeline processor, accumulating information from
one test and using it to seed subsequent tests.  A failure early on in the
pipeline may prevent other tests from running.

In addition this workbench dynamically generates tests based on the types returned
by the repository.  So for example,
the [Repository TypeDef Test Case](test-cases/repository-typedef-test-case.md)
runs for each TypeDef returned by the repository.   Again, a failure in the early set up
test cases will prevent the repository workbench from generating the full suite of
test cases for the repository under test.

## Repository conformance profiles

The functions expected of an open metadata are numerous.
These functions are broken down into the profiles listed below.
An open metadata repository needs to support at least one profile to be conformant.  However,
in practice, metadata sharing is required in order to support any of the other profiles so it is
effectively mandatory.

* **[Metadata sharing](profiles/metadata-sharing)** -                                                                                                            
The technology under test is able to share metadata with other members of the cohort.
* **[Reference copies](profiles/reference-copies)** -                                                                                                             
The technology under test is able to store reference copies of metadata from other members of the cohort.                 
* **[Metadata maintenance](profiles/metadata-maintenance)** -                                                                                                         
The technology under test supports requests to create, update and purge metadata instances.
* **[Effectivity dating](profiles/effectivity-dating)** -
The technology under test supports effectivity dating properties.
* **[Dynamic types](profiles/dynamic-types)** -                                                                                                                
The technology under test supports changes to the list of its supported types while it is running.
* **[Historical search](profiles/historical-search)** -                                                                                                            
The technology under test supports search for the state of the metadata instances at a specific time in the past.
* **[Entity proxies](profiles/entity-proxies)** -                                                                                                               
The technology under test is able to store stubs for entities to use on relationships when the full entity is not available.
* **[Soft-delete and restore](profiles/soft-delete-restore)** -                                                           
The technology under test allows an instance to be soft-deleted and restored.
* **[Undo an update](profiles/undo-update)** -                                                                                                      
The technology under test is able to restore an instance to its previous version (although the version number is updated).
* **[Reidentify instance](profiles/reidentify-instance)** -                                                                                                    
The technology under test supports the command to change the unique identifier (guid) of a metadata instance.
* **[Retype instance](profiles/retype-instance)** -                                                                                        
The technology under test supports the command to change the type of a metadata instance to either its super type or a subtype.
* **[Rehome instance](profiles/rehome-instance)** -                                                                                                             
The technology under test supports the command to update the metadata collection id for a metadata instance.

## Test cases

The following test case implementations support the validation of the profiles.  

The workbench dynamically
creates instances of the test cases as it explores the behavior of the technology under test.

Each test case typically focuses on a specific
requirement within a profile.  However, it may verify other requirements from either the same of different profiles
if it is efficient to do so.

When a test case encounters errors, it will log them and if possible it will continue testing.  However,
some failures are blocking and the test case will end when one of these is encountered.

* **[Repository AttributeTypeDef Test Case](test-cases/repository-attribute-typedef-test-case.md)**
* **[Repository Classification Lifecycle Test Case](test-cases/repository-classification-lifecycle-test-case.md)**
* **[Repository Entity Lifecycle Test Case](test-cases/repository-entity-lifecycle-test-case.md)**
* **[Repository Find AttributeTypeDefs by Category Test Case](test-cases/repository-find-attribute-typedefs-by-category-test-case.md)**
* **[Repository Find TypeDefs by Category Test Case](test-cases/repository-find-typedefs-by-category-test-case.md)**
* **[Repository Find Types By External Standard Identifiers Test Case](test-cases/repository-find-types-by-external-standard-identifiers-test-case.md)**
* **[Repository Get TypeDef Gallery Test Case](test-cases/repository-get-typedef-gallery-test-case.md)**
* **[Repository Metadata Collection Id Test Case](test-cases/repository-metadata-collection-id-test-case.md)**
* **[Repository Relationship Lifecycle Test Case](test-cases/repository-relationship-lifecycle-test-case.md)**
* **[Repository Server Ids Test Case](test-cases/repository-server-ids-test-case.md)**
* **[Repository TypeDef Test Case](test-cases/repository-typedef-test-case.md)**


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.


  
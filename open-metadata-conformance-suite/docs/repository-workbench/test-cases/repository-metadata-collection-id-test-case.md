<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Repository metadata collection id test case

Validate the retrieval of the metadata collection id from the open metadata repository.

## Operation

This test uses the `../open-metadata/repository-services/metadata-collection-id`
operation to test that the repository knows its metadata collection id.

## Assertions

* **repository-metadata-collection-id-01** Metadata collection id retrieved from cohort registration.

   The metadata collection id has successfully been retrieved from the cohort registration event.
   If this assertion fails then check that the technology under test is correctly registering with
   the cohort.
   
* **repository-metadata-collection-id-02** Metadata collection id retrieved from local repository connector.

   The metadata collection id has successfully been retrieved from the repository connector
   created from the cohort registration event.
   If this assertion fails then check that the technology under test is correctly configured to
   pass the correct connection information on the cohort registration event.  If the technology
   under test is running in the OMAG Server Platform then the localRepositoryRemoteConnection value
   needs to be corrected.
   
* **repository-metadata-collection-id-03** Metadata collection id retrieved from remote repository.

   The metadata collection id has successfully been retrieved from the remote repository by calling the
   getMetadataCollectionId() method on the metadataCollection object retrieved from the repository connector
   created from the cohort registration event.
   
   
   If this assertion fails then check that the technology under test is correctly configured to
   pass the correct connection information on the cohort registration event.  If the technology
   under test is running in the OMAG Server Platform then the localRepositoryRemoteConnection value
   needs to be corrected.   

* **repository-metadata-collection-id-02** Consistent metadata collection id retrieved from repository.

    The same metadata collection id was retrieved when the server was called
for a second time.

private static final  String assertion1    = testCaseId + "-01";
    private static final  String assertionMsg1 = "Metadata collection id retrieved from cohort registration.";
    private static final  String assertion2    = testCaseId + "-02";
    private static final  String assertionMsg2 = "Metadata collection id retrieved from local repository connector.";
    private static final  String assertion3    = testCaseId + "-03";
    private static final  String assertionMsg3 = "Metadata collection id retrieved from remote repository.";
    private static final  String assertion4    = testCaseId + "-04";
    private static final  String assertionMsg4 = "Metadata collection id retrieved from local repository connector matches registration.";
    private static final  String assertion5    = testCaseId + "-05";
    private static final  String assertionMsg5 = "Metadata collection id retrieved from remote repository matches registration.";

## Discovered properties

* **metadata collection id** - Unique identifier of the repository.  
* **metadata collection name** - Name of the repository.  

## Example output

```json
{
      "class" : "OpenMetadataTestCaseResult",
      "testCaseId" : "repository-metadata-collection",
      "testCaseName" : "Repository metadata collection id test case",
      "testCaseDescriptionURL" : "https://egeria-project.org/guides/cts/origin-workbench/repository-metadata-collection-id-test-case.md",
      "assertionMessage" : "Metadata collection id working consistently",
      "successfulAssertions" : [ "Metadata collection id retrieved from repository.", "Consistent metadata collection id retrieved from repository." ],
      "unsuccessfulAssertions" : [ ],
      "discoveredProperties" : {
        "metadata collection id" : "8b2aae04-c68c-47f8-a040-3ed12b82a50e"
      }
}
```



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
# Egeria Meeting 28th-30th August 2019 

### Attendees
* Ferd Scheepers
* Mandy Chessell 
* David Radley 
* Nigel Jones 
* Maryna Strelchuk 
* Daniela Otelea
* Mike Nicpan
* Bogdan-Mihail Sava
* Liviu-Constantin Popa
* Ruxandra-Gabriela Rosu
* Guy Ackermans
* Cong Chen
* Raluca Popa
* Moya Brannan 

### Agenda
## 28th August 2019
### Day 1: Deep Dive of Components and Technical Updates
#### Morning Session: 
* ING production target for Egeria lineage use case
* Automated Deployment & Configuration for Egeria:
  * We have went through the current process from Nigel about how can we do container deployment. 
  * Nigel has also showed a demo of building and deploying containers from Docker Hub to IBM Kubernetes clusters.
  * Action Point: Cong will contact the OpenShift Platform team in ING and talk to Nigel about what is the process in ING and how can we add new features from open source side.
* OMRS find method syntax
  * We have went through several regular expression examples as well as methods in OMRSRepositoryHelper.java to understand the syntax. Such as method: String getExactMatchRegex(String s). 
  * It is important for all the OMAS owners to use it to do an exact search, so that repositories that only supported a limited set of regular expressions can still service such requests.
#### Afternoon Session: 
* Glossary View OMAS Demo and Updates
  * Suggested that the https junit be moved to an FVT (maybe with subject area omas to set it up).
  * Nigel and David to work together to get the subject area FVT into a Helm chart. 
  * List Glossary view OMAS in the docs list of OMASes. Confirm that the list of docs have been updated for Glossary view as per the writing the OMAS document. 
* Information View OMAS
  * DeployedSoftwareComponent is a Ranger rule, should reflect the state of the rule in Ranger. Poll for changes in state of the rule. 
  * Using Apache SuperSet an open source BI tool for the next use case of IV OMAS. 
  * Initial load: when we bring in a new repo, metadata is replicated on query. For example: how do we know how to configure Gaian tables. Events for every table in the new repo would swamp the OMAS, suggest that an admin would know what data is relevant, and issue the appropriate query to bring it in. 
* UI Integration with Asset Consumer & Asset Owner
  * Asset consumer - has APIs to get connection from asset and various get apis to get connection and connector information. 
  * IT infrastructure OMAS needs to have defined the connection . 
  * Remove the connection definitions from the UI application.properties file when IT Infrastructure OMAS is ready. 
  * The UI needs to have the appropriate connector class in its classpath. 
  * Currently ING has one asset which is one information view (which is a deployedschema) with many tables. Mandy suggested that the LTs are being treated as separate assets but not defined as such in Egeria. Bogdan and Mandy agreed that more assets would work well to represent the LTs. One Oracle table in 5 schemas - to be exposed in GDB with 5 schemas. There maybe schemas with the same name brought into Gaian from 2 databases. 
  * Bogdan suggested that new notification (IT Infrastructure) when there is a new connection type, so there is an action to bring in the required drivers to the UI (not automatically). 
* Data Platform OMAS
  * Use case of DP omas on NoSql Database types: key value, document, column groups and graph 
  * Refactoring of Cassandra connector to governance-daemon-connectors.
  * Demo of bringing in Cassandra keystores into Egeria. 
  * Next step of how can we productionize it in ING by combining ING's current Cassandra Evidence Store application.
* Governance and Security
  * Triggering of classification, reclassification and declassification events. Need to consider other events including the effect of an asset changing of zones. 
  * Changing to the schema structure picked up when it sees the relationship created when the column semantically assigned to a term. 
  * Talked of allowing a classification on a reference copy if the owning repository does not support that classification. 
  * Open questions including 
    * Governance Classification Status enum still required for classification definitions 
    * Mandy talked of using a trash zone to put entities after retention expires. 
    * Need an event in stewardship when effectively ends or starts. 
    * Decided not to account for different confidentiality levels on glossary term synonyms. Subject Area calls should expose APIs to be able to report on these 'messy' glossaries. 
    * How much of area 0 should individual OMASes build out (should  security OMAS create : software server  as well as endpoints). Ideally IT infrastructure OMAS defines this * but OMASes may need to create area 0 content if they do not exist 
    * When do we sync governance rules. Need to draw use cases to draw out the friction points when using multiple rangers. Talked of having the Asset representing Ranger itself  in a zone * to help define it's scope. 
    * Governance types mapping. Who stitches deployed software components (deployed ranger rule) to the Governance Rule. 

## 29th August 2019
### Day 2: Architecture Discussion
#### Morning Session: 
* Open Metadata types SchemaType and SchemaAttribute optimisation
  * With the optimisation of two entities it will reduce the cost of schema processing as well as the complexity for understanding it.
  * Agreed that Mandy will model the types to fold in the schemaTypes into the schema attributes. 
* Lineage Group Update for ING Use Case
  * Virtual assets are unresolved entities in IGC representing unresolved tables from data stage templates. IGC stitches the templates to form design lineage. Need to get these virtual assets via the data stage proxy. Mandy suggested that we look to store these in Egeria - an asset place holder. 
  * Decided data lineage should not show the unresolved entities (process template stored in Egeria)
  * Once the entity is resolved then the data lineage should show it. This is likely to mean lineage server gets an event then queries Egeria to flesh out the entities and relationships. 
  * When a Data Stage job is created, it is then defined in IGC, the data stage proxy polls for changes in the jobs, then it needs to tell Egeria to create design lineage. 
  * We talked of how we stitch the lineage between processes. The thinking was that we would not have a process to process relationship; we should be able to infer this relationship from the output port from process 1 to a data store then to an input port to process 2. 
  * The exception where process-to-process mappings do make sense to capture is at a granular (sub-process) level, when processing is occurring in-memory.
  * For Data Stage proxy, it will send a process template event for job templates rather than a process event. 
  * For alias case then there is an optional bind to assets. If the job overrides this then a new process is created.  
* ING looking to define zones for IGC:
  * In general, we decided that it would make sense to represent zones through a new classification rather than as a list property directly on an entity. 
  * This change will allow us to master the zones in a repository other than the entity's home repository -- important when the home repository may not be functionally able to manage zones.
  * For IGC that is one zone this could be part of the igc proxy config 
  * For IGC with shared zones, have a new zone classification that could be set on a reference by the stewardship server. 

#### Afternoon Session: 
* Stewardship Server
  * We have went through how discovery engines will create service request of different annotations to discovery engine OMAS.
  * ActionTriage as a role / steward chooses whether and when to run a request for action. 
* Open Metadata types: Versioning and Patch Mechanism(https://github.com/odpi/egeria/issues/1367)
  * We have agreed:
    * We stop directly changing existing types after the significant change of optimization between schemaAttribute and schemaType in the OpenMetadataTypes archive.
    * Any changes to types should be added via a TypeDef patch. This patch is left open for a release. It can be changed but the version number should be changed for each change that goes into master.
    * The version name should be updated in each patch using the follwoing rules - update major number if attribute deleted; update minor number if adding an attribute. (The patch process does not allow an attribute's type to change.)
* Representing data ownership
  * We decided that, like zones, ownership should be moved to its own classification rather than a property on Asset. 
  * This allows the ownership to be maintained in a repository other than the entity's home repository -- important for cases where the home repository is not functionally capable of maintaining the ownership information."
* Migration steps for getting Egeria in production in ING
* UI Update, Demo & Discussion
  * David has talked about multi-tenant UI services. 


## 30th August 2019
### Day 3: Focus Group / Community Session
#### Morning Session: 
* Feedback update from San Diego Open Source Community Session
* Focus Group Session for ING Tech Romania
  * Exploration of Data Lineage with Egeria
  * Data Security & Governance with Egeria
#### Afternoon Session: 
* Github review and PR size workflow optimisation
  * Every PR should have an issue attached with it
  * OMRS and Type will have a separate PR
  * Specific and small commit for single file/functionality changes rather than big commit 
  * Add java doc as coding quality rules 
* Discovery Service Use Case for ING Wholesale Banking Advanced Analytics   
* Feedback Session: how can we build a better Egeria Community
  * Q&A session / Frequently Asked Questions
  * Simple Business Case for Egeria
  * Regular issue call that is open for anyone
  * Projects / issues for all work in progress (Better overview of what is being developed)
  * Improve Egeria Blog Space
  * Better Documentation
  * Egeria for dummies 
  * Newsletter
  * Fast & Easy run demos
  * Simple / Easy video introduction
  * Egeria Hoodies
  * Better web presence (Google Search Result, Wiki page, etc)
  * Better Egeria website design

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

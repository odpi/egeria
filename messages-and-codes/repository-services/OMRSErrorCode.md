<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OMRSErrorCode

The OMRSErrorCode is used to define first failure data capture (FFDC) for errors that occur within the OMRS It is used in conjunction with all OMRS Exceptions, both Checked and Runtime (unchecked).

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 188 |
| **Message identifiers begin** | `OMRS-` |
| **Java class** | `org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode` |
| **Module** | [open-metadata-implementation/repository-services/repository-services-apis](../../open-metadata-implementation/repository-services/repository-services-apis) |
| **Source** | [OMRSErrorCode.java](../../open-metadata-implementation/repository-services/repository-services-apis/src/main/java/org/odpi/openmetadata/repositoryservices/ffdc/OMRSErrorCode.java) |
| **Further reading** | <https://egeria-project.org/services/omrs/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [OMRS-REPOSITORY-400-001](#omrs-repository-400-001) | 400 | Unable to delete the TypeDef {0} (guid = {1}) since it is still in use in the open metadata repository {2} |
| [OMRS-REPOSITORY-400-003](#omrs-repository-400-003) | 400 | Unable to add the TypeDef {0} (guid = {1}) since it is already defined in the open metadata repository {2} |
| [OMRS-REPOSITORY-400-004](#omrs-repository-400-004) | 400 | Unable to add the AttributeTypeDef {0} (guid = {1}) since it is already defined in the open metadata repository {2} |
| [OMRS-REPOSITORY-400-005](#omrs-repository-400-005) | 400 | Classification {0} is not a recognized classification type by open metadata repository {1} |
| [OMRS-REPOSITORY-400-006](#omrs-repository-400-006) | 400 | Open metadata repository {0} cannot assign a classification of type {1} to an entity of type {2} as the classification type is not valid for this type of entity |
| [OMRS-REPOSITORY-400-007](#omrs-repository-400-007) | 400 | A null TypeDef name has been passed as the {0} parameter on a {1} request to open metadata repository {2} |
| [OMRS-REPOSITORY-400-008](#omrs-repository-400-008) | 400 | A null AttributeTypeDef name has been passed as the {0} parameter on a {1} request to open metadata repository {2} |
| [OMRS-REPOSITORY-400-009](#omrs-repository-400-009) | 400 | A null TypeDef category has been passed as the {0} parameter on a {1} request to open metadata repository {2} |
| [OMRS-REPOSITORY-400-010](#omrs-repository-400-010) | 400 | A null AttributeTypeDef category has been passed as the {0} parameter on a {1} request to open metadata repository {2} |
| [OMRS-REPOSITORY-400-011](#omrs-repository-400-011) | 400 | A null list of match criteria properties has been passed as the {0} parameter on a {1} request to open metadata repository {2} |
| [OMRS-REPOSITORY-400-012](#omrs-repository-400-012) | 400 | Null values for all the parameters describing an external id for a standard has been passed on a {0} request to open metadata repository {1} |
| [OMRS-REPOSITORY-400-013](#omrs-repository-400-013) | 400 | A null search criteria has been passed as the {0} parameter on a {1} request to open metadata repository {2} |
| [OMRS-REPOSITORY-400-014](#omrs-repository-400-014) | 400 | A null unique identifier (guid) has been passed as the {0} parameter on a {1} request to open metadata repository {2} |
| [OMRS-REPOSITORY-400-019](#omrs-repository-400-019) | 400 | A null TypeDef has been passed as the {0} parameter on a {1} request to the open metadata repository {2} |
| [OMRS-REPOSITORY-400-020](#omrs-repository-400-020) | 400 | A null AttributeTypeDef has been passed as the {0} parameter on a {1} request to the open metadata repository {2} |
| [OMRS-REPOSITORY-400-021](#omrs-repository-400-021) | 400 | A null TypeDefGalleryResponse object has been passed as the {0} parameter on a {1} request to open metadata repository {2} |
| [OMRS-REPOSITORY-400-022](#omrs-repository-400-022) | 400 | A null unique identifier (guid) for a TypeDef object has been passed as the {0} parameter on a {1} request to open metadata repository {2} |
| [OMRS-REPOSITORY-400-023](#omrs-repository-400-023) | 400 | A null unique name for a TypeDef object has been passed as the {0} parameter on a {1} request to open metadata repository {2} |
| [OMRS-REPOSITORY-400-024](#omrs-repository-400-024) | 400 | A null unique identifier (guid) for a AttributeTypeDef object has been passed as the {0} parameter on a {1} request to open metadata repository {2} |
| [OMRS-REPOSITORY-400-025](#omrs-repository-400-025) | 400 | Local metadata repository has not initialized correctly because it was unable to create its metadata collection |
| [OMRS-REPOSITORY-400-026](#omrs-repository-400-026) | 400 | A null classification name has been passed as the {0} parameter on a {1} request to open metadata repository {2} |
| [OMRS-REPOSITORY-400-027](#omrs-repository-400-027) | 400 | A null user name has been passed as the {0} parameter on a {1} request to open metadata repository {2} |
| [OMRS-REPOSITORY-400-028](#omrs-repository-400-028) | 400 | A property called {0} has been proposed for a metadata instance of category {1} and type {2}; it is not supported for this type in open metadata repository {3} |
| [OMRS-REPOSITORY-400-029](#omrs-repository-400-029) | 400 | Properties have been proposed for a new metadata instance of category {0} and type {1}; properties not supported for this type in open metadata repository {2} |
| [OMRS-REPOSITORY-400-030](#omrs-repository-400-030) | 400 | A property called {0} of type {1} has been proposed for a new metadata instance of category {2} and type {3}; this property should be of type {4} in open metadata repository {5} |
| [OMRS-REPOSITORY-400-031](#omrs-repository-400-031) | 400 | A null property name has been passed as the {0} parameter on a {1} request to open metadata repository {2} |
| [OMRS-REPOSITORY-400-032](#omrs-repository-400-032) | 400 | A null property value has been passed as the {0} parameter on a {1} request to open metadata repository {2} |
| [OMRS-REPOSITORY-400-033](#omrs-repository-400-033) | 400 | A null property type has been passed as the {0} parameter on a {1} request to open metadata repository {2} |
| [OMRS-REPOSITORY-400-034](#omrs-repository-400-034) | 400 | A invalid TypeDef unique identifier (guid) has been passed as the {0} parameter on a {1} request to open metadata repository {2} |
| [OMRS-REPOSITORY-400-035](#omrs-repository-400-035) | 400 | An instance status of {0} has been passed as the {1} parameter on a {2} request to open metadata repository {3} but this status is not valid for an instance of type {4} |
| [OMRS-REPOSITORY-400-037](#omrs-repository-400-037) | 400 | No properties have been passed on the {0} parameter on a {1} request to open metadata repository {2} |
| [OMRS-REPOSITORY-400-038](#omrs-repository-400-038) | 400 | A future time of {0} has been passed on the {0} parameter of a {1} request to open metadata repository {2} |
| [OMRS-REPOSITORY-400-039](#omrs-repository-400-039) | 400 | Incompatible TypeDef unique identifiers (name={0}, guid{1}) have been passed on a {2} request for instance {3} to open metadata repository {4} |
| [OMRS-REPOSITORY-400-040](#omrs-repository-400-040) | 400 | Unexpected exception {0} occurred when comparing properties against a search string of {1} during the {2} operation from {3}. Error message was {4} |
| [OMRS-REPOSITORY-400-041](#omrs-repository-400-041) | 400 | A null reference instance has been passed to repository {0} during the {1} in the {2} parameter |
| [OMRS-REPOSITORY-400-042](#omrs-repository-400-042) | 400 | The endMatchCriteria {0} is specified on method {1} without any end criteria |
| [OMRS-REPOSITORY-400-043](#omrs-repository-400-043) | 400 | A null entity proxy has been passed to repository {0} as the {1} parameter of the {2} operation |
| [OMRS-REPOSITORY-400-044](#omrs-repository-400-044) | 400 | An entity proxy has been passed to repository {0} as the {1} parameter of the {2} operation which has the local repository as its home |
| [OMRS-REPOSITORY-400-045](#omrs-repository-400-045) | 400 | A {0} request has been made to repository {1} for an instance {2} that is already deleted |
| [OMRS-REPOSITORY-400-046](#omrs-repository-400-046) | 400 | A {0} request has been made to repository {1} for an instance {2} that is not deleted |
| [OMRS-REPOSITORY-400-047](#omrs-repository-400-047) | 400 | A {0} request has been made to repository {1} for a relationship that has one or more ends of the wrong or invalid type.  Relationship type is {2}; entity proxy {3} for end 1 is of type {4} rather than {5} and entity proxy {6} for end 2 is of type {7} rather than {8} |
| [OMRS-REPOSITORY-400-048](#omrs-repository-400-048) | 400 | A {0} request has been made to repository {1} to access a non-existent classification {2} from entity {3} |
| [OMRS-REPOSITORY-400-049](#omrs-repository-400-049) | 400 | A null TypeDef patch has been passed on the {0} operation of repository {1} |
| [OMRS-REPOSITORY-400-050](#omrs-repository-400-050) | 400 | A negative pageSize of {0} has been passed on the {0} parameter of a {1} request to open metadata repository {2} |
| [OMRS-REPOSITORY-400-051](#omrs-repository-400-051) | 400 | A request for entity {0} has been passed to repository {1} as the {2} parameter of the {3} operation but only an entity proxy has been found |
| [OMRS-REPOSITORY-400-052](#omrs-repository-400-052) | 400 | The entity {0} retrieved from repository {1} during the {2} operation has invalid contents: {3} |
| [OMRS-REPOSITORY-400-053](#omrs-repository-400-053) | 400 | The relationship {0} retrieved from repository {1} during the {2} operation has invalid contents: {3} |
| [OMRS-REPOSITORY-400-054](#omrs-repository-400-054) | 400 | The element {0} retrieved from repository {1} during the {2} operation has a null metadata collection id in its header: {3} |
| [OMRS-REPOSITORY-400-055](#omrs-repository-400-055) | 400 | An unexpected {0} exception was received from a repository connector during the {1} operation which had message: {2} |
| [OMRS-REPOSITORY-400-056](#omrs-repository-400-056) | 400 | The OMRS repository connector operation {0} from the OMRS Enterprise Repository Services can not locate the home repository connector for instance {1} located in metadata collection {2} |
| [OMRS-REPOSITORY-400-057](#omrs-repository-400-057) | 400 | The OMRS repository connector operation {0} does not allow a null value for {1} from {2} |
| [OMRS-REPOSITORY-400-058](#omrs-repository-400-058) | 400 | An instance status of {0} has been passed as the {1} parameter on a {2} request to open metadata repository {3} however this status is not valid for an instance of type {4} |
| [OMRS-REPOSITORY-400-059](#omrs-repository-400-059) | 400 | Type definition with guid {0} and name {1} conflicts with an existing type definition in open metadata repository {2} |
| [OMRS-REPOSITORY-400-060](#omrs-repository-400-060) | 400 | The repository helper method {0} has been called with a null parameter |
| [OMRS-REPOSITORY-400-061](#omrs-repository-400-061) | 400 | An invalid instance has been detected by repository helper method {0}.  The instance is {1} |
| [OMRS-REPOSITORY-400-062](#omrs-repository-400-062) | 400 | An unexpected {0} exception was caught by {1}; error message was {2} |
| [OMRS-REPOSITORY-400-063](#omrs-repository-400-063) | 400 | Method {0} cannot request a refresh of instance {1} as it is a local member of metadata collection {2} in repository {3} |
| [OMRS-REPOSITORY-400-064](#omrs-repository-400-064) | 400 | Method {0} cannot locate an instance with guid {1} in the archive |
| [OMRS-REPOSITORY-400-065](#omrs-repository-400-065) | 400 | Method {0} cannot accept the new type definition {1} from {2} because it has a header version of {3} which is greater than this repository can support ({4}) |
| [OMRS-REPOSITORY-400-066](#omrs-repository-400-066) | 400 | Method {0} cannot accept the new {1} instance from {2} with guid {3} and type {4} because it has a header version of {5} which is greater than this repository can support ({6}) |
| [OMRS-REPOSITORY-400-067](#omrs-repository-400-067) | 400 | Method {0} has detected invalid version values in TypeDef patch from {1}. The updateToVersion {2} is less than the applyToVersion {3}.  This is the contents of the patch {4} |
| [OMRS-REPOSITORY-400-068](#omrs-repository-400-068) | 400 | Method {0} has detected that a TypeDef patch from {1} is for a future level from the active TypeDef.  The applyToVersion is {2} and the active TypeDef version is {3}. This is the contents of the patch {4} |
| [OMRS-REPOSITORY-400-069](#omrs-repository-400-069) | 400 | Method {0} has detected that a TypeDef patch from {1} has the mandatory field {2} set to null which is invalid. This is the contents of the patch {3} |
| [OMRS-REPOSITORY-400-070](#omrs-repository-400-070) | 400 | Method {0} has detected that a TypeDef patch from {1} attempts to change the type of property {2} from {3} to {4}. This is the contents of the patch {5} |
| [OMRS-REPOSITORY-400-071](#omrs-repository-400-071) | 400 | The Open Metadata Repository Services (OMRS) has been called to initialize with no audit log destinations defined for server {0} |
| [OMRS-REPOSITORY-400-072](#omrs-repository-400-072) | 400 | The Open Metadata Repository Services (OMRS) has been called to initialize its subsystems for server {0} before the audit log is initialized |
| [OMRS-REPOSITORY-400-073](#omrs-repository-400-073) | 400 | An invalid instance was found in a batch of reference instances send by a remote member of the cohort. The exception was {0} with message {1} |
| [OMRS-REPOSITORY-400-074](#omrs-repository-400-074) | 400 | An invalid list of property-based search conditions was provided: nestedConditions is mutually exclusive with property, operator, value |
| [OMRS-REPOSITORY-400-075](#omrs-repository-400-075) | 400 | An invalid list of classification-based search conditions was provided: name of the classification is mandatory |
| [OMRS-REPOSITORY-400-077](#omrs-repository-400-077) | 400 | An invalid string was provided for the value of a LIKE operator |
| [OMRS-REPOSITORY-400-078](#omrs-repository-400-078) | 400 | An invalid string was provided for the value of the {0} operator |
| [OMRS-REPOSITORY-400-079](#omrs-repository-400-079) | 400 | The provided subtype {0} is not a subtype of typedef {1} |
| [OMRS-REPOSITORY-400-080](#omrs-repository-400-080) | 400 | Classification {0} is not a supported classification type in open metadata repository {1} |
| [OMRS-REPOSITORY-400-081](#omrs-repository-400-081) | 400 | A {0} request has been made to repository {1} to add a classification {2} to entity {3} when this entity is already classified |
| [OMRS-REPOSITORY-400-082](#omrs-repository-400-082) | 400 | The OMRS repository connector operation {0} from the OMRS Enterprise Repository Services can not locate the home repository connector for classification {1} located in metadata collection {2} |
| [OMRS-REPOSITORY-400-083](#omrs-repository-400-083) | 400 | The OMRS repository connector operation {0} does not allow a time range from {1} to {2} |
| [OMRS-REPOSITORY-400-084](#omrs-repository-400-084) | 400 | The value supplied for property {0} of type {1} contains a null (U+0000) character at position {2}; it was passed to method {3} on the {4} parameter |
| [OMRS-PROPERTIES-400-002](#omrs-properties-400-002) | 400 | No name provided for entity classification |
| [OMRS-PROPERTIES-400-003](#omrs-properties-400-003) | 400 | Null property name passed to properties object |
| [OMRS-PROPERTIES-400-004](#omrs-properties-400-004) | 400 | {0} cannot add a new element to location {1} of an array of size {2} value |
| [OMRS-PROPERTIES-400-007](#omrs-properties-400-007) | 400 | Data type {0} is not supported by method {1} |
| [OMRS-REST-CONNECTOR-400-001](#omrs-rest-connector-400-001) | 400 | The connection passed in the cohort registration event does not contain the root URL for calling the server's REST API |
| [OMRS-CONNECTOR-400-004](#omrs-connector-400-004) | 400 | The connection {0} passed to the EnterpriseOMRSRepositoryConnector is invalid |
| [OMRS-CONNECTOR-400-005](#omrs-connector-400-005) | 400 | The connector to the local repository failed with a {0} exception and the following error message: {1} |
| [OMRS-TOPIC-CONNECTOR-400-001](#omrs-topic-connector-400-001) | 400 | Unable to send or receive events for source {0} because the connector to the OMRS Topic failed to initialize |
| [OMRS-TOPIC-CONNECTOR-400-002](#omrs-topic-connector-400-002) | 400 | The connector {0} has been configured without an embedded event bus connector |
| [OMRS-COHORT-REGISTRY-404-001](#omrs-cohort-registry-404-001) | 400 | The Open Metadata Repository Cohort Registry Store for cohort {0} is not available |
| [OMRS-COHORT-REGISTRY-400-002](#omrs-cohort-registry-400-002) | 400 | The Open Metadata Repository Cohort {0} is not available to server {1} because the local metadata collection id has been changed from {2} to {3} since this server registered with the cohort |
| [OMRS-ARCHIVE-MANAGER-400-001](#omrs-archive-manager-400-001) | 400 | An open metadata archive configured for server {0} is not accessible |
| [OMRS-LOCAL-REPOSITORY-400-001](#omrs-local-repository-400-001) | 400 | The repository event mapper configured for the local repository for server {0} is not accessible |
| [OMRS-LOCAL-REPOSITORY-400-002](#omrs-local-repository-400-002) | 400 | The local repository is not able to re-home the instance {0} of type {1} ({2}) because it is not managing the repository with the requested home metadata collection of {3}.  This local repository is managing the {4} metadata collection |
| [OMRS-ENTERPRISE-REPOSITORY-400-001](#omrs-enterprise-repository-400-001) | 400 | Conflicting TypeDefs have been detected |
| [OMRS-ARCHIVE-BUILDER-400-001](#omrs-archive-builder-400-001) | 400 | The same type {0} of category {1} has been added twice to an open metadata archive. First version was {2} and the second was {3} |
| [OMRS-ARCHIVE-BUILDER-400-002](#omrs-archive-builder-400-002) | 400 | The {0} instance {1} has been added twice to an open metadata archive. First version was {2} and the second was {3} |
| [OMRS-ARCHIVE-BUILDER-400-003](#omrs-archive-builder-400-003) | 400 | The same type name {0} has been added twice to an open metadata archive. First version was {1} and the second was {2} |
| [OMRS-ARCHIVE-BUILDER-400-004](#omrs-archive-builder-400-004) | 400 | The guid {0} has been used twice to an open metadata archive. First version was {1} and the second was {2} |
| [OMRS-ARCHIVE-BUILDER-400-005](#omrs-archive-builder-400-005) | 400 | The type {0} of category {1} is not found in an open metadata archive |
| [OMRS-ARCHIVE-BUILDER-400-006](#omrs-archive-builder-400-006) | 400 | A request for a type from category {0} passed a null name |
| [OMRS-ARCHIVE-BUILDER-400-007](#omrs-archive-builder-400-007) | 400 | RelationshipEndDef1 type {0} and EndDef1 name {1} in RelationshipDef {2} are incorrect, because another entity or relationship endDef is already using this attribute name |
| [OMRS-ARCHIVE-BUILDER-400-008](#omrs-archive-builder-400-008) | 400 | RelationshipEndDef2 type {0} and EndDef2 name {1} in RelationshipDef {2} are incorrect, because another entity or relationship endDef is already using this attribute name |
| [OMRS-ARCHIVE-BUILDER-400-009](#omrs-archive-builder-400-009) | 400 | Duplicate attribute name {0} is defined in RelationshipDef {1} |
| [OMRS-ARCHIVE-BUILDER-400-010](#omrs-archive-builder-400-010) | 400 | Duplicate attribute name {0} is defined in EntityDef {1} |
| [OMRS-ARCHIVE-BUILDER-400-011](#omrs-archive-builder-400-011) | 400 | Duplicate attribute name {0} is defined in ClassificationDef {1} |
| [OMRS-ARCHIVE-BUILDER-400-012](#omrs-archive-builder-400-012) | 400 | Type name {0} is invalid because it contains a blank character |
| [OMRS-ARCHIVE-BUILDER-400-013](#omrs-archive-builder-400-013) | 400 | The archive builder has been passed an unknown type name {0} |
| [OMRS-AUDIT-LOG-400-002](#omrs-audit-log-400-002) | 400 | An Audit Log destination for server {0} is not correctly configured and a {1} exception occurred with message {2} |
| [OMRS-AUDIT-LOG-400-003](#omrs-audit-log-400-003) | 400 | A null log record has been passed by the audit log to the audit log destination {0} |
| [OMRS-AUDIT-LOG-400-004](#omrs-audit-log-400-004) | 400 | A log record with a null originator has been passed by the audit log to the audit log destination {0} |
| [OMRS-AUDIT-LOG-400-005](#omrs-audit-log-400-005) | 400 | A log record with a null reporting component has been passed by the audit log to the audit log destination {0} |
| [OMRS-AUDIT-LOG-400-006](#omrs-audit-log-400-006) | 400 | The Audit Log destination {0} is not able to support queries |
| [OMRS-AUDIT-LOG-400-008](#omrs-audit-log-400-008) | 400 | The Audit log destination {0} is not able to convert an audit log record to JSON format |
| [OMRS-AUDIT-LOG-400-009](#omrs-audit-log-400-009) | 400 | The archive manager is not active in server {0}.  Redirect the load request to a metadata access store |
| [OMRS-REPOSITORY-404-001](#omrs-repository-404-001) | 404 | The open metadata repository connector for server {0} is not active and cannot service the {1} request |
| [OMRS-REPOSITORY-404-002](#omrs-repository-404-002) | 404 | The entity identified with guid {0} passed on the {1} call is not known to the open metadata repository {2} |
| [OMRS-REPOSITORY-404-003](#omrs-repository-404-003) | 404 | The relationship identified with guid {0} passed on the {1} call is not known to the open metadata repository {2} |
| [OMRS-REPOSITORY-404-004](#omrs-repository-404-004) | 404 | The TypeDef {0} (guid = {1}) passed on the {2} parameter of the {3} operation is not known to the open metadata repository {4} |
| [OMRS-REPOSITORY-404-005](#omrs-repository-404-005) | 404 | The TypeDef {0} of category {1} passed by the {2} operation is not known to the open metadata repository {3} |
| [OMRS-REPOSITORY-404-007](#omrs-repository-404-007) | 404 | The TypeDef unique identifier {0} passed as parameter {1} on a {2} request to open metadata repository {3} is not known to this repository |
| [OMRS-REPOSITORY-404-009](#omrs-repository-404-009) | 404 | The TypeDef unique name {0} passed on a {1} request to open metadata repository {2} is not known to this repository |
| [OMRS-REPOSITORY-404-011](#omrs-repository-404-011) | 404 | The relationship identified with guid {0} passed on the {1} call is not found to the open metadata repository {2} |
| [OMRS-REPOSITORY-404-012](#omrs-repository-404-012) | 404 | The {0} relationship identified with guid {1} passed on the {2} call is soft-deleted in the open metadata repository {3} |
| [OMRS-REPOSITORY-404-013](#omrs-repository-404-013) | 404 | The {0} entity identified with guid {1} passed on the {2} call is soft-deleted in the open metadata repository {3} |
| [OMRS-REPOSITORY-CONNECTOR-404-002](#omrs-repository-connector-404-002) | 404 | The Open Metadata Repository Servers in the cohort are not available |
| [OMRS-REPOSITORY-CONNECTOR-404-003](#omrs-repository-connector-404-003) | 404 | The open metadata repository servers in the cohort are not configured correctly |
| [OMRS-METADATA-HIGHWAY-404-002](#omrs-metadata-highway-404-002) | 404 | The local server cannot disconnect from an open metadata repository cohort {0} |
| [OMRS-METADATA-HIGHWAY-404-003](#omrs-metadata-highway-404-003) | 400 | There are more than one cohort configurations with the same name of {0} |
| [OMRS-REST-REPOSITORY-CONNECTOR-404-001](#omrs-rest-repository-connector-404-001) | 404 | A call to the {0} of the open metadata repository server {1} results in an exception {2} with message {3} |
| [OMRS-METADATA-HIGHWAY-404-004](#omrs-metadata-highway-404-004) | 404 | The local server cannot initiate a connection to the cohort {0} when starting up |
| [OMRS-ENTERPRISE-REPOSITORY-CONNECTOR-405-001](#omrs-enterprise-repository-connector-405-001) | 405 | The requested method {0} is not supported by the EnterpriseOMRSRepositoryConnector |
| [OMRS-METADATA-COLLECTION-409-001](#omrs-metadata-collection-409-001) | 409 | Multiple instances of type {0} have been returned to {2} of service {1} when there should be one at most.  These are examples of the entities returned: {3} |
| [OMRS-METADATA-COLLECTION-409-002](#omrs-metadata-collection-409-002) | 409 | Multiple instances of type {0} have been returned to {2} of service {1} when there should be a maximum of one.  These are examples of the entities returned: {3} |
| [OMRS-METADATA-COLLECTION-500-001](#omrs-metadata-collection-500-001) | 500 | The Java class {0} for PrimitiveDefCategory {1} is not known |
| [OMRS-METADATA-COLLECTION-500-002](#omrs-metadata-collection-500-002) | 500 | The primitive value should be stored in Java class {0} rather than {1} since it is of PrimitiveDefCategory {2} |
| [OMRS-METADATA-COLLECTION-500-003](#omrs-metadata-collection-500-003) | 500 | There is a problem in the definition of primitive type {0} |
| [OMRS-METADATA-COLLECTION-500-004](#omrs-metadata-collection-500-004) | 500 | Null home metadata collection identifier found by method {1} in property {0} from open metadata repository {3} |
| [OMRS-METADATA-COLLECTION-500-006](#omrs-metadata-collection-500-006) | 500 | The open metadata repository connector {0} has been initialized with a null metadata collection identifier |
| [OMRS-METADATA-COLLECTION-500-009](#omrs-metadata-collection-500-009) | 500 | Unable to complete operation {0} to open metadata repository {1} because the repository connector is null |
| [OMRS-METADATA-COLLECTION-500-010](#omrs-metadata-collection-500-010) | 500 | Unable to complete operation {0} to open metadata repository {1} because the repository validator is null |
| [OMRS-METADATA-COLLECTION-500-011](#omrs-metadata-collection-500-011) | 500 | Unable to complete operation {0} to open metadata repository {1} as the repository connector is null |
| [OMRS-METADATA-COLLECTION-500-012](#omrs-metadata-collection-500-012) | 500 | Open metadata repository {0} has encountered an unexpected exception during the {1} operation.  The full message was {2} |
| [OMRS-METADATA-COLLECTION-500-013](#omrs-metadata-collection-500-013) | 500 | During the {0} operation, open metadata repository {1} retrieved an instance from its metadata store that has a null type |
| [OMRS-METADATA-COLLECTION-500-014](#omrs-metadata-collection-500-014) | 500 | During the {0} operation, open metadata repository {1} retrieved an instance (guid={2}) from its metadata store that has an inactive type called {3} (type guid = {4}) |
| [OMRS-METADATA-COLLECTION-500-015](#omrs-metadata-collection-500-015) | 500 | The value supplied for an attribute of PrimitiveDefCategory {0} is expected as Java class {1} but was supplied as Java class {2} |
| [OMRS-METADATA-COLLECTION-500-016](#omrs-metadata-collection-500-016) | 500 | The home metadata collection identifier {0} found by method {1} for instance with GUID {2} is not the metadata collection identifier {3} for the local metadata repository {4} |
| [OMRS-METADATA-COLLECTION-500-017](#omrs-metadata-collection-500-017) | 500 | The home metadata collection identifier {0} found by method {1} for instance with GUID {2} is the metadata collection identifier {3} for the local metadata repository {4} |
| [OMRS-METADATA-COLLECTION-500-018](#omrs-metadata-collection-500-018) | 500 | The open metadata repository connector {0} has returned a null metadata collection identifier |
| [OMRS-COHORT-MANAGER-500-001](#omrs-cohort-manager-500-001) | 500 | OMRSCohortManager has been initialized with a null cohort name |
| [OMRS-OPERATIONAL-SERVICES-500-001](#omrs-operational-services-500-001) | 500 | No configuration has been passed to the Open Metadata Repository Services (OMRS) on initialization os server {0} |
| [OMRS-LOCAL-REPOSITORY-500-001](#omrs-local-repository-500-001) | 500 | The local repository services have been initialized with a null real metadata collection. |
| [OMRS-LOCAL-REPOSITORY-500-002](#omrs-local-repository-500-002) | 500 | The local repository for server {0} failed to initialize and returned a {1} exception with message {2} |
| [OMRS-ENTERPRISE-REPOSITORY-500-001](#omrs-enterprise-repository-500-001) | 500 | The enterprise repository services has detected a repository connector with a null metadata collection |
| [OMRS-CONTENT-MANAGER-500-001](#omrs-content-manager-500-001) | 500 | The repository content manager method {0} has detected an unknown TypeDef {1} from {2} on behalf of method {3} |
| [OMRS-CONTENT-MANAGER-500-002](#omrs-content-manager-500-002) | 500 | The repository content manager has detected an invalid attribute name in a TypeDef from {0} |
| [OMRS-CONTENT-MANAGER-500-003](#omrs-content-manager-500-003) | 500 | The repository content manager has detected a null attribute in a TypeDef from {0} |
| [OMRS-CONTENT-MANAGER-500-004](#omrs-content-manager-500-004) | 500 | Source {0} has requested type {1} with an incompatible category of {2} from repository content manager |
| [OMRS-CONTENT-MANAGER-500-005](#omrs-content-manager-500-005) | 500 | The repository content manager has detected an unknown TypeDef {0} ({1}) from {2}. It was passed to method {3} via parameters {4} and {5} |
| [OMRS-CONTENT-MANAGER-500-006](#omrs-content-manager-500-006) | 500 | The repository content manager has received an instance {0} of class {1} with an open metadata type name of {2}, which is from category {3} |
| [OMRS-OPEN-METADATA-ARCHIVE-500-001](#omrs-open-metadata-archive-500-001) | 500 | The archive builder failed to initialize |
| [OMRS-EVENT-MANAGEMENT-500-001](#omrs-event-management-500-001) | 500 | A null exchange rule has been passed to one of the event management components on method {0} for cohort {1} |
| [OMRS-EVENT-MANAGEMENT-500-002](#omrs-event-management-500-002) | 500 | A null repository validator has been passed to one of the event management components |
| [OMRS-EVENT-MANAGEMENT-500-003](#omrs-event-management-500-003) | 500 | A null repository helper has been passed to one of the event management components |
| [OMRS-EVENT-MANAGEMENT-500-004](#omrs-event-management-500-004) | 500 | A null event has been passed to one of the event management components |
| [OMRS-REST-REPOSITORY-CONNECTOR-500-001](#omrs-rest-repository-connector-500-001) | 500 | A remote open metadata repository {0} returned a metadata collection identifier of {1} on its REST API after it registered with the cohort using a metadata collection identifier of {2} |
| [OMRS-METADATA-TOPIC-CONNECTOR-500-001](#omrs-metadata-topic-connector-500-001) | 500 | A null topic listener has been passed to the {0} open metadata topic connector {1} |
| [OMRS-TOPIC-CONNECTOR-500-003](#omrs-topic-connector-500-003) | 500 | Connector {0} cannot send a null event |
| [OMRS-TOPIC-CONNECTOR-500-006](#omrs-topic-connector-500-006) | 500 | The requested connector for connection named {0} has not been created.  The connection was provided by the {1} service running in OMAG Server at {2} |
| [OMRS-TOPIC-CONNECTOR-500-007](#omrs-topic-connector-500-007) | 500 | The connector generated from the connection named {0} return by the {1} service running in OMAG Server at {2} is not of the required type. It should be an instance of {3} |
| [OMRS-METADATA-COLLECTION-501-001](#omrs-metadata-collection-501-001) | 501 | OMRSMetadataInstanceStore method {0} for OMRS Connector {1} to repository type {2} is not implemented |
| [OMRS-METADATA-COLLECTION-501-002](#omrs-metadata-collection-501-002) | 501 | Repository {0} is not able to support the {1} type |
| [OMRS-TOPIC-CONNECTOR-501-001](#omrs-topic-connector-501-001) | 501 | Connector {0} is not able to support event protocol {1} |
| [OMRS-ENTERPRISE-REPOSITORY-503-001](#omrs-enterprise-repository-503-001) | 503 | There are no open metadata repositories available for access service {0} |
| [OMRS-ENTERPRISE-REPOSITORY-503-003](#omrs-enterprise-repository-503-003) | 503 | The enterprise repository services has detected a repository connector from cohort {0} for metadata collection identifier {1} that has a null metadata collection API object |
| [OMRS-LOCAL-REPOSITORY-503-003](#omrs-local-repository-503-003) | 503 | The connection to the local open metadata repository server is not configured correctly |
| [OMRS-LOCAL-REPOSITORY-503-004](#omrs-local-repository-503-004) | 503 | The connection to the local open metadata repository server has not been configured correctly |
| [OMRS-LOCAL-REPOSITORY-503-005](#omrs-local-repository-503-005) | 503 | An OMRS repository connector or access service {0} has passed an invalid parameter to the repository validator {1} operation as part of the {2} request |
| [OMRS-LOCAL-REPOSITORY-503-006](#omrs-local-repository-503-006) | 503 | An OMRS repository connector {0} has passed an invalid parameter to the repository content manager {1} operation as part of the {2} request |
| [OMRS-LOCAL-REPOSITORY-503-007](#omrs-local-repository-503-007) | 503 | The local OMRS repository connector {0} hosts the home metadata collection for entity {1} but only has an entity proxy stored.  It is not able to complete the {2} request |
| [OMRS-LOCAL-REPOSITORY-503-008](#omrs-local-repository-503-008) | 503 | An OMRS repository connector or access server {0} has passed a null classification to the repository helper {1} operation as part of the {2} request |
| [OMRS-LOCAL-REPOSITORY-503-009](#omrs-local-repository-503-009) | 503 | The local OMRS repository connector {0} has been asked to update entity {1} but it is not the owner.It is not able to complete the {2} request |
| [OMRS-LOCAL-REPOSITORY-503-010](#omrs-local-repository-503-010) | 503 | The local OMRS repository connector {0} has been asked to update relationship {1} but it is not the owner.It is not able to complete the {2} request |
| [OMRS-LOCAL-REPOSITORY-503-011](#omrs-local-repository-503-011) | 503 | The local OMRS repository connector {0} requested an instance {1} from the real metadata collection but a null was returned.It is not able to complete the {2} request |
| [OMRS-REPOSITORY-HELPER-503-001](#omrs-repository-helper-503-001) | 503 | A caller {0} has passed an invalid parameter to the repository helper {1} operation as part of the {2} request |
| [OMRS-REPOSITORY-HELPER-503-002](#omrs-repository-helper-503-002) | 503 | A caller {0} has passed an invalid parameter to the repository helper {1} operation as part of the {2} request resulting in an unexpected exception {3} with message {4} |
| [OMRS-REST-API-503-001](#omrs-rest-api-503-001) | 503 | There is no local repository to support REST API call {0} |
| [OMRS-REST-API-503-002](#omrs-rest-api-503-002) | 503 | There is no enterprise repository to support REST API call {0} |
| [OMRS-REST-API-503-004](#omrs-rest-api-503-004) | 503 | A null response was received from REST API call {0} to repository {1} |
| [OMRS-REST-API-503-005](#omrs-rest-api-503-005) | 503 | Unable to create REST Client for repository {0}.  The error message was {1} |
| [OMRS-REST-API-503-006](#omrs-rest-api-503-006) | 503 | A client-side exception was received from API call {0} to repository {1}.  The error message was {2} |

----

### OMRS-REPOSITORY-400-001

> Unable to delete the TypeDef {0} (guid = {1}) since it is still in use in the open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.TYPEDEF_IN_USE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot delete the TypeDef because there are still instances in the metadata repository that are using it.

**User action**

Remove the existing instances from the open metadata repositories and try the delete again.


----

### OMRS-REPOSITORY-400-003

> Unable to add the TypeDef {0} (guid = {1}) since it is already defined in the open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.TYPEDEF_ALREADY_DEFINED` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot add the TypeDef to its repository because it is already defined.

**User action**

Validate that the existing type definition is as required.  It is possible to patch the TypeDef, or delete it and re-define it.


----

### OMRS-REPOSITORY-400-004

> Unable to add the AttributeTypeDef {0} (guid = {1}) since it is already defined in the open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.ATTRIBUTE_TYPEDEF_ALREADY_DEFINED` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot delete the AttributeTypeDef because because it is already defined.

**User action**

Validate that the existing attribute type definition is as required.  It is not possible to patch the AttributeTypeDef so re-define it with a new name.


----

### OMRS-REPOSITORY-400-005

> Classification {0} is not a recognized classification type by open metadata repository {1}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.UNKNOWN_CLASSIFICATION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot create a new classification for an entity because the open metadata repository does not recognize the classification type.

**User action**

Create a ClassificationDef for the classification and retry the request.


----

### OMRS-REPOSITORY-400-006

> Open metadata repository {0} cannot assign a classification of type {1} to an entity of type {2} as the classification type is not valid for this type of entity

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INVALID_CLASSIFICATION_FOR_ENTITY` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system is not able to classify an entity since the ClassificationDef for the classification does not list the entity type, or one of its super-types.

**User action**

Update the ClassificationDef to include the entity's type and rerun the request. Alternatively use a different classification.


----

### OMRS-REPOSITORY-400-007

> A null TypeDef name has been passed as the {0} parameter on a {1} request to open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NO_TYPEDEF_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot perform the request because the TypeDef name is needed.

**User action**

Correct the caller's code to include the TypeDef name and retry the request.


----

### OMRS-REPOSITORY-400-008

> A null AttributeTypeDef name has been passed as the {0} parameter on a {1} request to open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NO_ATTRIBUTE_TYPEDEF_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot perform the request because the AttributeTypeDef name is needed.

**User action**

Correct the caller's code to include the AttributeTypeDef name and retry the request.


----

### OMRS-REPOSITORY-400-009

> A null TypeDef category has been passed as the {0} parameter on a {1} request to open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NO_TYPEDEF_CATEGORY` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot perform the request because the TypeDef category is needed.

**User action**

Correct the caller's code and retry the request.


----

### OMRS-REPOSITORY-400-010

> A null AttributeTypeDef category has been passed as the {0} parameter on a {1} request to open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NO_ATTRIBUTE_TYPEDEF_CATEGORY` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot perform the request because the AttributeTypeDef category is needed.

**User action**

Fix the caller's code and try the request again.


----

### OMRS-REPOSITORY-400-011

> A null list of match criteria properties has been passed as the {0} parameter on a {1} request to open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NO_MATCH_CRITERIA` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot perform the request because the match criteria is needed.

**User action**

Correct the calling code and retry the request.


----

### OMRS-REPOSITORY-400-012

> Null values for all the parameters describing an external id for a standard has been passed on a {0} request to open metadata repository {1}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NO_EXTERNAL_ID` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot perform the request because at least one of the values are needed.

**User action**

Correct the caller's code and repeat the request.


----

### OMRS-REPOSITORY-400-013

> A null search criteria has been passed as the {0} parameter on a {1} request to open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NO_SEARCH_CRITERIA` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot perform the request because the search criteria is needed.

**User action**

Correct the caller's code and repeat the request again.


----

### OMRS-REPOSITORY-400-014

> A null unique identifier (guid) has been passed as the {0} parameter on a {1} request to open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NO_GUID` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot perform the request because the unique identifier is needed.

**User action**

Fix the calling code and retry the request.


----

### OMRS-REPOSITORY-400-019

> A null TypeDef has been passed as the {0} parameter on a {1} request to the open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_TYPEDEF` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot perform the request because the TypeDef is needed to perform the operation.

**User action**

Fix the invoking code and retry the request.


----

### OMRS-REPOSITORY-400-020

> A null AttributeTypeDef has been passed as the {0} parameter on a {1} request to the open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_ATTRIBUTE_TYPEDEF` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot perform the request because the AttributeTypeDef is needed to perform the operation.

**User action**

Correct the invoking source code and retry the request.


----

### OMRS-REPOSITORY-400-021

> A null TypeDefGalleryResponse object has been passed as the {0} parameter on a {1} request to open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_TYPEDEF_GALLERY` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot perform the request because the TypeDefGalleryResponse should contain the information to process.

**User action**

Fix the invocation of this call and retry the request.


----

### OMRS-REPOSITORY-400-022

> A null unique identifier (guid) for a TypeDef object has been passed as the {0} parameter on a {1} request to open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_TYPEDEF_IDENTIFIER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot perform the request because the identifier for the TypeDef is needed to proceed.

**User action**

Fix the invocation in the caller's code and retry the request.


----

### OMRS-REPOSITORY-400-023

> A null unique name for a TypeDef object has been passed as the {0} parameter on a {1} request to open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_TYPEDEF_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system is not able to perform the request because the identifier for the TypeDef is needed to proceed.

**User action**

Fix the invocation in the caller's code and repeat the request.


----

### OMRS-REPOSITORY-400-024

> A null unique identifier (guid) for a AttributeTypeDef object has been passed as the {0} parameter on a {1} request to open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_ATTRIBUTE_TYPEDEF_IDENTIFIER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot perform the request because the identifier for the AttributeTypeDef is needed to proceed.

**User action**

Correct the caller's code and try the request again.


----

### OMRS-REPOSITORY-400-025

> Local metadata repository has not initialized correctly because it was unable to create its metadata collection

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_METADATA_COLLECTION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | none |

**System action**

The metadata collection object provides access to the storage, or remote metadata service that is supporting the repository.  The system cannot process requests for this repository without a metadata collection.

**User action**

The repository connector for the local repository has not initialized correctly.  This may be an error in the repository connector's logic, or a missing or incorrect property in the connector's connection object stored in the server's configuration document, or a missing resource, or permission needed by the connector.  The repository connector should have output diagnostics either through an exception or message to the audit log that details the problem.  If no other diagnostics are present, contact the developers of the repository connector to request that the diagnostics are improved, particularly around initialization.  Use the diagnostics from the connector to diagnose the root cause of the problem and then correct either the repository connector's logic, or its configuration or runtime environment as appropriate.


----

### OMRS-REPOSITORY-400-026

> A null classification name has been passed as the {0} parameter on a {1} request to open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_CLASSIFICATION_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot access the local metadata repository.

**User action**

The classification name is supplied by the caller to the API. This call needs to be corrected before the server can operate correctly.


----

### OMRS-REPOSITORY-400-027

> A null user name has been passed as the {0} parameter on a {1} request to open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_USER_ID` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system is not able to access the local metadata repository.

**User action**

The user name is supplied by the caller to the API. This call needs to be corrected before the server can operate correctly.


----

### OMRS-REPOSITORY-400-028

> A property called {0} has been proposed for a metadata instance of category {1} and type {2}; it is not supported for this type in open metadata repository {3}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.BAD_PROPERTY_FOR_TYPE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot store the metadata instance in the metadata repository because the properties listed do not match the supplied type definition.

**User action**

Verify that the property name is spelt correctly and the correct type has been used. Correct the call to the metadata repository and retry.


----

### OMRS-REPOSITORY-400-029

> Properties have been proposed for a new metadata instance of category {0} and type {1}; properties not supported for this type in open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NO_PROPERTIES_FOR_TYPE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot store the metadata instance in the metadata repository as the properties listed do not match the supplied type definition.

**User action**

Verify that the property name is spelt correctly and the correct type has been used. Fix the call to the metadata repository and retry.


----

### OMRS-REPOSITORY-400-030

> A property called {0} of type {1} has been proposed for a new metadata instance of category {2} and type {3}; this property should be of type {4} in open metadata repository {5}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.BAD_PROPERTY_TYPE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The system cannot store the metadata instance in the metadata repository since the properties listed do not match the supplied type definition.

**User action**

Check that the property name is spelt correctly and the correct type has been used. Correct the call to the metadata repository and retry.


----

### OMRS-REPOSITORY-400-031

> A null property name has been passed as the {0} parameter on a {1} request to open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_PROPERTY_NAME_FOR_INSTANCE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot process the metadata instance.

**User action**

The property name is supplied by the caller to the API. This call needs to be corrected before the server can operate correctly.


----

### OMRS-REPOSITORY-400-032

> A null property value has been passed as the {0} parameter on a {1} request to open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_PROPERTY_VALUE_FOR_INSTANCE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system is not able to process the metadata instance.

**User action**

The property value is supplied by the caller to the API. This call needs to be corrected before the server can operate correctly.


----

### OMRS-REPOSITORY-400-033

> A null property type has been passed as the {0} parameter on a {1} request to open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_PROPERTY_TYPE_FOR_INSTANCE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot correctly process the metadata instance.

**User action**

The property type is supplied by the caller to the API. This call needs to be corrected before the server can operate correctly.


----

### OMRS-REPOSITORY-400-034

> A invalid TypeDef unique identifier (guid) has been passed as the {0} parameter on a {1} request to open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.BAD_TYPEDEF_ID_FOR_INSTANCE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot perform the request because the unique identifier is required.

**User action**

Fix the caller's code and try the request again when done.


----

### OMRS-REPOSITORY-400-035

> An instance status of {0} has been passed as the {1} parameter on a {2} request to open metadata repository {3} but this status is not valid for an instance of type {4}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.BAD_INSTANCE_STATUS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system cannot process this request.

**User action**

The instance status is supplied by the caller to the API. This call needs to be corrected before the server can complete this operation successfully.


----

### OMRS-REPOSITORY-400-037

> No properties have been passed on the {0} parameter on a {1} request to open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NO_NEW_PROPERTIES` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot process this metadata instance request.

**User action**

The instance status is supplied by the caller to the API. This call needs to be fixed before the server can operate correctly.


----

### OMRS-REPOSITORY-400-038

> A future time of {0} has been passed on the {0} parameter of a {1} request to open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.REPOSITORY_NOT_CRYSTAL_BALL` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system declines to process the request lest it gives away its secret powers.

**User action**

The asOfTime is supplied by the caller to the API. This call needs to be corrected before the server will function correctly.


----

### OMRS-REPOSITORY-400-039

> Incompatible TypeDef unique identifiers (name={0}, guid{1}) have been passed on a {2} request for instance {3} to open metadata repository {4}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.BAD_TYPEDEF_IDS_FOR_DELETE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system cannot perform the request because the unique identifiers are needed.

**User action**

Correct the caller's code to provide compatible type identifiers and retry the request.


----

### OMRS-REPOSITORY-400-040

> Unexpected exception {0} occurred when comparing properties against a search string of {1} during the {2} operation from {3}. Error message was {4}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.BAD_PROPERTY_FOR_INSTANCE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system cannot perform the request because the unique identifier must be provided.

**User action**

Correct the error in the requesting code and retry.


----

### OMRS-REPOSITORY-400-041

> A null reference instance has been passed to repository {0} during the {1} in the {2} parameter

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_REFERENCE_INSTANCE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot perform the request because the instance is needed.

**User action**

The reference instance comes from another server.  Look for errors in the audit log and validate that the message passing protocol levels are compatible. If nothing is obviously wrong with the set up, raise a Github issue or ask for help on the dev mailing list.


----

### OMRS-REPOSITORY-400-042

> The endMatchCriteria {0} is specified on method {1} without any end criteria

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INVALID_FIND_RELATIONSHIP_END_CRITERIA` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

An exception is returned to the caller and no search request is initiated.

**User action**

Either remove the endMatchCriteria, or constrain an end - by the entity guids allowed there, by the type of entity allowed there, or by both.


----

### OMRS-REPOSITORY-400-043

> A null entity proxy has been passed to repository {0} as the {1} parameter of the {2} operation

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_ENTITY_PROXY` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot perform the request because the entity proxy is needed.

**User action**

Correct the caller's code to supply the entity proxy and retry the request.


----

### OMRS-REPOSITORY-400-044

> An entity proxy has been passed to repository {0} as the {1} parameter of the {2} operation which has the local repository as its home

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.LOCAL_ENTITY_PROXY` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot perform the request because the entity proxy should represent an entity that is homed in another repository.

**User action**

Correct the bug in the caller's code and retry the request.


----

### OMRS-REPOSITORY-400-045

> A {0} request has been made to repository {1} for an instance {2} that is already deleted

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INSTANCE_ALREADY_DELETED` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot perform the request because the instance is in the wrong state.

**User action**

Try a different request or a different instance.


----

### OMRS-REPOSITORY-400-046

> A {0} request has been made to repository {1} for an instance {2} that is not deleted

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INSTANCE_NOT_DELETED` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot perform the request since the instance is in the wrong state.

**User action**

Try again with a different request or specify a different instance.


----

### OMRS-REPOSITORY-400-047

> A {0} request has been made to repository {1} for a relationship that has one or more ends of the wrong or invalid type.  Relationship type is {2}; entity proxy {3} for end 1 is of type {4} rather than {5} and entity proxy {6} for end 2 is of type {7} rather than {8}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INVALID_RELATIONSHIP_ENDS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}`, `{7}`, `{8}` |

**System action**

The system cannot perform the request because the instance has invalid values.

**User action**

Correct the caller's code and attempt the request again.


----

### OMRS-REPOSITORY-400-048

> A {0} request has been made to repository {1} to access a non-existent classification {2} from entity {3}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.ENTITY_NOT_CLASSIFIED` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot perform the request as the instance has a missing classification.

**User action**

Correct the caller's code and reattempt the request.


----

### OMRS-REPOSITORY-400-049

> A null TypeDef patch has been passed on the {0} operation of repository {1}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_TYPEDEF_PATCH` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot perform the request because it needs the patch to process the update.

**User action**

Correct the calling code and reattempt the request.


----

### OMRS-REPOSITORY-400-050

> A negative pageSize of {0} has been passed on the {0} parameter of a {1} request to open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NEGATIVE_PAGE_SIZE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot process the request.

**User action**

The pageSize parameter is supplied by the caller to the API. This call needs to be corrected before the server will operate correctly.


----

### OMRS-REPOSITORY-400-051

> A request for entity {0} has been passed to repository {1} as the {2} parameter of the {3} operation but only an entity proxy has been found

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.ENTITY_PROXY_ONLY` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot return all the details of the entity.  It can only supply an entity summary.

**User action**

The fact that the system has a proxy means that the entity exists in one of the members of the connected cohorts.  The repository where it is located may be unavailable, or the entity has been deleted but the delete request has not propagated through to this repository.


----

### OMRS-REPOSITORY-400-052

> The entity {0} retrieved from repository {1} during the {2} operation has invalid contents: {3}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INVALID_ENTITY_FROM_STORE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot continue processing the request.

**User action**

This error suggests there is a logic error in either this repository, or the home repository for the instance.  Raise a Github issue to get this fixed.


----

### OMRS-REPOSITORY-400-053

> The relationship {0} retrieved from repository {1} during the {2} operation has invalid contents: {3}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INVALID_RELATIONSHIP_FROM_STORE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system is not able to continue processing the request.

**User action**

This error suggests there is a logic error in either this repository, or the home repository for this instance.  Raise a Github issue to get this fixed.


----

### OMRS-REPOSITORY-400-054

> The element {0} retrieved from repository {1} during the {2} operation has a null metadata collection id in its header: {3}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_INSTANCE_METADATA_COLLECTION_ID` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot process the request further because the element has an invalid header.

**User action**

This error suggests there is a logic error in either this repository, or the home repository for the instance.  Open a Github issue to get this fixed.


----

### OMRS-REPOSITORY-400-055

> An unexpected {0} exception was received from a repository connector during the {1} operation which had message: {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.UNEXPECTED_EXCEPTION_FROM_COHORT` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system can can not continue processing the request.

**User action**

This error suggests there is a logic error in either this repository, or the home repository for the instance.  Open up a Github issue to get this fixed.


----

### OMRS-REPOSITORY-400-056

> The OMRS repository connector operation {0} from the OMRS Enterprise Repository Services can not locate the home repository connector for instance {1} located in metadata collection {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NO_HOME_FOR_INSTANCE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot proceed with processing this request.

**User action**

This error suggests there is a logic error in either this repository, or the home repository for the instance.  Raise a Github issue in order to get this fixed.


----

### OMRS-REPOSITORY-400-057

> The OMRS repository connector operation {0} does not allow a null value for {1} from {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_AS_OF_TIME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system is unable continue processing the request because there is an error with the caller of the method.

**User action**

Correct the code in the caller's method and retry the request.


----

### OMRS-REPOSITORY-400-058

> An instance status of {0} has been passed as the {1} parameter on a {2} request to open metadata repository {3} however this status is not valid for an instance of type {4}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.BAD_DELETE_INSTANCE_STATUS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system cannot process the request because this status is only for use by the open metadata repository services (OMRS).

**User action**

The instance status is supplied by the caller to the API. This call needs to be changed to either a delete of purge request.


----

### OMRS-REPOSITORY-400-059

> Type definition with guid {0} and name {1} conflicts with an existing type definition in open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.VERIFY_CONFLICT_DETECTED` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot verify the supplied type definition because has different values from the type already defined in the repository.

**User action**

Review the supplied and stored types to locate the conflict and then ensure they are aligned by patching or deleting one of the type definitions.


----

### OMRS-REPOSITORY-400-060

> The repository helper method {0} has been called with a null parameter

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_PARAMETER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot process the request because it needs the parameter value.

**User action**

This is probably a logic error in Egeria. Raise a git issue to get this investigated and fixed.


----

### OMRS-REPOSITORY-400-061

> An invalid instance has been detected by repository helper method {0}.  The instance is {1}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INVALID_INSTANCE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot work with the supplied instance because key values are missing from its contents.

**User action**

This is probably a logic error in Egeria. Raise a GitHub issue to get this investigated and fixed.


----

### OMRS-REPOSITORY-400-062

> An unexpected {0} exception was caught by {1}; error message was {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system is not able to take action on the request.

**User action**

Review the error message and other diagnostics created at the same time.


----

### OMRS-REPOSITORY-400-063

> Method {0} cannot request a refresh of instance {1} as it is a local member of metadata collection {2} in repository {3}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.HOME_REFRESH` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system can not continue to action the request.

**User action**

Review the error messages and other diagnostics created at the same time.


----

### OMRS-REPOSITORY-400-064

> Method {0} cannot locate an instance with guid {1} in the archive

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.UNKNOWN_GUID` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot process the incoming request.

**User action**

Check the error message and other diagnostics created at the same time.


----

### OMRS-REPOSITORY-400-065

> Method {0} cannot accept the new type definition {1} from {2} because it has a header version of {3} which is greater than this repository can support ({4})

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.UNSUPPORTED_TYPE_HEADER_VERSION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system will not to process this request.

**User action**

The repository is sharing metadata with a repository of greater capability and the local repository cannot work with its types.  It may be time to upgrade the local repository.


----

### OMRS-REPOSITORY-400-066

> Method {0} cannot accept the new {1} instance from {2} with guid {3} and type {4} because it has a header version of {5} which is greater than this repository can support ({6})

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.UNSUPPORTED_INSTANCE_HEADER_VERSION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}` |

**System action**

The system cannot process the call.

**User action**

The repository is sharing metadata with a repository of higher capability and the local repository cannot work with its types.  It may be time to upgrade the local repository.


----

### OMRS-REPOSITORY-400-067

> Method {0} has detected invalid version values in TypeDef patch from {1}. The updateToVersion {2} is less than the applyToVersion {3}.  This is the contents of the patch {4}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INVALID_PATCH_VERSION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system cannot process the patch because it is invalid.

**User action**

Correct the source of the patch and then try reloading it.


----

### OMRS-REPOSITORY-400-068

> Method {0} has detected that a TypeDef patch from {1} is for a future level from the active TypeDef.  The applyToVersion is {2} and the active TypeDef version is {3}. This is the contents of the patch {4}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INCOMPATIBLE_PATCH_VERSION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system cannot process the patch because it is for a future version of the type.  This means there is at least one missing patch that needs to be applied first

**User action**

Locate and load the previous versions of the patch and then try reloading this one.


----

### OMRS-REPOSITORY-400-069

> Method {0} has detected that a TypeDef patch from {1} has the mandatory field {2} set to null which is invalid. This is the contents of the patch {3}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_MANDATORY_PATCH_FIELD` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot process the patch as it is invalid.

**User action**

Correct the source of the patch and then try loading it again.


----

### OMRS-REPOSITORY-400-070

> Method {0} has detected that a TypeDef patch from {1} attempts to change the type of property {2} from {3} to {4}. This is the contents of the patch {5}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INCOMPATIBLE_PROPERTY_PATCH` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The system cannot process the patch since it is invalid.

**User action**

Correct the source of the patch and then try reloading it again.


----

### OMRS-REPOSITORY-400-071

> The Open Metadata Repository Services (OMRS) has been called to initialize with no audit log destinations defined for server {0}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NO_AUDIT_LOG_DESTINATIONS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The local server cannot continue without an audit log.

**User action**

Add at least one audit log destination to the server configuration.


----

### OMRS-REPOSITORY-400-072

> The Open Metadata Repository Services (OMRS) has been called to initialize its subsystems for server {0} before the audit log is initialized

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_AUDIT_LOG` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The local server can not continue without an audit log.

**User action**

Review and correct the start up sequence of the server.


----

### OMRS-REPOSITORY-400-073

> An invalid instance was found in a batch of reference instances send by a remote member of the cohort. The exception was {0} with message {1}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INVALID_INSTANCES` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The instances that appear earlier in the batch have been processed.  However the server will not process any more of the batch in case there are other problems with it.

**User action**

Review the instances from the event (passed as additional information on this log message) to determine the source of the error and its resolution.


----

### OMRS-REPOSITORY-400-074

> An invalid list of property-based search conditions was provided: nestedConditions is mutually exclusive with property, operator, value

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INVALID_PROPERTY_SEARCH` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | none |

**System action**

The system cannot process the requested search because the provided options are mutually-exclusive.

**User action**

Review the request payload and provide only a nestedConditions or a property, operator, value payload for each property-based condition object in the list of conditions.


----

### OMRS-REPOSITORY-400-075

> An invalid list of classification-based search conditions was provided: name of the classification is mandatory

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INVALID_CLASSIFICATION_SEARCH` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | none |

**System action**

The system cannot process the requested search because a classification name was not provided.

**User action**

Review the request payload and provide at least a classification name for each classification-based condition object in the list of conditions.


----

### OMRS-REPOSITORY-400-077

> An invalid string was provided for the value of a LIKE operator

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INVALID_LIKE_CONDITION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | none |

**System action**

The system cannot process the requested search because only a string is permitted as the value for the LIKE operator.

**User action**

Review the request payload and ensure that a PrimitivePropertyValue of type OM\_PRIMITIVE\_TYPE\_STRING is provided when using the LIKE operator.


----

### OMRS-REPOSITORY-400-078

> An invalid string was provided for the value of the {0} operator

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INVALID_NUMERIC_CONDITION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot process the requested search because only a date or number is permitted as the value for the provided operator.

**User action**

Review the request payload and ensure that a PrimitivePropertyValue of a date or numeric type is provided when using the provided operator.


----

### OMRS-REPOSITORY-400-079

> The provided subtype {0} is not a subtype of typedef {1}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.TYPEDEF_NOT_SUBTYPE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot process the requested search because the specified subtype is not a known subtype of the provided type.

**User action**

Review the request payload and ensure that the list of subtypes includes only valid subtypes for the provided entity type.


----

### OMRS-REPOSITORY-400-080

> Classification {0} is not a supported classification type in open metadata repository {1}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.UNSUPPORTED_CLASSIFICATION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The supplied classification is valid.  However, the system cannot maintain the classification for an entity in this repository because it does not support the classification type.  The system will attempt to store the classification in another member of the cohort

**User action**

Ensure there is at least one repository in the cohort that supports this classification type.


----

### OMRS-REPOSITORY-400-081

> A {0} request has been made to repository {1} to add a classification {2} to entity {3} when this entity is already classified

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.ENTITY_ALREADY_CLASSIFIED` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot perform the request as only one classification of a specific type is permitted.

**User action**

Use the updateClassificationProperties to make changed to an existing classification.


----

### OMRS-REPOSITORY-400-082

> The OMRS repository connector operation {0} from the OMRS Enterprise Repository Services can not locate the home repository connector for classification {1} located in metadata collection {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NO_HOME_FOR_CLASSIFICATION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot proceed with processing this classification update request because it does not know which repository to call.

**User action**

This error suggests there is a logic error in either this repository, or the home repository for the classification. Note this may be different from the home repository for the entity.  Raise a Github issue in order to get this fixed.


----

### OMRS-REPOSITORY-400-083

> The OMRS repository connector operation {0} does not allow a time range from {1} to {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INVALID_TIME_RANGE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system is unable continue processing the request because the time range provided does not overlap.

**User action**

Correct the code in the caller's method (potentially just reverse the times) and retry the request.


----

### OMRS-REPOSITORY-400-084

> The value supplied for property {0} of type {1} contains a null (U+0000) character at position {2}; it was passed to method {3} on the {4} parameter

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_CHARACTER_IN_PROPERTY` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The repository rejects the request rather than storing the value.  A null character is not storable text: PostgreSQL refuses the whole statement with "null character not permitted", other databases silently truncate the value where the null falls, and a repository that does store it hands back something no other repository could hold.  The request is refused everywhere so that a value which cannot survive open metadata is never accepted by one repository and rejected by another.

**User action**

Remove the null character from the offending property value and retry the request.  A null character in a name, description or other text property is almost always a symptom of a fault further upstream - a C-style null-terminated string copied byte-for-byte, a fixed-width field padded with zero bytes, or binary content mislabelled as text - so it is worth correcting whatever produced the value rather than only the single property.


----

### OMRS-PROPERTIES-400-002

> No name provided for entity classification

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_CLASSIFICATION_PROPERTY_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | none |

**System action**

A classification with a null name is assigned to an entity.   This value should come from a metadata repository, and always be filled in.

**User action**

Look for other error messages to identify the source of the problem.  Identify the metadata repository where the asset came from.  Correct the cause of the error and then retry.


----

### OMRS-PROPERTIES-400-003

> Null property name passed to properties object

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_PROPERTY_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | none |

**System action**

A request to set an additional property failed because the property name passed was null

**User action**

Recode the call to the property object with a valid property name and retry.


----

### OMRS-PROPERTIES-400-004

> {0} cannot add a new element to location {1} of an array of size {2} value

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.ARRAY_OUT_OF_BOUNDS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

There is an error in the update of an ArrayPropertyValue.

**User action**

Recode the call to the property object with a valid element location and retry.


----

### OMRS-PROPERTIES-400-007

> Data type {0} is not supported by method {1}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.BAD_DATA_TYPE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

This method needs to be extended to support other data types.

**User action**

Update this method to include the requested type.


----

### OMRS-REST-CONNECTOR-400-001

> The connection passed in the cohort registration event does not contain the root URL for calling the server's REST API

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.REPOSITORY_URL_NULL` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | none |

**System action**

The system cannot connect to the open metadata repository to retrieve metadata.

**User action**

Retry the cohort registration when the connection configuration for this repository is corrected.  If the server is running in an OMAG platform then the configuration of the LocalRepositoryRemoteConnection needs correcting.


----

### OMRS-CONNECTOR-400-004

> The connection {0} passed to the EnterpriseOMRSRepositoryConnector is invalid

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INVALID_OMRS_CONNECTION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system is not able to populate the EnterpriseOMRSRepositoryConnector object because it needs the connection to identify the repository.

**User action**

Look for other error messages to identify what caused this error.  When the issue is fixed, retry the request.


----

### OMRS-CONNECTOR-400-005

> The connector to the local repository failed with a {0} exception and the following error message: {1}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.BAD_REAL_LOCAL_REPOSITORY_CONNECTOR` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The server fails to start.

**User action**

Correct the configuration to ensure that the local repository local connection is valid.


----

### OMRS-TOPIC-CONNECTOR-400-001

> Unable to send or receive events for source {0} because the connector to the OMRS Topic failed to initialize

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_TOPIC_CONNECTOR` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The local server will not connect to the cohort.

**User action**

The connection to the connector is configured in the server configuration.  Review previous error messages to determine the precise error in the start up configuration. Correct the configuration and reconnect the server to the cohort.


----

### OMRS-TOPIC-CONNECTOR-400-002

> The connector {0} has been configured without an embedded event bus connector

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NO_EVENT_BUS_CONNECTORS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

There is an error in the connection for the connector. The connection is defined in the server's configuration document.

**User action**

Review the configuration document and correct the definition of the connection.


----

### OMRS-COHORT-REGISTRY-404-001

> The Open Metadata Repository Cohort Registry Store for cohort {0} is not available

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_REGISTRY_STORE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot process registration requests from the open metadata repository cohort.

**User action**

Correct the configuration for the registry store connection in the server configuration. Retry the request when the registry store configuration is correct.


----

### OMRS-COHORT-REGISTRY-400-002

> The Open Metadata Repository Cohort {0} is not available to server {1} because the local metadata collection id has been changed from {2} to {3} since this server registered with the cohort

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INVALID_LOCAL_METADATA_COLLECTION_ID` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot connect with other members of the cohort while this incompatibility exists.

**User action**

If there is no reason for the change of local metadata collection id (this is the normal case) change the local metadata collection id back to its original valid in the server configuration. If the local metadata collection id must be changed (due to a conflict for example) then shutdown the server, restart it with no local repository configured and shut it down normally once the server has successfully unregistered with the cohort. Then re-establish the local repository configuration.Restart the server once the configuration is correct.


----

### OMRS-ARCHIVE-MANAGER-400-001

> An open metadata archive configured for server {0} is not accessible

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_ARCHIVE_STORE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot process the contents of this open metadata archive.  Other services may fail if they were dependent on this open metadata archive.

**User action**

Correct the configuration for the open metadata archive connection in the server configuration. Retry the request when the open metadata archive configuration is correct.


----

### OMRS-LOCAL-REPOSITORY-400-001

> The repository event mapper configured for the local repository for server {0} is not accessible

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_EVENT_MAPPER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot create the repository event mapper which means that events from the local repository will not be captured and processed.  Other services may fail if they were dependent on this event notification.

**User action**

Correct the configuration for the repository event mapper connection in the server configuration. Retry the request when the repository event mapper configuration is correct.


----

### OMRS-LOCAL-REPOSITORY-400-002

> The local repository is not able to re-home the instance {0} of type {1} ({2}) because it is not managing the repository with the requested home metadata collection of {3}.  This local repository is managing the {4} metadata collection

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NOT_FOR_LOCAL_COLLECTION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system can not continue processing the request.

**User action**

Retry the request on the repository with the requested metadata collection identifier or retry the request on this repository with the local metadata collection identifier.


----

### OMRS-ENTERPRISE-REPOSITORY-400-001

> Conflicting TypeDefs have been detected

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.CONFLICTING_ENTERPRISE_TYPEDEFS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | none |

**System action**

The system cannot create a reliable list of TypeDefs for the enterprise.

**User action**

Details of the conflicts and the steps necessary to repair the situation can be found in the audit log. Retry the request when the cohort configuration is correct.


----

### OMRS-ARCHIVE-BUILDER-400-001

> The same type {0} of category {1} has been added twice to an open metadata archive. First version was {2} and the second was {3}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.DUPLICATE_TYPE_IN_ARCHIVE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The build of the archive terminates.

**User action**

Verify the definition of the types being added to the archive. Once the definitions have been corrected, rerun the request.


----

### OMRS-ARCHIVE-BUILDER-400-002

> The {0} instance {1} has been added twice to an open metadata archive. First version was {2} and the second was {3}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.DUPLICATE_INSTANCE_IN_ARCHIVE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The build of the archive terminates immediately.

**User action**

Verify the definition of the instance being added to the archive. Once the definitions have been corrected, rerun the request.


----

### OMRS-ARCHIVE-BUILDER-400-003

> The same type name {0} has been added twice to an open metadata archive. First version was {1} and the second was {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.DUPLICATE_TYPENAME_IN_ARCHIVE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The build of the archive ends.

**User action**

Check the definition of the types being added to the archive. Once the definitions have been corrected, rerun the request.


----

### OMRS-ARCHIVE-BUILDER-400-004

> The guid {0} has been used twice to an open metadata archive. First version was {1} and the second was {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.DUPLICATE_GUID_IN_ARCHIVE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The build of the archive will terminate.

**User action**

Verify the definition of the elements being added to the archive. Once the definitions have been corrected, rerun the request.


----

### OMRS-ARCHIVE-BUILDER-400-005

> The type {0} of category {1} is not found in an open metadata archive

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.MISSING_TYPE_IN_ARCHIVE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The build of the archive now ends.

**User action**

Verify the definition of all the elements being added to the archive. Once the definitions have been corrected, rerun the request.


----

### OMRS-ARCHIVE-BUILDER-400-006

> A request for a type from category {0} passed a null name

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.MISSING_NAME_FOR_ARCHIVE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The build of the archive stops.

**User action**

Verify the definition of the elements being added to the archive. Once the definitions are corrected, rerun the request.


----

### OMRS-ARCHIVE-BUILDER-400-007

> RelationshipEndDef1 type {0} and EndDef1 name {1} in RelationshipDef {2} are incorrect, because another entity or relationship endDef is already using this attribute name

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.DUPLICATE_ENDDEF1_NAME_IN_ARCHIVE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The build of the archive exits.

**User action**

Verify the definition of the types being added to the archive. Once the definitions have been corrected, repeat the request.


----

### OMRS-ARCHIVE-BUILDER-400-008

> RelationshipEndDef2 type {0} and EndDef2 name {1} in RelationshipDef {2} are incorrect, because another entity or relationship endDef is already using this attribute name

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.DUPLICATE_ENDDEF2_NAME_IN_ARCHIVE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The archive build terminates.

**User action**

Verify the definition of the types being added to the archive. Once the definitions have been fixed, repeat the request.


----

### OMRS-ARCHIVE-BUILDER-400-009

> Duplicate attribute name {0} is defined in RelationshipDef {1}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.DUPLICATE_RELATIONSHIP_ATTR_IN_ARCHIVE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The archive build stops.

**User action**

Verify the definition of the types being added to the archive. Once the definitions have been fixed, rerun the request.


----

### OMRS-ARCHIVE-BUILDER-400-010

> Duplicate attribute name {0} is defined in EntityDef {1}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.DUPLICATE_ENTITY_ATTR_IN_ARCHIVE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The archive build will terminate.

**User action**

Verify the definition of the types being added to the archive. Once the definitions are corrected, rerun the request.


----

### OMRS-ARCHIVE-BUILDER-400-011

> Duplicate attribute name {0} is defined in ClassificationDef {1}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.DUPLICATE_CLASSIFICATION_ATTR_IN_ARCHIVE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The archive build will exit.

**User action**

Check the definition of the types being added to the archive. Once the definitions have been fixed, retry the request.


----

### OMRS-ARCHIVE-BUILDER-400-012

> Type name {0} is invalid because it contains a blank character

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.BLANK_TYPENAME_IN_ARCHIVE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The archive build has ended.

**User action**

Verify the definition of the types being added to the archive. Once the definitions are fixed, rerun the request.


----

### OMRS-ARCHIVE-BUILDER-400-013

> The archive builder has been passed an unknown type name {0}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.UNKNOWN_TYPENAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

There is an problem in the code that created the instance.

**User action**

Identify the code that called the archive builder and correct the type name.


----

### OMRS-AUDIT-LOG-400-002

> An Audit Log destination for server {0} is not correctly configured and a {1} exception occurred with message {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_AUDIT_LOG_STORE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot send diagnostic and audit information to one of the configured audit log destinations because the supplied connector failed to initialize.

**User action**

Correct the configuration for the audit log store connection in the server configuration. Retry the request when the audit log store configuration is correct.


----

### OMRS-AUDIT-LOG-400-003

> A null log record has been passed by the audit log to the audit log destination {0}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_LOG_RECORD` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The audit log destination throws an exception and the log record is not written to the audit log destination.

**User action**

This is probably an internal error in the audit log.  Raise a Github issue to get this fixed.


----

### OMRS-AUDIT-LOG-400-004

> A log record with a null originator has been passed by the audit log to the audit log destination {0}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_LOG_RECORD_ORIGINATOR` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The audit log destination throws an exception and the log record is not written out to the audit log destination.

**User action**

This is probably an internal error in the audit log.  Raise a Github issue to get this addressed.


----

### OMRS-AUDIT-LOG-400-005

> A log record with a null reporting component has been passed by the audit log to the audit log destination {0}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_LOG_RECORD_REPORTING_COMPONENT` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The audit log destination throws an exception and the log record is not written to the configured audit log destination.

**User action**

This is probably an internal error in the audit log.  Raise a Github issue to get this investigated.


----

### OMRS-AUDIT-LOG-400-006

> The Audit Log destination {0} is not able to support queries

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.CAN_NOT_QUERY_AUDIT_LOG_STORE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot process the query request and throws the FunctionNotSupportedException.

**User action**

If queries on the audit log are required, then add a new audit log destination that supports queries and restart the server.


----

### OMRS-AUDIT-LOG-400-008

> The Audit log destination {0} is not able to convert an audit log record to JSON format

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.AUDIT_LOG_RECORD_NOT_JSON_ENABLED` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot store the log record to this destination because it is not able to convert its contents into a suitable format.

**User action**

Investigate and correct the cause of the conversion failure.


----

### OMRS-AUDIT-LOG-400-009

> The archive manager is not active in server {0}.  Redirect the load request to a metadata access store

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.ARCHIVE_MANAGER_NOT_ACTIVE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot load an open metadata archive because the archive manager is not active in this server.

**User action**

Redirect the load request to a metadata access store.


----

### OMRS-REPOSITORY-404-001

> The open metadata repository connector for server {0} is not active and cannot service the {1} request

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.REPOSITORY_NOT_AVAILABLE` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot retrieve any metadata properties from this repository.

**User action**

Retry the request when the repository connector is active.


----

### OMRS-REPOSITORY-404-002

> The entity identified with guid {0} passed on the {1} call is not known to the open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.ENTITY_NOT_KNOWN` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot retrieve the properties for the requested entity because the supplied guid is not recognized.

**User action**

The guid is supplied by the caller to the server.  It may have a logic problem that has corrupted the guid, or the entity has been deleted since the guid was retrieved.


----

### OMRS-REPOSITORY-404-003

> The relationship identified with guid {0} passed on the {1} call is not known to the open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.RELATIONSHIP_NOT_KNOWN` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot process the request for the requested relationship because the supplied guid is not recognized.

**User action**

The guid is supplied by the caller to the OMRS.  It may have a logic problem that has corrupted the guid, or the relationship has been deleted since the guid was retrieved.  It is necessary to understand the logic of the caller to determine if this is a problem.


----

### OMRS-REPOSITORY-404-004

> The TypeDef {0} (guid = {1}) passed on the {2} parameter of the {3} operation is not known to the open metadata repository {4}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.TYPEDEF_NOT_KNOWN` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The system cannot retrieve the properties for the requested TypeDef because the supplied identifier is not recognized.

**User action**

The identifier is supplied by the caller.  It may have a logic problem that has corrupted the identifier, or the typedef has been deleted since the identifier was retrieved.


----

### OMRS-REPOSITORY-404-005

> The TypeDef {0} of category {1} passed by the {2} operation is not known to the open metadata repository {3}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.TYPEDEF_NOT_KNOWN_FOR_INSTANCE` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot retrieve the properties for the requested TypeDef since the supplied identifier is not recognized.

**User action**

The identifier is supplied by the caller.  It may have a logic problem that has corrupted the identifier, or the typedef may have been deleted since the identifier was retrieved.


----

### OMRS-REPOSITORY-404-007

> The TypeDef unique identifier {0} passed as parameter {1} on a {2} request to open metadata repository {3} is not known to this repository

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.TYPEDEF_ID_NOT_KNOWN` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot retrieve the properties for the requested TypeDef because the supplied identifiers have not been recognized.

**User action**

The identifier is supplied by the caller.  It may have a logic defect that has corrupted the identifier, or the TypeDef has been deleted since the identifier was retrieved.


----

### OMRS-REPOSITORY-404-009

> The TypeDef unique name {0} passed on a {1} request to open metadata repository {2} is not known to this repository

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.TYPEDEF_NAME_NOT_KNOWN` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot retrieve the properties for the requested TypeDef because the supplied identifiers are not recognized.

**User action**

The identifier is supplied by the caller.  It may have a logic problem that has corrupted the identifier, or the TypeDef has been deleted since the identifier was retrieved.


----

### OMRS-REPOSITORY-404-011

> The relationship identified with guid {0} passed on the {1} call is not found to the open metadata repository {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.RELATIONSHIP_NOT_FOUND` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot retrieve the properties for the requested relationship because the supplied guid is not recognized.

**User action**

The guid is supplied by the caller to the OMRS.  It may have a logic problem that has corrupted the guid, or the relationship has been deleted since the guid was retrieved.


----

### OMRS-REPOSITORY-404-012

> The {0} relationship identified with guid {1} passed on the {2} call is soft-deleted in the open metadata repository {3}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.RELATIONSHIP_SOFT_DELETED` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot retrieve the properties for the requested relationship because the supplied guid is for a relationship that has already been deleted.

**User action**

The guid is supplied by the caller to the OMRS.  It is most likely to be a timing issue where the relationship was deleted by another process since the guid was retrieved.  However, there is a  possibility of a logic problem that has corrupted the guid.


----

### OMRS-REPOSITORY-404-013

> The {0} entity identified with guid {1} passed on the {2} call is soft-deleted in the open metadata repository {3}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.ENTITY_SOFT_DELETED` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot retrieve the properties for the requested entity because the supplied guid is for a entity that has already been deleted.

**User action**

The guid is supplied by the caller to the OMRS.  It is most likely to be a timing issue where the entity was deleted by another process since the guid was retrieved.  However, there is a  possibility of a logic problem that has corrupted the guid.


----

### OMRS-REPOSITORY-CONNECTOR-404-002

> The Open Metadata Repository Servers in the cohort are not available

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.COHORT_NOT_CONNECTED` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | none |

**System action**

The system is not able to retrieve any metadata properties from this repository.

**User action**

Repeat the request when the repository server is available.


----

### OMRS-REPOSITORY-CONNECTOR-404-003

> The open metadata repository servers in the cohort are not configured correctly

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INVALID_COHORT_CONFIG` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | none |

**System action**

The underlying cause of this error is recorded in previous exceptions.

**User action**

Review the other error messages to determine the source of the error.  When these are resolved, retry the request.


----

### OMRS-METADATA-HIGHWAY-404-002

> The local server cannot disconnect from an open metadata repository cohort {0}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.COHORT_DISCONNECT_FAILED` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}` |

**System action**

The underlying cause of this error is reported in previous exceptions.

**User action**

Check the other error messages to determine the source of the error.  When these are resolved, retry the request.


----

### OMRS-METADATA-HIGHWAY-404-003

> There are more than one cohort configurations with the same name of {0}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.DUPLICATE_COHORT_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot connect to more than one cohort with the same name.

**User action**

Correct the configuration for the cohorts in the server configuration. Retry the request when the cohort configuration is correct.


----

### OMRS-REST-REPOSITORY-CONNECTOR-404-001

> A call to the {0} of the open metadata repository server {1} results in an exception {2} with message {3}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.REMOTE_REPOSITORY_ERROR` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system is not able to retrieve metadata properties from this repository.

**User action**

Retry the request when the repository server is available.


----

### OMRS-METADATA-HIGHWAY-404-004

> The local server cannot initiate a connection to the cohort {0} when starting up

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.COHORT_STARTUP_ERROR` |
| **HTTP error code** | 404 - Not Found - the requested element does not exist |
| **Message inserts** | `{0}` |

**System action**

The server will now cancel startup, and shutdown.

**User action**

Check the other error messages to determine the source of the error. When these are resolved, retry the request.


----

### OMRS-ENTERPRISE-REPOSITORY-CONNECTOR-405-001

> The requested method {0} is not supported by the EnterpriseOMRSRepositoryConnector

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.ENTERPRISE_NOT_SUPPORTED` |
| **HTTP error code** | 405 - Method Not Allowed - this operation is not supported for this element |
| **Message inserts** | `{0}` |

**System action**

The system is not able to process the requested method because it is not supported by the Open Metadata Repository Services (OMRS) Enterprise Repository Services.

**User action**

Correct the application that called this method.


----

### OMRS-METADATA-COLLECTION-409-001

> Multiple instances of type {0} have been returned to {2} of service {1} when there should be one at most.  These are examples of the entities returned: {3}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.MULTIPLE_ENTITIES_FOUND` |
| **HTTP error code** | 409 - Conflict - the request clashes with the current state of the metadata |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The type model defines certain properties as unique.  This means only one instance of this type with that property value should be returned.

**User action**

Investigate why multiple instances exist and either delete the duplicates, or change the values in it so the unique properties are unique.


----

### OMRS-METADATA-COLLECTION-409-002

> Multiple instances of type {0} have been returned to {2} of service {1} when there should be a maximum of one.  These are examples of the entities returned: {3}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.MULTIPLE_RELATIONSHIPS_FOUND` |
| **HTTP error code** | 409 - Conflict - the request clashes with the current state of the metadata |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The type model defines how many relationships are allowed to connect with a specific entity instance.  This is an example of where the limit has been exceeded.

**User action**

Investigate why multiple instances exist and delete the duplicates.


----

### OMRS-METADATA-COLLECTION-500-001

> The Java class {0} for PrimitiveDefCategory {1} is not known

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INVALID_PRIMITIVE_CLASS_NAME` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}` |

**System action**

There is an internal error in Java class PrimitiveDefCategory as it has been set up with an invalid class.

**User action**

Raise a Github issue to get this fixed.


----

### OMRS-METADATA-COLLECTION-500-002

> The primitive value should be stored in Java class {0} rather than {1} since it is of PrimitiveDefCategory {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INVALID_PRIMITIVE_VALUE` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

There is an internal error in the creation of a PrimitiveTypeValue.

**User action**

Open an issue on GitHub to get this addressed.


----

### OMRS-METADATA-COLLECTION-500-003

> There is a problem in the definition of primitive type {0}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INVALID_PRIMITIVE_CATEGORY` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}` |

**System action**

There is an internal error during the creation of a PrimitiveTypeValue.

**User action**

Open a Github issue to get this looked into.


----

### OMRS-METADATA-COLLECTION-500-004

> Null home metadata collection identifier found by method {1} in property {0} from open metadata repository {3}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_HOME_METADATA_COLLECTION_ID` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{3}` |

**System action**

A request to retrieve a metadata instance (entity or relationship) has encountered a homeless metadata instance.

**User action**

Locate the open metadata repository that supplied the instance and correct the logic in its OMRSRepositoryConnector.


----

### OMRS-METADATA-COLLECTION-500-006

> The open metadata repository connector {0} has been initialized with a null metadata collection identifier

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_METADATA_COLLECTION_ID` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}` |

**System action**

There is an internal error in the OMRS initialization.

**User action**

Raise a Github issue to get this investigated and fixed.


----

### OMRS-METADATA-COLLECTION-500-009

> Unable to complete operation {0} to open metadata repository {1} because the repository connector is null

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NO_REPOSITORY_CONNECTOR_FOR_COLLECTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}` |

**System action**

There is an internal issue in the OMRS initialization.

**User action**

Raise a Github issue on Egeria to get this fixed.


----

### OMRS-METADATA-COLLECTION-500-010

> Unable to complete operation {0} to open metadata repository {1} because the repository validator is null

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NO_REPOSITORY_VALIDATOR_FOR_COLLECTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}` |

**System action**

There is an internal logic  error in the OMRS initialization.

**User action**

Raise a Github issue on Egeria to get this addressed.


----

### OMRS-METADATA-COLLECTION-500-011

> Unable to complete operation {0} to open metadata repository {1} as the repository connector is null

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NO_REPOSITORY_HELPER_FOR_COLLECTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}` |

**System action**

There is an internal logic problem in the OMRS initialization.

**User action**

Raise a Github issue on Egeria to get this investigated and fixed.


----

### OMRS-METADATA-COLLECTION-500-012

> Open metadata repository {0} has encountered an unexpected exception during the {1} operation.  The full message was {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.REPOSITORY_LOGIC_ERROR` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

There is an internal error within the OMRS repository connector.

**User action**

Open a Github issue against Egeria to get this fixed.


----

### OMRS-METADATA-COLLECTION-500-013

> During the {0} operation, open metadata repository {1} retrieved an instance from its metadata store that has a null type

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_INSTANCE_TYPE` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}` |

**System action**

There is an internal issue in the OMRS repository connector.

**User action**

Open a Github issue on Egeria to get this looked into.


----

### OMRS-METADATA-COLLECTION-500-014

> During the {0} operation, open metadata repository {1} retrieved an instance (guid={2}) from its metadata store that has an inactive type called {3} (type guid = {4})

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INACTIVE_INSTANCE_TYPE` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

There is an internal problem in the OMRS repository connector code.

**User action**

Report as a Github issue to get this fixed.


----

### OMRS-METADATA-COLLECTION-500-015

> The value supplied for an attribute of PrimitiveDefCategory {0} is expected as Java class {1} but was supplied as Java class {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INVALID_PRIMITIVE_TYPE` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

There is an internal error - code that sets a primitive property value is using an incorrect Java class.

**User action**

Report as a Github issue to get this addressed.


----

### OMRS-METADATA-COLLECTION-500-016

> The home metadata collection identifier {0} found by method {1} for instance with GUID {2} is not the metadata collection identifier {3} for the local metadata repository {4}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INSTANCE_HOME_NOT_LOCAL` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

A request to update a metadata instance (entity or relationship) has been encountered on a reference copy metadata instance.

**User action**

Locate the open metadata repository that has the home instance and perform the update at that repository.


----

### OMRS-METADATA-COLLECTION-500-017

> The home metadata collection identifier {0} found by method {1} for instance with GUID {2} is the metadata collection identifier {3} for the local metadata repository {4}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.INSTANCE_HOME_IS_LOCAL` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

A rehome request to update a metadata instance (entity or relationship) metadata collection id has been encountered on a local metadata instance.  This request should be issues on the new home repository.

**User action**

Locate the open metadata repository that is to be the new home of the instance and perform the rehome at that repository.


----

### OMRS-METADATA-COLLECTION-500-018

> The open metadata repository connector {0} has returned a null metadata collection identifier

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_METADATA_COLLECTION_ID_FROM_REMOTE` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}` |

**System action**

There is an internal error in the remote repository.

**User action**

Determine the source of the implementation of the remote repository and request help from its developers.


----

### OMRS-COHORT-MANAGER-500-001

> OMRSCohortManager has been initialized with a null cohort name

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_COHORT_NAME` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | none |

**System action**

There is an internal error within the OMRS initialization.

**User action**

Report as a Github issue in order to get this fixed.


----

### OMRS-OPERATIONAL-SERVICES-500-001

> No configuration has been passed to the Open Metadata Repository Services (OMRS) on initialization os server {0}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_CONFIG` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}` |

**System action**

here is an internal issue in the OMRS initialization.

**User action**

Report as a Github issue in order to get this looked in to.


----

### OMRS-LOCAL-REPOSITORY-500-001

> The local repository services have been initialized with a null real metadata collection.

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_LOCAL_METADATA_COLLECTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | none |

**System action**

There is an internal problem in the OMRS initialization.

**User action**

Report as a Github issue to get this corrected.


----

### OMRS-LOCAL-REPOSITORY-500-002

> The local repository for server {0} failed to initialize and returned a {1} exception with message {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.LOCAL_REPOSITORY_FAILED_TO_START` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

There is an internal problem in the local repositories initialization.

**User action**

Validate and correct wither the configuration or the implementation of the local repository connector.


----

### OMRS-ENTERPRISE-REPOSITORY-500-001

> The enterprise repository services has detected a repository connector with a null metadata collection

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_ENTERPRISE_METADATA_COLLECTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | none |

**System action**

There is an internal error in the Open Metadata Repository Services (OMRS) operation.

**User action**

Open an issue on GitHub to get this fixed.


----

### OMRS-CONTENT-MANAGER-500-001

> The repository content manager method {0} has detected an unknown TypeDef {1} from {2} on behalf of method {3}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.BAD_TYPEDEF` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

There is an internal error within the Open Metadata Repository Services (OMRS) operation.

**User action**

Open up a Github issue to get this fixed.


----

### OMRS-CONTENT-MANAGER-500-002

> The repository content manager has detected an invalid attribute name in a TypeDef from {0}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.BAD_TYPEDEF_ATTRIBUTE_NAME` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}` |

**System action**

There is an internal problem within the Open Metadata Repository Services (OMRS) operation.

**User action**

Open up a Github issue to get this investigated.


----

### OMRS-CONTENT-MANAGER-500-003

> The repository content manager has detected a null attribute in a TypeDef from {0}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_TYPEDEF_ATTRIBUTE` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}` |

**System action**

There is an internal error in the Open Metadata Repository Services (OMRS) code.

**User action**

Open up a Github issue to get this investigated and fixed.


----

### OMRS-CONTENT-MANAGER-500-004

> Source {0} has requested type {1} with an incompatible category of {2} from repository content manager

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.BAD_CATEGORY_FOR_TYPEDEF_ATTRIBUTE` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

There is an error in the Open Metadata Repository Services (OMRS) operation, probably in the source component.

**User action**

Raise a Github issue so that this can be fixed.


----

### OMRS-CONTENT-MANAGER-500-005

> The repository content manager has detected an unknown TypeDef {0} ({1}) from {2}. It was passed to method {3} via parameters {4} and {5}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.UNKNOWN_TYPEDEF` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

There is an internal problem in the Open Metadata Ecosystem code or its callers because an invalid unique identifier, or name of a type has been passed to the Open Metadata Repository Services (OMRS).

**User action**

Trace the caller of the request to determine where the type information was specified.  If the error is in the Egeria code, or you need help from the community, raise a Github issue so this can be addressed.


----

### OMRS-CONTENT-MANAGER-500-006

> The repository content manager has received an instance {0} of class {1} with an open metadata type name of {2}, which is from category {3}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.WRONG_TYPEDEF_CATEGORY` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The local repository has received an instance either from an Open Metadata Archive, or another member of one of its Open Metadata Repository Cohorts, that is using a type from a different category of instance.

**User action**

Trace the caller of the request to determine where the came from and correct the source.


----

### OMRS-OPEN-METADATA-ARCHIVE-500-001

> The archive builder failed to initialize

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.ARCHIVE_UNAVAILABLE` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | none |

**System action**

There is an internal error in the archive building process.

**User action**

Raise a Github issue this can be investigated.


----

### OMRS-EVENT-MANAGEMENT-500-001

> A null exchange rule has been passed to one of the event management components on method {0} for cohort {1}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_EXCHANGE_RULE` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}` |

**System action**

There is an internal error within the OMRS initialization code.

**User action**

Open a Github issue so this can be fixed.


----

### OMRS-EVENT-MANAGEMENT-500-002

> A null repository validator has been passed to one of the event management components

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_REPOSITORY_VALIDATOR` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | none |

**System action**

There is an internal error in the OMRS initialization code.

**User action**

Open a Github issue to get this looked in to.


----

### OMRS-EVENT-MANAGEMENT-500-003

> A null repository helper has been passed to one of the event management components

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_REPOSITORY_HELPER` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | none |

**System action**

There is an internal problem in the OMRS initialization code.

**User action**

Open a Github issue to get this checked and fixed.


----

### OMRS-EVENT-MANAGEMENT-500-004

> A null event has been passed to one of the event management components

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_OUTBOUND_EVENT` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | none |

**System action**

There is an internal defect in the OMRS initialization.

**User action**

Raise an issue on Github so that this can be fixed.


----

### OMRS-REST-REPOSITORY-CONNECTOR-500-001

> A remote open metadata repository {0} returned a metadata collection identifier of {1} on its REST API after it registered with the cohort using a metadata collection identifier of {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.METADATA_COLLECTION_ID_MISMATCH` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

There is a configuration error in the remote open metadata repository.

**User action**

Review the set up of the remote repository.  Has it be reconfigured and changed its metadata collection id? It may be that the server-url-root parameter is incorrectly set and is clashing with the setting in another server registered with the same cohort.


----

### OMRS-METADATA-TOPIC-CONNECTOR-500-001

> A null topic listener has been passed to the {0} open metadata topic connector {1}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_OPEN_METADATA_TOPIC_LISTENER` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}` |

**System action**

There is an internal error in the open metadata repository.

**User action**

Report this to the Egeria team via a GitHub issue so that it can be investigated.


----

### OMRS-TOPIC-CONNECTOR-500-003

> Connector {0} cannot send a null event

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.OMRS_TOPIC_SEND_NULL_EVENT` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}` |

**System action**

There is an internal problem in the open metadata repository code logic.

**User action**

Report this to the Egeria team via a GitHub issue so that it can be checked and fixed.


----

### OMRS-TOPIC-CONNECTOR-500-006

> The requested connector for connection named {0} has not been created.  The connection was provided by the {1} service running in OMAG Server at {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_CONNECTOR_RETURNED` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot create a connector which means some of its services will not work.

**User action**

This problem is likely to be caused by an incorrect connection object.  Check the settings on the remoteEnterpriseTopicConnection in the server configuration and correct if necessary.  If the connection is correct, contact the Egeria community for help.


----

### OMRS-TOPIC-CONNECTOR-500-007

> The connector generated from the connection named {0} return by the {1} service running in OMAG Server at {2} is not of the required type. It should be an instance of {3}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.WRONG_TYPE_OF_CONNECTOR` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot create the required connector which means some of its services will not work.

**User action**

Verify that the OMAG server is running and the OMAS service is correctly configured.


----

### OMRS-METADATA-COLLECTION-501-001

> OMRSMetadataInstanceStore method {0} for OMRS Connector {1} to repository type {2} is not implemented

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.METHOD_NOT_IMPLEMENTED` |
| **HTTP error code** | 501 - Not Implemented - this function is not implemented by the called component |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

A method in MetadataCollectionBase was called which means that the connector's OMRSMetadataInstanceStore (a subclass of MetadataCollectionBase) does not have a complete implementation.

**User action**

Report this to the Egeria team via a GitHub issue so that it can be addressed.


----

### OMRS-METADATA-COLLECTION-501-002

> Repository {0} is not able to support the {1} type

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.TYPE_NOT_IMPLEMENTED` |
| **HTTP error code** | 501 - Not Implemented - this function is not implemented by the called component |
| **Message inserts** | `{0}`, `{1}` |

**System action**

This repository has a fixed set of types that is can support.

**User action**

No action required, this is a limitation of the technology.


----

### OMRS-TOPIC-CONNECTOR-501-001

> Connector {0} is not able to support event protocol {1}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.OMRS_UNSUPPORTED_EVENT_PROTOCOL` |
| **HTTP error code** | 501 - Not Implemented - this function is not implemented by the called component |
| **Message inserts** | `{0}`, `{1}` |

**System action**

This server does not support the requested event protocol level.

**User action**

The protocol level is set in the configuration.  The admin services should not allow a protocol level that is not supported by its local OMRS. Raise a Github issue to get this fixed.


----

### OMRS-ENTERPRISE-REPOSITORY-503-001

> There are no open metadata repositories available for access service {0}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NO_REPOSITORIES` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | `{0}` |

**System action**

The configuration for the server is set up so there is no local repository and no remote repositories connected through the open metadata repository cohorts.  This may because of one or more configuration errors.

**User action**

Retry the request once the configuration is changed.


----

### OMRS-ENTERPRISE-REPOSITORY-503-003

> The enterprise repository services has detected a repository connector from cohort {0} for metadata collection identifier {1} that has a null metadata collection API object

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_COHORT_METADATA_COLLECTION` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | `{0}`, `{1}` |

**System action**

There is an internal error in the OMRS Repository Connector implementation.

**User action**

Raise a Github issue on the Egeria project to get this fixed.


----

### OMRS-LOCAL-REPOSITORY-503-003

> The connection to the local open metadata repository server is not configured correctly

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.LOCAL_REPOSITORY_CONFIGURATION_ERROR` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | none |

**System action**

The root cause of this error is recorded in previous exceptions.

**User action**

Review the other error messages to determine the location of the error.  When these are resolved, retry the request.


----

### OMRS-LOCAL-REPOSITORY-503-004

> The connection to the local open metadata repository server has not been configured correctly

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.BAD_LOCAL_REPOSITORY_CONNECTION` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | none |

**System action**

The root cause of this error is recorded in prior exceptions.

**User action**

Review the other error messages to determine the source of the error.  When these are fixed, retry the request.


----

### OMRS-LOCAL-REPOSITORY-503-005

> An OMRS repository connector or access service {0} has passed an invalid parameter to the repository validator {1} operation as part of the {2} request

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.VALIDATION_LOGIC_ERROR` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The open metadata component has called the repository validator operations in the wrong order or has a similar logic error.

**User action**

Raise a Github issue on the Egeria project to get this investigated and fixed.


----

### OMRS-LOCAL-REPOSITORY-503-006

> An OMRS repository connector {0} has passed an invalid parameter to the repository content manager {1} operation as part of the {2} request

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.CONTENT_MANAGER_LOGIC_ERROR` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The open metadata component has called the repository helper operations in the wrong order or has a similar logic error.

**User action**

Raise a Github issue so that this may be fixed.


----

### OMRS-LOCAL-REPOSITORY-503-007

> The local OMRS repository connector {0} hosts the home metadata collection for entity {1} but only has an entity proxy stored.  It is not able to complete the {2} request

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.ENTITY_PROXY_IN_HOME` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

There is a logic error either in the EnterpriseOMRSRepositoryConnector causing an update request to be routed to the wrong repository, or there is an error in the local repository.

**User action**

Open a Github issue so that this can be fixed.


----

### OMRS-LOCAL-REPOSITORY-503-008

> An OMRS repository connector or access server {0} has passed a null classification to the repository helper {1} operation as part of the {2} request

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_CLASSIFICATION_CREATED` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The repository connector has called the repository helper operations in the wrong order or has a similar logic error.

**User action**

Raise a Github issue in order for this to be addressed by the Egeria team.


----

### OMRS-LOCAL-REPOSITORY-503-009

> The local OMRS repository connector {0} has been asked to update entity {1} but it is not the owner.It is not able to complete the {2} request

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.ENTITY_CAN_NOT_BE_UPDATED` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

There is a logic issue either in the EnterpriseOMRSRepositoryConnector causing an update request to be routed to the wrong repository, or there is an error in the local repository.

**User action**

Open a Github issue in order for this to be addressed by the Egeria team.


----

### OMRS-LOCAL-REPOSITORY-503-010

> The local OMRS repository connector {0} has been asked to update relationship {1} but it is not the owner.It is not able to complete the {2} request

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.RELATIONSHIP_CAN_NOT_BE_UPDATED` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

There is a logic problem either in the EnterpriseOMRSRepositoryConnector causing an update request to be routed to the wrong repository, or there is an error in the local repository.

**User action**

Open up a Github issue in order for this to be addressed by the Egeria team.


----

### OMRS-LOCAL-REPOSITORY-503-011

> The local OMRS repository connector {0} requested an instance {1} from the real metadata collection but a null was returned.It is not able to complete the {2} request

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_INSTANCE` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

There is probably a logic error in the repository connector for the real repository because it should have thrown an exception rather than return null.

**User action**

Raise an issue with the supplier of the real repository connector to get this fixed.


----

### OMRS-REPOSITORY-HELPER-503-001

> A caller {0} has passed an invalid parameter to the repository helper {1} operation as part of the {2} request

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.HELPER_LOGIC_ERROR` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The open metadata component has invoked the repository helper operations in the wrong order or has a similar logic error.

**User action**

Open up a Github issue on Egeria in order for this to be addressed by the Egeria team.


----

### OMRS-REPOSITORY-HELPER-503-002

> A caller {0} has passed an invalid parameter to the repository helper {1} operation as part of the {2} request resulting in an unexpected exception {3} with message {4}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.HELPER_LOGIC_EXCEPTION` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The open metadata component has invoked the repository helper operations in the wrong sequence or has a similar logic error.

**User action**

Open up a Github issue on Egeria in order for this to be fixed by the Egeria team.


----

### OMRS-REST-API-503-001

> There is no local repository to support REST API call {0}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NO_LOCAL_REPOSITORY` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | `{0}` |

**System action**

The server has received a call on its open metadata repository REST API services but cannot process it because the local repository is not active.

**User action**

Ensure that the open metadata services have been activated in the server. If they are active and the server is supposed to have a local repository, correct the server's configuration document to include a local repository and restart the server.


----

### OMRS-REST-API-503-002

> There is no enterprise repository to support REST API call {0}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NO_ENTERPRISE_REPOSITORY` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | `{0}` |

**System action**

The server has received a call on its open metadata enterprise repository REST API services but cannot process it because the enterprise repository services are not active.

**User action**

Ensure that the enterprise repository services have been activated in the server. If they are active and the server is supposed to have the enterprise repository services, correct the server's configuration document to include these services and restart the server.


----

### OMRS-REST-API-503-004

> A null response was received from REST API call {0} to repository {1}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NULL_RESPONSE_FROM_API` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The server has issued a call to the open metadata repository REST API services in a remote repository and has received a null response.

**User action**

Look for errors in the remote repository's audit log and console to understand and correct the source of the error.


----

### OMRS-REST-API-503-005

> Unable to create REST Client for repository {0}.  The error message was {1}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.NO_REST_CLIENT` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The server has issued a call to the open metadata repository REST API services in a remote repository and has received an exception from the local client libraries.

**User action**

Look for errors in the local repository's audit log and console to understand and correct the source of the error.


----

### OMRS-REST-API-503-006

> A client-side exception was received from API call {0} to repository {1}.  The error message was {2}

|  |  |
|---|---|
| **Java constant** | `OMRSErrorCode.CLIENT_SIDE_REST_API_ERROR` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The server has invoked a call on the open metadata repository REST API services in a remote repository and has received an exception from the local client libraries.

**User action**

Look for errors in the local repository's audit log and console to identify and correct the source of the error.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.

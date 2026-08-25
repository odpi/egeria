<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OMRSAuditCode

The OMRSAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 127 |
| **Message identifiers begin** | `OMRS-AUDIT-` |
| **Java class** | `org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode` |
| **Module** | [open-metadata-implementation/repository-services/repository-services-apis](../../open-metadata-implementation/repository-services/repository-services-apis) |
| **Source** | [OMRSAuditCode.java](../../open-metadata-implementation/repository-services/repository-services-apis/src/main/java/org/odpi/openmetadata/repositoryservices/ffdc/OMRSAuditCode.java) |
| **Further reading** | <https://egeria-project.org/services/omrs/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OMRS-AUDIT-0001](#omrs-audit-0001) | STARTUP | The Open Metadata Repository Services (OMRS) is initializing the subsystems to support a new server |
| [OMRS-AUDIT-0002](#omrs-audit-0002) | STARTUP | Enterprise access through the Enterprise Repository Services is initializing |
| [OMRS-AUDIT-0003](#omrs-audit-0003) | STARTUP | The local repository is initializing the metadata collection named {0} with an id of {1} |
| [OMRS-AUDIT-0004](#omrs-audit-0004) | STARTUP | Connecting to the metadata highway |
| [OMRS-AUDIT-0005](#omrs-audit-0005) | STARTUP | Connecting to open metadata repository cohort {0} |
| [OMRS-AUDIT-0006](#omrs-audit-0006) | EXCEPTION | Configuration error detected while connecting to cohort {0}, exception {1} was caught with error message: {2} |
| [OMRS-AUDIT-0007](#omrs-audit-0007) | STARTUP | The Open Metadata Repository Services (OMRS) has initialized |
| [OMRS-AUDIT-0008](#omrs-audit-0008) | SHUTDOWN | Disconnecting from cohort {0} |
| [OMRS-AUDIT-0009](#omrs-audit-0009) | SHUTDOWN | Unregistering from cohort {0} |
| [OMRS-AUDIT-0010](#omrs-audit-0010) | SHUTDOWN | The Open Metadata Repository Services (OMRS) is disconnecting the open metadata repositories |
| [OMRS-AUDIT-0011](#omrs-audit-0011) | SHUTDOWN | The Open Metadata Repository Services (OMRS) has disconnected from the open metadata repositories |
| [OMRS-AUDIT-0013](#omrs-audit-0013) | EXCEPTION | Unable to start processing in the local repository due to error {0} |
| [OMRS-AUDIT-0015](#omrs-audit-0015) | STARTUP | The listener thread for an OMRS Topic Connector for topic {0} has started |
| [OMRS-AUDIT-0016](#omrs-audit-0016) | SHUTDOWN | The listener thread for the OMRS Topic Connector for topic {0} has shutdown |
| [OMRS-AUDIT-0018](#omrs-audit-0018) | ERROR | The OMRS Topic Listener has no embedded connector to an event bus |
| [OMRS-AUDIT-0019](#omrs-audit-0019) | STARTUP | An OMRS Topic Connector has registered with an event bus connector for topic {0} |
| [OMRS-AUDIT-0020](#omrs-audit-0020) | STARTUP | An OMRS Topic Connector is ready to send and receive events on topic {0} |
| [OMRS-AUDIT-0021](#omrs-audit-0021) | SHUTDOWN | The OMRS Topic Listener has disconnected the event bus connectors for topic {0} |
| [OMRS-AUDIT-0024](#omrs-audit-0024) | STARTUP | The local repository event mapper connector {0} is ready to send and receive events |
| [OMRS-AUDIT-0025](#omrs-audit-0025) | SHUTDOWN | The local repository event mapper connector {0} has disconnected the event bus connectors |
| [OMRS-AUDIT-0026](#omrs-audit-0026) | STARTUP | Initializing listener for cohort {0} |
| [OMRS-AUDIT-0027](#omrs-audit-0027) | EXCEPTION | The local server cannot initiate a connection to the cohort {0} when starting up, exception {1} was caught with error message: {2} |
| [OMRS-AUDIT-0028](#omrs-audit-0028) | INFO | A reference instance has been passed to the local repository on the {0} parameter of the {1} method which has the local repository metadata collection id {2} as its home |
| [OMRS-AUDIT-0029](#omrs-audit-0029) | STARTUP | The {0} event manager is initializing |
| [OMRS-AUDIT-0030](#omrs-audit-0030) | STARTUP | Registering the {0} event consumer with the {1} event manager |
| [OMRS-AUDIT-0031](#omrs-audit-0031) | STARTUP | The {0} event manager is starting with {1} type definition event consumer(s) and {2} instance event consumer(s) |
| [OMRS-AUDIT-0032](#omrs-audit-0032) | STARTUP | The {0} event manager is sending out the {1} type definition events that were generated and buffered during server initialization |
| [OMRS-AUDIT-0033](#omrs-audit-0033) | INFO | The {0} event manager is sending out the {1} instance events that were generated and buffered during server initialization |
| [OMRS-AUDIT-0034](#omrs-audit-0034) | EXCEPTION | The connector to the local repository failed with a {0} exception and the following error message: {1} |
| [OMRS-AUDIT-0035](#omrs-audit-0035) | EXCEPTION | The connector to the local repository event mapper failed with a {0} exception and the following error message: {1} |
| [OMRS-AUDIT-0036](#omrs-audit-0036) | EXCEPTION | The connector to the local repository archive store failed with a {0} exception and the following error message: {1} |
| [OMRS-AUDIT-0038](#omrs-audit-0038) | EXCEPTION | The connector to the {0} topic failed with a {1} exception and the following error message: {2} |
| [OMRS-AUDIT-0039](#omrs-audit-0039) | INFO | Ignoring duplicate request to classify {0} entity {1} with classification {2} in the {3} repository |
| [OMRS-AUDIT-0040](#omrs-audit-0040) | STARTUP | An enterprise OMRS connector has been created for the {0} |
| [OMRS-AUDIT-0041](#omrs-audit-0041) | STARTUP | The enterprise OMRS connector for the {0} has started |
| [OMRS-AUDIT-0042](#omrs-audit-0042) | SHUTDOWN | The enterprise OMRS Connector for the {0} has shutdown |
| [OMRS-AUDIT-0043](#omrs-audit-0043) | EXCEPTION | The start-up of an enterprise OMRS Connector for the {0} failed with exception {1} and error message: {2} |
| [OMRS-AUDIT-0044](#omrs-audit-0044) | STARTUP | The connector for the local repository is being started in mode {0} using connector provider {1} and configuration properties {2} |
| [OMRS-AUDIT-0045](#omrs-audit-0045) | STARTUP | The connector for the local repository has been initialized |
| [OMRS-AUDIT-0046](#omrs-audit-0046) | STARTUP | The connector for the local repository is about to be started |
| [OMRS-AUDIT-0047](#omrs-audit-0047) | STARTUP | The connector for the local repository has been started |
| [OMRS-AUDIT-0048](#omrs-audit-0048) | STARTUP | The local repository has started to initialize the local metadata collection which owns the metadata collection id |
| [OMRS-AUDIT-0049](#omrs-audit-0049) | STARTUP | The local repository has initialized the metadata collection named {0} with an id of {1} |
| [OMRS-AUDIT-0050](#omrs-audit-0050) | INFO | The Open Metadata Repository Services (OMRS) is about to process open metadata archive {0} |
| [OMRS-AUDIT-0051](#omrs-audit-0051) | ERROR | The Open Metadata Repository Services (OMRS) cannot process an open metadata archive because it is empty |
| [OMRS-AUDIT-0052](#omrs-audit-0052) | ERROR | The Open Metadata Repository Services (OMRS) cannot process an open metadata archive {0} because it has no control header |
| [OMRS-AUDIT-0053](#omrs-audit-0053) | INFO | The Open Metadata Repository Services (OMRS) has processed {0} types and {1} instances from open metadata archive {2} |
| [OMRS-AUDIT-0054](#omrs-audit-0054) | ERROR | The Open Metadata Repository Services (OMRS) is not processing type definitions from the archive because it does not have a type processor |
| [OMRS-AUDIT-0055](#omrs-audit-0055) | ERROR | The Open Metadata Repository Services (OMRS) is not processing instances from the archive because it does not have a local repository |
| [OMRS-AUDIT-0060](#omrs-audit-0060) | COHORT | Registering with open metadata repository cohort {0} using metadata collection id {1} |
| [OMRS-AUDIT-0061](#omrs-audit-0061) | COHORT | Refreshing local registration information with open metadata repository cohort {0} using metadata collection id {1} |
| [OMRS-AUDIT-0062](#omrs-audit-0062) | COHORT | Requesting registration information from other members of the open metadata repository cohort {0} |
| [OMRS-AUDIT-0063](#omrs-audit-0063) | COHORT | Unregistering with open metadata repository cohort {0} using metadata collection id {1} |
| [OMRS-AUDIT-0064](#omrs-audit-0064) | STARTUP | The Open Metadata Repository Services (OMRS) has initialized the audit log for the {0} called {1} |
| [OMRS-AUDIT-0100](#omrs-audit-0100) | EXCEPTION | Unable parse an incoming event {0} due to exception {1} |
| [OMRS-AUDIT-0101](#omrs-audit-0101) | EXCEPTION | Unable process an incoming event {0} due to exception {1} from listener {2} |
| [OMRS-AUDIT-0102](#omrs-audit-0102) | EXCEPTION | Connector {0} cannot process a null event {0} passed by the event bus |
| [OMRS-AUDIT-0105](#omrs-audit-0105) | EXCEPTION | Unable to send a registry event for cohort {0} due to an error in the OMRS Topic Connector |
| [OMRS-AUDIT-0106](#omrs-audit-0106) | COHORT | Refreshing registration with open metadata repository cohort {0} using metadata collection id {1} at the request of server {2} |
| [OMRS-AUDIT-0107](#omrs-audit-0107) | ACTION | Registration request for this server in cohort {0} was rejected by server {1} that hosts metadata collection {2} because the local metadata collection id {3} is not unique for this cohort |
| [OMRS-AUDIT-0108](#omrs-audit-0108) | ACTION | Two servers in cohort {0} are using the same metadata collection identifier {1} |
| [OMRS-AUDIT-0109](#omrs-audit-0109) | ACTION | Registration error occurred in cohort {0} because remote server {1} that hosts metadata collection {2} cannot use connection {3} to create a connector to this local server |
| [OMRS-AUDIT-0110](#omrs-audit-0110) | COHORT | A new registration request has been received for cohort {0} from server {1} that hosts metadata collection {2} |
| [OMRS-AUDIT-0111](#omrs-audit-0111) | COHORT | Server {0} hosting metadata collection {1} has left cohort {2} |
| [OMRS-AUDIT-0112](#omrs-audit-0112) | COHORT | A re-registration request has been received for cohort {0} from server {1} that hosts metadata collection {2} |
| [OMRS-AUDIT-0114](#omrs-audit-0114) | ACTION | Registration error occurred in cohort {0} because the local server is not able to use the remote connection {1} supplied by server {2} that hosts metadata collection {3} to create a connector to its metadata repository.  Error message is {4} |
| [OMRS-AUDIT-0118](#omrs-audit-0118) | ERROR | Unable to process the {0} request for cohort {1} from cohort member {2} because there is no cohort registry store |
| [OMRS-AUDIT-0127](#omrs-audit-0127) | COHORT | A new remote member from server {0} for cohort {1} is being configured into the enterprise connector manager.  It has a metadata collection id of {2}, a metadata collection name of {3}, the server type is {4} and its owning organization is {5} |
| [OMRS-AUDIT-0128](#omrs-audit-0128) | COHORT | Configuration received for an existing remote member from server {0} for cohort {1} has not changed.  It has a metadata collection id of {2}, a metadata collection name of {3}, the server type is {4} and its owning organization is {5} |
| [OMRS-AUDIT-0129](#omrs-audit-0129) | EXCEPTION | A exception occurred whilst adding a new remote member from server {0} for cohort {1} into the enterprise connector manager.  The exception was {2} and the error message was {3} |
| [OMRS-AUDIT-0130](#omrs-audit-0130) | COHORT | The enterprise repository services are managing federated queries to the following list of servers: {0} |
| [OMRS-AUDIT-0131](#omrs-audit-0131) | COHORT | A remote member from cohort {0} with metadata collection id {1} is being removed by the enterprise connector manager |
| [OMRS-AUDIT-0132](#omrs-audit-0132) | COHORT | A new remote connector from server {0} with metadata collection id {1} has been deployed to enterprise connector for {2} |
| [OMRS-AUDIT-0133](#omrs-audit-0133) | COHORT | Information about remote connector from server {0} with metadata collection id {1} has been refreshed in enterprise connector for {2} |
| [OMRS-AUDIT-0134](#omrs-audit-0134) | COHORT | The remote connector for metadata collection id {0} is no longer being called by {1} |
| [OMRS-AUDIT-0135](#omrs-audit-0135) | COHORT | Configuration for an existing remote member from server {0} for cohort {1} has changed and is being updated in the enterprise connector manager.  It has a metadata collection id of {2}, a metadata collection name of {3}, the server type is {4} and its owning organization is {5} |
| [OMRS-AUDIT-0136](#omrs-audit-0136) | EXCEPTION | A {0} exception was received from a remote repository with metadata collection id {1} during {2}.  The exception message was {3} |
| [OMRS-AUDIT-0137](#omrs-audit-0137) | INFO | The remote repository {0} with metadata collection id {1} does not support method {2} |
| [OMRS-AUDIT-0206](#omrs-audit-0206) | ACTION | Server {0} that hosts metadata collection {1} has detected that there are two different instances with the same guid of {2}: the instance from {3} has a type of {4} has a provenance type of {5} and the other instance from metadata collection {6} has a type of {7} - the instance in metadata collection {6} should have its guid changed.  The accompanying error message is : {8} |
| [OMRS-AUDIT-0207](#omrs-audit-0207) | INFO | Server {0} that hosts metadata collection {1} has changed the unique identifier (guid) of an instance of type {2} from {3} to {4} |
| [OMRS-AUDIT-0208](#omrs-audit-0208) | ACTION | Server {0} that hosts metadata collection {1} was unable to change the unique identifier (guid) of an instance of type {2} from {3}.  The exception was {4} with error message {5} |
| [OMRS-AUDIT-0209](#omrs-audit-0209) | ACTION | Server {0} that hosts metadata collection {1} has detected that the version of the type being used by instance with the unique identifier (guid) of {2} has regressed; the type that this server is using locally is {3} and the type from the instance from metadata collection {4} is {5}; the two versions need to be reconciled. The accompanying error message is: {6} |
| [OMRS-AUDIT-0210](#omrs-audit-0210) | ACTION | Server {0} that hosts metadata collection {1} has detected that the version of the type being used by the local instance with the unique identifier (guid) of {2} has regressed; the type that the reporting server is using locally is {3} and the type from the instance from this metadata collection {4} is {5}; the two versions need to be reconciled. The accompanying error message is: {6} |
| [OMRS-AUDIT-0211](#omrs-audit-0211) | ERROR | Server {0} that hosts metadata collection {1} was unable to remove the reference copy of an instance of type {2} with unique identifier (guid) of {3}.  The exception was {4} with error message: {5} |
| [OMRS-AUDIT-0301](#omrs-audit-0301) | TYPES | The local server has added a new type called {0} with a unique identifier of {1} and a version number of {2} from {3} |
| [OMRS-AUDIT-0302](#omrs-audit-0302) | TYPES | The local server cannot add a new type called {0} with a unique identifier of {1} and a version number of {2} because the server does not support this feature |
| [OMRS-AUDIT-0303](#omrs-audit-0303) | TYPES | The local server has updated an existing type called {0} with a unique identifier of {1} to version number of {2} using a patch from {3} |
| [OMRS-AUDIT-0305](#omrs-audit-0305) | TYPES | The local server has detected a conflict with an existing type called {0} with a unique identifier of {1}. This does not match the type name {2} and unique identifier {3} passed to it on a request from {4} |
| [OMRS-AUDIT-0306](#omrs-audit-0306) | TYPES | A new type called {0} (guid {1}) is being maintained through the API of server {2}.  The method called by user {3} was {4} |
| [OMRS-AUDIT-0307](#omrs-audit-0307) | TYPES | Conflicting type definitions for type {0} ({1}) have been detected in remote system {2} ({3}).  Other system involved has metadata collection {4} Error message is: {5} |
| [OMRS-AUDIT-0308](#omrs-audit-0308) | TYPES | The local server has detected a type called {0} from {1} with a null unique identifier |
| [OMRS-AUDIT-0309](#omrs-audit-0309) | TYPES | The local server has detected a type from {0} with a null unique name. The supplied unique identifier for the type is {1} |
| [OMRS-AUDIT-0310](#omrs-audit-0310) | TYPES | The local server has been asked to process a request from {0} containing an unrecognized type {1} ({2}) |
| [OMRS-AUDIT-0311](#omrs-audit-0311) | TYPES | The local server has been asked to process a request from {0} containing a type {1} ({2}) with a null type category |
| [OMRS-AUDIT-0312](#omrs-audit-0312) | TYPES | The local server has been asked to process a request from {0} containing a type {1} ({2}) with an unexpected type category of {3}.  The local type definition has a category of {4} |
| [OMRS-AUDIT-0313](#omrs-audit-0313) | TYPES | The local server method {0} has been asked to process a request from {1} that contains a null type |
| [OMRS-AUDIT-0314](#omrs-audit-0314) | TYPES | The local server has been asked to process a request from {0} containing a containing an instance with type {1} ({2}) of category {3} but a version of {4}.  The locally supported version is {5} |
| [OMRS-AUDIT-0315](#omrs-audit-0315) | ACTION | The local server has been asked to process a request from {0} containing a containing an instance with type {1} ({2}) of category {3} but a null unique identifier (GUID) |
| [OMRS-AUDIT-0316](#omrs-audit-0316) | ACTION | The local server method {0} has been asked to process a request from {1} containing an instance with unique identifier {2} and type {3} ({4}) of category {5} but a null unique home metadata collection identifier (GUID) |
| [OMRS-AUDIT-0317](#omrs-audit-0317) | ACTION | The local server method {0} has been asked to process a request from {1} containing a null instance |
| [OMRS-AUDIT-0318](#omrs-audit-0318) | TYPES | The local server has recognized a new type called {0} with a unique identifier of {1} and a version number of {2} from {3} |
| [OMRS-AUDIT-0319](#omrs-audit-0319) | TYPES | The local server does not have a local repository and so a new type called {0} with a unique identifier of {1} and a version number of {2} from {3} is just cached for information |
| [OMRS-AUDIT-0320](#omrs-audit-0320) | TYPES | A patch to the {0} ({1}) type definition was ignored because it is to be applied to the {2} version of the type and the patch is for version {3} |
| [OMRS-AUDIT-0321](#omrs-audit-0321) | TYPES | A patch to the {0} ({1}) type definition from {2} was ignored because the local repository does not support this type |
| [OMRS-AUDIT-0322](#omrs-audit-0322) | TYPES | The local server does not have a local repository and so an update to the type called {0} with a unique identifier of {1} that applies to version number of {2} from {3} is just cached for information |
| [OMRS-AUDIT-0401](#omrs-audit-0401) | ACTION | Skipping call to repository {0} since it is not responding correctly.  Error received was {1} with message {2} |
| [OMRS-AUDIT-8001](#omrs-audit-8001) | ERROR | Received unknown event: {0} |
| [OMRS-AUDIT-8002](#omrs-audit-8002) | TYPES | A later version of the instance {0} from server {1} and metadata collection {2} is using an older version of the type {3}.  Previous type header was {4}, new type header is {5} |
| [OMRS-AUDIT-8003](#omrs-audit-8003) | ACTION | Version {0} of the instance {1} from server {2} has a new metadata collection id of {3}.  Its original metadata collection id was {4} |
| [OMRS-AUDIT-8004](#omrs-audit-8004) | ACTION | Version {0} of the instance {1} from server {2} and metadata collection {3} has a new type of {4} ({5}).  Previous type was {6} ({7}) |
| [OMRS-AUDIT-8005](#omrs-audit-8005) | ACTION | An instance of type {0} ({1}) from server {2} and metadata collection {3} is using the same guid {4} as a stored instance of type {5} ({6}) |
| [OMRS-AUDIT-8006](#omrs-audit-8006) | EVENT | Processing incoming event of type {0} for instance {1} from: {2} |
| [OMRS-AUDIT-8007](#omrs-audit-8007) | EXCEPTION | An invalid instance was found in a batch of reference instances send by a remote member of the cohort. The exception was {0} with message {1} |
| [OMRS-AUDIT-8008](#omrs-audit-8008) | INFO | Retrying retrieve of an entity {0} for user {1} because only a proxy is available - attempt {2} |
| [OMRS-AUDIT-8009](#omrs-audit-8009) | EVENT | The Open Metadata Repository Services (OMRS) has sent event of type {0} to the cohort topic {1} |
| [OMRS-AUDIT-9002](#omrs-audit-9002) | EXCEPTION | Unable to process a received event from topic {0} because its content is null |
| [OMRS-AUDIT-9003](#omrs-audit-9003) | EXCEPTION | Unable to send a TypeDef event for cohort {0} due to an error in the OMRS Topic Connector |
| [OMRS-AUDIT-9005](#omrs-audit-9005) | EXCEPTION | Unable to send or receive a metadata instance event for cohort {0} due to an error in the OMRS Topic Connector |
| [OMRS-AUDIT-9011](#omrs-audit-9011) | EXCEPTION | An incoming event of type {0} from {1} ({2}) generated an exception of type {3} with message {4} |
| [OMRS-AUDIT-9012](#omrs-audit-9012) | EXCEPTION | Disconnecting from the {0} enterprise topic connector generated a {1} exception with message {2} |
| [OMRS-AUDIT-9013](#omrs-audit-9013) | EXCEPTION | Disconnecting of the enterprise connector manager generated an exception with message {0} |
| [OMRS-AUDIT-9014](#omrs-audit-9014) | EXCEPTION | The Open Metadata Repository Service has generated an unexpected {0} exception during method {1}.  The message was {2} |
| [OMRS-AUDIT-9015](#omrs-audit-9015) | ERROR | {0} has passed the {1} an invalid instance within an inbound event reportedly to be from server {2} of type {3} and owned by organization {4}.  The instance is from metadata collection {5} and is of this format: {6} |
| [OMRS-AUDIT-9016](#omrs-audit-9016) | EXCEPTION | The topic listener for the {0} service caught an unexpected exception {1} with message {2} |
| [OMRS-AUDIT-9017](#omrs-audit-9017) | EXCEPTION | The topic listener for the {0} service threw an unexpected exception {1} with message {2} |
| [OMRS-AUDIT-9018](#omrs-audit-9018) | ERROR | Exception {0} occurred when processing a type {1} from {2}.  The originator of the type was {3} ({4}).  The message in the exception was {5} |
| [OMRS-AUDIT-9019](#omrs-audit-9019) | EXCEPTION | The type definition event processor for the {0} service caught an unexpected exception {1} with message {2} |

----

### OMRS-AUDIT-0001

> The Open Metadata Repository Services (OMRS) is initializing the subsystems to support a new server

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.OMRS_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | none |

**System action**

The local server has started up the OMRS subsystems for a metadata server.  The choices of subsystem to start, and their configuration is specified in the repository services section of this server's configuration document.

**User action**

No action is required if this server is intended to support a metadata repository.  This metadata repository may be an integral part of this server, or may be a remote server that this server is providing open metadata proxy services to.  The conformance test server also uses a local metadata repository to drive particular types of workload to the server it is testing.


----

### OMRS-AUDIT-0002

> Enterprise access through the Enterprise Repository Services is initializing

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.ENTERPRISE_ACCESS_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | none |

**System action**

The local server has started the enterprise access support provided by the enterprise repository services.  This supports federated queries to all members of all open metadata repository cohorts that this server is a member of

**User action**

No action is required if the server is supporting Open Metadata Access Services (OMASs),providing metadata services for open connectors or hosting the conformance test suite.  If this feature is not required, it can be disabled the next time the server starts by removing the enterprise repository services configuration from this server's configuration document.


----

### OMRS-AUDIT-0003

> The local repository is initializing the metadata collection named {0} with an id of {1}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.LOCAL_REPOSITORY_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The local server has started to initialize the local repository subsystem.  This subsystem supports a metadata repository that supports the open metadata repository interfaces through the OMRSRepositoryConnector.  The physical repository may support these interfaces natively, or have a connector that supports these interfaces and translates between the open metadata interfaces and the real repository's interfaces.

**User action**

No action is required if this server is to support a local repository.  If this feature is not required then it can be disabled by removing the local repository services configuration from this server's configuration document.


----

### OMRS-AUDIT-0004

> Connecting to the metadata highway

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.METADATA_HIGHWAY_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | none |

**System action**

The local server has started to initialize the communication with the open metadata repository cohorts.  The cohorts enable peer-to-peer metadata exchange between metadata servers.

**User action**

No action is required if this server is intended to connect to one or more other metadata servers through open metadata.  If this feature is not required then it can be disabled by removing the list of cohorts from the repository services configuration in the this server's configuration document.   If however, the server has successfully connected to the cohort(s), it should be shutdown with the permanent option.  This will tell the other members of the cohort(s) that this server is leaving so that they can tidy up their cohort registry.  Then remove the cohort list from the configuration document.


----

### OMRS-AUDIT-0005

> Connecting to open metadata repository cohort {0}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.COHORT_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

The local server has started to initialize the communication with the named open metadata repository cohort.  This will allow it to exchange metadata with all the other members of this cohort.

**User action**

No action is required if connecting to this cohort is the expected behavior of this server.  If this server is not meant to connect to this cohort then it should be shutdown with the permanent option - this will tell the other members of the cohort that this server is permanently leaving the cohort.  Then remove the cohort configuration from the cohort list in the repository services configuration section of this server's configuration document and restart the server.  The registry store for this server can then be deleted.


----

### OMRS-AUDIT-0006

> Configuration error detected while connecting to cohort {0}, exception {1} was caught with error message: {2}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.COHORT_CONFIG_ERROR` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The local server has started to initialize the communication with the named open metadata repository cohort and a configuration error was detected.

**User action**

Review the exception and resolve the issue it documents. Then disconnect and reconnect this server to the cohort.


----

### OMRS-AUDIT-0007

> The Open Metadata Repository Services (OMRS) has initialized

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.OMRS_INITIALIZED` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | none |

**System action**

The local server has completed the initialization of the OMRS.

**User action**

Verify that the correct subsystems have been started and there are no unexpected errors.


----

### OMRS-AUDIT-0008

> Disconnecting from cohort {0}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.COHORT_DISCONNECTING` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The local server has started to shutdown the communication with the named open metadata repository cohort.  Other members will stop sending metadata requests to this server.

**User action**

No action is required if the server is shutting down, or there has been a deliberate action to temporarily remove the server from the cohort.  If this is unexpected then look for reasons why the server is disconnecting - it may be an operator action or failure in the cohort.


----

### OMRS-AUDIT-0009

> Unregistering from cohort {0}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.COHORT_PERMANENTLY_DISCONNECTING` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The local server has started to unregister from all future communication with the named open metadata repository cohort.

**User action**

No action is required if the server is shutting down, or there has been a deliberate action to remove the server from the cohort.  If this is unexpected then look for reasons why the server is disconnecting - it may be an operator action or failure in the cohort.


----

### OMRS-AUDIT-0010

> The Open Metadata Repository Services (OMRS) is disconnecting the open metadata repositories

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.OMRS_DISCONNECTING` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | none |

**System action**

The local server has begun the shutdown of the OMRS.

**User action**

Validate that the repository services successfully releases the resources it is using..


----

### OMRS-AUDIT-0011

> The Open Metadata Repository Services (OMRS) has disconnected from the open metadata repositories

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.OMRS_DISCONNECTED` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | none |

**System action**

The local server has completed the disconnection of the OMRS.

**User action**

Verify that the OMRS is being shutdown as part of the server shutdown.


----

### OMRS-AUDIT-0013

> Unable to start processing in the local repository due to error {0}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.LOCAL_REPOSITORY_FAILED_TO_START` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}` |

**System action**

The local server will not process events.

**User action**

Review previous error messages to determine the precise error in the start up configuration. Correct the configuration and restart the server.


----

### OMRS-AUDIT-0015

> The listener thread for an OMRS Topic Connector for topic {0} has started

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.OPEN_METADATA_TOPIC_LISTENER_START` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

The listener thread will process inbound events

**User action**

Verify that the topic is properly defined in the event bus so that events can flow.


----

### OMRS-AUDIT-0016

> The listener thread for the OMRS Topic Connector for topic {0} has shutdown

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.OPEN_METADATA_TOPIC_LISTENER_SHUTDOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The listener thread has stopped processing inbound events

**User action**

Verify that the topic connector is being shutdown as part of the server shutdown.  Without this listener, the server is not able to receive events from the named topic.


----

### OMRS-AUDIT-0018

> The OMRS Topic Listener has no embedded connector to an event bus

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.OMRS_TOPIC_LISTENER_DEAF` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | none |

**System action**

The OMRS Topic Connector cannot receive or send events because its connection object does not include an embedded connection to the event bus.

**User action**

Verify the start up configuration to ensure there is an event bus configured and that it was in place before the cohort was defined.


----

### OMRS-AUDIT-0019

> An OMRS Topic Connector has registered with an event bus connector for topic {0}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.OMRS_TOPIC_LISTENER_REGISTERED` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

The OMRS Topic Connector is able to receive or send events

**User action**

This is the connector that receives events from the OMRS Topic.  It is vital for the server'sability to register with the open metadata repository cohort, validate its types and replicate metadata.  Verify that these types of events are flowing.  If the server appears to be buffering events it means a problem with the topic definition or the event bus supporting it.


----

### OMRS-AUDIT-0020

> An OMRS Topic Connector is ready to send and receive events on topic {0}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.OMRS_TOPIC_LISTENER_STARTED` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

The OMRS Topic Connector is able to receive or send events.

**User action**

Verify that events are flowing and not buffering.


----

### OMRS-AUDIT-0021

> The OMRS Topic Listener has disconnected the event bus connectors for topic {0}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.OMRS_TOPIC_LISTENER_DISCONNECTED` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The OMRS Topic Connector is no longer able to receive or send events.

**User action**

Verify that this is part of the server disconnecting from the cohort.


----

### OMRS-AUDIT-0024

> The local repository event mapper connector {0} is ready to send and receive events

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.EVENT_MAPPER_LISTENER_STARTED` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

The event mapper connector is able to receive or send events

**User action**

Verify that events are flowing from the real repository into the open metadata repository cohort.


----

### OMRS-AUDIT-0025

> The local repository event mapper connector {0} has disconnected the event bus connectors

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.EVENT_MAPPER_LISTENER_DISCONNECTED` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The event mapper connector is no longer able to receive or send events

**User action**

Verify that this is part of the server shutdown of the local repository.


----

### OMRS-AUDIT-0026

> Initializing listener for cohort {0}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.OMRS_LISTENER_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

The local server has initialized a listener to receive inbound events from the named open metadata repository cohort.

**User action**

Verify that the local repository is receiving inbound events - or at least there are no errors reported related to incoming events


----

### OMRS-AUDIT-0027

> The local server cannot initiate a connection to the cohort {0} when starting up, exception {1} was caught with error message: {2}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.COHORT_STARTUP_ERROR` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The local server will now cancel startup, and shutdown.

**User action**

Review the exception and resolve the issue it documents. Then try starting the server again.


----

### OMRS-AUDIT-0028

> A reference instance has been passed to the local repository on the {0} parameter of the {1} method which has the local repository metadata collection id {2} as its home

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.LOCAL_REFERENCE_INSTANCE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system saves the instance since it assumes it comes from a back-up open metadata archive.

**User action**

Validate that the reference instance comes from a back-up.  If it does then all is well.  If it does not, look for errors in the audit log and validate that the message passing protocol levels are compatible. If nothing is clearly wrong with the set up, raise a Github issue or ask for help on the dev mailing list.


----

### OMRS-AUDIT-0029

> The {0} event manager is initializing

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.INITIALIZING_EVENT_MANAGER` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

The event manager is ready for internal OMRS event consumers to register with it.

**User action**

The event manager is responsible for passing events between internal components in the OMRS.There is one event manager for inbound events and other for outbound events. The name of the event manager indicates the direction of flow.  There will be other messages that show the linking of the internal components.


----

### OMRS-AUDIT-0030

> Registering the {0} event consumer with the {1} event manager

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.REGISTERING_EVENT_PROCESSOR` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

A consumer of events has been registered with the event manager.  This consumer will distribute events to its associated components.

**User action**

This is part of the process to connect the internal components inside the OMRS together to that they can pass events to one another.  This event passing is done via dynamic registration with an event manager because the internal components in OMRS are optional.  Their initialization is controlled by the server's configuration. The event manager removes the hard dependencies between the OMRS components.


----

### OMRS-AUDIT-0031

> The {0} event manager is starting with {1} type definition event consumer(s) and {2} instance event consumer(s)

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.STARTING_EVENT_MANAGER` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The event manager is fully initialized and beginning to distribute events

**User action**

This message confirms that the dynamic configuration of the event manger is complete and the running OMRS componentsare now connected together.


----

### OMRS-AUDIT-0032

> The {0} event manager is sending out the {1} type definition events that were generated and buffered during server initialization

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.DRAINING_TYPEDEF_EVENT_BUFFER` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The event manager is fully initialized and distributing buffered events that describe type definitions

**User action**

Look for evidence that TypeDef events are flowing to the topic on the event bus.


----

### OMRS-AUDIT-0033

> The {0} event manager is sending out the {1} instance events that were generated and buffered during server initialization

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.DRAINING_INSTANCE_EVENT_BUFFER` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The event manager is fully initialized and distributing buffered events that describe instance definitions

**User action**

Look for evidence that events are flowing to the topic on the event bus.


----

### OMRS-AUDIT-0034

> The connector to the local repository failed with a {0} exception and the following error message: {1}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.BAD_REAL_LOCAL_REPOSITORY_CONNECTOR` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The server fails to start.

**User action**

Correct the configuration to ensure that the local repository local connection is valid.


----

### OMRS-AUDIT-0035

> The connector to the local repository event mapper failed with a {0} exception and the following error message: {1}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.BAD_REAL_LOCAL_EVENT_MAPPER` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The server cannot start.

**User action**

Correct the configuration to ensure that the local repository's event mapper connection is valid.


----

### OMRS-AUDIT-0036

> The connector to the local repository archive store failed with a {0} exception and the following error message: {1}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.BAD_ARCHIVE_STORE` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The server cannot start up.

**User action**

Correct the configuration to ensure that the local repository's archive store connection is valid.


----

### OMRS-AUDIT-0038

> The connector to the {0} topic failed with a {1} exception and the following error message: {2}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.BAD_TOPIC_CONNECTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The server fails to start since it is not able to operate without an audit log.

**User action**

Correct the configuration to ensure that the cohort's topic connection is valid.


----

### OMRS-AUDIT-0039

> Ignoring duplicate request to classify {0} entity {1} with classification {2} in the {3} repository

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.IGNORING_DUPLICATE_CLASSIFICATION` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The server has detected that multiple services are attempting to add the same classification with the same properties to the entity.  It is ignoring the duplicate request

**User action**

This is a common race condition.  It is seen often with the Anchors classification that is added automatically when Egeria detects an entity does not have this classification.


----

### OMRS-AUDIT-0040

> An enterprise OMRS connector has been created for the {0}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.NEW_ENTERPRISE_CONNECTOR` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

This connector is responsible for supporting federated queries to both the local repository (if any)and all the other servers registered with the same open metadata repository cohorts as this server.  The enterprise OMRS connector is dynamically configured with details of the members of these cohorts by the enterprise connector manager.  At this stage it will be configured with the known members of the cohorts.  As the server operates, and the membership of the cohorts change, the enterprise OMRS connector is reconfigured.

**User action**

The enterprise connector is used by the Open Metadata Access Services (OMASs) and the Open Metadata Conformance Test Suite.  Validate that it is being used as part of these services.


----

### OMRS-AUDIT-0041

> The enterprise OMRS connector for the {0} has started

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.STARTING_ENTERPRISE_CONNECTOR` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

The connector will support federated queries to the metadata repositories connected to the same open metadata repository cohorts as this server as well as the local repository (if any).

**User action**

Validate that the expected servers are successfully registering with the open metadata repository cohorts that this server is also connecting to.


----

### OMRS-AUDIT-0042

> The enterprise OMRS Connector for the {0} has shutdown

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.DISCONNECTING_ENTERPRISE_CONNECTOR` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}` |

**System action**

The connector supported access to the connected open metadata repositories.  Federated queries will no longer reach out to other servers in the open metadata repository cohorts.  Requests to the local repository (if any) will also stop.

**User action**

Validate that this is part of the shutdown of either an Open Metadata Access Service (OMAS) or the Conformance Test Suite.


----

### OMRS-AUDIT-0043

> The start-up of an enterprise OMRS Connector for the {0} failed with exception {1} and error message: {2}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.ENTERPRISE_CONNECTOR_FAILED` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector will not be able to support access to the open metadata repositories connected via the cohort.

**User action**

Review the message to discover why the connector failed to start.


----

### OMRS-AUDIT-0044

> The connector for the local repository is being started in mode {0} using connector provider {1} and configuration properties {2}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.CREATING_REAL_CONNECTOR` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

This repository connector provides the metadata storage mechanism for this server.  It may be read only (and populated using events and open metadata archives) or provides read/write storage or access to a third party repository.

**User action**

Verify that the correct type of repository, with the correct type of storage has been configured for this server.


----

### OMRS-AUDIT-0045

> The connector for the local repository has been initialized

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.NEW_REAL_CONNECTOR` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | none |

**System action**

The connector configuration identified a valid connector.  The server continues initializing.

**User action**

Verify that the repository connector initialized without error.


----

### OMRS-AUDIT-0046

> The connector for the local repository is about to be started

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.STARTING_REAL_CONNECTOR` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | none |

**System action**

The server calls start() on the connector.

**User action**

Verify that the repository connector returns from the start() without error. Look for message OMRS-AUDIT-0047


----

### OMRS-AUDIT-0047

> The connector for the local repository has been started

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.STARTED_REAL_CONNECTOR` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | none |

**System action**

The server continues initializing.

**User action**

Verify that the repository connector started without error.


----

### OMRS-AUDIT-0048

> The local repository has started to initialize the local metadata collection which owns the metadata collection id

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.LOCAL_REPOSITORY_INITIALIZING_NULL_ID` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | none |

**System action**

The local server is about to initialize the local repository subsystem.  This subsystem supports a metadata repository that supports the open metadata repository interfaces through the OMRSRepositoryConnector.  The physical repository may support these interfaces natively, or have a connector that supports these interfaces and translates between the open metadata interfaces and the real repository's interfaces.  The repository connector is responsible for setting the metadata collection id.

**User action**

Check that the metadata collection id is properly set up by the local repository.  If so, no action is required if this server is to support a local repository.  If this feature is not required then it can be disabled by removing the local repository services configuration from this server's configuration document.


----

### OMRS-AUDIT-0049

> The local repository has initialized the metadata collection named {0} with an id of {1}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.LOCAL_REPOSITORY_INITIALIZED` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The local server has initialized the local repository subsystem with a unique identifier for the local metadata collection.  This subsystem supports a metadata repository that supports the open metadata repository interfaces through the OMRSRepositoryConnector.  The physical repository may support these interfaces natively, or have a connector that supports these interfaces and translates between the open metadata interfaces and the real repository's interfaces.

**User action**

No action is required if this server is to support a local repository. Check that the metadata collection is the correct one.  If this feature is not required then it can be disabled by removing the local repository services configuration from this server's configuration document.


----

### OMRS-AUDIT-0050

> The Open Metadata Repository Services (OMRS) is about to process open metadata archive {0}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.PROCESSING_ARCHIVE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

The local server is about to local types and instances from an open metadata archive.

**User action**

Validate that this archive is appropriate for the server.


----

### OMRS-AUDIT-0051

> The Open Metadata Repository Services (OMRS) cannot process an open metadata archive because it is empty

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.EMPTY_ARCHIVE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | none |

**System action**

The local server is skipping an open metadata archive because it is empty.

**User action**

Review the list of archives for the server and determine which archive is in error.  Request a new version of the archive or remove it from the server's archive list.


----

### OMRS-AUDIT-0052

> The Open Metadata Repository Services (OMRS) cannot process an open metadata archive {0} because it has no control header

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.NULL_PROPERTIES_IN_ARCHIVE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}` |

**System action**

The local server is skipping an open metadata archive because it does not have a correct header.  This header confirms the version and source of the archive and is necessary to process the archive.

**User action**

The most likely cause of this error is that the source is not actually a valid open metadata archive.  Verify that the name of the archive is correct and it is correctly built. Request a new version of the archive or remove it from the server's archive list.


----

### OMRS-AUDIT-0053

> The Open Metadata Repository Services (OMRS) has processed {0} types and {1} instances from open metadata archive {2}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.COMPLETED_ARCHIVE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The local server has completed the processing of the open metadata archive.

**User action**

Verify that the expected content has loaded into the local repository.


----

### OMRS-AUDIT-0054

> The Open Metadata Repository Services (OMRS) is not processing type definitions from the archive because it does not have a type processor

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.NO_TYPE_DEF_PROCESSOR` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | none |

**System action**

The local server is not able to process the open metadata archive because it is not a cohort member.

**User action**

If the type definitions from the open metadata archive are needed by the open metadata ecosystem, then load the open metadata archive into an OMAG Server that is a Cohort Member.  Detail of the different types of servers are found in the admin guide.


----

### OMRS-AUDIT-0055

> The Open Metadata Repository Services (OMRS) is not processing instances from the archive because it does not have a local repository

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.NO_INSTANCE_PROCESSOR` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | none |

**System action**

The local server is not able to process metadata instances from the open metadata archive because it does not have a local repository.

**User action**

If the instances from the open metadata archive are needed by the open metadata ecosystem, then load the open metadata archive into a Metadata Server or a Repository Proxy that connects to a third party repository that supports this type of metadata.  Detail of the different types of servers are found in the admin guide.


----

### OMRS-AUDIT-0060

> Registering with open metadata repository cohort {0} using metadata collection id {1}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.REGISTERED_WITH_COHORT` |
| **Severity** | COHORT - The server is exchanging registration information about an open metadata repository cohort that it is connecting to. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The local server has sent a registration event to the other members of the cohort.  This is to configure this repository into the enterprise OMRS repository connectors of all the other members of the cohort.

**User action**

Validate that this server is connecting to the right cohort.


----

### OMRS-AUDIT-0061

> Refreshing local registration information with open metadata repository cohort {0} using metadata collection id {1}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.RE_REGISTERED_WITH_COHORT` |
| **Severity** | COHORT - The server is exchanging registration information about an open metadata repository cohort that it is connecting to. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The local server has sent a re-registration request to the other members of the cohort as part of its routine to re-connect with the open metadata repository cohort.

**User action**

Validate that this server's registration is being successfully processed in the other members of the cohort.


----

### OMRS-AUDIT-0062

> Requesting registration information from other members of the open metadata repository cohort {0}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.REFRESH_REGISTRATION_REQUEST_WITH_COHORT` |
| **Severity** | COHORT - The server is exchanging registration information about an open metadata repository cohort that it is connecting to. |
| **Message inserts** | `{0}` |

**System action**

The local server has sent a registration refresh request to the other members of the cohort as part of its routine to re-connect with the open metadata repository cohort.

**User action**

Validate that the registration response from the other members of the cohort are received and processed successfully.


----

### OMRS-AUDIT-0063

> Unregistering with open metadata repository cohort {0} using metadata collection id {1}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.UNREGISTERING_FROM_COHORT` |
| **Severity** | COHORT - The server is exchanging registration information about an open metadata repository cohort that it is connecting to. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The local server has sent a unregistration event to the other members of the cohort as part of its routine to permanently disconnect with the open metadata repository cohort.

**User action**

Verify that the server should be unregistering from the cohort.  It will mean that the other servers in the cohort will no longer be able to retrieve metadata from this server.


----

### OMRS-AUDIT-0064

> The Open Metadata Repository Services (OMRS) has initialized the audit log for the {0} called {1}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.OMRS_AUDIT_LOG_READY` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The local server has started up the logging destinations defined in the configuration document.

**User action**

Validate that all the logging destinations are working.


----

### OMRS-AUDIT-0100

> Unable parse an incoming event {0} due to exception {1}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.EVENT_PARSING_ERROR` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The information in the event is not available to the server.

**User action**

Review the exception to determine the source of the error and correct it.


----

### OMRS-AUDIT-0101

> Unable process an incoming event {0} due to exception {1} from listener {2}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.EVENT_PROCESSING_ERROR` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The information in the event is unavailable to the server.

**User action**

Review the exception to determine the source of the error and fix it.


----

### OMRS-AUDIT-0102

> Connector {0} cannot process a null event {0} passed by the event bus

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.NULL_EVENT_TO_PROCESS` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}` |

**System action**

The OMRS Topic Connector was passed a null event by the event bus.

**User action**

Review the exception to identify the source of the error and correct it.


----

### OMRS-AUDIT-0105

> Unable to send a registry event for cohort {0} due to an error in the OMRS Topic Connector

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.SEND_REGISTRY_EVENT_ERROR` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}` |

**System action**

The local server cannot properly manage registration events for the metadata repository cohort. The cause of the error is recorded in the accompanying exception.

**User action**

Review the exception and resolve the issue it documents.  Then disconnect and reconnect this server to the cohort.


----

### OMRS-AUDIT-0106

> Refreshing registration with open metadata repository cohort {0} using metadata collection id {1} at the request of server {2}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.REFRESHING_REGISTRATION_WITH_COHORT` |
| **Severity** | COHORT - The server is exchanging registration information about an open metadata repository cohort that it is connecting to. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The local server has sent a re-registration event to the other members of the cohort in response to a registration refresh event from another member of the cohort.

**User action**

Verify that the other members of the cohort are receiving and processing the registration information from this server.


----

### OMRS-AUDIT-0107

> Registration request for this server in cohort {0} was rejected by server {1} that hosts metadata collection {2} because the local metadata collection id {3} is not unique for this cohort

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.INCOMING_CONFLICTING_LOCAL_METADATA_COLLECTION_ID` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The local server will not receive metadata from the open metadata repository with the same metadata collection id. There is a chance of metadata integrity issues since a metadata instance can be updated in two places.

**User action**

It is necessary to update the local metadata collection id to remove the conflict.


----

### OMRS-AUDIT-0108

> Two servers in cohort {0} are using the same metadata collection identifier {1}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.INCOMING_CONFLICTING_METADATA_COLLECTION_ID` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The local server will not be able to distinguish ownership of metadata from these open metadata repositorieswith the same metadata collection id. There is a chance of metadata integrity issues since a metadata instance can be updated in two places.

**User action**

Update the local metadata collection id to remove the conflict.


----

### OMRS-AUDIT-0109

> Registration error occurred in cohort {0} because remote server {1} that hosts metadata collection {2} cannot use connection {3} to create a connector to this local server

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.INCOMING_BAD_CONNECTION` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The remote server will not be able to query metadata from this local server.

**User action**

This error may be caused because the connection is incorrectly configured, or that the jar file for the connector is not available in the remote server.


----

### OMRS-AUDIT-0110

> A new registration request has been received for cohort {0} from server {1} that hosts metadata collection {2}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.NEW_MEMBER_IN_COHORT` |
| **Severity** | COHORT - The server is exchanging registration information about an open metadata repository cohort that it is connecting to. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The local server will process the registration request and if the parameters are correct, it will accept the new member.

**User action**

Verify that this is an expected new member of the cohort and that its metadata can be combined with the metadata of the local repository.


----

### OMRS-AUDIT-0111

> Server {0} hosting metadata collection {1} has left cohort {2}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.MEMBER_LEFT_COHORT` |
| **Severity** | COHORT - The server is exchanging registration information about an open metadata repository cohort that it is connecting to. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The local server will process the incoming unregistration request and if the parameters are correct, it will remove the former member from its cohort registry store.

**User action**

Verify that is is ok for this member to leave the cohort. Any metadata from this remote repository that is stored in the local repository will no longer be updated.  It may be kept in the local repository for reference or removed by calling the administration REST API.


----

### OMRS-AUDIT-0112

> A re-registration request has been received for cohort {0} from server {1} that hosts metadata collection {2}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.REFRESHED_MEMBER_IN_COHORT` |
| **Severity** | COHORT - The server is exchanging registration information about an open metadata repository cohort that it is connecting to. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The local server will process the registration request and if the parameters are correct, it will accept the latest registration values for this member.

**User action**

Verify that this registration information is successfully processed and that there are no errors being reported by the enterprise OMRS repositories in this server.


----

### OMRS-AUDIT-0114

> Registration error occurred in cohort {0} because the local server is not able to use the remote connection {1} supplied by server {2} that hosts metadata collection {3} to create a connector to its metadata repository.  Error message is {4}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.OUTGOING_BAD_CONNECTION` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The local server is not able to query metadata from the remote server.

**User action**

This error may be caused because the connection is incorrectly configured, or that the jar file for the connector is not available in the local server.


----

### OMRS-AUDIT-0118

> Unable to process the {0} request for cohort {1} from cohort member {2} because there is no cohort registry store

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.MISSING_MEMBER_REGISTRATION` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The local server can not process a member registration event because the registration information cal not be stored in the cohort registry store. This may simply be a timing issue. However, it may be the result of an earlier issue with the local cohort registry store.

**User action**

Verify that there are no issues with writing to the cohort registry store.


----

### OMRS-AUDIT-0127

> A new remote member from server {0} for cohort {1} is being configured into the enterprise connector manager.  It has a metadata collection id of {2}, a metadata collection name of {3}, the server type is {4} and its owning organization is {5}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.NEW_REMOTE_MEMBER` |
| **Severity** | COHORT - The server is exchanging registration information about an open metadata repository cohort that it is connecting to. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The enterprise connector manager is preparing to process a new connection from a remote member of the cohort.  It will attempt to create a connector for it in order to validate that the connection object is valid. If all is well, the list of systems that are called when a request is made by the access services is updated with the new member.

**User action**

Look for the confirmation message that indicated that the connection object is valid and has been incorporated into the enterprise connector manager's list.


----

### OMRS-AUDIT-0128

> Configuration received for an existing remote member from server {0} for cohort {1} has not changed.  It has a metadata collection id of {2}, a metadata collection name of {3}, the server type is {4} and its owning organization is {5}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.REMOTE_MEMBER_REFRESHED` |
| **Severity** | COHORT - The server is exchanging registration information about an open metadata repository cohort that it is connecting to. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The enterprise connector manager has validated that there is no need to update the enterprise configuration for this remote member.

**User action**

Validate that nothing has changed in the remote system.


----

### OMRS-AUDIT-0129

> A exception occurred whilst adding a new remote member from server {0} for cohort {1} into the enterprise connector manager.  The exception was {2} and the error message was {3}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.NEW_REMOTE_MEMBER_FAILURE` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The enterprise connector manager is preparing to process a new connection from a remote member of the cohort.  It will attempt to create a connector for it in order to validate that the connection object is valid. If all is correct, the list of systems that are called when a request is made by the access services is updated with the new member.

**User action**

Look for the confirmation message that shows that the connection object is correct and has been incorporated into the enterprise connector manager's list.


----

### OMRS-AUDIT-0130

> The enterprise repository services are managing federated queries to the following list of servers: {0}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.FEDERATION_LIST` |
| **Severity** | COHORT - The server is exchanging registration information about an open metadata repository cohort that it is connecting to. |
| **Message inserts** | `{0}` |

**System action**

The enterprise connector manager is recording the current list of servers that the enterprise repository services are calling on behalf of the access services.

**User action**

Validate that this list matches the members of the cohort that have local repositories.


----

### OMRS-AUDIT-0131

> A remote member from cohort {0} with metadata collection id {1} is being removed by the enterprise connector manager

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.REMOVE_REMOTE_MEMBER` |
| **Severity** | COHORT - The server is exchanging registration information about an open metadata repository cohort that it is connecting to. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The enterprise connector manager is removing the connection for a remote member of the cohort from the list of servers that are called during a metadata request from the access services.

**User action**

Validate that this remote server is shutting down, or unregistering from the cohort.  If it is not,then look for error messages to determine why it is being removed.


----

### OMRS-AUDIT-0132

> A new remote connector from server {0} with metadata collection id {1} has been deployed to enterprise connector for {2}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.NEW_REMOTE_MEMBER_DEPLOYED` |
| **Severity** | COHORT - The server is exchanging registration information about an open metadata repository cohort that it is connecting to. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The enterprise connector for the named service has had a new connector to a remote partner added to its federation list. It will begin to include calls to this partner when it processes metadata requests from the calling service.

**User action**

Verify that this message appears for each of the access services operating in this server.


----

### OMRS-AUDIT-0133

> Information about remote connector from server {0} with metadata collection id {1} has been refreshed in enterprise connector for {2}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.REMOTE_MEMBER_DEPLOY_REFRESH` |
| **Severity** | COHORT - The server is exchanging registration information about an open metadata repository cohort that it is connecting to. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The enterprise connector for the named service has had a new information about a remote partner refreshed in its federation list. It will make use of this new information on subsequent calls to this partner when it processes metadata requests from the calling service.

**User action**

Check that this message appears for each of the access services operating in this server.


----

### OMRS-AUDIT-0134

> The remote connector for metadata collection id {0} is no longer being called by {1}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.REMOTE_MEMBER_UNDEPLOYED` |
| **Severity** | COHORT - The server is exchanging registration information about an open metadata repository cohort that it is connecting to. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector has been removed from this access service's federation list of servers that are called during a metadata request.

**User action**

Ensure that this message appears for each of the access services operating in this server.


----

### OMRS-AUDIT-0135

> Configuration for an existing remote member from server {0} for cohort {1} has changed and is being updated in the enterprise connector manager.  It has a metadata collection id of {2}, a metadata collection name of {3}, the server type is {4} and its owning organization is {5}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.REMOTE_MEMBER_UPDATED` |
| **Severity** | COHORT - The server is exchanging registration information about an open metadata repository cohort that it is connecting to. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The enterprise connector manager is preparing to process new connection information from a remote member of the cohort.  It will attempt to create a connector for it in order to validate that the connection object is valid. If all is well, the list of systems that are called when a request is made by the access services is updated with the new member.

**User action**

Look for the confirmation message that indicated that the connection object is valid and has been added into the enterprise connector manager's list.


----

### OMRS-AUDIT-0136

> A {0} exception was received from a remote repository with metadata collection id {1} during {2}.  The exception message was {3}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.UNEXPECTED_EXCEPTION_FROM_REPOSITORY` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The enterprise connector has received an unexpected exception from a remote repository. This exception is saved and may be returned to the caller if the other repositories can not satisfy the caller's request.

**User action**

Investigate whether this exception is the result of an underlying issue in the remote repository.


----

### OMRS-AUDIT-0137

> The remote repository {0} with metadata collection id {1} does not support method {2}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.UNSUPPORTED_REMOTE_FUNCTION` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The cohort member client connector has received an function not supported exception from a remote repository. This exception is remembered and the connector will not call this method again until this local server is restarted.

**User action**

Verify that the repository is known not to support the requested function.


----

### OMRS-AUDIT-0206

> Server {0} that hosts metadata collection {1} has detected that there are two different instances with the same guid of {2}: the instance from {3} has a type of {4} has a provenance type of {5} and the other instance from metadata collection {6} has a type of {7} - the instance in metadata collection {6} should have its guid changed.  The accompanying error message is : {8}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.DUPLICATE_INSTANCES_FOR_GUID` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}`, `{7}`, `{8}` |

**System action**

The notification of this error is distributed around the cohort.  Reference copies of the instance targeted to change its identity are automatically deleted in anticipation of this change. The server targeted to repair its instance will make a single attempt to update its guid.  If this is successful, the updated instance will be distributed to other cohort members.

**User action**

Monitor for further messages.  If the automatic corrective action fails it may be necessary to update the guid of the instance in the other repository using the appropriate reIdentifyEntity() or reIdentifyRelationship() methods or delete and recreate one of the instances. Either of these changes will distribute the instance to the rest of the cohort.


----

### OMRS-AUDIT-0207

> Server {0} that hosts metadata collection {1} has changed the unique identifier (guid) of an instance of type {2} from {3} to {4}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.INSTANCE_SUCCESSFULLY_REIDENTIFIED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The updated instance will be distributed to other cohort members.

**User action**

No action is required.


----

### OMRS-AUDIT-0208

> Server {0} that hosts metadata collection {1} was unable to change the unique identifier (guid) of an instance of type {2} from {3}.  The exception was {4} with error message {5}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.UNABLE_TO_RE_IDENTIFY_INSTANCE` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The server returns an exception to the caller.

**User action**

Review the audit log for previous error messages.  This failed action may have been an attempt to correct a detected conflict.


----

### OMRS-AUDIT-0209

> Server {0} that hosts metadata collection {1} has detected that the version of the type being used by instance with the unique identifier (guid) of {2} has regressed; the type that this server is using locally is {3} and the type from the instance from metadata collection {4} is {5}; the two versions need to be reconciled. The accompanying error message is: {6}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.INSTANCES_WITH_CONFLICTING_TYPES` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}` |

**System action**

The server removes its reference copy of the instance and awaits an updated version.

**User action**

Review the message and decide which instance is correct and update the version of the instance in error.


----

### OMRS-AUDIT-0210

> Server {0} that hosts metadata collection {1} has detected that the version of the type being used by the local instance with the unique identifier (guid) of {2} has regressed; the type that the reporting server is using locally is {3} and the type from the instance from this metadata collection {4} is {5}; the two versions need to be reconciled. The accompanying error message is: {6}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.LOCAL_INSTANCE_WITH_CONFLICTING_TYPES` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}` |

**System action**

The server awaits an updated version.

**User action**

Review the message and decide which instance is correct and update the version of the instance which is in error.


----

### OMRS-AUDIT-0211

> Server {0} that hosts metadata collection {1} was unable to remove the reference copy of an instance of type {2} with unique identifier (guid) of {3}.  The exception was {4} with error message: {5}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.UNABLE_TO_REMOVE_REFERENCE_COPY` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The server will return an exception to the caller.

**User action**

Review the audit log for previous error messages.  This failed action may have been an attempt to correct an identified conflict.


----

### OMRS-AUDIT-0301

> The local server has added a new type called {0} with a unique identifier of {1} and a version number of {2} from {3}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.NEW_TYPE_ADDED` |
| **Severity** | TYPES - Activity is occurring that relates to the open metadata types in use by this server. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The local server will be able to manage metadata instances of this type and version.

**User action**

Validate that this new type is expected and it is appropriate that the local repository supports it.  Egeria supplied native repositories support all types, but other repositories and proxies to third party repositories may not.


----

### OMRS-AUDIT-0302

> The local server cannot add a new type called {0} with a unique identifier of {1} and a version number of {2} because the server does not support this feature

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.NEW_TYPE_NOT_SUPPORTED` |
| **Severity** | TYPES - Activity is occurring that relates to the open metadata types in use by this server. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The local server will not be able to manage metadata instances of this type.

**User action**

Validate that it is expected that this type is not supported by this repository.  Ensure that there is at least one repository in the cohort that does support this type if you need to store metadata of this type.  The Egeria supplied native repositories support all types.


----

### OMRS-AUDIT-0303

> The local server has updated an existing type called {0} with a unique identifier of {1} to version number of {2} using a patch from {3}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.TYPE_UPDATED` |
| **Severity** | TYPES - Activity is occurring that relates to the open metadata types in use by this server. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The local server will be able to manage metadata instances of this latest version of the type.

**User action**

No actions are required.  This message is for information only to record the level of type in use by the local repository.


----

### OMRS-AUDIT-0305

> The local server has detected a conflict with an existing type called {0} with a unique identifier of {1}. This does not match the type name {2} and unique identifier {3} passed to it on a request from {4}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.TYPE_IDENTIFIER_MISMATCH` |
| **Severity** | TYPES - Activity is occurring that relates to the open metadata types in use by this server. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The immediate request fails. The local server will not be able to manage metadata instances of this type from this source.

**User action**

This suggests that two types with the same name have been defined in the cohort. This is a serious error since it may cause metadata to be corrupted.  First check the caller to ensure it is operating properly.  Then investigate the source of the type and any other errors.


----

### OMRS-AUDIT-0306

> A new type called {0} (guid {1}) is being maintained through the API of server {2}.  The method called by user {3} was {4}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.AD_HOC_TYPE_DEFINITION` |
| **Severity** | TYPES - Activity is occurring that relates to the open metadata types in use by this server. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The local server will support this type until the next restart of the server.  After that the instances of this type stored in the local repository will not be retrievable.

**User action**

Using the API in this way is sufficient for development where the repository does not contain valuable metadata.  For production, all types should be defined and maintained through open metadata archives.


----

### OMRS-AUDIT-0307

> Conflicting type definitions for type {0} ({1}) have been detected in remote system {2} ({3}).  Other system involved has metadata collection {4} Error message is: {5}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.REMOTE_TYPE_CONFLICT` |
| **Severity** | TYPES - Activity is occurring that relates to the open metadata types in use by this server. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The open metadata cohort is not running with consistent types.

**User action**

Details of the conflicts and the steps necessary to repair the situation can be found in the audit log. Work to resolve the conflict as soon as possible since this will prevent exchange of instances for this type of metadata.


----

### OMRS-AUDIT-0308

> The local server has detected a type called {0} from {1} with a null unique identifier

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.NULL_TYPE_IDENTIFIER` |
| **Severity** | TYPES - Activity is occurring that relates to the open metadata types in use by this server. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The immediate request fails. The local server will be unable to manage metadata instances of this type from this source.

**User action**

This is a serious error since it may cause metadata to be corrupted.  First check the caller to ensure it is operating properly.  Then investigate the source of the type and any other errors.


----

### OMRS-AUDIT-0309

> The local server has detected a type from {0} with a null unique name. The supplied unique identifier for the type is {1}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.NULL_TYPE_NAME` |
| **Severity** | TYPES - Activity is occurring that relates to the open metadata types in use by this server. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The immediate request fails. The local server will not be able to manage any metadata instances of this type from this source.

**User action**

This suggests that two types with the same name have been defined in the cohort. This is a serious error since it may cause metadata to be corrupted.  First check the caller to ensure it is functioning properly.  Then investigate the source of the type and any other errors.


----

### OMRS-AUDIT-0310

> The local server has been asked to process a request from {0} containing an unrecognized type {1} ({2})

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.UNKNOWN_TYPE` |
| **Severity** | TYPES - Activity is occurring that relates to the open metadata types in use by this server. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The immediate request fails. The local server will not be able to manage metadata instances of this type from this origin.

**User action**

This suggests that two types with the same name have been defined in the cohort. This is a serious error since it may cause metadata to be corrupted.  First check the caller to ensure it is operating correctly.  Then investigate the source of the type and any other errors.


----

### OMRS-AUDIT-0311

> The local server has been asked to process a request from {0} containing a type {1} ({2}) with a null type category

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.NULL_TYPE_CATEGORY` |
| **Severity** | TYPES - Activity is occurring that relates to the open metadata types in use by this server. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The immediate request fails. The local server will not be able to manage any metadata instances of this type from this origin.

**User action**

This is a serious error since it may cause metadata to be corrupted if it is matched to the wrong type.  First check the caller to ensure it is functioning correctly.  Then investigate the source of the type and any other errors.


----

### OMRS-AUDIT-0312

> The local server has been asked to process a request from {0} containing a type {1} ({2}) with an unexpected type category of {3}.  The local type definition has a category of {4}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.UNKNOWN_TYPE_CATEGORY` |
| **Severity** | TYPES - Activity is occurring that relates to the open metadata types in use by this server. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The immediate request fails. The local server will be unable to manage metadata instances of this type from this origin.

**User action**

This is a serious error since it may cause metadata to be corrupted if it is matched to the wrong type.  First check the caller to verify it is operating properly.  Then investigate the source of the type and any other errors.


----

### OMRS-AUDIT-0313

> The local server method {0} has been asked to process a request from {1} that contains a null type

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.NULL_TYPE` |
| **Severity** | TYPES - Activity is occurring that relates to the open metadata types in use by this server. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The immediate request fails. The local server will not be able to manage metadata instances with a null type from this source.

**User action**

This is a serious error since it may cause metadata to be corrupted if it is matched to the wrong type.  First check the caller to verify it is operating correctly.  Then investigate the source of the type and any other errors.


----

### OMRS-AUDIT-0314

> The local server has been asked to process a request from {0} containing a containing an instance with type {1} ({2}) of category {3} but a version of {4}.  The locally supported version is {5}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.TYPE_VERSION_MISMATCH` |
| **Severity** | TYPES - Activity is occurring that relates to the open metadata types in use by this server. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The immediate request fails. The local server will not be able to manage metadata instances with a null GUID from this source.

**User action**

This is a serious error since it may cause metadata to be corrupted if it is matched to the wrong instance.  First check the caller to verify it is functioning correctly.  Then investigate the source of the type and any other errors.


----

### OMRS-AUDIT-0315

> The local server has been asked to process a request from {0} containing a containing an instance with type {1} ({2}) of category {3} but a null unique identifier (GUID)

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.NULL_INSTANCE_ID` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The immediate request fails. The local server will be unable to manage metadata instances with a null GUID from this source.

**User action**

This is a serious error since it may cause metadata to be corrupted if it is matched to the wrong instance.  First check the caller to ensure it is working properly.  Then investigate the source of the type and any other errors.


----

### OMRS-AUDIT-0316

> The local server method {0} has been asked to process a request from {1} containing an instance with unique identifier {2} and type {3} ({4}) of category {5} but a null unique home metadata collection identifier (GUID)

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.NULL_METADATA_COLLECTION_ID` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The immediate request fails. The local server will not be able to manage metadata instances with a null metadata collection GUID because it is not clear which repository has update rights..

**User action**

This is a serious error since it may cause metadata to be corrupted if it is matched to the wrong instance.  First check the caller to ensure it is working correctly.  Then investigate the source of the type and any other errors.


----

### OMRS-AUDIT-0317

> The local server method {0} has been asked to process a request from {1} containing a null instance

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.NULL_INSTANCE` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The immediate request fails. The local server will not be able to manage any metadata instances with a null GUID from this source.

**User action**

This is a serious error since it may cause metadata to be corrupted if it is matched to the wrong instance.  First check the caller to ensure it is working as expected.  Then investigate the source of the type and any other errors.


----

### OMRS-AUDIT-0318

> The local server has recognized a new type called {0} with a unique identifier of {1} and a version number of {2} from {3}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.TYPE_ALREADY_KNOWN` |
| **Severity** | TYPES - Activity is occurring that relates to the open metadata types in use by this server. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The local server already has knowledge of the new type and so it is able to store metadata of this type.

**User action**

No action is needed since you have a repository that has embedded support for the type.


----

### OMRS-AUDIT-0319

> The local server does not have a local repository and so a new type called {0} with a unique identifier of {1} and a version number of {2} from {3} is just cached for information

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.NEW_TYPE_CACHED_FOR_ENTERPRISE` |
| **Severity** | TYPES - Activity is occurring that relates to the open metadata types in use by this server. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The local server stores a copy of this type for use in enterprise repository queries - typically from Open Metadata Access Services (OMASs).

**User action**

Validate that there is at least once repository in the cohort that supports the storing of this type if you wish to manage metadata of this type.


----

### OMRS-AUDIT-0320

> A patch to the {0} ({1}) type definition was ignored because it is to be applied to the {2} version of the type and the patch is for version {3}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.TYPE_UPDATE_SKIPPED` |
| **Severity** | TYPES - Activity is occurring that relates to the open metadata types in use by this server. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The level of support for this type in the local repository remains at the current level.  This means that metadata instances that use the later version of the type can not be stored in this repository.

**User action**

Validate that types are loading correctly in the server since this may indicate that a patch has been missed.If the repository is not able to support the updating of types, or can not support the later versions of this type then this message is expected.  If there was an issue in the type loading then reload the type archives in the correct order to apply the intermediate patches.


----

### OMRS-AUDIT-0321

> A patch to the {0} ({1}) type definition from {2} was ignored because the local repository does not support this type

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.UNKNOWN_TYPE_UPDATE_SKIPPED` |
| **Severity** | TYPES - Activity is occurring that relates to the open metadata types in use by this server. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The type remains unsupported in this repository.

**User action**

No action is required since this type is out of scope for this repository.


----

### OMRS-AUDIT-0322

> The local server does not have a local repository and so an update to the type called {0} with a unique identifier of {1} that applies to version number of {2} from {3} is just cached for information

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.UPDATED_TYPE_CACHED_FOR_ENTERPRISE` |
| **Severity** | TYPES - Activity is occurring that relates to the open metadata types in use by this server. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The local server stores a copy of the updated type for use in enterprise repository queries - typically from Open Metadata Access Services (OMASs).

**User action**

Validate that there is at least once repository in the cohort that supports the storing of this version of the type if you wish to manage metadata of this type.


----

### OMRS-AUDIT-0401

> Skipping call to repository {0} since it is not responding correctly.  Error received was {1} with message {2}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.SKIPPING_METADATA_COLLECTION` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The local server is processing a federated query to all members of the connected cohorts.  However one of the members is not responding correctly and so it has been skipped from the call. The remote server is probably not running, or has been incorrectly configured.

**User action**

Validate the availability and configuration of the remote server.  It may be a temporary failure due to an outage in the network or the server itself.  However, if the remote server is not configured correctly, or has changed its metadata collection id, then this wil lbe a permanent error and this server will not be included in the federated query until it is fixed.


----

### OMRS-AUDIT-8001

> Received unknown event: {0}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.PROCESS_UNKNOWN_EVENT` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}` |

**System action**

The local server has received an unknown event from another member of the metadata repository cohort and cannot process it. This is possible if a server in the cohort is at a higher level than this server and is using a more advanced version of the protocol. The local server should continue to operate correctly.

**User action**

Verify that the event is a new event type introduced after this server was written.


----

### OMRS-AUDIT-8002

> A later version of the instance {0} from server {1} and metadata collection {2} is using an older version of the type {3}.  Previous type header was {4}, new type header is {5}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.PROCESS_INSTANCE_TYPE_CONFLICT` |
| **Severity** | TYPES - Activity is occurring that relates to the open metadata types in use by this server. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The local server has received an event from another member of the open metadata repository cohort containing suspicious information.  The local server has enacted conflict processing.

**User action**

Validate and correct the instance in the originator.  Monitor events from all members of the cohort to ensure all copies are corrected.


----

### OMRS-AUDIT-8003

> Version {0} of the instance {1} from server {2} has a new metadata collection id of {3}.  Its original metadata collection id was {4}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.NEW_HOME_INFORMATION` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The local server has received an event from another member of the open metadata repository cohort containing different header information .

**User action**

Validate and, if necessary, correct the home of the instance in the originator.  Monitor events from all members of the cohort to ensure all copies are consistent.


----

### OMRS-AUDIT-8004

> Version {0} of the instance {1} from server {2} and metadata collection {3} has a new type of {4} ({5}).  Previous type was {6} ({7})

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.NEW_TYPE_INFORMATION` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}`, `{7}` |

**System action**

The local server has received an event from another member of the open metadata repository cohort containing suspicious information.  The local server has commenced conflict processing.

**User action**

Validate and, if necessary, correct the type of the instance in the originator.  Monitor events from all members of the cohort to ensure all copies are updated.


----

### OMRS-AUDIT-8005

> An instance of type {0} ({1}) from server {2} and metadata collection {3} is using the same guid {4} as a stored instance of type {5} ({6})

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.PROCESS_INSTANCE_GUID_CONFLICT` |
| **Severity** | ACTION - Action is required by the administrator. At a minimum, the situation needs to be investigated and if necessary, corrective action taken. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}` |

**System action**

The local server has received an event from another member of the open metadata repository cohort that suggests that two different entities have the same unique identifier (guid).

**User action**

Validate and correct the guid of the instance in the originator.  Monitor events from all members of the cohort to ensure all copies are corrected.


----

### OMRS-AUDIT-8006

> Processing incoming event of type {0} for instance {1} from: {2}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.PROCESS_INCOMING_EVENT` |
| **Severity** | EVENT - An event was sent to or received from another participant in the server's ecosystem. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The local server has processed an event from another member of the metadata repository.

**User action**

This message is for audit purposes only.  If a record of incoming event is needed, ensure these types of events are routed to a permanent store


----

### OMRS-AUDIT-8007

> An invalid instance was found in a batch of reference instances send by a remote member of the cohort. The exception was {0} with message {1}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.INVALID_INSTANCES` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The instances that appear earlier in the batch have been processed.  However the server will not process any more of the batch in case there are other problems with it.

**User action**

Review the instances from the event (passed as additional information on this log message) to determine the source of the error and its resolution.


----

### OMRS-AUDIT-8008

> Retrying retrieve of an entity {0} for user {1} because only a proxy is available - attempt {2}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.RETRY_FOR_PROXY` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The enterprise connector is retrying the call to retrieve an entity from the cohort because a proxy (or no entity) has been returned.  Since most requests for entities are made with valid GUID, this suggests one of the repositories is not current registered and so the hope is that by retrying, the entity is returned on a subsequent attempt.

**User action**

If this message occurs frequently then seek to improve the availability of the cohort members.


----

### OMRS-AUDIT-8009

> The Open Metadata Repository Services (OMRS) has sent event of type {0} to the cohort topic {1}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.OUTBOUND_TOPIC_EVENT` |
| **Severity** | EVENT - An event was sent to or received from another participant in the server's ecosystem. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

This message is to create a record of the events that are being published.

**User action**

Validate that the server is sending the events that are expected


----

### OMRS-AUDIT-9002

> Unable to process a received event from topic {0} because its content is null

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.NULL_OMRS_EVENT_RECEIVED` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}` |

**System action**

The system cannot process an incoming event.

**User action**

This may be caused by an internal logic error or the receipt of an incompatible OMRSEvent, possibly from a later version of the OMRS protocol


----

### OMRS-AUDIT-9003

> Unable to send a TypeDef event for cohort {0} due to an error in the OMRS Topic Connector

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.SEND_TYPEDEF_EVENT_ERROR` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}` |

**System action**

The local server cannot properly manage TypeDef events for the metadata repository cohort. The cause of the error is recorded in the accompanying exception.

**User action**

Review the exception and resolve the issue it documents.  Then restart this server and it will send out its type information to the cohort.


----

### OMRS-AUDIT-9005

> Unable to send or receive a metadata instance event for cohort {0} due to an error in the OMRS Topic Connector

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.SEND_INSTANCE_EVENT_ERROR` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}` |

**System action**

The local server cannot properly manage the replication of metadata instances for the metadata repository cohort. The cause of the error is recorded in the accompanying exception.

**User action**

Review the exception and resolve the issue it documents. This often includes correcting the configuration document for the server.  Once this is done, restart the server and it will reconnect to the cohort.


----

### OMRS-AUDIT-9011

> An incoming event of type {0} from {1} ({2}) generated an exception of type {3} with message {4}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.UNEXPECTED_EXCEPTION_FROM_EVENT` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The contents of the event were not accepted by the local repository.

**User action**

Review the exception and resolve the issue that the local repository detected.


----

### OMRS-AUDIT-9012

> Disconnecting from the {0} enterprise topic connector generated a {1} exception with message {2}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.ENTERPRISE_TOPIC_DISCONNECT_ERROR` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The server may not have disconnected from the topic cleanly.

**User action**

Review the exception and resolve any issues with the topic listener that it documents.


----

### OMRS-AUDIT-9013

> Disconnecting of the enterprise connector manager generated an exception with message {0}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.ENTERPRISE_CONNECTOR_DISCONNECT_ERROR` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}` |

**System action**

The server may not have disconnected from all repositories cleanly.

**User action**

Review the exception and resolve any issue with the enterprise connectors or enterprise connector manager it documents.


----

### OMRS-AUDIT-9014

> The Open Metadata Repository Service has generated an unexpected {0} exception during method {1}.  The message was {2}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The request returns an exception to the caller.  The exception and stack trace is added to the audit log

**User action**

This is probably a logic error. Review the stack trace to identify where the error occurred and work to resolve the cause.


----

### OMRS-AUDIT-9015

> {0} has passed the {1} an invalid instance within an inbound event reportedly to be from server {2} of type {3} and owned by organization {4}.  The instance is from metadata collection {5} and is of this format: {6}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.BAD_EVENT_INSTANCE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}` |

**System action**

The service detected an error during the processing of an inbound event.  The event is not processed.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, new inbound events should be processed correctly.


----

### OMRS-AUDIT-9016

> The topic listener for the {0} service caught an unexpected exception {1} with message {2}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.UNEXPECTED_EXCEPTION_FROM_SERVICE_LISTENER` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The contents of the event were not accepted by the topic listener.

**User action**

Review the exception and resolve the issue that it documents.


----

### OMRS-AUDIT-9017

> The topic listener for the {0} service threw an unexpected exception {1} with message {2}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.UNHANDLED_EXCEPTION_FROM_SERVICE_LISTENER` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The contents of the event were unable to be accepted by the topic listener.

**User action**

Review the exception and resolve the issue with the event that it documents.


----

### OMRS-AUDIT-9018

> Exception {0} occurred when processing a type {1} from {2}.  The originator of the type was {3} ({4}).  The message in the exception was {5}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.UNEXPECTED_EXCEPTION_FROM_TYPE_PROCESSING` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The contents of the type were not accepted by the type definition processor.

**User action**

Review the exception and resolve the issue that it documents.  The new type information will not be usable in this server


----

### OMRS-AUDIT-9019

> The type definition event processor for the {0} service caught an unexpected exception {1} with message {2}

|  |  |
|---|---|
| **Java constant** | `OMRSAuditCode.UNHANDLED_EXCEPTION_FROM_TYPE_PROCESSING` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The contents of the type were not accepted by the topic listener.

**User action**

Review the exception and resolve the issue with the type that it documents.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.

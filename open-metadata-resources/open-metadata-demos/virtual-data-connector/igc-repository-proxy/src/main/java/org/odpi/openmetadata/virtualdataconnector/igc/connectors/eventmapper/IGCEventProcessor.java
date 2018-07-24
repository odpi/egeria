/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.virtualdataconnector.igc.connectors.eventmapper;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentHelper;
import org.odpi.openmetadata.virtualdataconnector.igc.connectors.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventManager;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;

/**
 * Process individual IGC Kafka events and publish them through OMRS.
 */
public class IGCEventProcessor {

    private OMRSRepositoryHelper repositoryHelper;
    private IGCOMRSRepositoryConnector igcomrsRepositoryConnector;
    private OMRSRepositoryEventManager omrsRepositoryEventManager;

    private IGCColumn igcColumn;
    private String originatorServerName;
    private String businessTerm;


    /**
     * Process individual IGC Kafka events and publish them through OMRS.
     *
     * @param igcKafkaEvent a Kafka event originating from IGC.
     */
    public  void process(IGCKafkaEvent igcKafkaEvent) {
        if (igcKafkaEvent.getASSETTYPE().equals("Database Column") &&
                (
                igcKafkaEvent.getACTION().equals("ASSIGNED_RELATIONSHIP") || igcKafkaEvent.getACTION().equals("MODIFY")
                )
            ) {
            try {
                igcColumn = igcomrsRepositoryConnector.queryIGCColumn(igcKafkaEvent.getASSETRID());
                originatorServerName = igcColumn.getName();
                if (igcColumn.getAssignedToTerms() != null) {
                    businessTerm = igcColumn.getAssignedToTerms().getItems().get(0).getName();
                    Relationship relationship = newRelationship("SemanticAssignment", igcKafkaEvent.getASSETRID(), "RelationalColumn", igcColumn.getAssignedToTerms().getItems().get(0).getId(), "GlossaryTerm");
                    omrsRepositoryEventManager.processNewRelationshipEvent(
                            "IGCOMRSRepositoryEventMapper",
                            igcomrsRepositoryConnector.getMetadataCollectionId(),
                            originatorServerName,
                            "IGC",
                            "",
                            relationship
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Set Relationship linking an IGC technical term with an IGC business term
     *
     * @param relationshipType      name of the type
     * @param entityId1             String unique identifier
     * @param entityType1           name of the type
     * @param entityId2             String unique identifier
     * @param entityType2           name of the type
     * @return                      Relationship is a POJO that manages the properties of an open metadata relationship.  This includes information
     *                              about the relationship type, the two entities it connects and the properties it holds.
     *
     * @throws TypeErrorException   The type name is not recognized as a relationship type.
     */
    private  Relationship newRelationship(String relationshipType, String entityId1,String entityType1, String entityId2, String entityType2) throws TypeErrorException {
        Relationship relationship = initiateRelationship(relationshipType);

        EntityProxy entityProxyOne = newEntityProxy(entityType1);
        EntityProxy entityProxyTwo = newEntityProxy(entityType2);

        entityProxyOne.setGUID(entityId1);
        entityProxyTwo.setGUID(entityId2);

        relationship.setEntityOnePropertyName(originatorServerName);
        relationship.setEntityOneProxy(entityProxyOne);


        relationship.setEntityTwoPropertyName(businessTerm);
        relationship.setEntityTwoProxy(entityProxyTwo);
        return relationship;
    }

    /**
     *  Return a filled out relationship which just needs the entity proxies added.
     *
     * @param relationshipType      name of the type
     * @return                      a filled out relationship.
     * @throws TypeErrorException   the type name is not recognized as a relationship type.
     */
    private  Relationship initiateRelationship(String relationshipType) throws TypeErrorException {
        return repositoryHelper.getNewRelationship(
                "IGCOMRSRepositoryEventMapper",
                igcomrsRepositoryConnector.getMetadataCollectionId(),
                InstanceProvenanceType.LOCAL_COHORT,
                "",
                relationshipType,
                null
        );
    }

    /**
     * Return a filled out entity.
     *
     * @param typeName              name of the type
     * @return                      an entity that is filled out
     * @throws TypeErrorException   the type name is not recognized as an entity type
     */
    private EntityProxy newEntityProxy(String typeName) throws TypeErrorException {
        EntityProxy entityProxy = repositoryHelper.getNewEntityProxy(
                "IGCOMRSRepositoryEventMapper",
                igcomrsRepositoryConnector.getMetadataCollectionId(),
                InstanceProvenanceType.LOCAL_COHORT,
                "",
                typeName,
                null,
                null
        );
        entityProxy.setStatus(InstanceStatus.ACTIVE);
        return entityProxy;
    }

    /**
     * The IGCOMRSRepositoryConnector is a connector to a remote IBM Information Governance Catalog (IGC) repository.
     * @param igcomrsRepositoryConnector
     */
    public void setIgcomrsRepositoryConnector(IGCOMRSRepositoryConnector igcomrsRepositoryConnector) {
        this.igcomrsRepositoryConnector = igcomrsRepositoryConnector;
    }

    /**
     * OMRSRepositoryEventManager is responsible for managing the distribution of TypeDef and instance events.
     * There is one OMRSRepositoryEventManager for each cohort that the local server is registered with and one for
     * the local repository.
     *
     * Since OMRSRepositoryEventManager sits at the crossroads of the flow of events between the cohorts,
     * the local repository and the enterprise access components, it performs detailed error checking of the
     * event contents to help assure the integrity of the open metadata ecosystem.
     *
     * @param omrsRepositoryEventManager
     */
    public void setOmrsRepositoryEventManager(OMRSRepositoryEventManager omrsRepositoryEventManager) {
        this.omrsRepositoryEventManager = omrsRepositoryEventManager;
    }

    /**
     * OMRSRepositoryHelper provides methods to repository connectors and repository event mappers to help
     * them build valid type definitions (TypeDefs), entities and relationships.  It is a facade to the
     * repository content manager which holds an in memory cache of all the active TypeDefs in the local server.
     * OMRSRepositoryHelper's purpose is to create an object that the repository connectors and event mappers can
     * create, use and discard without needing to know how to connect to the repository content manager.
     *
     * @param repositoryHelper
     */
    public void setRepositoryHelper(OMRSRepositoryHelper repositoryHelper) {
        this.repositoryHelper = repositoryHelper;
    }
}

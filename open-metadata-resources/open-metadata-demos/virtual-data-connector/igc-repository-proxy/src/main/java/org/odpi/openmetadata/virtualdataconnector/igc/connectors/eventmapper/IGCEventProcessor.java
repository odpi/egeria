/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.virtualdataconnector.igc.connectors.eventmapper;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentHelper;
import org.odpi.openmetadata.virtualdataconnector.igc.connectors.repositoryconnector.IGCOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventManager;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;


public class IGCEventProcessor {

    private IGCColumn igcColumn;
    private String originatorServerName;
    private OMRSRepositoryHelper repositoryHelper;
    private IGCOMRSRepositoryConnector igcomrsRepositoryConnector;
    private OMRSRepositoryEventManager omrsRepositoryEventManager;
    private String businessTerm;


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

    private  Relationship newRelationship(String relationshipType, String entityId1,String entityType1, String entityId2, String entityType2) throws TypeErrorException {
        //Set Relationship linking technical term with IGC business term
        Relationship relationship = initiateRelationship(relationshipType);

        EntityProxy entityProxyOne = newEntityProxy(entityType1);//TODO
        EntityProxy entityProxyTwo = newEntityProxy(entityType2);

        entityProxyOne.setGUID(entityId1);
        entityProxyTwo.setGUID(entityId2);

        relationship.setEntityOnePropertyName(originatorServerName);
        relationship.setEntityOneProxy(entityProxyOne);


        relationship.setEntityTwoPropertyName(businessTerm);
        relationship.setEntityTwoProxy(entityProxyTwo);
        return relationship;
    }

    private  Relationship initiateRelationship(String relationshipType) throws TypeErrorException {
        return new OMRSRepositoryContentHelper().getNewRelationship(
                "IGCOMRSRepositoryEventMapper",
                igcomrsRepositoryConnector.getMetadataCollectionId(),
                InstanceProvenanceType.LOCAL_COHORT,
                "",
                relationshipType,
                null
        );
    }

    private EntityProxy newEntityProxy(String typeName) throws TypeErrorException {
        EntityProxy entityProxy = new OMRSRepositoryContentHelper().getNewEntityProxyFORDEMO(
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


    public void setIgcomrsRepositoryConnector(IGCOMRSRepositoryConnector igcomrsRepositoryConnector) {
        this.igcomrsRepositoryConnector = igcomrsRepositoryConnector;
    }

    public void setOmrsRepositoryEventManager(OMRSRepositoryEventManager omrsRepositoryEventManager) {
        this.omrsRepositoryEventManager = omrsRepositoryEventManager;
    }

    public void setRepositoryHelper(OMRSRepositoryHelper repositoryHelper) {
        this.repositoryHelper = repositoryHelper;
    }
}

package org.odpi.openmetadata.accessservices.assetlineage.eventProcessors;

import org.odpi.openmetadata.accessservices.assetlineage.model.GlossaryTerm;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;

import java.util.List;

import static org.odpi.openmetadata.accessservices.assetlineage.eventProcessors.EventProcessor.instanceHandler;

public class GlossaryTermBuilder {

    private String serverName;
    private String userId;
    private OMRSMetadataCollection metadataCollectionForSearch;

    public GlossaryTermBuilder(String serverName, String userID) {
        this.serverName = serverName;
        this.userId = userID;
    }

    public GlossaryTerm getGlossaryTerm(EntityProxy entityProxy) {
        GlossaryTerm glossaryTerm = new GlossaryTerm();
        String GUID = entityProxy.getGUID();

        InstancePropertyValue qualifiedNameInstancePropertyValue = entityProxy.getUniqueProperties().getInstanceProperties().get("qualifiedName");
        PrimitivePropertyValue qualifiedNamePrimitivePropertyValue = (PrimitivePropertyValue) qualifiedNameInstancePropertyValue;
        String qualifiedName = qualifiedNamePrimitivePropertyValue.getPrimitiveValue().toString();

        glossaryTerm.setGuid(GUID);
        glossaryTerm.setQualifiedName(qualifiedName);

        try {

            this.metadataCollectionForSearch = instanceHandler.getMetadataCollection(serverName);
            EntityDetail entityDetail = metadataCollectionForSearch.getEntityDetail(userId, GUID);
            InstancePropertyValue displayNameInstancePropertyValue  = entityDetail.getProperties().getPropertyValue("displayName");
            PrimitivePropertyValue displayNamePrimitivePropertyValue = (PrimitivePropertyValue) displayNameInstancePropertyValue;
            String displayName = displayNamePrimitivePropertyValue.getPrimitiveValue().toString();

            //TODO Maybe classifications and displayname shouldn't be in the same try block. We only want to query once though.
            List<Classification> classifications = entityDetail.getClassifications();

            glossaryTerm.setDisplayName(displayName);
            glossaryTerm.setClassifications(classifications);
        } catch (Exception e) {
            //TODO Proper auditlog error logging!
            e.printStackTrace();
        }
        return glossaryTerm;
    }
}
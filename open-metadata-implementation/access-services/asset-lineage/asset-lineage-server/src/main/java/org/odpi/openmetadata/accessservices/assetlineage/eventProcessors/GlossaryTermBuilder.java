/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetlineage.eventProcessors;

import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exception.PropertyServerException;
import org.odpi.openmetadata.accessservices.assetlineage.model.GlossaryTerm;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;

import java.util.List;

import static org.odpi.openmetadata.accessservices.assetlineage.eventProcessors.EventProcessor.instanceHandler;

public class GlossaryTermBuilder {

    private String serverName;
    private String userId;
    private OMRSMetadataCollection metadataCollectionForSearch;

    //TODO Replace print stacktraces with proper audit logging!
    public GlossaryTermBuilder(String serverName, String userID) {
        this.serverName = serverName;
        this.userId = userID;
        try {
            this.metadataCollectionForSearch = instanceHandler.getMetadataCollection(serverName);
        } catch (PropertyServerException e) {
            e.printStackTrace();
        }
    }

    public GlossaryTerm getGlossaryTerm(EntityProxy entityProxy) {
        GlossaryTerm glossaryTerm = new GlossaryTerm();
        String GUID = entityProxy.getGUID();

        InstancePropertyValue qualifiedNameInstancePropertyValue = entityProxy.getUniqueProperties().getInstanceProperties().get("qualifiedName");
        PrimitivePropertyValue qualifiedNamePrimitivePropertyValue = (PrimitivePropertyValue) qualifiedNameInstancePropertyValue;
        String qualifiedName = qualifiedNamePrimitivePropertyValue.getPrimitiveValue().toString();

        glossaryTerm.setGuid(GUID);
        glossaryTerm.setQualifiedName(qualifiedName);
        EntityDetail entityDetail = null;
        try {
            entityDetail = metadataCollectionForSearch.getEntityDetail(userId, GUID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        InstancePropertyValue displayNameInstancePropertyValue = entityDetail.getProperties().getPropertyValue("displayName");
        PrimitivePropertyValue displayNamePrimitivePropertyValue = (PrimitivePropertyValue) displayNameInstancePropertyValue;
        String displayName = displayNamePrimitivePropertyValue.getPrimitiveValue().toString();

        List<Classification> classifications = entityDetail.getClassifications();

        glossaryTerm.setDisplayName(displayName);
        glossaryTerm.setClassifications(classifications);
        return glossaryTerm;
    }
}
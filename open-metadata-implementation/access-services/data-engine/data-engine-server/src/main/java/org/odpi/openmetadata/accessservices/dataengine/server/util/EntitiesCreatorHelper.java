/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.util;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * EntitiesCreatorHelper manages the creation of entities and relationships. It runs server-side in the Data Engine
 * OMAS and creates entities and relationships through the OMRSRepositoryConnector.
 */
public class EntitiesCreatorHelper {
    private static final String NAME_PROPERTY_NAME = "name";
    private static final String OWNER_PROPERTY_NAME = "owner";
    private static final String LATEST_CHANGE_PROPERTY_NAME = "latestChange";
    private static final String DESCRIPTION_PROPERTY_NAME = "description";
    private static final String ZONE_MEMBERSHIP_PROPERTY_NAME = "zoneMembership";
    private static final String QUALIFIED_NAME_PROPERTY_NAME = "qualifiedName";
    private static final String DISPLAY_NAME_PROPERTY_NAME = "displayName";

    private OMRSMetadataCollection metadataCollection;
    private OMRSRepositoryHelper repositoryHelper;
    private String serviceName;

    /**
     * Construct the process handler with a link to the property server's connector, a link to the metadata collection
     * and this access service's official name.
     *
     * @param serviceName        name of this service
     * @param repositoryHelper   helper object to parse entity/relationship
     * @param metadataCollection accessor object for the property server
     */
    public EntitiesCreatorHelper(String serviceName, OMRSRepositoryHelper repositoryHelper,
                                 OMRSMetadataCollection metadataCollection) {

        this.serviceName = serviceName;
        this.repositoryHelper = repositoryHelper;
        this.metadataCollection = metadataCollection;

    }

    /**
     * @param userId             the name of the calling user
     * @param instanceProperties list of properties for the new entity
     * @param typeName           name of the type
     *
     * @return the guid of the created entity
     *
     * @throws TypeErrorException unknown or invalid type
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws RepositoryErrorException no metadata collection
     * @throws PropertyErrorException here is a problem with one of the other parameters
     * @throws ClassificationErrorException the requested classification is either not known or not valid for the entity
     * @throws StatusNotSupportedException status not supported
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException the requesting user is
     * not authorized to issue this request
     */
    public String createEntity(String userId, InstanceProperties instanceProperties, String typeName) throws
                                                                                                      TypeErrorException,
                                                                                                      InvalidParameterException,
                                                                                                      RepositoryErrorException,
                                                                                                      PropertyErrorException,
                                                                                                      ClassificationErrorException,
                                                                                                      StatusNotSupportedException,
                                                                                                      org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException {

        EntityDetail entity = repositoryHelper.getSkeletonEntity(serviceName, "",
                InstanceProvenanceType.LOCAL_COHORT, userId, typeName);

        EntityDetail createdEntity = metadataCollection.addEntity(userId, entity.getType().getTypeDefGUID(),
                instanceProperties, entity.getClassifications(), entity.getStatus());

        return createdEntity.getGUID();
    }

    /**
     * @param userId        the name of the calling user
     * @param typeName      name of the relationship's type
     * @param entityOneGUID the unique identifier of one of the entities that the relationship is connecting together.
     * @param entityTwoGUID the unique identifier of the other entity that the relationship is connecting together.
     *
     * @throws TypeErrorException unknown or invalid type
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws RepositoryErrorException no metadata collection
     * @throws PropertyErrorException here is a problem with one of the other parameters
     * @throws StatusNotSupportedException status not supported
     * @throws org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException the requesting user is
     * not authorized to issue this request
     */
    public void addRelationship(String userId, String typeName, String entityOneGUID, String entityTwoGUID)
            throws TypeErrorException, InvalidParameterException, RepositoryErrorException, PropertyErrorException,
                   EntityNotKnownException, StatusNotSupportedException,
                   org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException {

        Relationship relationship = repositoryHelper.getSkeletonRelationship(serviceName, "",
                InstanceProvenanceType.LOCAL_COHORT, userId, typeName);

        metadataCollection.addRelationship(userId, relationship.getType().getTypeDefGUID(), null,
                entityOneGUID, entityTwoGUID, InstanceStatus.ACTIVE);
    }

    /**
     * Create an instance properties object for an asset
     *
     * @param name           the name of the asset
     * @param description    the description of the asset
     * @param latestChange   the description for the latest change done for the asset
     * @param zoneMembership the list of zones of the asset
     *
     * @return instance properties object
     */
    public InstanceProperties createAssetInstanceProperties(String userId, String name, String description,
                                                            String latestChange, List<String> zoneMembership) {
        final String methodName = "createAssetInstanceProperties";

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName, null,
                NAME_PROPERTY_NAME, name, methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, QUALIFIED_NAME_PROPERTY_NAME,
                name + ":" + new Date().toString(), methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, DESCRIPTION_PROPERTY_NAME,
                description, methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, LATEST_CHANGE_PROPERTY_NAME,
                latestChange, methodName);
        properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, OWNER_PROPERTY_NAME,
                userId, methodName);

        addZoneMembershipToInstance(zoneMembership, properties);

        return properties;
    }



    /**
     * Create an instance properties object for a port entity
     *
     * @param displayName the displayName for the port entity
     *
     * @return instance properties object
     */
    public InstanceProperties createPortInstanceProperties(String displayName) {
        final String methodName = "createPortInstanceProperties";

        InstanceProperties properties = repositoryHelper.addStringPropertyToInstance(serviceName, null,
                DISPLAY_NAME_PROPERTY_NAME, displayName, methodName);

        properties = repositoryHelper.addStringPropertyToInstance(serviceName, properties, QUALIFIED_NAME_PROPERTY_NAME,
                displayName + ":" + new Date().toString(), methodName);

        return properties;
    }

    private void addZoneMembershipToInstance(List<String> zoneMembership, InstanceProperties properties) {
        if (StringUtils.isEmpty(zoneMembership)) {
            return;
        }

        if (properties == null) {
            properties = new InstanceProperties();
        }

        ArrayPropertyValue arrayPropertyValue = new ArrayPropertyValue();
        arrayPropertyValue.setArrayCount(zoneMembership.size());

        for (String zone : zoneMembership) {
            PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();
            primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
            primitivePropertyValue.setPrimitiveValue(zone);

            arrayPropertyValue.setArrayValue(zoneMembership.indexOf(zone), primitivePropertyValue);
        }

        properties.setProperty(ZONE_MEMBERSHIP_PROPERTY_NAME, arrayPropertyValue);
    }
}

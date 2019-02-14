/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.contentmanager;


import org.odpi.openmetadata.accessservices.assetlineage.events.*;
import org.odpi.openmetadata.accessservices.assetlineage.utils.Constants;
import org.odpi.openmetadata.accessservices.assetlineage.utils.EntityPropertiesUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ColumnContextEventBuilder loads the full context for a column from a relational table
 */
public class ColumnContextEventBuilder {

    private static final Logger log = LoggerFactory.getLogger(ColumnContextEventBuilder.class);

    private OMRSRepositoryConnector enterpriseConnector;

    /**
     * @param enterpriseConnector - combined connector for all repositories
     */
    public ColumnContextEventBuilder(OMRSRepositoryConnector enterpriseConnector) {
        this.enterpriseConnector = enterpriseConnector;
    }

    public EntityDetail getEntity(String guid) throws RepositoryErrorException, UserNotAuthorizedException, EntityProxyOnlyException, InvalidParameterException, EntityNotKnownException {
        return enterpriseConnector.getMetadataCollection().getEntityDetail(Constants.USER_ID, guid);
    }

    public BusinessTerm buildBusinessTerm(EntityDetail businessTermEntity) {
        BusinessTerm businessTerm = new BusinessTerm();
        businessTerm.setGuid(businessTermEntity.getGUID());
        businessTerm.setQualifiedName(EntityPropertiesUtils.getStringValueForProperty(businessTermEntity.getProperties(), Constants.QUALIFIED_NAME));
        businessTerm.setSummary(EntityPropertiesUtils.getStringValueForProperty(businessTermEntity.getProperties(), Constants.SUMMARY));
        businessTerm.setName(EntityPropertiesUtils.getStringValueForProperty(businessTermEntity.getProperties(), Constants.DISPLAY_NAME));
        businessTerm.setExamples(EntityPropertiesUtils.getStringValueForProperty(businessTermEntity.getProperties(), Constants.EXAMPLES));
        businessTerm.setAbbreviation(EntityPropertiesUtils.getStringValueForProperty(businessTermEntity.getProperties(), Constants.ABBREVIATION));
        businessTerm.setQuery(EntityPropertiesUtils.getStringValueForProperty(businessTermEntity.getProperties(), Constants.QUERY));
        businessTerm.setDescription(EntityPropertiesUtils.getStringValueForProperty(businessTermEntity.getProperties(), Constants.DESCRIPTION));
        businessTerm.setUsage(EntityPropertiesUtils.getStringValueForProperty(businessTermEntity.getProperties(), Constants.USAGE));
        return businessTerm;
    }

}

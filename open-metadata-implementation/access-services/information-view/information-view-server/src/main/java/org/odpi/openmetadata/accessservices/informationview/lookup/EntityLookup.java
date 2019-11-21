/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.lookup;


import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.events.Source;
import org.odpi.openmetadata.accessservices.informationview.ffdc.exceptions.runtime.InformationViewExceptionBase;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler.buildMultipleEntitiesMatchingException;
import static org.odpi.openmetadata.accessservices.informationview.ffdc.ExceptionHandler.buildNoMatchingEntityException;

public abstract class EntityLookup<T extends Source> {

    private static final Integer PAGE_SIZE = 0;
    private static final Logger log = LoggerFactory.getLogger(EntityLookup.class);
    protected OMRSRepositoryConnector enterpriseConnector;
    protected OMEntityDao omEntityDao;
    protected OMRSAuditLog auditLog;
    protected EntityLookup parentChain;
    protected String equivalentOMType;

    public EntityLookup(OMRSRepositoryConnector enterpriseConnector, OMEntityDao omEntityDao, EntityLookup parentChain, OMRSAuditLog auditLog, String equivalentOMType) {
        this.enterpriseConnector = enterpriseConnector;
        this.omEntityDao = omEntityDao;
        this.auditLog = auditLog;
        this.parentChain = parentChain;
        this.equivalentOMType = equivalentOMType;
    }

    public void setParentChain(EntityLookup parentChain) {
        this.parentChain = parentChain;
    }

    public EntityDetail lookupEntity(T source){
        if(!StringUtils.isEmpty(source.getGuid())){
            return omEntityDao.getEntityByGuid(source.getGuid());
        }
        if(!StringUtils.isEmpty(source.getQualifiedName())){
            return omEntityDao.getEntity(equivalentOMType, source.getQualifiedName(), false);
        }
        return null;
    }

    /**
     * Returns the entity matching the criteria or null if none matches
     * @param typeNames - list of types to use for lookup
     * @param source - the source against which we want to match
     * @param list of entities in which to search
     * @return entity matching type and source
     */
    public EntityDetail filterEntities(List<String> typeNames, T source, List<EntityDetail> list)  {
        List<EntityDetail> filteredList = list.stream().filter(e -> isTypeOf(typeNames, e)).collect(Collectors.toList());
        InstanceProperties matchProperties = getMatchingProperties(source);
        return matchExactlyToUniqueEntity(filteredList, matchProperties);
    }

    /**
     * Return true if the entity type is among the list of types passed as argument
     * @param typeNames list of types to check
     * @param e entity to check
     * @return true if the type is the expected one
     */
    private boolean isTypeOf(List<String> typeNames, EntityDetail e) {
        return typeNames.stream().anyMatch(type -> enterpriseConnector.getRepositoryHelper().isTypeOf("lookupEntity", e.getType().getTypeDefName(), type));
    }

    /**
     *
     * @param source we want to match against
     * @return the properties to use from source bean to match an entity against
     */
    protected abstract InstanceProperties getMatchingProperties(T source);


    /**
     * Returns the entity matching the type and matchProperties
     * @param matchProperties properties to use for querying the repositories
     * @param typeDefName type name of the entities to search for
     * @return the entity detail matching the type and properties
     */
    public EntityDetail findEntity(InstanceProperties matchProperties, String typeDefName) {
        //
        // GDW - match properties passed to findEntities must be escaped for exact match but matchProperties for local filter match must be unescaped
        //
        return matchExactlyToUniqueEntity(omEntityDao.findEntities(matchProperties, typeDefName, 0, PAGE_SIZE), matchProperties);
    }


    /**
     * Returns the unique entity matching the properties
     * @param entities to search in
     * @param matchingProperties properties used to match
     * @return the entity matching the properties
     * @throws InformationViewExceptionBase throws NoMatchingEntityException if no entity is found matching
     * throws MultipleEntitiesMatching if multiple entities match
     */
    public EntityDetail matchExactlyToUniqueEntity(List<EntityDetail> entities, final InstanceProperties matchingProperties){
        if (!CollectionUtils.isEmpty(entities)) {
            List<EntityDetail> filteredEntities = entities.stream().filter(e -> matchProperties(e, matchingProperties)).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(filteredEntities)) {
                throw buildNoMatchingEntityException(matchingProperties, null, this.getClass().getName());
            }
            if (filteredEntities.size() > 1) {
                throw buildMultipleEntitiesMatchingException(matchingProperties, null, this.getClass().getName());
            }
            return filteredEntities.get(0);
        }
        throw buildNoMatchingEntityException(matchingProperties, null, this.getClass().getName());
    }

    /**
     *
     * @param entityDetail entity we want to match against
     * @param matchingProperties properties used for matching
     * @return true if properties match, false otherwise
     */
    public boolean matchProperties(EntityDetail entityDetail, InstanceProperties matchingProperties) {
        InstanceProperties entityProperties = entityDetail.getProperties();
        for (Map.Entry<String, InstancePropertyValue> property : matchingProperties.getInstanceProperties().entrySet()) {
            String actualValue = enterpriseConnector.getRepositoryHelper().getStringProperty(Constants.INFORMATION_VIEW_OMAS_NAME, property.getKey(), entityProperties, "matchProperties");//TODO only string supported for now
            // GDW - need to unescape any strings that were converted to exactMatchRegexes earlier
            String matchStringValue = (String)((PrimitivePropertyValue)property.getValue()).getPrimitiveValue();
            String literalMatchValue = enterpriseConnector.getRepositoryHelper().getUnqualifiedLiteralString(matchStringValue);
            if (!literalMatchValue.equals(actualValue)) {
                return false;
            }
        }
        return true;
    }


}

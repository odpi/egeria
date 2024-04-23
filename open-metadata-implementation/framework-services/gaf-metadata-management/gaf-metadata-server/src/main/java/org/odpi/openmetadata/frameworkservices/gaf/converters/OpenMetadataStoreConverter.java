/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.gaf.converters;

import org.odpi.openmetadata.commonservices.generichandlers.OCFConverter;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementControlHeader;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementOrigin;
import org.odpi.openmetadata.frameworks.governanceaction.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.enums.EngineActionStatus;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.governanceaction.search.ArrayTypePropertyValue;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.governanceaction.search.EnumTypePropertyValue;
import org.odpi.openmetadata.frameworks.governanceaction.search.MapTypePropertyValue;
import org.odpi.openmetadata.frameworks.governanceaction.search.PrimitiveTypeCategory;
import org.odpi.openmetadata.frameworks.governanceaction.search.PrimitiveTypePropertyValue;
import org.odpi.openmetadata.frameworks.governanceaction.search.StructTypePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ArrayPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceAuditHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.MapPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.StructPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * OpenMetadataStoreConverter provides the generic methods for the Governance Action Framework (GAF) beans converters.  Generic classes
 * have limited knowledge of the classes these are working on and this means creating a new instance of a
 * class from within a generic is a little involved.  This class provides the generic method for creating
 * and initializing a Governance Engine bean.
 */
abstract public class OpenMetadataStoreConverter<B> extends OCFConverter<B>
{
    private static final Logger log = LoggerFactory.getLogger(OpenMetadataStoreConverter.class);


    /**
     * Constructor
     *
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName name of this server
     */
    protected OpenMetadataStoreConverter(OMRSRepositoryHelper repositoryHelper,
                                         String serviceName,
                                         String serverName)
    {
        super(repositoryHelper, serviceName, serverName);
    }


    /*===============================
     * Methods to fill out headers and enums
     */


    /**
     * Extract the classifications from the entity.
     *
     * @param entityClassifications classifications direct from the entity
     * @return list of bean classifications
     */
    protected List<AttachedClassification> getAttachedClassifications(List<Classification> entityClassifications)
    {
        List<AttachedClassification> beanClassifications = null;

        if (entityClassifications != null)
        {
            beanClassifications = new ArrayList<>();

            for (Classification entityClassification : entityClassifications)
            {
                if (entityClassification != null)
                {
                    AttachedClassification beanClassification = new AttachedClassification();

                    fillElementControlHeader(beanClassification, entityClassification);

                    beanClassification.setClassificationName(entityClassification.getName());

                    if (entityClassification.getProperties() != null)
                    {
                        beanClassification.setClassificationProperties(mapElementProperties(entityClassification.getProperties()));
                        beanClassification.setEffectiveFromTime(entityClassification.getProperties().getEffectiveFromTime());
                        beanClassification.setEffectiveToTime(entityClassification.getProperties().getEffectiveToTime());
                    }

                    beanClassifications.add(beanClassification);
                }
            }

        }

        return beanClassifications;
    }


    /**
     * Retrieve and delete the EngineActionStatus enum property from the instance properties of an entity
     *
     * @param properties  entity properties
     * @return AssetOwnerType  enum value
     */
    protected EngineActionStatus removeActionStatus(String               propertyName,
                                                    InstanceProperties   properties)
    {
        EngineActionStatus actionStatus = this.getActionStatus(propertyName, properties);

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                instancePropertiesMap.remove(propertyName);
            }

            properties.setInstanceProperties(instancePropertiesMap);
        }

        return actionStatus;
    }


    /**
     * Retrieve the ActionStatus enum property from the instance properties of a Governance Action.
     *
     * @param propertyName name ot property to extract the enum from
     * @param properties  entity properties
     * @return ActionStatus  enum value
     */
    private EngineActionStatus getActionStatus(String               propertyName,
                                               InstanceProperties   properties)
    {
        EngineActionStatus engineActionStatus = EngineActionStatus.OTHER;

        if (properties != null)
        {
            Map<String, InstancePropertyValue> instancePropertiesMap = properties.getInstanceProperties();

            if (instancePropertiesMap != null)
            {
                InstancePropertyValue instancePropertyValue = instancePropertiesMap.get(propertyName);

                if (instancePropertyValue instanceof EnumPropertyValue enumPropertyValue)
                {
                    engineActionStatus = switch (enumPropertyValue.getOrdinal())
                    {
                        case 0 -> EngineActionStatus.REQUESTED;
                        case 1 -> EngineActionStatus.APPROVED;
                        case 2 -> EngineActionStatus.WAITING;
                        case 3 -> EngineActionStatus.ACTIVATING;
                        case 4 -> EngineActionStatus.IN_PROGRESS;
                        case 10 -> EngineActionStatus.ACTIONED;
                        case 11 -> EngineActionStatus.INVALID;
                        case 12 -> EngineActionStatus.IGNORED;
                        case 13 -> EngineActionStatus.FAILED;
                        case 14 -> EngineActionStatus.CANCELLED;
                        default -> engineActionStatus;
                    };
                }
            }
        }

        return engineActionStatus;
    }



    /**
     * Build an open metadata element bean from a matching entity.
     *
     * @param entityGUID unique identifier of desired entity
     * @param entities list of retrieved entities
     * @return new bean or null if guid is not in list
     */
    protected OpenMetadataElement getOpenMetadataElement(String entityGUID,
                                                         List<EntityDetail> entities)
    {
        OpenMetadataElement metadataElement = new OpenMetadataElement();

        if (entities != null)
        {
            for (EntityDetail entity : entities)
            {
                if (entity != null)
                {
                    if (entityGUID.equals(entity.getGUID()))
                    {
                        this.fillOpenMetadataElement(metadataElement, entity);

                        return metadataElement;
                    }
                }
            }
        }

        /*
         * The entity was not retrieved - however, returning the guid allows the caller to retrieve the element separately.
         */
        metadataElement.setElementGUID(entityGUID);

        return metadataElement;
    }


    /**
     * Fill a GAF control header from the information in a repository services element header.
     *
     * @param elementControlHeader GAF object control header
     * @param header OMRS element header
     */
    public void fillElementControlHeader(ElementControlHeader elementControlHeader,
                                         InstanceAuditHeader  header)
    {
        if (header != null)
        {
            elementControlHeader.setStatus(this.getElementStatus(header.getStatus()));
            elementControlHeader.setType(this.getElementType(header));

            ElementOrigin elementOrigin = new ElementOrigin();

            elementOrigin.setSourceServer(serverName);
            elementOrigin.setOriginCategory(this.getElementOriginCategory(header.getInstanceProvenanceType()));
            elementOrigin.setHomeMetadataCollectionId(header.getMetadataCollectionId());
            elementOrigin.setHomeMetadataCollectionName(header.getMetadataCollectionName());
            elementOrigin.setLicense(header.getInstanceLicense());

            elementControlHeader.setOrigin(elementOrigin);

            elementControlHeader.setVersions(this.getElementVersions(header));
        }
    }


    /**
     * Fill out the properties for the GAF Open Metadata Element bean with values from an OMRS entity.
     *
     * @param instanceProperties retrieve properties
     * @return  properties mapped to GAF
     */
    public ElementProperties mapElementProperties(InstanceProperties instanceProperties)
    {
        if (instanceProperties != null)
        {
            if (instanceProperties.getInstanceProperties() != null)
            {
                ElementProperties                  gafElementProperties = new ElementProperties();
                Map<String, InstancePropertyValue> omrsProperties       = instanceProperties.getInstanceProperties();

                for (String propertyName : omrsProperties.keySet())
                {
                    log.debug("=================================");
                    log.debug("Processing property: " + propertyName);
                    InstancePropertyValue omrsPropertyValue = omrsProperties.get(propertyName);

                    if (omrsPropertyValue != null)
                    {
                        log.debug("OMRS Property value: " + omrsPropertyValue);
                        log.debug("OMRS Property category: " + omrsPropertyValue.getInstancePropertyCategory());

                        switch (omrsPropertyValue.getInstancePropertyCategory())
                        {
                            case PRIMITIVE:
                                PrimitivePropertyValue omrsPrimitivePropertyValue = (PrimitivePropertyValue) omrsPropertyValue;
                                PrimitiveTypePropertyValue primitiveTypePropertyValue = new PrimitiveTypePropertyValue();

                                primitiveTypePropertyValue.setTypeName(omrsPrimitivePropertyValue.getTypeName());
                                primitiveTypePropertyValue.setPrimitiveValue(omrsPrimitivePropertyValue.getPrimitiveValue());
                                primitiveTypePropertyValue.setPrimitiveTypeCategory(mapPrimitiveDefCategory(omrsPrimitivePropertyValue.getPrimitiveDefCategory()));

                                gafElementProperties.setProperty(propertyName, primitiveTypePropertyValue);
                                break;

                            case ENUM:
                                EnumPropertyValue omrsEnumPropertyValue = (EnumPropertyValue) omrsPropertyValue;
                                EnumTypePropertyValue enumTypePropertyValue = new EnumTypePropertyValue();

                                enumTypePropertyValue.setTypeName(omrsEnumPropertyValue.getTypeName());
                                enumTypePropertyValue.setSymbolicName(omrsEnumPropertyValue.getSymbolicName());

                                gafElementProperties.setProperty(propertyName, enumTypePropertyValue);
                                break;

                            case MAP:
                                MapPropertyValue omrsMapPropertyValue = (MapPropertyValue) omrsPropertyValue;
                                MapTypePropertyValue mapTypePropertyValue = new MapTypePropertyValue();

                                mapTypePropertyValue.setTypeName(omrsMapPropertyValue.getTypeName());
                                mapTypePropertyValue.setMapValues(this.mapElementProperties(omrsMapPropertyValue.getMapValues()));

                                gafElementProperties.setProperty(propertyName, mapTypePropertyValue);
                                break;

                            case ARRAY:
                                ArrayPropertyValue omrsArrayPropertyValue = (ArrayPropertyValue) omrsPropertyValue;
                                ArrayTypePropertyValue arrayTypePropertyValue = new ArrayTypePropertyValue();

                                arrayTypePropertyValue.setTypeName(omrsArrayPropertyValue.getTypeName());
                                arrayTypePropertyValue.setArrayValues(this.mapElementProperties(omrsArrayPropertyValue.getArrayValues()));
                                if (arrayTypePropertyValue.getArrayValues() != null)
                                {
                                    arrayTypePropertyValue.setArrayCount(arrayTypePropertyValue.getArrayValues().getPropertyCount());
                                }

                                gafElementProperties.setProperty(propertyName, arrayTypePropertyValue);
                                break;

                            case STRUCT:
                                StructPropertyValue omrsStructPropertyValue = (StructPropertyValue) omrsPropertyValue;
                                StructTypePropertyValue structTypePropertyValue = new StructTypePropertyValue();

                                structTypePropertyValue.setTypeName(omrsStructPropertyValue.getTypeName());
                                structTypePropertyValue.setAttributes(this.mapElementProperties(omrsStructPropertyValue.getAttributes()));

                                gafElementProperties.setProperty(propertyName, structTypePropertyValue);
                                break;
                        }
                    }
                    else
                    {
                        log.debug("Ignoring property: " + propertyName);
                    }
                }

                log.debug("GAF properties: " + gafElementProperties);
                return gafElementProperties;
            }
        }

        return null;
    }


    /**
     * Fill out the properties for the GAF Open Metadata Element bean with values from an OMRS entity.
     *
     * @param bean bean to fill
     * @param entity values from repositories
     */
    public void fillOpenMetadataElement(OpenMetadataElement bean,
                                        EntityDetail        entity)
    {
        fillElementControlHeader(bean, entity);

        bean.setElementGUID(entity.getGUID());
        bean.setClassifications(this.getAttachedClassifications(entity.getClassifications()));

        InstanceProperties instanceProperties = entity.getProperties();

        if (instanceProperties != null)
        {
            bean.setEffectiveFromTime(instanceProperties.getEffectiveFromTime());
            bean.setEffectiveToTime(instanceProperties.getEffectiveToTime());
            if (log.isDebugEnabled())
            {
                log.debug("OMRS properties: " + instanceProperties);
            }

            ElementProperties elementProperties = this.mapElementProperties(instanceProperties);

            if (log.isDebugEnabled())
            {
                log.debug("GAF properties: " + elementProperties);
            }
            bean.setElementProperties(elementProperties);
        }
    }


    /**
     * Convert OMRS value in GAF value.
     *
     * @param primitiveDefCategory OMRS value
     * @return gaf equivalent
     */
    private PrimitiveTypeCategory mapPrimitiveDefCategory(PrimitiveDefCategory primitiveDefCategory)
    {
        switch (primitiveDefCategory)
        {
            case OM_PRIMITIVE_TYPE_INT:
                return PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_INT;

            case OM_PRIMITIVE_TYPE_BYTE:
                return PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BYTE;

            case OM_PRIMITIVE_TYPE_CHAR:
                return PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_CHAR;

            case OM_PRIMITIVE_TYPE_DATE:
                return PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DATE;

            case OM_PRIMITIVE_TYPE_LONG:
                return PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_LONG;

            case OM_PRIMITIVE_TYPE_FLOAT:
                return PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_FLOAT;

            case OM_PRIMITIVE_TYPE_SHORT:
                return PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_SHORT;

            case OM_PRIMITIVE_TYPE_DOUBLE:
                return PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DOUBLE;

            case OM_PRIMITIVE_TYPE_STRING:
                return PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING;

            case OM_PRIMITIVE_TYPE_BOOLEAN:
                return PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BOOLEAN;

            case OM_PRIMITIVE_TYPE_BIGDECIMAL:
                return PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BIGDECIMAL;

            case OM_PRIMITIVE_TYPE_BIGINTEGER:
                return PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BIGINTEGER;
        }

        return PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_UNKNOWN;
    }
}

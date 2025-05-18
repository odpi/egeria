/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.omf.converters;

import org.odpi.openmetadata.commonservices.generichandlers.OMFConverter;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElementStub;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementControlHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementOrigin;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.ArrayTypePropertyValue;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.EnumTypePropertyValue;
import org.odpi.openmetadata.frameworks.openmetadata.search.MapTypePropertyValue;
import org.odpi.openmetadata.frameworks.openmetadata.search.PrimitiveTypeCategory;
import org.odpi.openmetadata.frameworks.openmetadata.search.PrimitiveTypePropertyValue;
import org.odpi.openmetadata.frameworks.openmetadata.search.StructTypePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * OpenMetadataStoreConverter provides the generic methods for the Governance Action Framework (omf) beans converters.  Generic classes
 * have limited knowledge of the classes these are working on and this means creating a new instance of a
 * class from within a generic is a little involved.  This class provides the generic method for creating
 * and initializing a Governance Engine bean.
 */
abstract public class OpenMetadataStoreConverter<B> extends OMFConverter<B>
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
     * Build an open metadata element bean from a matching entity.
     *
     * @param entityGUID unique identifier of desired entity
     * @param entities list of retrieved entities
     * @return new bean or null if guid is not in list
     */
    protected OpenMetadataElement getOpenMetadataElement(String             entityGUID,
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
     * Fill a omf control header from the information in a repository services element header.
     *
     * @param elementControlHeader omf object control header
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
     * Create an OpenMetadataElementStub from an EntityProxy.
     *
     * @param entityProxy retrieved entity proxy
     * @return stub
     */
    public OpenMetadataElementStub getOpenMetadataElementStub(EntityProxy entityProxy)
    {
        OpenMetadataElementStub elementStub = new OpenMetadataElementStub();

        fillElementControlHeader(elementStub, entityProxy);
        elementStub.setClassifications(this.getAttachedClassifications(entityProxy.getClassifications()));
        elementStub.setGUID(entityProxy.getGUID());
        elementStub.setUniqueName(getQualifiedName(entityProxy.getUniqueProperties()));

        return elementStub;
    }


    /**
     * Fill out the properties for the omf Open Metadata Element bean with values from an OMRS entity.
     *
     * @param instanceProperties retrieve properties
     * @return  properties mapped to omf
     */
    public ElementProperties mapElementProperties(InstanceProperties instanceProperties)
    {
        if (instanceProperties != null)
        {
            if (instanceProperties.getInstanceProperties() != null)
            {
                ElementProperties                  omfElementProperties = new ElementProperties();
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

                                omfElementProperties.setProperty(propertyName, primitiveTypePropertyValue);
                                break;

                            case ENUM:
                                EnumPropertyValue omrsEnumPropertyValue = (EnumPropertyValue) omrsPropertyValue;
                                EnumTypePropertyValue enumTypePropertyValue = new EnumTypePropertyValue();

                                enumTypePropertyValue.setTypeName(omrsEnumPropertyValue.getTypeName());
                                enumTypePropertyValue.setSymbolicName(omrsEnumPropertyValue.getSymbolicName());

                                omfElementProperties.setProperty(propertyName, enumTypePropertyValue);
                                break;

                            case MAP:
                                MapPropertyValue omrsMapPropertyValue = (MapPropertyValue) omrsPropertyValue;
                                MapTypePropertyValue mapTypePropertyValue = new MapTypePropertyValue();

                                mapTypePropertyValue.setTypeName(omrsMapPropertyValue.getTypeName());
                                mapTypePropertyValue.setMapValues(this.mapElementProperties(omrsMapPropertyValue.getMapValues()));

                                omfElementProperties.setProperty(propertyName, mapTypePropertyValue);
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

                                omfElementProperties.setProperty(propertyName, arrayTypePropertyValue);
                                break;

                            case STRUCT:
                                StructPropertyValue omrsStructPropertyValue = (StructPropertyValue) omrsPropertyValue;
                                StructTypePropertyValue structTypePropertyValue = new StructTypePropertyValue();

                                structTypePropertyValue.setTypeName(omrsStructPropertyValue.getTypeName());
                                structTypePropertyValue.setAttributes(this.mapElementProperties(omrsStructPropertyValue.getAttributes()));

                                omfElementProperties.setProperty(propertyName, structTypePropertyValue);
                                break;
                        }
                    }
                    else
                    {
                        log.debug("Ignoring property: " + propertyName);
                    }
                }

                log.debug("omf properties: " + omfElementProperties);
                return omfElementProperties;
            }
        }

        return null;
    }


    /**
     * Fill out the properties for the omf Open Metadata Element bean with values from an OMRS entity.
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
                log.debug("omf properties: " + elementProperties);
            }
            bean.setElementProperties(elementProperties);
        }
    }


    /**
     * Convert OMRS value in omf value.
     *
     * @param primitiveDefCategory OMRS value
     * @return omf equivalent
     */
    private PrimitiveTypeCategory mapPrimitiveDefCategory(PrimitiveDefCategory primitiveDefCategory)
    {
        return switch (primitiveDefCategory)
        {
            case OM_PRIMITIVE_TYPE_INT -> PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_INT;
            case OM_PRIMITIVE_TYPE_BYTE -> PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BYTE;
            case OM_PRIMITIVE_TYPE_CHAR -> PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_CHAR;
            case OM_PRIMITIVE_TYPE_DATE -> PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DATE;
            case OM_PRIMITIVE_TYPE_LONG -> PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_LONG;
            case OM_PRIMITIVE_TYPE_FLOAT -> PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_FLOAT;
            case OM_PRIMITIVE_TYPE_SHORT -> PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_SHORT;
            case OM_PRIMITIVE_TYPE_DOUBLE -> PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_DOUBLE;
            case OM_PRIMITIVE_TYPE_STRING -> PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_STRING;
            case OM_PRIMITIVE_TYPE_BOOLEAN -> PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BOOLEAN;
            case OM_PRIMITIVE_TYPE_BIGDECIMAL -> PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BIGDECIMAL;
            case OM_PRIMITIVE_TYPE_BIGINTEGER -> PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_BIGINTEGER;
            default -> PrimitiveTypeCategory.OM_PRIMITIVE_TYPE_UNKNOWN;
        };

    }
}

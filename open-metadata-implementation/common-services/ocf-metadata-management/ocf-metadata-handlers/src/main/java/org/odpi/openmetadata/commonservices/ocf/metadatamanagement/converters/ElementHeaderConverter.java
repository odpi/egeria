/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.converters;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementOrigin;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;



/**
 * ElementHeaderConverter provides the root converter for the element beans.
 *
 * This root converter covers the OCF ElementBase attributes: type (ElementType), guid, url and the classifications.
 * It leaves extendedProperties to the sub lasses
 *
 * The root converter has two constructors.  Once constructor is for an object that is built just from an
 * entity (eg Discovery Engine Properties).  The other is for an object built from a combination of connected
 * entities.  In this second case, the root entity and possibly a relevant relationship is passed on the constructor.
 */
public class ElementHeaderConverter
{
    protected EntityDetail           entity;
    protected Relationship           relationship = null;
    protected OMRSRepositoryHelper   repositoryHelper;
    protected String                 serviceName;
    protected String                 serverName;
    protected String                 typeName;

    /**
     * Constructor captures the initial content
     *
     * @param entity properties to convert
     * @param repositoryHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName called server
     */
    ElementHeaderConverter(EntityDetail           entity,
                           OMRSRepositoryHelper   repositoryHelper,
                           String                 serviceName,
                           String                 serverName)
    {
        this.entity = entity;
        this.repositoryHelper = repositoryHelper;
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.setTypeName();
    }


    /**
     * Constructor captures the initial content with relationship
     *
     * @param entity properties to convert
     * @param relationship properties to convert
     * @param repositoryHelper helper object to parse entity/relationship
     * @param serviceName name of this component
     * @param serverName called server
     */
    public ElementHeaderConverter(EntityDetail         entity,
                                  Relationship         relationship,
                                  OMRSRepositoryHelper repositoryHelper,
                                  String               serviceName,
                                  String               serverName)
    {
        this.entity = entity;
        this.relationship = relationship;
        this.repositoryHelper = repositoryHelper;
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.setTypeName();
    }


    /**
     * Extract the type name from the entity (if not null).
     */
    protected void setTypeName()
    {
        if (entity != null)
        {
            InstanceType type = entity.getType();
            if (type != null)
            {
                this.typeName = type.getTypeDefName();
            }
        }
    }


    /**
     * Extract the properties from the entity.
     *
     * @param bean output bean
     */
    public void updateBean(ElementBase bean)
    {
        if (entity != null)
        {
            TypeConverter typeConverter = new TypeConverter();

            bean.setType(typeConverter.getElementType(entity.getType()));

            ElementOrigin elementOrigin = new ElementOrigin();

            elementOrigin.setHomeMetadataCollectionId(entity.getMetadataCollectionId());
            elementOrigin.setHomeMetadataCollectionName(entity.getMetadataCollectionName());
            elementOrigin.setOriginCategory(typeConverter.getElementOrigin(entity.getInstanceProvenanceType()));
            elementOrigin.setSourceServer(serverName);
            elementOrigin.setLicense(entity.getInstanceLicense());

            bean.setOrigin(elementOrigin);
            bean.setGUID(entity.getGUID());
            bean.setURL(entity.getInstanceURL());
        }
        else if (typeName != null)
        {
            ElementType type = new ElementType();

            type.setTypeName(typeName);

            bean.setType(type);
        }
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.events.beans.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.events.OMRSTypeDefEventType;

import java.io.Serial;
import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMRSEventV1TypeDefSection describes the properties specific to TypeDef related events
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMRSEventV1TypeDefSection implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    private OMRSTypeDefEventType typeDefEventType         = null;
    private String               typeDefGUID              = null;
    private String               typeDefName              = null;
    private AttributeTypeDef     attributeTypeDef         = null;
    private TypeDef              typeDef                  = null;
    private TypeDefPatch         typeDefPatch             = null;
    private TypeDefSummary       originalTypeDefSummary   = null;
    private AttributeTypeDef     originalAttributeTypeDef = null;


    /**
     * Default constructor
     */
    public OMRSEventV1TypeDefSection()
    {
    }


    /**
     * Return the reason for the event.
     *
     * @return enum
     */
    public OMRSTypeDefEventType getTypeDefEventType()
    {
        return typeDefEventType;
    }


    /**
     * Set up the reason for the event.
     *
     * @param typeDefEventType enum
     */
    public void setTypeDefEventType(OMRSTypeDefEventType typeDefEventType)
    {
        this.typeDefEventType = typeDefEventType;
    }


    /**
     * Return the unique identifier of the type.
     *
     * @return guid
     */
    public String getTypeDefGUID()
    {
        return typeDefGUID;
    }


    /**
     * Set up the unique identifier of the type.
     *
     * @param typeDefGUID guid
     */
    public void setTypeDefGUID(String typeDefGUID)
    {
        this.typeDefGUID = typeDefGUID;
    }


    /**
     * Return the unique name of the type.
     *
     * @return name
     */
    public String getTypeDefName()
    {
        return typeDefName;
    }


    /**
     * Set up the unique name of the type.
     *
     * @param typeDefName name
     */
    public void setTypeDefName(String typeDefName)
    {
        this.typeDefName = typeDefName;
    }


    /**
     * Return the associated attribute type.
     *
     * @return attribute typeDef
     */
    public AttributeTypeDef getAttributeTypeDef()
    {
        return attributeTypeDef;
    }

    /**
     * Set up the associated attribute type.
     *
     * @param attributeTypeDef attribute typeDef
     */
    public void setAttributeTypeDef(AttributeTypeDef attributeTypeDef)
    {
        this.attributeTypeDef = attributeTypeDef;
    }


    /**
     * Return the type for the event.
     *
     * @return typeDef
     */
    public TypeDef getTypeDef()
    {
        return typeDef;
    }


    /**
     * Set up the type for the event.
     *
     * @param typeDef typeDef
     */
    public void setTypeDef(TypeDef typeDef)
    {
        this.typeDef = typeDef;
    }


    /**
     * Return the patch (changes) to a type.
     *
     * @return typeDefPatch
     */
    public TypeDefPatch getTypeDefPatch()
    {
        return typeDefPatch;
    }


    /**
     * Set up the patch (changes) to a type.
     *
     * @param typeDefPatch typeDefPatch
     */
    public void setTypeDefPatch(TypeDefPatch typeDefPatch)
    {
        this.typeDefPatch = typeDefPatch;
    }


    /**
     * Return the type before the change.
     *
     * @return typeDef summary
     */
    public TypeDefSummary getOriginalTypeDefSummary()
    {
        return originalTypeDefSummary;
    }


    /**
     * Set up the type before the change.
     *
     * @param originalTypeDefSummary typeDef summary
     */
    public void setOriginalTypeDefSummary(TypeDefSummary originalTypeDefSummary)
    {
        this.originalTypeDefSummary = originalTypeDefSummary;
    }


    /**
     * Return the attribute before the change.
     *
     * @return attribute typeDef
     */
    public AttributeTypeDef getOriginalAttributeTypeDef()
    {
        return originalAttributeTypeDef;
    }


    /**
     * Set up the attribute before the change.
     *
     * @param originalAttributeTypeDef attribute typeDef
     */
    public void setOriginalAttributeTypeDef(AttributeTypeDef originalAttributeTypeDef)
    {
        this.originalAttributeTypeDef = originalAttributeTypeDef;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "OMRSEventV1TypeDefSection{" +
                       "typeDefEventType=" + typeDefEventType +
                       ", typeDefGUID='" + typeDefGUID + '\'' +
                       ", typeDefName='" + typeDefName + '\'' +
                       ", attributeTypeDef=" + attributeTypeDef +
                       ", typeDef=" + typeDef +
                       ", typeDefPatch=" + typeDefPatch +
                       ", originalTypeDefSummary=" + originalTypeDefSummary +
                       ", originalAttributeTypeDef=" + originalAttributeTypeDef +
                       '}';
    }
}

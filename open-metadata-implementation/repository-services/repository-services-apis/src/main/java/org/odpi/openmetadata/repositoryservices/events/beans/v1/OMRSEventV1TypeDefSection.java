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
    private static final long serialVersionUID = 1L;

    private OMRSTypeDefEventType typeDefEventType         = null;
    private String               typeDefGUID              = null;
    private String               typeDefName              = null;
    private AttributeTypeDef     attributeTypeDef         = null;
    private TypeDef              typeDef                  = null;
    private TypeDefPatch         typeDefPatch             = null;
    private TypeDefSummary       originalTypeDefSummary   = null;
    private AttributeTypeDef     originalAttributeTypeDef = null;

    public OMRSEventV1TypeDefSection()
    {
    }

    public OMRSTypeDefEventType getTypeDefEventType()
    {
        return typeDefEventType;
    }

    public void setTypeDefEventType(OMRSTypeDefEventType typeDefEventType)
    {
        this.typeDefEventType = typeDefEventType;
    }

    public String getTypeDefGUID()
    {
        return typeDefGUID;
    }

    public void setTypeDefGUID(String typeDefGUID)
    {
        this.typeDefGUID = typeDefGUID;
    }

    public String getTypeDefName()
    {
        return typeDefName;
    }

    public void setTypeDefName(String typeDefName)
    {
        this.typeDefName = typeDefName;
    }

    public AttributeTypeDef getAttributeTypeDef()
    {
        return attributeTypeDef;
    }

    public void setAttributeTypeDef(AttributeTypeDef attributeTypeDef)
    {
        this.attributeTypeDef = attributeTypeDef;
    }

    public TypeDef getTypeDef()
    {
        return typeDef;
    }

    public void setTypeDef(TypeDef typeDef)
    {
        this.typeDef = typeDef;
    }

    public TypeDefPatch getTypeDefPatch()
    {
        return typeDefPatch;
    }

    public void setTypeDefPatch(TypeDefPatch typeDefPatch)
    {
        this.typeDefPatch = typeDefPatch;
    }

    public TypeDefSummary getOriginalTypeDefSummary()
    {
        return originalTypeDefSummary;
    }

    public void setOriginalTypeDefSummary(TypeDefSummary originalTypeDefSummary)
    {
        this.originalTypeDefSummary = originalTypeDefSummary;
    }

    public AttributeTypeDef getOriginalAttributeTypeDef()
    {
        return originalAttributeTypeDef;
    }

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

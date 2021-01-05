/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.events.beans.v1;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.events.OMRSEventErrorCode;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMRSEventV1ErrorSection describes the properties used to record errors detected by one of the members of the
 * open metadata repository cohort.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OMRSEventV1ErrorSection implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private OMRSEventErrorCode     errorCode                  = null;
    private String                 errorMessage               = null;
    private String                 targetMetadataCollectionId = null;
    private Connection             targetRemoteConnection    = null;
    private TypeDefSummary         targetTypeDefSummary      = null;
    private AttributeTypeDef       targetAttributeTypeDef    = null;
    private String                 targetInstanceGUID        = null;
    private InstanceProvenanceType otherOrigin               = null;
    private String                 otherMetadataCollectionId = null;
    private TypeDefSummary         otherTypeDefSummary       = null;
    private TypeDef                otherTypeDef              = null;
    private AttributeTypeDef       otherAttributeTypeDef     = null;
    private String                 otherInstanceGUID         = null;

    public OMRSEventV1ErrorSection()
    {
    }

    public OMRSEventErrorCode getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(OMRSEventErrorCode errorCode)
    {
        this.errorCode = errorCode;
    }

    public String getErrorMessage()
    {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

    public String getTargetMetadataCollectionId()
    {
        return targetMetadataCollectionId;
    }

    public void setTargetMetadataCollectionId(String targetMetadataCollectionId)
    {
        this.targetMetadataCollectionId = targetMetadataCollectionId;
    }

    public Connection getTargetRemoteConnection()
    {
        return targetRemoteConnection;
    }

    public void setTargetRemoteConnection(Connection targetRemoteConnection)
    {
        this.targetRemoteConnection = targetRemoteConnection;
    }

    public TypeDefSummary getTargetTypeDefSummary()
    {
        return targetTypeDefSummary;
    }

    public void setTargetTypeDefSummary(TypeDefSummary targetTypeDefSummary)
    {
        this.targetTypeDefSummary = targetTypeDefSummary;
    }

    public AttributeTypeDef getTargetAttributeTypeDef()
    {
        return targetAttributeTypeDef;
    }

    public void setTargetAttributeTypeDef(AttributeTypeDef targetAttributeTypeDef)
    {
        this.targetAttributeTypeDef = targetAttributeTypeDef;
    }

    public String getTargetInstanceGUID()
    {
        return targetInstanceGUID;
    }

    public void setTargetInstanceGUID(String targetInstanceGUID)
    {
        this.targetInstanceGUID = targetInstanceGUID;
    }

    public InstanceProvenanceType getOtherOrigin()
    {
        return otherOrigin;
    }

    public void setOtherOrigin(InstanceProvenanceType otherOrigin)
    {
        this.otherOrigin = otherOrigin;
    }

    public String getOtherMetadataCollectionId()
    {
        return otherMetadataCollectionId;
    }

    public void setOtherMetadataCollectionId(String otherMetadataCollectionId)
    {
        this.otherMetadataCollectionId = otherMetadataCollectionId;
    }

    public TypeDefSummary getOtherTypeDefSummary() { return otherTypeDefSummary; }

    public void setOtherTypeDefSummary(TypeDefSummary otherTypeDefSummary)
    {
        this.otherTypeDefSummary = otherTypeDefSummary;
    }

    public TypeDef getOtherTypeDef()
    {
        return otherTypeDef;
    }

    public void setOtherTypeDef(TypeDef otherTypeDef)
    {
        this.otherTypeDef = otherTypeDef;
    }

    public AttributeTypeDef getOtherAttributeTypeDef()
    {
        return otherAttributeTypeDef;
    }

    public void setOtherAttributeTypeDef(AttributeTypeDef otherAttributeTypeDef)
    {
        this.otherAttributeTypeDef = otherAttributeTypeDef;
    }

    public String getOtherInstanceGUID()
    {
        return otherInstanceGUID;
    }

    public void setOtherInstanceGUID(String otherInstanceGUID)
    {
        this.otherInstanceGUID = otherInstanceGUID;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "OMRSEventV1ErrorSection{" +
                       "errorCode=" + errorCode +
                       ", errorMessage='" + errorMessage + '\'' +
                       ", targetMetadataCollectionId='" + targetMetadataCollectionId + '\'' +
                       ", targetRemoteConnection=" + targetRemoteConnection +
                       ", targetTypeDefSummary=" + targetTypeDefSummary +
                       ", targetAttributeTypeDef=" + targetAttributeTypeDef +
                       ", targetInstanceGUID='" + targetInstanceGUID + '\'' +
                       ", otherOrigin=" + otherOrigin +
                       ", otherMetadataCollectionId='" + otherMetadataCollectionId + '\'' +
                       ", otherTypeDefSummary=" + otherTypeDefSummary +
                       ", otherTypeDef=" + otherTypeDef +
                       ", otherAttributeTypeDef=" + otherAttributeTypeDef +
                       ", otherInstanceGUID='" + otherInstanceGUID + '\'' +
                       '}';
    }
}

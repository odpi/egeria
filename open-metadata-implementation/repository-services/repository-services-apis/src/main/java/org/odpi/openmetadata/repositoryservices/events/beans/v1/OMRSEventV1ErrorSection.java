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

import java.io.Serial;
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
    @Serial
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


    /**
     * Default constructor
     */
    public OMRSEventV1ErrorSection()
    {
    }


    /**
     * Return the reason for the error.
     *
     * @return enum
     */
    public OMRSEventErrorCode getErrorCode()
    {
        return errorCode;
    }


    /**
     * Set up the reason for the error.
     *
     * @param errorCode enum
     */
    public void setErrorCode(OMRSEventErrorCode errorCode)
    {
        this.errorCode = errorCode;
    }


    /**
     * Return the message associated with the error.  It details the elements in trouble.
     *
     * @return text message
     */
    public String getErrorMessage()
    {
        return errorMessage;
    }


    /**
     * Set up the message associated with the error.  It details the elements in trouble.
     *
     * @param errorMessage text message
     */
    public void setErrorMessage(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }


    /**
     * Return the identifier of the metadata collection nominated to resolve the error.
     *
     * @return guid
     */
    public String getTargetMetadataCollectionId()
    {
        return targetMetadataCollectionId;
    }


    /**
     * Set up the identifier of the metadata collection nominated to resolve the error.
     *
     * @param targetMetadataCollectionId guid
     */
    public void setTargetMetadataCollectionId(String targetMetadataCollectionId)
    {
        this.targetMetadataCollectionId = targetMetadataCollectionId;
    }


    /**
     * Return the remote collection that has an issue.
     *
     * @return connection
     */
    public Connection getTargetRemoteConnection()
    {
        return targetRemoteConnection;
    }


    /**
     * Set up the remote collection that has an issue.
     *
     * @param targetRemoteConnection connection
     */
    public void setTargetRemoteConnection(Connection targetRemoteConnection)
    {
        this.targetRemoteConnection = targetRemoteConnection;
    }


    /**
     * Return details of the type in trouble.
     *
     * @return typeDef summary
     */
    public TypeDefSummary getTargetTypeDefSummary()
    {
        return targetTypeDefSummary;
    }


    /**
     * Set up details of the type in trouble.
     *
     * @param targetTypeDefSummary typeDef summary
     */
    public void setTargetTypeDefSummary(TypeDefSummary targetTypeDefSummary)
    {
        this.targetTypeDefSummary = targetTypeDefSummary;
    }


    /**
     * Return details of the attribute type in trouble.
     *
     * @return attribute typeDef
     */
    public AttributeTypeDef getTargetAttributeTypeDef()
    {
        return targetAttributeTypeDef;
    }


    /**
     * Set up  details of the attribute type in trouble.
     *
     * @param targetAttributeTypeDef attribute typeDef
     */
    public void setTargetAttributeTypeDef(AttributeTypeDef targetAttributeTypeDef)
    {
        this.targetAttributeTypeDef = targetAttributeTypeDef;
    }


    /**
     * Return the unique identifier for the instance in trouble.
     *
     * @return guid
     */
    public String getTargetInstanceGUID()
    {
        return targetInstanceGUID;
    }


    /**
     * Set up the unique identifier for the instance in trouble.
     *
     * @param targetInstanceGUID guid
     */
    public void setTargetInstanceGUID(String targetInstanceGUID)
    {
        this.targetInstanceGUID = targetInstanceGUID;
    }


    /**
     * Return the provenance type of the conflicting metadata collection.
     *
     * @return enum
     */
    public InstanceProvenanceType getOtherOrigin()
    {
        return otherOrigin;
    }


    /**
     * Set up the provenance type of the conflicting metadata collection.
     *
     * @param otherOrigin enum
     */
    public void setOtherOrigin(InstanceProvenanceType otherOrigin)
    {
        this.otherOrigin = otherOrigin;
    }


    /**
     * Return the unique identifier of the conflicting metadata collection.
     *
     * @return guid
     */
    public String getOtherMetadataCollectionId()
    {
        return otherMetadataCollectionId;
    }


    /**
     * Set up the unique identifier of the conflicting metadata collection.
     *
     * @param otherMetadataCollectionId guid
     */
    public void setOtherMetadataCollectionId(String otherMetadataCollectionId)
    {
        this.otherMetadataCollectionId = otherMetadataCollectionId;
    }


    /**
     * Return the type information for the conflicting type.
     *
     * @return typeDef summary
     */
    public TypeDefSummary getOtherTypeDefSummary() { return otherTypeDefSummary; }


    /**
     * Set up the type information for the conflicting type.
     *
     * @param otherTypeDefSummary typeDef summary
     */
    public void setOtherTypeDefSummary(TypeDefSummary otherTypeDefSummary)
    {
        this.otherTypeDefSummary = otherTypeDefSummary;
    }


    /**
     * Return the type information for the conflicting type.
     *
     * @return typeDef
     */
    public TypeDef getOtherTypeDef()
    {
        return otherTypeDef;
    }

    /**
     * Set up the type information for the conflicting type.
     *
     * @param otherTypeDef typeDef
     */
    public void setOtherTypeDef(TypeDef otherTypeDef)
    {
        this.otherTypeDef = otherTypeDef;
    }


    /**
     * Return the type information for the conflicting type.
     *
     * @return attribute typeDef
     */
    public AttributeTypeDef getOtherAttributeTypeDef()
    {
        return otherAttributeTypeDef;
    }


    /**
     * Set up the type information for the conflicting type.
     *
     * @param otherAttributeTypeDef typeDef
     */
    public void setOtherAttributeTypeDef(AttributeTypeDef otherAttributeTypeDef)
    {
        this.otherAttributeTypeDef = otherAttributeTypeDef;
    }


    /**
     * Return the unique identifier of the conflicting instance.
     *
     * @return guid
     */
    public String getOtherInstanceGUID()
    {
        return otherInstanceGUID;
    }


    /**
     * Set up the unique identifier of the conflicting instance.
     *
     * @param otherInstanceGUID guid
     */
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

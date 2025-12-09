/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.events;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.events.beans.v2.OMRSEventV2;
import org.odpi.openmetadata.repositoryservices.events.beans.v2.OMRSEventV2TypeDefSection;

/**
 * OMRSTypeDefEvent provides the wrapper for an event that relates to a type definition (TypeDef).
 */
public class OMRSTypeDefEvent extends OMRSEvent
{
    /*
     * The type of the TypeDef event that defines how the rest of the values should be interpreted.
     */
    private OMRSTypeDefEventType typeDefEventType = OMRSTypeDefEventType.UNKNOWN_TYPEDEF_EVENT;

    /*
     * TypeDef specific properties.
     */
    private AttributeTypeDef attributeTypeDef = null;
    private TypeDef          typeDef          = null;
    private String           typeDefGUID      = null;
    private String           typeDefName      = null;
    private TypeDefPatch     typeDefPatch     = null;

    /*
     * TypeDef specific properties for events related to correcting conflicts in the open metadata repository
     * cohort.
     */
    private TypeDefSummary   originalTypeDefSummary   = null;
    private AttributeTypeDef originalAttributeTypeDef = null;

    /*
     * Specific variables only used in error reporting.  It defines the subset of error codes from OMRSEvent
     * that are specific to TypeDef events.
     */
    private OMRSTypeDefEventErrorCode   errorCode                  = OMRSTypeDefEventErrorCode.NOT_IN_USE;


    /**
     * Inbound event constructor that takes the object created by the Jackson JSON mapper and unpacks the
     * properties into the instance event.
     *
     * @param inboundEvent incoming Event.
     */
    public OMRSTypeDefEvent(OMRSEventV2 inboundEvent)
    {
        super(inboundEvent);

        OMRSEventV2TypeDefSection typeDefSection = inboundEvent.getTypeDefEventSection();

        if (typeDefSection != null)
        {
            this.typeDefEventType = typeDefSection.getTypeDefEventType();
            this.attributeTypeDef = typeDefSection.getAttributeTypeDef();
            this.typeDef = typeDefSection.getTypeDef();
            this.typeDefGUID = typeDefSection.getTypeDefGUID();
            this.typeDefName = typeDefSection.getTypeDefName();
            this.typeDefPatch = typeDefSection.getTypeDefPatch();
            this.originalTypeDefSummary = typeDefSection.getOriginalTypeDefSummary();
            this.originalAttributeTypeDef = typeDefSection.getOriginalAttributeTypeDef();
        }

        if (super.genericErrorCode != null)
        {
            switch(genericErrorCode)
            {
                case CONFLICTING_TYPEDEFS:
                    errorCode = OMRSTypeDefEventErrorCode.CONFLICTING_TYPEDEFS;
                    break;

                case CONFLICTING_ATTRIBUTE_TYPEDEFS:
                    errorCode = OMRSTypeDefEventErrorCode.CONFLICTING_ATTRIBUTE_TYPEDEFS;
                    break;

                case TYPEDEF_PATCH_MISMATCH:
                    errorCode = OMRSTypeDefEventErrorCode.TYPEDEF_PATCH_MISMATCH;
                    break;

                default:
                    errorCode = OMRSTypeDefEventErrorCode.UNKNOWN_ERROR_CODE;
                    break;
            }
        }
    }

    /**
     * Outbound event constructor for events such as newTypeDef.
     *
     * @param typeDefEventType type of event
     * @param typeDef Complete details of the TypeDef that is the subject of the event.
     */
    public OMRSTypeDefEvent(OMRSTypeDefEventType typeDefEventType,
                            TypeDef              typeDef)
    {
        super(OMRSEventCategory.TYPEDEF);

        this.typeDefEventType = typeDefEventType;
        this.typeDef = typeDef;
    }


    /**
     * Outbound event constructor for events such as newAttributeTypeDef.
     *
     * @param typeDefEventType type of event
     * @param attributeTypeDef Complete details of the AttributeTypeDef that is the subject of the event.
     */
    public OMRSTypeDefEvent(OMRSTypeDefEventType typeDefEventType,
                            AttributeTypeDef     attributeTypeDef)
    {
        super(OMRSEventCategory.TYPEDEF);

        this.typeDefEventType = typeDefEventType;
        this.attributeTypeDef = attributeTypeDef;
    }


    /**
     * Outbound event constructor for events such as updates.
     *
     * @param typeDefEventType type of event
     * @param typeDefPatch Complete details of the TypeDef that is the subject of the event.
     */
    public OMRSTypeDefEvent(OMRSTypeDefEventType typeDefEventType,
                            TypeDefPatch         typeDefPatch)
    {
        super(OMRSEventCategory.TYPEDEF);

        this.typeDefEventType = typeDefEventType;
        this.typeDefPatch = typeDefPatch;
    }


    /**
     * Outbound event constructor for events such as deletes.
     *
     * @param typeDefEventType type of event
     * @param typeDefGUID Unique identifier of the TypeDef that is the subject of the event.
     * @param typeDefName Unique name of the TypeDef that is the subject of the event.
     */
    public OMRSTypeDefEvent(OMRSTypeDefEventType typeDefEventType,
                            String               typeDefGUID,
                            String               typeDefName)
    {
        super(OMRSEventCategory.TYPEDEF);

        this.typeDefEventType = typeDefEventType;
        this.typeDefGUID = typeDefGUID;
        this.typeDefName = typeDefName;
    }


    /**
     * Outbound event constructor for changing the identifiers associated with TypeDefs.
     *
     * @param typeDefEventType type of event
     * @param originalTypeDefSummary description of the original TypeDef that is the subject of the event.
     * @param typeDef updated TypeDef with new identifiers
     */
    public OMRSTypeDefEvent(OMRSTypeDefEventType typeDefEventType,
                            TypeDefSummary       originalTypeDefSummary,
                            TypeDef              typeDef)
    {
        super(OMRSEventCategory.TYPEDEF);

        this.typeDefEventType = typeDefEventType;
        this.originalTypeDefSummary = originalTypeDefSummary;
        this.typeDef = typeDef;
    }


    /**
     * Outbound event constructor for changing the identifiers associated with AttributeTypeDefs.
     *
     * @param typeDefEventType type of event
     * @param originalAttributeTypeDef description of the original AttributeTypeDef that is the subject of the event.
     * @param attributeTypeDef updated AttributeTypeDef with new identifiers
     */
    public OMRSTypeDefEvent(OMRSTypeDefEventType typeDefEventType,
                            AttributeTypeDef     originalAttributeTypeDef,
                            AttributeTypeDef     attributeTypeDef)
    {
        super(OMRSEventCategory.TYPEDEF);

        this.typeDefEventType = typeDefEventType;
        this.originalAttributeTypeDef = originalAttributeTypeDef;
        this.attributeTypeDef = attributeTypeDef;
    }


    /**
     * Outbound event constructor for conflicting typedef errors.
     *
     * @param errorCode code enum indicating the cause of the error.
     * @param errorMessage descriptive message about the error.
     * @param originatorTypeDefSummary details of the TypeDef in the remote repository.
     * @param otherMetadataCollectionId unique id of the remote repository.
     * @param otherTypeDefSummary details of the TypeDef in the local repository.
     */
    public OMRSTypeDefEvent(OMRSTypeDefEventErrorCode errorCode,
                            String                    errorMessage,
                            TypeDefSummary            originatorTypeDefSummary,
                            String                    otherMetadataCollectionId,
                            TypeDefSummary            otherTypeDefSummary)
    {
        super(OMRSEventCategory.TYPEDEF,
              errorCode.getEncoding(),
              errorMessage,
              otherMetadataCollectionId,
              otherTypeDefSummary);

        this.originalTypeDefSummary = originatorTypeDefSummary;
        this.typeDefEventType = OMRSTypeDefEventType.TYPEDEF_ERROR_EVENT;
    }


    /**
     * Outbound event constructor for conflicting attribute typedef errors.
     *
     * @param errorCode code enum indicating the cause of the error.
     * @param errorMessage descriptive message about the error.
     * @param originatorAttributeTypeDef details of the TypeDef in the local repository.
     * @param otherMetadataCollectionId unique id of the remote repository.
     * @param otherAttributeTypeDef details of the TypeDef in the remote repository.
     */
    public OMRSTypeDefEvent(OMRSTypeDefEventErrorCode errorCode,
                            String                    errorMessage,
                            AttributeTypeDef          originatorAttributeTypeDef,
                            String                    otherMetadataCollectionId,
                            AttributeTypeDef          otherAttributeTypeDef)
    {
        super(OMRSEventCategory.TYPEDEF,
              errorCode.getEncoding(),
              errorMessage,
              otherMetadataCollectionId,
              otherAttributeTypeDef);

        this.originalAttributeTypeDef = originatorAttributeTypeDef;
        this.typeDefEventType = OMRSTypeDefEventType.TYPEDEF_ERROR_EVENT;
    }


    /**
     * Outbound event constructor for conflicting attribute typedef errors.
     *
     * @param errorCode code enum indicating the cause of the error.
     * @param errorMessage descriptive message about the error.
     * @param targetMetadataCollectionId identifier of the cohort member that issued the event in error.
     * @param targetAttributeTypeDef details of the TypeDef in the remote repository.
     * @param otherAttributeTypeDef details of the TypeDef in the local repository.
     */
    public OMRSTypeDefEvent(OMRSTypeDefEventErrorCode errorCode,
                            String                    errorMessage,
                            String                    targetMetadataCollectionId,
                            AttributeTypeDef          targetAttributeTypeDef,
                            AttributeTypeDef          otherAttributeTypeDef)
    {
        super(OMRSEventCategory.TYPEDEF,
              errorCode.getEncoding(),
              errorMessage,
              targetMetadataCollectionId,
              targetAttributeTypeDef,
              otherAttributeTypeDef);

        this.typeDefEventType = OMRSTypeDefEventType.TYPEDEF_ERROR_EVENT;
    }



    /**
     * Outbound event constructor for typedef mismatch errors.
     *
     * @param errorCode code enum indicating the cause of the error.
     * @param errorMessage descriptive message about the error.
     * @param targetMetadataCollectionId identifier of the cohort member that issued the event in error.
     * @param targetTypeDefSummary details of the TypeDef in the remote repository.
     * @param otherTypeDef details of the TypeDef in the local repository.
     */
    public OMRSTypeDefEvent(OMRSTypeDefEventErrorCode errorCode,
                            String                    errorMessage,
                            String                    targetMetadataCollectionId,
                            TypeDefSummary            targetTypeDefSummary,
                            TypeDef                   otherTypeDef)
    {
        super(OMRSEventCategory.TYPEDEF,
              errorCode.getEncoding(),
              errorMessage,
              targetMetadataCollectionId,
              targetTypeDefSummary,
              otherTypeDef);

        this.typeDefEventType = OMRSTypeDefEventType.TYPEDEF_ERROR_EVENT;
    }


    /**
     * Return the code for this event's type.
     *
     * @return OMRSTypeDefEventType enum
     */
    public OMRSTypeDefEventType getTypeDefEventType()
    {
        return typeDefEventType;
    }


    /**
     * Return the complete TypeDef object.
     *
     * @return TypeDef object
     */
    public TypeDef getTypeDef()
    {
        return typeDef;
    }


    /**
     * Return the complete AttributeTypeDef object.
     *
     * @return AttributeTypeDef object
     */
    public AttributeTypeDef getAttributeTypeDef()
    {
        return attributeTypeDef;
    }

    /**
     * Return the unique id of the TypeDef.
     *
     * @return String guid
     */
    public String getTypeDefGUID()
    {
        return typeDefGUID;
    }


    /**
     * Return the unique name of the TypeDef.
     *
     * @return String name
     */
    public String getTypeDefName()
    {
        return typeDefName;
    }


    /**
     * Return a patch for the TypeDef.
     *
     * @return TypeDefPatch object
     */
    public TypeDefPatch getTypeDefPatch()
    {
        return typeDefPatch;
    }


    /**
     * Return the details of the TypeDef before it was changed.
     *
     * @return TypeDefSummary containing identifiers, category and version
     */
    public TypeDefSummary getOriginalTypeDefSummary()
    {
        return originalTypeDefSummary;
    }


    /**
     * Return the details of the AttributeTypeDef before it was changed.
     *
     * @return AttributeTypeDef object
     */
    public AttributeTypeDef getOriginalAttributeTypeDef()
    {
        return originalAttributeTypeDef;
    }

    /**
     * Return the TypeDef error code for error events.
     *
     * @return OMRSTypeDefEventErrorCode enum
     */
    public OMRSTypeDefEventErrorCode getErrorCode()
    {
        return errorCode;
    }


    /**
     * Returns an OMRSEvent populated with details from this TypeDefEvent
     *
     * @return OMRSEvent (Version 1) object
     */
    public OMRSEventV2 getOMRSEventV1()
    {
        OMRSEventV2 omrsEvent = super.getOMRSEventV1();

        OMRSEventV2TypeDefSection typeDefSection = new OMRSEventV2TypeDefSection();

        typeDefSection.setTypeDefEventType(this.typeDefEventType);
        typeDefSection.setTypeDef(this.typeDef);
        typeDefSection.setAttributeTypeDef(this.attributeTypeDef);
        typeDefSection.setTypeDefPatch(this.typeDefPatch);
        typeDefSection.setTypeDefGUID(this.typeDefGUID);
        typeDefSection.setTypeDefName(this.typeDefName);
        typeDefSection.setOriginalTypeDefSummary(this.originalTypeDefSummary);
        typeDefSection.setOriginalAttributeTypeDef(this.originalAttributeTypeDef);

        omrsEvent.setTypeDefEventSection(typeDefSection);

        return omrsEvent;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OMRSTypeDefEvent{" +
                "typeDefEventType=" + typeDefEventType +
                ", attributeTypeDef=" + attributeTypeDef +
                ", typeDef=" + typeDef +
                ", typeDefGUID='" + typeDefGUID + '\'' +
                ", typeDefName='" + typeDefName + '\'' +
                ", typeDefPatch=" + typeDefPatch +
                ", originalTypeDefSummary=" + originalTypeDefSummary +
                ", originalAttributeTypeDef=" + originalAttributeTypeDef +
                ", errorCode=" + errorCode +
                ", eventTimestamp=" + eventTimestamp +
                ", eventDirection=" + eventDirection +
                ", eventCategory=" + eventCategory +
                ", eventOriginator=" + eventOriginator +
                ", genericErrorCode=" + genericErrorCode +
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

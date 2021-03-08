/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The TypeDefLink holds basic identifying information used to link one TypeDef to another.  It is used in
 * the definition of types, ie in the TypeDefs themselves.  Examples include linking a classification to an
 * entity, identifying super types and defining the entities at either end of a relationship.
 * <p>
 *     TypeDefs are identified using both the guid and the type name.  Both should be unique and most processing is
 *     with the type name because that is easiest for people to work with.  The guid provides a means to check the
 *     identity of the types since it is easy to introduce two types with the same name in the distributed model.
 * </p>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TypeDefLink extends TypeDefElementHeader
{
    private static final long    serialVersionUID = 1L;

    protected  String        guid               = null;
    protected  String        name               = null;
    protected  TypeDefStatus status             = null;
    protected  String        replacedByTypeGUID = null;
    protected  String        replacedByTypeName = null;

    /**
     * Default constructor
     */
    public TypeDefLink()
    {
        super();
    }


    /**
     * Typical constructor is passed the unique identifier and name of the typedef being constructed.
     * This constructor should only be used for new types.
     *
     * @param guid unique id for the TypeDef
     * @param name unique name for the TypeDef
     */
    TypeDefLink(String            guid,
                String            name)
    {
        super();

        this.guid = guid;
        this.name = name;
    }


    /**
     * Copy/clone constructor copies the values from the supplied template.
     *
     * @param template TypeDefLink
     */
    public TypeDefLink(TypeDefLink template)
    {
        super(template);

        if (template != null)
        {
            this.guid               = template.getGUID();
            this.name               = template.getName();
            this.status             = template.getStatus();
            this.replacedByTypeGUID = template.getReplacedByTypeGUID();
            this.replacedByTypeName = template.getReplacedByTypeName();
        }
    }


    /**
     * Return the unique identifier for this TypeDef.
     *
     * @return String guid
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Set up the unique identifier for this TypeDef.
     *
     * @param guid String guid
     */
    public void setGUID(String guid)
    {
        this.guid = guid;
    }


    /**
     * Return the type name for this TypeDef.  In simple environments, the type name is unique but where metadata
     * repositories from different vendors are in operation it is possible that 2 types may have a name clash.  The
     * GUID is the reliable unique identifier.
     *
     * @return String name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the type name for this TypeDef.  In simple environments, the type name is unique but where metadata
     * repositories from different vendors are in operation it is possible that 2 types may have a name clash.  The
     * GUID is the reliable unique identifier.
     *
     * @param name String name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the status of this attribute.
     *
     * @return status (null means ACTIVE)
     */
    public TypeDefStatus getStatus()
    {
        return status;
    }


    /**
     * Set up the status of this type.
     *
     * @param status status (null means ACTIVE)
     */
    public void setStatus(TypeDefStatus status)
    {
        this.status = status;
    }


    /**
     * If the type has been replaced, this contains the GUID of the new type.
     *
     * @return new type GUID
     */
    public String getReplacedByTypeGUID()
    {
        return replacedByTypeGUID;
    }


    /**
     * If the type has been replaced, this contains the GUID of the new type.
     *
     * @param replacedByTypeGUID new type GUID
     */
    public void setReplacedByTypeGUID(String replacedByTypeGUID)
    {
        this.replacedByTypeGUID = replacedByTypeGUID;
    }


    /**
     * If the type has been renamed, this contains the name of the new type.
     *
     * @return new type name
     */
    public String getReplacedByTypeName()
    {
        return replacedByTypeName;
    }


    /**
     * If the type has been renamed, this contains the name of the new type.
     *
     * @param replacedByTypeName new type name
     */
    public void setReplacedByTypeName(String replacedByTypeName)
    {
        this.replacedByTypeName = replacedByTypeName;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "TypeDefLink{" +
                "guid='" + guid + '\'' +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", replacedByTypeGUID='" + replacedByTypeGUID + '\'' +
                ", replacedByTypeName='" + replacedByTypeName + '\'' +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        TypeDefLink that = (TypeDefLink) objectToCompare;
        return Objects.equals(guid, that.guid) &&
                       Objects.equals(name, that.name) &&
                       status == that.status &&
                       Objects.equals(replacedByTypeGUID, that.replacedByTypeGUID) &&
                       Objects.equals(replacedByTypeName, that.replacedByTypeName);
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(guid, name, status, replacedByTypeGUID, replacedByTypeName);
    }
}

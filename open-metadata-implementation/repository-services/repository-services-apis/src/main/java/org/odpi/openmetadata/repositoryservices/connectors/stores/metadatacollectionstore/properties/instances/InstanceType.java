/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefCategory;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * InstanceType contains information from the instance's TypeDef that are useful for processing the instance.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class InstanceType extends InstanceElementHeader
{
    private static final long    serialVersionUID = 1L;

    private TypeDefCategory           typeDefCategory         = TypeDefCategory.UNKNOWN_DEF;
    private String                    typeDefGUID             = null;
    private String                    typeDefName             = null;
    private long                      typeDefVersion          = 0L;

    public static final long CURRENT_INSTANCE_TYPE_HEADER_VERSION = 1;

    /**
     * Default constructor relies on initialization of variables in the declaration.
     */
    public InstanceType()
    {
        super();
    }


    /**
     * Typical constructor that set all the properties at once.
     *
     * @param typeDefCategory the category of the type
     * @param typeDefGUID unique identifier of the type
     * @param typeDefName unique name of the type
     * @param typeDefVersion version number of the type
     */
    public InstanceType(TypeDefCategory           typeDefCategory,
                        String                    typeDefGUID,
                        String                    typeDefName,
                        long                      typeDefVersion)
    {
        this.typeDefCategory = typeDefCategory;
        this.typeDefGUID = typeDefGUID;
        this.typeDefName = typeDefName;
        this.typeDefVersion = typeDefVersion;
    }


    /**
     * Copy/clone constructor
     *
     * @param template instance type to copy
     */
    public InstanceType(InstanceType    template)
    {
        super(template);

        if (template != null)
        {
            this.typeDefCategory = template.getTypeDefCategory();
            this.typeDefGUID = template.getTypeDefGUID();
            this.typeDefName = template.getTypeDefName();
            this.typeDefVersion = template.getTypeDefVersion();
        }
    }


    /**
     * Return the category of this instance.  This defines the category of the TypeDef that determines its properties.
     *
     * @return TypeDefCategory enum
     */
    public TypeDefCategory getTypeDefCategory() { return typeDefCategory; }


    /**
     * Set up the category of this instance.  This defines the category of the TypeDef that determines its properties.
     *
     * @param typeDefCategory enum
     */
    public void setTypeDefCategory(TypeDefCategory typeDefCategory)
    {
        this.typeDefCategory = typeDefCategory;
    }


    /**
     * Return the unique identifier for the type of this instance.
     *
     * @return String unique identifier
     */
    public String getTypeDefGUID() { return typeDefGUID; }


    /**
     * Set up the unique identifier for the type of this instance.
     *
     * @param typeDefGUID String unique identifier
     */
    public void setTypeDefGUID(String typeDefGUID) { this.typeDefGUID = typeDefGUID; }


    /**
     * Return the name of this instance's type.
     *
     * @return String type name
     */
    public String getTypeDefName() { return typeDefName; }


    /**
     * Set up the name of this instance's type.
     *
     * @param typeDefName String type name
     */
    public void setTypeDefName(String typeDefName) { this.typeDefName = typeDefName; }


    /**
     * Return the version number of this instance's TypeDef.
     *
     * @return long version number
     */
    public long getTypeDefVersion()
    {
        return typeDefVersion;
    }


    /**
     * Set up the version for the TypeDef.
     *
     * @param typeDefVersion long version number
     */
    public void setTypeDefVersion(long typeDefVersion)
    {
        this.typeDefVersion = typeDefVersion;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "InstanceType{" +
                "typeDefName='" + typeDefName + '\'' +
                ", typeDefCategory=" + typeDefCategory +
                ", typeDefGUID='" + typeDefGUID + '\'' +
                ", typeDefVersion=" + typeDefVersion +
                '}';
    }

    /**
     * Validate if the supplied object equals this object.
     *
     * @param objectToCompare test object
     * @return boolean evaluation
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof InstanceType))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }

        InstanceType that = (InstanceType) objectToCompare;

        if (typeDefVersion != that.typeDefVersion)
        {
            return false;
        }
        if (typeDefCategory != that.typeDefCategory)
        {
            return false;
        }
        if (typeDefGUID != null ? ! typeDefGUID.equals(that.typeDefGUID) : that.typeDefGUID != null)
        {
            return false;
        }
        if (typeDefName != null ? ! typeDefName.equals(that.typeDefName) : that.typeDefName != null)
        {
            return false;
        }
        return true;
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + (typeDefCategory != null ? typeDefCategory.hashCode() : 0);
        result = 31 * result + (typeDefGUID != null ? typeDefGUID.hashCode() : 0);
        result = 31 * result + (typeDefName != null ? typeDefName.hashCode() : 0);
        result = 31 * result + (int) (typeDefVersion ^ (typeDefVersion >>> 32));
        return result;
    }
}

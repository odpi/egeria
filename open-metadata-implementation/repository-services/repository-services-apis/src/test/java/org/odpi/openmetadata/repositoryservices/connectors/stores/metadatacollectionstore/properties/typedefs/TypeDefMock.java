/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TypeDefMock is used to test the constructors of AttributeTypeDef
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TypeDefMock extends TypeDef
{
    private static final long     serialVersionUID = 1L;

    /**
     * Default Constructor
     */
    public TypeDefMock()
    {
        super();
    }


    /**
     * Minimal constructor is passed the category of the attribute type. Note that since
     * AttributeTypeDef is an abstract class, this method can only be called from a subclass.
     *
     * @param category category of this TypeDef
     */
    protected TypeDefMock(TypeDefCategory   category)
    {
        super(category);
    }


    /**
     * Typical constructor is passed the properties of the typedef's super class being constructed.
     *
     * @param category    category of this TypeDef
     * @param guid        unique id for the TypeDef
     * @param name        unique name for the TypeDef
     * @param version     active version number for the TypeDef
     * @param versionName name for the active version of the TypeDef
     */
    protected TypeDefMock(TypeDefCategory category,
                          String          guid,
                          String          name,
                          long            version,
                          String          versionName)
    {
        super(category, guid, name, version, versionName);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TypeDefMock(TypeDefMock template)
    {
        super(template);
    }



    /**
     * Delegate the process of cloning to the subclass.
     *
     * @return subclass of AttributeTypeDef
     */
    public TypeDef cloneFromSubclass()
    {
        return new TypeDefMock(this);
    }

}

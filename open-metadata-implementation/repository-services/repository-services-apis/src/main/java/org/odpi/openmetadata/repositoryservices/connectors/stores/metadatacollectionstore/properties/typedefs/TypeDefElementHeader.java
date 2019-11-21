/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TypeDefElementHeader provides a common base for all typedef information.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TypeDefElementHeader implements Serializable
{
    public static final long  CURRENT_TYPE_DEF_HEADER_VERSION = 1;

    private static final long serialVersionUID = 1L;

    /*
     * Version number for this header.  This is used to ensure that all of the critical header information
     * in read in a back-level version of the OMRS.  The default is 0 to indicate that the instance came from
     * a version of the OMRS that does not have a version number encoded.
     */
    private long headerVersion = 0;

    /**
     * Default constructor sets TypeDef to nulls.
     */
    public TypeDefElementHeader()
    {
        /*
         * Nothing to do
         */
    }


    /**
     * Copy/clone constructor set TypeDef to value in template.
     *
     * @param template TypeDefElementHeader
     */
    public TypeDefElementHeader(TypeDefElementHeader  template)
    {
        if (template != null)
        {
            this.headerVersion = template.getHeaderVersion();
        }
    }


    /**
     * Return the version of this header.  This is used by the OMRS to determine if it is back level and
     * should not process events from a source that is more advanced because it does not have the ability
     * to receive all of the header properties.
     *
     * @return long version number - the value is incremented each time a new non-informational field is added
     * to the type definition.
     */
    public long getHeaderVersion()
    {
        return headerVersion;
    }


    /**
     * Return the version of this header.  This is used by the OMRS to determine if it is back level and
     * should not process events from a source that is more advanced because it does not have the ability
     * to receive all of the header properties.
     *
     * @param headerVersion long version number - the value is incremented each time a new non-informational field is added
     * to the type definition.
     */
    public void setHeaderVersion(long headerVersion)
    {
        this.headerVersion = headerVersion;
    }


}

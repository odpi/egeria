/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.RepositoryElementHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceElementHeader;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TypeDefElementHeader provides a common base for all typedef information.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TypeDefElementHeader extends RepositoryElementHeader
{
    private static final long serialVersionUID = 1L;

    public static final long  CURRENT_TYPE_DEF_HEADER_VERSION = 1;

    /**
     * Default constructor sets TypeDef to nulls.
     */
    public TypeDefElementHeader()
    {
        super();

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
        super (template);

        /*
         * Nothing to do
         */
    }
}

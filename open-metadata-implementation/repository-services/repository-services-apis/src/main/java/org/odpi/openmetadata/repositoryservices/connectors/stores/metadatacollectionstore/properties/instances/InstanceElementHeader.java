/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.RepositoryElementHeader;

import java.io.Serial;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * InstanceElementHeader provides a common base for all instance information from the metadata collection.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ClassificationEntityExtension.class, name = "ClassificationEntityExtension"),
        @JsonSubTypes.Type(value = InstanceAuditHeader.class, name = "InstanceAuditHeader"),
        @JsonSubTypes.Type(value = InstanceGraph.class, name = "InstanceGraph"),
        @JsonSubTypes.Type(value = InstanceType.class, name = "InstanceType"),
        @JsonSubTypes.Type(value = InstancePropertyValue.class, name = "InstancePropertyValue"),
        @JsonSubTypes.Type(value = InstanceProperties.class, name = "InstanceProperties")
})
public abstract class InstanceElementHeader extends RepositoryElementHeader
{
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Default Constructor sets the instance to nulls
     */
    public InstanceElementHeader()
    {
        super();

        /*
         * Nothing to do.
         */
    }


    /**
     * Copy/clone constructor set values from the template
     *
     * @param template InstanceElementHeader to copy
     */
    public InstanceElementHeader(InstanceElementHeader   template)
    {
        super (template);

        /*
         * Nothing to do.
         */
    }
}

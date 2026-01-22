/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AuthoredReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * <p>
 * DataProcessingDescriptionProperties describes a data processing routine.  It may involve access to multiple
 * data sources.  The processing of each data source is described in a separate data processing action.
 * </p><p>
 * The data processing description can be linked to a process to describe it behaviour (via a DataProcessingSpecification
 * relationship).  It can also be linked to data processing purposes to show how its work has been classified
 * to support specific processes.
 * </p>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataProcessingDescriptionProperties extends AuthoredReferenceableProperties
{

    /**
     * Default constructor
     */
    public DataProcessingDescriptionProperties()
    {
        super();
        super.typeName = OpenMetadataType.DATA_PROCESSING_DESCRIPTION.typeName;
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public DataProcessingDescriptionProperties(DataProcessingDescriptionProperties template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param template object to copy
     */
    public DataProcessingDescriptionProperties(AuthoredReferenceableProperties template)
    {
        super(template);
        super.typeName = OpenMetadataType.DATA_PROCESSING_DESCRIPTION.typeName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataProcessingDescriptionProperties{" +
                "} " + super.toString();
    }
}
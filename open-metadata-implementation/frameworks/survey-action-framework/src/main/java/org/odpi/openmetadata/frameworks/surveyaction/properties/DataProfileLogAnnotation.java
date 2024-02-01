/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.surveyaction.properties;

import com.fasterxml.jackson.annotation.*;

import java.io.Serial;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataProfileLogAnnotation is an annotation that is used when the profile data generated is too big to store in the metadata server
 * and has been located in log files instead.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataProfileLogAnnotation extends DataFieldAnnotation
{
    @Serial
    private static final long serialVersionUID = 1L;

    private List<String> dataProfileLogGUIDs = null;

    /**
     * Default constructor
     */
    public DataProfileLogAnnotation()
    {
    }


    /**
     * Copy clone constructor
     *
     * @param template object to copy
     */
    public DataProfileLogAnnotation(DataProfileLogAnnotation template)
    {
        super(template);

        if (template != null)
        {
            dataProfileLogGUIDs = template.getDataProfileLogGUIDs();
        }
    }


    /**
     * Return the names of the log files used to store the profile data.
     *
     * @return list of guid of log files
     */
    public List<String> getDataProfileLogGUIDs()
    {
        if (dataProfileLogGUIDs == null)
        {
            return null;
        }
        else if (dataProfileLogGUIDs.isEmpty())
        {
            return null;
        }

        return dataProfileLogGUIDs;
    }


    /**
     * Set up the description of the log files used to store the profile data.
     *
     * @param dataProfileLogFileNames list of guids of log files
     */
    public void setDataProfileLogGUIDs(List<String> dataProfileLogFileNames)
    {
        this.dataProfileLogGUIDs = dataProfileLogFileNames;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataProfileLogAnnotation{" +
                "dataProfileLogFiles=" + dataProfileLogGUIDs +
                "} " + super.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        DataProfileLogAnnotation that = (DataProfileLogAnnotation) objectToCompare;
        return Objects.equals(dataProfileLogGUIDs, that.dataProfileLogGUIDs);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), dataProfileLogGUIDs);
    }
}

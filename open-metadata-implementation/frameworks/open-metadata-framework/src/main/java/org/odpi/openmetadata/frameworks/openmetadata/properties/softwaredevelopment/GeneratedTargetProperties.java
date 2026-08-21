/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.softwaredevelopment;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GeneratedTargetProperties provides the bean for the GeneratedTarget classification.  It identifies an element
 * that is the output of a build program or script, and so can be reproduced at will.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GeneratedTargetProperties extends ClassificationBeanProperties
{
    private String purpose          = null;
    private String buildId          = null;
    private String buildTool        = null;
    private String buildToolVersion = null;


    /**
     * Default constructor
     */
    public GeneratedTargetProperties()
    {
        super();
        super.typeName = OpenMetadataType.GENERATED_TARGET_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public GeneratedTargetProperties(GeneratedTargetProperties template)
    {
        super(template);

        if (template != null)
        {
            purpose          = template.getPurpose();
            buildId          = template.getBuildId();
            buildTool        = template.getBuildTool();
            buildToolVersion = template.getBuildToolVersion();
        }
    }


    /**
     * Return the purpose of the target.
     *
     * @return string
     */
    public String getPurpose()
    {
        return purpose;
    }


    /**
     * Set up the purpose of the target.
     *
     * @param purpose string
     */
    public void setPurpose(String purpose)
    {
        this.purpose = purpose;
    }


    /**
     * Return the identifier of the build process that created the target.
     *
     * @return string
     */
    public String getBuildId()
    {
        return buildId;
    }


    /**
     * Set up the identifier of the build process that created the target.
     *
     * @param buildId string
     */
    public void setBuildId(String buildId)
    {
        this.buildId = buildId;
    }


    /**
     * Return the name of the build tool that created the target.
     *
     * @return string
     */
    public String getBuildTool()
    {
        return buildTool;
    }


    /**
     * Set up the name of the build tool that created the target.
     *
     * @param buildTool string
     */
    public void setBuildTool(String buildTool)
    {
        this.buildTool = buildTool;
    }


    /**
     * Return the version of the build tool that created the target.
     *
     * @return string
     */
    public String getBuildToolVersion()
    {
        return buildToolVersion;
    }


    /**
     * Set up the version of the build tool that created the target.
     *
     * @param buildToolVersion string
     */
    public void setBuildToolVersion(String buildToolVersion)
    {
        this.buildToolVersion = buildToolVersion;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "GeneratedTargetProperties{" +
                "purpose='" + purpose + '\'' +
                ", buildId='" + buildId + '\'' +
                ", buildTool='" + buildTool + '\'' +
                ", buildToolVersion='" + buildToolVersion + '\'' +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        GeneratedTargetProperties that = (GeneratedTargetProperties) objectToCompare;
        return Objects.equals(purpose, that.purpose) &&
                       Objects.equals(buildId, that.buildId) &&
                       Objects.equals(buildTool, that.buildTool) &&
                       Objects.equals(buildToolVersion, that.buildToolVersion);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), purpose, buildId, buildTool, buildToolVersion);
    }
}

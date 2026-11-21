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
 * BranchProperties provides the bean for the Branch classification.  It identifies an element that holds the files
 * for a specific branch of a source code repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class BranchProperties extends ClassificationBeanProperties
{
    private String displayName = null;
    private String scope       = null;
    private String purpose     = null;
    private String url         = null;


    /**
     * Default constructor
     */
    public BranchProperties()
    {
        super();
        super.typeName = OpenMetadataType.BRANCH_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public BranchProperties(BranchProperties template)
    {
        super(template);

        if (template != null)
        {
            displayName = template.getDisplayName();
            scope       = template.getScope();
            purpose     = template.getPurpose();
            url         = template.getURL();
        }
    }


    /**
     * Return the name of the branch.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the name of the branch.
     *
     * @param displayName string
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the scope of the contents of the branch.
     *
     * @return string
     */
    public String getScope()
    {
        return scope;
    }


    /**
     * Set up the scope of the contents of the branch.
     *
     * @param scope string
     */
    public void setScope(String scope)
    {
        this.scope = scope;
    }


    /**
     * Return the purpose of the branch.
     *
     * @return string
     */
    public String getPurpose()
    {
        return purpose;
    }


    /**
     * Set up the purpose of the branch.
     *
     * @param purpose string
     */
    public void setPurpose(String purpose)
    {
        this.purpose = purpose;
    }


    /**
     * Return the url to access the master copy of this branch in the repository.
     *
     * @return string
     */
    public String getURL()
    {
        return url;
    }


    /**
     * Set up the url to access the master copy of this branch in the repository.
     *
     * @param url string
     */
    public void setURL(String url)
    {
        this.url = url;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "BranchProperties{" +
                "displayName='" + displayName + '\'' +
                ", scope='" + scope + '\'' +
                ", purpose='" + purpose + '\'' +
                ", url='" + url + '\'' +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        BranchProperties that = (BranchProperties) objectToCompare;
        return Objects.equals(displayName, that.displayName) &&
                Objects.equals(scope, that.scope) &&
                Objects.equals(purpose, that.purpose) &&
                Objects.equals(url, that.url);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), displayName, scope, purpose, url);
    }
}

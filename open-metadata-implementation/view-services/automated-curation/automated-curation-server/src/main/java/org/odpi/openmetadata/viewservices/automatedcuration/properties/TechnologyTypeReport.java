/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.automatedcuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ExternalReferenceElement;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TechnologyTypeReport summarizes the reference data for a particular technology.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TechnologyTypeReport extends TechnologyTypeSummary
{
    private List<CatalogTemplate>          catalogTemplates    = null;
    private List<ResourceDescription>      resourceList        = null;
    private List<ExternalReferenceElement> externalReferences  = null;

    /**
     * Default constructor
     */
    public TechnologyTypeReport()
    {
        super();
    }

    /**
     * Copy constructor
     */
    public TechnologyTypeReport(TechnologyTypeSummary template)
    {
        super(template);
    }


    /**
     * Return the list of templates for this technology type.
     * The templates are used to create catalog entities for an instance of the technology type.
     *
     * @return list
     */
    public List<CatalogTemplate> getCatalogTemplates()
    {
        return catalogTemplates;
    }


    /**
     * Set up the of templates for this technology type.
     *
     * @param catalogTemplates list
     */
    public void setCatalogTemplates(List<CatalogTemplate> catalogTemplates)
    {
        this.catalogTemplates = catalogTemplates;
    }


    /**
     * Return the list of resources available for working with this technology type.
     *
     * @return list
     */
    public List<ResourceDescription> getResourceList()
    {
        return resourceList;
    }


    /**
     * Set up the list of resources for working with this technology type.
     *
     * @param resourceList list
     */
    public void setResourceList(List<ResourceDescription> resourceList)
    {
        this.resourceList = resourceList;
    }


    /**
     * Return the list of external references that describe this technology type in detail.
     *
     * @return list
     */
    public List<ExternalReferenceElement> getExternalReferences()
    {
        return externalReferences;
    }


    /**
     * Set up the list of external references that describe this technology type in detail.
     *
     * @param externalReferences list
     */
    public void setExternalReferences(List<ExternalReferenceElement> externalReferences)
    {
        this.externalReferences = externalReferences;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "TechnologyTypeReport{" +
                "catalogTemplates=" + catalogTemplates +
                ", resourceList=" + resourceList +
                ", externalReferences=" + externalReferences +
                "} " + super.toString();
    }
}

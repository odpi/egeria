/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.automatedcuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetowner.metadataelements.ExternalReferenceElement;

import java.util.List;
import java.util.Map;

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
    private String                         technologySuperType = null;
    private List<String>                   technologySubtypes  = null;
    private String                         openMetadataType    = null;
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
     * Return the full name of a more generic technology type.
     *
     * @return name
     */
    public String getTechnologySuperType()
    {
        return technologySuperType;
    }


    /**
     * Set up the full name of a more generic technology type.
     *
     * @param technologySuperType name
     */
    public void setTechnologySuperType(String technologySuperType)
    {
        this.technologySuperType = technologySuperType;
    }


    /**
     * Return the names of specific technology types.
     *
     * @return list of names
     */
    public List<String> getTechnologySubtypes()
    {
        return technologySubtypes;
    }


    /**
     * Set up the names of specific technology types.
     *
     * @param technologySubtypes list of names
     */
    public void setTechnologySubtypes(List<String> technologySubtypes)
    {
        this.technologySubtypes = technologySubtypes;
    }



    public String getOpenMetadataType()
    {
        return openMetadataType;
    }

    public void setOpenMetadataType(String openMetadataType)
    {
        this.openMetadataType = openMetadataType;
    }

    public List<CatalogTemplate> getCatalogTemplates()
    {
        return catalogTemplates;
    }

    public void setCatalogTemplates(List<CatalogTemplate> catalogTemplates)
    {
        this.catalogTemplates = catalogTemplates;
    }


    public List<ResourceDescription> getResourceList()
    {
        return resourceList;
    }

    public void setResourceList(List<ResourceDescription> resourceList)
    {
        this.resourceList = resourceList;
    }

    public List<ExternalReferenceElement> getExternalReferences()
    {
        return externalReferences;
    }

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
                "technologySuperType='" + technologySuperType + '\'' +
                ", technologySubtypes=" + technologySubtypes +
                ", openMetadataType='" + openMetadataType + '\'' +
                ", catalogTemplates=" + catalogTemplates +
                ", resourceList=" + resourceList +
                ", externalReferences=" + externalReferences +
                '}';
    }
}

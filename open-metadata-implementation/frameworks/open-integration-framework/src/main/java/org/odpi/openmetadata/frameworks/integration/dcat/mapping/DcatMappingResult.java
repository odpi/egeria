/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.dcat.mapping;

import java.util.ArrayList;
import java.util.List;

/**
 * DcatMappingResult describes what happened when a DCAT document was catalogued in open metadata.
 */
public class DcatMappingResult
{
    /**
     * What was done to an open metadata element.
     */
    public enum Action
    {
        /**
         * A new element was created.
         */
        CREATED,

        /**
         * An existing element was updated.
         */
        UPDATED
    }


    /**
     * Describes an open metadata element that was created or updated for a resource in the document.
     *
     * @param kind kind of DCAT resource (Dataset, DatasetSeries, DataService or Catalog)
     * @param resourceKey the IRI, identifier or title that identifies the resource
     * @param qualifiedName qualified name of the open metadata element
     * @param elementGUID unique identifier of the open metadata element
     * @param action what was done
     */
    public record CataloguedResource(String kind,
                                     String resourceKey,
                                     String qualifiedName,
                                     String elementGUID,
                                     Action action)
    {
    }

    private final List<CataloguedResource> cataloguedResources = new ArrayList<>();
    private final List<String>             warnings            = new ArrayList<>();


    /**
     * Default constructor
     */
    public DcatMappingResult()
    {
    }


    /**
     * Record that a resource has been catalogued.
     *
     * @param cataloguedResource description of the element
     */
    public void addCataloguedResource(CataloguedResource cataloguedResource)
    {
        cataloguedResources.add(cataloguedResource);
    }


    /**
     * Return the elements that were created or updated for the catalogs, datasets, dataset series and data services
     * in the document.
     *
     * @return list
     */
    public List<CataloguedResource> getCataloguedResources()
    {
        return cataloguedResources;
    }


    /**
     * Return the unique identifiers of the elements that were created or updated for the catalogs, datasets,
     * dataset series and data services in the document.
     *
     * @return list of unique identifiers
     */
    public List<String> getElementGUIDs()
    {
        List<String> elementGUIDs = new ArrayList<>();

        for (CataloguedResource cataloguedResource : cataloguedResources)
        {
            elementGUIDs.add(cataloguedResource.elementGUID());
        }

        return elementGUIDs;
    }


    /**
     * Record something in the document that could not be mapped.
     *
     * @param warning description of the problem
     */
    public void addWarning(String warning)
    {
        warnings.add(warning);
    }


    /**
     * Return the descriptions of the parts of the document that could not be mapped.
     *
     * @return list of warnings
     */
    public List<String> getWarnings()
    {
        return warnings;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DcatMappingResult{" +
                       "cataloguedResources=" + cataloguedResources +
                       ", warnings=" + warnings +
                       '}';
    }
}

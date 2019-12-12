/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.adminservices.configuration.registration;

import java.io.Serializable;

/**
 * ViewServiceDescription provides a list of registered view services.
 */
public enum ViewServiceDescription implements Serializable
{
    ASSET_SEARCH  (1010,
            "Asset Search OMVS",
            "asset-search",
            "Search and understand your assets.",
            //TODO create appropriate wiki
            "https://odpi.github.io/egeria/open-metadata-implementation/access-services/asset-catalog/"),
    OPEN_LINEAGE  (1020,
            "Open Lineage OMVS",
            "open-lineage",
            "Explore open lineage.",
            //TODO create appropriate wiki
            "https://odpi.github.io/egeria/open-metadata-implementation/access-services/asset-owner/"),
    SUBJECT_AREA  (1030,
            "Subject Area OMVS",
            "subject-area",
            "Subject area authoring.",
            //TODO create appropriate wiki
            "https://odpi.github.io/egeria/open-metadata-implementation/access-services/subject-area/"),
    TYPE_EXPLORER (1040,
            "Type Explorer OMVS",
            "type-explorer",
            "Explore the open metadata types.",
            //TODO create appropriate wiki
            "https://odpi.github.io/egeria/open-metadata-implementation/access-services/subject-area/")
    ;

    private static final long     serialVersionUID    = 1L;

    private int    viewServiceCode;
    private String viewServiceName;
    private String viewServiceURLMarker;
    private String viewServiceDescription;
    private String viewServiceWiki;


    /**
     * Default Constructor
     *
     * @param viewServiceCode ordinal for this UI view
     * @param viewServiceURLMarker string used in URLs
     * @param viewServiceName symbolic name for this UI view
     * @param viewServiceDescription short description for this UI view
     * @param viewServiceWiki wiki page for the UI view for this UI view
     */
    ViewServiceDescription(int                            viewServiceCode,
                           String                         viewServiceName,
                           String                         viewServiceURLMarker,
                           String                         viewServiceDescription,
                           String                         viewServiceWiki)
    {
        /*
         * Save the values supplied
         */
        this.viewServiceCode = viewServiceCode;
        this.viewServiceName = viewServiceName;
        this.viewServiceURLMarker = viewServiceURLMarker;
        this.viewServiceDescription = viewServiceDescription;
        this.viewServiceWiki = viewServiceWiki;
    }


    /**
     * Return the code for this enum instance
     *
     * @return int type code
     */
    public int getViewServiceCode()
    {
        return viewServiceCode;
    }


    /**
     * Return the default name for this enum instance.
     *
     * @return String default name
     */
    public String getViewServiceName()
    {
        return viewServiceName;
    }


    /**
     * Return the string that appears in the REST API URL that identifies the owning service.
     * Null means no REST APIs supported by this service.
     *
     * @return String default name
     */
    public String getViewServiceURLMarker()
    {
        return viewServiceURLMarker;
    }


    /**
     * Return the default description for the type for this enum instance.
     *
     * @return String default description
     */
    public String getViewServiceDescription()
    {
        return viewServiceDescription;
    }


    /**
     * Return the URL for the wiki page describing this UI view.
     *
     * @return String URL name for the wiki page
     */
    public String getViewServiceWiki()
    {
        return viewServiceWiki;
    }


}

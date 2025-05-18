/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.converters;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CollectionElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CollectionGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.CollectionMemberGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * CollectionGraphConverter generates a CollectionGraph element from a Collection entity
 * and related elements.
 */
public class CollectionGraphConverter<B> extends OpenMetadataConverterBase<B>
{
    private final CollectionElement           collectionElement;
    private final List<CollectionMemberGraph> collectionMemberGraphs;

    /**
     * Constructor
     *
     * @param collectionElement starting collection object
     * @param collectionMemberGraphs nested graph of members
     * @param propertyHelper helper object to parse entity
     * @param serviceName name of this component
     * @param serverName local server name
     */
    public CollectionGraphConverter(CollectionElement           collectionElement,
                                    List<CollectionMemberGraph> collectionMemberGraphs,
                                    PropertyHelper              propertyHelper,
                                    String                      serviceName,
                                    String                      serverName)
    {
        super(propertyHelper, serviceName, serverName);

        this.collectionElement = collectionElement;
        this.collectionMemberGraphs = collectionMemberGraphs;
    }



}

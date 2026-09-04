/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.properties;


import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.integration.context.CatalogTargetContext;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationExplorerClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;

import java.util.Objects;

/**
 * RequestedCatalogTarget describes a catalog target that an integration connector should refresh.
 * <br><br>
 * The catalog target's element is read from open metadata on demand rather than up front.  The targets manager
 * retrieves each target with its header, its properties and the relationship that makes it a target, and
 * nothing more; that is enough to identify it and to build the connector to it.  A processor that needs to
 * see what is attached to the element asks for it here, and says how much: {@link #getCatalogTargetElement()}
 * reads the element in full, as a default read would, and {@link #getCatalogTargetElement(GetOptions)} reads
 * exactly what the caller's options describe.  Either way the result is kept for the rest of the refresh, so
 * reading it twice costs one read, and the targets manager clears it before each refresh so that no refresh
 * works from the last one's picture.
 * <br><br>
 * Reading in full assembles the element's whole graph to the default depth, one related-elements call per
 * element per level.  Done up front for every target, that was where a refresh with many targets spent most
 * of its time before any target was refreshed.
 */
public class RequestedCatalogTarget extends CatalogTarget
{
    protected final Connector            connectorToTarget;
    protected final CatalogTargetContext integrationContext;

    /*
     * The element as most recently read on demand, the options it was read with (null means the default, full
     * read), and the exception if the read failed.
     */
    private OpenMetadataRootElement retrievedCatalogTargetElement = null;
    private GetOptions              retrievedWithOptions          = null;
    private boolean                 retrievedInFull               = false;
    private Exception               retrievalException            = null;


    /**
     * Constructor for new catalog target processor
     *
     * @param template object to copy
     * @param catalogTargetContext context for this catalog target
     * @param connectorToTarget connector to access the target resource
     */
    public RequestedCatalogTarget(CatalogTarget        template,
                                  CatalogTargetContext catalogTargetContext,
                                  Connector            connectorToTarget)
    {
        super(template);

        this.connectorToTarget  = connectorToTarget;
        this.integrationContext = catalogTargetContext;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public RequestedCatalogTarget(RequestedCatalogTarget template)
    {
        super(template);

        if (template != null)
        {
            connectorToTarget  = template.getConnectorToTarget();
            integrationContext = template.getIntegrationContext();
        }
        else
        {
            connectorToTarget = null;
            integrationContext = null;
        }
    }


    /**
     * Indicates that the catalog target processor is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        if (connectorToTarget != null)
        {
            connectorToTarget.start();
        }
    }


    /**
     * Return the header of the catalog target's element - its identity, type, classifications and versions.
     * This was retrieved with the catalog target and costs nothing to return.  Use it in preference to
     * {@link #getCatalogTargetElement()} when the identity is all that is needed.
     *
     * @return element header, or null if the catalog target carries no element
     */
    public ElementHeader getCatalogTargetElementHeader()
    {
        OpenMetadataRootElement asRetrieved = super.getCatalogTargetElement();

        if (asRetrieved != null)
        {
            return asRetrieved.getElementHeader();
        }

        return null;
    }


    /**
     * Return the catalog target's element read in full - with everything attached to it, as a default read of
     * the element would return - reading it from open metadata the first time it is asked for in a refresh and
     * returning the same element thereafter.
     * <br><br>
     * This is what a processor that reads the target's schema, capabilities or other attachments should use,
     * and it is what existing processors get when they call the accessor they always called.  A processor that
     * uses only the header should call {@link #getCatalogTargetElementHeader()}, and one that uses a known
     * part of the graph should call {@link #getCatalogTargetElement(GetOptions)} and say which part.
     * <br><br>
     * If the read fails the element as retrieved by the targets manager - header and properties - is returned
     * instead, and the exception is available from {@link #getCatalogTargetElementRetrievalException()}.
     *
     * @return the element
     */
    @Override
    public OpenMetadataRootElement getCatalogTargetElement()
    {
        if ((retrievedCatalogTargetElement != null) && (retrievedInFull))
        {
            return retrievedCatalogTargetElement;
        }

        try
        {
            return this.getCatalogTargetElement(null);
        }
        catch (Exception error)
        {
            retrievalException = error;

            return super.getCatalogTargetElement();
        }
    }


    /**
     * Return the catalog target's element read with the supplied options - which say how much of what is
     * attached to the element to bring back - reading it from open metadata and keeping the result for the
     * rest of the refresh.  A later call in the same refresh with the same options returns the kept element
     * without another read.
     *
     * @param getOptions what to read; null means a full, default read
     * @return the element, or the element as retrieved by the targets manager if this catalog target has no
     *         context to read through
     * @throws InvalidParameterException the element cannot be read - probably a bug in the calling code
     * @throws PropertyServerException the repository is not available
     * @throws UserNotAuthorizedException the connector has been disconnected, or its userId may not read the element
     */
    public OpenMetadataRootElement getCatalogTargetElement(GetOptions getOptions) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        ElementHeader header = this.getCatalogTargetElementHeader();

        if ((header == null) || (integrationContext == null))
        {
            return super.getCatalogTargetElement();
        }

        if ((retrievedCatalogTargetElement != null) && (Objects.equals(retrievedWithOptions, getOptions)))
        {
            return retrievedCatalogTargetElement;
        }

        ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient();
        GetOptions                   effectiveOptions             = (getOptions == null) ? classificationExplorerClient.getGetOptions() : getOptions;

        retrievedCatalogTargetElement = classificationExplorerClient.getRootElementByGUID(header.getGUID(), effectiveOptions);
        retrievedWithOptions          = getOptions;
        retrievedInFull               = (getOptions == null);
        retrievalException            = null;

        return retrievedCatalogTargetElement;
    }


    /**
     * Forget the element read on demand, so that the next request reads it again.  The targets manager calls
     * this before each refresh of the catalog target.
     */
    public void clearRetrievedCatalogTargetElement()
    {
        retrievedCatalogTargetElement = null;
        retrievedWithOptions          = null;
        retrievedInFull               = false;
        retrievalException            = null;
    }


    /**
     * Return the exception from the last on-demand read of the element that failed, if
     * {@link #getCatalogTargetElement()} fell back to the element as retrieved by the targets manager.
     *
     * @return exception or null
     */
    public Exception getCatalogTargetElementRetrievalException()
    {
        return retrievalException;
    }


    /**
     * Return the connector to the target resource.
     *
     * @return connector
     */
    public Connector getConnectorToTarget()
    {
        return connectorToTarget;
    }


    /**
     * Return the network address from the connector's connection's endpoint.
     *
     * @return string or null
     */
    protected String getNetworkAddress()
    {
        Connection assetConnection = connectorToTarget.getConnection();

        if (assetConnection != null)
        {
            Endpoint endpointDetails = assetConnection.getEndpoint();

            if (endpointDetails != null)
            {
                return endpointDetails.getNetworkAddress();
            }
        }

        return null;
    }


    /**
     * Return the context for this catalog target.
     *
     * @return context
     */
    public CatalogTargetContext getIntegrationContext()
    {
        return integrationContext;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RequestedCatalogTarget{" +
                "connectorToTarget=" + connectorToTarget +
                ", integrationContext=" + integrationContext +
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
        RequestedCatalogTarget that = (RequestedCatalogTarget) objectToCompare;
        return Objects.equals(connectorToTarget, that.connectorToTarget) && Objects.equals(integrationContext, that.integrationContext);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), connectorToTarget, integrationContext);
    }
}

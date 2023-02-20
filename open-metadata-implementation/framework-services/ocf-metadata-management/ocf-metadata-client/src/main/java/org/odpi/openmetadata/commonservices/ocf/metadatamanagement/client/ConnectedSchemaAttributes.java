/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.client;

import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.SchemaAttributesResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaAttribute;
import org.odpi.openmetadata.frameworks.connectors.properties.SchemaAttributes;

import java.util.ArrayList;
import java.util.List;


/**
 * ConnectedSchemaAttributes provides the open metadata iterator for the attributes of a schema.
 * This iterator extends the Open Connector Framework (OCF) SchemaAttributes object.
 */
public class ConnectedSchemaAttributes extends SchemaAttributes
{
    private static final long    serialVersionUID = 1L;

    private String                 serviceName;
    private String                 serverName;
    private String                 userId;
    private String                 omasServerURL;
    private String                 schemaGUID;
    private int                    maxCacheSize;
    private OCFRESTClient          restClient;

    private final RESTExceptionHandler   restExceptionHandler    = new RESTExceptionHandler();

    /**
     * Typical constructor creates an iterator with the supplied list of elements.
     *
     * @param serviceName calling service
     * @param serverName  name of the server.
     * @param userId user id to use on server calls.
     * @param omasServerURL url root of the server to use.
     * @param schemaGUID unique identifier of the parent schema type.
     * @param totalElementCount the total number of elements to process.  A negative value is converted to 0.
     * @param maxCacheSize maximum number of elements that should be retrieved from the property server and
     *                     cached in the element list at any one time.  If a number less than one is supplied, 1 is used.
     * @param restClient client to call REST API
     */
    ConnectedSchemaAttributes(String                 serviceName,
                              String                 serverName,
                              String                 userId,
                              String                 omasServerURL,
                              String                 schemaGUID,
                              int                    totalElementCount,
                              int                    maxCacheSize,
                              OCFRESTClient          restClient)
    {
        super(totalElementCount, maxCacheSize);

        this.serviceName     = serviceName;
        this.serverName      = serverName;
        this.userId          = userId;
        this.omasServerURL   = omasServerURL;
        this.schemaGUID      = schemaGUID;
        this.maxCacheSize    = maxCacheSize;
        this.restClient      = restClient;

    }


    /**
     * Copy/clone constructor.  Used to reset iterator element pointer to 0;
     *
     * @param template type-specific iterator to copy; null to create an empty iterator
     */
    private ConnectedSchemaAttributes(ConnectedSchemaAttributes template)
    {
        super(template);

        if (template != null)
        {
            this.serviceName     = template.serviceName;
            this.serverName      = template.serverName;
            this.userId          = template.userId;
            this.omasServerURL   = template.omasServerURL;
            this.schemaGUID      = template.schemaGUID;
            this.maxCacheSize    = template.maxCacheSize;
            this.restClient      = template.restClient;
        }
    }


    /**
     * Clones this iterator.
     *
     * @return new cloned object.
     */
    @Override
    protected SchemaAttributes cloneIterator()
    {
        return new ConnectedSchemaAttributes(this);
    }


    /**
     * Method implemented by subclass to retrieve the next cached list of elements.
     *
     * @param cacheStartPointer where to start the cache.
     * @param maximumSize maximum number of elements in the cache.
     * @return list of elements corresponding to the supplied cache pointers.
     * @throws PropertyServerException there is a problem retrieving elements from the property (metadata) server.
     */
    @Override
    protected List<ElementBase> getCachedList(int  cacheStartPointer,
                                              int  maximumSize) throws PropertyServerException
    {
        final String   methodName = "SchemaAttributes.getCachedList";
        final String   urlTemplate = "/servers/{0}/open-metadata/framework-services/{1}/connected-asset/users/{2}/assets/schemas/{3}/schema-attributes?elementStart={4}&maxElements={5}";


        try
        {
            SchemaAttributesResponse restResult = restClient.callOCFSchemaAttributesGetRESTCall(methodName,
                                                                                                omasServerURL + urlTemplate,
                                                                                                serverName,
                                                                                                serviceName,
                                                                                                userId,
                                                                                                schemaGUID,
                                                                                                cacheStartPointer,
                                                                                                maximumSize);

            restExceptionHandler.detectAndThrowInvalidParameterException(restResult);
            restExceptionHandler.detectAndThrowUserNotAuthorizedException(restResult);
            restExceptionHandler.detectAndThrowPropertyServerException(restResult);

            List<SchemaAttribute> schemaAttributes = restResult.getList();
            if ((schemaAttributes == null) || (schemaAttributes.isEmpty()))
            {
                return null;
            }
            else
            {
                List<ElementBase> resultList = new ArrayList<>();

                for (SchemaAttribute schemaAttribute : schemaAttributes)
                {
                    if (schemaAttribute != null)
                    {
                        resultList.add(new SchemaAttribute(schemaAttribute));
                    }
                }

                if (! resultList.isEmpty())
                {
                    return resultList;
                }
            }
        }
        catch (Exception error)
        {
            restExceptionHandler.handleUnexpectedException(error, methodName, serverName, omasServerURL);
        }

        return null;
    }
}

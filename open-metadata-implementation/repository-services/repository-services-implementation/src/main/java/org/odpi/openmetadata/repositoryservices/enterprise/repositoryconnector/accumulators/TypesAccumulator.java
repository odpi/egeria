/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.enterprise.repositoryconnector.accumulators;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefGallery;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TypesAccumulator accumulates type information for each of the members of the connected cohort members.
 */
public class TypesAccumulator extends QueryAccumulatorBase
{
    private volatile Map<String, AttributeTypeDef> accumulatedAttributeTypeDefs = new HashMap<>();
    private volatile Map<String, TypeDef>          accumulatedTypeDefs = new HashMap<>();


    /**
     * Construct a type def gallery accumulator.  Its base class manages the common variables needed to
     * control the execution of requests across all members of the cohort(s).
     *
     * @param localMetadataCollectionId collection Id of local repository - null means no local repository
     * @param auditLog audit log provides destination for log messages
     * @param repositoryValidator validator provides common validation routines
     */
    public TypesAccumulator(String                   localMetadataCollectionId,
                            AuditLog                 auditLog,
                            OMRSRepositoryValidator  repositoryValidator)
    {
        super(localMetadataCollectionId, auditLog, repositoryValidator);
    }


    /**
     * Process an attribute type definition received from an open metadata repository.
     *
     * @param incomingAttributeTypeDef type returned from an open metadata repository
     * @param metadataCollectionId unique identifier for the collection of metadata stored in this repository
     *                             This is used for error reporting.
     */
    private  void addAttributeTypeDef(AttributeTypeDef   incomingAttributeTypeDef,
                                      String             metadataCollectionId)
    {
        if ((incomingAttributeTypeDef != null) && (incomingAttributeTypeDef.getGUID() != null) && (metadataCollectionId != null))
        {
            String            typeGUID = incomingAttributeTypeDef.getGUID();
            String            typeVersion = incomingAttributeTypeDef.getVersionName();

            if (typeVersion != null)
            {
                accumulatedAttributeTypeDefs.put(typeGUID + typeVersion, incomingAttributeTypeDef);
            }
            else
            {
                accumulatedAttributeTypeDefs.put(typeGUID, incomingAttributeTypeDef);
            }

        }
    }


    /**
     * Process a type definition received from an open metadata repository.
     *
     * @param incomingTypeDef type returned from an open metadata repository
     * @param metadataCollectionId unique identifier for the collection of metadata stored in this repository
     *                             This is used for error reporting.
     */
    private  void addTypeDef(TypeDef   incomingTypeDef,
                             String    metadataCollectionId)
    {
        if ((incomingTypeDef != null) && (incomingTypeDef.getGUID() != null) && (metadataCollectionId != null))
        {
            String            typeGUID = incomingTypeDef.getGUID();
            String            typeVersion = incomingTypeDef.getVersionName();

            if (typeVersion != null)
            {
                accumulatedTypeDefs.put(typeGUID + typeVersion, incomingTypeDef);
            }
            else
            {
                accumulatedTypeDefs.put(typeGUID, incomingTypeDef);
            }

        }
    }


    /**
     * Add a gallery of types to the accumulator. This method is included to save the executors from coding this
     * loop to process each instance.
     *
     * @param types gallery of retrieved types
     * @param metadataCollectionId source metadata collection
     */
    public synchronized void addTypeDefGallery(TypeDefGallery types,
                                               String         metadataCollectionId)
    {
        int numberOrAttributeTypeDefs = 0;
        int numberOfTypeDefs = 0;

        if (types != null)
        {
            List<AttributeTypeDef> attributeTypeDefs = types.getAttributeTypeDefs();
            if (attributeTypeDefs != null)
            {
                for (AttributeTypeDef type : attributeTypeDefs)
                {
                    this.addAttributeTypeDef(type, metadataCollectionId);
                }
            }

            List<TypeDef> typeDefs = types.getTypeDefs();
            if (typeDefs != null)
            {
                for (TypeDef type : typeDefs)
                {
                    this.addTypeDef(type, metadataCollectionId);
                }
            }
        }

        super.setResultsReturned(metadataCollectionId, numberOrAttributeTypeDefs + numberOfTypeDefs);
    }


    /**
     * Extract the results - this will the a unique list of types selected from the responses
     * supplied to this accumulator.  It should be called once all the executors have completed processing
     * their request(s).
     *
     * @return type definition gallery
     */
    public synchronized TypeDefGallery  getResults()
    {

        if (((accumulatedAttributeTypeDefs == null) || (accumulatedAttributeTypeDefs.isEmpty())) &&
            ((accumulatedTypeDefs == null) || (accumulatedTypeDefs.isEmpty())))
        {
            return null;
        }

        TypeDefGallery results = new TypeDefGallery();

        if (! accumulatedAttributeTypeDefs.isEmpty())
        {
            results.setAttributeTypeDefs(new ArrayList<>(accumulatedAttributeTypeDefs.values()));
        }

        if (! accumulatedTypeDefs.isEmpty())
        {
            results.setTypeDefs(new ArrayList<>(accumulatedTypeDefs.values()));
        }

        return results;
    }
}

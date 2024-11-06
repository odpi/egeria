/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.mappers;


import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.JDBCDataValue;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.ffdc.PostgresErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.schema.RepositoryColumn;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Map between a relationship object and a collection of database rows.
 * This mapping can go in either direction.
 */
public class RelationshipMapper extends RepositoryMapper
{
    private Relationship                     relationship                    = null;
    private Map<String, JDBCDataValue>       relationshipTableRow            = null;
    private List<Map<String, JDBCDataValue>> relationshipPropertiesTableRows = null;
    private EntityMapper                     end1Mapper                      = null;
    private EntityMapper                     end2Mapper                      = null;


    /**
     * Construct a relationship mapper using an OMRS instance.
     *
     * @param relationship relationship
     * @param repositoryHelper repository helper
     * @param repositoryName repository name
     */
    public RelationshipMapper(Relationship         relationship,
                              OMRSRepositoryHelper repositoryHelper,
                              String               repositoryName)
    {
        super (repositoryHelper, repositoryName);

        this.relationship = relationship;
    }


    /**
     * Construct a relationship mapper using the values from the database.
     *
     * @param relationshipTableRow row from the relationship table
     * @param relationshipPropertiesTableRows rows from the relationship properties table
     * @param end1Mapper entity mapper for the end1 proxy
     * @param end2Mapper entity mapper for the end2 proxy
     *
     * @param repositoryHelper repository helper
     * @param repositoryName repository name
     */
    public RelationshipMapper(Map<String, JDBCDataValue>       relationshipTableRow,
                              List<Map<String, JDBCDataValue>> relationshipPropertiesTableRows,
                              EntityMapper                     end1Mapper,
                              EntityMapper                     end2Mapper,
                              OMRSRepositoryHelper             repositoryHelper,
                              String                           repositoryName)
    {
        super (repositoryHelper, repositoryName);

        this.relationshipTableRow            = relationshipTableRow;
        this.relationshipPropertiesTableRows = relationshipPropertiesTableRows;
        this.end1Mapper                      = end1Mapper;
        this.end2Mapper                      = end2Mapper;
    }


    /**
     * Construct a relationship object using the values from the database.
     *
     * @return relationship description
     * @throws RepositoryErrorException problem mapping the properties
     */
    public Relationship getRelationship() throws RepositoryErrorException
    {
        final String methodName = "getRelationship";

        if (relationship == null)
        {
            if (relationshipTableRow != null)
            {
                Relationship newRelationship = new Relationship();

                super.fillInstanceHeader(newRelationship, relationshipTableRow);
                newRelationship.setProperties(super.getInstanceProperties(relationshipTableRow, relationshipPropertiesTableRows));
                newRelationship.setEntityOneProxy(end1Mapper.getEntityProxy());
                newRelationship.setEntityTwoProxy(end2Mapper.getEntityProxy());

                relationship = newRelationship;
            }
            else
            {
                throw new RepositoryErrorException(PostgresErrorCode.MISSING_MAPPING_VALUE.getMessageDefinition("relationshipTableRow",
                                                                                                                methodName,
                                                                                                                this.getClass().getName()),
                                                   this.getClass().getName(),
                                                   methodName);
            }
        }

        return relationship;
    }



    /**
     * Extract the relationship table information from the relationship object.
     *
     * @return  table row for this relationship
     * @throws RepositoryErrorException problem mapping the properties
     */
    public Map<String, JDBCDataValue> getRelationshipTableRow() throws RepositoryErrorException
    {
        if (relationshipTableRow == null)
        {
            Map<String, JDBCDataValue> newRelationshipTableRow = new HashMap<>();

            if (relationship != null)
            {
                if (relationship.getProperties() != null)
                {
                    super.extractValuesFromInstanceHeader(newRelationshipTableRow,
                                                          null,
                                                          relationship.getProperties().getEffectiveFromTime(),
                                                          relationship.getProperties().getEffectiveToTime(),
                                                          null,
                                                          relationship);
                }
                else
                {
                    super.extractValuesFromInstanceHeader(newRelationshipTableRow,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          relationship);
                }

                super.setUpStringValueInRow(newRelationshipTableRow, relationship.getEntityOneProxy().getGUID(), RepositoryColumn.END_1_GUID.getColumnName(), true);
                super.setUpStringValueInRow(newRelationshipTableRow, relationship.getEntityTwoProxy().getGUID(), RepositoryColumn.END_2_GUID.getColumnName(), true);
            }

            if (! newRelationshipTableRow.isEmpty())
            {
                relationshipTableRow = newRelationshipTableRow;
            }
        }

        return relationshipTableRow;
    }


    /**
     * Return the rows that describe the properties for this relationship.
     *
     * @return property rows
     * @throws RepositoryErrorException problem mapping the properties
     */
    public List<Map<String, JDBCDataValue>> getRelationshipPropertiesTableRows() throws RepositoryErrorException
    {
        final String methodName = "getRelationshipPropertiesTableRows";

        if (relationshipPropertiesTableRows == null)
        {
            if (relationship != null)
            {
                relationshipPropertiesTableRows = extractValuesFromInstanceProperties(relationship.getGUID(),
                                                                                      null,
                                                                                      relationship.getVersion(),
                                                                                      relationship.getType().getTypeDefName(),
                                                                                      relationship.getProperties(),
                                                                                      null,
                                                                                      null);
            }
            else
            {
                throw new RepositoryErrorException(PostgresErrorCode.MISSING_MAPPING_VALUE.getMessageDefinition("relationship",
                                                                                                                methodName,
                                                                                                                this.getClass().getName()),
                                                   this.getClass().getName(),
                                                   methodName);
            }
        }

        return relationshipPropertiesTableRows;
    }


    /**
     * Return the mapper for the entity proxy at end 1 of the relationship.
     *
     * @return mapper
     * @throws RepositoryErrorException problem mapping the properties
     */
    public EntityMapper getEnd1Mapper() throws RepositoryErrorException
    {
        final String methodName = "getEnd1Mapper";

        if (end1Mapper == null)
        {
            if (relationship != null)
            {
                end1Mapper = new EntityMapper(relationship.getEntityOneProxy(), repositoryHelper, repositoryName);
            }
            else
            {
                throw new RepositoryErrorException(PostgresErrorCode.MISSING_MAPPING_VALUE.getMessageDefinition("relationship",
                                                                                                                methodName,
                                                                                                                this.getClass().getName()),
                                                   this.getClass().getName(),
                                                   methodName);
            }
        }

        return end1Mapper;
    }


    /**
     * Return the mapper for the entity proxy at end 2 of the relationship.
     *
     * @return mapper
     * @throws RepositoryErrorException problem mapping the properties
     */
    public EntityMapper getEnd2Mapper() throws RepositoryErrorException
    {
        final String methodName = "getEnd2Mapper";

        if (end2Mapper == null)
        {
            if (relationship != null)
            {
                end2Mapper = new EntityMapper(relationship.getEntityTwoProxy(), repositoryHelper, repositoryName);
            }
            else
            {
                throw new RepositoryErrorException(PostgresErrorCode.MISSING_MAPPING_VALUE.getMessageDefinition("relationship",
                                                                                                                methodName,
                                                                                                                this.getClass().getName()),
                                                   this.getClass().getName(),
                                                   methodName);
            }
        }

        return end2Mapper;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "RelationshipMapper{" +
                "relationship=" + relationship +
                ", relationshipTableRow=" + relationshipTableRow +
                ", relationshipPropertiesTableRows=" + relationshipPropertiesTableRows +
                ", end1Mapper=" + end1Mapper +
                ", end2Mapper=" + end2Mapper +
                "} " + super.toString();
    }
}

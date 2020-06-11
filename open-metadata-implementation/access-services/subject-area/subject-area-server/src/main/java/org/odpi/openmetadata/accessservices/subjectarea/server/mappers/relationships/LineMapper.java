/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.SubjectAreaErrorCode;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.ILineMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.MessageDefinition;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Iterator;


/**
 * Static mapping methods to map between the antonym and the equivalent generated OMRSRelationshipBean
 */
public abstract class LineMapper implements ILineMapper
{
    private static final Logger log = LoggerFactory.getLogger( LineMapper.class);
    private static final String className = LineMapper.class.getName();
    protected final OMRSAPIHelper omrsapiHelper;
    protected final OMRSRepositoryHelper repositoryHelper;

    public LineMapper(OMRSAPIHelper omrsapiHelper) {
        this.omrsapiHelper = omrsapiHelper;
        this.repositoryHelper = omrsapiHelper.getOMRSRepositoryHelper();
    }
   public Line  mapRelationshipToLine(Relationship relationship)
    {
        Line line = getLineInstance();
        line.setSystemAttributes(SubjectAreaUtils.createSystemAttributes(relationship));
        EntityProxy  proxy1 =relationship.getEntityOneProxy();
        if (proxy1 !=null) {
            String guid1 =  proxy1.getGUID();
            if (guid1 !=null) {
                setEnd1GuidInLine(line,guid1);
            }
        }
        EntityProxy  proxy2 =relationship.getEntityTwoProxy();
        if (proxy2 !=null) {
            String guid2 =  proxy2.getGUID();
            if (guid2 !=null) {
                setEnd2GuidInLine(line,guid2);
            }
        }


        // Set properties
        InstanceProperties relationshipProperties = relationship.getProperties();
        if (relationshipProperties!=null) {
            mapLineEffectivityToInstanceProperties(line, relationshipProperties);
            Iterator<String> omrsPropertyIterator =relationshipProperties.getPropertyNames();
            while (omrsPropertyIterator.hasNext()) {
                String propertyName = omrsPropertyIterator.next();
                // this is a property we expect
                InstancePropertyValue value =relationshipProperties.getPropertyValue(propertyName);

                // supplied guid matches the expected type

                Object actualValue;
                switch (value.getInstancePropertyCategory()) {
                    case PRIMITIVE:
                        PrimitivePropertyValue primitivePropertyValue = (PrimitivePropertyValue) value;
                        actualValue = primitivePropertyValue.getPrimitiveValue();

                        if (!mapPrimitiveToLine(line, propertyName, actualValue)) {
                            // there are properties we are not aware of, as they have been added by a subtype, put them in the additionalProperties
                            if (null==line.getAdditionalProperties())  {
                                line.setAdditionalProperties(new HashMap<String, String>());
                            }
                            line.getAdditionalProperties().put(propertyName, actualValue.toString());
                        }
                        break;
                    case ENUM:
                        EnumPropertyValue enumPropertyValue = (EnumPropertyValue) value;
                        if (!mapEnumToLine(line, propertyName,enumPropertyValue)) {
                            if (null==line.getAdditionalProperties())  {
                                line.setAdditionalProperties(new HashMap<String, String>());
                            }
                            line.getAdditionalProperties().put(propertyName, enumPropertyValue.valueAsString());
                        }

                        break;
                    case MAP:
                        MapPropertyValue mapPropertyValue = (MapPropertyValue) value;
                        InstanceProperties instancePropertyForMap = mapPropertyValue.getMapValues();
                        if (!mapMapToLine(line, propertyName, instancePropertyForMap)) {
                            if (null==line.getAdditionalProperties())  {
                                line.setAdditionalProperties(new HashMap<String, String>());
                            }
                            line.getAdditionalProperties().put(propertyName,mapPropertyValue.valueAsString());
                        }
                        break;
                    case ARRAY:
                    case STRUCT:
                    case UNKNOWN:
                        // error
                        break;
                }

            }   // end while
        }
        return line;
    }

    /**
     * Pass the guid of the EntityProxy for end 1 to the Line ,so it can store it appropriately
     * @param line line to update
     * @param guid guid to add into the Line
     */
    protected abstract void setEnd1GuidInLine(Line line, String guid);

    /**
     * Pass the guid of the EntityProxy for end 2 to the Line ,so it can store it appropriately
     * @param line line to update
     * @param guid guid to add into the Line
     */
    protected abstract void setEnd2GuidInLine(Line line, String guid);

    /**
     * Map the instance properties to the Line
     * This method should be overridden by subclasses to map the properties to the Line.
     * @param line line to be updated
     * @param instanceProperties properties to use for the update
     */
    protected void mapInstancePropertiesToLine(Line line, InstanceProperties instanceProperties)
    {
    }
    /**
     * map the effectivity dates from the Line to the InstanceProperties
     * @param line line
     * @param instanceProperties instance properties to update
     */
    private void mapLineEffectivityToInstanceProperties(Line line, InstanceProperties instanceProperties)
    {
        instanceProperties.setEffectiveFromTime(line.getEffectiveFromTime());
        instanceProperties.setEffectiveToTime(line.getEffectiveToTime());
    }

    /**
     * Map Line to the omrs relationship equivalent
     * @param line supplied line
     * @return omrs relationship equivalent
     */
    public Relationship mapLineToRelationship(Line line) {
        Relationship relationship = new Relationship();
        InstanceProperties  instanceProperties = new InstanceProperties();
        relationship.setProperties(instanceProperties);
        mapLineToInstanceProperties(line, instanceProperties);
        String proxy1Guid =getProxy1Guid(line);
        String proxy2Guid =getProxy2Guid(line);

        EntityProxy proxy1 = new EntityProxy();
        proxy1.setGUID(proxy1Guid);
        relationship.setEntityOneProxy(proxy1);
        EntityProxy proxy2 = new EntityProxy();
        proxy2.setGUID(proxy2Guid);
        relationship.setEntityTwoProxy(proxy2);
        String typeName = getTypeName();
        TypeDef typedef = omrsapiHelper.getOMRSRepositoryHelper().getTypeDefByName(omrsapiHelper.getServiceName(),typeName);
        InstanceType type = new InstanceType();
        type.setTypeDefName(typedef.getName());
        type.setTypeDefGUID(typedef.getGUID());
        relationship.setType(type);

        if (line.getSystemAttributes()!=null) {
            SubjectAreaUtils.populateSystemAttributesForInstanceAuditHeader(line.getSystemAttributes(),relationship);
            relationship.setGUID(line.getSystemAttributes().getGUID());
        }
        return relationship;
    }

    /**
     * get type name
     * @return the omrs type name.
     */
    public abstract  String getTypeName() ;
    abstract Line getLineInstance();
    /**
     * get the relationshipTypeDef Guid
     * This method should be overridden to provide the appropriate guid for the type.
     * @param relationship to get the guid from
     * @return the guid of the relationship typedef
     */
    protected String  getRelationshipTypeDefGuid(Relationship relationship)
    {
        return null;
    }

    /**
     * Get proxy 1 guid
     * @param line for this Line
     * @return proxy 1 guid
     */
    protected String getProxy1Guid(Line line)
    {
        return null;
    }
    /**
     * Get proxy 2 guid
     * @param line for this Line
     * @return proxy 2 guid
     */
    protected String getProxy2Guid(Line line)
    {
         return null;
    }

    /**
     * Map an omrs primitive property to a Subject Area Line property.
     * The child class is expected to override this method if the type has primitive properties
     * @param line the line to be updated
     * @param propertyName the omrs property name
     * @param value the omrs primitive property value
     * @return true if it was a property we were expecting , otherwise false;
     */
    protected boolean mapPrimitiveToLine(Line line, String propertyName, Object value)
    {
        return false;
    }


    /**
     * Map an omrs enum property to a Subject Area Line property.
     * The child class is expected to override this method if the type has enum properties
     * @param line the line to be updated
     * @param propertyName the omrs property name
     * @param enumPropertyValue the omrs enum property value
     * @return true if it was a property we were expecting , otherwise false;
     */
    protected boolean mapEnumToLine(Line line, String propertyName, EnumPropertyValue enumPropertyValue)
    {
        return false;
    }
    /**
     * Map an omrs  map property to a Subject Area Line property.
     * The child class is expected to override this method if the type has map properties
     * @param line the line to be updated
     * @param propertyName the omrs property name
     * @param instancePropertyForMap the omrs map property value
     * @return true if it was a property we were expecting , otherwise false;
     */
    protected boolean mapMapToLine(Line line, String propertyName, InstanceProperties instancePropertyForMap)
    {
        return false;
    }
    /**
     * Map the supplied Line to omrs InstanceProperties.
     * This method should be overridden to populate the instance properties
     * @param line supplied line
     * @param instanceProperties equivalent instance properties to the Line
     */
    protected void mapLineToInstanceProperties(Line line, InstanceProperties instanceProperties) {

    }

}

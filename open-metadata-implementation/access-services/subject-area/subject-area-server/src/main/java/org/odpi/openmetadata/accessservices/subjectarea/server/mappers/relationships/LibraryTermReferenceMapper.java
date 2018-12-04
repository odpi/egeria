/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.LibraryTermReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Static mapping methods to map between the libraryTermReference and the equivalent generated OMRSRelationshipBean
 */
public class LibraryTermReferenceMapper
{
    private static final Logger log = LoggerFactory.getLogger( LibraryTermReferenceMapper.class);
    private static final String className = LibraryTermReferenceMapper.class.getName();

    /**
     * map LibraryTermReference to the omrs relationship bean equivalent
     * @param libraryTermReference supplied LibraryTermReference
     * @return omrs relationship bean equivalent
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    static public org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.LibraryTermReference.LibraryTermReference mapLibraryTermReferenceToOMRSRelationshipBean(LibraryTermReference libraryTermReference) throws InvalidParameterException {
        // copy over the Line attributes
        org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.LibraryTermReference.LibraryTermReference omrsRelationshipBean = new  org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.LibraryTermReference.LibraryTermReference(libraryTermReference);
        //Set properties
        omrsRelationshipBean.setDescription(libraryTermReference.getDescription());
        omrsRelationshipBean.setIdentifier(libraryTermReference.getIdentifier());
        omrsRelationshipBean.setLastVerified(libraryTermReference.getLastVerified());
        omrsRelationshipBean.setSteward(libraryTermReference.getSteward());
        omrsRelationshipBean.setGuid(libraryTermReference.getGuid());

        Map<String, Object> extraAttributes = omrsRelationshipBean.getExtraAttributes();
        if (extraAttributes !=null)
        {
            String[] properties = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.LibraryTermReference.LibraryTermReference.PROPERTY_NAMES_SET_VALUES;
            for (String property : properties)
            {
                if (extraAttributes.containsKey(property))
                {
                    extraAttributes.remove(property);
                }
            }
            omrsRelationshipBean.setExtraAttributes(extraAttributes);
        }

        return omrsRelationshipBean;
    }

    /**
     * Map omrs relationship bean equivalent to LibraryTermReference
     * @param omrsRelationshipBean omrs relationship bean equivalent
     * @return LibraryTermReference libraryTermReference
     */
    public static LibraryTermReference mapOMRSRelationshipBeanToLibraryTermReference(org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.LibraryTermReference.LibraryTermReference omrsRelationshipBean) {
        // copy over the Line attributes
        LibraryTermReference libraryTermReference = new LibraryTermReference(omrsRelationshipBean);
        libraryTermReference.setDescription(omrsRelationshipBean.getDescription());
        libraryTermReference.setSteward(omrsRelationshipBean.getSteward());
        libraryTermReference.setIdentifier(omrsRelationshipBean.getIdentifier());
        libraryTermReference.setLastVerified(omrsRelationshipBean.getLastVerified());
        libraryTermReference.setGuid(omrsRelationshipBean.getGuid());
        String[] properties=org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.LibraryTermReference.LibraryTermReference.PROPERTY_NAMES_SET_VALUES;
        Map<String, Object> extraAttributes =libraryTermReference.getExtraAttributes();
        if (properties!=null && properties.length >0) {
            if (extraAttributes ==null) {
                extraAttributes =new HashMap<>();
            }
            for (String property : properties) {
                if (extraAttributes.containsKey(property)) {
                    extraAttributes.remove(property);
                }
            }
            libraryTermReference.setExtraAttributes(extraAttributes);
        }
        return libraryTermReference;
    }
}

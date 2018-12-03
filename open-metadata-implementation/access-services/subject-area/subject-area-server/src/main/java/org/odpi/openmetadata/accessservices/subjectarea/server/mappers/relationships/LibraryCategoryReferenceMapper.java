/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.LibraryCategoryReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Static mapping methods to map between the libraryCategoryReference and the equivalent generated OMRSRelationshipBean
 */
public class LibraryCategoryReferenceMapper
{
    private static final Logger log = LoggerFactory.getLogger( LibraryCategoryReferenceMapper.class);
    private static final String className = LibraryCategoryReferenceMapper.class.getName();

    /**
     * map LibraryCategoryReference to the omrs relationship bean equivalent
     * @param libraryCategoryReference supplied LibraryCategoryReference
     * @return omrs relationship bean equivalent
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    static public org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.LibraryCategoryReference.LibraryCategoryReference mapLibraryCategoryReferenceToOMRSRelationshipBean(LibraryCategoryReference libraryCategoryReference) throws InvalidParameterException {
        // copy over the Line attributes
        org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.LibraryCategoryReference.LibraryCategoryReference omrsRelationshipBean = new  org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.LibraryCategoryReference.LibraryCategoryReference(libraryCategoryReference);
        //Set properties
        omrsRelationshipBean.setDescription(libraryCategoryReference.getDescription());
        omrsRelationshipBean.setIdentifier(libraryCategoryReference.getIdentifier());
        omrsRelationshipBean.setLastVerified(libraryCategoryReference.getLastVerified());
        omrsRelationshipBean.setSteward(libraryCategoryReference.getSteward());
        omrsRelationshipBean.setGuid(libraryCategoryReference.getGuid());

        Map<String, Object> extraAttributes = omrsRelationshipBean.getExtraAttributes();
        if (extraAttributes !=null)
        {
            String[] properties = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.LibraryCategoryReference.LibraryCategoryReference.PROPERTY_NAMES_SET_VALUES;
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
     * Map omrs relationship bean equivalent to LibraryCategoryReference
     * @param omrsRelationshipBean omrs relationship bean equivalent
     * @return LibraryCategoryReference libraryCategoryReference
     */
    public static LibraryCategoryReference mapOMRSRelationshipBeanToLibraryCategoryReference(org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.LibraryCategoryReference.LibraryCategoryReference omrsRelationshipBean) {
        // copy over the Line attributes
        LibraryCategoryReference libraryCategoryReference = new LibraryCategoryReference(omrsRelationshipBean);
        libraryCategoryReference.setDescription(omrsRelationshipBean.getDescription());
        libraryCategoryReference.setSteward(omrsRelationshipBean.getSteward());
        libraryCategoryReference.setIdentifier(omrsRelationshipBean.getIdentifier());
        libraryCategoryReference.setLastVerified(omrsRelationshipBean.getLastVerified());
        libraryCategoryReference.setGuid(omrsRelationshipBean.getGuid());
        String[] properties=org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.LibraryCategoryReference.LibraryCategoryReference.PROPERTY_NAMES_SET_VALUES;
        Map<String, Object> extraAttributes =libraryCategoryReference.getExtraAttributes();
        if (properties!=null && properties.length >0) {
            if (extraAttributes ==null) {
                extraAttributes =new HashMap<>();
            }
            for (String property : properties) {
                if (extraAttributes.containsKey(property)) {
                    extraAttributes.remove(property);
                }
            }
            libraryCategoryReference.setExtraAttributes(extraAttributes);
        }
        return libraryCategoryReference;
    }
}

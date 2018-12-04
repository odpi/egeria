/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.Status;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common.SystemAttributes;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Synonym;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;



/**
 * Static mapping methods to map between the synonym and the equivalent omrs relationship bean
 */
public class TestSynonymMapper
{
    @Test
     void testRoundTrip() throws InvalidParameterException
     {
         Synonym synonym = new Synonym();
         org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Synonym.Synonym omrsRelationshipBean =
            SynonymMapper.mapSynonymToOMRSRelationshipBean(synonym);
         synonym.setSteward("st");
         synonym.setSource("so");
         synonym.setExpression("Ex");
         synonym.setDescription("des  ");
         synonym.setStatus(TermRelationshipStatus.Draft);

         Map<String, Object> extraAttributes = new HashMap<>();
         extraAttributes.put("aaa","bbb");
         synonym.setExtraAttributes(extraAttributes);

         omrsRelationshipBean = SynonymMapper.mapSynonymToOMRSRelationshipBean(synonym);
         assertEquals(synonym.getSteward(),omrsRelationshipBean.getSteward());
         assertEquals(synonym.getSource(),omrsRelationshipBean.getSource());
         assertEquals(synonym.getDescription(),omrsRelationshipBean.getDescription());
         assertEquals(synonym.getExpression(),omrsRelationshipBean.getExpression());
         assertEquals(synonym.getStatus(),omrsRelationshipBean.getStatus());
         assertEquals(synonym.getExtraAttributes(),omrsRelationshipBean.getExtraAttributes());
         // check extra attributes are removed if they match an attribute we already have
         Map<String, Object> extraAttributes2 = new HashMap<>();
         extraAttributes2.put("aaa","bbb");
         extraAttributes2.put("description","des   ");
         synonym.setExtraAttributes(extraAttributes2);
         omrsRelationshipBean = SynonymMapper.mapSynonymToOMRSRelationshipBean(synonym);
         assertEquals(extraAttributes,omrsRelationshipBean.getExtraAttributes());

         Synonym synonym2 = SynonymMapper.mapOMRSRelationshipBeanToSynonym(omrsRelationshipBean);
         assertEquals(synonym2.getSteward(),omrsRelationshipBean.getSteward());
         assertEquals(synonym2.getSource(),omrsRelationshipBean.getSource());
         assertEquals(synonym2.getDescription(),omrsRelationshipBean.getDescription());
         assertEquals(synonym2.getExpression(),omrsRelationshipBean.getExpression());
         assertEquals(synonym2.getStatus(),omrsRelationshipBean.getStatus());
         assertEquals(synonym2.getExtraAttributes(),omrsRelationshipBean.getExtraAttributes());
         System.err.println(
                 synonym2.toString()
         );
     }
    @Test
    void testRoundTripWithsystemAttributes() throws InvalidParameterException
    {
        Synonym synonym = new Synonym();
        org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Synonym.Synonym omrsRelationshipBean = SynonymMapper.mapSynonymToOMRSRelationshipBean(synonym);
        synonym.setSteward("st");
        synonym.setSource("so");
        synonym.setExpression("Ex");
        synonym.setDescription("des  ");
        synonym.setStatus(TermRelationshipStatus.Draft);
        SystemAttributes systemAttributes = new SystemAttributes();
        systemAttributes.setGUID("111");
        systemAttributes.setStatus(Status.ACTIVE);
        systemAttributes.setCreatedBy("aaa");
        systemAttributes.setCreateTime(new Date());
        systemAttributes.setUpdatedBy("bbb");
        systemAttributes.setVersion(23L);
        systemAttributes.setUpdateTime(new Date() );

        synonym.setSystemAttributes(systemAttributes);
        Map<String, Object> extraAttributes = new HashMap<>();
        extraAttributes.put("aaa","bbb");
        synonym.setExtraAttributes(extraAttributes);

        omrsRelationshipBean = SynonymMapper.mapSynonymToOMRSRelationshipBean(synonym);
        assertEquals(synonym.getSteward(),omrsRelationshipBean.getSteward());
        assertEquals(synonym.getSource(),omrsRelationshipBean.getSource());
        assertEquals(synonym.getDescription(),omrsRelationshipBean.getDescription());
        assertEquals(synonym.getExpression(),omrsRelationshipBean.getExpression());
        assertEquals(synonym.getStatus(),omrsRelationshipBean.getStatus());
        assertEquals(synonym.getExtraAttributes(),omrsRelationshipBean.getExtraAttributes());

        assertEquals(synonym.getSystemAttributes().getCreatedBy(),omrsRelationshipBean.getSystemAttributes().getCreatedBy());
        assertEquals(synonym.getSystemAttributes().getGUID(),omrsRelationshipBean.getSystemAttributes().getGUID());
        assertEquals(synonym.getSystemAttributes().getUpdatedBy(),omrsRelationshipBean.getSystemAttributes().getUpdatedBy());
        assertEquals(synonym.getSystemAttributes().getCreateTime(),omrsRelationshipBean.getSystemAttributes().getCreateTime());
        assertEquals(synonym.getSystemAttributes().getStatus(),omrsRelationshipBean.getSystemAttributes().getStatus());
        assertEquals(synonym.getSystemAttributes().getVersion(),omrsRelationshipBean.getSystemAttributes().getVersion());



        // check extra attributes are removed if they match an attribute we already have
        Map<String, Object> extraAttributes2 = new HashMap<>();
        extraAttributes2.put("aaa","bbb");
        extraAttributes2.put("description","des   ");
        synonym.setExtraAttributes(extraAttributes2);
        omrsRelationshipBean = SynonymMapper.mapSynonymToOMRSRelationshipBean(synonym);
        assertEquals(extraAttributes,omrsRelationshipBean.getExtraAttributes());

        Synonym synonym2 = SynonymMapper.mapOMRSRelationshipBeanToSynonym(omrsRelationshipBean);
        assertEquals(synonym2.getSteward(),omrsRelationshipBean.getSteward());
        assertEquals(synonym2.getSource(),omrsRelationshipBean.getSource());
        assertEquals(synonym2.getDescription(),omrsRelationshipBean.getDescription());
        assertEquals(synonym2.getExpression(),omrsRelationshipBean.getExpression());
        assertEquals(synonym2.getStatus(),omrsRelationshipBean.getStatus());
        assertEquals(synonym2.getExtraAttributes(),omrsRelationshipBean.getExtraAttributes());
        assertEquals(synonym2.getSystemAttributes().getCreatedBy(),omrsRelationshipBean.getSystemAttributes().getCreatedBy());
        assertEquals(synonym2.getSystemAttributes().getGUID(),omrsRelationshipBean.getSystemAttributes().getGUID());
        assertEquals(synonym2.getSystemAttributes().getUpdatedBy(),omrsRelationshipBean.getSystemAttributes().getUpdatedBy());
        assertEquals(synonym2.getSystemAttributes().getCreateTime(),omrsRelationshipBean.getSystemAttributes().getCreateTime());
        assertEquals(synonym2.getSystemAttributes().getStatus(),omrsRelationshipBean.getSystemAttributes().getStatus());
        assertEquals(synonym2.getSystemAttributes().getVersion(),omrsRelationshipBean.getSystemAttributes().getVersion());

        System.err.println(
                synonym2.toString()
        );
    }

}

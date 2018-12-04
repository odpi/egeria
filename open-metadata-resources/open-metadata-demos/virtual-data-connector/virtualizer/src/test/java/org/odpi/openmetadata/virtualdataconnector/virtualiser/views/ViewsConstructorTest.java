/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.virtualdataconnector.virtualiser.views;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.informationview.events.TableContextEvent;
import org.odpi.openmetadata.accessservices.informationview.events.InformationViewEvent;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.JsonReadHelper;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.gaian.GaianQueryConstructor;
import org.odpi.openmetadata.virtualdataconnector.virtualiser.kafka.KafkaVirtualiserProducer;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import static org.testng.Assert.assertNotNull;


@SpringBootTest
public class ViewsConstructorTest extends AbstractTestNGSpringContextTests {
    private static final String TESTIN = "json/testIn.json";
    private static final String DELETE = "json/delete.json";
    private static final String BUSINESS = "json/business.json";
    private static final String TECHNICAL = "json/technical.json";
    //events object
    private static ObjectMapper mapper = new ObjectMapper();
    //json file received from Information View OMAS
    private String ivJson = null;
    //object matched with json file
    private TableContextEvent tableContextEvent = null;
    @Mock
    private KafkaVirtualiserProducer kafkaVirtualiserProducer;
    @Autowired
    @InjectMocks
    private ViewsConstructor viewsConstructor;
    private ClassLoader classLoader;


    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        classLoader = this.getClass().getClassLoader();
        ivJson =  FileUtils.readFileToString(new File("./src/test/resources/" + TESTIN), "UTF-8");
        // try to delete the views
        //ivJson= JsonReadHelper.readFile(new File(classLoader.getResource(DELETE).getFile()));
        tableContextEvent = mapper.readValue(ivJson, TableContextEvent.class);

    }

    @AfterMethod
    public void tearDown() throws Exception {
    }

    @Test
    public void testNotifyIVOMAS() throws Exception {

        HashMap<String, String> createdViews = new HashMap<>();
        createdViews.put(GaianQueryConstructor.BUSINESS_PREFIX, "LTB_EMPSALARYANALYSIS");
        createdViews.put(GaianQueryConstructor.TECHNICAL_PREFIX, "LTT_EMPSALARYANALYSIS");
        List<InformationViewEvent> views = viewsConstructor.notifyIVOMAS(tableContextEvent, createdViews);
        assertNotNull(views);
        String business = FileUtils.readFileToString(new File("./src/test/resources/" + BUSINESS), "UTF-8");
        String technical = FileUtils.readFileToString(new File("./src/test/resources/" + TECHNICAL), "UTF-8");
        JSONAssert.assertEquals(business, mapper.writeValueAsString(views.get(0)),
                                new CustomComparator(JSONCompareMode.LENIENT, new Customization("tableSource.@id", (o1,o2) -> true)));
        JSONAssert.assertEquals(technical, mapper.writeValueAsString(views.get(1)),
                                new CustomComparator(JSONCompareMode.LENIENT, new Customization("tableSource.@id", (o1,o2) -> true)));
    }

}










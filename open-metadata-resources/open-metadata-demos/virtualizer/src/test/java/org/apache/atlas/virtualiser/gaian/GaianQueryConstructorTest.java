/* SPDX-License-Identifier: Apache-2.0 */
package org.apache.atlas.virtualiser.gaian;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.atlas.omas.informationview.events.ColumnContextEvent;
import org.apache.atlas.virtualiser.JsonReadHelper;
import org.apache.atlas.virtualiser.util.PropertiesHelper;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.assertEquals;
/**
 * GaianQueryConstructorTest is not able to run without local Gaian running.
 * When you run testNotifyGaian() you have to run your local Gaian in advance.
 * And you should see the test is succeed. And there are two Logical Tables created for the local Gaian.
 */
public class GaianQueryConstructorTest {

    //json file received from Information View OMAS
    private String IVJson=null;
    //events object
    private ObjectMapper mapper=null;
    //object matched with json file
    private ColumnContextEvent columnContextEvent=null;
    private GaianQueryConstructor gaianQueryConstructor=null;
    private static final String TESTIN="json/testIn.json";
    private static final String DELETE="json/delete.json";
    private static final String FULLASSIGN="json/fullyAssign.json";
    private ClassLoader classLoader;


    @BeforeMethod
    public void setUp() throws Exception {
        classLoader=this.getClass().getClassLoader();
        IVJson= JsonReadHelper.readFile(new File(classLoader.getResource(TESTIN).getFile()));
        //delete all Logical Tables
        //IVJson=JsonReadHelper.readFile(new File(classLoader.getResource(DELETE).getFile()));
        //all columns have assigned terms
        //IVJson=JsonReadHelper.readFile(new File(classLoader.getResource(FULLASSIGN).getFile()));
        mapper=new ObjectMapper();
        columnContextEvent=mapper.readValue(IVJson,ColumnContextEvent.class);
        gaianQueryConstructor=new GaianQueryConstructor();
        PropertiesHelper.loadProperties();
    }

    @Test
    public void testNotifyGaian() throws Exception {

        //todo: mokup. unit tests should not depend on external systems
        //        boolean result=gaianQueryConstructor.notifyGaian(columnContextEvent);
        //        assertEquals(result,true);
    }

}
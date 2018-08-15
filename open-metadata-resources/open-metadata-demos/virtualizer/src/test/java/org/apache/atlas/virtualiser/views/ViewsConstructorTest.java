/* SPDX-License-Identifier: Apache-2.0 */
package org.apache.atlas.virtualiser.views;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.atlas.omas.informationview.events.ColumnContextEvent;
import org.apache.atlas.virtualiser.JsonReadHelper;
import org.apache.atlas.virtualiser.util.PropertiesHelper;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class ViewsConstructorTest {
    //json file received from Information View OMAS
    private String IVJson=null;
    //events object
    private ObjectMapper mapper=null;
    //object matched with json file
    private ColumnContextEvent columnContextEvent=null;
    private ViewsConstructor viewsConstructor=null;
    private static final String TESTIN="json/testIn.json";
    private static final String DELETE="json/delete.json";
    private static final String BUSINESS="json/business.json";
    private static final String TECHNICAL="json/technical.json";
    private ClassLoader classLoader;

    @BeforeMethod
    public void setUp() throws Exception {
        classLoader=this.getClass().getClassLoader();
        IVJson= JsonReadHelper.readFile(new File(classLoader.getResource(TESTIN).getFile()));
        // try to delete the views
        //IVJson= JsonReadHelper.readFile(new File(classLoader.getResource(DELETE).getFile()));
        mapper = new ObjectMapper();
        columnContextEvent=mapper.readValue(IVJson,ColumnContextEvent.class);
        viewsConstructor=new ViewsConstructor();
        PropertiesHelper.loadProperties();
    }

    @AfterMethod
    public void tearDown() throws Exception {
    }

    @Test
    public void testNotifyIVOMAS() throws Exception {
        String[] views=viewsConstructor.notifyIVOMAS(columnContextEvent);
        assertNotNull(views);
        //run delete json
        // assertEquals(views,null);
        String business= JsonReadHelper.readFile(new File(classLoader.getResource(BUSINESS).getFile()));
        String technical= JsonReadHelper.readFile(new File(classLoader.getResource(TECHNICAL).getFile()));
        assertEquals(views[0],business);
        assertEquals(views[1],technical);
    }

}










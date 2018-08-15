<!--SPDX-License-Identifier: Apache-2.0 -->
# Virtualiser
Virtualiser mainly communicate with Gaian and Information View OMAS.

Virtualiser has three main functions:
1. listen to Information View OMAS (information-view-out-topic) and retrieve the payload (json structure);
2. update business Logical Table and technical Logical Table, send the updates to Gaian;
3. create business view json file and technical view json file, notify Information View OMAS the update(information-view-in-topic).

## Introduction of Gaian
The Gaian is a lightweight dynamically distributed federated database (DDFD) engine based on Apache Derby 10.x. It provides a single centralized view over multiple, heterogeneous back-end data sources using an extensible Logical Table abstraction layer. Detailed information please go to [GitHub/gaian](https://github.com/gaiandb).

For Virtualiser we assume there are multiple Gaian nodes. And we have a front-end Gaian and multiple back-end Gaian nodes which connect all phsical databases. And our Logical Tables will be created in the front-end Gaian Node and DBA have already set up Logical Table in the back-end Gaian node. Detailed documentation can be found in [ATLAS-1831](https://issues.apache.org/jira/browse/ATLAS-1831).
## Test
1. Test KafkaVirtualiser

   -Go to test/org.apache.atlas.virtualiser.
   
   -Right click KafkaVirtualiserTest, run the class and you should see no failures. And it should send out two information_view_in_topic and create two Logical Tables in Gaian.
   
   

2. Test ViewsConstructor separately:

    -Go to test/org.apache.atlas.virtualiser/views.
    
    -Right click ViewsConstructorTest, run the class and you should see no failures. Here it will read a standard json file and compare it with the business.json and technical.json.
        
        
3.  Test GaianQueryConstructor separately:
  
     Here we run our local Gaian as the front-end Gaian. And install Gaian in another server and set proper database in this Gaian. And make sure there is a table in the database and a Logical Table is created in the server's Gaian.
       
     In our example, our server address is "lx13v9.ad.ing.net" and the table name is "CST" which belongs to database "hr". Based on your own settings you can change testIn.json and follow below steps.
       
     -before you run the test case, you need to connect the local gaian with the gaian node in lx13v9.ad.ing.net.
   
    ```
    call gconnect('lx13v9adingnet','lx13v9.ad.ing.net')
    call listnodes() //here youshould see lx13v9adingnet as one of the nodes
    ``` 

    -Go to test/org.apache.atlas.virtualiser/gaian. 
    Right click GaianQueryConstructorTest, run the class and you should see no failures. And then you can start Gaian dashboard, and run below code in Gaian:
    ```
    call listlts()
    ```
    you should see two Logical Tables are created in your local Gaian: 
   
    LTB_LX13V9ADINGNET_HR_CST 
    
    LTT_LX13V9ADINGNET_HR_CST.
   
    **Delete existed Logical Tables**
       
     If you enable below code and run the test again, you shloud see above two Logical Tables are deleted when you run "call listlts()". Because in delete.json all the columns do not have assigned business terms.
       ```
       IVJson=JsonReadHelper.readFile(new File(classLoader.getResource(DELETE).getFile()));
       ```
       
     **Logical Tables contain all the columns**
           
     If you enable below code and run the test again, you shloud see deleted two Logical Tables show up again when you run "call listlts()". Because in fullyAssign.json all the columns have assigned business terms.
           
     ```
     IVJson=JsonReadHelper.readFile(new File(classLoader.getResource(FULLASSIGN).getFile()));
     ``` 
   


## Use
We can build the project by running "mvn clean package". Then under target folder, there is a jar file called virtualiser-1.0.0-SNAPSHOT-jar-with-dependencies.jar.

Then we could use the jar and run it's main function which contains below code.
```
 loadProperties();
 KafkaVirtualiser kafkaVirtualiser = new KafkaVirtualiser();
 kafkaVirtualiser.listenToIVOMAS();
```        

## Note

1.  **Resources folder**
    
    Here contains all the json files we need for testing and properties file. And testIn.json is the mocked up json file. It shows the json structure what Information View OMAS would send out. And you can change the valude for your own testing, but the structure and names can not be changed.
   
    -connectionDetails:gaianNodeName : 
    
    This is the Gaian node name where the real datasource is running
    
    -front end gaian node: 
    
    It is set up in virtualiser.properties("gaian_front_end_name"). You need to change it to your own front-end Gaian node name when you run Virtualiser.

    Other settings can be found in virtualiser.properties.
2. **Future possible change**

   Virtualiser can have multiple KafkaVirtualConsumer and run as a Thread. So it is not necessary to run its main function.

   Also for later version if the whole project stops running, then we should close consumer and stop listening to Information View out topic using below code:
   ```
   kafkaVirtualiser.stopListening();
   ```    




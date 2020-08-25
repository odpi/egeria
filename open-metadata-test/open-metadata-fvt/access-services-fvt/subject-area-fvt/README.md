<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
  
# Subject Area Access Services FVT Resources
  
The resources to run Subject Area Access Services (OMAS) FVT.

There are a number of ways of running the Subject Area OMAS FVTs

In a development environment:
* Each file ending in FVT has a main that can be run. This runs the FVT against 2 has hard coded server names  
* RunAllFVTOn2Servers. This runs all the FVT against 2 has hard coded server names. It prompts for the url and userid. 
* RunAllFVT runs all the FVTs against the supplied serverName, url and userId. 

Each FVT file has a static runIt method that runs the the FVT with the supplied server name , url and userid.  

As part of a maven install the FVTs are automatically run. The maven install calls each of the runIt methods in turn abd runs them against an OMAG Server started in a docker container.
    

 



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->


The **Repository Explorer (Rex)** can help you explore and visualize the metadata in a repository.
It retrieves entities and relationships from the repository and displays them.
A details panel also shows the properties and other information about an object.
Each entity or relationship is added to a network diagram, which shows how they are connected.
 
Starting with an initial object - an entity or relationship - Rex enables you to explore the
neighborhood of objects around that initial object. It does so by traversing the relationships
connected to each entity, and allows you to select what types of relationship and types of
neighboring entity you want it to retrieve. You can also filter the traversal by specifying
classifications that the neighboring entities must possess.
                         
You can repeat this traversal process to assemble a graph of the objects that you are
interested in.

You don't need to know the available relationship types, entity types or classifications in
advance, because Rex will display what is possible and let you choose.
   
In general, one object in the graph is the 'focus'; this is the object from where the next
traversal starts; the focus object is the one that is displayed in the details panel.
You can set which object is the focus by clicking on it in the diagram. If you retrieve an
object from the repository by searching or typing in its GUID, that object automatically
becomes the new focus.  

Rex can retrieve and display objects of the types supported by whichever repository you
connect it to. When you connect it to a repository, Rex will automatically read the type information
from the repository. It uses this information to populate things like the search filters (explained
below).
 
Rex is intended as a tool to help targeted exploration of relatively small sets of entities and relationships.
It is not a general graph visualization utility, so it would not be wise to use to construct a large graph.
It is advisable to limit the number of traversals you perform, and avoid performing a traversal that would include
a large number of objects. There are no hard limits on graph size, but it is recommended that exploration is
kept specific and aimed at revealing the structure of relatively small sub-graphs. 
 
To help with this, Rex provides assistance to help limit what you get back from each traversal. For
example, it provides Traversal Filters that allow you to filter the types of entity and relationship to include. The
Traversal Filters work by types and classifications. They do not support fine-grained (property-based) filtering of
objects within a traversal. This is not a limitation of Rex, rather a reflection of the functions available in the
metadata collection interface.

If the type-based filtering provided by Rex's Traversal Filters proves to be too coarse-grained, and you find yourself struggling
to select a particular entity within a possible set of neighbors, consider adopting a different approach. For example,
it may be better to with a search for the interesting entity, followed by traversing from that entity. It may be possible
in future to add more sophisticated traversal filters but there would most likely still be repositories that would not support
the full metadata collection interface.          
                        
                      
**A Typical Workflow:**
                    
Typically you would perform the following steps:
                        
* Set the repository server details
* Retrieve one or more initial metadata objects, either using Search or by specifying an object's GUID if you know it
* Explore starting from the initial object, applying filters at each step
* Explore from additional objects or using different filters
* When you have something you want to keep, capture the diagram and history views
* At any point you can Undo the most recent operation (repeatedly) or can clear the whole graph
                        
                     
**Set the Repository Server:**
                        
To get started Rex needs the name and URL of the repository server that you wish to explore.
Enter these details in the "Server Name" and "Server URL Root" fields and press the 'Connect' button.
Immediately below the server input fields, you will see a status message. On first start the message
asks that you enter the server details and press Connect. Rex will use the server
details to request the type information from the repository, which Rex uses to populate its search filters.
If the request to the server completes successfully you should see the message change to "Server OK".
If Rex cannot connect to the server it will display an error message and ask you to check the details and retry.
              
Rex does not maintain a long-running connection to the server; each time it needs to get something from the
server it will use the server details you have set.
                        
**Get some initial metadata:**
                       
Rex needs an object as the starting point of a traversal. You can retrieve a starting object either by entering a
GUID or by performing a Search, which is explained below.  If an object is found it will appear in the diagram.
If no object could be found you will see an alert pop up.

You can start with either an entity or a relationship - use whichever is most convenient.
Entities are drawn as circles. Relationships are drawn as lines connecting pairs of circles (entities).
Classifications are not shown in the diagram, but any classifications associated with an entity are shown in
the details panel when the entity is selected.

**Setting the focus object:**

If one object was retrieved it will automatically be set to the focus, so it will be highlighted and its details will
be displayed in the details panel.
If multiple objects were retrieved they will be shown in the diagram and none will be highlighted. You can select a
focus object in the diagram by clicking on it. The focus object will be highlighted in the diagram
and its details will be shown in the details panel.
  
The focus object is highlighted in the primary color for the UI - this is the same color used for things like buttons and
banners. The default  primary color is the Egeria 'aqua' color (a pale blue), but this may be changed if a different color
scheme has been applied. Other objects (not the focus) are given a color that reflects the home repository of the object; objects from the
same home repository will be the same color. These 'repository colors' are generally shades of gray.
            
If you want to deselect the selected object, click on it again. This can be useful if you want to view the graph with no
object focused, so that all objects are shown in their 'repository colors'.                
                       
                     
**Traverse to find related metadata:**
      
Once there's an initial object in the diagram, you can use the Explore button to retrieve further objects that are
related to the focus object. Rex needs to traverse from an entity, so if you retrieved or selected a relationship,
click on one of its entities before attempting to traverse.
When you traverse, Rex will explore the neighborhood around the selected entity, traversing outward
along the available relationships to find adjacent entities. This process can be repeated to enlarge the graph of
entities and relationships that are displayed.
Rex currently supports a traversal depth of 1 - meaning it will only traverse as far
as the immediate neighbors of the focus entity. It could support deeper traversals, but this would make it harder
to understand the traversal filters - so depth is limited to 1.
  
When you press the Explore button, Rex display a set of (optional) traversal filters, that you can set before it
actually performs the traversal. This is to let you decide which relationships and entities will be included in
the traversal. The traversal filters display the number of relationships and entities of each type, that could be
included in the displayed graph. Check the counts and select the types you want. Rex will then only retrieve the
objects you are really interested in. There is more information below under "Traversal Filtering".      

                       
**Search:**
                    
The search utility in Rex is fairly basic. It allows you to enter a text string and optionally apply an
entity, relationship or classification filter, which limits the search to one particular type. The classification
filter is not yet enabled, but that capability should be added soon.
                   
The search utility does not include property-based searching, it is just a text-based search. It therefore relies
on objects having string-type attributes that can be searched. If an object is of a type that has no string-type
attributes then it will not be found by the search. It is possible that property-based searching could be added,
but it is not available yet.
            
When you enter a string as search text, it can include regular expression characters. Beware that the level of support
for regular expressions varies between repositories. If you are searching an Egeria graph repository or in-memory
repository, then you have more freedom than you might have with some other repositories.                   

If the text you are searching for contains special characters, you will need to literalize the string. As an example,
suppose there are two Assets called asset-qn001 and asset-(qn001). In this example, the parentheses are special
characters, because they affect the processing of the regular expression.
                  
* If you type asset-qn001 as the search text, the first asset (only) will match.
* If you type asset-(qn001) as the search text, the first asset (only) will match.
* If you type \Qasset-(qn001)\E in as the search text, the second asset (only) will match.
                       
This example was tested using the Egeria graph repository. Some repositories support broad regular expressions
but others do not. All repositories should support exact, prefix and suffix matches - so you should be able to
use expressions like \Qany.characters.you.like!\E.* - which would literalize the part between \Q and \E as the
beginning of the search expression and permit any other characters to follow it.

**Traversal Filtering:**
           
When you press Explore to expand the neighborhood around an entity, a dialog box will show how many entities and
relationships might be included. The number of occurrences of each type of entity or relationship are shown alongside the type
name. You can decide which types to include in the traversal. This dialog box also shows numbers of classifications
associated with the neighboring entities.
  
The filters are separated into three columns - for entity types, relationship types and classifications, respectively.

Each column operates as follows:
             
If none of the types in a column are checked, no filtering is applied to that column. This means that all entity types
will be included in the traversal, or all relationship types.
  
If any of the types in a column are checked then filtering is performed. Only the checked types are allowed. If you were
to check ALL the types in the entity or relationship columns you achieve the same as when none of the checkboxes in
those columns are checked (i.e. when there is no filtering). That is, the traversal will include all entities or all
relationships. Note that if you wish to include a relationship to a neighboring entity, you need to enable both the
relationship type and the entity type. This allows you to be very specific about which relationships to traverse. If
there are relationships of the same type to entities of different types, or vice versa, you can independently
select relationship and entity types to achieve finer grain traversal.
            
If no classification types are checked there is no classification filtering. This means all entities will be eligible,
regardless of their classifications (if any). Be careful with the classification column - if you check ALL the
classification types it does NOT mean that all entities are eligible - it means only those entities that have at
least one of the checked classifications will be eligible. If there is a neighboring entity that has none of the
classifications it will not be reached.
                
**Adding to the graph:**
                     
If after a number of traversals you realize that you would like to perform an additional traversal from an entity you
visited at an earlier stage, just go back and select the entity from which you want to perform the additional traversal. Then press
the Explore button. You can set the filters to include the types that you would like to add to the graph.
                      
**Undoing a change:**
                       
If you realize that you have added things to the graph that you actually do not want, you can use the Undo button to undo
the most recent change. The Undo button can be used repeatedly to unwind back to a state that contains objects you want to
keep. You can then use Explore from this state to add more objects.
                       
**Clearing the graph:**
         
The Clear button will clear the graph diagram, the GUID input field and the details panel. It effectively resets Rex to the
state it was in  when the page was first loaded, apart from the search text.
                 
**Traversal History:**

At any point during an exploration session you can use the History button to list the operations that resulted
in the graph as it is currently displayed. The traversal history shows the sequence of operations and describes
the type and parameters of each operation and the entities and relationships that were added to the graph by each
operation. The types of operation include retrieval using a GUID, search and traversal. In each case the GUID,
search string or traversal parameters are recorded.
                  
**Diagram layouts:**

There is currently one type of diagram - called the 'Network Diagram'. Other types of diagram may be added later.
The Network Diagram is a visualization of the traversed graph, in which entities and relationships are drawn as circles and arcs.
Each entity or relationship is labelled according to Rex's built-in labelling scheme (see below). The GUID (globally unique id) 
associated with an instance may be the only way to uniquely identify it, but it is not particularly convenient or memorable. 
Rex includes the GUID in the details panel and in the traversal history to help to uniquely identify an instance.
                  
There are two layout settings within the Network Diagram, that can be selected using the radio button in the top-left corner of
the diagram:

* Time-based (default) layout. This layout places objects starting at the top of the diagram and working vertically downwards with each
  stage of traversal; the more recently visited objects will be located toward the bottom of the diagram. This layout may be helpful while
  you are building the diagram. The temporal placement is not rigid - if a relationship connects a pair of entities retrieved in different
  stages of the traversal, it will tend to pull them together, possibly distorting the temporal sequence.
  
* Proximity-based layout. This layout places objects using a straightforward force-directed graph layout. There is no temporal ordering.
  This layout may be preferred when you have finished adding to the diagram and would like to lay it out for display or printing.
            
You can switch back and forth between the layout options.

** Labelling of objects:
                      
Rex has a built-in labeler that assigns a label to each object (entity or relationship) that is retrieved from the
repository. The labels are used in the diagram and attempts to find a concise, meaningful and hopefully unique label, based on the 
properties of the entity or relationship. 

The labelling strategy behaves differently depending on the type of an object. For some types of object there are
not many possible labels, but in the default case Rex will examine the instance properties of the object and choose a label 
based on the following precedence order:
* displayName - which should have been set with the expectation that it will be used for display purposes
* name - which should be easier to read than qualified name
* qualifiedName - limited to the last 24 characters as qualified names can be long and the last part is often the best 
  means of discrimination.                        
                                  
If you retrieve a relationship (from Get or Search), the entities at the ends of the relationship will be assigned 
labels based on the information available - which is limited to the unique properties in the EntityProxy. If such
an entity is subsequently selected (by clicking on it) Rex will retrieve the full entity and if it can identify
a better label, it will update the label associated with the entity in both the diagram and in the details panel.
                                  
**Tips for using Rex:**
                                 
* Rex uses the Metadata Collection API to interrogate the chosen repository. All Egeria repositories support this API, but
  some functions of the API are optional, so not all repositories support them. For maximum support for graph
  traversals, if possible choose a repository server that has the Egeria graph repository. Remember that Egeria servers can
  create reference copies of objects, so you can ask a server with a graph repository about objects that belong to
  other repositories in the cohort.
  
* Relationship types do not have many attributes; some have no type-defined attributes at all. If you have trouble searching
  for a relationship, it may be easier to search for an entity that you believe is connected to the relationship. Alternatively,
  use another tool to retrieve the relationship GUID and paste that into Rex and use the Get button.

* If you are short of screen space, there a couple of things you can do to save space with Rex:

  * One is to click the 'hamburger' menu icon to toggle away the left-hand navigation menu.
  
  * Another is that, once you have selected the repository server to use and retrieved the first
    entity or relationship, you may not need to visit the top part of the screen again, or only infrequently, so 
    try scrolling until the top edge of the diagram and controls are at the top of the browser page. The controls
    have been placed level with the top of the diagram to enable this.
    
* At any point during an exploration session you can use the History button to display a dialog that lists where the
  exploration started and what steps have been used, to arrive at the graph that is currently displayed.

* You can save the current diagram as a .png image file, using the button near the top-left of the diagram.
  Before capturing the image, consider clicking on the currently selected object to remove the focus color, if you want to
  see its home repository color instead.
    
* After capturing the history and image, as described above, it is straightforward to copy and paste them into a report
document.
                    
**Customization:**
               
Rex uses a styled set of colors, so it can be re-themed. The important colors are defined as CSS variables in the
shared-styles.js file. They are called --egeria-primary-color and --egeria-secondary-color.
By setting the egeria-primary-color you can achieve different themes.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext, useState, useRef }   from "react";

import PropTypes                                 from "prop-types";

import { InstancesContext }                      from "../../contexts/InstancesContext";

import { RepositoryServerContext }               from "../../contexts/RepositoryServerContext";

import TraversalResultHandler                    from "./TraversalResultHandler";

import HistoryResultHandler                      from "./HistoryResultHandler";

import "../../rex.scss";


export default function GraphControls(props) {

  const instancesContext        = useContext(InstancesContext);
  
  const repositoryServerContext = useContext(RepositoryServerContext);

  
  /*
   * status records the state of the current traversal request (if any) for cancellation
   * There is also a useRef for current state so that the callbacks from the POSTs can
   * read the **current** version of state, which may have changed since the callback 
   * was registered (i.e. on the POST call). In the event of a cancel, status should 
   * have changed to 'cancelled' and we need the callback to see the change.
   */
  const [status, setStatus] = useState("idle");  // { "idle", "pending", "cancelled:", "complete" }
  const statusRef = useRef();
  statusRef.current = status;

  const [histStatus, setHistStatus] = useState("idle");  // { "idle", "pending", "cancelled:", "complete" }
  const histStatusRef = useRef();
  histStatusRef.current = histStatus;

  const [history, setHistory]                  = useState([]); 

  console.log("GraphControls: rendering, status is "+status);



  const [traversalSpecification, setTraversalSpecification]                    = useState({}); 
  const [preTraversalEntityTypes, setPreTraversalEntityTypes]                  = useState([]); 
  const [preTraversalRelationshipTypes, setPreTraversalRelationshipTypes]      = useState([]); 
  const [preTraversalClassificationTypes, setPreTraversalClassificationTypes]  = useState([]); 


  

  /*
   * Handler for Explore button - initiate a pre-traversal
   * Function to explore the neighborhood around the current focus entity
   */
  const preTraversal = () => {

    setStatus("pending");

    console.log("GraphControls: perform pre-traversal starting at the focus entity");

    const focusCat = instancesContext.getFocusCategory();

    if (focusCat !== "Entity") {
      console.log("Please select an entity from which to explore...");  // need user-visible feedback
      return;
    }

    const entityGUID = instancesContext.getFocusGUID();

    if (entityGUID === "") {
      console.log("Please select an entity from which to explore...");  // need user-visible feedback
      return;
    }

    const entityLabel = instancesContext.getFocusEntity().entityDigest.label;

    // Save the traversal parameters into the traversal spec....
    let traversalSpec = {};    
    traversalSpec.serverName = repositoryServerContext.repositoryServerName;
    traversalSpec.entityGUID = entityGUID;
    traversalSpec.entityLabel = entityLabel;
    traversalSpec.depth = 1;
    setTraversalSpecification(traversalSpec);

    // No filtering is applied to the pre-traversal...
    repositoryServerContext.repositoryPOST("instances/rex-pre-traversal", 
      { entityGUID : entityGUID,
        depth      : 1            },  // depth is always limited to 1
        _preTraversal); 
  }


  /*
   * Handle completion of explore
   */
  const _preTraversal = (json) => {  

    if (statusRef.current !== "cancelled" && statusRef.current !== "complete") {

      if (json !== null) {

        if (json.relatedHTTPCode === 200) {

          /*
           * Should have a traversal object        
           */
          let rexPreTraversal = json.rexPreTraversal;
          if (rexPreTraversal !== null) {
            console.log("_preTraversal: got back rexPreTraversal..."+rexPreTraversal);
         
            // Display traversal filters. On the submit handler launch the real (filtered) traversal
            // and push the result up to the InstancesContext.

            // Unpack the RexPreTraversal fields
            //    private String                    entityGUID;                    // must be non-null
            //    private Map<String,RexTypeStats>  entityInstanceCounts;          // a list of type guids or null
            //    private Map<String,RexTypeStats>  relationshipInstanceCounts;    // a list of type guids or null
            //    private Map<String,RexTypeStats>  classificationInstanceCounts;  // a list of names or null
            //    private Integer                   depth;
                   

            let localPreTraversalResults = {};

            // Process the entity instance stats...
            localPreTraversalResults.entityTypes = [];
            const entityInstanceCounts = rexPreTraversal.entityInstanceCounts;
            if (entityInstanceCounts != null) {
              const typeNames = Object.keys(entityInstanceCounts);
              typeNames.forEach(typeName => {
                const count = entityInstanceCounts[typeName].count;
                const typeGUID = entityInstanceCounts[typeName].typeGUID;
                // Stash the typeName, typeGUID (and count) in this.preTraversal for later access
                localPreTraversalResults.entityTypes.push( { 'name' : typeName  , 'guid' : typeGUID , 'count' : count , 'checked' : false });
              });
              localPreTraversalResults.entityTypes.sort((a, b) => (a.name > b.name) ? 1 : -1);
              localPreTraversalResults.entityTypes.forEach(ent => {
                console.log("Traversal encountered entity type "+ent.name);
              });
            }

            // Process the relationship instance stats...
            localPreTraversalResults.relationshipTypes = [];
            const relationshipInstanceCounts = rexPreTraversal.relationshipInstanceCounts;
            if (relationshipInstanceCounts != null) {
              const typeNames = Object.keys(relationshipInstanceCounts);
              typeNames.forEach(typeName => {
                const count = relationshipInstanceCounts[typeName].count;
                const typeGUID = relationshipInstanceCounts[typeName].typeGUID;
                // Stash the typeName, typeGUID (and count) in this.preTraversal for later access
                localPreTraversalResults.relationshipTypes.push( { 'name' : typeName, 'guid' : typeGUID  , 'count' : count , 'checked' : false });
              });
              localPreTraversalResults.relationshipTypes.sort((a, b) => (a.name > b.name) ? 1 : -1);
              localPreTraversalResults.relationshipTypes.forEach(rel => {
                console.log("Traversal encountered relationship type "+rel.name);
              });
            }

            // Process the classification instance stats...
            localPreTraversalResults.classificationTypes = [];
            const classificationInstanceCounts = rexPreTraversal.classificationInstanceCounts;
            if (classificationInstanceCounts != null) {
              const typeNames = Object.keys(classificationInstanceCounts);
              typeNames.forEach(typeName => {
                const count = classificationInstanceCounts[typeName].count;
                //const typeGUID = classificationInstanceCounts[typeName].typeGUID;
                // Stash the typeName, typeGUID (and count) in this.preTraversal for later access
                localPreTraversalResults.classificationTypes.push( { 'name' : typeName, 'guid' : null  , 'count' : count , 'checked' : false });
              });
              localPreTraversalResults.classificationTypes.sort((a, b) => (a.name > b.name) ? 1 : -1);
              localPreTraversalResults.classificationTypes.forEach(cls => {
                console.log("Traversal encountered classification type "+cls.name);
              });
            }

            setPreTraversalEntityTypes(localPreTraversalResults.entityTypes);
            setPreTraversalRelationshipTypes(localPreTraversalResults.relationshipTypes);
            setPreTraversalClassificationTypes(localPreTraversalResults.classificationTypes);      
          }
        }
        else {
          /*
           * Request failed
           */      
          alert("Traversal request to repository server returned status code "+json.relatedHTTPCode+" exception "+json.exceptionErrorMessage);
        }
      }
  
      console.log("_preTraversal set status to complete");
      setStatus("complete");
    }  
    
    else {
      setStatus("idle");
    }
    
  };
  
 



  /*
   * Handler for submit of traversal results modal
   */
  const submitTraversalModal = (evt) => {
    //console.log("GraphControls: submit the traversal modal :"+evt.target.value);

    /*     
     * Invoke the InstancesComtext explore operation - passing it the filters which 
     * for entities and relationships are converted to typeGUIDs. Classifcations are 
     * passed as a list of names, so don't need conversion but we just want the 'name'.
     * The explore will perform the full traversal and processes the retrieved instance graph.
     */
    let selectedEntityTypeGUIDs = [];
    preTraversalEntityTypes.forEach( (type) => {
      //const typeGUID = typesContext.getEntityType(type.name).entityDef.guid;
      if (type.checked) {
        selectedEntityTypeGUIDs.push(type.guid);
      }
    });
    //setSelectedEntityTypeGUIDs(localEntityTypeGUIDs);

    let selectedRelationshipTypeGUIDs = [];
    preTraversalRelationshipTypes.forEach(type=> {
      //const typeGUID = typesContext.getRelationshipType(typeName).relationshipDef.guid;
      if (type.checked) {
        selectedRelationshipTypeGUIDs.push(type.guid);
      }
    });

    let selectedClassificationTypeNames = [];
    preTraversalClassificationTypes.forEach(type=> {      
      if (type.checked) {
        selectedClassificationTypeNames.push(type.name);
      }
    });

    instancesContext.explore(selectedEntityTypeGUIDs, selectedRelationshipTypeGUIDs, selectedClassificationTypeNames);

    // Clear the traversal results   
    setPreTraversalEntityTypes([]);
    setPreTraversalRelationshipTypes([]);
    setPreTraversalClassificationTypes([]);

    // Hide the traversal dialog
    //setTraversalModalShow(false);
    setStatus("idle");
  }

  /*
   * Handler for cancel of traversal results modal.
   * The modal is hidden but is not cleared - so if needed it can be re-displayed and the results
   * of the previous traversal will still be available.
   */
  const cancelTraversalModal = (evt) => {
    //console.log("GraphControls: cancel the traversal modal :"+evt.target.value);
    //setTraversalModalShow(false);
    console.log("GraphControls: cancel callback invoked status is "+status);
    if (status === "cancelled") {
      console.log("GraphControls: cancel callback setting status to idle");
      setStatus("idle");
    }
    else if (status === "complete") {
      console.log("GraphControls: cancel callback setting status to idle");
      setStatus("idle");
    }
    else {
      console.log("GraphControls: cancel callback setting status to cancelled");
      setStatus("cancelled");
    }
    
  }





  /*
   * Handler for updating traversal results when user checks or unchecks a type in the traversal results
   */
  const selectCallback = (category, name) => {

    console.log("GraphControls: selectCallback invoked category "+category+" name "+name);

  
    if (category === "Entity") {
      let updates = [];      
      preTraversalEntityTypes.forEach((type) => {
        let newtype = type;
        if (type.name === name) {
          console.log("Entity type "+name+" toggled");         
          newtype.checked = !(type.checked);          
        }          
        updates.push( newtype );                
      });   
      setPreTraversalEntityTypes(updates);   // reflect the change in checked state in the pre-trav list
    }

    if (category === "Relationship") {
      let updates = [];      
      preTraversalRelationshipTypes.forEach((type) => {
        let newtype = type;
        if (type.name === name) {
          console.log("Relationship type "+name+" was toggled");
          newtype.checked = !(type.checked);              
        }   
        updates.push( newtype );       
      });   
      setPreTraversalRelationshipTypes( updates );
    } 

    if (category === "Classification") {
      let updates = [];      
      preTraversalClassificationTypes.forEach((type) => {
        let newtype = type;
        if (type.name === name) {
          console.log("Classification type "+name+" was toggled");
          newtype.checked = !(type.checked);              
        }   
        updates.push( newtype );  
      });   
      setPreTraversalClassificationTypes( updates );
    } 
          
  }


  const setAllCallback = (checked) => {
    /*
     * Set all entity types to checked...
     */
    let updates = [];      
    preTraversalEntityTypes.forEach((type) => {
      let newtype = Object.assign(type, {checked : checked});                                     
      updates.push( newtype );                
    });   
    setPreTraversalEntityTypes(updates); 
    /*
     * Set all relationship types to checked...
     */
    updates = [];   
    preTraversalRelationshipTypes.forEach((type) => {
      let newtype = Object.assign(type, {checked : checked});                                     
      updates.push( newtype );                
    });   
    setPreTraversalRelationshipTypes(updates); 
    /*
     * Set all classification types to checked...
     */
    updates = [];   
    preTraversalClassificationTypes.forEach((type) => {
      let newtype = Object.assign(type, {checked : checked});                                     
      updates.push( newtype );                
    });   
    setPreTraversalClassificationTypes(updates); 

  }

  const getHistory = () => {

    setHistory(instancesContext.getHistory());
    setHistStatus("complete");
  };

const cancelHistoryModal = () => {
  // NO OP really
  setHistStatus("idle");
};

const submitHistoryModal = () => {
  // NO OP really
  setHistStatus("idle");
};



  
  
  return (
    
    <div className={props.className}>
             
        <p className="descriptive-text">
          Traversal count : {instancesContext.getLatestActiveGenId()}
        </p>
         
        <button className="graph-control-button"
          onClick = { () => preTraversal() }  >
          Explore
        </button>

        <button className="graph-control-button"
          onClick = { () => instancesContext.removeGen() }  >
          Undo
        </button>

        <button className="graph-control-button"
          onClick = { () => instancesContext.clear() }  >
          Clear
        </button>
       
        <button className="graph-control-button" 
          onClick = { () => getHistory() }  >
          History
        </button>      

        <TraversalResultHandler status                = { status }
                                spec                  = { traversalSpecification }  
                                selectCallback        = { selectCallback }
                                setAllCallback        = { setAllCallback }
                                entityTypes           = {preTraversalEntityTypes}                     
                                relationshipTypes     = {preTraversalRelationshipTypes}
                                classificationTypes   = {preTraversalClassificationTypes}                                                      
                                onCancel              = { cancelTraversalModal }
                                onSubmit              = { submitTraversalModal } />
        

        <HistoryResultHandler   status                = { histStatus }                                
                                history               = { history }                                                                
                                onCancel              = { cancelHistoryModal }
                                onSubmit              = { submitHistoryModal } />
               

    </div>     
  
  );

}

GraphControls.propTypes = {  
  className  : PropTypes.string
}


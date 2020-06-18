/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext, useState, useEffect, useRef }    from "react";

import { TypesContext }                                      from "../../contexts/TypesContext";

import { FocusContext }                                      from "../../contexts/FocusContext";

import * as d3                                               from "d3";

import PropTypes                                             from "prop-types";

import "./diagram.scss";

/**
 *
 * EntityNeighborhoodDiagram implements a web component for drawing the relationships
 * in which an entity type can participate.
 *
 * This component renders a radial plot with the focus entity type at the centre and the
 * available relationship types radiating from it, with the neighbour entity types at the
 * outer ends of the relationship types. The focus type is highlighted.
 *
 * It is possible to click on a relationship or entity type to 'select' it - if the type is
 * an entity type it becomes the new focus; if it is a relationship type it becomes the new
 * view type (and will be displayed in the details panel).
 *
 * If the user selects any of the neighbouring entity types it causes that type to become
 * the new focus, which puts the newly selected entity type at the center and shows its details
 * in the details panel.
 *
 * If the user selects any of the relationship types, either in the diagram or in
 * the entity's details, this changes the view type to the selected relationship type. If the user
 * subsequently clicks on another entity type from the relationship's details panel, that new
 * entity type becomes the selected focus type and both the diagram and the details panel are updated.
 * Similarly, selecting an entity type from within a classification type displayed in the
 * details panel, the entity type will become the focus.
 *
 *
 * A 'neighbourhood' object looks like this:
 *
 * {
 *   "root": "alpha",
 *   "nodes" : [
 *       { "name" : "beta" } ,
 *       { "name" : "gamma" }
 *   ]
 *   "links": [
 *       {  "source" : "alpha" , "target" : "beta" , "name" : "alphabeta" } ,
 *       {  "source" : "alpha" , "target" : "gamma" , "name" : "alphagamma" }
 *   ]
 * }
 *
 * The code will add x and y coordinates to center the named node and arrange the
 * others around it radially.
 *
 */

export default function EntityNeighborhoodDiagram(props) {

 
  const typesContext = useContext(TypesContext);

  const focusContext = useContext(FocusContext);

  const margin                      = {top: 0, right: 0, bottom: 0, left: 0};

  const egeria_primary_color_string = "#71ccdc";
  
  /*
   * Need to retain d3 across calls to render diagram
   */
  const d3Container = useRef(null);

  const drgContainerDiv = useRef();

  let scrolling = false;

  const checkFocusSet = () => {
    /*
     * Check focusType is set...
     */
    if (focusContext.focus === undefined || focusContext.focus === "") {
      return false;
    }
    return true;
  };


  /*
   * This method clears any introductory text or previous rendering of the diagram, and resets control properties
   */
  const initialiseNeighborhoodDiagram = () => {

    const svg = d3.select(d3Container.current);
    svg.selectAll("svg").remove();
    /*
     * Clear the introductory text...
     */
    d3Container.current.innerHTML = "";

    /*
     * Initialise control state...
     */
    scrolling = false;
  }



  /*
   * Convert type explorer information into an entity type neighbourhood
   * 
   * The neighbourhood object needs to be formatted as follows:
   *
   * {
   *   "root" :
   *   {
   *       "category" : "Entity";
   *       "name"     : "typeName" ,
   *       "data"     : eex
   *   }
   *
   *   "nodes" : [
   *       {
   *           "category" : "Entity" | "Relationship" ,
   *           "name"     : "typeName" ,
   *           "data"     : eex
   *       }
   *   ]
   *
   *   "links": [
   *       {  "source" : "alpha" , "target" : "beta" , "role" : "roleName" } ,
   *       {  "source" : "alpha" , "target" : "gamma" , "role" : "roleName" }
   *   ]
   * }
   */

  const createNeighborhood = (typeName) => {

    let eex = typesContext.getEntityType(typeName);

    let root = {};
    root.name = typeName;
    root.data = eex;
    root.category = "Entity";

    let n = {};
    n.root = root;
    n.nodes = [];
    n.links = [];

    /*
     * Form a list of all relationships in alpha order
     */
    let relationshipEntries = {};

    /*
     * Inherited relationships
     */
    const inheritedRelNames = eex.inheritedRelationshipNames;
    if (inheritedRelNames !== undefined) {
      inheritedRelNames.forEach(inheritedRelName => {
        relationshipEntries[inheritedRelName]={};
        relationshipEntries[inheritedRelName].inherited=true;
      });
    }

    /*
     * Local relationships
     */
    const relationshipNames = eex.relationshipNames;
    if (relationshipNames !== undefined) {
      relationshipNames.forEach(relationshipName => {
        relationshipEntries[relationshipName]={};
        relationshipEntries[relationshipName].inherited=false;
      });
    }

    const relationshipNamesUnsorted = Object.keys(relationshipEntries);
    const relationshipNamesSorted = relationshipNamesUnsorted.sort();

    relationshipNamesSorted.forEach(relName => {
      const rex = typesContext.getRelationshipType(relName);
      const end1 = rex.relationshipDef.endDef1;
      const end2 = rex.relationshipDef.endDef2;
      const end1TypeName = end1.entityType.name;
      const end2TypeName = end2.entityType.name;

      let remoteTypeName;
      let roleRemote;
      let roleRoot;
      let cardRemote;
      let cardRoot;
      let numRoot;


      /*
       * Match the currently selected type to one end of the relationship type.
       */

      /*
       * Test whether this type is a type of end1 - and get the distance
       */
      const dist1 = entityIsTypeOf(typeName, end1TypeName);

      /*
       * Test whether this type is a type of end2 - and get the distance
       */
      const dist2 = entityIsTypeOf(typeName, end2TypeName);

      /*
       * Choose the shorter valid distance - for max. entropy
       */

      /*
       * Dispense with the obvious error case first:
       */
      if (dist1 < 0 && dist2 < 0) {
        alert("ERROR: neither end of relationship type "+relName+" is suitable for type "+typeName);
        /*
         * Return (from loop function) without adding the relationship
         */
        return;
      }

      /*
       * We know that at least one of dist1, dist2 is >=0, and should select the lower of the two.
       * If they are equal then can arbitrarily select end1.
       * 
       * Simplest tests are:
       *   if dist1 < 0     - it must be end2
       *   if dist2 >= 0    - if dist1 > dist2 it must be end2
       *   else             - it must be end1
       * 
       * Also set end roles
       */

      if (dist1 < 0 || (dist2 >= 0 && dist1 > dist2) ) {
        remoteTypeName = end1TypeName;
        roleRemote     = end1.attributeName;
        roleRoot       = end2.attributeName;
        cardRemote     = convertCardinalityToSymbol(end1.attributeCardinality);
        cardRoot       = convertCardinalityToSymbol(end2.attributeCardinality);
        numRoot        = "2";
      }
      else {
        remoteTypeName = end2TypeName;
        roleRemote     = end2.attributeName;
        roleRoot       = end1.attributeName;
        cardRemote     = convertCardinalityToSymbol(end2.attributeCardinality);
        cardRoot       = convertCardinalityToSymbol(end1.attributeCardinality);
        numRoot        = "1";
      }

      /*
       * Set up nodes
       */
      const remoteEntityX = typesContext.getEntityType(remoteTypeName);

      let relNode = {};
        relNode.name = relName;
        relNode.data = rex;
        relNode.category = "Relationship";
        relNode.numRoot = numRoot;
        relNode.inherited = relationshipEntries[relName].inherited;
        n.nodes.push(relNode);

        let remoteNode = {};
        remoteNode.name = remoteTypeName;
        remoteNode.data = remoteEntityX;
        remoteNode.category = "Entity";
        n.nodes.push(remoteNode);

        let innerLink = {};
        innerLink.source = n.root;
        innerLink.target = relNode;
        innerLink.name = "("+cardRoot+")"+":"+roleRoot;
        n.links.push(innerLink);

        let outerLink = {};
        outerLink.source = relNode;
        outerLink.target = remoteNode;
        outerLink.name = "("+cardRemote+")"+":"+roleRemote;
        n.links.push(outerLink);
      }
    );

    return n;
  }

  /*
   * Method to convert Egeria cardinality into a displayable symbol
   */
  const convertCardinalityToSymbol = (card) => {
    let symbol;
    switch (card) {
      case "AT_MOST_ONE" :
        symbol = "1";
        break;
      case "ANY_NUMBER" :
        symbol = "*";
        break;
    }
    return symbol;
  };


  /*
   * Is this entity type a [sub-]type of another entity type? i.e. is it the same or a subtype?
   * Parameters are type names.
   * Returns the distance within the inheritance tree or -1 if not a match (type/subtype)
   */
  const entityIsTypeOf = (thisType, thatType) => {

    if (thisType === thatType) {
      return 0;
    }

    let distance = 0;
    const thisX = typesContext.getEntityType(thisType);
    let superType = thisX.entityDef.superType;
    while (superType !== undefined) {
      distance = distance+1;
      const superTypeName = superType.name;
      if (superTypeName === thatType) {
        return distance;
      }
      const superTypeX = typesContext.getEntityType(superTypeName);
      superType = superTypeX.entityDef.superType;
    }
    /*
     * If you get to here there has not been a match...
     */
    return -1;
  }

  /*
   * This method renders a warning that no focus has been set and displays it in the 'diagram' element
   */

  const renderLackOfFocus = () => {

    const displayDiv = d3Container.current;

    const svg = d3.select(displayDiv);
    svg.selectAll("svg").remove();

    /*
     * Clear the introductory text...
     */
    const textNode = document.createTextNode("No entity type has been selected as the focus");
    const paraNode = document.createElement("p");
    paraNode.appendChild(textNode);
    displayDiv.appendChild(paraNode);
  }



  /*
   * Render the neighborhood diagram
   */
  const renderNeighborhoodDiagram = (nhbd) => {
    
    /*
     * The diagram is approximately circular so set the effective height to the width.
     */
    const diagram = d3Container.current;
    const width = diagram.offsetWidth;
    const height = width;

    let updatedHood = Object.assign(nhbd);

    updatedHood.svg = d3.select(d3Container.current)
                        .append("svg")
                        .attr("width", width)
                        .attr("height", height)
                        .attr("viewBox", [-margin.left, -margin.top, width, height])
                        .style("font", "12px sans-serif")
                        .style("user-select", "none");

    updatedHood.gLink = updatedHood.svg
                                   .append("g")
                                   .attr("fill", "none")
                                   .attr("stroke", "#CCC")
                                   .attr("stroke-opacity", 0.4)
                                   .attr("stroke-width", 1.5);

    updatedHood.gNode = updatedHood.svg
                                   .append("g")
                                   .attr("cursor", "pointer");

    return updatedHood;
}




/*
 * Update the diagram
 */
const nhbdUpdate = (nhbd) => {


  const duration = d3.event && d3.event.altKey ? 2500 : 250;

  /*
   * Although we know the offset width and height, the diagram is approximately circular
   * so set the effective height to the width.
   */
  const diagram = d3Container.current;
  const width = diagram.offsetWidth;
  const height = width;

  const root = nhbd.root;

  /*
   * Compute the new layout.
   */

  nhbd.root.x = width / 2;
  nhbd.root.y = height / 2;

  /*
   * The root node has an extra property to distinguish it...
   */
  nhbd.root.root = true;

  const neighbours = nhbd.nodes;

  if (neighbours !== undefined) {

    const radius = 0.7 * width / 2;

    let center = {};
    center.x = width / 2;
    center.y = height / 2;

    const numNodes = neighbours.length;
    const numRels = numNodes / 2;
    const angle = Math.PI * 2 / numRels;
    const ang_offset = angle / 2;

    /*
     * Place the relationships first.
     */
    let index = 0;
    neighbours.forEach((nhbr) => {
      if (nhbr.category === "Relationship") {
        const ang = index * angle - Math.PI / 2 + ang_offset;
        nhbr.x = radius / 2 * Math.cos(ang) + width/2;
        nhbr.y = radius / 2 * Math.sin(ang) + height/2;
        index = index+1;
      }
    })

    /*
     * Place the remote entities relative to the relationships. Note that this relies
     * on consistent ordering between the two lists.
     */

    index = 0;
    neighbours.forEach((nhbr) => {
      if (nhbr.category === "Entity") {
        const ang = index * angle - Math.PI / 2 + ang_offset;;
        nhbr.x = radius * Math.cos(ang) + width/2;
        nhbr.y = radius * Math.sin(ang) + height/2;
        index = index+1;
      }
    })       
  }
  else {
    /*
     * Neighbourhood root has no neighbours... odd but may as well give up now
     */
    return;
  }


  /*
   * Get the lists of nodes and links
   */
  let nodes = [];
  if (nhbd.nodes !== undefined) {
    nodes = nhbd.nodes;
  }
  else {
    /*
     * Neighbourhood root has no nodes... odd but may as well give up now
     */
    return;
  }

  nodes.push(nhbd.root);
  const links = nhbd.links;

  const transition = nhbd.svg
                         .transition()
                         .duration(duration)
                         .attr("height", height)
                         .attr("viewBox", [-margin.left, -margin.top, width, height])
                         .tween("resize", window.ResizeObserver ? null : () => () => nhbd.svg.dispatch("toggle"));


  /*
   * Update the nodes…
   */

  const node = nhbd.gNode
                   .selectAll("g")
                   .data(nodes, d => d.name);

  /*
   * Enter new nodes at the root's current position.
   */
  const nodeEnter = node.enter()
                        .append("g")
                        .attr("transform", d => `translate(${root.x},${root.y})`)
                        .attr("fill-opacity", 0)
                        .attr("stroke-opacity", 0);



  /*
   * Each node is rendered as a circle plus text label
   */


  /*
   * node text consists of clickable text rendered on top of a shadow to provide contrast with links
   */

  nodeEnter.append("text")
           .attr("dy", "0.31em")
           .attr("x", d => (d.category === "Relationship" ? locateRelationshipLabelX(d, width, height) : locateEntityLabelX(d, width, height)))
           .attr("y", d => (d.category === "Relationship" ? locateRelationshipLabelY(d, width, height) : locateEntityLabelY(d, width, height)))
           .attr("text-anchor", d => locateLabelAnchor(d,width,height))
           .style("font-style", d => d.inherited ? "italic" : "normal")
           .text(d => d.name)
           .on("click", d => { nodeSelected(d.category, d.name); })
           .clone(true)
           .lower()
           .attr("stroke-linejoin", "round")
           .attr("stroke-width", 3)
           .attr("stroke", "white");

  nodeEnter.append("circle")
           .attr('id',d => d.root ? "root-"+d.name : "elem-"+d.name)
           .lower()
           .attr("r", 6)
           .attr("stroke-opacity", d => (d.category === "Entity" ? 1 : 0))
           .attr("stroke-width",1)
           .attr("stroke", "#000")
           .attr("fill", d => (d.category === "Relationship" ? "none" : "#FFF"))
           .on("click", d => { nodeSelected(d.category, d.name); });

  nodeEnter.append("polyline")
           .lower()
           .attr("points","-4 -8, 0 8, 4 -8")
           .attr("stroke-width",1)
           .attr("stroke-opacity", d => (d.category === "Relationship" ? 1 : 0))
           .attr("stroke", "#CCC")
           .attr("fill", "none")
           .attr("transform" , d => `rotate(${ ((d.numRoot==="1")?180:0) + ( (d.x===root.x)?(d.y<root.y?0:180): 180/Math.PI *  Math.atan( (d.y-root.y) / (d.x-root.x) ) + (d.x>root.x?90:-90) ) },0,0)`)

  /*
   * Transition nodes to their new position.
   */
  const nodeUpdate = node.merge(nodeEnter)
                         .transition(transition)
                         .attr("transform", d => `translate(${d.x},${d.y})`)
                         .attr("fill-opacity", 1)
                         .attr("stroke-opacity", 1);



  /*
   * Highlight a selected node, if a type has been selected and selectedCategory is Entity
   */
  nodeUpdate.selectAll('text')
            .attr("fill", d => nhbHighlight(d) ? "blue" : "black" );


  /*
   * Transition exiting nodes to the root's position.
   */
  const nodeExit = node.exit()
                       .transition(transition).remove()
                       .attr("transform", d => `translate(${root.x},${root.y})`)
                       .attr("fill-opacity", 0)
                       .attr("stroke-opacity", 0);



  /*
   * Update the links…
   */

  const link = nhbd.gLink
                   .selectAll("g")
                   .data(links, d => d.name);

  const linkEnter = link.enter()
                        .append("g");

  /*
   * link text consists of non-clickable text rendered on top of a shadow to provide contrast with links
   */
  linkEnter.append("text")
           .attr("fill", "#888")
           .attr("stroke", "none")
           .attr("stroke-width", 0)
           .attr("dy", "0.31em")
           .attr("x", d => (d.source.x+9*d.target.x)/10)
           .attr("y", d => (d.source.y+9*d.target.y)/10)
           .attr("text-anchor", d => ( (d.target.x >= width/2) ? "end" : "start" ) )
           .attr("transform" , d => `rotate(${180/Math.PI * Math.atan((d.target.y-d.source.y)/(d.target.x-d.source.x))},${(d.source.x+9*d.target.x)/10},${(d.source.y+9*d.target.y)/10})`)
           .text(d => d.name)
           .clone(true)
           .lower()
           .attr("stroke-linejoin", "round")
           .attr("stroke-width", 3)
           .attr("stroke", "white");

  linkEnter.append("path")
           .attr("fill-opacity", 1)
           .attr("stroke-opacity", 1)
           .attr("d", d => {
              const src = {x: d.source.x, y: d.source.y};
              const tgt = {x: d.target.x, y: d.target.y};
              return straightPath( {source : src , target : tgt } );
            })
           .lower();

  /*
   * Transition links to their new position.
   */
  const linkUpdate = link.merge(linkEnter)
                         .transition(transition)
                         .attr("fill-opacity", 1)
                         .attr("stroke-opacity", 1);

  /*
   * Transition exiting nodes to the root's position.
   */
  link.exit()
      .transition(transition)
      .remove()
      .attr('d', function (d) {
        const o = {x: root.x, y: root.y};
        return straightPath( {source : o , target : o } );
      });

       
  /* 
   * Opportune moment to scroll so that focus is in center of diagramContainer
   */
   
  scrollSelectedIntoView(focusContext.focus);

}


  /*
   * If an entity type is selected and the view has not already been scrolled, scroll it now.
   */
  const scrollSelectedIntoView = (typeToView) => {

    if (scrolling === false) {

      scrolling = true;

      if (typeToView !== undefined && typeToView !== "") {

        const drg = document.getElementById("drawingContainer");

        const leftOffset = 750;
        const h_target = (leftOffset + props.outerWidth/2.0);
        const topOffset = 230;
        const v_target = (topOffset + props.outerHeight/2.0);

        const tol = 10; /* #pixels tolerance */

        /*
         * Supply target as hpos, vpos to achieve initial scroll attempt.
         */
        incrementalScroll(drg, typeToView, h_target, true, v_target, true, tol);

      }
    }
  }


  /*
   * Although both dimensions could be scrolled together, if one hits the wall it should not
   * prevent the other from continuing to make progress. Yuo could employ lots of state
   * checking, but it is simpler to scroll each dimension independently.
   *
   * Conversion is necessary between bounding client rectangle and
   * parent container offset position, which requires (fixed) offsets to accommmodate
   * top and lhs containers.
   */
  const incrementalScroll = (drg, typeName, h_pos, h, v_pos, v, tol) => {

    if (scrolling) {

      /* See where the element is since the previous scroll attempt */
      const elem = document.getElementById('root-'+typeName);
      const brect = elem.getBoundingClientRect();
      const hcur = brect.left;
      const vcur = brect.top;

      /*
       * Horizontal
       */
      if (h) {
        /* Did predecessor manage to move by at least tol? */
        if (hcur - h_pos === 0) {
          h = false;
        }
        /* Continue even if above was false, to get at least one scroll effort */
        else {
          const leftOffset = 750;
          const h_target = (leftOffset + props.outerWidth/2.0);
          const h_diff = hcur - h_target;

          if (Math.abs(h_diff) > tol) {
            const h_mov = h_diff / 2.0;
            drg.scrollBy(h_mov,0); 
          }
          else {
            h = false;
          }
        }
      }

      /*
       * Vertical
       */
      if (v) {
        /* Did predecessor manage to move by at least tol? */
        if (vcur - v_pos === 0) {
          v = false;
        }
        else {
          const topOffset = 230;
          const v_target = (topOffset + props.outerHeight/2.0);
          const v_diff = vcur - v_target;

          if (Math.abs(v_diff) > tol) {
            const v_mov = v_diff / 2.0;
            drg.scrollBy(0,v_mov);
          }
          else {
            v = false;
          }
        }
      }

      if (h || v) {
        /* Repeat... */
        setTimeout( () => incrementalScroll(drg, typeName, hcur, h, vcur, v, tol) , 10);
      }
      else {
        scrolling = false;
      }
    }
  }


  /*
   * Draw a straight path from source node to target node
   */
  const straightPath = ({source, target}) => {
    return 'M' + source.x + ',' + source.y
         + 'L' + target.x + ',' + target.y;
  }


  /*
   * Indicate whether a node should be highlighted - only the focus is highlighted so applies to Entity types only
   */
  const nhbHighlight = (d) => {
    /*
     * Check whether node is selected as focus
     */
    if (focusContext.focus === d.name) {
      return true;
    }
    return false;
  }



  /*
   * Utility functions
   */


  const locateRelationshipLabelX = (d, width, height) => {
    const x = (d.x-width/2)*.05 + (0.2*Math.abs(d.y - height/2) > Math.abs(d.x - width/2) ? (width/2 - d.x)*.5 : 0);
    return x;
  }

  const locateEntityLabelX = (d, width, height) => {
    const x = (d.x-width/2)*.05;
    return x;
  }

  const locateRelationshipLabelY = (d, width, height) => {
    const y = (d.y-height/2)*.15 + (0.15*Math.abs(d.y - height/2) > Math.abs(d.x - width/2) ? (d.y-height/2)*.08 : 0);
    return y;
  }


  const locateEntityLabelY = (d, width, height) => {
    const y = (d.y-height/2)*.05;
    return y;
  }

  const locateLabelAnchor = (d,width,height) => {
    return (0.05*Math.abs(d.y - height/2) > Math.abs(d.x - width/2) ? "middle" : ((d.x < width/2) ? "end" : ((d.x > width/2) ? "start" : "middle")) );
  }

  /*
   * Because all types in the inheritance diagram are entity types, the selection of a
   * type will request a change of focus.
   */
  const nodeSelected = (cat, typeName) => {
    focusContext.typeSelected(cat, typeName);
  }



  useEffect(
    () => {

      if ( d3Container.current && typesContext.tex) {       
        /* 
         * Initial rendering...
         * Get the entity types and create and render the neighborhood diagram.
         *
         * Data is unlikely to change unless server is changed - repeat the initial rendering...
         */ 

        if (!checkFocusSet()) {
          renderLackOfFocus();
        }
        else {
          initialiseNeighborhoodDiagram();
          let nhbd = createNeighborhood(focusContext.focus);
          nhbd = renderNeighborhoodDiagram(nhbd);
          nhbdUpdate(nhbd);
        }
      }
    },
    [d3Container.current, typesContext.tex, focusContext.focus]
  )


  useEffect(
    () => {

      drgContainerDiv.current.style.width=""+props.outerWidth+"px";
      drgContainerDiv.current.style.height=""+props.outerHeight+"px";

      /*
       * It is better not to scroll on a resize as we do not know the user's
       * intentions and they mau not want the focus type in the (new) center.
       * If we did want to, it would be possible to auto-scroll but would need
       * to ensure it is an 'immediate' scroll rather than the smooth scroll
       * implemented for focus changes. This is because resize is not a single
       * event, but a stream of events, with the user very much driving the
       * current size. So snapping to the target position would be preferable
       * over the use of a decaying trajectory which does not necessarily
       * reflect the latest size target. The events would need to be serialized
       * and the one currently being processed is likely to be using a stale
       * target.
       */

    },
    [props.outerHeight, props.outerWidth]
  )


  return (
    <div className="drawing-container" id="drawingContainer" ref={drgContainerDiv}>
      <div id="drawingArea" ref={d3Container}>
      </div>
    </div>
  );

}


EntityNeighborhoodDiagram.propTypes = {
  children: PropTypes.node,
  outerHeight: PropTypes.number,
  outerWidth: PropTypes.number
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import React, { useContext, useState, useEffect, useRef }    from "react";

import { TypesContext }                   from "../../contexts/TypesContext";

import { FocusContext }                   from "../../contexts/FocusContext";

import * as d3                            from "d3";

import PropTypes                          from "prop-types";

import "./diagram.scss";


/*
 * The EntityInheritanceDiagram renders a tree of entity types organised as a 
 * hierarchy by inheritance. The tree is clickable for selection of an entity 
 * type and collapse/expand of a subtree.
 * 
 */

export default function EntityInheritanceDiagram(props) {

 
  const typesContext = useContext(TypesContext);

  const focusContext = useContext(FocusContext);

  const width                       = 1200;
  const height                      = 1120;
  const margin                      = {top: 30, right: 30, bottom: 30, left: 30};
  const egeria_primary_color_string = "#71ccdc";




  /*
   * Need to retain d3 across calls to render diagram
   */
  const d3Container                       = useRef(null);

  const drgContainerDiv                   = useRef();

  const [renderedTrees, setRenderedTrees] = useState({});

  let scrolled = false;

  let treeDepth;
  

  /*
   * This method clears any introductory text or previous rendering of the diagram, and resets control properties
   */
  const initialiseInheritanceDiagram = () => {

    const svg = d3.select(d3Container.current);

    svg.selectAll("svg").remove();

    /*
     * Clear the introductory text...
     */
    svg.innerHTML = "";

    /* 
     * Initialise control state...
     */
    setRenderedTrees([]);
    scrolled = false;
    
  }

  /*
   * This method iterates over the known entity types and creates a separate tree for
   * any that have no supertype (i.e. the entity is itself a root)
   * For each such root, create the inheritance tree and render it
   */
  const createInheritanceTrees = () => {

    /*
     * The inheritance diagram shows the type hierarchy of entity types.
     * The user may have already selected a type (of any category) but it is optional
     *
     * Each inheritance tree needs to be formatted as follows:
     *
     * {
     *   "name": "alpha",
     *   "children": [
     *     {
     *       "name": "beta"
     *     },
     *     {
     *       "name": "gamma"
     *     },
     *   ]
     * }
     *
     *
     * The createInheritanceTree method will get an entity type and its subtypes from 
     * the typesContext and produce the necessary tree.
     */


    /*
     * For any entity types that don't have a supertype - create a tree
     * Those with supertypes will be included in one of the trees.
     * Tree roots are processed in alpha order
     */

    const entityTypes = typesContext.getEntityTypes();
    const entityTypeNamesUnsorted = Object.keys(entityTypes);
    const entityTypeNamessSorted = entityTypeNamesUnsorted.sort();
    entityTypeNamessSorted.forEach(entityTypeName => {
      if (entityTypes[entityTypeName].entityDef.superType == null) {
        const typeName = entityTypes[entityTypeName].entityDef.name;
        treeDepth = 0;
        const tree = createInheritanceTree(typeName);
        renderInheritanceTree(tree, typeName, treeDepth);
      }
    })
  }


  /*
   * This method creates the inheritance tree for a single root entity
   */
  const createInheritanceTree = (typeName) => {

    let inheritanceTree = {};

    /*
     * Start at the type with typeName and follow subtype links to compose children
     */
    const entityType = typesContext.getEntityType(typeName);
    const childNames = entityType.subTypeNames;
    inheritanceTree = addSubTree(inheritanceTree, typeName, childNames, 1)
    return inheritanceTree;
  }


  /*
   * Recursively work down the tree adding subtrees...
   */
  const addSubTree = (tree, name, childNames, nodeDepth) => {

    /*
     * Add the current node then recurse for the children
     */
    const level = nodeDepth;
    if (nodeDepth > treeDepth) {
      treeDepth = nodeDepth;
    }
    tree.name = name;
    tree.category = "Entity";
    if (childNames !== null && childNames.length > 0) {
      tree.children = [];
      const childNamesSorted = childNames.sort();
      childNamesSorted.forEach( childName => {
        const entityType = typesContext.getEntityType(childName);
        const nodeChildNames = entityType.subTypeNames;
        let subtree = {};
        addSubTree(subtree, childName, nodeChildNames, level + 1);
        tree.children.push(subtree);
      });
    }
    return tree;
  }

  
  /*
   * Render one inheritance tree
   */
  const renderInheritanceTree = (typeHierarchy, typeName, treeDepth) => {

    let thisTree     = {};
    thisTree.dx      = 30;
    thisTree.dy      = width / treeDepth;
    thisTree.root    = d3.hierarchy(typeHierarchy);
    thisTree.root.x0 = thisTree.dy / 2;
    thisTree.root.y0 = 0;
    thisTree.root.descendants().forEach((d, i) => {
      d.id = i;
      d._children = d.children;
      /*
       * For nodes to be initially in collapsed state, set d.children = null;
       */
    });

    thisTree.svg = d3.select(d3Container.current)
                     .append("svg")
                     .attr("width", width)
                     .attr("height", thisTree.dx)
                     .attr("viewBox", [-margin.left, -margin.top, width, thisTree.dx])
                     .style("font", "12px sans-serif")
                     .style("user-select", "none");

    thisTree.gLink = thisTree.svg
                             .append("g")
                             .attr("fill", "none")
                             .attr("stroke", "#888")
                             .attr("stroke-opacity", 0.4)
                             .attr("stroke-width", 1.5);

    thisTree.gNode = thisTree.svg
                             .append("g")
                             .attr("cursor", "pointer");

    /*
     * Remember this tree
     */
    const loc_renderedTrees = renderedTrees;
    loc_renderedTrees[typeName] = thisTree;
    setRenderedTrees(loc_renderedTrees);

  }

  /*
   * Refresh all trees in the diagram.
   * This method is called on initial rendering and if the focus type is changed
   */
  const updateAllTrees = () => {

    const treeNames = Object.keys(renderedTrees);

    treeNames.forEach(treeName => {
      const thisTree = renderedTrees[treeName];
      const root = thisTree.root;
      if (root !== undefined) {
        update(thisTree, root);
      }
    });
  }

  /*
   * Draw a curved path from parent node to child node
   */
  const curvedPath = ({source, target}) => {
    return 'M' + source.y + ',' + source.x
         + 'C' + (source.y + target.y)/2 + ',' + source.x
         + ' ' + (source.y + target.y)/2 + ',' + target.x
         + ' ' + target.y + ',' + target.x
  }

  const transitionComplete = () => {

    /*
     * Earliest opportunity to scroll accurately
     */
    if (focusContext.focus !== undefined && focusContext.focus !== "") {
        scrollSelectedIntoView(focusContext.focus);
    }
  }


  /*
   * If an entity type is selected and the view has not already been scrolled, scroll it now.
   */
  const scrollSelectedIntoView = (typeToView) => {

    /*
     * Conversion is necessary between bounding client rectangle and
     * parent container offset position, which requires (fixed) offsets to accommmodate
     * top and lhs containers.
     */
    const topOffset =  230;
    const leftOffset = 750;

    if (scrolled === false) {
      scrolled = true;

      if (typeToView !== undefined && typeToView !== "") {

        const elem = document.getElementById('elem'+typeToView);

        /*
         * scrollIntoView almost works but the options do not work across browsers,
         * including Safari, so it does not center and is not smooth. The lack of centering
         * means it doesn't unscroll a scrolled diagram
         * The following is what we might *like* to do:
         * elem.scrollIntoView({behavior: "smooth", block:"center", inline:"center"});
         *
         * Instead of scrollIntoView - use incremental scrolling which does work outside 
         * of a web component...
         */
        const brect = elem.getBoundingClientRect();

        let v_togo = brect.top-(topOffset + props.outerHeight/2.0);
        let h_togo = brect.left-(leftOffset + props.outerWidth/2.0);

        const inc = 10;

        const drg = document.getElementById("drawingContainer");
        incrementalScroll(drg, typeToView, h_togo, v_togo, inc);

      }
    }
  }

  const incrementalScroll = (drg, typeName, h_togo, v_togo, inc) => {

    let v_dirinc, h_dirinc;

    /*
     * Vertical dimension
     */
    let v_inc = inc;

    if (Math.abs(v_togo) < v_inc) {
      v_inc = Math.abs(v_togo);
    }
    const v_rate = Math.abs(v_togo) / (10 * v_inc);
    if (Math.abs(v_togo) > 0) {
      v_dirinc = Math.sign(v_togo) * v_inc * v_rate;
      /*
       * scrollBy does not seem to work when in a web component
       * scrollIntoView (which could be called from scrollSelectedIntoView() almost works
       * but the center and smooth options are not well-supported across browsers
       */
      v_togo = v_togo - v_dirinc;
    }

    /*
     * Horizontal dimension
     */
    let h_inc = inc;
    if (Math.abs(h_togo) < h_inc) {
      h_inc = Math.abs(h_togo);
    }
    const h_rate = Math.abs(h_togo) / (10 * h_inc);
    if (Math.abs(h_togo) > 0) {
      h_dirinc = Math.sign(h_togo) * h_inc * h_rate;
      /*
       * scrollBy does not seem to work when in a web component
       * scrollIntoView (which could be called from scrollSelectedIntoView() almost works
       *  but the center and smooth options are not well-supported across browsers
       */
      h_togo = h_togo - h_dirinc;
    }

    drg.scrollBy(h_dirinc, v_dirinc);

    if (Math.abs(h_togo) > h_inc || Math.abs(v_togo) > v_inc) {
      setTimeout( () => incrementalScroll(drg, typeName, h_togo, v_togo, inc) , 10);
    }
  }


  /*
   * Update a subtree within the diagram
   * 'subtree' is the node at the root of the subtree being updated
   */
  const update = (tree, subtree) => {

    /*
     * Since an update is being performed, unset scrolled so that on transition completion
     * the code will re-evaluate the scroll position
     */
    scrolled = false;

    const thisTree = tree;

    const duration = d3.event && d3.event.altKey ? 2500 : 250;

    /*
     * Compute the new tree layout.
     */
    const treeLayout = d3.tree();
    treeLayout.nodeSize([thisTree.dx, thisTree.dy])
    treeLayout(thisTree.root);

    const root = thisTree.root;

    /*
     * Get the lists of nodes and links
     */
    const nodes = root.descendants();
    const links = root.links();

    /*
     * Calculate the overall height of the tree (across all the nodes - the tree is drawn horizontally)
     */
    let left  = root;
    let right = root;
    root.eachBefore(node => {
      if (node.x < left.x)   left = node;
      if (node.x > right.x) right = node;
    });
    const height = right.x - left.x + margin.top + margin.bottom;


    const transition = thisTree.svg
                               .transition()
                               .duration(duration)
                               .attr("height", height)
                               .attr("viewBox", [-margin.left, left.x - margin.top, width, height])
                               .tween("resize", window.ResizeObserver ? null : () => () => thisTree.svg.dispatch("toggle"))
                               .on('end',  () => transitionComplete() );



    /*
     * Update the nodes…
     */

    const node = thisTree.gNode
                         .selectAll("g")
                         .data(nodes, d => d.id);

    /*
     * Enter new nodes at the subtree root's prior position.
     */

    const nodeEnter = node.enter()
                          .append("g")
                          .attr("transform", d => `translate(${subtree.y0},${subtree.x0})`)
                          .attr("fill-opacity", 0)
                          .attr("stroke-opacity", 0)
                          ;

    /*
     * Each node is rendered as a circle with decoration for expand/collapse, plus text label
     */

    nodeEnter.append("circle")
              .attr('id',d => "elem"+d.data.name)
              .attr("r", 6)
              .attr("stroke-width",1)
              .attr("stroke", "#000")
              .attr("fill", "#FFF")
              .on("click", d => {
                d.children = d.children ? null : d._children;
                update(tree,d);
              });

    nodeEnter.append("line")
             .attr("x1", 0).attr("y1", -2).attr("x2", 0).attr("y2", 2)
             .attr("stroke-width",1)
             .attr("stroke-linecap","round")
             .attr("stroke", d => d._children && !d.children ? "#000" : "#FFF")
             .attr("pointer-events","none");

    nodeEnter.append("line")
             .attr("x1", -2).attr("y1", 0).attr("x2", 2).attr("y2", 0)
             .attr("stroke-width",1)
             .attr("stroke-linecap","round")
             .attr("stroke", d => d._children ? "#000" : "#FFF")
             .attr("pointer-events","none");

    /*
     * node text consists of clickable text rendered on top of a shadow to provide contrast with links
     */

    nodeEnter.append("text")
             .attr("dy", "0.31em")
             .attr("x", 12)
             .attr("text-anchor", "start")
             .text(d => d.data.name)
             .on("click", d => { typeSelected("Entity", d.data.name); })
             .clone(true)
             .lower()
             .attr("stroke-linejoin", "round")
             .attr("stroke-width", 3)
             .attr("stroke", "white");

    /*
     * Transition nodes to their new position.
     */

    const nodeUpdate = node.merge(nodeEnter).transition(transition)
                           .attr("transform", d => `translate(${d.y},${d.x})`)
                           .attr("fill-opacity", 1)
                           .attr("stroke-opacity", 1);

    /*
     * Toggle minus to plus depending on collapsed/expanded state...
     */
    nodeUpdate.select('line')
              .attr("x1", 0).attr("y1", -2).attr("x2", 0).attr("y2", 2)
              .attr("stroke", d => d._children && !d.children ? "#000" : "#FFF");

    /*
     * Highlight a selected node, if a type has been selected and selectedCategory is Entity
     */
    nodeUpdate.selectAll('text')
              .attr("fill", d => inhHighlight(d) ? "blue" : "black" );

    /*
     * Transition exiting nodes to the parent's new position.
     */
    const nodeExit = node.exit();
    nodeExit.transition(transition).remove()
                                   .attr("transform", d => `translate(${subtree.y},${subtree.x})`)
                                   .attr("fill-opacity", 0)
                                   .attr("stroke-opacity", 0);


    /*
     * Update the links…
     */

    const link = thisTree.gLink
                         .selectAll("path")
                         .data(links, d => d.target.id);

    /*
     * Enter any new links at the parent's prior position.
     */

    const linkEnter = link.enter()
                          .append("path")
                          .attr("d", d => {
                            const o = {x: subtree.x0, y: subtree.y0};
                            var curve = curvedPath( {source : o , target : o } );
                            return curve;
                          });


    /*
     * Transition links to their new position.
     */

    link.merge(linkEnter)
        .transition(transition)
        .attr("d", d => {
          const s = {x: d.source.x, y: d.source.y};
          const t = {x: d.target.x, y: d.target.y};
          var curve = curvedPath( {source : s , target : t } );
          return curve;
        });

    /*
     * Transition exiting nodes to the parent's new position.
     */

    link.exit()
        .transition(transition)
        .remove()
        .attr('d', function (d) {
          const o = {x: subtree.x, y: subtree.y};
          var curve =  curvedPath( {source : o , target : o } );
          return curve;
        });

    /*
     * Remember the current positions as prior positions - they will be used to calculate transitions
     */

    root.eachBefore(d => {
      d.x0 = d.x;
      d.y0 = d.y;
    });

  }


  /*
   * Indicate whether a node should be highlighted
   */
  const inhHighlight = (d) => {
    /*
     * Check whether node is selected as focus
     */
    if (focusContext.focus === d.data.name) {
      return true;
    }
    return false;
  }



  /*
   * Because all types in the inheritance diagram are entity types, the selection of a
   * type will request a change of focus.
   */
  const typeSelected = (cat, typeName) => {

    focusContext.typeSelected("Entity", typeName);
  }



  useEffect(
    () => {
      if ( d3Container.current && typesContext.tex) {       
        /* 
         * Initial rendering...
         * Get the entity types and create and render the trees (one per root)
         * Call update() on each of the trees.
         *
         * Data is unlikely to change unless server is changed - repeat the initial rendering...
         */ 
        initialiseInheritanceDiagram();

        createInheritanceTrees();

        updateAllTrees();

      }
    },
    [d3Container.current, typesContext.tex]
  )

  useEffect(
    () => {
        updateAllTrees();
    },
    [focusContext.focus]
  )

  useEffect(
    () => {
      drgContainerDiv.current.style.width=""+props.outerWidth+"px";
      drgContainerDiv.current.style.height=""+props.outerHeight+"px";
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


EntityInheritanceDiagram.propTypes = {
  children: PropTypes.node,
  outerHeight: PropTypes.number,
  outerWidth: PropTypes.number
}

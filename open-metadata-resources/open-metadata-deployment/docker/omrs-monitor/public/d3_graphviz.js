/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


function omrsMon_initialize() {

  console.log('called d3_initialize');

  width = 600;
  height = 600;
  node_radius = 5;

  // Nodes represent entities. The id is set to the entityGUID, the x and y properties are for layout
  // and the qualifiedName is the qualifiedName of the entity.
  //myNodes = [ {id:1, x:100, y:100, qualifiedName:"alice"},
  //            {id:2, x:200, y:100, qualifiedName:"bob"},
  //            {id:3, x:200, y:200, qualifiedName:"charlie"}
  //          ];
  myNodes = [];

  // Links represent relationships. The source and target are the entityGUIDs (node.id)
  //myLinks = [ { source: 1, target: 2 },
  //            { source: 1, target: 3 },
  //            { source: 2, target: 2 }
  //          ];
  myLinks = [];

  d3initialize();

  d3update();
}

function nodeId(d) {
  return d.id;
}

function d3initialize() {

  svg = d3.select('#graphviz').append('svg')
    .attr('width', width)
    .attr('height', height);

  attraction = d3.forceManyBody().strength(100).distanceMax(width/2).distanceMin(width/10);
  repulsion  = d3.forceManyBody().strength(-200).distanceMax(width/2).distanceMin(width/10);

  sim = d3.forceSimulation(myNodes)
          .alphaDecay(0.006)
          .force('horiz', d3.forceX(width / 2).strength(0.1))
          .force('vert', d3.forceY(height / 2).strength(0.1))
          .force('repulsion', repulsion)
          .on('tick',tick_function)
          .force('link', d3.forceLink().links(myLinks).id(nodeId).distance(75));
          //.force('center', d3.forceCenter(width / 2, height / 2))
           //.force('attraction', attraction)

  d3update();

}

function nodeId(d) {
   return d.id;
}

function getNode(id) {
  return ( myNodes.filter(obj => { return obj.id === id  })[0] );
}

function tick_function() {

    // If not using a forceLink force, then use attr('x1', function(d) { return getNode(d.source).x; })
    //line.attr('x1', function(d) { return getNode(d.source).x; })
    //    .attr('y1', function(d) { return getNode(d.source).y; })
    //    .attr('x2', function(d) { return getNode(d.target).x; })
    //    .attr('y2', function(d) { return getNode(d.target).y; });
    // However, if using forceLink force the node id accessor is defined by nodeId()
    line.attr('x1', function(d) { return d.source.x; })
        .attr('y1', function(d) { return d.source.y; })
        .attr('x2', function(d) { return d.target.x; })
        .attr('y2', function(d) { return d.target.y; });

    // Prior to node labelling
    //circ.attr('r', node_radius)
    //    .attr('cx', function(d) { return d.x = Math.max(node_radius, Math.min(width  - node_radius, d.x)); })
    //    .attr('cy', function(d) { return d.y = Math.max(node_radius, Math.min(height - node_radius, d.y)); });

    // With node labelling...

    circ.attr('transform', function(d) { return "translate(" + d.x + "," + d.y + ")";});

    circ.selectAll('circle')
        .attr('r', node_radius)
        .attr('cx', 0)
        .attr('cy', 0);

    circ.selectAll("text")
        .text(function(d) { return d.qualifiedName });

    //node_para.text(function(d) { return ""+d.id+":"+Math.floor(d.x)+","+Math.floor(d.y); });

    // When not using forceLink() force, you only need d.source
    //link_para.text(function(d) { return ""+d.source+","+d.target; });
    // When using forceLink() force, node accessor nodeId() means you will need to use d.source.id, not d.source
    //link_para.text(function(d) { return ""+d.source.id+","+d.target.id; });
}

// In order to get the nodes always in front of the links, the nodes need to be re-added to the svg
// So whenever links changes we need to re-generate all the nodes. Or you do select and re-gen of all
// nodes in the tick function. The former is probably more efficient.
function d3update() {
  console.log('tickle the links');
  d3updateLinks();
  console.log('tickle the nodes');
  d3updateNodes();
  console.log('tickle the layout');
  sim.alpha(1);
  sim.restart();
}

function d3updateNodes() {

  // update the visual rendering of the nodes

  sim.nodes(myNodes);

  u = svg.selectAll(".node")
         .data(myNodes)
         .attr('cx',function(d) { return d.x = Math.max(node_radius, Math.min(width  - node_radius, d.x)); })
         .attr('cy',function(d) { return d.y = Math.max(node_radius, Math.min(height - node_radius, d.y)); });

  u.exit().remove();

  // Prior to node labelling
  //circ = u.enter()
  //        .append('circle')
  //        .attr('class', 'node')
  //        .attr('r',node_radius)
  //        .attr('cx',function(d) { return d.x = Math.max(node_radius, Math.min(width  - node_radius, d.x)); })
  //        .attr('cy',function(d) { return d.y = Math.max(node_radius, Math.min(height - node_radius, d.y)); })
  //        .merge(u);

   // With node labelling...
   circ = u.enter()
          .append("g")
          .attr('class', 'node')
          .attr('x',function(d) { return d.x = Math.max(node_radius, Math.min(width  - node_radius, d.x)); })
          .attr('y',function(d) { return d.y = Math.max(node_radius, Math.min(height - node_radius, d.y)); })
          .merge(u)

   circ.append('circle')
          .attr('r',node_radius);
          //.attr('cx',function(d) { return d.x = Math.max(node_radius, Math.min(width  - node_radius, d.x)); })
          //.attr('cy',function(d) { return d.y = Math.max(node_radius, Math.min(height - node_radius, d.y)); })

   circ.append('text')
        .attr("fill","red")
        .text( function(d) { return d.qualifiedName; } )
        .attr("font-family","sans-serif")
        .attr("font-size","15px")
        .attr("stroke-width","0")
        .attr("dx",20)
        .attr("dy",".35em");


  // update the textual rendering of the graph

  //u = d3.select("body").select("#nodes")
  //    .selectAll("p")
  //    .data(myNodes);
  //
  //node_para = u.enter()
  //  .append("p")
  //  .merge(u)
  //  .text(function(d) { return ""+d.id+":"+Math.floor(d.x)+","+Math.floor(d.y); });
  //
  //u.exit().remove();

} 

function d3updateLinks() {

  // update the visual rendering of the links

  sim.force('link').links(myLinks);

  u = svg.selectAll(".link")
    .data(myLinks)
    .attr('x1', function(d) { return d.source.x; })
          .attr('y1', function(d) { return d.source.y; })
          .attr('x2', function(d) { return d.target.x; })
          .attr('y2', function(d) { return d.target.y; });

  u.exit().remove();

  // If not using forceLink() then specify { return getNode(d.source).x; }
  //line = u.enter().append('line')
  //  .attr('class', 'link')
  //  .merge(u)
  //  .attr('x1', function(d) { return getNode(d.source).x; })
  //  .attr('y1', function(d) { return getNode(d.source).y; })
  //  .attr('x2', function(d) { return getNode(d.target).x; })
  //  .attr('y2', function(d) { return getNode(d.target).y; });
  // If using forceLink() then you only need { return d.source.x; }
  line = u.enter().append('line')
          .attr('class', 'link')
          .attr('x1', function(d) { return d.source.x; })
          .attr('y1', function(d) { return d.source.y; })
          .attr('x2', function(d) { return d.target.x; })
          .attr('y2', function(d) { return d.target.y; })
          .merge(u);


  // Re-generate the nodes - so that they are 'on top' of the links
  d3regenNodes();

  // update the textual rendering of the graph

  u = d3.select("body")
        .select("#links")
        .selectAll("p")
        .data(myLinks);

  // If not using forceLink() then specify { return d.source; }
  // If using forceLink() then specify { return d.source.id; }
  link_para = u.enter()
    .append("p")
    .merge(u)
    .text(function(d) { return ""+d.source.id+","+d.target.id; });

  u.exit().remove();
}

function d3regenNodes() {

  // remove and re-append all the nodes

  svg.selectAll(".node").remove()

  d3updateNodes();

}



function omrsMon_addNode(guid, name) {
  console.log('addNode called with name '+name);
  createNode(guid, name);
  console.log('node was added');
  console.log('call d3update');
  d3update();
  console.log('d3 was updated');
}

function omrsMon_addLink(guid,end1Guid,end2Guid) {
  createLinkedNode(guid,end1Guid,end2Guid);
  d3update();
}

function createNode(guid, name) {
  if (myNodes.length == 0) {
    newNode = {id:guid, x:100, y:100, qualifiedName: name};
  }
  else {
    // find an unused id
    //var candidateId = 1;
    //var unique = false;
    //while (unique == false) {
    //  var found = false;
    //  for (var i=0;i<myNodes.length;i++) {
    //    thisNode = myNodes[i];
    //    if (thisNode.id == candidateId) {
    //      found = true;
    //      candidateId = candidateId+1;
    //    }
    //  }
    //  if (i==myNodes.length && found == false) {
    //    unique = true;
    //  }
    //}
    // // var curMaxNode = myNodes[myNodes.length-1]
    var newX = width/2 + Math.floor((Math.random()-0.5)*width*0.90);
    var newY = height/2 + Math.floor((Math.random()-0.5)*height*0.90);
    var newNode = {};
    newNode.id=guid;
    newNode.qualifiedName=name;
    newNode.x=newX;
    newNode.y=newY;
  }
  console.log("new node: "+newNode.qualifiedName+":"+newNode.id+":"+newNode.x+","+newNode.y);
  // Status update
  //d3.select("body").select("#status")
  //    .select("#text")
  //    .text("New node id"+newNode.id+":"+newNode.x+","+newNode.y);
  myNodes.push( newNode );
}


function createLinkedNode(guid,end1Guid,end2Guid) {
  end1 = getNode(end1Guid);
  console.log('end 1 is '+end1);
  end2 = getNode(end2Guid);
  console.log('end 2 is '+end2);

  //if (myNodes.length == 0) {
  //  createNode();
  //}
  //else {
  //  createNode();
  //  // pick random node by index
  //  var rndIdx = Math.floor(Math.random() * myNodes.length);
  //  var newIdx = myNodes.length-1;
  //  // Get the node ids
  var sourceId = end1Guid;
  var targetId = end2Guid;
  newLink = {}; newLink.id=guid; newLink.source=sourceId; newLink.target=targetId;
  //}
  myLinks.push( newLink );
}

function removeNode() {
  destroyNode();
  d3update();
}
  
function destroyNode() {
  // pick random node  index
  var rndIdx = Math.floor(Math.random() * myNodes.length);
  //appendStatus("Remove node idx "+rndIdx);
  // Get the node id 
  var rndId = myNodes[rndIdx].id;
  //appendStatus("Remove node id "+rndId);
  // remove any links involving this node
  if (myLinks.length > 0) {
    var i=0;
    do {
      thisLink = myLinks[i];
      //appendStatus("Link index "+i);
      if (thisLink.source.id == rndId || thisLink.target.id == rndId) {
        // Status update
        //appendStatus("Remove link "+thisLink.source.id+"->"+thisLink.target.id);

        myLinks.splice(i,1);
      }
      else {
        i=i+1;
      }
    } while(i<myLinks.length);
  }
  // Status update
  //appendStatus("Remove node idx "+rndIdx+" id "+rndId);
  //d3.select("body").select("#status")
  //    .select("#text")
  //    .text("Remove node idx "+rndIdx+" id "+rndId);

  myNodes.splice(rndIdx,1);
  d3update();
}

function removeLink() {
  var lastIndex = myLinks.length-1;
  myLinks.splice(lastIndex,1);
  d3update();
}

//function appendStatus(s) {
//    var statusArea = d3.select("body").select("#status").select("#text")
//    var existingStatus = statusArea.text();
//    statusArea.text(existingStatus + " " + s);

    // Check status is always scrolled to the bottom
    //el.scrollTop = el.scrollHeight - el.clientHeight;

//}


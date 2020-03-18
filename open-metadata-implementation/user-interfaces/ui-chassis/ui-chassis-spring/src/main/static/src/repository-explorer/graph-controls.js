/* SPDX-License-Identifier: Apache-2.0 */

/* Copyright Contributors to the ODPi Egeria project. */


import { mixinBehaviors } from "../../node_modules/@polymer/polymer/lib/legacy/class.js";
import { AppLocalizeBehavior } from "../../node_modules/@polymer/app-localize-behavior/app-localize-behavior.js";
import { PolymerElement, html } from "../../node_modules/@polymer/polymer/polymer-element.js";
import '../shared-styles.js';
import '../token-ajax.js';
import './traversal-filters.js';
import './traversal-history.js';

/**
*
* GraphControls is the implementation of a web component for enabling the user to manipulate the displayed graph.
*
* It includes controls to Explore (get the neighborhood), to Undo (the last operation), Clear the graph and
* report the History of the user's exploration (since the start or the last clear operation).
*
* It is anticipated that the graph controls will be displayed across the top of the page (above the diagram manager).
*/

class GraphControls extends PolymerElement {

    static get template() {
        return html`

            <style include="rex-styles">

            </style>

            <body>

                <token-ajax id="doPreTraversalAjaxId" last-response="{{lastPreTraversalResp}}" ></token-ajax>
                <token-ajax id="doTraversalAjaxId" last-response="{{lastTraversalResp}}" ></token-ajax>


                <div style="width=400px; height:80px; position:relative; top:0px; left:0px; padding:0px 20px; text-align:center;">


                    <!-- Not supporting depth of more than 1 yet - it would work but for type filtering there is no             -->
                    <!--          easy way to know which types were encountered at various depths from the root. We could       -->
                    <!--          just display all the types within the blast radius, but it would be preferable to allow       -->
                    <!--         the user to alter the depth and to update the types encoutnered within the new depth.          -->
                    <!--          encountered within the current depth from the focus entity - and ideally as the user alters   -->
                    <!--          For now, sticking to depth 1 to keep things simple.                                           -->
                    <!--          The types to include in the filtering lists are known from the pre-traversal.                 -->
                    <!--                                                                                                        -->
                    <!-- <select id="depthSelector" style="width: 300px; float:right;  left: 150px"                             -->
                    <!--            on-change="depthSelectorHandler">                                                           -->
                    <!--               /* <option value="0"          >Just the entity</option> */                               -->
                    <!--               <option value="1"  selected >Meet the neighbours</option>                                -->
                    <!--               <option value="2"  disabled >Invite the Street</option>                                  -->
                    <!--               <option value="3"  disabled >Party time!</option>                                        -->
                    <!-- </select>                                                                                              -->

                    <p>
                       Traversal count : {{currentGen}}
                    </p>

                    <paper-button
                        class="inline-element"
                        style="padding:10px; text-align:center; text-transform:none;"
                        id = "exploreButton"
                        raised
                        on-click="doPreTraversal">
                        Explore
                    </paper-button>

                    <paper-button
                        class="inline-element"
                        style="padding:10px; text-transform:none; text-align:center;"
                        id = "undoButton"
                        raised
                        on-click="doUndo">
                        Undo
                    </paper-button>

                    <paper-button
                        class="inline-element"
                        style="padding:10px; text-align:center; text-transform:none;"
                        id = "clearButton"
                        raised
                        on-click="doClear">
                        Clear
                    </paper-button>

                    <paper-button
                        class="inline-element"
                        style="padding:10px; text-align:center; text-transform:none;"
                        id = "historyButton"
                        raised
                        on-click="showHistory">
                        History
                    </paper-button>

                </div>

                <!--Traversal Filters dialog - initially hidden and made visible when pre-traversal stats are received. -->
                <div>
                        <paper-dialog id="traversalFiltersDialog" style=" height:500px; width:800px; ">

                            <p>
                            To restrict the traversal to specific types, set the traversal filters below.
                            <p>
                            For each category (column):
                            <ul style="padding: 0px 40px;">
                            <li>If NO types are checked, there is no filtering for the category. All types are permitted.
                            <li>If ANY (or all) types are checked, the traversal will be restricted to the checked types.
                            </ul>
                            <p>
                            A neighboring entity can be reached if its entity type is permitted, it has one or more of any
                            required classifications and the connecting relationship type is permitted.
                            </p>

                            <!--   Display 3 things per type included in the pre-traversal stats
                              --   1. The type category and name
                              --   2. The count of instances of this type
                              --   3. A checkbox to include/exclude from the traversal
                              -->

                            <div id="containerForTraversalFilters">
                            </div>

                            <div class="buttons">
                                <paper-button dialog-dismiss>Cancel</paper-button>
                                <paper-button dialog-confirm autofocus on-tap="_filterSubmitHandler">OK</paper-button>
                            </div>

                        </paper-dialog>
                </div>  <!-- END OF TRAVERSAL FILTERS DIALOG -->


                <!--History dialog - initially hidden and made visible when History! is pressed. -->
                <div>
                        <paper-dialog id="historyDialog" style=" height:450px;width:900px; ">

                            <!--This section is used to fetch the input from the input-field and display on the dialog using one-way data binding-->

                            <p>
                            Summary of traversals since the graph was last cleared.
                            </p>

                            <!--   Display 3 things per gen in the traversal history                   -->
                            <!--   1. The gen number                                                   -->
                            <!--   2. The server and a summary of the query that was performed         -->
                            <!--   3. The labels, guids of the entities and relationships returned     -->
                            <!--                                                                       -->

                            <div id="containerForHistory">
                            </div>

                            <div class="buttons">
                                <paper-button dialog-confirm autofocus on-tap="_historyDismissHandler">Dismiss</paper-button>
                            </div>

                        </paper-dialog>
                </div>   <!-- END OF HISTORY DIALOG -->

            </body>
        `; }



    static get properties() {

        return {

            // Delegate everything to the instanceRetriever

            instanceRetriever : {
                type : Object,
                value : {}
            },

            connectionManager: Object,

            // Depth control to be used for the next query
            //intDepth: {
            //    type   : Number,
            //    value  : 1            // default to getting immediate neighbours
            //},

             currentGen: {
                 type: Number,
                 value: 0
             },

             lastPreTraversalResp: {
                type: Object,
                observer: '_preTraversalRespChanged'    // Observer called when this property changes
             },


              lastTraversalResp: {
                             type: Object,
                             observer: '_traversalRespChanged'    // Observer called when this property changes
                          },


            // The preTraversal contains the stats retrieved from the initial (pre-) traversal query.
            // This consists of a map of 3 maps (entityTypes, relationshipTypes, classificationTypes)
            // Each category map is a map keyed by by a type/classification name, whose value is:
            //   {   count    : <number of instances of this type>  ,
            //       include  : true | false (default)  ,
            //   }
            preTraversal: {
                 type : Object,
                 value : undefined
            }


        };
    }

    /*
     * Element is ready
     */
    ready() {
        // Ensure you call super.ready() first to initialise node hash...
        super.ready();

        this.preTraversal                     = {};
        this.preTraversal.entityTypes         = {};
        this.preTraversal.relationshipTypes   = {};
        this.preTraversal.classificationTypes = {};

    }


    // UI handlers


    // Inter-component event handlers


    /*
     * On receipt of pre traversal result, this function will launch a dialog box with the type filters
     * including names, stats and selection controls
     */
    inEvtPreTraversalLoaded() {
        // Clear the filters from the dialog - we want to build a new one from the template...
        var containerForTraversalFilters = this.$.containerForTraversalFilters;
        while (containerForTraversalFilters.firstChild) {
            containerForTraversalFilters.removeChild(containerForTraversalFilters.firstChild);
        }
        // Now add a new set of traversal filters to the container....
        var traversalFilters                 = document.createElement("traversal-filters");
        traversalFilters.entityTypes         = this.preTraversal.entityTypes;
        traversalFilters.relationshipTypes   = this.preTraversal.relationshipTypes;
        traversalFilters.classificationTypes = this.preTraversal.classificationTypes;
        containerForTraversalFilters.appendChild(traversalFilters);
        this.$.traversalFiltersDialog.open();
    }

    inEvtGraphExtended() {
        /*
         * Opportunity to update the traversal count
         */
        this.currentGen = this.instanceRetriever.getCurrentGen();
    }

    inEvtGraphCleared() {
        /*
         * Opportunity to update the traversal count
         */
        this.currentGen = this.instanceRetriever.getCurrentGen();
    }

    inEvtGraphReduced() {
        /*
         * Opportunity to update the traversal count
         */
        this.currentGen = this.instanceRetriever.getCurrentGen();
    }

     outEvtPreTraversalLoaded() {
         var customEvent = new CustomEvent('pre-traversal-loaded', {
             bubbles: true, composed: true,
             detail: {source: "graph-controls"}  });
         this.dispatchEvent(customEvent);
     }



    _filterSubmitHandler() {
        /*
         * This function is called from the filter selection dialog so it needs to access the updated filter settings
         * and formulate a fresh traversal request using the filter settings the user has specified.
         *
         * The filter lists operate as follows:
         * If a category has NO types selected - then the caller wants all possible types included; so generate an empty list (which
         * will become null at the server side. No filtering will be performed.
         * If a category has SOME types selected - then the caller wants only those types included; so generate a non-empty list.
         * If a category has ALL types selected - then the caller wants no filtering of types included; in this case generate an
         * empty list as in the case for NO types selected.
         *
         */


        // Get the user's selected filters from the traversal-filters element..
        var containerForTraversalFilters = this.$.containerForTraversalFilters;
        var traversalFilters             = containerForTraversalFilters.firstChild;

        // Process entity type filters...
        var entityTypeGUIDs = [];
        var selEntTypes = traversalFilters.getSelectedEntityTypes();
        if (selEntTypes !== undefined && selEntTypes !== null && selEntTypes.length > 0) {
            selEntTypes.forEach(function(entry) {
                entityTypeGUIDs.push(entry.guid);
            });
        }

         // Process relationship type filters...
         var relationshipTypeGUIDs = [];
         var selRelTypes = traversalFilters.getSelectedRelationshipTypes();
         if (selRelTypes !== undefined && selRelTypes !== null && selRelTypes.length > 0) {
             selRelTypes.forEach(function(entry) {
                 relationshipTypeGUIDs.push(entry.guid);
             });
         }

         // Process classification name filters...
         var classificationNames = [];
         var selClsTypes = traversalFilters.getSelectedClassificationTypes();
         if (selClsTypes !== undefined && selClsTypes !== null && selClsTypes.length > 0) {
             selClsTypes.forEach(function(entry) {
                 classificationNames.push(entry.name);
             });
         }


        // Traversal always works from the current focus entity.
        // We need to provide it with the filter lists....
        this.doTraversal(entityTypeGUIDs,relationshipTypeGUIDs, classificationNames );
    }


    /*
     * This function needs to build the history summary and render the history dialog
     * Launch a dialog box with the history including list of gen, query and results
     */
    showHistory() {

        // Clear any existing history from the dialog - we want to build a new one from the template...
        var containerForHistory = this.$.containerForHistory;
        while (containerForHistory.firstChild) {
            containerForHistory.removeChild(containerForHistory.firstChild);
        }

        // Now add the latest history to the container....
        var traversalHistory = document.createElement("traversal-history");

        /*
         * Delegate to the instance-retriever to construct the history from the gens
         */
        traversalHistory.historyList = this.instanceRetriever.getHistoryList();

        if (traversalHistory.historyList.length > 0) {

            containerForHistory.appendChild(traversalHistory);
            this.$.historyDialog.open();

        }
        else {
            alert("There is currently no history to report - please retrieve metadata optionally perform traversals.");
        }

    }

    _historyDismissHandler() {
        // This function is called from the history dialog and is a NO OP - there
        // is no action to take on dismiss.
    }


    doPreTraversal() {

        var focusGUID     = this.instanceRetriever.getFocusGUID();
        var focusCategory = this.instanceRetriever.getFocusCategory();

        if (focusCategory !== 'Entity') {
            alert("Cannot explore from a relationship - please select an entity");
            return;
        }

        if (focusGUID === undefined || focusGUID === null) {
            alert("Cannot explore cannot proceed because there is no focus instance - please load or select an entity first");
            return;
        }

        var serverDetails = this.connectionManager.getServerDetails();
        if (this.validate(serverDetails.serverName) && this.validate(serverDetails.serverURLRoot)) {
            var body = {};
            body.serverName       = serverDetails.serverName;
            body.serverURLRoot    = serverDetails.serverURLRoot;
            body.enterpriseOption = serverDetails.enterpriseOption;
            body.entityGUID       = focusGUID;
            body.depth            = "1";  /* deliberately hard-coded */

            this.doPreTraversalAjax(body);

        }
    }

    /*
     * This function will clear the whole graph, and the selected instance.
     */
    doClear() {

        /*
         * Delegate this to the instance-retriever
         */
         this.outEvtGraphCleared();
    }


    validate(parameter) {
        if (parameter === undefined || parameter === null || parameter === "" || parameter.length <= 0)
            return false;
        return true;
    }

    /*
     * This function will undo the last operation on the graph.
     */
    doUndo() {

        this.outEvtUndo();
    }

    outEvtUndo() {

        var customEvent = new CustomEvent('undo',
            { bubbles: true, composed: true,
              detail: {source: "graph-controls"} } );
        this.dispatchEvent(customEvent);
    }

    outEvtGraphCleared() {

        var customEvent = new CustomEvent('graph-cleared',
            { bubbles: true, composed: true,
              detail: {source: "graph-controls"} } );
        this.dispatchEvent(customEvent);
    }



     /*
      * The pre-traversal asks the view-service to get the types of relationship and neighboring entity adjacent to
      * the focus entity. It returns traversal statistics that are used in the display of the user's choices for filtering
      * of the real traversal, if the user chooses to proceed. The user can adjust the filters and either cancel or
      * submit the request.
      */
     doPreTraversalAjax(body) {
         // Pre-Traversal - this is a post operation to the UI server
         this.$.doPreTraversalAjaxId.method ="post";
         this.$.doPreTraversalAjaxId.body   = body;
         this.$.doPreTraversalAjaxId.url    = "/api/instances/rex-pre-traversal";
         this.$.doPreTraversalAjaxId._go();
     }



     /*
      * This function requests a traversal of the server's repository with the filters specified in list parameters
      */
     doTraversal(entityTypeGUIDs, relationshipTypeGUIDs, classificationNames) {

         var focusGUID      = this.instanceRetriever.getFocusGUID();
         var focusCategory  = this.instanceRetriever.getFocusCategory();
         var currentGen     = this.instanceRetriever.getCurrentGen();

         if (focusCategory !== 'Entity') {
             alert("Cannot explore from a relationship - please select an entity");
             return;
         }

         if (focusGUID === undefined || focusGUID === null) {
             alert("Cannot explore cannot proceed because there is no focus instance - please load or select an entity first");
             return;
         }


         var serverDetails = this.connectionManager.getServerDetails();
         if (this.validate(serverDetails.serverName) && this.validate(serverDetails.serverURLRoot)) {
             var body = {};
             body.serverName       = serverDetails.serverName;
             body.serverURLRoot    = serverDetails.serverURLRoot;
             body.enterpriseOption = serverDetails.enterpriseOption;
             body.entityGUID       = focusGUID;
             body.depth            = 1;    /* deliberately hard-coded */
             body.gen              = currentGen;
             // Set the filter lists...
             body.entityTypeGUIDs          = entityTypeGUIDs;
             body.relationshipTypeGUIDs    = relationshipTypeGUIDs;
             body.classificationNames      = classificationNames;

             this.doTraversalAjax(body);

         }
     }

     /*
      * The pre-traversal is a visit to the VS to get the types of relationship and neighboring entity adjacent to
      * the focus entity.
      * The pre-traversal results in a traversalStats object that is displayed, the filters/depth adjusted (not yet)
      * and the user can finally submit the request.
      */
     doTraversalAjax(body) {
         // RexTraversal - this is a post operation to the UI server
         this.$.doTraversalAjaxId.method ="post";
         this.$.doTraversalAjaxId.body   = body;
         this.$.doTraversalAjaxId.url    = "/api/instances/rex-traversal";
         this.$.doTraversalAjaxId._go();
     }




    validate(parameter) {
        if (parameter === undefined || parameter === null || parameter === "" || parameter.length <= 0)
            return false;
        return true;
    }



    /*
     * Observer to handle receipt of packaged instance data response from UI Application
     */
    _preTraversalRespChanged(newValue,oldValue) {

        if (newValue !== undefined && newValue !== null) {

            if (newValue.httpStatusCode == 200) {
                // Success

                // This block provides the ability to inject test type data at this point...
                var injectTestData = false;
                if (injectTestData) {
                    this.injectTestData();
                }
                else {

                    // Unpack the RexPreTraversal fields
                    //    private String                    entityGUID;                    // must be non-null
                    //    private Map<String,RexTypeStats>  entityInstanceCounts;          // a list of type guids or null
                    //    private Map<String,RexTypeStats>  relationshipInstanceCounts;    // a list of type guids or null
                    //    private Map<String,RexTypeStats>  classificationInstanceCounts;  // a list of names or null
                    //    private Integer                   depth;
                    var rexPreTraversal = newValue.rexPreTraversal;
                    var depth = rexPreTraversal.depth;
                    var entityGUID = rexPreTraversal.entityGUID;

                    // Process the entity instance stats...
                    this.preTraversal.entityTypes = [];
                    var entityInstanceCounts = rexPreTraversal.entityInstanceCounts;
                    if (entityInstanceCounts != null) {
                        for (var typeName in entityInstanceCounts) {
                            var count = entityInstanceCounts[typeName].count;
                            var typeGUID = entityInstanceCounts[typeName].typeGUID;
                            // Stash the typeName, typeGUID (and count) in this.preTraversal for later access
                            this.preTraversal.entityTypes.push( { 'name' : typeName  , 'guid' : typeGUID , 'count' : count , 'checked' : false });
                        }
                        this.preTraversal.entityTypes.sort((a, b) => (a.name > b.name) ? 1 : -1);
                    }

                    // Process the relationship instance stats...
                    this.preTraversal.relationshipTypes = [];
                    var relationshipInstanceCounts = rexPreTraversal.relationshipInstanceCounts;
                    if (relationshipInstanceCounts != null) {
                        for (var typeName in relationshipInstanceCounts) {
                            var count = relationshipInstanceCounts[typeName].count;
                            var typeGUID = relationshipInstanceCounts[typeName].typeGUID;
                            // Stash the typeName, typeGUID (and count) in this.preTraversal for later access
                            this.preTraversal.relationshipTypes.push( { 'name' : typeName, 'guid' : typeGUID  , 'count' : count , 'checked' : false });
                        }
                        this.preTraversal.relationshipTypes.sort((a, b) => (a.name > b.name) ? 1 : -1);
                    }

                    // Process the classification instance stats...
                    this.preTraversal.classificationTypes = [];
                    var classificationInstanceCounts = rexPreTraversal.classificationInstanceCounts;
                    if (classificationInstanceCounts != null) {
                        for (var typeName in classificationInstanceCounts) {
                            var count = classificationInstanceCounts[typeName].count;
                            var typeGUID = classificationInstanceCounts[typeName].typeGUID;
                            // Stash the typeName, typeGUID (and count) in this.preTraversal for later access
                            this.preTraversal.classificationTypes.push( { 'name' : typeName, 'guid' : null  , 'count' : count , 'checked' : false });
                        }
                        this.preTraversal.classificationTypes.sort((a, b) => (a.name > b.name) ? 1 : -1);
                    }
                }

                this.outEvtPreTraversalLoaded();


            }
            else {

                // Failure
                if (newValue.exceptionText) {
                    alert('Error occurred: ' +newValue.exceptionText);
                }
                else {
                    alert('Error occurred: no exception message given');
                }
                // Generate a failure to load event - this will allow the status to be reported
                var customEvent = new CustomEvent('pre-traversal-not-loaded', {
                   bubbles: true, composed: true, detail: {source: "graph-controls"}  });
                this.dispatchEvent(customEvent);
            }
        }
    }

    getPreTraversal() {
        return this.preTraversal;
    }


    /*
     * Function to invent a set of sample stats so we can test the UI/dialog:
     */
    injectTestData() {

        // Note that the types for a category are stored as a list of maps.
        // Each list is sorted on type name - this is the order it will be presented in the filter dialog
        this.preTraversal.entityTypes = [];
        this.preTraversal.entityTypes.push( { 'name' : 'beta'  , 'count' : 12 , 'checked' : true });
        this.preTraversal.entityTypes.push( { 'name' : 'alpha' , 'count' : 11 , 'checked' : false });
        this.preTraversal.entityTypes.push( { 'name' : 'gamma' , 'count' : 13 , 'checked' : false });
        this.preTraversal.entityTypes.sort((a, b) => (a.name > b.name) ? 1 : -1)

        this.preTraversal.relationshipTypes = [];
        this.preTraversal.relationshipTypes.push( { 'name' : 'father' , 'count' : 12 , 'checked' : true });
        this.preTraversal.relationshipTypes.push( { 'name' : 'mother' , 'count' : 11 , 'checked' : false });
        this.preTraversal.relationshipTypes.push( { 'name' : 'sister' , 'count' : 13 , 'checked' : false });
        this.preTraversal.relationshipTypes.sort((a, b) => (a.name > b.name) ? 1 : -1)

        this.preTraversal.classificationTypes    = [];
        this.preTraversal.classificationTypes.push( { 'name' : 'confidence' , 'count' : 12 , 'checked' : true });
        this.preTraversal.classificationTypes.push( { 'name' : 'subliminal' , 'count' : 11 , 'checked' : false });
        this.preTraversal.classificationTypes.push( { 'name' : 'woop-woop' , 'count' : 13 , 'checked' : false });
        this.preTraversal.classificationTypes.sort((a, b) => (a.name > b.name) ? 1 : -1)

    }



    /*
     * Observer to handle receipt of packaged instance data response from UI Application
     */
    _traversalRespChanged(newValue,oldValue) {

        if (newValue !== undefined && newValue !== null) {

            if (newValue.httpStatusCode == 200) {
                // Success
                var rexTraversal = newValue.rexTraversal;
                rexTraversal.operation = "traversal";
                this.instanceRetriever.processTraversal(rexTraversal);
            }

            else {

                // Failure
                if (newValue.exceptionText) {
                    alert('Error occurred: ' +newValue.exceptionText);
                }
                else {
                    alert('Error occurred: no exception message given');
                }
                // Generate a failure to load event - this will allow the status to be reported
                var customEvent = new CustomEvent('traversal-not-loaded', { bubbles: true, composed: true, detail: {source: "graph-controls"}  });
                this.dispatchEvent(customEvent);
            }
        }
    }

}

window.customElements.define('graph-controls', GraphControls);
/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
import { PolymerElement, html } from '@polymer/polymer';
import '@polymer/paper-button/paper-button';
import './shared-styles.js';

class MyView1 extends PolymerElement {
    static get template() {
        return html`
      <style include="shared-styles">
        :host {
          display: block;

          padding: 10px;
        }
      </style>

      <div class="card">
        <div class="circle">1</div>
        <h1>View Two</h1>
        <p>Ea duis bonorum nec, falli paulo aliquid ei eum.</p>
        <p>Id nam odio natum malorum, tibique copiosae expetenda mel ea.Detracto suavitate repudiandae no eum. Id adhuc minim soluta nam.Id nam odio natum malorum, tibique copiosae expetenda mel ea.</p>
      </div>
       <paper-button id="login-button" on-tap="_aTest" raised>Test</paper-button>
       <paper-icon-button on-tap="_logout" icon="exit-to-app"></paper-icon-button>
    `;
    }

    constructor(){
        super();
        console.log('pageChanged:' );
    }

}

window.customElements.define('my-view1', MyView1);

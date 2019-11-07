/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import { PolymerElement, html } from "@polymer/polymer/polymer-element.js";
import '@polymer/paper-input/paper-input.js';
import '@polymer/paper-material/paper-material.js';
import '@vaadin/vaadin-date-picker/vaadin-date-picker.js';
import '@vaadin/vaadin-time-picker/vaadin-time-picker.js';
import '../../shared-styles.js';

class DatetimeWidget extends PolymerElement {
    static get template() {
        return html`
      <style include="shared-styles">
        :host {
          display: block;
        }
      </style>

      <vaadin-date-picker placeholder="Pick a date" value ="{{dateValue}}" on-change='_handleDateChange' clear-button-visible readonly={{readonly}}>
      </vaadin-date-picker>
      <vaadin-time-picker placeholder="(hh:mm.sss)" value ="{{timeValue}}" on-change='_handleTimeChange' clear-button-visible readonly={{readonly}}>
      </vaadin-time-picker>
    `;
    }

    static get properties() {
        return {
            name: {
                type: String,
                notify: true
            },
            value: {
                type: Object,
                notify: true
            },
            dateValue: {
                type: Object,
                computed: 'computeDateValue(value)',
                // Observer called  when this property changes
                observer: '_dateValueChanged',
                notify: true
            },
            timeValue: {
                type: Object,
                computed: 'computeTimeValue(value)',
                // Observer called  when this property changes
                observer: '_timeValueChanged',
                notify: true
            },
            readonly: {
                type: Boolean,
                notify: true
            }
        };
    }
    ready(){
        super.ready();
    }
    computeDateValue(val) {
       var computedDate;
       if (val) {
           computedDate = this.getDateFromValue(val);
       }
       return computedDate;
    }
    computeTimeValue(val) {
       var computedTime;
       if (val) {
           computedTime = this.getTimeFromValue(val)
       }
       return computedTime;
    }
    _dateValueChanged(newDate,oldDate) {
        this.value = newDate + "T" + this.getTimeFromValue(this.value);
    }
    _timeValueChanged(newTime,oldTime) {
       this.value =this.getDateFromValue(this.value) + "T" + newTime;
    }
    _handleDateChange(newDate) {
       this.value = newDate + "T" + this.getTimeFromValue(this.value);
    }
    _handleTimeChange(newTime) {
       this.value =this.getDateFromValue(this.value) + "T" + newTime;
    }
    getTimeFromValue(val) {
       var vaadimTimeVal;
       if (val) {
            // grab the time
            var timeVal =   val.split(/T(.+)/)[1];
            var timepieces = timeVal.split(":");
            // get the first and second pieces split by colon and create the time in the required format hh:mm
            vaadimTimeVal =timepieces[0] + ":" + timepieces[1];
            console.log("vaadimTimeVal is " + vaadimTimeVal);
       }
       return vaadimTimeVal;
    }
    getDateFromValue(val) {
       var dateVal;
       if (val) {
          dateVal = val.split(/T(.+)/)[0];
          console.log("date Val is " + dateVal);
       }
       return dateVal;
    }

}

window.customElements.define('datetime-widget', DatetimeWidget);
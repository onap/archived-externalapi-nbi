/**
 *     Copyright (c) 2018 Orange
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package org.onap.nbi.apis.hub.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;


@Document
public class EventSubscription {

    @Id
    private String id;

    private String callback;

    private String query;

    private String eventType;

    public EventSubscription(){

    }

    public EventSubscription(String id, String callback, String query, String eventType) {
        this.id = id;
        this.callback = callback;
        this.query = query;
        this.eventType = eventType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventSubscription that = (EventSubscription) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(callback, that.callback) &&
                Objects.equals(query, that.query) &&
                Objects.equals(eventType, that.eventType);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, callback, query, eventType);
    }
}

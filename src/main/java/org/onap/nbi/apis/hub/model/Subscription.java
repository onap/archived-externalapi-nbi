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

import java.util.stream.Collectors;
import org.onap.nbi.commons.Resource;

public class Subscription implements Resource{


    private String id;

    private String callback;

    private String query;
    private String ewId;
    private String ewHost;

    public void setEwHost(String ewHost) {
        this.ewHost = ewHost;
    }

    public String getEwId() {
        return ewId;
    }

    public void setEwId(String ewId) {
        this.ewId = ewId;
    }



    public String getEwHost() {
        return ewHost;
    }

    public Subscription(){

    }

    public Subscription(String id, String callback, String query) {
        this.id = id;
        this.callback = callback;
        this.query = query;
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

    public static Subscription createFromSubscriber(Subscriber subscriber) {
        Subscription sub = new Subscription();
        sub.setId(subscriber.getId());
        sub.setCallback(subscriber.getCallback());
        sub.setEwId( subscriber.getEwId());
        sub.setEwHost( subscriber.getEwHost());


        String query = subscriber.getQuery().entrySet()
                .stream()
                .map(entry -> entry.getKey()+ "=" + String.join(" ",entry.getValue()))
                .collect(Collectors.joining());

        sub.setQuery(query);
        return sub;
    }
}

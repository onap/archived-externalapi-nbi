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

import org.onap.nbi.commons.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


@Document
public class Subscriber implements Resource {
    private static final Logger logger = LoggerFactory.getLogger(Subscriber.class);

    @Id
    private String id;
    private String callback;

    public String getEwId() {
        return ewId;
    }

    public void setEwId(String ewId) {
        this.ewId = ewId;
    }

    private String ewId;

    public String getEwHost() {
        return ewHost;
    }

    public void setEwHost(String ewHost) {
        this.ewHost = ewHost;
    }

    private String ewHost;

    private Map<String, String[]> query = new HashMap<>();

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

    public Map<String, String[]> getQuery() {
        return query;
    }

    public static Subscriber createFromSubscription(Subscription request) {
        Subscriber sub = new Subscriber();
        sub.setCallback(request.getCallback());
        sub.setEwId( request.getEwId());
        sub.setEwHost( request.getEwHost());

        Stream.of(request.getQuery().split("&"))
                .map(q -> q.split("="))
                .filter(q -> q.length == 2)
                .forEach(q -> sub.getQuery().put(q[0].trim(), q[1].trim().split(",")));

        return sub;
    }
}

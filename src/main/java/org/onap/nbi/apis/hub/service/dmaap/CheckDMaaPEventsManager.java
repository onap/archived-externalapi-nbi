/**
 * Copyright (c) 2019 Huawei
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.onap.nbi.apis.hub.service.dmaap;

import java.io.IOException;
import java.net.URI;
import java.text.MessageFormat;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.commons.lang3.StringUtils;
import org.onap.nbi.OnapComponentsUrlPaths;
import org.onap.nbi.apis.hub.model.Event;
import org.onap.nbi.apis.hub.model.EventType;
import org.onap.nbi.apis.hub.model.ServiceInstanceEvent;
import org.onap.nbi.apis.hub.repository.SubscriberRepository;
import org.onap.nbi.apis.hub.service.EventFactory;
import org.onap.nbi.apis.hub.service.NotifierService;
import org.onap.nbi.apis.serviceorder.model.RelatedParty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CheckDMaaPEventsManager {

  public static final String RESPONSE_STATUS = "response status : ";
  public static final String RETURNS = " returns ";
  public static final String ERROR_ON_CALLING = "error on calling ";
  private final Logger logger = LoggerFactory.getLogger(CheckDMaaPEventsManager.class);
  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private SubscriberRepository subscriberRepository;
  @Autowired
  private NotifierService notifier;
  @Value("${dmaap.host}")
  private String dmaapHostname;
  @Value("${dmaap.aai.topic}")
  private String aaiTopic;
  @Value("${dmaap.sdc.topic}")
  private String sdcTopic;
  @Value("${dmaap.consumergroup}")
  private String consumerGroup;
  @Value("${dmaap.consumerid}")
  private String consumerId;
  @Value("${dmaap.timeout}")
  private String timeout;
  private String dmaapGetEventsUrl;

  @PostConstruct
  private void setUpAndLogDMaaPUrl() {
    dmaapGetEventsUrl = new StringBuilder().append(dmaapHostname)
        .append(OnapComponentsUrlPaths.DMAAP_CONSUME_EVENTS).toString();
    logger.info("DMaaP Get Events url :  " + dmaapGetEventsUrl);
  }

  public void checkForDMaaPAAIEvents() {
    ObjectMapper mapper = new ObjectMapper();
    List<String> dmaapResponse = callDMaaPGetEvents(aaiTopic);
    if (!CollectionUtils.isEmpty(dmaapResponse)) {
      for (int i = 0; i < dmaapResponse.size(); i++) {
        String aaiEventString = dmaapResponse.get(i);
        if (logger.isDebugEnabled()) {
          logger.debug("aai event returned was {}", aaiEventString);
        }
        try {
          JsonNode jsonNode = mapper.readValue(aaiEventString, JsonNode.class);
          JsonNode eventHeader = jsonNode.get("event-header");
          String aaiEventEntityType = eventHeader.get("entity-type").asText();
          String action = eventHeader.get("action").asText();
          if (logger.isDebugEnabled()) {
            logger.debug("aaiEventEntityType is {} and action is {}", aaiEventEntityType, action);
          }
          if (aaiEventEntityType.equals("service-instance")) {
            {
              // parse the AAI-EVENT service-instance tree
              ServiceInstanceEvent serviceInstanceEvent = new ServiceInstanceEvent();
              RelatedParty relatedParty = new RelatedParty();
              JsonNode entity = jsonNode.get("entity");
              relatedParty.setId(entity.get("global-customer-id").asText());
              relatedParty.setName(entity.get("subscriber-name").asText());
              serviceInstanceEvent.setRelatedParty(relatedParty);
              JsonNode childServiceSubscription = entity.get("service-subscriptions");
              JsonNode serviceSubscriptions = childServiceSubscription.get("service-subscription");
              JsonNode serviceSubscription = serviceSubscriptions.get(0);
              String serviceSubscriptionPrint = serviceSubscription.toString();
              JsonNode childserviceInstances = serviceSubscription.get("service-instances");
              JsonNode serviceInstances = childserviceInstances.get("service-instance");
              JsonNode serviceInstance = serviceInstances.get(0);
              serviceInstanceEvent.setId(serviceInstance.get("service-instance-id").asText());
              serviceInstanceEvent
                  .setHref("service/" + serviceInstance.get("service-instance-id").asText());
              if (serviceInstance.get("orchestration-status") != null) {
                serviceInstanceEvent.setState(serviceInstance.get("orchestration-status").asText());
              }
              if (action.equals("CREATE")) {
                if (logger.isDebugEnabled()) {
                  logger.debug("sending service inventory event to listeners");
                }
                processEvent(
                    EventFactory.getEvent(EventType.SERVICE_CREATION, serviceInstanceEvent));
              } else if (action.equals("DELETE")) {
                processEvent(EventFactory.getEvent(EventType.SERVICE_REMOVE, serviceInstanceEvent));
              } else if (action.equals("UPDATE")) {
                processEvent(EventFactory.getEvent(EventType.SERVICE_ATTRIBUTE_VALUE_CHANGE,
                    serviceInstanceEvent));
              }

            }

          }

        } catch (JsonParseException e) {
          logger.error(" unable to Parse AAI Event JSON String {}, exception is", aaiEventString,
              e.getMessage());
        } catch (JsonMappingException e) {
          logger.error(" unable to Map AAI Event JSON String {} to Java Pojo, exception is",
              aaiEventString, e.getMessage());
        } catch (IOException e) {
          logger.error("IO Error when parsing AAI Event JSON String {} ", aaiEventString,
              e.getMessage());
        }
      }
    }
  }

  public void checkForDMaaPSDCEvents() {
    List<String> dmaapResponse = callDMaaPGetEvents(sdcTopic);
    if (!CollectionUtils.isEmpty(dmaapResponse)) {
      for (int i = 0; i < dmaapResponse.size(); i++) {
        String sdcEventString = dmaapResponse.get(i);
        if (logger.isDebugEnabled()) {
          logger.debug("sdc event returned was {}", sdcEventString);
        }
        processEvent(EventFactory.getEvent(EventType.SDC_DISTRIBUTION, sdcEventString));
      }
    }
  }

  public List<String> callDMaaPGetEvents(String topic) {

    URI callURI = buildRequest(topic);
    ResponseEntity<Object> response = callDMaaP(callURI);
    if (response != null) {
      return (List<String>) response.getBody();

    } else {
      return null;
    }
  }

  public ResponseEntity<Object> callCheckConnectivity() {
    URI callURI = buildRequest(null);

    ResponseEntity<Object> response =
        restTemplate.exchange(callURI, HttpMethod.GET, buildRequestHeader(), Object.class);

    if (logger.isDebugEnabled()) {
      logger.debug("CheckConnectivity response body : {} ", response.getBody().toString());
      logger.debug("CheckConnectivity response status : {}", response.getStatusCodeValue());
      logger.debug("CheckConnectivity callURI is : {} ", callURI.toString());
    }
    return response;

  }

  private URI buildRequest(String topic) {
    if (StringUtils.isEmpty(topic)) {
      topic = aaiTopic;
    }
    String dmaapGetEventsUrlFormated = dmaapGetEventsUrl.replace("$topic", topic);
    dmaapGetEventsUrlFormated = dmaapGetEventsUrlFormated.replace("$consumergroup", consumerGroup);
    dmaapGetEventsUrlFormated = dmaapGetEventsUrlFormated.replace("$consumerid", consumerId);
    dmaapGetEventsUrlFormated = dmaapGetEventsUrlFormated.replace("$timeout", timeout);
    if (logger.isDebugEnabled()) {
      logger.debug("Calling DMaaP Url : " + dmaapGetEventsUrlFormated);
    }
    UriComponentsBuilder callURI = UriComponentsBuilder.fromHttpUrl(dmaapGetEventsUrlFormated);
    return callURI.build().encode().toUri();
  }

  private ResponseEntity<Object> callDMaaP(URI callURI) {
    try {
      ResponseEntity<Object> response =
          restTemplate.exchange(callURI, HttpMethod.GET, buildRequestHeader(), Object.class);
      if (logger.isDebugEnabled()) {
        logger.debug("response body : {} ", response.getBody().toString());
        logger.debug("response status : {}", response.getStatusCodeValue());
      }
      return response;
    } catch (Exception e) {
      String message = MessageFormat.format("Exception while calling dmaap URI: {0}", callURI);
      logger.error(message);
      logger.error("Exception while calling DMaaP is {} ", e.toString());
      return null;
    }

  }

  private HttpEntity<String> buildRequestHeader() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Accept", "application/json");
    httpHeaders.add("Content-Type", "application/json");
    return new HttpEntity<>("parameters", httpHeaders);
  }

  /**
   * Retrieve subscribers that match an event and fire notification asynchronously
   */
  private void processEvent(Event event) {
    subscriberRepository.findSubscribersUsingEvent(event).forEach(sub -> notifier.run(sub, event));
  }

}

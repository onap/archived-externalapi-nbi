/**
 * Copyright (c) 2018 Orange
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.onap.nbi.apis.hub.service;

import com.google.common.collect.Lists;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import org.onap.nbi.apis.hub.model.Subscriber;
import org.onap.nbi.apis.hub.model.Subscription;
import org.onap.nbi.apis.hub.repository.SubscriberRepository;
import org.onap.nbi.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

@Service
public class SubscriptionService {

    @Autowired
    SubscriberRepository subscriberRepository;

    public Optional<Subscriber> findSubscriptionById(String subscriptionId) {
        return subscriberRepository.findById(subscriptionId);
    }

    public Subscriber createSubscription(Subscription subscription) {
        subscription.setId(null);
        Subscriber subscriber = Subscriber.createFromSubscription(subscription);
        if (isSubscriberAlreadyExisting(subscriber)) {
            String message = MessageFormat
                .format("subscription with callback {0} and query {1} already exists", subscription.getCallback(),
                    subscription.getQuery());
            ObjectError error = new ObjectError("subscription", message);
            List<ObjectError> errors = Lists.newArrayList(error);
            throw new ValidationException(errors);
        } else {
            return subscriberRepository.save(subscriber);
        }
    }

    private boolean isSubscriberAlreadyExisting(Subscriber subscriber) {
        Example<Subscriber> subscriberExample = Example.of(subscriber);
        Optional<Subscriber> subscriberAlreadyExisting = subscriberRepository.findOne(subscriberExample);
        return subscriberAlreadyExisting.isPresent();
    }

    public void deleteSubscription(String subscriptionId) {
        subscriberRepository.deleteById(subscriptionId);
    }

    public void deleteAll() {
        subscriberRepository.deleteAll();
    }

    public long countSubscription() {
        return subscriberRepository.count();
    }

}

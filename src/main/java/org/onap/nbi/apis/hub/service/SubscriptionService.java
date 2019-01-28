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
package org.onap.nbi.apis.hub.service;

import java.util.Optional;
import org.onap.nbi.apis.hub.model.Subscriber;
import org.onap.nbi.apis.hub.model.Subscription;
import org.onap.nbi.apis.hub.repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

    @Autowired
    SubscriberRepository subscriberRepository;

    public Optional<Subscriber> findSubscriptionById(String subscriptionId){
        return  subscriberRepository.findById(subscriptionId);
    }

    public Subscriber createSubscription(Subscription subscription){
        Subscriber sub = Subscriber.createFromSubscription(subscription);
        return subscriberRepository.save(sub);
    }

    public void deleteSubscription(String subscriptionId){
        subscriberRepository.deleteById(subscriptionId);
    }

    public void deleteAll() {
        subscriberRepository.deleteAll();
    }

    public long countSubscription(){
        return subscriberRepository.count();
    }

}

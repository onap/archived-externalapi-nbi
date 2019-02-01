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
package org.onap.nbi.test;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.StateType;
import org.onap.nbi.apis.serviceorder.repositories.ServiceOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode= ClassMode.AFTER_CLASS)
@ActiveProfiles("test")
public class ServiceOrderRepositoryTest {

    @Autowired
    ServiceOrderRepository serviceOrderRepository;


    @Before
    public void setUp() {
        ServiceOrder serviceOrder = new ServiceOrder();
        serviceOrder.setId("test");
        serviceOrder.setState(StateType.INPROGRESS);
        serviceOrderRepository.save(serviceOrder);
    }

    @Test
    public void findById() {
        Optional<ServiceOrder> optionalServiceOrderChecked = serviceOrderRepository.findById("test");
        assertTrue(optionalServiceOrderChecked.isPresent());
    }

    @Test
    public void findByState() {
        List<ServiceOrder> result = serviceOrderRepository.findByState(StateType.INPROGRESS);
        assertFalse(result.isEmpty());
    }

}

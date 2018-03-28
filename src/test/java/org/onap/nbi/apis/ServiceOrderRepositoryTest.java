package org.onap.nbi.apis;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.StateType;
import org.onap.nbi.apis.serviceorder.repositories.ServiceOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
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
        ServiceOrder result = serviceOrderRepository.findOne("test");
        assertNotNull(result);
    }

    @Test
    public void findByState() {
        List<ServiceOrder> result = serviceOrderRepository.findByState(StateType.INPROGRESS);
        assertFalse(result.isEmpty());
    }

}

package org.onap.nbi.apis.serviceorder.serviceordervalidator;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.onap.nbi.apis.serviceorder.model.ActionType;
import org.onap.nbi.apis.serviceorder.model.ServiceOrder;
import org.onap.nbi.apis.serviceorder.model.ServiceOrderItem;
import org.springframework.util.StringUtils;

public class ServiceOrderValidator implements ConstraintValidator<ValidServiceOrder, ServiceOrder> {


    @Override
    public boolean isValid(ServiceOrder serviceOrder, ConstraintValidatorContext context) {
        for (ServiceOrderItem serviceOrderItem : serviceOrder.getOrderItem()) {
            if (ActionType.ADD != serviceOrderItem.getAction()
                    && StringUtils.isEmpty(serviceOrderItem.getService().getId())) {
                return false;
            }
        }

        return true;
    }

}

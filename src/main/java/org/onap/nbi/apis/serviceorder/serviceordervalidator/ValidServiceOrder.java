package org.onap.nbi.apis.serviceorder.serviceordervalidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ServiceOrderValidator.class)
@Documented
public @interface ValidServiceOrder {
    String message() default "one of orderItem action is not add : serviceOrderItem.service.id is mandatory.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

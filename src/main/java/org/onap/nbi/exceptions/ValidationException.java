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

package org.onap.nbi.exceptions;

import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import java.util.List;

public class ValidationException extends ApiException {

    private final String messages;

    public ValidationException(List<ObjectError> listErrors) {
        super();
        StringBuilder sb = new StringBuilder();
        for (ObjectError error : listErrors) {
            if (error instanceof FieldError) {
                sb.append(((FieldError) error).getField()).append(" ").append(((FieldError) error).getDefaultMessage())
                        .append(". ");
            } else {
                sb.append(" ").append(error.getDefaultMessage()).append(". ");
            }
        }
        messages = sb.toString();

    }

    public String getMessages() {
        return messages;
    }
}

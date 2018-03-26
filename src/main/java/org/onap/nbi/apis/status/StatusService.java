package org.onap.nbi.apis.status;

import org.onap.nbi.apis.status.model.ApplicationStatus;

public interface StatusService {

    ApplicationStatus get(String serviceName, String serviceVersion);

}

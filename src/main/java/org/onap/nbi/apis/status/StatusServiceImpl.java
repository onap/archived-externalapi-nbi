package org.onap.nbi.apis.status;

import org.onap.nbi.apis.status.model.ApplicationStatus;
import org.onap.nbi.apis.status.model.StatusType;
import org.springframework.stereotype.Service;

@Service("statusService")
public class StatusServiceImpl implements StatusService {

    @Override
    public ApplicationStatus get(final String serviceName, final String serviceVersion) {

        final boolean applicationIsUp = true;


        final ApplicationStatus applicationStatus =
                new ApplicationStatus(serviceName, (applicationIsUp ? StatusType.OK : StatusType.KO), serviceVersion);


        return applicationStatus;
    }


    public boolean serviceIsUp() {
        return true;
    }

}

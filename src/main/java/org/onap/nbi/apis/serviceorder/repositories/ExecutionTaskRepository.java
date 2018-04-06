/**
 *
 *     Copyright (c) 2017 Orange.  All rights reserved.
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
package org.onap.nbi.apis.serviceorder.repositories;

import org.onap.nbi.apis.serviceorder.model.orchestrator.ExecutionTask;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface ExecutionTaskRepository extends CrudRepository<ExecutionTask, Long> {

    @Query("SELECT t FROM ExecutionTask t WHERE TRIM(t.reliedTasks) LIKE '' ")
    List<ExecutionTask> findByReliedTasksIsEmpty();

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Modifying
    @Query("UPDATE ExecutionTask t SET t.reliedTasks = REPLACE(t.reliedTasks, :internalId, '') WHERE t.reliedTasks "
            + "LIKE CONCAT('%', :internalId, '%')")
    void updateReliedTaskAfterDelete(@Param("internalId") Long internalId);

    @Query("SELECT t FROM ExecutionTask t WHERE t.reliedTasks LIKE CONCAT('%', :internalId, '%')")
    List<ExecutionTask> findTasksReliedToAnOrderItemId(@Param("internalId") Long internalId);
}

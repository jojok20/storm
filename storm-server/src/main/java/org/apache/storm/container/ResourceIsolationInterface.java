/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.storm.container;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A plugin to support resource isolation and limitation within Storm
 */
public interface ResourceIsolationInterface {
    
    /**
     * Called when starting up
     * @param conf the cluster config
     * @throws IOException on any error.
     */
    void prepare(Map<String, Object> conf) throws IOException;

    /**
     * This function should be used prior to starting the worker to reserve resources for the worker
     * @param workerId worker id of the worker to start
     * @param resources set of resources to limit
     */
    void reserveResourcesForWorker(String workerId, Map<String, Number> resources);

    /**
     * This function will be called when the worker needs to shutdown.  This function should include logic to clean up after a worker is shutdown
     * @param workerId worker id to shutdown and clean up after
     */
    void releaseResourcesForWorker(String workerId);

    /**
     * After reserving resources for the worker (i.e. calling reserveResourcesForWorker). This function can be used
     * to get the modified command line to launch the worker with resource isolation
     * @param existingCommand
     * @return new commandline with necessary additions to launch worker with resource isolation
     */
    List<String> getLaunchCommand(String workerId, List<String> existingCommand);

    /**
     * After reserving resources for the worker (i.e. calling reserveResourcesForWorker). this function can be used
     * to get the launch command prefix
     * @param workerId the of the worker
     * @return the command line prefix for launching a worker with resource isolation
     */
    List<String> getLaunchCommandPrefix(String workerId);

    /**
     * Get the list of PIDs currently in an isolated container
     * @param workerId the id of the worker to get these for
     * @return the set of PIDs, this will be combined with
     * other ways of getting PIDs. An Empty set if
     * no PIDs are found.
     * @throws IOException on any error
     */
    Set<Long> getRunningPIDs(String workerId) throws IOException;
}

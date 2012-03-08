/*
 * Copyright 2012 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.cf

import org.gradle.api.tasks.TaskAction
import org.springframework.http.HttpStatus
import org.cloudfoundry.client.lib.CloudFoundryException
import org.gradle.api.GradleException

/**
 * Tasks used to push an application on the CloudFoundry cloud.
 *
 * @author Cedric Champeau
 */
class PushApplicationCloudFoundryTask extends AbstractCreateApplicationCloudFoundryTask {

    PushApplicationCloudFoundryTask() {
        super()
        description = 'Pushes an application to the cloud'
    }

    @TaskAction
    void push() {
        ensureWarFile()
        connectToCloudFoundry()
        if (client) {
            boolean found = true
            try {
                client.getApplication(getApplication())
            } catch (CloudFoundryException e) {
                if (HttpStatus.NOT_FOUND == e.statusCode) {
                    found = false;
                } else {
                    throw new GradleException("Unable to retrieve application info from CloudFoundry",e)
                }
            }
            if (found) {
                throw new GradleException("Application '${getApplication()}' is already deployed. Use 'cf-update' instead.")
            }
            log "Creating application '${getApplication()}'"
            client.createApplication(getApplication(), getFramework(), getMemory(), getUris(), getServices())
            
            log "Deploying '${getWarFile()}'"
            client.uploadApplication(getApplication(), getWarFile())

            if (getInstances()>0) {
                log "Updating number of instances to ${getInstances()}"
                client.updateApplicationInstances(getApplication(), getInstances())
            }
            
            if (isStartApp()) {
                log "Starting '${getApplication()}'"
                client.startApplication(getApplication())
            } else {
                log "Application '${getApplication()}' has been uploaded but not started (disabled by config)"
            }
        }
    }
}

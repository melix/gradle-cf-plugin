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

import org.cloudfoundry.client.lib.CloudFoundryException
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction
import org.springframework.http.HttpStatus
import org.cloudfoundry.client.lib.CloudApplication

/**
 * Tasks used to update an application on the CloudFoundry cloud.
 *
 * @author Cedric Champeau
 */
class UpdateApplicationCloudFoundryTask extends AbstractCreateApplicationCloudFoundryTask {
    UpdateApplicationCloudFoundryTask() {
        super()
        description = 'Updates an application which is already deployed'
    }

    @TaskAction
    void update() {
        ensureWarFile()
        connectToCloudFoundry()
        if (client) {
            boolean found = true
            CloudApplication app = null
            try {
                app = client.getApplication(getApplication())
            } catch (CloudFoundryException e) {
                if (HttpStatus.NOT_FOUND == e.statusCode) {
                    found = false;
                } else {
                    throw new GradleException("Unable to retrieve application info from CloudFoundry",e)
                }
            }
            if (!found) {
                throw new GradleException("Application '${getApplication()}' is not deployed. Use 'cf-push' instead.")
            }

            checkValidMemory(app.memory)

            if (getEnvers()) {
                log 'Applying environment variables: ' + getEnvers()
                def envAsMap = app.getEnvAsMap()
                envAsMap += getEnvers()
                client.updateApplicationEnv(getApplication(), envAsMap)
            }

            log "Deploying '${getFile()}'"
            client.uploadApplication(getApplication(), getFile())

            if (getInstances()>0 && app.instances!=getInstances()) {
                log "Updating number of instances to ${getInstances()}"
                client.updateApplicationInstances(getApplication(), getInstances())
            }
            
            if (getUris() as Set != app.getUris() as Set) {
                client.updateApplicationUris(application, uris)
            }

            if (isStartApp() && app.state == CloudApplication.AppState.STOPPED) {
                log "Starting '${getApplication()}'"
                client.startApplication(getApplication())
            } else if (isStartApp() && app.state != CloudApplication.AppState.UPDATING) {
                log "Restarting '${getApplication()}"
                client.restartApplication(getApplication())
            }
        }
    }
}

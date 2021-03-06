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

/**
 * Task used to delete a service. If the service name is '*', then
 * all services are deleted.
 */
class DeleteServiceCloudFoundryTask extends AbstractCloudFoundryTask {
    String serviceName

    DeleteServiceCloudFoundryTask() {
        super()
        description = 'Deletes a service from the cloud'
    }

    @TaskAction
    void deleteService() {
        connectToCloudFoundry()
        if (client && getServiceName()) {
            if ('*' == getServiceName()) {
                log "Deleting all services"
                client.deleteAllServices()
            } else {
                log "Deleting '${getServiceName()}' service"
                client.deleteService(getServiceName())
            }
            client.logout()
        }
    }
}

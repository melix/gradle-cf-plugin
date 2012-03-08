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
import org.cloudfoundry.client.lib.CloudService
import org.cloudfoundry.client.lib.ServiceConfiguration
import org.gradle.api.GradleException

/**
 * Task used to add a service.
 *
 * @author Cedric Champeau
 */
class AddServiceCloudFoundryTask extends AbstractCloudFoundryTask {
    String application
    String serviceName
    String vendor
    String tier
    String version
    String type
    boolean bind = false

    AddServiceCloudFoundryTask() {
        super()
        description = 'Creates a service, optionally bound to an application'
    }

    @TaskAction
    void addService() {
        connectToCloudFoundry()
        if (client) {
            List<ServiceConfiguration> configs = client.serviceConfigurations
            ServiceConfiguration config = configs.find {
                it.vendor == getVendor()
            }
            if (!config) {
                throw new GradleException("No matching service vendor '${getVendor()}' found")
            }

            // ensure type matches
            String t = getType()
            if (t && t!=config.type) {
                throw new GradleException("Service type mismatch. You declared '$t' but found '${config.type}'")
            }

            if (isBind() && !getApplication()) {
                throw new GradleException("Cannot use 'bind' without an application")
            }
            
            // if not specified, pick version from service configuration
            String ver = getVersion()?:config.version

            // if name is not specified, generate a name
            String name = getServiceName()
            if (!name) {
                name = "${getVendor()}-service-${UUID.randomUUID().toString()[0..7]}"
            }
            
            // create service
            log "Provisioning ${getVendor()} service '$name'"
            client.createService(new CloudService(
                    name: name, tier: getTier(), type: config.type,
                    vendor: config.vendor, version: ver
            ))
            
            // bind if necessary
            if (isBind()) {
                log "Binding service '$name' to '${getApplication()}'"
                client.bindService(getApplication(), name)
            }
        }
    }
}

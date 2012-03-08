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

import org.cloudfoundry.client.lib.CloudFoundryClient
import org.cloudfoundry.client.lib.CloudFoundryException
import org.gradle.api.DefaultTask
import org.springframework.http.HttpStatus
import org.springframework.web.client.ResourceAccessException
import org.gradle.api.GradleException

abstract class AbstractCloudFoundryTask extends DefaultTask {
    protected CloudFoundryClient client

    String username
    String password
    String target = 'http://api.cloudfoundry.com'

    AbstractCloudFoundryTask() {
        super()
        group = 'CloudFoundry'
    }



    protected void log(msg) {
        println "CloudFoundry - $msg"
    }

    protected void connectToCloudFoundry() {
        log "Connecting to '${getTarget()}' with user '${getUsername()}'"
        CloudFoundryClient localClient = null

        try {
            localClient = new CloudFoundryClient(getUsername(), getPassword(), getTarget())
        } catch (MalformedURLException e) {
            throw new GradleException("Incorrect Cloud Foundry target url, are you sure '${getTarget()}' is correct? Make sure the url contains a scheme, e.g. http://...", e)
        }

        try {
            localClient.login()
        } catch (CloudFoundryException e) {
            if (HttpStatus.FORBIDDEN == e.statusCode) {
                throw new GroovyRuntimeException("Login failed to '${getTarget()}' using username '${getUsername()}'. Please verify your login credentials.", e)
            } else if (HttpStatus.NOT_FOUND == e.statusCode) {
                throw new GradleException("The target host '${getTarget()}' exists but it does not appear to be a valid Cloud Foundry target url.", e)
            } else {
                throw e
            }
        } catch (ResourceAccessException e) {
            throw new GradleException("Cannot access hotst at '${getTarget()}'.", e)
        } finally {
            client = localClient
        }
    }

    protected void logout() {
        try {
            client.logout()
        } finally {
            client = null
        }
    }

}

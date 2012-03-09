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
import org.gradle.api.GradleException
import org.gradle.api.tasks.TaskAction

/**
 * Task which allows adding deleting a user account.
 */
class DeleteUserCloudFoundryTask extends AbstractCloudFoundryTask {

    DeleteUserCloudFoundryTask() {
        super()
        description = 'Deletes a user account. Uses the current credentials!'
    }

    @TaskAction
    void login() {
        connectToCloudFoundry()
        if (client) {
            client.unregister()
        }
    }

    private boolean validateEmail(String email) {
        email ==~ /^[_A-Za-z0-9-+]+(\.[_A-Za-z0-9-+]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9-]+)*(\.[A-Za-z]{2,})$/
    }
}

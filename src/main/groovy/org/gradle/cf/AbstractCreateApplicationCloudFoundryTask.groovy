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

import org.gradle.api.GradleException
import org.cloudfoundry.client.lib.CloudInfo

/**
 * Base class for tasks related to the createApplication API.
 *
 * @author Cedric Champeau
 */
abstract class AbstractCreateApplicationCloudFoundryTask extends AbstractCloudFoundryTask {
    private static final ArrayList<Integer> DEFAULT_MEMORY_SIZES = [64, 128, 256, 512, 1024, 2048]

    String application
    String framework
    int memory
    List<String> uris
    List<String> services
    File warFile

    int instances = -1
    boolean startApp = true

    protected void ensureWarFile() {
        if (!getWarFile() || !getWarFile().isFile()) {
            throw new GradleException("You must specify a valid WAR file ('${getWarFile()}' is not valid)")
        }
    }

    protected void checkValidMemory(int freed = 0) {
        int requestedMemory = getMemory()
        CloudInfo info = client.cloudInfo
        if (!info.limits || !info.usage) {
            if (!(requestedMemory in DEFAULT_MEMORY_SIZES)) {
                throw new GradleException("You must choose memory size in this list $DEFAULT_MEMORY_SIZES")
            }
        }
        int available = freed+info.limits.maxTotalMemory-info.usage.totalMemory
        def valid = DEFAULT_MEMORY_SIZES.findAll { it <= available }
        if (valid.empty) {
            throw new GradleException("Memory quota exceeded!")
        }
        if (!(requestedMemory in valid)) {
            throw new GradleException("You must choose memory size in this list $DEFAULT_MEMORY_SIZES")
        }
    }
}

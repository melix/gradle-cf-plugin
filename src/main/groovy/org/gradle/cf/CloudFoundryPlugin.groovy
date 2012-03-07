package org.gradle.cf

import org.gradle.api.Project
import org.gradle.api.Plugin


class CloudFoundryPlugin implements Plugin<Project> {
    void apply(Project project) {
	println 'ok'
    }
}

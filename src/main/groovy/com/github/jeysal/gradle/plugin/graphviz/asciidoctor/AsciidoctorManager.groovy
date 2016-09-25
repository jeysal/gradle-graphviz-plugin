package com.github.jeysal.gradle.plugin.graphviz.asciidoctor

import com.github.jeysal.gradle.plugin.graphviz.node.VizSetupTask
import org.gradle.api.Project

/**
 * @author Tim Seckinger
 * @since 9/24/16
 */
class AsciidoctorManager {
    private final Project project

    AsciidoctorManager(final Project project) {
        this.project = project
    }

    /**
     * If present, tells asciidoctor where to find Graphviz.
     */
    void registerGraphviz() {
        if (project.extensions.graphvizHooks.asciidoctor.enabled) {
            def asciidoctor = project.tasks.findByName 'asciidoctor'

            asciidoctor?.dependsOn VizSetupTask.NAME
            asciidoctor?.attributes graphvizdot: project.tasks.findByName('graphviz').executablePath
        }
    }
}

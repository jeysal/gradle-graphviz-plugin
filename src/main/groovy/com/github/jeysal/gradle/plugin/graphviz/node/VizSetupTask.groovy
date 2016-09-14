package com.github.jeysal.gradle.plugin.graphviz.node

import com.moowork.gradle.node.task.NpmSetupTask
import com.moowork.gradle.node.task.NpmTask

/**
 * @author Tim Seckinger
 * @since 9/2/16
 */
class VizSetupTask extends NpmTask {
    public static final String NAME = 'vizSetup'

    public static final String VIZ_MODULE_NAME = 'viz.js-cli-wrapper'
    public static final String VIZ_MODULE_VERSION = '1.1.3'

    public VizSetupTask() {
        group = 'Node'
        description = 'Installs the viz binaries via npm'

        setNpmCommand 'install', '--silent', "$VIZ_MODULE_NAME@$VIZ_MODULE_VERSION"
        dependsOn NpmSetupTask.NAME

        // nodeModulesDir might still change
        project.afterEvaluate {
            File nodeModules = new File(project.extensions.node.nodeModulesDir as File, 'node_modules')
            outputs.files(new File(nodeModules, VIZ_MODULE_NAME),
                    new File(nodeModules, '.bin'))
        }
    }
}

package com.github.jeysal.gradle.plugin.graphviz

/**
 * Provides configuration for integration with other plugins / build steps.
 * @author Tim Seckinger
 * @since 9/25/16
 */
class GraphvizHooksExtension {
    public static final NAME = 'graphvizHooks'

    /**
     * If true, asciidoctor will automatically be told where to find the Graphviz dot executable
     * for use in asciidoctor-diagram. Defaults to true.
     */
    def asciidoctor = true
}

package com.github.jeysal.gradle.plugin.graphviz.exec

import com.github.jeysal.gradle.plugin.graphviz.GraphvizTask
import org.gradle.api.file.FileVisitDetails
import org.gradle.api.file.FileVisitor
import org.gradle.api.tasks.TaskExecutionException

/**
 * @author Tim Seckinger
 * @since 9/14/16
 */
class GraphvizSourceFileVisitor implements FileVisitor {
    private GraphvizTask graphviz

    GraphvizSourceFileVisitor(GraphvizTask graphviz) {
        this.graphviz = graphviz
    }

    @Override
    void visitDir(FileVisitDetails dirDetails) {
        // Do nothing
    }

    @Override
    void visitFile(FileVisitDetails fileDetails) {
        def source = fileDetails.relativePath.getFile(graphviz.sourceDir)
        def target = fileDetails.relativePath.getFile(graphviz.outputDir)

        graphviz.formats.forEach { format ->

            def cmd = [graphviz.executablePath,
                       '-o', target.path + ((graphviz.formatSuffix && format) ? ".$format" : ''),
                       source.path]

            if (graphviz.layout)
                cmd << '-K' << graphviz.layout
            if (format)
                cmd << '-T' << format

            def proc = cmd.execute()
            proc.consumeProcessOutputStream(System.out as OutputStream)
            proc.consumeProcessErrorStream(System.err as OutputStream)
            if (proc.waitFor()) {
                fileDetails.stopVisiting()
                throw new TaskExecutionException(graphviz, new RuntimeException("graphviz command $cmd failed"))
            }
        }
    }
}

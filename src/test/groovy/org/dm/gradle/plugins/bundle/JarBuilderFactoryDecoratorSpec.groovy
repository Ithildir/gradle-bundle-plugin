package org.dm.gradle.plugins.bundle

import aQute.bnd.osgi.Builder
import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar
import org.gradle.testfixtures.ProjectBuilder
import spock.lang.Specification

class JarBuilderFactoryDecoratorSpec extends Specification {
    Project project
    Jar jarTask
    Builder builder
    org.gradle.internal.Factory<JarBuilder> factory
    JarBuilderFactoryDecorator factoryDecorator

    def setup() {
        GroovyMock(BundleUtils, global: true)
        builder = Mock(Builder)
        project = ProjectBuilder.builder().build()
        jarTask = project.task('jar', type: Jar) as Jar
        factory = Mock(org.gradle.internal.Factory)
        factoryDecorator = new JarBuilderFactoryDecorator(jarTask, factory)
    }

    def "creates jarBuilder only once"() {
        setup:
        BundleUtils.getResources(project) >> []
        BundleUtils.getVersion(project) >> ''

        when:
        def jarBuilder1 = factoryDecorator.create()
        def jarBuilder2 = factoryDecorator.create()

        then:
        1 * factory.create() >> new JarBuilder(builder)
        jarBuilder1.is jarBuilder2
    }
}

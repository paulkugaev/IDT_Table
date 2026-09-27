// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.detekt) apply false
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        config.setFrom(files("$rootDir/config/detekt/detekt.yml"))
        buildUponDefaultConfig = true
    }
}

// === Run tests only for affected modules (PR) ===
// affectedTests: with -PchangedModules=domain,data runs unit tests for these modules plus all
// modules that depend on them transitively. Without the property, tests for all modules run.
val affectedTests = tasks.register("affectedTests") {
    group = "verification"
    description =
        "Runs unit tests for changed modules and their Gradle dependents (or all modules if changedModules is not set)."
}

gradle.projectsEvaluated {
    val modules = rootProject.allprojects.filter { it != rootProject }
    val changed = providers.gradleProperty("changedModules").orElse("").get()
        .split(",").map(String::trim).filter(String::isNotEmpty)

    // graph: moduleName -> modules that depend on it (transitively)
    val graph = mutableMapOf<String, MutableSet<String>>()
    modules.forEach { p ->
        p.configurations.forEach { cfg ->
            cfg.allDependencies.forEach { d ->
                if (d is ProjectDependency) {
                    // for a project dependency, Dependency.getName() returns the target module name
                    graph.getOrPut(d.name) { mutableSetOf() }.add(p.name)
                }
            }
        }
    }

    fun addWithDependents(name: String, into: MutableSet<String>) {
        if (!into.add(name)) return
        graph[name].orEmpty().forEach { addWithDependents(it, into) }
    }

    val affected = mutableSetOf<String>()
    changed.forEach { addWithDependents(it, affected) }

    if (affected.isEmpty()) {
        modules.forEach { p -> affectedTests.configure { dependsOn(p.tasks.named("test")) } }
    } else {
        affected.forEach { name ->
            modules.find { it.name == name }?.let { p ->
                affectedTests.configure { dependsOn(p.tasks.named("test")) }
            }
        }
    }
}

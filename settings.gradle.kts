pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "IDT Table"
include(":app")

// Auto-include modules from sources/**. Every directory containing a build.gradle.kts becomes a
// module named after its folder (e.g. :domain, :common-ui, :feature-table-impl).
fun includeRecursive(dir: File) {
    dir.walkTopDown().maxDepth(5).forEach { subDir ->
        if (isModule(subDir)) {
            val moduleName = ":${subDir.name}"
            include(moduleName)
            project(moduleName).projectDir = subDir
        }
    }
}

fun isModule(dir: File): Boolean {
    return File(dir, "build.gradle").exists() || File(dir, "build.gradle.kts").exists()
}

includeRecursive(file("sources"))

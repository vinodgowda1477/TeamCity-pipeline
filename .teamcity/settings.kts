import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2020.1"

project {

    buildType(Build)

    features {
        feature {
            id = "PROJECT_EXT_2"
            type = "sonar-qube"
            param("useToken", "true")
            param("name", "sonarqube")
            param("id", "9f20be28-5cc2-4c58-bb53-ed26f94b8707")
            param("url", "http://localhost:9000/")
            param("token", "scrambled:ODk2Y2I5MmFhMjIzNjYyMjgzY2U2MjI3ZTVlMWUzYWUyMDgxMmQ0Yg==")
        }
    }
}

object Build : BuildType({
    name = "Build"

    publishArtifacts = PublishMode.SUCCESSFUL

    params {
        param("env.JDK_1_7", """C:\Program Files\Java\jdk-14.0.2\bin""")
    }

    vcs {
        root(DslContext.settingsRoot)

        cleanCheckout = true
    }

    steps {
        step {
            name = "scan code"
            type = "sonar-plugin"
            param("target.jdk.home", "%env.JDK_14_0_x64%")
            param("sonarServer", "9f20be28-5cc2-4c58-bb53-ed26f94b8707")
        }
        maven {
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
            jdkHome = "%env.JDK_1_7%"
        }
    }

    triggers {
        vcs {
            branchFilter = ""
        }
    }
})

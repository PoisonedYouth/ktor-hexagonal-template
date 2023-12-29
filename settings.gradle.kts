rootProject.name = "ktor-hexagonal-template"

include("domain")
include("application")
include("framework")
include("common")
include("bootstrap")
include("konsist")
includeBuild("buildConfiguration")


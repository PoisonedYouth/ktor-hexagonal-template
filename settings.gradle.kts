rootProject.name = "ktor-hexagonal-template"

include("domain")
include("application")
include("infrastructure")
include("common")
include("bootstrap")
include("konsist")
includeBuild("buildConfiguration")


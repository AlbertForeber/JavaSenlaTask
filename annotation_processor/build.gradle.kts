plugins {
    id("java")
}

dependencies {
    implementation(project(":annotation")) // ← если module1 использует module2
}
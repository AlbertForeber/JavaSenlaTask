plugins {
    id("java")
    id("application") // ← ДОБАВЬТЕ ЭТУ СТРОКУ
}

application {
    mainClass.set("com.senla.app.Application")
}

dependencies {
    implementation(project(":annotation"))
    implementation(project(":annotation_processor"))
    implementation("org.postgresql:postgresql:42.7.7")
}

tasks.named<JavaExec>("run") {
    // Показать вывод приложения
    standardOutput = System.out
    errorOutput = System.err

    // Не падать сразу при ошибке
    isIgnoreExitValue = true
}
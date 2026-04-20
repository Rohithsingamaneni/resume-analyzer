import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

plugins {
    base
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

val setupVenv by tasks.registering(Exec::class) {
    group = "machine learning"
    description = "Creates a local Python virtual environment."

    outputs.dir(file("venv"))
    commandLine("python3", "-m", "venv", "venv")
}

val installDeps by tasks.registering(Exec::class) {
    group = "machine learning"
    dependsOn(setupVenv)

    inputs.file(file("requirements.txt"))
    outputs.file(file("venv/bin/activate"))

    val pipPath = if (DefaultNativePlatform.getCurrentOperatingSystem().isWindows) "venv/Scripts/pip" else "venv/bin/pip"
    commandLine(pipPath, "install", "-r", "requirements.txt")
}

val generateModel by tasks.registering(Exec::class) {
    group = "machine learning"
    dependsOn(installDeps)

    inputs.file(file("generate_model.py"))
    outputs.file(file("../src/main/resources/models/resume_ranker.onnx"))

    val pythonPath = if (DefaultNativePlatform.getCurrentOperatingSystem().isWindows) "venv/Scripts/python" else "venv/bin/python"
    commandLine(pythonPath, "generate_model.py")
}

tasks.assemble {
    dependsOn(generateModel)
}

tasks.clean {
    delete("venv")
    delete("../src/main/resources/models/resume_ranker.onnx")
}

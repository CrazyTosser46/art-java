plugins {
    id("adkProject") version "1.0"
}

group = "ru.adk"

adk {
    withSpockFramework()
    providedModules {
        applicationCore()
    }
}
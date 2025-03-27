![](https://github.com/AntonButov/code-factory/blob/trunk/codeFactorySmallIcon.png)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/antonbutov/code-factory)
![Maven Central](https://img.shields.io/maven-central/v/io.github.antonbutov/code-factory-processor)
![GitHub issues](https://img.shields.io/github/issues/antonbutov/code-factory)
![License](https://img.shields.io/github/license/antonbutov/code-factory)
![Kotlin Version](https://img.shields.io/badge/Kotlin-1.9.24-blue?logo=kotlin)
![KSP](https://img.shields.io/badge/KSP-Supported-green)
![GitHub stars](https://img.shields.io/github/stars/antonbutov/code-factory?style=social)

# Code Factory
Write your tests, and let AI generate the code.

If changes are needed, simply update the tests, and AI will regenerate the code to meet the new requirements. No more debuging.

## Setup

1. Create a `local.properties` file and add the following:
```
API_KEY="sk-proj-mSwcp..."
```
2. Kotlin (KTS)
```
repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}
```
```
plugins {
    id("com.google.devtools.ksp") version "1.9.24-1.0.20"
}
``` 
```
dependencies {
    ksp("io.github.antonbutov:code-factory-processor:$last_version")
}
```
## How it works
1. Create an interface without an implementation
```
interface ForGenerate {
    fun plus(first: Int, second: Int): Int
}
```   
2. Write a test with not implemented implementation
``` 
class GeneratedTest {

    @Test
    fun generatedFileGets() {
       val forGenerate: ForGenerate = GeneratedCode()
       val expected = forGenerate.plus(2, 2)
       assertEquals(4, expected)
    }
}
```
3. Build the module 🚀. The plugin has to generate
```
class GeneratedCode(): ForGenerate {
    override fun plus(first: Int, second: Int): Int {
    return first + second
    }
}
```
## You will see all requests to OpenAI:
```
w: [ksp] Request: ---->
You had to implement interface: ForGenerate Return only code implementation please.
interface ForGenerate {
    fun plus(
        first: Int,
        second: Int,
    ): Int
}

import kotlin.test.Test
import kotlin.test.assertEquals

class GeneratedTest {
    @Test
    fun generatedFileGets() {
        val forGenerate: ForGenerate = GeneratedCode()
        val expected = forGenerate.plus(2, 2)
        assertEquals(4, expected)
    }
}

Response: <----
    Sure, the implementation of the interface `ForGenerate` can be done as follows:

    ```kotlin
class GeneratedCode(): ForGenerate {
    override fun plus(first: Int, second: Int): Int {
    return first + second
    }
}
```
## Samples
[kotlin-code-factory-samples](https://github.com/AntonButov/kotlin-code-factory-samples.git)

## Technologies Used  
Code Factory is built with the following technologies:  
- **Test-Driven Development (TDD)** using [Kotest](https://kotest.io/) – `io.kotest:kotest`
- **AI Integration** with [OpenAI API](https://github.com/aallam/openai-kotlin) – `com.aallam.openai:openai`
- **Code Quality** enforced with [ktlint](https://github.com/pinterest/ktlint)  
- **PR Generation** enforced with [pr-agent](https://github.com/qodo-ai/pr-agenthttps://github.com/qodo-ai/pr-agent)

## Contributions Welcome! 
We follow best practices to ensure a robust and maintainable codebase. Contributions are welcome! 🚀 
We're looking for skilled contributors, especially those with expertise in AI and Kotlin. Join us in enhancing Code Factory's capabilities!

If you need OpenAI API key, write to me at butov6101@gmail.com. I can give it to you within 24 hours.

# License
```xml
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

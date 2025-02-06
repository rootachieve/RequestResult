# RequestResult
> **RequestResult** is an open-source library that aims to provide convenient **API request handling and response state management.**

## 📌 Installation
[![](https://jitpack.io/v/rootachieve/RequestResult.svg)](https://jitpack.io/#rootachieve/RequestResult)
### Gradle
```gradle
// setting.gradle.kts
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    mavenCentral()
    maven ( url = "https://jitpack.io" )
  }
}

// build.gradle.kts (modules)
implementation ("com.github.rootachieve:RequestResult:{latest_version}") 
```

### Maven
```XML
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
 	</repository>
</repositories>

<dependency>
  <groupId>com.github.rootachieve</groupId>
  <artifactId>RequestResult</artifactId>
  <version>{latest_version}</version>
</dependency>
```
## 🎨 Usage
### sealed class RequestResult
RequestResult is a sealed class that represents four states: None, Progress, Failure, and Success. It can be applied in many areas, including handling requests, managing responses, error handling, and controlling loading screens.

```kotlin
//android sample
//viewmodel
private val _dataState = MutableStateFlow<RequestResult<data>>(RequestResult.None)
val dataState: StateFlow<RequestResult<data>>
  get() = _dataState

//view
val dataState by viewmodel.dataState.collectAsStateWithLifecycle()
```
```kotlin
//--- Response
val data = dataState.getOrNull
//or
dataState.onSuccess { data ->
  //do something
}

//--- Progress
Column {
  //...
  if(dataState.isProgress){
    ProgressScreen()
  }
}

//--- Request
dataState.onNone {
  viewmodel.getSome()
}

//--- Error
dataState.onFailure {code, e ->
  when(code) {
    "404" ->
    "502" ->
    //...
  }
}

```

## 📚 Contributors

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tbody>
    <tr>
      <td align="center" valign="top" width="14.28%"><a href="https://github.com/rootachieve"><img src="https://avatars.githubusercontent.com/u/76468787?v=4?s=100" width="100px;" alt="K_Gs"/><br /><sub><b>K_Gs</b></sub></a><br /><a href="#projectManagement-rootachieve" title="Project Management">📆</a></td>
    </tr>
  </tbody>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

## 📢 Report Issues
If you encounter any issues or have any questions about the library, please feel free to open an issue.

## License
```
Copyright 2025 rootachieve.

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

# RedUtilities
RedUtilities is a library with all of my utility classes in one

How to use in my project
=====

Option 1: Manual download
-----
Download the jar file from [releases page](https://github.com/Redstonneur1256/RedUtilities/releases) and add it to your project

Option 2: Using gradle
-----

* Github download:
```groovy
ext.redUtilitiesVersion = "1.3.2"
ext.remoteDownload = { String url, String name ->
    File file = new File("$buildDir/lib/${name}")
    if (!file.exists()) {
        file.parentFile.mkdirs()
        println "${name} not found, downloading it from ${url}, please wait..."
        new URL(url).withInputStream { connectionInput ->
            file.withOutputStream { fileOutput ->
                fileOutput << connectionInput
            }
        }
        println "${name} downloaded sucessfully"
    }
    files(file.absolutePath)
}

dependencies {
    compile remoteDownload("https://github.com/Redstonneur1256/RedUtilities/releases/download/${redUtilitiesVersion}/RedUtilities.jar", "RedUtilities.jar")
}
```
* MavenLocal:
Steps:
  - Clone this repository in a folder
  - Open a cmd in this folder
  - Run the following commands when you need to update:
    - `git pull`
    - `gradlew publishMavenPublicationToMavenLocal`
  - Declare `mavenLocal()` in build.gradle repositories of your project and `compile 'fr.redstonneur1256:RedUtilities:x.y.z` in the dependencies.

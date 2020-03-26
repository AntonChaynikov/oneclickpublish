#### Setup

To import the plugin to your project:
```groovy
dependencies: {
  …
  classpath ‘com.github.AntonChaynikov.oneclickpublish:oneclickpublish:0.12.0’
  …
}
```
Apply the plugin in the app module’s Gradle config file:
```
apply plugin: ‘com.antonchaynikov.oneclickpublish’
```

Synchronize Gradle with the project’s files, new Gradle task group named ‘oneclickpublish’ should appear among the available tasks.

The group will contain 3 new tasks that will bump major, minor or patch version of your app. The versioning follows [semver](https://semver.org/) pattern.

Create or modify the main module’s gradle.properties file. The file should contain lines with the current build version name and version code:
```
appVersionName=1.0.0
appVersionCode=1000000
```
Customization — you can choose branches that you can update version from and the remote repo’s name to push the changes to. By default the branch name is ‘master’ and remote repo name is ‘origin’:
```groovy
oneClickPublish {
  branchNames = [“master”]
  remoteRepoName = “origin”
}
```
#### Usage

After the steps described above you should be able to bump the app’s version, commit changes, put git tag and push the changes  to the git repository using just one command. The plugin will increase one of the version segments and calculate the version code accordingly.

The version code is calculated by the following formula:

code = Major * 100000 + Minor * 1000 + Patch
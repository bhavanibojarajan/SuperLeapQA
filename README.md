# SuperLeapQA
UI tests for SuperLeap.

Cross-platform tests using [https://github.com/protoman92/XTestKit.git].

### Setting up ###
* Clone this repository in Intellij as a Gradle-based project.
* git submodule update --init
* Other setup steps can be found in the **XTestKit** repository.
* Once everything is set up and working properly, open **Config.java** to view configurations.
* Comment/Uncomment the appropriate Engine(s) to run the tests on **Android** and/or **iOS**.
* Test classes can be found in various packages in **com.holmusk.SuperleapQA.test**, but they can be run at the same time with **UIScreenValidationTestType** and **UILogicValidationTestType**.

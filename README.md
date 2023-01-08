#   PartyChat
##  Table of Contents
0.  [Table of Contents](#table-of-contents)
1.  [Introduction](#introduction)
2.  [Building](#building)
4.  [Usage](#usage)
3.  [License](#license)

##  Introduction
Party chat is an open-source chat application that allows individuals connected to the same network to chat without the need of internet or registration.

##  Building
1.  Clone the github repository & navigate to the local repo
```sh
git clone https://github.com/ItsMonish/PartyChat
cd PartyChat
```

2.  The project is built and managed using gradle (wrapper). Therefore, it can be easily built using
```sh
./gradlew build
```

Upon successful build, the application is present under `app/buid/libs/app.jar`

##  Usage
In order to run the application
### On Windows
Simply double-click the `.jar` file (ensure that Java Runtime 11 or above is installed)

### On Linux and other *NIXs
```sh
java -jar <PATH_TO_JAR_FILE>
```
##  License
Party chat is licensed under [BSD 3 clause license][bsd-3-clause-license]. For a detaiiled license, see [LICENSE](LICENSE)

<!-- Annotations -->
[bsd-3-clause-license]: https://opensource.org/licenses/BSD-3-Clause

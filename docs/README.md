# Documentation
Important: To not cause overhead with classpath and build-system configs, you must run a set of commands to run the program as intended:

Ensure that you are in the `ts-1-1-16/` root directory.

Use this command to clean the classes:

```shell
./gradlew clean classes
```

Then run the program (with or without arugments):

```shell
java -cp build/classes/java/main TopSecret
```
This will return a list of files (w/ corresponding #)
### OR
```shell
java -cp build/classes/java/main TopSecret 1
```
This will return the decrypted contents of file 1 with the default key.
### OR
```shell
java -cp build/classes/java/main TopSecret 1 key2.txt
```
This will return the decrypted contents of file 1 with the key in key2.txt (ciphers/key2.txt must exist).

---

# Architecture

The CLI runs the program (reads terminal input)

-> which is sent to the program controller

-> which specifies what data to retrieve from file handler

-> which sends back the requested decrypted data to the program controller

-> which sends back the output to the CLI

-> which prints the data to the terminal and ends the program

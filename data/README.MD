# Make .arr library

## 1, Types
- .aar (Android Archive)
- .jar (Java Archive)

## 2. Create a Library Module
1, File → New → New Module…
2, Choose Android Library (for .aar) or Java/Kotlin Library (for .jar).
3, Give it a name, e.g., commonextensions.

## 3. Add Your Code
Inside commonextensions/src/main/java/
Inside commonextensions/src/main/res/

## 4. Generate .aar
```
./gradlew :commonextensions:assembleRelease
```

## 5. Use in Another Project
```
dependencies {
    implementation(files("libs/commonextensions-release.aar"))
    // OR for JAR
    implementation(files("libs/commonextensions.jar"))
}
```

# Important java jdk 17 (preferred) or Java 11 (older projects).

## Step 1: Check if Java is installed

```java -version```
- If you see a version (e.g. openjdk 17.x), Java is installed.
- If you see not recognized, you need to install it.

## Step 2: Download and install
```https://adoptium.net/temurin/releases/?version=17```

## Step 3: Find your java installation path
```C:\Program Files\Eclipse Adoptium\jdk-17```

## Step 4: Step 3: Set JAVA_HOME in Windows
1, Press Win + R → type sysdm.cpl → Enter.
2, Go to Advanced → Environment Variables.
3, Under System variables → click New (if JAVA_HOME doesn’t exist).
- Name: JAVA_HOME
- Value: C:\Program Files\Eclipse Adoptium\jdk-17
4, Still under System variables, find Path → Edit → New → add:
```
%JAVA_HOME%\bin
```
5, Save & restart your terminal.

6, Verify
```java -version```
You should see something like:
```openjdk version "17.0.x" 2025-xx-xx```


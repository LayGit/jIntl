# jIntl
![Maven](https://img.shields.io/maven-central/v/com.laylib/jIntl.svg)
![License](https://img.shields.io/github/license/LayGit/jIntl.svg)

### Overview

a Java internationalization components

### Installation(Maven)

Add the dependency to your pom.xml file:

```
<dependency>
    <groupId>com.laylib</groupId>
    <artifactId>jIntl</artifactId>
    <version>2.0.0</version>
</dependency>
```

Then run from the root dir of the project:

```
mvn install
```

### Installation(Gradle)

Add the dependency to your build.gradle file:

```
implementation 'com.laylib:jIntl:2.0.0'
```

Then load gradle changes

### Example

#### resources file structure
```
resources  
    |--intl  
       |--index.yaml  
       |--global  
          |--global_en.yaml  
          |--global_zh_CN.yaml  
```

The `index.yaml` file content:
```yaml
global:
  - en
  - zh-CN
```

The `global_en.yaml` file content:
```yaml
http:
    internalServerError: "Internal Server Error"
```

The `global_zh_CN.yaml` file content:
```yaml
http:
  internalServerError: "服务器内部错误"
```

`Example.java`

```java
import com.laylib.jintl.IntlSource;

import java.util.Locale;

class Application {
    public static void main(String[] args) {
        IntlSource intlSource = new IntlSource();
        String code = "http.internalServerError";
        System.out.printf("Message of English: %s \n", intlSource.getMessage(code, Locale.ENGLISH));
        System.out.printf("Message of Simplified Chinese: %s \n", intlSource.getMessage(code, Locale.SIMPLIFIED_CHINESE));
    }
}
```

### Using with Nacos Source
See [jIntl-provider-nacos](https://github.com/LayGit/jIntl-provider-nacos)

### Using with SpringBoot
See [jIntl-spring-boot-starter](https://github.com/LayGit/jIntl-spring-boot-starter)
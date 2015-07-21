**The current released version is 0.1.5**

(The current development version is 0.1.6-SNAPSHOT)

To use spann in a maven/ivy/etc based project, use the following repository/dependencies.

You can also [Download manually](https://oss.sonatype.org/content/groups/staging/com/masetta/spann)

```
<project>
  ...
  <repositories>
    <repository>
      <releases>
        <enabled>true</enabled>
      </releases>
      <snapshots>
        <enabled>true</enabled>
      </snapshots>
      <id>nexus-oss</id>
      <name>Nexus Open Source Staging Repository</name>
      <url>https://oss.sonatype.org/content/groups/staging</url>
    </repository>
  </repositories>
  ...
  <dependencies>

    <dependency>
      <groupId>com.masetta.spann</groupId>
      <!-- uncomment one.. -->
      <!--  <artifactId>spann-metadata</artifactId> -->
      <!--  <artifactId>spann-spring</artifactId> -->
      <!--  <artifactId>spann-orm</artifactId> -->
        <version>0.1.5</version>
    </dependency>
  
  </dependencies>
  ...
</project>
```

**Note**: if using spann-metadata directly, you will also need to add an ASM implementation to your project. (for example `asm:asm:3.2`)
# Module overview #

Spann consists of 2 core API modules and other modules which leverage this API to implement solutions for common usecases.

## com.masetta.spann:spann-metadata ##

The metadata module provides a complete Metadata scanning API. It uses ASM (objectweb) to read metadata from selected classes.

Main features of spann-meatdata are:

  * API to reflect all java metadata of the target classes (with exception of code instructions) without loading **any** of the scanned classes.

  * Rule based scanning: API to optimize the scan by defining which metadata artifacts are read, ignoring unneeded metadata. For example, the spann-spring module reads only class metadata and annotations; fields, methods and their annotations are initially not read, but are available via lazy loading (see below).

  * Lazy loading of all unloaded metadata on demand. For example: metadata of super classes, methods etc is transparently lazy-loaded when needed.

  * Rule based lazy loading: API to define how much metadata is lazy loaded on demand. For example: when lazy-loading methods of a class, load also method-annotations.

  * Portable accross different versions of ASM: can be used from within spring without any ASM dependency (using the ASM implementation delivered with spring), or elsewhere with any other ASM version.


## com.masetta.spann:spann-spring ##

The spann-spring module builds on spann-metadata class path scanning facility and offers a ClassMetadata Visitor API for the spring container. By Implementing Visitors, developers can use the ScanContext to create BeanDefinitions, thus defining new spring beans.

Main features of spann-spring:

  * API to define custom visitors for class metadata. This allows developers to  process of annotations or any other class metadata.

  * Additional API for custom handling of class and/or method annotations.

  * A few generic annotations for common tasks, to allow developers further access to spring features (for example to implement abstract methods by delegation)

  * ScanContext's Attach API allows sharing BeanDefinitions between Visitors, making it easy to develop **Annotation Composition**.
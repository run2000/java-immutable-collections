# About this directory

This directory contains the JUnit and Hamcrest Core jar files for building
and running the test suite.

## Populating this directory

This can be done using [Apache Ivy][1], using the supplied ivy.xml and the
[Ant][2] `resolve` task. This task is standalone from the rest of the build
targets to avoid a hard dependency on Ivy.

Alternatively, copy in the `junit-4.x.jar` and `hamcrest-core-1.x.jar`
files manually from a local copy you have available.


[1]: http://ant.apache.org/ivy/
[2]: http://ant.apache.org/

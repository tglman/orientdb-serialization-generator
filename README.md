# orientdb-serialization-generator

Generate a serialized version for each serializer present in orient for each json document present in the source folder.


Usage:
mvn clean install
mvn exec:java -Dexec.mainClass=com.orientechnologies.serialization.validator.SerializedDocumentGenerator

# Migrant
An RDBMS migration tool that is triggered from code.

Migrant is written in kotlin and is currently only tested with PostgresSQL

To use Migrant just run:

```kotlin
val directory = File(directoryPath) // directoryPath: The location of the sql file for your migrations
val migrationScriptsScanner = MigrationScriptsScanner(directory)
val conn = DriverManager.getConnection(url, props) //jdbc driver connection
val migrant = Migrant(migrationScriptsScanner, conn)
migrant.beginMigration()
```

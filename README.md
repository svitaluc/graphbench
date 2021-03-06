# graph-db-benchmark

This project serves as a platform for running various benchmarks on various graph databases. Its motivation is to simplify
writing and running benchmark tests for different graph databases. It provides
a unified interface for the most common graph databases so that running a benchmark would only require
writing one test and a specification of which database(s) to use.

The current project is a work in progress, and any suggestions are welcomed.

## Usage

You can either run the jar file directly or use an Automatic Launcher. In both cases, the configuration
is set with configuration files placed in graphbench-run-${project.version}/conf directory. In the case of the Automatic
Launcher, the configuration possibilities are a bit wider.
First, both approaches of how to run the project are introduced, followed by the configuration description.

* **jar file** - you can run the provided jar file directly in 
graph-db-benchmark\graphbench-run\target\graphbench-run-${project.version}\graphbench-run-${project.version}\graphbench.jar
* **Automatic Launcher** - you can set more repetitions of tests in various directories on different databases
and run this at once without a need to call the jar file with a different configuration each time.

### Cofiguration

All the configurations are read from the various files in the _conf_ directory. There is a config for general settings
and also specific configs placed in corresponding directories.

#### config.properties

* _DATABASE_TYPE_ - a type of database that must match one of the supported databases listed in 
GraphDBType. So far the supported databases are
    * JANUSGRAPH
    * TITAN
* _DATABASE_DIR_ - directory of database storage - a place where the database will be stored. It's cleaned
each time before a new test is run.
* _TEST_TYPE_ - similarly to _DATABASE_TYPE_, a type of database must match one of the tests listed in
the TestType enum. So far the supported tests are
    * BASIC_OPERATIONS
If you want to run your own test, see the section _Extensions - Add Your Own Test or a Database_.
* _DATASET_DIR_ - directory of a dataset. The supported data model is described in section _Data Model_. The data
can be generated by the project _data-generator_, which is described in its separate README file.

These are the basic settings. The _config.properties_ file contains more configurations that are all described by comments
 inside the file.
 
#### janusgraph/janusgraph.properties

Standard JanusGraph properties described in 
[JanusGraph documentation](https://docs.janusgraph.org/basics/configuration-reference/).
The properties defined in this file are all used for the JanusGraph configuration if the JanusGraph database is used.

#### titan/titan.properties

Standard Titan properties described in 
[Titan documentation](http://titan.thinkaurelius.com/wikidoc/0.4.4/Graph-Configuration.html).
Note, that the option _TITAN_ invokes the version 0.4.4.
The properties defined in this file are all used for the Titan configuration if the Titan database is used.

#### cassandra
The Cassandra directory contains several yaml files that are used based on the Cassandra version that is 
required by a given database. The file serves as a standard Cassandra yaml configuration
described at [its documentation](https://docs.datastax.com/en/archived/cassandra/3.0/cassandra/configuration/configCassandra_yaml.html).

## Data Model

So far, the data model expects three types of data stored in separate csv files, where each row
represents one record of the item (vertex/edge/edge attribute).
* _vertex_ - each vertex record is expected to contain information about its 
	* id
	* name
	* parent id - originally, the model was taking advantage of vertex hierarchy and often acquiring
	 information about parent vertex ids. Therefore, the `parent id` field is present for optimization reasons - having 
	 it as a property does not require further search for an edge with a parent label and its opposite vertex 
	 (which proved to be significantly slower, at least in Titan)
Optionally a description can be included.
* _edge_ - each edge record is expected to contain information about its
	* id
	* starting vertex
	* ending vertex
Optionally, type information can be included.
* _edge attribute_ -  The edge attribute represents a property of an edge to be stored as an item of its 
properties set. Each edge-attribute record is expected to contain information about 
	* edge id to which the attribute belongs
	* the name of the attribute
	* the value of the attribute.

In all the cases, the column indices of individual data information - that is, the information in which column of 
the csv file to find which type of information (f.e. id, starting vertex, and ending vertex) - are set 
in the _config.properties_ file. 

## Project description

The project is divided into the following modules
* `data-generator` - this module is actually a separate project that can generate data that serve as an input 
for the graphbench project. It contains its separate README file.
* `graphbench-all` - contains the main method which starts the whole application and a database factory.
* `graphbench-backend` - contains necessary implementations for storage backends (such as Cassandra)
that can be necessary for some graph database products (such as Titan/JanusGraph).
* `graphbench-core` - contains all general logic and connectivity interfaces. It includes general
interfaces for a graph database, general handling of property files and csv files, dataset handling,
general test interfaces and others.
* `graphbench-janusgraph` - contains implementation for the JanusGraph.
* `graphbench-run` - this module has two main purposes
    * it assembles the whole project together, creating the final product of a runnable jar file
    _graphbench.jar_. 
   * it provides the AutomaticLauncher that can set more repetitions of tests in various directories on different databases
and run such setting at once without a need to call the jar file with a different configuration each time
* `graphbench-tinkerpop2` - contains implementation for the TinkerPop framework, version 2. 
* `graphbench-tinkerpop3` - contains implementation for the TinkerPop framework, version 3. 
* `graphbench-titan` - contain implementation for the Titan, so far the version 0.4.4 (also the last version
supporting Persistit storage backend).

## Extensions

### Add Your Test

In order to add a new test, you need to proceed with the following steps 
(all mentioned classes are part of the package cz.cvut.fit.manta.graphbench.test):

* specific implementations of tests are placed in the package cz.cvut.fit.manta.graphbench.test.benchmark. 
Therefore, your new test class should be part of the benchmark package as well.
* the new test class must implement the ITest interface.
* the TestType enumeration must be extended with your new test. The given name will have to match the name you 
enter in the config.properties file in the property TEST_TYPE.
* the TestFactory must be extended with the case of your new test.

The corresponding classes further contain javadoc and comments that should help you with the creation of a new test.

After these steps, recompile the project (or the necessary modules) and run it normally, as explained above.

### Add Your Database

In order to add a new database, you need to proceed with the following steps:

* create a new module `graphbench-<name-of-your-database>` with a class representing your database, that
implements the interface `cz.cvut.fit.manta.graphbench.core.db.GraphDBConnector`.
* add your database as another database type to `GraphDBType`.
* include your database in `GraphDBFactory`.

After these steps, recompile the project (or the necessary modules) and run it normally, as explained above.


## Acknowledgements

This project is developed under [FIT CTU](https://fit.cvut.cz/en) and [MANTA](https://getmanta.com/).

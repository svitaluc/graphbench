# graph-db-benchmark

This project serves for running various benchmarks on various graph databases. Its motivation is to simplify
writing and running benchmark tests. It provides
a unified interface for the most common graph databases, so that running a benchmark would require
writing only one test and just defining which database(s) to use.

## Project description

The project is divided into following modules
* `graphbench-core` - containing all general logic and connectivity interfaces. It includes
    * _access package_ - general interfaces for a graph database, such as `IEdge` or `IVertex`.
    * _config package_ - general implementation for handling property files. It contains implementation for
    the `config.properties` file.
    * _csv package_ - manipulation with csv files, for either reading or writing the files.
    * _dataset package_ - handling datasets
    * _db package_ - 

## Usage


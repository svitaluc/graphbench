# Data Generator

This module serves for generating data as an input to the Graphbench project. Based on the defined number of 
vertices and edges it creates corresponding data files of
* `node.csv` - csv file containing information about all vertices of the graph. It contains 3 columns of
    * vertex id
    * parent id
    * name
    
    So, each vertex is connected to its parent with an edge. Therefore, the first record contains an empty
    string for the parent id, meaning it's a root node, and all the following vertices have a parent from
    the set of previous vertices.
* `edge.csv` - csv file containing information about edges, extra to those connecting vertices with their parents.
    It contains 4 columns of
    * edge id
    * start vertex id
    * end vertex id
    * edge name
* `edge_attribute.csv` - csv file containing information about all edge attributes. So far, each edge
has exactly one attributed generated. It contains 3 columns of
    * edge id
    * attribute key
    * attribute value

First, the generated graph forms a tree since each
vertex is connected to a parent chosen randomly among already generated vertices. Therefore, a root is created first.
Then, additional edges
are generated if the current number of edges is lower than the required number of edges of the `edgesAmount` variable.


## Usage
Data generator is a basic Java SE application. In order to generate required data, set following
properties in the main method
* `outputPath` - existing directory where the data files will be generated
* `verticesAmount` - the number of generated vertices
* `edgesAmount` - the number of generated edges, both those connecting vertices with their parents and
 all additional ones (hence, it is also the number of generated edge attributes)

and launch the application.


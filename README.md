# Fuzzy Gradual Patterns Extraction from Property Graphs
 
Gradual Pattern (GP) extraction is the process of discovering knowledge from databases as comparable attributes of co-variations.  In linguistic expression it may be represented as, “the more the value of X1,. . . , the more the value of Xn”, where X1 and Xn are numerical attributes [1]. Fuzzy gradual patterns of the form “the more the X is A, the more the Y is B” [2]. These patterns express the information about the attributes along with the fuzziness attributes and their co-variation. For instance, for a doctor, the more the age is “almost 40”, the more the experience is “almost high”. We consider two modalities namely “Low” and “High” for each property of the node as well as the corresponding relationship. For example, for property “Age”, we have “Age Low” and “Age High” attributes. Similarly, for relationship type “TAKES”, we have “TAKES Low” and “TAKES High” respectively.

A Property Graph (PG) refers to a data model in which data has (key:value) pairs. A property graph data model enables us to represent the data in natural way in form of graphstructure where vertices are called as nodes and edges are called as relationships. 

Neo4j is a NoSQL Graph database which helps to model nodes and relationships in form of connected data. Neo4j uses a declarative query language i.e., “cypher”  to retrieve data from the graph database. 


Following is summary of the contents of this readme file.

1. Getting Started <br />
    1.1 System Specifications<br />
    1.2 Prerequisites<br />
    1.3 Installations<br />
2. Running the program<br />
    2.1 Individual program execution<br />
    2.2 Multiple executions of program for plotting charts<br />
3. Graph Generartor<br />
4. References<br />


## 1. Getting Started

 This program provides the details about how to run this Java program for extraction of gradual patterns from property graphs. We show the necessary tool and commands to generate output files. 

**IMPORTANT NOTE**: *The following steps show the program execution with a artifact (.jar file) created to simply show the process of gradual patterns extraction. User can generates their a new jar file from the available source code.* 
 
### 1.1 System Specifications

For running these experiments, following were our system specification.<br />
Hardware: Intel Core i7-4610M, 3.00 GHz, quad core processor, 16 GB RAM <br />
Operating System: Linux generic kernel 4.4.0-134, Ubuntu 16.04 LTS<br />

#### Note:
It is not necessary to have exactly same or higher hardware specifications to run this program. 
If you choose to run this program for a database smaller of nodes and relationships, for example upto 5000 nodes and 5000 relationships, then 
Linux OS with 4 GB RAM and Core i3/i5 processor can easily run this program. An example setup for a database with 1000 nodes and 1000 relationships, as show in following setup. 
The same setup can execute higher number of nodes and relationships e.g. 20,000 nodes and 20,000 relationships with the above mentioned hardware specifications or with higher specifications.

### 1.2 Prerequisites

To run this program, you need to have following softwares:

Mandatory:
1. Java version 1.8.0\_181, Java(TM) SE Runtime Environment (build 1.8.0_181-b13) or higher
2. Neo4j graph database version 3.4.7 or higher

Optional (if you want to execute scripts to reproduce results): 

3. Python 2.7.12
4. Linux Ubuntu 16.04.5 LTS 

### 1.3 Installations

We tested this program execution with neo4j community version 3.4.7. The installation instructions for neo4j on debian are available at [3]. Java 8 installation package is available at [4]. 
If you want to download the community version that we used for running these experiments, then it is available at [5.] 

After the successful installations, following are the outputs of version commands for installations verification:

```
$ java -version
```
output:

java version "1.8.0_181"<br />
Java(TM) SE Runtime Environment (build 1.8.0_181-b13)<br />
Java HotSpot(TM) 64-Bit Server VM (build 25.181-b13, mixed mode)<br />


```
$ readlink -f $(which java)
```
output:<br />
/usr/lib/jvm/jdk1.8.0_181/bin/java

Python may be installed in Ubuntu by executing following command:

```
$ sudo apt-get install python-minimal
```

```
$python --version
```
output:<br />
Python 2.7.12

```
$ readlink -f $(which python)
```
output:<br />
/usr/bin/python2.7



## 2. Running the program

#### Step 1: 

Download the project in any format (tar or zip)

#### Step 2: 

Go to the download location in your host machine and untar the downloaded file "gpnr-master.tar.gz". 

```
$ tar -zxvf gpnr-master.tar.gz 
```
#### Step 3: 

Go to the directory and check files as shown below

```
$ cd gpnr-master/GPNR/
```

```
$ ls -l
total 80476
drwxrwxr-x 2 faaiz faaiz     4096 janv. 10 18:01 csv
-rw-rw-r-- 1 faaiz faaiz 78865438 janv. 10 18:03 FGPG-Node-Rel-Nodes.jar
-rwxrwxr-x 1 faaiz faaiz      672 janv.  7 13:38 FGPGNode-Rel.sh
-rw-rw-r-- 1 faaiz faaiz  1135143 déc.  18 15:53 graph-10K-GG.db.tar.gz
-rw-rw-r-- 1 faaiz faaiz   118054 déc.  18 15:53 graph-1K-GG.db.tar.gz
-rw-rw-r-- 1 faaiz faaiz  2255903 déc.  18 15:53 graph-20K-GG.db.tar.gz
-rw-rw-r-- 1 faaiz faaiz     1153 janv.  3 14:22 pom.xml
-rwxrwxr-x 1 faaiz faaiz     2016 janv.  3 14:22 resultsPythonScriptFGPG.py
drwxrwxr-x 4 faaiz faaiz     4096 janv.  3 14:22 src
drwxrwxr-x 4 faaiz faaiz     4096 janv.  3 14:32 target


```
The above output contains the "src" directory which contains complete source code of this program. It also contains the "pom.xml" file to see the packaging and dependencies details.

If you see the "FGPG-Node-Rel-Nodes.jar" and "graph-1K-GG.db.tar.gz", then you are ready to execute the program. 


### 2.1 Individual program execution

Now, first we have to configure the neo4j.conf file.

#### Step 4: 

Open the config file

```
$ nano /etc/neo4j/neo4j.conf
```

In the case, you are using community version, go the directory of neo4j config directory and open the config file
```
$ cd neo4j-community-3.4.7/
```

```
$ nano ./conf/neo4j.conf
```
#### Step 5: 

Comment the following line and add a new line as shown in neo4j.conf file

```
#dbms.active_database=graph.db
dbms.active_database=graph-1K-GG.db
```

Save the file Ctrl+o and exit Ctrl+x
 
#### Step 6: 
 
untar the "graph-1K-GG.db.tar.gz" and place it into neo4j database folder
 
```
$ cd ~/fgpg-master/FGPG/
```

Copy the database file into ~/neo4j-home/data/databases/ folder. In our case, we are using community version. So the command will be as follows:
 
```
$ cp graph-1K-GG.db.tar.gz ~/neo4j-community-3.4.7/data/databases/
```

Go to the file location and untar the file

```
$ cd ~/neo4j-community-3.4.7/data/databases/
```

```
$ tar -zxvf graph-1K-GG.db.tar.gz
```

This will result an folder with name "graph-1K-GG.db" in location ~/neo4j-community-3.4.7/data/databases/

#### Step 7: 

Start the neo4j service

```
$ service neo4j start
```

In case you are using community version,

```
$ cd ~/neo4j-community-3.4.7/bin/
```

```
$ ./neo4j console
```

On successful start of the service, you will see the output on console, something similar to following:

```
INFO  ======== Neo4j 3.4.7 ========
INFO  Starting...
INFO  Bolt enabled on 0.0.0.0:7687.
INFO  Started.
Remote interface available at http://localhost:7474/
```
Test that neo4j is working correctly and having nodes and relationships. Open http://localhost:7474/ in browser to verify. 
The username is "neo4j" and password is "lirmm" without quotes. Please note that username and password must be same as mentioned. 
These credentials are used by java program to communicate with graph database. 


### Note:
If you do not want to use this test database and want to create a new neo4j database using our synthetic graph generator, 
then a detailed method is discussed below in section 3 "Graph Generator"


### Step 8:

After the successful start and verification of Neo4j on host machine, now we will run our java program. 

The program requires two inputs.<br />
1. Input jar file (in our test case i.e., FGPG-Node-Rel-Nodes.jar)
2. Minimum support threshold (normally in range from 0.1 to 0.9 )

It is better to save the output of the program in a text file so as to view it separately. Therefore, the command will be:

```
$ java -jar FGPG-Node-Rel-Nodes.jar 0.2 > Result-Support-0.2.txt
```

For experiments, sometime it is better to allocate static memeory (e.g., 2G as shown below) to program, efficient G1 garbage collection and zero "0" biased locking delay. 
The detailed reasoning about memory metrics can be further studied from [6]. We use following JVM flags to run the individual execution. 

```
-Xmx2G -XX:+UseG1GC -XX:BiasedLockingStartupDelay=0
```

Therefore, the final command with jvm flags would be:

```
$ java  -Xmx10G -XX:BiasedLockingStartupDelay=0 -XX:+UseG1GC -jar FGPG-Node-Rel-Nodes.jar 0.2 > Result-Support-0.2.txt
```

The successful execution of the program will generate a text file with name "Result-Support-0.2.txt" in the current directory, in our case at the moment 
i.e., ~/fgpg-master/FGPG/. You will also notice that after the program execution, a folder with name "csv" is created that contains the data files retrieved from graph database. 
The final lines of the output file may look like:

```
List of Patterns with Valid_Database:
------------------------------------------------

Total Number of Patterns :  56 

[[2+], [1+]] 0.3338108643867335
[[1+], [2-]] 0.3096400193233496
[[4+], [1+]] 0.5149839249720978
[[4-], [1+]] 0.4654095384051573
[[5+], [1+]] 0.21682130899035498
[[5-], [1+]] 0.23287967883260316
[[6+], [1+]] 0.4866985390881378
.
.
.
.

[[8+], [6-], [1+]] 0.20677649880894872
[[6+], [8-], [4+]] 0.2336292915327081

*************************************************** 

 ------ Run Time & Peak Heap Memory Result are: ------ 

Total Heap Peak Used : 282
Program Run time : 10.083783252 seconds
```



### 2.2 Multiple executions of program for plotting charts 

In this section we will describe the details of multiple executions of program in order to plot the charts. As we can see in Step 3, the output of the command
"ls -l" shows two script files namely, "FGPGNode-Rel.sh" and "resultsPythonScriptFGPG.py"

Please note, to run this script, it must be in the directory where the jar file and python script is present. In our case currently, it is "~/fgpg-master/FGPG/"<br />

#### 2.2.1 Program execution: graph database with 1,000 nodes and 1,000 relationships: 

This database contains 3 node labels, 3 relationship types, and 9 node properties per label.

In order to execute given .jar file (i.e., FGPG-Node-Rel-Nodes.jar) for minimum support threshold values ranging from 0.1 to 0.8, we will execute the bash script as follows:

```
$ ./FGPGNode-Rel.sh
```

The script will ask you to provide the input jar file as shown below:

```
$ ./FGPGNode-Rel.sh

Bienvenue et Bonjour !

entrez le nom de fichier avec l'extension .jar :

```

Now, type the input file i.e., FGPG-Node-Rel-Nodes.jar

The script will start executing the program and you will see the output on console like:

```
$ ./FGPGNode-Rel.sh

Bienvenue et Bonjour !

entrez le nom de fichier avec l'extension .jar :   GPNR-Node-Rel-1K-Nodes.jar


 WELCOME TO GRADUAL PATTERN EXTRACTION PROGRAM 

Input file: FGPG-Node-Rel-Nodes.jar
Minimum support: 0.1

AvgTimeConsumption: 10.4684838968   AvgMemoryUtilization: 258.4


 WELCOME TO GRADUAL PATTERN EXTRACTION PROGRAM 

Input file: FGPG-Node-Rel-Nodes.jar
Minimum support: 0.2

AvgTimeConsumption: 6.0477500466   AvgMemoryUtilization: 182.6
.
.
.
.

Program ends
```

This script runs the program five (5) times keeping an interval of 15 seconds in each execution. It will create a new directory namely "Output-Results" in the current path that contains 
the results of all the 5 executions along with an average result csv file for each minimum support threshold ranging from 0.1 to 0.8.    



#### 2.2.2 Program execution: graph database with 10,000 nodes and 10,000 relationships: 


Before moving to the next step, we have to:
1. untar the database directory and place it into ~/neo4j-home/data/databases/ 
2. make changes in the neo4j.conf file to direct it to the new database directory
3. of you want to keep the pervious resutls in "Output-Results" folder then change the file name from FGPG-Node-Rel-Nodes.jar to FGPG-Node-Rel-Nodes-10K.jar

Follow the step 4 onwards for 10,000 nodes and 10,000 relationships database. 

This database contains 3 node labels, 3 relationship types, and 9 node properties per label. 
In order to execute given .jar file (i.e., FGPG-Node-Rel-Nodes.jar) for minimum support threshold values ranging from 0.1 to 0.8, we will execute the bash script as follows:

```
$ ./FGPGNode-Rel.sh
```

The script will ask you to provide the input jar file as shown below:

```
$ ./FGPGNode-Rel.sh

Bienvenue et Bonjour !

entrez le nom de fichier avec l'extension .jar :

```

Now, type the input file i.e., FGPG-Node-Rel-Nodes-10K.jar 

The script will start executing the program and you will see the output on console like:

```
$ ./GPNR-Node-Rel.sh

Bienvenue et Bonjour !

entrez le nom de fichier avec l'extension .jar :   FGPG-Node-Rel-Nodes-10K.jar


 WELCOME TO GRADUAL PATTERN EXTRACTION PROGRAM 

Input file: FGPG-Node-Rel-Nodes-10K.jar
Minimum support: 0.1

.
.

Program ends
```
#### 2.2.3 Program execution: graph database with 20,000 nodes and 20,000 relationships: 

The same process applies as mentioned in section 2.2.2 to run the program for this database. Only the respective file names need to changed.


### Output Results


The output results after executing the program on these three (1K, 10K and 20K nodes and relationships) synthetic graph databases are  available with file name "Output-Results.tar.gz" at [4].




## 3. Graph Generator

The synthetic graph generator was first developed by Mégane Wintz, who was student at polytech Montpellier, France. It is available at [7].
Later on, to add the node properties feature, it was extended by FADO team [8]. The extended graph generator is available at [9].  


Details about how to use, will be added  soon :-)


## 4. References
[1] Di-Jorio, Lisa, Anne Laurent, and Maguelonne Teisseire. “Mining frequent gradual itemsets from large databases.” International Symposium on Intelli-
gent Data Analysis. Springer Berlin Heidelberg, 2009<br />
[2] B. Bouchon-Meunier, A. Laurent, M. Lesot, and M. Rifqi, “Strengthening fuzzy gradual rules through all the more clauses,” in International Conference on Fuzzy Systems, July 2010, pp. 1–7.
[3] Neo4j Debian installation.  https://neo4j.com/docs/operations-manual/current/installation/linux/debian/<br />
[4] Java SE 8 installation. https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html<br />
[5] Neo4j Community version 3.4.7 https://gite.lirmm.fr/shah/neo4j-community-version-3.4.7<br />
[6] Memory metrics. https://cruftex.net/2017/03/28/The-6-Memory-Metrics-You-Should-Track-in-Your-Java-Benchmarks.html<br />
[7] Mégane Wintz. Graph Generator. https://fastgraph-generator.herokuapp.com/<br />
[8] FADO Team. https://www.lirmm.fr/recherche/equipes/fado<br />
[9] Extended Graph Generator. https://faaizshah.github.io/graphgenerator/<br />

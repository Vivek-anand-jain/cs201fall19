# CS201: Compiler Constructions

## Project Description
The goal of this project is to understand and implement the basic program analysis and program profiling techniques. In this project, you are required to implement the followings:

## Team members:

Viyom Mittal (vmitt003@ucr.edu, 862187209)
Vivek Jain (vjain014@ucr.edu, 862187137)

### Static Analysis:
1. For each program under analysis, you need to identify all of the loops in the program.
2. For each loop, identify the instructions where the definition of at least one use of a variable in that instruction might come from outside of the loop.

### Dynamic Analysis (Profiling):
3. Output the number of executions for instructions found at step 2.

You will carry out this project as follows:
- Parse the input program, collect the control flow information and identify and output all the loops in the program.
- For each loop, identify the instructions where the definition of at least one uses in that instruction might come from outside of the loop.
- Instrument the code to find the execution frequency of instructions you found in previous steps.
- Run the instrumented code and output the profiling results.

### Setting up

1. Download [Eclipse Kepler](http://www.eclipse.org/downloads/packages/release/kepler/sr2/eclipse-standard-432) and setup [Java 7](https://www.oracle.com/technetwork/java/javase/downloads/java-archive-downloads-javase7-521261.html) as working environment
2. Download the [project](https://github.com/Vivek-anand-jain/cs201fall19/archive/master.zip), extract it and import it on eclipse (Select File -> Import -> select "Existing Projects into Workspace" -> Select Next -> For root directory, select the extracted folder CS201Fall19-master -> Select Finish.
3. Download and add [Soot jar](https://www.cs.ucr.edu/~aalav003/soot-2.5.0.jar) file to your project (Right click on your project and select Properties -> Select Java Build Path -> Select Libraries -> Select Add External JARs -> Select the downloaded jar file -> Select Open -> Select OK)

### Running Static Analysis
1. Configure `INPUT_PATH`, `OUTPUT_PATH` and `CLASS_UNDER_ANALYSIS` in `src/Constant.java`
2. Open `src/StaticAnalysis.java` and run it.

### Running Dynamic Analysis
1. Configure `INPUT_PATH`, `OUTPUT_PATH` and `CLASS_UNDER_ANALYSIS` in `src/Constant.java`
2. Open `src/DynamicAnalysis.java` and run it. (It will take 1 - 2 minute to generate class file.)
3. After generating class file, open the terminal and go to project root and run the following command: 

*For Linux:*
```sh
java -cp bin:sootOutput <Test class file name without extenstion>
```

*For Windows:*
```sh
java -cp bin;sootOutput <Test class file name without extenstion>
```

> Note: You have to update CLASS_UNDER_ANALYSIS to use target java file.

#### Pointers
1. For Static Analysis step 2, we used [this](https://gist.github.com/musabhusaini/4141729) gist to achieve the def-use functionality.
2. For Dynamic Analysis, we used [Soot's profiler tutorial](https://github.com/Sable/soot/tree/master/tutorial/profiler2)

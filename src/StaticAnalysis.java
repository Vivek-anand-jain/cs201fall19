import java.util.List;

import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.BriefBlockGraph;
import soot.toolkits.graph.BriefUnitGraph;

public class StaticAnalysis {
	public static void main(String[] args) {
		configure(Constant.INPUT_PATH);
		SootClass sootClass = Scene.v().loadClassAndSupport(
				Constant.CLASS_UNDER_ANALYSIS);
		sootClass.setApplicationClass();
		Scene.v().loadNecessaryClasses();

		for (SootMethod sm : sootClass.getMethods()) {
			System.out.println("===================================");
			System.out.println("Method: " + sm.getSignature());
			Body b = sm.retrieveActiveBody();
			BlockGraph blockGraph = new BriefBlockGraph(b);
			PrintBasicBlocks(blockGraph);

			System.out.println("Loops: ");
			LoopAnalysis loopAnalysis = new LoopAnalysis(blockGraph);
			loopAnalysis.PrintAllLoops();

			DataDependencies dataDependencies = new DataDependencies(
					new BriefUnitGraph(b));

			System.out.println("Statement using variables from outside the loop: ");
			List<Stmt> getVariablesUsedFromOutsideLoop = dataDependencies
					.GetVariablesUsedFromOutsideLoop();
			System.out.println(getVariablesUsedFromOutsideLoop);
			System.out.println();
		}
	}

	private static void PrintBasicBlocks(BlockGraph blockGraph) {
		for (Block block : blockGraph) {
			System.out.println(block.toString());
		}
	}

	public static void configure(String classpath) {
		Options.v().set_whole_program(true);
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_src_prec(Options.src_prec_java);
		Options.v().set_output_format(Options.output_format_jimple);
		Options.v().set_soot_classpath(classpath);
		Options.v().set_prepend_classpath(true);
		Options.v().setPhaseOption("cg.spark", "on");
	}
}

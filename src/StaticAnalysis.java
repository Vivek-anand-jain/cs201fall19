import soot.Body;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.options.Options;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.graph.BriefBlockGraph;

public class StaticAnalysis {
	public static void main(String[] args) {
		configure(Constant.ANALYSIS_PATH);
		SootClass sootClass = Scene.v().loadClassAndSupport("Test1");
		sootClass.setApplicationClass();
		Scene.v().loadNecessaryClasses();

		for (SootMethod sm : sootClass.getMethods()) {
			System.out.println("Method: " + sm.getSignature());
			Body b = sm.retrieveActiveBody();
			BlockGraph blockGraph = new BriefBlockGraph(b);
			PrintBasicBlocks(blockGraph);

			System.out.println("Loops: ");
			LoopAnalysis loopAnalysis = new LoopAnalysis(blockGraph);
			loopAnalysis.PrintAllLoops();
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
		Options.v().set_output_format(Options.output_format_class);
		Options.v().set_soot_classpath(classpath);
		Options.v().set_prepend_classpath(true);
		Options.v().setPhaseOption("cg.spark", "on");
	}
}

// UnitGraph graph = new BriefUnitGraph(b);
// Set<Value> definitions = new HashSet<Value>();
//
// for (Unit unit : graph) {
// if (unit instanceof DefinitionStmt) {
// System.out.println(unit.toString());
// DefinitionStmt definitionStmt = (DefinitionStmt) unit;
// Value leftOp = definitionStmt.getLeftOp();
// definitions.add(leftOp);
// }
// if (unit instanceof AssignStmt) {
// System.out.println(unit.toString());
//
// AssignStmt definitionStmt = (AssignStmt) unit;
// Value rightOp = definitionStmt.getRightOp();
// String[] split = rightOp.toString().split(" ");
// for (String string : split) {
// if (definitions.contains(string)) {
// }
// }
// }
//
// }
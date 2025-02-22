import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import soot.Pack;
import soot.PackManager;
import soot.Scene;
import soot.SootClass;
import soot.Transform;
import soot.options.Options;

public class DynamicAnalysis {
	public static void main(String[] args) {
		
		args = Constant.ARGS.split(" ");
		List<String> arguements = new ArrayList<String>(Arrays.asList(args));
		arguements.add(Constant.CLASS_UNDER_ANALYSIS);
		System.out.println(arguements);

		configure(Constant.INPUT_PATH);

		dynamicAnalysis();

		Scene.v().addBasicClass("java.lang.String", SootClass.SIGNATURES);
		Scene.v().addBasicClass("java.lang.System", SootClass.SIGNATURES);
		Scene.v().addBasicClass("java.util.Map", SootClass.SIGNATURES);
		Scene.v().addBasicClass("java.util.HashMap", SootClass.SIGNATURES);
		soot.Main.main(arguements.toArray(new String[arguements.size()]));
	}

	private static void dynamicAnalysis() {
		Pack jtp = PackManager.v().getPack("jtp");
		jtp.add(new Transform("jtp.instrumenter", new UseProfiler()));
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

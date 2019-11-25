import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import soot.Body;
import soot.BodyTransformer;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.AssignStmt;
import soot.jimple.IfStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.Jimple;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.jimple.toolkits.annotation.logic.LoopFinder;
import soot.util.Chain;

public class UseProfiler extends BodyTransformer {
	static SootClass counterClass;
	static SootMethod increaseCounter, reportCounter;

	static {
		counterClass = Scene.v().loadClassAndSupport("MyCounter");
		increaseCounter = counterClass
				.getMethod("void increase(java.lang.String)");
		reportCounter = counterClass.getMethod("void report()");
	}

	protected void internalTransform(Body body, String phase, Map options) {
		SootMethod method = body.getMethod();
		Chain units = body.getUnits();

		LoopFinder loopFinder = new LoopFinder();
		loopFinder.transform(body);
		Collection<Loop> loops = loopFinder.loops();
		for (Loop loop : loops) {
			List<Stmt> loopStatements = loop.getLoopStatements();
			for (Stmt stmt : loopStatements) {
				if (stmt instanceof AssignStmt || stmt instanceof IfStmt) {
					InvokeExpr incExpr = Jimple.v().newStaticInvokeExpr(
							increaseCounter.makeRef(),
							StringConstant.v(stmt.toString()));
					Stmt incStmt = Jimple.v().newInvokeStmt(incExpr);
					units.insertBefore(incStmt, stmt);
				}
			}
		}

		Iterator stmtIt = units.snapshotIterator();

		String signature = method.getSubSignature();
		boolean isMain = signature.equals("void main(java.lang.String[])");

		if (isMain) {
			stmtIt = units.snapshotIterator();
			while (stmtIt.hasNext()) {
				Stmt stmt = (Stmt) stmtIt.next();
				if ((stmt instanceof ReturnStmt)
						|| (stmt instanceof ReturnVoidStmt)) {
					InvokeExpr reportExpr = Jimple.v().newStaticInvokeExpr(
							reportCounter.makeRef());
					Stmt reportStmt = Jimple.v().newInvokeStmt(reportExpr);
					units.insertBefore(reportStmt, stmt);
				}
			}
		}
	}
}

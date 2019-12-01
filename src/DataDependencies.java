import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import soot.Body;
import soot.Local;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.Stmt;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.jimple.toolkits.annotation.logic.LoopFinder;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.LocalDefs;
import soot.toolkits.scalar.LocalUses;
import soot.toolkits.scalar.SimpleLocalDefs;
import soot.toolkits.scalar.SimpleLocalUses;

public class DataDependencies implements LocalDefs, LocalUses {

	private Map<Unit, List<Unit>> unitToDefs;

	private LocalDefs localDefs;
	private LocalUses localUses;

	private Body body;

	public DataDependencies(Body body, LocalDefs localDefs, LocalUses localUses) {
		this.body = body;
		this.localDefs = localDefs;
		this.localUses = localUses;

		unitToDefs = new HashMap<>();

		for (Unit unit : body.getUnits()) {
			List<Unit> defs = new ArrayList<>();
			for (ValueBox value : unit.getUseBoxes()) {
				if (value.getValue() instanceof Local) {
					Local local = (Local) value.getValue();

					defs.addAll(this.localDefs.getDefsOfAt(local, unit));
				}
			}

			this.unitToDefs.put(unit, defs);
		}
	}

	public DataDependencies(UnitGraph graph, LocalDefs localDefs,
			LocalUses localUses) {
		this(graph.getBody(), localDefs, localUses);
	}

	public DataDependencies(UnitGraph graph) {
		this(graph, new SimpleLocalDefs(graph), new SimpleLocalUses(graph,
				new SimpleLocalDefs(graph)));
	}

	public List<Unit> getDefsOf(Unit use) {
		List<Unit> defs = this.unitToDefs.get(use);
		return defs != null ? Collections.unmodifiableList(defs) : null;
	}

	public List<Stmt> GetVariablesUsedFromOutsideLoop() {
		List<Stmt> results = new ArrayList<Stmt>();
		
		LoopFinder loopFinder = new LoopFinder();
		loopFinder.transform(body);
		Collection<Loop> loops = loopFinder.loops();


		for (Loop loop : loops) {
			List<Stmt> loopStatements = loop.getLoopStatements();
			for (Stmt unit : loopStatements) {
				List<Unit> defs = getDefsOf(unit);
				for (Unit def : defs) {
					if (!loopStatements.contains(def)) {
						results.add(unit);
					}
				}
			}
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Unit> getUsesOf(Unit def) {
		return (List<Unit>) this.localUses.getUsesOf(def);
	}

	@Override
	public List<Unit> getDefsOfAt(Local local, Unit unit) {
		return this.localDefs.getDefsOfAt(local, unit);
	}
}
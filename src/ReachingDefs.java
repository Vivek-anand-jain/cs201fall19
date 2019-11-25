import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import soot.Local;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.toolkits.graph.UnitGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;
import soot.toolkits.scalar.ForwardFlowAnalysis;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ReachingDefs {
	protected Map<Unit, List> unitToReachingDefs;

	public ReachingDefs(UnitGraph graph) {
		ReachingDefsAnalysis analysis = new ReachingDefsAnalysis(graph);

		unitToReachingDefs = new HashMap<Unit, List>();
		for (Unit unit : graph) {
			FlowSet set = (FlowSet) analysis.getFlowBefore(unit);
			unitToReachingDefs.put(unit,
					Collections.unmodifiableList(set.toList()));
		}
	}

	public List getReachingDefs(Unit s) {
		return unitToReachingDefs.get(s);
	}
}

@SuppressWarnings({ "unchecked", "rawtypes" })
class ReachingDefsAnalysis extends ForwardFlowAnalysis {
	FlowSet emptySet = new ArraySparseSet();
	Map<Unit, FlowSet> unitToGenerateSet;

	ReachingDefsAnalysis(UnitGraph graph) {
		super(graph);
		unitToGenerateSet = new HashMap<Unit, FlowSet>();

		// pre-compute generate sets
		for (Unit unit : graph) {
			FlowSet genSet = emptySet.clone();

			if (unit instanceof AssignStmt) {
				List<ValueBox> defBoxes = unit.getDefBoxes();

				for (ValueBox valueBox : defBoxes) {
					if (valueBox.getValue() instanceof Local)
						genSet.add(valueBox.getValue(), genSet);
				}
			}
			unitToGenerateSet.put(unit, genSet);
		}

		doAnalysis();
	}

	protected Object newInitialFlow() {
		return emptySet.clone();
	}

	protected Object entryInitialFlow() {
		return emptySet.clone();
	}

	protected void flowThrough(Object inValue, Object unit, Object outValue) {
		FlowSet in = (FlowSet) inValue, out = (FlowSet) outValue;

		in.union(unitToGenerateSet.get(unit), out);
	}

	protected void merge(Object in1, Object in2, Object out) {
		FlowSet inSet1 = (FlowSet) in1, inSet2 = (FlowSet) in2, outSet = (FlowSet) out;

		inSet1.union(inSet2, outSet);
	}

	protected void copy(Object source, Object dest) {
		FlowSet sourceSet = (FlowSet) source, destSet = (FlowSet) dest;
		sourceSet.copy(destSet);
	}
}

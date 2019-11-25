import java.util.ArrayList;
import java.util.List;

import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;

public class DominatorSetAnalysis {
	List<FlowSet> dominatorSet;

	public DominatorSetAnalysis(BlockGraph blockGraph) {
		int numOfBlock = blockGraph.getBlocks().size();

		Block head = blockGraph.getHeads().get(0);

		FlowSet startSet = new ArraySparseSet();
		startSet.add(head);

		FlowSet initialSet = new ArraySparseSet();
		for (Block block : blockGraph) {
			initialSet.add(block);
		}

		dominatorSet = new ArrayList<FlowSet>();
		dominatorSet.add(startSet);

		for (int i = 1; i < numOfBlock; i++) {
			dominatorSet.add(initialSet);
		}
		boolean stable = false;
		while (!stable) {
			List<FlowSet> copyOfDominatorSet = new ArrayList<FlowSet>(
					dominatorSet);
			for (Block block : blockGraph.getBlocks()) {
				if (!block.equals(head)) {

					int currentBasicBlockNumber = block.getIndexInMethod();

					FlowSet self = new ArraySparseSet();
					self.add(block);

					FlowSet finalSet = initialSet.clone();
					for (Block pred : block.getPreds()) {
						int predBasicBlock = pred.getIndexInMethod();
						FlowSet predDominatorSet = dominatorSet
								.get(predBasicBlock);
						finalSet.intersection(predDominatorSet, finalSet);
					}

					FlowSet currentDominatorSet = new ArraySparseSet();
					self.union(finalSet, currentDominatorSet);
					dominatorSet.set(currentBasicBlockNumber,
							currentDominatorSet);
				}
			}
			if (dominatorSet.equals(copyOfDominatorSet)) {
				stable = true;
			}
		}
	}

	public boolean IsDominatedBy(Block obj1, Block obj2) {
		if (dominatorSet.get(obj1.getIndexInMethod()).contains(obj2)) {
			return true;
		}
		return false;

	}

}
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import soot.jimple.spark.ondemand.genericutil.Stack;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;
import soot.toolkits.scalar.ArraySparseSet;
import soot.toolkits.scalar.FlowSet;

public class LoopAnalysis {
	private List<FlowSet> loop;
	private Stack<Block> stack;

	private void Insert(FlowSet currentLoopSet, Block b) {
		if (!currentLoopSet.contains(b)) {
			FlowSet toInsert = new ArraySparseSet();
			toInsert.add(b);
			currentLoopSet.union(toInsert, currentLoopSet);
			stack.push(b);
		}
	}

	public LoopAnalysis(BlockGraph blockGraph) {
		DominatorSetAnalysis dominatorSet = new DominatorSetAnalysis(blockGraph);
		loop = new ArrayList<FlowSet>();
		for (Block currentBlock : blockGraph) {
			for (Block succ : currentBlock.getSuccs()) {
				if (dominatorSet.IsDominatedBy(currentBlock, succ)) {
					stack = new Stack<Block>();
					FlowSet currentLoopSet = new ArraySparseSet();
					currentLoopSet.add(succ);
					Insert(currentLoopSet, currentBlock);
					while (!stack.isEmpty()) {
						Block poppedBlock = stack.pop();
						for (Block pred : poppedBlock.getPreds()) {
							Insert(currentLoopSet, pred);
						}
					}
					loop.add(currentLoopSet);
				}
			}
		}
	}

	public List<FlowSet> GetAllLoops () {
		return loop;
	}
	
	public void PrintAllLoops() {
		if (loop.isEmpty()) {
			System.out.println("No loops");
			return;
		}
		for (FlowSet item : loop) {
			Iterator<Block> iterator = item.iterator();
			System.out.print("[");
			while (iterator.hasNext()) {
				System.out.print(iterator.next().getIndexInMethod());
				if (iterator.hasNext()) {
					System.out.print(", ");
				}
			}
			System.out.println("]");
		}
	}

}

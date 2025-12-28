package report_sample.ex12a.search;

import java.util.*;

public interface World extends Cloneable {
	boolean isValid();
	boolean isGoal();
	List<Action> actions();
	World successor(Action action);
}

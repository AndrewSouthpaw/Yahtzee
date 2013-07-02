
/**
 * The DiceSelection class stores a unique combination of selected dice for reroll.
 * 
 * Each combination contains a set of possible dice combinations that could result from rerolling
 * with this dice selection. Each of these dice combinations has an associated evalue. These evalues
 * are summed together to produce the evalue associated with the dice selection.
 * 
 */

import java.util.*;

public class DiceSelection implements YahtzeeConstants {

	/**
	 * Constructor for DiceSelection.
	 * @param arr The dice selections
	 */
	public DiceSelection(boolean[] arr) {
		selection = arr;
		name = createName();
	}
	
	
	
	/**
	 * Creates a unique name for the combination, which are the dice selections in String format
	 * @return The name of the combination
	 */
	private String createName() {
		String result = "";
		for (int i = 0; i < selection.length; i++) {
			result += selection[i];
		}
		return result;
	}
	
	/** Returns the name of the selection */
	public String getName() {
		return name;
	}
	
	
	/** Returns the dice selection */
	public boolean[] getDiceSelection() {
		return selection;
	}
	
	/** Resets the aggregate evalue of the dice selection */
	public void resetEValue() {
		evalue = 0.0;
	}
	
	/** Sets the possible dice combinations for the selection */
	public void setDiceCombinations(List<DiceCombination> list) {
		allDiceCombinations = list;
	}
	
	/** Adds the evalue of a dice combination to the evalue of the selection */
	public void addEValue(double eval) {
		evalue += eval;
	}
	
	
	public Iterator<DiceCombination> getDiceCombinationsIterator() {
		Iterator<DiceCombination> it = allDiceCombinations.iterator();
		return it;
	}

	
	/* Private instance variables */
	private boolean[] selection = new boolean[N_DICE];
	private String name;
	private List<DiceCombination> allDiceCombinations = new ArrayList<DiceCombination>();
	private double evalue = 0.0;
	
}

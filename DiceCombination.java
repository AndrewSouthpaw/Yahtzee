/**
 * The DiceCombination class stores a unique dice combination for Yahtzee. It contains
 * information associated with the dice combination: the probability of achieving the combination,
 * the best category number and score possible, and the expected value of the combination.
 */

public class DiceCombination implements YahtzeeConstants {

/** Constructor */
	public DiceCombination(int[] dice) {
		combination = dice;
		combinationName = createName();
	}
	
/**
 * Creates a unique name for the combination, which are the dice numbers in String format
 * @return The name of the combination
 */
	private String createName() {
		String result = "";
		for (int i = 0; i < combination.length; i++) {
			result += combination[i];
		}
		return result;
	}
	
	
	public void setBestCategory(int cat, int sc) {
		category = cat;
		score = sc;
	}
	
/** Exports a string version */
	public String toString() {
		String result = "[ ";
		for (int i = 0; i < combination.length; i++) {
			result += combination[i] + " ";
		}
		result += "]";
		return result;
	}
	
	
	
/* Private instance variables */
	
/** The dice combination */
	private int[] combination = new int[N_DICE];		/* The dice in the combination 		*/
	private String combinationName;						/* The combination name 			*/
	private int category;								/* The highest scoring category		*/
	private int score;									/* The score for the best category 	*/
}

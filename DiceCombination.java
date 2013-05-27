/**
 * The DiceCombination class stores a unique dice combination for Yahtzee. It contains
 * information associated with the dice combination: the probability of achieving the combination,
 * the best category number and score possible, and the expected value of the combination.
 */

import java.util.*;

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
	
/** Gets the name of the combination */
	public String getName() {
		return combinationName;
	}
	
/**
 * Updates the best category, score, probability, and expected value of the combination.
 * @param dice The current dice
 * @param cat The highest-scoring category
 * @param sc The score for the category
 */
	public void updateCombination(int[] dice, int cat, int sc) {
		category = cat;
		score = sc;
		updateProbability(dice);
		eValue = score * probability; 
	}
	
/**
 * Updates the probability of getting the combination given the current dice.
 * @param dice The current dice
 */
	public void updateProbability(int[] dice) {
		int nonmatches = 0;
		boolean[] nonmatchingDice = selectNonmatchingDiceForReroll(dice);
		for(int i = 0; i < nonmatchingDice.length; i++) {
			if (nonmatchingDice[i] == true) nonmatches++;
		}
		probability = Math.pow(1.0 / 6.0, nonmatches);
		
	}
	
	
	public boolean[] selectNonmatchingDiceForReroll(int[] dice) {
		boolean[] diceSelections = new boolean[N_DICE];
		for (int i = 0; i < diceSelections.length; i++) {
			diceSelections[i] = true;
		}
		List<Integer> diceList = new ArrayList<Integer>();
		for (int i = 0; i < dice.length; i++) {
			diceList.add(dice[i]);
		}
		for (int i = 0; i < combination.length; i++) {
			Integer die = combination[i];
			int index = diceList.indexOf(die);
			if (index != -1) {
				diceList.set(index, 0);
				diceSelections[index] = false;
			} else {
				
			}
		}
		return diceSelections;
	}
	
	public int[] getCombination() {
		return combination;
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
	private double probability;							/* The probability of getting that combination	*/
	private double eValue;								/* The expected value of the combination		*/
}

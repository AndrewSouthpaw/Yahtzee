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
 * @param selectedDice The dice selection for reroll
 */
	public void updateCombination(int[] dice, int cat, int sc, boolean[] selectedDice) {
		category = cat;
		score = sc;
		updateProbability(selectedDice);
		eValue = probability * (double) score; 
	}
	
/**
 * Updates the probability of getting the combination given the current dice.
 * @param diceSelections The dice selections for reroll
 */
	private void updateProbability(boolean[] diceSelections) {
		int diceRerolled = 0;
		for (int i = 0; i < diceSelections.length; i++) {
			if (diceSelections[i] == true) diceRerolled++;
		}
		/*
		boolean[] nonmatchingDice = getNonmatchingDiceForReroll(dice);
		for(int i = 0; i < nonmatchingDice.length; i++) {
			if (nonmatchingDice[i] == true) nonmatches++;
		}*/
		probability = Math.pow(1.0 / 6.0, diceRerolled);
		
	}
	
/**
 * Gets the nonmatching dice (between the current dice and the combination) to be selected for reroll.
 * @param dice The current dice
 * @return The dice to be rerolled
 */
	public boolean[] getNonmatchingDiceForReroll(int[] dice) {
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
	
/** Gets the highest scoring category. */
	public int getCategory() {
		return category;
	}
	
/** Gets the score for the highest scoring category. */
	public int getScore() {
		return score;
	}
	
/** Gets the e-value for the combination. */
	public double getEValue() {
		return eValue;
	}
	
	public double getProbability() {
		return probability;
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

/**
 * Provides the methods for a Yahtzee computer player.
 * 
 * Debugging: set as own program.
 */

import acm.program.*;
import acm.util.*;
import java.util.*;

public class YahtzeeAI extends ConsoleProgram implements YahtzeeConstants {

	public void run() {
		playGame();
		
	}
	
	private void playGame() {
		boolean gameOver = false;
		scorecard = new int[N_CATEGORIES + 1][nPlayers + 1];
		categoryHasBeenChosen = new boolean[N_CATEGORIES + 1][nPlayers + 1];
		int round = 1;
		while (!gameOver) {
			playRound(round);
			if (round == N_SCORING_CATEGORIES) gameOver = true;
			round++;
		}
		//int winner = highestScoringPlayer();
		println("Congratulations, you are the winner with a total score of " + scorecard[TOTAL][1] + "!");
		pause(DELAY);
		
		
	}
	
/**
 * Plays a round, which is one turn for every player.
 * @param round The round number
 */
	private void playRound(int round) {
		for (int i = 1; i <= nPlayers; i++) {
			playTurn(i, round);
			evaluateTotalScores(i, round);
			printScorecard(i);
			readLine("Press enter to continue.");
		}
		//if (nPlayers > 1) announcePlayerInTheLead();
	}
	
	
/**
 * Plays a turn for one player. Includes rolling dice, and picking category for scoring.
 * @param player The player whose turn it is (index base 1)
 * @param round The round number
 */
	public void playTurn(int player, int round) {
		println("Playing round " + round);
		int[] dice = new int[N_DICE];
		generateAllDiceSelections();
		boolean[] selectedDice = new boolean[N_DICE];
		//display.waitForPlayerToClickRoll(player);
		for (int rolls = 0; rolls < MAX_ROLLS; rolls++) {
			
/*
 * For each roll:
 * 		Keep track of the best score so far, and index of the combo
 * 		Take each combination
 * 			Determine best category
 * 			Calculate score
 * 			Calculate probability
 * 			Evaluate expected value 
 * 		While entering, compare with best score to capture the best one
 * 		
 */
			
			println("Rolling dice...");
			pause(delay);
			rollDice(rolls, dice, selectedDice);
			//display.displayDice(dice);
			println("Dice for roll " + rolls + ": " + diceToString(dice));
			if (rolls == MAX_ROLLS - 1) break;
			for(String name: allSelections.keySet()) {
				DiceSelection selectionCombo = allSelections.get(name);
				selectedDice = selectionCombo.getDiceSelection();
				selectionCombo.resetEValue();
				selectionCombo.setDiceCombinations(generateDiceCombinations(selectedDice, dice));
				for(String str: selectionCombo.getDiceCombinationsIterator()) {
					DiceCombination diceCombo = selectionCombo.getDiceCombination(str);
					int[] comboDice = diceCombo.getCombination();
					int category = chooseBestCategory(player, comboDice);
					boolean isValid = isDiceValidForCategory(comboDice, category);
					int score = calculateCategoryScore(category, isValid, comboDice);
					diceCombo.updateCombination(dice, category, score);
					double eValue = diceCombo.getEValue();
					selectionCombo.addEValue(eValue);
				}
			}
			
			
			
			/*String bestCombo = "";
			double bestEValue = -1.0;
			for(String name: combos.keySet()) {
				DiceCombination combo = combos.get(name);
				int[] comboDice = combo.getCombination();
				int category = chooseBestCategory(player, comboDice);
				boolean isValid = isDiceValidForCategory(comboDice, category);
				int score = calculateCategoryScore(category, isValid, comboDice);
				combo.updateCombination(dice, category, score);
				double eValue = combo.getEValue();
				if (eValue > bestEValue) {
					bestCombo = name;
					bestEValue = eValue;
				}
			}*/
			
			
			
			println("The best combination to aim for: " + combos.get(bestCombo).getName());
			println("The probability of achieving this combo: " + combos.get(bestCombo).getProbability());
			println("The best category: " + combos.get(bestCombo).getCategory());
			println("The score for this category is: " + combos.get(bestCombo).getScore());
			println("The best evalue: " + combos.get(bestCombo).getEValue());
			selectedDice = combos.get(bestCombo).getNonmatchingDiceForReroll(dice);
			println("Selections for next roll: " + selectionsToString(selectedDice));
		}
		println("Turn is over.");
		pause(delay);
		int category = chooseBestCategory(player, dice);
		categoryHasBeenChosen[category][player] = true;
		println("Choosing category " + category);
		boolean isValid = isDiceValidForCategory(dice, category);
		println("Dice are valid for this category: " + isValid);
		int score = calculateCategoryScore(category, isValid, dice);
		println("Score for this category: " + score);
		updateScore(player, category, score);
		pause(DELAY);
		
		/*
		CategoryResult result = chooseCategory(player, dice);
		int category = result.getCategory();
		boolean isValid = result.isValid();
		int score = calculateCategoryScore(category, isValid, dice);
		updateScore(player, category, score);
		*/
	}
	
/**
 * Rolls the dice that are selected (or all the dice on the first roll).
 * @param roll The number of rolls that have occurred
 * @param dice The set of dice
 */
	private void rollDice(int roll, int[] dice, boolean[] isDieSelected) {
		for (int i = 0; i < N_DICE; i++) {
			if (roll == 0 || isDieSelected[i]) {
				int die = rgen.nextInt(1, 6);
				dice[i] = die;				
			}
		}
	}
	
/**
 * Returns a string version of the dice.
 */
	private String diceToString(int[] dice) {
		String result = "[ ";
		for (int i = 0; i < dice.length; i++) {
			result += dice[i] + " ";
		}
		result += "]";
		return result;
	}
	
	private String selectionsToString(boolean[] selections) {
		String result = "[ ";
		for (int i = 0; i < selections.length; i++) {
			result += selections[i] + " ";
		}
		result += "]";
		return result;
	}
	
	
	/* generate dice combinations from selection */
	
	/** Creates a set of all combinations of dice selections.
	 * 	USAGE NOTE: not currently generalizable for N_DICE.
	 */
	
	private void generateAllDiceSelections() {
		for (int d0 = 0; d0 <= 1; d0++) {
			for (int d1 = 0; d1 <= 1; d1++) {
				for (int d2 = 0; d2 <= 1; d2++) {
					for (int d3 = 0; d3 <= 1; d3++) {
						for (int d4 = 0; d4 <= 1; d4++) {
							boolean[] arr = new boolean[5];
							arr[0] = (d0 == 0) ? false : true;
							arr[1] = (d1 == 0) ? false : true;
							arr[2] = (d2 == 0) ? false : true;
							arr[3] = (d3 == 0) ? false : true;
							arr[4] = (d4 == 0) ? false : true;
							DiceSelection combo = new DiceSelection(arr);
							allSelections.put(combo.getName(), combo);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Generates the dice combinations possible for a given set of selected dice.
	 * 
	 * USER NOTE: currently not generalizable for N_DICE.
	 * 
	 * @param selections The dice selections for re-roll
	 * @param dice The current dice
	 * @return The set of dice combinations
	 */
	private List<DiceCombination> generateDiceCombinations(boolean[] selections, int[] dice) {
		List<DiceCombination> result = new ArrayList<DiceCombination>();
		int lb0 = (selections[0] == false ? dice[0] : 1);
		int ub0 = (selections[0] == false ? dice[0] : 6);
		for (int d0 = lb0; d0 <= ub0; d0++) {
			
			int lb1 = (selections[1] == false ? dice[1] : 1);
			int ub1 = (selections[1] == false ? dice[1] : 6);
			for (int d1 = lb1; d1 <= ub1; d1++) {
				
				int lb2 = (selections[2] == false ? dice[2] : 1);
				int ub2 = (selections[2] == false ? dice[2] : 6);
				for (int d2 = lb2; d2 <= ub2; d2++) {
					
					int lb3 = (selections[3] == false ? dice[3] : 1);
					int ub3 = (selections[3] == false ? dice[3] : 6);
					for (int d3 = lb3; d3 <= ub3; d3++) {
						
						int lb4 = (selections[4] == false ? dice[4] : 1);
						int ub4 = (selections[4] == false ? dice[4] : 6);
						for (int d4 = lb4; d4 <= ub4; d4++) {
							
							int[] arr = {d0, d1, d2, d3, d4};
							DiceCombination combo = new DiceCombination(arr);
							result.add(combo);
						}
					}
				}
			}
		}
		return result;
	}
	
	
	
/** Creates a list of all possible dice combinations */
	/* Code here is not currently generalizeable for N_DICE. Need to fix. 
	 * May need to scrap pending investigation of AI inefficiency 
	*/
	private void generateAllDiceCombinations() {
		
		for (int d1 = 1; d1 <= 6; d1++) {
			for (int d2 = d1; d2 <= 6; d2++) {
				for (int d3 = d2; d3 <= 6; d3++) {
					for (int d4 = d3; d4 <= 6; d4++) {
						for (int d5 = d4; d5 <= 6; d5++) {
							int[] arr = new int[N_DICE];
							arr[0] = d1;
							arr[1] = d2;
							arr[2] = d3;
							arr[3] = d4;
							arr[4] = d5;
							DiceCombination combo = new DiceCombination(arr);
							combos.put(combo.getName(), combo);
						}
					}
				}
			}
		}
		
	}
	
	
	
	
	
/** Selects the highest scoring category */
	private int chooseBestCategory(int player, int[] dice) {
		int categoryIndex = 0;
		int highestScore = -1;
		for (int i = 1; i < 16; i++) {  // sloppy, fix later.
			if (categoryHasBeenChosen[i][player] == false) {
				boolean isValid = isDiceValidForCategory(dice, i);
				int score = calculateCategoryScore(i, isValid, dice);
				if (score > highestScore) {
					highestScore = score;
					categoryIndex = i;
				}
				if (i == 6) i = 8; // sloppy, fix later.
			}
		}
		
		return categoryIndex;
	}
	
	
	
/**
 * Determines whether the dice fulfill the requirements of a category.
 * @param dice The set of dice
 * @param category The category in question
 * @return Whether the dice fulfill the requirements of a category
 */
	private boolean isDiceValidForCategory(int[] dice, int category) {
		if (category >= ONES && category <= SIXES) {
			for (int i = 0; i < N_DICE; i++) {
				if (dice[i] == category) return true;
			}
		}
		switch (category) {
			case THREE_OF_A_KIND: return isNOfAKind(3, dice, false);
			case FOUR_OF_A_KIND: return isNOfAKind(4, dice, false);
			case FULL_HOUSE: return (isNOfAKind(3, dice, true) && isNOfAKind(2, dice, true));
			case SMALL_STRAIGHT: return isStraight(4, dice);
			case LARGE_STRAIGHT: return isStraight(5, dice);
			case YAHTZEE: return isNOfAKind(5, dice, false);
			case CHANCE: return true;
			default: return false;
		}
	}
	
	
/**
 * Calculates the score for a category.
 * @param category The selected category
 * @param dice The set of dice
 * @return The score
 */
	private int calculateCategoryScore(int category, boolean isValid, int[] dice) {
		if (isValid) {
			switch (category) {
				case ONES:
				case TWOS:
				case THREES:
				case FOURS:
				case FIVES:
				case SIXES: return sumDice(dice, category);
				case THREE_OF_A_KIND: return sumDice(dice, 0);
				case FOUR_OF_A_KIND: return sumDice(dice, 0);
				case FULL_HOUSE: return FULL_HOUSE_SCORE;
				case SMALL_STRAIGHT: return SMALL_STRAIGHT_SCORE;
				case LARGE_STRAIGHT: return LARGE_STRAIGHT_SCORE;
				case YAHTZEE: return YAHTZEE_SCORE;
				case CHANCE: return sumDice(dice, 0);
				default: return 0;
			}
			
		} else {
			return 0;
		}
	}
	
/**
 * Determines whether there are N of the same die value showing
 * @param n The number of dice with the same value
 * @param dice The set of dice
 * @param exact Whether it must be exactly N or at least N
 * @return Whether there are N of a kind showing
 */
	private boolean isNOfAKind(int n, int[] dice, boolean exact) {
		boolean result = false;
		int[] frequency = diceValueFrequency(dice);
		for (int i = 0; i < frequency.length; i++) {
			if (exact) {
				if (frequency[i] == n) return true;
			} else {
				if (frequency[i] >= n) return true;
			}
		}
		return result;
	}
	
/**
 * Creates an array listing the frequency of each possible die value for the set of dice
 * @param dice The set of dice
 * @return An ordered array listing the frequency of each die value
 */
	private int[] diceValueFrequency(int[] dice) {
		int[] result = new int[6];
		for (int i = 0; i < N_DICE; i++) {
			result[dice[i] - 1]++;
		}
		return result;
	}
	
/**
 * Sums the dice.
 * @param dice The set of dice
 * @param dieValueRequirement Whether only a specific value should be summed. Enter '0' for no requirement (sum all)
 * @return The sum of the dice
 */
	private int sumDice(int[] dice, int dieValueRequirement) {
		int result = 0;
		for (int i = 0; i < N_DICE; i++) {
			if (dieValueRequirement == 0) {
				// Sum all dice
				result += dice[i];
			} else {
				// Add die if it matches the required value
				if (dice[i] == dieValueRequirement) result += dice[i];
			}
		}
		return result;
	}
	
/**
 * Determines whether the dice contain a straight (sequential) of a specific length.
 * @param n The length of the straight
 * @param dice The set of dice
 * @return Whether the dice contain a straight
 */
	private boolean isStraight(int n, int[] dice) {
		int[] frequency = diceValueFrequency(dice);
		for (int i = 0; i < (frequency.length - n + 1); i++) {
			int nInARow = 0;
			for (int j = 0; j < n; j++) {
				if (frequency[i + j] > 0) nInARow++;
			}
			if (nInARow == n) return true;
		}
		return false;
	}
	
	
/**
 * Updates the players score on the scorecard and the display.
 * @param player The player number (index base 1)
 * @param category The category the score will be placed in
 * @param score The score
 */
	private void updateScore(int player, int category, int score) {
		scorecard[category][player] = score;
	}
	
/**
 * Evaluates total score fields.
 * @param player The player number (index base 1)
 * @param round The round number
 */
	private void evaluateTotalScores(int player, int round) {
		updateScore(player, UPPER_SCORE, sumScores(player, ONES, SIXES));
		updateScore(player, LOWER_SCORE, sumScores(player, THREE_OF_A_KIND, CHANCE));
		updateScore(player, TOTAL, (scorecard[UPPER_SCORE][player] + scorecard[UPPER_BONUS][player] + 
										scorecard[LOWER_SCORE][player]));
		if (isUpperScoreComplete(player)) {
			if (scorecard[UPPER_SCORE][player] >= 63) {
				updateScore(player, UPPER_BONUS, UPPER_BONUS_SCORE);
			} else {
				updateScore(player, UPPER_BONUS, 0);
			}
		}
	}
	
	
/**
 * Sums a set of scores on the scorecard.
 * @param player The player number (index base 1)
 * @param startCategory The starting category (inclusive)
 * @param endCategory The ending category (inclusive)
 * @return The sum of the scores
 */
	private int sumScores(int player, int startCategory, int endCategory) {
		int result = 0;
		for (int i = startCategory; i <= endCategory; i++) {
			result += scorecard[i][player];
			
		}
		return result;
	}

/**
 * Determines if all categories in the upper score have been filled.
 * @param player The player number (index base 1)
 * @return Whether all the upper score categories have been filled
 */
	private boolean isUpperScoreComplete(int player) {
		for (int i = ONES; i <= SIXES; i++) {
			if (scorecard[i][player] == 0) return false;
		}
		return true;
	}
	
/** Prints the scorecard */
	private void printScorecard(int player) {
		println("Printing scorecard...");
		for (int i = 1; i <= N_CATEGORIES; i++) {
			if (categoryHasBeenChosen[i][player] == true) {
				println("[" + i + "]: " + scorecard[i][player]);
			} else {
				println("[" + i + "]: ");
			}
		}
	}
	
	
/* Private instance variables */
	private int scorecard[][];
	private final int nPlayers = 1;
	private boolean[][] categoryHasBeenChosen;
	private int delay = 500;
	private final RandomGenerator rgen = RandomGenerator.getInstance();
	private final Map<String, DiceCombination> combos = new HashMap<String, DiceCombination>();
	private final Map<String, DiceSelection> allSelections = new HashMap<String, DiceSelection>();
}

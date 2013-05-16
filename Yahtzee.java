/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	
	public static void main(String[] args) {
		new Yahtzee().start(args);
	}
	
	public void run() {
		nPlayers = dialog.readInt("Enter number of players");
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
		/*
		String response = dialog.readLine("Would you like to play again?");
		if(response.startsWith("Y") || response.startsWith("y")) {
			clearBoard();
			run();
		}
		*/	
		display.printMessage("Thanks for playing!");
	}

/**
 * Plays one game of Yahtzee.
 */
	private void playGame() {
		boolean gameOver = false;
		scorecard = new int[N_CATEGORIES + 1][nPlayers + 1];
		int round = 1;
		while (!gameOver) {
			playRound(round);
			if (round == N_SCORING_CATEGORIES) gameOver = true;
			round++;
		}
		int winner = highestScoringPlayer();
		display.printMessage("Congratulations, " + playerNames[winner - 1] + 
								", you are the winner with a total score of " + scorecard[TOTAL][winner] + "!");
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
		}
		if (nPlayers > 1) announcePlayerInTheLead();
	}
	
/**
 * Plays a turn for one player. Includes rolling dice, and picking category for scoring.
 * @param player The player whose turn it is (index base 1)
 * @param round The round number
 */
	private void playTurn(int player, int round) {
		display.printMessage("It is " + playerNames[player - 1] + "'s turn.");
		int[] dice = new int[N_DICE];
		display.waitForPlayerToClickRoll(player);
		for (int rolls = 0; rolls < MAX_ROLLS; rolls++) {
			rollDice(rolls, dice);
			display.displayDice(dice);
			if (rolls == MAX_ROLLS - 1) break;
			display.printMessage("Results from roll " + (rolls + 1) + ".");
			display.waitForPlayerToSelectDice();
		}
		display.printMessage("You are done rolling. Please choose a category.");
		int category = chooseCategory(dice);
		int score = calculateCategoryScore(category, dice);
		updateScore(player, category, score);
	}
	
/**
 * Rolls the dice that are selected (or all the dice on the first roll).
 * @param roll The number of rolls that have occurred
 * @param dice The set of dice
 */
	private void rollDice(int roll, int[] dice) {
		for (int i = 0; i < N_DICE; i++) {
			if (roll == 0 || display.isDieSelected(i)) {
				int entry = dialog.readInt("Enter a value for dice #" + (i + 1));
				dice[i] = entry;				
			}
		}
	}
	
/**
 * Gets a category selection from the user. If the category will produce a score of 0, the user is cautioned
 * and offered a chance to change the selection.
 * @param dice The set of dice
 * @return A category selection
 */
	private int chooseCategory(int[] dice) {
		int category = 0;
		while (true) {
			category = display.waitForPlayerToSelectCategory();
			boolean b = isDiceValidForCategory(dice, category);
			if (b) {
				break;
			}
			String str = dialog.readLine("You will get a 0 for this category. Are you sure?");
			if (str.startsWith("y") || str.startsWith("Y")) {
				break;
			} 
		}
		
		return category;
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
	private int calculateCategoryScore(int category, int[] dice) {
		boolean b = isDiceValidForCategory(dice, category);
		if (b) {
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
		display.updateScorecard(category, player, score);
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
		if (round == N_SCORING_CATEGORIES) {
			display.printMessage(playerNames[player - 1] + "'s game is finished, with a final score of " +
									scorecard[TOTAL][player]);
			pause(DELAY);
		}
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
	
	
	private void announcePlayerInTheLead() {
		int player = highestScoringPlayer();
		if (player == -1) {
			display.printMessage("Players are tied!");
		} else {
			display.printMessage(playerNames[player - 1] + " is in the lead.");
		}
		pause(DELAY);
	}
	
	private int highestScoringPlayer() {
		if (nPlayers == 1) return 1;
		
		int tiedScores = 0;
		for (int player = 1; player <= nPlayers - 1; player++) {
			if (scorecard[TOTAL][player] != scorecard[TOTAL][player + 1]) break;
			tiedScores++;
			if (tiedScores == nPlayers - 1) return -1;			
		}
		
		int result = 0;
		int score = -1;
		for (int player = 1; player <= nPlayers; player++) {
			if (scorecard[TOTAL][player] > score) {
				result = player;
				score = scorecard[TOTAL][player];
			}
		}
		return result;
	}
		
/* Private constants */

/** Delay for special messages */
	private static final int DELAY = 2000;
	
/* Private instance variables */
	
/** The number of players */
	private int nPlayers;
	
/** The names of the players, index base 0 */
	private String[] playerNames;
	
/** The scorecard for all players (index base 1, so row and column [0] are empty) */
	private int[][] scorecard;
	
/** The Yahtzee Display board */
	private YahtzeeDisplay display;
	
/** Random number generator */
	private RandomGenerator rgen = new RandomGenerator();
	
/** Dialog for user inputs */
	private IODialog dialog = getDialog();
	
/** Delay for special messages */

}

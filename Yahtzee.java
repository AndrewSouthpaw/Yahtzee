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

	private void playGame() {
		boolean gameOver = false;
		int[][] scorecard = new int[N_CATEGORIES + 1][nPlayers + 1];
		int round = 0;
		while (!gameOver) {
			playRound(round);
			round++;
			if (round == N_SCORING_CATEGORIES) gameOver = true;
			
			
			/*
			IODialog dialog = getDialog();
			int player = 1;
			int score = 15;
			int category = display.waitForPlayerToSelectCategory();
			display.updateScorecard(category, player, score);
			display.printMessage("Thanks for entering a score.");
			*/
			
		}
		
	}
	
	private void playRound(int round) {
		for (int i = 1; i <= nPlayers; i++) {
			playTurn(i);
		}
		
	}
	
	private void playTurn(int player) {
		display.printMessage("It is " + playerNames[player - 1] + "'s turn.");
		int[] dice = new int[N_DICE];
		display.waitForPlayerToClickRoll(player);
		for (int rolls = 0; rolls < MAX_ROLLS; rolls++) {
			rollDice(rolls, dice);
			display.displayDice(dice);
			if (rolls == MAX_ROLLS - 1) break;
			display.waitForPlayerToSelectDice();
		}
		display.printMessage("You are done rolling.");
		
		int category = chooseCategory(dice);
		//int category = display.waitForPlayerToSelectCategory();  // *** include error checking later
		int score = calculateCategoryScore(category, dice);
		
		display.updateScorecard(category, player, score);
	}
	
	private void rollDice(int roll, int[] dice) {
		for (int i = 0; i < N_DICE; i++) {
			if (roll == 0 || display.isDieSelected(i)) {
				int entry = dialog.readInt("Enter a value for dice #" + (i + 1));
				dice[i] = entry;				
			}
		}
	}
	
	private int chooseCategory(int[] dice) {
		int category = 0;
		while (true) {
			category = display.waitForPlayerToSelectCategory();
			boolean b = isDiceValidForCategory(dice, category);
			if (b) {
				display.printMessage("You picked a good category.");
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
	
	private int calculateCategoryScore(int category, int[] dice) {
		//boolean b = YahtzeeMagicStub.checkCategory(dice, category);
		boolean b = isDiceValidForCategory(dice, category);
		if (b) {
			if (category >= ONES && category <= SIXES) {
				return sumDice(dice, category);
			}
			
			switch (category) {
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
			display.printMessage("Invalid category. Score is 0.");
			pause(2000);
			return 0;
		}
	}
	
	
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
	
	private int[] diceValueFrequency(int[] dice) {
		int[] result = new int[6];
		for (int i = 0; i < N_DICE; i++) {
			result[dice[i] - 1]++;
		}
		return result;
	}
	
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
		
/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();
	private IODialog dialog = getDialog();

}

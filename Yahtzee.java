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
			boolean b = YahtzeeMagicStub.checkCategory(dice, category);
			if (b) {
				display.printMessage("You picked a good category.");
				break;
			}
			String str = dialog.readLine("This is an invalid category. Are you sure?");
			if (str.startsWith("y") || str.startsWith("Y")) {
				break;
			} 
		}
		
		return category;
	}
	
	private int calculateCategoryScore(int category, int[] dice) {
		boolean b = YahtzeeMagicStub.checkCategory(dice, category);
		int score = 0;
		
		if (b) {
			if (category >= ONES && category <= SIXES) {
				return sumDice(dice, category);
			}
			
			switch (category) {
				case THREE_OF_A_KIND: if(isNOfAKind(3, dice, false)) return sumDice(dice, 0);
				case FOUR_OF_A_KIND: if(isNOfAKind(4, dice, false)) return sumDice(dice, 0);
				case FULL_HOUSE: if(isNOfAKind(3, dice, true) && isNOfAKind(2, dice, true)) return FULL_HOUSE_SCORE;
				case SMALL_STRAIGHT: if(isStraight(4, dice)) return SMALL_STRAIGHT_SCORE;
				case LARGE_STRAIGHT: if(isStraight(5, dice)) return LARGE_STRAIGHT_SCORE;
				case YAHTZEE: if(isNOfAKind(5, dice, false)) return YAHTZEE_SCORE;
				case CHANCE: sumDice(dice, 0);
			}
			
			// score = dialog.readInt("Valid category. Enter a score:");
			
		} else {
			display.printMessage("Invalid category. Score is 0.");
			
		} 
		return score;
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

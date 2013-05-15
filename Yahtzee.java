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
		int score = dialog.readInt("Enter a score:");
/*		int score = 0;
		
		if (b) {
			
			return score;
		} else {
			
		} */
		return score;
	}
		
/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();
	private IODialog dialog = getDialog();

}

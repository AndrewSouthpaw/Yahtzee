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
		IODialog dialog = getDialog();
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
		for (int playerTurn = 1; playerTurn <= nPlayers; playerTurn++) {
			playTurn(playerTurn);
		}
		
	}
		
/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();

}

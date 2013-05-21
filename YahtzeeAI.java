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
		}
		if (nPlayers > 1) announcePlayerInTheLead();
	}
	
	
/**
 * Plays a turn for one player. Includes rolling dice, and picking category for scoring.
 * @param player The player whose turn it is (index base 1)
 * @param round The round number
 */
	public void playTurn(int player, int round) {
		int[] dice = new int[N_DICE];
		boolean[] diceSelections = new boolean[N_DICE];
		//display.waitForPlayerToClickRoll(player);
		for (int rolls = 0; rolls < MAX_ROLLS; rolls++) {
			println("Rolling dice...");
			pause(delay);
			rollDice(rolls, dice, diceSelections);
			//display.displayDice(dice);
			println("Dice for roll " + rolls + ": " + diceToString(dice));
			if (rolls == MAX_ROLLS - 1) break;
			diceSelections = selectDice(dice);
			println("Selections for next roll: " + selectionsToString(diceSelections));
		}
		println("Round is over.");
		pause(100000000);
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
	
/** Stub. Initially dice are selected at random. */
	private boolean[] selectDice(int[] dice) {
		boolean[] result = new boolean[N_DICE];
		for (int i = 0; i < dice.length; i++) {
			result[i] = rgen.nextBoolean();
		}
		return result;
	}
	
	
/* Private instance variables */
	private int scorecard[][];
	private final int nPlayers = 1;
	private boolean[][] categoryHasBeenChosen;
	private int delay = 500;
	private final RandomGenerator rgen = RandomGenerator.getInstance();
}

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
		//playGame();
		int[] dice = new int[N_DICE];
		boolean[] diceSelections = new boolean[N_DICE];
		diceSelections[0] = true;
		diceSelections[1] = diceSelections[4] = true; 
		for (int rolls = 0; rolls < MAX_ROLLS; rolls++) {
			println("Rolling dice...");
			pause(delay);
			rollDice(rolls, dice, diceSelections);
			//display.displayDice(dice);
			println("Dice for roll " + rolls + ": " + diceToString(dice));
			if (rolls == MAX_ROLLS - 1) break;
			
		}
	}
	
	private void playGame() {
		boolean gameOver = false;
		int nPlayers = 1;
		scorecard = new int[N_CATEGORIES + 1][nPlayers + 1];
		categoryHasBeenChosen = new boolean[N_CATEGORIES + 1][nPlayers + 1];
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
			display.printMessage("Results from roll " + (rolls + 1) + ".");
			display.waitForPlayerToSelectDice();
		}
		if (isNOfAKind(5, dice, false)) {
			display.printMessage("Yahtzee!");
			pause(DELAY);
		}
		CategoryResult result = chooseCategory(player, dice);
		int category = result.getCategory();
		boolean isValid = result.isValid();
		int score = calculateCategoryScore(category, isValid, dice);
		updateScore(player, category, score);
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
	
	
	
/* Private instance variables */
	private int scorecard[][];
	private boolean[][] categoryHasBeenChosen;
	private int delay = 500;
	private final RandomGenerator rgen = RandomGenerator.getInstance();
}

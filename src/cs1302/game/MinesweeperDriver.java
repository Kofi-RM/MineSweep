package cs1302.game;

import java.util.Scanner;

/**
 * Contains the main method in which the MineSweeper Alpha will run.
 *
 */

public class MinesweeperDriver {

    /**
     * Main method..
     * @param args
     */

    public static void main(String[] args) {

        Scanner stdIn = new Scanner(System.in);


        if (args.length != 1) {
            System.err.println();
            System.err.println("Usage: MinesweeperDriver SEED_FILE_PATH");
            System.exit(1);
        } else {

            String seed = args[0];

            MinesweeperGame game = new MinesweeperGame(stdIn, seed);

            game.readSeed();
            game.printWelcome();
            game.printMinefield();
            game.play();
        }
    }
}

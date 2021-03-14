package cs1302.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.NoSuchElementException;
/**
 * All the methods used in Minesweeper game will be defined here.
 *
 */

public class MinesweeperGame {

    private String seedPath;
    private int row; // set to rows in seed file
    private int col; // set to cols in seed file
    private int round = 0;
    private int markRow = 0; // set to user inputted row
    private int markCol = 0; // set to user inputted col
    private final Scanner stdIn; // only scanner for standard input
    private String scan;
    private boolean won = false;
    private int numOfMines; // number of mines on the grid
    private boolean [][] mineLocation;
    private String [][] gridIcon;
    private Scanner scanPlayer;
    private boolean command = false; // if true, prints an invalid command error message
    private boolean command2 = false; // if true, prints an invalid command error message
    private boolean validRow = true;
    private boolean validCol = true;
    private boolean inBounds;
    private boolean fogCleared = false; // if true, fog of war is gone and mines are revealed
    private boolean noElement = false;
    int winConditionRow;
    int winConditionCol;

    /**
     * MinesweeperGame constructor.
     *
     * @param stdIn Hard coded standard input variable
     * @param seedPath Path to seed file given by user
     */

    public MinesweeperGame(Scanner stdIn, String seedPath) {
        this.seedPath = seedPath;
        this.stdIn = stdIn;
    } //MinesweeperGame()

    /**
     * Prints a welcome message to the user.
     */

    public void printWelcome() {
        String welcome = "        _  \n " +
            " /\\/\\ (F)_ __   ___  _____      _____  ___ _ __   ___ _ __ \n" +
            " /    \\| | '_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ '_ \\ / _ \\ '__|\n" +
            "/ /\\/\\ \\ | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |\n" +
            "\\/    \\/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|" +
            "\n\t\t\t     ALPHA EDITION |_| v2021.sp";
        System.out.println(welcome);

    } //printWelcome()


    /**
     * Prints out the minefield board.
     */

    public void printMinefield() {

        int loopCol = 0;

        System.out.println(" \n\n Rounds Completed: " + round + "\n");

        for  (int loopRow = 0; loopRow < this.row; loopRow++) {

            System.out.print(loopRow + " |");
            for  (loopCol = 0; loopCol < this.col; loopCol++) {

                System.out.print("" + gridIcon[loopRow][loopCol] + "|");

            }
            loopCol = this.col;
            System.out.println("");
        } // prints row and col

        int num = 0;

        System.out.print("    " + num);

        for (num = 1; num < col; num++) {
            System.out.print("   " + num);
        } // prints numbers at the bottom of grid

        System.out.println("\n");
    } //printMinefield()

     /**
     * Sets error message boolean values.
     */

    public void setErrorBooleans() {

        round++;
        command = false;
        command2 = false;
        validRow = true;
        validCol = true;
    } // setErrorBooleans()

    /**
     * Sets error message boolean values.
     */

    public void printNoSuchElementError() {
        System.err.println();
        System.err.println(errorMsg("command"));
        noElement = true;
        System.err.println("");
        return;
    } // printNoSuchElementError()

    /**
     * Prompts for user input.
     */

    public void promptUser() {
        System.out.print("minesweeper-alpha: ");
        try {
            this.makeCommandScanner();

        } catch (NullPointerException npe) {
            this.printNoSuchElementError();
        } catch (NoSuchElementException e) {
            this.printNoSuchElementError();
        }
        if (!noElement) {
            switch (this.scan) {

            case ("help"):
            case ("h"):
                this.printHelp();
                break;

            case ("mark"):
            case ("m"):
                this.setCommandError();
                this.checkCommandError();
                this.commandMarkMine();
                System.out.println("");
                break;

            case ("guess"):
            case ("g"):
                this.setCommandError();
                this.checkCommandError();
                this.commandGuessMine();
                System.out.println("");
                break;

            case ("quit"):
            case ("q"):
                this.quitGame();
                break;

            case ("reveal"):
            case ("r"):
                this.setCommandError();
                this.checkCommandError();
                this.commandRevealMine();
                break;

            case ("nofog"):
                this.noFog();
                break;

            default:
                System.err.println();
                System.err.println(this.errorMsg("command"));
            } // switch
        } // if
    } //promptUser()


    /**
     * Excutes markMine() if there are no errors.
     */

    public void commandMarkMine() {
        if (inBounds) {
            if (!command || !command2) {
                if (validRow && validCol) {
                    this.markMine(markRow, markCol);
                } // nested if
            } // nested if
        } // if
    } // commandGuessMine()

    /**
     * Excutes guessMine() if there are no errors.
     */

    public void commandGuessMine() {
        if (inBounds) {
            if (!command || !command2) {
                if (validRow && validCol) {
                    this.guessMine(markRow, markCol);
                } // nested if
            } // nested if
        } // if
    } // commandGuessMine()

    /**
      * Excutes revealMine() if there are no errors.
      */

    public void commandRevealMine() {
        if (inBounds) {
            if (!command || !command2) {
                if (validRow && validCol) {
                    this.revealMine(markRow, markCol);
                } // nested if
            } // nested if
        } // if
    } // commandRevealMine()

    /**
     * Sets if the command has errors. Will be checked later on.
     */

    public void setCommandError() {

        if  (this.isCommandValid()) {
            this.isInBounds(markRow, markCol);

            if (inBounds) {
                this.resetFog();
            } // nested if
        } // if
        // uses IsCommandValid() and IsInBounds(); used by promptUser()
    } // setCommandError()

    /**
     * Checks if command is valid. If it is, the command will execute.
     */
    public void checkCommandError() {
        if (command2) {
            System.err.println("");
            System.err.println(this.errorMsg("command2"));
            round--;
        } else if (command) {
            System.err.println("");
            System.err.println(this.errorMsg("command"));
            round--;
        }

        if (!validRow) {
            System.err.println("");
            System.err.println(this.errorMsg("bounds"));
            round--;
        } else if (!validCol) {
            System.err.println("");
            System.err.println(this.errorMsg("bounds2"));
            round--;
        } // if else

        setErrorBooleans(); // reset error booleans if there was an error
    } // checkCommandError()

    /**
     * Makes scanner out of player input.
     */

    public void makeCommandScanner() {
        String playerInput = this.stdIn.nextLine();
        playerInput = playerInput.trim();
        scanPlayer = new Scanner(playerInput);
        scan = scanPlayer.next();
        // used by userPrompt()
    } // makeCommandScanner()

    /**
     * Prints a consolatory  message to the user.
     */

    public void printLoss() {
        String L =
            " Oh no... You revealed a mine!" +
            "\n  __ _  __ _ _ __ ___   ___    _____   _____ _ __" +
            "\n / _` |/ _` | '_ ` _ \\ / _ \\  / _ \\ \\ / / _ \\ '__|" +
            "\n| (_| | (_| | | | | | |  __/ | (_) \\ V /  __/ |" +
            "\n \\__, |\\__,_|_| |_| |_|\\___|  \\___/ \\_/ \\___|_|" +
            "\n |___/ ";
        System.out.println(L);

    } //printLoss()

    /**
     * Prints a congratulatory message to the user.
     */

    public void printWin() {
        String dub =
            "\n ░░░░░░░░░▄░░░░░░░░░░░░░░▄░░░░ \"So Doge\"" +
            "\n ░░░░░░░░▌▒█░░░░░░░░░░░▄▀▒▌░░░" +
            "\n ░░░░░░░░▌▒▒█░░░░░░░░▄▀▒▒▒▐░░░ \"Such Score\"" +
            "\n ░░░░░░░▐▄▀▒▒▀▀▀▀▄▄▄▀▒▒▒▒▒▐░░░" +
            "\n ░░░░░▄▄▀▒░▒▒▒▒▒▒▒▒▒█▒▒▄█▒▐░░░ \"Much Minesweeping\"" +
            "\n ░░░▄▀▒▒▒░░░▒▒▒░░░▒▒▒▀██▀▒▌░░░" +
            "\n ░░▐▒▒▒▄▄▒▒▒▒░░░▒▒▒▒▒▒▒▀▄▒▒▌░░ \"Wow\"" +
            "\n ░░▌░░▌█▀▒▒▒▒▒▄▀█▄▒▒▒▒▒▒▒█▒▐░░" +
            "\n ░▐░░░▒▒▒▒▒▒▒▒▌██▀▒▒░░░▒▒▒▀▄▌░" +
            "\n ░▌░▒▄██▄▒▒▒▒▒▒▒▒▒░░░░░░▒▒▒▒▌░" +
            "\n ▀▒▀▐▄█▄█▌▄░▀▒▒░░░░░░░░░░▒▒▒▐░" +
            "\n ▐▒▒▐▀▐▀▒░▄▄▒▄▒▒▒▒▒▒░▒░▒░▒▒▒▒▌" +
            "\n ▐▒▒▒▀▀▄▄▒▒▒▄▒▒▒▒▒▒▒▒░▒░▒░▒▒▐░" +
            "\n ░▌▒▒▒▒▒▒▀▀▀▒▒▒▒▒▒░▒░▒░▒░▒▒▒▌░" +
            "\n ░▐▒▒▒▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▒▄▒▒▐░░" +
            "\n ░░▀▄▒▒▒▒▒▒▒▒▒▒▒░▒░▒░▒▄▒▒▒▒▌░░" +
            "\n ░░░░▀▄▒▒▒▒▒▒▒▒▒▒▄▄▄▀▒▒▒▒▄▀░░░ CONGRATULATIONS!" +
            "\n ░░░░░░▀▄▄▄▄▄▄▀▀▀▒▒▒▒▒▄▄▀░░░░░ YOU HAVE WON!" +
            "\n ░░░░░░░░░▒▒▒▒▒▒▒▒▒▒▀▀░░░░░░░░ SCORE: " +
            String.format("%5.2f", score());
        System.out.println(dub);
    } //printWin()

    /**
     * Reveals mine at certain square.
     * @param row Row user inputted.
     * @param col Col user inputted.
     */

    public void revealMine(int row, int col) {
        if (mineLocation[row][col] == true) {
            this.printLoss();
            System.exit(0);
            // if reveal a mine, loss the game
        } else {
            gridIcon[row][col] = " " + this.getNumNearbyMines(markRow, markCol) + " ";
        } // else reveal nearby mines
    } //revealMine()

    /**
     * Marks mine at a certain square.
     * @param row Row user inputted.
     * @param col Col user inputted.
     */

    public void markMine(int row, int col) {
        gridIcon[row][col] = " F ";
    } //markMine()


    /**
     * Guesses mine at a certain square.
     * @param row Row user inputted.
     * @param col Col user inputted.
     */

    public void guessMine(int row, int col) {
        gridIcon[row][col] = " ? ";
    } //guessMine()


    /**
     * Removes fog of war.
     */

    public void noFog() {

        fogCleared = true;

        for (int loopRow = 0; loopRow < this.row; loopRow++) {
            for (int loopCol = 0; loopCol < this.col; loopCol++) {
                if  (mineLocation[loopRow][loopCol] == true) {

                    if (gridIcon[loopRow][loopCol].equals(" F ")) {
                        gridIcon[loopRow][loopCol] = "<F>";
                    } else if (gridIcon[loopRow][loopCol].equals("   ")) {
                        gridIcon[loopRow][loopCol] = "< >";
                    } else if (gridIcon[loopRow][loopCol].equals(" ? ")) {
                        gridIcon[loopRow][loopCol] = "<?>";
                    } // else if
                } // outer if
            } // for loopCol
        } // for loopRow
        round++;
    } // noFog()

    /**
     * Prints help menu.
     */

    public void printHelp() {
        String help = "\n\nCommands Available...\n" +
            " - Reveal: r/reveal row col\n" +
            " -   Mark: m/mark   row col\n" +
            " -  Guess: g/guess  row col\n" +
            " -   Help: h/help\n" +
            " -   Quit: q/quit";

        System.out.println(help);
        round++;
        resetFog();
    } // printHelp

    /**
     * Displays parting message and quits the game.
     */

    public void quitGame() {
        System.out.println("");
        String bye = "Quitting the game...\nBye!";
        System.out.println(bye);
        System.exit(0);
    } //quitGame()

    /**
     * Method that loops prompting the user and printing the minefield.
     */

    public void play() {

        if (won) {
            System.exit(0);
        }

        while (!won) {
            this.promptUser();
            this.gameWon();
            this.printMinefield();

            noElement = false;
        } // while

    } // play()

    /**
     * Reads values of the seed file.
     */

    public void readSeed() {

        try {
            File configFile = new File(this.seedPath);
            Scanner configScanner = new Scanner(configFile);
            int seedRow = configScanner.nextInt();
            int seedCol = configScanner.nextInt();

            if (seedRow < 5 || seedRow > 10) {
                System.err.println();
                System.err.println(this.errorMsg("malform1"));
                System.exit(3);
            } else if (seedCol < 5 || seedCol > 10) {
                System.err.println();
                System.err.println(this.errorMsg("malform1"));
                System.exit(3);
            } else {
                this.row = seedRow;
                this.col = seedCol;
            }
            // if the seed file rows or cols are out of bounds prints error.
            // Otherwise sets the object row/col to those values
            this.setGridIcons();

            numOfMines = configScanner.nextInt();
            if (numOfMines < 1 || numOfMines > (row * col) - 1) {
                System.err.println();
                System.err.println(this.errorMsg("malform2"));
                System.exit(3);
            }
            mineLocation = new boolean[this.row][this.col];

            for (int loopRow = 0; loopRow < this.row; loopRow++) {
                for (int loopCol = 0; loopCol < this.col; loopCol++) {
                    mineLocation[loopRow][loopCol] = false;
                }
            }
            for (int loop = 0;loop < numOfMines;loop++) {
                mineLocation[configScanner.nextInt()][configScanner.nextInt()] = true;
            }

        } catch (FileNotFoundException e) {

            System.err.println();
            System.err.println(errorMsg("found"));
            System.exit(2);
            // handles the exception here
            // prints error message and exits
        } // try

    }    //readSeed()

    /**
     * Returns 1 of 8 error messages.
     *
     * @param errorType
     * @return err String containing the error message
     */

    public String errorMsg(String errorType) {
        String malformError = "Seedfile Malformed Error: Cannot create a mine field" +
            " with that many rows and/or columns!";
        String notFoundError = "Seedfile Not Found Error: No file found in the given seed path.";
        String invalidUsageError = "Usage: MinesweeperDriver SEED_FILE_PATH";
        String invalidCommandError = "\nInvalid Command: Could not recognize command. Type "
            + "\"help\"" + " to see accepted commands.";
        String invalidCommandError2 = "\nInvalid Command: null";
        String outOfBounds = "\nInvalid Command: Index " + markRow + " is out of bounds for length "
            + row;
        String outOfBounds2 = "\nInvalid Command: Index " + markCol + " is out of bounds for length"
            + " " + col;
        String mineCount = "\nSeedfile Malformed Error: Invalid mine count";
        String err = "";

        switch (errorType) {
        case ("malform1"):
            err =  malformError;
        case ("malform2"):
            err =  mineCount;
            break;

        case ("found"):
            err = notFoundError;
            break;

        case ("usage"):
            err = invalidUsageError;
            break;

        case ("command"):
            err = invalidCommandError;
            break;

        case ("command2"):
            err = invalidCommandError2;
            break;

        case ("bounds"):
            err = outOfBounds;
            break;

        case ("bounds2"):
            err = outOfBounds2;
            break;

            // used to call error messages efficiently
        } // switch
        return err;
    } // errorMsg

    /**
     * Returns the computed score.
     * @return score Users score after winning the game.
     */

    public double score() {
        double score = (row * col * 100.0) / round;

        return score;
    }

    /**
     * Returns the string value of blank square (# of nearby mines) once it has been revealed.
     *
     * @param loopRow index of the grid the loop is currently at
     * @param loopCol index of the grid the loop is currently at
     * @return string value of # of nearby mines. Also identical to gridIcon of revealed square.
     */

    public String getNearbyMinesString(int loopRow, int loopCol) {
        String nearbyMines = "";
        switch (this.getNumNearbyMines(loopRow, loopCol)) {

        case 0:
            nearbyMines = " 0 ";
            break;

        case 1:
            nearbyMines = " 1 ";
            break;

        case 2:
            nearbyMines = " 2 ";
            break;

        case 3:
            nearbyMines = " 3 ";
            break;

        case 4:
            nearbyMines = " 4 ";
            break;

        case 5:
            nearbyMines = " 5 ";
            break;

        case 6:
            nearbyMines = " 6 ";
            break;
        case 7:
            nearbyMines = " 7 ";
            break;
        case 8:
            nearbyMines = " 8 ";
            break;
        }
        return nearbyMines;
    } // getNearbyMines()

    /**
     * Determines if the player has won the game.
     */

    public void gameWon() {

        int winCondition1 = 0;
        int winCondition2 = 0;
        for (int loopRow = 0; loopRow < this.row; loopRow++) {
            for (int loopCol = 0; loopCol < this.col; loopCol++) {

                if  (mineLocation[loopRow][loopCol] == true) {
                    if (gridIcon[loopRow][loopCol].equals(" F ")) {
                        winCondition1++;

                    } // nested if
                } // if
            } // nested for
        } // outer for
        // Checks if all mines have flag


        for (int loopRow = 0; loopRow < this.row; loopRow++) {
            for (int loopCol = 0; loopCol < this.col; loopCol++) {

                if  (mineLocation[loopRow][loopCol] == false) {
                    if (gridIcon[loopRow][loopCol].equals(getNearbyMinesString(loopRow, loopCol))) {

                        winCondition2++;
                    } // nested if
                } // if
            } // nested for
        }   // outer for

        if (winCondition1 == numOfMines && winCondition2 == ((row * col) - numOfMines)) {
            won = true;
            // winCondition2 set equal to the number of squares without a mine
        } // if
        // Checks if all empty squares have been revealed

        if (won) {
            this.printWin();
            System.exit(0);
        } // if
    } // gameWon()

    /**
     * Checks square to the right for a mine.
     *
     * @param row Row index of square in question.
     * @param col Column index of square in question.
     * @return mines Number of mines in the square to the right of target square.
     */

    public int minesToRight(int row, int col) {
        int mines = 0;
        if (mineLocation[row][col + 1] == true) {
            mines++;
        } // if
        return mines;
    } // minesToRight()

    /**
     * Checks square to the left for a mine.
     *
     * @param row Row index of square in question.
     * @param col Column index of square in question.
     * @return mines Number of mines in the square to the right of target square.
     */

    public int minesToLeft(int row, int col) {
        int mines = 0;
        if (mineLocation[row][col - 1] == true) {
            mines++;
        } // if
        return mines;
    } // minesToLeft()


    /**
     * Returns the number of mines surrounding the specified square.
     *
     * @param userRow the row index of the square the user inputted
     * @param userCol the column index of the square the user inputted
     * @return the number of adjacent mines
     */

    private int getNumNearbyMines(int userRow, int userCol) {
        int loopRow = userRow;
        int loopCol = userCol;
        int maxLoopCol = loopCol + 1;
        int nearbyMines = 0;
        if (loopRow == 0) { // if top row

            if (loopCol == 0) { // first col
                nearbyMines += this.minesToRight(loopRow, loopCol);
                nearbyMines += this.calcNumAdjMines1(loopRow + 1, loopCol, maxLoopCol + 1);

            } else  if (loopCol == (this.col - 1)) { // last col
                nearbyMines += this.minesToLeft(loopRow, loopCol);
                nearbyMines += this.calcNumAdjMines1(loopRow + 1, loopCol - 1, maxLoopCol);
            } else if (loopCol > 0 && loopCol < (this.col - 1))  { // middle cols
                nearbyMines += this.calcNumAdjMines2(loopRow, loopCol);
                nearbyMines += this.calcNumAdjMines1(loopRow + 1, loopCol - 1, maxLoopCol + 1);
            }

        }

        if (loopRow == (this.row - 1)) { // if bottom row
            if (loopCol == 0) { // first col
                nearbyMines += this.minesToRight(loopRow, loopCol);
                nearbyMines += this.calcNumAdjMines1(loopRow - 1, loopCol , maxLoopCol + 1);

            }  else if (loopCol == (this.col - 1)) { // last col
                nearbyMines += this.minesToLeft(loopRow, loopCol);
                nearbyMines += this.calcNumAdjMines1(loopRow - 1, loopCol - 1, maxLoopCol); //

            } else if (loopCol > 0 && loopCol < (this.col - 1)) { // middle cols
                nearbyMines += this.calcNumAdjMines1(loopRow - 1, loopCol - 1, maxLoopCol + 1);
                nearbyMines += this.calcNumAdjMines2(loopRow , loopCol);

            }
        }
        if (loopRow > 0 && loopRow < (this.row - 1)) { // if middle row

            if (loopCol == 0) { // // first col
                nearbyMines += this.minesToRight(loopRow, loopCol);
                nearbyMines += this.calcNumAdjMines1(loopRow - 1, loopCol , maxLoopCol + 1);
                nearbyMines += this.calcNumAdjMines1(loopRow + 1 , loopCol , maxLoopCol + 1);

            } else if (loopCol == this.col - 1) { //  last col
                nearbyMines += this.minesToLeft(loopRow, loopCol);
                nearbyMines += this.calcNumAdjMines1(loopRow - 1, loopCol - 1 , maxLoopCol );
                nearbyMines += this.calcNumAdjMines1(loopRow + 1, loopCol - 1 , maxLoopCol );

            } else if ( loopCol > 0 && loopCol < this.col - 1) { // middle cols
                nearbyMines += this.calcNumAdjMines1(loopRow - 1, loopCol - 1, maxLoopCol + 1);
                nearbyMines += this.calcNumAdjMines1(loopRow + 1, loopCol - 1, maxLoopCol + 1 );
                nearbyMines += this.calcNumAdjMines2(loopRow , loopCol);
            }
        }
        // fyi because the first calc mines call only reads left to, the number of times is
        // determined by the specific call
        // last else if bracket
        return nearbyMines;
    } //getNumNearbyMines()


    /**
     * Method that checks and logs nearby mines. Reads mines in a row left to right.
     *
     * @param loopRow User inputted row; Will be used in array loop.
     * @param loopCol User inputted col; Will be used in array loop.
     * @param maxLoopCol 1 more than user inputted col. Standard for end of loop.
     * @return calNumAdjMines1 Number of nearby mines found with this method.
     */
    private int calcNumAdjMines1(int loopRow, int loopCol, int maxLoopCol) {
        int nearbyMines = 0;
        int inLoopRow = loopRow;
        int inLoopCol;
        int inLoopMaxCol = maxLoopCol;

        for (inLoopCol = loopCol; inLoopCol <  maxLoopCol; inLoopCol++)  {

            if (mineLocation[inLoopRow][inLoopCol]  == true) {
                nearbyMines += 1;
            } // if
        } // for
        return nearbyMines;
    } // calcNumAdjMines1()

    /**
     * Checks and logs mines both to left and right of target spot.
     *
     * @param loopRow User inputted row; Will be used in array loop.
     * @param loopCol User inputted col; Will be used in array loop.
     * @return calcNumAdjMines2 Number of nearby mines found with this method.
     */

    private int calcNumAdjMines2(int loopRow, int loopCol) {
        int nearbyMines = 0;
        if (mineLocation[loopRow][loopCol + 1] == true) {
            nearbyMines++;
        } // for mines to right

        if (mineLocation[loopRow][loopCol - 1] == true) {
            nearbyMines++;
        } // for mines to left
        return nearbyMines;
    } // calcNumAdjMines2()

    /**
     * Indicates whether or not the square is on the game grid.
     *
     * @param row the row index of the square
     * @param col the column index of the square
     * @return true if the square is in the game grid; false otherwise
     */

    private boolean isInBounds(int row, int col) {
        inBounds = true;

        if (row > this.row - 1 || row < 0) {
            inBounds = false;
            validRow = false;
        } else if (col > this.col - 1 || col < 0) {
            inBounds = false;
            validCol = false;
        }
        return inBounds;
    } //isInBounds()

    /**
     * Initializes array String values (gridIcons) to 3 blanks.
     */

    private void setGridIcons() {

        if (fogCleared == false) {
            gridIcon =  new String[this.row][this.col];
            for (int loopRow = 0; loopRow < this.row; loopRow++) {
                for (int loopCol = 0; loopCol < this.col; loopCol++) {
                    gridIcon[loopRow][loopCol] = "   ";
                } // for loopRow
            }
        } else {
            for (int loopRow = 0; loopRow < this.row; loopRow++) {
                for (int loopCol = 0; loopCol < this.col; loopCol++) {

                    if (gridIcon[loopRow][loopCol].equals("<F>")) {
                        gridIcon[loopRow][loopCol] = " F ";
                    } else if (gridIcon[loopRow][loopCol].equals("< >")) {
                        gridIcon[loopRow][loopCol] = "   ";
                    } else if (gridIcon[loopRow][loopCol].equals("<?>")) {
                        gridIcon[loopRow][loopCol] = " ? ";
                    } // else if
                } // for loopCol
            } // for loopRow
        } // else
    } // setGridIcons()

    /**
     * Reads if input command is valid.
     * @return valid Boolean value for if the input was valid
     */

    public boolean isCommandValid() {

        boolean valid = false;

        if (scanPlayer.hasNextInt()) {
            markRow = scanPlayer.nextInt();
        } else {
            command2 = true;;
        } // else

        if (scanPlayer.hasNextInt()) {
            markCol = scanPlayer.nextInt();

            if (scanPlayer.hasNextInt()) {
                command = true;
            } else {
                valid = true;
            } // if
        } else {
            command2 = true;
        } // else

        // sets markRow amd markCol to int values if correctly provided in seed file
        // otherwiser, sets error booleans to true

        return valid;
    }  // isCommandReader()

    /**
     * Resets grid after fog is lifted.
     *
     */

    public void resetFog() {
        if (fogCleared) {
            this.setGridIcons();
            fogCleared = false;

            // if fog is cleared, removes brackets indicating mines and updates its boolean value
        }
    } // resetFog()
} // Minesweeper

import java.util.*;

// Ship class to represent each ship with name, size, and hit tracking
class Ship {
    String name;
    int size;
    int hitCount;

    Ship(String name, int size) {
        this.name = name;
        this.size = size;
        this.hitCount = 0;
    }

    boolean isSunk() {
        return hitCount >= size;
    }
}

// Board class to represent the 10x10 game grid and ship placements
class Board {
    char[][] grid = new char[10][10];
    Map<String, Ship> ships = new HashMap<>();

    Board() {
        for (char[] row : grid) {
            Arrays.fill(row, '~'); // '~' represents water
        }
    }

    // Places ship on board if space is valid and not overlapping
    boolean placeShip(String name, int size, int row, int col, boolean horizontal) {
        if (horizontal) {
            if (col + size > 10) return false;
            for (int i = 0; i < size; i++) {
                if (grid[row][col + i] != '~') return false;
            }
            for (int i = 0; i < size; i++) {
                grid[row][col + i] = name.charAt(0);
            }
        } else {
            if (row + size > 10) return false;
            for (int i = 0; i < size; i++) {
                if (grid[row + i][col] != '~') return false;
            }
            for (int i = 0; i < size; i++) {
                grid[row + i][col] = name.charAt(0);
            }
        }

        ships.put(name, new Ship(name, size));
        return true;
    }

    // Handles an attack at a given position
    boolean attack(int row, int col) {
        char cell = grid[row][col];

        if (cell == '~') {
            grid[row][col] = 'M'; // Miss
            return false;
        } else if (cell == 'X' || cell == 'M') {
            return false; // Already hit or miss
        } else {
            grid[row][col] = 'X'; // Hit
            for (Ship ship : ships.values()) {
                if (ship.name.charAt(0) == cell) {
                    ship.hitCount++;
                    break;
                }
            }
            return true;
        }
    }

    // Checks if all ships have been sunk
    boolean allShipsSunk() {
        for (Ship ship : ships.values()) {
            if (!ship.isSunk()) {
                return false;
            }
        }
        return true;
    }

    // Displays the board, optionally hiding ships
    void print(boolean hideShips) {
        System.out.print("  ");
        for (int i = 0; i < 10; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < 10; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < 10; j++) {
                char c = grid[i][j];
                if (hideShips && c != '~' && c != 'X' && c != 'M') {
                    System.out.print("~ ");
                } else {
                    System.out.print(c + " ");
                }
            }
            System.out.println();
        }
    }
}

public class BattleshipGame {
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("Select mode: 1 for Single Player, 2 for Two Player: ");
        int mode = scanner.nextInt();

        Board player1Board = new Board();
        Board player2Board = new Board();

        System.out.println("\nPlayer 1, place your ships:");
        placeAllShips(player1Board, false);

        if (mode == 1) {
            // Single-player: Computer is opponent
            System.out.println("\nComputer placing ships...");
            placeAllShips(player2Board, true);
        } else {
            // Two-player mode
            System.out.println("\nPlayer 2, place your ships:");
            placeAllShips(player2Board, false);
        }

        boolean player1Turn = true;

        while (true) {
            Board attackerBoard = player1Turn ? player1Board : player2Board;
            Board targetBoard = player1Turn ? player2Board : player1Board;
            String attackerName = player1Turn ? "Player 1" : (mode == 1 ? "Computer" : "Player 2");

            System.out.println("\n" + attackerName + "'s Turn:");

            if (attackerName.equals("Computer")) {
                // Computer turn logic
                Random rand = new Random();
                int cr, cc;
                do {
                    cr = rand.nextInt(10);
                    cc = rand.nextInt(10);
                } while (targetBoard.grid[cr][cc] == 'X' || targetBoard.grid[cr][cc] == 'M');

                System.out.println("Computer attacks: " + cr + " " + cc);
                if (targetBoard.attack(cr, cc)) {
                    System.out.println("Computer hit a ship!");
                } else {
                    System.out.println("Computer missed.");
                }
            } else {
                // Player turn logic
                targetBoard.print(true);
                System.out.print("Enter your attack (row col): ");
                int r = scanner.nextInt();
                int c = scanner.nextInt();

                if (targetBoard.attack(r, c)) {
                    System.out.println("Hit!");
                } else {
                    System.out.println("Miss.");
                }
            }

            // Check for winner
            if (targetBoard.allShipsSunk()) {
                System.out.println("\n" + attackerName + " wins!");
                break;
            }

            player1Turn = !player1Turn;
        }
    }

    // Places all ships on the board, either manually or randomly
    static void placeAllShips(Board board, boolean random) {
        int[] sizes = {5, 4, 3, 3, 2};
        String[] names = {"Carrier", "Battleship", "Cruiser", "Submarine", "Destroyer"};
        Random rand = new Random();

        for (int i = 0; i < sizes.length; i++) {
            boolean placed = false;
            while (!placed) {
                int row = random ? rand.nextInt(10) : getCoord("row for " + names[i]);
                int col = random ? rand.nextInt(10) : getCoord("col for " + names[i]);
                boolean horizontal = random ? rand.nextBoolean() : getDirection();

                placed = board.placeShip(names[i], sizes[i], row, col, horizontal);
                if (!placed && !random) {
                    System.out.println("Invalid placement, try again.");
                }
            }
        }
    }

    // Get coordinate input from player
    static int getCoord(String prompt) {
        System.out.print("Enter " + prompt + ": ");
        return scanner.nextInt();
    }

    // Get direction input from player (horizontal or vertical)
    static boolean getDirection() {
        System.out.print("Horizontal? (true/false): ");
        return scanner.nextBoolean();
    }
}

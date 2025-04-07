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

    boolean attack(int row, int col) {
        char cell = grid[row][col];

        if (cell == '~') {
            grid[row][col] = 'M';
            return false;
        } else if (cell == 'X' || cell == 'M') {
            return false;
        } else {
            grid[row][col] = 'X';
            for (Ship ship : ships.values()) {
                if (ship.name.charAt(0) == cell) {
                    ship.hitCount++;
                    break;
                }
            }
            return true;
        }
    }

    boolean allShipsSunk() {
        for (Ship ship : ships.values()) {
            if (!ship.isSunk()) {
                return false;
            }
        }
        return true;
    }

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
        int mode = 0;
        while (mode < 1 || mode > 3) {
            System.out.print("Select mode: 1 for Single Player, 2 for Two Player, 3 for Easy AI: ");
            if (scanner.hasNextInt()) {
                mode = scanner.nextInt();
                if (mode < 1 || mode > 3) {
                    System.out.println("Invalid input. Please enter 1, 2, or 3.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }

        Board player1Board = new Board();
        Board player2Board = new Board();

        System.out.println("\nPlayer 1, place your ships:");
        placeAllShips(player1Board, false);

        if (mode == 1 || mode == 3) {
            System.out.println("\nComputer placing ships...");
            placeAllShips(player2Board, true);
        } else {
            System.out.println("\nPlayer 2, place your ships:");
            placeAllShips(player2Board, false);
        }

        boolean player1Turn = true;

        while (true) {
            Board attackerBoard = player1Turn ? player1Board : player2Board;
            Board targetBoard = player1Turn ? player2Board : player1Board;
            String attackerName;

            if (player1Turn) {
                attackerName = "Player 1";
            } else if (mode == 1 || mode == 3) {
                attackerName = "Computer";
            } else {
                attackerName = "Player 2";
            }

            System.out.println("\n" + attackerName + "'s Turn:");

            if (attackerName.equals("Computer")) {
                Random rand = new Random();
                int cr = 0, cc = 0;

                // Easy AI: Attacks randomly
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
                targetBoard.print(true);
                int r = -1, c = -1;
                boolean validInput = false;
                while (!validInput) {
                    System.out.print("Enter your attack (row col): ");
                    if (scanner.hasNextInt()) {
                        r = scanner.nextInt();
                        if (scanner.hasNextInt()) {
                            c = scanner.nextInt();
                            if (r >= 0 && r < 10 && c >= 0 && c < 10) {
                                validInput = true;
                            } else {
                                System.out.println("Invalid coordinates. Enter numbers between 0 and 9.");
                            }
                        } else {
                            System.out.println("Invalid input. Please enter two integers.");
                            scanner.next();
                        }
                    } else {
                        System.out.println("Invalid input. Please enter two integers.");
                        scanner.next();
                    }
                }

                if (targetBoard.attack(r, c)) {
                    System.out.println("Hit!");
                } else {
                    System.out.println("Miss.");
                }
            }

            if (targetBoard.allShipsSunk()) {
                System.out.println("\n" + attackerName + " wins!");
                break;
            }

            player1Turn = !player1Turn;
        }
    }

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

    static int getCoord(String prompt) {
        int coord = -1;
        while (coord < 0 || coord >= 10) {
            System.out.print("Enter " + prompt + ": ");
            if (scanner.hasNextInt()) {
                coord = scanner.nextInt();
                if (coord < 0 || coord >= 10) {
                    System.out.println("Please enter a number between 0 and 9.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
        return coord;
    }

    static boolean getDirection() {
        while (true) {
            System.out.print("Horizontal? (true/false): ");
            if (scanner.hasNextBoolean()) {
                return scanner.nextBoolean();
            } else {
                System.out.println("Invalid input. Please enter true or false.");
                scanner.next();
            }
        }
    }
}

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;
import java.lang.System;

public class Main {

    public static void main(String[] args) {
        System.out.println("Добро пожаловать в игру 'XO'");
        System.out.println("Как играть: вводите число исходя из игрового поля начиная с нуля. Как вы поставите три креста в любом из направлений вы победите!");
        System.out.println("Удачи!!!");
        Grid grid = new Grid();
        grid.initGrid();
    }
}

class Grid {
    char[][] grid = new char[3][3];
    int initLogicCount = 0;
    int initLogicCountFreeCell = 0;

    public void initGrid() {
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 3; k++) {
                grid[i][k] = '.';
            }
        }
        Logic logic = new Logic();
        logic.printGrid(grid, initLogicCount, initLogicCount);
        logic.choice(grid, initLogicCount, initLogicCount);
    }
}

class Logic {
    boolean winStatus = false;

    public void printGrid(char[][] grid, int winPlayerCount, int winBotCount) {
        System.out.println("  012");
        int freeCell = 0;
        int steep = 0;
        for (int i = 0; i < 3; i++) {
            steep = steep + 1;
            for (int k = 0; k < 3; k++) {
                if (grid[i][k] == '.') {
                    freeCell = freeCell + 1;
                }
                if (steep == 1) {
                    System.out.print(i + " ");
                }
                steep = 0;
                System.out.print(grid[i][k]);
                if (k == 2) {
                    System.out.println(" ");
                }
            }
        }
    }

    public int checkFreeCell(char[][] grid) {
        int freeCell = 0;
        for (int i = 0; i < 3; i++) {
            for (int k = 0; k < 3; k++) {
                if (grid[i][k] == '.') {
                    freeCell = freeCell + 1;
                }
            }
        }
        return freeCell;
    }


    public void checkWin(char[][] grid, int freeCell, int winPlayerCount, int winBotCount) {
        if (!getWinStatus()) {
            if (freeCell == 0) {
                retry(grid);
                setWinStatus();
            } else if (winPlayerCount == 3) {
                win(grid);
                setWinStatus();
            } else if (winBotCount == 3) {
                winBot(grid);
                setWinStatus();
            }
        }
    }

    void setWinStatus() {
        this.winStatus = true;
    }

    boolean getWinStatus() {
        return winStatus;
    }

    public void initChoisePlayer(char[][] grid, int freeCell, int winPlayerCount, int winBotCount) {
        choice(grid, winPlayerCount, winBotCount);
    }

    public void choice(char[][] grid, int winPlayerCount, int winBotCount) {
        if (!getWinStatus()) {
            try {
                System.out.println("Выберите ячейку");
                Scanner scanner = new Scanner(System.in);
                int firstGridIndex = scanner.nextInt();
                if (firstGridIndex > 2) {
                    throw new Exception("Вводить требуется числа от 0 до 2, исходя из координат расположения ячейки.");
                }
                int secondGridIndex = scanner.nextInt();
                if (grid[firstGridIndex][secondGridIndex] == '.') {
                    grid[firstGridIndex][secondGridIndex] = 'X';
                } else {
                    System.out.println("Данная клетка занята!");
                    choice(grid, winPlayerCount, winBotCount);
                }

            } catch (ArrayIndexOutOfBoundsException exception) {
                System.out.println("Вводить следует построчно: сначала номер строки, а потом столбца. Отсчёт начинаетcя с 0.");
                choice(grid, winPlayerCount, winBotCount);
            } catch (InputMismatchException exception) {
                System.out.println("Вводить следует цифры и построчно: сначала номер строки, а потом столбца. Отсчёт начинаетcя с 0.");
                choice(grid, winPlayerCount, winBotCount);
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                choice(grid, winPlayerCount, winBotCount);
            }
            printGrid(grid, winPlayerCount, winBotCount);
            winningVertical(grid);
            winningHorizontal(grid);
            winnnigDiagonalRight(grid);
            winningDiagonalLeft(grid);
            botRandomChoice(grid, winPlayerCount, winBotCount);
        }
    }

    public void botRandomChoice(char[][] grid, int winPlayerCount, int winBotCount) {
        if (!getWinStatus()) {
            Random random = new Random();
            int firstGridIndex = random.nextInt(3);
            int secondGridIndex = random.nextInt(3);
            if (grid[firstGridIndex][secondGridIndex] == '.') {
                grid[firstGridIndex][secondGridIndex] = 'O';
                printGrid(grid, winPlayerCount, winBotCount);
                winningVertical(grid);
                winningHorizontal(grid);
                winnnigDiagonalRight(grid);
                winningDiagonalLeft(grid);

                choice(grid, winPlayerCount, winBotCount);
            } else {
                botRandomChoice(grid, winPlayerCount, winBotCount);
            }
        }
    }

    public void winningVertical(char[][] grid) {
        int winPlayerCount;
        int winBotCount;
        int y = 0;
        for (int k = 0; k < 3; k++) {
            winPlayerCount = 0;
            winBotCount = 0;
            for (int i = 0; i < 3; i++) {
                if (grid[i][k] == 'X') {
                    winPlayerCount = winPlayerCount + 1;
                } else if (grid[i][k] == 'O') {
                    winBotCount = winBotCount + 1;
                }
                y = y + 1;
                if (winPlayerCount == 3) {
                    checkWin(grid, checkFreeCell(grid), winPlayerCount, winBotCount);
                } else if (winBotCount == 3) {
                    checkWin(grid, checkFreeCell(grid), winPlayerCount, winBotCount);
                } else if (y == 3) {
                    y = 0;
                }
            }
        }
    }

    public void winningHorizontal(char[][] grid) {
        int winPlayerCount;
        int winBotCount;
        for (int i = 0; i < 3; i++) {
            winPlayerCount = 0;
            winBotCount = 0;
            for (int k = 0; k < 3; k++) {
                if (grid[i][k] == 'X') {
                    winPlayerCount = winPlayerCount + 1;
                } else if (grid[i][k] == 'O') {
                    winBotCount = winBotCount + 1;
                }
                if (winPlayerCount == 3) {
                    checkWin(grid, checkFreeCell(grid), winPlayerCount, winBotCount);
                } else if (winBotCount == 3) {
                    checkWin(grid, checkFreeCell(grid), winPlayerCount, winBotCount);
                }
            }
        }
    }

    public void winnnigDiagonalRight(char[][] grid) {
        int winPlayerCount = 0;
        int winBotCount = 0;
        int k = 2;
        for (int i = 0; i < 3; i++) {
            if (grid[i][k] == 'X') {
                winPlayerCount = winPlayerCount + 1;
            } else if (grid[i][k] == 'O') {
                winBotCount = winBotCount + 1;
            }
            k = k - 1;
        }
        if (winPlayerCount == 3) {
            checkWin(grid, checkFreeCell(grid), winPlayerCount, winBotCount);
        } else if (winBotCount == 3) {
            checkWin(grid, checkFreeCell(grid), winPlayerCount, winBotCount);
        }
    }

    public void winningDiagonalLeft(char[][] grid) {
        int winPlayerCount = 0;
        int winBotCount = 0;
        for (int i = 0; i < 3; i++) {
            if (grid[i][i] == 'X') {
                winPlayerCount = winPlayerCount + 1;
            } else if (grid[i][i] == 'O') {
                winBotCount = winBotCount + 1;
            }
            if (winPlayerCount == 3) {
                checkWin(grid, checkFreeCell(grid), winPlayerCount, winBotCount);
            } else if (winBotCount == 3) {
                checkWin(grid, checkFreeCell(grid), winPlayerCount, winBotCount);
            }
        }
    }

    public void win(char[][] grid) {
        System.out.println("Вы выиграли! Поздравляем!!!");
        System.out.println("......");
        System.out.println("Игра завершена!");
        Scanner scanner = new Scanner(System.in);
        String gameOverStopScanner = scanner.nextLine();
    }

    public void winBot(char[][] grid) {
        System.out.println("Вы проиграли!!!");
        System.out.println("......");
        System.out.println("Игра завершена!");
        Scanner scanner = new Scanner(System.in);
        String gameOverStopScanner = scanner.nextLine();
    }

    public void retry(char[][] grid) {
        System.out.println("Ничья. Свободные ячейки кончились.");
        System.out.println("......");
        System.out.println("Игра завершена!");
        Scanner scanner = new Scanner(System.in);
        String gameOverStopScanner = scanner.nextLine();
    }

}


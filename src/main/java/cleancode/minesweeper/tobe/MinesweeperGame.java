package cleancode.minesweeper.tobe;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {


    public static final int BOARD_LOW_SIZE = 8;
    public static final int BOARD_COL_SIZE = 10;
    private static final Cell[][] BOARD = new Cell[BOARD_LOW_SIZE][BOARD_COL_SIZE];
    public static final int LAND_MINE_COUNT = 10;
    public static final Scanner SCANNER = new Scanner(System.in);

    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        showGameStartComments();
        initializeGame();

        while (true) {
            try {
                showBoard();

                if (doesUserWinTheGame()) {
                    System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                    break;
                }
                if (doseUserLoseTheGame()) {
                    System.out.println("지뢰를 밟았습니다. GAME OVER!");
                    break;
                }

                String cellInput = getCellInputFromUser();
                String userActionInput = getUserActionInputFromUser();
                actOnCell(cellInput, userActionInput);
            }catch (AppException e){
                System.out.println(e.getMessage());
            }catch (Exception e){
                System.out.println("프로그램에 문제가 생겼습니다.");
            }
        }
    }

    private static void actOnCell(String cellInput, String userActionInput) {
        int selectedColIndex = getSelectedColIndex(cellInput);
        int selectedRowIndex = getSelectedRowIndex(cellInput);

        if (doseUserChooseToPlantFlag(userActionInput)) {
            BOARD[selectedRowIndex][selectedColIndex].flag();
            checkIfGameIsOver();
            return;
        }

        if (doseUserChooseToOpenCell(userActionInput)) {
            if (isLandMineCell(selectedRowIndex, selectedColIndex)) {
                BOARD[selectedRowIndex][selectedColIndex].open();
                changeGameStatusToLose();
                return;
            }

            open(selectedRowIndex, selectedColIndex);
            checkIfGameIsOver();
            return;
        }
        System.out.println("잘못된 번호를 선택하셨습니다.");
    }

    private static void changeGameStatusToLose() {
        gameStatus = -1;
    }

    private static boolean isLandMineCell(int selectedRowIndex, int selectedColIndex) {
        return BOARD[selectedRowIndex][selectedColIndex].isLandMine();
    }

    private static boolean doseUserChooseToOpenCell(String userActionInput) {
        return userActionInput.equals("1");
    }

    private static boolean doseUserChooseToPlantFlag(String userActionInput) {
        return userActionInput.equals("2");
    }

    private static int getSelectedRowIndex(String cellInput) {
        char cellInputRow = cellInput.charAt(1);
        return convertRowFrom(cellInputRow);
    }

    private static int getSelectedColIndex(String cellInput) {
        char cellInputCol = cellInput.charAt(0);
        return convertColFrom(cellInputCol);
    }

    private static String getUserActionInputFromUser() {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
        return SCANNER.nextLine();
    }

    private static String getCellInputFromUser() {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
        return SCANNER.nextLine();
    }

    private static boolean doseUserLoseTheGame() {
        return gameStatus == -1;
    }

    private static boolean doesUserWinTheGame() {
        return gameStatus == 1;
    }

    private static void checkIfGameIsOver() {
        boolean isAllChecked = isAllCellChecked();
        if (isAllChecked) {
            changeGameStatusToWin();
        }
    }

    private static void changeGameStatusToWin() {
        gameStatus = 1;
    }

    private static boolean isAllCellChecked() {
        return Arrays.stream(BOARD)
                .flatMap(Arrays::stream)
                //비교를 할때 null이 아닌 값과 비교를 하는게 좋다 cell.equals(CLOSED_CELL_SIGN)이것보다
                //cell.equals(CLOSED_CELL_SIGN)여기서 cell은 null일 가능성이 있는 값이기 때문에 비교를 했을때 NPE가 터질수 있음
                .allMatch(Cell::isChecked);
    }

    private static int convertRowFrom(char cellInputRow) {
        int rowIndex = Character.getNumericValue(cellInputRow) - 1;
        if(rowIndex >= BOARD_LOW_SIZE) {
            throw new AppException("잘못된 입력입니다");
        }
        return rowIndex;
    }

    private static int convertColFrom(char cellInputCol) {
        int selectedColIndex;
        switch (cellInputCol) {
            case 'a':
                selectedColIndex = 0;
                break;
            case 'b':
                selectedColIndex = 1;
                break;
            case 'c':
                selectedColIndex = 2;
                break;
            case 'd':
                selectedColIndex = 3;
                break;
            case 'e':
                selectedColIndex = 4;
                break;
            case 'f':
                selectedColIndex = 5;
                break;
            case 'g':
                selectedColIndex = 6;
                break;
            case 'h':
                selectedColIndex = 7;
                break;
            case 'i':
                selectedColIndex = 8;
                break;
            case 'j':
                selectedColIndex = 9;
                break;
            default:
                throw new AppException("잘못된 입력입니다.");
        }
        return selectedColIndex;
    }

    private static void showBoard() {
        System.out.println("   a b c d e f g h i j");
        for (int row = 0; row < BOARD_LOW_SIZE; row++) {
            System.out.printf("%d  ", row + 1);
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                System.out.print(BOARD[row][col].getSign() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void initializeGame() {
        for (int row = 0; row < BOARD_LOW_SIZE; row++) {
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                BOARD[row][col] = Cell.create();
            }
        }

        for (int i = 0; i < LAND_MINE_COUNT; i++) {
            int col = new Random().nextInt(BOARD_COL_SIZE);
            int row = new Random().nextInt(BOARD_LOW_SIZE);
            BOARD[row][col].turnOnLandMine();
        }

        for (int row = 0; row < BOARD_LOW_SIZE; row++) {
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                if (isLandMineCell(row, col)) {
                    continue;
                }
                int count = countNearbyLandMines(row, col);
                BOARD[row][col].updateNearbyLandMineCount(count);
            }
        }
    }

    private static int countNearbyLandMines(int row, int col) {
        int count = 0;
        if (row - 1 >= 0 && col - 1 >= 0 && isLandMineCell(row - 1, col - 1)) {
            count++;
        }
        if (row - 1 >= 0 && isLandMineCell(row - 1, col)) {
            count++;
        }
        if (row - 1 >= 0 && col + 1 < BOARD_COL_SIZE && isLandMineCell(row - 1, col + 1)) {
            count++;
        }
        if (col - 1 >= 0 && isLandMineCell(row, col - 1)) {
            count++;
        }
        if (col + 1 < BOARD_COL_SIZE && isLandMineCell(row, col + 1)) {
            count++;
        }
        if (row + 1 < BOARD_LOW_SIZE && col - 1 >= 0 && isLandMineCell(row + 1, col - 1)) {
            count++;
        }
        if (row + 1 < BOARD_LOW_SIZE && isLandMineCell(row + 1, col)) {
            count++;
        }
        if (row + 1 < BOARD_LOW_SIZE && col + 1 < BOARD_COL_SIZE && isLandMineCell(row + 1, col + 1)) {
            count++;
        }
        return count;
    }

    private static void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private static void open(int row, int col) {
        if (row < 0 || row >= BOARD_LOW_SIZE || col < 0 || col >= BOARD_COL_SIZE) {
            return;
        }
        if (BOARD[row][col].isOpened()) {
            return;
        }
        if (isLandMineCell(row, col)) {
            return;
        }
        BOARD[row][col].open();

        if (BOARD[row][col].hasLandMineCount()) {
            return;
        }

        open(row - 1, col - 1);
        open(row - 1, col);
        open(row - 1, col + 1);
        open(row, col - 1);
        open(row, col + 1);
        open(row + 1, col - 1);
        open(row + 1, col);
        open(row + 1, col + 1);
    }

}

package cleancode.minesweeper.tobe.minesweeper.board.cell;

import static cleancode.minesweeper.tobe.minesweeper.board.cell.CellSnapshot.*;

public class LandMineCell implements Cell {
    private final CellState cellState = CellState.initialize();

    @Override
    public boolean isLandMine() {
        return true;
    }

    @Override
    public boolean hasLandMineCount() {
        return false;
    }

    @Override
    public CellSnapshot getSnapshot() {
        if (cellState.isOpened()) {
            return ofLandMine();
        }
        if (cellState.isFlagged()) {
            return ofFlag();
        }
        return ofUnchecked();
    }

    @Override
    public void flag() {
        cellState.flag();
    }

    @Override
    public void open() {
        cellState.open();
    }

    @Override
    public boolean isChecked() {
        return cellState.isFlagged();
    }

    @Override
    public boolean isOpened() {
        return cellState.isOpened();
    }
}

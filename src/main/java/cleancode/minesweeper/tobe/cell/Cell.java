package cleancode.minesweeper.tobe.cell;

public interface Cell {
    String UNCHECKED_SIGN = "□";
    String FLAG_SIGN = "⚑";

    boolean isLandMine();

    boolean hasLandMineCount();

    String getSign();

    void flag();

    void open();

    boolean isChecked();

    boolean isOpened();
}

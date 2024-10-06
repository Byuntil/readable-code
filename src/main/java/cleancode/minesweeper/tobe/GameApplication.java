package cleancode.minesweeper.tobe;

import cleancode.minesweeper.tobe.config.GameConfig;
import cleancode.minesweeper.tobe.gamelevel.Advanced;
import cleancode.minesweeper.tobe.gamelevel.Beginner;
import cleancode.minesweeper.tobe.gamelevel.GameLevel;
import cleancode.minesweeper.tobe.gamelevel.VeryBeginner;
import cleancode.minesweeper.tobe.io.ConsoleInputHandler;
import cleancode.minesweeper.tobe.io.ConsoleOutputHandler;
import cleancode.minesweeper.tobe.io.InputHandler;
import cleancode.minesweeper.tobe.io.OutputHandler;

public class GameApplication {

    public static void main(String[] args) {

        GameConfig gameConfig = new GameConfig(
                new VeryBeginner(),
                new ConsoleInputHandler(),
                new ConsoleOutputHandler());
        Minesweeper minesweeper = new Minesweeper(gameConfig);
        minesweeper.initialize();
        minesweeper.run();
    }

    /**
     * DIP (Dependency Inversion Principle)
     * DI (Dependency Injection)
     *      필요한 의존성을 외부에서 주입 받겠다.
     *      제 3자가 의존성을 넣어주어야함 (컨테이너가 해줌)
     * IoC (Inversion of Control)
     *      프로그램의 흐름은 프레임워크가 제어한다. (생성부터 지우는것까지 생명주기 관리해줌)
     */
}

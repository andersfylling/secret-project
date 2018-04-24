package team.adderall.game.framework;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import team.adderall.game.framework.component.GameComponent;
import team.adderall.game.framework.component.GameComponentHolder;
import team.adderall.game.framework.component.GameDepWire;

@GameComponent
public class GameLogicManager
{
    private GameLogicInterface[][] gameLogicInterfaces;
    private Map<Integer, List<GameLogicInterface>> logicComponents;

    @GameDepWire
    public GameLogicManager() {
        gameLogicInterfaces = null;
        this.logicComponents = new TreeMap<>();
    }

    public void addGameLogic(int wave, GameLogicInterface logic) {
        boolean initializedList = this.logicComponents.containsKey(wave);
        if (!initializedList) {
            this.logicComponents.put(wave, new ArrayList<>());
        }

        this.logicComponents.get(wave).add(logic);
    }

    public GameLogicInterface[][] getGameLogicInterfacesAsArray() {
        // create logic 2d list
        // note that this destroys the logic wave numbers and
        // optimizes the array. We only care about the order and
        // not how many empty logic waves exist between each @GameLogic
        int totalWaves = logicComponents.size();
        int highestNrOfWaveWorkers = 0;
        for (Map.Entry<Integer, List<GameLogicInterface>> entry : logicComponents.entrySet()) {
            if (highestNrOfWaveWorkers < entry.getValue().size()) {
                highestNrOfWaveWorkers = entry.getValue().size();
            }
        }
        if (gameLogicInterfaces == null) {
            gameLogicInterfaces = new GameLogicInterface[totalWaves][highestNrOfWaveWorkers];
        }

        int wave = 0;
        for (Map.Entry<Integer, List<GameLogicInterface>> entry : logicComponents.entrySet()) {
            List<GameLogicInterface> holders = entry.getValue();
            int index = 0;
            for (GameLogicInterface component : holders) {
                // TODO: do a safe check to make sure the object implements this interface
                gameLogicInterfaces[wave][index] = component;
                index++;
            }
            wave++;
        }

        return gameLogicInterfaces;
    }
}

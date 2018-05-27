package team.adderall.game.gameloop;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import addy.annotations.*;
import addy.context.ServiceContext;

@Service
public class GameLogicManager
{
    private GameLogicInterface[][] gameLogicInterfaces;
    private Map<Integer, List<GameLogicInterface>> logicComponents;

    @DepWire
    public GameLogicManager() {
        gameLogicInterfaces = null;
        this.logicComponents = new TreeMap<>();
    }

    @DepWire
    public void extractGameLogicComponents(@Inject(ServiceContext.NAME) ServiceContext ctx)
    {
        ctx.foreach((String name, Object service) -> {
            int wave = service.getClass().getAnnotation(GameLogic.class).wave();
            addGameLogic(wave, (GameLogicInterface) service);
            return false;
        }, GameLogic.class); // filter services. Show only those with @GameLogic

    }

    private void addGameLogic(int wave, GameLogicInterface logic) {
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
                gameLogicInterfaces[wave][index] = component;
                index++;
            }
            wave++;
        }

        return gameLogicInterfaces;
    }
}

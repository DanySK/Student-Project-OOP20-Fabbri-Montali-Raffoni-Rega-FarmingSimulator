package control;

import java.util.List;

import engine.GameState;
import entity.Animal;
import entity.Player;
import entity.PlayerImpl;
import gameMap.MapImpl;
import gameMap.Map;
import gameShop.Shop;
import item.SeedType;

public interface Game {

    void loadGame(MapImpl map, PlayerImpl player);

    void loop();

    Map getMap();

    Player getPlayer();

    Shop getShop();

    boolean buy(SeedType st, int quantity);

    double sellAll();

    GameState getState();

    void interact();

    double getUnlockPrice();

    void growAllSeed();

    void play();

    void shop();

    void info();

    List<Animal> getAllAnimals();

    void resetAnimals();

}
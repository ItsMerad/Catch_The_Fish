package Catch_The_Fish;

import java.util.Random;

public class Game {
    private final Player[] players;
    private final CatchableItem[] items;
    private final Random rnd = new Random();

    public Game(int playerCount) {
        players = new Player[playerCount];
        for (int i = 0; i < playerCount; i++) {
            players[i] = new Player();
        }
        items = new CatchableItem[] {
            new CatchableItem("old shoe", 1),
            new CatchableItem("huge fish", 100),
            new CatchableItem("leaf", 2),
            new CatchableItem("little fish", 50),
            new CatchableItem("rock", 3),
            new CatchableItem("garbage", 0)
        };
    }

    public CatchableItem catchFish(int playerIndex) {
        int idx = rnd.nextInt(items.length);
        CatchableItem item = items[idx];
        players[playerIndex].addScore(item.getPoints());
        players[playerIndex].addTry();
        return item;
    }

    public Player getPlayer(int index) {
        return players[index];
    }
}
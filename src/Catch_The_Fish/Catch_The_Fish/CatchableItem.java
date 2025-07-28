package Catch_The_Fish;

public class CatchableItem {
    private final String name;
    private final int points;

    public CatchableItem(String name, int points) {
        this.name = name;
        this.points = points;
    }

    public String getName() { return name; }
    public int getPoints() { return points; }
}
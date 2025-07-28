package Catch_The_Fish;

public class Player {
    private int score;
    private int tries;

    public Player() {
        score = 0;
        tries = 0;
    }

    public void addScore(int points) { score += points; }
    public void addTry() { tries++; }
    public int getScore() { return score; }
    public int getTries() { return tries; }
}
package participant;

public class User {
    private String username;
    private int score = 0;

    public User(String username) {
        this.username = username;
    }

    public void addScore() {
        score++;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }
}
package client;

// The MenuItem class, stores its name and an action to execute when selected
public class MenuItem {
    private final String name;
    private final Action action;

    public MenuItem(String name, Action action) {
        this.name = name;
        this.action = action;
    }

    public void select() {
        action.execute();
    }

    @Override
    public String toString() {
        return name;
    }
}
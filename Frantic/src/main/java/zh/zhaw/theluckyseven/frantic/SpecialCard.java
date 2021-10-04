package zh.zhaw.theluckyseven.frantic;

public class SpecialCard extends HandCard {
    int value;
    String name;
    String description;

    public SpecialCard(int value, String name, String description) {
        this.value = value;
        this.name = name;
        this.description = description;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

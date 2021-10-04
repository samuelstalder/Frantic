package zh.zhaw.theluckyseven.frantic;

public class NumberCard extends HandCard{
    int number;
    int value;

    public int getNumber() {
        return number;
    }

    public int getValue() {
        return value;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

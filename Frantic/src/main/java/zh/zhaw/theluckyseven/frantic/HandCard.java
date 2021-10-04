package zh.zhaw.theluckyseven.frantic;

import java.util.ArrayList;

public abstract class HandCard extends Card {
    Config config = new Config() {
        @Override
        public void setupCards() {
            super.setupCards();
        }
    };
    ArrayList<Config.Color> colors = new ArrayList<>();

    public boolean isColor(Config color) {
        return true;
    }


}

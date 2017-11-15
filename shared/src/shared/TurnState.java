package shared;

public enum TurnState {
    lobby,
    init {
        public int maxReturnCards() {
            return 1;
        }
    },
    beginning,
    returnDest {
        public int maxReturnCards() {
            return 2;
        }
    },
    drawTrain,
    waiting;

    public int maxReturnCards() {
        throw new UnsupportedOperationException("wrong state: " + this);
    }
}

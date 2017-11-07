package shared;

public enum TurnState {
    lobby, init, beginning, returnDest, drawTrain, waiting;

    public int maxReturnCards() {
        if (this.equals(init)) {
            return 1;
        } else if (this.equals(returnDest)) {
            return 2;
        }
        throw new UnsupportedOperationException("wrong state: " + this);
    }
}

package com.thefunteam.android.model.shared;

import com.thefunteam.android.NextLayerFacade;
import com.thefunteam.android.model.Atom;

import java.util.List;

interface TurnStateInterface {
    void drawDestCard();
    void returnDestCard(DestinationCard[] returnCard);
    void drawTrainCard();
    void drawFaceUpCard(int index);
    void claimRoute(Route route, List<TrainType> cards);
}

public enum TurnState implements TurnStateInterface {

    lobby {
        public void drawDestCard() {}
        public void returnDestCard(DestinationCard[] returnCard) {}
        public void drawTrainCard() {}
        public void drawFaceUpCard(int index) {}
        public void claimRoute(Route route, List<TrainType> cards) {}
    }
    , init {
        public void drawDestCard() {}
        public void returnDestCard(DestinationCard[] returnCard) { _.returnDestCard(returnCard); }
        public void drawTrainCard() {}
        public void drawFaceUpCard(int index) {}
        public void claimRoute(Route route, List<TrainType> cards) {}
    }, beginning {
        public void drawDestCard() { _.drawDestCard(); }
        public void returnDestCard(DestinationCard[] returnCard) { }
        public void drawTrainCard() { _.drawTrainCard(); }
        public void drawFaceUpCard(int index) { _.drawFaceUpCard(index);}
        public void claimRoute(Route route, List<TrainType> cards) { _.claimRoute(route, cards); }
    }, returnDest {
        public void drawDestCard() {}
        public void returnDestCard(DestinationCard[] returnCard) { _.returnDestCard(returnCard);}
        public void drawTrainCard() {}
        public void drawFaceUpCard(int index) {}
        public void claimRoute(Route route, List<TrainType> cards) {}
    }, drawTrain {
        public void drawDestCard() {}
        public void returnDestCard(DestinationCard[] returnCard) {}
        public void drawTrainCard() { _.drawTrainCard(); }
        public void drawFaceUpCard(int index) {
            if(Atom.getInstance().getModel().getCurrentGame().getFaceUpDeck().get(index) != TrainType.any) {
                _.drawFaceUpCard(index);
            }
        }
        public void claimRoute(Route route, List<TrainType> cards) {}
    }, waiting {
        public void drawDestCard() {}
        public void returnDestCard(DestinationCard[] returnCard) {}
        public void drawTrainCard() {}
        public void drawFaceUpCard(int index) {}
        public void claimRoute(Route route, List<TrainType> cards) {}
    };

    NextLayerFacade _ = NextLayerFacade.getInstance();

    public int maxReturnCards() {
        if (this.equals(init)) {
            return 1;
        } else {//if (this.equals(returnDest)) {
            return 2;
        }
        //throw new UnsupportedOperationException("wrong state: " + this);
    }
}

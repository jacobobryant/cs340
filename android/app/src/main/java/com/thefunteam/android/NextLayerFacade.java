package com.thefunteam.android;

import com.google.gson.Gson;
import com.thefunteam.android.model.*;
import com.thefunteam.android.model.MessageCommand;
import com.thefunteam.android.model.shared.DestinationCard;

/**
 * This class is used to do everything that the presenters need to do.
 * That way the presenters don't have to worry about the ClientCommunticator and json and junk like that.
 */
public class NextLayerFacade {

    private static NextLayerFacade ourInstance = new NextLayerFacade();

    /**
     *
     * @return an instance... Duh
     */
    public static NextLayerFacade getInstance() { return ourInstance; }

    private NextLayerFacade() { }

    /**
     * logs the player in
     *
     * @pre the user is not currently logged in and the username and password are a correct pair stored on the server
     * @post the model changes to a logged in state
     * @param username login name than the player goes by
     * @param password an unsafe no out in the open password that could be stolen at any moment
     */
    public void login(String username, String password) {
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/login",
                gson.toJson(new LoginCommand(username, password))
        );
    }

    /**
     * registers a new account
     *
     * @pre the user is not currently logged in and the username does not yet exist
     * @post create an account, the model changes to a logged in state on the new account
     * @param username login name than the player goes by
     * @param password an unsafe no out in the open password that could be stolen at any moment
     */
    public void register(String username, String password) {
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/register",
                gson.toJson(new LoginCommand(username, password))
        );
    }

    /**
     * joins the game with gameId
     *
     * @pre currently logged in, and the gameId is the same as a game the exists in the available game list
     * @post the server will modify the the model to reflect that the game has been joined
     * @param gameId the id is unique for each game is part of the AvailableGame model
     */
    public void joinGame(String gameId) {
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/join",
                gson.toJson(new GameCommand(sessionId, gameId))
        );
    }

    /**
     * This method creates a game for the logged in user
     *
     * @pre currently logged in, and not already in a game
     * @post the model is changed to include the new game with the user already joined
     */
    public void createGame() {
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/create",
                gson.toJson(new UserCommand(sessionId))
        );
    }

    /**
     * This method will start the game if the user is currently in a game and it hasn't started yet
     *
     * @pre currently logged in, and currently in a game
     * @post the model changes to a initialize game ready to begin
     */
    public void startGame() {
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/start",
                gson.toJson(new UserCommand(sessionId))
        );
    }

    /**
     * leave the game the user is currently in
     *
     * @pre currently logged in, and part of a game
     * @post the model is change with the player no longer part of a current game
     */
    public void leaveGame() {
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/leave",
                gson.toJson(new UserCommand(sessionId))
        );
    }

    /**
     * send a message for all users to see
     *
     * @pre currently logged in, and part of a started game
     * @post the model is changed to include the new message
     * @param message a message that the user wants to send to everyone else
     */
    public void sendMessage(String message){
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/chat",
                gson.toJson(new MessageCommand(sessionId, message))
        );
    }

    /**
     * return a card to the server right after the game starts
     *
     * @pre currently at the start of the game, and in the state where you can return a card according to the game rules
     * @post the model is changed to not have the destinationCard in your hand
     * @param destinationCard the card that the user choose to return
     */
    public void returnCard(DestinationCard destinationCard) {
        String sessionId = Atom.getInstance().getModel().getSessionId();
        Gson gson = new Gson();
        ClientCommunicator.getInstance().post(
                "/return-dest",
                gson.toJson(new ReturnDestCommand(sessionId, destinationCard))
        );
    }
}

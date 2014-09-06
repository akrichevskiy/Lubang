package controllers;

import models.GameContext;
import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    // in real app use database
    private static GameContext gameContext = new GameContext();

    public static Result index() {
        return ok(index.render("Your new application is ready."));
    }


    public static Result onMove(int pitIdx) {
        Logger.info("move " + pitIdx);
        gameContext.onMove(pitIdx);
        return ok(game.render(gameContext));
    }

    public static Result lubang() {
        return ok(game.render(gameContext));
    }
}

package controllers;

import models.GameContext;
import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    // in real app use database
    private static GameContext gameContext = new GameContext();

    public static Result move(int pitIdx) {
        gameContext.onMove(pitIdx);
        return ok(game.render(gameContext));
    }

    public static Result lubang() {
        return ok(game.render(gameContext));
    }

    public static Result jsRoutes()
    {
        response().setContentType("text/javascript");
        return ok(Routes.javascriptRouter("appRoutes", //appRoutes will be the JS object available in our view
                routes.javascript.Application.move()));
    }
}

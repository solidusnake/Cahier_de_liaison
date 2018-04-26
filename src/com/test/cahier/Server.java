package com.test.cahier;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import java.util.HashMap;
import java.util.Map;

/**Projet cahier**/

//La classe Server
public class Server extends AbstractVerticle { 
  public static void main(String[] args) {

  }



  private Map<String, JsonObject> users = new HashMap<>(); //On cree une instance de l'objet HashMap

  @Override
  public void start() { //la méthode

    setUpInitialData();
//Le routage
    Router router = Router.router(vertx); 

    router.route().handler(BodyHandler.create());
    router.get("/cahier/:userID").handler(this::handleGetUser); //pour une requete get on lance la methode handleGetuser
    router.put("/cahier/:userID").handler(this::handleAddUser); //pour une requete put on lance la methode handleAdduser
    router.post("/cahier/:userID").handler(this::handlePostUser); //pour une requete post on lance la methode handlePostUser
    router.get("/cahier").handler(this::handleListUser); //si l'url users est demander on affiche handleListUsers qui va afficher un tableau Json
    
  router.get("/") //page d'accueil serveur
    .handler(req -> {
        System.out.println("Ecrire un message");
        req.response()
            .putHeader("content-type", "text/html") 
            .end(
              "<html><body><h1>Bienvenue sur le cahier de liaison</h1><p>Serveur OK</p></body></html>");
    });

//On ecoute sur le port 8888
    vertx.createHttpServer().requestHandler(router::accept).listen(8888);
  }

//méthode get
  private void handleGetUser(RoutingContext routingContext) {
    System.out.println("GET");
    String userID = routingContext.request().getParam("userID");
    HttpServerResponse response = routingContext.response();
    if (userID == null) {
      sendError(400, response);
    } else {
      JsonObject user = users.get(userID);
      if (user == null) {
        sendError(404, response);
      } else {
        response.putHeader("content-type", "application/json").end(user.encodePrettily());
      }
    }
  }

//méthode put
  private void handleAddUser(RoutingContext routingContext) {
    System.out.println("PUT");
    String userID = routingContext.request().getParam("userID"); 
    HttpServerResponse response = routingContext.response();
    if (userID == null) {
      sendError(400, response);
    } else {
      JsonObject user = routingContext.getBodyAsJson();
      if (user == null) {
        sendError(400, response);
      } else {
        users.put(userID, user);
        response.end();
      }
    }
  }
  
//méthode post
  public void handlePostUser(RoutingContext routingContext) {
  System.out.println("POST");

}

//afficher la liste
  private void handleListUser(RoutingContext routingContext) {
    JsonArray arr = new JsonArray(); //On instancier l'objet JsonArray
    users.forEach((k, v) -> arr.add(v));
    routingContext.response().putHeader("content-type", "application/json").end(arr.encodePrettily());
  }

  private void sendError(int statusCode, HttpServerResponse response) {
    response.setStatusCode(statusCode).end();
  }

//init default
  private void setUpInitialData() { //ajouter des users set
    System.out.println("InitDataDefault");
    addUsers(new JsonObject().put("id", "enseignant").put("name", "Toto").put("message", "corps du message").put("type", true));
    addUsers(new JsonObject().put("id", "parents").put("name", "Titi").put("message", "corps du message").put("type", true));
  }

//méthode ajouter Users
  private void addUsers(JsonObject user) { 
    users.put(user.getString("id"), user);
  }
}
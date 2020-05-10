package Server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;

public class userAuthentication extends AbstractVerticle {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new userAuthentication());
    }

    public void start() {
        final Router router = Router.router(vertx);

        router.get().handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "application/json")
                    .setStatusCode(200)
                    .end(new JsonObject().put("balance", "hello world").encode());
                });

        vertx.createHttpServer()
                .requestHandler(router)
                .listen(8080, res -> {
                    if (res.succeeded()) {
                        System.out.println("Server is now listening!");
                    } else {
                        System.out.println("Failed to bind!");
                    }
                });
    }
}

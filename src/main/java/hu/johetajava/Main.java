package hu.johetajava;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Consumer;

public class Main {

    public static String TOKEN = "ELvHmVVgGrUgn1uwRaM1mYCTJ5VBLwGwEyw0SHDAmiCokQTvOBulyewN7H8HGneo0aX59pP";
    public static String GAME_SERVER_URI = "https://ambrusweb11.hu/felho/codingTomorrowCup.json";

    public static void main(String[] args) {
        sendToken();
        World world = new World();

        sendTick();

        System.out.println(World.cars);

    }


    public void nextTick(World world) {


    }

    public static void sendToken() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", TOKEN);

        Unirest.post(GAME_SERVER_URI)
                .header("accept", "application/json")
                .header("Content-Type", "application/json")

                .body(jsonObject)
                .asJson()

                .ifFailure(response -> {
                    System.out.println("ERROR: Token elküldése");
                })
                .ifSuccess(response -> {

                    JSONObject jsonObject1 = response.getBody().getObject();


                });

    }


    public static void sendTick() {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", TOKEN);

        Unirest.post(GAME_SERVER_URI)
                .header("accept", "application/json")
                .header("Content-Type", "application/json")

                .body(jsonObject)
                .asJson()

                .ifFailure(response -> {
                    System.out.println("ERROR: Token elküldése");
                })
                .ifSuccess(response -> {

                    JSONObject jsonObject1 = response.getBody().getObject();

                    // Get CAR data
                    JSONArray jsonCars = jsonObject1.getJSONArray("cars");

                    World.cars.clear();
                    for (int i = 0; i < jsonCars.length(); i++) {
                        JSONObject jsonCar = jsonCars.getJSONObject(i);

                        World.cars.add(new Car(
                                jsonCar.getInt("id"),
                                new Position(jsonCar.getJSONObject("pos").getInt("x"), jsonCar.getJSONObject("pos").getInt("y")),
                                Direction.getDirectionBySign(jsonCar.getString("direction").charAt(0)),
                                jsonCar.getInt("speed"),
                                Command.getCommandBySign(jsonCar.getString("next_command").charAt(0)),
                                jsonCar.getInt("life"),
                                jsonCar.getInt("transported")
                        ));
                    }

                });

    }


    public static void error(String message) {
        System.out.println("ERROR: " + message);
    }

    public static void log(String message) {
        System.out.println(message);
    }

}

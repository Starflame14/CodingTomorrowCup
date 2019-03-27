package hu.johetajava;

import kong.unirest.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Main {

    public static String TOKEN = "ELvHmVVgGrUgn1uwRaM1mYCTJ5VBLwGwEyw0SHDAmiCokQTvOBulyewN7H8HGneo0aX59pP";
    public static String GAME_SERVER_URI = "http://31.46.64.35:12323";

    public static void main(String[] args) {
        World world = new World();

        sendToken();

        sendCommand(Commands.NO_OP); // 0th tick: look around
        while (!World.isDead()) {
            sendCommand(nextTick());
        }

    }


    public static Commands nextTick() {

        log("-- " + World.tick + ". TICK -----------------\n");

        System.out.println(World.cars);
        System.out.println(World.pedestrians);
        System.out.println(World.passengers);

        System.out.println(World.car);

        System.out.println("MESSAGES: ");
        for (int i = 0; i < World.messages.length; i++) {
            System.out.println(" - " + World.messages[i]);
        }
        log("---------------------------");


        // TODO set command
        return Commands.NO_OP;

    }

    public static void sendToken() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", TOKEN);


        Unirest.post(GAME_SERVER_URI)
                .header("accept", "application/json")
                .header("Content-Type", "application/json")

                .body(jsonObject)
                .asJson()

                .ifFailure((response) -> {
                    System.out.println("ERROR: Token elküldése");
                })
                .ifSuccess((response) -> {

                    JSONArray messages = response.getBody().getObject().getJSONArray("messages");
                    log("TOKEN sikeresen elküldve.\nMESSAGES:");
                    for (int i = 0; i < messages.length(); i++) {
                        log(" - " + messages.getString(i));
                    }

                });

    }


    public static void sendCommand(Commands command) {

        JSONObject requestObject = new JSONObject();
        requestObject.put("token", TOKEN);

        JSONObject responseId = new JSONObject();
        responseId.put("game_id", World.gameId);
        responseId.put("tick", World.tick);
        responseId.put("car_id", World.carId);

        requestObject.put("response_id", responseId);
        requestObject.put("command", command);

        Unirest.post(GAME_SERVER_URI)
                .header("accept", "application/json")
                .header("Content-Type", "application/json")

                .body(requestObject)
                .asJson()

                .ifFailure(response -> {
                    System.out.println("ERROR: Token elküldése");
                })
                .ifSuccess(response -> {

                    JSONObject jsonObject1 = response.getBody().getObject();

                    // Get request data
                    World.carId = jsonObject1.getJSONObject("request_id").getInt("car_id");
                    World.tick = jsonObject1.getJSONObject("request_id").getInt("tick");
                    World.gameId = jsonObject1.getJSONObject("request_id").getInt("game_id");

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
                                Command.getNextCommandBySign(jsonCar.getString("next_command").charAt(0)),
                                jsonCar.getInt("life"),
                                jsonCar.getInt("transported")
                        ));
                    }

                    for (int i = 0; i < World.cars.size(); i++) {
                        if (World.cars.get(i).id == World.carId) {
                            World.car = World.cars.get(i);
                        }
                    }

                    // Get pedestrians:
                    JSONArray jsonPedestrians = jsonObject1.getJSONArray("pedestrians");
                    for (int i = 0; i < jsonPedestrians.length(); i++) {
                        JSONObject jsonPedestrian = jsonPedestrians.getJSONObject(i);

                        World.pedestrians.add(new Pedestrian(
                                jsonPedestrian.getInt("id"),
                                new Position(jsonPedestrian.getJSONObject("pos").getInt("x"), jsonPedestrian.getJSONObject("pos").getInt("y")),
                                Direction.getDirectionBySign(jsonPedestrian.getString("direction").charAt(0)),
                                jsonPedestrian.getInt("speed"),
                                Command.getNextCommandBySign(jsonPedestrian.getString("next_command").charAt(0))
                        ));
                    }


                    // Get passengers:
                    JSONArray jsonPassengers = jsonObject1.getJSONArray("passengers");
                    for (int i = 0; i < jsonPassengers.length(); i++) {
                        JSONObject jsonPassenger = jsonPassengers.getJSONObject(i);

                        World.passengers.add(new Passenger(
                                jsonPassenger.getInt("id"),
                                new Position(
                                        jsonPassenger.getJSONObject("pos").getInt("x"),
                                        jsonPassenger.getJSONObject("pos").getInt("y")
                                ),
                                jsonPassenger.getInt("car_id"),
                                new Position(
                                        jsonPassenger.getJSONObject("dest_pos").getInt("x"),
                                        jsonPassenger.getJSONObject("dest_pos").getInt("y")
                                )
                        ));
                    }

                    // Get messages
                    JSONArray jsonMessages = jsonObject1.getJSONArray("messages");
                    ArrayList<String> messages = new ArrayList<>();
                    for (int i = 0; i < jsonMessages.length(); i++) {
                        messages.add(jsonMessages.getString(i));
                    }
                    World.messages = messages.toArray(new String[0]);
                });

    }


    public static void error(String message) {
        System.out.println("ERROR: " + message);
    }

    public static void log(String message) {
        System.out.println(message);
    }

}

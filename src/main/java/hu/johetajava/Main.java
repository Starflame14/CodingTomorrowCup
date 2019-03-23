package hu.johetajava;

import kong.unirest.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Main {

    public static String TOKEN = "ELvHmVVgGrUgn1uwRaM1mYCTJ5VBLwGwEyw0SHDAmiCokQTvOBulyewN7H8HGneo0aX59pP";
    public static String GAME_SERVER_URI = "https://ambrusweb11.hu/felho/codingTomorrowCup.json";

    public static void main(String[] args) {
        sendToken();
        World world = new World();

        // TODO set the command


        while (!World.isDead()) {
            sendCommand(nextTick());
        }


    }


    public static Commands nextTick() {

        System.out.println(World.cars);
        System.out.println(World.pedestrians);
        System.out.println(World.passengers);

        System.out.println("MESSAGES: ");
        for (int i = 0; i < World.messages.length; i++) {
            System.out.println(" - " + World.messages[i]);
        }
        System.out.println("---------------------------");


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

                .ifFailure(response -> {
                    System.out.println("ERROR: Token elküldése");
                })
                .ifSuccess(response -> {

                    JSONObject jsonObject1 = response.getBody().getObject();


                });

    }


    public static void sendCommand(Commands command) {

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
                                Command.getNextCommandBySign(jsonCar.getString("next_command").charAt(0)),
                                jsonCar.getInt("life"),
                                jsonCar.getInt("transported")
                        ));
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

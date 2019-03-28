package hu.johetajava;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Main {

    private static final int GAMESERVER_PORT = 12323;
    public static String TOKEN = "ELvHmVVgGrUgn1uwRaM1mYCTJ5VBLwGwEyw0SHDAmiCokQTvOBulyewN7H8HGneo0aX59pP";
    public static String GAME_SERVER_URI = "31.46.64.35";

    private static DataOutputStream outputStream;
    private static DataInputStream inputStream;


    public static void main(String[] args) {
        World world = new World();
        startClient();

        sendToken();
        System.out.println("Token elküldve.");

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


        if (World.tick == 0) {
            return Commands.ACCELERATION;
        }

        if (World.isThere(Field.SIDEWALK, Direction.getPositionByDirection(World.car.position, World.car.direction, World.car.speed))) {

        }


        // TODO set command
        return Commands.NO_OP;

    }

    public static void startClient() {

        try {
            Socket socket = new Socket(GAME_SERVER_URI, GAMESERVER_PORT);
            inputStream = new DataInputStream(socket.getInputStream());
            outputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void sendMessage(String message) {
        System.out.println("Message: " + message);
        try {
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readMessage() {
        try {
            StringBuilder sb = new StringBuilder();
            int brackets = 0;

            do {
                int readedChar = inputStream.read();

                char c = (char) readedChar;
                switch (c) {
                    case '{':
                        ++brackets;
                        break;
                    case '}':
                        --brackets;
                        break;
                }
                sb.append(c);
            } while (brackets > 0);

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * Sends the token to the server then gets the first tick information
     */
    public static void sendToken() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", TOKEN);

        sendMessage(jsonObject.toString(0));

        // GET JSON DATA
        JSONObject response = new JSONObject(readMessage());
        eatJsonObject(response);
    }

    /**
     * Sends the command then fetches the new tick information from the server
     *
     * @param command Next command
     */
    public static void sendCommand(Commands command) {
        JSONObject requestObject = new JSONObject();
        requestObject.put("token", TOKEN);

        JSONObject responseId = new JSONObject();
        responseId.put("game_id", World.gameId);
        responseId.put("tick", World.tick);
        responseId.put("car_id", World.carId);

        requestObject.put("response_id", responseId);
        requestObject.put("command", command);

        // SEND JSON DATA
        sendMessage(requestObject.toString(0));

        // GET JSON DATA
        JSONObject response = new JSONObject(readMessage());

        eatJsonObject(response);

    }

    /**
     * Gets the tick information from a JSONObject
     *
     * @param jsonObject JSON response
     */
    public static void eatJsonObject(JSONObject jsonObject) {

        // Get request data
        World.carId = jsonObject.getJSONObject("request_id").getInt("car_id");
        World.tick = jsonObject.getJSONObject("request_id").getInt("tick");
        World.gameId = jsonObject.getJSONObject("request_id").getInt("game_id");

        // Get CAR data
        JSONArray jsonCars = jsonObject.getJSONArray("cars");

        World.cars.clear();
        for (int i = 0; i < jsonCars.length(); i++) {
            JSONObject jsonCar = jsonCars.getJSONObject(i);

            Commands next_command;
            if (jsonCar.has("next_command")) {
                next_command = Command.getNextCommandBySign(jsonCar.getString("next_command").charAt(0));
            } else {
                next_command = Commands.NO_OP;
            }

            World.cars.add(new Car(
                    jsonCar.getInt("id"),
                    new Position(jsonCar.getJSONObject("pos").getInt("x"), jsonCar.getJSONObject("pos").getInt("y")),
                    Direction.getDirectionBySign(jsonCar.getString("direction").charAt(0)),
                    jsonCar.getInt("speed"), next_command,
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
        JSONArray jsonPedestrians = jsonObject.getJSONArray("pedestrians");
        for (int i = 0; i < jsonPedestrians.length(); i++) {
            JSONObject jsonPedestrian = jsonPedestrians.getJSONObject(i);

            Commands next_command = jsonPedestrian.has("next_command") ? Command.getNextCommandBySign(jsonPedestrian.getString("next_command").charAt(0)) : Commands.NO_OP;

            World.pedestrians.add(new Pedestrian(
                    jsonPedestrian.getInt("id"),
                    new Position(jsonPedestrian.getJSONObject("pos").getInt("x"), jsonPedestrian.getJSONObject("pos").getInt("y")),
                    Direction.getDirectionBySign(jsonPedestrian.getString("direction").charAt(0)),
                    jsonPedestrian.getInt("speed"),
                    next_command
            ));
        }


        // Get passengers:
        JSONArray jsonPassengers = jsonObject.getJSONArray("passengers");
        for (int i = 0; i < jsonPassengers.length(); i++) {
            JSONObject jsonPassenger = jsonPassengers.getJSONObject(i);

            World.passengers.add(new Passenger(
                    jsonPassenger.getInt("id"),
                    new Position(
                            jsonPassenger.getJSONObject("pos").getInt("x"),
                            jsonPassenger.getJSONObject("pos").getInt("y")
                    ),
                    jsonPassenger.has("car_id") ? jsonPassenger.getInt("car_id") : -1,
                    new Position(
                            jsonPassenger.getJSONObject("dest_pos").getInt("x"),
                            jsonPassenger.getJSONObject("dest_pos").getInt("y")
                    )
            ));
        }

        // Get messages
        JSONArray jsonMessages = jsonObject.getJSONArray("messages");
        ArrayList<String> messages = new ArrayList<>();
        for (int i = 0; i < jsonMessages.length(); i++) {
            messages.add(jsonMessages.getString(i));
        }
        World.messages = messages.toArray(new String[0]);
    }

    public static void error(String message) {
        System.out.println("ERROR: " + message);
    }

    public static void log(String message) {
        System.out.println(message);
    }

}

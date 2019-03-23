package hu.johetajava;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class World {

    public static int gameId;
    public static int tick;
    public static int carId;
    public static String[] messages = new String[0];

    public static Car car;
    public static ArrayList<Car> cars;
    public static ArrayList<Passenger> passengers;
    public static ArrayList<Pedestrian> pedestrians;

    public static String stringMap;
    public static Field[][] map;

    World() {
        cars = new ArrayList<>();
        passengers = new ArrayList<>();
        pedestrians = new ArrayList<>();

        loadMap();
    }

    public static Position checkTeleportPosition(Position oldPosition) {
        // TODO get teleport position
        return oldPosition;
    }

    public static void loadMap() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("map.txt"));

            String line;

            ArrayList<Field[]> fieldLines = new ArrayList<>();

            for (int i = 0; (line = reader.readLine()) != null; i++) {
                char[] charA = line.toCharArray();
                Field[] fieldLine = new Field[charA.length];
                for (int j = 0; j < charA.length; j++) {
                    switch (charA[j]) {
                        case 'S':
                            fieldLine[j] = Field.ROAD;
                            break;
                        case 'Z':
                            fieldLine[j] = Field.ZEBRA;
                            break;
                        case 'P':
                            fieldLine[j] = Field.SIDEWALK;
                            break;
                        case 'G':
                            fieldLine[j] = Field.GRASS;
                            break;
                        case 'B':
                            fieldLine[j] = Field.BUILDING;
                            break;
                        case 'T':
                            fieldLine[j] = Field.TREE;
                            break;
                    }
                }
                fieldLines.add(fieldLine);
            }

            map = fieldLines.toArray(new Field[0][]);

            Main.log("MAP betöltve");
        } catch (FileNotFoundException e) {
            Main.error("Map file reading error.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* Debug:
        for (Field[] line: map) {
            System.out.println();
            for(Field field : line){
                System.out.print(field.name() + "\t");
            }
        }
        */
    }


    public static boolean isDead(){
        List<String> message_list = Arrays.asList(messages);
        return message_list.contains("REAL_TIMEOUT")
                || message_list.contains("COMMUNICATION")
                || message_list.contains("CRASHED")
                || message_list.contains("TICK_TIMEOUT");



    }
}

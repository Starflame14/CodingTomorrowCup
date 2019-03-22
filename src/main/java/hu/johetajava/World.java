package hu.johetajava;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class World {

    public static int gameId;
    public static int tick;
    public static int carId;

    public static ArrayList<Car> cars;
    public static ArrayList<Passenger> passengers;
    public static ArrayList<Pedestrian> pedestrians;

    public static String stringMap;
    public static Field[][] map;

    World() {
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

}

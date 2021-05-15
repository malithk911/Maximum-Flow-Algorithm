/*
 * Student ID : 2018412
 * UOW ID : w1761910
 * Name : Parinda Malith Kulathilake
 */

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

public class Parser {
    /**
     * This method reads inputs from a txt file and returns a List<String>
     * @return List of strings
     */
    public static List<String> getFileInputs() {

        List<String> inputs = Collections.emptyList(); // declare empty List

        try {
            // read all lines from a txt file and assign to List at once
            inputs = Files.readAllLines(Paths.get("src/data/inputs.txt"), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // return created List
        return inputs;
    }
}

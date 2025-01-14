package core;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class World {

    public static final int SIZE = 12;

    protected Set<Point> availableFuel = Collections.newSetFromMap(new ConcurrentHashMap<Point, Boolean>());
    private Random rand = new Random();
    protected Robot[] robots;

    public World() {
        // No.0 robot is null. The programme will only call robot no.1 and 2.
        robots = new Robot[] { null, new Robot(this, 0, 0, "red", false),
                new Robot(this, SIZE - 1, SIZE - 1, "blue", false) };
    }

    /**
     * This world can no longer be used after this call.
     */
    public void reset() {
        availableFuel.clear();
        for (int i = 1; i <= 2; i++) {
            robots[i].updatePending();
            robots[i].cancel();
        }
    }

    public Set<Point> getAvailableFuel() {
        return availableFuel;
    }

    public void updateWorld() {
        addFuel(false);
        for (int i = 1; i <= 2; i++) {
            robots[i].updatePending();
        }
    }

    public void loadRobotProgram(int id, File code) {
        RobotProgramNode prog = Parser.parseFile(code);
        if (prog != null) {
            System.out.println("Robot " + id + " now has program: ");
            System.out.println(prog);
            robots[id].setProgram(prog);
        }
    }

    public void start() {
        // add some initial fuel
        if (availableFuel.isEmpty()) {
            addFuel(true);
            addFuel(true);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    robots[1].run();
                } catch (RobotInterruptedException e) {
                }
                robots[1].setFinished(true);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    robots[2].run();
                } catch (RobotInterruptedException e) {
                }
                robots[2].setFinished(true);
            }
        }).start();
    }

    public Robot getRobot(int id) {
        if (id <= 0 || id > robots.length) {
            return null;
        }
        return robots[id];
    }

    /** Returns the opponent robot of the argument */
    public Robot getOtherRobot(Robot robot) {
        if (robot == robots[2])
            return robots[1];
        if (robot == robots[1])
            return robots[2];
        return null;
    }

    private void addFuel(boolean definitely) {
        if (definitely || rand.nextDouble() < 0.2) {
            int x = rand.nextInt(12);
            int y = rand.nextInt(12);
            Point fuel = new Point(x, y);
            availableFuel.add(fuel);
        }
    }

    public static class RoboGamePrinter extends World {
        public RoboGamePrinter() {
            super();
            // we remake the robots to have noWait set to true, this makes the
            // program run without the timer.
            // the blue robot is never used, but the red robot does call some
            // methods on it so we can't make it null.
            this.robots = new Robot[] { null, new Robot(this, 0, 0, "red", true),
                    new Robot(this, SIZE - 1, SIZE - 1, "blue", true) };
        }

        public static void main(String[] args) throws IOException {
            if (args.length == 0) {
                // for the students.
                System.out.println("WRONG FILE");
                System.out.println("Use the main function in RoboGame.java to run the assignment.");
            }

            RoboGamePrinter rgp = new RoboGamePrinter();

            // load the program.
            RobotProgramNode prog = Parser.parseFile(new File(args[0]));
            rgp.robots[1].setProgram(prog);

            // load fuel placement from file.
            List<String> fuel = Files.readAllLines(Paths.get(args[1]), StandardCharsets.UTF_8);
            for (String line : fuel) {
                String[] coords = line.split("\\s+");
                int x = Integer.parseInt(coords[0]);
                int y = Integer.parseInt(coords[1]);
                rgp.availableFuel.add(new Point(x, y));
            }

            // run the robot.
            rgp.robots[1].run();
        }
    }
}

package ir.agar.Model;

import ir.agar.Controller.ClientController;
import ir.agar.Controller.PowerTypeResource;
import ir.agar.Model.Objects.*;
import ir.agar.Controller.ClientController;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class ClientEngine implements GameEngine {

    private static ClientEngine currentInstance;

    private Map<String, Player> players = new Hashtable<>();
    private List<PlayerCircle> playerCircles = new ArrayList<>();
    private List<PowerCircle> powerCircles = new ArrayList<>();
    private List<GearCircle> gearCircles = new ArrayList<>();
    private List<EnergyCircle> energyCircles = new ArrayList<>();

    private boolean isUpdated = true;

    private Direction direction = null;

    public ClientEngine() {
        currentInstance = this;
    }

    public synchronized void updateData(Object[] serverData){
        players = ((Map<String, Player>) serverData[0]);
        playerCircles = ((List<PlayerCircle>) serverData[1]);
        powerCircles = ((List<PowerCircle>) serverData[2]);
        gearCircles = ((List<GearCircle>) serverData[3]);
        energyCircles = ((List<EnergyCircle>) serverData[4]);

        isUpdated = true;
    }

    public static ClientEngine getCurrentInstance() {
        return currentInstance;
    }



    @Override
    public synchronized boolean isUpdated() {
        if (isUpdated) {
            this.isUpdated = false;
            return true;
        }
        return false;
    }

    @Override
    public void splitPlayer(int player) {

    }

    @Override
    public synchronized Map<String, Player> getPlayers() {
        return players;
    }

    @Override
    public synchronized List<PlayerCircle> getPlayerCircles() {
        return playerCircles;
    }

    @Override
    public synchronized List<PowerCircle> getPowerCircles() {
        return powerCircles;
    }

    @Override
    public synchronized List<GearCircle> getGearCircles() {
        return gearCircles;
    }

    @Override
    public synchronized List<EnergyCircle> getEnergyCircles() {
        return energyCircles;
    }

    @Override
    public Player getPlayer(String username) {
        return players.getOrDefault(username, null);
    }

    public void setMouseLocation(int x, int y) {
        ClientController.getCurrentInstance().setMouseLocation(x, y);

    }

    public void addPowerTypeResource(ArrayList<PowerTypeResource> ptr) {

    }
}

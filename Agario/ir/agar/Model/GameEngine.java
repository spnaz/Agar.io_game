package ir.agar.Model;

import ir.agar.Model.Objects.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public interface GameEngine{

    Map<String, Player> getPlayers();

    List<PowerCircle> getPowerCircles();

    List<GearCircle> getGearCircles();

    List<EnergyCircle> getEnergyCircles();

    List<PlayerCircle> getPlayerCircles();

    Player getPlayer(String username);

    boolean isUpdated();

    void splitPlayer(int player);
}

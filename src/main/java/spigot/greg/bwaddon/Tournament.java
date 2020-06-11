package spigot.greg.bwaddon;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.screamingsandals.bedwars.api.BedwarsAPI;
import org.screamingsandals.bedwars.api.game.Game;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Tournament {
    private String name = "Tournament";
    private List<TournamentTeam> teams = new ArrayList<>();
    private List<Phase> phases = new ArrayList<>();
    private RunningTournament runningTournament = null;

    public String getName() {
        return name;
    }

    public List<TournamentTeam> getTeams() {
        return teams;
    }

    public void addTeam(TournamentTeam team) {
        if (!teams.contains(team)) {
            teams.add(team);
        }
    }

    public void removeTeam(TournamentTeam team) {
        if (teams.contains(team)) {
            teams.remove(team);
        }
    }

    public boolean hasTeam(String name) {
        return getTeam(name) != null;
    }

    public TournamentTeam getTeam(String name) {
        for (TournamentTeam team : teams) {
            if (team.getTeamName().equals(name)) {
                return team;
            }
        }
        return null;
    }

    public boolean isActive() {
        return runningTournament != null;
    }

    public List<Phase> getPhases() {
        return phases;
    }

    public void addPhase(Phase phase) {
        if (!phases.contains(phase)) {
            phases.add(phase);
        }
    }

    public boolean hasPhase(String name) {
        return getPhase(name) != null;
    }

    public Phase getPhase(String name) {
        for (Phase phase : phases) {
            if (phase.getCodeName().equals(name)) {
                return phase;
            }
        }
        return null;
    }

    public void removePhase(Phase phase) {
        if (phases.contains(phase)) {
            phases.remove(phase);
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void startTournament() {
        runningTournament = new RunningTournament(this);
    }

    public void stopTournament() {
        runningTournament = null;
        Bukkit.getOnlinePlayers().forEach(player -> {
            Game game = BedwarsAPI.getInstance().getGameOfPlayer(player);
            if (game != null) {
                game.leaveFromGame(player);
            }
        });
    }

    public RunningTournament getRunningTournament() {
        return runningTournament;
    }

    public void save() {
        File dir = BwAddon.getInstance().getDataFolder();
        if (!dir.exists())
            dir.mkdirs();
        File file = new File(dir, "tournament-settings.yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        YamlConfiguration configMap = new YamlConfiguration();
        configMap.set("name", name);
        List<Map<String, Object>> phasesList = new ArrayList<>();
        for (Phase phase : this.phases) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", phase.getCodeName());
            map.put("hasParent", phase.getParentPhase() != null);
            map.put("parent", phase.getParentPhase());
            map.put("arenas", phase.getPossibleArenas());
            map.put("teams", phase.getTeams());
            phasesList.add(map);
        }
        configMap.set("phases", phasesList);
        List<Map<String, Object>> teamsList = new ArrayList<>();
        for (TournamentTeam team : this.teams) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", team.getTeamName());
            List<String> players = new ArrayList<>();
            team.getPlayers().forEach(uuid -> players.add(uuid.toString()));
            map.put("player", players);
            teamsList.add(map);
        }
        configMap.set("teams", teamsList);
        try {
            configMap.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        File dir = BwAddon.getInstance().getDataFolder();
        File file = new File(dir, "tournament-settings.yml");
        if (!file.exists()) {
            return;
        }
        YamlConfiguration configMap = new YamlConfiguration();
        try {
            configMap.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
            return;
        }

        this.name = configMap.getString("name");
        phases.clear();
        for (Map<String, Object> map : (List<Map<String, Object>>) configMap.getList("phases")) {
            Phase phase = new Phase(map.get("name").toString());
            if (Boolean.parseBoolean(map.get("hasParent").toString())) {
                phase.setParentPhase(map.get("parent").toString());
            }
            phase.setTeams(Integer.parseInt(map.get("teams").toString()));
            for (String possibleArena : (List<String>) map.get("arenas")) {
                phase.addPossibleArena(possibleArena);
            }
            phases.add(phase);
        }

        teams.clear();
        for (Map<String, Object> map : (List<Map<String, Object>>) configMap.getList("teams")) {
            TournamentTeam team = new TournamentTeam(map.get("name").toString());
            for (String uuid : (List<String>) map.get("players")) {
                team.addPlayer(UUID.fromString(uuid));
            }
            teams.add(team);
        }
    }
}

package me.pikamug.BossQuestsModule;

import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.mineacademy.boss.api.event.BossDeathEvent;

import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import me.blackvein.quests.Quests;

public class BossKillObjective extends CustomObjective implements Listener {
	private static Quests quests = (Quests) Bukkit.getServer().getPluginManager().getPlugin("Quests");
	
	public BossKillObjective() {
		setName("Boss Kill Mobs Objective");
		setAuthor("PikaMug");
		setShowCount(true);
		addStringPrompt("Kill Obj", "Set a name for the objective", "Kill Boss");
		addStringPrompt("Kill Names", "Enter Boss names, separating each one by a comma", "ANY");
		setCountPrompt("Set the amount of Boss to kill");
		setDisplay("%Kill Obj% %Kill Names%: %count%");
	}
	
	@EventHandler
	public void onBossDeath(BossDeathEvent event) {
		Player killer = event.getEntity().getKiller();
		if (killer == null) {
			return;
		}
		Quester quester = quests.getQuester(killer.getUniqueId());
		if (quester == null) {
			return;
		}
		String mobName = event.getBoss().getName();
		for (Quest q : quester.getCurrentQuests().keySet()) {
			Map<String, Object> datamap = getDataForPlayer(killer, this, q);
			if (datamap != null) {
				String mobNames = (String)datamap.getOrDefault("Kill Names", "ANY");
				if (mobNames == null) {
					return;
				}
				String[] spl = mobNames.split(",");
				for (String str : spl) {
					if (str.equals("ANY") || mobName.equalsIgnoreCase(str)) {
						incrementObjective(killer, this, 1, q);
						return;
					}
				}
			}
		}
	}
}
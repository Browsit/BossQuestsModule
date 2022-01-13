/*
 * Copyright (c) 2019 PikaMug and contributors. All rights reserved.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN
 * NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS
 * OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package me.pikamug.BossQuestsModule;

import me.blackvein.quests.CustomObjective;
import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.mineacademy.boss.api.event.BossDeathEvent;

import java.util.Map;

public class BossKillObjective extends CustomObjective implements Listener {
	
	public BossKillObjective() {
		setName("Boss Kill Mobs Objective");
		setAuthor("PikaMug");
		setShowCount(true);
		addStringPrompt("Boss Kill Obj", "Set a name for the objective", "Kill Boss");
		addStringPrompt("Boss Kill Names", "Enter Boss names, separating each one by a comma", "ANY");
		setCountPrompt("Set the amount of Boss to kill");
		setDisplay("%Boss Kill Obj% %Boss Kill Names%: %count%");
	}

	@Override
	public String getModuleName() {
		return BossModule.getModuleName();
	}

	@Override
	public Map.Entry<String, Short> getModuleItem() {
		return BossModule.getModuleItem();
	}
	
	@EventHandler
	public void onBossDeath(BossDeathEvent event) {
		Player killer = event.getEntity().getKiller();
		if (killer == null) {
			return;
		}
		Quester quester = BossModule.getQuests().getQuester(killer.getUniqueId());
		if (quester == null) {
			return;
		}
		String mobName = event.getBoss().getName();
		String customMobName = event.getBoss().getAlias();
		for (Quest q : quester.getCurrentQuests().keySet()) {
			Map<String, Object> datamap = getDataForPlayer(killer, this, q);
			if (datamap != null) {
				String mobNames = (String)datamap.getOrDefault("Boss Kill Names", "ANY");
				if (mobNames == null) {
					return;
				}
				String[] spl = mobNames.split(",");
				for (String str : spl) {
					if (str.equals("ANY") || mobName.equalsIgnoreCase(str)
					        || str.equalsIgnoreCase(customMobName)) {
						incrementObjective(killer, this, 1, q);
						return;
					}
				}
			}
		}
	}
}
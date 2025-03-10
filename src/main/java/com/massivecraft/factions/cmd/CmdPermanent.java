package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.Logger;
import com.massivecraft.factions.zcore.util.TL;


public class CmdPermanent extends FCommand {

    /**
     * @author FactionsUUID Team - Modified By CmdrKittens
     */

    public CmdPermanent() {
        super();
        this.aliases.addAll(Aliases.permanent_faction);
        this.requiredArgs.add("faction tag");

        this.requirements = new CommandRequirements.Builder(Permission.SET_PERMANENT)
                .build();
    }

    @Override
    public void perform(CommandContext context) {
        Faction faction = context.argAsFaction(0);
        if (faction == null) {
            return;
        }

        String change;
        if (faction.isPermanent()) {
            change = TL.COMMAND_PERMANENT_REVOKE.toString();
            faction.setPermanent(false);
        } else {
            change = TL.COMMAND_PERMANENT_GRANT.toString();
            faction.setPermanent(true);
        }

        Logger.print(context.fPlayer == null ? "A server admin" : context.fPlayer.getName() + " " + change + " the faction \"" + faction.getTag() + "\".", Logger.PrefixType.DEFAULT);

        // Inform all players
        for (FPlayer fplayer : FPlayers.getInstance().getOnlinePlayers()) {
            String blame = (context.fPlayer == null ? TL.GENERIC_SERVERADMIN.toString() : context.fPlayer.describeTo(fplayer, true));
            if (fplayer.getFaction() == faction) {
                fplayer.msg(TL.COMMAND_PERMANENT_YOURS, blame, change);
            } else {
                fplayer.msg(TL.COMMAND_PERMANENT_OTHER, blame, change, faction.getTag(fplayer));
            }
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_PERMANENT_DESCRIPTION;
    }
}


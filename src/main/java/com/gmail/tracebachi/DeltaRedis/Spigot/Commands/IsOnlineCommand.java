/*
 * This file is part of DeltaRedis.
 *
 * DeltaRedis is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DeltaRedis is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DeltaRedis.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.gmail.tracebachi.DeltaRedis.Spigot.Commands;

import com.gmail.tracebachi.DeltaRedis.Shared.Prefixes;
import com.gmail.tracebachi.DeltaRedis.Shared.Registerable;
import com.gmail.tracebachi.DeltaRedis.Shared.Shutdownable;
import com.gmail.tracebachi.DeltaRedis.Spigot.DeltaRedis;
import com.gmail.tracebachi.DeltaRedis.Spigot.DeltaRedisApi;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by Trace Bachi (tracebachi@gmail.com, BigBossZee) on 11/28/15.
 */
public class IsOnlineCommand implements CommandExecutor, Registerable, Shutdownable
{
    private DeltaRedisApi deltaApi;
    private DeltaRedis plugin;

    public IsOnlineCommand(DeltaRedisApi deltaApi, DeltaRedis plugin)
    {
        this.deltaApi = deltaApi;
        this.plugin = plugin;
    }

    @Override
    public void register()
    {
        plugin.getCommand("isonline").setExecutor(this);
    }

    @Override
    public void unregister()
    {
        plugin.getCommand("isonline").setExecutor(null);
    }

    @Override
    public void shutdown()
    {
        unregister();
        deltaApi = null;
        plugin = null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args)
    {
        if(!sender.hasPermission("DeltaRedis.IsOnline"))
        {
            sender.sendMessage(Prefixes.FAILURE + "You do not have permission to run this command.");
            return true;
        }

        if(args.length < 1)
        {
            sender.sendMessage(Prefixes.INFO + "/isonline <name>");
            return true;
        }

        String senderName = sender.getName();
        String nameToFind = args[0];

        deltaApi.findPlayer(nameToFind, (cachedPlayer) ->
        {
            if(cachedPlayer != null)
            {
                sendMessage(senderName, Prefixes.INFO + Prefixes.input(nameToFind) +
                    " is " + Prefixes.input("online") +
                    " on " + Prefixes.input(cachedPlayer.getServer()));
            }
            else
            {
                sendMessage(senderName, Prefixes.INFO + Prefixes.input(nameToFind) +
                    " is " + Prefixes.input("offline"));
            }
        });
        return true;
    }

    private void sendMessage(String name, String message)
    {
        if(name.equalsIgnoreCase("console"))
        {
            Bukkit.getConsoleSender().sendMessage(message);
        }
        else
        {
            Player player = Bukkit.getPlayer(name);
            player.sendMessage(message);
        }
    }
}

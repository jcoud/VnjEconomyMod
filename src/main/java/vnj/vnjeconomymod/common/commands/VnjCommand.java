package vnj.vnjeconomymod.common.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import vnj.vnjeconomymod.VnjEconomyMod;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VnjCommand extends CommandBase {
    public static boolean MARKET_ACCESSING = false;
    @Override
    public String getName() {
        return "access";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "access <true|false>";
    }

    @Override
    public List<String> getAliases() {
        return new ArrayList<String>(){{add(VnjEconomyMod.MOD_ID);}};
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        MARKET_ACCESSING = parseBoolean(args[0]);
        sender.sendMessage(new TextComponentString("admin access: " + MARKET_ACCESSING));
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return true;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1)
            return getListOfStringsMatchingLastWord(args, new String[] {"true", "1", "false", "0"});
        return Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}

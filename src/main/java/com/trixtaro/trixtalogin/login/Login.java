
package com.trixtaro.trixtalogin.login;

import com.flowpowered.math.vector.Vector3d;
import com.trixtaro.trixtalogin.TrixtaLogin;
import com.trixtaro.trixtalogin.config.Config;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class Login implements CommandExecutor{

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        
        String password = args.<String>getOne(Text.of("password")).get();
        
        if(src instanceof Player){
            
            Player player = (Player) src;
            
            if(Config.confNode.getNode("Player", player.getName(), "isLogged").getBoolean() == false){
                
                if(TrixtaLogin.hashWith256(password).equals(Config.confNode.getNode("Player", player.getName(), "password").getString())){

                    Config.confNode.getNode("Player", player.getName(), "isLogged").setValue(true);
                    Config.save();
                    
                    player.sendMessage(Text.of("Te has logeado. Bienvenido."));
                    player.offer(Keys.WALKING_SPEED, 0.1);
                } else {
                    
                    player.kick(Text.of("La contraseña esta mal."));
                    
                }
           
            } else {
                
                player.sendMessage(Text.of("Ya estas logeado."));

            }
            
        } else {
            
            src.sendMessage(Text.of(TextColors.RED, "Solo un jugador puede usar el comando."));
            
        }
        return CommandResult.success();
        
    }
    
    public static void logout(Player player){
       
        Config.confNode.getNode("Player", player.getName(), "isLogged").setValue(false);
        Config.save();
        
    }
    
    public static CommandSpec base(){
        return CommandSpec.builder()
                .description(Text.of("This is the command for login."))
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("password"))))
                .executor(new Login())
                .build();
    }
}

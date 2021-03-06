
package com.trixtaro.trixtalogin.login;

import com.trixtaro.trixtalogin.Greeting;
import com.trixtaro.trixtalogin.Message;
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

public class Register implements CommandExecutor{

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        
        String password = args.<String>getOne(Text.of("password")).get();
        String repeat_password = args.<String>getOne(Text.of("repeat_password")).get();
        
        if(src instanceof Player){
            
            Player player = (Player) src;
            
            if(Config.confNode.getNode("Player", player.getName(),"password").isVirtual()){
                
                if(password.equals(repeat_password)){
                    
                    Config.confNode.getNode("Player", player.getName(), "password").setValue(TrixtaLogin.hashWith256(password));
                    Config.confNode.getNode("Player", player.getName(), "isLogged").setValue(true);
                    
                    Config.save();
                    
                    player.sendMessage(Text.of("Te has registrado en el servidor. Bienvenido."));
                    player.offer(Keys.WALKING_SPEED, 0.1);
                } else {
                    
                    player.kick(Text.of("Las contraseñas deben coincidir."));
                    
                }
           
            } else {
                
                player.sendMessage(Text.of("Ya estas registrado, utiliza /login contraseña."));

            }
            
        } else {
            
            src.sendMessage(Text.of(TextColors.RED, "Solo un jugador puede usar el comando."));
            
        }
        return CommandResult.success();
    }
    
    public static CommandSpec base(){
        return CommandSpec.builder()
                .description(Text.of("This is the command for register"))
                .arguments(GenericArguments.seq(GenericArguments.onlyOne(GenericArguments.string(Text.of("password"))),
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("repeat_password")))))
                .executor(new Register())
                .build();
    }
    
}
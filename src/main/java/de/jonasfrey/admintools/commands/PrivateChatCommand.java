package de.jonasfrey.admintools.commands;

import de.jonasfrey.admintools.AdminTools;
import de.jonasfrey.admintools.SpecialChatType;

/**
 * @author Jonas Frey
 * @version 1.0, 08.08.17
 */

public class PrivateChatCommand extends SpecialChatCommand {
    
    public PrivateChatCommand(AdminTools plugin) {
        super(plugin, SpecialChatType.PRIVATE_CHAT);
    }
    
}

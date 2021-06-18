package xy.xy718.xyfires.common.service;

import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import xy.xy718.xyfires.XyFireShieldPlugin;
import xy.xy718.xyfires.event.PlayerFiredEvent;

import java.util.Map;
import java.util.UUID;

public class ShieldService {

    private static final Logger LOGGER = XyFireShieldPlugin.LOGGER;

    public static void playerShieldRecovery() {
        for (Map.Entry<UUID, Integer> kv : PlayerFiredEvent.playerShields.entrySet()) {
            if(kv.getValue()!=null&&kv.getValue()<200){
                PlayerFiredEvent.playerShields.put(kv.getKey(),kv.getValue()+5);
                //TODO 这里要异步发送提高性能
                Sponge.getServer().getPlayer(kv.getKey()).ifPresent(p->p.sendMessage(Text.of("恢复了您的5点护盾")));
            }
        }
        LOGGER.info("恢复了{}名玩家的护盾",PlayerFiredEvent.playerShields.size());
    }
}

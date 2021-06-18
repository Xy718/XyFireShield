package xy.xy718.xyfires;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.TextTemplate;
import xy.xy718.xyfires.common.service.ShieldService;
import xy.xy718.xyfires.event.PlayerFiredEvent;

import java.util.concurrent.TimeUnit;

@Plugin(
        id = XyFireShieldPlugin.PLUGIN_ID,
        name = XyFireShieldPlugin.NAME,
        description = "??????",
        url = "https://xy718.xyz",
        authors = {
                "Xy718"
        }
)
public class XyFireShieldPlugin {
    @Getter public static final String PLUGIN_ID = "xyfireshield";
    @Getter public static final String NAME = "XyFireShield";

    public static final Logger LOGGER = LoggerFactory.getLogger(NAME);
    public static TextTemplate shieldDefenseTextTpl = TextTemplate.of(
            "为您抵挡了一次火焰伤害，原本有",
            TextTemplate.arg("old"),
            "点护盾，现在剩余",
            TextTemplate.arg("new"),
            "点。");
    public static TextTemplate shieldUnDefenseTextTpl = TextTemplate.of(
            "由于火焰对于护盾的伤害",
            TextTemplate.arg("fireDamage"),
            "大于护盾值",
            TextTemplate.arg("shieldValue"),
            "，无法防御本次火焰伤害");

    @Listener
    public void onGameStarting(GameInitializationEvent event) {
        LOGGER.info("配置加载完成,{}开始注册事件与指令~",NAME);
        Sponge.getEventManager().registerListeners(this,new PlayerFiredEvent());
        Task playerShieldRecoveryTask = Task.builder()
                .execute(
                        task -> {
                            ShieldService.playerShieldRecovery();
                        }
                )
                //.async()
                .interval(5, TimeUnit.SECONDS)
                .name("XyFireShield-PlayerShieldRecoveryTask")
                .submit(this);
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {
    }
}

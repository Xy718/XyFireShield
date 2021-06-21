package xyz.xy718.xyfires.event;

import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.effect.potion.PotionEffect;
import org.spongepowered.api.effect.potion.PotionEffectTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.damage.DamageTypes;
import org.spongepowered.api.event.cause.entity.damage.source.DamageSource;
import org.spongepowered.api.event.entity.DamageEntityEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.filter.cause.Root;
import org.spongepowered.api.text.Text;
import xyz.xy718.xyfires.XyFireShieldPlugin;

import java.util.*;

/**
 * 玩家浴火的事件
 * @author Xy718
 * @date 2021-06-16 22:37:09
 */
public class PlayerFiredEvent {


    private static final Logger LOGGER = XyFireShieldPlugin.LOGGER;

    public static Map<UUID, Integer> playerShields=new HashMap<>();

    @Listener
    public void onFall(
            DamageEntityEvent event,
            @Root DamageSource source,
            @Getter("getTargetEntity") Player subject
    ) {
        if(source.getType().equals(DamageTypes.FIRE)){
            LOGGER.info("take fire damage: {}",subject.getName());
            //不存在就新建一个
            Integer shieldValue = playerShields.compute(subject.getUniqueId(),(k,v)->(v==null)?200:v);
            LOGGER.info("remaining shield value: {}",shieldValue);
            if(shieldValue>0){
                //结算伤害
                int damage =(int)event.getBaseDamage()*10;
                if(shieldValue>= damage){
                    playerShields.put(subject.getUniqueId(),shieldValue- damage);
                    subject.sendMessage(XyFireShieldPlugin.shieldDefenseTextTpl.apply(ImmutableMap.of(
                            "old",
                            Text.of(shieldValue),
                            "new",
                            Text.of(shieldValue- damage)
                    )).build());
                    event.setCancelled(true);
                    List<PotionEffect> effects =new ArrayList<>();
                    subject.get(Keys.POTION_EFFECTS).ifPresent(effects::addAll);
                    effects.add(PotionEffect.of(PotionEffectTypes.FIRE_RESISTANCE,1,19));
                    subject.offer(Keys.POTION_EFFECTS, effects);
                }else{
                    subject.sendMessage(XyFireShieldPlugin.shieldUnDefenseTextTpl.apply(ImmutableMap.of(
                            "fireDamage",
                            Text.of(damage),
                            "shieldValue",
                            Text.of(shieldValue)
                    )).build());
                }
            }
        }
    }
}

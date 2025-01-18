package com.lnatit.ccw.item.sugaring;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.alchemy.Potion;

import java.util.List;

public class Sugar {
    public static final Codec<Sugar> CODEC =
            RecordCodecBuilder.create(ins -> ins.group(
                    Codec.STRING.fieldOf("name").forGetter(o -> o.name),
                    Potion.CODEC.fieldOf("potion").forGetter(o -> o.potion),
                    Codec.INT.fieldOf("duration").forGetter(o -> o.duration)
            ).apply(ins, Sugar::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, Sugar> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.STRING_UTF8,
                    o -> o.name,
                    Potion.STREAM_CODEC,
                    o -> o.potion,
                    ByteBufCodecs.VAR_INT,
                    o -> o.duration,
                    Sugar::new
            );
    public static final Sugar VANILLA = new Sugar("vanilla", null, 0);

    private final String name;
    private final Holder<Potion> potion;
    private final int duration;

    public Sugar(String name, Holder<Potion> potion, int duration) {
        this.name = name;
        this.potion = potion;
        this.duration = duration;
    }

    public void applySugarOn(LivingEntity entity) {
        if (potion != null) {
            List<MobEffectInstance> effects = potion.value().getEffects();
            for (MobEffectInstance effect : effects) {
                Holder<MobEffect> apply = effect.getEffect();
                // Instantenous effect behaves differently
                if (apply.value().isInstantenous()) {
                    apply.value().applyInstantenousEffect(entity, entity, entity, effect.getAmplifier(), 0.5);
                } else {
                    MobEffectInstance exist = entity.getEffect(apply);
                    int duration = this.duration;
                    if (exist != null) {
                        duration += exist.getDuration();
                    }
                    entity.addEffect(new MobEffectInstance(apply, duration, effect.getAmplifier()));
                }
            }
        }
    }
}

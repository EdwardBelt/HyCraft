package es.edwardbelt.hycraft.network.handler.minecraft.manager.entity;

import com.hypixel.hytale.component.ComponentAccessor;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.math.vector.Vector3d;
import com.hypixel.hytale.math.vector.Vector3f;
import com.hypixel.hytale.protocol.*;
import com.hypixel.hytale.server.core.asset.type.item.config.Item;
import com.hypixel.hytale.server.core.entity.LivingEntity;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.entity.knockback.KnockbackComponent;
import com.hypixel.hytale.server.core.inventory.Inventory;
import com.hypixel.hytale.server.core.modules.entity.component.HeadRotation;
import com.hypixel.hytale.server.core.modules.entity.component.TransformComponent;
import com.hypixel.hytale.server.core.modules.entity.damage.Damage;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCalculatorSystems;
import com.hypixel.hytale.server.core.modules.entity.damage.DamageCause;
import com.hypixel.hytale.server.core.modules.entity.damage.DeathComponent;
import com.hypixel.hytale.server.core.modules.entitystats.asset.DefaultEntityStatTypes;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.Interaction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.RootInteraction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.server.DamageEntityInteraction;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.server.combat.DamageCalculator;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.server.combat.DamageClass;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.server.combat.DamageEffects;
import com.hypixel.hytale.server.core.modules.interaction.interaction.config.server.combat.Knockback;
import com.hypixel.hytale.server.core.universe.PlayerRef;
import com.hypixel.hytale.server.core.universe.world.World;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;
import es.edwardbelt.hycraft.mapping.MappingRegistry;
import es.edwardbelt.hycraft.network.handler.hytale.HytaleUtil;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.InteractionContext;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.InteractionExtractorResponse;
import es.edwardbelt.hycraft.network.handler.hytale.manager.interaction.InteractionManager;
import es.edwardbelt.hycraft.network.handler.minecraft.data.entity.Entity;
import es.edwardbelt.hycraft.network.handler.minecraft.data.entity.TextEntity;
import es.edwardbelt.hycraft.network.handler.minecraft.data.entity.goal.impl.ParabolicMoveGoal;
import es.edwardbelt.hycraft.network.handler.minecraft.data.entity.metadata.PoseMetadataValue;
import es.edwardbelt.hycraft.network.handler.minecraft.data.item.ItemStack;
import es.edwardbelt.hycraft.network.player.ClientConnection;
import es.edwardbelt.hycraft.protocol.packet.play.*;
import es.edwardbelt.hycraft.util.reflection.FieldAccessor;
import es.edwardbelt.hycraft.util.reflection.MethodAccessor;
import es.edwardbelt.hycraft.util.reflection.Reflections;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class EntityManager {
    private static final EntityManager INSTANCE = new EntityManager();
    public static EntityManager get() { return INSTANCE; }

    public void handleEntityUpdate(ClientConnection connection, int entityId, ComponentUpdate[] components) {
        if (connection.getNetworkId() == entityId) {
            handleOwnConnection(connection, components);
            return;
        }

        boolean isEntitySpawned = connection.isEntitySpawned(entityId);
        if (!isEntitySpawned) addEntity(connection, entityId, components);
        else updateEntity(connection, entityId, components);
    }

    public void removeEntities(ClientConnection connection, int[] entityIds) {
        RemoveEntitiesPacket packet = new RemoveEntitiesPacket(entityIds);
        connection.getChannel().writeAndFlush(packet);
        connection.removeEntities(entityIds);
    }

    public void handleOwnConnection(ClientConnection connection, ComponentUpdate[] components) {
        for (ComponentUpdate componentUpdate : components) {
            switch (componentUpdate.getTypeId()) {
                /*case 11: // TODO: id 33 seems to be fire effect
                    EntityEffectsUpdate effectsUpdate = (EntityEffectsUpdate) componentUpdate;
                    for (EntityEffectUpdate entityEffectUpdate : effectsUpdate.entityEffectUpdates) {
                        System.out.println("effect: " + entityEffectUpdate.id);
                        System.out.println(EntityEffect.getAssetMap().getAsset(entityEffectUpdate.id).getId());
                        System.out.println("infinite: " + entityEffectUpdate.infinite);
                        System.out.println("time: " + entityEffectUpdate.remainingTime);
                    }
                    break;*/
                case 9:
                    ModelTransform transformUpdate = ((TransformUpdate) componentUpdate).transform;

                    connection.respawn(
                            transformUpdate.position.x,
                            transformUpdate.position.y,
                            transformUpdate.position.z,
                            transformUpdate.bodyOrientation.yaw,
                            transformUpdate.bodyOrientation.pitch
                    );
                    break;
                case 8:
                    EntityStatsUpdate entityStatsUpdate = (EntityStatsUpdate) componentUpdate;
                    EntityStatUpdate[] healthUpdates = entityStatsUpdate.entityStatUpdates.get(DefaultEntityStatTypes.getHealth());
                    if (healthUpdates == null) continue;
                    float health = connection.getHealth();
                    for (EntityStatUpdate statUpdate : healthUpdates) {
                        switch (statUpdate.op) {
                            case Init, Set -> connection.setHealth(statUpdate.value);
                            case Reset -> connection.setHealth(100);
                            case Add -> connection.setHealth(health+statUpdate.value);
                            case Remove -> connection.setHealth(health-statUpdate.value);
                            /*case PutModifier -> {
                                System.out.println("amount modifier: " + statUpdate.modifier.amount);
                                System.out.println("type modifier: " + statUpdate.modifier.calculationType.name());
                            }*/
                        }
                    }

                    if (connection.getHealth() <= 0) {
                        EntitySoundPacket deathSoundPacket = new EntitySoundPacket(1205, 7, connection.getNetworkId(), 1, 1, 0);
                        connection.getChannel().writeAndFlush(deathSoundPacket);
                        PlayerRef playerRef = connection.getPlayerRef();
                        Ref<EntityStore> entityRef = playerRef.getReference();
                        Store<EntityStore> store = entityRef.getStore();
                        store.getExternalData().getWorld().execute(() -> DeathComponent.respawn(store, entityRef));
                        /*GameEventPacket respawnScreenPacket = new GameEventPacket(11, 1);
                        connection.getChannel().writeAndFlush(respawnScreenPacket);

                        CombatDeathPacket deathPacket = new CombatDeathPacket(connection.getNetworkId(), "You died!");
                        connection.getChannel().writeAndFlush(deathPacket);*/
                    } else {
                        SetHealthPacket healthPacket = new SetHealthPacket(connection.getHealth()/5, 20, 0);
                        connection.getChannel().writeAndFlush(healthPacket);
                    }

                    break;
            }

        }
    }

    public void addEntity(ClientConnection connection, int entityId, ComponentUpdate[] components) {
        Store<EntityStore> entityStore = connection.getPlayerRef().getReference().getStore();
        Entity entity = new Entity(entityId);
        connection.addSpawnedEntity(entityId, entity);

        entityStore.getExternalData().getWorld().execute(() -> {
            String possibleHologram = null;
            String assetId = null;
            List<ComponentUpdate> updates = new ArrayList<>();

            for (ComponentUpdate component : components) {
                updates.add(component);
                switch (component.getTypeId()) {
                    case 0:
                        NameplateUpdate nameplateUpdate = (NameplateUpdate) component;
                        possibleHologram = nameplateUpdate.text;
                        entity.setCustomName(possibleHologram);
                        break;
                    case 8:
                        EntityStatsUpdate entityStatsUpdate = (EntityStatsUpdate) component;
                        EntityStatUpdate[] healthUpdates = entityStatsUpdate.entityStatUpdates.get(DefaultEntityStatTypes.getHealth());
                        if (healthUpdates == null) continue;
                        float health = entity.getHealth();
                        for (EntityStatUpdate statUpdate : healthUpdates) {
                            switch (statUpdate.op) {
                                case Init, Set -> entity.setHealth(statUpdate.value);
                                case Reset -> entity.setHealth(100);
                                case Add -> entity.setHealth(health+statUpdate.value);
                                case Remove -> entity.setHealth(health-statUpdate.value);
                            }
                        }

                        break;
                    case 3:
                        ModelUpdate modelUpdate = (ModelUpdate) component;
                        Model model = modelUpdate.model;
                        if (model == null) continue;
                        assetId = model.assetId;
                        int entityType = MappingRegistry.get().getEntityMapper().getMapping(assetId);
                        entity.setType(entityType);
                        if (assetId.equalsIgnoreCase("player")) {
                            Ref<EntityStore> targetPlayer = entityStore.getExternalData().getRefFromNetworkId(entityId);
                            PlayerRef targetPlayerRef = entityStore.getComponent(targetPlayer, PlayerRef.getComponentType());

                            entity.setUuid(targetPlayerRef.getUuid());
                        }
                        break;
                    case 9:
                        ModelTransform transformUpdate = ((TransformUpdate) component).transform;
                        Position position = transformUpdate.position;
                        entity.setX(position.x);
                        entity.setY(position.y);
                        entity.setZ(position.z);
                        float[] rotation = HytaleUtil.getMinecraftYawPitch(transformUpdate.bodyOrientation.yaw, transformUpdate.lookOrientation.pitch);
                        float headYaw = HytaleUtil.getMinecraftYaw(transformUpdate.lookOrientation.yaw);
                        entity.setRotation(rotation[0], rotation[1]);
                        entity.setHeadYaw(headYaw);
                        break;
                    case 7:
                        EquipmentUpdate equipmentUpdate = (EquipmentUpdate) component;

                        String mainHand = equipmentUpdate.rightHandItemId;
                        String offHand = equipmentUpdate.leftHandItemId;
                        String[] armor = equipmentUpdate.armorIds;

                        if (mainHand != null) entity.setEquipment(SetEquipmentPacket.Type.MAIN_HAND, ItemStack.fromHytaleItemId(mainHand));
                        if (offHand != null) entity.setEquipment(SetEquipmentPacket.Type.OFF_HAND, ItemStack.fromHytaleItemId(offHand));

                        if (armor != null) {
                            for (int i=0; i<armor.length; i++) {
                                String piece = armor[i];
                                if (piece == null || piece.isEmpty()) piece = "Empty";
                                SetEquipmentPacket.Type type = SetEquipmentPacket.Type.fromId(5-i);
                                entity.setEquipment(type, ItemStack.fromHytaleItemId(piece));
                            }
                        }

                        break;
                    case 5:
                        ItemWithAllMetadata item = ((ItemUpdate) component).item;
                        entity.setType(70);
                        entity.setItem(ItemStack.fromHytale(item));
                        break;

                }
            }

            if (possibleHologram != null && assetId != null && assetId.equalsIgnoreCase("Projectile")) {
                TextEntity textEntity = new TextEntity(entityId);
                textEntity.setPosition(entity.getX(), entity.getY(), entity.getZ());
                textEntity.setText(possibleHologram);
                textEntity.setFace((byte) 1);
                textEntity.setLineWidth(Integer.MAX_VALUE);
                textEntity.setBackgroundColor(0, 0, 0, 0);

                connection.addSpawnedEntity(entityId, textEntity);
                textEntity.spawn(connection);
                return;
            }


            connection.addSpawnedEntity(entityId, entity);
            entity.spawn(connection);
        });
    }

    public void updateEntity(ClientConnection connection, int entityId, ComponentUpdate[] components) {
        Entity entity = connection.getSpawnedEntity(entityId);
        if (entity == null) return;
        if (entity.getType() == -1) return;

        for (ComponentUpdate component : components) {
            switch (component.getTypeId()) {
                case 7:
                    EquipmentUpdate equipmentUpdate = (EquipmentUpdate) component;

                    String mainHand = equipmentUpdate.rightHandItemId;
                    String offHand = equipmentUpdate.leftHandItemId;
                    String[] armor = equipmentUpdate.armorIds;

                    if (mainHand != null) entity.setEquipment(SetEquipmentPacket.Type.MAIN_HAND, ItemStack.fromHytaleItemId(mainHand));
                    if (offHand != null) entity.setEquipment(SetEquipmentPacket.Type.OFF_HAND, ItemStack.fromHytaleItemId(offHand));

                    if (armor != null) {
                        for (int i=0; i<armor.length; i++) {
                            String piece = armor[i];
                            if (piece == null || piece.isEmpty()) piece = "Empty";
                            SetEquipmentPacket.Type type = SetEquipmentPacket.Type.fromId(5-i);
                            entity.setEquipment(type, ItemStack.fromHytaleItemId(piece));
                        }
                    }

                    entity.sendEquipment(connection);
                    break;
                case 8:
                    EntityStatsUpdate entityStatsUpdate = (EntityStatsUpdate) component;
                    EntityStatUpdate[] healthUpdates = entityStatsUpdate.entityStatUpdates.get(DefaultEntityStatTypes.getHealth());
                    if (healthUpdates == null) continue;
                    float health = entity.getHealth();

                    for (EntityStatUpdate statUpdate : healthUpdates) {
                        switch (statUpdate.op) {
                            case Init, Set -> entity.setHealth(statUpdate.value);
                            case Reset -> {
                                entity.setHealth(100);
                                entity.despawn(connection);
                                entity.spawn(connection);
                            }
                            case Add -> entity.setHealth(health+statUpdate.value);
                            case Remove -> entity.setHealth(health-statUpdate.value);
                        }
                    }

                    if (entity.getHealth() < 0) entity.setHealth(1); // set health 0 is handled by the animation packet handler

                    entity.sendMetadata(connection);
                    break;
                case 9:
                    ModelTransform transformUpdate = ((TransformUpdate) component).transform;
                    Position position = transformUpdate.position;
                    teleportEntity(connection, entity, position.x, position.y, position.z, transformUpdate);
                    break;
                case 10:
                    MovementStates movementStates = ((MovementStatesUpdate) component).movementStates;
                    entity.setOnGround(movementStates.onGround);

                    PoseMetadataValue.Pose pose;
                    if (movementStates.swimming) pose = PoseMetadataValue.Pose.SWIMMING;
                    else if (movementStates.crouching) pose = PoseMetadataValue.Pose.SNEAKING;
                    else if (movementStates.gliding) pose = PoseMetadataValue.Pose.FALL_FLYING;
                    else pose = PoseMetadataValue.Pose.STANDING;

                    entity.setPose(pose);
                    entity.sendMetadata(connection);
                    break;
                case 2:
                    CombatTextUpdate textUpdate = (CombatTextUpdate) component;
                    TextEntity textEntity = new TextEntity(ThreadLocalRandom.current().nextInt(5_000_000, 10_000_000));
                    textEntity.setText(textUpdate.text);
                    textEntity.setFace((byte) 1);
                    textEntity.setBackgroundColor(0, 0, 0, 0);
                    textEntity.setPosition(entity.getX(), entity.getY() + 2, entity.getZ());
                    textEntity.spawn(connection);

                    double angle = ThreadLocalRandom.current().nextDouble(0, Math.PI * 2);
                    double radius = ThreadLocalRandom.current().nextDouble(0.5, 1.5);
                    double endX = textEntity.getX() + Math.cos(angle) * radius;
                    double endZ = textEntity.getZ() + Math.sin(angle) * radius;
                    double endY = textEntity.getY() + 0.75;

                    ParabolicMoveGoal goal = new ParabolicMoveGoal(
                            new Vector3d(textEntity.getX(), textEntity.getY(), textEntity.getZ()),
                            new Vector3d(endX, endY, endZ),
                            0.5,
                            1000L
                    );
                    goal.setConnection(connection);
                    goal.setEntity(textEntity);
                    goal.setEndRunnable(() -> textEntity.despawn(connection));
                    goal.init();

                    break;
            }
        }
    }

    public void teleportEntity(ClientConnection connection, Entity entity, double x, double y, double z, ModelTransform transformUpdate) {
        double prevX = entity.getX();
        double prevY = entity.getY();
        double prevZ = entity.getZ();

        double deltaX = x - prevX;
        double deltaY = y - prevY;
        double deltaZ = z - prevZ;

        boolean positionChanged = deltaX != 0 || deltaY != 0 || deltaZ != 0;
        boolean mustTeleport = Math.abs(deltaX) > 7.9 || Math.abs(deltaY) > 7.9 || Math.abs(deltaZ) > 7.9;

        float yaw = HytaleUtil.getMinecraftYaw(transformUpdate.bodyOrientation.yaw);
        float pitch = HytaleUtil.getMinecraftPitch(transformUpdate.lookOrientation.pitch);
        float headYaw = HytaleUtil.getMinecraftYaw(transformUpdate.lookOrientation.yaw);

        if (mustTeleport) {
            entity.teleport(connection, x, y, z, yaw, pitch);
        } else if (positionChanged) {
            entity.moveAndRot(connection, x, y, z, yaw, pitch);
        } else {
            entity.rotate(connection, yaw, pitch);
        }

        entity.rotateHead(connection, headYaw);
    }

    private static final FieldAccessor<DamageCalculator> DAMAGE_CALCULATOR_FIELD = Reflections.getField(DamageEntityInteraction.class, DamageCalculator.class);
    private static final FieldAccessor<DamageEffects> DAMAGE_EFFECTS_FIELD = Reflections.getField(DamageEntityInteraction.class, DamageEffects.class);
    private static final FieldAccessor<DamageEntityInteraction.EntityStatOnHit[]> ENTITY_STAT_ON_HIT_FIELD = Reflections.getField(DamageEntityInteraction.class, DamageEntityInteraction.EntityStatOnHit[].class);
    private static final MethodAccessor CALC_KNOCKBACK_AND_ARMOR_METHOD = Reflections.getMethod(
            DamageEntityInteraction.class,
            DamageClass.class,
            Object2FloatMap.class,
            Ref.class,
            Ref.class,
            float[].class,
            double[].class,
            ComponentAccessor.class);

    public void hitEntity(ClientConnection connection, int entityId) {
        Ref<EntityStore> attackerRef = connection.getPlayerRef().getReference();
        Store<EntityStore> entityStore = attackerRef.getStore();
        World world = entityStore.getExternalData().getWorld();
        Ref<EntityStore> targetRef = entityStore.getExternalData().getRefFromNetworkId(entityId);

        if (targetRef == null || !attackerRef.isValid() || !targetRef.isValid()) return;

        world.execute(() -> {
            LivingEntity playerEntity = entityStore.getComponent(attackerRef, Player.getComponentType());
            Vector3d attackerPos = entityStore.getComponent(attackerRef, TransformComponent.getComponentType()).getPosition();
            Vector3d targetPos = entityStore.getComponent(targetRef, TransformComponent.getComponentType()).getPosition();

            Inventory inventory = playerEntity.getInventory();
            com.hypixel.hytale.server.core.inventory.ItemStack hand = inventory.getActiveHotbarItem();
            Item item = hand != null ? hand.getItem() : null;
            String itemId = item != null ? item.getId() : "Empty";

            Map<String, String> interactionVars = item != null ? item.getInteractionVars() : new HashMap<>();

            String interactionId = item != null ? item.getInteractions().get(InteractionType.Primary) : "*Empty_Interactions_Primary";
            if (interactionId == null) return;

            RootInteraction rootInteraction = RootInteraction.getAssetMap().getAsset(interactionId);
            if (rootInteraction == null) return;

            Interaction mainInteraction = InteractionManager.getInteractionFromRoot(rootInteraction);

            long currentTime = System.currentTimeMillis();
            float cooldown = rootInteraction.getCooldown() == null ? 0.35F : rootInteraction.getCooldown().cooldown;

            if (connection.getItemIdsCooldowns().containsKey(itemId)) {
                if (currentTime < connection.getItemIdsCooldowns().get(itemId)) {
                    SoundPacket soundPacket = new SoundPacket(1198, 7, targetPos.x, targetPos.y, targetPos.z, 1, 1, 0);
                    connection.getChannel().writeAndFlush(soundPacket);
                    return;
                };
            }

            long invalidUntil = currentTime + (long) (cooldown * 1000);
            connection.getItemIdsCooldowns().put(itemId, invalidUntil);

            SetCooldownPacket cooldownPacket = new SetCooldownPacket("slot:"+inventory.getActiveHotbarSlot(), (int)(cooldown*20));
            connection.getChannel().writeAndFlush(cooldownPacket);

            InteractionExtractorResponse result = InteractionManager.get().extract(new InteractionContext(connection, interactionVars), mainInteraction);
            if (result.getInteraction() == null || !(result.getInteraction() instanceof DamageEntityInteraction)) return;

            DamageEntityInteraction damageInteraction = (DamageEntityInteraction) result.getInteraction();

            result.getInteractionPath().forEach(i -> InteractionManager.get().playInteraction(connection, i, InteractionType.Primary));

            SoundPacket soundPacket = new SoundPacket(1200, 7, targetPos.x, targetPos.y, targetPos.z, 1, 1, 0);
            connection.getChannel().writeAndFlush(soundPacket);

            DamageCalculator damageCalculator = DAMAGE_CALCULATOR_FIELD.get(damageInteraction);

            DamageEffects damageEffects = DAMAGE_EFFECTS_FIELD.get(damageInteraction);
            DamageEntityInteraction.EntityStatOnHit[] entityStatsOnHit = ENTITY_STAT_ON_HIT_FIELD.get(damageInteraction);

            if (damageCalculator == null) return;

            Object2FloatMap<DamageCause> damageMap = damageCalculator.calculateDamage(1);

            if (damageMap == null || damageMap.isEmpty()) return;

            float[] armorDamageModifiers = new float[]{0.0F, 1.0F};
            double[] knockbackMultiplier = new double[]{1.0};

            CALC_KNOCKBACK_AND_ARMOR_METHOD.invoke(null,
                    damageCalculator.getDamageClass(),
                    damageMap,
                    targetRef,
                    attackerRef,
                    armorDamageModifiers,
                    knockbackMultiplier,
                    entityStore
            );

            KnockbackComponent knockbackComponent = null;
            if (damageEffects != null && damageEffects.getKnockback() != null) {
                knockbackComponent = entityStore.getComponent(targetRef, KnockbackComponent.getComponentType());
                if (knockbackComponent == null) {
                    knockbackComponent = new KnockbackComponent();
                    entityStore.putComponent(targetRef, KnockbackComponent.getComponentType(), knockbackComponent);
                }

                Knockback knockback = damageEffects.getKnockback();

                HeadRotation attackerHeadRotation = entityStore.getComponent(attackerRef, HeadRotation.getComponentType());
                Vector3f attackerDirection = attackerHeadRotation != null ?
                        attackerHeadRotation.getRotation() : Vector3f.ZERO;

                Vector3d knockbackVec = knockback.calculateVector(
                        attackerPos,
                        attackerDirection.getYaw(),
                        targetPos
                ).scale(knockbackMultiplier[0]);

                knockbackComponent.setVelocity(knockbackVec);
                knockbackComponent.setVelocityType(knockback.getVelocityType());
                knockbackComponent.setVelocityConfig(knockback.getVelocityConfig());
                knockbackComponent.setDuration(knockback.getDuration());
            }

            Damage.EntitySource source = new Damage.EntitySource(attackerRef);

            Player attackerPlayerComponent = entityStore.getComponent(attackerRef, Player.getComponentType());
            com.hypixel.hytale.server.core.inventory.ItemStack itemInHand = (attackerPlayerComponent != null &&
                    !attackerPlayerComponent.canApplyItemStackPenalties(attackerRef, entityStore)) ?
                    null : hand;

            Damage[] hits = DamageCalculatorSystems.queueDamageCalculator(
                    world,
                    damageMap,
                    targetRef,
                    null,
                    source,
                    itemInHand
            );

            if (hits.length > 0) {
                Damage firstDamage = hits[0];

                DamageCalculatorSystems.Sequence sequentialHits = new DamageCalculatorSystems.Sequence();
                DamageCalculatorSystems.DamageSequence seq = new DamageCalculatorSystems.DamageSequence(
                        sequentialHits,
                        damageCalculator
                );
                seq.setEntityStatOnHit(entityStatsOnHit);
                firstDamage.putMetaObject(DamageCalculatorSystems.DAMAGE_SEQUENCE, seq);

                if (damageEffects != null) {
                    damageEffects.addToDamage(firstDamage);
                }

                for (Damage damageEvent : hits) {
                    if (knockbackComponent != null) {
                        damageEvent.putMetaObject(Damage.KNOCKBACK_COMPONENT, knockbackComponent);
                    }

                    float damageValue = damageEvent.getAmount();
                    damageValue += armorDamageModifiers[0];
                    damageEvent.setAmount(damageValue * Math.max(0.0F, armorDamageModifiers[1]));

                    entityStore.invoke(targetRef, damageEvent);
                }
            }
        });
    }
}
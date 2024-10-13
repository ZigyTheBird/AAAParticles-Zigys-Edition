package com.zigythebird.zigys_particles.api.client.effekseer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.zigythebird.zigys_particles.client.registry.EffectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.util.HashMap;
import java.util.Map;

public class EffekRenderHelper {
    private static final Map<Object, Map<ResourceLocation, ClientEffek>> OBJECT_EFFEKS = new HashMap<>();
    private static final Map<ResourceLocation, ClientEffek> EFFEKS = new HashMap<>();

    public static class ClientEffek {
        private final ParticleEmitter emitter;
        private boolean wasUsed;
        private final boolean shouldDestroyIfInactive;

        public ClientEffek(ResourceLocation id, boolean shouldDestroyIfInactive) {
            this.emitter = EffectRegistry.get(id).play();
            this.shouldDestroyIfInactive = shouldDestroyIfInactive;
        }

        public void render(PoseStack poseStack, float scaleX, float scaleY, float scaleZ) {
            Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
            Matrix4f m = poseStack.last().pose();
            float[] positionMatrix = {m.m00(), m.m01(), m.m02(), m.m03(),
                    m.m10(), m.m11(), m.m12(), m.m13(),
                    m.m20(), m.m21(), m.m22(), m.m23(),
                    m.m30(), m.m31(), m.m32(), m.m33()};
            emitter.setTransformMatrix(positionMatrix);
            emitter.setScale(scaleX, scaleY, scaleZ);
            emitter.setPosition((float) (m.m30() + cameraPos.x), (float) (m.m31() + cameraPos.y), (float) (m.m32() + cameraPos.z));
            wasUsed = true;
        }

        private boolean wasUsed() {
            if (wasUsed) {
                wasUsed = false;
                return true;
            }
            return false;
        }
    }

    public static ClientEffek getOrCreateEffekForObject(Object object, ResourceLocation id, ResourceLocation effekID) {
        return getOrCreateEffekForObject(object, id, effekID, true);
    }

    public static ClientEffek getOrCreateEffekForObject(Object object, ResourceLocation id, ResourceLocation effekID, boolean shouldDestroyIfInactive) {
        if (!OBJECT_EFFEKS.containsKey(object)) {
            OBJECT_EFFEKS.put(object, new HashMap<>());
        }
        Map<ResourceLocation, ClientEffek> effeks = OBJECT_EFFEKS.get(object);
        if (!effeks.containsKey(id)) {
            effeks.put(id, new ClientEffek(effekID, shouldDestroyIfInactive));
        }
        return effeks.get(id);
    }

    public static ClientEffek getOrCreateEffek(ResourceLocation id, ResourceLocation effekID) {
        return getOrCreateEffek(id, effekID, true);
    }

    public static ClientEffek getOrCreateEffek(ResourceLocation id, ResourceLocation effekID, boolean shouldDestroyIfInactive) {
        if (!EFFEKS.containsKey(id)) {
            EFFEKS.put(id, new ClientEffek(effekID, shouldDestroyIfInactive));
        }
        return EFFEKS.get(id);
    }

    public static boolean removeEffekForObject(Object object, ResourceLocation id) {
        if (!OBJECT_EFFEKS.containsKey(object)) {
            return false;
        }
        Map<ResourceLocation, ClientEffek> effeks = OBJECT_EFFEKS.get(object);
        if (effeks.containsKey(id)) {
            effeks.remove(id);
            return true;
        }
        return false;
    }

    public static boolean removeEffek(ResourceLocation id) {
        if (EFFEKS.containsKey(id)) {
            EFFEKS.remove(id);
            return true;
        }
        return false;
    }

    public static void deleteInactiveEffeks() {
        for (Map.Entry<Object, Map<ResourceLocation, ClientEffek>> entry : OBJECT_EFFEKS.entrySet()) {
            for (Map.Entry<ResourceLocation, ClientEffek> entry1 : entry.getValue().entrySet()) {
                if (!entry1.getValue().wasUsed() && entry1.getValue().shouldDestroyIfInactive) {
                    entry1.getValue().emitter.stop();
                    entry.getValue().remove(entry1.getKey());
                    if (entry.getValue().isEmpty()) {
                        OBJECT_EFFEKS.remove(entry.getKey());
                    }
                }
            }
        }
        for (Map.Entry<ResourceLocation, ClientEffek> entry1 : EFFEKS.entrySet()) {
            if (!entry1.getValue().wasUsed() && entry1.getValue().shouldDestroyIfInactive) {
                entry1.getValue().emitter.stop();
                EFFEKS.remove(entry1.getKey());
            }
        }
    }
}

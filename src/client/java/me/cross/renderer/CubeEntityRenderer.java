package me.cross.renderer;

import me.cross.CrossClient;
import me.cross.entity.CubeEntity;
import me.cross.model.CubeEntityModel;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class CubeEntityRenderer extends MobEntityRenderer<CubeEntity, CubeEntityModel> {
    public CubeEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new CubeEntityModel(context.getPart(CrossClient.MODEL_CUBE_LAYER)), 0.5F);
    }

    @Override
    public Identifier getTexture(CubeEntity entity) {
        return new Identifier("cross", "textures/entity/cube/cube.png");
    }
}

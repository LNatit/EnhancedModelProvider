package com.lnatit.emp.data.model;

import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.function.Function;

public class Generators {
    public static ResourceLocation getDefaultModel(ResourceLocation id) {
        return id.withPrefix("item/");
    }

    interface Acceptor {
        ResourceLocation getId();

        ResourceLocation getModelResourceLocation();

        TextureMapping getTextureMapping();

        ModelTemplate getModelTemplate();

        ClientItemModelGenerators.ClientItemBuilder getClientItemBuilder();

        ClientItem getClientItem();
    }

    /**
     * Methods within this interface with {@code Default} in it's name will wrap the model with {@link Generators#getDefaultModel(ResourceLocation)}, the others will not.
     */
    interface ModelAcceptor extends Acceptor {
        Impl withModel(ResourceLocation modelResourceLocation);

        default Impl withModelPath(String modelPath) {
            return this.withModel(this.getId().withPath(modelPath));
        }

        default Impl withModelPrefix(String prefix) {
            return this.withModel(this.getId().withPrefix(prefix));
        }

        default Impl withModelAffix(String prefix, String suffix) {
            return this.withModel(this.getId().withPrefix(prefix).withSuffix(suffix));
        }

        default Impl withDefaultModelPath(String defaultModelPath) {
            return this.withModel(getDefaultModel(this.getId().withPath(defaultModelPath)));
        }

        default Impl withDefaultModelSuffix(String suffix) {
            return this.withModel(getDefaultModel(this.getId()).withSuffix(suffix));
        }
    }

    interface TextureMappingAcceptor extends Acceptor {
        Impl withTextureMapping(TextureMapping textureMapping);
    }

    interface ModelTemplateAcceptor extends Acceptor {
        Impl withModelTemplate(ModelTemplate modelTemplate);
    }

    interface ClientItemAcceptor extends Acceptor {
        Impl withClientItem(ClientItem clientItem);

        default Impl withClientItem(Function<ClientItemModelGenerators.ClientItemBuilder, ClientItem> clientItem) {
            return this.withClientItem(clientItem.apply(this.getClientItemBuilder()));
        }
    }

    public interface Init {
        Impl withId(ResourceLocation id);

        default Impl withId(DeferredHolder<?, ?> registryItem) {
            return this.withId(registryItem.getId());
        }
    }

    public interface Impl extends ModelAcceptor, TextureMappingAcceptor, ModelTemplateAcceptor, ClientItemAcceptor {
        void all();

        void modelOnly();

        void clientItemOnly();

        default void generate() {
            this.all();
        }
    }
}

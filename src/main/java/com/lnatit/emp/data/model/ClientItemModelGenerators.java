package com.lnatit.emp.data.model;

import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ClientItem;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * <li>Models: ResourceLocation(ModelLocation), TextureMapping & ModelTemplate
 * <li>ClientItems: ResourceLocation(RegistryId) & ClientItem
 * <p>
 * For mostly clientItems {@code ModelLocation = RegistryId.withSuffix("item/");}
 */
public class ClientItemModelGenerators
{
    public final ClientItemCollector clientItemOutput;
    public final BiConsumer<ResourceLocation, ModelInstance> modelOutput;

    public ClientItemModelGenerators(ClientItemCollector clientItemOutput, ItemModelGenerators template) {
        this.clientItemOutput = clientItemOutput;
        this.modelOutput = template.modelOutput;
    }

    public Generators.Init gen() {
        return new Generator(this);
    }

    public void genClientItem(ResourceLocation registryId, ClientItem clientItem) {
        this.clientItemOutput.accept(registryId, clientItem);
    }

    public void genModel(ResourceLocation modelLocation, TextureMapping textureMapping, ModelTemplate modelTemplate) {
        modelTemplate.create(modelLocation, textureMapping, this.modelOutput);
    }

    public void generate(ResourceLocation registryId, ClientItem clientItem, ResourceLocation modelLocation, TextureMapping textureMapping, ModelTemplate modelTemplate) {
        this.genClientItem(registryId, clientItem);
        this.genModel(modelLocation, textureMapping, modelTemplate);
    }

    public static class Generator implements Generators.Init, Generators.Impl
    {
        private final ClientItemModelGenerators genRef;
        private ResourceLocation id;
        private ResourceLocation modelResourceLocation;
        private TextureMapping textureMapping;
        private ModelTemplate modelTemplate;
        private ClientItem clientItem;

        public Generator(ClientItemModelGenerators genRef) {
            this.genRef = genRef;
        }

        @Override
        public Generators.Impl withId(ResourceLocation id) {
            this.id = id;
            return this;
        }

        @Override
        public Generators.Impl withModel(ResourceLocation modelResourceLocation) {
            this.modelResourceLocation = modelResourceLocation;
            return this;
        }

        @Override
        public Generators.Impl withTextureMapping(TextureMapping textureMapping) {
            this.textureMapping = textureMapping;
            return this;
        }

        @Override
        public Generators.Impl withModelTemplate(ModelTemplate modelTemplate) {
            this.modelTemplate = modelTemplate;
            return this;
        }

        @Override
        public Generators.Impl withClientItem(ClientItem clientItem) {
            this.clientItem = clientItem;
            return this;
        }

        @Override
        public ResourceLocation getId() {
            return this.id;
        }

        @Override
        public ResourceLocation getModelResourceLocation() {
            return this.modelResourceLocation == null ? Generators.getDefaultModel(this.id) : this.modelResourceLocation;
        }

        @Override
        public TextureMapping getTextureMapping() {
            return this.textureMapping == null ? TextureMapping.layer0(this.getModelResourceLocation()) : this.textureMapping;
        }

        @Override
        public ModelTemplate getModelTemplate() {
            return this.modelTemplate == null ? ModelTemplates.FLAT_ITEM : this.modelTemplate;
        }

        @Override
        public ClientItemBuilder getClientItemBuilder() {
            return new ClientItemBuilder(this.getModelResourceLocation());
        }

        @Override
        public ClientItem getClientItem() {
            return this.clientItem == null ? this.getClientItemBuilder().build() : this.clientItem;
        }

        @Override
        public void all() {
            this.genRef.generate(
                    this.getId(),
                    this.getClientItem(),
                    this.getModelResourceLocation(),
                    this.getTextureMapping(),
                    this.getModelTemplate()
            );
        }

        @Override
        public void modelOnly() {
            this.genRef.genModel(
                    this.getModelResourceLocation(),
                    this.getTextureMapping(),
                    this.getModelTemplate()
            );
        }

        @Override
        public void clientItemOnly() {
            this.genRef.genClientItem(
                    this.getId(),
                    this.getClientItem()
            );
        }

        public void likeFlatItem(ResourceLocation id) {
            this.withId(id).all();
        }
    }

    public static class ClientItemBuilder
    {
        @Nonnull
        private ResourceLocation modelResourceLocation;
        private Function<ResourceLocation, ItemModel.Unbaked> unbakedGen;
        private ItemModel.Unbaked unbaked;
        private ClientItem.Properties properties;

        public ClientItemBuilder(ResourceLocation modelResourceLocation) {
            this.modelResourceLocation = modelResourceLocation;
        }

        public void withModel(ResourceLocation modelResourceLocation) {
            this.modelResourceLocation = modelResourceLocation;
        }

        public void withUnbakedGen(Function<ResourceLocation, ItemModel.Unbaked> unbakedGen) {
            this.unbakedGen = unbakedGen;
        }

        public void withUnbaked(ItemModel.Unbaked unbaked) {
            this.unbaked = unbaked;
        }

        public void withProperties(ClientItem.Properties properties) {
            this.properties = properties;
        }

        public ClientItem build() {
            if (this.unbaked == null) {
                if (this.unbakedGen == null) {
                    this.unbakedGen = ItemModelUtils::plainModel;
                }
                this.unbaked = this.unbakedGen.apply(this.modelResourceLocation);
            }
            if (this.properties == null) {
                this.properties = ClientItem.Properties.DEFAULT;
            }

            return new ClientItem(this.unbaked, this.properties);
        }
    }

    /**
     * A more nimble way to DataGen {@link ClientItem} that doesn't actually bound to a registered {@link net.minecraft.world.item.Item}.
     * Basically you can call {@link ClientItemCollector#accept(ResourceLocation, ClientItem)} to generate,
     * but there are also many overloads for convenient uses.
     *
     * @see ModelProvider.ItemInfoCollector
     */
    public static class ClientItemCollector implements BiConsumer<ResourceLocation, ClientItem>
    {
        private final Map<ResourceLocation, ClientItem> clientItemInfos = new HashMap<>();

        @Override
        public void accept(ResourceLocation resourceLocation, ClientItem clientItem) {
            this.clientItemInfos.put(resourceLocation, clientItem);
        }

        public CompletableFuture<?> save(CachedOutput output, PackOutput.PathProvider pathProvider) {
            return DataProvider.saveAll(output, ClientItem.CODEC, pathProvider::json, this.clientItemInfos);
        }
    }
}

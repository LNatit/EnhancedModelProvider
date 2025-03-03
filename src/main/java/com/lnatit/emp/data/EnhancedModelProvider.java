package com.lnatit.emp.data;

import com.lnatit.emp.data.model.ClientItemModelGenerators;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;

import java.util.concurrent.CompletableFuture;

public class EnhancedModelProvider extends ModelProvider
{
    private final PackOutput.PathProvider itemInfoPathProvider;
    private final ClientItemModelGenerators.ClientItemCollector clientItemCollector;

    public EnhancedModelProvider(PackOutput output, String modId) {
        super(output, modId);
        this.itemInfoPathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "items");
        this.clientItemCollector = new ClientItemModelGenerators.ClientItemCollector();
    }

    /**
     * @deprecated Call {@link EnhancedModelProvider#registerModels(BlockModelGenerators, ItemModelGenerators, ClientItemModelGenerators)} instead to customize clientItem & models
     */
    @Deprecated
    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        registerModels(blockModels, itemModels, new ClientItemModelGenerators(this.clientItemCollector, itemModels));
    }

    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels, ClientItemModelGenerators clientItemModels) {
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        // No validation needed maybe
        return CompletableFuture.allOf(super.run(output), this.clientItemCollector.save(output, this.itemInfoPathProvider));
    }
}
